<!--
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Think Gem
-->
<template>
  <template v-for="item in dictList" :key="item.id">
    <span class="jeesite-dict-label">
      <template v-if="item.cssClass?.startsWith('tag ')">
        <Tag :color="item.cssClass?.substring(4)?.split(' ')[0]" :title="item.name">
          <Icon v-if="props.icon && item.icon && item.icon != ''" :icon="item.icon" class="pr-1" />
          {{ item.name }}
        </Tag>
      </template>
      <template v-else-if="item.cssClass?.startsWith('badge ')">
        <Icon v-if="props.icon && item.icon && item.icon != ''" :icon="item.icon" class="pr-1" />
        <Badge
          :status="
            item.cssClass.indexOf(' error') >= 0
              ? 'error'
              : item.cssClass.indexOf(' success') >= 0
                ? 'success'
                : item.cssClass.indexOf(' warning') >= 0
                  ? 'warning'
                  : item.cssClass.indexOf(' processing') >= 0
                    ? 'processing'
                    : 'default'
          "
          :text="item.name"
          :title="item.name"
        />
      </template>
      <template v-else>
        <span :class="item.cssClass" :style="item.cssStyle" :title="item.name">
          <Icon v-if="props.icon && item.icon && item.icon != ''" :icon="item.icon" class="pr-1" />
          {{ item.name }}
        </span>
      </template>
    </span>
  </template>
  <template v-if="dictList.length == 0">
    <span class="jeesite-dict-label">
      {{ props.defaultValue }}
    </span>
  </template>
</template>
<script lang="ts">
  import { defineComponent, ref, watch } from 'vue';
  import { Tag, Badge } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { propTypes } from '/@/utils/propTypes';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useDict } from './useDict';

  const { t } = useI18n();
  const { getDictList } = useDict();

  const props = {
    dictType: propTypes.string,
    dictValue: propTypes.any,
    defaultValue: propTypes.string.def(t('未知')),
    icon: propTypes.bool.def(true),
  };

  export default defineComponent({
    components: { Tag, Badge, Icon },
    inheritAttrs: false,
    props,
    setup(props) {
      const dictList = ref<any[]>([]);
      const { initDict } = useDict();
      initDict([props.dictType]);

      watch(
        () => props.dictValue,
        () => {
          dictList.value = getDictList(props.dictType).filter((item) =>
            (',' + props.dictValue + ',').includes(',' + item.value + ','),
          );
        },
        { immediate: true },
      );

      return {
        dictList,
        props,
        t,
      };
    },
  });
</script>
<style lang="less">
  .jeesite-dict-label {
    padding: 0 2px;
  }
</style>
