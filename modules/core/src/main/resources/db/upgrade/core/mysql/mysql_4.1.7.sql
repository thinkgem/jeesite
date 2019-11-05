
-- 文件表增加排序字段
ALTER TABLE `${_prefix}sys_file_upload` 
ADD COLUMN `file_sort` decimal(10) NULL COMMENT '文件排序' AFTER `file_type`;
