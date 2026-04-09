import { getThemeColors, generateColors } from '@jeesite/vite/theme/themeConfig';

import { replaceStyleVariables } from '@jeesite/vite/theme/client/client';
import { mixLighten, mixDarken, tinycolor } from '@jeesite/vite/theme/client/colorUtils';
import { setCssVar } from '@jeesite/core/logics/theme/util';

export async function changeTheme(color: string) {
  const colors = generateColors({
    mixDarken,
    mixLighten,
    tinycolor,
    color,
  });

  setCssVar('--primary-color', color);

  return await replaceStyleVariables({
    colorVariables: [...getThemeColors(color), ...colors],
  });
}
