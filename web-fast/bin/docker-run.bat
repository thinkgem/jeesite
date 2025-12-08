@echo off
rem /**
rem  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 运行Docker镜像
echo.

%~d0
cd %~dp0

if defined JAVA_HOME17 (
  set "JAVA_HOME=%JAVA_HOME17%" & set "PATH=%JAVA_HOME17%\bin;%PATH%"
)
call mvn -v
echo.

cd ..
mvn docker:stop docker:start docker:logs -Ddocker.follow

echo.
echo 启动完成后访问：http://127.0.0.1:8980   用户名：system   密码：admin
echo.

cd bin
pause