<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					//loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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
		<li><a href="${ctx}/prj/project/">项目列表</a></li>
		<li><a href="${ctx}/prj/project/form?id=${project.id}">项目<shiro:hasPermission name="prj:project:edit">${not empty project.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="prj:project:edit">查看</shiro:lacksPermission></a></li>
		<li class="active"><a href="${ctx}/prj/project/generate?id=${project.id}">代码生成</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="project" action="${ctx}/prj/project/generate" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				${project.name}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数据文件:</label>
			<div class="controls">
				<c:forEach items="${project.ermPathList}" var="ermPath">
					<a target="_blank" href="${ermPath}">${fns:substringAfterLast(ermPath,"/")}</a>&nbsp;&nbsp;
				</c:forEach>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">包名:</label>
			<div class="controls">
				${project.rootPackage}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				${project.remarks}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模板类型:</label>
			<div class="controls">
				<form:select path="templateType" class="required">
					<option value=""></option>
					<form:options items="${fns:getDictList('prj_template_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="prj:project:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="生成"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
