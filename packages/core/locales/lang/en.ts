import { genMessage } from '../helper';
import antdLocale from 'antdv-next/locale/en_US';

const modules = import.meta.glob('./en/**/*.ts', { eager: true });

export default {
  message: {
    ...genMessage(modules as Recordable<Recordable>, 'en'),
    antdLocale,
  },
  dateLocale: null,
  dateLocaleName: 'en',
};
