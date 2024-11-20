import UserService from '../service/UserService';
import { router } from '../router';

const service: UserService = new UserService();

router.get('/login', async (ctx) => {
  ctx.body = await service.login(ctx);
});

router.get('/index', async (ctx) => {
  ctx.body = await service.index(ctx);
});
