import { withInstall } from '@jeesite/core/utils';
import basicDrawer from './src/BasicDrawer.vue';

export const BasicDrawer = withInstall(basicDrawer);
export type BasicDrawerInstance = InstanceType<typeof basicDrawer>;

export * from './src/typing';
export { useDrawer, useDrawerInner } from './src/useDrawer';
