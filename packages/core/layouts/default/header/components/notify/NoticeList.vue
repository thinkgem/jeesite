<template>
  <a-list :class="prefixCls" bordered :pagination="getPagination">
    <template v-for="item in getData" :key="item.id">
      <a-list-item class="list-item" v-if="!item.titleDelete">
        <a-list-item-meta @click="handleTitleClick(item)">
          <template #title>
            <div class="title">
              <a-typography-paragraph
                style="width: 100%; margin-bottom: 0 !important"
                :style="{ cursor: isTitleClickable ? 'pointer' : '' }"
                :delete="!!item.titleDelete"
                :ellipsis="
                  $props.titleRows && $props.titleRows > 0
                    ? { rows: $props.titleRows, tooltip: !!item.title }
                    : false
                "
                :content="item.title"
              />
              <div class="extra" v-if="item.extra">
                <a-tag class="tag" :color="item.color">
                  {{ item.extra }}
                </a-tag>
              </div>
            </div>
          </template>

          <template #avatar>
            <a-avatar
              v-if="item.avatar && item.avatar.indexOf('://') != -1"
              class="avatar"
              :src="item.avatar"
            />
            <a-avatar
              v-else-if="item.avatar && item.avatar.indexOf(':') != -1"
              class="avatar avatar-icon"
            >
              <Icon :icon="item.avatar" />
            </a-avatar>
            <span v-else> {{ item.avatar }}</span>
          </template>

          <template #description>
            <div>
              <div class="description" v-if="item.description">
                <a-typography-paragraph
                  style="width: 100%; margin-bottom: 0 !important"
                  :ellipsis="
                    $props.descRows && $props.descRows > 0
                      ? { rows: $props.descRows, tooltip: !!item.description }
                      : false
                  "
                  :content="item.description"
                />
              </div>
              <div class="datetime">
                {{ item.datetime }}
              </div>
            </div>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</template>
<script lang="ts">
  import { computed, defineComponent, PropType, ref, watch, unref } from 'vue';
  import { ListItem } from './data';
  import { useDesign } from '@jeesite/core/hooks/web/useDesign';
  import { List, Avatar, Tag, Typography } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { isNumber } from '@jeesite/core/utils/is';

  export default defineComponent({
    components: {
      [Avatar.name as string]: Avatar,
      [List.name as string]: List,
      [List.Item.name as string]: List.Item,
      AListItemMeta: List.Item.Meta,
      ATypographyParagraph: Typography.Paragraph,
      [Tag.name as string]: Tag,
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
        return list.slice(size * (unref(current) - 1), size * unref(current));
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

  .ant-list.@{prefix-cls} {
    &::-webkit-scrollbar {
      display: none;
    }

    ::v-deep(.ant-pagination-disabled) {
      display: inline-block !important;
    }

    ::v-deep(.ant-list-pagination) {
      margin: 12px 18px !important;
    }

    .ant-list-item.list-item {
      padding: 6px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        background-color: rgb(0 0 0 / 3%);
      }

      .ant-list-item-meta {
        &-title {
          margin-top: 3px;
        }
      }

      .title {
        margin-bottom: 3px;
        font-weight: normal;

        .extra {
          float: right;
          margin-top: -22px;
          margin-right: 0;
          font-weight: normal;

          .tag {
            margin-right: 0;
          }
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
      }

      .datetime {
        margin-top: 4px;
        font-size: 12px;
        line-height: 18px;
      }
    }
  }
</style>
