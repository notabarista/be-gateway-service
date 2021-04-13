package com.notabarrista.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author codrea.tudor
 *
 */
@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

		http.csrf().disable();
		http.authorizeExchange().pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.pathMatchers("/*", "/static/**", "/assets/**", "/actuator/**", "/admin/**", "/doc-service/**",
						"/discovery/**", "/eureka/**", "/health/**", "/loggers/**").permitAll()
				.pathMatchers(HttpMethod.GET, "/catalog-service/**").permitAll()
				.anyExchange().authenticated().and().oauth2Login().and().oauth2ResourceServer().jwt();
		http.headers().frameOptions().disable();
		http.headers().xssProtection();

		return http.build();
	}
}