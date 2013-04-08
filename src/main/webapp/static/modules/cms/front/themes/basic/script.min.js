/*!
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
function addFavorite(b,a){if(!a){a=document.title}try{window.external.addFavorite(b,a)}catch(c){try{window.sidebar.addPanel(a,b,"")}catch(c){alert("加入收藏失败，请使用Ctrl+D进行添加")}}}function autoScroll(b){var a=$(b).find("ul:first li:first").height()+3;$(b).find("ul:first").animate({marginTop:"-"+a+"px"},"slow",function(){$(this).css({marginTop:"0px"}).find("li:first").appendTo(this)})};