
ALTER TABLE ${_prefix}cms_site ADD COLUMN site_sort decimal(10);
COMMENT ON COLUMN ${_prefix}cms_site.site_sort IS '站点排序';
