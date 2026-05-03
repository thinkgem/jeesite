<template>
  <Dropdown :trigger="trigger" v-bind="$attrs">
    <span>
      <slot></slot>
    </span>
    <template #popupRender>
      <Menu :selectedKeys="selectedKeys">
        <template v-for="(item, index) in dropMenuList" :key="`${item.event}`">
          <MenuItem v-bind="getAttr(`item-${index}`)" @click="handleClickMenu(item, $event)" :disabled="item.disabled">
            <Popconfirm v-if="popconfirm && item.popConfirm" v-bind="getPopConfirmAttrs(item.popConfirm)">
              <template #icon v-if="item.popConfirm.icon">
                <Icon :icon="item.popConfirm.icon" />
              </template>
              <div>
                <Icon :icon="item.icon" v-if="item.icon" />
                <span v-if="item.text" class="ml-1">{{ item.text }}</span>
              </div>
            </Popconfirm>
            <template v-else>
              <Icon :icon="item.icon" v-if="item.icon" />
              <span v-if="item.text" class="ml-1 mr-1">{{ item.text }}</span>
            </template>
          </MenuItem>
          <MenuDivider v-if="item.divider" :key="`d-${item.event}`" />
        </template>
      </Menu>
    </template>
  </Dropdown>
</template>

<script lang="ts" setup>
  import { computed, PropType } from 'vue';
  import type { DropMenu } from './typing';
  import { Dropdown, Menu, MenuItem, MenuDivider, Popconfirm } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { omit } from 'lodash-es';
  import { isFunction } from '@jeesite/core/utils/is';

  const props = defineProps({
    popconfirm: Boolean,
    /**
     * the trigger mode which executes the drop-down action
     * @default ['hover']
     * @type string[]
     */
    trigger: {
      type: [Array] as PropType<('contextmenu' | 'click' | 'hover')[]>,
      default: () => {
        return ['contextmenu'];
      },
    },
    dropMenuList: {
      type: Array as PropType<(DropMenu & Recordable)[]>,
      default: () => [],
    },
    selectedKeys: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
  });

  const emit = defineEmits(['menuEvent']);

  const getAttr = (key: string | number) => ({ key });

  function handleClickMenu(item: DropMenu, _info?: any) {
    // 如果 _info 是原生事件（PointerEvent/MouseEvent），说明是第一次调用，跳过
    // 只有当 _info 是 MenuInfo 对象时才处理
    if (_info && !_info.key && !_info.keyPath) {
      return;
    }

    const { event } = item;
    const menu = props.dropMenuList.find((item) => `${item.event}` === `${event}`);
    emit('menuEvent', menu);
    item.onClick?.();
  }

  const getPopConfirmAttrs = computed(() => {
    return (attrs) => {
      const originAttrs = omit(attrs, ['confirm', 'cancel', 'icon']);
      if (!attrs.onConfirm && attrs.confirm && isFunction(attrs.confirm)) originAttrs['onConfirm'] = attrs.confirm;
      if (!attrs.onCancel && attrs.cancel && isFunction(attrs.cancel)) originAttrs['onCancel'] = attrs.cancel;
      return originAttrs;
    };
  });
</script>
