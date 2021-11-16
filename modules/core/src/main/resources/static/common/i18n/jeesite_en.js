/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * 
 * 国际化文件
 * @author ThinkGem
 * @version 2017-12-24
 */
(function($) {
	window.js = window.js || {};
	js.i18n = $.extend({}, js.i18n, {
		'tabpanel.newTabPage':'New Page',
		'loading.message':'Loading ...',
		'loading.submitMessage':'Submission ...',
		'showMessage.error':'failure,error',
		'showMessage.warning':'no,not,Not,already exists',
		'showMessage.success':'success,completion'
	});
	// ============ layer ============
	window.layer = window.layer || {};
	layer.i18n = $.extend({}, layer.i18n, {
		btnOk: 'Ok',
		btnCancel: 'Cancel',
		title: 'Information',
		promptTipA: 'Enter ',
		promptTipB: 'character at most.',
		noPicture: 'No picture',
		photoError: 'Current image address error.<br>Next slide?',
		photoNextPage: 'The next',
		photoClose: 'Close'
	});
})(jQuery);
