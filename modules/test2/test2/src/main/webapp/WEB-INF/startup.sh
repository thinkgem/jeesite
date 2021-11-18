#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 运行Web工程。"
echo ""

# 优化JVM参数
JAVA_OPTS="$MAVEN_OPTS -Xms256m -Xmx1024m"

# 方式一、配置外部自定义的属性文件（建议）
# JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=$PWD\app.yml"

# 方式二、配置环境名称，加载不同的属性文件
# JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"

if [ -z "$JAVA_HOME" ]; then
  RUN_JAVA=java
else
  RUN_JAVA="$JAVA_HOME"/bin/java
fi

exec $RUN_JAVA -cp $PWD/../ $JAVA_OPTS org.springframework.boot.loader.WarLauncher