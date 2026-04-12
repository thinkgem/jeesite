FROM docker.1ms.run/eclipse-temurin:17.0.18_8-jdk
LABEL maintainer="ThinkGem@163.com"

ENV TZ=Asia/Shanghai LANG=C.UTF-8 JAVA_OPTS="\
-Dspring.profiles.active=prod \
-DcustomLogPath=/data"

WORKDIR /app

#COPY jeesite.lic WEB-INF/
COPY ./maven/web.jar ./

VOLUME /tmp /data

EXPOSE 8980

ENTRYPOINT ["bash", "-c", "java -jar $JAVA_OPTS web.jar"]
