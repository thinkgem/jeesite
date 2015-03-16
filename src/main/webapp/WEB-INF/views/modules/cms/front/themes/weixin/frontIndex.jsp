<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="cms_default_${site.theme}" />
	<meta name="description" content="JeeSite ${site.description}" />
	<meta name="keywords" content="JeeSite ${site.keywords}" />
	<style type="text/css">
		@charset "utf-8";
		*{margin:0;padding:0;}.wz_17_wrapper a{ display:block;color: #FFF;text-decoration: none;}
		.wz_17_flat_left{float:left;}.wz_17_flat_right{float:right;}.clfl{	float:none;clear:both;}
		.wz_17_color_01{background-color:#228981;}.wz_17_color_02{background-color:#8019bf;}
		.wz_17_color_03{background-color:#22601a;}.wz_17_color_04{background-color:#1f4b89;}
		.wz_17_color_05{background-color:#af1d42;}.wz_17_color_06{background-color:#ce6619;}
		.wz_17_color_07{background-color:#228981;}.wz_17_color_08{background-color:#8019bf;}
		.wz_17_color_09{background-color:#22601a;}.wz_17_color_010{background-color:#1f4b89;}
		.wz_17_color_011{background-color:#af1d42;}.wz_17_color_012{background-color:#ce6619;}
		.wz_17_color_013{background-color:#af1d42;}.wz_17_color_014{background-color:#ce6619;}
		.wz_17_color_015{background-color:#af1d42;}.wz_17_color_016{background-color:#ce6619;}
		.wz_17_color_017{background-color:#af1d42;}.wz_17_color_018{background-color:#ce6619;}
		.wz_17_color_019{background-color:#af1d42;}.wz_17_color_020{background-color:#ce6619;}
		.wz_17_wrapper {width: 320px;margin-right: auto;margin-left: auto;}
		.wz_17_line{color:#FFF;font-size:16px;font-family:"微软雅黑";background-color:#999;height:103px;width:100%;margin-bottom:10px;}
		.wz_17_text{overflow:hidden;text-align:center;line-height:103px;width:103px;height:103px;}
		.wz_17_img{	overflow:hidden;height:103px;width:217px;}	
		.wz_17_img img{	width:100%;min-height:103px;}
		.foot_bq{width:300px;margin:auto;bottom:0;font-size:12px;text-align:center;line-height:60px;color:#fff;}
		.btn_music {width:40px;height:40px;margin:5px 0 20px 20px;background: none;background-image: url("${ctxStaticTheme}/wz/images/wz26/music.png");
			background-size: 100%;background-repeat: no-repeat;border: none;}
		.btn_music.on {background-image: url("${ctxStaticTheme}/wz/images/wz26/musicstop.png");}
		/*.btn_music:hover{background-image:url(${ctxStaticTheme}/wz/images/wz26/musichover.png);}*/
	</style>
</head>
<body>
	<img id="ygj_bg_show" src="${ctxStaticTheme}/images/idx_bg.jpg" 
		style="width: 100%; height: 100%; min-width: 320px; z-index: -1; position: fixed; top: 0; left: 0" />
	<div class="wz_17_wrapper" style="z-index:100;">
		<c:forEach items="${fnc:getMainNavList(site.id)}" var="category" varStatus="status">
			<c:if test="${status.index lt 20}">
	    		<div class="wz_17_line">
					<a href="${category.url}">
						<div class="wz_17_text wz_17_color_0${status.index+1} wz_17_flat_${status.index % 2 eq 0 ? 'left' : 'right'}">${category.name}</div>
						<div class="wz_17_img ">
							<img src="${category.image}" />
						</div>
					</a>
					<div class="clfl"></div>
				</div>
			</c:if>
    	</c:forEach>
	</div>
	<div class="foot_bq"></div>
</body>
</html>