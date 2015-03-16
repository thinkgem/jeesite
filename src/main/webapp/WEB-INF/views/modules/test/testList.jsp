<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/supcan.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			su = supcan(AF, "Test", {
				ready: function(){
					// 加载数据
					page();
				},
				event: function(Event, p1, p2, p3, p4){
					if(Event == 'DblClicked'){
						edit();
					}
				}
			});
		});
		
		// 新建
		function add(){
			alertx("新建数据");
			location = "${ctx}/test/test/form";
		}
		
		// 修改
		function edit(){
			var id = su.getCellText("id");
			if (id != ""){
				location = "${ctx}/test/test/form?id=" + id;
				alertx("修改数据：id=" + id);
			}else{
				alertx("请选择一行数据！");
			}
		}
		
		//删除
		function dele(){
			var ids = su.getCellText("id", true);
			if (ids != ""){
				$.get("${ctx}/test/test/delete?id=" + ids, function(data){
					if (data == "true"){
						showTip("删除“" + su.getCellText("name", true) + "”成功。");
						page();
					}else{
						showTip("删除“" + su.getCellText("name", true) + "”失败！");
					}
				});
			}else{
				alertx("请选择一行或多行数据！");
			}
		}
		
		// 翻页
		function page(pageNo, pageSize){
			if (pageNo != ""){
				$("#pageNo").val(pageNo);
			}
			if (pageSize != ""){
				$("#pageSize").val(pageSize);
			}
			su.loadByForm("#searchForm", "#page");
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/test/test/">硕正列表</a></li>
		<shiro:hasPermission name="test:test:edit"><li><a href="${ctx}/test/test/form">组件表单</a></li></shiro:hasPermission>
	</ul>
	<div class="btn-toolbar breadcrumb">
		<div class="btn-group">
			<a class="btn" href="javascript:add();"><i class="icon-file"></i>新建</a>
			<a class="btn" href="javascript:edit();"><i class="icon-edit"></i>修改</a>
			<a class="btn" href="javascript:dele();" onclick="return confirmx('确认要删除该测试吗？', this.href)"><i class="icon-remove"></i>删除</a>
		</div>
		<div class="btn-group" >
			<a class="btn" onclick="$('#searchForm').toggle();su.autoHeight();"><i class="icon-search"></i>查询</a>
		</div>
	</div>
	<form:form id="searchForm" modelAttribute="test" action="${ctx}/test/test/listData.json" method="post" class="breadcrumb form-search hide" onsubmit="return page();">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<sys:message content="${message}"/>
	<div id="supcan" class="supcan">
		<script>insertTreeList('AF', 'border=none;')</script>
	</div>
	<div id="page" class="pagination">${test.page}</div>
</body>
</html>
