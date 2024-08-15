/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { CSSOptions } from 'vite';
import { generateModifyVars } from '../theme/modifyVars';

export function createCSSOptions(): CSSOptions {
  return {
    preprocessorOptions: {
      less: {
        modifyVars: generateModifyVars(),
        javascriptEnabled: true,
      },
    },
  };
}
