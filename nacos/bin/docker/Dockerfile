FROM openjdk:8-slim
LABEL maintainer="ThinkGem@163.com"
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app

ADD ./app.yml ./app.yml
ADD ./maven/web.jar ./app.jar

#ENV JAVA_OPTS "$JAVA_OPTS -Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.config.location=/app/app.yml"
ENV JAVA_OPTS "$JAVA_OPTS -Dnacos.home=/app"

ENTRYPOINT java $JAVA_OPTS -jar app.jar

EXPOSE 8848

#docker run -p 8848:8848 thinkgem/jeesite-cloud-nacos:5.0
