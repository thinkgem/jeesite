<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>站点切换</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/site/select">站点切换</a></li>
	</ul>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><shiro:hasPermission name="cms:view"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${fnc:getSiteList()}" var="site">
			<tr>
				<td><a href="${ctx}/cms/site/select?id=${site.id}">${site.name}</a> ${fnc:getCurrentSiteId() eq site.id ? ' <font color="red">[当前站点]</font>' : ''}</td>
				<shiro:hasPermission name="cms:view"><td>
    				<a href="${ctx}/cms/site/select?id=${site.id}">切换</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>