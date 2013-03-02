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
	<div style="float:left;*margin-top:20px;margin-left:20px;">
		<h4>${category.name}</h4>
		<c:if test="${category.module eq 'article'}">
			<ul><c:forEach items="${page.list}" var="article">
				<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbreviate(article.title,20)}</a> <span>[<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd"/>]</span></li>
			</c:forEach></ul>
			<div class="page">${page}</div>
			<script type="text/javascript">
				function page(n,s){
					location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+n+"&pageSize="+s;
				}
			</script>
		</c:if>
		<c:if test="${category.module eq 'link'}">
			<ul><c:forEach items="${page.list}" var="link">
				<li><a href="${link.href}" target="_blank" style="color:${link.color}"><c:out value="${link.title}" /></a></li>
			</c:forEach></ul>
		</c:if>
	</div>
	<div style="clear:both;"></div>
</body>
</html>
