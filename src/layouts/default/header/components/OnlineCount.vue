<template>
  <Tooltip
    :title="t('在线用户')"
    placement="bottom"
    :mouseEnterDelay="0.5"
    @click="handleToOnlineList"
  >
    <Badge
      :count="count"
      :offset="[-6, 11]"
      :overflowCount="99"
      :number-style="{ backgroundColor: '#00a65a' }"
    >
      <Icon icon="icon-people" />
    </Badge>
  </Tooltip>
</template>
<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { Tooltip, Badge } from 'ant-design-vue';
  import Icon from '/@/components/Icon';

  import { useI18n } from '/@/hooks/web/useI18n';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { onlineCount } from '/@/api/sys/online';

  import { useRouter } from 'vue-router';

  export default defineComponent({
    name: 'JeeSiteOnlineCount',
    components: { Icon, Tooltip, Badge },

    setup() {
      const { t } = useI18n();
      const { push } = useRouter();
      const { hasPermission } = usePermission();

      const count = ref<Number>(0);

      async function refreshOnlineCount() {
        let num = Number(await onlineCount());
        if (!num || Number.isNaN(num)) {
          num = 0;
        }
        count.value = num;
      }

      onMounted(async () => {
        refreshOnlineCount(); // 先执行一次
        setInterval(refreshOnlineCount, 180000); // 3分钟执行一次
      });

      function handleToOnlineList() {
        if (hasPermission('sys:online:view')) {
          push('/sys/online/list');
        }
      }

      return {
        t,
        count,
        handleToOnlineList,
      };
    },
  });
</script>
