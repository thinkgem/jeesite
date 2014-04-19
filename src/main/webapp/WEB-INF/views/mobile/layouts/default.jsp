<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
	<head>
		<title><sitemesh:title/> - Powered By Jeesite</title>
		<%@include file="/WEB-INF/views/mobile/include/head.jsp" %>
		<sitemesh:head/>
	</head>
	<body data-role="page">
		<div data-role="header" data-theme="b">
		    <a href="${ctx}" data-icon="home">首页</a>
		    <h1>欢迎：${fns:getUser().name }-Jeesite</h1>
		    <a href="${ctx}/logout" data-icon="back">退出</a>
		</div>
		<div role="main" class="ui-content">
			<sitemesh:body/>
		</div><!-- /content -->
		<div data-role="footer"  data-theme="b">
			<h4>Copyright 2013 The Jeesite Foundation</h4>
		</div><!-- /footer -->
	</body>
</html>