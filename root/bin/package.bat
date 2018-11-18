@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 安装到本地仓库。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean install -Dmaven.test.skip=true -Ppackage

pause