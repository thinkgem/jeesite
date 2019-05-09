/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
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
		'showMessage.success':'success,completion',
		'showMessage.warning':'no'
	});
	// ============ layer ============
	window.layer = window.layer || {};
	layer.i18n = $.extend({}, layer.i18n, {
		btnOk: 'Ok',
		btnCancel: 'Cancle',
		title: 'Information',
		promptTipA: 'Enter ',
		promptTipB: 'character at most.',
		noPicture: 'No picture',
		photoError: 'Current image address error.<br>Next slide?',
		photoNextPage: 'The next',
		photoClose: 'Close'
	});
})(jQuery);
