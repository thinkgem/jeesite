<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:title default="欢迎光临"/> - ${site.title} - Powered By JeeSite</title>
	<%@include file="/WEB-INF/views/modules/cms/front/include/head.jsp" %>
	<!-- Baidu tongji analytics --><script>var _hmt=_hmt||[];(function(){var hm=document.createElement("script");hm.src="//hm.baidu.com/hm.js?82116c626a8d504a5c0675073362ef6f";var s=document.getElementsByTagName("script")[0];s.parentNode.insertBefore(hm,s);})();</script>
	<sitemesh:head/>
</head>
<body>
	<div class="main">
		<div class="header">
			<div class="nav"><a href="${ctx}/map-${site.id}${fns:getUrlSuffix()}">站点地图</a> | <a href="javascript:" onclick="addFavorite('<%=request.getRequestURL() %>')">加入收藏</a></div>
			<h1>JeeSite Web</h1>
			<div class="time"><br/>今天是：<span>${fns:getDate('yyyy年MM月dd日 E')}</span></div>
	    	<form action="${ctx}/search" method="get" target="_blank">
	    		<div class="search">站内搜索：<span class="box"><input type="text" name="q" class="txt" maxlength="20"/><input type="submit" value="搜索" class="btn"/></span></div>
	    	</form>
		</div>
		<div class="clear"></div>
		<div class="menu_nav">
			<ul class="nav_content">
				<li class="${empty category.id?'current':''}"><a href="${ctx}/index-${site.id}${fns:getUrlSuffix()}"><span>首  页</span></a></li>
				<c:forEach items="${fnc:getMainNavList(site.id)}" var="category">
					<c:choose>
		    			<c:when test="${not empty category.href}">
			    			<c:choose>
				    			<c:when test="${fn:indexOf(category.href, '://') eq -1}"><c:set var="url" value="${ctx}${category.href}"/></c:when>
				    			<c:otherwise><c:set var="url" value="${category.href}"/></c:otherwise>
				    		</c:choose>
		    			</c:when>
		    			<c:otherwise><c:set var="url" value="${ctx}/list-${category.id}${fns:getUrlSuffix()}"/></c:otherwise>
		    		</c:choose>
		    		<li class="${requestScope.category.id eq category.id||fn:indexOf(requestScope.category.parentIds,category.id) ge 1?'current':''}"><a href="${url}" target="${category.target}"><span>${category.name}</span></a></li>
		    	</c:forEach>
			</ul>
			<div class="menu_nav_right"></div>
		</div>
		<div class="clear"></div>
		<div class="content">
			<sitemesh:body/>
		</div>
		<div class="clear"></div>
	    <div class="footer">
			<div class="footer_nav"><a href="${ctx}/guestbook" target="_blank">公共留言</a> | <a href="${ctx}/search" target="_blank">全站搜索</a> | <a href="${ctx}/map-${site.id}${fns:getUrlSuffix()}" target="_blank">站点地图</a> | <a href="mailto:thinkgem@163.com">技术支持</a> | <a href="${pageContext.request.contextPath}${fns:getAdminPath()}" target="_blank">后台管理</a></div>
			<div class="copyright">${site.copyright}</div>
		</div>
	</div>
</body>
</html>