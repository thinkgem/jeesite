/*
SQLyog v10.2 
MySQL - 5.1.33-community : Database - jeesite
*********************************************************************
*/


/*Table structure for table `cms_article` */

DROP TABLE IF EXISTS `cms_article`;

CREATE TABLE `cms_article` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `category_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '栏目编号',
  `title` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '标题',
  `link` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文章链接',
  `color` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '标题颜色',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文章图片',
  `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '关键字',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述、摘要',
  `weight` int(11) DEFAULT '0' COMMENT '权重，越大越靠前',
  `weight_date` datetime DEFAULT NULL COMMENT '权重期限',
  `hits` int(11) DEFAULT '0' COMMENT '点击数',
  `posid` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '推荐位，多选',
  `custom_content_view` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '自定义内容视图',
  `view_config` text COLLATE utf8_bin COMMENT '视图配置',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `cms_article_create_by` (`create_by`),
  KEY `cms_article_title` (`title`),
  KEY `cms_article_keywords` (`keywords`),
  KEY `cms_article_del_flag` (`del_flag`),
  KEY `cms_article_weight` (`weight`),
  KEY `cms_article_update_date` (`update_date`),
  KEY `cms_article_category_id` (`category_id`),
  KEY `cms_article_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='文章表';

/*Data for the table `cms_article` */

insert  into `cms_article`(`id`,`category_id`,`title`,`link`,`color`,`image`,`keywords`,`description`,`weight`,`weight_date`,`hits`,`posid`,`custom_content_view`,`view_config`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','3','文章标题标题标题标题',NULL,'green',NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','3','文章标题标题标题标题',NULL,'red',NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','3','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','3','文章标题标题标题标题',NULL,'green',NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','3','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','3','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','4','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','4','文章标题标题标题标题',NULL,'blue',NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','4','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','4','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','5','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','5','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','5','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','7','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('15','7','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('16','7','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('17','7','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('18','8','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('19','8','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('20','8','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('21','8','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('22','9','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('23','9','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('24','9','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('25','9','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('26','9','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('27','11','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('28','11','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('29','11','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('30','11','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('31','11','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('32','12','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('33','12','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('34','12','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('35','12','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('36','12','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('37','13','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('38','13','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('39','13','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('40','13','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('41','14','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('42','14','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('43','14','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('44','14','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('45','14','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('46','15','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('47','15','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('48','15','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('49','16','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('50','17','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('51','17','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('52','26','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('53','26','文章标题标题标题标题',NULL,NULL,NULL,'关键字1,关键字2',NULL,0,NULL,0,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `cms_article_data` */

DROP TABLE IF EXISTS `cms_article_data`;

CREATE TABLE `cms_article_data` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `content` text COLLATE utf8_bin COMMENT '文章内容',
  `copyfrom` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文章来源',
  `relation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '相关文章',
  `allow_comment` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否允许评论',
  PRIMARY KEY (`id`),
  KEY `cms_article_data_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='文章详表';

/*Data for the table `cms_article_data` */

insert  into `cms_article_data`(`id`,`content`,`copyfrom`,`relation`,`allow_comment`) values ('1','文章内容内容内容内容','来源','1,2,3','1'),('2','文章内容内容内容内容','来源','1,2,3','1'),('3','文章内容内容内容内容','来源','1,2,3','1'),('4','文章内容内容内容内容','来源','1,2,3','1'),('5','文章内容内容内容内容','来源','1,2,3','1'),('6','文章内容内容内容内容','来源','1,2,3','1'),('7','文章内容内容内容内容','来源','1,2,3','1'),('8','文章内容内容内容内容','来源','1,2,3','1'),('9','文章内容内容内容内容','来源','1,2,3','1'),('10','文章内容内容内容内容','来源','1,2,3','1'),('11','文章内容内容内容内容','来源','1,2,3','1'),('12','文章内容内容内容内容','来源','1,2,3','1'),('13','文章内容内容内容内容','来源','1,2,3','1'),('14','文章内容内容内容内容','来源','1,2,3','1'),('15','文章内容内容内容内容','来源','1,2,3','1'),('16','文章内容内容内容内容','来源','1,2,3','1'),('17','文章内容内容内容内容','来源','1,2,3','1'),('18','文章内容内容内容内容','来源','1,2,3','1'),('19','文章内容内容内容内容','来源','1,2,3','1'),('20','文章内容内容内容内容','来源','1,2,3','1'),('21','文章内容内容内容内容','来源','1,2,3','1'),('22','文章内容内容内容内容','来源','1,2,3','1'),('23','文章内容内容内容内容','来源','1,2,3','1'),('24','文章内容内容内容内容','来源','1,2,3','1'),('25','文章内容内容内容内容','来源','1,2,3','1'),('26','文章内容内容内容内容','来源','1,2,3','1'),('27','文章内容内容内容内容','来源','1,2,3','1'),('28','文章内容内容内容内容','来源','1,2,3','1'),('29','文章内容内容内容内容','来源','1,2,3','1'),('30','文章内容内容内容内容','来源','1,2,3','1'),('31','文章内容内容内容内容','来源','1,2,3','1'),('32','文章内容内容内容内容','来源','1,2,3','1'),('33','文章内容内容内容内容','来源','1,2,3','1'),('34','文章内容内容内容内容','来源','1,2,3','1'),('35','文章内容内容内容内容','来源','1,2,3','1'),('36','文章内容内容内容内容','来源','1,2,3','1'),('37','文章内容内容内容内容','来源','1,2,3','1'),('38','文章内容内容内容内容','来源','1,2,3','1'),('39','文章内容内容内容内容','来源','1,2,3','1'),('40','文章内容内容内容内容','来源','1,2,3','1'),('41','文章内容内容内容内容','来源','1,2,3','1'),('42','文章内容内容内容内容','来源','1,2,3','1'),('43','文章内容内容内容内容','来源','1,2,3','1'),('44','文章内容内容内容内容','来源','1,2,3','1'),('45','文章内容内容内容内容','来源','1,2,3','1'),('46','文章内容内容内容内容','来源','1,2,3','1'),('47','文章内容内容内容内容','来源','1,2,3','1'),('48','文章内容内容内容内容','来源','1,2,3','1'),('49','文章内容内容内容内容','来源','1,2,3','1'),('50','文章内容内容内容内容','来源','1,2,3','1'),('51','文章内容内容内容内容','来源','1,2,3','1'),('52','文章内容内容内容内容','来源','1,2,3','1'),('53','文章内容内容内容内容','来源','1,2,3','1');

/*Table structure for table `cms_category` */

DROP TABLE IF EXISTS `cms_category`;

CREATE TABLE `cms_category` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `site_id` varchar(64) COLLATE utf8_bin DEFAULT '1' COMMENT '站点编号',
  `office_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '归属机构',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `module` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '栏目模块',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '栏目名称',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '栏目图片',
  `href` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '链接',
  `target` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '目标',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '关键字',
  `sort` int(11) DEFAULT '30' COMMENT '排序（升序）',
  `in_menu` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '是否在导航中显示',
  `in_list` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '是否在分类页中显示列表',
  `show_modes` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '展现方式',
  `allow_comment` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否允许评论',
  `is_audit` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否需要审核',
  `custom_list_view` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '自定义列表视图',
  `custom_content_view` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '自定义内容视图',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  `view_config` text COLLATE utf8_bin COMMENT '视图配置',
  PRIMARY KEY (`id`),
  KEY `cms_category_parent_id` (`parent_id`),
  KEY `cms_category_parent_ids` (`parent_ids`(333)),
  KEY `cms_category_module` (`module`),
  KEY `cms_category_name` (`name`),
  KEY `cms_category_sort` (`sort`),
  KEY `cms_category_del_flag` (`del_flag`),
  KEY `cms_category_office_id` (`office_id`),
  KEY `cms_category_site_id` (`site_id`),
  KEY `cms_category_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='栏目表';

/*Data for the table `cms_category` */

insert  into `cms_category`(`id`,`site_id`,`office_id`,`parent_id`,`parent_ids`,`module`,`name`,`image`,`href`,`target`,`description`,`keywords`,`sort`,`in_menu`,`in_list`,`show_modes`,`allow_comment`,`is_audit`,`custom_list_view`,`custom_content_view`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`,`view_config`) values ('1','0','1','0','0,',NULL,'顶级栏目',NULL,NULL,NULL,NULL,NULL,0,'1','1','0','0','1',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('2','1','3','1','0,1,','article','组织机构',NULL,NULL,NULL,NULL,NULL,10,'1','1','0','0','1',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('3','1','3','2','0,1,2,','article','网站简介',NULL,NULL,NULL,NULL,NULL,30,'1','1','0','0','1',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('4','1','3','2','0,1,2,','article','内部机构',NULL,NULL,NULL,NULL,NULL,40,'1','1','0','0','1',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('5','1','3','2','0,1,2,','article','地方机构',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','0','1',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('6','1','3','1','0,1,','article','质量检验',NULL,NULL,NULL,NULL,NULL,20,'1','1','1','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('7','1','3','6','0,1,6,','article','产品质量',NULL,NULL,NULL,NULL,NULL,30,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('8','1','3','6','0,1,6,','article','技术质量',NULL,NULL,NULL,NULL,NULL,40,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('9','1','3','6','0,1,6,','article','工程质量',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('10','1','4','1','0,1,','article','软件介绍',NULL,NULL,NULL,NULL,NULL,20,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('11','1','4','10','0,1,10,','article','网络工具',NULL,NULL,NULL,NULL,NULL,30,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('12','1','4','10','0,1,10,','article','浏览工具',NULL,NULL,NULL,NULL,NULL,40,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('13','1','4','10','0,1,10,','article','浏览辅助',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('14','1','4','10','0,1,10,','article','网络优化',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('15','1','4','10','0,1,10,','article','邮件处理',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('16','1','4','10','0,1,10,','article','下载工具',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('17','1','4','10','0,1,10,','article','搜索工具',NULL,NULL,NULL,NULL,NULL,50,'1','1','2','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('18','1','5','1','0,1,','link','友情链接',NULL,NULL,NULL,NULL,NULL,90,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('19','1','5','18','0,1,18,','link','常用网站',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('20','1','5','18','0,1,18,','link','门户网站',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('21','1','5','18','0,1,18,','link','购物网站',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('22','1','5','18','0,1,18,','link','交友社区',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('23','1','5','18','0,1,18,','link','音乐视频',NULL,NULL,NULL,NULL,NULL,50,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('24','1','6','1','0,1,',NULL,'百度一下',NULL,'http://www.baidu.com','_blank',NULL,NULL,90,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('25','1','6','1','0,1,',NULL,'全文检索',NULL,'/search',NULL,NULL,NULL,90,'0','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('26','2','6','1','0,1,','article','测试栏目',NULL,NULL,NULL,NULL,NULL,90,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL),('27','1','6','1','0,1,',NULL,'公共留言',NULL,'/guestbook',NULL,NULL,NULL,90,'1','1','0','1','0',NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0',NULL);

/*Table structure for table `cms_comment` */

DROP TABLE IF EXISTS `cms_comment`;

CREATE TABLE `cms_comment` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `category_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '栏目编号',
  `content_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '栏目内容的编号',
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '栏目内容的标题',
  `content` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '评论内容',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '评论姓名',
  `ip` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '评论IP',
  `create_date` datetime NOT NULL COMMENT '评论时间',
  `audit_user_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '审核人',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `cms_comment_category_id` (`category_id`),
  KEY `cms_comment_content_id` (`content_id`),
  KEY `cms_comment_status` (`del_flag`),
  KEY `cms_comment_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评论表';

/*Data for the table `cms_comment` */

/*Table structure for table `cms_guestbook` */

DROP TABLE IF EXISTS `cms_guestbook`;

CREATE TABLE `cms_guestbook` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `type` char(1) COLLATE utf8_bin NOT NULL COMMENT '留言分类',
  `content` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '留言内容',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `email` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `phone` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '电话',
  `workunit` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '单位',
  `ip` varchar(100) COLLATE utf8_bin NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '留言时间',
  `re_user_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '回复人',
  `re_date` datetime DEFAULT NULL COMMENT '回复时间',
  `re_content` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '回复内容',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `cms_guestbook_del_flag` (`del_flag`),
  KEY `cms_site_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='留言板';

/*Data for the table `cms_guestbook` */

/*Table structure for table `cms_link` */

DROP TABLE IF EXISTS `cms_link`;

CREATE TABLE `cms_link` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `category_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '栏目编号',
  `title` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '链接名称',
  `color` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '标题颜色',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '链接图片',
  `href` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '链接地址',
  `weight` int(11) DEFAULT '0' COMMENT '权重，越大越靠前',
  `weight_date` datetime DEFAULT NULL COMMENT '权重期限',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `cms_link_category_id` (`category_id`),
  KEY `cms_link_title` (`title`),
  KEY `cms_link_del_flag` (`del_flag`),
  KEY `cms_link_weight` (`weight`),
  KEY `cms_link_create_by` (`create_by`),
  KEY `cms_link_update_date` (`update_date`),
  KEY `cms_link_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='友情链接';

/*Data for the table `cms_link` */

insert  into `cms_link`(`id`,`category_id`,`title`,`color`,`image`,`href`,`weight`,`weight_date`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','19','JeeSite',NULL,NULL,'http://thinkgem.github.com/jeesite',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','19','ThinkGem',NULL,NULL,'http://thinkgem.iteye.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','19','百度一下',NULL,NULL,'http://www.baidu.com',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','19','谷歌搜索',NULL,NULL,'http://www.google.com',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','20','新浪网',NULL,NULL,'http://www.sina.com.cn',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','20','腾讯网',NULL,NULL,'http://www.qq.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','21','淘宝网',NULL,NULL,'http://www.taobao.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','21','新华网',NULL,NULL,'http://www.xinhuanet.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','22','赶集网',NULL,NULL,'http://www.ganji.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','22','58同城',NULL,NULL,'http://www.58.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','23','视频大全',NULL,NULL,'http://v.360.cn/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','23','凤凰网',NULL,NULL,'http://www.ifeng.com/',0,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `cms_site` */

DROP TABLE IF EXISTS `cms_site`;

CREATE TABLE `cms_site` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '站点名称',
  `title` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '站点标题',
  `logo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '站点Logo',
  `domain` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '站点域名',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '关键字',
  `theme` varchar(255) COLLATE utf8_bin DEFAULT 'default' COMMENT '主题',
  `copyright` text COLLATE utf8_bin COMMENT '版权信息',
  `custom_index_view` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '自定义站点首页视图',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `cms_site_del_flag` (`del_flag`),
  KEY `cms_site_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='站点表';

/*Data for the table `cms_site` */

insert  into `cms_site`(`id`,`name`,`title`,`logo`,`domain`,`description`,`keywords`,`theme`,`copyright`,`custom_index_view`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','默认站点','JeeSite Web',NULL,NULL,'JeeSite','JeeSite','basic','Copyright &copy; 2012-2013 <a href=\'http://thinkgem.iteye.com\' target=\'_blank\'>ThinkGem</a> - Powered By <a href=\'https://github.com/thinkgem/jeesite\' target=\'_blank\'>JeeSite</a> V1.0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','子站点测试','JeeSite Subsite',NULL,NULL,'JeeSite subsite','JeeSite subsite','basic','Copyright &copy; 2012-2013 <a href=\'http://thinkgem.iteye.com\' target=\'_blank\'>ThinkGem</a> - Powered By <a href=\'https://github.com/thinkgem/jeesite\' target=\'_blank\'>JeeSite</a> V1.0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `oa_leave` */

DROP TABLE IF EXISTS `oa_leave`;

CREATE TABLE `oa_leave` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `process_instance_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '流程实例编号',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `leave_type` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '请假类型',
  `reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请假理由',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `reality_start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `reality_end_time` datetime DEFAULT NULL COMMENT '实际结束时间',
  `process_status` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '流程状态',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `oa_leave_create_by` (`create_by`),
  KEY `oa_leave_process_instance_id` (`process_instance_id`),
  KEY `oa_leave_del_flag` (`del_flag`),
  KEY `oa_leave_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='请假流程表';

/*Data for the table `oa_leave` */

/*Table structure for table `prj_project` */

DROP TABLE IF EXISTS `prj_project`;

CREATE TABLE `prj_project` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `root_package` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '包名',
  `erm_path` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '数据文件路径',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`),
  KEY `prj_project_id` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='项目';

/*Data for the table `prj_project` */

/*Table structure for table `sys_area` */

DROP TABLE IF EXISTS `sys_area`;

CREATE TABLE `sys_area` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '区域名称',
  `type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '区域类型',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_area_parent_id` (`parent_id`),
  KEY `sys_area_parent_ids` (`parent_ids`(333)),
  KEY `sys_area_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='区域表';

/*Data for the table `sys_area` */

insert  into `sys_area`(`id`,`parent_id`,`parent_ids`,`code`,`name`,`type`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','100000','中国','1','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','0,1,','110000','北京市','2','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','2','0,1,2,','110101','东城区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','2','0,1,2,','110102','西城区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','2','0,1,2,','110103','朝阳区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','2','0,1,2,','110104','丰台区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','2','0,1,2,','110105','海淀区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','1','0,1,','370000','山东省','2','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','8','0,1,2,','370531','济南市','3','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','8','0,1,2,','370532','历城区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','8','0,1,2,','370533','历城区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','8','0,1,2,','370534','历下区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','8','0,1,2,','370535','天桥区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','8','0,1,2,','370536','槐荫区','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `sys_dict` */

DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `label` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '标签名',
  `value` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '数据值',
  `type` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '类型',
  `description` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '描述',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

/*Data for the table `sys_dict` */

insert  into `sys_dict`(`id`,`label`,`value`,`type`,`description`,`sort`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','正常','0','del_flag','删除标记',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','删除','1','del_flag','删除标记',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','显示','1','show_hide','显示/隐藏',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','隐藏','0','show_hide','显示/隐藏',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','是','1','yes_no','是/否',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','否','0','yes_no','是/否',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','红色','red','color','颜色值',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','绿色','green','color','颜色值',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','蓝色','blue','color','颜色值',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','黄色','yellow','color','颜色值',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','橙色','orange','color','颜色值',50,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','默认主题','default','theme','主题方案',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','天蓝主题','cerulean','theme','主题方案',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','橙色主题','readable','theme','主题方案',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('15','红色主题','united','theme','主题方案',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('16','Flat主题','flat','theme','主题方案',60,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('17','国家','1','sys_area_type','区域类型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('18','省份、直辖市','2','sys_area_type','区域类型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('19','地市','3','sys_area_type','区域类型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('20','区县','4','sys_area_type','区域类型',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('21','公司','1','sys_office_type','机构类型',60,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('22','部门','2','sys_office_type','机构类型',70,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('23','一级','1','sys_office_grade','机构等级',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('24','二级','2','sys_office_grade','机构等级',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('25','三级','3','sys_office_grade','机构等级',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('26','四级','4','sys_office_grade','机构等级',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('27','所有数据','1','sys_data_scope','数据范围',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('28','所在公司及以下数据','2','sys_data_scope','数据范围',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('29','所在公司数据','3','sys_data_scope','数据范围',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('30','所在部门及以下数据','4','sys_data_scope','数据范围',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('31','所在部门数据','5','sys_data_scope','数据范围',50,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('32','仅本人数据','8','sys_data_scope','数据范围',90,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('33','按明细设置','9','sys_data_scope','数据范围',100,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('34','系统管理','1','sys_user_type','用户类型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('35','部门经理','2','sys_user_type','用户类型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('36','普通用户','3','sys_user_type','用户类型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('37','基础主题','basic','cms_theme','站点主题',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('38','蓝色主题','blue','cms_theme','站点主题',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('39','红色主题','red','cms_theme','站点主题',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('40','文章模型','article','cms_module','栏目模型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('41','图片模型','picture','cms_module','栏目模型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('42','下载模型','download','cms_module','栏目模型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('43','链接模型','link','cms_module','栏目模型',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('44','专题模型','special','cms_module','栏目模型',50,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'1'),('45','默认展现方式','0','cms_show_modes','展现方式',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('46','首栏目内容列表','1','cms_show_modes','展现方式',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('47','栏目第一条内容','2','cms_show_modes','展现方式',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('48','发布','0','cms_del_flag','内容状态',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('49','删除','1','cms_del_flag','内容状态',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('50','审核','2','cms_del_flag','内容状态',15,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('51','首页焦点图','1','cms_posid','推荐位',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('52','栏目页文章推荐','2','cms_posid','推荐位',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('53','咨询','1','cms_guestbook','留言板分类',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('54','建议','2','cms_guestbook','留言板分类',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('55','投诉','3','cms_guestbook','留言板分类',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('56','其它','4','cms_guestbook','留言板分类',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('57','公休','1','oa_leave_type','请假类型',10,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('58','病假','2','oa_leave_type','请假类型',20,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('59','事假','3','oa_leave_type','请假类型',30,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('60','调休','4','oa_leave_type','请假类型',40,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('61','婚假','5','oa_leave_type','请假类型',60,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('62','接入日志','1','sys_log_type','日志类型',30,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('63','异常日志','2','sys_log_type','日志类型',40,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('64','单表增删改查','single','prj_template_type','代码模板',10,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('65','所有entity和dao','entityAndDao','prj_template_type','代码模板',20,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0');

/*Table structure for table `sys_log` */

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `type` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '日志类型',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '操作方式',
  `params` text COLLATE utf8_bin COMMENT '操作提交的数据',
  `exception` text COLLATE utf8_bin COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='日志表';

/*Data for the table `sys_log` */

/*Table structure for table `sys_mdict` */

DROP TABLE IF EXISTS `sys_mdict`;

CREATE TABLE `sys_mdict` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `sort` int(11) DEFAULT NULL COMMENT '排序（升序）',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_mdict_parent_id` (`parent_id`),
  KEY `sys_mdict_parent_ids` (`parent_ids`(333)),
  KEY `sys_mdict_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多级字典表';

/*Data for the table `sys_mdict` */

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `href` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '链接',
  `target` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '图标',
  `sort` int(11) NOT NULL COMMENT '排序（升序）',
  `is_show` char(1) COLLATE utf8_bin NOT NULL COMMENT '是否在菜单中显示',
  `is_activiti` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否同步工作流',
  `permission` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_menu_parent_id` (`parent_id`),
  KEY `sys_menu_parent_ids` (`parent_ids`(333)),
  KEY `sys_menu_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜单表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`parent_ids`,`name`,`href`,`target`,`icon`,`sort`,`is_show`,`is_activiti`,`permission`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','顶级菜单',NULL,NULL,NULL,0,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','0,1,','系统设置',NULL,NULL,NULL,900,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','2','0,1,2,','系统设置',NULL,NULL,NULL,980,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','3','0,1,2,3,','菜单管理','/sys/menu/',NULL,'list-alt',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','4','0,1,2,3,4,','查看',NULL,NULL,NULL,30,'0','0','sys:menu:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','4','0,1,2,3,4,','修改',NULL,NULL,NULL,30,'0','0','sys:menu:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','3','0,1,2,3,','角色管理','/sys/role/',NULL,'lock',50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','7','0,1,2,3,7,','查看',NULL,NULL,NULL,30,'0','0','sys:role:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','7','0,1,2,3,7,','修改',NULL,NULL,NULL,30,'0','0','sys:role:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','3','0,1,2,3,','字典管理','/sys/dict/',NULL,'th-list',60,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','10','0,1,2,3,10,','查看',NULL,NULL,NULL,30,'0','0','sys:dict:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','10','0,1,2,3,10,','修改',NULL,NULL,NULL,30,'0','0','sys:dict:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','2','0,1,2,','机构用户',NULL,NULL,NULL,970,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','13','0,1,2,13,','区域管理','/sys/area/',NULL,'th',50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('15','14','0,1,2,13,14,','查看',NULL,NULL,NULL,30,'0','0','sys:area:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('16','14','0,1,2,13,14,','修改',NULL,NULL,NULL,30,'0','0','sys:area:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('17','13','0,1,2,13,','机构管理','/sys/office/',NULL,'th-large',40,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('18','17','0,1,2,13,17,','查看',NULL,NULL,NULL,30,'0','0','sys:office:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('19','17','0,1,2,13,17,','修改',NULL,NULL,NULL,30,'0','0','sys:office:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('20','13','0,1,2,13,','用户管理','/sys/user/',NULL,'user',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('21','20','0,1,2,13,20,','查看',NULL,NULL,NULL,30,'0','0','sys:user:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('22','20','0,1,2,13,20,','修改',NULL,NULL,NULL,30,'0','0','sys:user:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('23','2','0,1,2,','关于帮助',NULL,NULL,NULL,990,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('24','23','0,1,2,23','项目首页','http://jeesite.com','_blank',NULL,30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('25','23','0,1,2,23','项目支持','http://jeesite.com/donation.html','_blank',NULL,50,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('26','23','0,1,2,23','论坛交流','http://bbs.jeesite.com','_blank',NULL,80,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('27','1','0,1,','我的面板',NULL,NULL,NULL,100,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('28','27','0,1,27,','个人信息',NULL,NULL,NULL,990,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('29','28','0,1,27,28,','个人信息','/sys/user/info',NULL,'user',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('30','28','0,1,27,28,','修改密码','/sys/user/modifyPwd',NULL,'lock',40,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('31','1','0,1,','内容管理',NULL,NULL,NULL,500,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('32','31','0,1,31,','栏目设置',NULL,NULL,NULL,990,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('33','32','0,1,31,32','栏目管理','/cms/category/',NULL,'align-justify',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('34','33','0,1,31,32,33,','查看',NULL,NULL,NULL,30,'0','0','cms:category:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('35','33','0,1,31,32,33,','修改',NULL,NULL,NULL,30,'0','0','cms:category:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('36','32','0,1,31,32','站点设置','/cms/site/',NULL,'certificate',40,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('37','36','0,1,31,32,36,','查看',NULL,NULL,NULL,30,'0','0','cms:site:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('38','36','0,1,31,32,36,','修改',NULL,NULL,NULL,30,'0','0','cms:site:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('39','32','0,1,31,32','切换站点','/cms/site/select',NULL,'retweet',50,'1','0','cms:site:select','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('40','31','0,1,31,','内容管理',NULL,NULL,NULL,500,'1','0','cms:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('41','40','0,1,31,40,','内容发布','/cms/',NULL,'briefcase',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('42','41','0,1,31,40,41,','文章模型','/cms/article/',NULL,'file',40,'0','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('43','42','0,1,31,40,41,42,','查看',NULL,NULL,NULL,30,'0','0','cms:article:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('44','42','0,1,31,40,41,42,','修改',NULL,NULL,NULL,30,'0','0','cms:article:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('45','42','0,1,31,40,41,42,','审核',NULL,NULL,NULL,30,'0','0','cms:article:audit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('46','41','0,1,31,40,41,','链接模型','/cms/link/',NULL,'random',60,'0','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('47','46','0,1,31,40,41,46,','查看',NULL,NULL,NULL,30,'0','0','cms:link:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('48','46','0,1,31,40,41,46,','修改',NULL,NULL,NULL,30,'0','0','cms:link:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('49','46','0,1,31,40,41,46,','审核',NULL,NULL,NULL,30,'0','0','cms:link:audit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('50','40','0,1,31,40,','评论管理','/cms/comment/?status=2',NULL,'comment',40,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('51','50','0,1,31,40,50,','查看',NULL,NULL,NULL,30,'0','0','cms:comment:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('52','50','0,1,31,40,50,','审核',NULL,NULL,NULL,30,'0','0','cms:comment:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('53','40','0,1,31,40,','公共留言','/cms/guestbook/?status=2',NULL,'glass',80,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('54','53','0,1,31,40,53,','查看',NULL,NULL,NULL,30,'0','0','cms:guestbook:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('55','53','0,1,31,40,53,','审核',NULL,NULL,NULL,30,'0','0','cms:guestbook:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('56','40','0,1,31,40,','文件管理','/../static/ckfinder/ckfinder.html',NULL,'folder-open',90,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('57','56','0,1,31,40,56,','查看',NULL,NULL,NULL,30,'0','0','cms:ckfinder:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('58','56','0,1,31,40,56,','上传',NULL,NULL,NULL,30,'0','0','cms:ckfinder:upload','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('59','56','0,1,31,40,56,','修改',NULL,NULL,NULL,30,'0','0','cms:ckfinder:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('60','31','0,1,31,','统计分析',NULL,NULL,NULL,600,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('61','60','0,1,31,60,','信息量统计','/cms/stats/article',NULL,'tasks',30,'1','0','cms:stats:article','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('62','1','0,1,','在线办公',NULL,NULL,NULL,200,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('63','62','0,1,62,','个人办公',NULL,NULL,NULL,30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('64','63','0,1,62,63,','请假办理','/oa/leave',NULL,'leaf',30,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('65','64','0,1,62,63,64,','查看',NULL,NULL,NULL,30,'0','0','oa:leave:view','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('66','64','0,1,62,63,64,','修改',NULL,NULL,NULL,40,'0','0','oa:leave:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('67','2','0,1,2,','日志查询',NULL,NULL,NULL,985,'1','0',NULL,'1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('68','67','0,1,2,67,','日志查询','/sys/log',NULL,'pencil',30,'1','0','sys:log:view','1','2013-06-03 08:00:00','1','2013-06-03 08:00:00',NULL,'0'),('69','2','0,1,2,','流程管理',NULL,NULL,NULL,983,'1','0',NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('70','69','0,1,2,69,','流程管理','/sys/workflow/processList',NULL,'road',300,'1','0','sys:workflow:edit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('71','64','0,1,62,63,64,','部门领导审批',NULL,NULL,NULL,50,'0','1','oa:leave:deptLeaderAudit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('72','64','0,1,62,63,64,','人事审批',NULL,NULL,NULL,60,'0','1','oa:leave:hrAudit','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('73','32','0,1,31,32','模板管理','/cms/template',NULL,'pencil',40,'1','0',NULL,'1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('74','73','0,1,31,32,73,','查看',NULL,NULL,NULL,30,'0','0','cms:template:view','1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('75','73','0,1,31,32,73,','修改',NULL,NULL,NULL,30,'0','0','cms:template:edit','1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('76','1','0,1,','项目管理',NULL,NULL,NULL,1000,'1','0',NULL,'1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('77','76','0,1,76,','项目数据',NULL,NULL,NULL,30,'1','0',NULL,'1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('78','77','0,1,76,77,','项目管理','/prj/project/',NULL,'globe',30,'1','0',NULL,'1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('79','78','0,1,76,77,78,','查看',NULL,NULL,NULL,30,'0','0','prj:project:view','1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0'),('80','78','0,1,76,77,78,','修改',NULL,NULL,NULL,40,'0','0','prj:project:edit','1','2013-12-12 08:00:00','1','2013-12-12 08:00:00',NULL,'0');

/*Table structure for table `sys_office` */

DROP TABLE IF EXISTS `sys_office`;

CREATE TABLE `sys_office` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `parent_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) COLLATE utf8_bin NOT NULL COMMENT '所有父级编号',
  `area_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '归属区域',
  `code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '区域编码',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '机构名称',
  `type` char(1) COLLATE utf8_bin NOT NULL COMMENT '机构类型',
  `grade` char(1) COLLATE utf8_bin NOT NULL COMMENT '机构等级',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '传真',
  `email` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_office_parent_id` (`parent_id`),
  KEY `sys_office_parent_ids` (`parent_ids`(333)),
  KEY `sys_office_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='机构表';

/*Data for the table `sys_office` */

insert  into `sys_office`(`id`,`parent_id`,`parent_ids`,`area_id`,`code`,`name`,`type`,`grade`,`address`,`zip_code`,`master`,`phone`,`fax`,`email`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','0','0,','2','100000','北京市总公司','1','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','0,1,','2','100001','公司领导','2','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','1','0,1,','2','100002','人力部','2','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','1','0,1,','2','100003','市场部','2','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','1','0,1,','2','100004','技术部','2','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','1','0,1,','2','100005','研发部','2','1',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','1','0,1,','8','200000','山东省分公司','1','2',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('8','7','0,1,7,','8','200001','公司领导','2','2',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('9','7','0,1,7,','8','200002','综合部','2','2',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('10','7','0,1,7,','8','200003','市场部','2','2',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('11','7','0,1,7,','8','200004','技术部','2','2',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('12','7','0,1,7,','9','201000','济南市分公司','1','3',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('13','12','0,1,7,12,','9','201001','公司领导','2','3',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('14','12','0,1,7,12,','9','201002','综合部','2','3',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('15','12','0,1,7,12,','9','201003','市场部','2','3',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('16','12','0,1,7,12,','9','201004','技术部','2','3',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('17','12','0,1,7,12,','11','201010','济南市历城区分公司','1','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('18','17','0,1,7,12,17,','11','201011','公司领导','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('19','17','0,1,7,12,17,','11','201012','综合部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('20','17','0,1,7,12,17,','11','201013','市场部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('21','17','0,1,7,12,17,','11','201014','技术部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('22','12','0,1,7,12,','12','201020','济南市历下区分公司','1','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('23','22','0,1,7,12,22,','12','201021','公司领导','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('24','22','0,1,7,12,22,','12','201022','综合部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('25','22','0,1,7,12,22,','12','201023','市场部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('26','22','0,1,7,12,22,','12','201024','技术部','2','4',NULL,NULL,NULL,NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `office_id` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `data_scope` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '数据范围',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_role_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`office_id`,`name`,`data_scope`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','1','系统管理员','1','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('2','1','公司管理员','2','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('3','1','本公司管理员','3','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('4','1','部门管理员','4','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('5','1','本部门管理员','5','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('6','1','普通用户','8','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0'),('7','7','山东省管理员','9','1','2013-05-27 08:00:00','1','2013-05-27 08:00:00',NULL,'0');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色编号',
  `menu_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色-菜单';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`role_id`,`menu_id`) values ('1','1'),('1','10'),('1','11'),('1','12'),('1','13'),('1','14'),('1','15'),('1','16'),('1','17'),('1','18'),('1','19'),('1','2'),('1','20'),('1','21'),('1','22'),('1','23'),('1','24'),('1','25'),('1','26'),('1','27'),('1','28'),('1','29'),('1','3'),('1','30'),('1','31'),('1','32'),('1','33'),('1','34'),('1','35'),('1','36'),('1','37'),('1','38'),('1','39'),('1','4'),('1','40'),('1','41'),('1','42'),('1','43'),('1','44'),('1','45'),('1','46'),('1','47'),('1','48'),('1','49'),('1','5'),('1','50'),('1','51'),('1','52'),('1','53'),('1','54'),('1','55'),('1','56'),('1','57'),('1','58'),('1','59'),('1','6'),('1','60'),('1','61'),('1','62'),('1','63'),('1','64'),('1','65'),('1','66'),('1','67'),('1','68'),('1','69'),('1','7'),('1','70'),('1','71'),('1','72'),('1','8'),('1','9'),('2','1'),('2','10'),('2','11'),('2','12'),('2','13'),('2','14'),('2','15'),('2','16'),('2','17'),('2','18'),('2','19'),('2','2'),('2','20'),('2','21'),('2','22'),('2','23'),('2','24'),('2','25'),('2','26'),('2','27'),('2','28'),('2','29'),('2','3'),('2','30'),('2','31'),('2','32'),('2','33'),('2','34'),('2','35'),('2','36'),('2','37'),('2','38'),('2','39'),('2','4'),('2','40'),('2','41'),('2','42'),('2','43'),('2','44'),('2','45'),('2','46'),('2','47'),('2','48'),('2','49'),('2','5'),('2','50'),('2','51'),('2','52'),('2','53'),('2','54'),('2','55'),('2','56'),('2','57'),('2','58'),('2','59'),('2','6'),('2','60'),('2','61'),('2','62'),('2','63'),('2','64'),('2','65'),('2','66'),('2','67'),('2','68'),('2','69'),('2','7'),('2','70'),('2','8'),('2','9'),('3','1'),('3','10'),('3','11'),('3','12'),('3','13'),('3','14'),('3','15'),('3','16'),('3','17'),('3','18'),('3','19'),('3','2'),('3','20'),('3','21'),('3','22'),('3','23'),('3','24'),('3','25'),('3','26'),('3','27'),('3','28'),('3','29'),('3','3'),('3','30'),('3','31'),('3','32'),('3','33'),('3','34'),('3','35'),('3','36'),('3','37'),('3','38'),('3','39'),('3','4'),('3','40'),('3','41'),('3','42'),('3','43'),('3','44'),('3','45'),('3','46'),('3','47'),('3','48'),('3','49'),('3','5'),('3','50'),('3','51'),('3','52'),('3','53'),('3','54'),('3','55'),('3','56'),('3','57'),('3','58'),('3','59'),('3','6'),('3','60'),('3','61'),('3','62'),('3','63'),('3','64'),('3','65'),('3','66'),('3','67'),('3','68'),('3','69'),('3','7'),('3','70'),('3','8'),('3','9'),('4','1'),('4','10'),('4','11'),('4','12'),('4','13'),('4','14'),('4','15'),('4','16'),('4','17'),('4','18'),('4','19'),('4','2'),('4','20'),('4','21'),('4','22'),('4','23'),('4','24'),('4','25'),('4','26'),('4','27'),('4','28'),('4','29'),('4','3'),('4','30'),('4','31'),('4','32'),('4','33'),('4','34'),('4','35'),('4','36'),('4','37'),('4','38'),('4','39'),('4','4'),('4','40'),('4','41'),('4','42'),('4','43'),('4','44'),('4','45'),('4','46'),('4','47'),('4','48'),('4','49'),('4','5'),('4','50'),('4','51'),('4','52'),('4','53'),('4','54'),('4','55'),('4','56'),('4','57'),('4','58'),('4','59'),('4','6'),('4','60'),('4','61'),('4','62'),('4','63'),('4','64'),('4','65'),('4','66'),('4','67'),('4','68'),('4','69'),('4','7'),('4','70'),('4','8'),('4','9'),('5','1'),('5','10'),('5','11'),('5','12'),('5','13'),('5','14'),('5','15'),('5','16'),('5','17'),('5','18'),('5','19'),('5','2'),('5','20'),('5','21'),('5','22'),('5','23'),('5','24'),('5','25'),('5','26'),('5','27'),('5','28'),('5','29'),('5','3'),('5','30'),('5','31'),('5','32'),('5','33'),('5','34'),('5','35'),('5','36'),('5','37'),('5','38'),('5','39'),('5','4'),('5','40'),('5','41'),('5','42'),('5','43'),('5','44'),('5','45'),('5','46'),('5','47'),('5','48'),('5','49'),('5','5'),('5','50'),('5','51'),('5','52'),('5','53'),('5','54'),('5','55'),('5','56'),('5','57'),('5','58'),('5','59'),('5','6'),('5','60'),('5','61'),('5','62'),('5','63'),('5','64'),('5','65'),('5','66'),('5','67'),('5','68'),('5','69'),('5','7'),('5','70'),('5','8'),('5','9'),('6','1'),('6','10'),('6','11'),('6','12'),('6','13'),('6','14'),('6','15'),('6','16'),('6','17'),('6','18'),('6','19'),('6','2'),('6','20'),('6','21'),('6','22'),('6','23'),('6','24'),('6','25'),('6','26'),('6','27'),('6','28'),('6','29'),('6','3'),('6','30'),('6','31'),('6','32'),('6','33'),('6','34'),('6','35'),('6','36'),('6','37'),('6','38'),('6','39'),('6','4'),('6','40'),('6','41'),('6','42'),('6','43'),('6','44'),('6','45'),('6','46'),('6','47'),('6','48'),('6','49'),('6','5'),('6','50'),('6','51'),('6','52'),('6','53'),('6','54'),('6','55'),('6','56'),('6','57'),('6','58'),('6','59'),('6','6'),('6','60'),('6','61'),('6','62'),('6','63'),('6','64'),('6','65'),('6','66'),('6','67'),('6','68'),('6','69'),('6','7'),('6','70'),('6','8'),('6','9'),('7','1'),('7','10'),('7','11'),('7','12'),('7','13'),('7','14'),('7','15'),('7','16'),('7','17'),('7','18'),('7','19'),('7','2'),('7','20'),('7','21'),('7','22'),('7','23'),('7','24'),('7','25'),('7','26'),('7','27'),('7','28'),('7','29'),('7','3'),('7','30'),('7','31'),('7','32'),('7','33'),('7','34'),('7','35'),('7','36'),('7','37'),('7','38'),('7','39'),('7','4'),('7','40'),('7','41'),('7','42'),('7','43'),('7','44'),('7','45'),('7','46'),('7','47'),('7','48'),('7','49'),('7','5'),('7','50'),('7','51'),('7','52'),('7','53'),('7','54'),('7','55'),('7','56'),('7','57'),('7','58'),('7','59'),('7','6'),('7','60'),('7','61'),('7','62'),('7','63'),('7','64'),('7','65'),('7','66'),('7','67'),('7','68'),('7','69'),('7','7'),('7','70'),('7','8'),('7','9');

/*Table structure for table `sys_role_office` */

DROP TABLE IF EXISTS `sys_role_office`;

CREATE TABLE `sys_role_office` (
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色编号',
  `office_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '机构编号',
  PRIMARY KEY (`role_id`,`office_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色-机构';

/*Data for the table `sys_role_office` */

insert  into `sys_role_office`(`role_id`,`office_id`) values ('7','10'),('7','11'),('7','12'),('7','13'),('7','14'),('7','15'),('7','16'),('7','17'),('7','18'),('7','19'),('7','20'),('7','21'),('7','22'),('7','23'),('7','24'),('7','25'),('7','26'),('7','7'),('7','8'),('7','9');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `company_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '归属公司',
  `office_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '归属部门',
  `login_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '登录名',
  `password` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `no` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '工号',
  `name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `email` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '电话',
  `mobile` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '手机',
  `user_type` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '用户类型',
  `login_ip` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_user_office_id` (`office_id`),
  KEY `sys_user_login_name` (`login_name`),
  KEY `sys_user_company_id` (`company_id`),
  KEY `sys_user_update_date` (`update_date`),
  KEY `sys_user_del_flag` (`del_flag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`company_id`,`office_id`,`login_name`,`password`,`no`,`name`,`email`,`phone`,`mobile`,`user_type`,`login_ip`,`login_date`,`create_by`,`create_date`,`update_by`,`update_date`,`remarks`,`del_flag`) values ('1','1','1','thinkgem','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0001','Thinkgem','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','最高管理员','0'),('2','1','1','admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0002','管理员','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','管理员','0'),('3','1','3','bj_zhb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0003','综合部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','综合部','0'),('4','1','4','bj_scb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0004','市场部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','市场部','0'),('5','1','5','bj_jsb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0005','技术部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','技术部','0'),('6','1','6','bj_yfb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0006','研发部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','研发部','0'),('7','7','8','sd_admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0007','山分领导','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','山东省分公司领导','0'),('8','7','9','sd_zhb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0008','山分综合部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','山东省分公司综合部','0'),('9','7','10','sd_scb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0009','山分市场部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','山东省分公司市场部','0'),('10','7','11','sd_jsb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0010','山东省分公司技术部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','山东省分公司技术部','0'),('11','12','13','sdjn_admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0011','济分公司领导','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','济南分公司领导','0'),('12','12','18','sdjnlc_admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0012','济分历城领导','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','济南市历城区分公司领导','0'),('13','22','23','sdjnlx_admin','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0013','济分历下领导','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','济南市历下区分公司领导','0'),('14','22','24','sdjnlx_zhb','02a3f0772fcca9f415adc990734b45c6f059c7d33ee28362c4852032','0014','济分历下综合部','thinkgem@163.com','8675','8675',NULL,NULL,NULL,'1','2013-05-27 08:00:00','1','2013-05-27 08:00:00','济南市历下区分公司综合部','0');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户编号',
  `role_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户-角色';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`user_id`,`role_id`) values ('1','1'),('10','2'),('11','3'),('12','4'),('13','5'),('14','6'),('2','1'),('3','2'),('4','3'),('5','4'),('6','5'),('7','2'),('7','7'),('8','2'),('9','1');

