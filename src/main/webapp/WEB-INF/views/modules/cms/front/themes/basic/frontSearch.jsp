<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>全站搜索</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${site.desciption}" />
	<meta name="keywords" content="${site.keywords}" />
	<script src="${ctxStatic}/jquery/jquery.highlight.min.js" type="text/javascript"></script>
	<style type="text/css">
		form.search{margin:10px 12px 12px;}
		form.search .txt{padding:6px;font-size:16px;width:300px;}
		form.search .sel{font-size:16px;}
		form.search .act{font-weight:bold;}
		form.search .btn{padding:6px 18px;*padding:4px;font-size:16px;}
		dl.search{line-height:25px;border-bottom:1px solid #efefef;}
		dl.search dt{border-top:1px solid #efefef;padding:8px 15px 0px;font-size:16px;}
		dl.search dt a.title{color:#0000cc;text-decoration:underline;}
		dl.search dd{margin:0 15px 10px;font-size:14px;color:#555}
		dl.search dd span{font-size:12px;color:#008000;}
		dl.search .highlight{color:#DF0037;}
		dl.search dd span.highlight{color:#DF0037;font-size:14px;}
		dl.search dd span.info span.highlight{color:#DF0037;font-size:13px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function(){
			<c:if test="${not empty message}">alert("${message}");</c:if>
			var qs = "${q}".replace(",", " ").replace("，", " ")
				.replace(";", " ").replace("；", " ").split(" ");
			for (i=0; i<qs.length; i++){
				$("dl").highlight(qs[i]);
			}
		});
	</script>
</head>
<body>
	<form:form id="searchForm" method="get" class="search">
		<input type="hidden" id="pageNo" name="pageNo" value="${pageNo}"/>
		<input type="hidden" id="pageSize" name="pageSize" value="${pageSize}"/>
		<input type="hidden" id="t" name="t" value="${not empty t?t:'article'}"/><%--
		<p class="sel"><c:forEach items="${fns:getDictList('cms_search')}" var="type">
			<a href="javascript:" onclick="$('#t').val('${type.value}');$('.sel a').removeClass('act');$(this).addClass('act')" class="${(empty t && type.value eq 'article') || t eq type.value?'act':''}">${type.label}</a>
		</c:forEach></p> --%>
		<input type="text" name="q" value="${q}" class="txt"/>
		<input type="submit" value="搜  索" class="btn"/>
	</form:form>
	<dl class="search"><c:forEach items="${page.list}" var="article">
		<dt><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}" class="title" target="_blank">${fns:abbreviate(article.title,80)}</a></dt>
		<dd>${article.desciption}<span class="info"><br/>${article.keywords} [<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>]</span><br/>
	</c:forEach>
	<c:if test="${fn:length(page.list) eq 0}">
		<dt><c:if test="${empty q}">请键入要查找的关键字。</c:if><c:if test="${not empty q}">抱歉，没有找到与“${q}”相关内容。</c:if><br/><br/>建议：</dt>
		<dd><ul><li>检查输入是否正确；</li><li>简化输入词；</li><li>尝试其他相关词，如同义、近义词等。</li></ul></dd>
	</c:if></dl>
	<div class="page">${page}</div>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</body>
</html>
