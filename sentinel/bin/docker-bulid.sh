#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 使用 Spring Boot Docker 编译 Web 工程。"
echo ""

cd ..
mvn clean package docker:stop docker:remove docker:build docker:run -Dmaven.test.skip=true -U

cd bin