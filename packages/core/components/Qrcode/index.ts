import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export * from './src/typing';
export const QrCode = createAsyncComponent(() => import('./src/Qrcode.vue'));
