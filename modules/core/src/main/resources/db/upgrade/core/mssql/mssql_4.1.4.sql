
-- 删除sys_file_entity表的file_md5唯一索引
-- ALTER TABLE [${_prefix}sys_file_entity] DROP CONSTRAINT [UQ__js_sys_f__3BD63C9E32E0915F];

-- 增加文件信息字段（可存储图片大小）
ALTER TABLE [${_prefix}js_sys_file_entity] ADD [file_meta] varchar(64) NULL;

-- 集群的实例名字  
ALTER TABLE [${_prefix}sys_job] ADD [instance_name] varchar(64) DEFAULT JeeSiteScheduler NOT NULL;
