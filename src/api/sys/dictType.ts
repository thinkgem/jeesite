/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, TreeDataModel } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface DictType extends BasicModel<DictType> {
  dictName?: string; // 字典名称
  dictType?: string; // 字典类型
  isSys?: string; // 是否系统字典
}

export const dictTypeList = (params?: DictType | any) =>
  defHttp.get<DictType>({ url: adminPath + '/sys/dictType/list', params });

export const dictTypeListData = (params?: DictType | any) =>
  defHttp.post<DictType[]>({ url: adminPath + '/sys/dictType/listData', params });

export const dictTypeForm = (params?: DictType | any) =>
  defHttp.get<DictType>({ url: adminPath + '/sys/dictType/form', params });

export const dictTypeSave = (params?: any, data?: DictType | any) =>
  defHttp.postJson<DictType>({ url: adminPath + '/sys/dictType/save', params, data });

export const checkDictType = (oldDictType: string, dictType: string) =>
  defHttp.get<DictType>({
    url: adminPath + '/sys/dictType/checkDictType',
    params: { oldDictType, dictType },
  });

export const dictTypeDelete = (params?: DictType | any) =>
  defHttp.get<DictType>({ url: adminPath + '/sys/dictType/delete', params });

export const dictTypeTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/dictType/treeData', params });
