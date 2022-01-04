/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
export interface BasicModel<T> extends Recordable {
  id: string;
  page: PageModel<T>;
  isNewRecord: boolean;
  dataMap: Map<string, any>;
}

export interface TreeModel<T> extends BasicModel<T> {
  parentCode?: string; // 父级编码
  parentCodes?: string; // 所有父级编号

  treeNames?: string; // 全节点名

  treeSort?: string; // 排序号
  treeSorts?: string; // 所有排序号

  treeLeaf?: string; // 是否叶子节点
  treeLevel?: number; // 树层次级别（从0开始）

  childList?: T[]; // 子项列表

  isRoot?: boolean; // 是否根节点
  isTreeLeaf?: boolean; // 是否叶子
  isLoading?: boolean; // 是否加载中
}

export interface PageModel<T> {
  pageNo: number;
  pageSize: number;
  orderBy: string;
  count: number;
  list: T[];
}

export interface TreeDataModel {
  id: string;
  pId: string;
  name: string;
  value?: string;
  title?: string;
}
