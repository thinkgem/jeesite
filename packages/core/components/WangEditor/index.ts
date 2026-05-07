import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export const WangEditor = createAsyncComponent(() => import('./src/WangEditor.vue'));
