/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { Page } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export const dbmTreeList = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/list', data });

export const dbmTreeListData = (data?: Recordable | any) =>
  defHttp.postJson<Page<Recordable>>({ url: adminPath + '/dbm/tree/listData', data });

export const dbmTreeForm = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/form', data });

export const dbmTreeSave = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/save', data });

export const dbmTreeDisable = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/disable', data });

export const dbmTreeEnable = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/enable', data });

export const dbmTreeDelete = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/delete', data });

export const dbmFixTreeData = (data?: Recordable | any) =>
  defHttp.postJson<Recordable>({ url: adminPath + '/dbm/tree/fixTreeData', data });
