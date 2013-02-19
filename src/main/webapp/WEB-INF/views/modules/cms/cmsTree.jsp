<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>栏目列表</title>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			var categoryData={};<c:forEach items="${categoryList}" var="category">
			categoryData["${not empty category.parent?category.parent.id:'-1'}_${category.id}"] = "text: ${not empty category.parent?category.name:'栏目列表'}; url:${ctx}/cms/${not empty category.module?category.module:'none'}/?category.id=${category.id}; target:cmsMainFrame";</c:forEach>
			var categoryTree = new MzTreeView(categoryData);
			categoryTree.showRoot=false;
			$("#categoryTree").html(categoryTree.render());
			categoryTree.expandAll("1");
		});
	</script>
</head>
<body>
	<div class="accordion-group">
	    <div class="accordion-heading">
	    	<a class="accordion-toggle">栏目列表</a>
	    </div>
	    <div class="accordion-body ">
			<div class="accordion-inner">
				<div id="categoryTree" style="margin-top:5px;"></div>
			</div>
	    </div>
	</div>
</body>
</html>
