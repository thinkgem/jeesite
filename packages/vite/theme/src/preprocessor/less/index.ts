import path from 'path';
import fs from 'fs';
import { Alias, normalizePath, ResolvedConfig } from 'vite';
import less from 'less';

const less2 = less['default'] || less;

export type ResolveFn = (id: string, importer?: string, aliasOnly?: boolean) => Promise<string | undefined>;

type CssUrlReplacer = (url: string, importer?: string) => string | Promise<string>;

export const externalRE = /^(https?:)?\/\//;
export const isExternalUrl = (url: string) => externalRE.test(url);

export const dataUrlRE = /^\s*data:/i;
export const isDataUrl = (url: string) => dataUrlRE.test(url);

const cssUrlRE = /url\(\s*('[^']+'|"[^"]+"|[^'")]+)\s*\)/;

let ViteLessManager: any;

function createViteLessPlugin(rootFile: string, alias: Alias[], resolvers: { less: ResolveFn }): Less.Plugin {
  if (!ViteLessManager) {
    ViteLessManager = class ViteManager extends less2.FileManager {
      resolvers;
      rootFile;
      alias;
      constructor(rootFile: string, resolvers: ResolveFn, alias: Alias[]) {
        super();
        this.rootFile = rootFile;
        this.resolvers = resolvers;
        this.alias = alias;
      }
      supports() {
        return true;
      }
      supportsSync() {
        return false;
      }
      async loadFile(filename: string, dir: string, opts: any, env: any): Promise<Less.FileLoadResult> {
        const resolved = await this.resolvers.less(filename, path.join(dir, '*'));
        if (resolved) {
          const result = await rebaseUrls(resolved, this.rootFile, this.alias);
          let contents: string;
          if (result && 'contents' in result) {
            contents = result.contents;
          } else {
            contents = fs.readFileSync(resolved, 'utf-8');
          }
          return {
            filename: path.resolve(resolved),
            contents,
          };
        } else {
          return super.loadFile(filename, dir, opts, env);
        }
      }
    };
  }

  return {
    install(_, pluginManager) {
      pluginManager.addFileManager(new ViteLessManager(rootFile, resolvers, alias));
    },
    minVersion: [3, 0, 0],
  };
}

export function lessPlugin(id, config: ResolvedConfig) {
  const resolvers = createCSSResolvers(config);
  return createViteLessPlugin(id, config.resolve.alias, resolvers);
}

function createCSSResolvers(config: ResolvedConfig): { less: ResolveFn } {
  let lessResolve: ResolveFn | undefined;
  return {
    get less() {
      return (
        lessResolve ||
        (lessResolve = config.createResolver({
          extensions: ['.less', '.css'],
          mainFields: ['less', 'style'],
          tryIndex: false,
          preferRelative: true,
        }))
      );
    },
  };
}

/**
 * relative url() inside \@imported sass and less files must be rebased to use
 * root file as base.
 */
async function rebaseUrls(file: string, rootFile: string, alias: Alias[]): Promise<any> {
  file = path.resolve(file); // ensure os-specific flashes
  // in the same dir, no need to rebase
  const fileDir = path.dirname(file);
  const rootDir = path.dirname(rootFile);
  if (fileDir === rootDir) {
    return { file };
  }
  // no url()
  const content = fs.readFileSync(file, 'utf-8');
  if (!cssUrlRE.test(content)) {
    return { file };
  }
  const rebased = await rewriteCssUrls(content, (url) => {
    if (url.startsWith('/')) return url;
    // match alias, no need to rewrite
    for (const { find } of alias) {
      const matches = typeof find === 'string' ? url.startsWith(find) : find.test(url);
      if (matches) {
        return url;
      }
    }
    const absolute = path.resolve(fileDir, url);
    const relative = path.relative(rootDir, absolute);
    return normalizePath(relative);
  });
  return {
    file,
    contents: rebased,
  };
}

function rewriteCssUrls(css: string, replacer: CssUrlReplacer): Promise<string> {
  return asyncReplace(css, cssUrlRE, async (match) => {
    const [matched, rawUrl] = match;
    return await doUrlReplace(rawUrl, matched, replacer);
  });
}

export async function asyncReplace(
  input: string,
  re: RegExp,
  replacer: (match: RegExpExecArray) => string | Promise<string>,
) {
  let match: RegExpExecArray | null;
  let remaining = input;
  let rewritten = '';
  while ((match = re.exec(remaining))) {
    rewritten += remaining.slice(0, match.index);
    rewritten += await replacer(match);
    remaining = remaining.slice(match.index + match[0].length);
  }
  rewritten += remaining;
  return rewritten;
}

async function doUrlReplace(rawUrl: string, matched: string, replacer: CssUrlReplacer) {
  let wrap = '';
  const first = rawUrl[0];
  if (first === `"` || first === `'`) {
    wrap = first;
    rawUrl = rawUrl.slice(1, -1);
  }
  if (isExternalUrl(rawUrl) || isDataUrl(rawUrl) || rawUrl.startsWith('#')) {
    return matched;
  }

  return `url(${wrap}${await replacer(rawUrl)}${wrap})`;
}
