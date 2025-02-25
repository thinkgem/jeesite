import { withInstall } from '/@/utils';
import validCode from './src/ValidCode.vue';

export const ValidCode = withInstall(validCode);
export type ValidCodeInstance = InstanceType<typeof validCode>;
