<template>
  <BasicTitle class="jeesite-basic-table-title" v-if="getTitle" :helpMessage="helpMessage">
    {{ getTitle }}
  </BasicTitle>
</template>
<script lang="ts">
  import { computed, defineComponent, PropType } from 'vue';
  import { BasicTitle } from '@jeesite/core/components/Basic';
  import { isFunction } from '@jeesite/core/utils/is';

  export default defineComponent({
    name: 'BasicTableTitle',
    components: { BasicTitle },
    props: {
      title: {
        type: [Function, String] as PropType<string | ((data: Recordable) => string)>,
      },
      getSelectRows: {
        type: Function as PropType<() => Recordable[]>,
      },
      helpMessage: {
        type: [String, Array] as PropType<string | string[]>,
      },
    },
    setup(props) {
      const getTitle = computed(() => {
        const { title, getSelectRows = () => {} } = props;
        let tit = title;

        if (isFunction(title)) {
          tit = title({
            selectRows: getSelectRows(),
          });
        }
        return tit;
      });

      return { getTitle };
    },
  });
</script>
<style lang="less">
  .jeesite-basic-table-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>
