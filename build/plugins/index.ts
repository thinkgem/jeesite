/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { PluginOption } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import vueSetupExtend from 'vite-plugin-vue-setup-extend';
import vitePluginCertificate from 'vite-plugin-mkcert';
import { appConfigPlugin } from '../config/appConfig';
import { configUnoCSSPlugin } from './unocss';
import { configHtmlPlugin } from './html';
import { configCompressPlugin } from './compress';
import { configVisualizerPlugin } from './visualizer';
import { configThemePlugin } from '../theme';
import { PackageJson } from 'pkg-types';
import { configLegacyPlugin } from './legacy';

export function createVitePlugins(isBuild: boolean, viteEnv: ViteEnv, pkg: PackageJson) {
  const vitePlugins: (PluginOption | PluginOption[])[] = [
    vue(),
    vueJsx(),
    vueSetupExtend(),
    vitePluginCertificate({
      source: 'coding',
    }),
  ];

  // app-config-plugin
  vitePlugins.push(appConfigPlugin(isBuild, viteEnv, pkg));

  // UnoCSS-vite-plugin
  vitePlugins.push(configUnoCSSPlugin());

  // vite-plugin-html
  vitePlugins.push(configHtmlPlugin(isBuild));

  // rollup-plugin-visualizer
  vitePlugins.push(configVisualizerPlugin());

  // vite-plugin-theme-vite3
  vitePlugins.push(configThemePlugin(isBuild));

  // rollup-plugin-gzip
  vitePlugins.push(configCompressPlugin(isBuild, viteEnv));

  // @vitejs/plugin-legacy
  vitePlugins.push(configLegacyPlugin(isBuild, viteEnv));

  return vitePlugins;
}
