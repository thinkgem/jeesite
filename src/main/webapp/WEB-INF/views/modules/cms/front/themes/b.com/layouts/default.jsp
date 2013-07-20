<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
<head>
	<title><sitemesh:title default="欢迎光临"/> - ${site.title}</title>
	<%@include file="/WEB-INF/views/modules/cms/front/include/head.jsp" %>
	<sitemesh:head/>
</head>
<body>
	<div class="navbar navbar-fixed-top" style="position:static;margin-bottom:10px;">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="${ctx}/index${fns:getUrlSuffix()}">${site.title}</a>
          <div class="nav-collapse">
            <ul id="main_nav" class="nav">
             	<li class="${empty category.id?'active':''}"><a href="${ctx}/index${fns:getUrlSuffix()}"><span>首  页</span></a></li>
				<c:forEach items="${fnc:getMainNavList(site.id)}" var="category" varStatus="status"><c:if test="${status.index lt 6}">
					<c:choose>
		    			<c:when test="${not empty category.href}">
			    			<c:choose>
				    			<c:when test="${fn:indexOf(category.href, '://') eq -1}"><c:set var="url" value="${ctx}${category.href}"/></c:when>
				    			<c:otherwise><c:set var="url" value="${category.href}"/></c:otherwise>
				    		</c:choose>
		    			</c:when>
		    			<c:otherwise><c:set var="url" value="${ctx}/list-${category.id}${fns:getUrlSuffix()}"/></c:otherwise>
		    		</c:choose>
		    		<li class="${requestScope.category.id eq category.id||fn:indexOf(requestScope.category.parentIds,category.id) ge 1?'active':''}"><a href="${url}" target="${category.target}"><span>${category.name}</span></a></li>
		    	</c:if></c:forEach>
		    	<li id="themeSwitch" class="dropdown">
			       	<a class="dropdown-toggle" data-toggle="dropdown" href="#" title="主题切换"><i class="icon-th-large"></i></a>
				    <ul class="dropdown-menu">
				      <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
				    </ul>
				    <!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
			    </li>
            </ul>
            <form class="navbar-search pull-right" action="${ctx}/search" method="get">
              	<input type="text" name="q" maxlength="20" class="search-query span2" placeholder="全站搜索..." value="${q}">
            </form>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
	<div class="container">
		<div class="content">
			<sitemesh:body/>
		</div>
		<hr style="margin:20px 0 10px;">
		<footer>
			<div class="footer_nav"><a href="${ctx}/guestbook" target="_blank">公共留言</a> | <a href="${ctx}/search" target="_blank">全站搜索</a> | <a href="${ctx}/map${fns:getUrlSuffix()}" target="_blank">站点地图</a> | <a href="mailto:thinkgem@163.com">技术支持</a> | <a href="${pageContext.request.contextPath}${fns:getAdminPath()}" target="_blank">后台管理</a></div>
			<div class="pull-right">${fns:getDate('yyyy年MM月dd日 E')}</div><div class="copyright">${site.copyright}</div>
      	</footer>
    </div> <!-- /container -->
</body>
</html>