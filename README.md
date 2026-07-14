# Tech Challenger - Grupo80

Backend API desenvolvida para o Tech Challenger FIAP - Fase 2 - Grupo 80.

O projeto expõe uma API REST para gestão de restaurantes: cadastro de restaurantes e seus itens de cardápio, gestão de usuários e tipos de usuário, e autenticação/autorização via JWT. Qualquer pessoa pode se auto-registrar como cliente (`CUSTOMER`) através de `POST /v1/auth/register`; os demais endpoints exigem um token de acesso válido.

## Tech Stack

- Java 21
- Spring Boot 4.0.4
- Spring Security (JWT stateless, via `com.auth0:java-jwt`)
- Spring Data JPA + Flyway (migrations)
- Maven
- PostgreSQL
- springdoc-openapi (Swagger UI)
- Testcontainers (testes de integração)
- Docker / Docker Compose

---

## Arquitetura

O projeto segue uma arquitetura em camadas inspirada em Clean/Hexagonal Architecture, com o domínio isolado de frameworks. Pacote base: `br.com.tech.challenger.api_restaurante`.

| Camada / Pacote | Responsabilidade |
|---|---|
| `domain.entity` | Entidades de domínio (`User`, `Restaurant`, `MenuItem`, `UserType`, etc.), sem dependência de frameworks |
| `domain.enums` | Enumerações de domínio (ex.: `UserTypeEnum`) |
| `domain.repository` | Interfaces de repositório (portas) definidas pelo domínio |
| `application.usecase.{auth,user,usertype,restaurant,menuitem}` | Casos de uso — um por operação de negócio, agrupados por contexto |
| `application.dto` | DTOs de request/response que cruzam a borda da API |
| `application.mapper` | Conversão entre entidades de domínio e DTOs |
| `application.exception` | Exceções de aplicação (ex.: `EmailAlreadyExistsException`, `UserTypeNotFoundException`) |
| `infrastructure.presentation.controller` | Controllers REST (adaptadores de entrada) |
| `infrastructure.persistence.jpa.{entity,repository}` | Entidades JPA e repositórios Spring Data |
| `infrastructure.persistence.gateway` | Adaptadores que implementam as portas de `domain.repository` via JPA |
| `infrastructure.persistence.mapper` | Conversão entre entidades JPA e entidades de domínio |
| `infrastructure.security` | Filtro JWT, serviço de geração/validação de token, `UserDetailsService` |
| `infrastructure.config` | Configuração do Spring Security (`SecurityConfig`) e do OpenAPI/Swagger (`OpenApiConfig`) |

**Fluxo de uma requisição:** Controller → UseCase → (Mapper) → Repository (porta de domínio) → Gateway (adaptador JPA) → banco de dados. As entidades de domínio nunca dependem de JPA ou de DTOs — a conversão acontece sempre nas bordas.

### Modelo de dados

Migrations Flyway em `src/main/resources/db/migration`:

| Tabela | Propósito |
|---|---|
| `user_types` | Tipos/papéis de usuário (`CUSTOMER`, `RESTAURANT_OWNER`, `ADMIN`) |
| `user_address` | Endereço vinculável a um usuário |
| `users` | Usuários da aplicação (credenciais, perfil, tipo e endereço vinculados) |
| `restaurants` | Restaurantes cadastrados |
| `menu_items` | Itens de cardápio de um restaurante |

Uma migration adicional popula `user_types` com os valores padrão (`V6`).

---

## Autenticação e Segurança

Autenticação stateless via JWT (`infrastructure.security`):

- Login (`POST /v1/auth/login`) e registro (`POST /v1/auth/register`) retornam um par de tokens (access + refresh).
- Requisições autenticadas enviam `Authorization: Bearer <accessToken>`.
- `POST /v1/auth/refresh-token` troca um refresh token válido por um novo par de tokens.
- Senhas são armazenadas com hash BCrypt; CSRF é desabilitado (API stateless).
- Novas contas criadas via `/v1/auth/register` são sempre do tipo `CUSTOMER` — o cliente não pode escolher o tipo de usuário na requisição (evita escalonamento de privilégio via auto-registro). Contas `ADMIN`/`RESTAURANT_OWNER` não podem ser criadas pela API pública hoje.
- Endpoints públicos (sem token): `/v1/auth/**`, Swagger (`/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`), `/error`.
- Todos os demais endpoints exigem um token de acesso válido (não há restrição por papel/role — qualquer usuário autenticado pode acessar qualquer endpoint protegido).

---

## Endpoints da API

Prefixo base: `/v1`. Documentação interativa (Swagger UI) disponível em `/swagger-ui.html` com a aplicação em execução; especificação OpenAPI crua em `/v3/api-docs`.

### Auth (`/v1/auth`) — públicos

| Método | Path | Descrição |
|---|---|---|
| POST | `/v1/auth/register` | Auto-registro público; cria um usuário `CUSTOMER` e já retorna tokens (201) |
| POST | `/v1/auth/login` | Autentica com login/senha e retorna par de tokens |
| POST | `/v1/auth/refresh-token` | Troca um refresh token válido por um novo par de tokens |

### Users (`/v1/users`) — autenticados

| Método | Path | Descrição |
|---|---|---|
| GET | `/v1/users` | Lista todos os usuários |
| GET | `/v1/users/{id}` | Busca usuário por ID |
| GET | `/v1/users/search/by-name?name=` | Busca usuários por nome |
| GET | `/v1/users/search/by-email?email=` | Busca usuário por e-mail |
| GET | `/v1/users/search/by-login?login=` | Busca usuário por login |
| PUT | `/v1/users/{id}` | Atualiza um usuário |
| DELETE | `/v1/users/{id}` | Remove um usuário |

