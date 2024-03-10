/**
 * Plugin to minimize and use ejs template syntax in index.html.
 * https://github.com/anncwb/vite-plugin-html
 */
import type { PluginOption } from 'vite';
import { createHtmlPlugin } from 'vite-plugin-html';

export function configHtmlPlugin(isBuild: boolean): PluginOption[] {
  return createHtmlPlugin({
    minify: isBuild,
  });
}
