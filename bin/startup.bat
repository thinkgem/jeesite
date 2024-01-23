@echo off
%~d0
cd %~dp0

cd..
yarn serve

cd bin
pause