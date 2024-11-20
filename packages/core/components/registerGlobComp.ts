import type { App } from 'vue';
import { Button } from './Button';
import { Input } from 'ant-design-vue';

export function registerGlobComp(app: App) {
  app.use(Input).use(Button);
}
