interface GroupItem {
  title: string;
  icon: string;
  color: string;
  desc: string;
  date: string;
  group: string;
}

interface NavItem {
  title: string;
  icon: string;
  color: string;
}

interface DynamicInfoItem {
  avatar: string;
  name: string;
  date: string;
  desc: string;
}

export const navItems: NavItem[] = [
  {
    title: '首页',
    icon: 'i-ion:home-outline',
    color: '#1fdaca',
  },
  {
    title: '仪表盘',
    icon: 'i-ion:grid-outline',
    color: '#bf0c2c',
  },
  {
    title: '组件',
    icon: 'i-ion:layers-outline',
    color: '#e18525',
  },
  {
    title: '系统管理',
    icon: 'i-ion:settings-outline',
    color: '#3fb27f',
  },
  {
    title: '权限管理',
    icon: 'i-ion:key-outline',
    color: '#4daf1bc9',
  },
  {
    title: '图表',
    icon: 'i-ion:bar-chart-outline',
    color: '#00d8ff',
  },
];

export const dynamicInfoItems: DynamicInfoItem[] = [
  {
    avatar: 'icons/dynamic-avatar-4.svg',
    name: 'ThinkGem',
    date: '刚刚',
    desc: `在 <a>开源组</a> 创建了项目 <a>Vue</a>`,
  },
  {
    avatar: 'icons/dynamic-avatar-2.svg',
    name: '果汁',
    date: '1个小时前',
    desc: `关注了 <a>JeeSite</a> `,
  },
  {
    avatar: 'icons/dynamic-avatar-3.svg',
    name: 'JeeSite',
    date: '1天前',
    desc: `发布了 <a>个人动态</a> `,
  },
  {
    avatar: 'icons/dynamic-avatar-5.svg',
    name: 'Vben',
    date: '2天前',
    desc: `发表文章 <a>如何编写一个Vite插件</a> `,
  },
  {
    avatar: 'icons/dynamic-avatar-4.svg',
    name: 'ThinkGem',
    date: '3天前',
    desc: `回复了 <a>杰克</a> 的问题 <a>如何进行项目优化？</a>`,
  },
  {
    avatar: 'icons/dynamic-avatar-6.svg',
    name: 'JeeSite',
    date: '1周前',
    desc: `关闭了问题 <a>如何运行项目</a> `,
  },
  {
    avatar: 'icons/dynamic-avatar-1.svg',
    name: '彩虹',
    date: '1周前',
    desc: `发布了 <a>个人动态</a> `,
  },
  {
    avatar: 'icons/dynamic-avatar-1.svg',
    name: '彩虹',
    date: '2021-09-01 20:00',
    desc: `推送了代码到 <a>Gitee</a>`,
  },
];

export const groupItems: GroupItem[] = [
  {
    title: 'Gitee',
    icon: 'i-simple-icons:gitee',
    color: '#ce2323',
    desc: '不要等待机会，而要创造机会。',
    group: '开源组',
    date: '2021-09-01',
  },
  {
    title: 'Vue',
    icon: 'i-ion:logo-vue',
    color: '#3fb27f',
    desc: '现在的你决定将来的你。',
    group: '前端组',
    date: '2021-09-01',
  },
  {
    title: 'Html5',
    icon: 'i-ion:logo-html5',
    color: '#e18525',
    desc: '没有什么才能比努力更重要。',
    group: '上班摸鱼',
    date: '2021-09-01',
  },
  {
    title: 'Java',
    icon: 'i-logos:java',
    color: '#bf0c2c',
    desc: '热情和欲望可以突破一切难关。',
    group: '算法组',
    date: '2021-09-01',
  },
  {
    title: 'Spring',
    icon: 'i-bx:bxl-spring-boot',
    color: '#00d8ff',
    desc: '健康的身体是实目标的基石。',
    group: '技术牛',
    date: '2021-09-01',
  },
  {
    title: 'JeeSite',
    icon: 'i-ion:logo-javascript',
    color: '#4daf1bc9',
    desc: '路是走出来的，而不是空想出来的。',
    group: '架构组',
    date: '2021-09-01',
  },
];
