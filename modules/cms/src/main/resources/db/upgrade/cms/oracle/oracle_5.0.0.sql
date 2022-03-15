
ALTER TABLE ${_prefix}cms_category ADD extend_json varchar2(1000);
COMMENT ON COLUMN ${_prefix}cms_category.extend_json IS '扩展 JSON';

ALTER TABLE ${_prefix}cms_article_data ADD extend_json varchar2(1000);
COMMENT ON COLUMN ${_prefix}cms_article_data.extend_json IS '扩展 JSON';
