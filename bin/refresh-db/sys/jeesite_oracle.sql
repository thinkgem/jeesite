
/* Drop Indexes */

DROP INDEX cms_article_user_id;
DROP INDEX cms_article_title;
DROP INDEX cms_article_keywords;
DROP INDEX cms_article_status;
DROP INDEX cms_article_weight;
DROP INDEX cms_article_input_date;
DROP INDEX cms_article_update_date;
DROP INDEX cms_category_parent_id;
DROP INDEX cms_category_parent_ids;
DROP INDEX cms_category_module;
DROP INDEX cms_category_name;
DROP INDEX cms_category_sort;
DROP INDEX cms_category_user_id;
DROP INDEX cms_category_del_flag;
DROP INDEX cms_comment_module;
DROP INDEX cms_comment_content_id;
DROP INDEX cms_comment_status;
DROP INDEX cms_guestbook_status;
DROP INDEX cms_link_user_id;
DROP INDEX cms_link_title;
DROP INDEX cms_link_status;
DROP INDEX cms_link_weight;
DROP INDEX cms_link_input_date;
DROP INDEX cms_link_update_date;
DROP INDEX cms_site_del_flag;
DROP INDEX sys_area_parent_id;
DROP INDEX sys_area_parent_ids;
DROP INDEX sys_area_del_flag;
DROP INDEX sys_dict_value;
DROP INDEX sys_dict_label;
DROP INDEX sys_dict_del_flag;
DROP INDEX sys_mdict_parent_id;
DROP INDEX sys_mdict_parent_ids;
DROP INDEX sys_mdict_del_flag;
DROP INDEX sys_menu_parent_id;
DROP INDEX sys_menu_parent_ids;
DROP INDEX sys_menu_user_id;
DROP INDEX sys_menu_del_flag;
DROP INDEX sys_office_parent_id;
DROP INDEX sys_office_parent_ids;
DROP INDEX sys_office_del_flag;
DROP INDEX sys_role_user_id;
DROP INDEX sys_role_del_flag;
DROP INDEX sys_user_office_id;
DROP INDEX sys_user_login_name;
DROP INDEX sys_user_del_flag;
DROP INDEX sys_user_department_id;
DROP INDEX sys_user_company_id;



/* Drop Triggers */

DROP TRIGGER TRI_cms_article_data_id;
DROP TRIGGER TRI_cms_article_id;
DROP TRIGGER TRI_cms_category_id;
DROP TRIGGER TRI_cms_comment_id;
DROP TRIGGER TRI_cms_guestbook_id;
DROP TRIGGER TRI_cms_link_id;
DROP TRIGGER TRI_cms_site_id;
DROP TRIGGER TRI_sys_area_id;
DROP TRIGGER TRI_sys_dict_id;
DROP TRIGGER TRI_sys_mdict_id;
DROP TRIGGER TRI_sys_menu_id;
DROP TRIGGER TRI_sys_office_id;
DROP TRIGGER TRI_sys_role_id;
DROP TRIGGER TRI_sys_user_id;



/* Drop Tables */

DROP TABLE cms_article_data;
DROP TABLE cms_article;
DROP TABLE cms_link;
DROP TABLE cms_category;
DROP TABLE cms_comment;
DROP TABLE cms_guestbook;
DROP TABLE cms_site;
DROP TABLE sys_user_role;
DROP TABLE sys_user;
DROP TABLE sys_office;
DROP TABLE sys_area;
DROP TABLE sys_dict;
DROP TABLE sys_mdict;
DROP TABLE sys_role_menu;
DROP TABLE sys_menu;
DROP TABLE sys_role;



/* Drop Sequences */

