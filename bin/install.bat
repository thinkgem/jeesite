@echo off
%~d0
cd %~dp0

cd..
npm run install

cd bin
pause