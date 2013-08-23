<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>站点地图</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${site.description}" />
	<meta name="keywords" content="${site.keywords}" />
	<style type="text/css">
		dl.map{border:1px solid #efefef;border-top:0;margin:10px 8px 8px;}
		dl.map dt{border-top:1px solid #efefef;padding:10px 15px;}
		dl.map dd{margin:10px 30px 20px;}
		dl.map span{border:1px solid #efefef;padding:8px 10px;}
		dl.map span:hover{border:1px solid #bbb;}
		dl.map span a:hover{text-decoration:none;color:#333;}
	</style>
</head>
<body>
	<dl class="map"><c:forEach items="${fnc:getMainNavList(site.id)}" var="category">
		<dt>
			<c:choose>
    			<c:when test="${not empty category.href}">
	    			<c:choose>
		    			<c:when test="${fn:indexOf(category.href, '://') eq -1}"><c:set var="url" value="${ctx}${category.href}"/></c:when>
		    			<c:otherwise><c:set var="url" value="${category.href}"/></c:otherwise>
		    		</c:choose>
    			</c:when>
    			<c:otherwise><c:set var="url" value="${ctx}/list-${category.id}${urlSuffix}"/></c:otherwise>
    		</c:choose>
    		<a href="${url}" target="_blank">${category.name}</a>
		<dd>
			<c:forEach items="${fnc:getCategoryList(site.id, category.id, -1, '')}" var="category">
				<c:choose>
	    			<c:when test="${not empty category.href}">
		    			<c:choose>
			    			<c:when test="${fn:indexOf(category.href, '://') eq -1}"><c:set var="url" value="${ctx}${category.href}"/></c:when>
			    			<c:otherwise><c:set var="url" value="${category.href}"/></c:otherwise>
			    		</c:choose>
	    			</c:when>
	    			<c:otherwise><c:set var="url" value="${ctx}/list-${category.id}${urlSuffix}"/></c:otherwise>
	    		</c:choose>
	    		<span><a href="${url}" target="_blank">${category.name}</a></span>
			</c:forEach>
		</dd>
	</c:forEach></dl>
</body>
</html>