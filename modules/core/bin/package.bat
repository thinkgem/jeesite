@echo off
rem /**
rem  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 打包安装工程，生成jar包文件。
echo.

%~d0
cd %~dp0

if defined JAVA_HOME8 (
  set "JAVA_HOME=%JAVA_HOME8%" & set "PATH=%JAVA_HOME8%\bin;%PATH%"
)
call mvn -v
echo.

cd ..
call mvn clean install -Dmaven.test.skip=true -Ppackage

cd bin
pause