#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 部署Docker镜像，包含：MySQL、Redis、Nacos 服务。"
echo ""

echo "如果是首次启动，请访问 Nacos 设置初始密码，并导入 jeesite-cloud-yml.zip 配置"
echo ""
echo "请按回车键继续 ... "
read text

cd ..

# 停止并移除容器，如果存在
docker compose -f docker-compose-basic.yml stop
docker compose -f docker-compose-basic.yml rm -f

mvn clean package -Dmaven.test.skip=true -U

# 运行容器
docker compose -f docker-compose-basic.yml up -d

# 查看日志
docker compose -f docker-compose-basic.yml logs -f

echo ""
echo "启动完成 ... "
echo ""

cd bin
