package br.com.tech.challenger.api_restaurante.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.tech.challenger.api_restaurante.infrastructure.persistence.jpa.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {

}

