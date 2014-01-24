<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
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
		<li class="active"><a href="${ctx}/prj/project/">项目列表</a></li>
		<shiro:hasPermission name="prj:project:edit"><li><a href="${ctx}/prj/project/form">项目添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="project" action="${ctx}/prj/project/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>名称</th>
			<th>数据文件</th>
			<th>包名</th>
			<th>备注</th>
			<shiro:hasPermission name="prj:project:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="project">
			<tr>
				<td><a href="${ctx}/prj/project/form?id=${project.id}">${project.name}</a></td>
				<td><c:forEach items="${project.ermPathList}" var="ermPath">
						<a target="_blank" href="${ermPath}">${fns:substringAfterLast(ermPath,"/")}</a>&nbsp;&nbsp;
				</c:forEach></td>
				<td>${project.rootPackage }</td>
				<td>${project.remarks}</td>
				<shiro:hasPermission name="prj:project:edit"><td>
    				<a href="${ctx}/prj/project/generate?id=${project.id}">代码生成</a>
    				<a href="${ctx}/prj/project/form?id=${project.id}">修改</a>
					<a href="${ctx}/prj/project/delete?id=${project.id}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
