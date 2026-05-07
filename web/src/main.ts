/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import 'virtual:uno.css';
import 'antdv-next/dist/reset.css';
import '@jeesite/core/design/index.less';

import App from './App.vue';
import { createApp } from 'vue';
import { isDevMode } from '@jeesite/core/utils/env';
import { registerGlobComp } from '@jeesite/core/components/registerGlobComp';
import { initAppConfigStore } from '@jeesite/core/logics/initAppConfig';
import { setupErrorHandle } from '@jeesite/core/logics/error-handle';
import { setupGlobDirectives } from '@jeesite/core/directives';
import { setupI18n } from '@jeesite/core/locales/setupI18n';
import { setupRouter, router } from '@jeesite/core/router';
import { setupRouterGuard } from '@jeesite/core/router/guard';
import { setupStore } from '@jeesite/core/store';
import { setupDForm } from '@jeesite/dfm';

async function bootstrap() {
  const app = createApp(App);

  // 状态管理
  setupStore(app);

  // 系统配置初始化（主题、布局等）
  initAppConfigStore();

  // 全局组件注册（仅 Input 和 Button，体积小）
  registerGlobComp(app);

  // 国际化配置（await 确保首屏文案正确渲染）
  await setupI18n(app);

  // 路由配置
  setupRouter(app);

  // 路由守卫（权限控制等）
  setupRouterGuard(router);

  // 全局指令（权限、加载等）
  setupGlobDirectives(app);

  // 全局错误处理
  setupErrorHandle(app);

  // 先挂载应用，让用户尽快看到页面
  app.mount('#app');

  // 动态表单设计器（延迟加载）
  setupDForm();
}

// 仅开发模式显示
if (!isDevMode()) {
  console.log(
    '%c JeeSite %c快速开发平台 \n%c 用心去做我们的快速开发平台，用心去帮助我们的客户！让您用着省心的平台。\n 您的一个关注，就是对我们最大的支持：  https://gitee.com/thinkgem/jeesite-vue  （请点 star 收藏我们）\n 免费 QQ 技术交流群： 127515876、209330483、223507718、709534275、730390092、1373527、183903863(外包) \n 免费 微信 技术交流群： http://s.jeesite.com 如果加不上，可添加 微信 jeesitex 邀请您进群。%c\n ',
    'font-family:"Helvetica Neue",Helvetica,Arial,sans-serif;font-size:39px;color:#0f87e8;-webkit-text-fill-color:#0f87e8;-webkit-text-stroke:1px #0f87e8;',
    'font-size:24px;color:#aaa;',
    'font-size:14px;color:#888;',
    'font-size:12px;',
  );
}
bootstrap().then();
