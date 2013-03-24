<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>登录页</title>
	<meta name="decorator" content="default"/>
	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
		});
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
	</script>
</head>
<body>
	<table style="width:100%;height:500px"><tr><td align="center" valign="middle">
	<div id="header" style="width:500px;">
		<h1>JeeSite Admin</h1><br/>
		<div class="well"><br/>
			<form:form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
				<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME); if(error != null){%>
				<div id="message" class="alert alert-error"><button data-dismiss="alert" class="close">×</button>登录失败, 请重试.</div><%}%>
				<div class="control-group">
					<label class="control-label">登录名:</label>
					<div class="controls">
						<input type="text" id="username" name="username" maxlength="50" value="${username}" class="required input-medium"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">密码:</label>
					<div class="controls">
						<input type="password" id="password" name="password" maxlength="50"  class="required input-medium"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">&nbsp;</label>
					<div class="controls">
						<input id="submit" class="btn btn-primary" type="submit" value="登 录"/>&nbsp;
						<label for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</label>
					</div>
				</div>
			</form:form>
		</div>
		<div class="copyright">
            Copyright &copy; 2012-2013 <a href="http://thinkgem.iteye.com" target="_blank">ThinkGem</a> - Powered By <a href="https://github.com/thinkgem/jeesite" target="_blank">JeeSite</a> V1.0
        </div>
	</div></td></tr></table>
</body>
</html>
