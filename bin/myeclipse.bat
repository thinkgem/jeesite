@echo off
echo [INFO] Use maven eclipse-plugin download jars and generate myeclipse project files.

%~d0
cd %~dp0
cd ..

call mvn -Declipse.workspace=%cd% eclipse:clean eclipse:myeclipse

cd bin
pause
