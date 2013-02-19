// 三页切换焦点图
(function($){
	$(document).ready(function() {
		var ready = true;
		$('ul#preview-slider-holder > li').mouseover(function () {
			var _this = this;
			var position = 0;
			if($(this).hasClass('preview-first')){
				position = 0;
			} else if($(this).hasClass('preview-second')){
				position = 1;
			} else if($(this).hasClass('preview-third')){
				position = 2;
			}
			if(!ready){
				$("#preview-slider > div:not(':last'):not(':eq(" + position + ")')").each(function(){
					if($(this).is(':animated')){
						$(this).stop(true, true);
					}				
				});				
			}
			ready = false;
			$("#preview-slider > div:not(':last'):not(':eq(" + position + ")')").hide();
			$('img', this).animate({"top": "-10px"}, 200);
			$('#preview-slide-' + (position + 1)).fadeIn(600, function(){
				ready = true;
			});
			$('img[id^="preview-thumbnail-im"]:not([id="preview-thumbnail-im' + (position+1) + '"])').stop(true, false).animate({'top': '0px'}, 200);
		});	
	});
})(jQuery);