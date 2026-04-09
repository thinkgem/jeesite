import { PluginOption } from 'vite';
import path from 'path';
import fs from 'fs-extra';
import colors from 'picocolors';
import { createFileHash, extractVariable, formatCss, minifyCSS } from './utils';
import { cssLangRE, cssVariableString, VITE_CLIENT_ENTRY } from './constants';
import { injectClientPlugin } from './injectClientPlugin';
import { createContext } from './context';

export * from '../client/colorUtils';

export { antdDarkThemePlugin } from './antdDarkThemePlugin';

export type ResolveSelector = (selector: string) => string;

export type InjectTo = 'head' | 'body' | 'body-prepend';

export interface ViteThemeOptions {
  injectClientPath?: string;
  colorVariables: string[];
  wrapperCssSelector?: string;
  resolveSelector?: ResolveSelector;
  customerExtractVariable?: (code: string) => string;
  fileName?: string;
  injectTo?: InjectTo;
  verbose?: boolean;
}

export function viteThemePlugin(opt: ViteThemeOptions): PluginOption {
  const styleMap = new Map<string, string>();
  const extCssSet = new Set<string>();

  const emptyPlugin: PluginOption = {
    name: 'vite:theme',
  };

  const options: ViteThemeOptions = Object.assign(
    {
      colorVariables: [],
      wrapperCssSelector: '',
      fileName: 'app-theme-style',
      injectTo: 'body',
      verbose: true,
    },
    opt,
  );

  // debug('plugin options:', options);

  const { colorVariables, wrapperCssSelector, resolveSelector, customerExtractVariable, fileName, verbose } = options;

  if (!colorVariables || colorVariables.length === 0) {
    console.error('colorVariables is not empty!');
    return [emptyPlugin];
  }

  const resolveSelectorFn = resolveSelector || ((s: string) => `${wrapperCssSelector} ${s}`);

  const cssOutputName = `${fileName}.${createFileHash()}.css`;

  const context = createContext({ colorThemeOptions: options, colorThemeFileName: cssOutputName });

  return [
    injectClientPlugin(),
    {
      enforce: 'post',
      apply: 'serve',
      ...emptyPlugin,
      configResolved(resolvedConfig) {
        createContext({
          viteOptions: resolvedConfig,
          devEnvironment: resolvedConfig.command === 'serve',
          needSourceMap: !!resolvedConfig.build.sourcemap,
        });
        // debug('plugin config:', resolvedConfig);
      },

      async transform(code, id) {
        if (!cssLangRE.test(id)) {
          return null;
        }
        const getResult = (content: string) => {
          return {
            map: context.needSourceMap ? this.getCombinedSourcemap() : null,
            code: content,
          };
        };

        const clientCode = await getClientStyleString(code);

        // Used to extract the relevant color configuration in css, you can pass in the function to override
        const extractCssCodeTemplate =
          typeof customerExtractVariable === 'function'
            ? customerExtractVariable(clientCode)
            : extractVariable(clientCode, colorVariables, resolveSelectorFn);

        // debug('extractCssCodeTemplate:', id, extractCssCodeTemplate);

        if (!extractCssCodeTemplate) {
          return null;
        }

        // dev-server
        const retCode = [
          `import { addCssToQueue } from ${opt.injectClientPath || context.injectClientPath}`,
          `const themeCssId = ${JSON.stringify(id)}`,
          `const themeCssStr = ${JSON.stringify(formatCss(extractCssCodeTemplate))}`,
          `addCssToQueue(themeCssId, themeCssStr)`,
          code,
        ];

        return getResult(retCode.join('\n'));
      },
    },
    {
      apply: 'build',
      ...emptyPlugin,
      configResolved(resolvedConfig) {
        createContext({
          viteOptions: resolvedConfig,
          devEnvironment: resolvedConfig.command === 'serve',
          needSourceMap: !!resolvedConfig.build.sourcemap,
        });
        // debug('plugin config:', resolvedConfig);
      },

      async transform(code, id) {
        if (!cssLangRE.test(id)) {
          return null;
        }

        const clientCode = code.replace('export default', '').replace('"', '');

        // Used to extract the relevant color configuration in css, you can pass in the function to override
        const extractCssCodeTemplate =
          typeof customerExtractVariable === 'function'
            ? customerExtractVariable(clientCode)
            : extractVariable(clientCode, colorVariables, resolveSelectorFn);

        // debug('extractCssCodeTemplate:', id, extractCssCodeTemplate);

        if (!extractCssCodeTemplate) {
          return null;
        }

        if (!styleMap.has(id)) {
          extCssSet.add(extractCssCodeTemplate);
        }
        styleMap.set(id, extractCssCodeTemplate);

        return null;
      },

      async writeBundle() {
        const {
          root,
          build: { outDir, assetsDir, minify },
        } = context.viteOptions;
        let extCssString = '';
        for (const css of extCssSet) {
          extCssString += css;
        }
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
      //     console.log(colors.cyan('\n✨ [vite-plugin-theme-vite3]') + ` - extract css code file is successfully:`);
      //     try {
      //       const { size } = fs.statSync(path.join(outDir, assetsDir, cssOutputName));
      //       console.log(
      //         colors.dim(outDir + '/') +
      //           colors.magenta(`${assetsDir}/${cssOutputName}`) +
      //           `\t\t${colors.dim((size / 1024).toFixed(2) + 'kb')}` +
      //           '\n',
      //       );
      //     } catch (error) {}
      //   }
      // },
    },
  ];
}

// Intercept the css code embedded in js
async function getClientStyleString(code: string) {
  if (!code.includes(VITE_CLIENT_ENTRY)) {
    return code;
  }
  code = code.replace(/\\n/g, '');
  const cssPrefix = cssVariableString;
  const cssPrefixLen = cssPrefix.length;

  const cssPrefixIndex = code.indexOf(cssPrefix);
  const len = cssPrefixIndex + cssPrefixLen;
  const cssLastIndex = code.indexOf('\n', len + 1);

  if (cssPrefixIndex !== -1) {
    code = code.slice(len, cssLastIndex);
  }
  return code;
}
