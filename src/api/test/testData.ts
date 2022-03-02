/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, Page } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface TestData extends BasicModel<TestData> {
  testInput?: string; // 单行文本
  testTextarea?: string; // 多行文本
  testSelect?: string; // 下拉框
  testSelectMultiple?: string; // 下拉多选
  testRadio?: string; // 单选框
  testCheckbox?: string; // 复选框
  testDate?: string; // 日期选择
  testDatetime?: string; // 日期时间
  testUser?: any; // 用户选择
  testOffice?: any; // 机构选择
  testAreaCode?: string; // 区域选择
  testAreaName?: string; // 区域名称

  testDataChildList?: any[]; // 子表列表
}

export const testDataList = (params?: TestData | any) =>
  defHttp.get<TestData>({ url: adminPath + '/test/testData/list', params });

export const testDataListData = (params?: TestData | any) =>
  defHttp.post<Page<TestData>>({ url: adminPath + '/test/testData/listData', params });

export const testDataForm = (params?: TestData | any) =>
  defHttp.get<TestData>({ url: adminPath + '/test/testData/form', params });

export const testDataSave = (params?: any, data?: TestData | any) =>
  defHttp.postJson<TestData>({ url: adminPath + '/test/testData/save', params, data });

export const testDataDisable = (params?: TestData | any) =>
  defHttp.get<TestData>({ url: adminPath + '/test/testData/disable', params });

export const testDataEnable = (params?: TestData | any) =>
  defHttp.get<TestData>({ url: adminPath + '/test/testData/enable', params });

export const testDataDelete = (params?: TestData | any) =>
  defHttp.get<TestData>({ url: adminPath + '/test/testData/delete', params });
