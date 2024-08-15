/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import UnoCSS from 'unocss/vite';

export function configUnoCSSPlugin(): PluginOption {
  return UnoCSS();
}
