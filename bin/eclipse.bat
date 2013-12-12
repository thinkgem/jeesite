@echo off
echo [INFO] Use maven eclipse-plugin download jars and generate eclipse project files.

cd %~dp0
cd ..

call mvn -Declipse.workspace=%cd% eclipse:clean eclipse:eclipse

cd bin
pause