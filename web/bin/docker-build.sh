#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，编译Docker镜像。"
echo ""

cd ..
mvn clean package docker:remove docker:build -Dmaven.test.skip=true -U

cd bin