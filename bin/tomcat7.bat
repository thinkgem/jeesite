@echo off
echo [INFO] Use maven tomcat7-plugin run the project.

cd %~dp0
cd ..

set path=%MAVEN_HOME%/bin;%windir%/system32;%path%
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m
call mvn clean tomcat7:run

cd bin
pause