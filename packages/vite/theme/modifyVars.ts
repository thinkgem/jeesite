/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { generateAntColors, primaryColor, darkPrimaryColor } from './themeConfig';
import { theme } from 'antdv-next';
import tokenVars from './tokenVars';

/**
 * less global variable
 */
export function generateModifyVars(dark = false) {
  const palettes = generateAntColors(primaryColor);
  const primary = dark ? darkPrimaryColor : palettes[5];

  const { darkAlgorithm, defaultAlgorithm, defaultSeed } = theme;
  const mapToken = dark ? darkAlgorithm(defaultSeed) : defaultAlgorithm(defaultSeed);
  const modifyVars = tokenVars(mapToken);

  return {
    ...modifyVars,
    // Used for global import to avoid the need to import each style file separately
    // reference:  Avoid repeated references
    hack: `true; @import (reference) "${__dirname + '/../../core/design/var/index.less'}";`,
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
