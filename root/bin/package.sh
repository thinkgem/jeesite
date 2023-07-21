#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 打包安装工程，生成jar包文件。"
echo ""

mvn -v
echo ""

cd ..
mvn clean install -Dmaven.test.skip=true -Ppackage

cd bin