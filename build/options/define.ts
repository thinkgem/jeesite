/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { PackageJson, readPackageJSON } from 'pkg-types';
import dayjs from 'dayjs';

export async function createDefineOptions(): Promise<Record<string, any>> {
  const root = process.cwd() + '/../../';
  const pkg: PackageJson = await readPackageJSON();

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
