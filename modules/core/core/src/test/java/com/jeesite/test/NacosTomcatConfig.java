package com.jeesite.test;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosTomcatConfig implements ApplicationRunner {

    private final NacosAutoServiceRegistration registration;

    @Value("${server.port}")
    private Integer port;

	public NacosTomcatConfig(NacosAutoServiceRegistration registration) {
		this.registration = registration;
	}

	@Override
	@SuppressWarnings("deprecation")
    public void run(ApplicationArguments args) throws Exception {
        if (registration != null && port != null) {
            registration.setPort(port);
            registration.start();
        }
    }

}
