
-- 删除sys_file_entity表的file_md5唯一索引
ALTER TABLE `${_prefix}sys_file_entity` 
DROP INDEX `file_md5`,
ADD INDEX `file_md5`(`file_md5`);

-- 增加文件信息字段（可存储图片大小）
ALTER TABLE `${_prefix}js_sys_file_entity` 
ADD COLUMN `file_meta` varchar(255) NULL COMMENT '文件信息(JSON格式)' AFTER `file_size`;

-- 集群的实例名字  
ALTER TABLE `${_prefix}sys_job` 
ADD COLUMN `instance_name` varchar(64) NOT NULL DEFAULT JeeSiteScheduler COMMENT '集群的实例名字' AFTER `concurrent`;