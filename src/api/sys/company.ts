/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel } from '/@/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Company extends TreeModel<Company> {
  companyCode?: string; // 公司编码
  viewCode?: string; // 公司代码
  companyName?: string; // 公司名称
  fullName?: string; // 公司全称
  areaCode?: string; // 区域编码
  extend?: any; // 扩展字段
}

export const companyList = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/list', params });

export const companyListData = (params?: Company | any) =>
  defHttp.post<Company[]>({ url: adminPath + '/sys/company/listData', params });

export const companyForm = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/form', params });

export const companyCreateNextNode = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/createNextNode', params });

export const companySave = (params?: any, data?: Company | any) =>
  defHttp.postJson<Company>({ url: adminPath + '/sys/company/save', params, data });

export const companyDisable = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/disable', params });

export const companyEnable = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/enable', params });

export const companyDelete = (params?: Company | any) =>
  defHttp.get<Company>({ url: adminPath + '/sys/company/delete', params });

export const companyTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/company/treeData', params });
