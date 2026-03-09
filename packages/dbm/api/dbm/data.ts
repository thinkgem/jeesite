/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { Page } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export const dbmDataList = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/list', data });

export const dbmDataListData = (data?: Recordable | any) =>
  defHttp.postJson<Page<Recordable>>({ url: adminPath + '/dbm/data/listData', data });

export const dbmDataForm = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/form', data });

export const dbmDataSave = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/save', data });

export const dbmDataDisable = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/disable', data });

export const dbmDataEnable = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/enable', data });

export const dbmDataDelete = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/data/delete', data });
