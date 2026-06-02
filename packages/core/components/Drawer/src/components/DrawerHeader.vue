<template>
  <BasicTitle v-if="!isDetail" class="jeesite-basic-drawer-header">
    <slot name="title"></slot>
    {{ !$slots.title ? title : '' }}
  </BasicTitle>

  <div :class="['jeesite-basic-drawer-header', 'jeesite-basic-drawer-header--detail']" v-else>
    <span class="jeesite-basic-drawer-header__twrap">
      <span @click="handleClose" v-if="showDetailBack">
        <Icon icon="i-ant-design:arrow-left-outlined" class="jeesite-basic-drawer-header__back" />
      </span>
      <span v-if="title">{{ title }}</span>
    </span>

    <span class="jeesite-basic-drawer-header__toolbar">
      <slot name="titleToolbar"></slot>
    </span>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTitle } from '@jeesite/core/components/Basic';
  import { propTypes } from '@jeesite/core/utils/propTypes';

  export default defineComponent({
    name: 'BasicDrawerHeader',
    components: { Icon, BasicTitle },
    props: {
      isDetail: propTypes.bool,
      showDetailBack: propTypes.bool,
      title: propTypes.string,
    },
    emits: ['close'],
    setup(_, { emit }) {
      function handleClose() {
        emit('close');
      }

      return { handleClose };
    },
  });
</script>

<style lang="less">
  @footer-height: 60px;

  .jeesite-basic-drawer-header {
    display: flex;
    height: 100%;
    align-items: center;

    &__back {
      padding: 0 12px;
      cursor: pointer;

      &:hover {
        color: @primary-color;
      }
    }

    &__twrap {
      flex: 1;
    }

    &__toolbar {
      padding-right: 50px;
    }
  }
</style>
