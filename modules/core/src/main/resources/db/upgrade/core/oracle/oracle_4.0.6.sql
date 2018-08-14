
UPDATE ${_prefix}sys_menu SET permission='sys:stste:cache' WHERE permission='sys:stste:ehcache';
UPDATE ${_prefix}sys_menu SET menu_href='/state/cache/index' WHERE menu_href='/state/ehcache';
UPDATE ${_prefix}sys_menu SET menu_href='/state/server/index' WHERE menu_href='/state/server';

commit;