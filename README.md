

<p align="center">
 <img alt="JeeSite" src="https://jeesite.com/assets/images/logo.png" width="120" height="120" style="margin-bottom: 10px;">
</p>
<h3 align="center" style="margin:30px 0 30px;font-weight:bold;font-size:30px;">JeeSite Vue3 前端框架</h3>
<p align="center">
 <a href="https://v3.cn.vuejs.org/" target="__blank"><img alt="TypeScript-Vue3" src="https://img.shields.io/badge/TypeScript-Vue3-green.svg"></a>
 <a href="https://www.antdv.com/" target="__blank"><img alt="Ant Design Vue-4.0" src="https://img.shields.io/badge/Ant Design Vue-4.0-blue.svg"></a>
 <a href="https://jeesite.com" target="__blank"><img alt="JeeSite-Vue" src="https://img.shields.io/badge/JeeSite-5.6-blue.svg"></a>
 <a href="https://gitee.com/thinkgem/jeesite-vue/stargazers" target="__blank"><img alt="star" src="https://gitee.com/thinkgem/jeesite-vue/badge/star.svg?theme=dark"></a>
 <a href="https://gitee.com/thinkgem/jeesite-vue/members" target="__blank"><img alt="fork" src="https://gitee.com/thinkgem/jeesite-vue/badge/fork.svg?theme=dark"></a>
</p>

## 技术交流

* 交流 QQ 群（千人大群）：`127515876`、`209330483`、`223507718`、`709534275`、`730390092`、`1373527`、`183903863(外包)`
* 码云Gitee：<https://gitee.com/thinkgem/jeesite-vue>
* GitHub：<https://github.com/thinkgem/jeesite-vue>
* 作者博客：<https://my.oschina.net/thinkgem>
* **帮助文档：**<http://docs.jeesite.com>
* 官方网站：<http://jeesite.com>
* 问题反馈：<http://jeesite.net> [【新手必读】](https://gitee.com/thinkgem/jeesite-vue/issues/I18ARR)
* 关注微信公众号，了解最新动态：

