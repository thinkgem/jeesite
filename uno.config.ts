import { defineConfig, UserConfig, presetTypography, presetIcons, transformerDirectives } from 'unocss';
import { FileSystemIconLoader } from '@iconify/utils/lib/loader/node-loaders';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { presetWind3 } from '@unocss/preset-wind3';
import { presetAntd } from '@antdv-next/unocss';

// 配置目录（等价 __dirname，兼容 Vite 的 native 配置加载器）
const currentDir = path.dirname(fileURLToPath(import.meta.url));

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
        svg: FileSystemIconLoader(currentDir + '/packages/assets/icons'),
      },
    }),
  ],
  transformers: [transformerDirectives()],
} as UserConfig);
