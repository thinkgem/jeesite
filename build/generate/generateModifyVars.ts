import { generateAntColors, primaryColor, darkPrimaryColor } from '../config/themeConfig';
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

  const { darkAlgorithm, defaultAlgorithm, defaultSeed } = theme;
  const mapToken = dark ? darkAlgorithm(defaultSeed) : defaultAlgorithm(defaultSeed);
  const modifyVars = convertLegacyToken(mapToken);
  return {
    ...modifyVars,
    hack: `true; @import (reference) "${resolve('src/design/config.less')}";`,
    'primary-color': primary,
    'link-color': primary,
    'info-color': primary,
  };
}
