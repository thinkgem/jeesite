<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${category.desciption}" />
	<meta name="keywords" content="${category.keywords}" />
</head>
<body>
	<div style="float:left;*margin-top:20px;width:20%;">
		<h4>栏目列表</h4>
		<ol><c:forEach items="${categoryList}" var="category">
			<li><a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a></li>
		</c:forEach></ol>
	</div>
	<div style="float:left;margin-left:20px;width:75%;">
		<c:forEach items="${categoryMap}" var="map" varStatus="status">
			<div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
				<h4>${map.key.name} <small><a href="${ctx}/list-${map.key.id}${urlSuffix}">更多>></a></small></h4>
				<c:if test="${map.key.module eq 'article'}">
					<ul><c:forEach items="${map.value}" var="article">
						<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbreviate(article.title,20)}</a> <span>[<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/>]</span></li>
					</c:forEach></ul>
				</c:if>
				<c:if test="${map.key.module eq 'link'}">
					<ul><c:forEach items="${map.value}" var="link">
						<li><a href="${link.href}" target="_blank" style="color:${link.color}"><c:out value="${link.title}" /></a></li>
					</c:forEach></ul>
				</c:if>
			</div>
			<c:if test="${status.index % 2 ne 0}"><div style="clear:both;"></div></c:if>
		</c:forEach>
	</div>
	<div style="clear:both;"></div>
</body>
</html>
