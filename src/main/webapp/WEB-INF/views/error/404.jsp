<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>404 - 页面不存在</title>
</head>
<body>
	<div><h1>页面不存在.</h1></div>
	<div><a href="javascript:" onclick="history.go(-1);">返回上一页</a></div>
	<script>try{top.$.jBox.closeTip();}catch(e){}</script>
</body>
</html>