import { InjectionKey } from 'vue';
import { createContext, useContext } from '@jeesite/core/hooks/core/useContext';

export interface ModalContextProps {
  redoModalHeight: () => void;
}

const key: InjectionKey<ModalContextProps> = Symbol();

export function createModalContext(context: ModalContextProps) {
  return createContext<ModalContextProps>(context, key);
}

export function useModalContext() {
  return useContext<ModalContextProps>(key);
}
