@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 运行Docker镜像，使用 MySQL 数据库。
echo.

pause

%~d0
cd %~dp0

cd ..

rem 停止并移除容器，如果存在
call docker-compose -f docker-compose-mysql.yml stop
call docker-compose -f docker-compose-mysql.yml rm -f

rem 运行容器
call docker-compose -f docker-compose-mysql.yml up -d

rem 查看日志
call docker-compose -f docker-compose-mysql.yml logs -f

echo .
echo 启动完成 ... 
echo .

cd bin
pause