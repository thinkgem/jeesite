import type { defineComponent } from 'vue';
import type { ComponentType } from '../../types/componentType';
import { componentMap } from '/@/components/Table/src/componentMap';

import { Popover } from 'ant-design-vue';
import { h } from 'vue';

export interface ComponentProps {
  component: ComponentType;
  rule: boolean;
  popoverOpen: boolean;
  ruleMessage: string;
  getPopupContainer?: Fn;
}

export const CellComponent = (
  { component = 'Input', rule = true, ruleMessage, popoverOpen }: ComponentProps,
  { attrs },
) => {
  const Comp = componentMap.get(component) as typeof defineComponent;

  const DefaultComp = h(Comp, attrs);
  if (!rule) {
    return DefaultComp;
  }
  return h(
    Popover,
    {
      overlayClassName: 'edit-cell-rule-popover',
      open: !!popoverOpen,
      //...(getPopupContainer ? { getPopupContainer } : {}),
      placement: 'right',
      autoAdjustOverflow: false,
      getPopupContainer: (trigger: HTMLElement | any) => {
        return trigger?.parentElement;
      },
    },
    {
      default: () => DefaultComp,
      content: () => ruleMessage,
    },
  );
};
