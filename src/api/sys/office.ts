/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface Office extends TreeModel<Office> {
  officeCode?: string; // 机构编码
  viewCode?: string; // 机构代码
  officeName?: string; // 机构名称
  fullName?: string; // 机构全称
  officeType?: string; // 机构类型
  leader?: string; // 负责人
  phone?: string; // 办公电话
  address?: string; // 联系地址
  zipCode?: string; // 邮政编码
  email?: string; // 电子邮箱
  extend?: any; // 扩展字段

  companyCode?: string; // 根据公司查询机构，组织机构所属公司
}

export const officeList = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/list', params });

export const officeListData = (params?: Office | any) =>
  defHttp.post<Office[]>({ url: adminPath + '/sys/office/listData', params });

export const officeForm = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/form', params });

export const officeCreateNextNode = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/createNextNode', params });

export const officeSave = (params?: any, data?: Office | any) =>
  defHttp.postJson<Office>({ url: adminPath + '/sys/office/save', params, data });

export const officeDisable = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/disable', params });

export const officeEnable = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/enable', params });

export const officeDelete = (params?: Office | any) =>
  defHttp.get<Office>({ url: adminPath + '/sys/office/delete', params });

export const officeTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/office/treeData', params });
