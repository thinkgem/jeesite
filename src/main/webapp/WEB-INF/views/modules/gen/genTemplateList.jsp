<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码模板管理</title>
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
		<li class="active"><a href="${ctx}/gen/genTemplate/">代码模板列表</a></li>
		<shiro:hasPermission name="gen:genTemplate:edit"><li><a href="${ctx}/gen/genTemplate/form">代码模板添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="genTemplate" action="${ctx}/gen/genTemplate/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>分类 ：</label><form:select path="category" class="input-medium">
			<form:option value=""></form:option>
			<form:options items="${fns:getDictList('gen_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</form:select>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<div id="messageBoxError" class="alert alert-error"><button data-dismiss="alert" class="close">×</button>
		代码模板管理，已废弃！模板管理改为XML配置方式，见  /src/main/java/com/thinkgem/jeesite/modules/gen/template 文件夹
	</div>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>分类</th><th>备注</th><shiro:hasPermission name="gen:genTemplate:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="genTemplate">
			<tr>
				<td><a href="${ctx}/gen/genTemplate/form?id=${genTemplate.id}">${genTemplate.name}</a></td>
				<td>${fns:getDictLabels(genTemplate.category, 'gen_category', '')}</td>
				<td>${fns:abbr(genTemplate.remarks, 100)}</td>
				<shiro:hasPermission name="gen:genTemplate:edit"><td>
    				<a href="${ctx}/gen/genTemplate/form?id=${genTemplate.id}">修改</a>
					<a href="${ctx}/gen/genTemplate/delete?id=${genTemplate.id}" onclick="return confirmx('确认要删除该代码模板吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
