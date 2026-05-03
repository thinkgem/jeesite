<template>
  <div :class="prefixCls">
    <div v-for="item in getData" :key="item.id" class="list-item">
      <div class="list-item-meta" @click="handleTitleClick(item)">
        <div class="avatar">
          <a-avatar v-if="item.avatar && item.avatar.indexOf('://') != -1" class="avatar" :src="item.avatar" />
          <a-avatar v-else-if="item.avatar && item.avatar.indexOf(':') != -1" class="avatar avatar-icon">
            <Icon :icon="item.avatar" />
          </a-avatar>
          <a-avatar v-else class="avatar-icon">
            <Icon icon="i-ant-design:user-outlined" />
          </a-avatar>
        </div>

        <div class="description-container">
          <div class="description" v-if="item.title || item.description">
            <div
              class="description-text"
              :class="{ 'description-ellipsis': $props.descRows && $props.descRows > 0 }"
              :title="!!item.description && $props.descRows && $props.descRows > 0 ? item.description : undefined"
            >
              {{ item.title }} {{ item.description }}
            </div>
          </div>
          <div class="datetime">
            {{ item.datetime }}
          </div>
        </div>
      </div>
    </div>
    <div v-if="getPagination" class="list-pagination">
      <a-pagination
        :total="getPagination.total"
        :page-size="getPagination.pageSize"
        size="small"
        :current="getPagination.current"
        @change="getPagination.onChange"
      />
    </div>
  </div>
</template>
<script lang="ts">
  import { computed, defineComponent, PropType, ref, watch, unref } from 'vue';
  import { ListItem } from './data';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { Avatar, Tag, Pagination } from 'antdv-next';
  import { Icon } from '@jeesite/core/components/Icon';
  import { isNumber } from '@jeesite/core/utils/is';

  export default defineComponent({
    components: {
      [Avatar.name as string]: Avatar,
      [Tag.name as string]: Tag,
      [Pagination.name as string]: Pagination,
      Icon,
    },
    props: {
      list: {
        type: Array as PropType<ListItem[]>,
        default: () => [],
      },
      pageSize: {
        type: [Boolean, Number] as PropType<boolean | number>,
        default: 5,
      },
      currentPage: {
        type: Number,
        default: 1,
      },
      titleRows: {
        type: Number,
        default: 1,
      },
      descRows: {
        type: Number,
        default: 2,
      },
      onTitleClick: {
        type: Function as PropType<(Recordable) => void>,
      },
    },
    emits: ['update:currentPage'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('header-notify-list');
      const current = ref(props.currentPage || 1);
      const getData = computed<ListItem[]>(() => {
        const { pageSize, list } = props;
        if (pageSize === false) return [];
        let size = isNumber(pageSize) ? pageSize : 5;
        return list.slice(size * (unref(current) - 1), size * unref(current)).filter((item) => !item.titleDelete);
      });
      watch(
        () => props.currentPage,
        (v) => {
          current.value = v;
        },
      );
      const isTitleClickable = computed(() => !!props.onTitleClick);
      const getPagination = computed(() => {
        const { list, pageSize } = props;
        if ((pageSize as number) > 0 && list && list.length > (pageSize as number)) {
          return {
            total: list.length,
            pageSize,
            size: 'small',
            current: unref(current),
            onChange(page) {
              current.value = page;
              emit('update:currentPage', page);
            },
          };
        } else {
          return false;
        }
      });

      function handleTitleClick(item: ListItem) {
        props.onTitleClick && props.onTitleClick(item);
      }

      return { prefixCls, getPagination, getData, handleTitleClick, isTitleClickable };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'jeesite-header-notify-list';

  .@{prefix-cls} {
    &::-webkit-scrollbar {
      display: none;
    }

    .ant-pagination-disabled {
      display: inline-block !important;
    }

    .list-pagination {
      margin: 12px 18px !important;
    }

    .list-item {
      padding: 6px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background-color: rgb(0 0 0 / 3%);
      }

      .list-item-meta {
        display: flex;
        align-items: flex-start;

        .avatar {
          margin: 2px;
          flex-shrink: 0;
        }

        .description-container {
          flex: 1;
          margin-left: 12px;
        }
      }

      .avatar {
        margin: 8px 0 0 8px;
      }

      .avatar-icon {
        background-color: @primary-color;
      }

      .description {
        font-size: 12px;
        line-height: 18px;

        .description-text {
          width: 100%;
          margin-bottom: 0;

          &.description-ellipsis {
            display: -webkit-box;
            overflow: hidden;
            text-overflow: ellipsis;
            -webkit-line-clamp: v-bind('$props.descRows');
            -webkit-box-orient: vertical;
            word-break: break-word;
          }
        }
      }

      .datetime {
        margin-top: 4px;
        font-size: 12px;
        line-height: 18px;
      }
    }
  }
</style>
