/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '/@/utils/http/axios';
import { useGlobSetting } from '/@/hooks/setting';
import { BasicModel, Page } from '../model/baseModel';

const { adminPath } = useGlobSetting();

export interface Post extends BasicModel<Post> {
  postCode?: string; // 岗位编码
  postName?: string; // 岗位名称
  postType?: string; // 岗位分类（高管、中层、基层）
  postSort?: number; // 岗位排序（升序）
  viewCode?: string; // 岗位代码
}

export const postList = (params?: Post | any) =>
  defHttp.get<Post>({ url: adminPath + '/sys/post/list', params });

export const postListData = (params?: Post | any) =>
  defHttp.post<Page<Post>>({ url: adminPath + '/sys/post/listData', params });

export const postForm = (params?: Post | any) =>
  defHttp.get<Post>({ url: adminPath + '/sys/post/form', params });

export const postDisable = (params?: Post | any) =>
  defHttp.get<Post>({ url: adminPath + '/sys/post/disable', params });

export const postEnable = (params?: Post | any) =>
  defHttp.get<Post>({ url: adminPath + '/sys/post/enable', params });

export const postSave = (params?: any, data?: Post | any) =>
  defHttp.postJson<Post>({ url: adminPath + '/sys/post/save', params, data });

export const postDelete = (params?: Post | any) =>
  defHttp.get<Post>({ url: adminPath + '/sys/post/delete', params });
