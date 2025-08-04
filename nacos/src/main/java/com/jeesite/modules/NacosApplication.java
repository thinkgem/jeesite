/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.StartingApplicationListener;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.mcpregistry.NacosMcpRegistry;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * Nacos Application
 * @author ThinkGem
 */
@SpringBootApplication
public class NacosApplication {

	private static final Logger logger = LoggerFactory.getLogger(StartingApplicationListener.class);
    private static final String SPRING_JXM_ENABLED = "spring.jmx.enabled";

	private static void initialize() {
		if (StringUtils.isBlank(System.getProperty("nacos.dev"))) {
			System.setProperty("nacos.standalone", "true");
		}
		if (StringUtils.isBlank(System.getProperty("nacos.home"))) {
			System.setProperty("nacos.home", System.getProperty("user.home") + "/nacos6boot3");
		}
		System.setProperty("nacos.logs.path", System.getProperty("nacos.home") + "/logs");
		System.setProperty("derby.stream.error.file", System.getProperty("nacos.home") + "/.derby.log");
		System.setProperty("logging.config", "classpath:nacos-logback.xml");
	}

	public static void main(String[] args) {
		NacosApplication.initialize();
        String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
        DeploymentType deploymentType = DeploymentType.getType(type);
        EnvUtil.setDeploymentType(deploymentType);
        switch (deploymentType) {
            case MERGED:
                startWithConsole(args);
                break;
            case SERVER:
                startWithoutConsole(args);
                break;
            case CONSOLE:
                startOnlyConsole(args);
                break;
            default:
                throw new IllegalArgumentException("Unsupported nacos deployment type " + type);
        }
		logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + NacosApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
	}

    private static void prepareCoreContext(ConfigurableApplicationContext coreContext) {
        if (coreContext.getEnvironment().getProperty(SPRING_JXM_ENABLED, Boolean.class, false)) {
            // Avoid duplicate registration MBean to exporter.
            coreContext.getBean(MBeanExporter.class).setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        }
    }

    private static void startWithoutConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext webContext = startServerWebContext(args, coreContext);
        if (isEnabledMcpRegistryApi(coreContext)) {
            ConfigurableApplicationContext mcpRegistryContext = startMcpRegistryContext(args, coreContext);
        }
    }

    private static void startWithConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext serverWebContext = startServerWebContext(args, coreContext);
        ConfigurableApplicationContext consoleContext = startConsoleContext(args, coreContext);
        if (isEnabledMcpRegistryApi(coreContext)) {
            ConfigurableApplicationContext mcpRegistryContext = startMcpRegistryContext(args, coreContext);
        }
    }

    private static ConfigurableApplicationContext startCoreContext(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosServerBasicApplication.class).web(WebApplicationType.NONE)
                .banner(getBanner("core-banner.txt")).run(args);
    }

    private static ConfigurableApplicationContext startServerWebContext(String[] args,
            ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosServerWebApplication.class).parent(coreContext)
                .banner(getBanner("nacos-server-web-banner.txt")).run(args);
    }

    private static ConfigurableApplicationContext startConsoleContext(String[] args,
            ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosConsole.class).parent(coreContext)
                .banner(getBanner("nacos-console-banner.txt")).run(args);
    }

    private static ConfigurableApplicationContext startMcpRegistryContext(String[] args,
                                                                          ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.MCP_REGISTRY_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosMcpRegistry.class).parent(coreContext)
                .banner(getBanner("nacos-mcp-registry-banner.txt")).run(args);
    }

    private static void startOnlyConsole(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
        ConfigurableApplicationContext consoleContext = new SpringApplicationBuilder(NacosConsole.class).banner(
                getBanner("nacos-console-banner.txt")).run(args);
    }

    private static Banner getBanner(String bannerFileName) {
        return new ResourceBanner(new ClassPathResource(bannerFileName));
    }

    private static boolean isEnabledMcpRegistryApi(ConfigurableApplicationContext coreContext) {
        return coreContext.getEnvironment().getProperty("nacos.ai.mcp.registry.enabled", Boolean.class, false);
    }

}