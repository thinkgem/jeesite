#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 部署工程到Maven服务器。"
echo ""

mvn -v
echo ""

cd ..
mvn clean deploy -Dmaven.test.skip=true -Pdeploy

cd bin