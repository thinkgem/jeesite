/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import visualizer from 'rollup-plugin-visualizer';
import { PluginOption } from 'vite';

export function isReportMode(): boolean {
  return process.env.REPORT === 'true';
}

export function configVisualizerPlugin(): PluginOption {
  if (!isReportMode()) {
    return [];
  }
  return (visualizer as any).default({
    filename: './node_modules/.cache/visualizer/stats.html',
    open: true,
    gzipSize: true,
    brotliSize: true,
  });
}
