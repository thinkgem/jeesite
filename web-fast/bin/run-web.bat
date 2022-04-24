@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包Web工程，并运行Web工程。
echo.

%~d0
cd %~dp0

rem 打包Web工程（开始）
cd ..
call mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
rem 打包Web工程（结束）

rem 启动服务
cd target
call java -jar web.jar

pause