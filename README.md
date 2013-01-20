#JeeSite 企业信息管理系统基础框架
=======

##简介

JeeSite是一个 **开源的企业信息管理系统** 基础框架。主要定位于“企业信息管理”领域，可用作企业信息管理类系统、网站后台管理类系统等。JeeSite是非常强调开发的高效性、健壮性和安全性的。

JeeSite是轻量级的，简单易学，本框架以Spring Framework为核心、Spring MVC作为模型视图控制器、Spring Data JPA + Hibernate作为数据库操作层，此组合是Java界业内最经典、最优的搭配组合。前端界面风格采用了结构简单、性能优良、页面精致的 Twitter Bootstrap作为前端展示框架。

JeeSite **已内置** 一系列企业信息管理系统的基础功能，目前包括两大模块，系统管理（SYS）模块和内容管理（CMS）模块。系统管理模块，包括企业组织架构（用户管理、部门管理、区域管理）、菜单管理、角色权限管理、字典管理等功能；内容管理模块，包括内容管理（文章、链接），栏目管理、站点管理、公共留言、文件管理、前端网站展示等功能。

JeeSite提供了常用工具进行封装，包括日志工具、缓存工具、服务器端验证、数据字典、当前组织机构数据（用户、区域、部门）以及其它常用小工具等。另外还提供一个基于本基础框架的 **代码生成器** ，为你生成基本模块代码，如果你使用了JeeSite基础框架，就可以很快速开发出优秀的信息管理系统。

##为何选择

1. 使用Apache License 2.0协议，源代码完全开源，无商业限制。
2. 使用目前最主流的J2EE开发框架，简单易学，学习成本低。
3. 数据库无限制，支持MySql、Oracle、SQL Server、H2等数据库
4. 模块化设计，层次结构清晰。内置一系列企业信息管理的基础功能。
5. 权限控制精密细致，对所有管理链接都进行权限验证，可控制到按钮。
6. 提供基本功能模块的源代码生成器，提高开发效率及质量。
7. 提供常用工具类封装，日志、缓存、验证、字典、组织机构等，常用标签（taglib），获取当前组织机构、字典等数据。
8. 完全兼容目前最流行浏览器（IE6、IE7+、Firefox、Chrome）。


##使用技术

1、Services相关

* Core Framework：Spring Framework 3.1。
* Security Framework：Apache Shiro 1.2。

2、Web相关

* MVC Framework：SpringMVC 3.1。
* Layout Decoration：SiteMesh 2.4。
* JavaScript Library：JQuery 1.7。
* CSS Framework：Twitter Bootstrap 2.0.4。
* JavaScript/CSS Compressor：YUI Compressor 2.4。
* Front Validation：JQuery Validation Plugin 1.9。

3、Database相关

* ORM Framework：Spring-Data-JPA 1.2、Hibernate 4.1。
* Connection Pool：BoneCP 0.7
* Bean Validation：Hibernate Validation 4.3.0。
* Cache：Ehcache 2.6。

4、Tools 相关

* Commons：Apache Commons
* JSON Mapper：Jackson 2.1
* Bean Mapper：Dozer 5.3.2
* Full-text search：Hibernate Search 4.2（Apache Lucene 3.6）、IK Analyzer 2012_u6中文分词
* Log Manager：Log4j 1.2

##安全考虑

1. 开发语言：系统采用Java 语言开发，具有卓越的通用性、高效性、平台移植性和安全性。
2. 分层设计：（数据库层，数据访问层，业务逻辑层，展示层）层次清楚，低耦合，各层必须通过接口才能接入并进行参数校验（如：在展示层不可直接操作数据库），保证数据操作的安全。
3. 双重验证：用户表单提交双验证：包括服务器端验证及客户端验证，防止用户通过浏览器恶意修改（如不可写文本域、隐藏变量篡改、上传非法文件等），跳过客户端验证操作数据库。
4. 安全编码：用户表单提交所有数据，在服务器端都进行安全编码，防止用户提交非法脚本及SQL注入获取敏感数据等，确保数据安全。
5. 密码加密：登录用户密码进行SHA1散列加密，此加密方法是不可逆的。保证密文泄露后的安全问题。
6. 强制访问：系统对所有管理端链接都进行用户身份权限验证，防止用户

##更多文档

* [JeeSite介绍演示.docx](https://github.com/thinkgem/jeesite/raw/master/doc/JeeSite%E4%BB%8B%E7%BB%8D%E6%BC%94%E7%A4%BA.docx)
* [JeeSite开发手册.docx](https://github.com/thinkgem/jeesite/raw/master/doc/JeeSite%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C.docx)
