FROM docker.m.daocloud.io/eclipse-temurin:17
LABEL maintainer="ThinkGem@163.com"
ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
WORKDIR /app

COPY ./maven/web.war ./

ENV JAVA_OPTS "-Dspring.profiles.active=prod"
ENV JAVA_OPTS "$JAVA_OPTS -DcustomLogPath=/data"

VOLUME /tmp /data

ENTRYPOINT ["bash", "-c", "if [ -f 'web.war' ]; then jar -xvf web.war && \
                           rm web.war; fi && cd WEB-INF && sh startup.sh"]

EXPOSE 9411

#docker run -p 9411:9411 thinkgem/jeesite-cloud-zipkin:latest
