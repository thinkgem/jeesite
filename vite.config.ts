/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { UserConfig, ConfigEnv } from 'vite';
import { AliasOptions, defineConfig, loadEnv, ResolveOptions } from 'vite';
import { PackageJson, readPackageJSON } from 'pkg-types';
import { resolve } from 'node:path';
import { generateModifyVars } from './build/generate/modifyVars';
import { createProxy } from './build/vite/proxy';
import { wrapperEnv } from './build/utils';
import { createVitePlugins } from './build/vite/plugin';
import { OUTPUT_DIR } from './build/constant';
import dayjs from 'dayjs';

export default defineConfig(async ({ command, mode }: ConfigEnv) => {
  const root = process.cwd();
  const isBuild = command === 'build';
  const env = loadEnv(mode, root);
  const viteEnv = wrapperEnv(env);
  const timestamp = new Date().getTime();
  const pkg = await readPackageJSON(root);

  const config: UserConfig = {
    root,
    base: viteEnv.VITE_PUBLIC_PATH,
    define: createDefineOptions(pkg),
    plugins: createVitePlugins(isBuild, viteEnv, pkg),
    resolve: createResolveOptions(root),
    server: {
      https: false as any,
      open: false,
      host: true,
      port: viteEnv.VITE_PORT,
      proxy: createProxy(viteEnv.VITE_PROXY),
      warmup: {
        clientFiles: ['./index.html', './src/{views,components}/*'],
      },
    },
    esbuild: {
      // pure: viteEnv.VITE_DROP_CONSOLE ? ['console.log', 'debugger'] : [],
      drop: mode === 'production' ? ['console', 'debugger'] : [],
    },
    build: {
      // target: 'es2015',
      // 兼容 Chrome 内核比较低的浏览器，如 360、QQ 浏览器
      cssTarget: 'chrome80',
      outDir: OUTPUT_DIR,
      // 启用 terser 缩小器，当设置 terserOptions 时才会有效
      // minify: 'terser',
      // terserOptions: {
      //   compress: {
      //     keep_infinity: true,
      //     drop_console: viteEnv.VITE_DROP_CONSOLE,
      //   },
      // },
      // 禁用报告压缩块大小，可以稍微提高构建速度
      reportCompressedSize: false,
      chunkSizeWarningLimit: 9000,
      rollupOptions: {
        // maxParallelFileOps: 3,
        output: {
          entryFileNames: `assets/[name]-[hash]-${timestamp}.js`,
          manualChunks: {
            vue: ['vue', 'vue-router', 'pinia'],
            antd: ['ant-design-vue', '@ant-design/icons-vue'],
            icon: ['src/components/Icon/index.ts'],
            svg: ['virtual:svg-icons-register'],
          },
        },
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
    css: {
      preprocessorOptions: {
        less: {
          modifyVars: generateModifyVars(),
          javascriptEnabled: true,
        },
      },
    },
  };
  return config;
});

function createResolveOptions(root: string) {
  const pathResolve = (pathname: string) => resolve(root, '.', pathname);

  const resolveData: ResolveOptions & { alias?: AliasOptions } = {
    alias: [
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js',
      },
      {
        find: /\/@\//, // /@/xxxx => src/xxxx
        replacement: pathResolve('src') + '/',
      },
      {
        find: /\/#\//, // /#/xxxx => types/xxxx
        replacement: pathResolve('types') + '/',
      },
    ],
  };
  return resolveData;
}

function createDefineOptions(pkg: PackageJson) {
  try {
    const { dependencies, devDependencies, name, version } = pkg;
    const __APP_INFO__ = {
      pkg: { dependencies, devDependencies, name, version },
      lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
    };

    return {
      // __INTLIFY_PROD_DEVTOOLS__: 'false',
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    };
  } catch (error) {
    console.log('createDefine', error);
    return {};
  }
}
