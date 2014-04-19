<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Jeesite Mobile 登录</title>
		<%@include file="/WEB-INF/views/mobile/include/head.jsp" %>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#loginForm").validate({
					rules: {
						validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
					},
					messages: {
						username: {required: "请填写用户名."},password: {required: "请填写密码."},
						validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
					},
					errorLabelContainer: "#messageBox",
					errorPlacement: function(error, element) {
						error.appendTo($("#loginError").parent());
					} 
				});
			});
		</script>
	</head>
	<body>
	<div data-role="page">
		<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
		<div data-role="header"  data-theme="b">
			<h1>Jeesite 登陆</h1>
		</div><!-- /header -->
		<div role="main" class="ui-content">
			<div id="messageBox" class="<%=error==null?"hide":""%>">
				<label id="loginError" class="error"><%=error==null?"":"com.thinkgem.jeesite.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
			</div>
			<form id="loginForm"  action="${ctx}/login" method="post" data-ajax="false">
				<div data-role="fieldcontain">
					<label for="username">用户名：</label> <input type="text" name="username" id="username"  value="${username}" class="required"/>
				</div>
				<div data-role="fieldcontain">
					<label for="password">密　码：</label> <input type="password" name="password" id="password" value="" class="required"/>
				</div>
				<c:if test="${isValidateCodeLogin}"><div class="validateCode">
					<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
				</div></c:if>
				<input type="hidden" name="rememberMe" value="1"> 
				<input type="submit" id="loginButton" value="登录" />
			</form>
		</div><!-- /content -->
		<div data-role="footer"  data-theme="b">
			<h4>Copyright 2013 The Jeesite Foundation</h4>
		</div><!-- /footer -->
	</div><!-- /page -->
	</body>
</html>