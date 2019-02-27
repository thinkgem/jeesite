
-- 删除sys_file_entity表的file_md5唯一索引
ALTER TABLE `js_sys_file_entity` 
  DROP INDEX `file_md5`,
  ADD INDEX `file_md5`(`file_md5`);