import type { ResolvedConfig } from 'vite';
import { ViteThemeOptions } from './index';
import { AntdDarkThemeOption } from './antdDarkThemePlugin';
import { CLIENT_PUBLIC_ABSOLUTE_PATH } from './constants';

export interface ViteThemeContext {
  colorThemeFileName: string;
  colorThemeOptions: ViteThemeOptions;
  antdThemeFileName: string;
  antdThemeOptions: AntdDarkThemeOption;
  viteOptions: ResolvedConfig;
  devEnvironment: boolean;
  needSourceMap: boolean;
  injectClientPath: string;
}

const context: ViteThemeContext = {
  colorThemeFileName: '',
  antdThemeFileName: '',
  viteOptions: undefined!,
  colorThemeOptions: undefined!,
  antdThemeOptions: undefined!,
  devEnvironment: false,
  needSourceMap: false,
  injectClientPath: JSON.stringify(CLIENT_PUBLIC_ABSOLUTE_PATH),
};

export function createContext(options?: Partial<ViteThemeContext>): ViteThemeContext {
  if (options) Object.assign(context, options);
  return context;
}
