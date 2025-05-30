<template>
  <PageWrapper title="关于">
    <template #headerContent>
      <div class="flex items-center justify-between">
        <span class="flex-1">
          <a href="https://jeesite.com" target="_blank">JeeSite</a>
          快速开发平台，不仅仅是一个后台开发框架，它是一个企业级快速开发解决方案，有平台来封装技术细节，
          让开发者更专注业务，降低软件的开发难度。平台基于经典组合 Spring Boot、Apache MyBatis，
          前端采用：Vue3、Vite、Monorepo、Ant-Design-Vue、TypeScript、
          <a href="https://github.com/anncwb/vue-vben-admin" target="_blank">Vue Vben Admin</a>，
          最先进的技术栈，让初学者能够更快的入门并投入到团队开发中去。
          提供在线代码生成功能，包括模块如：组织机构、角色用户、菜单及按钮授权、数据权限、系统参数、内容管理、工作流等。
          众多账号安全设置，密码策略；文件在线预览；消息推送；多元化第三方登录；在线定时任务配置；支持集群，支持SAAS；
          支持多数据源；支持读写分离、分库分表；支持 Spring Cloud 分布式微服务应用架构。
          强大的组件封装，数据驱动视图。为微小中大型项目的开发，提供现成的开箱解决方案及丰富的示例。
        </span>
      </div>
    </template>
    <Description @register="infoRegister" class="enter-y" />
    <Description @register="register" class="enter-y my-4" />
    <Description @register="registerDev" class="enter-y" />
  </PageWrapper>
</template>
<script lang="ts" setup name="AboutPage">
  import { h } from 'vue';
  import { Tag } from 'ant-design-vue';
  import { PageWrapper } from '@jeesite/core/components/Page';
  import { Description, DescItem, useDescription } from '@jeesite/core/components/Description';

  const { pkg, lastBuildTime } = __APP_INFO__;

  const { dependencies, devDependencies, version } = pkg;

  const schema: DescItem[] = [];
  const devSchema: DescItem[] = [];

  const commonTagRender = (color: string) => (curVal) => h(Tag, { color }, () => curVal);
  const commonLinkRender = (text: string) => (href) => h('a', { href, target: '_blank' }, text);

  const infoSchema: DescItem[] = [
    {
      label: '版本',
      field: 'version',
      render: commonTagRender('blue'),
    },
    {
      label: '最后编译时间',
      field: 'lastBuildTime',
      render: commonTagRender('blue'),
    },
    {
      label: '文档地址',
      field: 'docs',
      render: commonLinkRender('http://docs.jeesite.com'),
    },
    {
      label: '官方网站',
      field: 'website',
      render: commonLinkRender('https://jeesite.com'),
    },
    {
      label: '下载地址',
      field: 'download',
      render: commonLinkRender('https://gitee.com/thinkgem'),
    },
    {
      label: '联系我',
      field: 'linkers',
      render: commonLinkRender('http://s.jeesite.com'),
    },
  ];

  const infoData = {
    version,
    lastBuildTime,
    docs: 'http://docs.jeesite.com',
    website: 'https://jeesite.com',
    download: 'https://gitee.com/thinkgem',
    linkers: 'http://s.jeesite.com',
  };

  const [infoRegister] = useDescription({
    title: '项目信息',
    data: infoData,
    schema: infoSchema,
    column: 2,
  });

  let register: any;
  if (dependencies) {
    Object.keys(dependencies).forEach((key) => {
      schema.push({ field: key, label: key });
    });
    register = useDescription({
      title: '生产环境依赖',
      data: dependencies,
      schema: schema,
      column: 3,
    })[0];
  }

  let registerDev: any;
  if (devDependencies) {
    Object.keys(devDependencies).forEach((key) => {
      devSchema.push({ field: key, label: key });
    });
    registerDev = useDescription({
      title: '开发环境依赖',
      data: devDependencies,
      schema: devSchema,
      column: 3,
    })[0];
  }
</script>
