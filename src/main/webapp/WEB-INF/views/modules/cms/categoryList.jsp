<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目管理</title>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
		});
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
		<li class="active"><a href="${ctx}/cms/category/">栏目列表</a></li>
		<shiro:hasPermission name="cms:category:edit"><li><a href="${ctx}/cms/category/form">栏目添加</a></li></shiro:hasPermission>
	</ul>
	<tags:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<tr><th>栏目名称</th><th>栏目模型</th><th>排序</th><th title="是否在导航中显示该栏目">导航菜单</th><th title="是否在分类页中显示该栏目的文章列表">栏目列表</th><th>展现方式</th><shiro:hasPermission name="cms:category:edit"><th>操作</th></shiro:hasPermission></tr>
		<c:forEach items="${list}" var="category">
			<tr id="${category.id}" pId="${category.parent.id ne 1?category.parent.id:'0'}">
				<td><a href="${ctx}/cms/category/form?id=${category.id}">${category.name}</a></td>
				<td>${fns:getDictLabel(category.module, 'cms_module', '公共模型')}</td>
				<td>${category.sort}</td>
				<td>${fns:getDictLabel(category.inMenu, 'show_hide', '隐藏')}</td>
				<td>${fns:getDictLabel(category.inList, 'show_hide', '隐藏')}</td>
				<td>${fns:getDictLabel(category.showModes, 'cms_show_modes', '默认展现方式')}</td>
				<shiro:hasPermission name="cms:category:edit"><td>
					<a href="${ctx}/cms/category/form?id=${category.id}">修改</a>
					<a href="${ctx}/cms/category/delete?id=${category.id}" onclick="return confirmx('要删除该栏目及所有子栏目项吗？', this.href)">删除</a>
					<a href="${ctx}/cms/category/form?parent.id=${category.id}">添加下级栏目</a> 
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
