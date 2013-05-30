<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">.table td i{margin:0 2px;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
	    	}
	    	
	    	// 下面两方法用来批量保存菜单排序
	    	function getTableForm(){
	    		return document.getElementById("updateMenu");
	    	}
	    	
	    	function optPriority() {
		    	var f = getTableForm();
		    	f.action="${ctx}/sys/menu/updateSort";
		    	f.submit();
	    	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/menu/">菜单列表</a></li>
		<shiro:hasPermission name="sys:menu:edit"><li><a href="${ctx}/sys/menu/form">菜单添加</a></li></shiro:hasPermission>
	</ul>
	<tags:message content="${message}"/>
	<form id="updateMenu" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>名称</th><th>链接</th><th>排序</th><th>可见</th><th>权限标识</th><shiro:hasPermission name="sys:menu:edit"><th>操作</th></shiro:hasPermission></tr>
			<c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${menu.parent.id ne 1?menu.parent.id:'0'}">
					<td><i class="icon-${not empty menu.icon?menu.icon:' hide'}"></i><a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a></td>
					<td>${menu.href}</td>
					
					<!-- 批量排序 -->
					<shiro:hasPermission name="sys:menu:edit"><td>
						<input type="hidden" name="ids" value="${menu.id}"/>
						<div class="input-prepend" style="margin:0;">
							<input class="pagination-right" name="sorts" type="text" value="${menu.sort}" style="width:50px;height:14px;">
						</div>
					</td></shiro:hasPermission>
					<shiro:lacksPermission name="sys:menu:edit">${menu.sort}</shiro:lacksPermission>
					
					<td>${menu.isShow eq 1?'显示':'隐藏'}</td>
					<td>${menu.permission}</td>
					<shiro:hasPermission name="sys:menu:edit"><td>
						<a href="${ctx}/sys/menu/form?id=${menu.id}">修改</a>
						<a href="${ctx}/sys/menu/delete?id=${menu.id}" onclick="return confirmx('要删除该菜单及所有子菜单项吗？', this.href)">删除</a>
						<a href="${ctx}/sys/menu/form?parent.id=${menu.id}">添加下级菜单</a> 
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
		</table>
		<shiro:hasPermission name="sys:menu:edit"><div class="form-actions pagination-left">
	        	<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存菜单排序" onclick="optPriority();"/></div>
	        </shiro:hasPermission>
	 </form>
</body>
</html>
