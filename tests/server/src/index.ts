import path from 'path';
import Koa from 'koa';
import koaStatic from 'koa-static';
import koaWebsocket from 'koa-websocket';
import websocket from './ws';
import config from './config';
import router from './router';
import './router/routes';

const app = koaWebsocket(new Koa());

websocket(app);

router(app);

app.use(koaStatic(path.join(__dirname, '../')));

app.listen(config.port, () => {
  console.log(`Application started successfully: http://${config.host}:${config.port}`);
});
