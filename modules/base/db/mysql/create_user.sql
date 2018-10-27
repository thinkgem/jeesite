
-- 打开 my.ini 给 [mysqld] 增加如下配置：
-- sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION

set global read_only=0;

create user 'jeesite'@'%' identified by 'jeesite';

create database jeesite DEFAULT CHARSET utf8 COLLATE utf8_general_ci;  

grant all privileges on jeesite.* to 'jeesite'@'%' identified by 'jeesite';

flush privileges;
