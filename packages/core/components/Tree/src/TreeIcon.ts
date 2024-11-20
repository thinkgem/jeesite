import type { VNode } from 'vue';

import { h } from 'vue';
import { isString } from '@jeesite/core/utils/is';
import { Icon } from '@jeesite/core/components/Icon';

export const TreeIcon = ({ icon }: { icon: VNode | string | undefined }) => {
  if (!icon) return null;
  if (isString(icon)) {
    return h(Icon, { icon, class: 'mr-1' });
  }
  return h(Icon);
};
