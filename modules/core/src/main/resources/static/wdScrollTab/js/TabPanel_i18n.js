(function($) {
	
	window.TabPanel = window.TabPanel || {};
	if (window.lang == 'en'){
		TabPanel.i18n = $.extend({}, TabPanel.i18n, {
			closeTab: 'Close',
			refreshTab: 'Refresh',
			closeOther: 'Close Other',
			closeLeft: 'Close Left',
			closeRight: 'Close Right',
			detachTab: 'Detach Tab'
		});
	} else if (window.lang == 'ja_JP'){
		TabPanel.i18n = $.extend({}, TabPanel.i18n, {
			closeTab: '閉じる',
			refreshTab: '更新',
			closeOther: '他を閉じる',
			closeLeft: '左側を閉じる',
			closeRight: '右側を閉じる',
			detachTab: '新しい窓が'
		});
	}
	
})(jQuery);
