
-- 将config表的key设置为唯一索引
DROP INDEX [idx_sys_config_key] ON [${_prefix}sys_config];
CREATE UNIQUE INDEX [idx_sys_config_key] ON [${_prefix}sys_config] ([config_key]);

-- 新增主导航菜单显示风格配置
INSERT INTO ${_prefix}sys_config(id, config_name, config_key, config_value, is_sys, create_by, create_date, update_by, update_date, remarks)
VALUES ('1092344803460943872', '主框架页-主导航菜单显示风格', 'sys.index.menuStyle', '1', '0', 'system', getdate(), 'system', getdate(), '1：菜单全部在左侧；2：根菜单显示在顶部');
