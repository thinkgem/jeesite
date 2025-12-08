#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 打包Web工程，并运行Web工程。"
echo ""

if [ -n "$JAVA_HOME17" ] && [ -d "$JAVA_HOME17" ]; then
  export JAVA_HOME="$JAVA_HOME17" PATH="$JAVA_HOME/bin:$PATH"
fi
mvn -v
echo ""

cd ..
mvn clean package spring-boot:repackage -Dmaven.test.skip=true -U
cd target

# web.war 与 pom.xml 中 finalName、packaging 一致
mkdir app
cp web.war ./app
cd app
jar -xvf web.war
rm web.war
cd WEB-INF
sh ./startup.sh
