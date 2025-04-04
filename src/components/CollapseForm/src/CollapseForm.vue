<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="jeesite-collapse-form-page">
    <ScrollContainer ref="contentRef" :style="{ height: contentHeight + 'px' }" v-loading="props.loading">
      <div v-for="item in configList" :key="item.value">
        <Collapse
          :class="item.value"
          :default-active-key="configList.filter((i) => i.open || true).map((i) => i.value)"
        >
          <Collapse.Panel :key="item.value" :header="item.label">
            <slot :name="item.value"></slot>
          </Collapse.Panel>
        </Collapse>
      </div>
    </ScrollContainer>
    <div class="jeesite-collapse-form-actions">
      <slot v-if="$slots.actions" name="actions"></slot>
      <template v-else>
        <a-button type="default" @click="handleClose" v-auth="props.okAuth">
          <Icon icon="i-ant-design:close-outlined" /> {{ t('common.closeText') }}
        </a-button>
        <a-button type="primary" @click="handleSubmit" :loading="props.loading || props.okLoading">
          <Icon icon="i-ant-design:check-outlined" /> {{ t('common.okText') }}
        </a-button>
      </template>
    </div>
  </div>
</template>
<script lang="ts" setup name="CollapseForm">
  import { nextTick, ref } from 'vue';
  import { Collapse } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { propTypes } from '/@/utils/propTypes';
  import { ScrollContainer } from '/@/components/Container';
  import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
  import { useLayoutHeight } from '/@/layouts/default/content/useContentViewHeight';

  const props = defineProps({
    config: propTypes.array.def([]),
    okAuth: propTypes.string,
    loading: propTypes.bool,
    okLoading: propTypes.bool,
  });

  const emit = defineEmits(['close', 'ok']);

  const { t } = useI18n();
  const configList = ref<any[]>(props.config);
  const contentRef = ref<ComponentRef>();
  const contentHeight = ref<number>(200);
  const { headerHeightRef } = useLayoutHeight();

  function calcContentHeight() {
    const parentElement = contentRef.value?.$el.parentElement;
    const actionsElement = parentElement?.querySelector('.jeesite-collapse-form-actions');
    if (parentElement && actionsElement) {
      contentHeight.value = document.body.clientHeight - headerHeightRef.value - actionsElement.scrollHeight - 32;
    }
  }

  onMountedOrActivated(() => {
    nextTick(() => {
      calcContentHeight();
    });
  });

  useWindowSizeFn(
    () => {
      calcContentHeight();
    },
    50,
    { immediate: true },
  );

  function handleClose() {
    emit('close');
  }

  function handleSubmit() {
    emit('ok');
  }
</script>
<style lang="less">
  .jeesite-collapse-form {
    &-page {
      .scrollbar {
        border-radius: 4px !important;

        &__view > div {
          margin-bottom: 5px;

          &:last-child {
            margin-bottom: 0;
          }
        }
      }

      .ant-collapse {
        border: 0 !important;

        &-item {
          border: 0 !important;
          border-radius: 4px !important;
        }

        &-header {
          font-size: 16px;
          padding: 8px 16px !important;
          border: 0 !important;
          border-radius: 4px !important;
          background-color: @component-background;

          .ant-collapse-expand-icon {
            padding-top: 3px;
          }
        }

        &-content {
          border: 0 !important;
          padding-top: 5px !important;
          border-radius: 0 0 4px 4px !important;
        }

        &-item-active {
          .ant-collapse-header {
            border-radius: 4px 4px 0 0 !important;
          }
        }
      }
    }

    &-actions {
      padding: 10px;
      margin-top: 5px;
      margin-bottom: 0;
      text-align: center;
      border-radius: 4px !important;
      background-color: @component-background;

      .ant-btn {
        margin-right: 8px;

        &-primary {
          background: fade(@primary-color, 85);
        }
      }
    }
  }
</style>
