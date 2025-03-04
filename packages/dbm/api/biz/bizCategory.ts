/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { TreeDataModel, TreeModel } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface BizCategory extends TreeModel<BizCategory> {
  categoryCode?: string; // 流程分类
  viewCode?: string; // 分类代码
  categoryName?: string; // 分类名称
}

export const bizCategoryList = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/list', params });

export const bizCategoryListData = (params?: BizCategory | any) =>
  defHttp.post<BizCategory[]>({ url: adminPath + '/biz/bizCategory/listData', params });

export const bizCategoryForm = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/form', params });

export const bizCategoryCreateNextNode = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/createNextNode', params });

export const bizCategorySave = (params?: any, data?: BizCategory | any) =>
  defHttp.postJson<BizCategory>({ url: adminPath + '/biz/bizCategory/save', params, data });

export const bizCategoryDisable = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/disable', params });

export const bizCategoryEnable = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/enable', params });

export const bizCategoryDelete = (params?: BizCategory | any) =>
  defHttp.get<BizCategory>({ url: adminPath + '/biz/bizCategory/delete', params });

export const bizCategoryTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/biz/bizCategory/treeData', params });
