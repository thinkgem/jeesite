
-- 字典表新增字典图标
ALTER TABLE `${_prefix}sys_dict_data` 
ADD COLUMN dict_icon varchar(100) COMMENT '字典图标';
