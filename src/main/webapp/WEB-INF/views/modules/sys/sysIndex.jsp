<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>管理主页</title>
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
	<style type="text/css">
		.nav li{margin-top:8px;}
		.nav li.title{margin-top:0;}
		.nav li a{font-size:14px;padding:6px 8px;*padding:3px 8px;}
	</style>
</head>
<body>
	<div id="main" class="container-fluid">
	    <div id="header" class="row-fluid">
			<div id="title">
				<span class="pull-right">您好, 
			    	<shiro:user><a href="${ctx}/sys/user/info" target="mainFrame"><shiro:principal property="name"/></a></shiro:user><shiro:guest>请<a href="${ctx}/login">登录</a>!</shiro:guest>
					<shiro:user> | <a href="${ctx}/logout">退出</a></shiro:user> | <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/index-${fnc:getCurrentSiteId()}.html" target="_blank">访问网站</a>
					&nbsp;&nbsp;&nbsp;
				</span>
		    	<ul class="nav nav-pills nav-tabs2" style="margin:0;">
		    	  <li class="title"><h1>JeeSite Admin <small></small></h1></li>
				  <li style="width:15px;">&nbsp;</li><%--
				  <li class="active"><a href="${ctx}">首页</a></li><li><a href="${ctx}">内容</a></li>
				  <li><a href="${ctx}">模块</a></li><li><a href="${ctx}">设置</a></li> --%>
		    	  <li style="width:15px;">&nbsp;</li>
				  <shiro:hasPermission name="cms:view"><li class="dropdown">
				    <a class="dropdown-toggle" data-toggle="dropdown" href="#">${fnc:getSite(fnc:getCurrentSiteId()).name}<b class="caret"></b></a>
				    <ul class="dropdown-menu">
				      <c:forEach items="${fnc:getSiteList()}" var="site"><li><a href="${ctx}/cms/site/select?id=${site.id}">${site.name}</a></li></c:forEach>
				    </ul>
				  </li></shiro:hasPermission>
				</ul>
			</div>
		</div>
		<div id="content" class="row-fluid">
			<div id="left">
				<iframe id="menuFrame" name="menuFrame" src="${ctx}/sys/menu/tree" style="overflow:visible;"
					scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
			</div>
			<div id="openClose" class="close">&nbsp;</div>
			<div id="right">
				<iframe id="mainFrame" name="mainFrame" src="${ctx}/sys/user/info" style="overflow:visible;"
					scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
			</div>
		</div>
	    <div id="footer" class="row-fluid">
            Copyright &copy; 2012-2013 <a href="mailto:thinkgem@163.com">ThinkGem</a> - Powered By <a href="https://github.com/thinkgem/jeesite">JeeSite</a> V1.0
		</div>
	</div>
	<script type="text/javascript"> 
		var lw = "14.89%", rw = "82.97%"; // 左侧窗口展开大小
		var lwClose = "0%", rwClose = "97.58%"; // 左侧窗口折叠大小
		function wSize(){
			var strs=getWindowSize().toString().split(",");
			if(strs[0]<550){
				$("#main").css("height",550+"px");
			}else{
				$("#menuFrame, #mainFrame, #openClose").height(strs[0]-$("#header").height() - $("#footer").height() - 80);  
			}
			if(strs[1]<980){
				$("#main").css("width",970+"px");
				$("html").css("overflow-x","auto");
			}else{
				$("#main").css("width","auto");
				$("html").css("overflow-x","hidden");
			}
		}
	</script>
	<script src="${ctxStatic}/wsize.js" type="text/javascript"></script>
</body>
</html>
