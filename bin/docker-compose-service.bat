@echo off
rem /**
rem  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 部署Docker镜像，包含：Core、Files、Test1、Test2、Test3、Gateway 服务。
echo.

echo 请预先执行 package.sh 进行打包，如已打包，请按任意键继续... 
echo.
pause

%~d0
cd %~dp0

cd ..

rem 停止并移除容器，如果存在
call docker-compose -f docker-compose-service.yml stop
call docker-compose -f docker-compose-service.yml rm -f

rem 运行容器
call docker-compose -f docker-compose-service.yml up -d

rem 查看日志
call docker-compose -f docker-compose-service.yml logs -f

echo .
echo 启动完成 ... 
echo .

cd bin
pause