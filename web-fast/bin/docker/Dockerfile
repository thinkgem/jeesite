FROM docker.m.daocloud.io/openjdk:8
LABEL maintainer="ThinkGem@163.com"
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp
VOLUME /data

WORKDIR /app

#ADD jeesite.lic ./
ADD ./maven/web.jar ./app.jar

#ENV JAVA_OPTS "$JAVA_OPTS -Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.profiles.active=prod"

ENTRYPOINT java -jar $JAVA_OPTS app.jar

EXPOSE 8980
