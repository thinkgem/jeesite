FROM docker.1ms.run/eclipse-temurin:21.0.10_7-jdk
LABEL maintainer="ThinkGem@163.com"

ENV TZ=Asia/Shanghai LANG=C.UTF-8 JAVA_OPTS="\
-Dspring.profiles.active=prod \
-DcustomLogPath=/data"

WORKDIR /app

COPY ./maven/web.war ./

VOLUME /tmp /data

EXPOSE 8970

ENTRYPOINT ["sh", "-c","\
if [ -f 'web.war' ]; then jar -xvf web.war && rm web.war; fi && \
cd WEB-INF && exec sh startup.sh\
"]
