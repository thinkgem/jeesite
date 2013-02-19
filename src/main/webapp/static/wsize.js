/*!
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

// 主框架窗口大小调整
$("#left").width(lw);$("#right").width(rw);
$("#openClose").click(function(){
	if($(this).hasClass("close")){
		$(this).removeClass("close");
		$(this).addClass("open");
		$("#left").animate({width:lwClose,opacity:"hide"});
		$("#right").animate({width:rwClose});
	}else{
		$(this).addClass("close");
		$(this).removeClass("open");
		$("#left").animate({width:lw,opacity:"show"});
		$("#right").animate({width:rw});
	}
});
if(!Array.prototype.map)
	Array.prototype.map = function(fn,scope) {
	var result = [],ri = 0;
	for (var i = 0,n = this.length; i < n; i++){
	  if(i in this){
	    result[ri++]  = fn.call(scope ,this[i],i,this);
	  }
	}
	return result;
};
var getWindowSize = function(){
	return ["Height","Width"].map(function(name){
	  return window["inner"+name] ||
		document.compatMode === "CSS1Compat" && document.documentElement[ "client" + name ] || document.body[ "client" + name ];
	});
};
window.onload = function (){
	if(!+"\v1" && !document.querySelector) { // for IE6 IE7
	  document.body.onresize = resize;
	} else { 
	  window.onresize = resize;
	}
	function resize() {
		wSize();
		return false;
	}
};
wSize(); // 在主窗体中定义，设置调整目标