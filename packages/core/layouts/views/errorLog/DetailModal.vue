<template>
  <BasicModal v-bind="$attrs" :title="t('sys.errorLog.tableActionDesc')" :showOkBtn="false" :width="800">
    <Description :data="info" @register="register" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { ErrorLogInfo } from '@jeesite/types/store';
  import { BasicModal } from '@jeesite/core/components/Modal';
  import { Description, useDescription } from '@jeesite/core/components/Description';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { getDescSchema } from './data';

  defineProps({
    info: {
      type: Object as PropType<ErrorLogInfo>,
      default: null,
    },
  });

  const { t } = useI18n();

  const [register] = useDescription({
    column: 2,
    schema: getDescSchema()!,
  });
</script>
