@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 初始化数据库，请谨慎操作。
echo.
pause
echo.
echo [信息] 此操作会清空您的现有数据表，并恢复初始状态。
echo.
echo [信息] 确认继续吗？否则请关闭窗口。（5）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（4）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（3）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（2）
echo.
pause
echo.
echo [信息] 您真的确认继续吗？否则请关闭窗口。（1）
echo.
pause
echo.

%~d0
cd %~dp0

cd ..
call mvn test -Dtest=com.jeesite.test.InitCoreData,com.jeesite.test.InitGenData

pause