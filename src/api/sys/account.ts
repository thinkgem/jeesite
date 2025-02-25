/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
// import { useGlobSetting } from '/@/hooks/setting';
import { ErrorMessageMode } from '/#/axios';
import { LoginResult } from '/@/api/sys/login';

// const { adminPath } = useGlobSetting();

export const getLoginValidCode = (params?: any) =>
  defHttp.post({ url: '/account/getLoginValidCode', params });

export const loginByValidCode = (params?: any, mode: ErrorMessageMode = 'none') =>
  defHttp.post<LoginResult>(
    { url: '/account/loginByValidCode', params, timeout: 20 * 1000 },
    { errorMessageMode: mode },
  );
