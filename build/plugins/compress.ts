/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import compressPlugin from 'vite-plugin-compression';

/**
 * Used to package and output gzip. Note that this does not work properly in Vite, the specific reason is still being investigated
 * https://github.com/anncwb/vite-plugin-compression
 */
export function configCompressPlugin(isBuild: boolean, viteEnv: ViteEnv): PluginOption {
  if (!isBuild) {
    return [];
  }
  const plugins: PluginOption = [];
  const compress: 'gzip' | 'brotli' | 'none' = viteEnv.VITE_BUILD_COMPRESS;
  const deleteOriginFile = viteEnv.VITE_BUILD_COMPRESS_DELETE_ORIGIN_FILE || false;

  const compressList = compress.split(',');

  if (compressList.includes('gzip')) {
    plugins.push(
      compressPlugin({
        ext: '.gz',
        deleteOriginFile,
      }),
    );
  }

  if (compressList.includes('brotli')) {
    plugins.push(
      compressPlugin({
        ext: '.br',
        algorithm: 'brotliCompress',
        deleteOriginFile,
      }),
    );
  }
  return plugins;
}
