/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, Page } from '/@/api/model/baseModel';
import { encryptByBase64 } from '/@/utils/cipher';

const { adminPath } = useGlobSetting();

export interface User extends BasicModel<User> {
  userCode?: string; // 用户编码
  loginCode?: string; // 登录账号
  userName?: string; // 用户昵称
  password?: string; // 登录密码
  email?: string; // 电子邮箱
  mobile?: string; // 手机号码
  phone?: string; // 办公电话
  sex?: string; // 用户性别
  avatar?: string; // 头像路径
  sign?: string; // 个性签名
  wxOpenid?: string; // 绑定的微信号
  mobileImei?: string; // 绑定的手机串号
  userType?: string; // 用户类型（none未设置 employee员工  member会员）
  refCode?: string; // 用户类型引用编号
  refName?: string; // 用户类型引用姓名
  mgrType?: string; // 管理员类型（0非管理员 1系统管理员  2二级管理员）
  lastLoginIp?: string; // 最后登陆IP
  lastLoginDate?: string; // 最后登陆时间
  freezeDate?: string; // 冻结时间
  freezeCause?: string; // 冻结原因
  userWeight?: number; // 用户权重（降序）

  avatarBase64?: string; // 头像Base64数据，修改头像时用

  oldLastLoginIp?: string; // 上次登陆IP
  oldLastLoginDate?: string; // 上次登陆日期

  roleCode?: string; // 根据角色查询用户条件
  isAll?: string; // 不过滤数据权限，查询全部用户
  ctrlPermi?: string; // 权限控制类型（拥有权限、管理权限）
}

export const userListData = (params?: User | any) =>
  defHttp.post<Page<User>>({ url: adminPath + '/sys/user/listData', params });

export const checkLoginCode = (oldLoginCode: string, loginCode: string) =>
  defHttp.get<User>({
    url: adminPath + '/sys/user/checkLoginCode',
    params: { oldLoginCode, loginCode },
  });

export const userInfo = (params?: any) =>
  defHttp.post<User>({ url: adminPath + '/sys/user/info', params });

export const infoSaveBase = (params?: any) =>
  defHttp.post<User>({ url: adminPath + '/sys/user/infoSaveBase', params });

export const infoSavePwd = (params?: any) => {
  params.oldPassword = encryptByBase64(params.oldPassword);
  params.newPassword = encryptByBase64(params.newPassword);
  params.confirmNewPassword = encryptByBase64(params.confirmNewPassword);
  return defHttp.post<User>({ url: adminPath + '/sys/user/infoSavePwd', params });
};

export const infoSavePqa = (params?: any) =>
  defHttp.post<User>({ url: adminPath + '/sys/user/infoSavePqa', params });
