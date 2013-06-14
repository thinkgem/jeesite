
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
	id bigint NOT NULL IDENTITY ,
	category_id bigint NOT NULL,
	title nvarchar(255) NOT NULL,
	color nvarchar(50),
	image nvarchar(255),
	keywords nvarchar(255),
	description nvarchar(255),
	weight int DEFAULT 0,
	weight_date datetime,
	hits int DEFAULT 0,
	posid nvarchar(10),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_article_data
(
	id bigint NOT NULL IDENTITY ,
	content text,
	copyfrom nvarchar(255),
	relation nvarchar(255),
	allow_comment char(1),
	PRIMARY KEY (id)
);


CREATE TABLE cms_category
(
	id bigint NOT NULL IDENTITY ,
	site_id bigint DEFAULT 1,
	office_id bigint,
	parent_id bigint NOT NULL,
	parent_ids nvarchar(255) NOT NULL,
	module nvarchar(20),
	name nvarchar(100) NOT NULL,
	image nvarchar(255),
	href nvarchar(255),
	target nvarchar(20),
	description nvarchar(255),
	keywords nvarchar(255),
	sort int DEFAULT 30,
	in_menu char(1) DEFAULT '1',
	in_list char(1) DEFAULT '1',
	show_modes char(1) DEFAULT '0',
	allow_comment char(1),
	is_audit char(1),
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_comment
(
	id bigint NOT NULL IDENTITY ,
	category_id bigint NOT NULL,
	content_id bigint NOT NULL,
	title nvarchar(255),
	content nvarchar(255),
	name nvarchar(100),
	ip nvarchar(100),
	create_date datetime NOT NULL,
	audit_user_id bigint,
	audit_date datetime,
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_guestbook
(
	id bigint NOT NULL IDENTITY ,
	type char(1) NOT NULL,
	content nvarchar(255) NOT NULL,
	name nvarchar(100) NOT NULL,
	email nvarchar(100) NOT NULL,
	phone nvarchar(100) NOT NULL,
	workunit nvarchar(100) NOT NULL,
	ip nvarchar(100) NOT NULL,
	create_date datetime NOT NULL,
	re_user_id bigint,
	re_date datetime,
	re_content nvarchar(100),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_link
(
	id bigint NOT NULL IDENTITY ,
	category_id bigint NOT NULL,
	title nvarchar(255) NOT NULL,
	color nvarchar(50),
	image nvarchar(255),
	href nvarchar(255),
	weight int DEFAULT 0,
	weight_date datetime,
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
	del_flag char(1) DEFAULT '0' NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE cms_site
(
	id bigint NOT NULL IDENTITY ,
	name nvarchar(100) NOT NULL,
	title nvarchar(100) NOT NULL,
	description nvarchar(255),
	keywords nvarchar(255),
	theme nvarchar(255) DEFAULT 'default',
	copyright text,
	create_by bigint,
	create_date datetime,
	update_by bigint,
	update_date datetime,
	remarks nvarchar(255),
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



