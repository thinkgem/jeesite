import { defineBuildConfig } from 'unbuild';

export default defineBuildConfig({
  clean: true,
  declaration: true,
  entries: [
    './index',
    './config/getEnvConfigName',
    './theme/themeConfig',
  ],
});
