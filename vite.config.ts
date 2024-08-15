/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { UserConfig, ConfigEnv } from 'vite';
import { defineConfig, loadEnv } from 'vite';
import { readPackageJSON } from 'pkg-types';
import { resolve } from 'node:path';
import { createDefineOptions } from './build/options/define';
import { createEsBuildOptions } from './build/options/esBuild';
import { createBuildOptions } from './build/options/build';
import { createVitePlugins } from './build/plugins';
import { createServerOptions } from './build/options/server';
import { createCSSOptions } from './build/options/css';
import { wrapperEnv } from './build/config';

export default defineConfig(async ({ command, mode }: ConfigEnv) => {
  const root = process.cwd();
  const isBuild = command === 'build';
  const viteEnv = wrapperEnv(loadEnv(mode, root));
  const pkg = await readPackageJSON(root);
  const pathResolve = (pathname: string) => resolve(root, '.', pathname);
  const config: UserConfig = {
    root,
    base: viteEnv.VITE_PUBLIC_PATH,
    define: createDefineOptions(pkg),
    plugins: createVitePlugins(isBuild, viteEnv, pkg),
    server: createServerOptions(viteEnv),
    esbuild: createEsBuildOptions(mode),
    build: createBuildOptions(viteEnv),
    css: createCSSOptions(),
    resolve: {
      alias: {
        '/@/': pathResolve('src') + '/',
        '/#/': pathResolve('types') + '/',
      },
    },
    optimizeDeps: {
      include: [
        'qrcode',
        'echarts/core',
        'echarts/charts',
        'echarts/components',
        'echarts/renderers',
        '@iconify/iconify',
        'ant-design-vue/es/locale/zh_CN',
        'ant-design-vue/es/locale/en_US',
      ],
    },
  };
  return config;
});
