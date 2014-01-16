<#assign columnList = sourceTable.sourceColumnList>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${sourceTable.logicalName}管理</title>
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
		<li class="active"><a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/">${sourceTable.logicalName}列表</a></li>
		<shiro:hasPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit"><li><a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/form">${sourceTable.logicalName}添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="${sourceTable.entityClassName?uncap_first}" action="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/" method="post" class="breadcrumb form-search">
	
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${f.dollar}{message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<#list columnList as column>
			<#if (column.sourceRelationList?size==0)>
			<td>${column.logicalName}</td>
			</#if>
		</#list>
		<#if sourceTable.entityExtendType =='DataEntity'>
			<td>创建时间</td>
			<td>更新时间</td>
			<td>备注</td>
		</#if>
			<shiro:hasPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${f.dollar}{page.list}" var="${sourceTable.entityClassName?uncap_first}">
			<tr>
		<#list columnList as column>
			<#if (column.sourceRelationList?size==0)>
			<td>${f.dollar}{${sourceTable.entityClassName?uncap_first}.${column.instance?uncap_first}}</td>
			</#if>
		</#list>
		<#if sourceTable.entityExtendType =='DataEntity'>
			<td><fmt:formatDate value="${f.dollar}{${sourceTable.entityClassName?uncap_first}.createDate}" pattern="yyyy-MM-dd HH:mm"/></td>
			<td><fmt:formatDate value="${f.dollar}{${sourceTable.entityClassName?uncap_first}.updateDate}" pattern="yyyy-MM-dd HH:mm"/></td>
			<td>${f.dollar}{${sourceTable.entityClassName?uncap_first}.remarks}</td>
		</#if>
				<shiro:hasPermission name="${sourceTable.moduleName}:${sourceTable.entityClassName?uncap_first}:edit"><td>
    				<a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/form?id=${f.dollar}{${sourceTable.entityClassName?uncap_first}.id}">修改</a>
					<a href="${f.dollar}{ctx}/${sourceTable.moduleName}/${sourceTable.entityClassName?uncap_first}/delete?id=${f.dollar}{${sourceTable.entityClassName?uncap_first}.id}" onclick="return confirmx('确认要删除该${sourceTable.logicalName}吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${f.dollar}{page}</div>
</body>
</html>