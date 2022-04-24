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

ENTRYPOINT java $JAVA_OPTS -jar app.jar

EXPOSE 9411

#docker run -p 9411:9411 thinkgem/jeesite-cloud-zipkin:5.0
