
ALTER TABLE ${_prefix}cms_article RENAME COLUMN source TO article_source;

ALTER TABLE ${_prefix}cms_guestbook RENAME COLUMN type TO gb_type;

ALTER TABLE ${_prefix}cms_site RENAME COLUMN domain TO domain_name;
