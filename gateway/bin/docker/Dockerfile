FROM frolvlad/alpine-java:jdk8-slim
MAINTAINER ThinkGem@163.com
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app
ADD ./maven/web.war ./app.war

RUN jar -xvf app.war
#RUN chmod -R 755 ./*

WORKDIR /app/WEB-INF
ADD ./app.yml ./classes/config/bootstrap-prod.yml
#ADD jeesite.lic ./

#ENV JAVA_OPTS "-Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.profiles.active=prod"

ENTRYPOINT java $JAVA_OPTS -cp /app $JAVA_OPTS org.springframework.boot.loader.WarLauncher

EXPOSE 8980

#docker run -p 8980:8980 com.jeesite/jeesite-cloud-module-gateway:4.2
