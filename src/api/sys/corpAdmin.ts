/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { Page, TreeDataModel } from '/@/api/model/baseModel';
import { User } from '/@/api/sys/user';

const { adminPath } = useGlobSetting();

export const corpAdminList = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/list', params });

export const corpAdminListData = (params?: User | any) =>
  defHttp.post<Page<User>>({ url: adminPath + '/sys/corpAdmin/listData', params });

export const corpAdminForm = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/form', params });

export const corpAdminSave = (params?: any, data?: User | any) =>
  defHttp.postJson<User>({ url: adminPath + '/sys/corpAdmin/save', params, data });

export const corpAdminDisable = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/disable', params });

export const corpAdminEnable = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/enable', params });

export const corpAdminResetpwd = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/resetpwd', params });

export const corpAdminDelete = (params?: User | any) =>
  defHttp.get<User>({ url: adminPath + '/sys/corpAdmin/delete', params });

export const corpAdminTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/corpAdmin/treeData', params });

export const switchCorp = (corpCode: string) =>
  defHttp.get<User>({
    url: adminPath + '/sys/corpAdmin/switch/' + corpCode,
  });
