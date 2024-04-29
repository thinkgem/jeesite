
exec sp_rename '${_prefix}cms_article.source', 'article_source', 'COLUMN';

exec sp_rename '${_prefix}cms_guestbook.type', 'gb_type', 'COLUMN';

exec sp_rename '${_prefix}cms_site.domain', 'domain_name', 'COLUMN';
