import Router from 'koa-router';
// import routes from './routes';
import cors from 'koa2-cors';
import body from 'koa-body';

const router = new Router();

function initRouter(app: any): Router<any, {}> {
  // routes.forEach((route) => router[route.method](route.path, route.action));
  app.use(cors());
  app.use(
    body({
      encoding: 'gzip',
      multipart: true,
      formidable: {
        // uploadDir: path.join(__dirname, '../../static/upload/'), // 设置文件上传目录
        keepExtensions: true,
        maxFieldsSize: 20 * 1024 * 1024,
      },
    }),
  );
  app.use(router.routes());
  app.use(router.allowedMethods());
  return router;
}

export { router };

export default initRouter;
