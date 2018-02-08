@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 运行Web工程。
echo.
rem pause
rem echo.

%~d0
cd %~dp0

cd ../
title %cd%

rem set JAVA_OPTS= -Xms256m -Xmx1024m -XX:PermSize=128m -XX:MaxPermSize=512m
set JAVA_OPTS= -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

cd target/
java -jar jeesite-web.war

pause