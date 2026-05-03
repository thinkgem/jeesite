<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div class="jeesite-collapse-form-page">
    <div class="jeesite-collapse-form-header" v-if="$slots.title">
      <div class="jeesite-collapse-form-header-title">
        <slot name="title"></slot>
      </div>
    </div>
    <ScrollContainer ref="contentRef" :style="{ height: contentHeight + 'px' }" v-loading="props.loading">
      <div v-if="configList.length === 1" class="jeesite-collapse-form-single">
        <slot :name="configList[0].value"></slot>
      </div>
      <div v-else class="jeesite-collapse-form-content">
        <template v-for="item in configList" :key="item.value">
          <Collapse :class="item.value" :default-active-key="item.open ? [item.value] : []">
            <CollapsePanel :key="item.value" :header="item.label" :forceRender="true">
              <slot :name="item.value"></slot>
            </CollapsePanel>
          </Collapse>
        </template>
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
  import { nextTick, ref, shallowRef, watch } from 'vue';
  import { Collapse, CollapsePanel } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { propTypes } from '@jeesite/core/utils/propTypes';
  import { ScrollContainer } from '@jeesite/core/components/Container';
  import { useWindowSizeFn } from '@jeesite/core/hooks/event/useWindowSizeFn';
  import { onMountedOrActivated } from '@jeesite/core/hooks/core/onMountedOrActivated';
  import { useLayoutHeight } from '@jeesite/core/layouts/default/content/useContentViewHeight';

  const props = defineProps({
    config: propTypes.array.def([]),
    storeKey: propTypes.string.def('path'),
    okAuth: propTypes.string,
    loading: propTypes.bool,
    okLoading: propTypes.bool,
  });

  const emit = defineEmits(['update:checked', 'close', 'ok']);

  const { t } = useI18n();
  const configList = ref<any[]>(props.config);
  const checkedList = ref<any[]>(props.config);
  const contentRef = shallowRef<InstanceType<typeof ScrollContainer>>();
  const contentHeight = ref<number>(200);
  const { headerHeightRef } = useLayoutHeight();

  function calcContentHeight() {
    const parentElement = contentRef.value?.$el.parentElement;
    const tabsElement = parentElement?.querySelector('.jeesite-form-settings-tabs');
    const headerElement = parentElement?.querySelector('.jeesite-collapse-form-header');
    const actionsElement = parentElement?.querySelector('.jeesite-collapse-form-actions');
    if (parentElement && actionsElement) {
      let height =
        document.body.clientHeight -
        headerHeightRef.value -
        (tabsElement?.scrollHeight || 0) -
        actionsElement.scrollHeight -
        25;
      if (headerElement) {
        height -= headerElement.scrollHeight;
      }
      contentHeight.value = height;
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

  watch(
    () => checkedList.value,
    (v) => {
      emit('update:checked', v);
    },
    {
      immediate: true,
    },
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
      background-color: @component-background;
      border-radius: 10px;
    }

    &-header {
      background-color: @component-background;
      //margin-bottom: 5px;
      padding: 10px 12px;
      border-bottom: 1px solid @header-light-bottom-border-color;
      border-radius: 10px 10px 0 0;

      &-title {
        font-size: 16px;
      }

      .anticon {
        color: @primary-color;
      }
    }

    &-single {
      padding: 30px 30px 20px;
      background-color: @component-background;
      //border-bottom: 1px solid @header-light-bottom-border-color;
      border-radius: 10px;
    }

    &-content {
      padding: 0 25px 10px;
      background-color: @component-background;
      //border-bottom: 1px solid @header-light-bottom-border-color;
      border-radius: 10px;

      .scrollbar {
        //border-radius: 4px !important;

        &__view > div {
          //margin-bottom: 5px;

          &:last-child {
            margin-bottom: 0;
          }
        }
      }

      .ant-collapse {
        border: 0 !important;

        &-item {
          border: 0 !important;
          //border-radius: 4px !important;
        }

        &-header {
          font-size: 15px !important;
          color: fade(@primary-color, 90%) !important;
          background-color: @component-background;
          padding: 15px 5px 5px !important;
          border: 0 !important;
          //border-radius: 4px !important;
          border-radius: 0 !important;
          border-bottom: 1px solid @header-light-bottom-border-color !important;

          .ant-collapse-expand-icon {
            padding-top: 3px;
          }
        }

        &-panel {
          border: 0 !important;
          border-radius: 0 !important;
          //border-bottom: 1px solid @header-light-bottom-border-color !important;
          //padding-top: 5px !important;
          //border-radius: 0 0 4px 4px !important;

          &-box {
            padding: 20px 0 0 !important;
          }
        }

        //&-item-active {
        //  .ant-collapse-header {
        //    border-radius: 4px 4px 0 0 !important;
        //  }
        //}

        .ant-form {
          margin: 0 13px 0 15px;

          &-item {
            &.no-label {
              margin-left: 0;
            }
          }
        }
      }
    }

    &-actions {
      padding: 10px 0;
      //margin-top: 5px;
      margin-bottom: 0;
      text-align: center;
      border-radius: 0 0 10px 10px !important;
      background-color: @component-background;

      .ant-btn {
        margin-left: 8px;
      }
    }
  }
</style>
