/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { BuildOptions } from 'vite';

// 兼容 Chrome 内核最低版本的浏览器，如 360、QQ 浏览器
export const legacyTargets = ['Chrome 80'];
export const viteCssTarget = 'chrome80';

export function createBuildOptions(viteEnv: ViteEnv): BuildOptions {
  const timestamp = new Date().getTime();
  return {
    // target: 'es2015',
    cssTarget: viteCssTarget,
    outDir: viteEnv.VITE_OUTPUT_DIR ?? 'dist',
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
        // manualChunks: {
        //   vue: ['vue', 'vue-router', 'pinia'],
        //   antdv: ['ant-design-vue', '@ant-design/icons-vue'],
        // },
      },
    },
  };
}
