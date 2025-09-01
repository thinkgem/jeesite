/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { BuildOptions } from 'vite';

// 现代浏览器支持列表 https://cn.vitejs.dev/config/build-options.html#build-target
export const viteTarget = ['chrome107', 'edge107', 'firefox104', 'safari16'];
// 低版本浏览器支持列表，VITE_LEGACY 参数开启时有效 https://www.npmjs.com/package/@vitejs/plugin-legacy
export const legacyTargets = ['chrome>=87', 'edge>=88', 'firefox>=78', 'safari>=14'];

export function createBuildOptions(viteEnv: ViteEnv): BuildOptions {
  const timestamp = new Date().getTime();
  return {
    target: viteTarget,
    cssTarget: viteTarget,
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
      maxParallelFileOps: 50,
      output: {
        entryFileNames: `assets/[name]-[hash]-${timestamp}.js`,
        experimentalMinChunkSize: 12288,
        // manualChunks: {
        //   vue: ['vue', 'vue-router'],
        //   antd: ['ant-design-vue', '@ant-design/icons-vue'],
        // },
      },
    },
  };
}
