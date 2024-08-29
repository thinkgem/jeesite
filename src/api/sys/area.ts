/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel, Page } from '/@/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Area extends TreeModel<Area> {
  areaCode?: string; // 区域编码
  areaName?: string; // 区域名称
  areaType?: string; // 区域类型
}

export const areaList = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/list', params });

export const areaListData = (params?: Area | any) =>
  defHttp.post<Page<Area>>({ url: adminPath + '/sys/area/listPageData', params });

export const areaForm = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/form', params });

export const areaCreateNextNode = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/createNextNode', params });

export const areaSave = (params?: any, data?: Area | any) =>
  defHttp.postJson<Area>({ url: adminPath + '/sys/area/save', params, data });

export const areaDisable = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/disable', params });

export const areaEnable = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/enable', params });

export const areaDelete = (params?: Area | any) =>
  defHttp.get<Area>({ url: adminPath + '/sys/area/delete', params });

export const areaTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/area/treeData', params });
