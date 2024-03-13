/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.config;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 网关跨域过滤器
 * @author ThinkGem
 */
@Component
public class GatewayCorsFilter implements WebFilter, Ordered {


    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String origin = request.getHeaders().getFirst(HttpHeaders.ORIGIN);
        if (origin != null) {
            HttpHeaders respHeaders = response.getHeaders();
            respHeaders.add("Access-Control-Allow-Origin", origin);
            respHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            respHeaders.add("Access-Control-Allow-Headers", "content-type, x-requested-with, x-ajax, x-token, x-remember");
            respHeaders.add("Access-Control-Expose-Headers", "x-token, x-remember");
        }
        if (HttpMethod.OPTIONS.equals(request.getMethod())){
            exchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }

}