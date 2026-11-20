<template>
  <div :class="getClass">
    <template v-if="canFullscreen">
      <Tooltip :title="t('component.modal.restore')" placement="bottom" v-if="fullScreen">
        <Icon icon="i-ant-design:fullscreen-exit-outlined" role="full" @click="handleFullScreen" />
      </Tooltip>
      <Tooltip :title="t('component.modal.maximize')" placement="bottom" v-else>
        <Icon icon="i-ant-design:fullscreen-outlined" role="close" @click="handleFullScreen" />
      </Tooltip>
    </template>
    <Tooltip :title="t('component.modal.close')" placement="bottom">
      <Icon icon="i-ant-design:close-outlined" class="anticon-close cursor-pointer" @click="handleCancel" />
    </Tooltip>
  </div>
</template>
<script lang="ts" setup name="ModalClose">
  import { computed } from 'vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { Tooltip } from 'antdv-next';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';

  const props = defineProps({
    canFullscreen: { type: Boolean, default: true },
    fullScreen: { type: Boolean },
  });

  const emit = defineEmits(['cancel', 'fullscreen']);

  const { t } = useI18n();

  const getClass = computed(() => {
    return [
      'jeesite-basic-modal-close',
      'jeesite-basic-modal-close--custom',
      {
        ['jeesite-basic-modal-close--can-full']: props.canFullscreen,
      },
    ];
  });

  function handleCancel(e: Event) {
    emit('cancel', e);
  }

  function handleFullScreen(e: Event) {
    e?.stopPropagation();
    e?.preventDefault();
    emit('fullscreen');
  }
</script>
<style lang="less">
  .jeesite-basic-modal-close {
    // 父级 .ant-modal-close 已是 52px 高的 flex 容器，这里只负责水平排布与垂直居中
    display: flex;
    align-items: center;
    height: 100%;
    text-align: right;
    margin-right: 10px;

    .jeesite-icon {
      padding: 0 9px;
      font-size: 18px;
      color: @text-color-base !important;
      cursor: pointer;
      opacity: 0.6;

      &:hover {
        opacity: 1;
      }
    }

    .anticon-close {
      &:hover {
        color: @error-color !important;
        opacity: 1;
      }
    }
  }
</style>
