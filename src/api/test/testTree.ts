/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { TreeDataModel, TreeModel } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface TestTree extends TreeModel<TestTree> {
  treeCode?: string; // 节点编码
  treeName?: string; // 节点名称
}

export const testTreeList = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/list', params });

export const testTreeListData = (params?: TestTree | any) =>
  defHttp.post<TestTree[]>({ url: adminPath + '/test/testTree/listData', params });

export const testTreeForm = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/form', params });

export const testTreeCreateNextNode = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/createNextNode', params });

export const testTreeSave = (params?: any, data?: TestTree | any) =>
  defHttp.postJson<TestTree>({ url: adminPath + '/test/testTree/save', params, data });

export const testTreeDisable = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/disable', params });

export const testTreeEnable = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/enable', params });

export const testTreeDelete = (params?: TestTree | any) =>
  defHttp.get<TestTree>({ url: adminPath + '/test/testTree/delete', params });

export const testTreeTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/test/testTree/treeData', params });
