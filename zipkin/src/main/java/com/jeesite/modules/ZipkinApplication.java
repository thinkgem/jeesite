package com.jeesite.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * ZipkinApplication
 * @author 长春叭哥
 * @version 2019-06-28
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class ZipkinApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
    
}