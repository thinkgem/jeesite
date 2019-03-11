
-- 删除sys_file_entity表的file_md5唯一索引
-- ALTER TABLE ${_prefix}SYS_FILE_ENTITY DROP CONSTRAINT SYS_C0019398

-- 增加文件信息字段（可存储图片大小）
ALTER TABLE ${_prefix}SYS_FILE_ENTITY 
ADD (file_meta VARCHAR2(64) NULL );
COMMENT ON COLUMN ${_prefix}SYS_FILE_ENTITY.file_meta IS '文件信息(JSON格式)'

-- 集群的实例名字  
ALTER TABLE ${_prefix}SYS_JOB 
ADD (instance_name VARCHAR2(64) DEFAULT JeeSiteScheduler NOT NULL );
COMMENT ON COLUMN ${_prefix}SYS_JOB.instance_name IS '集群的实例名字'