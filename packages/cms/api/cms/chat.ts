/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { AxiosProgressEvent, GenericAbortSignal } from 'axios';

const { adminPath } = useGlobSetting();

export const cmsChatMessage = (params?: Recordable | any) =>
  defHttp.get<Recordable[]>({ url: adminPath + '/cms/chat/message', params });

export const cmsChatList = (params?: Recordable | any) =>
  defHttp.get<Recordable[]>({ url: adminPath + '/cms/chat/list', params });

export const cmsChatSave = (params?: Recordable | any) =>
  defHttp.post<Recordable>({ url: adminPath + '/cms/chat/save', params });

export const cmsChatDelete = (params?: Recordable | any) =>
  defHttp.get<Recordable>({ url: adminPath + '/cms/chat/delete', params });

export const cmsChatStream = (
  params?: Recordable | any,
  signal?: GenericAbortSignal,
  onDownloadProgress?: (progressEvent: AxiosProgressEvent) => void,
) =>
  defHttp.post<Recordable>({
    url: adminPath + '/cms/chat/stream',
    params,
    signal,
    onDownloadProgress,
    responseType: 'stream',
    headers: {
      'x-ajax': 'event-stream',
    },
  });
