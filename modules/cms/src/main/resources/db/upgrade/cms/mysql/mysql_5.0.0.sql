
ALTER TABLE `${_prefix}cms_category` 
ADD COLUMN extend_json text COMMENT '扩展 JSON';

ALTER TABLE `${_prefix}cms_article_data` 
ADD COLUMN extend_json text COMMENT '扩展 JSON';