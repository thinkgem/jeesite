<template>
  <li :class="getClass" @click.stop="handleClickItem" :style="getCollapse ? {} : getItemStyle">
    <Tooltip placement="right" v-if="showTooptip">
      <template #title>
        <slot name="title"></slot>
      </template>
      <div :class="`${prefixCls}-tooltip`">
        <slot></slot>
      </div>
    </Tooltip>

    <template v-else>
      <slot></slot>
      <slot name="title"></slot>
    </template>
    <div style="position: absolute; top: 9px; right: 6px" v-if="getDemoMode && item.name == '角色管理'">
      <Badge count="hot" />
    </div>
  </li>
</template>
<script lang="ts" setup name="MenuItem">
  import { PropType, useSlots } from 'vue';
  import { defineComponent, ref, computed, unref, getCurrentInstance, watch } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { propTypes } from '/@/utils/propTypes';
  import { useMenuItem } from './useMenu';
  import { Badge, Tooltip } from 'ant-design-vue';
  import { useSimpleRootMenuContext } from './useSimpleMenuContext';
  import { useUserStore } from '/@/store/modules/user';

  const props = defineProps({
    name: {
      type: [String, Number] as PropType<string | number>,
      required: true,
    },
    item: {
      type: Object,
      default: {},
    },
    disabled: propTypes.bool,
  });

  const slots = useSlots();
  const userStore = useUserStore();
  const instance = getCurrentInstance();
  const active = ref(false);

  const { getItemStyle, getParentList, getParentMenu, getParentRootMenu } = useMenuItem(instance);
  const { prefixCls } = useDesign('menu');
  const { rootMenuEmitter, activeName } = useSimpleRootMenuContext();

  const getClass = computed(() => {
    return [
      `${prefixCls}-item`,
      {
        [`${prefixCls}-item-active`]: unref(active),
        [`${prefixCls}-item-selected`]: unref(active),
        [`${prefixCls}-item-disabled`]: !!props.disabled,
      },
    ];
  });

  const getCollapse = computed(() => unref(getParentRootMenu)?.props.collapse);

  const showTooptip = computed(() => {
    return unref(getParentMenu)?.type.name === 'Menu' && unref(getCollapse) && slots.title;
  });

  function handleClickItem() {
    const { disabled } = props;
    if (disabled) {
      return;
    }

    rootMenuEmitter.emit('on-menu-item-select', { name: props.name, item: props.item });
    if (unref(getCollapse)) {
      return;
    }
    const { uidList } = getParentList();

    rootMenuEmitter.emit('on-update-opened', {
      opend: false,
      parent: instance?.parent,
      uidList: uidList,
    });
  }
  watch(
    () => activeName.value,
    (name: string | number) => {
      if (name === props.name) {
        const { list, uidList } = getParentList();
        active.value = true;
        list.forEach((item) => {
          if (item.proxy) {
            (item.proxy as any).active = true;
          }
        });

        rootMenuEmitter.emit('on-update-active-name:submenu', uidList);
      } else {
        active.value = false;
      }
    },
    { immediate: true },
  );

  const getDemoMode = computed(() => {
    return userStore.getPageCacheByKey('demoMode');
  });
</script>
