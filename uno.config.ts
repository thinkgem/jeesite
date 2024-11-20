import {
  defineConfig,
  presetTypography,
  presetUno,
  presetIcons,
  transformerDirectives,
} from 'unocss';
import { FileSystemIconLoader } from '@iconify/utils/lib/loader/node-loaders';

export default defineConfig({
  content: {
    pipeline: {
      include: ['**/*.vue', '**/*.tsx', '**/*.ts', '**/assets/*.js'],
      exclude: ['.git', '.idea', '.turbo', 'dist', 'node_modules', 'public'],
    },
  },
  presets: [
    presetUno(),
    presetTypography(),
    presetIcons({
      extraProperties: {
        display: 'inline-block',
        'vertical-align': 'middle',
      },
      collections: {
        svg: FileSystemIconLoader(__dirname + '/common/assets/icons'),
      },
    }),
  ],
  transformers: [transformerDirectives()],
});