![JeeSite微信公众号](https://images.gitee.com/uploads/images/2020/0727/091951_a3ab258c_6732.jpeg "JeeSite微信公众号")

## 框架介绍

基于 Vue3、Vite、Ant-Design-Vue、TypeScript、Vue Vben Admin，最先进的技术栈，让初学者能够更快的入门并投入到团队开发中去。包括模块如：组织机构、角色用户、菜单授权、数据权限、系统参数等。强大的组件封装，数据驱动视图。为微小中大型项目的开发，提供现成的开箱解决方案及丰富的示例。

在 Vben Admin 基础上做的改进：

* 更精致的界面细节优化改进，非常适合信息化管理后台
* 主题风格改进，不同的布局风格，菜单及权限体验优化
* 顶部菜单、分隔菜单、混合菜单的活动状态激活和加载优化改进
* 树表支持异步的封装，提升展开折叠性能，支持按层次展开折叠树表
* 树结构新增快捷刷新、动态生成树、层次独立和不独立的数据返回兼容
* 增加左树右表功能展示，可折叠左树，树组件增加默认图标
* 表单组件适应各种数据格式来源，特别是多选字符串到数组的互转兼容
* 表单新增各种便捷属性和表单组件，下拉框和树选择支持标签名回显
* 表单组件，改进折叠表单 Action 的算法，智能化布局
* 表格组件，Action 更多，支持横向显示操作，更方便
* 表格组件，子表编辑改进，表格列排序和重置改进优化
* 新增字典组件，支持展示到表格列、表单组件下拉框单选框等
* 字典标签支持 Tag、Badge、自定义 class、style 等，显示风格
* 更方便的支持 Tab 页面的缓存，切换页签的时候不重载页面内容
* Tab 页签界面美化、图标显示、任何标签上右键，可快速刷新等等
* 全局 Axios 改进，兼容各种数据格式，超时消息提醒改进
* 功能权限鉴权改进，并兼容本地路由和后台路由同时使用
* 等等各种细节改进，体验优化，黑暗布局细节优化
* Vue端完全开源，用上你就会爱上，实在太方便了

## 设计特点

定义众多组件，非常贴心的组件属性及小功能，符合 JeeSite 以往的设计思想，列表和表单以数据驱动视图，极大简化了业务功能开发，注释分解详见本页最下【源码解析】

为什么做数据驱动视图？前端向下兼容一直是最大的问题，有了一套相应的标准，会对框架升级帮助很大。比如你可以非常小的成本，业务代码改动非常小的情况下，去升级前端；数据驱动视图可以为未来自定义拖拽表单做更好的铺垫，数据存储结构更清晰化，更利于维护。

提示：请仔细阅读源码解析，表单视图和列表视图上的注释哦，复杂表单可以多表单联合使用。

## 演示地址

1. 地址：<http://vue.jeesite.com/>

## 学习准备

- [VSCode](https://code.visualstudio.com/) - 推荐 IDE 集成开发工具
- [Node.js 18](https://nodejs.org/dist/latest-v18.x/) 和 [git](https://git-scm.com/) - 开发环境
- [Vite](https://vitejs.dev/) - 熟悉 Vite 特性
- [Vue-v3](https://cn.vuejs.org/) - 熟悉 Vue 基础语法
- [TypeScript](https://www.typescriptlang.org/) - 熟悉 TS 基本语法
- [ES6+](http://es6.ruanyifeng.com/) - 熟悉 ES6 基本语法
- [Vue-Router-v4](https://next.router.vuejs.org/) - 熟悉 vue-router 基本使用
- [Vue-Vben-Admin](https://jeesite.com/front/vben-admin/) - 熟悉 UI 及表单列表及常用组件使用
- [Ant-Design-Vue](https://antdv.com/components/overview-cn/) - 熟悉 UI 基本使用

## 安装使用

- 如果没有安装 Node.js 18+，下载地址：<https://nodejs.org>

```bash
# 验证
node -v
```

- 如果没有安装 Yarn 执行安装（要求 Yarn v1.x）

```bash
npm i -g yarn
# 验证
yarn -v
```

- 获取代码

```bash
git clone https://gitee.com/thinkgem/jeesite-vue.git
cd jeesite-vue
```
注意：不要放到中文或带空格的目录下。

- 安装依赖

```bash
yarn config set registry https://registry.npmmirror.com
yarn install
```

- 开发环境运行访问（方式一）

```bash
yarn serve
```
开发环境会加载文件较多，便于调试，请耐心等待。

- 编译打包后运行访问（方式二）

```bash
yarn preview
```
编译打包后，会整合这些文件，所以访问性能会大大提高，生产环境可以开启 gzip

- 打包发布程序

```bash
yarn build
```
打包完成后，会在根目录生成 dist 文件夹，发布 nginx。

详见文档：<https://jeesite.com/docs/vue-install-deploy/#部署到正式服务器>

### 后端服务

- 安装后台服务 [JeeSite v5.x](https://gitee.com/thinkgem/jeesite4/tree/v5.6/)
- 打开 [.env.development](https://jeesite.com/docs/vue-settings/#env-development-详解) 文件，修改后台接口：

```bash
# 代理设置，可配置多个，不能换行，格式：[访问接口的根路径, 代理地址, 是否保持Host头]
# VITE_PROXY = [["/js","https://vue.jeesite.com/js",true]]
VITE_PROXY = [["/js","http://127.0.0.1:8980/js",false]]

# 访问接口的根路径（例如：https://vue.jeesite.com）
VITE_GLOB_API_URL = 

# 访问接口的前缀，在根路径之后
VITE_GLOB_API_URL_PREFIX = /js
```

### 如果您使用的 VSCode 的话，推荐安装以下插件：

* [Iconify IntelliSense](https://marketplace.visualstudio.com/items?itemName=antfu.iconify) - Iconify 图标插件
* [windicss IntelliSense](https://marketplace.visualstudio.com/items?itemName=voorjaar.windicss-intellisense) - windicss 提示插件
* [I18n-ally](https://marketplace.visualstudio.com/items?itemName=Lokalise.i18n-ally) - i18n 插件
* [Volar](https://marketplace.visualstudio.com/items?itemName=johnsoncodehk.volar) - Vue3 开发必备（Vetur禁用）
* [ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint) - 脚本代码检查
* [Prettier](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) - 代码格式化
* [Stylelint](https://marketplace.visualstudio.com/items?itemName=stylelint.vscode-stylelint) - CSS 格式化
* [DotENV](https://marketplace.visualstudio.com/items?itemName=mikestead.dotenv) - .env 文件高亮

## 常见问题

* Vue 版本的浏览器支持情况：支持所有现代浏览器，Vue3 已不再支持 IE 浏览器。
* 为什么使用抽屉作为表单组件，因为抽屉空间更大，可以展示更多内容，且操作更友好。
* 如何将表单抽屉改为弹窗，替换 list 和 form 页面的 Drawer 为 Modal 即可，V5.6增加了路由表单和弹窗表单的代码生成。
* 打不开代码生成工具怎么办？提示 404，请检查 .env.development 中的代理配置 VITE_PROXY 最后一个参数（是否保持Host头），本地服务 127.0.0.1 应设置为 false，远程服务设置为 true。

## 授权许可协议条款

1. 本项目为商业内部源码，不可向第三方泄漏，不可开源，不可分享，还须遵守以下条款。
2. 不得将本软件应用于危害国家安全、荣誉和利益的行为，不能以任何形式用于非法为目的的行为。
3. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议、版权声明和其他原作者
   规定需要包含的说明（请尊重原作者的著作权，不要删除或修改文件中的`Copyright`和`@author`信息）
   更不要，全局替换源代码中的 jeesite 或 ThinkGem 等字样，否则你将违反本协议条款承担责任。
4. 基于本软件的作品，只能使用 JeeSite5 作为后台服务，除外情况不可商用且不允许二次分发或开源。
5. 您若套用本软件的一些代码或功能参考，请保留源文件中的版权和作者，需要在您的软件介绍明显位置
   说明出处，举例：本软件基于 JeeSite Vue 快速开发平台，并附带链接：http://jeesite.com
6. 任何基于本软件而产生的一切法律纠纷和责任，均于我司无关。
7. 如果你对本软件有改进，希望可以贡献给我们，共同进步。
8. 本项目已申请软件著作权，请尊重开源，感谢阅读。

## 专业版增加的功能

1. 主题标签页的三种风格自由切换
2. 业务流程、流程设计、流程办理
3. 文件管理、上传秒传、文件预览
4. 高级折叠表单和个性化本地存储
5. 表格个性化设置参数本地存储
6. 租户管理功能、租户切换
7. 消息推送、消息提醒
8. 语言国际化、本地化
9. 更多功能详见文档
