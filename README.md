# Tech Challenger - Grupo80

Backend API desenvolvida para o Tech Challenger FIAP - Fase 2 - Grupo 80.

## Tech Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Docker / Docker Compose

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
