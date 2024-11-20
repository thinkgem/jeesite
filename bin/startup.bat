@echo off
%~d0
cd %~dp0

cd..
npm run dev

cd bin
pause