FROM openjdk:17-slim
LABEL maintainer="ThinkGem@163.com"
ENV TZ "Asia/Shanghai"
ENV LANG C.UTF-8
VOLUME /tmp

WORKDIR /app

#RUN mkdir WEB-INF
#ADD jeesite.lic ./WEB-INF
ADD ./maven/web.war ./app.war

#ENV JAVA_OPTS "$JAVA_OPTS -Xms256m -Xmx1024m"
ENV JAVA_OPTS "$JAVA_OPTS -Dspring.profiles.active=git"

ENTRYPOINT jar -xvf app.war && rm app.war && cd WEB-INF && sh startup.sh

EXPOSE 8971

#docker run -p 8971:8971 thinkgem/jeesite-cloud-module-config:5.3
