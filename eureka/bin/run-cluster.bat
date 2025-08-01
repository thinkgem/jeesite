@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  */

%~d0
cd %~dp0

echo.
echo �Ⱦ�����
echo.
echo 1������ 3 ���ڵ�
echo.
echo 2��hosts �ļ�������������
echo.
echo 127.0.0.1 eureka1
echo 127.0.0.1 eureka2
echo 127.0.0.1 eureka3
echo.
pause

echo.
echo ====== ��� Eureka ======
echo.

cd ..
call mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
cd target

echo.
echo ====== ��ѹ Eureka ======
echo.

rem web.war �� pom.xml �� finalName��packaging һ��
mkdir app
copy web.war app
cd app
jar -xvf web.war
del web.war
cd WEB-INF

echo.
echo ====== ���� Eureka ======
echo.

set "JAVA_OPTS=-cp %cd%/../ -Xms512m -Xmx512m -Dfile.encoding=utf-8"
set "JAVA_OPTS=%JAVA_OPTS% -Deureka.client.registerWithEureka=true"
set "JAVA_OPTS=%JAVA_OPTS% -Deureka.client.serviceUrl.defaultZone=http://eureka1:8970/eureka/,"
set "JAVA_OPTS=%JAVA_OPTS%http://eureka2:8969/eureka/,http://eureka3:8968/eureka/"

set "JAVA_OPTS1=-Dserver.port=8970 -Deureka.instance.hostname=eureka1"
set "JAVA_OPTS2=-Dserver.port=8969 -Deureka.instance.hostname=eureka2"
set "JAVA_OPTS3=-Dserver.port=8968 -Deureka.instance.hostname=eureka3"

set "CLASS_NAME=org.springframework.boot.loader.launch.WarLauncher"

start cmd /c "title %JAVA_OPTS1% && java %JAVA_OPTS% %JAVA_OPTS1% %CLASS_NAME%"
start cmd /c "title %JAVA_OPTS2% && java %JAVA_OPTS% %JAVA_OPTS2% %CLASS_NAME%"
start cmd /c "title %JAVA_OPTS3% && java %JAVA_OPTS% %JAVA_OPTS3% %CLASS_NAME%"

echo http://eureka1:8970/
echo http://eureka2:8969/
echo http://eureka3:8968/

echo.
pause
