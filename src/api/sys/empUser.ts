/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { Page, TreeDataModel } from '../model/baseModel';
import { User } from './user';
import { UploadApiResult } from './upload';
import { UploadFileParams } from '/#/axios';
import { AxiosProgressEvent } from 'axios';

const { ctxPath, adminPath } = useGlobSetting();

export interface EmpUser extends User {
  employee?: any;
}

export const empUserIndex = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/index', params });

export const empUserList = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/list', params });

export const empUserListData = (params?: EmpUser | any) =>
  defHttp.post<Page<EmpUser>>({ url: adminPath + '/sys/empUser/listData', params });

export const empUserForm = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/form', params });

export const empUserSave = (params?: any, data?: EmpUser | any) =>
  defHttp.postJson<EmpUser>({ url: adminPath + '/sys/empUser/save', params, data });

export const checkEmpNo = (oldEmpNo: string, empNo: string) =>
  defHttp.get<EmpUser>({
    url: adminPath + '/sys/empUser/checkEmpNo',
    params: { oldEmpNo, 'employee.empNo': empNo },
  });

export const empUserImportData = (
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.uploadFile<UploadApiResult>(
    {
      url: ctxPath + adminPath + '/sys/empUser/importData',
      onUploadProgress,
    },
    params,
  );

export const empUserDisable = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/disable', params });

export const empUserEnable = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/enable', params });

export const resetpwd = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/resetpwd', params });

export const empUserDelete = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>(
    { url: adminPath + '/sys/empUser/delete', params },
    { errorMessageMode: 'none' },
  );

export const formAuthDataScope = (params?: EmpUser | any) =>
  defHttp.get<EmpUser>({ url: adminPath + '/sys/empUser/formAuthDataScope', params });

export const ctrlDataTreeData = (params?: any) => {
  const { url, ...params2 } = params;
  return defHttp.get<EmpUser>({ url: adminPath + url, params: params2 });
};

export const saveAuthDataScope = (params?: EmpUser | any) =>
  defHttp.post<EmpUser>({ url: adminPath + '/sys/empUser/saveAuthDataScope', params });

export const empUserTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/sys/empUser/treeData', params });
