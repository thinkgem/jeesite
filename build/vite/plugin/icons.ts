/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { type PluginOption } from 'vite';
import PurgeIcons from 'vite-plugin-purge-icons';

export function configIconsPlugin(): PluginOption {
  return PurgeIcons({
    iconSource: 'remote',
    remoteDataAPI: 'https://gitee.com/thinkgem/icon-sets/raw/master/json',
  });
}
