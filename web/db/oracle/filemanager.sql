
/* Drop Tables */

DROP TABLE js_filemanager_shared CASCADE CONSTRAINTS;
DROP TABLE js_filemanager_folder CASCADE CONSTRAINTS;




/* Create Tables */

-- 文件管理文件夹
CREATE TABLE js_filemanager_folder
(
	id varchar2(64) NOT NULL,
	parent_code varchar2(64) NOT NULL,
	parent_codes varchar2(1000) NOT NULL,
	tree_sort number(10) NOT NULL,
	tree_sorts varchar2(1000) NOT NULL,
	tree_leaf char(1) NOT NULL,
	tree_level number(4) NOT NULL,
	tree_names varchar2(1000) NOT NULL,
	folder_name nvarchar2(100) NOT NULL,
	group_type varchar2(64) NOT NULL,
	office_code varchar2(64),
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


-- 文件管理共享表
CREATE TABLE js_filemanager_shared
(
	id varchar2(64) NOT NULL,
	folder_id varchar2(64),
	file_upload_id varchar2(64),
	file_name nvarchar2(500) NOT NULL,
	receive_user_code varchar2(100) NOT NULL,
	receive_user_name nvarchar2(200) NOT NULL,
	click_num number(10),
	status char(1) DEFAULT '0' NOT NULL,
	create_by varchar2(64) NOT NULL,
	create_date timestamp NOT NULL,
	update_by varchar2(64) NOT NULL,
	update_date timestamp NOT NULL,
	remarks nvarchar2(500),
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



/* Comments */

COMMENT ON TABLE js_filemanager_folder IS '文件管理文件夹';
COMMENT ON COLUMN js_filemanager_folder.id IS '编号';
COMMENT ON COLUMN js_filemanager_folder.parent_code IS '父级编号';
COMMENT ON COLUMN js_filemanager_folder.parent_codes IS '所有父级编号';
COMMENT ON COLUMN js_filemanager_folder.tree_sort IS '本级排序号（升序）';
COMMENT ON COLUMN js_filemanager_folder.tree_sorts IS '所有级别排序号';
COMMENT ON COLUMN js_filemanager_folder.tree_leaf IS '是否最末级';
COMMENT ON COLUMN js_filemanager_folder.tree_level IS '层次级别';
COMMENT ON COLUMN js_filemanager_folder.tree_names IS '全节点名';
COMMENT ON COLUMN js_filemanager_folder.folder_name IS '文件夹名';
COMMENT ON COLUMN js_filemanager_folder.group_type IS '文件分组类型';
COMMENT ON COLUMN js_filemanager_folder.office_code IS '部门编码';
COMMENT ON COLUMN js_filemanager_folder.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_filemanager_folder.create_by IS '创建者';
COMMENT ON COLUMN js_filemanager_folder.create_date IS '创建时间';
COMMENT ON COLUMN js_filemanager_folder.update_by IS '更新者';
COMMENT ON COLUMN js_filemanager_folder.update_date IS '更新时间';
COMMENT ON COLUMN js_filemanager_folder.remarks IS '备注信息';
COMMENT ON COLUMN js_filemanager_folder.corp_code IS '租户代码';
COMMENT ON COLUMN js_filemanager_folder.corp_name IS '租户名称';
COMMENT ON TABLE js_filemanager_shared IS '文件管理共享表';
COMMENT ON COLUMN js_filemanager_shared.id IS '编号';
COMMENT ON COLUMN js_filemanager_shared.folder_id IS '文件夹编码';
COMMENT ON COLUMN js_filemanager_shared.file_upload_id IS '文件上传编码';
COMMENT ON COLUMN js_filemanager_shared.file_name IS '文件或文件夹名';
COMMENT ON COLUMN js_filemanager_shared.receive_user_code IS '接受者用户编码';
COMMENT ON COLUMN js_filemanager_shared.receive_user_name IS '接收者用户名称';
COMMENT ON COLUMN js_filemanager_shared.click_num IS '点击次数';
COMMENT ON COLUMN js_filemanager_shared.status IS '状态（0正常 1删除 2停用）';
COMMENT ON COLUMN js_filemanager_shared.create_by IS '创建者';
COMMENT ON COLUMN js_filemanager_shared.create_date IS '创建时间';
COMMENT ON COLUMN js_filemanager_shared.update_by IS '更新者';
COMMENT ON COLUMN js_filemanager_shared.update_date IS '更新时间';
COMMENT ON COLUMN js_filemanager_shared.remarks IS '备注信息';



