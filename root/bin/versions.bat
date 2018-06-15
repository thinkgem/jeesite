@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 更新项目版本号。
echo.

%~d0
cd %~dp0

set /p new=请输入新版本号：
echo.

set /p choice=如果是正式版请按 "y" 否则按任意键继续：
if /i "%choice%" neq "y" set new=%new%-SNAPSHOT
echo.

cd ../jeesite

rem 更新pom版本号
cd ../parent
call mvn versions:set -DnewVersion=%new%

cd ../web
call mvn versions:set -DnewVersion=%new%

pause