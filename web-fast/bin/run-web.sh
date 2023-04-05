#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，并运行Web工程。"
echo ""

# 打包Web工程（开始）
cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
# 打包Web工程（结束）

# 启动服务
cd target
java -jar web.jar