<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				rules: {
					name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")}
				},
				messages: {
					name: {remote: "角色名已存在"}
				},
				submitHandler: function(form){
					$("#menuIds").val(menuTree.getChecked());
					$("#categoryIds").val(categoryTree.getChecked());
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			var menuData={};<c:set var="strMenuIds" value="${','}${role.menuIds}${','}" /><c:forEach items="${menuList}" var="menu"><c:set var="strMenuId" value="${','}${menu.id}${','}" />
			menuData["${not empty menu.parent.id?menu.parent.id:'-1'}_${menu.id}"] = "text: ${not empty menu.parent.id?menu.name:'权限列表'}; checked: ${fn:contains(strMenuIds, strMenuId)}";</c:forEach>
			var menuTree = new MzTreeView(menuData);
			menuTree.useCheckbox=true;
			menuTree.linkFocus = false;
			menuTree.linkCheckbox = true;
			menuTree.isParentCheckbox = true;
			$("#menuTree").html(menuTree.render());
			menuTree.expandAll("1");
			var categoryData={};<c:set var="strCategoryIds" value="${','}${role.categoryIds}${','}" /><c:forEach items="${categoryList}" var="category"><c:set var="strCategoryId" value="${','}${category.id}${','}" />
			categoryData["${not empty category.parent?category.parent.id:'-1'}_${category.id}"] = "text: ${not empty category.parent?category.name:'栏目列表'}; checked: ${fn:contains(strCategoryIds, strCategoryId)}";</c:forEach>
			var categoryTree = new MzTreeView(categoryData);
			categoryTree.useCheckbox=true;
			categoryTree.linkFocus = false;
			categoryTree.linkCheckbox = true;
			categoryTree.isParentCheckbox = true;
			$("#categoryTree").html(categoryTree.render());
			categoryTree.expandAll("1");
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<li class="active"><a href="${ctx}/sys/role/form?id=${role.id}">角色<shiro:hasPermission name="sys:role:edit">${not empty role.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${role.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">授权:</label>
			<div class="controls">
				<div id="menuTree" style="margin-top:5px;float:left;"></div>
				<form:hidden path="menuIds"/>
				<div id="categoryTree" style="margin-left:100px;margin-top:5px;float:left;"></div>
				<form:hidden path="categoryIds"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="sys:role:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
