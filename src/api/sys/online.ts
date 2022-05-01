/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import type { Result } from '/#/axios';

const { adminPath } = useGlobSetting();

export interface Online {
  id?: string;
  startTimestamp?: string;
  lastAccessTime?: string;
  timeout?: string;
  userCode?: string;
  userName?: string;
  userType?: string;
  deviceType?: string;
  host?: string;
}

export const onlineList = (params?: Online | any) =>
  defHttp.get<Online>({ url: adminPath + '/sys/online/list', params });

export const onlineListData = (params?: Online | any) =>
  defHttp.post<Online[]>({ url: adminPath + '/sys/online/listData', params });

export const onlineTickOut = (params?: Online | any) =>
  defHttp.post<Result>({ url: adminPath + '/sys/online/tickOut', params });

export const onlineCount = () => defHttp.post<Number>({ url: adminPath + '/sys/online/count' });
