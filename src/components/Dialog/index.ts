import { withInstall } from '/@/utils';
import basicDialog from './src/BasicDialog.vue';

export const BasicDialog = withInstall(basicDialog);
export type BasicDialogInstance = InstanceType<typeof basicDialog>;
