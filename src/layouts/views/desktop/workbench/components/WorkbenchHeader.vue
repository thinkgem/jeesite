<template>
  <div class="pt-2 lg:flex">
    <Avatar :src="userinfo.avatarUrl || headerImg" :size="72" class="!mx-auto !block" />
    <div class="mt-2 flex flex-col justify-center md:ml-6 md:mt-0">
      <h1 class="text-md md:text-lg">早安, {{ userinfo.userName }}, 开始您一天的工作吧！</h1>
      <span class="text-secondary"> 今日晴，20℃ - 32℃！ </span>
    </div>
    <div class="mt-4 flex flex-1 justify-end md:mt-0">
      <div class="flex flex-col justify-center text-right">
        <span class="text-secondary"> 待办 </span>
        <span class="text-2xl">2/10</span>
      </div>

      <div class="mx-12 flex flex-col justify-center text-right md:mx-16">
        <span class="text-secondary"> 项目 </span>
        <span class="text-2xl">8</span>
      </div>
      <div class="mr-4 flex flex-col justify-center text-right md:mr-10">
        <span class="text-secondary"> 团队 </span>
        <span class="text-2xl">300</span>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { Avatar } from 'ant-design-vue';
  import { useUserStore } from '/@/store/modules/user';
  import headerImg from '/@/assets/images/header.jpg';

  const userStore = useUserStore();
  const userinfo = computed(() => userStore.getUserInfo);
</script>
