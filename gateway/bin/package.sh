#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 打包Web工程，生成war/jar包文件。"
echo ""

if [ -n "$JAVA_HOME8" ] && [ -d "$JAVA_HOME8" ]; then
  export JAVA_HOME="$JAVA_HOME8" PATH="$JAVA_HOME/bin:$PATH"
fi
mvn -v
echo ""

cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U

cd bin