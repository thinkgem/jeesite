/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import { PackageJson } from 'pkg-types';
import { getEnvConfig } from './index';
import { getEnvConfigName } from './getEnvConfigName';
import colors from 'picocolors';

const PLUGIN_NAME = 'app-config';
const APP_CONFIG_FILE_NAME = '_app.config.js';

export function appConfigPlugin(
  isBuild: boolean,
  viteEnv: ViteEnv,
  pkg: PackageJson,
): PluginOption {
  if (!isBuild) {
    return {
      name: PLUGIN_NAME,
    };
  }

  let publicPath: string;
  let appConfigContent: string;

  return {
    name: PLUGIN_NAME,
    async configResolved(_config) {
      publicPath = _config.base;
      appConfigContent = await getConfigContent(viteEnv);
    },
    async transformIndexHtml(html) {
      publicPath = (publicPath.endsWith('/') ? publicPath : `${publicPath}/`) || '/';
      const src = `${publicPath}${APP_CONFIG_FILE_NAME}?v=${pkg.version}-${new Date().getTime()}`;
      return { html, tags: [{ tag: 'script', attrs: { src } }] };
    },
    async generateBundle() {
      try {
        this.emitFile({
          type: 'asset',
          fileName: APP_CONFIG_FILE_NAME,
          source: appConfigContent,
        });
        this.emitFile({
          type: 'asset',
          fileName: 'timestamp.txt',
          source: `${new Date().getTime()}`,
        });
        console.log(colors.cyan(`✨configuration file is build successfully!`));
      } catch (error) {
        console.log(
          colors.red('configuration file configuration file failed to package:\n' + error),
        );
      }
    },
  };
}

async function getConfigContent(env: ViteEnv) {
  const config = await getEnvConfig();
  const envConfigName = getEnvConfigName(env);
  const windowVariable = `window.${envConfigName}`;
  // Ensure that the variable will not be modified
  let source = `${windowVariable}=${JSON.stringify(config)};`;
  source += `
    Object.freeze(${windowVariable});
    Object.defineProperty(window, "${envConfigName}", {
      configurable: false,
      writable: false,
    });
    var _hmt = _hmt || [];
    (function() {
      var hm = document.createElement("script");
      hm.src = "https://hm.baidu.com/hm.js?65b88e88a94e0118de2962f328f17622";
      var s = document.getElementsByTagName("script")[0]; 
      s.parentNode.insertBefore(hm, s);
    })();
  `.replace(/(?!var|\b)\s/g, '');
  return source;
}
