/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { UserConfig, ConfigEnv } from 'vite';
import { defineConfig, loadEnv } from 'vite';
import { readPackageJSON } from 'pkg-types';
import {
  createBuildOptions,
  createCSSOptions,
  createDefineOptions,
  createEsBuildOptions,
  createServerOptions,
  createVitePlugins,
  wrapperEnv,
} from '../common/build';
import path from 'path';

export default defineConfig(async ({ command, mode }: ConfigEnv) => {
  const root = process.cwd();
  const isBuild = command === 'build';
  const viteEnv = wrapperEnv(loadEnv(mode, root));
  const pkg = await readPackageJSON(root);
  const config: UserConfig = {
    root,
    base: viteEnv.VITE_PUBLIC_PATH,
    define: createDefineOptions(pkg),
    plugins: createVitePlugins(isBuild, viteEnv, pkg),
    server: createServerOptions(viteEnv),
    esbuild: createEsBuildOptions(mode),
    build: createBuildOptions(viteEnv),
    css: createCSSOptions(),
    // resolve: {
    //   alias: {
    //     '@jeesite/web': path.resolve(__dirname, './'),
    //   },
    // },
  };
  return config;
});
