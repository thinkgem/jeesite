import { createProdMockServer } from 'vite-plugin-mock/client';

import user from './demo/user';
import menu from './demo/menu';

export function setupProdMockServer() {
  const mockList: any[] = [...user, ...menu];
  createProdMockServer(mockList);
}
