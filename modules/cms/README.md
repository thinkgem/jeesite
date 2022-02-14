
# 技术交流

* 交流 QQ 群（千人大群）：`127515876`、`209330483`、`223507718`、`709534275`、`730390092`、`1373527`、`183903863(外包)`
* 码云Gitee：<https://gitee.com/thinkgem/jeesite4>
* GitHub：<https://github.com/thinkgem/jeesite4>
* 作者博客：<https://my.oschina.net/thinkgem>
* **帮助文档：**<http://docs.jeesite.com>
* 官方网站：<http://jeesite.com>
* 问题反馈：<http://jeesite.net> [【新手必读】](https://gitee.com/thinkgem/jeesite4/issues/I18ARR)
* 关注微信公众号，了解最新动态：

![JeeSite微信公众号](https://images.gitee.com/uploads/images/2020/0727/091951_a3ab258c_6732.jpeg "JeeSite微信公众号")

## 项目介绍

JeeSite CMS 内容管理模块

包含功能：站点管理、栏目管理、模板管理、内容发布

规划内容：站内统计、站内搜索、标签管理、留言管理、权限及审核、相关文章选取、文章多栏目

## 快速体验

### 在线演示

1. 前端演示：<http://demo.jeesite.com/js/f/index-main.html>
2. 后端演示：<http://demo.jeesite.com/js/a/index#/js/a/cms/index#内容发布>
3. 账号：system
4. 密码：admin

### 本地运行

1. 环境准备：`JDK 1.8 or 11、17`、`Maven 3.6+`、`MySQL 5.7 or 8.0`
2. 下载源码：<https://gitee.com/thinkgem/jeesite4/tree/master/modules/cms>
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

## 授权协议声明

1. 基于 Apache License Version 2.0 协议发布，可用于商业项目，但必须遵守以下补充条款。
2. 不得将本软件应用于危害国家安全、荣誉和利益的行为，不能以任何形式用于非法为目的的行为。
3. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者
   规定需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`Copyright`和`@author`信息）
   更不要，全局替换源代码中的 jeesite 或 ThinkGem 等字样，否则你将违反本协议条款承担责任。
4. 您若套用本软件的一些代码或功能参考，请保留源文件中的版权和作者，需要在您的软件介绍明显位置
   说明出处，举例：本软件基于 JeeSite 快速开发平台，并附带链接：http://jeesite.com
5. 任何基于本软件而产生的一切法律纠纷和责任，均于我司无关。
6. 如果你对本软件有改进，希望可以贡献给我们，共同进步。
7. 本项目已申请软件著作权，请尊重开源，感谢阅读。
8. 本版本无用户数限制，无在线人数限制。

## 技术服务与支持

* 没有资金的支撑就很难得到发展，特别是一个好的产品，如果 JeeSite 帮助了您，请为我们点赞。支持我们，您可以得到一些回报，有了这些我们会把公益事业做的更好，回报社区和社会，请给我们一些动力吧，在此非常感谢已支持我们的朋友！
* **联系我们**：请访问技术支持服务页面：<https://jeesite.com/docs/support/> 

## 今后如何升级？

尽量不修改 web 项目以外的源码项目，如 jeesite-common、jeesite-modele-core，如果修改了，请 Pull Requests 上来，否则代码与官方不同步，可能会将对你的日后升级增加难度。

如果您修改了依赖模块代码，也没关系，这时你需要利用 Git 版本控制工具，与官方仓库代码进行同步，合并代码即可。

每个版本升级，我们都会附带详细更新日志：<https://jeesite.com/docs/upgrade/>。

在这里，你可以看到 JeeSite 新增哪些新功能和改进，在每个版本下都有对应升级方法。

如果跨版本升级，可以将版本号直接修改为最新版本，然后去看每个版本的升级方法，修改对应业务。

# Git 全局设置技巧

```
1、提交检出均不转换换行符

git config --global core.autocrlf false

2、拒绝提交包含混合换行符的文件

git config --global core.safecrlf true
```