
/* Drop Tables */

DROP TABLE js_filemanager_shared;
DROP TABLE js_filemanager_folder;




/* Create Tables */

-- 文件管理文件夹
CREATE TABLE js_filemanager_folder
(
	id varchar(64) NOT NULL,
	parent_code varchar(64) NOT NULL,
	parent_codes varchar(1000) NOT NULL,
	tree_sort decimal(10) NOT NULL,
	tree_sorts varchar(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level decimal(4) NOT NULL,
	tree_names varchar(1000) NOT NULL,
	folder_name vargraphic(100) NOT NULL,
	group_type varchar(64) NOT NULL,
	office_code varchar(64),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	corp_code varchar(64) DEFAULT '0' NOT NULL,
	corp_name vargraphic(100) DEFAULT 'JeeSite' NOT NULL,
	PRIMARY KEY (id)
);


-- 文件管理共享表
CREATE TABLE js_filemanager_shared
(
	id varchar(64) NOT NULL,
	folder_id varchar(64),
	file_upload_id varchar(64),
	file_name vargraphic(500) NOT NULL,
	receive_user_code varchar(100) NOT NULL,
	receive_user_name vargraphic(200) NOT NULL,
	click_num numeric(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks vargraphic(500),
	PRIMARY KEY (id)
);



/* Create Indexes */

CREATE INDEX idx_sys_file_tree_pc ON js_filemanager_folder (parent_code);
CREATE INDEX idx_sys_file_tree_ts ON js_filemanager_folder (tree_sort);
CREATE INDEX idx_sys_file_tree_tss ON js_filemanager_folder (tree_sorts);
CREATE INDEX idx_sys_file_tree_gt ON js_filemanager_folder (group_type);
CREATE INDEX idx_sys_file_tree_oc ON js_filemanager_folder (office_code);
CREATE INDEX idx_sys_file_tree_cb ON js_filemanager_folder (create_by);
CREATE INDEX idx_sys_file_share_ruc ON js_filemanager_shared (receive_user_code);
CREATE INDEX idx_sys_file_share_cb ON js_filemanager_shared (create_by);



