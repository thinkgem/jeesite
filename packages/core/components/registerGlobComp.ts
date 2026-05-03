import type { App } from 'vue';
import { withInstall } from '../utils';
import { Button } from './Button';
import { Input } from 'antdv-next';

export function registerGlobComp(app: App) {
  app.use(Input).use(withInstall(Button));
}
