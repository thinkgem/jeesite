import { isDevMode } from '@jeesite/core/utils/env';

// System default cache time, in seconds ( 60 days )
export const DEFAULT_CACHE_TIME = 60 * 60 * 24 * 60;

// aes encryption key（key and iv 16 bits）
export const cacheCipher = {
  key: '_11111000001111@',
  iv: '@11111000001111_',
};

// Whether the system cache is encrypted using aes
export const enableStorageEncryption = !isDevMode();
