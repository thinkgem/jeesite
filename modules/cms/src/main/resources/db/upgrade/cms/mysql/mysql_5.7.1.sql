
ALTER TABLE ${_prefix}cms_article CHANGE source article_source CHAR(1);

ALTER TABLE ${_prefix}cms_guestbook CHANGE type gb_type CHAR(1);

ALTER TABLE ${_prefix}cms_site CHANGE domain domain_name VARCHAR(500);
