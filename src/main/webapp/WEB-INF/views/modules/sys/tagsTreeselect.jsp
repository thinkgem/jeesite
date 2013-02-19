<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/views/include/treeview.jsp"%>
<div id="tree" style="padding:15px 20px;"></div>
<script type="text/javascript">
	var tree = new MzTreeView();
	if ("${checked}" == "true"){
		tree.useCheckbox=true;
		tree.linkFocus = false;
		tree.linkCheckbox = true;
		tree.isParentCheckbox = true;
	}
	tree.loadJsData("${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}&module=${module}&checkedIds=${checkedIds}&extId=${extId}&t="+new Date().getTime());
	$("#tree").html(tree.render());
	tree.expandAll("1");
	tree.focusNodeByPath("${parentIds}");
	$("#tree").bind("dblclick", function(){
		top.$.jBox.getBox().find("button[value='ok']").trigger("click");
		//alert($("input[type='text']", top.mainFrame.document).val());
		//$("input[type='text']", top.mainFrame.document).focus();
	});
</script>