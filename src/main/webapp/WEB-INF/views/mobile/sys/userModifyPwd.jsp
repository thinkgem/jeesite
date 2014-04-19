<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default_mb"/>
	<script type="text/javascript"> 
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
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
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post">
		<tags:mbmessage content="${message}"/>
		<form:hidden path="id"/>
		<div data-role="fieldcontain">
			<label  for="oldPassword">旧密码:</label>
			<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
		</div>
		<div data-role="fieldcontain">
			<label  for="newPassword">新密码:</label>
			<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
		</div>
		<div data-role="fieldcontain">
			<label  for="confirmNewPassword">确认新密码:</label>
			<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required"equalTo="#newPassword"/>
		</div>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
	</form:form>
</body>
</html>