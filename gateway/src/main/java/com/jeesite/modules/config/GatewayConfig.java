/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * Gateway
 * @author ThinkGem
 * @version 2018年10月20日
 */
@Configuration
public class GatewayConfig {
	
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
				return chain.filter(exchange);
			}
		};
	}
	
}
