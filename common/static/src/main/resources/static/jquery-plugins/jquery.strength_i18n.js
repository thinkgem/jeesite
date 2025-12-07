(function($) {

	$.fn.strength = $.fn.strength || {};
	if (window.lang == 'en'){
		$.extend(true, $.fn.strength.defaults, {
			strengthTipText: 'Password no less than 8 bits in length, and included, upper case letters, lower case letters, Numbers and symbols.',
			strengthButtonText: 'Click to display the password.',
			strengthButtonTextToggle: 'Click the hidden password.',
			veryweakText: 'Password is too weak!',
			weakText: 'Password is weak!',
			mediumText: 'Password is safer!',
			strongText: 'Password is very strong and safe!'
		});
	} else if (window.lang == 'ja_JP'){
		$.extend(true, $.fn.strength.defaults, {
			strengthTipText: 'パスワードは长さが8ビット以内で、その中には、上のケース、下のケース、数字、記号があります。',
			strengthButtonText: 'クリックしてパスワードを表示します。',
			strengthButtonTextToggle: '隠れパスワードをクリックします。',
			veryweakText: 'パスワードが弱すぎる!',
			weakText: '体の弱いパスワードは',
			mediumText: 'パスワードは!平和な',
			strongText: 'パスワードは丈夫で安全です!'
		});
	}
	
})(jQuery);
