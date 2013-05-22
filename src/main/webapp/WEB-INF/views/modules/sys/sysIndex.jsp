<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		#main {padding:0 12px;} #header {margin:5px} #header h1 {font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:26px;color:#0663A2;}
		#header h1 small {color:#0088CC;} #header li {list-style:none;}
		#footer {margin:8px 0 0 0;padding:6px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #0663A2;}
		#footer a {color:#999;} .nav li {margin-top:5px;} .nav li.title{margin-top:0;} .nav li.menu,.nav li.dropdown {margin:5px 3px 0 3px}
		.nav li.menu a {padding:5px 6px;*padding:4px 5px 3px 5px;}.nav li.dropdown a {padding:5px 6px;*padding:1px 5px 3px 5px;}
		.nav li a {font-size:14px;padding:6px 8px;*padding:3px 8px;} .nav-info {float:left;margin:3px;padding:0 10px 0 5px;border-right:1px solid #ddd}
		.nav-info-last {float:left;margin:3px;padding:0 20px 0 5px;border-right:0}
	</style>
	<script type="text/javascript"> 
		$(document).ready(function() {
			$("#menu a.menu").click(function(){
				$("#menu li.menu").removeClass("active");
				$(this).parent().addClass("active");
				if(!$("#openClose").hasClass("close")){
					$("#openClose").click();
				}
			});
		});
	</script>
</head>
<body>
	<div id="main" class="container-fluid">
	    <div id="header" class="row-fluid">
			<div id="title"><%--
				<span class="pull-right hide">您好, 
			    	<shiro:user><a href="${ctx}/sys/user/info" target="mainFrame"><shiro:principal property="name"/></a></shiro:user><shiro:guest>请<a href="${ctx}/login">登录</a>!</shiro:guest>
					<shiro:user> | <a href="${ctx}/logout">退出</a></shiro:user> | <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/index-${fnc:getCurrentSiteId()}.html" target="_blank">访问网站</a>
					&nbsp;&nbsp;&nbsp;
				</span> --%>
				<ul class="pull-right">
				  	<shiro:user>
						<li class="nav-info dropdown">
						    <a class="dropdown-toggle" data-toggle="dropdown" href="#">您好, <shiro:principal property="name"/><b class="caret"></b></a>
						    <ul class="dropdown-menu">
						      <li><a href="${ctx}/sys/user/info" target="mainFrame">个人信息</a></li>
						      <li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame">修改密码</a></li>
						    </ul>
					  	</li><li class="nav-info"><a href="${ctx}/logout">退出</a></li>
				  	</shiro:user>
				  	<shiro:guest>
				  		<li class="nav-info">请<a href="${ctx}/login">登录</a>!</li>
				  	</shiro:guest>
				  	<li class="nav-info-last"><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/index-${fnc:getCurrentSiteId()}.html" target="_blank">访问网站</a></li>
			  	</ul>
		    	<ul class="nav nav-pills" style="margin:0;" id="menu">
		    	  <li class="title"><h1>${fns:getConfig('productName')}<small></small></h1></li>
				  <li style="width:18px;">&nbsp;</li>
				  <c:set var="firstMenu" value="true"/><c:forEach items="${fns:getMenuList()}" var="menu" varStatus="idxStatus"><c:if test="${menu.parent.id eq 1&&menu.isShow eq 1}">
					<li class="menu ${firstMenu?' active':''}"><a class="menu" href="${ctx}/sys/menu/tree?parentId=${menu.id}" target="menuFrame" >${menu.name}</a></li>
				  <c:if test="${firstMenu}"><c:set var="firstMenuId" value="${menu.id}"/></c:if><c:set var="firstMenu" value="false"/></c:if></c:forEach>
				  <shiro:hasPermission name="cms:site:select"><li class="dropdown">
				    <a class="dropdown-toggle" data-toggle="dropdown" href="#">${fnc:getSite(fnc:getCurrentSiteId()).name}<b class="caret"></b></a>
				    <ul class="dropdown-menu">
				      <c:forEach items="${fnc:getSiteList()}" var="site"><li><a href="${ctx}/cms/site/select?id=${site.id}&flag=1">${site.name}</a></li></c:forEach>
				    </ul>
				  </li></shiro:hasPermission>
				</ul>
			</div>
		</div>
		<div id="content" class="row-fluid">
			<div id="left">
				<iframe id="menuFrame" name="menuFrame" src="${ctx}/sys/menu/tree?parentId=${firstMenuId}" style="overflow:visible;"
					scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
			</div>
			<div id="openClose" class="close">&nbsp;</div>
			<div id="right">
				<iframe id="mainFrame" name="mainFrame" src="${ctx}/sys/user/info" style="overflow:visible;"
					scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
			</div>
		</div>
	    <div id="footer" class="row-fluid">
            Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="http://thinkgem.iteye.com" target="_blank">ThinkGem</a> - Powered By <a href="https://github.com/thinkgem/jeesite" target="_blank">JeeSite</a> ${fns:getConfig('version')}
		</div>
	</div>
	<script type="text/javascript"> 
		var leftWidth = "160"; // 左侧窗口大小
		function wSize(){
			var minHeight = 500, minWidth = 980;
			var strs=getWindowSize().toString().split(",");
			$("#menuFrame, #mainFrame, #openClose").height((strs[0]<minHeight?minHeight:strs[0])-$("#header").height()-$("#footer").height()-38);
			$("#openClose").height($("#openClose").height()-5);
			if(strs[1]<minWidth){
				$("#main").css("width",minWidth-10);
				$("html,body").css({"overflow":"auto","overflow-x":"auto","overflow-y":"auto"});
			}else{
				$("#main").css("width","auto");
				$("html,body").css({"overflow":"hidden","overflow-x":"hidden","overflow-y":"hidden"});
			}
			$("#right").width($("#content").width()-$("#left").width()-$("#openClose").width()-9);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>