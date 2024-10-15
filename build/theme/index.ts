/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
// import path from 'path';
import {
  viteThemePlugin,
  antdDarkThemePlugin,
  mixLighten,
  mixDarken,
  tinycolor,
} from 'vite-plugin-theme-vite3';
import { getThemeColors, generateColors } from './themeConfig';
import { generateModifyVars } from './modifyVars';

/**
 * Vite plugin for website theme color switching
 * https://gitee.com/thinkgem/vite-plugin-theme-vite3
 */
export function configThemePlugin(isBuild: boolean): PluginOption[] {
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor,
  });
  // console.log(
  //   [...getThemeColors(), ...colors]
  //     .map((e) => `<a style="background:${e}">${e}</a>`)
  //     .join('&nbsp;'),
  // );
  // 抽取出viteThemePlugin插件，下方会根据不同环境设置enforce
  const vite_theme_plugin = viteThemePlugin({
    resolveSelector: (s) => {
      s = s.trim();
      if (s.includes('const __vite__css = "')) {
        return s.replace(/(.*)const __vite__css = "/g, '');
      }
      return s;
      // switch (s) {
      //   case '.ant-steps-item-process .ant-steps-item-icon > .ant-steps-icon':
      //     return '.ant-steps-item-icon > .ant-steps-icon';
      //   case '.ant-radio-button-wrapper-checked:not(.ant-radio-button-wrapper-disabled)':
      //   case '.ant-radio-button-wrapper-checked:not(.ant-radio-button-wrapper-disabled):hover':
      //   case '.ant-radio-button-wrapper-checked:not(.ant-radio-button-wrapper-disabled):active':
      //   case '.ant-steps-item-icon > .ant-steps-icon':
      //   case '.ant-select-item-option-selected:not(.ant-select-item-option-disabled)':
      //   case '.ant-menu-item-selected':
      //   case '.ant-drawer .jeesite-basic-drawer .ant-drawer-body':
      //   case '.jeesite-basic-drawer-footer':
      //     return s;
      //   default:
      //     // 按钮被重新定制过，需要过滤掉class防止覆盖
      //     if (s.indexOf('.ant-btn') >= 0) {
      //       return s;
      //     }
      // }
      // return s.startsWith('[data-theme') ? s : `[data-theme] ${s}`;
    },
    colorVariables: [...getThemeColors(), ...colors],
  });
  //console.log('vite_theme_plugin:'+JSON.stringify(vite_theme_plugin));
  ((vite_theme_plugin || []) as any[]).forEach(function (item) {
    //对vite:theme插件特殊配置
    if ('vite:theme' === item.name) {
      //console.log(item);
      // 打包时去除enforce: "post"，vite 2.6.x适配，否则生成app-theme-style为空，
      // 因为 async transform(code, id) { 的code没有正确获取
      if (isBuild) {
        delete item.enforce;
      }
      //console.log(item);
    }
  });
  //console.log('vite_theme_plugin后:'+JSON.stringify(vite_theme_plugin));
  const plugin = [
    vite_theme_plugin,
    antdDarkThemePlugin({
      // preloadFiles: [
      //   // path.resolve(process.cwd(), 'node_modules/ant-design-vue/dist/antd.less'),
      //   // path.resolve(process.cwd(), 'node_modules/ant-design-vue/dist/antd.dark.less'),
      //   path.resolve(process.cwd(), 'src/design/index.less'),
      // ],
      // filter: (id) => (isBuild ? !id.endsWith('antd.less') : true),
      // extractCss: false,
      darkModifyVars: {
        ...generateModifyVars(true),
        // 'text-color': '#c9d1d9',
        // 'primary-1': 'rgb(255 255 255 / 8%)',
        // 'text-color-base': '#c9d1d9',
        // 'component-background': '#151515',
        // 'heading-color': 'rgb(255 255 255 / 65%)',
        // // black: '#0e1117',
        // // #8b949e
        // 'text-color-secondary': '#8b949e',
        // 'border-color-base': '#303030',
        // // 'border-color-split': '#30363d',
        // 'item-active-bg': '#111b26',
        // // 'tree-node-selected-bg': '#11263c',
        // 'alert-success-border-color': '#274916',
        // 'alert-success-bg-color': '#162312',
        // 'alert-success-icon-color': '#49aa19',
        // 'alert-info-border-color': '#153450',
        // 'alert-info-bg-color': '#111b26',
        // 'alert-info-icon-color': '#177ddc',
        // 'alert-warning-border-color': '#594214',
        // 'alert-warning-bg-color': '#2b2111',
        // 'alert-warning-icon-color': '#d89614',
        // 'alert-error-border-color': '#58181c',
        // 'alert-error-bg-color': '#2a1215',
        // 'alert-error-icon-color': '#a61d24',
        'content-bg': '#141414',
        'content-bg-striped': '#1e1e1e',
        'button-cancel-color': '#c9d1d9',
        'button-cancel-bg-color': '#2d2d2d',
        'button-cancel-border-color': '#303030',
        'button-cancel-hover-color': '#c9d1d9',
        'button-cancel-hover-bg-color': '#4a4a4a',
        'button-cancel-hover-border-color': '#4a4a4a',
        'header-light-bottom-border-color': '#303030',
      },
    }),
  ];
  return plugin as unknown as PluginOption[];
}
