@echo off
rem /**
rem  * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 更新项目版本号。
echo.
rem pause
echo.

cd %~dp0

set /p new=请输入新版本号：
echo.

pause
echo.
cd ..

rem 更新pom版本号
call mvn versions:set -DnewVersion=%new%

rem 替换 jeesite.properties 中的版本号
echo.
set f=%cd%\src\main\resources\jeesite.properties
echo [INFO] Update %f%
set s1=version=
set s2=version=V%new%
for /f "delims=:" %%a in ('findstr /in "%s1%" "%f%"') do set n=%%a
(for /f "tokens=1* delims=:" %%a in ('findstr /n ".*" "%f%"') do (
  if %%a equ %n% ( echo.%s2%) else ( echo.%%b)
))>newfile
echo.
move newfile "%f%" >nul
echo.

cd bin
pause