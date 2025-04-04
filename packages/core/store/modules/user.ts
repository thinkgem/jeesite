/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import type { UserInfo } from '@jeesite/types/store';
import type { ErrorMessageMode } from '@jeesite/types/axios';
import { defineStore } from 'pinia';
import { store } from '@jeesite/core/store';
import { RoleEnum } from '@jeesite/core/enums/roleEnum';
import { PageEnum } from '@jeesite/core/enums/pageEnum';
import { TOKEN_KEY, ROLES_KEY, USER_INFO_KEY, SESSION_TIMEOUT_KEY } from '@jeesite/core/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '@jeesite/core/utils/auth';
import { loginApi, logoutApi, userInfoApi, LoginParams, LoginResult } from '@jeesite/core/api/sys/login';
// import { useI18n } from '@jeesite/core/hooks/web/useI18n';
import { useMessage } from '@jeesite/core/hooks/web/useMessage';
import { router } from '@jeesite/core/router';
import { usePermissionStore } from '@jeesite/core/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '@jeesite/core/router/routes/basic';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import logoImg from '@jeesite/assets/images/logo.png';
import { mitt, Emitter } from '@jeesite/core/utils/mitt';

const { showMessage, createConfirm } = useMessage();

interface UserState {
  userInfo: Nullable<UserInfo>;
  token?: string;
  roleList: RoleEnum[] | string[];
  sessionTimeout?: boolean;
  lastUpdateTime: number;
  pageCache: any;
  emitter: Emitter<any>;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token
    token: undefined,
    // roleList
    roleList: [],
    // Whether the login expired
    sessionTimeout: undefined,
    // Last fetch time
    lastUpdateTime: 0,
    // 刷新页面及销毁的缓存
    pageCache: {},
    // 全局事件 think gem
    emitter: mitt(),
  }),
  getters: {
    getUserInfo(): UserInfo {
      return this.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {};
    },
    getToken(): string {
      return this.token || getAuthCache<string>(TOKEN_KEY);
    },
    getRoleList(): RoleEnum[] | string[] {
      return this.roleList.length > 0 ? this.roleList : getAuthCache<RoleEnum[] | string[]>(ROLES_KEY);
    },
    getSessionTimeout(): boolean {
      return !!(this.sessionTimeout || getAuthCache<boolean>(SESSION_TIMEOUT_KEY));
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime;
    },
    getPageCache(): any {
      return this.pageCache;
    },
    getEmitter(): any {
      return this.emitter;
    },
  },
  actions: {
    setToken(token: string | undefined) {
      this.token = token ? token : ''; // for null or undefined value
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(TOKEN_KEY, token);
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
      setAuthCache(SESSION_TIMEOUT_KEY, flag);
    },
    setUserInfo(res: Recordable | null) {
      const info: UserInfo = res?.user;
      if (res && info) {
        const { ctxPath } = useGlobSetting();
        let url = info.avatarUrl || '/ctxPath/static/images/user1.jpg';
        url = url.replace('/ctxPath/', ctxPath + '/');
        info.avatarUrl = url || logoImg;
        info.homePath = res.desktopUrl;
        info.roleList = res.roleList;
        info.postList = res.postList;
      }
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setRoleList(roleList: RoleEnum[] | string[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    resetState() {
      this.userInfo = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = true;
    },
    setPageCache(key: string, value: any) {
      this.pageCache[key] = value;
    },
    getPageCacheByKey(key: string, defaultValue?: any): any {
      if (!this.pageCache[key] && defaultValue) {
        this.pageCache[key] = defaultValue;
      }
      return this.pageCache[key];
    },
    async login(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ) {
      const { goHome = true, mode, ...loginParams } = params;
      const res = await loginApi(loginParams, mode);
      if (res.result !== 'true') {
        showMessage(res.message);
        this.initPageCache(res);
        return res;
      }
      await this.afterLoginAction(res, goHome);
      return res;
    },
    // async afterLoginAction(goHome?: boolean): Promise<GetUserInfoModel | null> {
    async afterLoginAction(res: LoginResult, goHome?: boolean) {
      this.setUserInfo(res);
      this.initPageCache(res);
      this.setSessionTimeout(false);
      const permissionStore = usePermissionStore();
      if (!permissionStore.isDynamicAddedRoute) {
        const routes = await permissionStore.buildRoutesAction();
        routes.forEach((route) => {
          router.addRoute(route as unknown as RouteRecordRaw);
        });
        router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
        permissionStore.setDynamicAddedRoute(true);
      }
      if (goHome) {
        const currentRoute = router.currentRoute.value;
        let path = currentRoute.query.redirect;
        if (path !== '/') {
          path = path || res.user?.homePath || PageEnum.BASE_HOME;
        } else {
          path = res.user?.homePath || PageEnum.BASE_HOME;
        }
        await router.replace(decodeURIComponent(path as string));
      }
      if (res['modifyPasswordTip']) {
        createConfirm({
          content: res['modifyPasswordTip'],
          maskClosable: false,
          iconType: 'info',
          cancelText: '取消',
          okText: '确定',
          onOk: () => {
            router.replace('/account/modPwd');
          },
        });
      }
      return res.user || null;
    },
    async getUserInfoAction() {
      // if (!this.getToken) return null;
      const res = await userInfoApi();
      this.setUserInfo(res);
      this.initPageCache(res);
      // this.setRoleList(roleList);
      this.setSessionTimeout(false);
      return res.user;
    },
    initPageCache(res: LoginResult) {
      this.setPageCache('demoMode', res.demoMode);
      this.setPageCache('useCorpModel', res.useCorpModel);
      this.setPageCache('modifyPasswordTip', res.modifyPasswordTip);
      this.setPageCache('modifyPasswordMsg', res.modifyPasswordMsg);
      this.setPageCache('sysCode', res.sysCode);
      this.setPageCache('roleCode', res.roleCode);
      this.setPageCache('postCode', res.postCode);
      this.setPageCache('title', res.title);
      this.setPageCache('company', res.company);
      this.setPageCache('version', res.version);
      this.setPageCache('year', res.year);
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await logoutApi();
        } catch {
          console.log('注销Token失败');
        }
      }
      this.setToken(undefined);
      this.setSessionTimeout(true);
      this.setUserInfo(null);
      this.setRoleList([]);
      goLogin && (await router.replace(PageEnum.BASE_LOGIN));
    },
    /**
     * @description: Confirm before logging out
     */
    async confirmLoginOut() {
      // const { createConfirm } = useMessage();
      // const { t } = useI18n();
      // createConfirm({
      //   iconType: 'warning',
      //   title: () => h('span', t('sys.app.logoutTip')),
      //   content: () => h('span', t('sys.app.logoutMessage')),
      //   onOk: async () => {
      await this.logout(true);
      //   },
      // });
    },
  },
});

// Global emit by think gem
export function useEmitter() {
  return useUserStore().emitter;
}

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}
