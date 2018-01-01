



-- 更新模块数据库版本
update ${_prefix}sys_module set current_version = '4.0.xx' where module_code = 'core';
commit;