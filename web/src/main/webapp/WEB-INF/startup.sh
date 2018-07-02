#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 运行Web工程。"
echo ""

JAVA_OPTS="$MAVEN_OPTS -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"

if [ -z "$JAVA_HOME" ]; then
  RUN_JAVA=java
else
  RUN_JAVA="$JAVA_HOME"/bin/java
fi

exec $RUN_JAVA -cp $PWD/../ $JAVA_OPTS org.springframework.boot.loader.WarLauncher