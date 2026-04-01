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

export function createBuildOptions(viteEnv: ViteEnv): BuildOptions {
  const timestamp = new Date().getTime();
  return {
    target: viteTarget,
    cssTarget: viteTarget,
    outDir: viteEnv.VITE_OUTPUT_DIR ?? 'dist',
    // 禁用报告压缩块大小，可以稍微提高构建速度
    reportCompressedSize: false,
    chunkSizeWarningLimit: 9000,
    rolldownOptions: {
      output: {
        entryFileNames: `assets/[name]-[hash]-${timestamp}.js`,
        chunkFileNames: `assets/[name]-[hash]-${timestamp}.js`,
        comments: false,
        minify: {
          compress: { dropConsole: true, dropDebugger: true },
        },
      },
    },
  };
}
