<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）"%>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false" description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）"%>
<div class="input-append">
	<input id="${id}Id" name="${name}" class="required" type="hidden" value="${value}"/>
	<input id="${id}Name" name="${labelName}" readonly="readonly" type="text" value="${labelValue}" maxlength="50"
		class="input-medium" /><a id="${id}Button" href="javascript:" class="btn">选择</a>&nbsp;&nbsp;
</div>
<script type="text/javascript">
	$("#${id}Button").click(function(){
		top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("${url}")+"&module=${module}&checked=${checked}&extId=${extId}&selectIds="+$("#${id}Id").val(), "选择${title}", 300, 420, {
			buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
				if (v=="ok"){
					var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
					var ids = [], names = [], nodes = [];
					if ("${checked}" == "true"){
						nodes = tree.getCheckedNodes(true);
					}else{
						nodes = tree.getSelectedNodes();
					}
					for(var i=0; i<nodes.length; i++) {
						if ("${checked}" == "true" && nodes[i].isParent){
							continue; // 如果为复选框选择，则过滤掉父节点
						}
						if ("${notAllowSelectRoot}" == "true" && nodes[i].level == 0){
							top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
							return false;
						}
						if ("${notAllowSelectParent}" == "true" && nodes[i].isParent){
							top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
							return false;
						}
						if ("${module}" != "" && "${selectScopeModule}" == "true"){
							if (nodes[i].module == ""){
								top.$.jBox.tip("不能选择公共模型（"+nodes[i].name+"）请重新选择。");
								return false;
							}else if (nodes[i].module != "${module}"){
								top.$.jBox.tip("不能选择当前栏目以外的栏目模型，请重新选择。");
								return false;
							}
						}
						ids.push(nodes[i].id);
						names.push(nodes[i].name);
					}
					$("#${id}Id").val(ids);
					$("#${id}Name").val(names);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});
</script>