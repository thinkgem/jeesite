<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除的编号（不能选择的编号）"%>
<%@ attribute name="parentIds" type="java.lang.String" required="false" description="父编号层次路径，用于首次打开时选中的节点"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否可复选"%>
<%@ attribute name="checkedIds" type="java.lang.String" required="false" description="如果是可复选，则指定默认选中的Id"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<div class="input-append">
	<input id="${id}Id" name="${name}" class="required" type="hidden" value="${value}"/>
	<input id="${id}Name" name="${labelName}" readonly="readonly" type="text" value="${labelValue}" maxlength="50"
		class="input-medium" /><a id="${id}Button" href="javascript:" class="btn">选择</a>&nbsp;&nbsp;
</div>
<script type="text/javascript">
	var ${id}ParentIds = "-1_1_${fn:replace(fn:substringAfter(parentIds,','),',','_')}";
	var ${id}CheckedIds = "${checkedIds}";
	$("#${id}Button").click(function(){
		top.$.jBox.open("iframe:${ctx}/tags/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&checkedIds="+${id}CheckedIds+"&extId=${extId}&parentIds="+${id}ParentIds, "选择${title}", 300, 420, {
			buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
				if (v=="ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					if ("${checked}" == "true"){
						${id}CheckedIds = tree.getChecked();
						$("#${id}Id").val(${id}CheckedIds);
						$("#${id}Name").val(tree.getCheckedText());
					} else if (tree.selectedNode){
						if ("${notAllowSelectRoot}" == "true" && tree.selectedNode.id==1){
							top.$.jBox.tip("不能选择根节点（"+tree.selectedNode.text+"）请重新选择。");
							return false;
						}
						if ("${notAllowSelectParent}" == "true" && tree.selectedNode.childNodes.length>0){
							top.$.jBox.tip("不能选择父节点（"+tree.selectedNode.text+"）请重新选择。");
							return false;
						}
						if ("${module}" != "" && "${selectScopeModule}" == "true"){
							if (tree.selectedNode.get("module") == ""){
								top.$.jBox.tip("不能选择公共模型（"+tree.selectedNode.text+"）请重新选择。");
								return false;
							}else if (tree.selectedNode.get("module") != "${module}"){
								top.$.jBox.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
								return false;
							}
						}
						$("#${id}Id").val(tree.selectedNode.id);
						$("#${id}Name").val(tree.selectedNode.text);
						${id}ParentIds = tree.selectedNode.path;
					}
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
</script>