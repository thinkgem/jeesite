@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 使用 Spring Boot Docker 编译 Web 工程。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package docker:build -Dmaven.test.skip=true -U -Pdocker

pause