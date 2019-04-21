SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS js_filemanager_shared;
DROP TABLE IF EXISTS js_filemanager_folder;




/* Create Tables */

-- 文件管理文件夹
CREATE TABLE js_filemanager_folder
(
	id varchar(64) NOT NULL COMMENT '编号',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	folder_name varchar(100) NOT NULL COMMENT '文件夹名',
	group_type varchar(64) NOT NULL COMMENT '文件分组类型',
	office_code varchar(64) COMMENT '部门编码',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '文件管理文件夹';


-- 文件管理共享表
CREATE TABLE js_filemanager_shared
(
	id varchar(64) NOT NULL COMMENT '编号',
	folder_id varchar(64) COMMENT '文件夹编码',
	file_upload_id varchar(64) COMMENT '文件上传编码',
	file_name varchar(500) NOT NULL COMMENT '文件或文件夹名',
	receive_user_code varchar(100) NOT NULL COMMENT '接受者用户编码',
	receive_user_name varchar(200) NOT NULL COMMENT '接收者用户名称',
	click_num numeric(10) COMMENT '点击次数',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '文件管理共享表';



/* Create Indexes */

CREATE INDEX idx_sys_file_tree_pc ON js_filemanager_folder (parent_code ASC);
CREATE INDEX idx_sys_file_tree_ts ON js_filemanager_folder (tree_sort ASC);
CREATE INDEX idx_sys_file_tree_tss ON js_filemanager_folder (tree_sorts ASC);
CREATE INDEX idx_sys_file_tree_gt ON js_filemanager_folder (group_type ASC);
CREATE INDEX idx_sys_file_tree_oc ON js_filemanager_folder (office_code ASC);
CREATE INDEX idx_sys_file_tree_cb ON js_filemanager_folder (create_by ASC);
CREATE INDEX idx_sys_file_share_ruc ON js_filemanager_shared (receive_user_code ASC);
CREATE INDEX idx_sys_file_share_cb ON js_filemanager_shared (create_by ASC);



