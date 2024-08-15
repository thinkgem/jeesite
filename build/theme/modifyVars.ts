/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { generateAntColors, primaryColor, darkPrimaryColor } from './themeConfig';
// import { getThemeVariables } from 'ant-design-vue/dist/theme';
import { resolve } from 'path';

import { theme } from 'ant-design-vue';
import convertLegacyToken from 'ant-design-vue/lib/theme/convertLegacyToken';

/**
 * less global variable
 */
export function generateModifyVars(dark = false) {
  const palettes = generateAntColors(primaryColor);
  const primary = dark ? darkPrimaryColor : palettes[5];

  // const primaryColorObj: Record<string, string> = {};
  // for (let index = 0; index < 10; index++) {
  //   primaryColorObj[`primary-${index + 1}`] = palettes[index];
  // }

  // const modifyVars = getThemeVariables({ dark: false });
  const { darkAlgorithm, defaultAlgorithm, defaultSeed } = theme;
  const mapToken = dark ? darkAlgorithm(defaultSeed) : defaultAlgorithm(defaultSeed);
  const modifyVars = (convertLegacyToken as any).default(mapToken);
  // const modifyVars = convertLegacyToken(mapToken);
  // !!dark && console.log('modifyVars', dark, modifyVars);

  return {
    ...modifyVars,
    // Used for global import to avoid the need to import each style file separately
    // reference:  Avoid repeated references
    // hack: `${modifyVars.hack} @import (reference) "${resolve('src/design/config.less')}";`,
    hack: `true; @import (reference) "${resolve('src/design/config.less')}";`,
    // ...primaryColorObj,
    'primary-color': primary,
    'link-color': primary,
    'info-color': primary,
    // 'processing-color': primary,
    // 'success-color': '#55D187', //  Success color
    // 'error-color': '#ED6F6F', //  False color
    // 'warning-color': '#EFBD47', //   Warning color
    // 'font-size-base': '14px', //  Main font size
    // 'border-radius-base': '2px', //  Component/float fillet
    // 'content-bg-striped': '#fafafa',
  };
}
