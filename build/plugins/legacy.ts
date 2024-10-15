/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import legacy from '@vitejs/plugin-legacy';
import { legacyTargets } from '../options/build';

export function configLegacyPlugin(isBuild: boolean, viteEnv: ViteEnv): PluginOption {
  if (!(isBuild && viteEnv.VITE_LEGACY)) {
    return [];
  }
  return legacy({
    targets: legacyTargets,
    modernPolyfills: true,
  });
}
