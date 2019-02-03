@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
cd target
rem 打包Web工程（结束）

title %cd%

rem 优化JVM参数
set "JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"

rem 方式一、配置外部自定义的属性文件（建议）
rem set "JAVA_OPTS=%JAVA_OPTS% -Dspring.config.location=%cd%\app.yml"

rem 方式二、配置环境名称，加载不同的属性文件
rem set "JAVA_OPTS=%JAVA_OPTS% -Dspring.profiles.active=prod"

if "%JAVA_HOME%" == "" goto noJavaHome
if not "%JAVA_HOME%" == "" goto gotJavaHome
goto end

:noJavaHome
set RUN_JAVA=java
goto runJava

:gotJavaHome
set "RUN_JAVA=%JAVA_HOME%\bin\java"
goto runJava

:runJava
rem 根据情况修改 web.war 为您的 war 包名称
call "%RUN_JAVA%" %JAVA_OPTS% -jar web.war
goto end

:end
pause