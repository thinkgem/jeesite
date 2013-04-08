<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>内容管理</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<div id="left">
		<iframe id="cmsMenuFrame" name="cmsMenuFrame" src="${ctx}/cms/tree" style="overflow:visible;"
			scrolling="yes" frameborder="no" width="100%"></iframe>
	</div>
	<div id="openClose" class="close">&nbsp;</div>
	<div id="right">
		<iframe id="cmsMainFrame" name="cmsMainFrame" src="${ctx}/cms/none" style="overflow:visible;"
			scrolling="yes" frameborder="no" width="100%"></iframe>
	</div>
	<script type="text/javascript"> 
		var lw = "14.89%", rw = "82%"; // 左侧窗口展开大小
		var lwClose = "0%", rwClose = "97%"; // 左侧窗口折叠大小
		function wSize(){
			var strs=getWindowSize().toString().split(",");
			$("#cmsMenuFrame, #cmsMainFrame, #openClose").height(strs[0]-6);
		}
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>
