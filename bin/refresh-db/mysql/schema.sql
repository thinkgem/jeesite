-- 内容管理留言本表
drop table if exists cms_guestbook;
create table cms_guestbook (
	id bigint not null auto_increment comment '编号',
	type varchar(100) not null comment '留言分类（咨询、建议、投诉、其它）',
	content varchar(255) not null comment '留言内容',
	name varchar(100) not null comment '姓名',
	email varchar(100) not null comment '邮箱',
	phone varchar(100) not null comment '电话',
	workunit varchar(100) not null comment '单位',
	ip varchar(100) not null comment 'IP',
	create_date timestamp default now() comment '留言时间',
	re_user_id bigint comment '回复人',
	re_date timestamp null comment '回复时间',
	re_content varchar(100) comment '回复内容',
	status char(1) default '0' comment '状态（0：发布；1：作废；2：审核；）',
	primary key (id),
	key (status)
) engine=innodb comment '内容管理留言本表';

-- 内容管理内容评论表
drop table if exists cms_comment;
create table cms_comment (
	id bigint not null auto_increment comment '编号',
	module varchar(20) not null comment '内容模型（article：文章；picture：图片；download：下载）',
	content_id bigint not null comment '归属分类内容的编号（Article.id、Photo.id、Download.id）',
	title varchar(255) comment '归属分类内容的标题（Article.title、Photo.title、Download.title）',
	content varchar(255) comment '评论内容',
	name varchar(100) comment '评论姓名',
	ip varchar(100) comment '评论IP',
	create_date timestamp default now() comment '评论时间',
	audit_user_id bigint comment '审核人',
	audit_date timestamp null comment '审核时间',
	status char(1) default '0' comment '删除标记（0：发布；1：作废；2：审核；）',
	primary key (id),
	key (module),
	key (content_id),
	key (status)
) engine=innodb comment '内容管理内容评论表';

-- 内容管理链接模型表
drop table if exists cms_link;
create table cms_link (
	id bigint not null auto_increment comment '编号',
	category_id bigint not null comment '分类编号',
	user_id bigint not null comment '发布者',
	title varchar(255) not null comment '链接名称',
	color varchar(50) default '' comment '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
	image varchar(255) default '' comment '如果上传了图片，则显示为图片链接',
	href varchar(255) default '' comment '链接地址',
	remarks varchar(255) default '' comment '备注',
	status char(1) default '' comment '状态（0：发布；1：作废；2：审核；）',
	weight int default 0 comment '权重，越大越靠前',
	input_date timestamp default now() comment '录入时间',
	update_date timestamp comment '更新时间',
	primary key (id),
	key (user_id),
	key (title),
	key (status),
	key (weight),
	key (input_date),
	key (update_date)
) engine=innodb comment '内容管理链接模型表';

-- 内容管理文章模型表
drop table if exists cms_article;
create table cms_article (
	id bigint not null auto_increment comment '编号',
	category_id bigint not null comment '分类编号',
	user_id bigint not null comment '发布者',
	title varchar(255) not null comment '标题',
	color varchar(50) default '' comment '标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）',
	thumb varchar(255) default '' comment '缩略图',
	keywords varchar(255) default '' comment '关键字',
	desciption varchar(255) default '' comment '描述、摘要',
	status char(1) default '' comment '状态（0：发布；1：作废；2：审核；）',
	weight int default 0 comment '权重，越大越靠前',
    hits int default 0 comment '点击数',
    posid varchar(10) comment '推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）',
	input_date timestamp default now() comment '录入时间',
	update_date timestamp comment '更新时间',
	primary key (id),
	key (user_id),
	key (title),
	key (keywords),
	key (status),
	key (weight),
	key (input_date),
	key (update_date)
) engine=innodb comment '内容管理文章模型表';

-- 内容管理文章模型副表
drop table if exists cms_article_data;
create table cms_article_data (
	id bigint not null auto_increment comment '编号',
	content mediumtext comment '内容',
	copyfrom varchar(255) comment '来源',
	relation varchar(255) comment '相关文章',
	allow_comment char(1) comment '是否允许评论',
	primary key (id)
) engine=innodb comment '内容管理文章模型表';

-- 内容管理栏目表
drop table if exists cms_category;
create table cms_category (
	id bigint not null auto_increment comment '编号',
    site_id bigint default '1' comment '站点编号',
    parent_id bigint not null comment '父级编号',
	parent_ids varchar(255) not null comment '所有父级编号',
	module varchar(20) not null comment '栏目模块（article：文章；picture：图片；download：下载；link：链接；special：专题）',
	name varchar(100) not null comment '栏目名称',
	image varchar(255) default '' comment '栏目图片',
    href varchar(255) not null comment '链接',
    target varchar(20) not null comment '目标（ _blank、_self、_parent、_top）',
	desciption varchar(255) default '' comment '描述，填写有助于搜索引擎优化',
    keywords varchar(255) default '' comment '关键字，填写有助于搜索引擎优化',
    sort int default 30 comment '排序（升序）',
    in_menu char(1) default '1' comment '是否在导航中显示（1：显示；0：不显示）',
    in_list char(1) default '1' comment '是否在分类页中显示列表（1：显示；0：不显示）',
	show_modes char(1) default '0' comment '展现方式（0:有子栏目显示栏目列表，无子栏目显示内容列表;1：首栏目内容列表；2：栏目第一条内容）',
	allow_comment char(1) comment '是否允许评论',
	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
	primary key (id),
	key (parent_id),
	key (parent_ids),
	key (module),
	key (name),
	key (sort),
	key (del_flag)
) engine=innodb comment '内容管理栏目表';

