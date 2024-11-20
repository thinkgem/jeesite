<template>
  <div @click="openDrawer(true)">
    <Icon icon="i-ion:settings-outline" />
    <SettingDrawer @register="register" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import SettingDrawer from './SettingDrawer';
  import { Icon } from '@jeesite/core/components/Icon';

  import { useDrawer } from '@jeesite/core/components/Drawer';

  export default defineComponent({
    name: 'SettingButton',
    components: { SettingDrawer, Icon },
    setup() {
      const [register, { openDrawer }] = useDrawer();

      return {
        register,
        openDrawer,
      };
    },
  });
</script>
