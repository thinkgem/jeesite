/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel } from '/@/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Menu extends TreeModel<Menu> {
  menuCode?: string; // 菜单编码
  menuNameRaw?: string; // 菜单名称
  menuType?: string; // 菜单类型（1菜单 2权限）
  menuUrl?: string; // 菜单链接
  menuTarget?: string; // 目标窗口
  menuIcon?: string; // 菜单图标
  menuColor?: string; // 菜单颜色
  menuTitle?: string; // 菜单标题
  permission?: string; // 权限标识
  weight?: number; // 菜单权重（权重越大，表示菜单的重要性越大）
  isShow?: string; // 是否显示（1显示 0隐藏）
  sysCode?: string; // 归属系统（default:主导航菜单、mobileApp:APP菜单）
  moduleCodes?: string; // 归属模块（多个用逗号隔开）
}

export const menuIndex = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/index', params });

export const menuList = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/list', params });

export const menuListData = (params?: Menu | any) =>
  defHttp.post<Menu[]>({ url: adminPath + '/sys/menu/listData', params });

export const menuForm = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/form', params });

export const menuCreateNextNode = (params?: Menu | any) =>
  defHttp.get<Menu>({ url: adminPath + '/sys/menu/createNextNode', params });

export const menuSave = (params?: any, data?: Menu | any) =>
  defHttp.postJson<Menu>({ url: adminPath + '/sys/menu/save', params, data });

export const menuDisable = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/disable', params });

export const menuEnable = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/enable', params });

export const menuDelete = (params?: Menu | any) => defHttp.get<Menu>({ url: adminPath + '/sys/menu/delete', params });

export const menuTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/menu/treeData', params });
