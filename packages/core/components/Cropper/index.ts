import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export * from './src/typing';
export const CropperImage = createAsyncComponent(() => import('./src/Cropper.vue'));
export const CropperAvatar = createAsyncComponent(() => import('./src/CropperAvatar.vue'));