-- 内容管理站点配置表
drop table if exists cms_site;
create table cms_site (
	id bigint not null auto_increment comment '编号',
	name varchar(100) not null comment '站点名称',
	title varchar(100) not null comment '站点标题',
	desciption varchar(255) default '' comment '描述，填写有助于搜索引擎优化',
    keywords varchar(255) default '' comment '关键字，填写有助于搜索引擎优化',
    theme varchar(255) default 'default' comment '主题',
    copyright mediumtext comment '版权信息',
	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
	primary key (id),
	key (del_flag)
) engine=innodb comment '内容管理站点配置表';

-- 系统上传文件表
--drop table if exists sys_attachment;
--create table sys_attachment (
--	id bigint not null auto_increment comment '编号',
--	area_id bigint not null comment '归属区域',
--	office_id bigint not null comment '归属部门',
--	user_id bigint not null comment '上传者',
--	name varchar(255) not null comment '文件名',
--	file_name varchar(255) not null comment '上传后的文件名',
--	file_ext varchar(16) not null comment '扩展名',
--	file_type varchar(100) not null comment '文件类型',
--	file_size bigint not null comment '文件大小',
--	file_path varchar(255) not null comment '存放路径（/files/year/month/area_office_user/）',
--	create_date timestamp default now() comment '上传时间',
--	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
--	primary key (id),
--	key (area_id),
--	key (office_id),
--	key (user_id),
--	key (del_flag)
--) engine=innodb comment '系统上传文件表';

-- 系统字典表
drop table if exists sys_dict;
create table sys_dict (
	id bigint not null auto_increment comment '编号',
	label varchar(100) not null comment '标签',
	value varchar(100) not null comment '键值',
	type varchar(100) not null comment '类型',
	desciption varchar(100) not null comment '描述',
    sort int not null comment '排序（升序）',
	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
	primary key (id),
	key (value),
	key (label),
	key (del_flag)
) engine=innodb comment '系统字典表';

-- 系统区域表
drop table if exists sys_area;
create table sys_area (
	id bigint not null auto_increment comment '编号',
	parent_id bigint not null comment '父级编号',
	parent_ids varchar(255) not null comment '所有父级编号',
	code varchar(100) comment '区域编码',
	name varchar(100) comment '区域名称',
	remarks varchar(255) comment '备注',
	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
	primary key (id),
	key (parent_id),
	key (parent_ids),
	key (del_flag)
) engine=innodb comment '系统区域表';

-- 系统部门表
drop table if exists sys_office;
create table sys_office (
	id bigint not null auto_increment comment '编号',
	parent_id bigint not null comment '父级编号',
	parent_ids varchar(255) not null comment '所有父级编号',
	area_id bigint not null comment '归属区域',
	code varchar(100) comment '区域编码',
	name varchar(100) not null comment '部门名称',
	remarks varchar(255) comment '备注',
	del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
	primary key (id),
	key (parent_id),
	key (parent_ids),
	key (del_flag)
) engine=innodb comment '系统部门表';

-- 系统菜单表
drop table if exists sys_menu;
create table sys_menu (
    id bigint not null auto_increment comment '编号',
    parent_id bigint not null comment '父级编号',
	parent_ids varchar(255) not null comment '所有父级编号',
    name varchar(100) not null comment '菜单名称',
    href varchar(255) not null comment '链接',
    target varchar(20) not null comment '目标（mainFrame、 _blank、_self、_parent、_top）',
    icon varchar(100) not null comment '图标',
    sort int not null comment '排序（升序）',
    is_show char(1) not null comment '是否在菜单中显示（1：显示；0：不显示）',
    permission varchar(200) not null comment '权限标识',
    del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
    primary key (id),
	key (parent_id),
	key (parent_ids),
	key (del_flag)
) engine=innodb comment '系统菜单表';

-- 系统角色表
drop table if exists sys_role;
create table sys_role (
    id bigint not null auto_increment comment '编号',
    name varchar(100) not null comment '名称',
    del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
    primary key (id),
	key (del_flag)
) engine=innodb comment '系统角色表';

-- 系统角色与菜单关联表
drop table if exists sys_role_menu;
create table sys_role_menu (
    role_id bigint not null comment '角色编号',
    menu_id bigint not null comment '菜单编号',
	primary key (role_id,menu_id)
) engine=innodb comment '系统角色与菜单关联表';

-- 系统角色与菜单关联表
drop table if exists sys_role_category;
create table sys_role_category (
  role_id bigint(20) not null comment '角色编号',
  category_id bigint(20) not null comment '内容分类编号',
  primary key (role_id,category_id)
) engine=innodb comment '系统角色与内容分类关联表';

-- 系统用户表
drop table if exists sys_user;
create table sys_user (
    id bigint not null auto_increment comment '编号',
    area_id bigint not null comment '区域编号',
    office_id bigint not null comment '部门编号',
    login_name varchar(100) not null comment '登录名',
    password varchar(100) comment '密码',
    name varchar(100) comment '姓名',
    email varchar(200) comment '邮箱',
    phone varchar(200) comment '电话',
    mobile varchar(200) comment '手机',
    remarks varchar(255) default '' comment '备注',
    user_type varchar(100) default '' comment '用户类型',
    create_date timestamp default now() comment '创建时间',
    del_flag char(1) default '0' comment '删除标记（0：正常；1：删除）',
    login_ip varchar(100) comment '最后登陆IP',
    login_date datetime comment '最后登陆时间',
    primary key (id),
	key (area_id),
	key (office_id),
	key (login_name),
	key (del_flag)
) engine=innodb comment '系统用户表';

-- 系统用户与角色关联表
drop table if exists sys_user_role;
create table sys_user_role (
    user_id bigint not null comment '用户编号',
    role_id bigint not null comment '角色编号',
	primary key (user_id,role_id)
) engine=innodb comment '系统用户与角色关联表';
