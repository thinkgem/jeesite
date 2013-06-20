<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${article.title} - ${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${article.description} ${category.description}" />
	<meta name="keywords" content="${article.keywords} ${category.keywords}" />
	<script type="text/javascript">
		$(document).ready(function() {
			if ("${category.allowComment}"=="1" && "${article.articleData.allowComment}"=="1"){
				$("#comment").show();
				page(1);
			}
		});
		function page(n,s){
			$.get("${ctx}/comment",{theme: '${category.site.theme}', 'category.id': '${category.id}',
				contentId: '${article.id}', title: '${article.title}', pageNo: n, pageSize: s, date: new Date().getTime()
			},function(data){
				$("#comment").html(data);
			});
		}
	</script>
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
	     <div class="row">
	       <div class="span10">
			<h3 style="color:#555555;font-size:20px;text-align:center;border-bottom:1px solid #ddd;padding-bottom:15px;margin:25px 0;">${article.title}</h3>
			<c:if test="${not empty article.description}"><div>摘要：${article.description}</div></c:if>
			<div>${article.articleData.content}</div>
			<div style="border-top:1px solid #ddd;padding:10px;margin:25px 0;">发布者：${article.createBy.name} &nbsp; 点击数：${article.hits} &nbsp; 发布时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> &nbsp; 更新时间：<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
  	       </div>
  	     </div>
	     <div class="row">
			<div id="comment" class="hide span10">
				正在加载评论...
			</div>
	     </div>
	     <div class="row">
	       <div class="span10">
			<h5>相关文章</h5>
			<ol><c:forEach items="${relationList}" var="relation">
				<li style="float:left;width:230px;"><a href="${ctx}/view-${relation[0]}-${relation[1]}${urlSuffix}">${fns:abbr(relation[2],30)}</a></li>
			</c:forEach></ol>
	  	  </div>
  	    </div>
  	  </div>
   </div>
</body>
</html>