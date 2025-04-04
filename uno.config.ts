import { defineConfig, UserConfig, presetTypography, presetIcons, transformerDirectives } from 'unocss';
import { FileSystemIconLoader } from '@iconify/utils/lib/loader/node-loaders';
import { presetWind3 } from '@unocss/preset-wind3';

export default defineConfig({
  inspector: false,
  content: {
    pipeline: {
      include: ['**/*.vue', '**/*.tsx', '**/*.ts', '**/*-lib/**/*.mjs'],
      exclude: ['.git', '.idea', '.turbo', 'node_modules', 'public'],
    },
  },
  presets: [
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
