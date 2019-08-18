package com.jeesite.modules;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * AdminApplication
 * @author 长春叭哥
 * @version 2019-06-28
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
public class AdminApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
    
}