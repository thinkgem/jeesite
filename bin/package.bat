@echo off
echo [INFO] Package jar.

cd %~dp0
cd ..

call mvn clean package -Dmaven.test.skip=true

cd bin
pause