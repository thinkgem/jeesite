@echo off
echo [INFO] Install jar to local repository.

%~d0
cd %~dp0
cd ..

call mvn clean install -Dmaven.test.skip=true

cd bin
pause
