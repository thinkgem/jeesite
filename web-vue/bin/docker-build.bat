@echo off
rem /**
rem  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包Web工程，编译Docker镜像
echo.

%~d0
cd %~dp0

if defined JAVA_HOME17 (
  set "JAVA_HOME=%JAVA_HOME17%" & set "PATH=%JAVA_HOME17%\bin;%PATH%"
)
call mvn -v
echo.

cd ..
call mvn clean package docker:remove docker:build -Dmaven.test.skip=true -U

cd bin
pause