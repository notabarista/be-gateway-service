package com.notabarrista.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.security.Principal;

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

    public SCGPreFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
                                            .map(Principal::getName)
                                            .map(userId -> {
                                                //adds header to proxied request
                                                exchange.getRequest().mutate().header("uid", userId).build();
                                                return exchange;
                                            })
                                            .flatMap(chain::filter);
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
