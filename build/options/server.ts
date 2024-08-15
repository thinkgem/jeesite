/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { ServerOptions, ProxyOptions } from 'vite';
import { IncomingMessage } from 'node:http';

type ProxyItem = [string, string, boolean];
type ProxyList = ProxyItem[];
type ProxyTargetList = Record<string, ProxyOptions>;
const httpsRE = /^https:\/\//;

export function createServerOptions(viteEnv: ViteEnv): ServerOptions {
  return {
    https: false as any,
    open: false,
    host: true,
    port: viteEnv.VITE_PORT,
    proxy: createProxy(viteEnv.VITE_PROXY),
    warmup: {
      clientFiles: ['./index.html', './src/{views,components}/*'],
    },
  };
}

/**
 * Used to parse the .env.development proxy configuration
 */
export function createProxy(list: ProxyList = []) {
  const ret: ProxyTargetList = {};
  for (const [prefix, target, changeOrigin] of list) {
    const isHttps = httpsRE.test(target);
    // https://github.com/http-party/node-http-proxy#options
    ret[prefix] = {
      target: target,
      changeOrigin,
      ws: true,
      // https is require secure=false
      ...(isHttps ? { secure: false } : {}),
      rewrite: (path) => path.replace(new RegExp(`^${prefix}`), ''),
      bypass: (req: IncomingMessage) => {
        if (req.method === 'GET') {
          req['url'] = req['originalUrl'];
        }
      },
    };
  }
  return ret;
}
