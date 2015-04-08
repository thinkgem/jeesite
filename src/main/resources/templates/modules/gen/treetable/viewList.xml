<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>viewList</name>
	<filePath>src/main/webapp/WEB-INF/views/${lastPackageName}/${moduleName}/${subModuleName}</filePath>
	<fileName>${className}List.jsp</fileName>
	<content><![CDATA[
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionNameSimple}管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${"$"}{fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
						rootIds.push(data[i].parentId);
					}
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 5});
		});
		function addRow(list, tpl, data, pid, root){
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${"$"}{fns:jsGetVal('row.parentId')}) == pid){
					$(list).append(Mustache.render(tpl, {
						dict: {
							<#list table.columnList as c>
								<#if c.isList?? && c.isList == "1" && (c.showType == "select" || c.showType == "checkbox" || c.showType == "radiobox")>
							${c.simpleJavaField}: getDictLabel(${"$"}{fns:toJson(fns:getDictList('${c.dictType}'))}, row.${c.simpleJavaField}),
								</#if>
							</#list>
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${r"${ctx}"}/${urlPrefix}/">${functionNameSimple}列表</a></li>
		<shiro:hasPermission name="${permissionPrefix}:edit"><li><a href="${r"${ctx}"}/${urlPrefix}/form">${functionNameSimple}添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="${className}" action="${r"${ctx}"}/${urlPrefix}/" method="post" class="breadcrumb form-search">
		<ul class="ul-form">
		<#list table.columnList as c>
			<#if c.isQuery?? && c.isQuery == "1">
			<li><label>${c.comments}：</label>
			<#if c.showType == "input" || c.showType == "textarea">
				<form:input path="${c.javaFieldId}" htmlEscape="false"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-medium"/>
			<#elseif c.showType == "select">
				<form:select path="${c.javaFieldId}" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<#elseif c.showType == "checkbox">
				<form:checkboxes path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			<#elseif c.showType == "radiobox">
				<form:radiobuttons path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			<#elseif c.showType == "dateselect" && c.queryType == "between">
				<input name="begin${c.simpleJavaField?cap_first}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${"$"}{${className}.begin${c.simpleJavaField?cap_first}}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="end${c.simpleJavaField?cap_first}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${"$"}{${className}.end${c.simpleJavaField?cap_first}}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<#elseif c.showType == "dateselect">
				<input name="${c.javaFieldId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${"$"}{${className}.${c.javaFieldId}}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<#elseif c.showType == "userselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "officeselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "areaselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</#if>
			</li>
			</#if>
		</#list>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${r"${message}"}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<#list table.columnList as c>
					<#if c.isList?? && c.isList == "1">
				<th>${c.comments}</th>
					</#if>
				</#list>
				<shiro:hasPermission name="${permissionPrefix}:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="treeTableList"></tbody>
	</table>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<#assign firstListField = true>
			<#list table.columnList as c>
				<#if c.isList?? && c.isList == "1">
			<td><#if firstListField><a href="${r"${ctx}"}/${urlPrefix}/form?id={{row.id}}"></#if>
			<#if c.showType == "select" || c.showType == "checkbox" || c.showType == "radiobox">
				{{dict.${c.simpleJavaField}}}
			<#elseif c.showType == "userselect" || c.showType == "officeselect" || c.showType == "areaselect">
				{{row.${c.javaFieldName}}}
			<#else>
				{{row.${c.javaFieldId}}}
			</#if>
			<#if firstListField></a></#if></td>
					<#assign firstListField = false>
				</#if>
			</#list>
			<shiro:hasPermission name="${permissionPrefix}:edit"><td>
   				<a href="${r"${ctx}"}/${urlPrefix}/form?id={{row.id}}">修改</a>
				<a href="${r"${ctx}"}/${urlPrefix}/delete?id={{row.id}}" onclick="return confirmx('确认要删除该${functionNameSimple}及所有子${functionNameSimple}吗？', this.href)">删除</a>
				<a href="${r"${ctx}"}/${urlPrefix}/form?parent.id={{row.id}}">添加下级${functionNameSimple}</a> 
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>]]>
	</content>
</template>