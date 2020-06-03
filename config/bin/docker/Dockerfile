FROM frolvlad/alpine-java:jdk8-slim
MAINTAINER ThinkGem@163.com
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app
ADD web.war .
RUN jar -xvf web.war
# RUN chmod -R 755 ./*

WORKDIR /app/WEB-INF
# ENV JAVA_OPTS "-Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.profiles.active=native"

ADD application-native.yml ./classes/config

ENTRYPOINT java -cp /app $JAVA_OPTS org.springframework.boot.loader.WarLauncher

EXPOSE 8971

# docker run -p 8971:8971 com.jeesite/jeesite-cloud-config:4.1
