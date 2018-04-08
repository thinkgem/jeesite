#!/bin/sh

cd ..

MAVEN_OPTS=$MAVEN_OPTS -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

exec mvn clean spring-boot:run -U
