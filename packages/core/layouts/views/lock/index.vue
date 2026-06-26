<template>
  <transition name="fade-bottom" mode="out-in">
    <component :is="LockPageComp" v-if="getIsLock && LockPageComp" />
  </transition>
</template>
<script lang="ts" setup>
  import { computed, shallowRef, watch } from 'vue';
  import { useLockStore } from '@jeesite/core/store/modules/lock';

  const lockStore = useLockStore();
  const getIsLock = computed(() => lockStore?.getLockInfo?.isLock ?? false);
  const LockPageComp = shallowRef<any>(null);

  watch(
    getIsLock,
    (val) => {
      if (val && !LockPageComp.value) {
        import('./LockPage.vue').then((mod) => {
          LockPageComp.value = mod.default;
        });
      }
    },
    { immediate: true },
  );
</script>
