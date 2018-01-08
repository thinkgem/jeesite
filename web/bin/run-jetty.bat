@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
title %cd%
echo.
echo [信息] 使用Jetty插件运行Web工程。
echo.
rem pause
rem echo.

cd %~dp0
cd ..
set currPath=%cd%

set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m

if exist "../parent/pom.xml" (
	cd ../parent
	call mvn clean install -Dmaven.test.skip=true
)

if exist "../modules/pom.xml" (
	cd ../modules
	call mvn clean install -Dmaven.test.skip=true
)

cd %currPath%
call mvn jetty:run -D maven.javadoc.skip=true -U

cd bin
pause