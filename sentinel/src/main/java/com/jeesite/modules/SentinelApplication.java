/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeesite.modules;

import com.alibaba.csp.sentinel.init.InitExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Sentinel dashboard application.
 *
 * @author Carpenter Lee
 */
@SpringBootApplication(scanBasePackages = "com.alibaba.csp.sentinel.dashboard")
@EnableDiscoveryClient
public class SentinelApplication {

    private static final Logger logger = LoggerFactory.getLogger(SentinelApplication.class);

    public static void main(String[] args) {
        triggerSentinelInit();
        SpringApplication.run(SentinelApplication.class, args);
        logger.info(
				"\r\n\r\n==============================================================\r\n"
				+ "\r\n   " + SentinelApplication.class.getName() + " 启动完成。"
				+ "\r\n\r\n==============================================================\r\n");
    }

    private static void triggerSentinelInit() {
        new Thread(() -> InitExecutor.doInit()).start();
    }
    
}
