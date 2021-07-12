
-- 字典表新增字典图标
ALTER TABLE ${_prefix}sys_dict_data 
ADD COLUMN dict_icon nvarchar2(100);
COMMENT ON COLUMN ${_prefix}sys_dict_data.dict_icon IS '字典图标';