DROP SEQUENCE SEQ_cms_article_data_id;
DROP SEQUENCE SEQ_cms_article_id;
DROP SEQUENCE SEQ_cms_category_id;
DROP SEQUENCE SEQ_cms_comment_id;
DROP SEQUENCE SEQ_cms_guestbook_id;
DROP SEQUENCE SEQ_cms_link_id;
DROP SEQUENCE SEQ_cms_site_id;
DROP SEQUENCE SEQ_sys_area_id;
DROP SEQUENCE SEQ_sys_dict_id;
DROP SEQUENCE SEQ_sys_mdict_id;
DROP SEQUENCE SEQ_sys_menu_id;
DROP SEQUENCE SEQ_sys_office_id;
DROP SEQUENCE SEQ_sys_role_id;
DROP SEQUENCE SEQ_sys_user_id;




/* Create Sequences */

CREATE SEQUENCE SEQ_cms_article_data_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_article_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_category_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_comment_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_guestbook_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_link_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_cms_site_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_area_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_dict_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_mdict_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_menu_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_office_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_role_id INCREMENT BY 1 START WITH 1;
CREATE SEQUENCE SEQ_sys_user_id INCREMENT BY 1 START WITH 1;



/* Create Tables */

CREATE TABLE cms_article
(
	id number(19,0) NOT NULL,
	category_id number(19,0) NOT NULL,
	user_id number(19,0) NOT NULL,
	title varchar2(255) NOT NULL,
	color varchar2(50),
	thumb varchar2(255),
	keywords varchar2(255),
	description varchar2(255),
	status char(1) NOT NULL,
	weight number(10,0) DEFAULT 0,
	hits number(10,0) DEFAULT 0,
	posid varchar2(10),
	input_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_date timestamp,
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
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	module varchar2(20),
	name varchar2(100) NOT NULL,
	image varchar2(255),
	href varchar2(255),
	target varchar2(20),
	description varchar2(255),
	keywords varchar2(255),
	sort number(0,0) DEFAULT 30,
	in_menu char(1) DEFAULT '1',
	in_list char(1) DEFAULT '1',
	show_modes char(1) DEFAULT '0',
	allow_comment char(1),
	user_id number(19,0) NOT NULL,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_comment
(
	id number(19,0) NOT NULL,
	module varchar2(20) NOT NULL,
	content_id number(19,0) NOT NULL,
	title varchar2(255),
	content varchar2(255),
	name varchar2(100),
	ip varchar2(100),
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	audit_user_id number(19,0),
	audit_date timestamp,
	status char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_guestbook
(
	id number(19,0) NOT NULL,
	type varchar2(100) NOT NULL,
	content varchar2(255) NOT NULL,
	name varchar2(100) NOT NULL,
	email varchar2(100) NOT NULL,
	phone varchar2(100) NOT NULL,
	workunit varchar2(100) NOT NULL,
	ip varchar2(100) NOT NULL,
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	re_user_id number(19,0),
	re_date timestamp,
	re_content varchar2(100),
	status char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_link
(
	id number(19,0) NOT NULL,
	category_id number(19,0) NOT NULL,
	user_id number(19,0) NOT NULL,
	title varchar2(255) NOT NULL,
	color varchar2(50),
	image varchar2(255),
	href varchar2(255),
	remarks varchar2(255),
	status char(1) NOT NULL,
	weight number(10,0) DEFAULT 0,
	input_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	update_date timestamp,
	PRIMARY KEY (id)
);


CREATE TABLE cms_site
(
	id number(19,0) NOT NULL,
	name varchar2(100) NOT NULL,
	title varchar2(100) NOT NULL,
	description varchar2(255),
	keywords varchar2(255),
	theme varchar2(255) DEFAULT 'default',
	copyright clob,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_area
(
	id number(19,0) NOT NULL,
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	code varchar2(100),
	name varchar2(100) NOT NULL,
	type varchar2(100),
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_dict
(
	id number(19,0) NOT NULL,
	label varchar2(100) NOT NULL,
	value varchar2(100) NOT NULL,
	type varchar2(100) NOT NULL,
	description varchar2(100) NOT NULL,
	sort number(0,0) NOT NULL,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_mdict
(
	id number(19,0) NOT NULL,
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	name varchar2(100) NOT NULL,
	description varchar2(100),
	sort number(0,0),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_menu
(
	id number(19,0) NOT NULL,
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	name varchar2(100) NOT NULL,
	href varchar2(255),
	target varchar2(20),
	icon varchar2(100),
	sort number(0,0) NOT NULL,
	is_show char(1) NOT NULL,
	permission varchar2(200),
	user_id number(19,0) NOT NULL,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_office
(
	id number(19,0) NOT NULL,
	parent_id number(19,0) NOT NULL,
	parent_ids varchar2(255) NOT NULL,
	area_id number(19,0) NOT NULL,
	code varchar2(100),
	name varchar2(100) NOT NULL,
	type varchar2(100) NOT NULL,
	remarks varchar2(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role
(
	id number(19,0) NOT NULL,
	name varchar2(100) NOT NULL,
	user_id number(19,0) NOT NULL,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE sys_role_menu
(
	role_id number(19,0) NOT NULL,
	menu_id number(19,0) NOT NULL,
	PRIMARY KEY (role_id, menu_id)
);


CREATE TABLE sys_user
(
	id number(19,0) NOT NULL,
	company_id number(19,0) NOT NULL,
	department_id number(19,0) NOT NULL,
	office_id number(19,0) NOT NULL,
	login_name varchar2(100) NOT NULL,
	password varchar2(100) NOT NULL,
	name varchar2(100) NOT NULL,
	email varchar2(200),
	phone varchar2(200),
	mobile varchar2(200),
	remarks varchar2(255),
	user_type varchar2(100),
	create_date timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	del_flag char(1) DEFAULT '0' NOT NULL,
	login_ip varchar2(100),
	login_date timestamp,
	PRIMARY KEY (id)
);


CREATE TABLE sys_user_role
(
	user_id number(19,0) NOT NULL,
	role_id number(19,0) NOT NULL,
	PRIMARY KEY (user_id, role_id)
);



/* Create Triggers */

CREATE TRIGGER TRI_cms_article_data_id BEFORE INSERT ON cms_article_data
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_article_data_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_article_id BEFORE INSERT ON cms_article
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_article_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_category_id BEFORE INSERT ON cms_category
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_category_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_comment_id BEFORE INSERT ON cms_comment
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_comment_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_guestbook_id BEFORE INSERT ON cms_guestbook
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_guestbook_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_link_id BEFORE INSERT ON cms_link
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_link_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_cms_site_id BEFORE INSERT ON cms_site
FOR EACH ROW
BEGIN
	SELECT SEQ_cms_site_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_area_id BEFORE INSERT ON sys_area
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_area_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_dict_id BEFORE INSERT ON sys_dict
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_dict_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_mdict_id BEFORE INSERT ON sys_mdict
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_mdict_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_menu_id BEFORE INSERT ON sys_menu
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_menu_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_office_id BEFORE INSERT ON sys_office
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_office_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_role_id BEFORE INSERT ON sys_role
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_role_id.nextval
	INTO :new.id
	FROM dual; END;;
CREATE TRIGGER TRI_sys_user_id BEFORE INSERT ON sys_user
FOR EACH ROW
BEGIN
	SELECT SEQ_sys_user_id.nextval
	INTO :new.id
	FROM dual; END;;



/* Create Indexes */

CREATE INDEX cms_article_user_id ON cms_article (user_id);
CREATE INDEX cms_article_title ON cms_article (title);
CREATE INDEX cms_article_keywords ON cms_article (keywords);
CREATE INDEX cms_article_status ON cms_article (status);
CREATE INDEX cms_article_weight ON cms_article (weight);
CREATE INDEX cms_article_input_date ON cms_article (input_date);
CREATE INDEX cms_article_update_date ON cms_article (update_date);
CREATE INDEX cms_category_parent_id ON cms_category (parent_id);
CREATE INDEX cms_category_parent_ids ON cms_category (parent_ids);
CREATE INDEX cms_category_module ON cms_category (module);
CREATE INDEX cms_category_name ON cms_category (name);
CREATE INDEX cms_category_sort ON cms_category (sort);
CREATE INDEX cms_category_user_id ON cms_category (user_id);
CREATE INDEX cms_category_del_flag ON cms_category (del_flag);
CREATE INDEX cms_comment_module ON cms_comment (module);
CREATE INDEX cms_comment_content_id ON cms_comment (content_id);
CREATE INDEX cms_comment_status ON cms_comment (status);
CREATE INDEX cms_guestbook_status ON cms_guestbook (status);
CREATE INDEX cms_link_user_id ON cms_link (user_id);
CREATE INDEX cms_link_title ON cms_link (title);
CREATE INDEX cms_link_status ON cms_link (status);
CREATE INDEX cms_link_weight ON cms_link (weight);
CREATE INDEX cms_link_input_date ON cms_link (input_date);
CREATE INDEX cms_link_update_date ON cms_link (update_date);
CREATE INDEX cms_site_del_flag ON cms_site (del_flag);
CREATE INDEX sys_area_parent_id ON sys_area (parent_id);
CREATE INDEX sys_area_parent_ids ON sys_area (parent_ids);
CREATE INDEX sys_area_del_flag ON sys_area (del_flag);
CREATE INDEX sys_dict_value ON sys_dict (value);
CREATE INDEX sys_dict_label ON sys_dict (label);
CREATE INDEX sys_dict_del_flag ON sys_dict (del_flag);
CREATE INDEX sys_mdict_parent_id ON sys_mdict (parent_id);
CREATE INDEX sys_mdict_parent_ids ON sys_mdict (parent_ids);
CREATE INDEX sys_mdict_del_flag ON sys_mdict (del_flag);
CREATE INDEX sys_menu_parent_id ON sys_menu (parent_id);
CREATE INDEX sys_menu_parent_ids ON sys_menu (parent_ids);
CREATE INDEX sys_menu_user_id ON sys_menu (user_id);
CREATE INDEX sys_menu_del_flag ON sys_menu (del_flag);
CREATE INDEX sys_office_parent_id ON sys_office (parent_id);
CREATE INDEX sys_office_parent_ids ON sys_office (parent_ids);
CREATE INDEX sys_office_del_flag ON sys_office (del_flag);
CREATE INDEX sys_role_user_id ON sys_role (user_id);
CREATE INDEX sys_role_del_flag ON sys_role (del_flag);
CREATE INDEX sys_user_office_id ON sys_user (office_id);
CREATE INDEX sys_user_login_name ON sys_user (login_name);
CREATE INDEX sys_user_del_flag ON sys_user (del_flag);
CREATE INDEX sys_user_department_id ON sys_user (department_id);
CREATE INDEX sys_user_company_id ON sys_user (company_id);



/* Comments */

COMMENT ON TABLE cms_article IS '文章表';
COMMENT ON COLUMN cms_article.id IS '编号';
COMMENT ON COLUMN cms_article.category_id IS '栏目编号';
COMMENT ON COLUMN cms_article.user_id IS '发布者';
COMMENT ON COLUMN cms_article.title IS '标题';
COMMENT ON COLUMN cms_article.color IS '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）';
COMMENT ON COLUMN cms_article.thumb IS '缩略图';
COMMENT ON COLUMN cms_article.keywords IS '关键字';
COMMENT ON COLUMN cms_article.description IS '描述、摘要';
COMMENT ON COLUMN cms_article.status IS '状态（0：发布；1：作废；2：审核；）';
COMMENT ON COLUMN cms_article.weight IS '权重，越大越靠前';
COMMENT ON COLUMN cms_article.hits IS '点击数';
COMMENT ON COLUMN cms_article.posid IS '推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）';
COMMENT ON COLUMN cms_article.input_date IS '录入时间';
COMMENT ON COLUMN cms_article.update_date IS '更新时间';
COMMENT ON TABLE cms_article_data IS '文章详表';
COMMENT ON COLUMN cms_article_data.id IS '编号';
COMMENT ON COLUMN cms_article_data.content IS '文章内容';
COMMENT ON COLUMN cms_article_data.copyfrom IS '文章来源';
COMMENT ON COLUMN cms_article_data.relation IS '相关文章';
COMMENT ON COLUMN cms_article_data.allow_comment IS '是否允许评论';
COMMENT ON TABLE cms_category IS '栏目表';
COMMENT ON COLUMN cms_category.id IS '编号';
COMMENT ON COLUMN cms_category.site_id IS '站点编号';
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
COMMENT ON COLUMN cms_category.user_id IS '创建者';
COMMENT ON COLUMN cms_category.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE cms_comment IS '评论表';
COMMENT ON COLUMN cms_comment.id IS '编号';
COMMENT ON COLUMN cms_comment.module IS '栏目模块（article：文章；picture：图片；download：下载）';
COMMENT ON COLUMN cms_comment.content_id IS '栏目内容的编号（Article.id、Photo.id、Download.id）';
COMMENT ON COLUMN cms_comment.title IS '栏目内容的标题（Article.title、Photo.title、Download.title）';
COMMENT ON COLUMN cms_comment.content IS '评论内容';
COMMENT ON COLUMN cms_comment.name IS '评论姓名';
COMMENT ON COLUMN cms_comment.ip IS '评论IP';
COMMENT ON COLUMN cms_comment.create_date IS '评论时间';
COMMENT ON COLUMN cms_comment.audit_user_id IS '审核人';
COMMENT ON COLUMN cms_comment.audit_date IS '审核时间';
COMMENT ON COLUMN cms_comment.status IS '状态（0：发布；1：作废；2：审核；）';
COMMENT ON TABLE cms_guestbook IS '留言板';
COMMENT ON COLUMN cms_guestbook.id IS '编号';
COMMENT ON COLUMN cms_guestbook.type IS '留言分类（咨询、建议、投诉、其它）';
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
COMMENT ON COLUMN cms_guestbook.status IS '状态（0：发布；1：作废；2：审核；）';
COMMENT ON TABLE cms_link IS '友情链接';
COMMENT ON COLUMN cms_link.id IS '编号';
COMMENT ON COLUMN cms_link.category_id IS '栏目编号';
COMMENT ON COLUMN cms_link.user_id IS '发布者';
COMMENT ON COLUMN cms_link.title IS '链接名称';
COMMENT ON COLUMN cms_link.color IS '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）';
COMMENT ON COLUMN cms_link.image IS '链接图片，如果上传了图片，则显示为图片链接';
COMMENT ON COLUMN cms_link.href IS '链接地址';
COMMENT ON COLUMN cms_link.remarks IS '备注';
COMMENT ON COLUMN cms_link.status IS '状态（0：发布；1：作废；2：审核；）';
COMMENT ON COLUMN cms_link.weight IS '权重，越大越靠前';
COMMENT ON COLUMN cms_link.input_date IS '录入时间';
COMMENT ON COLUMN cms_link.update_date IS '更新时间';
COMMENT ON TABLE cms_site IS '站点表';
COMMENT ON COLUMN cms_site.id IS '编号';
COMMENT ON COLUMN cms_site.name IS '站点名称';
COMMENT ON COLUMN cms_site.title IS '站点标题';
COMMENT ON COLUMN cms_site.description IS '描述，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_site.keywords IS '关键字，填写有助于搜索引擎优化';
COMMENT ON COLUMN cms_site.theme IS '主题';
COMMENT ON COLUMN cms_site.copyright IS '版权信息';
COMMENT ON COLUMN cms_site.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_area IS '区域表';
COMMENT ON COLUMN sys_area.id IS '编号';
COMMENT ON COLUMN sys_area.parent_id IS '父级编号';
COMMENT ON COLUMN sys_area.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_area.code IS '区域编码';
COMMENT ON COLUMN sys_area.name IS '区域名称';
COMMENT ON COLUMN sys_area.type IS '区域类型（1：国家；2：地区；3：省份；4：地市；5：区县）';
COMMENT ON COLUMN sys_area.remarks IS '备注';
COMMENT ON COLUMN sys_area.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_dict IS '字典表';
COMMENT ON COLUMN sys_dict.id IS '编号';
COMMENT ON COLUMN sys_dict.label IS '标签名';
COMMENT ON COLUMN sys_dict.value IS '数据值';
COMMENT ON COLUMN sys_dict.type IS '类型';
COMMENT ON COLUMN sys_dict.description IS '描述';
COMMENT ON COLUMN sys_dict.sort IS '排序（升序）';
COMMENT ON COLUMN sys_dict.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_mdict IS '区域表';
COMMENT ON COLUMN sys_mdict.id IS '编号';
COMMENT ON COLUMN sys_mdict.parent_id IS '父级编号';
COMMENT ON COLUMN sys_mdict.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_mdict.name IS '名称';
COMMENT ON COLUMN sys_mdict.description IS '描述';
COMMENT ON COLUMN sys_mdict.sort IS '排序（升序）';
COMMENT ON COLUMN sys_mdict.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_menu IS '菜单表';
COMMENT ON COLUMN sys_menu.id IS '编号';
COMMENT ON COLUMN sys_menu.parent_id IS '父级编号';
COMMENT ON COLUMN sys_menu.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_menu.name IS '菜单名称';
COMMENT ON COLUMN sys_menu.href IS '链接';
COMMENT ON COLUMN sys_menu.target IS '目标（mainFrame、 _blank、_self、_parent、_top）';
COMMENT ON COLUMN sys_menu.icon IS '图标';
COMMENT ON COLUMN sys_menu.sort IS '排序（升序）';
COMMENT ON COLUMN sys_menu.is_show IS '是否在菜单中显示（1：显示；0：不显示）';
COMMENT ON COLUMN sys_menu.permission IS '权限标识';
COMMENT ON COLUMN sys_menu.user_id IS '创建者';
COMMENT ON COLUMN sys_menu.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_office IS '部门表';
COMMENT ON COLUMN sys_office.id IS '编号';
COMMENT ON COLUMN sys_office.parent_id IS '父级编号';
COMMENT ON COLUMN sys_office.parent_ids IS '所有父级编号';
COMMENT ON COLUMN sys_office.area_id IS '归属区域';
COMMENT ON COLUMN sys_office.code IS '区域编码';
COMMENT ON COLUMN sys_office.name IS '机构名称';
COMMENT ON COLUMN sys_office.type IS '机构类型（1：公司；2：部门；3：小组）';
COMMENT ON COLUMN sys_office.remarks IS '备注';
COMMENT ON COLUMN sys_office.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_role IS '角色表';
COMMENT ON COLUMN sys_role.id IS '编号';
COMMENT ON COLUMN sys_role.name IS '名称';
COMMENT ON COLUMN sys_role.user_id IS '创建者';
COMMENT ON COLUMN sys_role.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON TABLE sys_role_menu IS '角色-菜单';
COMMENT ON COLUMN sys_role_menu.role_id IS '角色编号';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单编号';
COMMENT ON TABLE sys_user IS '用户';
COMMENT ON COLUMN sys_user.id IS '编号';
COMMENT ON COLUMN sys_user.company_id IS '归属公司编号';
COMMENT ON COLUMN sys_user.department_id IS '编号';
COMMENT ON COLUMN sys_user.office_id IS '归属小组编号';
COMMENT ON COLUMN sys_user.login_name IS '登录名';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.name IS '姓名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '电话';
COMMENT ON COLUMN sys_user.mobile IS '手机';
COMMENT ON COLUMN sys_user.remarks IS '备注';
COMMENT ON COLUMN sys_user.user_type IS '用户类型';
COMMENT ON COLUMN sys_user.create_date IS '创建时间';
COMMENT ON COLUMN sys_user.del_flag IS '删除标记（0：正常；1：删除）';
COMMENT ON COLUMN sys_user.login_ip IS '最后登陆IP';
COMMENT ON COLUMN sys_user.login_date IS '最后登陆时间';
COMMENT ON TABLE sys_user_role IS '用户-角色';
COMMENT ON COLUMN sys_user_role.user_id IS '用户编号';
COMMENT ON COLUMN sys_user_role.role_id IS '角色编号';



