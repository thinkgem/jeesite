@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包Web工程，并运行Web工程。
echo.
rem pause
rem echo.

%~d0
cd %~dp0

cd ../
call mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U

title %cd%

rem set JAVA_OPTS= -Xms256m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=512m
set JAVA_OPTS= -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

cd target/
call unzip -n jeesite-web.war -d jeesite-web

cd jeesite-web
call java org.springframework.boot.loader.WarLauncher

pause