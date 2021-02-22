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
MAVEN_OPTS="$MAVEN_OPTS -Xms512m -Xmx1024m -Dfile.encoding=utf-8"
mvn clean spring-boot:run -Dmaven.test.skip=true