> Não há endpoint autenticado para criação de usuário — a única forma de criar uma conta é via `POST /v1/auth/register`.

### User Types (`/v1/user-types`) — autenticados

| Método | Path | Descrição |
|---|---|---|
| POST | `/v1/user-types` | Cria um tipo de usuário |
| GET | `/v1/user-types` | Lista todos os tipos de usuário |
| GET | `/v1/user-types/{id}` | Busca tipo de usuário por ID |
| PUT | `/v1/user-types/{id}` | Atualiza um tipo de usuário |
| DELETE | `/v1/user-types/{id}` | Remove um tipo de usuário |

### Restaurants (`/v1/restaurants`) — autenticados

| Método | Path | Descrição |
|---|---|---|
| POST | `/v1/restaurants` | Cria um restaurante |
| GET | `/v1/restaurants` | Lista todos os restaurantes |
| GET | `/v1/restaurants/{id}` | Busca restaurante por ID |
| PUT | `/v1/restaurants/{id}` | Atualiza um restaurante |
| DELETE | `/v1/restaurants/{id}` | Remove um restaurante |

### Menu Items (`/v1/menu-items`) — autenticados

| Método | Path | Descrição |
|---|---|---|
| POST | `/v1/menu-items` | Cria um item de cardápio |
| GET | `/v1/menu-items?restaurantId=` | Lista itens de cardápio, opcionalmente filtrando por restaurante |
| GET | `/v1/menu-items/{id}` | Busca item de cardápio por ID |
| PUT | `/v1/menu-items/{id}` | Atualiza um item de cardápio |
| DELETE | `/v1/menu-items/{id}` | Remove um item de cardápio |

Uma collection Postman pronta para uso está disponível em [`Restaurant API Collection.postman_collection.json`](./Restaurant%20API%20Collection.postman_collection.json), incluindo scripts que capturam automaticamente os tokens após login/registro.

---

## Environment Files

| File | Purpose |
|------|---------|
| `.env` | Local development (app runs on host, DB runs in Docker) |
| `.env.prod` | Full Docker deployment (app + DB both in containers) |

> Never commit these files. Add them to `.gitignore` if not already there.

---

## Running Locally (app on host, DB in Docker)

Use this mode during active development for faster iteration.

**Prerequisites:** Docker, Java 21, Maven

**1. Start only the database:**

```bash
docker compose up -d postgres
```

**2. Run the application:**

```bash
./mvnw spring-boot:run
```

The app reads `application.properties`, which uses `${VAR:default}` placeholders. It connects to `localhost:5432` by default — no need to export `.env` manually, but you can if you want to override any value:

```bash
export $(grep -v '^#' .env | xargs) && ./mvnw spring-boot:run
```

**3. Access the API:**

```
http://localhost:8080
```

---

## Running with Docker Compose (full Docker)

Use this mode to test the production-like setup locally or to deploy.

**Prerequisites:** Docker, Docker Compose v2+

**1. Build and start all services:**

```bash
docker compose -f docker-compose.prod.yml --env-file .env.prod up --build
```

To run in detached mode:

```bash
docker compose -f docker-compose.prod.yml --env-file .env.prod up --build -d
```

**2. Access the API:**

```
http://127.0.0.1:8080
```

---

## Spring Profiles

| Profile | Config file loaded | Activated by |
|---------|-------------------|--------------|
| default | `application.properties` | No profile set (local dev) |
| docker | `application.properties` + `application-docker.properties` | `SPRING_PROFILES_ACTIVE=docker` in `.env.prod` |

---

## Tests

The project has two kinds of tests, run by different Maven plugins:

| Type | Naming | Plugin | Command | Needs Docker? |
|------|--------|--------|---------|----------------|
| Unit tests | `*Test.java` | Surefire | `./mvnw test` | No |
| Integration tests | `*IT.java` | Failsafe | `./mvnw verify` | Yes |

**Unit tests** (`*Test.java`) use Mockito and don't touch a real database. They run in the `test` phase.

**Integration tests** (`*IT.java`) spin up a real PostgreSQL container via [Testcontainers](https://testcontainers.com/), run the actual Flyway migrations against it, and exercise the JPA repositories. They run in the `integration-test`/`verify` phases, after the unit tests, so a failing unit test fails fast without needing Docker at all.

**Prerequisites for integration tests:** Docker running locally.

**Run everything (unit + integration):**

```bash
./mvnw verify
```

**Run only unit tests (fast, no Docker required):**

```bash
./mvnw test
```

**Adding a new integration test:** extend `AbstractIntegrationTest` (`src/test/java/.../support/AbstractIntegrationTest.java`), annotate the class with `@DataJpaTest` + `@AutoConfigureTestDatabase(replace = Replace.NONE)`, and name it `<Something>IT`. The Postgres container is started once and reused across all integration test classes in the same run.

---

## Troubleshooting

**View logs:**

```bash
# local DB container
docker compose logs -f postgres

# full Docker setup
docker compose -f docker-compose.prod.yml logs -f
```

**Force rebuild (ignore cache):**

```bash
docker compose -f docker-compose.prod.yml --env-file .env.prod build --no-cache
```

**Stop and remove containers:**

```bash
docker compose down
docker compose -f docker-compose.prod.yml down
```

**Reset the database (removes all data):**

```bash
docker compose down -v
```
