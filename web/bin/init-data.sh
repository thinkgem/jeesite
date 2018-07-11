#!/usr/bin/env bash
# /**
#  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
#  *
#  * Author: ThinkGem@163.com
#  * 
#  */
echo ""
echo "[信息] 初始化数据库，请谨慎操作。"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 此操作会清空您的现有数据表，并恢复初始状态。"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 确认继续吗？否则请关闭窗口。（5）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 您真的确认继续吗？否则请关闭窗口。（4）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 您真的确认继续吗？否则请关闭窗口。（3）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 您真的确认继续吗？否则请关闭窗口。（2）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[信息] 您真的确认继续吗？否则请关闭窗口。（1）"
read -s -n1 -p "请按任意键继续 ... "
echo ""

cd ..
mvn test -Dmaven.test.skip=false -Dtest=com.jeesite.test.InitCoreData -Djeesite.initdata=true -U
