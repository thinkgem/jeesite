@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 使用 Spring Boot Tomcat 运行 Web 工程。
echo.

%~d0
cd %~dp0

if defined JAVA_HOME8 (
  set "JAVA_HOME=%JAVA_HOME8%" & set "PATH=%JAVA_HOME8%\bin;%PATH%"
)
call mvn -v
echo.

cd ..
title %cd%
set "MAVEN_OPTS=%MAVEN_OPTS% -Xms512m -Xmx1024m"
call mvn clean spring-boot:run -Dmaven.test.skip=true

pause