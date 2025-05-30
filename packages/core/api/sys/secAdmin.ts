/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { Page } from '@jeesite/core/api/model/baseModel';
import { User } from '@jeesite/core/api/sys/user';

const { adminPath } = useGlobSetting();

export const secAdminList = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/secAdmin/list', params });

export const secAdminListData = (params?: User | any) =>
  defHttp.post<Page<User>>({ url: adminPath + '/sys/secAdmin/listData', params });

export const secAdminForm = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/secAdmin/form', params });

export const secAdminSave = (params?: any) => defHttp.post<User>({ url: adminPath + '/sys/secAdmin/save', params });

export const secAdminDelete = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/secAdmin/delete', params });
