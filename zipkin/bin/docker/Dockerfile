FROM frolvlad/alpine-java:jdk8-slim
MAINTAINER ThinkGem@163.com
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app
ADD ./maven/web.jar ./app.jar
ADD ./app.yml ./app.yml

#ENV JAVA_OPTS "-Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.config.location=/app/app.yml"

ENTRYPOINT java $JAVA_OPTS -jar app.jar

EXPOSE 9411

#docker run -p 9411:9411 com.jeesite/jeesite-cloud-zipkin:4.2
