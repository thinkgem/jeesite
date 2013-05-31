<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
      html,body,table{background-color:#f5f5f5;width:100%;height:500px;text-align:center;}.form-signin-heading{font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:relative;text-align:left;width:300px;padding:25px 29px 29px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#999;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin .btn.btn-large{font-size:16px;} .form-signin #themeSwitch{position:absolute;right:15px;bottom:10px;}
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:60px;padding-top:30px;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;padding:2px;font-weight:normal;color:inherit;margin:0;}
    </style>
	<script type="text/javascript">
		$(document).ready(function() {
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
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
	</script>
</head>
<body>
	  <div class="header"><%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
	    <div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
			<label id="loginError" class="error"><%=error==null?"":"com.thinkgem.jeesite.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
		</div>
	  </div>
      <h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
      <form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
        <label class="input-label" for="username">登录名</label>
        <input type="text" id="username" name="username" class="input-block-level required" value="${username}">
        <label class="input-label" for="password">密码</label>
        <input type="password" id="password" name="password" class="input-block-level required">
        <c:if test="${isValidateCodeLogin}"><div class="validateCode">
	        <label class="input-label mid" for="validateCode">验证码</label>
	        <tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
        </div></c:if>
        <input class="btn btn-large btn-primary" type="submit" value="登 录"/>&nbsp;&nbsp;
        <label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</label>
        <div id="themeSwitch" class="dropdown">
	       	<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(cookie.theme.value,'theme','')}<b class="caret"></b></a>
		    <ul class="dropdown-menu">
		      <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
		    </ul>
		    <!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
	     </div>
      </form>
      Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="http://thinkgem.iteye.com" target="_blank">ThinkGem</a> - Powered By <a href="https://github.com/thinkgem/jeesite" target="_blank">JeeSite</a> ${fns:getConfig('version')}
</body>
</html>