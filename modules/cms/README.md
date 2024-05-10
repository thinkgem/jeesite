
# 技术交流

* 交流 QQ 群（千人大群）：`127515876`、`209330483`、`223507718`、`709534275`、`730390092`、`1373527`、`183903863(外包)`
* 码云Gitee：<https://gitee.com/thinkgem/jeesite5>
* GitHub：<https://github.com/thinkgem/jeesite5>
* 作者博客：<https://my.oschina.net/thinkgem>
* **帮助文档：**<http://docs.jeesite.com>
* 官方网站：<http://jeesite.com>
* 问题反馈：<http://jeesite.net> [【新手必读】](https://gitee.com/thinkgem/jeesite5/issues/I18ARR)
* 关注微信公众号，了解最新动态：

![JeeSite微信公众号](https://images.gitee.com/uploads/images/2020/0727/091951_a3ab258c_6732.jpeg "JeeSite微信公众号")

## 介绍

JeeSite CMS 内容管理，是一个轻量级的 Web 建站神器，可以快速进行企业网站、门户网站、个人博客、知识库等建设。

* 基础功能：站点管理、栏目管理、栏目权限、模板管理、内容发布
* 高级功能：多租户、多站点、页面静态化访问、全文检索

规划内容：站内统计、标签管理、留言管理、相关文章选取、文章多栏目

## 快速体验

### 在线演示

1. 前端演示：<http://demo.jeesite.com/js/f/index-main.html>
2. 后端演示：<http://demo.jeesite.com/js/a/index#/js/a/cms/index#内容发布>
3. 账号：system
4. 密码：admin

### 本地运行

1. 环境准备：`JDK 1.8 or 11、17`、`Maven 3.6+`、`MySQL 5.7 or 8.0`
2. 下载源码：<https://gitee.com/thinkgem/jeesite5/tree/master/modules/cms>
3. 打开文件：/web`/src/main/resources/config/application.yml` 配置JDBC连接
4. 打开文件：/web`/pom.xml` 打开 `jeesite-module-cms` 模块（去掉的注释标记）
5. 执行脚本：/web`/bin/init-data.bat` 初始化数据库
5. 执行脚本：/web`/bin/run-tomcat.bat` 启动服务即可
6. 浏览器访问：<http://127.0.0.1:8980/js/>  账号 system 密码 admin
7. 部署常见问题：<https://jeesite.com/docs/faq/>

### 开发环境

1. 部署运行文档：<https://jeesite.com/docs/install-deploy/>
2. 部署常见问题：<https://jeesite.com/docs/faq/>

## 在线文档

* <http://docs.jeesite.com>
