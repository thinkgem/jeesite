@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 部署Docker镜像，包含：Test1、Test2、Test3 服务。
echo.

pause

%~d0
cd %~dp0

cd ..

rem 停止并移除容器，如果存在
call docker-compose -f docker-compose-test.yml stop
call docker-compose -f docker-compose-test.yml rm -f

rem 运行容器
call docker-compose -f docker-compose-test.yml up -d

rem 查看日志
call docker-compose -f docker-compose-test.yml logs -f

echo .
echo 启动完成 ... 
echo .

cd bin
pause