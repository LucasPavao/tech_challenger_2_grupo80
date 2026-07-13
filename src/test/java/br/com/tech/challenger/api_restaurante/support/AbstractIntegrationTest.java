package br.com.tech.challenger.api_restaurante.support;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base para testes de integração que precisam de um Postgres real.
 * O container é iniciado manualmente uma única vez (bloco estático) e
 * reaproveitado por todas as classes de teste que estendem esta base;
 * o Ryuk do Testcontainers derruba o container quando a JVM termina.
 * Não usar @Container/@Testcontainers aqui: essas anotações fazem o
 * JUnit parar o container ao final de cada classe, quebrando o singleton.
 */
public abstract class AbstractIntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgres.start();
    }
}
