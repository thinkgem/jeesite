#!/bin/sh
# /**
#  * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
#  * No deletion without permission, or be held responsible to law.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 运行Docker镜像，使用 MySQL 数据库。"
echo ""

echo "请按回车键继续 ... "
read text

cd ..

# 停止并移除容器，如果存在
docker-compose -f docker-compose-mysql.yml stop
docker-compose -f docker-compose-mysql.yml rm -f

# 运行容器
docker-compose -f docker-compose-mysql.yml up -d

# 查看日志
docker-compose -f docker-compose-mysql.yml logs -f

echo ""
echo "启动完成 ... "
echo ""

cd bin
