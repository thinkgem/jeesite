<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/cms/category/updateSort");
	    	$("#listForm").submit();
    	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/category/">栏目列表</a></li>
		<shiro:hasPermission name="cms:category:edit"><li><a href="${ctx}/cms/category/form">栏目添加</a></li></shiro:hasPermission>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>栏目名称</th><th>归属机构</th><th>栏目模型</th><th style="text-align:center;">排序</th><th title="是否在导航中显示该栏目">导航菜单</th><th title="是否在分类页中显示该栏目的文章列表">栏目列表</th><th>展现方式</th><th>操作</th></tr>
			<c:forEach items="${list}" var="category">
				<tr id="${category.id}" pId="${category.parent.id ne 1?category.parent.id:'0'}">
					<td><a href="${ctx}/cms/category/form?id=${category.id}">${category.name}</a></td>
					<td>${category.office.name}</td>
					<td>${fns:getDictLabel(category.module, 'cms_module', '公共模型')}</td>
					<td style="text-align:center;">
						<shiro:hasPermission name="cms:category:edit">
							<input type="hidden" name="ids" value="${category.id}"/>
							<input name="sorts" type="text" value="${category.sort}" style="width:50px;margin:0;padding:0;text-align:center;">
						</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">
							${category.sort}
						</shiro:lacksPermission>
					</td>
					<td>${fns:getDictLabel(category.inMenu, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(category.inList, 'show_hide', '隐藏')}</td>
					<td>${fns:getDictLabel(category.showModes, 'cms_show_modes', '默认展现方式')}</td>
					<td>
						<a href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${category.id}${fns:getUrlSuffix()}" target="_blank">访问</a>
						<shiro:hasPermission name="cms:category:edit">
							<a href="${ctx}/cms/category/form?id=${category.id}">修改</a>
							<a href="${ctx}/cms/category/delete?id=${category.id}" onclick="return confirmx('要删除该栏目及所有子栏目项吗？', this.href)">删除</a>
							<a href="${ctx}/cms/category/form?parent.id=${category.id}">添加下级栏目</a> 
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</table>
		<shiro:hasPermission name="cms:category:edit"><div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div></shiro:hasPermission>
	</form>
</body>
</html>