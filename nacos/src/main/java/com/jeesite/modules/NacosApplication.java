/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.modules;

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.airegistry.NacosAiRegistry;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.StartingApplicationListener;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
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
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Nacos bootstrap class.
 * @author xiweng.yy, jeesite
 */
@SpringBootApplication
public class NacosApplication {

	private static final Logger logger = LoggerFactory.getLogger(StartingApplicationListener.class);
    private static final String SPRING_JMX_ENABLED = "spring.jmx.enabled";

	private static void initialize() {
		if (StringUtils.isBlank(System.getProperty("nacos.dev"))) {
			System.setProperty("nacos.standalone", "true");
		}
		if (StringUtils.isBlank(System.getProperty("nacos.home"))) {
			System.setProperty("nacos.home", System.getProperty("user.home") + "/nacos6boot4");
		}
		if (StringUtils.isBlank(System.getProperty("nacos.deployment.type"))) {
			System.setProperty("nacos.deployment.type", "merged");
		}
		if (StringUtils.isBlank(System.getProperty("nacos.server.ip"))) {
			System.setProperty("nacos.server.ip", "127.0.0.1");
		}
		System.setProperty("nacos.logs.path", System.getProperty("nacos.home") + "/logs");
		System.setProperty("derby.stream.error.file", System.getProperty("nacos.home") + "/.derby.log");
		System.setProperty("logging.config", "classpath:nacos-logback.xml");
		
	}
	
	private static boolean checkAddOpens() {
		List<String> inputArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
		String targetParam1 = "--add-opens=java.base/java.util=ALL-UNNAMED";
		String targetParam2 = "--add-opens java.base/java.util=ALL-UNNAMED"; // 空格形式
		for (String arg : inputArgs) {
			if (arg.equals(targetParam1) || arg.equals(targetParam2)) {
				return true;
			}
		}
		return false;
	}

    public static void main(String[] args) {
		NacosApplication.initialize();
		if (!NacosApplication.checkAddOpens()) {
			logger.error(
					"\n\n==============================================================\n\n" +
					"  ⚠️ 警告：未检测到 JVM 必要的参数 add-opens，参考如下：\n\n" +
					"  • 命令行中添加：java --add-opens=java.base/java.util=ALL-UNNAMED -jar app.jar\n" +
					"  • 或在 IDE “虚拟机选项” 中添加：--add-opens=java.base/java.util=ALL-UNNAMED\n" +
					"\n==============================================================\n"
			);
			return;
		}
		ConfigurableApplicationContext context;
        String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
        DeploymentType deploymentType = DeploymentType.getType(type);
        EnvUtil.setDeploymentType(deploymentType);
        switch (deploymentType) {
            case MERGED:
                context = startWithConsole(args);
                break;
            case SERVER:
                context = startWithoutConsole(args);
                break;
            case CONSOLE:
                context = startOnlyConsole(args);
                break;
            default:
                throw new IllegalArgumentException("Unsupported nacos deployment type " + type);
        }
		Environment env = context.getEnvironment();
		logger.info(
				"\n\n==============================================================\n"
				+ "\n   启动完成，访问地址：http://127.0.0.1:{}/#/login\n"
				+ "\n==============================================================\n",
				env.getProperty("local.server.port"));
    }

    private static void prepareCoreContext(ConfigurableApplicationContext coreContext) {
        if (coreContext.getEnvironment().getProperty(SPRING_JMX_ENABLED, Boolean.class, false)) {
            // Avoid duplicate registration MBean to exporter.
            coreContext.getBean(MBeanExporter.class).setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        }
    }
    
    private static ConfigurableApplicationContext startWithoutConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext webContext = startServerWebContext(args, coreContext);
        if (isEnabledAiRegistry(coreContext)) {
            ConfigurableApplicationContext aiRegistryContext =
                startAiRegistryContext(args, coreContext);
        }
		return webContext;
    }
    
    private static ConfigurableApplicationContext startWithConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext serverWebContext = startServerWebContext(args, coreContext);
        ConfigurableApplicationContext consoleContext = startConsoleContext(args, coreContext);
        if (isEnabledAiRegistry(coreContext)) {
            ConfigurableApplicationContext aiRegistryContext =
                startAiRegistryContext(args, coreContext);
        }
		return consoleContext;
    }
    
    private static ConfigurableApplicationContext startCoreContext(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosServerBasicApplication.class)
            .web(WebApplicationType.NONE)
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
    
    private static ConfigurableApplicationContext startAiRegistryContext(String[] args,
        ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.AI_REGISTRY_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosAiRegistry.class).parent(coreContext)
            .banner(getBanner("nacos-ai-registry-banner.txt")).run(args);
    }
    
    private static ConfigurableApplicationContext startOnlyConsole(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosConsole.class).banner(
            getBanner("nacos-console-banner.txt")).run(args);
    }
    
    private static Banner getBanner(String bannerFileName) {
        return new ResourceBanner(new ClassPathResource(bannerFileName));
    }
    
    private static boolean isEnabledAiRegistry(ConfigurableApplicationContext coreContext) {
        boolean mcpRegistryEnabled = coreContext.getEnvironment()
            .getProperty("nacos.ai.mcp.registry.enabled", Boolean.class, false);
        boolean skillRegistryEnabled = coreContext.getEnvironment()
            .getProperty("nacos.ai.skill.registry.enabled", Boolean.class, false);
        return mcpRegistryEnabled || skillRegistryEnabled;
    }
}
