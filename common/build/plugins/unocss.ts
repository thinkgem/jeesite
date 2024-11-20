/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import UnoCSSPlugin from 'unocss/vite';
import UnoCSSConfig from '../../../uno.config';

export function configUnoCSSPlugin(): PluginOption {
  return UnoCSSPlugin(UnoCSSConfig);
}
