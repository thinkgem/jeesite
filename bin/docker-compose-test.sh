#!/bin/sh
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 部署Docker镜像，包含：Test1、Test2、Test3 服务。"
echo ""

echo "请按回车键继续 ... "
read text

cd ..

# 停止并移除容器，如果存在
docker compose -f docker-compose-test.yml stop
docker compose -f docker-compose-test.yml rm -f

# 运行容器
docker compose -f docker-compose-test.yml up -d

# 查看日志
docker compose -f docker-compose-test.yml logs -f

echo ""
echo "启动完成 ... "
echo ""

cd bin
