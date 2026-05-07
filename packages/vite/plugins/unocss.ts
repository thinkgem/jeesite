/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import UnoCSS from 'unocss/vite';
import unoConfig from '../../../uno.config';

export function configUnoCSSPlugin(): PluginOption {
  return UnoCSS(unoConfig);
}
