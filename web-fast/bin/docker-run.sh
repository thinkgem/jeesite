#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 运行Docker镜像。"
echo ""

if [ -n "$JAVA_HOME17" ] && [ -d "$JAVA_HOME17" ]; then
  export JAVA_HOME="$JAVA_HOME17" PATH="$JAVA_HOME/bin:$PATH"
fi
mvn -v
echo ""

cd ..
mvn docker:stop docker:start docker:logs -Ddocker.follow

echo ""
echo "启动完成后访问：http://127.0.0.1:8980   用户名：system   密码：admin"
echo ""

cd bin