@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 使用 Spring Boot Tomcat 运行 Web 工程。
echo.
rem pause
rem echo.

%~d0
cd %~dp0

cd ../
title %cd%

set currPath=%cd%

set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m

if exist "../package/pom.xml" (
	cd ../package
	call mvn clean install -Dmaven.test.skip=true -Ppackage -U
)

cd %currPath%

call mvn clean spring-boot:run -U

pause