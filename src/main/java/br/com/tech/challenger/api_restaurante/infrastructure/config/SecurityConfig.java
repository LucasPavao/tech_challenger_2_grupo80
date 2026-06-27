package br.com.tech.challenger.api_restaurante.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Endpoints do Swagger/OpenAPI liberados sem autenticação
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        // Allow public endpoints for users
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").permitAll()
                        // Allow public endpoints for user types (controllers under /v1/user-types)
                        .requestMatchers(HttpMethod.GET, "/v1/user-types/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user-types").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user-types").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/user-types/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/user-types/**").permitAll()
                        // Allow public endpoints for user types (controllers under /v1/restaurant)
                        .requestMatchers(HttpMethod.GET, "/v1/restaurants/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/restaurants").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/restaurants").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/restaurants/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/v1/restaurants/**").permitAll()
                        // TODO: adicionar endpoint de autenticação (ex: POST /auth/login)
                        // .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated()
                );

        // TODO: adicionar filtro JWT antes do UsernamePasswordAuthenticationFilter
        // http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
