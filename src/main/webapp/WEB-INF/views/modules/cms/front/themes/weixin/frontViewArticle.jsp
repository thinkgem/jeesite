<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${article.title} - ${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${article.description} ${category.description}" />
	<meta name="keywords" content="${article.keywords} ${category.keywords}" />
    <link href="${ctxStaticTheme}/wz/css/zf.css-02.css"  rel="stylesheet" type="text/css" />
    <style id="table">div table.noBorderTable td,div table.noBorderTable th,div table.noBorderTable caption{border:1px dashed #ddd !important}div table{margin-bottom:10px;border-collapse:collapse;display:table;}div td,div th{ background:white; padding: 5px 10px;border: 1px solid #DDD;}div caption{border:1px dashed #DDD;border-bottom:0;padding:3px;text-align:center;}div th{border-top:2px solid #BBB;background:#F7F7F7;}div td p{margin:0;padding:0;}</style>
	<style type="text/css">
		body {font-family: Helvetica, STHeiti STXihei, Microsoft JhengHei,	Microsoft YaHei, Tohoma, Arial;margin: 0;color: #222222;
		/* 	background: none repeat scroll 0 0 #F8F7F4; */height: 100%;position: relative;}
		#wz_title {color: #000000;font-size: 20px;font-weight: bold;margin: 0;}
		#wz_date {color: #8C8C8C;font-size: 11px;}
		.wz_fengmian {margin: 10px 0;}
		.wz_content {color: #3E3E3E;line-height: 1.5;overflow: hidden;width: 100%;font-size: 15px;}
		.wz_content img,.wz_content embed{max-width: 100%;}
		.seemore {background: none repeat scroll 0 0 #FFFFFF;border: 1px solid #D9D9D9;border-radius: 10px 10px 10px 10px;color: #DDDDDD;display: block;font-size: 16px;height: 33px;line-height: 33px;margin: 10px auto;text-align: center;text-decoration: none;}
		.seemore a.no_border {border: medium none;}
		.seemore a {border-right: 1px solid #DDDDDD;color: #535353;display: inline-block;text-align: center;text-decoration: none;width: 100%;}
		a.activity-meta {outline: 0 none;text-decoration: none;}
		a.activity-meta:active {color: #607FA6;}
		a.activity-meta:active .icon_link_arrow {background: url("${ctxStaticTheme}/images/link_arrow.png") no-repeat	scroll 0 0/100% auto transparent;}
		.activity-info .icon_link_arrow {margin-left: 3px;margin-top: -5px;}
		.icon_link_arrow {background: url("${ctxStaticTheme}/images/link_arrow.png") no-repeat	scroll 0 0/100% auto transparent;display: inline-block;height: 7px;vertical-align: middle;width: 7px;}
		.activity-meta {color: #607FA6;display: inline-block;font-size: 11px;margin-left: 8px;padding-bottom: 2px;padding-top: 2px;}
		.text-ellipsis {display: inline-block;max-width: 104px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
		/*顶部微信号*/
		#biz-link {background: url("${ctxStaticTheme}/images/wz_toptiao.png") no-repeat scroll center center / 100% 100% transparent;border: medium none;border-radius: 0 0 0 0;box-shadow: none;height: 42px;padding: 12px 12px 12px 62px;position: relative;text-align: left;}#biz-link .arrow {position: absolute;right: 15px;top: 25px;}#biz-link .logo {height: 42px;left: 5px;overflow: hidden;padding: 6px;position: absolute;top: 6px;width: 42px;}#biz-link .logo img {border-radius: 15px 15px 15px 15px;height: 42px;position: relative;width: 42px;z-index: 10;}#biz-link .logo .circle {background: url("${ctxStaticTheme}/images/wz_topquan.png") no-repeat scroll center center / 100% 100% transparent;height: 54px;left: 0;position: absolute;top: 0;width: 54px;z-index: 100;}#nickname {color: #454545;font-size: 15px;text-shadow: 0 1px 1px white;}#weixinid {color: #A3A3A3;font-size: 12px;line-height: 20px;text-shadow: 0 1px 1px white;}
		.icons {border-radius: 5px 5px 5px 5px;height: 25px;overflow: hidden;position: relative;width: 25px;}.icons.arrow-r {background: url("${ctxStaticTheme}/images/mesgIcon.png") no-repeat scroll center center / 100% auto transparent;height: 16px;width: 12px;}
		.btn {background-color: #FCFCFC;border: 1px solid #CCCCCC;border-radius: 5px 5px 5px 5px;box-shadow: 0 1px 4px rgba(0, 0, 0, 0.3);color: #222222;cursor: pointer;display: block;font-size: 15px;font-weight: bold;margin: 8px 0;padding: 10px;text-align: center;text-decoration: none;}
		</style>
		<style type="text/css">
		.wz_header {/*position:relative;height:100%;*/color:#222;font-family:Microsoft YaHei,Helvitica,Verdana,Tohoma,Arial,san-serif;/*background-color:#f0f0f0;*/margin:0;padding: 0;text-decoration: none;}
		.wz_header a {color:#000000;text-decoration: none;}
		#ui-header {width: 100%;height: 46px;line-height: 46px;z-index: 3;position: absolute;left: 0;top: 0;right: 0;}
		#ui-header .fixed {display: block;margin: 0 auto;min-width: 320px;height: 46px;top: 0;border-bottom: 1px solid #D1D1D1;box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);-moz-box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);-webkit-box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);background-image: -webkit-gradient(linear, left top, left bottom, from(#ffffff),	to(#f9f9f9) );background-image: -webkit-linear-gradient(#ffffff, #f9f9f9);background-image: -moz-linear-gradient(#ffffff, #f9f9f9);background-image: -ms-linear-gradient(#ffffff, #f9f9f9);background-image: -o-linear-gradient(#ffffff, #f9f9f9);background-image: linear-gradient(#ffffff, #f9f9f9);/*opacity: 0.95;*/}
		.ui-title {min-height: 46px;text-align: center;font-size: 16px;font-weight: normal;display: block;margin: 10px 70px 0;padding: 0;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;outline: 0 !important;display: none;text-indent: -10px;}
		.ui-btn-left {position: absolute;top: 0px;left: 0px;background: url("${ctxStaticTheme}/images/wz_menu.png") no-repeat center center;display: block;width: 60px;height: 44px;text-indent: -9999px;margin: 0;padding: 0;background-size: 24px auto;}
		.ui-btn-left_pre {position: absolute;top: 0px;left: 0px;background: url("${ctxStaticTheme}/images/wz_pre.png") no-repeat center center;display: block;width: 60px;height: 44px;text-indent: -9999px;margin: 0;padding: 0;background-size: 24px auto;}
		.ui-btn-right {position: absolute;top: 0px;right: 0px;background: url("${ctxStaticTheme}/images/wz_refresh.png") no-repeat center center;display: block;width: 60px;height: 44px;text-indent: -9999px;margin: 0;padding: 0;background-size: 28px auto;}
		.ui-btn-right_menu {position: absolute;top: 0px;right: 0px;background: url("${ctxStaticTheme}/images/wz_menu.png") no-repeat center center;display: block;width: 60px;height: 44px;text-indent: -9999px;margin: 0;padding: 0;background-size: 24px auto;}
		.ui-btn-right_home {position: absolute;top: 0px;right: 0px;background: url("${ctxStaticTheme}/images/wz_home.png") no-repeat center center;display: block;width: 60px;height: 44px;text-indent: -9999px;margin: 0;padding: 0;background-size: 24px auto;}
		#ui-header li:hover {background-color: #000000;}
		#ui-header li:active {background-color: #000000;}
		#overlay {position: absolute;top: 0;left: 0;width: 100%;height: 100%;background: #fff;opacity: 0;filter: alpha(opacity = 0);display: none;z-index: 4;}
		#win {position: absolute;top: 55px;left: 50%;width: 200px;margin: 0 0 0 -100px;display: none;z-index: 5;}
		/*popmenu*/
		#popmenu{cursor:pointer; display:block; position:relative;  text-align: center; width: 200px; margin:0 auto}
		#popmenu:after {content: "";width: 0;height: 0;position: absolute;right: 50px;top: 50%;margin-top: -2px;border-width: 5px 5px 0 5px;border-style: solid;border-color: #9D9D9D transparent;}
		.dropdown { /* Size and position */position: relative;display: block;margin: 0 auto;padding: 5px;/* Styles */background-color: rgba(47, 47, 47, 1);border-radius: 7px;border: 1px solid rgba(0, 0, 0, 0.15);box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);-moz-box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);-webkit-box-shadow: 0 0 10px 2px rgba(0, 0, 0, 0.15);cursor: pointer;outline: none;list-style: none outside none;}
		.dropdown:after {content: "";width: 0;height: 0;position: absolute;bottom: 100%;right: 90px;border-width: 0 6px 6px 6px;border-style: solid;border-color: rgba(47, 47, 47, 0.9) transparent;}
		.dropdown:before {content: "";width: 0;height: 0;position: absolute;bottom: 100%;right: 88px;border-width: 0 8px 8px 8px;border-style: solid;border-color: rgba(0, 0, 0, 0.1) transparent;}
		.dropdown li {background-color: rgba(58, 58, 58, 1);display: block;float: left;height: 37px;overflow: hidden;width: 50%;}
		.dropdown li span {border-color: #494949 #181818 #181818 #494949;border-left: 1px solid #494949;border-style: solid;border-width: 1px;color: #FFFFFF;line-height: 37px;display: block;font-size: 15px;height: 37px;text-align: center;text-shadow: 0 2px 2px #000000;width: 100%;overflow: hidden;}
		.dropdown li:hover {background-color: rgba(0, 0, 0, 0.9);}
		.clr {display: block;clear: both;height: 1px;overflow: hidden;}
		.top46 {height: 46px;padding: 0;margin: 0}
	</style>
	<script src="${ctxStaticTheme}/template/default/vip/js/jquery.js" 	type="text/javascript"></script>
	<script	src="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/js/wxm-core1530d0.js" ></script>
	<script type="text/javascript">
		function cli_hide2(){
			$("#layer_mask2").hide();
			$('#forward2').hide();
			$('#men_pho_id').hide(); 
		}
		function men_pho_cli2(){
			$("#layer_mask2").show();
			$('#men_pho_id').show();
		}
		function cli_zhuanfa(){
			$("#layer_mask2").show();
			$('#forward2').show();
		}
	</script>
</head>
<body>

   <div class="layer-mask" id="layer_mask2" style="z-index: 10002;display:none;  position: absolute;    position: fixed;    width: 100%;    height: 100%;    background-color: #1b1b1b;    opacity: 0.9;    top: 0;left: 0;" onClick="cli_hide2()"></div>
   <div class="helper forward" id="forward2" style="z-index:11111;;display:none"></div>
   <div class="men_pho" id="men_pho_id" style="	z-index:10003;display:none"></div>
	<script type="text/javascript">
		window.onload = function () {
			var oWin = document.getElementById("win");
			var oLay = document.getElementById("overlay");	
			var oBtn = document.getElementById("popmenu");
			oBtn.onclick = function ()
			{
				oLay.style.display = "block";
				oWin.style.display = "block";	
			};
			oLay.onclick = function ()
			{
				oLay.style.display = "none";
				oWin.style.display = "none";
			};
		};
		
		function clickBack(){
			window.location.href = "${ctx}/list-${category.id}${urlSuffix}";
		/*    if (/(iPhone|iPad|iPod)/i.test(navigator.userAgent)) {             
		        window.location.href = "wz.php-mod=wzfenlei&openid=fromuserid&wzid=14251.htm#yyy.qq.com";
			} else { window.history.go("-1"); }*/
		}
	</script>
	<div class="wz_header">
		<div id="ui-header">
			<div class="fixed">
				<a class="ui-title" id="popmenu">选择分类</a> 
				<a class="ui-btn-left_pre" href="javascript:" onClick="clickBack();"></a>
				<a class="ui-btn-right" href="javascript:window.location.reload();"></a>
			</div>
		</div>
		<div id="overlay"></div>
		<div id="win">
			<ul class="dropdown">
				<c:forEach items="${fnc:getMainNavList(site.id)}" var="category" varStatus="status">
					<li><a href="${category.url}" ><span>${category.name}</span></a></li>
			   	</c:forEach>
				<div class="clr"></div>
			</ul>
		</div>
		<div class="top46"></div>
	</div>
	<div class="container" style="padding: 15px 15px 0;">
		<div class="row-fluid">
			<div class="span12">
				<div>
					<h4 id="wz_title">${article.title}</h4>
					<span id="wz_date">发布者：${article.createBy.name} &nbsp; 点击数：${article.hits} &nbsp; 发布时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> &nbsp; 更新时间：<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
					<a class="activity-meta" href="weixin://addfriend/chinatea2014" onclick="men_pho_cli2()"> 
						<span class="text-ellipsis">${article.description}</span> <i class="icon_link_arrow"></i>
					</a>
				</div>
				<div class="wz_fengmian">
					<img width="100%" src="${article.image}" ></img>
				</div>
				<div class="wz_content">${article.articleData.content}</div>
				<div class="seemore">
					<a class="no_border"  href="${ctx}/index-${site.id}${fns:getUrlSuffix()}" >查看更多</a>
				</div>
				<div class="seemore" style="background: none repeat scroll 0 0 #5BB75B;">
					<a class="no_border" style="color:#FFFFFF;" href="weixin://addfriend/thinkgem" onclick="men_pho_cli2()">关注公众账号</a>
				</div>
				<div class="zf_wrapper_001"> 
              		<div id="gallery">
    					<button class="zf_wrapper_zhuangfa" type="button" onclick="cli_zhuanfa()">     <span>转发</span><span><img src="${ctxStaticTheme}/wz/images/zf.png"  width="16" /></span></button> 
   						<button class="zf_wrapper_fenxiang" type="button" onclick="cli_zhuanfa()">     <span>分享</span><span><img src="${ctxStaticTheme}/wz/images/fx.png"  width="16" /></span></button> 
  					</div><!-- end of gallery-->
 				</div>
				<div style="height: 30px;"></div>				<div style="height: 50px;"></div>
			</div>
		</div>
	</div>
</body>
</html>
