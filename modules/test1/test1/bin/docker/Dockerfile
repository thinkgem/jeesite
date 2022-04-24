FROM frolvlad/alpine-java:jdk8-slim
LABEL maintainer="ThinkGem@163.com"
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

ENTRYPOINT java -cp /app $JAVA_OPTS org.springframework.boot.loader.WarLauncher

EXPOSE 8982

#docker run -p 8982:8982 thinkgem/jeesite-cloud-module-test1:5.0
