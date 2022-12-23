/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, Page } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface MsgInner extends BasicModel<MsgInner> {
  msgTitle?: string; // 消息标题
  contentLevel?: string; // 内容级别（1普通 2一般 3紧急）
  contentType?: string; // 内容类型（1公告 2新闻 3会议 4其它）
  msgContent?: string; // 消息内容
  receiveType?: string; // 接受者类型（0全部 1用户 2部门 3角色 4岗位）
  receiveCodes?: string; // 接受者字符串
  receiveNames?: string; // 接受者名称字符串
  sendUserCode?: string; // 发送者用户编码
  sendUserName?: string; // 发送者用户姓名
  sendDate?: string; // 发送时间
  isAttac?: string; // 是否有附件
  notifyTypes?: string; // 通知类型（PC APP 短信 邮件 微信）多选
}

export const msgInnerList = (params?: MsgInner | any) =>
  defHttp.get<MsgInner>({ url: adminPath + '/msg/msgInner/list', params });

export const msgInnerListData = (params?: MsgInner | any) =>
  defHttp.post<Page<MsgInner>>({ url: adminPath + '/msg/msgInner/listData', params });

export const msgInnerForm = (params?: MsgInner | any) =>
  defHttp.get<MsgInner>({ url: adminPath + '/msg/msgInner/form', params });

export const msgInnerView = (params?: MsgInner | any) =>
  defHttp.get<MsgInner>({ url: adminPath + '/msg/msgInner/view', params });

export const msgInnerSave = (params?: any, data?: MsgInner | any) =>
  defHttp.postJson<MsgInner>({ url: adminPath + '/msg/msgInner/save', params, data });

export const msgInnerDelete = (params?: MsgInner | any) =>
  defHttp.get<MsgInner>({ url: adminPath + '/msg/msgInner/delete', params });
