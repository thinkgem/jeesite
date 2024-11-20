import { SizeEnum } from '@jeesite/core/enums/sizeEnum';

export interface LoadingProps {
  tip: string;
  size: SizeEnum;
  absolute: boolean;
  loading: boolean;
  background: string;
  theme: 'dark' | 'light';
}
