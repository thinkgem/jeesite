#!/bin/sh

echo ""
echo "[信息] 使用 Spring Boot Tomcat 运行 Web 工程。"
echo ""

cd ..
MAVEN_OPTS="$MAVEN_OPTS -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
exec mvn clean spring-boot:run -U
