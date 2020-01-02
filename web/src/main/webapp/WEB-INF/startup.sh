#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 运行Web工程。"
echo ""

# 设置JDK目录
# JAVA_HOME="$PWD/jdk1.8.0_x64"

# 设置类加载路径
CLASS_PATH="$PWD/../"

# 优化JVM参数
JAVA_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"

# 方式一、配置外部自定义的属性文件（建议）
# JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=$PWD/app.yml"

# 方式二、配置环境名称，加载不同的属性文件
# JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"

if [ -z "$JAVA_HOME" ]; then
  RUN_JAVA=java
else
  RUN_JAVA="$JAVA_HOME"/bin/java
fi

exec $RUN_JAVA -cp $CLASS_PATH $JAVA_OPTS org.springframework.boot.loader.WarLauncher