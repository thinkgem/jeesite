<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" /><meta http-equiv="Pragma" content="no-cache" /><meta http-equiv="Expires" content="0" />
<meta name="author" content="http://thinkgem.iteye.com"/><meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<c:if test="${site.theme eq 'basic' or site.theme eq 'b.com'}">
	<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
	<link href="${ctxStatic}/common/jeesite.min.css" type="text/css" rel="stylesheet" />
</c:if>
<link href="${ctxStaticTheme}/style.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStaticTheme}/script.min.js" type="text/javascript"></script>