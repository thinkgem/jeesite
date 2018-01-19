(function() {
	if (jQuery && jQuery.fn && jQuery.fn.select2 && jQuery.fn.select2.amd)
		var e = jQuery.fn.select2.amd;
	return e.define("select2/i18n/zh_CN", [], function() {
		return { errorLoading : function() {
			return "无法载入结果。"
		}, inputTooLong : function(e) {
			var t = e.input.length - e.maximum, n = "请删除" + t + "个字符";
			return n
		}, inputTooShort : function(e) {
			var t = e.minimum - e.input.length, n = "请再输入至少" + t + "个字符";
			return n
		}, loadingMore : function() {
			return "加载更多..."
		}, maximumSelected : function(e) {
			var t = "最多只能选择" + e.maximum + "个项目";
			return t
		}, noResults : function() {
			return "没有找到匹配项"
		}, searching : function() {
			return "正在搜索..."
		} }
	}), { define : e.define, require : e.require }
})();