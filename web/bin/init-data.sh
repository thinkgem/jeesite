#!/usr/bin/env bash

echo "[信息] 初始化数据库，请谨慎操作。\n"
read -s -n1 -p "按任意键继续 ... "
echo "\n[信息] 此操作会清空您的现有数据表，并恢复初始状态。\n"
read -s -n1 -p "按任意键继续 ... "
echo "\n[信息] 确认继续吗？否则请关闭窗口。\n"
read -s -n1 -p "按任意键继续 ... "

cd ..
mvn test -Dtest=com.jeesite.test.InitCoreData,com.jeesite.test.InitGenData
