#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 部署工程到Maven服务器。"
echo ""

if [ -n "$JAVA_HOME17" ] && [ -d "$JAVA_HOME17" ]; then
  export JAVA_HOME="$JAVA_HOME17" PATH="$JAVA_HOME17/bin:$PATH"
fi
mvn -v
echo ""

cd ..
mvn clean deploy -Dmaven.test.skip=true -Pdeploy

cd bin