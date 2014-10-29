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
					if ($("#ermPath").val()==""){
						top.$.jBox.tip('请选择ERMaster数据文件','warning');
					} else {
						loading('正在提交，请稍等...');
						form.submit();
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/prj/project/">项目列表</a></li>
		<li class="active"><a href="${ctx}/prj/project/form?id=${project.id}">项目<shiro:hasPermission name="prj:project:edit">${not empty project.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="prj:project:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="project" action="${ctx}/prj/project/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label" for="name">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数据文件:</label>
			<div class="controls">
				<form:hidden path="ermPath" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<tags:ckfinder input="ermPath" type="files" uploadPath="/prj/project" selectMultiple="true" />
				<span class="help-inline">ERMaster的数据文件，erm文件格式</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="rootPackage">包名:</label>
			<div class="controls">
				<form:input path="rootPackage" htmlEscape="false" maxlength="200" class="required"/>
				<span class="help-inline">例如com.thinkgem.jeesite</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="prj:project:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
