<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${category.name}</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="${category.description}" />
	<meta name="keywords" content="${category.keywords}" />
	<link href="${ctxStaticTheme}/wz/css/wz.css"  rel="stylesheet" type="text/css">
</head>
<body>
	<div id="ui-header_xiao">
		<div class="ui-btn-left" onclick="dourl('${ctx}/index-${site.id}${fns:getUrlSuffix()}')"></div>
		<a class="ui-btn-right" href="javascript:window.location.reload();"></a>
	</div>
	<div class="Listpage">
		<div class="top46"></div>
		<div class="focus">
			<ul>
				<li><div style="max-height:250px;"><img src="${category.image}" ></div>
					<div class="opacity"></div>
					<h2>${category.name}</h2>
				</li>
			</ul>
		</div>
		<div id="todayList">
			<ul class="todayList">
				<c:if test="${category.module eq 'article'}">
					<c:forEach items="${page.list}" var="article">
						<li class="only1_xiao" onclick="dourl('${article.url}')">
							<h2>${fns:abbr(article.title,96)}</h2>
							<div class="img">
								<img style="width:72px;height:auto;max-height:55px;" src="${article.image}" >
							</div>
							<p class="onlyheight_60">${article.description} <fmt:formatDate value="${article.updateDate}" pattern="yyyy.MM.dd"/></p>
							<div class="commentNum"></div>
						</li>
					</c:forEach>
				</c:if>
			</ul>
			<div class="page pagination pagination-mini">${page}</div>
			<script type="text/javascript">
				function page(n,s){
					location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+n+"&pageSize="+s;
				}
			</script>
		</div>
	</div>
	<script>
		function dourl(url){
			location.href= url;
		}
	</script>
</body>
</html>