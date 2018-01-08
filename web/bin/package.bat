@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包Web工程，生成war包文件。
echo.
pause
echo.

%~d0
cd %~dp0

cd ../
call mvn clean package -Dmaven.test.skip=true -Ppackage -U

pause