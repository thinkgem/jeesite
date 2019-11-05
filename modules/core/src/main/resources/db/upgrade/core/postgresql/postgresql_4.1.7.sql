
-- 文件表增加排序字段
ALTER TABLE ${_prefix}sys_file_upload ADD COLUMN file_sort decimal(10);
COMMENT ON COLUMN ${_prefix}sys_file_upload.file_sort IS '文件排序';
