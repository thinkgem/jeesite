# JeeSite 4.0

JeeSite是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring MVC、Apache Shiro、MyBatis、Beetl、Bootstrap、AdminLTE），在线代码生成功能，包括核心模块如：组织机构、角色用户、菜单及按钮授权、数据权限、系统参数、内容管理、工作流等。采用松耦合设计；界面无刷新，一键换肤；众多账号安全设置，密码策略；在线定时任务配置；支持集群，支持SAAS；支持多数据源；

JeeSite自开源以来已被用到了企业、政府、医疗、金融、互联网等各个领域中，JeeSite的设计思想和开发模式也深入开发者的内心，并得到一致好评，于2017和2018连续两年获得最受欢迎中国开源软件奖杯，期间也帮助了不少刚毕业的大学生去快速学习和实践。4.0的升级和规划作者也是结合了多年的经验和总结各方面的应用案例，对架构完成了一次全部重构，也纳入很多新的思想。不管是从开发者模式、底层架构、逻辑处理还是到用户界面都有很大的进步，最重要的是安全稳定、降低学习成本、提高开发效率。

# JeeSite 能为你做什么

## JeeSite 4.0 新特性

<https://my.oschina.net/thinkgem/blog/913777>

## JeeSite 4.0 持久层介绍

<https://my.oschina.net/thinkgem/blog/1503611>

## JeeSite 4.0 业务层介绍

<https://my.oschina.net/thinkgem/blog/1538766>

## JeeSite 4.0 MVC层及前端组件介绍

<https://my.oschina.net/thinkgem/blog/1561129>

## JeeSite 4.0 功能模块划分

<https://my.oschina.net/thinkgem/blog/1609852>

# 快速体验部署运行

1. 已具备环境：JDK 1.8、Maven 3.3、MySQL 5.7
2. 打开 /web/src/main/resources/jeesite.yml 文件，配置JDBC连接
3. 执行 /web/bin/init-data.bat 初始化数据库
4. 执行 /web/bin/run-tomcat.bat 启动服务
5. 浏览器访问 <http://127.0.0.1:8980/js/>  账号 system 密码 admin

# Eclipse中强制更新依赖jar包

在web项目上右键，选择菜单 -> Maven -> Update Project... -> 点击 Select All 按钮 -> 选择 Force Update of Snapshots/Releases 复选框 -> 点击OK按钮


# Git 全局设置技巧

```
1、提交检出均不转换换行符

git config --global core.autocrlf false

2、拒绝提交包含混合换行符的文件

git config --global core.safecrlf true
```