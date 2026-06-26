import { createAsyncComponent } from '@jeesite/core/utils/factory/createAsyncComponent';

export const StrengthMeter = createAsyncComponent(() => import('./src/StrengthMeter.vue'));
