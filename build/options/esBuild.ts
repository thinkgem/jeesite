/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { ESBuildOptions } from 'vite';

export function createEsBuildOptions(mode: string): ESBuildOptions {
  return {
    // pure: viteEnv.VITE_DROP_CONSOLE ? ['console.log', 'debugger'] : [],
    drop: mode === 'production' ? ['console', 'debugger'] : [],
  };
}
