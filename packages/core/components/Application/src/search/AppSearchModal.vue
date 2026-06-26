<template>
  <Teleport to="body">
    <transition name="zoom-fade" mode="out-in">
      <div :class="getClass" @click.stop v-if="open">
        <div class="jeesite-app-search-modal-content" v-click-outside="handleClose">
          <div class="jeesite-app-search-modal-input__wrapper">
            <a-input
              ref="inputRef"
              class="jeesite-app-search-modal-input"
              :placeholder="t('common.searchText')"
              :allow-clear="true"
              @change="handleSearch"
            >
              <template #prefix>
                <Icon icon="i-ant-design:search-outlined" class="text-gray-500" />
              </template>
            </a-input>
            <span class="jeesite-app-search-modal-cancel" @click="handleClose">
              {{ t('common.cancelText') }}
            </span>
          </div>

          <div class="jeesite-app-search-modal-not-data" v-show="getIsNotData">
            {{ t('component.app.searchNotData') }}
          </div>

          <ul class="jeesite-app-search-modal-list" v-show="!getIsNotData" ref="scrollWrap">
            <li
              :ref="setRefs(index)"
              v-for="(item, index) in searchResult"
              :key="item.path"
              :data-index="index"
              @mouseenter="handleMouseenter"
              @click="handleEnter"
              :class="[
                'jeesite-app-search-modal-list__item',
                {
                  ['jeesite-app-search-modal-list__item--active']: activeIndex === index,
                },
              ]"
            >
              <div class="jeesite-app-search-modal-list__item-icon">
                <Icon :icon="item.icon || 'mdi:form-select'" :size="20" />
              </div>
              <div class="jeesite-app-search-modal-list__item-text">
                {{ item.name }}
              </div>
              <div class="jeesite-app-search-modal-list__item-enter">
                <Icon icon="i-ant-design:enter-outlined" :size="20" />
              </div>
            </li>
          </ul>
          <AppSearchFooter />
        </div>
      </div>
    </transition>
  </Teleport>
</template>

<script lang="ts" setup>
  import { computed, unref, ref, watch, nextTick, shallowRef } from 'vue';
  import AppSearchFooter from './AppSearchFooter.vue';
  import { Icon } from '@jeesite/core/components/Icon';
  // @ts-ignore
  import vClickOutside from '@jeesite/core/directives/clickOutside';
  import { useRefs } from '@jeesite/core/hooks/core/useRefs';
  import { useMenuSearch } from './useMenuSearch';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useAppInject } from '@jeesite/core/hooks/web/useAppInject';

  const props = defineProps({
    open: { type: Boolean },
  });

  const emit = defineEmits(['close']);

  const scrollWrap = ref(null);
  const inputRef = shallowRef<Nullable<HTMLElement>>();

  const { t } = useI18n();
  const { refs, setRefs } = useRefs();
  const { getIsMobile } = useAppInject();

  const { handleSearch, searchResult, keyword, activeIndex, handleEnter, handleMouseenter } = useMenuSearch(
    refs,
    scrollWrap,
    emit,
  );

  const getIsNotData = computed(() => !keyword || unref(searchResult).length === 0);

  const getClass = computed(() => {
    return [
      'jeesite-app-search-modal',
      {
        ['jeesite-app-search-modal--mobile']: unref(getIsMobile),
      },
    ];
  });

  watch(
    () => props.open,
    (open: boolean) => {
      open &&
        nextTick(() => {
          unref(inputRef)?.focus();
        });
    },
  );

  function handleClose() {
    searchResult.value = [];
    emit('close');
  }
</script>
<style lang="less">
  .jeesite-app-search-modal {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 800;
    display: flex;
    width: 100%;
    height: 100%;
    padding-top: 50px;
    background-color: rgb(0 0 0 / 25%);
    justify-content: center;

    &--mobile {
      padding: 0;

      > div {
        width: 100%;
      }

      .jeesite-app-search-modal-input {
        width: calc(100% - 38px);
      }

      .jeesite-app-search-modal-cancel {
        display: inline-block;
      }

      .jeesite-app-search-modal-content {
        width: 100%;
        height: 100%;
        border-radius: 0;
      }

      .jeesite-app-search-footer {
        display: none;
      }

      .jeesite-app-search-modal-list {
        height: calc(100% - 80px);
        max-height: unset;

        &__item {
          &-enter {
            opacity: 0 !important;
          }
        }
      }
    }

    &-content {
      position: relative;
      width: 632px;
      margin: 0 auto auto;
      background-color: @component-background;
      border-radius: 16px;
      box-shadow: 0 25px 50px -12px rgb(0 0 0 / 25%);
      flex-direction: column;
    }

    &-input__wrapper {
      display: flex;
      padding: 14px 14px 0;
      justify-content: space-between;
      align-items: center;
    }

    &-input {
      width: 100%;
      height: 48px;
      font-size: 1.5em;
      color: #1c1e21;
      border-radius: 6px;

      span[role='img'] {
        color: #999;
      }
    }

    &-cancel {
      display: none;
      font-size: 1em;
      color: #666;
    }

    &-not-data {
      display: flex;
      width: 100%;
      height: 100px;
      font-size: 0.9;
      color: rgb(150 159 175);
      align-items: center;
      justify-content: center;
    }

    &-list {
      max-height: 472px;
      padding: 0 14px;
      padding-bottom: 20px;
      margin: 0 auto;
      margin-top: 14px;
      overflow: auto;

      &__item {
        position: relative;
        display: flex;
        width: 100%;
        height: 56px;
        padding-bottom: 4px;
        padding-left: 14px;
        margin-top: 8px;
        font-size: 14px;
        color: @text-color-base;
        cursor: pointer;
        background-color: @component-background;
        border-radius: 4px;
        box-shadow: 0 1px 3px 0 #d4d9e1;
        align-items: center;

        > div:first-child,
        > div:last-child {
          display: flex;
          align-items: center;
        }

        &--active {
          color: #fff;
          background-color: @primary-color;

          .jeesite-app-search-modal-list__item-enter {
            opacity: 1;
          }
        }

        &-icon {
          width: 30px;
        }

        &-text {
          flex: 1;
        }

        &-enter {
          width: 30px;
          opacity: 0;
        }
      }
    }
  }
</style>
