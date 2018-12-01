## 引言

JeeSite Spring Cloud 是基于 Spring Cloud Finchley 的一个分布式系统套件的整合。

**特点：用经典开发模式，开发分布式应用，两个字【简单】，一个字【快】。**

## 技术选型

* 分布式系统套件版本：Spring Cloud Finchley
* 服务治理注册与发现：Spring Cloud Netflix Eureka
* 服务容错保护限流降级：Spring Cloud Netflix Hystrix
* 分布式统一配置中心：Spring Cloud Config
* 网关路由代理调用：Spring Cloud Gateway
* 声明式服务调用：Spring Cloud OpenFeign

## 子项目介绍

* 服务治理：jeesite-cloud-eureka ： <http://127.0.0.1:8970>
* 配置中心：jeesite-cloud-config ： <http://127.0.0.1:8971/project/default>
* 网关路由：jeesite-cloud-gateway ： <http://127.0.0.1:8980/js/a/login>
* 核心项目：jeesite-cloud-module-core ： <http://127.0.0.1:8981/js>
* 测试模块1：
    - 模块1主项目：jeesite-cloud-module-test1 ： <http://127.0.0.1:8982/js>
    - 模块1客户端项目（提供其它模块调用）：jeesite-cloud-module-test1-client
* 测试模块2：
    - 模块2主项目：jeesite-cloud-module-test2 ： <http://127.0.0.1:8983/js>
    - 模块2客户端项目（提供其它模块调用）：jeesite-cloud-module-test2-client

## 快速运行

* 初始化数据库（下载最新的mysql脚本）：
     https://gitee.com/thinkgem/jeesite4/attach_files
* 配置 `/jeesite-cloud-config/../cloud-config/application.yml`
     分布式统一配置文件 JDBC 和 Redis 等信息。
* 按顺序运行以下启动类的main方法（启动完成一个再启下一个）：
    - /jeesite-cloud-eureka/../EurekaApplication.java
    - /jeesite-cloud-config/../ConfigApplication.java
    - /jeesite-cloud-gateway/../GatewayApplication.java
    - /jeesite-cloud-module-core/../CoreApplication.java
    - /jeesite-cloud-module-test1/../Test1Application.java
    - /jeesite-cloud-module-test2/../Test2Application.java
* 以上都启动成功后，浏览器访问网关项目地址即可：
    - 访问地址：<http://127.0.0.1:8980/js>   system   admin
    - 若访问报错，请再等待一会，可能服务未完全启动完成

## 调用实例演示

### 网关代理模块调用

* 代理 test1 模块（单表）：<http://127.0.0.1:8980/js/a/test1/testData/list>
    - 控制器位置：jeesite-cloud-module-test1/../web/TestData1Controller.java
* 代理 test2 模块（树表）：<http://127.0.0.1:8980/js/a/test2/testTree/list>
    - 控制器位置：jeesite-cloud-module-test2/../web/TestTree2Controller.java

### 模块之间互相调用

* test2 模块调用 test1 模块（单表）：<http://127.0.0.1:8980/js/a/test2/testData/list>
    - 服务消费者位置：jeesite-cloud-module-test2/../web/TestData2Controller.java
    - 服务提供者位置：/jeesite-cloud-module-test1/../service/TestDataService.java
* test1 模块调用 test2 模块（树表）：<http://127.0.0.1:8980/js/a/test1/testTree/list>
    - 服务消费者位置：jeesite-cloud-module-test1/../web/TestTree1Controller.java
    - 服务提供者位置：/jeesite-cloud-module-test2/../service/TestTreeService.java

## 授权协议声明

1. 您可以免费使用、修改和衍生代码，但不允许修改后和衍生的代码做为闭源软件发布。
2. 修改后和衍生的代码必须也按照当前协议进行流通，对修改后和衍生的代码必须向社会公开。
3. 如果您修改了代码，需要在被修改的文件中进行说明，并遵守代码格式规范，帮助他人更好的理解您的用意。
4. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者规定
    需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`@author`信息）。
5. 本项目仅用于学习和交流，未得到官方授权不得用于商业用途。

### 获得技术服务支持：<http://jeesite4.mydoc.io/?t=267685>

* 随着JeeSite发展，我们也需要有相应的资金支持才能持续的开发和维护。支持我们，您可以得到一些回报，我们有了这些也会把开源事业做的更好，请给我们一些动力吧，谢谢您的支持！

# 技术交流方式

* QQ 群号：`127515876`、`209330483`、`223507718`、`709534275`、`730390092`、`183903863(外包)`
* 问题反馈：<https://gitee.com/thinkgem/jeesite4-cloud/issues> 　[【新手必读】](http://www.dianbo.org/9238/stone/tiwendezhihui.htm)
* 码云Gitee：<https://gitee.com/thinkgem/jeesite4-cloud>
* GitHub：<https://github.com/thinkgem/jeesite4-cloud>
* 作者博客：<https://my.oschina.net/thinkgem>
* 官方网站：<http://jeesite.com>
* 官方论坛：<http://jeesite.net>
* 微信公众号：

![JeeSite4微信公众号](https://static.oschina.net/uploads/space/2018/0302/145133_OGZf_941661.jpg "JeeSite4微信公众号")

# Git 全局设置技巧

```
1、提交检出均不转换换行符

git config --global core.autocrlf false

2、拒绝提交包含混合换行符的文件

git config --global core.safecrlf true
```