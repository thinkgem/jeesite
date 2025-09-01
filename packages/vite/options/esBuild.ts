/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { ESBuildOptions } from 'vite';

export function createEsBuildOptions(viteEnv: ViteEnv): ESBuildOptions {
  return {
    drop: viteEnv.VITE_DROP_CONSOLE ? ['console', 'debugger'] : [],
  };
}
