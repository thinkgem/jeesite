FROM docker.m.daocloud.io/eclipse-temurin:17
LABEL maintainer="ThinkGem@163.com"
ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
WORKDIR /app

#RUN mkdir -p WEB-INF
#COPY jeesite.lic WEB-INF/
COPY ./maven/web.war ./

ENV JAVA_OPTS "-Dspring.profiles.active=prod"
ENV JAVA_OPTS "$JAVA_OPTS -DcustomLogPath=/data"

VOLUME /tmp /data

ENTRYPOINT ["bash", "-c", "if [ -f 'web.war' ]; then jar -xvf web.war && \
                           rm web.war; fi && cd WEB-INF && sh startup.sh"]

EXPOSE 8980

#docker run -p 8980:8980 thinkgem/jeesite-web:latest
