(function($) {
	
	window.TabPanel = window.TabPanel || {};
	if (window.lang == 'en'){
		TabPanel.i18n = $.extend({}, TabPanel.i18n, {
			closeTab: '<i class="fa fa-close"></i> Close',
			refreshTab: '<i class="fa fa-refresh"></i> Refresh',
			closeOther: '<i class="fa fa-expand"></i> Close Other',
			closeLeft: '<i class="fa fa-angle-double-left"></i> Close Left',
			closeRight: '<i class="fa fa-angle-double-right"></i> Close Right',
			closeAll: '<i class="fa fa-minus"></i> Close All',
			detachTab: '<i class="fa fa-share"></i> Detach Tab'
		});
	} else if (window.lang == 'ja_JP'){
		TabPanel.i18n = $.extend({}, TabPanel.i18n, {
			closeTab: '<i class="fa fa-close"></i> 閉じる',
			refreshTab: '<i class="fa fa-refresh"></i> 更新',
			closeOther: '<i class="fa fa-expand"></i> 他を閉じる',
			closeLeft: '<i class="fa fa-angle-double-left"></i> 左側を閉じる',
			closeRight: '<i class="fa fa-angle-double-right"></i> 右側を閉じる',
			closeAll: '<i class="fa fa-minus"></i> 閉じる全体',
			detachTab: '<i class="fa fa-share"></i> 新しい窓が'
		});
	}
	
})(jQuery);
