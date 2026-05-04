import { genMessage } from '../helper';
import antdLocale from 'antdv-next/locale/zh_CN';

const modules = import.meta.glob('./zh_CN/**/*.ts', { eager: true });

export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'zh_CN'),
    antdLocale,
  },
  dateLocale: null,
  dateLocaleName: 'zh-cn',
};
