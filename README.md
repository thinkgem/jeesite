
## 技术交流

* 交流 QQ 群（千人大群）：`127515876`、`209330483`、`223507718`、`709534275`、`730390092`、`1373527`、`183903863(外包)`
* 问题反馈：<https://gitee.com/thinkgem/jeesite4/issues> 　[【新手必读】](https://gitee.com/thinkgem/jeesite4/issues/I18ARR)
* 码云Gitee：<https://gitee.com/thinkgem/jeesite4>
* GitHub：<https://github.com/thinkgem/jeesite4>
* 作者博客：<https://my.oschina.net/thinkgem>
* **技术服务：**<http://s.jeesite.com>
* 官方网站：<http://jeesite.com>
* 官方论坛：<http://jeesite.net>
* 关注微信公众号，了解最新动态：

![JeeSite4微信公众号](https://images.gitee.com/uploads/images/2020/0727/091951_a3ab258c_6732.jpeg "JeeSite4微信公众号")

## 平台介绍

JeeSite 快速开发平台，不仅仅是一个后台开发框架，它是一个企业级快速开发解决方案，基于经典技术组合（Spring Boot、Spring MVC、Apache Shiro、MyBatis、Beetl、Bootstrap、AdminLTE）采用经典开发模式，让初学者能够更快的入门并投入到团队开发中去。在线代码生成功能，包括模块如：组织机构、角色用户、菜单及按钮授权、数据权限、系统参数、内容管理、工作流等。采用松耦合设计，模块增减便捷；界面无刷新，一键换肤；众多账号安全设置，密码策略；文件在线预览；消息推送；多元化第三方登录；在线定时任务配置；支持集群，支持SAAS；支持多数据源；支持读写分离、分库分表；支持微服务应用。

JeeSite 快速开发平台的主要目的是能够让初级的研发人员快速的开发出复杂的业务功能（经典架构会的人多），让开发者注重专注业务，其余有平台来封装技术细节，降低技术难度，从而节省人力成本，缩短项目周期，提高软件安全质量。

JeeSite 自 2013 年发布以来已被广大爱好者用到了企业、政府、医疗、金融、互联网等各个领域中，JeeSite 架构精良、易于扩展、大众思维的设计模式、工匠精神打磨每一个细节，深入开发者的内心，并荣获开源中国《最受欢迎中国开源软件》奖杯，期间也帮助了不少刚毕业的大学生，教师作为入门教材，快速的去实践。

JeeSite4 的升级，作者结合了多年总结和经验，以及各方面的应用案例，对架构完成了一次全部重构，也纳入很多新的思想。不管是从开发者模式、底层架构、逻辑处理还是到用户界面，用户交互体验上都有很大的进步，在不忘学习成本、提高开发效率的情况下，安全方面也做和很多工作，包括：身份认证、密码策略、安全审计、日志收集等众多安全选项供你选择。努力为大中小微企业打造全方位企业级快速开发解决方案。

学会使用 JeeSite 的知识点非常多，也非常少，因为她使用的都是一些通用的技术，通俗的设计风格，多数基础知识点大家都能掌握。 你只需要学会 JeeSite 的功能使用及组件应用就可以完成开发了。

### 4.x 的新特性及优势：<http://jeesite.com/docs/feature/>

## 技术选型

* 主框架：Spring Boot 2.2、Spring Framework 5.2、Apache Shiro 1.6、J2Cache
* 持久层：Apache MyBatis 3.5、Hibernate Validator 6.0、Alibaba Druid 1.1
* 视图层：Spring MVC 5.2、Beetl 3.1（替换JSP）、Bootstrap 3.3、AdminLTE 2.4
* 前端组件：jQuery 3.4、jqGrid 4.7、layer 3.1、zTree 3.5、jquery validation
* 工作流引擎：Flowable 6.5、符合 BPMN 规范、在线流程设计器、中国式工作流
* 技术选型详情：<http://jeesite.com/docs/technology/>

## 内置功能

* <http://jeesite.com/docs/function/>

## 生态系统

* 分布式微服务系统（Spring Cloud）：<https://gitee.com/thinkgem/jeesite4-cloud>
* JFlow工作流引擎：<https://gitee.com/thinkgem/jeesite4-jflow> ：<http://ccflow.org>
* Flowable业务流程模块（BPM）：<http://jeesite.com/docs/bpm/>
* 内容管理模块（CMS）：<https://gitee.com/thinkgem/jeesite4-cms>

## 快速体验

### 在线演示

1. 地址：<http://demo.jeesite.com/>
2. 账号：system
3. 密码：admin

### 本地运行

1. 环境准备：`JDK 1.8 or 11`、`Maven 3.6+`、`MySQL 5.7 or 8.0`
2. 执行命令：`git clone https://gitee.com/thinkgem/jeesite4.git` 下载源码
3. 打开文件：`/web/src/main/resources/config/application.yml` 配置JDBC连接
4. 执行脚本：`/web/bin/init-data.bat` 初始化数据库
5. 执行脚本：`/web/bin/run-tomcat.bat` 启动服务即可
6. 浏览器访问：<http://127.0.0.1:8980/js/>  账号 system 密码 admin
7. 部署常见问题：<http://jeesite.com/docs/faq/>

### 开发环境

1. 部署运行文档：<http://jeesite.com/docs/install-deploy/>
2. 部署常见问题：<http://jeesite.com/docs/faq/>

## 在线文档

* 在线文档学习地址：<http://docs.jeesite.com>

## 授权协议声明

1. 已开源的代码，授权协议采用 AGPL v3 + Apache Licence v2 进行发行。
2. 您可以免费使用、修改和衍生代码，但不允许修改后和衍生的代码做为闭源软件发布。
3. 修改后和衍生的代码必须也按照AGPL协议进行流通，对修改后和衍生的代码必须向社会公开。
4. 如果您修改了代码，需要在被修改的文件中进行说明，并遵守代码格式规范，帮助他人更好的理解您的用意。
5. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者规定
需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`@author`信息）。
6. 您可以应用于商业软件，但必须遵循以上条款原则（请协助改进本作品）。
7. 您若套用本平台的一些代码或功能参考，需要在您的软件介绍明显位置说明出处。
8. 请知悉社区版，用户数不可超过100个，最大允许20个用户同时在线（不含匿名）。
9. **无限制版下载：<https://gitee.com/thinkgem/jeesite4/tree/v4.0_dev/>**

## 技术服务与支持

* 没有资金的支撑就很难得到发展，特别是一个好的产品，如果 JeeSite 帮助了您，请为我们点赞。支持我们，您可以得到一些回报，有了这些我们会把公益事业做的更好，回报社区和社会，请给我们一些动力吧，在此非常感谢已支持我们的朋友！
* **联系方式（官方商务）QQ：[1766571055](http://wpa.qq.com/msgrd?v=3&uin=1766571055&site=qq&menu=yes)**
* 技术服务支持网页：<http://s.jeesite.com>

## 今后如何升级？

尽量不修改 web 项目以外的源码项目，如 jeesite-common、jeesite-modele-core，如果修改了，请 Pull Requests 上来，否则代码与官方不同步，会将对你的日后升级增加难度。

如果您修改了依赖模块代码，也没关系，这时你需要利用 Git 版本控制工具，与官方仓库代码进行同步，合并代码即可。

每个版本升级，我们都会附带详细更新日志：<http://jeesite.com/docs/upgrade/>。

在这里，你可以看到 JeeSite 新增哪些新功能和改进，在每个版本下都有对应升级方法。

# Git 全局设置技巧

```
1、提交检出均不转换换行符

git config --global core.autocrlf false

2、拒绝提交包含混合换行符的文件

git config --global core.safecrlf true
```