
ALTER TABLE ${_prefix}cms_site ADD site_sort NUMBER(10) NULL;
COMMENT ON COLUMN ${_prefix}cms_site.site_sort IS '站点排序';
