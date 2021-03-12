package com.notabarrista.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * This filter is needed to add '/index.html' to request with deep links
 * that need to be handled by the frontend code
 * Add '- name: SPAGatewayFilter' to the frontend gateway route
 */
@Component
public class SPAGatewayFilter extends AbstractGatewayFilterFactory<SPAGatewayFilter.Config> {

    public SPAGatewayFilter() {
        super(SPAGatewayFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange,chain) ->{
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpRequest modifiedRequest = request;
            if (request.getHeaders().getAccept().contains(MediaType.TEXT_HTML)) {
                modifiedRequest = exchange.getRequest().mutate().path("/index.html").build();
            }

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
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
