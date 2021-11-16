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
		'tabpanel.newTabPage':'新タブ',
		'loading.message':'ローディング...お待ちください...',
		'loading.submitMessage':'送信中...お待ちください...',
		'showMessage.error':'失敗,エラー,未完了',
		'showMessage.warning':'不可,不許可,必須,既に存在,不要,不正確',
		'showMessage.success':'成功,完了'
	});
	// ============ layer ============
	window.layer = window.layer || {};
	layer.i18n = $.extend({}, layer.i18n, {
		btnOk: '確定',
		btnCancel: 'キャンセル',
		title: 'メッセージ',
		promptTipA: '最大入力',
		promptTipB: '個文字',
		noPicture: '画像が存在しません',
		photoError: 'この画像が表示できません<br>次の画像へ遷移しますか？',
		photoNextPage: '次へ',
		photoClose: '閉じる'
	});
})(jQuery);
