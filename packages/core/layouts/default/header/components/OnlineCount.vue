<template>
  <Tooltip :title="t('在线用户')" placement="bottom" :mouseEnterDelay="0.5" @click="handleToOnlineList">
    <Badge :count="count" :offset="[-6, 11]" :overflowCount="99" :number-style="{ backgroundColor: '#00a65a' }">
      <Icon icon="i-simple-line-icons:people" />
    </Badge>
  </Tooltip>
</template>
<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { Tooltip, Badge } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { usePermission } from '@jeesite/core/hooks/web/usePermission';
  import { onlineCount } from '@jeesite/core/api/sys/online';

  import { useRouter } from 'vue-router';

  export default defineComponent({
    name: 'JeeSiteOnlineCount',
    components: { Icon, Tooltip, Badge },

    setup() {
      const { t } = useI18n();
      const { push } = useRouter();
      const { hasPermission } = usePermission();
      const { createConfirm } = useMessage();

      const count = ref<number>(0);

      async function refreshOnlineCount() {
        const data = await onlineCount();
        if (data && data.message) {
          if (data.result == 'false' || data.result == 'login') {
            if ((window as any).rocInt) clearInterval((window as any).rocInt);
            if ((window as any).ppmInt) clearInterval((window as any).ppmInt);
          }
          createConfirm({
            title: t('sys.api.errorTip'),
            content: data.message,
            iconType: 'info',
            onOk() {
              location.reload();
            },
          });
          return;
        }
        let num = Number(data || 0);
        count.value = num !== num ? 0 : num;
      }

      onMounted(async () => {
        await refreshOnlineCount(); // 先执行一次
        (window as any).rocInt = setInterval(refreshOnlineCount, 180000); // 3分钟执行一次
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
