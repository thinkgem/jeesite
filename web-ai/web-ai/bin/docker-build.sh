#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，编译Docker镜像。"
echo ""

if [ -n "$JAVA_HOME17" ] && [ -d "$JAVA_HOME17" ]; then
  export JAVA_HOME="$JAVA_HOME17" PATH="$JAVA_HOME17/bin:$PATH"
fi
mvn -v
echo ""

cd ..
mvn clean package docker:remove docker:build -Dmaven.test.skip=true -U

cd bin