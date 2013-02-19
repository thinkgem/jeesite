<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${article.title} - ${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${article.desciption} ${category.desciption}" />
	<meta name="keywords" content="${article.keywords} ${category.keywords}" />
	<script type="text/javascript">
		$(document).ready(function() {
			if ("${category.allowComment}"=="1" && "${article.articleData.allowComment}"=="1"){
				$("#comment").show();
				page(1);
			}
		});
		function page(i){
			$.get("${ctx}/comment",{theme: '${category.site.theme}', module: '${category.module}',
				contentId: '${article.id}', title: '${article.title}', pageNo: i, date: new Date().getTime()
			},function(data){
				$("#comment").html(data);
			});
		}
	</script>
</head>
<body>
	<div style="float:left;*margin-top:20px;width:20%;">
	  	<h4>栏目列表</h4>
		<ol><c:forEach items="${categoryList}" var="category">
			<li><a href="${ctx}/list-${category.id}${urlSuffix}">${category.name}</a></li>
		</c:forEach></ol>
	</div>
	<div style="float:left;*margin-top:20px;margin-left:20px;width:75%;">
		<h3 style="color: #DF0037;font-size:22px;text-align:center;border-bottom:1px solid #ddd;padding-bottom:15px;margin:25px 0;">${article.title}</h3>
		<c:if test="${not empty article.desciption}"><p>摘要：${article.desciption}  </p></c:if>
		<p>${article.articleData.content}</p>
		<div style="border-top:1px solid #ddd;padding:10px;margin:25px 0;">发布者：${article.user.name} &nbsp; 点击数：${article.hits} &nbsp; 发布时间：<fmt:formatDate value="${article.inputDate}" pattern="yyyy-MM-dd HH:mm:ss"/> &nbsp; 更新时间：<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
		<div id="comment" class="hide">
			正在加载评论...
		</div>
	</div>
	<div style="clear:both;"></div>
</body>
</html>
