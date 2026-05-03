<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div :class="prefixCls">
    <SpaceCompact block>
      <a-input
        :placeholder="t('component.icon.placeholder')"
        :class="`${prefixCls}-input`"
        v-model:value="currentSelect"
      />
      <SpaceAddon>
        <Popover placement="bottom" trigger="click" v-model="open" :classes="{ container: `${prefixCls}-popover` }">
          <template #title>
            <div class="flex justify-between">
              <a-input :placeholder="t('component.icon.search')" @change="debounceHandleSearchChange" allowClear />
            </div>
          </template>
          <template #content>
            <div v-if="getPaginationList.length">
              <ul class="content flex flex-wrap flex-content-start">
                <li
                  v-for="icon in getPaginationList"
                  :key="icon"
                  :class="currentSelect === icon ? 'active' : ''"
                  class="flex cursor-pointer items-center justify-center"
                  @click="handleClick(icon)"
                  :title="icon"
                >
                  <Icon :icon="icon" />
                </li>
              </ul>
              <div class="flex items-center justify-center py-2" v-if="getTotal >= pageSize">
                <Pagination
                  size="small"
                  :pageSize="pageSize"
                  :total="getTotal"
                  :showLessItems="true"
                  :showSizeChanger="false"
                  @change="handlePageChange"
                />
              </div>
            </div>
            <template v-else>
              <div class="p-5"><Empty /></div>
            </template>
          </template>
          <span class="flex cursor-pointer items-center px-1 py-1">
            <Icon :icon="currentSelect || 'ion:apps-outline'" />
          </span>
        </Popover>
      </SpaceAddon>
    </SpaceCompact>
  </div>
</template>
<script lang="ts" setup>
  import { ref, watchEffect, watch, unref } from 'vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { Popover, Pagination, Empty, SpaceCompact, SpaceAddon } from 'antdv-next';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { usePagination } from '@jeesite/core/hooks/web/usePagination';
  import { useDebounceFn } from '@vueuse/core';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useCopyToClipboard } from '@jeesite/core/hooks/web/useCopyToClipboard';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import IconData from './IconData';
  import Icon from './Icon.vue';

  // 直接使用导入的组件，Vue 3 script setup 会自动注册

  function getIcons() {
    const data = IconData as any;
    const prefix: string = data?.prefix ?? '';
    let result: string[] = [];
    if (prefix) {
      result = (data?.icons ?? []).map((item) => `${prefix}:${item}`);
    } else if (Array.isArray(IconData)) {
      result = IconData as string[];
    }
    return result;
  }

  const props = defineProps({
    value: propTypes.string,
    width: propTypes.string.def('100%'),
    pageSize: propTypes.number.def(70),
    copy: propTypes.bool.def(false),
  });

  const emit = defineEmits(['change', 'update:value']);

  const icons = getIcons();

  const currentSelect = ref('');
  const open = ref(false);
  const currentList = ref(icons);

  const { t } = useI18n();
  const { prefixCls } = useDesign('icon-picker');

  const debounceHandleSearchChange = useDebounceFn(handleSearchChange, 100) as any;
  const { clipboardRef, isSuccessRef } = useCopyToClipboard(props.value);
  const { createMessage } = useMessage();

  const { getPaginationList, getTotal, setCurrentPage } = usePagination(currentList, props.pageSize);

  watchEffect(() => {
    currentSelect.value = props.value;
  });

  watch(
    () => currentSelect.value,
    (v) => {
      emit('update:value', v);
      return emit('change', v);
    },
  );

  function handlePageChange(page: number) {
    setCurrentPage(page);
  }

  function handleClick(icon: string) {
    if (icon.startsWith('i-')) {
      icon = icon.substring(2);
    }
    currentSelect.value = icon;
    if (props.copy) {
      clipboardRef.value = icon;
      if (unref(isSuccessRef)) {
        createMessage.success(t('component.icon.copy'));
      }
    }
  }

  function handleSearchChange(e: ChangeEvent) {
    const value = e.target.value;
    if (!value) {
      setCurrentPage(1);
      currentList.value = icons;
      return;
    }
    currentList.value = icons.filter((item) => item.includes(value));
  }
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-icon-picker';

  .@{prefix-cls} {
    width: 100%;

    &-input {
      padding-right: 3px;
    }

    .ant-input-group-addon {
      padding: 0;

      > span {
        width: 30px;
        height: 30px;
      }
    }

    &-popover {
      width: 540px;

      .content {
        border: 1px solid @border-color-base;
        border-radius: 8px;
        margin-bottom: 0;
        padding: 5px;

        li {
          width: 32px;
          height: 32px;
          font-size: 16px;
          border: 1px solid @border-color-base;
          border-radius: 4px;
          margin: 2px;

          &.active {
            border-color: @primary-color;
          }
        }
      }
    }
  }
</style>
