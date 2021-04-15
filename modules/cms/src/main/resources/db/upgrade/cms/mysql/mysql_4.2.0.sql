
ALTER TABLE ${_prefix}cms_site
ADD COLUMN `site_sort` decimal(10,0) NULL COMMENT '站点排序' AFTER `site_name`;
