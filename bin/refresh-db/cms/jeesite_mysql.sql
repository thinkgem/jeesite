SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE cms_article_data;
DROP TABLE cms_article;
DROP TABLE cms_comment;
DROP TABLE cms_link;
DROP TABLE cms_category;
DROP TABLE cms_guestbook;
DROP TABLE cms_site;




/* Create Tables */

CREATE TABLE cms_article
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	category_id bigint NOT NULL COMMENT '栏目编号',
	title varchar(255) NOT NULL COMMENT '标题',
	color varchar(50) COMMENT '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
	image varchar(255) COMMENT '文章图片',
	keywords varchar(255) COMMENT '关键字',
	description varchar(255) COMMENT '描述、摘要',
	weight int DEFAULT 0 COMMENT '权重，越大越靠前',
	weight_date datetime COMMENT '权重期限，过期后将权重设置为：0',
	hits int DEFAULT 0 COMMENT '点击数',
	posid varchar(10) COMMENT '推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）',
	create_by bigint COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by bigint COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '文章表';


CREATE TABLE cms_article_data
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	content text COMMENT '文章内容',
	copyfrom varchar(255) COMMENT '文章来源',
	relation varchar(255) COMMENT '相关文章',
	allow_comment char(1) COMMENT '是否允许评论',
	PRIMARY KEY (id)
) COMMENT = '文章详表';


CREATE TABLE cms_category
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	site_id bigint DEFAULT 1 COMMENT '站点编号',
	office_id bigint COMMENT '归属机构',
	parent_id bigint NOT NULL COMMENT '父级编号',
	parent_ids varchar(255) NOT NULL COMMENT '所有父级编号',
	module varchar(20) COMMENT '栏目模块（article：文章；picture：图片；download：下载；link：链接；special：专题）',
	name varchar(100) NOT NULL COMMENT '栏目名称',
	image varchar(255) COMMENT '栏目图片',
	href varchar(255) COMMENT '链接',
	target varchar(20) COMMENT '目标（ _blank、_self、_parent、_top）',
	description varchar(255) COMMENT '描述，填写有助于搜索引擎优化',
	keywords varchar(255) COMMENT '关键字，填写有助于搜索引擎优化',
	sort int DEFAULT 30 COMMENT '排序（升序）',
	in_menu char(1) DEFAULT '1' COMMENT '是否在导航中显示（1：显示；0：不显示）',
	in_list char(1) DEFAULT '1' COMMENT '是否在分类页中显示列表（1：显示；0：不显示）',
	show_modes char(1) DEFAULT '0' COMMENT '展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）',
	allow_comment char(1) COMMENT '是否允许评论',
	is_audit char(1) COMMENT '是否需要审核',
	create_by bigint COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by bigint COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '栏目表';


CREATE TABLE cms_comment
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	category_id bigint NOT NULL COMMENT '栏目编号',
	content_id bigint NOT NULL COMMENT '栏目内容的编号（Article.id、Photo.id、Download.id）',
	title varchar(255) COMMENT '栏目内容的标题（Article.title、Photo.title、Download.title）',
	content varchar(255) COMMENT '评论内容',
	name varchar(100) COMMENT '评论姓名',
	ip varchar(100) COMMENT '评论IP',
	create_date datetime NOT NULL COMMENT '评论时间',
	audit_user_id bigint COMMENT '审核人',
	audit_date datetime COMMENT '审核时间',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '评论表';


CREATE TABLE cms_guestbook
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	type char(1) NOT NULL COMMENT '留言分类（1咨询、2建议、3投诉、4其它）',
	content varchar(255) NOT NULL COMMENT '留言内容',
	name varchar(100) NOT NULL COMMENT '姓名',
	email varchar(100) NOT NULL COMMENT '邮箱',
	phone varchar(100) NOT NULL COMMENT '电话',
	workunit varchar(100) NOT NULL COMMENT '单位',
	ip varchar(100) NOT NULL COMMENT 'IP',
	create_date datetime NOT NULL COMMENT '留言时间',
	re_user_id bigint COMMENT '回复人',
	re_date datetime COMMENT '回复时间',
	re_content varchar(100) COMMENT '回复内容',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '留言板';


CREATE TABLE cms_link
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	category_id bigint NOT NULL COMMENT '栏目编号',
	title varchar(255) NOT NULL COMMENT '链接名称',
	color varchar(50) COMMENT '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
	image varchar(255) COMMENT '链接图片，如果上传了图片，则显示为图片链接',
	href varchar(255) COMMENT '链接地址',
	weight int DEFAULT 0 COMMENT '权重，越大越靠前',
	weight_date datetime COMMENT '权重期限，过期后将权重设置为：0',
	create_by bigint COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by bigint COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
	PRIMARY KEY (id)
) COMMENT = '友情链接';


CREATE TABLE cms_site
(
	id bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
	name varchar(100) NOT NULL COMMENT '站点名称',
	title varchar(100) NOT NULL COMMENT '站点标题',
	domain varchar(255) NOT NULL COMMENT '域名',
	description varchar(255) COMMENT '描述，填写有助于搜索引擎优化',
	keywords varchar(255) COMMENT '关键字，填写有助于搜索引擎优化',
	theme varchar(255) DEFAULT 'default' COMMENT '主题',
	copyright text COMMENT '版权信息',
	create_by bigint COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	update_by bigint COMMENT '更新者',
	update_date datetime COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记（0：正常；1：删除）',
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
CREATE INDEX cms_category_parent_ids ON cms_category (parent_ids ASC);
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



