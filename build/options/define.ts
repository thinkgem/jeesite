/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { PackageJson, readPackageJSON } from 'pkg-types';
import dayjs from 'dayjs';

export async function createDefineOptions(): Promise<Record<string, any>> {
  const rootPkg: PackageJson = await readPackageJSON(process.cwd() + '/../');
  const buildPkg: PackageJson = await readPackageJSON(process.cwd() + '/../build');
  const corePkg: PackageJson = await readPackageJSON(process.cwd() + '/../packages/core');
  try {
    const __APP_INFO__ = {
      pkg: {
        dependencies: Object.fromEntries(
          Object.entries({
            ...rootPkg.dependencies,
            ...buildPkg.dependencies,
            ...corePkg.dependencies,
          }).filter(([key]) => !key.endsWith('-lib'))
        ),
        devDependencies: Object.fromEntries(
          Object.entries({
            ...rootPkg.devDependencies,
            ...buildPkg.devDependencies,
            ...corePkg.devDependencies,
          }).filter(([key]) => !key.endsWith('-lib'))
        ),
        name: rootPkg.name,
        version: rootPkg.version,
      },
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
