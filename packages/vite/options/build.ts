/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { BuildOptions } from 'vite';

// 现代浏览器支持列表 https://cn.vitejs.dev/config/build-options.html#build-target
export const viteTarget = ['chrome107', 'edge107', 'firefox104', 'safari16'];
// 低版本浏览器支持列表，VITE_LEGACY 参数开启时有效 https://www.npmjs.com/package/@vitejs/plugin-legacy
export const legacyTargets = ['chrome>=87', 'edge>=88', 'firefox>=78', 'safari>=14'];

// 创建 Vite 构建配置选项
export function createBuildOptions(viteEnv: ViteEnv): BuildOptions {
  const timestamp = new Date().getTime();
  return {
    target: viteTarget,
    cssTarget: viteTarget,
    outDir: viteEnv.VITE_OUTPUT_DIR ?? 'dist',
    reportCompressedSize: false,
    chunkSizeWarningLimit: 5000,
    cssCodeSplit: true,
    rolldownOptions: {
      output: {
        // strictExecutionOrder: true,
        entryFileNames: `assets/[name]-[hash]-${timestamp}.js`,
        chunkFileNames: `assets/[name]-[hash]-${timestamp}.js`,
        assetFileNames: `assets/[name]-[hash]-${timestamp}.[ext]`,
        comments: false,
        codeSplitting: {
          minSize: 50_000,
          groups: [
            {
              name: 'vue',
              test: /node_modules[\\/](vue|vue-router|@vue|vue-demi|pinia)[\\/]/,
              priority: 900,
            },
            {
              name: 'ant',
              test: /node_modules[\\/](antdv-next|@antdv-next|@ant-design)[\\/]/,
              priority: 800,
            },
            {
              name: 'utils',
              test: /node_modules[\\/](lodash-es|dayjs|axios|qs|path-to-regexp)[\\/]/,
              priority: 700,
            },
            {
              name: 'layouts',
              test: /layouts[\\/](default|iframe|page|views[\\/](desktop|lock|login))[\\/]/,
              priority: 600,
            },
            {
              name: 'monaco-editor',
              test: /node_modules[\\/]monaco-editor[\\/]/,
              priority: 400,
            },
            {
              name: 'markdown',
              test: /node_modules[\\/](markdown-it|@traptitech|markdown-it-link-attributes|highlight\.js|katex)[\\/]/,
              priority: 500,
            },
          ],
        },
        minify: {
          compress: { dropConsole: viteEnv.VITE_DROP_CONSOLE, dropDebugger: true },
        },
      },
    },
  };
}
