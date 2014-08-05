# JeeSite 企业信息管理系统基础框架

## 框架简介

JeeSite 是一个 **开源的企业信息管理系统** 基础框架。主要定位于“企业信息管理”领域，可用作企业信息管理类系统、网站后台管理类系统等。JeeSite是非常强调开发的高效性、健壮性和安全性的。

JeeSite 是轻量级的，简单易学，本框架以Spring Framework为核心、Spring MVC作为模型视图控制器、Spring Data JPA + Hibernate作为数据库操作层，此组合是Java界业内最经典、最优的搭配组合。前端界面风格采用了结构简单、性能优良、页面精致的Twitter Bootstrap作为前端展示框架。

JeeSite 已内置一系列企业信息管理系统的基础功能，目前包括三大模块，系统管理（SYS）模块、内容管理（CMS）模块和在线办公（OA）模块。 **系统管理模块** ，包括企业组织架构（用户管理、机构管理、区域管理）、菜单管理、角色权限管理、字典管理等功能； **内容管理模块** ，包括内容管理（文章、链接），栏目管理、站点管理、公共留言、文件管理、前端网站展示等功能； **在线办公模块** ，提供简单的请假流程实例。

JeeSite 提供了常用工具进行封装，包括日志工具、缓存工具、服务器端验证、数据字典、当前组织机构数据（用户、机构、区域）以及其它常用小工具等。另外还提供一个基于本基础框架的 **代码生成器** ，为你生成基本模块代码，如果你使用了JeeSite基础框架，就可以很快速开发出优秀的信息管理系统。

## 为何选择

1. 使用 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议，源代码完全开源，无商业限制。
2. 使用目前最主流的J2EE开发框架，简单易学，学习成本低。
3. 数据库无限制，支持MySql、Oracle、SQL Server、H2等数据库。
4. 模块化设计，层次结构清晰。内置一系列企业信息管理的基础功能。
5. 操作权限控制精密细致，对所有管理链接都进行权限验证，可控制到按钮。
6. 数据权限控制精密细致，对指定数据集权限进行过滤，七种数据权限可供选择。
7. 提供基本功能模块的源代码生成器，提高开发效率及质量。
8. 提供常用工具类封装，日志、缓存、验证、字典、组织机构等，常用标签（taglib），获取当前组织机构、字典等数据。
9. 完全兼容目前最流行浏览器（IE6、IE7+、Firefox、Chrome）。
10. 提供目前最流行的Activit流程引擎实例。

## 技术选型

1、Services相关

* Core Framework：Spring Framework 3.2。
* Security Framework：Apache Shiro 1.2。
* Workflow Engine：Activit 5.12。

2、Web相关

* MVC Framework：SpringMVC 3.2。
* Layout Decoration：SiteMesh 2.4。
* JavaScript Library：JQuery 1.9。
* CSS Framework：Twitter Bootstrap 2.3.1。
* JavaScript/CSS Compressor：YUI Compressor 2.4。
* Front Validation：JQuery Validation Plugin 1.11。

3、Database相关

* ORM Framework：Spring-Data-JPA 1.3、Hibernate 4.1。
* Connection Pool：Alibaba Druid 0.2。
* Bean Validation：Hibernate Validation 5.0。
* Cache：Ehcache 2.6。

4、Tools 相关

* Commons：Apache Commons
* JSON Mapper：Jackson 2.1
* Bean Mapper：Dozer 5.3
* Office Tools: Apache POI 3.9
* Full-text search：Hibernate Search 4.2（Apache Lucene 3.6）、IK Analyzer 2012_u6中文分词
* Log Manager：Log4j 1.2

## 安全考虑

1. 开发语言：系统采用Java 语言开发，具有卓越的通用性、高效性、平台移植性和安全性。
2. 分层设计：（数据库层，数据访问层，业务逻辑层，展示层）层次清楚，低耦合，各层必须通过接口才能接入并进行参数校验（如：在展示层不可直接操作数据库），保证数据操作的安全。
3. 双重验证：用户表单提交双验证：包括服务器端验证及客户端验证，防止用户通过浏览器恶意修改（如不可写文本域、隐藏变量篡改、上传非法文件等），跳过客户端验证操作数据库。
4. 安全编码：用户表单提交所有数据，在服务器端都进行安全编码，防止用户提交非法脚本及SQL注入获取敏感数据等，确保数据安全。
5. 密码加密：登录用户密码进行SHA1散列加密，此加密方法是不可逆的。保证密文泄露后的安全问题。
6. 强制访问：系统对所有管理端链接都进行用户身份权限验证，防止用户

## 快速体验

### 在线体验

体验地址1（感谢刘杰提供）：<http://demo.jeesite.com:1234/jeesite> 

体验地址2（感谢devnote提供）：<http://www.devnote.cn/jeesite/a> 

用户名：thinkgem 或 admin    密码：admin

### 本地体验

1. 具备运行环境：JDK1.6、Maven3.0、MySql5。
2. 修改src\main\resources\application.properties文件中的数据库设置参数。
3. 根据修改参数创建对应MySql数据库（数据库编码：UTF-8）。
4. 运行bin\resresh-db\refresh-db.bat脚本，即可导入表结构及演示数据(linux操作系统：在控制台中切换至项目根目录，运行命令：mvn antrun:run -Prefresh-db)
5. 运行bin\jetty.bat，启动Web服务器（第一次运行，需要下载依赖jar包，请耐心等待）。
6. 最高管理员账号，用户名：thinkgem 密码：admin

## 更多文档

* [JeeSite介绍演示.docx](https://github.com/thinkgem/jeesite/raw/master/doc/JeeSite About.docx)
* [JeeSite开发手册.docx](https://github.com/thinkgem/jeesite/raw/master/doc/JeeSite Developer.docx)

## 如何交流、反馈、参与贡献？

* QQ Group：127515876
* E-mail：<mailto:thinkgem@163.com>
* Github：<https://github.com/thinkgem/jeesite>
* 开源中国：<http://git.oschina.net/thinkgem/jeesite>
* 官方网址：<http://jeesite.com>  论坛：<http://bbs.jeesite.com>
* 支持JeeSite：<http://jeesite.com/donation.html>

如果你想参与进来共同完善它或有更好的建议，请联系我吧(^_^)。

## 版权声明

本软件使用 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议，请严格遵照协议内容：

1. 需要给代码的用户一份Apache Licence。
2. 如果你修改了代码，需要在被修改的文件中说明。
3. **在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议，商标，专利声明和其他原来作者规定需要包含的说明。**
4. 如果再发布的产品中包含一个Notice文件，则在Notice文件中需要带有Apache Licence。你可以在Notice中增加自己的许可，但不可以表现为对Apache Licence构成更改。
3. Apache Licence也是对商业应用友好的许可。使用者也可以在需要的时候修改代码来满足需要并作为开源或商业产品发布/销售

## 未来开发计划

* 代码生成器：自动建立表结构、生成的mvc中包括属性字段，哪些可作为查询条件等等。
* 模块管理功能：可方便增减模块，如内容管理模块不需要，可直接启用或停用。
* 内容管理模块：文章评论、完善内容关键字、图片模型、专题功能、会员功能。
* 添加通用模块：公告功能、内部短消息、在线列表、短信和邮件群发功能。
* 公共工具封装：嵌入地图、生成报表、WebService
* 移动客户端：暂定为Android客户端开发
