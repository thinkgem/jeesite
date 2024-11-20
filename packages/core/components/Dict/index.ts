import { withInstall } from '@jeesite/core/utils';
import dictLabel from './src/DictLabel.vue';

export * from './src/useDict';
export const DictLabel = withInstall(dictLabel);
