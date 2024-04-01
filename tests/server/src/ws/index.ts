import route from 'koa-route';

function initWebsocket(app: any) {
  app.ws.use(function (ctx, next) {
    ctx.websocket.send('connection succeeded!');
    return next(ctx);
  });

  app.ws.use(
    route.all('/test', function (ctx) {
      // ctx.websocket.send('Hello World');
      ctx.websocket.on('message', function (message) {
        // do something with the message from client
        if (message !== 'ping') {
          const data = JSON.stringify({
            id: Math.ceil(Math.random() * 1000),
            time: new Date().getTime(),
            res: `${message}`,
          });
          ctx.websocket.send(data);
        }
        console.log(message);
      });
    }),
  );
}

export default initWebsocket;
