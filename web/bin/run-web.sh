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

JAVA_OPTS="-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
#JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=$PWD\app.yml"

if [ -z "$JAVA_HOME" ]; then
  RUN_JAVA=java
else
  RUN_JAVA="$JAVA_HOME"/bin/java
fi

exec $RUN_JAVA $JAVA_OPTS -jar web.war