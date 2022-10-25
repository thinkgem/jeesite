/**
 * Used to parse the .env.development proxy configuration
 */
import type { ProxyOptions } from 'vite';
import { IncomingMessage } from 'node:http';

type ProxyItem = [string, string, boolean];

type ProxyList = ProxyItem[];

type ProxyTargetList = Record<string, ProxyOptions>;

const httpsRE = /^https:\/\//;

/**
 * Generate proxy
 * @param list
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
