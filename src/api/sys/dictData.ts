/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface DictData extends TreeModel<DictData> {
  dictCode?: string; // 字典编码
  dictLabelRaw?: string; // 字典标签
  dictValue?: string; // 字典键值
  dictIcon?: string; // 字典图标
  dictType?: string; // 字典类型
  isSys?: string; // 系统内置（1是 0否）
  description?: string; // 字典描述
  cssStyle?: string; // css样式（如：color:red)
  cssClass?: string; // css类名（如：red）
}

export interface DictDataTree extends TreeDataModel {
  icon?: string; // 字典图标
  cssStyle?: string; // css样式（如：color:red)
  cssClass?: string; // css类名（如：red）
}

export const dictDataList = (params?: DictData | any) =>
  defHttp.get<DictData>({ url: adminPath + '/sys/dictData/list', params });

export const dictDataListData = (params?: DictData | any) =>
  defHttp.post<DictData[]>({ url: adminPath + '/sys/dictData/listData', params });

export const dictDataForm = (params?: DictData | any) =>
  defHttp.get<DictData>({ url: adminPath + '/sys/dictData/form', params });

export const dictDataCreateNextNode = (params?: DictData | any) =>
  defHttp.get<DictData>({ url: adminPath + '/sys/dictData/createNextNode', params });

export const dictDataSave = (params?: any, data?: DictData | any) =>
  defHttp.postJson<DictData>({ url: adminPath + '/sys/dictData/save', params, data });

export const dictDataDelete = (params?: DictData | any) =>
  defHttp.get<DictData>({ url: adminPath + '/sys/dictData/delete', params });

export const dictDataTreeData = (params?: any) =>
  defHttp.get<DictDataTree[]>({ url: adminPath + '/sys/dictData/treeData', params });
