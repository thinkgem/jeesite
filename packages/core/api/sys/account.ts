/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { encryptByBase64 } from '@jeesite/core/utils/cipher';

export const getLoginValidCode = (params?: any) =>
  defHttp.post({ url: '/account/getLoginValidCode', params }, { errorMessageMode: 'none' });

export const loginByValidCode = (params?: any) =>
  defHttp.post({ url: '/account/loginByValidCode', params }, { errorMessageMode: 'none' });

export const getFpValidCode = (params?: any) =>
  defHttp.post({ url: '/account/getFpValidCode', params }, { errorMessageMode: 'none' });

export const getPwdQuestion = (params?: any) =>
  defHttp.post({ url: '/account/getPwdQuestion', params }, { errorMessageMode: 'none' });

export const savePwdByValidCode = (params?: any) => {
  if (params.password || params.confirmPassword) {
    params.password = encryptByBase64(params.password);
    params.confirmPassword = encryptByBase64(params.confirmPassword);
  }
  return defHttp.post({ url: '/account/savePwdByValidCode', params }, { errorMessageMode: 'none' });
};

export const savePwdByPwdQuestion = (params?: any) => {
  if (params.pwdQuestionAnswer || params.pwdQuestionAnswer2 || params.pwdQuestionAnswer3) {
    params.pwdQuestionAnswer = encryptByBase64(params.pwdQuestionAnswer);
    params.pwdQuestionAnswer2 = encryptByBase64(params.pwdQuestionAnswer2);
    params.pwdQuestionAnswer3 = encryptByBase64(params.pwdQuestionAnswer3);
  }
  if (params.password || params.confirmPassword) {
    params.password = encryptByBase64(params.password);
    params.confirmPassword = encryptByBase64(params.confirmPassword);
  }
  return defHttp.post({ url: '/account/savePwdByPwdQuestion', params }, { errorMessageMode: 'none' });
};

export const getRegValidCode = (params?: any) =>
  defHttp.post({ url: '/account/getRegValidCode', params }, { errorMessageMode: 'none' });

export const saveRegByValidCode = (params?: any) => {
  if (params.password || params.confirmPassword) {
    params.password = encryptByBase64(params.password);
    params.confirmPassword = encryptByBase64(params.confirmPassword);
  }
  return defHttp.post({ url: '/account/saveRegByValidCode', params }, { errorMessageMode: 'none' });
};
