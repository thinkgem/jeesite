<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
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
					if (element.is(":checkbox")||element.is(":radio")){
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
		<li><a href="${ctx}/sys/area/">区域列表</a></li>
		<li class="active"><a href="form?id=${area.id}&parent.id=${area.parent.id}">区域<shiro:hasPermission name="sys:area:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:area:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级区域:</label>
			<div class="controls">
				<%--<tags:treeDialog id="area" title="区域" url="/sys/area/treeData" extId="${area.id}" parentIds="${area.parentIds}"/>
				<div class="input-append">
					<form:hidden id="areaId" path="parent.id" class="required"/>
					<form:input id="areaName" path="parent.name" htmlEscape="false" maxlength="50" readonly="true" 
						/><a data-toggle="modal" href="#areaDialog" data-keyboard="true" data-backdrop="true" class="btn">选择</a>
				</div> --%>
				<tags:treeselect id="area" name="parent.id" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"
					title="区域" url="/sys/area/treeData" extId="${area.id}" parentIds="${area.parentIds}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">编码:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:area:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
