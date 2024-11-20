import { withInstall } from '@jeesite/core/utils';
import basicDialog from './src/BasicDialog.vue';

export const BasicDialog = withInstall(basicDialog);
export type BasicDialogInstance = InstanceType<typeof basicDialog>;
