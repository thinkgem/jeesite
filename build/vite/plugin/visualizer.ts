/**
 * Package file volume analysis
 */
import visualizer from 'rollup-plugin-visualizer';
import { isReportMode } from '../../utils';
import { PluginOption } from 'vite';

export function configVisualizerPlugin() {
  if (isReportMode()) {
    return (visualizer as any).default({
      filename: './node_modules/.cache/visualizer/stats.html',
      open: true,
      gzipSize: true,
      brotliSize: true,
    }) as PluginOption;
  }
  return [];
}
