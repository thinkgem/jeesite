#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 使用 Spring Boot Tomcat 运行 Web 工程。"
echo ""

cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U

cd target
unzip -n *.war -d web

cd web/WEB-INF
chmod +x *.sh
./startup.sh
