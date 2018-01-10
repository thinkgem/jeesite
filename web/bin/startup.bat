@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 运行 Web 工程。
echo.
rem pause
rem echo.

%~d0
cd %~dp0

cd ../
title %cd%

set JAVA_OPTS= -Xms256m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m

java -jar target/jeesite-web-4.0-SNAPSHOT.war

pause