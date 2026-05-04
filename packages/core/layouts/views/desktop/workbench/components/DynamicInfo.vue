<template>
  <Card title="最新动态" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small">更多</a-button>
    </template>
    <div class="list-container">
      <div v-for="item in dynamicInfoItems" :key="item.id" class="list-item">
        <div class="list-item-meta">
          <div class="avatar-wrapper">
            <Icon :icon="item.avatar" :size="30" />
          </div>
          <div class="meta-content">
            <div class="meta-title"> {{ item.name }} <span v-html="item.desc"> </span> </div>
            <div class="meta-description">
              {{ item.date }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </Card>
</template>
<script lang="ts" setup>
  import { Card } from 'antdv-next';
  import { dynamicInfoItems } from './Data';
  import { Icon } from '@jeesite/core/components/Icon';
</script>
<style lang="less">
  .list-container {
    padding: 5px 0;

    .list-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 16px 20px;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .list-item-meta {
        display: flex;
        align-items: center;
        flex: 1;

        .avatar-wrapper {
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          .avatar {
            font-size: 32px;
          }
        }

        .meta-content {
          flex: 1;

          .meta-title {
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 14px;
            font-weight: 500;
            color: rgb(0 0 0 / 85%);
            margin-bottom: 4px;

            .extra {
              color: #1890ff;
              font-size: 14px;
              font-weight: 400;
              cursor: pointer;

              &:hover {
                color: #40a9ff;
              }
            }
          }

          .meta-description {
            font-size: 13px;
            color: rgb(0 0 0 / 45%);
          }
        }
      }
    }
  }
</style>
