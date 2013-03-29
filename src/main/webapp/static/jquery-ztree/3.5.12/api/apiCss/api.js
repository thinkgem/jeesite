var apiContent = {
	zTree_Setting: null,
	zTree_Node: null,
	zTree_Function: null,
	overlayDiv : null,
	overlayContent : null,
	overlayDetailDiv : null,
	overlayCloseBtn: null,
	overlayArrow: null,
	contentBoxDiv : null,
	settingDiv : null,
	functionDiv : null,
	overlaySearch: null,
	searchKey: null,
	searchResultInput: null,
	searchPrevBtn: null,
	searchNextBtn: null,
	apiCache: {},
	lastValue: "",
	searchNodes: [],
	searchNodesCur: 0,

	_init: function() {
		this.overlayDiv = $("#overlayDiv");
		this.overlayContent = $("#overlayContent");
		this.overlayDetailDiv = $("#overlayDetailDiv");
		this.overlayCloseBtn = $("#overlayDivCloseBtn");
		this.overlayArrow = $("#overlayDivArrow");
		this.contentBoxDiv = $("#contentBox");
		this.settingDiv = $("#api_setting");
		this.functionDiv = $("#api_function");
		this.searchKey = $(".searchKey");
		this.overlaySearch = $(".overlaySearch");
		this.searchResultInput = $(".searchResult");
		this.searchPrevBtn = $(".searchPrev");
		this.searchNextBtn = $(".searchNext");
		var setting = {
			view: {
				fontCss: this.getFontCss,
				showLine: false,
				showIcon: this.showIcon,
				showTitle: this.getTitle,
				selectedMulti: false,
				dblClickExpand: false
			},
			data: {
				key: {
					title: "tt"
				},
				simpleData: {
					enable:true,
					idKey: "id",
					pIdKey: "pId",
					rootPId: ""
				}
			},
			callback: {
				onNodeCreated: this.onNodeCreated,
				beforeClick: this.beforeClick
			}
		}
		var setting_nodes =[
			{id:1, pId:0, t:"setting", name:"var setting = {", open:true},
			{id:11, pId:1, t:"treeId", name:"treeId : \"\",", iconSkin:"core", showAPI:true},
			{id:12, pId:1, t:"treeObj", name:"treeObj : null,", iconSkin:"core", showAPI:true},
			{id:121, pId:1, name:""},

			{id:20, pId:1, t:"async", name:"async : {", open:true},
			{id:201, pId:20, t:"autoParam", name:"autoParam : [],", iconSkin:"core", showAPI:true},
			{id:208, pId:20, t:"contentType", name:"contentType : \"application...\",", iconSkin:"core", showAPI:true},
			{id:202, pId:20, t:"dataFilter", name:"dataFilter : null,", iconSkin:"core", showAPI:true},
			{id:203, pId:20, t:"dataType", name:"dataType : \"text\",", iconSkin:"core", showAPI:true},
			{id:204, pId:20, t:"enable", name:"enable : false,", iconSkin:"core", showAPI:true},
			{id:205, pId:20, t:"otherParam", name:"otherParam : [],", iconSkin:"core", showAPI:true},
			{id:206, pId:20, t:"type", name:"type : \"post\",", iconSkin:"core", showAPI:true},
			{id:207, pId:20, t:"url", name:"url : \"\"", iconSkin:"core", showAPI:true},
			{id:21, pId:1, name:"},"},
			{id:22, pId:1, name:""},

			{id:30, pId:1, t:"callback", name:"callback : {", open:true},
			{id:3001, pId:30, t:"beforeAsync", name:"beforeAsync : null,", iconSkin:"core", showAPI:true},
			{id:3002, pId:30, t:"beforeCheck", name:"beforeCheck : null,", iconSkin:"check", showAPI:true},
			{id:3003, pId:30, t:"beforeClick", name:"beforeClick : null,", iconSkin:"core", showAPI:true},
			{id:3004, pId:30, t:"beforeCollapse", name:"beforeCollapse : null,", iconSkin:"core", showAPI:true},
			{id:3004, pId:30, t:"beforeDblClick", name:"beforeDblClick : null,", iconSkin:"core", showAPI:true},
			{id:3005, pId:30, t:"beforeDrag", name:"beforeDrag : null,", iconSkin:"edit", showAPI:true},
			{id:3006, pId:30, t:"beforeDragOpen", name:"beforeDragOpen : null,", iconSkin:"edit", showAPI:true},
			{id:3007, pId:30, t:"beforeDrop", name:"beforeDrop : null,", iconSkin:"edit", showAPI:true},
			{id:3029, pId:30, t:"beforeEditName", name:"beforeEditName : null,", iconSkin:"edit", showAPI:true},
			{id:3008, pId:30, t:"beforeExpand", name:"beforeExpand : null,", iconSkin:"core", showAPI:true},
			{id:3009, pId:30, t:"beforeMouseDown", name:"beforeMouseDown : null,", iconSkin:"core", showAPI:true},
			{id:3010, pId:30, t:"beforeMouseUp", name:"beforeMouseUp : null,", iconSkin:"core", showAPI:true},
			{id:3011, pId:30, t:"beforeRemove", name:"beforeRemove : null,", iconSkin:"edit", showAPI:true},
			{id:3012, pId:30, t:"beforeRename", name:"beforeRename : null,", iconSkin:"edit", showAPI:true},
			{id:3013, pId:30, t:"beforeRightClick", name:"beforeRightClick : null,", iconSkin:"core", showAPI:true},
			{id:3014, pId:30, name:""},
			{id:3015, pId:30, t:"onAsyncError", name:"onAsyncError : null,", iconSkin:"core", showAPI:true},
			{id:3016, pId:30, t:"onAsyncSuccess", name:"onAsyncSuccess : null,", iconSkin:"core", showAPI:true},
			{id:3017, pId:30, t:"onCheck", name:"onCheck : null,", iconSkin:"check", showAPI:true},
			{id:3018, pId:30, t:"onClick", name:"onClick : null,", iconSkin:"core", showAPI:true},
			{id:3019, pId:30, t:"onCollapse", name:"onCollapse : null,", iconSkin:"core", showAPI:true},
			{id:3019, pId:30, t:"onDblClick", name:"onDblClick : null,", iconSkin:"core", showAPI:true},
			{id:3020, pId:30, t:"onDrag", name:"onDrag : null,", iconSkin:"edit", showAPI:true},
			{id:3021, pId:30, t:"onDrop", name:"onDrop : null,", iconSkin:"edit", showAPI:true},
			{id:3022, pId:30, t:"onExpand", name:"onExpand : null,", iconSkin:"core", showAPI:true},
			{id:3023, pId:30, t:"onMouseDown", name:"onMouseDown : null,", iconSkin:"core", showAPI:true},
			{id:3024, pId:30, t:"onMouseUp", name:"onMouseUp : null,", iconSkin:"core", showAPI:true},
			{id:3025, pId:30, t:"onNodeCreated", name:"onNodeCreated : null,", iconSkin:"core", showAPI:true},
			{id:3026, pId:30, t:"onRemove", name:"onRemove : null,", iconSkin:"edit", showAPI:true},
			{id:3027, pId:30, t:"onRename", name:"onRename : null,", iconSkin:"edit", showAPI:true},
			{id:3028, pId:30, t:"onRightClick", name:"onRightClick : null", iconSkin:"core", showAPI:true},
			{id:31, pId:1, name:"},"},
			{id:32, pId:1, name:""},

			{id:40, pId:1, t:"check", name:"check : {", open:true},
			{id:405, pId:40, t:"autoCheckTrigger", name:"autoCheckTrigger : false,", iconSkin:"check", showAPI:true},
			{id:401, pId:40, t:"chkboxType", name:"chkboxType : {\"Y\": \"ps\", \"N\": \"ps\"},", iconSkin:"check", showAPI:true},
			{id:402, pId:40, t:"chkStyle", name:"chkStyle : \"checkbox\",", iconSkin:"check", showAPI:true},
			{id:403, pId:40, t:"enable", name:"enable : false,", iconSkin:"check", showAPI:true},
			{id:406, pId:40, t:"nocheckInherit", name:"nocheckInherit : false", iconSkin:"check", showAPI:true},
			{id:407, pId:40, t:"chkDisabledInherit", name:"chkDisabledInherit : false", iconSkin:"check", showAPI:true},
			{id:404, pId:40, t:"radioType", name:"radioType : \"level\"", iconSkin:"check", showAPI:true},
			{id:41, pId:1, name:"},"},
			{id:42, pId:1, name:""},

			{id:50, pId:1, t:"data", name:"data : {", open:true},
			{id:500, pId:50, t:"keep", name:"keep : {", open:true},
			{id:5001, pId:500, t:"leaf", name:"leaf : false,", iconSkin:"core", showAPI:true},
			{id:5002, pId:500, t:"parent", name:"parent : false", iconSkin:"core", showAPI:true},
			{id:501, pId:50, name:"},"},

			{id:510, pId:50, t:"key", name:"key : {", open:true},
			{id:5101, pId:510, t:"checked", name:"checked : \"checked\",", iconSkin:"check", showAPI:true},
			{id:5102, pId:510, t:"children", name:"children : \"children\",", iconSkin:"core", showAPI:true},
			{id:5103, pId:510, t:"name", name:"name : \"name\",", iconSkin:"core", showAPI:true},
			{id:5104, pId:510, t:"title", name:"title : \"\"", iconSkin:"core", showAPI:true},
			{id:5105, pId:510, t:"url", name:"url : \"url\"", iconSkin:"core", showAPI:true},
			{id:511, pId:50, name:"},"},

			{id:520, pId:50, t:"simpleData", name:"simpleData : {", open:true},
			{id:5201, pId:520, t:"enable", name:"enable : false,", iconSkin:"core", showAPI:true},
			{id:5202, pId:520, t:"idKey", name:"idKey : \"id\",", iconSkin:"core", showAPI:true},
			{id:5203, pId:520, t:"pIdKey", name:"pIdKey : \"pId\",", iconSkin:"core", showAPI:true},
			{id:5204, pId:520, t:"rootPId", name:"rootPId : null", iconSkin:"core", showAPI:true},
			{id:521, pId:50, name:"}"},
			{id:51, pId:1, name:"},"},
			{id:52, pId:1, name:""},

			{id:60, pId:1, t:"edit", name:"edit : {", open:true},
			{id:601, pId:60, t:"drag", name:"drag : {", open:true},
			{id:60111, pId:601, t:"autoExpandTrigger", name:"autoExpandTrigger : true,", iconSkin:"edit", showAPI:true},
			{id:60101, pId:601, t:"isCopy", name:"isCopy : true,", iconSkin:"edit", showAPI:true},
			{id:60102, pId:601, t:"isMove", name:"isMove : true,", iconSkin:"edit", showAPI:true},
			{id:60103, pId:601, t:"prev", name:"prev : true,", iconSkin:"edit", showAPI:true},
			{id:60104, pId:601, t:"next", name:"next : true,", iconSkin:"edit", showAPI:true},
			{id:60105, pId:601, t:"inner", name:"inner : true,", iconSkin:"edit", showAPI:true},
			{id:60107, pId:601, t:"borderMax", name:"borderMax : 10,", iconSkin:"edit", showAPI:true},
			{id:60108, pId:601, t:"borderMin", name:"borderMin : -5,", iconSkin:"edit", showAPI:true},
			{id:60106, pId:601, t:"minMoveSize", name:"minMoveSize : 5,", iconSkin:"edit", showAPI:true},
			{id:60109, pId:601, t:"maxShowNodeNum", name:"maxShowNodeNum : 5,", iconSkin:"edit", showAPI:true},
			{id:60110, pId:601, t:"autoOpenTime", name:"autoOpenTime : 500", iconSkin:"edit", showAPI:true},
			{id:602, pId:60, name:"},"},
			{id:608, pId:60, t:"editNameSelectAll", name:"editNameSelectAll : false,", iconSkin:"edit", showAPI:true},
			{id:603, pId:60, t:"enable", name:"enable : false,", iconSkin:"edit", showAPI:true},
			{id:604, pId:60, t:"removeTitle", name:"removeTitle : \"remove\",", iconSkin:"edit", showAPI:true},
			{id:605, pId:60, t:"renameTitle", name:"renameTitle : \"rename\",", iconSkin:"edit", showAPI:true},
			{id:606, pId:60, t:"showRemoveBtn", name:"showRemoveBtn : true,", iconSkin:"edit", showAPI:true},
			{id:607, pId:60, t:"showRenameBtn", name:"showRenameBtn : true", iconSkin:"edit", showAPI:true},
			{id:61, pId:1, name:"},"},
			{id:62, pId:1, name:""},

			{id:70, pId:1, t:"view", name:"view : {", open:true},
			{id:7001, pId:70, t:"addDiyDom", name:"addDiyDom : null,", iconSkin:"core", showAPI:true},
			{id:7002, pId:70, t:"addHoverDom", name:"addHoverDom : null,", iconSkin:"edit", showAPI:true},
			{id:7003, pId:70, t:"autoCancelSelected", name:"autoCancelSelected : true,", iconSkin:"core", showAPI:true},
			{id:7004, pId:70, t:"dblClickExpand", name:"dblClickExpand : true,", iconSkin:"core", showAPI:true},
			{id:7005, pId:70, t:"expandSpeed", name:"expandSpeed : \"fast\",", iconSkin:"core", showAPI:true},
			{id:7006, pId:70, t:"fontCss", name:"fontCss : {},", iconSkin:"core", showAPI:true},
			{id:7012, pId:70, t:"nameIsHTML", name:"nameIsHTML : false,", iconSkin:"core", showAPI:true},
			{id:7007, pId:70, t:"removeHoverDom", name:"removeHoverDom : null,", iconSkin:"edit", showAPI:true},
			{id:7008, pId:70, t:"selectedMulti", name:"selectedMulti : true,", iconSkin:"core", showAPI:true},
			{id:7009, pId:70, t:"showIcon", name:"showIcon : true,", iconSkin:"core", showAPI:true},
			{id:7010, pId:70, t:"showLine", name:"showLine : true,", iconSkin:"core", showAPI:true},
			{id:7011, pId:70, t:"showTitle", name:"showTitle : true", iconSkin:"core", showAPI:true},
			{id:71, pId:1, name:"}"},

			{id:2, pId:0, name:"}"}
		];

		var treenode_nodes =[
			{id:1, pId:0, t:"treeNode", name:"treeNode : {", open:true},
			{id:101, pId:1, t:"checked", name:"checked", iconSkin:"check", showAPI:true},
			{id:102, pId:1, t:"children", name:"children", iconSkin:"core", showAPI:true},
			{id:128, pId:1, t:"chkDisabled", name:"chkDisabled", iconSkin:"check", showAPI:true},
			{id:127, pId:1, t:"click", name:"click", iconSkin:"core", showAPI:true},
			{id:103, pId:1, t:"getCheckStatus", name:"getCheckStatus ()", iconSkin:"check", showAPI:true},
			{id:104, pId:1, t:"getNextNode", name:"getNextNode ()", iconSkin:"core", showAPI:true},
			{id:105, pId:1, t:"getParentNode", name:"getParentNode ()", iconSkin:"core", showAPI:true},
			{id:106, pId:1, t:"getPreNode", name:"getPreNode ()", iconSkin:"core", showAPI:true},
			{id:129, pId:1, t:"halfCheck", name:"halfCheck", iconSkin:"check", showAPI:true},
			{id:107, pId:1, t:"icon", name:"icon", iconSkin:"core", showAPI:true},
			{id:108, pId:1, t:"iconClose", name:"iconClose", iconSkin:"core", showAPI:true},
			{id:109, pId:1, t:"iconOpen", name:"iconOpen", iconSkin:"core", showAPI:true},
			{id:110, pId:1, t:"iconSkin", name:"iconSkin", iconSkin:"core", showAPI:true},
			{id:131, pId:1, t:"isHidden", name:"isHidden", iconSkin:"hide", showAPI:true},
			{id:111, pId:1, t:"isParent", name:"isParent", iconSkin:"core", showAPI:true},
			{id:132, pId:1, t:"name", name:"name", iconSkin:"core", showAPI:true},
			{id:112, pId:1, t:"nocheck", name:"nocheck", iconSkin:"check", showAPI:true},
			{id:113, pId:1, t:"open", name:"open", iconSkin:"core", showAPI:true},
			{id:133, pId:1, t:"target", name:"target", iconSkin:"core", showAPI:true},
			{id:134, pId:1, t:"url", name:"url", iconSkin:"core", showAPI:true},
			{id:114, pId:1, t:"diy", name:"*DIY*", iconSkin:"core", showAPI:true},
			{id:115, pId:1, name:""},
			{id:116, pId:1, t:"check_Child_State", name:"[check_Child_State]", iconSkin:"check", showAPI:true},
			{id:117, pId:1, t:"check_Focus", name:"[check_Focus]", iconSkin:"check", showAPI:true},
			{id:118, pId:1, t:"checkedOld", name:"[checkedOld]", iconSkin:"check", showAPI:true},
			{id:119, pId:1, t:"editNameFlag", name:"[editNameFlag]", iconSkin:"edit", showAPI:true},
			{id:120, pId:1, t:"isAjaxing", name:"[isAjaxing]", iconSkin:"core", showAPI:true},
			{id:121, pId:1, t:"isFirstNode", name:"[isFirstNode]", iconSkin:"core", showAPI:true},
			{id:122, pId:1, t:"isHover", name:"[isHover]", iconSkin:"edit", showAPI:true},
			{id:123, pId:1, t:"isLastNode", name:"[isLastNode]", iconSkin:"core", showAPI:true},
			{id:124, pId:1, t:"level", name:"[level]", iconSkin:"core", showAPI:true},
			{id:125, pId:1, t:"parentTId", name:"[parentTId]", iconSkin:"core", showAPI:true},
			{id:126, pId:1, t:"tId", name:"[tId]", iconSkin:"core", showAPI:true},
			{id:130, pId:1, t:"zAsync", name:"[zAsync]", iconSkin:"core", showAPI:true},
			{id:2, pId:0, name:"}"}
		];

		var function_nodes =[
			{id:1, pId:0, t:"$.fn.zTree", name:"$.fn.zTree : {", open:true},
			{id:11, pId:1, t:"init", name:"init (obj, zSetting, zNodes)", iconSkin:"core", showAPI:true},
			{id:12, pId:1, t:"getZTreeObj", name:"getZTreeObj (treeId)", iconSkin:"core", showAPI:true},
			{id:14, pId:1, t:"destroy", name:"destroy (treeId)", iconSkin:"core", showAPI:true},
			{id:13, pId:1, t:"_z", name:"_z : {tools, view, event, data}", iconSkin:"core", showAPI:true},
			{id:2, pId:0, name:"}"},
			{id:3, pId:0, name:""},
			{id:4, pId:0, t:"zTreeObj", name:"zTreeObj : {", open:true},
			{id:401, pId:4, t:"setting", name:"setting", iconSkin:"core", showAPI:true},
			{id:402, pId:4, t:"addNodes", name:"addNodes (parentNode, newNodes, isSilent)", iconSkin:"core", showAPI:true},
			{id:403, pId:4, t:"cancelEditName", name:"cancelEditName (newName)", iconSkin:"edit", showAPI:true},
			{id:404, pId:4, t:"cancelSelectedNode", name:"cancelSelectedNode (node)", iconSkin:"core", showAPI:true},
			{id:405, pId:4, t:"checkAllNodes", name:"checkAllNodes (checked)", iconSkin:"check", showAPI:true},
			{id:406, pId:4, t:"checkNode", name:"checkNode (node, checked, checkTypeFlag, callbackFlag)", iconSkin:"check", showAPI:true},
			{id:407, pId:4, t:"copyNode", name:"copyNode (targetNode, node, moveType, isSilent)", iconSkin:"edit", showAPI:true},
			{id:436, pId:4, t:"destroy", name:"destroy ()", iconSkin:"core", showAPI:true},
			{id:408, pId:4, t:"editName", name:"editName (node)", iconSkin:"edit", showAPI:true},
			{id:409, pId:4, t:"expandAll", name:"expandAll (expandFlag)", iconSkin:"core", showAPI:true},
			{id:410, pId:4, t:"expandNode", name:"expandNode (node, expandFlag, sonSign, focus, callbackFlag)", iconSkin:"core", showAPI:true},
			{id:411, pId:4, t:"getChangeCheckedNodes", name:"getChangeCheckedNodes ()", iconSkin:"check", showAPI:true},
			{id:412, pId:4, t:"getCheckedNodes", name:"getCheckedNodes (checked)", iconSkin:"check", showAPI:true},
			{id:413, pId:4, t:"getNodeByParam", name:"getNodeByParam (key, value, parentNode)", iconSkin:"core", showAPI:true},
			{id:414, pId:4, t:"getNodeByTId", name:"getNodeByTId (tId)", iconSkin:"core", showAPI:true},
			{id:415, pId:4, t:"getNodeIndex", name:"getNodeIndex (node)", iconSkin:"core", showAPI:true},
			{id:416, pId:4, t:"getNodes", name:"getNodes ()", iconSkin:"core", showAPI:true},
			{id:431, pId:4, t:"getNodesByFilter", name:"getNodesByFilter (filter, isSingle, parentNode, invokeParam)", iconSkin:"core", showAPI:true},
			{id:417, pId:4, t:"getNodesByParam", name:"getNodesByParam (key, value, parentNode)", iconSkin:"core", showAPI:true},
			{id:418, pId:4, t:"getNodesByParamFuzzy", name:"getNodesByParamFuzzy (key, value, parentNode)", iconSkin:"core", showAPI:true},
			{id:419, pId:4, t:"getSelectedNodes", name:"getSelectedNodes ()", iconSkin:"core", showAPI:true},
			{id:432, pId:4, t:"hideNode", name:"hideNode (node)", iconSkin:"hide", showAPI:true},
			{id:433, pId:4, t:"hideNodes", name:"hideNodes (nodes)", iconSkin:"hide", showAPI:true},
			{id:420, pId:4, t:"moveNode", name:"moveNode (targetNode, node, moveType, isSilent)", iconSkin:"edit", showAPI:true},
			{id:421, pId:4, t:"reAsyncChildNodes", name:"reAsyncChildNodes (parentNode, reloadType, isSilent)", iconSkin:"core", showAPI:true},
			{id:422, pId:4, t:"refresh", name:"refresh ()", iconSkin:"core", showAPI:true},
			{id:423, pId:4, t:"removeChildNodes", name:"removeChildNodes (parentNode)", iconSkin:"core", showAPI:true},
			{id:424, pId:4, t:"removeNode", name:"removeNode (node, callbackFlag)", iconSkin:"core", showAPI:true},
			{id:425, pId:4, t:"selectNode", name:"selectNode (node, addFlag)", iconSkin:"core", showAPI:true},
			{id:430, pId:4, t:"setChkDisabled", name:"setChkDisabled (node, disabled, inheritParent, inheritChildren)", iconSkin:"check", showAPI:true},
			{id:426, pId:4, t:"setEditable", name:"setEditable (editable)", iconSkin:"edit", showAPI:true},
			{id:434, pId:4, t:"showNode", name:"showNode (node)", iconSkin:"hide", showAPI:true},
			{id:435, pId:4, t:"showNodes", name:"showNodes (nodes)", iconSkin:"hide", showAPI:true},
			{id:427, pId:4, t:"transformToArray", name:"transformToArray (nodes)", iconSkin:"core", showAPI:true},
			{id:428, pId:4, t:"transformTozTreeNodes", name:"transformTozTreeNodes (simpleNodes)", iconSkin:"core", showAPI:true},
			{id:429, pId:4, t:"updateNode", name:"updateNode (node, checkTypeFlag)", iconSkin:"core", showAPI:true},
			{id:5, pId:0, name:"}"}
		];

		apiContent.zTree_Setting = $.fn.zTree.init($("#settingTree"), $.fn.zTree._z.tools.clone(setting), setting_nodes);
		apiContent.zTree_Node = $.fn.zTree.init($("#treenodeTree"), $.fn.zTree._z.tools.clone(setting), treenode_nodes);
		apiContent.zTree_Function = $.fn.zTree.init($("#functionTree"), $.fn.zTree._z.tools.clone(setting), function_nodes);
		this.bindEvent();

	},
	bindEvent: function() {
		$(document).bind("keydown", this.listenKeyDown)
		this.overlayCloseBtn.bind("click", apiContent.overlayClose);
		this.searchResultInput.bind("click", function(e) {
			$(this).prev().get(0).focus();
			this.blur();
		}).bind("focus", function(e) {
			this.blur();
		});
		this.searchKey.bind("focus", this.focusKey)
			.bind("blur", this.blurKey)
			.bind("propertychange", this.searchNode)
			.bind("input", this.searchNode);
		this.searchPrevBtn.bind("click", this.searchPrev);
		this.searchNextBtn.bind("click", this.searchNext);
	},
	setSameKey: function(value) {
		apiContent.searchKey.attr("value", value);
	},
	focusKey: function(e) {
		if (apiContent.searchKey.hasClass("empty")) {
			apiContent.searchKey.removeClass("empty");
		}
	},
	blurKey: function(e) {
		apiContent.setSameKey(e.target.value);
		if (e.target.value === "") {
			apiContent.searchKey.addClass("empty");
		}
	},
	listenKeyDown: function(e) {
		if (e.keyCode=="13" && apiContent.overlayDiv.is(":hidden")) {
			apiContent.openAPI();
		} else if (e.keyCode=="37") {
			apiContent.searchPrev();
		} else if (e.keyCode=="13" || e.keyCode=="39") {
			apiContent.searchNext();
		}
	},
	openAPI: function() {
		if (apiContent.searchNodes.length > 0) {
			var setting_zTree = $.fn.zTree.getZTreeObj("settingTree"),
			treenode_zTree = $.fn.zTree.getZTreeObj("treenodeTree"),
			function_zTree = $.fn.zTree.getZTreeObj("functionTree");
			if (apiContent.searchNodesCur < 0 || apiContent.searchNodesCur > apiContent.searchNodes.length -1) {
				apiContent.searchNodesCur = 0;
			}
			var node = apiContent.searchNodes[apiContent.searchNodesCur];

			if (node.tId.indexOf("setting") > -1) {
				setting_zTree.selectNode(node);
			} else if (node.tId.indexOf("treenode") > -1) {
				treenode_zTree.selectNode(node);
			} else {
				function_zTree.selectNode(node);
			}
			apiContent.beforeClick(node.tId.substring(0, node.tId.indexOf("_")), node, true);
			apiContent.searchCur();
		}
	},
	searchNode: function(e) {
		var setting_zTree = $.fn.zTree.getZTreeObj("settingTree"),
		treenode_zTree = $.fn.zTree.getZTreeObj("treenodeTree"),
		function_zTree = $.fn.zTree.getZTreeObj("functionTree");
		if (apiContent.curKey == e.target.value) return;
		apiContent.curKey = e.target.value;
		var value = $.trim(apiContent.curKey);
		apiContent.setSameKey(apiContent.curKey);
		if (apiContent.searchKey.hasClass("empty")) {
			value = "";
			apiContent.searchResultInput.removeClass("noResult").attr("value","");
		}
		if (apiContent.lastValue === value) return;

		apiContent.updateNodes(false);
		apiContent.lastValue = value;
		if (value === "" || value.length < 2) {
			apiContent.searchNodes = [];
			apiContent.searchNodesCur = -1;
			apiContent.searchCur(true);
			return;
		}

		var settingNodeList = setting_zTree.getNodesByFilter(apiContent.searchFilter);
		var functionNodeList = function_zTree.getNodesByFilter(apiContent.searchFilter);
		var treenodeNodeList = treenode_zTree.getNodesByFilter(apiContent.searchFilter);
		apiContent.searchNodes = settingNodeList.concat(functionNodeList).concat(treenodeNodeList);
		apiContent.searchNodesCur = -1;
		apiContent.searchCur();
		apiContent.updateNodes(true);
	},
	searchFilter: function(node) {
		var value = $.trim(apiContent.searchKey.get(0).value).toLowerCase();
		return (node.showAPI && node.name.toLowerCase().indexOf(value) > -1);
	},
	searchPrev: function(e) {
		if (apiContent.searchPrevBtn.hasClass("disabled")) return;
		apiContent.searchNodesCur--;
		if (apiContent.searchNodesCur < 0 || apiContent.searchNodesCur > apiContent.searchNodes.length -1) {
			apiContent.searchNodesCur = apiContent.searchNodes.length -1;
		}
		apiContent.openAPI();
	},
	searchNext: function(e) {
		if (apiContent.searchNextBtn.hasClass("disabled")) return;
		apiContent.searchNodesCur++;
		apiContent.openAPI();
	},
	searchCur: function(init) {
		var result = apiContent.searchNodes;
		if (init) {
			apiContent.searchResultInput.removeClass("noResult").attr("value","");
		} else if (result.length == 0) {
			apiContent.searchResultInput.addClass("noResult").attr("value","  [ 0 / 0 ]  ");
		} else {
			apiContent.searchResultInput.removeClass("noResult").attr("value"," [ " + (apiContent.searchNodesCur > -1 ? apiContent.searchNodesCur+1 : "?")+ " / " + result.length + " ] ");
		}
		if (result.length > 0) {
			apiContent.searchPrevBtn.removeClass("disabled");
			apiContent.searchNextBtn.removeClass("disabled");
		} else {
			apiContent.searchPrevBtn.addClass("disabled");
			apiContent.searchNextBtn.addClass("disabled");
		}
	},
	updateNodes: function(highlight) {
		var setting_zTree = $.fn.zTree.getZTreeObj("settingTree"),
		treenode_zTree = $.fn.zTree.getZTreeObj("treenodeTree"),
		function_zTree = $.fn.zTree.getZTreeObj("functionTree"),
		node = null;
		for( var i=0, l=apiContent.searchNodes.length; i<l; i++) {
			node = apiContent.searchNodes[i];
			if (node.level > 0) {
				node.highlight = highlight;
				if (node.tId.indexOf("setting") > -1) {
					setting_zTree.updateNode(node);
				} else if (node.tId.indexOf("treenode") > -1) {
					treenode_zTree.updateNode(node);
				} else {
					function_zTree.updateNode(node);
				}
			}
		}
	},
	getFontCss: function(treeId, treeNode) {
		return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
	},
	getTitle: function(treeId, node) {
		var t = [], n = node;
		while (n && !!n.t) {
			t.push(n.t);
			n = n.getParentNode();
		}
		t = t.reverse();
		node.tt = t.join('.');
		return true;
	},
	showIcon: function(treeId, node) {
		return (!!node.iconSkin);
	},
	onNodeCreated: function (e, treeId, node) {
		var a = $("#" + node.tId + "_a");
		if (node.showAPI) {
			a.attr("rel", "#overlayDiv");
		} else {
			a.css({cursor: "default"});
		}
	},
	beforeClick: function (treeId, node, noClear) {
		if (!node.showAPI) return false;
		var o = $("#" + node.tId + "_a");
		if (!!apiContent.apiCache[node.tId]) {
			apiContent.tmpDiv.html(apiContent.apiCache[node.tId]);
			apiContent.overlayShow(o, (apiContent.lastNode === node));
		} else {
			apiContent.overlayAjax(treeId, node);
		}
		apiContent.lastNode = node;
		if (node.tId.indexOf("settingTree")>-1) {
			apiContent.settingDiv.removeClass("right").addClass("left");
			apiContent.functionDiv.removeClass("left").addClass("right");
		} else {
			apiContent.settingDiv.removeClass("left").addClass("right");
			apiContent.functionDiv.removeClass("right").addClass("left");
		}

		if (!noClear) {
			apiContent.clearSelectedNode();
		}
		return true;
	},
	clearSelectedNode: function() {
		apiContent.zTree_Setting.cancelSelectedNode();
		apiContent.zTree_Node.cancelSelectedNode();
		apiContent.zTree_Function.cancelSelectedNode();
	},
	overlayAutoClose: function(e) {
		var eId = e.target.id, eRel = e.target.getAttribute("rel"), eClass = e.target.className;
		if (eId === "overlayDiv" || eId === "overlayDivArrow" || eClass.indexOf("searchPrev") > -1 || eClass.indexOf("searchNext") > -1 || !!eRel) return;
		if (!$(e.target).parents("[rel]").length && !$(e.target).parents("#overlayDiv").length) {
			apiContent.overlayClose();
		}
	},
	overlayClose: function() {
		var o = apiContent.overlayDiv;
		o.stop();
		apiContent.clearSelectedNode();
		if (ie) {
			o.hide();
		} else {
			setTimeout(function() {o.fadeTo("fast", 0, function(){o.hide();})}, 200);
		}
		$(document).unbind("click", apiContent.overlayAutoClose);
	},
	overlayShow: function(target, isSameNode) {
		var w = $(window), o = apiContent.overlayDiv, a = apiContent.overlayArrow,
		oc = apiContent.overlayContent, c = apiContent.contentBoxDiv,
		t = target.offset().top - 30,
		cMaxLeft = c.offset().left + c.outerWidth({margin:true}) - o.outerWidth({margin:true}) - 10,
		l = Math.min(cMaxLeft, target.offset().left + target.width() + 40),
		arrowT = target.offset().top + 16,
		wMinTop = 100, footerHeight = 50, onlyFade = false,
		wHeight = w.height(), wScrollTop=w.scrollTop(), wMaxTop = wHeight + wScrollTop - footerHeight;
		if (!apiContent.overlayMaxTop) {
			apiContent.overlayMaxTop = apiContent.contentBoxDiv.offset().top + apiContent.contentBoxDiv.height();
		}
		o.stop();
		if (o.css("display") !== "block") {
			o.css({top: t, left: l});
			a.css({top:arrowT - t});
			$(document).bind("click", apiContent.overlayAutoClose);
		}
		if (ie) {
			onlyFade = true;
			o.show();
		} else {
			o.fadeTo("fast", 1);
		}

		var h = apiContent.tmpDiv.outerHeight({margin:true}) + apiContent.overlaySearch.outerHeight();
		if ((t + h) > wMaxTop) {
			t = wMaxTop - h;
		}
		if ((t + h) > apiContent.overlayMaxTop) {
			t = apiContent.overlayMaxTop - h;
		}
		t = Math.max(t, wScrollTop, wMinTop);
		if ((t + h) > ($("body").height()-footerHeight-20)) {
			o.css("padding-bottom", footerHeight + "px");
		} else {
			o.css("padding-bottom", "0");
		}
		apiContent.overlayDetailDiv.empty();
		apiContent.overlayDetailDiv.append(apiContent.tmpDiv.children());
		if (!onlyFade) {
			onlyFade = (isSameNode && t === parseInt(o.css("top").replace("px", "")));
		}

		a.removeClass("reverse");
		if ( (arrowT - t) > (h-55) ) {
			a.addClass("reverse");
			arrowT -= 55;
		}

		if (onlyFade) {
			o.css({top: t, left: l});
			oc.css({height: h});
			a.css({top:arrowT - t});
		} else {
			o.animate({top: t, left: l}, {duration: "normal",easing: "swing", complete:null});
			oc.animate({height: h}, {duration: "fast",easing: "swing", complete:null});
			a.animate({top:arrowT - t}, {duration: "normal",easing: "linear", complete:null});
		}
	},
	overlayAjax: function(treeId, node) {
		var o = $("#" + node.tId + "_a");
		if (node.isAjax) return;
		node.isAjax = true;
		$.ajax({
			type: "get",
			url: "" + lang + "/" + node.tt.replace("$.", "") + ".html",
			data: null,
			dataType: "text",
			success: function(msg) {
				if (!apiContent.tmpDiv) {
					var tmpDiv = $(document.createElement("div"));
					tmpDiv.addClass("baby_overlay_tmp");
					$("body").append(tmpDiv)
					apiContent.tmpDiv = $(document.createElement("div"));
					apiContent.tmpDiv.addClass("details");
					tmpDiv.append(apiContent.tmpDiv);

				} else {
					apiContent.tmpDiv.empty();
				}
				apiContent.tmpDiv.html(msg);
				apiContent.overlayShow(o, false);
				apiContent.apiCache[node.tId] = msg;
				node.isAjax = false;
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert(ajaxMsg)
				if (apiContent.tmpDiv) apiContent.tmpDiv.empty();
				node.isAjax = false;
			}
		});
	}
}