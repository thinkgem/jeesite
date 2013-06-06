<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	
		var officeTree;
		var selectedTree;//zTree已选择对象
		
		// 初始化
		$(document).ready(function(){
			officeTree = $.fn.zTree.init($("#officeTree"), setting, officeNodes);
			selectedTree = $.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		});

		var setting = {view: {selectedMulti:false},
				data: {simpleData: {enable: true}},
				callback: {onClick: treeOnClick}};
		
		var officeNodes=[
	            <c:forEach items="${officeList}" var="office">
	            {id:"${office.id}",
	             pId:"${not empty office.parent?office.parent.id:0}", 
	             name:"${office.name}"},
	            </c:forEach>];
	
		var selectedNodes =[];
		        /*<c:forEach items="${selectedList}" var="user">
		        {id:"${user.id}",
		         pId:"0",
		         name:"${user.name}"},
		        </c:forEach>];*/
		
		var ids = [];
		
		//点击选择项回调
		function treeOnClick(event, treeId, treeNode, clickFlag){
			if("officeTree"==treeId){
				$.get("${ctx}/sys/role/users?officeId=" + treeNode.id, function(userNodes){
					// 初始化树结构
					$.fn.zTree.init($("#userTree"), setting, userNodes);
				});
			}
			if("userTree"==treeId){
				if($.inArray(treeNode.id, ids)<0){
					selectedTree.addNodes(null, treeNode);
					ids.push(treeNode.id);
				}
			};
		};

		//清除选中项
		function clearSelected(){
			selectedNodes=null;
			$.fn.zTree.init($("#selectedTree"), setting, selectedNodes);
		};
		
	</script>
</head>
<body>
	<div class="breadcrumb">
		<label>通过选择部门，然后为列出的人员分配角色。</label>
	</div>
	<tags:message content="${message}"/>
	<div id="assignRole" class="row-fluid span12">
		<div class="span4" style="border-right: 1px solid #A8A8A8;">
			<p>所在部门：</p>
			<div id="officeTree" class="ztree"></div>
		</div>
		<div class="span4">
			<p>待选人员：</p>
			<div id="userTree" class="ztree"></div>
		</div>
		<div class="span4" style="padding-left:16px;border-left: 1px solid #A8A8A8;">
			<p>已选人员：</p>
			<div id="selectedTree" class="ztree"></div>
		</div>
	</div>
</body>
</html>