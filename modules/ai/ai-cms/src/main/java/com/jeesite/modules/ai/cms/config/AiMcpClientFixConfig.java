/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules.ai.cms.config;

import io.modelcontextprotocol.spec.McpSchema;
import org.springaicommunity.mcp.annotation.McpElicitation;
import org.springaicommunity.mcp.annotation.McpLogging;
import org.springaicommunity.mcp.annotation.McpSampling;
import org.springframework.ai.mcp.annotation.spring.ClientMcpSyncHandlersRegistry;
import org.springframework.ai.mcp.client.common.autoconfigure.annotations.McpClientAnnotationScannerProperties;
import org.springframework.ai.mcp.client.common.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClientMcp*HandlersRegistry: support beans with unresolvable types #4918
 * (To fix the issues in Spring AI 1.1.0, they need to be removed in 1.1.1.)
 * @author ThinkGem
 */
@Configuration
@ConditionalOnClass(McpLogging.class)
@ConditionalOnProperty(prefix = McpClientAnnotationScannerProperties.CONFIG_PREFIX, name = "enabled",
		havingValue = "true", matchIfMissing = true)
public class AiMcpClientFixConfig {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = McpClientCommonProperties.CONFIG_PREFIX, name = "type", havingValue = "SYNC",
			matchIfMissing = true)
	public ClientMcpSyncHandlersRegistry clientMcpSyncHandlersRegistry() {
		return new ClientMcpSyncHandlersRegistry() {
			@Override
			public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
				this.beanFactory = beanFactory;
				Map<String, List<String>> elicitationClientToAnnotatedBeans = new HashMap<>();
				Map<String, List<String>> samplingClientToAnnotatedBeans = new HashMap<>();
				for (var beanName : beanFactory.getBeanDefinitionNames()) {
					if (!beanFactory.getBeanDefinition(beanName).isSingleton()) {
						// Only process singleton beans, not scoped beans
						continue;
					}

					// ClientMcp*HandlersRegistry: support beans with unresolvable types #4918
					Class<?> beanClass = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
					if (beanClass == null) {
						// If we cannot determine the bean class, we cannot scan it before
						// it is really resolved. This is very likely an infrastructure-level
						// bean, not a "service" type, skip it entirely.
						continue;
					}
					var foundAnnotations = this.scan(beanClass);
					// #4918 end

					if (!foundAnnotations.isEmpty()) {
						this.allAnnotatedBeans.add(beanName);
					}
					for (var foundAnnotation : foundAnnotations) {
						if (foundAnnotation instanceof McpSampling sampling) {
							for (var client : sampling.clients()) {
								samplingClientToAnnotatedBeans.computeIfAbsent(client, c -> new ArrayList<>()).add(beanName);
							}
						}
						else if (foundAnnotation instanceof McpElicitation elicitation) {
							for (var client : elicitation.clients()) {
								elicitationClientToAnnotatedBeans.computeIfAbsent(client, c -> new ArrayList<>()).add(beanName);
							}
						}
					}
				}

				for (var elicitationEntry : elicitationClientToAnnotatedBeans.entrySet()) {
					if (elicitationEntry.getValue().size() > 1) {
						throw new IllegalArgumentException(
								"Found 2 elicitation handlers for client [%s], found in bean with names %s. Only one @McpElicitation handler is allowed per client"
									.formatted(elicitationEntry.getKey(), new LinkedHashSet<>(elicitationEntry.getValue())));
					}
				}
				for (var samplingEntry : samplingClientToAnnotatedBeans.entrySet()) {
					if (samplingEntry.getValue().size() > 1) {
						throw new IllegalArgumentException(
								"Found 2 sampling handlers for client [%s], found in bean with names %s. Only one @McpSampling handler is allowed per client"
									.formatted(samplingEntry.getKey(), new LinkedHashSet<>(samplingEntry.getValue())));
					}
				}

				Map<String, McpSchema.ClientCapabilities.Builder> capsPerClient = new HashMap<>();
				for (var samplingClient : samplingClientToAnnotatedBeans.keySet()) {
					capsPerClient.computeIfAbsent(samplingClient, ignored -> McpSchema.ClientCapabilities.builder()).sampling();
				}
				for (var elicitationClient : elicitationClientToAnnotatedBeans.keySet()) {
					capsPerClient.computeIfAbsent(elicitationClient, ignored -> McpSchema.ClientCapabilities.builder())
						.elicitation();
				}

				this.capabilitiesPerClient = capsPerClient.entrySet()
					.stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build()));
			}
		};
	}

}
