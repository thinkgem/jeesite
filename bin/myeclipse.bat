@echo off
echo [INFO] Use maven eclipse-plugin download jars and generate myeclipse project files.

cd %~dp0
cd ..

set path=%MAVEN_HOME%/bin;%windir%/system32;%path%
call mvn -Declipse.workspace=%cd% eclipse:clean eclipse:myeclipse

cd bin
pause