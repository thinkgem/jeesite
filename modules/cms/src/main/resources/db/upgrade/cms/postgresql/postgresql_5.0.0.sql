
ALTER TABLE ${_prefix}cms_category ADD COLUMN extend_json text;
COMMENT ON COLUMN ${_prefix}cms_category.extend_json IS '扩展 JSON';

ALTER TABLE ${_prefix}cms_article_data ADD COLUMN extend_json text;
COMMENT ON COLUMN ${_prefix}cms_article_data.extend_json IS '扩展 JSON';
