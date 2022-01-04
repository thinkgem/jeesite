export interface GrowCardItem {
  icon: string;
  title: string;
  value: number;
  total: number;
  color: string;
  action: string;
  url: string;
}

export const growCardList: GrowCardItem[] = [
  {
    title: '工作台',
    icon: 'visit-count|svg',
    value: 1999,
    total: 120000,
    color: 'green',
    action: '时',
    url: '/desktop/workbench',
  },
  {
    title: '关于我们',
    icon: 'total-sales|svg',
    value: 2999,
    total: 500000,
    color: 'blue',
    action: '日',
    url: '/desktop/about',
  },
  {
    title: '源码下载',
    icon: 'download-count|svg',
    value: 3999,
    total: 120000,
    color: 'orange',
    action: '周',
    url: 'https://gitee.com/thinkgem/jeesite-vue',
  },
  {
    title: '官方网站',
    icon: 'transaction|svg',
    value: 9999,
    total: 99999,
    color: 'purple',
    action: '月',
    url: 'http://jeesite.com',
  },
];
