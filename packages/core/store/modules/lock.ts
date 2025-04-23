import type { LockInfo } from '@jeesite/types/store';

import { defineStore } from 'pinia';

import { LOCK_INFO_KEY } from '@jeesite/core/enums/cacheEnum';
import { Persistent } from '@jeesite/core/utils/cache/persistent';
import { useUserStore } from './user';

interface LockState {
  lockInfo: Nullable<LockInfo>;
}

export const useLockStore = defineStore('app-lock', {
  state: (): LockState => ({
    lockInfo: Persistent.getLocal(LOCK_INFO_KEY),
  }),
  getters: {
    getLockInfo(): Nullable<LockInfo> {
      return this.lockInfo;
    },
  },
  actions: {
    setLockInfo(info: LockInfo) {
      this.lockInfo = Object.assign({}, this.lockInfo, info);
      Persistent.setLocal(LOCK_INFO_KEY, this.lockInfo, true);
    },
    resetLockInfo() {
      Persistent.removeLocal(LOCK_INFO_KEY, true);
      this.lockInfo = null;
    },
    // Unlock
    async unLock(password?: string) {
      const userStore = useUserStore();
      if (this.lockInfo?.pwd === password) {
        this.resetLockInfo();
        return true;
      }
      const tryLogin = async () => {
        try {
          const username = userStore.getUserInfo?.loginCode;
          const res = await userStore.login({
            username: username,
            password: password!,
            goHome: false,
            mode: 'none',
          });
          if (res.result == 'true') {
            this.resetLockInfo();
            return true;
          }
        } catch (error: any) {
          console.log(error);
        }
        return false;
      };
      return await tryLogin();
    },
  },
});
