/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 * @version 2022-5-16
 */
$(function(){
	if ($(window).width() <= 767){
		$('body').append('<div class="btn btn-default full-screen-menu"><i class="fa fa-bars"></i></div>');
	}
	$('.full-screen-menu,.user-panel .img-circle').click(function(){
		if (!$('.main-header').is(':hidden')){
			$('.main-header').slideUp(100, function(){
				if (($(window).width() > 767 && !$('body').hasClass('sidebar-collapse'))
					|| $('body').hasClass('sidebar-open')){
					$('[data-toggle="push-menu"]').click();
				}
			});
		}else{
			$('.main-header').slideDown(100, function(){
				setTimeout(function(){
					// if (${@Global.getConfig('sys.index.sidebarStyle', '1') != '2'}){
					if (!$('body').hasClass('sidebar-collapse')){
						$('[data-toggle="push-menu"]').click();
					}
				},100);
			});
		}
		setTimeout(function(){
			$('#tabpanel').tabPanel('resize');
		},500);
	});
	$('#leftMenu').on('click', '.addTabPage', function (e) {
		if ($(window).width() <= 767 && $('body').hasClass('sidebar-open')){
			var href = $(this).data('href');
			if (href != '' && href != 'blank'){
				if ($(window).width() <= 767){
					$('.full-screen-menu').click();
				}
			}
		}
	});
});
