/*-----------------------------------------------------------------------------------------/
 * JavaScript Framework Written For PowerEasy. Based On JQuery 1.3.2+.
/*----------------------------------------------------------------------------------------*/


/*-----------------------------------------------------------------------------------------/
 * JQuery PlugIn - Slide Show(or, Switch Tab)
 * JQuery滑动切换插件 ver 1.2.0
 *
 * defaultIndex 	- 默认选中的标签索引，从0开始
 * titOnClassName	- 标签选中时的样式
 * titCell			- 自定义标题标签，支持选择符
 * mainCell			- 自定义标题标签，支持选择符
 * delayTime		- 延迟触发时间. 当这个时间小于切换动画效果时间时, 动画将被禁用
 * interTime		- 自动切换时间. 当这个时间大于0时, 标签将定时自动切换
 * trigger			- 滑动触发方式. 默认为click, 可选择mouseover
 * effect			- 切换动画. 默认不使用动画. 目前仅提供fade(淡出), slide(向下展开)两种
 * omitLinks		- 是否忽略带链接标签，默认为否
 * debug			- 调试模式. 默认关闭
/*----------------------------------------------------------------------------------------*/

jQuery.fn.switchTab = function(settings) {
	settings = jQuery.extend({//可配置参数
		defaultIndex: 0,
		titOnClassName: "on",
		titCell: "dt span",
		mainCell: "dd",
		delayTime: 250,
		interTime: 0,
		trigger: "click",
		effect: "",
		omitLinks: false,
		debug: ""
	},
	settings,
	{//插件信息
		version: 120
	});

	this.each(function() {
		var st;
		var curTagIndex = -1;
		var obj = jQuery(this);
		if(settings.omitLinks && settings.titCell.substr(settings.titCell.length-1, 1)=="a"){
			settings.titCell = settings.titCell + "[href^='#']";
		}
		var oTit = obj.find(settings.titCell);
		var oMain = obj.find(settings.mainCell);
		var cellCount = oTit.length;//可切换个数
		var ShowSTCon = function (oi){
			if(oi != curTagIndex){
				if(curTagIndex<0)obj.find(settings.titCell+"."+settings.titOnClassName).removeClass(settings.titOnClassName);
				else oTit.eq(curTagIndex).removeClass(settings.titOnClassName);
				oMain.hide();
				obj.find(settings.titCell + ":eq(" + oi + ")").addClass(settings.titOnClassName);
				if(settings.delayTime <250 && settings.effect != "")settings.effect = "";
				if(settings.effect == "fade"){
					obj.find(settings.mainCell + ":eq(" + oi + ")").fadeIn({queue: false, duration: 250});
				}else if(settings.effect == "slide"){
					obj.find(settings.mainCell + ":eq(" + oi + ")").slideDown({queue: false, duration: 250});
				}else{
					obj.find(settings.mainCell + ":eq(" + oi + ")").show();
				}
				curTagIndex = oi;
			}
		};
		
		var ShowNext = function (){
			oTit.eq(curTagIndex).removeClass(settings.titOnClassName);
			oMain.hide();
			if(++curTagIndex >= cellCount)curTagIndex = 0;
			oTit.eq(curTagIndex).addClass(settings.titOnClassName);
			oMain.eq(curTagIndex).show();
			//ShowSTCon(curTagIndex);
		};
		
		//根据defaultIndex初始化
		ShowSTCon(settings.defaultIndex);

		//定时切换
		if(settings.interTime > 0){
			var sInterval = setInterval(function(){
				ShowNext();
			}, settings.interTime);
		}

		//处理交互事件
		oTit.each(function(i, ele){
			if(settings.trigger=="click"){
				jQuery(ele).click(function(){
					ShowSTCon(i);
					return false;//若有链接而选择了click模式, 链接不起作用
				});
			}else if(settings.delayTime > 0){
				jQuery(ele).hover(function(){
					st = setTimeout(function(){//延时触发
						ShowSTCon(i);
						st = null;
					}, settings.delayTime);
				},function(){
					if(st!=null)clearTimeout(st);
				});
			}else{
				jQuery(ele).mouseover(function(){
					ShowSTCon(i);
				});
			}
		});
	});
	if(settings.debug!="")alert(settings[settings.debug]);
	return this;
};


