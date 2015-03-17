SET SESSION FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS cms_article;
DROP TABLE IF EXISTS cms_article_data;
DROP TABLE IF EXISTS cms_category;
DROP TABLE IF EXISTS cms_comment;
DROP TABLE IF EXISTS cms_guestbook;
DROP TABLE IF EXISTS cms_link;
DROP TABLE IF EXISTS cms_site;
/* Create Tables */

CREATE TABLE cms_article
(
	id varchar(64) NOT NULL COMMENT '编号',
	category_id varchar(64) NOT NULL COMMENT '栏目编号',
	title varchar(255) NOT NULL COMMENT '标题',
	link varchar(255) COMMENT '文章链接',
	color varchar(50) COMMENT '标题颜色',
	image varchar(255) COMMENT '文章图片',
	keywords varchar(255) COMMENT '关键字',
	description varchar(255) COMMENT '描述、摘要',
	weight int DEFAULT 0 COMMENT '权重，越大越靠前',
	weight_date datetime COMMENT '权重期限',
	hits int DEFAULT 0 COMMENT '点击数',
	posid varchar(10) COMMENT '推荐位，多选',
	custom_content_view varchar(255) COMMENT '自定义内容视图',
	view_config text COMMENT '视图配置',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '文章表';


CREATE TABLE cms_article_data
(
	id varchar(64) NOT NULL COMMENT '编号',
	content text COMMENT '文章内容',
	copyfrom varchar(255) COMMENT '文章来源',
	relation varchar(255) COMMENT '相关文章',
	allow_comment char(1) COMMENT '是否允许评论',
	PRIMARY KEY (id)
) COMMENT = '文章详表';


CREATE TABLE cms_category
(
	id varchar(64) NOT NULL COMMENT '编号',
	parent_id varchar(64) NOT NULL COMMENT '父级编号',
	parent_ids varchar(2000) NOT NULL COMMENT '所有父级编号',
	site_id varchar(64) DEFAULT '1' COMMENT '站点编号',
	office_id varchar(64) COMMENT '归属机构',
	module varchar(20) COMMENT '栏目模块',
	name varchar(100) NOT NULL COMMENT '栏目名称',
	image varchar(255) COMMENT '栏目图片',
	href varchar(255) COMMENT '链接',
	target varchar(20) COMMENT '目标',
	description varchar(255) COMMENT '描述',
	keywords varchar(255) COMMENT '关键字',
	sort int DEFAULT 30 COMMENT '排序（升序）',
	in_menu char(1) DEFAULT '1' COMMENT '是否在导航中显示',
	in_list char(1) DEFAULT '1' COMMENT '是否在分类页中显示列表',
	show_modes char(1) DEFAULT '0' COMMENT '展现方式',
	allow_comment char(1) COMMENT '是否允许评论',
	is_audit char(1) COMMENT '是否需要审核',
	custom_list_view varchar(255) COMMENT '自定义列表视图',
	custom_content_view varchar(255) COMMENT '自定义内容视图',
	view_config text COMMENT '视图配置',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '栏目表';


CREATE TABLE cms_comment
(
	id varchar(64) NOT NULL COMMENT '编号',
	category_id varchar(64) NOT NULL COMMENT '栏目编号',
	content_id varchar(64) NOT NULL COMMENT '栏目内容的编号',
	title varchar(255) COMMENT '栏目内容的标题',
	content varchar(255) COMMENT '评论内容',
	name varchar(100) COMMENT '评论姓名',
	ip varchar(100) COMMENT '评论IP',
	create_date datetime NOT NULL COMMENT '评论时间',
	audit_user_id varchar(64) COMMENT '审核人',
	audit_date datetime COMMENT '审核时间',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '评论表';


CREATE TABLE cms_guestbook
(
	id varchar(64) NOT NULL COMMENT '编号',
	type char(1) NOT NULL COMMENT '留言分类',
	content varchar(255) NOT NULL COMMENT '留言内容',
	name varchar(100) NOT NULL COMMENT '姓名',
	email varchar(100) NOT NULL COMMENT '邮箱',
	phone varchar(100) NOT NULL COMMENT '电话',
	workunit varchar(100) NOT NULL COMMENT '单位',
	ip varchar(100) NOT NULL COMMENT 'IP',
	create_date datetime NOT NULL COMMENT '留言时间',
	re_user_id varchar(64) COMMENT '回复人',
	re_date datetime COMMENT '回复时间',
	re_content varchar(100) COMMENT '回复内容',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '留言板';


CREATE TABLE cms_link
(
	id varchar(64) NOT NULL COMMENT '编号',
	category_id varchar(64) NOT NULL COMMENT '栏目编号',
	title varchar(255) NOT NULL COMMENT '链接名称',
	color varchar(50) COMMENT '标题颜色',
	image varchar(255) COMMENT '链接图片',
	href varchar(255) COMMENT '链接地址',
	weight int DEFAULT 0 COMMENT '权重，越大越靠前',
	weight_date datetime COMMENT '权重期限',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '友情链接';


CREATE TABLE cms_site
(
	id varchar(64) NOT NULL COMMENT '编号',
	name varchar(100) NOT NULL COMMENT '站点名称',
	title varchar(100) NOT NULL COMMENT '站点标题',
	logo varchar(255) COMMENT '站点Logo',
	domain varchar(255) COMMENT '站点域名',
	description varchar(255) COMMENT '描述',
	keywords varchar(255) COMMENT '关键字',
	theme varchar(255) DEFAULT 'default' COMMENT '主题',
	copyright text COMMENT '版权信息',
	custom_index_view varchar(255) COMMENT '自定义站点首页视图',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by varchar(64) COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	PRIMARY KEY (id)
) COMMENT = '站点表';



/* Create Indexes */

CREATE INDEX cms_article_create_by ON cms_article (create_by ASC);
CREATE INDEX cms_article_title ON cms_article (title ASC);
CREATE INDEX cms_article_keywords ON cms_article (keywords ASC);
CREATE INDEX cms_article_del_flag ON cms_article (del_flag ASC);
CREATE INDEX cms_article_weight ON cms_article (weight ASC);
CREATE INDEX cms_article_update_date ON cms_article (update_date ASC);
CREATE INDEX cms_article_category_id ON cms_article (category_id ASC);
CREATE INDEX cms_category_parent_id ON cms_category (parent_id ASC);
/*CREATE INDEX cms_category_parent_ids ON cms_category (parent_ids ASC);*/
CREATE INDEX cms_category_module ON cms_category (module ASC);
CREATE INDEX cms_category_name ON cms_category (name ASC);
CREATE INDEX cms_category_sort ON cms_category (sort ASC);
CREATE INDEX cms_category_del_flag ON cms_category (del_flag ASC);
CREATE INDEX cms_category_office_id ON cms_category (office_id ASC);
CREATE INDEX cms_category_site_id ON cms_category (site_id ASC);
CREATE INDEX cms_comment_category_id ON cms_comment (category_id ASC);
CREATE INDEX cms_comment_content_id ON cms_comment (content_id ASC);
CREATE INDEX cms_comment_status ON cms_comment (del_flag ASC);
CREATE INDEX cms_guestbook_del_flag ON cms_guestbook (del_flag ASC);
CREATE INDEX cms_link_category_id ON cms_link (category_id ASC);
CREATE INDEX cms_link_title ON cms_link (title ASC);
CREATE INDEX cms_link_del_flag ON cms_link (del_flag ASC);
CREATE INDEX cms_link_weight ON cms_link (weight ASC);
CREATE INDEX cms_link_create_by ON cms_link (create_by ASC);
CREATE INDEX cms_link_update_date ON cms_link (update_date ASC);
CREATE INDEX cms_site_del_flag ON cms_site (del_flag ASC);



