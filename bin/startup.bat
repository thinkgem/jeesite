@echo off
%~d0
cd %~dp0

cd..
pnpm dev

cd bin
pause