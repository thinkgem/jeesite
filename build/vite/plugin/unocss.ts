import { type PluginOption } from 'vite';
import UnoConfig from '../../../uno.config';
import UnoCSS from 'unocss/vite';

export function configUnoCSSPlugin(): PluginOption {
  return UnoCSS(UnoConfig);
}
