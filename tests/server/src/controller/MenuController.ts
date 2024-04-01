import MenuService from '../service/MenuService';
import { router } from '../router';

const service: MenuService = new MenuService();

router.get('/menuRoute', async (ctx) => {
  ctx.body = await service.menuRoute(ctx);
});
