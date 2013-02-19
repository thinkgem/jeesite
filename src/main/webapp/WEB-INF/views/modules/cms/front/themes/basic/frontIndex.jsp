<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="JeeSite ${site.desciption}" />
	<meta name="keywords" content="JeeSite ${site.keywords}" />
</head>
<body>
	<div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
		<c:set var="article" value="${fnc:getArticle(2)}"/>
		<h4>${fns:abbreviate(article.title,20)}</h4><p>${fns:abbreviate(article.articleData.content,200)} [<a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}">详情</a>]</p>
	</div>
	<div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
		<h4>组织机构</h4>
		<ul><c:forEach items="${fnc:getArticleList(site.id, 2, 8, '')}" var="article">
			<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbreviate(article.title,20)}</a> <span>[<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/>]</span></li>
		</c:forEach></ul>
	</div>
	<div style="clear:both;"></div>
	<div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
		<h4>质量监督</h4>
		<ul><c:forEach items="${fnc:getArticleList(site.id, 6, 8, '')}" var="article">
			<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbreviate(article.title,20)}</a> <span>[<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/>]</span></li>
		</c:forEach></ul>
	</div>
	<div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
		<h4>政策法规</h4>
		<ul><c:forEach items="${fnc:getArticleList(site.id, 10, 8, '')}" var="article">
			<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbreviate(article.title,20)}</a> <span>[<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/>]</span></li>
		</c:forEach></ul>
	</div>
	<div style="clear:both;"></div>
	<h3>友情链接</h3>
	<c:forEach items="${fnc:getCategoryList(site.id, 18, 10, '')}" var="category" varStatus="status"><div style="float:left;*margin-top:20px;margin-left:20px;width:450px;">
		<h4>${category.name}</h4>
		<ul><c:forEach items="${fnc:getLinkList(site.id, category.id, 2, '')}" var="link">
			<li><a href="${link.href}" target="_blank" style="color:${link.color}"><c:out value="${link.title}" /></a></li>
		</c:forEach></ul>
	</div>
	<c:if test="${status.index % 2 ne 0}"><div style="clear:both;"></div></c:if></c:forEach>
	<div style="clear:both;"></div>
</body>
</html>
