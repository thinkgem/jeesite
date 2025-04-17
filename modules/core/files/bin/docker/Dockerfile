FROM docker.m.daocloud.io/openjdk:8
LABEL maintainer="ThinkGem@163.com"
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app

#RUN mkdir WEB-INF
#ADD jeesite.lic ./WEB-INF
ADD ./maven/web.war ./app.war

#ENV JAVA_OPTS "$JAVA_OPTS -Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.profiles.active=prod"

ENTRYPOINT if [ -f "app.war" ]; then jar -xvf app.war && rm app.war; \
           fi && cd WEB-INF && sh startup.sh && cd WEB-INF && sh startup.sh

EXPOSE 8990

#docker run -p 8990:8990 thinkgem/jeesite-cloud-module-core:5.10
