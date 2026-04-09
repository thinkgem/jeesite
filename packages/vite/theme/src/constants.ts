import { normalizePath } from 'vite';

export const VITE_CLIENT_ENTRY = '/@vite/client';

export const VITE_PLUGIN_THEME_CLIENT_ENTRY = normalizePath('@jeesite/vite/theme/client/');

export const CLIENT_PUBLIC_ABSOLUTE_PATH = normalizePath(`/${VITE_PLUGIN_THEME_CLIENT_ENTRY}/client`);

export const commentRE = /\\\\?n|\n|\\\\?r|\/\*[\s\S]+?\*\//g;

export const colorRE = /#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})|rgba?\((.*),\s*(.*),\s*(.*)(?:,\s*(.*(?:.*)?))?\)/gi;

export const linkID = '__VITE_PLUGIN_THEME-ANTD_DARK_THEME_LINK__';

export const cssVariableString = `const css = "`;

export const cssBlockRE = /[^}]*\{[^{]*\}/g;

export const cssLangRE = new RegExp(`\\.(css|less|sass|scss|styl|stylus|postcss)($|\\?)`);
export const ruleRE = /(\w+-)*\w+:/;
export const cssValueRE = /(\s?[a-z0-9]+\s)*/;
export const safeEmptyRE = /\s?/;
export const importSafeRE = /(\s*!important)?/;
