package com.notabarrista.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

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
@Log4j2
public class SCGPreFilter extends AbstractGatewayFilterFactory<SCGPreFilter.Config> {

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
                DecodedJWT jwt = JWT.decode(jwtString);
                String uid = jwt.getClaims().get("uid").toString();
                log.debug("Parsed JWT token claims: " + jwt.getClaims());
                uid = uid.replace("\"", "");
                request = exchange.getRequest().mutate()
                        .header("uid", uid)
                        .build();
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
