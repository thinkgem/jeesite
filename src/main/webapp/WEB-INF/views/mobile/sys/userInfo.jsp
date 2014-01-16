<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default_mb"/>
	<script type="text/javascript"> 
	$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element.parent());
					}
				}
			});
		});
	</script>
</head>
<body>
	<form:form id="inputForm"  modelAttribute="user" action="${ctx}/sys/user/info" method="post" >
		<tags:mbmessage content="${message}"/>
		<div data-role="fieldcontain">
	         <label>归属公司:</label>
	         <label>${user.company.name}</label>
		</div>
		<div data-role="fieldcontain">
	         <label>归属部门:</label>
	         <label>${user.office.name}</label>
		</div>
		<div data-role="fieldcontain">
	          <label for="name">姓名:</label>
				<form:input path="name" htmlEscape="false" maxlength="50" class="required" readonly="true"/>
		</div>
		<div data-role="fieldcontain">
	          <label for="email">邮箱:</label>
				<form:input path="email" htmlEscape="false" maxlength="50" class="email"/>
		</div>
		<div data-role="fieldcontain">
	          <label for="phone">电话:</label>
				<form:input path="phone" htmlEscape="false" maxlength="50"/>
		</div>
		<div data-role="fieldcontain">
	          <label for="mobile">手机:</label>
				<form:input path="mobile" htmlEscape="false" maxlength="50"/>
		</div>
		<div data-role="fieldcontain">
	          <label for="remarks">备注:</label>
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
		</div>
		<div data-role="fieldcontain">
	          <label>用户角色:</label>
	         <label>${user.roleNames}</label>
		</div>
		<div data-role="fieldcontain">
	          <label>最后登陆:</label>
	         <label>IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
		</div>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
	</form:form>
</body>
</html>