//垂直向上滚动
//wrapper, 容器选择符
//sItem, 滚动元素选择符
jQuery.fn.scrollUp = function(settings) {
	settings = jQuery.extend({
		delayTime: 4000,
		sItem: 'li'
	}, settings);

	var obj = jQuery(this);
	this.each(function() {
		var stepScroll = function(){
			var curItem = obj.find(settings.sItem + ":first");
			curItem.animate({
				marginTop: "-" + curItem.height()
			},
			300,
			"",
			function(){
				curItem.appendTo(obj);
				curItem.css("margin-top", "0px");
			});
		};
		setInterval(stepScroll, settings.delayTime);
	});
	return this;
};

//内容无缝滚动
function Marquee(marqueeBox, delaytime, direction, itemCell){
	if(delaytime == undefined)delaytime = 50;
	if(direction == undefined)direction = "up";
	if(itemCell == undefined)itemCell = "ul";
	var oMarquee = jQuery(marqueeBox);
	var oMarqueeCon = oMarquee.find(itemCell);

	if( direction=="left"&&( oMarqueeCon.width()< oMarquee.width() ) ) return; //zx增加，当内容宽度少于显示宽度时不滚动
	if( direction=="up" &&( oMarqueeCon.height()< oMarquee.height() ) ) return; //zx增加，当内容高度少于显示高度时不滚动

	var oMarqueeCopy = oMarqueeCon.clone(true).insertAfter(oMarqueeCon);

	var ScrollUp = function(){
		if(oMarqueeCopy[0].offsetHeight-oMarquee[0].scrollTop<=0){
			oMarquee[0].scrollTop = 0;
		}else{
			oMarquee[0].scrollTop++;
		}
	}
	
	var ScrollLeft = function(){
		if(oMarqueeCopy[0].offsetWidth-oMarquee[0].scrollLeft<=0){
			oMarquee[0].scrollLeft = 0;
		}else{
			oMarquee[0].scrollLeft++;
		}
	}
	
	switch(direction){
		case "left":
			var oScroll=setInterval(ScrollLeft, delaytime);
			oMarquee.hover(function(){
				clearInterval(oScroll);
			}, function(){
				oScroll=setInterval(ScrollLeft, delaytime);
			});
			break;
		case "up":
		default:
			var oScroll = setInterval(ScrollUp, delaytime);
			oMarquee.hover(function(){
				clearInterval(oScroll);
			}, function(){
				oScroll=setInterval(ScrollUp, delaytime);
			});
	}
}

//列高度统一
function HeightFix(column, column2, offset){
	var oCol = jQuery(column);
	var oCol2 = jQuery(column2);
	if(offset == undefined)offset = 0;
	if(oCol.height() > oCol2.height()){
		oCol2.height(oCol.height() - offset);
	}else{
		oCol.height(oCol2.height() - offset);
	}
}


// 加入收藏代码 Start -->
function AddFavorite(sURL, sTitle) {
    try {
        window.external.addFavorite(sURL, sTitle);
    } catch (e) {
        try {
            window.sidebar.addPanel(sTitle, sURL, "");
        } catch (e) {
            alert("加入收藏失败,请手动添加.");
        }
    }
}

// 设为首页代码 Start -->
function SetHome(pageURL) {
    if (document.all) {
        document.body.style.behavior='url(#default#homepage)';
        document.body.setHomePage(pageURL);
    }
    else if (window.sidebar) {
        if(window.netscape) {
            try {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            }
            catch (e) {
                alert( "该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项signed.applets.codebase_principal_support 值该为true" );
            }
        }
        var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components. interfaces.nsIPrefBranch);
        prefs.setCharPref('browser.startup.homepage',pageURL);
    }
}

jQuery(function($){
	$("input.inputtext").focus(function(){ 
	$(this).addClass("inpFucos"); 
	}); 
	$("input.inputtext").blur(function(){ 
	$(this).removeClass("inpFucos"); 
	}); 
});

//////////////////////////////////////////////////////////////////////////////////

var timer;
var currentpoint;
currentpoint = 0;
var playId;
centertu = 1;
var MaxPoint = 4;
var imgArr = [];
var isUp=true;




function setTimeer() {
    clearTimer();
    TimerdoClick('tbnav', 'tbsub', MaxPoint, currentpoint);
    //alert(currentpoint);

    timer = setInterval("loadImage(setTimeer)", 5000);
}

function loadImage(callback) {
if (currentpoint >= MaxPoint) currentpoint = 0;
    callback.call(); //将回调函数的this替换为Image对象

}

function clearTimer() {
    clearInterval(timer);
}






