import { defineConfig, UserConfig, presetTypography, presetIcons, transformerDirectives } from 'unocss';
import { FileSystemIconLoader } from '@iconify/utils/lib/loader/node-loaders';
import { presetWind3 } from '@unocss/preset-wind3';
import { presetAntd } from '@antdv-next/unocss';

export default defineConfig({
  inspector: false,
  content: {
    pipeline: {
      include: ['**/*.vue', '**/*.tsx', '**/*.ts'],
      exclude: ['.git', '.idea', '.turbo', 'node_modules', 'public'],
    },
  },
  presets: [
    presetAntd(),
    presetWind3(),
    presetTypography(),
    presetIcons({
      extraProperties: {
        display: 'inline-block',
        'vertical-align': 'middle',
      },
      collections: {
        svg: FileSystemIconLoader(__dirname + '/packages/assets/icons'),
      },
    }),
  ],
  transformers: [transformerDirectives()],
} as UserConfig);
