
/* Drop Tables */

DROP TABLE cms_article_data;
DROP TABLE cms_article;
DROP TABLE cms_comment;
DROP TABLE cms_link;
DROP TABLE cms_category;
DROP TABLE cms_guestbook;
DROP TABLE cms_site;



/* Drop Sequences */

DROP SEQUENCE seq_cms_article;
DROP SEQUENCE seq_cms_article_data;
DROP SEQUENCE seq_cms_category;
DROP SEQUENCE seq_cms_comment;
DROP SEQUENCE seq_cms_guestbook;
DROP SEQUENCE seq_cms_link;
DROP SEQUENCE seq_cms_site;




/* Create Sequences */

CREATE SEQUENCE seq_cms_article INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_article_data INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_category INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_comment INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_guestbook INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_link INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE seq_cms_site INCREMENT BY 1 START WITH 1;



/* Create Tables */

CREATE TABLE cms_article
(
	id number(19,0) NOT NULL,
	category_id number(19,0) NOT NULL,
	title varchar2(255) NOT NULL,
	color varchar2(50),
	image varchar2(255),
	keywords varchar2(255),
	description varchar2(255),
	weight number(10,0) DEFAULT 0,
	weight_date timestamp,
	hits number(10,0) DEFAULT 0,
	posid varchar2(10),
	create_by number(19,0),
	create_date timestamp,
	update_by number(19,0),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_article_data
(
	id number(19,0) NOT NULL,
	content clob,
	copyfrom varchar2(255),
	relation varchar2(255),
	allow_comment char(1),
	PRIMARY KEY (id)
);


CREATE TABLE cms_category
(
	id number(19,0) NOT NULL,
	site_id number(19,0) DEFAULT 1,
	office_id number(19,0),
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	module varchar2(20),
	name varchar2(100) NOT NULL,
	image varchar2(255),
	href varchar2(255),
	target varchar2(20),
	description varchar2(255),
	keywords varchar2(255),
	sort number(10,0) DEFAULT 30,
	in_menu char(1) DEFAULT '1',
	in_list char(1) DEFAULT '1',
	show_modes char(1) DEFAULT '0',
	allow_comment char(1),
	is_audit char(1),
	create_by number(19,0),
	create_date timestamp,
	update_by number(19,0),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_comment
(
	id number(19,0) NOT NULL,
	category_id number(19,0) NOT NULL,
	content_id number(19,0) NOT NULL,
	title varchar2(255),
	content varchar2(255),
	name varchar2(100),
	ip varchar2(100),
	create_date timestamp NOT NULL,
	audit_user_id number(19,0),
	audit_date timestamp,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_guestbook
(
	id number(19,0) NOT NULL,
	type char(1) NOT NULL,
	content varchar2(255) NOT NULL,
	name varchar2(100) NOT NULL,
	email varchar2(100) NOT NULL,
	phone varchar2(100) NOT NULL,
	workunit varchar2(100) NOT NULL,
	ip varchar2(100) NOT NULL,
	create_date timestamp NOT NULL,
	re_user_id number(19,0),
	re_date timestamp,
	re_content varchar2(100),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_link
(
	id number(19,0) NOT NULL,
	category_id number(19,0) NOT NULL,
	title varchar2(255) NOT NULL,
	color varchar2(50),
	image varchar2(255),
	href varchar2(255),
	weight number(10,0) DEFAULT 0,
	weight_date timestamp,
	create_by number(19,0),
	create_date timestamp,
	update_by number(19,0),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_site
(
	id number(19,0) NOT NULL,
	name varchar2(100) NOT NULL,
	title varchar2(100) NOT NULL,
	domain varchar2(255) NOT NULL,
	description varchar2(255),
	keywords varchar2(255),
	theme varchar2(255) DEFAULT 'default',
	copyright clob,
	create_by number(19,0),
	create_date timestamp,
	update_by number(19,0),
	update_date timestamp,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX cms_article_create_by ON cms_article (create_by);
CREATE INDEX cms_article_title ON cms_article (title);
CREATE INDEX cms_article_keywords ON cms_article (keywords);
CREATE INDEX cms_article_del_flag ON cms_article (del_flag);
CREATE INDEX cms_article_weight ON cms_article (weight);
CREATE INDEX cms_article_update_date ON cms_article (update_date);
CREATE INDEX cms_article_category_id ON cms_article (category_id);
CREATE INDEX cms_category_parent_id ON cms_category (parent_id);
CREATE INDEX cms_category_parent_ids ON cms_category (parent_ids);
CREATE INDEX cms_category_module ON cms_category (module);
CREATE INDEX cms_category_name ON cms_category (name);
CREATE INDEX cms_category_sort ON cms_category (sort);
CREATE INDEX cms_category_del_flag ON cms_category (del_flag);
CREATE INDEX cms_category_office_id ON cms_category (office_id);
CREATE INDEX cms_category_site_id ON cms_category (site_id);
CREATE INDEX cms_comment_category_id ON cms_comment (category_id);
CREATE INDEX cms_comment_content_id ON cms_comment (content_id);
CREATE INDEX cms_comment_status ON cms_comment (del_flag);
CREATE INDEX cms_guestbook_del_flag ON cms_guestbook (del_flag);
CREATE INDEX cms_link_category_id ON cms_link (category_id);
CREATE INDEX cms_link_title ON cms_link (title);
CREATE INDEX cms_link_del_flag ON cms_link (del_flag);
CREATE INDEX cms_link_weight ON cms_link (weight);
CREATE INDEX cms_link_create_by ON cms_link (create_by);
CREATE INDEX cms_link_update_date ON cms_link (update_date);
CREATE INDEX cms_site_del_flag ON cms_site (del_flag);



/* Comments */

COMMENT ON TABLE cms_article IS '文章表';
COMMENT ON COLUMN cms_article.id IS '编号';
COMMENT ON COLUMN cms_article.category_id IS '栏目编号';
COMMENT ON COLUMN cms_article.title IS '标题';
COMMENT ON COLUMN cms_article.color IS '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）';
COMMENT ON COLUMN cms_article.image IS '文章图片';
COMMENT ON COLUMN cms_article.keywords IS '关键字';
COMMENT ON COLUMN cms_article.description IS '描述、摘要';
COMMENT ON COLUMN cms_article.weight IS '权重，越大越靠前';
COMMENT ON COLUMN cms_article.weight_date IS '权重期限，过期后将权重设置为：0';
COMMENT ON COLUMN cms_article.hits IS '点击数';
COMMENT ON COLUMN cms_article.posid IS '推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）';
COMMENT ON COLUMN cms_article.create_by IS '创建者';
COMMENT ON COLUMN cms_article.create_date IS '创建时间';
COMMENT ON COLUMN cms_article.update_by IS '更新者';
COMMENT ON COLUMN cms_article.update_date IS '更新时间';
COMMENT ON COLUMN cms_article.remarks IS '备注信息';
COMMENT ON COLUMN cms_article.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_article_data IS '文章详表';
COMMENT ON COLUMN cms_article_data.id IS '编号';
COMMENT ON COLUMN cms_article_data.content IS '文章内容';
COMMENT ON COLUMN cms_article_data.copyfrom IS '文章来源';
COMMENT ON COLUMN cms_article_data.relation IS '相关文章';
COMMENT ON COLUMN cms_article_data.allow_comment IS '是否允许评论';
COMMENT ON TABLE cms_category IS '栏目表';
COMMENT ON COLUMN cms_category.id IS '编号';
COMMENT ON COLUMN cms_category.site_id IS '站点编号';
COMMENT ON COLUMN cms_category.office_id IS '归属机构';
COMMENT ON COLUMN cms_category.parent_id IS '父级编号';
COMMENT ON COLUMN cms_category.parent_ids IS '所有父级编号';
COMMENT ON COLUMN cms_category.module IS '栏目模块（article：文章；picture：图片；download：下载；link：链接；special：专题）';
COMMENT ON COLUMN cms_category.name IS '栏目名称';
COMMENT ON COLUMN cms_category.image IS '栏目图片';
COMMENT ON COLUMN cms_category.href IS '链接';
COMMENT ON COLUMN cms_category.target IS '目标（ _blank、_self、_parent、_top）';
COMMENT ON COLUMN cms_category.description IS '描述，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_category.keywords IS '关键字，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_category.sort IS '排序（升序）';
COMMENT ON COLUMN cms_category.in_menu IS '是否在导航中显示（1：显示；0：不显示）';
COMMENT ON COLUMN cms_category.in_list IS '是否在分类页中显示列表（1：显示；0：不显示）';
COMMENT ON COLUMN cms_category.show_modes IS '展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）';
COMMENT ON COLUMN cms_category.allow_comment IS '是否允许评论';
COMMENT ON COLUMN cms_category.is_audit IS '是否需要审核';
COMMENT ON COLUMN cms_category.create_by IS '创建者';
COMMENT ON COLUMN cms_category.create_date IS '创建时间';
COMMENT ON COLUMN cms_category.update_by IS '更新者';
COMMENT ON COLUMN cms_category.update_date IS '更新时间';
COMMENT ON COLUMN cms_category.remarks IS '备注信息';
COMMENT ON COLUMN cms_category.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_comment IS '评论表';
COMMENT ON COLUMN cms_comment.id IS '编号';
COMMENT ON COLUMN cms_comment.category_id IS '栏目编号';
COMMENT ON COLUMN cms_comment.content_id IS '栏目内容的编号（Article.id、Photo.id、Download.id）';
COMMENT ON COLUMN cms_comment.title IS '栏目内容的标题（Article.title、Photo.title、Download.title）';
COMMENT ON COLUMN cms_comment.content IS '评论内容';
COMMENT ON COLUMN cms_comment.name IS '评论姓名';
COMMENT ON COLUMN cms_comment.ip IS '评论IP';
COMMENT ON COLUMN cms_comment.create_date IS '评论时间';
COMMENT ON COLUMN cms_comment.audit_user_id IS '审核人';
COMMENT ON COLUMN cms_comment.audit_date IS '审核时间';
COMMENT ON COLUMN cms_comment.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_guestbook IS '留言板';
COMMENT ON COLUMN cms_guestbook.id IS '编号';
COMMENT ON COLUMN cms_guestbook.type IS '留言分类（1咨询、2建议、3投诉、4其它）';
COMMENT ON COLUMN cms_guestbook.content IS '留言内容';
COMMENT ON COLUMN cms_guestbook.name IS '姓名';
COMMENT ON COLUMN cms_guestbook.email IS '邮箱';
COMMENT ON COLUMN cms_guestbook.phone IS '电话';
COMMENT ON COLUMN cms_guestbook.workunit IS '单位';
COMMENT ON COLUMN cms_guestbook.ip IS 'IP';
COMMENT ON COLUMN cms_guestbook.create_date IS '留言时间';
COMMENT ON COLUMN cms_guestbook.re_user_id IS '回复人';
COMMENT ON COLUMN cms_guestbook.re_date IS '回复时间';
COMMENT ON COLUMN cms_guestbook.re_content IS '回复内容';
COMMENT ON COLUMN cms_guestbook.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_link IS '友情链接';
COMMENT ON COLUMN cms_link.id IS '编号';
COMMENT ON COLUMN cms_link.category_id IS '栏目编号';
COMMENT ON COLUMN cms_link.title IS '链接名称';
COMMENT ON COLUMN cms_link.color IS '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）';
COMMENT ON COLUMN cms_link.image IS '链接图片，如果上传了图片，则显示为图片链接';
COMMENT ON COLUMN cms_link.href IS '链接地址';
COMMENT ON COLUMN cms_link.weight IS '权重，越大越靠前';
COMMENT ON COLUMN cms_link.weight_date IS '权重期限，过期后将权重设置为：0';
COMMENT ON COLUMN cms_link.create_by IS '创建者';
COMMENT ON COLUMN cms_link.create_date IS '创建时间';
COMMENT ON COLUMN cms_link.update_by IS '更新者';
COMMENT ON COLUMN cms_link.update_date IS '更新时间';
COMMENT ON COLUMN cms_link.remarks IS '备注信息';
COMMENT ON COLUMN cms_link.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_site IS '站点表';
COMMENT ON COLUMN cms_site.id IS '编号';
COMMENT ON COLUMN cms_site.name IS '站点名称';
COMMENT ON COLUMN cms_site.title IS '站点标题';
COMMENT ON COLUMN cms_site.description IS '描述，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_site.keywords IS '关键字，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_site.theme IS '主题';
COMMENT ON COLUMN cms_site.copyright IS '版权信息';
COMMENT ON COLUMN cms_site.create_by IS '创建者';
COMMENT ON COLUMN cms_site.create_date IS '创建时间';
COMMENT ON COLUMN cms_site.update_by IS '更新者';
COMMENT ON COLUMN cms_site.update_date IS '更新时间';
COMMENT ON COLUMN cms_site.remarks IS '备注信息';
COMMENT ON COLUMN cms_site.del_flag IS '删除标记（0：正常；1：删除）';



