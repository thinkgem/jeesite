import { withInstall } from '@jeesite/core/utils';
import basicUpload from './src/BasicUpload.vue';

export { Upload } from 'ant-design-vue';
export const BasicUpload = withInstall(basicUpload);
