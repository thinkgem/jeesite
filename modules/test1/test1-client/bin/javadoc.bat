@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 编译工程Javadoc，生成jar包文件。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Pjavadoc

pause