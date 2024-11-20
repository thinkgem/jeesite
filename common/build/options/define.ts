/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { PackageJson } from 'pkg-types';
import dayjs from 'dayjs';

export function createDefineOptions(pkg: PackageJson): Record<string, any> {
  try {
    const { dependencies, devDependencies, name, version } = pkg;
    const __APP_INFO__ = {
      pkg: { dependencies, devDependencies, name, version },
      lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
    };
    return {
      // __INTLIFY_PROD_DEVTOOLS__: 'false',
      __APP_INFO__: JSON.stringify(__APP_INFO__),
    };
  } catch (error) {
    console.log('createDefine', error);
    return {};
  }
}
