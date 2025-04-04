import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
import FullScreen from './FullScreen.vue';
import UserDropDown from './user-dropdown/index.vue';

export const LayoutBreadcrumb = createAsyncComponent(() => import('./Breadcrumb.vue'));

export const Notify = createAsyncComponent(() => import('./notify/index.vue'));

export const ErrorAction = createAsyncComponent(() => import('./ErrorAction.vue'));

export const OnlineCount = createAsyncComponent(() => import('./OnlineCount.vue'));

export const SettingDrawer = createAsyncComponent(() => import('/@/layouts/default/setting/index.vue'), {
  loading: true,
});

export { FullScreen, UserDropDown };
