<template>
  <span v-if="getIsEditRule" class="c-red vertical-middle pr-1">*</span>
  <EditTableHeaderCell v-if="getIsEdit">
    <template v-for="title in getTitles" :key="title">
      <component :is="title" v-if="isComponent(title)" />
      <template v-else>{{ title }}</template>
    </template>
  </EditTableHeaderCell>
  <template v-else v-for="title in getTitles" :key="title">
    <component :is="title" v-if="isComponent(title)" />
    <template v-else>{{ title }}</template>
  </template>
  <BasicHelp v-if="getHelpMessage" :text="getHelpMessage" :class="`${prefixCls}__help`" />
</template>
<script lang="ts" setup name="TableHeaderCell">
  import type { PropType } from 'vue';
  import type { BasicColumn } from '../types/table';
  import { defineComponent, computed } from 'vue';
  import BasicHelp from '@jeesite/core/components/Basic/src/BasicHelp.vue';
  import EditTableHeaderCell from './EditTableHeaderIcon.vue';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';

  const props = defineProps({
    column: {
      type: Object as PropType<BasicColumn>,
      default: () => ({}),
    },
  });
  const { prefixCls } = useDesign('basic-table-header-cell');

  const getIsEdit = computed(() => !!props.column?.edit);
  const getIsEditRule = computed(() => props.column?.editRule === true);
  const getHelpMessage = computed(() => props.column?.helpMessage);

  const getTitles = computed(() => {
    const title = props.column?.title;
    return title && typeof title === 'object' && 'length' in title ? title : [title];
  });

  function isComponent(title: any) {
    return typeof title === 'function' || (title && typeof title === 'object' && 'type' in title);
  }
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-basic-table-header-cell';

  .@{prefix-cls} {
    &__help {
      margin-left: 8px;
      color: rgb(0 0 0 / 65%) !important;
    }
  }
</style>
