@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [��Ϣ] ���Web���̣�������Web���̡�
echo.

%~d0
cd %~dp0

rem ���Web���̣���ʼ��
cd ..
call mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
cd target
rem ���Web���̣�������


rem web.war �� pom.xml �� finalName��packaging һ��
mkdir app
copy web.war app
cd app
jar -xvf web.war
del web.war
cd WEB-INF
call startup.bat

pause