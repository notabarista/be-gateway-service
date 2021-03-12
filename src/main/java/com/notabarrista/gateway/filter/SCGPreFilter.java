package com.notabarrista.gateway.filter;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Add '- name: SCGPreFilter' to the gateway routes config for this to be used
 * Also check that the gateway has the default filter
 * spring:
 *   cloud:
 *     gateway:
 *       default-filters:
 *         - TokenRelay
 */
@Component
public class SCGPreFilter extends AbstractGatewayFilterFactory<SCGPreFilter.Config> {

    @Value( "${okta.oauth2.issuer}" )
    private String jwtIssuer;

    @Value( "${okta.oauth2.audience}" )
    private String jwtAudience;

    public SCGPreFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest().mutate().build();
            //adds header to proxied request
            HttpHeaders headers = exchange.getRequest().getHeaders();
            if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                String jwtString = headers.getFirst(HttpHeaders.AUTHORIZATION);
                jwtString = jwtString.replace("Bearer ", "");
                AccessTokenVerifier jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                                                              .setIssuer(jwtIssuer)
                                                              .setAudience(jwtAudience)                   // defaults to 'api://default'
                                                              .setConnectionTimeout(Duration.ofSeconds(1))    // defaults to 1s
                                                              .setRetryMaxAttempts(2)                     // defaults to 2
                                                              .setRetryMaxElapsed(Duration.ofSeconds(10)) // defaults to 10s
                                                              .build();
                try {
                    Jwt jwt = jwtVerifier.decode(jwtString);
                    request = exchange.getRequest().mutate()
                                      .header("uid", jwt.getClaims().get("uid").toString())
                                      .header("email", jwt.getClaims().get("email").toString())
                                      .header("claims", jwt.getClaims().toString())
                                      .build();
                } catch (JwtVerificationException e) {
                    e.printStackTrace();
                }
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
