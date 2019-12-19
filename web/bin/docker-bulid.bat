@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包Web工程，导入到Docker服务。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package docker:build -Dmaven.test.skip=true -U -Pdocker

cmd /c msg %username% /time:0 /w "打包完成..."
pause