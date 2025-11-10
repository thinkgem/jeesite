@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  * No deletion without permission, or be held responsible to law.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [信息] 部署Docker镜像，包含：MySQL、Redis、Nacos 服务。
echo.

echo 如果是首次启动，请访问 Nacos 设置初始密码，并导入 jeesite-cloud-yml.zip 配置
echo.
pause

%~d0
cd %~dp0

if defined JAVA_HOME17 (
  set "JAVA_HOME=%JAVA_HOME17%" & set "PATH=%JAVA_HOME17%\bin;%PATH%"
)
call mvn -v
echo.

cd ..

rem 停止并移除容器，如果存在
call docker-compose -f docker-compose-basic.yml stop
call docker-compose -f docker-compose-basic.yml rm -f

rem 打包工程
call mvn clean package -Dmaven.test.skip=true -U

rem 运行容器
call docker-compose -f docker-compose-basic.yml up -d

rem 查看日志
call docker-compose -f docker-compose-basic.yml logs -f

echo .
echo 启动完成 ... 
echo .

cd bin
pause