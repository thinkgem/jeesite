/*!
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

// 添加收藏
function addFavorite(sURL, sTitle){
	if(!sTitle){sTitle = document.title;}
    try{
    	window.external.addFavorite(sURL, sTitle);
    }catch (e){
        try{
        	window.sidebar.addPanel(sTitle, sURL, "");
        }catch (e){
        	alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
}

// 自动滚动：setInterval("autoScroll('.jcarousellite')",3000);
function autoScroll(obj){
	var height = $(obj).find("ul:first li:first").height()+3;
    $(obj).find("ul:first").animate({marginTop:"-" + height + "px"},'slow',function(){
    		$(this).css({marginTop:"0px"}).find("li:first").appendTo(this);
    });
}