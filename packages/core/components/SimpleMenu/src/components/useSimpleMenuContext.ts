import type { InjectionKey, Ref } from 'vue';
import type { Emitter } from '@jeesite/core/utils/mitt';
import { createContext, useContext } from '@jeesite/core/hooks/core/useContext';

export interface SimpleRootMenuContextProps {
  rootMenuEmitter: Emitter<any>;
  activeName: Ref<string | number>;
}

const key: InjectionKey<SimpleRootMenuContextProps> = Symbol();

export function createSimpleRootMenuContext(context: SimpleRootMenuContextProps) {
  return createContext<SimpleRootMenuContextProps>(context, key, { readonly: false, native: true });
}

export function useSimpleRootMenuContext() {
  return useContext<SimpleRootMenuContextProps>(key);
}
