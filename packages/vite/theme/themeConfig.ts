/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { generate } from '@ant-design/colors';
// import { APP_PRESET_COLOR_LIST } from '../../../src/settings/designSetting';

export const primaryColor = '#2a50ec'; // APP_PRESET_COLOR_LIST[0];
export const darkPrimaryColor = '#2a50ec';

export const darkMode = 'light';

type Fn = (...arg: any) => any;

type GenerateTheme = 'default' | 'dark';

export interface GenerateColorsParams {
  mixLighten: Fn;
  mixDarken: Fn;
  tinycolor: any;
  color?: string;
}

export function generateAntColors(color: string, theme: GenerateTheme = 'default') {
  return generate(color, {
    theme,
  });
}

export function getThemeColors(color?: string) {
  const primary = color || primaryColor;
  const lightColors = generateAntColors(primary);
  const darkPrimary = darkPrimaryColor; //lightColors[5];
  const darkColors = generateAntColors(darkPrimary, 'dark');
  return [...lightColors, ...darkColors];
}

export function generateColors({ color = primaryColor, mixLighten, mixDarken, tinycolor }: GenerateColorsParams) {
  const arr = new Array(19).fill(0);

  const lightens = arr.map((_t, i) => {
    return mixLighten(color, i / 5);
  });

  const darkens = arr
    .map((_t, i) => {
      return mixDarken(color, i / 5);
    })
    .filter((item) => !item.includes('-'));

  const alphaColors = arr.map((_t, i) => {
    return tinycolor(color)
      .setAlpha(i / 20)
      .toRgbString();
  });

  const shortAlphaColors = alphaColors.map((item) => item.replace(/\s/g, '').replace(/0\./g, '.'));

  const tinycolorLightens = arr
    .map((_t, i) => {
      return tinycolor(color)
        .lighten(i * 5)
        .toHexString();
    })
    .filter((item) => item !== '#ffffff');

  const tinycolorDarkens = arr
    .map((_t, i) => {
      return tinycolor(color)
        .darken(i * 5)
        .toHexString();
    })
    .filter((item) => item !== '#000000');

  // console.log('lightens', lightens);
  // console.log('darkens', darkens);
  // console.log('alphaColors', alphaColors);
  // console.log('shortAlphaColors', shortAlphaColors);
  // console.log('tinycolorLightens', tinycolorLightens);
  // console.log('tinycolorDarkens', tinycolorDarkens);

  return [...lightens, ...darkens, ...alphaColors, ...shortAlphaColors, ...tinycolorDarkens, ...tinycolorLightens];
}
