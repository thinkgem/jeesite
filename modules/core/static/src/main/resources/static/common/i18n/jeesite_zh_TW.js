/*!
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * 
 * 国际化文件
 * @author ThinkGem
 * @version 2017-12-24
 */
(function($) {
	window.js = window.js || {};
	js.i18n = $.extend({}, js.i18n, {
		'tabpanel.newTabPage': '新增頁籤',
		'loading.message': '載入中……',
		'loading.submitMessage': '提交中……',
		'showMessage.error': '失敗，發生錯誤',
		'showMessage.warning': '否、不存在、已存在',
		'showMessage.success': '成功、已完成'
	});
	// ============ layer ============
	window.layer = window.layer || {};
	layer.i18n = $.extend({}, layer.i18n, {
		btnOk: '確定',
		btnCancel: '取消',
		title: '提示訊息',
		promptTipA: '請輸入 ',
		promptTipB: ' 個字元以內。',
		noPicture: '無圖片',
		photoError: '目前圖片地址錯誤。<br>是否跳至下一張？',
		photoNextPage: '下一張',
		photoClose: '關閉'
	});
})(jQuery);