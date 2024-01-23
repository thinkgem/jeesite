<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <a-input
    disabled
    :style="{ width }"
    :placeholder="t('component.icon.placeholder')"
    :class="prefixCls"
    v-model:value="currentSelect"
  >
    <template #addonAfter>
      <a-popover
        placement="bottomLeft"
        trigger="click"
        v-model="open"
        :overlayClassName="`${prefixCls}-popover`"
      >
        <template #title>
          <div class="flex justify-between">
            <a-input
              :placeholder="t('component.icon.search')"
              @change="debounceHandleSearchChange"
              allowClear
            />
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
              <a-pagination
                size="small"
                :pageSize="pageSize"
                :total="getTotal"
                :showLessItems="true"
                :showSizeChanger="false"
                @change="handlePageChange"
              />
            </div>
          </div>
          <template v-else
            ><div class="p-5"><a-empty /></div>
          </template>
        </template>
        <span class="cursor-pointer px-2 py-1 flex items-center">
          <Icon :icon="currentSelect || 'ion:apps-outline'" />
        </span>
      </a-popover>
    </template>
  </a-input>
</template>
<script lang="ts" setup>
  import { ref, watchEffect, watch, unref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Input, Popover, Pagination, Empty } from 'ant-design-vue';
  import { propTypes } from '/@/utils/propTypes';
  import { usePagination } from '/@/hooks/web/usePagination';
  import { useDebounceFn } from '@vueuse/core';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import { useMessage } from '/@/hooks/web/useMessage';
  import svgIcons from 'virtual:svg-icons-names';
  import iconsData from '../data/icons.data';
  import Icon from './Icon.vue';

  const AInput = Input;
  const APopover = Popover;
  const APagination = Pagination;
  const AEmpty = Empty;

  function getIcons() {
    const data = iconsData as any;
    const prefix: string = data?.prefix ?? '';
    let result: string[] = [];
    if (prefix) {
      result = (data?.icons ?? []).map((item) => `${prefix}:${item}`);
    } else if (Array.isArray(iconsData)) {
      result = iconsData as string[];
    }
    return result;
  }

  function getSvgIcons() {
    return svgIcons.map((icon) => icon.replace('icon-', '') + '|svg');
  }

  const props = defineProps({
    value: propTypes.string,
    width: propTypes.string.def('100%'),
    pageSize: propTypes.number.def(70),
    copy: propTypes.bool.def(false),
    mode: propTypes.oneOf(['svg', 'iconify']),
  });

  const emit = defineEmits(['change', 'update:value']);

  let iconsTemp: string[];
  if (props.mode === 'svg') {
    iconsTemp = getSvgIcons();
  } else if (props.mode === 'iconify') {
    iconsTemp = getIcons();
  } else {
    iconsTemp = [...getIcons(), ...getSvgIcons()];
  }
  const icons = iconsTemp;

  const currentSelect = ref('');
  const open = ref(false);
  const currentList = ref(icons);

  const { t } = useI18n();
  const { prefixCls } = useDesign('icon-picker');

  const debounceHandleSearchChange = useDebounceFn(handleSearchChange, 100) as any;
  const { clipboardRef, isSuccessRef } = useCopyToClipboard(props.value);
  const { createMessage } = useMessage();

  const { getPaginationList, getTotal, setCurrentPage } = usePagination(
    currentList,
    props.pageSize,
  );

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
