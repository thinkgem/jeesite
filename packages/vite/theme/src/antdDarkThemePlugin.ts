import type { PluginOption } from 'vite';
import path from 'path';
import fs from 'fs-extra';
import less from 'less';
import { createFileHash, extractVariable, minifyCSS } from './utils';
import { colorRE, linkID } from './constants';
import { injectClientPlugin } from './injectClientPlugin';
import { lessPlugin } from './preprocessor/less';
// import colors from 'picocolors';
import { createContext } from './context';

const less2 = less['default'] || less;

export interface AntdDarkThemeOption {
  injectClientPath?: string;
  /**
   * darkModifyVars
   */
  darkModifyVars?: never;
  /**
   * when extractCss is true, the file name of the extracted css file
   */
  fileName?: string;
  verbose?: boolean;
  selector?: string;
  /**
   * Files that result in true will be processed.
   * @param id (file path)
   */
  filter?: (id: string) => boolean;
  /**
   * when run in dev mode, the plugin will preloadFile
   */
  preloadFiles?: string[];
  /**
   * extractCss to a single file
   * @default true
   */
  extractCss?: boolean;
  /**
   * load darkCss type
   * @default 'link'
   */
  loadMethod?: 'link' | 'ajax';
}

export function antdDarkThemePlugin(opt: AntdDarkThemeOption): PluginOption {
  const options = Object.assign(
    {
      verbose: true,
      fileName: 'app-antd-dark-theme-style',
      preloadFiles: [],
      loadMethod: 'link',
      extractCss: true,
    },
    opt,
  );

  const { darkModifyVars, verbose, fileName, selector, filter, preloadFiles, loadMethod, extractCss } = options;

  let extCssString = '';

  const styleMap = new Map<string, string>();
  const codeCache = new Map<string, { code: string; css: string }>();

  const cssOutputName = `${fileName}.${createFileHash()}.css`;

  const context = createContext({ antdThemeOptions: options, antdThemeFileName: cssOutputName });

  const getCss = (css: string) => {
    return `[${selector || 'data-theme="dark"'}] {${css}}`;
  };

  async function preloadLess() {
    if (!preloadFiles || !preloadFiles.length) {
      return;
    }
    for (const id of preloadFiles) {
      const code = fs.readFileSync(id, 'utf-8');
      less2
        .render(code, {
          javascriptEnabled: true,
          modifyVars: darkModifyVars,
          filename: path.resolve(id),
          plugins: [lessPlugin(id, context.viteOptions)],
        })
        .then(({ css }) => {
          const colors = css.match(colorRE);
          if (colors) {
            css = extractVariable(css, colors.concat(['transparent']));
            codeCache.set(id, { code, css });
          }
        });
    }
  }

  return [
    injectClientPlugin(),
    {
      name: 'vite:antd-dark-theme',
      enforce: 'pre',
      configResolved(resolvedConfig) {
        createContext({
          viteOptions: resolvedConfig,
          devEnvironment: resolvedConfig.command === 'serve',
          needSourceMap: !!resolvedConfig.build.sourcemap,
        });
        if (resolvedConfig.command === 'serve') {
          preloadLess().then();
        }
      },
      transformIndexHtml(html) {
        if (context.devEnvironment || loadMethod !== 'link' || !extractCss) {
          return html;
        }

        const config = context.viteOptions;
        return {
          html,
          tags: [
            {
              tag: 'link',
              attrs: {
                disabled: true,
                id: linkID,
                rel: 'alternate stylesheet',
                href: path.posix.join(config.base, config.build.assetsDir, cssOutputName),
              },
              injectTo: 'head',
            },
          ],
        };
      },

      async transform(code, id) {
        if (!id.endsWith('.less') || !code.includes('@')) {
          return null;
        }

        if (typeof filter === 'function' && !filter(id)) {
          return null;
        }

        const getResult = (content: string) => {
          return {
            map: context.needSourceMap ? this.getCombinedSourcemap() : null,
            code: content,
          };
        };

        let processCss = '';
        const cache = codeCache.get(id);
        const isUpdate = !cache || cache.code !== code;

        if (isUpdate) {
          const { css } = await less2.render(code, {
            javascriptEnabled: true,
            modifyVars: darkModifyVars,
            filename: path.resolve(id),
            plugins: [lessPlugin(id, context.viteOptions)],
          });

          const colors = css.match(colorRE);
          if (colors) {
            // The theme only extracts css related to color
            // Can effectively reduce the size
            processCss = extractVariable(css, colors.concat(['transparent']));
          }
        } else {
          processCss = cache!.css;
        }

        if (context.devEnvironment) {
          if (isUpdate) {
            codeCache.set(id, { code, css: processCss });
          }
          return getResult(`${getCss(processCss)}\n` + code);
        } else {
          if (!styleMap.has(id)) {
            const { css } = await less2.render(getCss(processCss), {
              filename: path.resolve(id),
              plugins: [lessPlugin(id, context.viteOptions)],
            });

            extCssString += `${css}\n`;
          }
          styleMap.set(id, processCss);
        }

        return null;
      },

      async writeBundle() {
        const {
          root,
          build: { outDir, assetsDir, minify },
        } = context.viteOptions;
        if (minify) {
          extCssString = await minifyCSS(extCssString, context.viteOptions);
        }
        const cssOutputPath = path.resolve(root, outDir, assetsDir, cssOutputName);
        fs.writeFileSync(cssOutputPath, extCssString);
      },

      // closeBundle() {
      //   if (verbose && !context.devEnvironment) {
      //     const {
      //       build: { outDir, assetsDir },
      //     } = context.viteOptions;
      //     console.log(
      //       colors.cyan('\n✨ [vite-plugin-theme-vite3:antd-dark]') +
      //         ` - extract antd dark css code file is successfully:`,
      //     );
      //     try {
      //       const { size } = fs.statSync(path.join(outDir, assetsDir, cssOutputName));
      //       console.log(
      //         colors.dim(outDir + '/') +
      //           colors.magenta(`${assetsDir}/${cssOutputName}`) +
      //           `\t\t${colors.dim((size / 1024).toFixed(2) + 'kb')}` +
      //           '\n',
      //       );
      //     } catch (error) {
      //       console.log(colors.cyan('\n✨ [vite-plugin-theme-vite3:antd-dark]') + ` - closeBundle error:`, error);
      //     }
      //   }
      // },
    },
  ];
}
