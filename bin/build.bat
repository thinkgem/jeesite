@echo off
%~d0
cd %~dp0

cd..
npm run build

cd bin
pause