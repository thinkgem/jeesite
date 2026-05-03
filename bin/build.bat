@echo off
%~d0
cd %~dp0

cd..
pnpm build

cd bin
pause