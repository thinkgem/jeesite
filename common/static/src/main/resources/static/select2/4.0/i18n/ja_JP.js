(function() {
	if (jQuery && jQuery.fn && jQuery.fn.select2 && jQuery.fn.select2.amd)
		var e = jQuery.fn.select2.amd;
	return e.define("select2/i18n/zh_CN", [], function() {
		return { errorLoading : function() {
			return "結果に載せることはできない。"
		}, inputTooLong : function(e) {
			var t = e.input.length - e.maximum, n = "" + t + "文字を削除してください";
			return n
		}, inputTooShort : function(e) {
			var t = e.minimum - e.input.length, n = "少なくとも" + t + "文字を入力してください";
			return n
		}, loadingMore : function() {
			return "より多くの……"
		}, maximumSelected : function(e) {
			var t = "選択するしかない" + e.maximum + "項目別";
			return t
		}, noResults : function() {
			return "一致する項目を見つけることができなかった"
		}, searching : function() {
			return "検索しています……"
		} }
	}), { define : e.define, require : e.require }
})();