function TimerdoClick(navname, subname, idnum, o) {

    if (currentpoint >= MaxPoint) { currentpoint = 0; }
    if (currentpoint < 0) {currentpoint = MaxPoint - 1; }
	
	var curLeft=-currentpoint *980;
    
    switchBanner(currentpoint);
	curBtnDisplay();
	
	currentpoint++;
}


function switchBanner(id){
   playId=id;
   $(".big_banner_list").fadeOut(300,function(){
	   $(".big_banner_list").css("left",(-playId*980)+"px");
	   $(".big_banner_list").fadeIn(500);
   })
}

$(document).ready(function () {
	//最新动态
	myGod('div4',2000);
	//banner显示
    baneermain();
	
	btmNavDsiplay();
	
});

    function baneermain() {
		//底部5个区域的交互
		btmInteractive();
        MaxPoint = imgArr.length;
		
		//加载其他的大图
		bannerdisplay();
		
        loadImage(setTimeer);
		
		
		//按钮交互
		btnclickEvent();
		
		
    }
	
	function btmInteractive(){
	    $(".banner_adv").each(function(i){
			var curLeft=i*(173+18)+5;
			$(this).css("left",curLeft+"px");
			$(this).css("position","absolute");
			$(this).css("top","0px");
		});
		$(".banner_adv").mouseenter(function(){
	
			//$(this).css("display","none");
			
			
			$(this).animate( { top: "-10px"}, 200 );
		})
		
		$(".banner_adv").mouseleave(function(){
	        
			$(this).animate( { top: "0px"}, 200 );
			//$(this).css("display","none");
			//$(this).animate( { top: "10px"}, 50 );
		})
			
	}
	
	
	function btmNavDsiplay(){
		$("#btmNavBtn").click(function(){
			$(".footCategory").slideToggle(500);
			if(isUp){
				$(this).html('<span class="close">全站快速导航</span>');
				isUp=false;
			}else{
				$(this).html('<span class="open">全站快速导航</span>');
				isUp=true;
			}
		})

	}
	
	function bannerdisplay(){
		var imghtml=$(".big_banner_list").html();
		for(var i=1;i<MaxPoint;i++){
			imghtml +="<div class='banner_img_box'><a href='"+imgArr[i][1]+"' target='_blank'><img src='"+imgArr[i][0]+"' /></a></div>";
		}
		
		$(".big_banner_list").html(imghtml);
	}
	
	function btnclickEvent(){
	$(".index_banner_btn_bg").click(function(){
		  var id=$(this).attr("rel");
		  
		  var e = document.getElementById('tbsub');
		  currentpoint=id-1;

		  curShowImg(e);

		  curBtnDisplay();
	});
	
	
}

//按钮显示当前的状态；
function curBtnDisplay(){

	 $(".index_banner_btn_bg").each(function(i){
			
			   if(i==currentpoint){
				   if ($.browser.version == "6.0"){
					   $(this).css("background","url(http://www.ufida.com.cn/images/index/banner_img_bg2.png)" );
					   $(this).css("filter","none");
				   }else{
					   

				       if($(this).hasClass("nocur")){
					       $(this).removeClass("nocur");
				       }
				       if(!$(this).hasClass("cur")){
					       $(this).addClass("cur");
				       }
				   
				   }	
					
					
			   }else{
				   
				    if ($.browser.version == "6.0"){
					   $(this).css("background","url(http://www.ufida.com.cn/images/index/banner_img_bg.png)" );
					   $(this).css("filter","alpha(opacity=55)");
				   }else{

				  
				        if($(this).hasClass("cur")){
					        $(this).removeClass("cur");
				       }
				        if(!$(this).hasClass("nocur")){
					       $(this).addClass("nocur");
				       }
				   }

				  
			   }
			   
		 });

}


   
      
   /*最新动态js*/
   function myGod(id,w,n){
	var box=document.getElementById(id),can=true,w=w||1500,fq=fq||10,n=n==-1?-1:1;
	box.innerHTML+=box.innerHTML;
	box.onmouseover=function(){can=false};
	box.onmouseout=function(){can=true};
	var max=parseInt(box.scrollHeight/2);
	new function (){
		var stop=box.scrollTop%18==0&&!can;
		if(!stop){
			var set=n>0?[max,0]:[0,max];
			box.scrollTop==set[0]?box.scrollTop=set[1]:box.scrollTop+=n;
		};
		setTimeout(arguments.callee,box.scrollTop%18?fq:w);
	};
};
