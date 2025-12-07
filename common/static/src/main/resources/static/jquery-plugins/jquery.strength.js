/*!
 * strength.js
 * Original author: @aaronlumsden
 * Further changes, comments: jeesite
 * Licensed under the MIT license
 */
;(function ($, window, document, undefined) {
	
	var pluginName = "strength";
	
	function Plugin(element, options) {
		this.element = element;
		this.$elem = $(this.element);
		this.options = $.extend({}, $.fn.strength.defaults, options);
		this._defaults = $.fn.strength.defaults;
		this._name = pluginName;
		this.init();
	}
	
	Plugin.prototype = {
		
		init : function () {
			
			var options = this.options;
			
			var upperCaseExp = new RegExp("[A-Z]");
			var lowerCaseExp = new RegExp("[a-z]");
			var numberExp = new RegExp("[0-9]");
			var specialExp = new RegExp("[~!@#$%\\^&\\*()_+\\{\\}:\"\\|<>?`\\-=\\[\\];\\\'\\\\,\\./]");
			
			function check_strength(thisval, thisid) {
				var charLength = thisval.length >= 8 ? 1 : 0;
				var upperCase = thisval.match(upperCaseExp) ? 1 : 0;
				var lowerCase = thisval.match(lowerCaseExp) ? 1 : 0;
				var number = thisval.match(numberExp) ? 1 : 0;
				var special = thisval.match(specialExp) ? 1 : 0;
				var total = charLength + upperCase + lowerCase + number + special;
				get_total(total, thisid);
				options.strengthCheck(thisval, total);
			}
			
			function get_total(total, thisid) {
				var thismeter = $('div[data-meter="' + thisid + '"]').removeClass();
				thismeter.parent().removeClass().addClass(options.strengthMeterClass);
				if (total == 1) {
					thismeter.parent().addClass('veryweak');
					thismeter.addClass('veryweak').html('<p>'+options.veryweakText+'</p>');
				} else if (total == 2) {
					thismeter.parent().addClass('weak');
					thismeter.addClass('weak').html('<p>'+options.weakText+'</p>');
				} else if (total == 3 || total == 4) {
					thismeter.parent().addClass('medium');
					thismeter.addClass('medium').html('<p>'+options.mediumText+'</p>');
				} else if (total > 4) {
					thismeter.parent().addClass('strong');
					thismeter.addClass('strong').html('<p>'+options.strongText+'</p>');
				} else {
					thismeter.html('');
				}
			}
			
			var isShown = false;
			var strengthButtonText = options.strengthTipText + ' ' + options.strengthButtonText;
			var strengthButtonTextToggle = options.strengthTipText + ' ' + options.strengthButtonTextToggle;
			
			var thisid = this.$elem.attr('id');
			
			this.$elem.parent().addClass(options.strengthClass);
			
			this.$elem.addClass(options.strengthInputClass).attr('data-password', thisid)
				.after('<input style="display:none" class="' + this.$elem.attr('class') + '" data-password="'
					+ thisid + '" type="text" name="" value=""><a data-password-button="' + thisid 
					+ '" href="javascript:" class="' + options.strengthButtonClass + '" tabindex="-1">'
					+ strengthButtonText + '</a><div class="' + options.strengthMeterClass
					+ '"><div data-meter="' + thisid + '"><p></p></div></div>');
			
			this.$elem.bind('keyup keydown', function (event) {
				thisval = $('#' + thisid).val();
				var st = $('input[type="text"][data-password="' + thisid + '"]').val(thisval);
				try{st.resetValid();}catch(e){}; // 如果表单加了验证，则验证。
				check_strength(thisval, thisid);
			});
			
			$('input[type="text"][data-password="' + thisid + '"]').bind('keyup keydown', function (event) {
				thisval = $('input[type="text"][data-password="' + thisid + '"]').val();
				$('input[type="password"][data-password="' + thisid + '"]').val(thisval);
				check_strength(thisval, thisid);
			});
			
			$(document.body).on('click', '.' + options.strengthButtonClass, function (e) {
				e.preventDefault();
				
				thisclass = 'hide_' + $(this).attr('class');
				
				if (isShown) {
					$('input[type="text"][data-password="' + thisid + '"]').hide();
					$('input[type="password"][data-password="' + thisid + '"]').show().focus();
					$('a[data-password-button="' + thisid + '"]').removeClass(thisclass).html(strengthButtonText);
					isShown = false;
					
				} else {
					$('input[type="text"][data-password="' + thisid + '"]').show().focus();
					$('input[type="password"][data-password="' + thisid + '"]').hide();
					$('a[data-password-button="' + thisid + '"]').addClass(thisclass).html(strengthButtonTextToggle);
					isShown = true;
					
				}
				
			});
			
		}
	};
	
	// A really lightweight plugin wrapper around the constructor,
	// preventing against multiple instantiations
	$.fn[pluginName] = function (options) {
		return this.each(function () {
			if (!$.data(this, "plugin_" + pluginName)) {
				$.data(this, "plugin_" + pluginName, new Plugin(this, options));
			}
		});
	};
	
	$.fn[pluginName].defaults = {
		strengthClass : 'strength',
		strengthInputClass : 'strength_input',
		strengthMeterClass : 'strength_meter',
		strengthButtonClass : 'button_strength',
		strengthTipText: '建议：长度不小于8位，且包含，大写英文字母、小写英文字母、数字和符号。',
		strengthButtonText: '点击显示密码。',
		strengthButtonTextToggle: '点击隐藏密码。',
		veryweakText: '密码太弱啦！',
		weakText: '密码比较弱哦！',
		mediumText: '密码较安全！',
		strongText: '密码很强很安全！',
		// 验证后调用方法（当前值，安全级别）
		strengthCheck: function(thisval, thislevel){ }
	};
	
})(jQuery, window, document);
