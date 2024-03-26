<template>
  <div>
    <slot name="insertFooter"></slot>
    <a-button v-bind="cancelButtonProps" @click="handleCancel" v-if="showCancelBtn">
      <Icon icon="i-ant-design:close-outlined" />
      {{ cancelText || (getOkAuth && showOkBtn ? t('common.cancelText') : t('common.closeText')) }}
    </a-button>
    <slot name="centerFooter"></slot>
    <a-button
      :type="okType"
      @click="handleOk"
      :loading="confirmLoading"
      v-bind="okButtonProps"
      v-if="showOkBtn && getOkAuth"
    >
      <Icon icon="i-ant-design:check-outlined" />
      {{ okText || t('common.okText') }}
    </a-button>
    <slot name="appendFooter"></slot>
  </div>
</template>
<script lang="ts">
  import { computed, defineComponent } from 'vue';
  import { Icon } from '/@/components/Icon';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { usePermission } from '/@/hooks/web/usePermission';

  import { basicProps } from '../props';
  export default defineComponent({
    name: 'BasicModalFooter',
    components: { Icon },
    props: basicProps,
    emits: ['ok', 'cancel'],
    setup(props, { emit }) {
      const { t } = useI18n();
      const { hasPermission } = usePermission();

      const getOkAuth = computed(() => {
        return hasPermission(props.okAuth);
      });

      function handleOk(e: Event) {
        emit('ok', e);
      }

      function handleCancel(e: Event) {
        emit('cancel', e);
      }

      return { t, getOkAuth, handleOk, handleCancel };
    },
  });
</script>
