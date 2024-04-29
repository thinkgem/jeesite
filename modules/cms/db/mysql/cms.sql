SET SESSION FOREIGN_KEY_CHECKS=0;


/* Create Tables */

-- 文章表
CREATE TABLE js_cms_article
(
	id varchar(64) NOT NULL COMMENT '编号',
	category_code varchar(64) NOT NULL COMMENT '栏目编码',
	module_type varchar(50) COMMENT '模块类型',
	title varchar(255) NOT NULL COMMENT '内容标题',
	href varchar(1000) COMMENT '外部链接',
	color varchar(50) COMMENT '标题颜色',
	image varchar(1000) COMMENT '内容图片',
	keywords varchar(500) COMMENT '关键字',
	description varchar(500) COMMENT '描述',
	weight decimal(10) DEFAULT 0 COMMENT '权重，越大越靠前',
	weight_date datetime COMMENT '权重期限',
	article_source char(1) COMMENT '来源（转载/原创）',
	copyfrom varchar(255) COMMENT '文章来源出处',
	hits decimal(20) DEFAULT 0 COMMENT '点击数',
	hits_plus numeric(10) COMMENT '支持数',
	hits_minus numeric(10) COMMENT '反对数',
	word_count numeric(10) COMMENT '字数（不包含html）',
	custom_content_view varchar(255) COMMENT '自定义内容视图',
	view_config varchar(1000) COMMENT '视图配置',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '文章表';


-- 文章详情表
CREATE TABLE js_cms_article_data
(
	id varchar(64) NOT NULL COMMENT '编号',
	content longtext COMMENT '文章内容',
	relation varchar(1000) COMMENT '相关文章',
	is_can_comment char(1) COMMENT '是否允许评论',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	extend_json varchar(1000) COMMENT '扩展 JSON',
	PRIMARY KEY (id)
) COMMENT = '文章详情表';


-- 文章推荐位
CREATE TABLE js_cms_article_posid
(
	article_id varchar(64) NOT NULL COMMENT '内容编号',
	postid char(1) NOT NULL COMMENT '推荐位置（1轮播图 2首页推荐 3栏目页面）'
) COMMENT = '文章推荐位';


-- 文章与标签关系
CREATE TABLE js_cms_article_tag
(
	article_id varchar(64) NOT NULL COMMENT '内容编号',
	tag_name varchar(200) NOT NULL COMMENT '标签名称'
) COMMENT = '文章与标签关系';


-- 栏目表
CREATE TABLE js_cms_category
(
	category_code varchar(64) NOT NULL COMMENT '栏目编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(767) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '排序号（升序）',
	tree_sorts varchar(767) NOT NULL COMMENT '所有排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(767) NOT NULL COMMENT '全节点名',
	category_name varchar(100) NOT NULL COMMENT '栏目名称',
	site_code varchar(64) NOT NULL COMMENT '站点编码',
	module_type varchar(50) COMMENT '模块类型',
	image varchar(255) COMMENT '栏目图片',
	href varchar(255) COMMENT '链接',
	target varchar(20) COMMENT '目标',
	keywords varchar(500) COMMENT '关键字',
	description varchar(500) COMMENT '描述',
	in_menu char(1) COMMENT '是否在导航中显示',
	in_list char(1) COMMENT '是否在分类页中显示列表',
	show_modes char(1) COMMENT '展现模式',
	is_need_audit char(1) COMMENT '是否需要审核',
	is_can_comment char(1) COMMENT '是否允许评论',
	custom_list_view varchar(255) COMMENT '自定义列表视图',
	custom_content_view varchar(255) COMMENT '自定义内容视图',
	view_config varchar(1000) COMMENT '视图配置',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	extend_json varchar(1000) COMMENT '扩展 JSON',
	PRIMARY KEY (category_code)
) COMMENT = '栏目表';


-- 文章评论表
CREATE TABLE js_cms_comment
(
	id varchar(64) NOT NULL COMMENT '编号',
	category_code varchar(64) NOT NULL COMMENT '栏目编码',
	article_id varchar(64) NOT NULL COMMENT '内容编号',
	parent_id varchar(64) COMMENT '父级评论',
	article_title varchar(255) NOT NULL COMMENT '内容标题',
	content varchar(255) NOT NULL COMMENT '评论内容',
	name varchar(50) COMMENT '评论姓名',
	ip varchar(100) COMMENT '评论IP',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	audit_user_code varchar(64) COMMENT '审核人',
	audit_date datetime COMMENT '审核时间',
	audit_comment varchar(200) COMMENT '审核意见',
	hits_plus numeric(10) COMMENT '支持数',
	hits_minus numeric(10) COMMENT '反对数',
	status char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '文章评论表';


-- 留言板表
CREATE TABLE js_cms_guestbook
(
	id varchar(64) NOT NULL COMMENT '编号',
	gb_type char(1) NOT NULL COMMENT '留言分类',
	content varchar(255) NOT NULL COMMENT '留言内容',
	name varchar(100) NOT NULL COMMENT '姓名',
	email varchar(100) NOT NULL COMMENT '邮箱',
	phone varchar(100) NOT NULL COMMENT '电话',
	workunit varchar(100) NOT NULL COMMENT '单位',
	ip varchar(100) NOT NULL COMMENT 'IP',
	create_by varchar(64) COMMENT '创建者',
	create_date datetime COMMENT '创建时间',
	re_user_code varchar(64) COMMENT '回复人',
	re_date datetime COMMENT '回复时间',
	re_content varchar(100) COMMENT '回复内容',
	status char(1) NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '留言板表';


-- 内容举报表
CREATE TABLE js_cms_report
(
	id varchar(64) NOT NULL COMMENT '编号',
	report_source char(1) COMMENT '举报来源（1文章、2评论）',
	report_content varchar(500) COMMENT '举报内容（文章标题 评论内容）',
	report_url varchar(1000) COMMENT '举报的URL',
	report_type char(1) COMMENT '举报类型（色情 政治...）',
	report_cause varchar(500) COMMENT '举报原因',
	PRIMARY KEY (id)
) COMMENT = '内容举报表';


-- 站点表
CREATE TABLE js_cms_site
(
	site_code varchar(64) NOT NULL COMMENT '站点编码',
	site_name varchar(100) NOT NULL COMMENT '站点名称',
	site_sort decimal(10) COMMENT '站点排序号',
	title varchar(100) NOT NULL COMMENT '站点标题',
	logo varchar(1000) COMMENT '站点Logo',
	domain_name varchar(500) COMMENT '站点域名',
	keywords varchar(500) COMMENT '关键字',
	description varchar(500) COMMENT '描述',
	theme varchar(500) COMMENT '主题',
	copyright varchar(1000) COMMENT '版权信息',
	custom_index_view varchar(500) COMMENT '自定义站点首页视图',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (site_code)
) COMMENT = '站点表';


-- 内容标签
CREATE TABLE js_cms_tag
(
	tag_name varchar(200) NOT NULL COMMENT '标签名称',
	clicknum numeric(10) NOT NULL COMMENT '点击次数',
	PRIMARY KEY (tag_name)
) COMMENT = '内容标签';


-- 访问日志表
CREATE TABLE js_cms_visit_log
(
	id varchar(64) NOT NULL COMMENT '编号',
	request_url varchar(1000) COMMENT '请求的URL地址',
	request_url_host varchar(128) COMMENT '受访域名',
	source_referer varchar(1000) COMMENT '来源页面/上一个页面',
	source_referer_host varchar(128) COMMENT '来源域名',
	source_type char(1) COMMENT '访问来源类型（1直接访问 2搜索引擎 3外部链接 4内部访问）',
	search_engine varchar(200) COMMENT '使用的搜索引擎',
	search_word varchar(200) COMMENT '搜索的关键词',
	remote_addr varchar(50) COMMENT '客户IP地址',
	user_agent varchar(500) COMMENT '用户代理字符串',
	user_language varchar(32) COMMENT '客户机语言',
	user_screen_size varchar(32) COMMENT '客户机屏幕大小0x0',
	user_device varchar(32) COMMENT '客户机设备类型（电脑、平板、手机、未知）',
	user_os_name varchar(32) COMMENT '客户机操作系统',
	user_browser varchar(32) COMMENT '客户机浏览器',
	user_browser_version varchar(16) COMMENT '浏览器版本',
	unique_visit_id varchar(64) COMMENT '唯一访问标识',
	visit_date char(8) COMMENT '本次访问日期（年月日）',
	visit_time datetime COMMENT '本次访问时间',
	is_new_visit char(1) COMMENT '是否新访问（30分内）',
	first_visit_time decimal(20) COMMENT '首次访问时间戳（30分钟内）',
	prev_remain_time decimal(20) COMMENT '上页面停留时间（秒）',
	total_remain_time decimal(20) COMMENT '本次访问总停留时间（秒）',
	site_code varchar(64) COMMENT '站点编码',
	site_name varchar(100) COMMENT '站点名称',
	category_code varchar(64) COMMENT '栏目编码',
	category_name varchar(100) COMMENT '栏目名称',
	content_id varchar(64) COMMENT '栏目内容编号',
	content_title varchar(255) COMMENT '访问页面标题',
	visit_user_code varchar(100) COMMENT '访问用户编码',
	visit_user_name varchar(100) COMMENT '访问用户姓名',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '访问日志表';



/* Create Indexes */

CREATE INDEX idx_cms_article_cb ON js_cms_article (create_by ASC);
CREATE INDEX idx_cms_article_cc ON js_cms_article (category_code ASC);
CREATE INDEX idx_cms_article_corp_code ON js_cms_article (corp_code ASC);
CREATE INDEX idx_cms_article_status ON js_cms_article (status ASC);
CREATE INDEX idx_cms_article_ud ON js_cms_article (update_date ASC);
CREATE INDEX idx_cms_article_weight ON js_cms_article (weight ASC);
CREATE INDEX idx_cms_category_pc ON js_cms_category (parent_code ASC);
CREATE INDEX idx_cms_category_ts ON js_cms_category (tree_sort ASC);
CREATE INDEX idx_cms_category_status ON js_cms_category (status ASC);
CREATE INDEX idx_cms_category_tss ON js_cms_category (tree_sorts ASC);
CREATE INDEX idx_cms_comment_catc ON js_cms_comment (category_code ASC);
CREATE INDEX idx_cms_comment_ai ON js_cms_comment (article_id ASC);
CREATE INDEX idx_cms_comment_cc ON js_cms_comment (corp_code ASC);
CREATE INDEX idx_cms_comment_status ON js_cms_comment (status ASC);
CREATE INDEX idx_cms_guestbook_cc ON js_cms_guestbook (corp_code ASC);
CREATE INDEX idx_cms_guestbook_status ON js_cms_guestbook (status ASC);
CREATE INDEX idx_cms_guestbook_type ON js_cms_guestbook (gb_type ASC);
CREATE INDEX idx_cms_site_status ON js_cms_site (status ASC);
CREATE INDEX cms_visit_log_cc ON js_cms_visit_log (category_code ASC);
CREATE INDEX cms_visit_log_ci ON js_cms_visit_log (content_id ASC);
CREATE INDEX cms_visit_log_fvt ON js_cms_visit_log (first_visit_time ASC);
CREATE INDEX cms_visit_log_inv ON js_cms_visit_log (is_new_visit ASC);
CREATE INDEX cms_visit_log_ra ON js_cms_visit_log (remote_addr ASC);
CREATE INDEX cms_visit_log_sc ON js_cms_visit_log (site_code ASC);
CREATE INDEX cms_visit_log_uvid ON js_cms_visit_log (unique_visit_id ASC);
CREATE INDEX cms_visit_log_vd ON js_cms_visit_log (visit_date ASC);
CREATE INDEX cms_visit_log_vt ON js_cms_visit_log (visit_time ASC);
CREATE INDEX idx_cms_visit_log_corpc ON js_cms_visit_log (corp_code ASC);



