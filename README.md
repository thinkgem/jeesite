# 引言

JeeSite 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring MVC、Apache Shiro、MyBatis、Beetl、Bootstrap、AdminLTE），在线代码生成功能，包括核心模块如：组织机构、角色用户、菜单及按钮授权、数据权限、系统参数、内容管理、工作流等。采用松耦合设计；界面无刷新，一键换肤；众多账号安全设置，密码策略；在线定时任务配置；支持集群，支持SAAS；支持多数据源。

JeeSite 自开源以来已被广大爱好者用到了企业、政府、医疗、金融、互联网等各个领域中，JeeSite 依架构简单精良、易于扩展、大众思维的设计模式，深入开发者的内心，并得到一致好评，于[2017](http://www.oschina.net/project/top_cn_2016?sort=1)和[2018](http://www.oschina.net/project/top_cn_2017?sort=1)连续两年获得开源中国《最受欢迎中国开源软件》奖杯，期间也帮助了不少刚毕业的大学生作为入门教材，快速的去实践。

现在 JeeSite 4.0 来了，4.0的升级，作者结合了多年总结和经验，以及各方面的应用案例，对架构完成了一次全部重构，也纳入很多新的思想。不管是从开发者模式、底层架构、逻辑处理还是到用户界面，用户交互体验上都有很大的进步，在不忘学习成本、提高开发效率的情况下，安全方面也做和很多工作，包括：身份认证、密码策略、安全审计、日志收集。

# 快速了解 JeeSite 4.0

* ##### JeeSite 4.0 新特性、技术选型

   <https://my.oschina.net/thinkgem/blog/913777>

* ##### JeeSite 4.0 简化MyBatis持久层开发

   <https://my.oschina.net/thinkgem/blog/1503611>

* ##### JeeSite 4.0 简化业务逻辑层开发

   <https://my.oschina.net/thinkgem/blog/1538766>

* ##### JeeSite 4.0 MVC层及前端组件介绍

   <https://my.oschina.net/thinkgem/blog/1561129>

* ##### JeeSite 4.0 内置功能模块划分

   <https://my.oschina.net/thinkgem/blog/1609852>

# 快速体验、部署运行

1. 环境准备：`JDK 1.8`、`Maven 3.3`、`MySQL 5.7`
2. 打开文件 `/web/src/main/resources/jeesite.yml` 配置JDBC连接
3. 执行脚本 `/web/bin/init-data.bat` 初始化数据库
4. 执行脚本 `/web/bin/run-tomcat.bat` 启动服务即可
5. 浏览器访问 <http://127.0.0.1:8980/js/>  账号 system 密码 admin

# 常见问题

* 管理员账号system与admin的区别：system为开发者使用的最高级别管理员，主要用于开发和调试，有些修改会直接影响系统的正常运行；admin为客户方使用的系统管理员，用于一些基础数据配置，如机构、用户、权限、用户字典等。 

# 技术交流方式

* QQ 群： `127515876`、`209330483`、`223507718` 
* 入群须知：目前为付费群，刚入群会有5分钟禁言，腾讯预设置的，无法解除，稍等片刻即可正常发言；由于群容量有限，为了维持运营千人QQ群的所需支付的QQ年费会员费用，故开启付费入群模式，申请者只需支付少量金额即可加入，这样也可以保证只有真实交流需求的人进入，避免闲杂做广告人员的乱入；新手提问前，请先阅读此[【文章】](http://www.dianbo.org/9238/stone/tiwendezhihui.htm)。
* 开源问答：https://www.oschina.net/question/tag/jeesite
* 联系作者：`E-mail：thinkgem@163.com` &nbsp; `QQ：78112665`
* GitHub：<https://github.com/thinkgem/jeesite4>
* 开源中国：<http://git.oschina.net/thinkgem/jeesite4>
* 官方网址：<http://jeesite.com>  论坛：<http://bbs.jeesite.com>

# 关于授权协议声明

1. 当前开源代码的授权协议采用 AGPL v3 + Apache Licence v2 进行发行。
2. 您可以免费使用、修改和衍生代码，但不允许修改后和衍生的代码做为闭源软件发布和销售。
3. 修改后和衍生的代码必须也按照AGPL协议进行流通，对修改后和衍生的代码必须向社会公开。
4. 如果您修改了代码，需要在被修改的文件中进行说明（帮助他人更好的理解您的用意）。
5. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者规定需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`@author`信息）。
6. **社区版**您可以应用于商业软件，但必须遵循以上条款原则（请协助改进本作品）。
7. 没有收入的开源软件不是好软件，请给我们一些动力吧，后面会推出**企业版**敬请期待。

# Maven快照强制更新

Eclipse： 在web项目上右键，选择菜单 -> Maven -> Update Project...（或按Alt+F5） -> 点击 Select All 按钮 -> 选择 Force Update of Snapshots/Releases 复选框 -> 点击OK按钮即可

# Git 全局设置技巧

```
1、提交检出均不转换换行符

git config --global core.autocrlf false

2、拒绝提交包含混合换行符的文件

git config --global core.safecrlf true
```