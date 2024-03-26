import { defineConfig, presetTypography, presetUno, presetIcons } from 'unocss';

export default defineConfig({
  presets: [presetUno(), presetTypography(), presetIcons()],
});
