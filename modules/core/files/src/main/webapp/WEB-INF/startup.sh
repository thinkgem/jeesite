#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  */
echo ""
echo "[信息] 运行Web工程。"
echo ""

if [ -n "$JAVA_HOME17" ] && [ -d "$JAVA_HOME17" ]; then
  export JAVA_HOME="$JAVA_HOME17" PATH="$JAVA_HOME/bin:$PATH"
fi
mvn -v
echo ""

cd "$(cd "$(dirname "$0")"; pwd)"

# 设置JDK目录
# JAVA_HOME="$PWD/jdk1.8.0_x64"

# 设置类加载路径
CLASS_PATH="$PWD/../"

# 优化JVM参数
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx1024m -Dfile.encoding=utf-8"

# 方式一、配置外部自定义的属性文件（建议）
# JAVA_OPTS="$JAVA_OPTS -Dspring.config.location=$PWD/app.yml"

# 方式二、配置环境名称，加载不同的属性文件
# JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"

if [ -z "$JAVA_HOME" ]; then
  RUN_JAVA=java
else
  RUN_JAVA="$JAVA_HOME"/bin/java
fi

exec "$RUN_JAVA" -cp $CLASS_PATH $JAVA_OPTS org.springframework.boot.loader.launch.WarLauncher $@