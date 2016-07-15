@echo off
rem /**
rem  * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 生成Eclipse工程文件。
echo.
pause
echo.

cd %~dp0
cd..

call mvn deploy

cd bin
pause