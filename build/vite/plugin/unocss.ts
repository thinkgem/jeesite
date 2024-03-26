import { type PluginOption } from 'vite';
import { presetTypography, presetUno, presetIcons } from 'unocss';
import UnoCSS from 'unocss/vite';

export function configUnoCSSPlugin(): PluginOption {
  return UnoCSS({
    presets: [presetUno(), presetTypography(), presetIcons()],
  });
}
