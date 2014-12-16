<?xml version="1.0" encoding="utf-8"?>
<template>
	<name>viewForm</name>
	<filePath>src/main/webapp/WEB-INF/views/${lastPackageName}/${moduleName}/${subModuleName}</filePath>
	<fileName>${className}Form.jsp</fileName>
	<content><![CDATA[
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${functionNameSimple}管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${r"${ctx}"}/${urlPrefix}/">${functionNameSimple}列表</a></li>
		<li class="active"><a href="${r"${ctx}"}/${urlPrefix}/form?id=${"${"+className+".id}"}&parent.id=${"${"+className+"parent.id}"}">${functionNameSimple}<shiro:hasPermission name="${permissionPrefix}:edit">${r"${not empty "+className+".id?'修改':'添加'}"}</shiro:hasPermission><shiro:lacksPermission name="${permissionPrefix}:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="${className}" action="${r"${ctx}"}/${urlPrefix}/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${r"${message}"}"/>		
		<#list table.columnList as c>
			<#if c.simpleJavaField == 'parent'>
		<div class="control-group">
			<label class="control-label">上级${c.comments}:</label>
			<div class="controls">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="${c.comments}" url="/${urlPrefix}/treeData" extId="${'$'}{${className}.id}" cssClass="" allowClear="true"/>
			</div>
		</div>
			<#elseif c.isEdit?? && c.isEdit == "1" && (c.isNotBaseField || c.simpleJavaField == 'remarks') && c.javaFieldId != 'parentIds'>
		<div class="control-group">
			<label class="control-label">${c.comments}：</label>
			<div class="controls">
			<#if c.showType == "input">
				<form:input path="${c.javaFieldId}" htmlEscape="false"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xlarge <#if c.isNull != "1">required</#if><#if c.javaType == "Long" || c.javaType == "Integer"> digits</#if><#if c.javaType == "Double"> number</#if>"/>
			<#elseif c.showType == "textarea">
				<form:textarea path="${c.javaFieldId}" htmlEscape="false" rows="4"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xxlarge <#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "select">
				<form:select path="${c.javaFieldId}" class="input-xlarge <#if c.isNull != "1">required</#if>">
					<form:option value="" label=""/>
					<form:options items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<#elseif c.showType == "checkbox">
				<form:checkboxes path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false" class="<#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "radiobox">
				<form:radiobuttons path="${c.javaFieldId}" items="${"$"}{fns:getDictList('${c.dictType}')}" itemLabel="label" itemValue="value" htmlEscape="false" class="<#if c.isNull != "1">required</#if>"/>
			<#elseif c.showType == "dateselect">
				<input name="${c.javaFieldId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate <#if c.isNull != "1">required</#if>"
					value="<fmt:formatDate value="${"$"}{${className}.${c.javaFieldId}}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<#elseif c.showType == "userselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "officeselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "areaselect">
				<sys:treeselect id="${c.simpleJavaField}" name="${c.javaFieldId}" value="${"$"}{${className}.${c.javaFieldId}}" labelName="${c.javaFieldName}" labelValue="${"$"}{${className}.${c.javaFieldName}}"
					title="区域" url="/sys/area/treeData" cssClass="<#if c.isNull != "1">required</#if>" allowClear="true" notAllowSelectParent="true"/>
			<#elseif c.showType == "fileselect">
				<form:hidden id="${c.simpleJavaField}" path="${c.javaFieldId}" htmlEscape="false"<#if c.dataLength != "0"> maxlength="${c.dataLength}"</#if> class="input-xlarge"/>
				<sys:ckfinder input="${c.simpleJavaField}" type="files" uploadPath="/${moduleName}<#if subModuleName != "">/${subModuleName}</#if>/${className}" selectMultiple="true"/>
			</#if>
			<#if c.isNull != "1">
				<span class="help-inline"><font color="red">*</font> </span>
			</#if>
			</div>
		</div>
			</#if>
		</#list>
		<div class="form-actions">
			<shiro:hasPermission name="${permissionPrefix}:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>]]>
	</content>
</template>