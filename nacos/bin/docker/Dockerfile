FROM docker.m.daocloud.io/openjdk:17
LABEL maintainer="ThinkGem@163.com"
ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
WORKDIR /app

COPY ./maven/web.war ./

ENV JAVA_OPTS "-Dspring.profiles.active=prod"
ENV JAVA_OPTS "$JAVA_OPTS -DcustomLogPath=/data"
ENV JAVA_OPTS "$JAVA_OPTS -Dnacos.home=/app"

VOLUME /tmp /data

ENTRYPOINT ["bash", "-c", "if [ -f 'web.war' ]; then jar -xvf web.war && \
                           rm web.war; fi && cd WEB-INF && sh startup.sh"]

EXPOSE 8848 9848 8849

#docker run -p 8848:8848 -p 9848:9848 -p 8849:8849 thinkgem/jeesite-cloud-nacos:latest