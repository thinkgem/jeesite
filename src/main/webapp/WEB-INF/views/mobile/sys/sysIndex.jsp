<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<meta name="decorator" content="default_mb"/>
	<script type="text/javascript"> 
		$(document).ready(function() {
		});
	</script>
</head>
<body>
	<ul data-role="listview" data-ajax="false"  data-inset="true" data-divider-theme="b">
		<li data-role="list-divider">我的面板</li>
		<li><a href="${ctx }/sys/user/info">个人信息</a></li>
	    <li><a href="${ctx }/sys/user/modifyPwd">修改密码</a></li>
	</ul>
</body>
</html>