/*!
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

// 引入js和css文件
function include(id, path, file){
	if (document.getElementById(id)==null){
        var files = typeof file == "string" ? [file] : file;
        for (var i = 0; i < files.length; i++){
            var name = files[i].replace(/^\s|\s$/g, "");
            var att = name.split('.');
            var ext = att[att.length - 1].toLowerCase();
            var isCSS = ext == "css";
            var tag = isCSS ? "link" : "script";
            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
            var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
            document.write("<" + tag + (i==0?" id="+id:"") + attr + link + "></" + tag + ">");
        }
	}
}

// 打开一个窗体
function windowOpen(url, name, width, height){
	var top=parseInt((window.screen.height-height)/2,10),left=parseInt((window.screen.width-width)/2,10),
		options="location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes,"+
		"resizable=yes,scrollbars=yes,"+"width="+width+",height="+height+",top="+top+",left="+left;
	window.open(url ,name , options);
}

// 显示加载框
function loading(mess){
	top.$.jBox.tip.mess = null;
	top.$.jBox.tip(mess,'loading',{opacity:0});
}

// 确认对话框
function confirmx(mess, href){
	top.$.jBox.confirm(mess,'系统提示',function(v,h,f){
		if(v=='ok'){
			loading('正在提交，请稍等...');
			location = href;
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}

//设置validate的默认值
$.validator.setDefaults({
	 submitHandler: function(form) { 
		 loading('正在提交，请稍等...');
		 form.submit();
	 },
	 errorPlacement: function(error, element) {
		 $("#messageBox").text("输入有误，请先更正！").removeClass("alert-success").addClass("alert-error");
		 if ( element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append") ){
			 error.appendTo(element.parent().parent());
		 } else {
			 error.insertAfter(element);
		 }
	 }
});

//表格排序
function tableSort( configuration ){
	
	var defaults = {
		orderBy : '#orderBy', // 排序字段
		contentTable : '#contentTable', // 表格
		sortClass : 'sort', // 标志要排序的列的class
		callBack : $.noop // 回调函数
	};
	
	var config = $.extend({}, defaults, configuration);
	
	var $orderBy = $(config.orderBy),
		  $sortCol = $(config.contentTable + " th." + config.sortClass),
	 	  orderBy = $orderBy.val().split(" ");

	$sortCol.each(function(){
		if ($(this).hasClass(orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase() == "DESC" ? "down" : "up";
			$(this).html($(this).html() + " <i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
		}
	});
	
	$sortCol.click(function(){
		var order = $(this).attr("class").split(" "),
			  sort = $orderBy.val().split(" ");
		
		for(var i=0; i<order.length; i++){
			if (order[i] == config.sortClass){order = order[i+1]; break;}
		}
		
		if (order == sort[0]){
			sort = (sort[1]&&sort[1].toUpperCase()=="DESC" ? "ASC" : "DESC");
			$orderBy.val(order + " DESC" != order+" " + sort ? "" : order + " " + sort);
		}else{
			$orderBy.val(order + " ASC");
		}
		
		config.callBack();
	});
}

$(document).ready(function() {
	//所有下拉框使用select2
	$("select").select2();
	$('.fancybox').fancybox();
});