#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，生成war/jar包文件。"
echo ""

cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
