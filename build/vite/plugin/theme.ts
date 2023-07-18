/**
 * Vite plugin for website theme color switching
 * https://gitee.com/thinkgem/vite-plugin-theme-vite3
 */
import type { PluginOption } from 'vite';
import {
  viteThemePlugin,
  antdDarkThemePlugin,
  mixLighten,
  mixDarken,
  tinycolor,
} from 'vite-plugin-theme-vite3';
import { getThemeColors, generateColors } from '../../config/themeConfig';
import { generateModifyVars } from '../../generate/generateModifyVars';

export function configThemePlugin(isBuild: boolean): PluginOption[] {
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor,
  });
  // 抽取出viteThemePlugin插件，下方会根据不同环境设置enforce
  const vite_theme_plugin = viteThemePlugin({
    resolveSelector: (s) => {
      s = s.trim();
      if (s.includes('const __vite__css = "')) {
        return s.replace(/(.*)const __vite__css = "/g, '');
      }
      return s;
    },
    colorVariables: [...getThemeColors(), ...colors],
  });
  ((vite_theme_plugin || []) as any[]).forEach(function (item) {
    if ('vite:theme' === item.name) {
      if (isBuild) {
        delete item.enforce;
      }
    }
  });
  const plugin = [
    vite_theme_plugin,
    antdDarkThemePlugin({
      darkModifyVars: {
        ...generateModifyVars(true),
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
