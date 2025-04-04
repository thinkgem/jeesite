/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, Page } from '/@/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Log extends BasicModel<Log> {
  logTitle?: string; // 日志标题
  requestUri?: string; // 请求URI
  logType?: string; // 日志类型
  createBy?: string; // 操作用户编码
  createByName?: string; // 操作用户名称
  requestMethod?: string; // 操作方式
  requestParams?: string; // 操作提交的数据
  diffModifyData?: string; // 新旧数据比较结果
  bizType?: string; // 业务类型
  bizKey?: string; // 业务主键
  remoteAddr?: string; // 客户端IP
  serverAddr?: string; // 请求服务器地址
  isException?: string; // 是否异常
  exceptionInfo?: string; // 异常信息
  userAgent?: string; // 用户代理
  deviceName?: string; // 设备名称
  browserName?: string; // 浏览器名称
  executeTime?: number; // 响应时间
}

export const logList = (params?: Log | any) => defHttp.get<Log>({ url: adminPath + '/sys/log/list', params });

export const logListData = (params?: Log | any) =>
  defHttp.post<Page<Log>>({ url: adminPath + '/sys/log/listData', params });

export const logForm = (params?: Log | any) => defHttp.get<Log>({ url: adminPath + '/sys/log/form', params });
