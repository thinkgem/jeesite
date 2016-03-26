<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${fns:getConfig('productName')} | 登录</title>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet"
          href="${pageContext.servletContext.contextPath}/static/adminlte/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/adminlte/dist/css/AdminLTE.min.css">
    <link rel="stylesheet"
          href="${pageContext.servletContext.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
</head>
<body class="hold-transition login-page">

<!--[if lte IE 6]><br/>
<div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a>
    <h4>温馨提示：</h4>
    <p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的
        Chrome、Firefox、Safari 等。</p></div><![endif]-->

<div class="header">
    <div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}">
        <button data-dismiss="alert" class="close">×</button>
        <label id="loginError" class="error">${message}</label>
    </div>
</div>

<div class="login-box">
    <div class="login-logo">
        <a href="#"><b>${fns:getConfig('productName')}</b></a>
    </div>
    <div class="login-box-body">
        <p class="login-box-msg">登陆</p>
        <form id="loginForm" action="${ctx}/login" method="post">
            <div class="form-group has-feedback">
                <input name="username" id="username" class="form-control" placeholder="用户名">
                <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" name="password" id="password" class="form-control"
                       placeholder="密码">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>

            <c:if test="${isValidateCodeLogin}">
                <div class="validateCode">
                    <label class="input-label mid" for="validateCode">验证码</label>
                    <sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
                </div>
            </c:if>

            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck" style="margin-left: 20px">
                        <label>
                            <input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}/>
                            记住我
                        </label>
                    </div>
                </div><!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">登陆</button>
                </div><!-- /.col -->
            </div>
        </form>
    </div><!-- /.login-box-body -->
</div><!-- /.login-box -->

<script src="${pageContext.servletContext.contextPath}/static/adminlte/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/static/adminlte/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script>
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });

        $("#loginForm").validate({
            rules: {
                validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
            },
            messages: {
                username: {required: "请填写用户名."}, password: {required: "请填写密码."},
                validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
            },
            errorLabelContainer: "#messageBox",
            errorPlacement: function (error, element) {
                error.appendTo($("#loginError").parent());
            }
        });
    });
    // 如果在框架或在对话框中，则弹出提示并跳转到首页
    if (self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
        alert('未登录或登录超时。请重新登录，谢谢！');
        top.location = "${ctx}";
    }
    })
    ;
</script>
</body>
</html>
