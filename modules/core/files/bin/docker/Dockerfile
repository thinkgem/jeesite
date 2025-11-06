FROM docker.m.daocloud.io/eclipse-temurin:17
LABEL maintainer="ThinkGem@163.com"

ENV TZ=Asia/Shanghai LANG=C.UTF-8 JAVA_OPTS="\
-Dspring.profiles.active=prod \
-DcustomLogPath=/data"

WORKDIR /app

#COPY jeesite.lic WEB-INF/
COPY ./maven/web.war ./

VOLUME /tmp /data

EXPOSE 7999

ENTRYPOINT ["sh", "-c","\
if [ -f 'web.war' ]; then jar -xvf web.war && rm web.war; fi && \
cd WEB-INF && exec sh startup.sh\
"]
