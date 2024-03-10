@echo off
%~d0
cd %~dp0

cd..
yarn build

cd bin
pause