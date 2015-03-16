<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
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
		<li><a href="${ctx}/test/test/">硕正列表</a></li>
		<li class="active"><a href="${ctx}/test/test/form?id=${test.id}">组件<shiro:hasPermission name="test:test:edit">${not empty test.id?'修改':'表单'}</shiro:hasPermission><shiro:lacksPermission name="test:test:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="test" action="${ctx}/test/test/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">下拉框:</label>
			<div class="controls">
				<form:select path="name" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('act_category')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单选框:</label>
			<div class="controls">
				<form:radiobuttons path="name" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">复选框:</label>
			<div class="controls">
				<form:checkboxes path="name" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">选人，选部门，选区域:</label>
			<div class="controls">
                <sys:treeselect id="user" name="user.id" value="${user.user.id}" labelName="user.name" labelValue="${user.office.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="required"  allowClear="true" notAllowSelectParent="true"/>
                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="required" notAllowSelectParent="true"/>
				<sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required"/><br/>多选：<br/>
                <sys:treeselect id="user2" name="user.id" value="${user.user.id}" labelName="user.name" labelValue="${user.office.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="required" notAllowSelectParent="true" checked="true"/>
                <sys:treeselect id="office2" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="required" notAllowSelectParent="true" checked="true"/>
				<sys:treeselect id="company2" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required" notAllowSelectParent="true" checked="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传图片、文件:</label>
			<div class="controls">
				<form:hidden id="nameImage" path="name" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/test/test" selectMultiple="false"/>
				<form:hidden id="nameFiles" path="name" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="nameFiles" type="files" uploadPath="/test/test" selectMultiple="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">富文本编辑器:</label>
			<div class="controls">
				<form:textarea id="remarks" htmlEscape="true" path="remarks" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="remarks" uploadPath="/test/test" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">多行文本框:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">验证码:</label>
			<div class="controls">
				<label class="input-label mid" for="validateCode">验证码</label>
				<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">JSTL、EL:</label>
			<div class="controls">
				日期格式：<fmt:formatDate value="${test.createDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>
				<input id="createDate" name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${test.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<br/>
				当前用户：${fns:getUser().name}<br/>
				当前用户缓存：${fns:getCache('menuList','')[0]}<br/>
				当前用户部门：${fns:getOfficeList()[0]}<br/>
				字典取值：[${fns:getDictLabel('0', 'yes_no', '未知')}, ${fns:getDictLabel('1', 'yes_no', '未知')}] 
				字典取列表：${fns:getDictList('yes_no')}<br/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">CSS、JS:</label>
			<div class="controls">
				<a class="btn" href="${ctxStatic}/bootstrap/2.3.1/docs/index.html">Bootstrap</a>
				<a class="btn" href="http://ivaynberg.github.io/select2/">多功能下拉框组件</a>
				<a class="btn" href="${ctxStatic}/jquery-ztree/3.5.12/demo/cn/index.html">树结构组件</a>
				<input class="btn" type="button" value="对话框" onclick="top.$.jBox('iframe:${ctxStatic}/jquery-jbox/2.3/docs/jbox-demo.html',{title: '对话框',width: 950,height: 600,buttons: { '关闭': true }});"/>
				<input class="btn" type="button" value="客户端验证" onclick="top.$.jBox('iframe:${ctxStatic}/jquery-validation/1.11.0/demo/index.html',{title: '客户端验证',width: 950,height: 600,buttons: { '关闭': true }});"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="test:test:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
