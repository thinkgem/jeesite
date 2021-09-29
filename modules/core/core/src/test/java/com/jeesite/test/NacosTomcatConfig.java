package com.jeesite.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;

@Configuration
public class NacosTomcatConfig implements ApplicationRunner {

    @Autowired(required = false)
    private NacosAutoServiceRegistration registration;

    @Value("${server.port}")
    private Integer port;

	@Override
	@SuppressWarnings("deprecation")
    public void run(ApplicationArguments args) throws Exception {
        if (registration != null && port != null) {
            registration.setPort(port);
            registration.start();
        }
    }

}
