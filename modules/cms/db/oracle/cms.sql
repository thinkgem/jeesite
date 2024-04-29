

/* Create Tables */

-- 文章表
CREATE TABLE js_cms_article
(
	id varchar2(64) NOT NULL,
	category_code varchar2(64) NOT NULL,
	module_type varchar2(50),
	title nvarchar2(255) NOT NULL,
	href nvarchar2(1000),
	color varchar2(50),
	image nvarchar2(1000),
	keywords nvarchar2(500),
	description nvarchar2(500),
	weight number(10) DEFAULT 0,
	weight_date timestamp,
	article_source char(1),
	copyfrom nvarchar2(255),
	hits number(20) DEFAULT 0,
	hits_plus number(10),
	hits_minus number(10),
	word_count number(10),
	custom_content_view varchar2(255),
	view_config nvarchar2(1000),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 文章详情表
CREATE TABLE js_cms_article_data
(
	id varchar2(64) NOT NULL,
	content clob,
	relation varchar2(1000),
	is_can_comment char(1),
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	extend_json varchar2(1000),
	PRIMARY KEY (id)
);


-- 文章推荐位
CREATE TABLE js_cms_article_posid
(
	article_id varchar2(64) NOT NULL,
	postid char(1) NOT NULL
);


-- 文章与标签关系
CREATE TABLE js_cms_article_tag
(
	article_id varchar2(64) NOT NULL,
	tag_name nvarchar2(200) NOT NULL
);


-- 栏目表
CREATE TABLE js_cms_category
(
	category_code varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(767) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(767) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names nvarchar2(767) NOT NULL,
	category_name nvarchar2(100) NOT NULL,
	site_code varchar2(64) NOT NULL,
	module_type varchar2(50),
	image varchar2(255),
	href varchar2(255),
	target varchar2(20),
	keywords nvarchar2(500),
	description nvarchar2(500),
	in_menu char(1),
	in_list char(1),
	show_modes char(1),
	is_need_audit char(1),
	is_can_comment char(1),
	custom_list_view varchar2(255),
	custom_content_view varchar2(255),
	view_config nvarchar2(1000),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	extend_s1 nvarchar2(500),
	extend_s2 nvarchar2(500),
	extend_s3 nvarchar2(500),
	extend_s4 nvarchar2(500),
	extend_s5 nvarchar2(500),
	extend_s6 nvarchar2(500),
	extend_s7 nvarchar2(500),
	extend_s8 nvarchar2(500),
	extend_i1 number(19),
	extend_i2 number(19),
	extend_i3 number(19),
	extend_i4 number(19),
	extend_f1 number(19,4),
	extend_f2 number(19,4),
	extend_f3 number(19,4),
	extend_f4 number(19,4),
	extend_d1 timestamp,
	extend_d2 timestamp,
	extend_d3 timestamp,
	extend_d4 timestamp,
	extend_json varchar2(1000),
	PRIMARY KEY (category_code)
);


-- 文章评论表
CREATE TABLE js_cms_comment
(
	id varchar2(64) NOT NULL,
	category_code varchar2(64) NOT NULL,
	article_id varchar2(64) NOT NULL,
	parent_id varchar2(64),
	article_title nvarchar2(255) NOT NULL,
	content nvarchar2(255) NOT NULL,
	name nvarchar2(50),
	ip varchar2(100),
	create_by varchar2(64),
	create_date timestamp NOT NULL,
	audit_user_code varchar2(64),
	audit_date timestamp,
	audit_comment nvarchar2(200),
	hits_plus number(10),
	hits_minus number(10),
	status char(1) NOT NULL,
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 留言板表
CREATE TABLE js_cms_guestbook
(
	id varchar2(64) NOT NULL,
	gb_type char(1) NOT NULL,
	content varchar2(255) NOT NULL,
	name varchar2(100) NOT NULL,
	email varchar2(100) NOT NULL,
	phone varchar2(100) NOT NULL,
	workunit varchar2(100) NOT NULL,
	ip varchar2(100) NOT NULL,
	create_by varchar2(64),
	create_date timestamp,
	re_user_code varchar2(64),
	re_date timestamp,
	re_content varchar2(100),
	status char(1) NOT NULL,
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 内容举报表
CREATE TABLE js_cms_report
(
	id varchar2(64) NOT NULL,
	report_source char(1),
	report_content nvarchar2(500),
	report_url nvarchar2(1000),
	report_type char(1),
	report_cause nvarchar2(500),
	PRIMARY KEY (id)
);


-- 站点表
CREATE TABLE js_cms_site
(
	site_code varchar2(64) NOT NULL,
	site_name nvarchar2(100) NOT NULL,
	site_sort number(10),
	title nvarchar2(100) NOT NULL,
	logo nvarchar2(1000),
	domain_name nvarchar2(500),
	keywords nvarchar2(500),
	description nvarchar2(500),
	theme nvarchar2(500),
	copyright nvarchar2(1000),
	custom_index_view varchar2(500),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
	PRIMARY KEY (site_code)
);


-- 内容标签
CREATE TABLE js_cms_tag
(
	tag_name nvarchar2(200) NOT NULL,
	clicknum number(10) NOT NULL,
	PRIMARY KEY (tag_name)
);


-- 访问日志表
CREATE TABLE js_cms_visit_log
(
	id varchar2(64) NOT NULL,
	request_url nvarchar2(1000),
	request_url_host varchar2(128),
	source_referer nvarchar2(1000),
	source_referer_host varchar2(128),
	source_type char(1),
	search_engine varchar2(200),
	search_word nvarchar2(200),
	remote_addr varchar2(50),
	user_agent nvarchar2(500),
	user_language varchar2(32),
	user_screen_size varchar2(32),
	user_device varchar2(32),
	user_os_name varchar2(32),
	user_browser varchar2(32),
	user_browser_version varchar2(16),
	unique_visit_id varchar2(64),
	visit_date char(8),
	visit_time timestamp,
	is_new_visit char(1),
	first_visit_time number(20),
	prev_remain_time number(20),
	total_remain_time number(20),
	site_code varchar2(64),
	site_name nvarchar2(100),
	category_code varchar2(64),
	category_name nvarchar2(100),
	content_id varchar2(64),
	content_title nvarchar2(255),
	visit_user_code varchar2(100),
	visit_user_name varchar2(100),
	corp_code varchar2(64) DEFAULT '0' NOT NULL,
	corp_name nvarchar2(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX idx_cms_article_cb ON js_cms_article (create_by);
CREATE INDEX idx_cms_article_cc ON js_cms_article (category_code);
CREATE INDEX idx_cms_article_corp_code ON js_cms_article (corp_code);
CREATE INDEX idx_cms_article_status ON js_cms_article (status);
CREATE INDEX idx_cms_article_ud ON js_cms_article (update_date);
CREATE INDEX idx_cms_article_weight ON js_cms_article (weight);
CREATE INDEX idx_cms_category_pc ON js_cms_category (parent_code);
CREATE INDEX idx_cms_category_ts ON js_cms_category (tree_sort);
CREATE INDEX idx_cms_category_status ON js_cms_category (status);
CREATE INDEX idx_cms_category_tss ON js_cms_category (tree_sorts);
CREATE INDEX idx_cms_comment_catc ON js_cms_comment (category_code);
CREATE INDEX idx_cms_comment_ai ON js_cms_comment (article_id);
CREATE INDEX idx_cms_comment_cc ON js_cms_comment (corp_code);
CREATE INDEX idx_cms_comment_status ON js_cms_comment (status);
CREATE INDEX idx_cms_guestbook_cc ON js_cms_guestbook (corp_code);
CREATE INDEX idx_cms_guestbook_status ON js_cms_guestbook (status);
CREATE INDEX idx_cms_guestbook_type ON js_cms_guestbook (gb_type);
CREATE INDEX idx_cms_site_status ON js_cms_site (status);
CREATE INDEX cms_visit_log_cc ON js_cms_visit_log (category_code);
CREATE INDEX cms_visit_log_ci ON js_cms_visit_log (content_id);
CREATE INDEX cms_visit_log_fvt ON js_cms_visit_log (first_visit_time);
CREATE INDEX cms_visit_log_inv ON js_cms_visit_log (is_new_visit);
CREATE INDEX cms_visit_log_ra ON js_cms_visit_log (remote_addr);
CREATE INDEX cms_visit_log_sc ON js_cms_visit_log (site_code);
CREATE INDEX cms_visit_log_uvid ON js_cms_visit_log (unique_visit_id);
CREATE INDEX cms_visit_log_vd ON js_cms_visit_log (visit_date);
CREATE INDEX cms_visit_log_vt ON js_cms_visit_log (visit_time);
CREATE INDEX idx_cms_visit_log_corpc ON js_cms_visit_log (corp_code);



/* Comments */

COMMENT ON TABLE js_cms_article IS '文章表';
COMMENT ON COLUMN js_cms_article.id IS '编号';
COMMENT ON COLUMN js_cms_article.category_code IS '栏目编码';
COMMENT ON COLUMN js_cms_article.module_type IS '模块类型';
COMMENT ON COLUMN js_cms_article.title IS '内容标题';
COMMENT ON COLUMN js_cms_article.href IS '外部链接';
COMMENT ON COLUMN js_cms_article.color IS '标题颜色';
COMMENT ON COLUMN js_cms_article.image IS '内容图片';
COMMENT ON COLUMN js_cms_article.keywords IS '关键字';
COMMENT ON COLUMN js_cms_article.description IS '描述';
COMMENT ON COLUMN js_cms_article.weight IS '权重，越大越靠前';
COMMENT ON COLUMN js_cms_article.weight_date IS '权重期限';
COMMENT ON COLUMN js_cms_article.article_source IS '来源（转载/原创）';
COMMENT ON COLUMN js_cms_article.copyfrom IS '文章来源出处';
COMMENT ON COLUMN js_cms_article.hits IS '点击数';
COMMENT ON COLUMN js_cms_article.hits_plus IS '支持数';
COMMENT ON COLUMN js_cms_article.hits_minus IS '反对数';
COMMENT ON COLUMN js_cms_article.word_count IS '字数（不包含html）';
COMMENT ON COLUMN js_cms_article.custom_content_view IS '自定义内容视图';
COMMENT ON COLUMN js_cms_article.view_config IS '视图配置';
COMMENT ON COLUMN js_cms_article.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_cms_article.create_by IS '创建者';
COMMENT ON COLUMN js_cms_article.create_date IS '创建时间';
COMMENT ON COLUMN js_cms_article.update_by IS '更新者';
COMMENT ON COLUMN js_cms_article.update_date IS '更新时间';
COMMENT ON COLUMN js_cms_article.remarks IS '备注信息';
COMMENT ON COLUMN js_cms_article.corp_code IS '租户代码';
COMMENT ON COLUMN js_cms_article.corp_name IS '租户名称';
COMMENT ON TABLE js_cms_article_data IS '文章详情表';
COMMENT ON COLUMN js_cms_article_data.id IS '编号';
COMMENT ON COLUMN js_cms_article_data.content IS '文章内容';
COMMENT ON COLUMN js_cms_article_data.relation IS '相关文章';
COMMENT ON COLUMN js_cms_article_data.is_can_comment IS '是否允许评论';
COMMENT ON COLUMN js_cms_article_data.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_cms_article_data.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_cms_article_data.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_cms_article_data.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_cms_article_data.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_cms_article_data.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_cms_article_data.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_cms_article_data.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_cms_article_data.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_cms_article_data.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_cms_article_data.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_cms_article_data.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_cms_article_data.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_cms_article_data.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_cms_article_data.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_cms_article_data.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_cms_article_data.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_cms_article_data.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_cms_article_data.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_cms_article_data.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN js_cms_article_data.extend_json IS '扩展 JSON';
COMMENT ON TABLE js_cms_article_posid IS '文章推荐位';
COMMENT ON COLUMN js_cms_article_posid.article_id IS '内容编号';
COMMENT ON COLUMN js_cms_article_posid.postid IS '推荐位置（1轮播图 2首页推荐 3栏目页面）';
COMMENT ON TABLE js_cms_article_tag IS '文章与标签关系';
COMMENT ON COLUMN js_cms_article_tag.article_id IS '内容编号';
COMMENT ON COLUMN js_cms_article_tag.tag_name IS '标签名称';
COMMENT ON TABLE js_cms_category IS '栏目表';
COMMENT ON COLUMN js_cms_category.category_code IS '栏目编码';
COMMENT ON COLUMN js_cms_category.parent_code IS '父级编号';
COMMENT ON COLUMN js_cms_category.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_cms_category.tree_sort IS '排序号（升序）';
COMMENT ON COLUMN js_cms_category.tree_sorts IS '所有排序号';
COMMENT ON COLUMN js_cms_category.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_cms_category.tree_level IS '层次级别';
COMMENT ON COLUMN js_cms_category.tree_names IS '全节点名';
COMMENT ON COLUMN js_cms_category.category_name IS '栏目名称';
COMMENT ON COLUMN js_cms_category.site_code IS '站点编码';
COMMENT ON COLUMN js_cms_category.module_type IS '模块类型';
COMMENT ON COLUMN js_cms_category.image IS '栏目图片';
COMMENT ON COLUMN js_cms_category.href IS '链接';
COMMENT ON COLUMN js_cms_category.target IS '目标';
COMMENT ON COLUMN js_cms_category.keywords IS '关键字';
COMMENT ON COLUMN js_cms_category.description IS '描述';
COMMENT ON COLUMN js_cms_category.in_menu IS '是否在导航中显示';
COMMENT ON COLUMN js_cms_category.in_list IS '是否在分类页中显示列表';
COMMENT ON COLUMN js_cms_category.show_modes IS '展现模式';
COMMENT ON COLUMN js_cms_category.is_need_audit IS '是否需要审核';
COMMENT ON COLUMN js_cms_category.is_can_comment IS '是否允许评论';
COMMENT ON COLUMN js_cms_category.custom_list_view IS '自定义列表视图';
COMMENT ON COLUMN js_cms_category.custom_content_view IS '自定义内容视图';
COMMENT ON COLUMN js_cms_category.view_config IS '视图配置';
COMMENT ON COLUMN js_cms_category.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_cms_category.create_by IS '创建者';
COMMENT ON COLUMN js_cms_category.create_date IS '创建时间';
COMMENT ON COLUMN js_cms_category.update_by IS '更新者';
COMMENT ON COLUMN js_cms_category.update_date IS '更新时间';
COMMENT ON COLUMN js_cms_category.remarks IS '备注信息';
COMMENT ON COLUMN js_cms_category.extend_s1 IS '扩展 String 1';
COMMENT ON COLUMN js_cms_category.extend_s2 IS '扩展 String 2';
COMMENT ON COLUMN js_cms_category.extend_s3 IS '扩展 String 3';
COMMENT ON COLUMN js_cms_category.extend_s4 IS '扩展 String 4';
COMMENT ON COLUMN js_cms_category.extend_s5 IS '扩展 String 5';
COMMENT ON COLUMN js_cms_category.extend_s6 IS '扩展 String 6';
COMMENT ON COLUMN js_cms_category.extend_s7 IS '扩展 String 7';
COMMENT ON COLUMN js_cms_category.extend_s8 IS '扩展 String 8';
COMMENT ON COLUMN js_cms_category.extend_i1 IS '扩展 Integer 1';
COMMENT ON COLUMN js_cms_category.extend_i2 IS '扩展 Integer 2';
COMMENT ON COLUMN js_cms_category.extend_i3 IS '扩展 Integer 3';
COMMENT ON COLUMN js_cms_category.extend_i4 IS '扩展 Integer 4';
COMMENT ON COLUMN js_cms_category.extend_f1 IS '扩展 Float 1';
COMMENT ON COLUMN js_cms_category.extend_f2 IS '扩展 Float 2';
COMMENT ON COLUMN js_cms_category.extend_f3 IS '扩展 Float 3';
COMMENT ON COLUMN js_cms_category.extend_f4 IS '扩展 Float 4';
COMMENT ON COLUMN js_cms_category.extend_d1 IS '扩展 Date 1';
COMMENT ON COLUMN js_cms_category.extend_d2 IS '扩展 Date 2';
COMMENT ON COLUMN js_cms_category.extend_d3 IS '扩展 Date 3';
COMMENT ON COLUMN js_cms_category.extend_d4 IS '扩展 Date 4';
COMMENT ON COLUMN js_cms_category.extend_json IS '扩展 JSON';
COMMENT ON TABLE js_cms_comment IS '文章评论表';
COMMENT ON COLUMN js_cms_comment.id IS '编号';
COMMENT ON COLUMN js_cms_comment.category_code IS '栏目编码';
COMMENT ON COLUMN js_cms_comment.article_id IS '内容编号';
COMMENT ON COLUMN js_cms_comment.parent_id IS '父级评论';
COMMENT ON COLUMN js_cms_comment.article_title IS '内容标题';
COMMENT ON COLUMN js_cms_comment.content IS '评论内容';
COMMENT ON COLUMN js_cms_comment.name IS '评论姓名';
COMMENT ON COLUMN js_cms_comment.ip IS '评论IP';
COMMENT ON COLUMN js_cms_comment.create_by IS '创建者';
COMMENT ON COLUMN js_cms_comment.create_date IS '创建时间';
COMMENT ON COLUMN js_cms_comment.audit_user_code IS '审核人';
COMMENT ON COLUMN js_cms_comment.audit_date IS '审核时间';
COMMENT ON COLUMN js_cms_comment.audit_comment IS '审核意见';
COMMENT ON COLUMN js_cms_comment.hits_plus IS '支持数';
COMMENT ON COLUMN js_cms_comment.hits_minus IS '反对数';
COMMENT ON COLUMN js_cms_comment.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_cms_comment.corp_code IS '租户代码';
COMMENT ON COLUMN js_cms_comment.corp_name IS '租户名称';
COMMENT ON TABLE js_cms_guestbook IS '留言板表';
COMMENT ON COLUMN js_cms_guestbook.id IS '编号';
COMMENT ON COLUMN js_cms_guestbook.gb_type IS '留言分类';
COMMENT ON COLUMN js_cms_guestbook.content IS '留言内容';
COMMENT ON COLUMN js_cms_guestbook.name IS '姓名';
COMMENT ON COLUMN js_cms_guestbook.email IS '邮箱';
COMMENT ON COLUMN js_cms_guestbook.phone IS '电话';
COMMENT ON COLUMN js_cms_guestbook.workunit IS '单位';
COMMENT ON COLUMN js_cms_guestbook.ip IS 'IP';
COMMENT ON COLUMN js_cms_guestbook.create_by IS '创建者';
COMMENT ON COLUMN js_cms_guestbook.create_date IS '创建时间';
COMMENT ON COLUMN js_cms_guestbook.re_user_code IS '回复人';
COMMENT ON COLUMN js_cms_guestbook.re_date IS '回复时间';
COMMENT ON COLUMN js_cms_guestbook.re_content IS '回复内容';
COMMENT ON COLUMN js_cms_guestbook.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_cms_guestbook.corp_code IS '租户代码';
COMMENT ON COLUMN js_cms_guestbook.corp_name IS '租户名称';
COMMENT ON TABLE js_cms_report IS '内容举报表';
COMMENT ON COLUMN js_cms_report.id IS '编号';
COMMENT ON COLUMN js_cms_report.report_source IS '举报来源（1文章、2评论）';
COMMENT ON COLUMN js_cms_report.report_content IS '举报内容（文章标题 评论内容）';
COMMENT ON COLUMN js_cms_report.report_url IS '举报的URL';
COMMENT ON COLUMN js_cms_report.report_type IS '举报类型（色情 政治...）';
COMMENT ON COLUMN js_cms_report.report_cause IS '举报原因';
COMMENT ON TABLE js_cms_site IS '站点表';
COMMENT ON COLUMN js_cms_site.site_code IS '站点编码';
COMMENT ON COLUMN js_cms_site.site_name IS '站点名称';
COMMENT ON COLUMN js_cms_site.site_sort IS '站点排序号';
COMMENT ON COLUMN js_cms_site.title IS '站点标题';
COMMENT ON COLUMN js_cms_site.logo IS '站点Logo';
COMMENT ON COLUMN js_cms_site.domain_name IS '站点域名';
COMMENT ON COLUMN js_cms_site.keywords IS '关键字';
COMMENT ON COLUMN js_cms_site.description IS '描述';
COMMENT ON COLUMN js_cms_site.theme IS '主题';
COMMENT ON COLUMN js_cms_site.copyright IS '版权信息';
COMMENT ON COLUMN js_cms_site.custom_index_view IS '自定义站点首页视图';
COMMENT ON COLUMN js_cms_site.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_cms_site.create_by IS '创建者';
COMMENT ON COLUMN js_cms_site.create_date IS '创建时间';
COMMENT ON COLUMN js_cms_site.update_by IS '更新者';
COMMENT ON COLUMN js_cms_site.update_date IS '更新时间';
COMMENT ON COLUMN js_cms_site.remarks IS '备注信息';
COMMENT ON TABLE js_cms_tag IS '内容标签';
COMMENT ON COLUMN js_cms_tag.tag_name IS '标签名称';
COMMENT ON COLUMN js_cms_tag.clicknum IS '点击次数';
COMMENT ON TABLE js_cms_visit_log IS '访问日志表';
COMMENT ON COLUMN js_cms_visit_log.id IS '编号';
COMMENT ON COLUMN js_cms_visit_log.request_url IS '请求的URL地址';
COMMENT ON COLUMN js_cms_visit_log.request_url_host IS '受访域名';
COMMENT ON COLUMN js_cms_visit_log.source_referer IS '来源页面/上一个页面';
COMMENT ON COLUMN js_cms_visit_log.source_referer_host IS '来源域名';
COMMENT ON COLUMN js_cms_visit_log.source_type IS '访问来源类型（1直接访问 2搜索引擎 3外部链接 4内部访问）';
COMMENT ON COLUMN js_cms_visit_log.search_engine IS '使用的搜索引擎';
COMMENT ON COLUMN js_cms_visit_log.search_word IS '搜索的关键词';
COMMENT ON COLUMN js_cms_visit_log.remote_addr IS '客户IP地址';
COMMENT ON COLUMN js_cms_visit_log.user_agent IS '用户代理字符串';
COMMENT ON COLUMN js_cms_visit_log.user_language IS '客户机语言';
COMMENT ON COLUMN js_cms_visit_log.user_screen_size IS '客户机屏幕大小0x0';
COMMENT ON COLUMN js_cms_visit_log.user_device IS '客户机设备类型（电脑、平板、手机、未知）';
COMMENT ON COLUMN js_cms_visit_log.user_os_name IS '客户机操作系统';
COMMENT ON COLUMN js_cms_visit_log.user_browser IS '客户机浏览器';
COMMENT ON COLUMN js_cms_visit_log.user_browser_version IS '浏览器版本';
COMMENT ON COLUMN js_cms_visit_log.unique_visit_id IS '唯一访问标识';
COMMENT ON COLUMN js_cms_visit_log.visit_date IS '本次访问日期（年月日）';
COMMENT ON COLUMN js_cms_visit_log.visit_time IS '本次访问时间';
COMMENT ON COLUMN js_cms_visit_log.is_new_visit IS '是否新访问（30分内）';
COMMENT ON COLUMN js_cms_visit_log.first_visit_time IS '首次访问时间戳（30分钟内）';
COMMENT ON COLUMN js_cms_visit_log.prev_remain_time IS '上页面停留时间（秒）';
COMMENT ON COLUMN js_cms_visit_log.total_remain_time IS '本次访问总停留时间（秒）';
COMMENT ON COLUMN js_cms_visit_log.site_code IS '站点编码';
COMMENT ON COLUMN js_cms_visit_log.site_name IS '站点名称';
COMMENT ON COLUMN js_cms_visit_log.category_code IS '栏目编码';
COMMENT ON COLUMN js_cms_visit_log.category_name IS '栏目名称';
COMMENT ON COLUMN js_cms_visit_log.content_id IS '栏目内容编号';
COMMENT ON COLUMN js_cms_visit_log.content_title IS '访问页面标题';
COMMENT ON COLUMN js_cms_visit_log.visit_user_code IS '访问用户编码';
COMMENT ON COLUMN js_cms_visit_log.visit_user_name IS '访问用户姓名';
COMMENT ON COLUMN js_cms_visit_log.corp_code IS '租户代码';
COMMENT ON COLUMN js_cms_visit_log.corp_name IS '租户名称';



