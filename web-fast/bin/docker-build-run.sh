#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，编译并运行Docker镜像。"
echo ""

cd ..
mvn clean package docker:remove docker:build docker:stop docker:start docker:logs -Dmaven.test.skip=true -U -Ddocker.follow

echo ""
echo "启动完成后访问：http://127.0.0.1:8980   用户名：system   密码：admin"
echo ""

cd bin