<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="blank"/>
	<style type="text/css">
      html,body,table{background-color:#3E75C0;width:100%;text-align:center;}.form-signin-heading{font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:absolute;text-align:left;width:300px;padding:25px 29px 29px;margin:0 auto 20px;border:0;background:none;box-shadow:none;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#fff;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin .btn.btn-large{font-size:16px;} .form-signin #themeSwitch{position:absolute;right:15px;bottom:10px;}
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:80px;padding-top:20px;} .footer{position:absolute;} .footer, .footer a{color:#779ACA}
      .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;width:270px;font-weight:normal;color:inherit;margin:0;}
      body{background:url(${ctxStatic}/images/login_bg.jpg) no-repeat center center;}
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
			$(window).resize();
		});
		// 如果在框架或在对话框中，则弹出提示并跳转到首页
		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
			alert('未登录或登录超时。请重新登录，谢谢！');
			top.location = "${ctx}";
		}
		$(window).resize(function(){
			$('body').height($(window).height());
			var top = $('body').height() / 2 - $('.form-signin').height() / 2 - 39;
			var left = $('body').width() / 2 - $('.form-signin').width() / 2 - 14;
			$('.form-signin').css({'top':top,'left':left});
			$('.footer').css({'top':top+250,'left':left+30});
		});
	</script>
</head>
<body>
	<div class="header">
		<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
			<label id="loginError" class="error">${message}</label>
		</div>
	</div>
	<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		<label class="input-label" for="username">登录名</label>
		<input type="text" id="username" name="username" class="input-block-level required" value="${username}">
		<label class="input-label" for="password">密码</label>
		<input type="password" id="password" name="password" class="input-block-level required">
		<c:if test="${isValidateCodeLogin}"><div class="validateCode">
			<label class="input-label mid" for="validateCode">验证码</label>
			<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
		</div></c:if>
		<input class="btn btn-large btn-primary" type="submit" value="登 录"/>&nbsp;&nbsp;
		<label for="rememberMe" title="下次不需要再登录" style="color:#fff"><input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}/> 记住我（公共场所慎用）</label>
		<div id="themeSwitch" class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#" style="color:#fff">${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}<b class="caret"></b></a>
			<ul class="dropdown-menu">
			  <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
			</ul>
		</div>
	</form>
	<div class="footer">
		Copyright &copy; 2012-${fns:getConfig('copyrightYear')}  - Powered By <a href="http://jeesite.com" target="_blank">JeeSite</a> ${fns:getConfig('version')}
	</div>
	<script src="${ctxStatic}/flash/zoom.min.js" type="text/javascript"></script>
</body>
</html>