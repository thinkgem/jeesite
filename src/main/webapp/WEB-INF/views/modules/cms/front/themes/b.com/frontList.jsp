<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${category.description}" />
	<meta name="keywords" content="${category.keywords}" />
</head>
<body>
	<div class="row">
	   <div class="span2">
	  	 <h4>栏目列表</h4>
		 <ol><c:forEach items="${categoryList}" var="category">
			<li><a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a></li>
		 </c:forEach></ol>
		 <h4>推荐阅读</h4>
		 <ol><c:forEach items="${fnc:getArticleList(site.id, category.id, 8, 'posid:2')}" var="article">
		 	<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbr(article.title,18)}</a></li>
		 </c:forEach></ol>
   	   </div>
	   <div class="span10">
		 <ul class="breadcrumb">
		   <li><strong>当前位置：</strong><a href="${ctx}">首页</a> <span class="divider">/</span></li>
		   <c:forEach items="${fnc:getCategoryListByIds(category.parentIds)}" var="category">
		     <c:if test="${category.id ne 1}"><li><a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a><span class="divider">/</span></li></c:if>
		   </c:forEach><li class="active">${category.name}</li>
		  </ul>
	   </div>
       <div class="span10">
		  <h4>${category.name}</h4>
		  <c:if test="${category.module eq 'article'}">
			<ul><c:forEach items="${page.list}" var="article">
				<li><span class="pull-right"><fmt:formatDate value="${article.updateDate}" pattern="yyyy.MM.dd"/></span><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" style="color:${article.color}">${fns:abbr(article.title,96)}</a></li>
			</c:forEach></ul>
			<div class="pagination">${page}</div>
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
   </div>
</body>
</html>