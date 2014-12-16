/**
 * jqGrid 封装类
 * @author ThinkGem@163.COM
 * @version 2014-8-22
 */
var DataGrid = function(options){
	
	var dataGrid = options.dataGrid ? options.dataGrid : $("#dataGrid");
	var dataGridPage = options.dataGridPage ? options.dataGridPage : $("#dataGridPage");
	var searchForm = options.searchForm ? options.searchForm : $("#searchForm");
	
	options = $.extend({
		url: searchForm.attr('action'),
		postData: searchForm.serializeArray(),
		mtype: "POST", datatype: "json",
		jsonReader: { 	// 自定义表格的JSON读取参数
			id: options.dataId, root: "list", page: "pageNo", 
			total: "totalPage", records: "count", subgrid: {root:"list"}
		},
		treeReader: {	// 自定义树表格JSON读取参数
			level_field: "sortGrade", parent_id_field: "parentCode",
			leaf_field: "isLeaf", expanded_field: "isOpen", icon_field: "_icon"
		},
		prmNames: {		// 自定义Ajax请求参数
			page:"pageNo", rows:"pageSize", sort: "orderBy",
			order: "sord", search:"_search", nd:"nd", id:"id",
			oper:"oper",editoper:"edit",addoper:"add",deloper:"del", 
			subgridid:"id", npage: null, totalrows:"totalPage"
		},
		rowNum: -1, 		// 显示行数，-1为显示全部
		rownumWidth: 30,	// 序号列宽
		multiboxonly: true,	// 单击复选框时在多选
		altRows: true, 		// 斑马线样式，交替行altclass

		// 当前页签编号
		tabPageId: '',
		
		// 设置列模型
		columnModel: [], colNames: [], colModel: options.columnModel,

		// 列表参数
		dataId: 'id', 		// 指定数据主键
		showRownum: true,	// 是否显示行号
		showCheckbox: false,// 是否显示复选框
		sortable: true,	// 列表是否允许支持
		
		autoGridHeight: true, // 自动表格高度
		autoGridHeightFix: 0,  // 自动表格高度宽度
		autoGridWidth: true,  // 自动表格宽度
		autoGridWidthFix: 0,  // 自动表格修复宽度
		
		// 树结构表格
		treeGrid: false,							// 启用树结构表格
		treeGridModel: 'adjacency',					// 启用简单结构模式
		ExpandColClick: true,						// 单击列可展开
		ExpandColumn: options.treeColumn,			// 要展开的列
		defaultExpandLevel: 0,						// 默认展开的层次
		initExpandLevel: options.defaultExpandLevel,// 保存初始化是设置的展开层次

		// 窗体按钮绑定
        btnSearch: $("#btnSearch"), 					// 查询按钮
        btnRefreshTree: $("#btnRefreshTree"), 			// 刷新树按钮
        btnExpandTreeNode: $("#btnExpandTreeNode"), 	// 展开树节点按钮
        btnCollapseTreeNode: $("#btnCollapseTreeNode"), // 折叠树节点按钮

        // 分页相关字段
        inputPageNo: $("#pageNo", searchForm),		// 当前页码字段
        inputPageSize: $("#pageSize", searchForm),	// 页面大小字段
        inputOrderBy: $("#orderBy", searchForm),	// 排序字段
        
        // 数据请求前调用方法
		beforeRequest: function(data){
			loading();
			// 如果是树结构表格
			if (options.treeGrid){
				// 一次性查询设置
				var postData = getParam('postData');
				if (postData.id){
					setParam({postData: {id: postData.id}});
				}else if (postData.nodeid){
					setParam({postData: {parentCode: postData.nodeid}});
				}
				// 设置请求参数
				else{
					setParam({postData: searchForm.serializeArray()});
				}
			}else{
				// 设置请求参数
				setParam({postData: searchForm.serializeArray()});
			}
			
			// 请求加载前调用方法
			if (typeof options.ajaxLoad == 'function'){
				options.ajaxLoad(data);
			}
			
			$('.btn').attr("disabled", true);
			$('.ui-jqgrid .loading').remove();
		},
		
		// 数据请求完成调用方法
		loadComplete: function(data){
			
			// 如果是树结构表格
			if (options.treeGrid){
				
				// 展开等待展开树节点
				if (dataGrid.expandNodeIds){
					setTimeout(function(){
						if (dataGrid.expandNodeIds.length > 0){
							$('#'+dataGrid.expandNodeIds.shift()+':visible .tree-plus', dataGrid).click();
						}else{
							if (dataGrid.currentLevel < dataGrid.expandLevel){
								dataGrid.currentLevel++;
								dataGrid.expandNodeIds = [];
								$('.jqgrow:visible .tree-plus', dataGrid).each(function(){
									var id = $(this).parents('.jqgrow').attr('id');
									dataGrid.expandNodeIds.push(id);
								});
								$('#'+dataGrid.expandNodeIds.shift()+':visible .tree-plus', dataGrid).click();
							}else{
								// 如果已经展开完成，则销毁展开ID队列
								dataGrid.expandNodeIds = null;
							}
						}
					}, 10);
				}
				// 树加载后的默认展开级别
				else if (options.defaultExpandLevel && options.defaultExpandLevel > 0){
					expandTreeNode(options.defaultExpandLevel);
					options.defaultExpandLevel = 0;
				}
				// 一次性查询，需清除对应参数
				setParam({postData: {id: '', nodeid: ''}});
			}else{
				// 显示分页代码
				if (data && data.html){
					dataGridPage.html(data.html);
				}
			}
			
			// 请求成功之后调用方法
			if (typeof options.ajaxSuccess == 'function'){
				options.ajaxSuccess(data);
			}
			
			// 绑定列表按钮事件
			if (typeof options.btnEventBind == 'function'){
				options.btnEventBind($('.btnList'));
			}
			
			resizeDataGrid();
			$('.btn').attr("disabled", false);
			closeTip();
		},
		loadError: function(data){
			if (typeof options.ajaxError == 'function'){
				options.ajaxError(data);
			}
			$('.btn').attr("disabled", false);
			showTip('操作失败，' + data.responseText);
			closeTip(0, true);
		},
		gridComplete : function() {
			if (typeof options.complete == 'function'){
				options.complete();
			}
			resizeDataGrid();
		},
		onSortCol: function (index, iCol, sortorder) {
			if (options.inputOrderBy && options.inputOrderBy.length){
				options.inputOrderBy.val(index + ' ' + sortorder);
				//setParam({postData: searchForm.serializeArray()});
			}
        },
//		ondblClickRow: function(id, rownum, colnum, event){
////		$('.jqgrow td').attr('onselectstart', 'return false;').css('style', '-moz-user-select:none;');
//			if(document.selection && document.selection.empty) {
//		        document.selection.empty();
//		    }
//		    else if(window.getSelection) {
//		        var sel = window.getSelection();
//		        sel.removeAllRanges();
//		    }
//			$('#' + id + '.jqgrow a:first').click();
//		},
		
//		// 设置多级表头
//		groupHeaders: {
//	 		twoLevel:[
//	 			{startColumnName: 'postCode', numberOfColumns: 2, titleText: '二级表头'},
//	 			{startColumnName: 'remarks', numberOfColumns:2, titleText:'二级表头2'}
//	 		],
//	 		threeLevel:[
//	 			{startColumnName: 'postCode', numberOfColumns:4, titleText:'三级表头'}
//	 		]
//		},
//		frozenCols: true, 	// 冻结列，在colModel指定frozen: true
//		showFooter: true,	// 是否显示底部合计行
		
        // 按钮事件绑定
		btnEventBind: function(elements){ 
			elements.each(function(){
				var clickBinded = $(this).attr('data-click-binded');
				if (clickBinded == undefined){
					// 防止重复绑定
					$(this).attr('data-click-binded', true);
					// 绑定按钮单击事件
					$(this).click(function(){
						var se = $(this);
						var url = se.attr('href');
						var title = se.data('title');
						if (title == undefined){
							title = se.attr('title');
						}
						var confirm = se.data('confirm');
						if(confirm != undefined){
							confirmx(confirm, url, function(data){
								showTip(data.message);
								if(data.result==Global.TRUE){
									var confirmSuccess = se.data('confirmSuccess');
									if (confirmSuccess != undefined){
										try{
											eval(confirmSuccess);
										}catch(e){
											Log.log('confirmSuccess error: ' + e);
										}
									}else{
										// 如果是树结构表格
										if (options.treeGrid){
											var row = getRowData(se.parents('.jqgrow').attr('id'));
											if (row && !isRoot(row.parentCode)){
												refreshTree(1, row.parentCode);
											}else{
												refreshTree();
											}
										}else{
											refresh();
										}
									}
								}
							}, "json");
						}else{
							addTabPage($(this), title, url, options.tabPageId);
						}
						return false;
					});
				}
			});
			return self;
		},
		ajaxLoad: function(data){ // 加载前执行方法
			
		},
		ajaxSuccess: function(data){ // 加载成功后执行方法
			
		},
		ajaxError: function(data){ // 加载失败后执行方法
			
		},
		complete: function(){ // 表格加载完成后执行
			
		}
    }, options);
	
	// 获取列标题
	options.colNames = [];
	for(var i=0; i<options.colModel.length; i++){
		options.colNames.push(options.colModel[i].header);
		// 如果是树结构表格
		if (options.treeGrid || !options.sortable){
			options.colModel[i].sortable = false; // 是否排序列
		}
	}
	
	// 如果是树结构表格
	if (options.treeGrid){
		options.showRownum = false;	// 是否显示行号
		options.showCheckbox = false; // 是否显示复选框
	}
	
	// 显示序号
	if (options.showRownum){
		options.rownumbers = true;	// 显示序号
	}
	
	// 显示多选复选框
	if (options.showCheckbox){
		options.multiselect = true;	// 显示多选复选框
	}
	
	// 如果设置了多级表头或冻结列
	if (options.groupHeaders || options.frozenCols){
		options.shrinkToFit = false;// 不按百分比自适应列宽
	}else{
		options.shrinkToFit = true;	// 按百分比自适应列宽
	}
	
	// 是否显示合计行
	if (options.showFooter){
		options.footerrow = true; 		// 显示底部合计行
		options.userDataOnFooter = true; // 使用json数据作为合计行数据
	}

	// 添加分页及排序参数
	if(options.inputPageNo.length == 0){
		var pageNo = searchForm.data('pageNo');
		searchForm.append('<input id="pageNo" name="pageNo" type="hidden" value="'+(pageNo?pageNo:'')+'"/>');
		options.inputPageNo = $("#pageNo", searchForm);
	}
	if(options.inputPageSize.length == 0){
		var pageSize = searchForm.data('pageSize');
		searchForm.append('<input id="pageSize" name="pageSize" type="hidden" value="'+(pageSize?pageSize:'')+'"/>');
		options.inputPageSize = $("#pageSize", searchForm);
	}
	if(options.inputOrderBy.length == 0){
		var orderBy = searchForm.data('orderBy');
		searchForm.append('<input id="orderBy" name="orderBy" type="hidden" value="'+(orderBy?orderBy:'')+'"/>');
		options.inputOrderBy = $("#orderBy", searchForm);
	}
	
	// 绑定查询表单提交事件
	searchForm.submit(function(){
		refresh();
		return false;
	});
	
	// 绑定工具条上的按钮
	if (typeof options.btnEventBind == 'function'){
		
		// 绑定工具栏上的按钮
		options.btnEventBind($('.btnTool'));
		
		// 绑定查询按钮
		options.btnSearch.click(function(){
			searchForm.toggleClass('hide');
			resizeDataGrid();
			return false;
		});
		
		// 绑定刷新按钮
		options.btnRefreshTree.click(function(){
			$('input[type=reset]', searchForm).click();
			refreshTree();
			return false;
		});
		
		// 绑定全部展开按钮
		options.btnExpandTreeNode.click(function(){
			expandTreeNode(1);
			return false;
		});
		
		// 绑定全部折叠按钮
		options.btnCollapseTreeNode.click(function(){
			collapseTreeNode();
			return false;
		});
	};
	
	// 初始化jqGrid
	dataGrid.jqGrid(options);
	
	// 如果设置了多级表头
	if (options.groupHeaders && options.groupHeaders.twoLevel){

	 	// 设置三级表头
		if (options.groupHeaders.threeLevel){
		 	dataGrid.jqGrid('setComplexHeaders', {
		 	  complexHeaders:{
		 		defaultStyle:true,
		 		twoLevel: options.groupHeaders.twoLevel,
		 		threeLevel: options.groupHeaders.threeLevel
		 	  }
		 	});
		}
		
	 	// 设置二级表头
		else{
		 	dataGrid.jqGrid('setGroupHeaders', {
		 	  useColSpanStyle: true, 
		 	  groupHeaders: options.groupHeaders.twoLevel
		 	});
		}
		
	}
	
	// 是否冻结列
	if (options.frozenCols){
		dataGrid.jqGrid('setFrozenColumns'); // 冻结列，在colModel指定frozen: true
	}	
	
	// 自动调整表格大小
	$(window).resize(function(){
		resizeDataGrid();
	});
	resizeDataGrid();

	/**
	 * 自动调整表格大小
	 */ 
	function resizeDataGrid(){

		// 获取Grid上级元素
		var jqGridParent = $(dataGrid).parents('.ui-jqgrid').parent();

		// 设置表格高度，最小高度150像素
		var setGridHeight = function(){
			if (!options.autoGridHeight){
				return;
			}
			var tabContent = $(dataGrid).parents('.tab-content');
			if (tabContent.length > 0){
				var headerHeight = 30;
				if (options.groupHeaders && options.groupHeaders.twoLevel){
					if (options.groupHeaders.threeLevel){
						headerHeight *= 3;
					}else{
						headerHeight *= 2;
					}
				}
				var gridHeight = tabContent.height() - headerHeight - options.autoGridHeightFix;
				dataGrid.jqGrid('setGridHeight', gridHeight);
			}else{
				var gridHeight = $(dataGrid).height();
				var gridParent = $(dataGrid).parent();
				if (gridParent.length != 0){
					gridHeight = gridParent.height();
				}
				gridHeight = ($(window).height() - $('body').height() + gridHeight - options.autoGridHeightFix);
				if (gridHeight < 150){
					gridHeight = 150; // 最小高度
				}
				gridParent.height(gridHeight);
				dataGrid.jqGrid('setGridHeight', gridHeight);
			}
//			var outerHeight = 0;
//			jqGridParent.children().not(jqGrid).each(function(){
//				var height = $(this).outerHeight();
//				outerHeight += height > 0 ? height : 0;
//			});
//			var bodyHeight = jqGridParent.parents('.portlet-body').height();
//			var gridHeight = bodyHeight - outerHeight;
//			dataGrid.jqGrid('setGridHeight', gridHeight);
		}
		setGridHeight();

		// 设置表格宽度，最小宽度500像素
		var setGridWidth = function(){
			if (!options.autoGridWidth){
				return;
			}
			var gridWidth = 0;
			if (jqGridParent.is(':visible')){
				gridWidth = jqGridParent.width() - 2;
			}else{
				gridWidth = $(window).width() - 24;
			}
			dataGrid.jqGrid('setGridWidth', gridWidth - options.autoGridWidthFix, (options.shrinkToFit && $(window).width() > 500));
		};
		setGridWidth();

		// 修正ie8弹出框内的grid计算不正确问题
		if (!! navigator.userAgent.match(/MSIE 8.0/)){
			setTimeout(function(){
				setGridHeight();
				setGridWidth();
			}, 10);
		}
		
 		// 底部合计行修正
		if (options.showFooter){
			setTimeout(function(){
		 		$(".ui-jqgrid-bdiv>div").css({"height":"auto"});
		 		$(".ui-jqgrid-bdiv").css({"padding-bottom":"24px"});
		 		$(".ui-jqgrid-sdiv").width($(".ui-jqgrid-sdiv").width()-12);
		 		$(".ui-jqgrid-sdiv").css({"position":"absolute","bottom":"12px"});
		 		$(".ui-jqgrid-sdiv td").css({"border-bottom":"solid 1px #ddd"});
		 		$(".ui-jqgrid-ftable td").not("td.jqgrid-rownum").css({"background":"#FFFFE7"});
		 		$(".frozen-sdiv.ui-jqgrid-sdiv").css({"top":"auto","z-index":"1"});
		 		$(".frozen-sdiv td:eq(2)").html("&nbsp;合计：");
			}, 100);
		}
	}

	/**
	 * 判断编号是否是根节点
	 */
	function isRoot(code){
		return !code || code == '' || code == '0';
	}
	
	/**
	 * 获取参数
	 */
	function getParam(paramName){
		return dataGrid.jqGrid('getGridParam', paramName);
	};

	/**
	 * 设置参数
	 */
	function setParam(params){
		dataGrid.jqGrid('setGridParam', params);
		return self;
	};
	
	/**
	 * 获取参数
	 */
	function getRowData(id){
		return dataGrid.jqGrid("getRowData", id);
	};

	/**
	 * 获取选择行ID
	 */
	function getSelectRow(){
		return getParam("selrow");
	};
	
	/**
	 * 获取选择行数组ID
	 */
	function getSelectRows(){
		return getParam("selarrrow");
	};

	/**
	 * 设置选择行
	 */
	function setSelectRow(id){
		if (id == undefined){
			dataGrid.jqGrid("resetSelection");
		}
		dataGrid.jqGrid("setSelection", id);
		return self;
	};
	
	/**
	 * 刷新表格
	 */
	function refresh(pageNo, pageSize){
		if(pageNo){
			options.inputPageNo.val(pageNo);
		}
		if(pageSize){
			options.inputPageSize.val(pageSize);
		}
		var params = {};
		if (options.inputPageNo.val() != ''){
			params.page = options.inputPageNo.val();
		}
		if (options.inputPageSize.val() != ''){
			params.rowNum = options.inputPageSize.val();
		}
		//params.postData = searchForm.serializeArray();
		setParam(params);
		dataGrid.trigger('reloadGrid');
		return self;
	};
	
	/**
	 * 删除树节点
	 */
	function delTreeNode(id){
		dataGrid.jqGrid("delTreeNode", id);
		return self;
	};

	/**
	 * 展开树节点
	 * @param level 一次展开的层次
	 */
	function expandTreeNode(level){
		loading();
		setTimeout(function(){
			
			// 一次展开的层次
			dataGrid.expandLevel = level;
			
			// 按层次展开节点
			dataGrid.currentLevel = 1;
			dataGrid.expandNodeIds = [];
			$('.jqgrow:visible .tree-plus', dataGrid).each(function(){
				var id = $(this).parents('.jqgrow')[0].id;
				dataGrid.expandNodeIds.push(id);
			});
			if (dataGrid.expandNodeIds.length > 100){
				showTip("本次需要展开“" + dataGrid.expandNodeIds.length
						+ "”个节点，因为展开节点过多，执行取消。");
				dataGrid.expandNodeIds = [];
			}else{
				$('#'+dataGrid.expandNodeIds.shift()+':visible .tree-plus', dataGrid).click();
			}
			
			// 如果已加载，则直接展开
	 		for (var i=0; i<dataGrid.expandLevel; i++){
	 			$('.jqgrow:visible .tree-plus', dataGrid).click();
	 		}
			
			closeTip();
		}, 10);
		return self;
	};
	
	/**
	 * 折叠树节点
	 */
	function collapseTreeNode(){
		loading();
		setTimeout(function(){
			$('.tree-minus', dataGrid).click();
			closeTip();
		}, 10);
		return self;
	};
	
	/**
	 * 刷新树表格
	 */
	function refreshTree(expandLevel, parentCode){
		if (expandLevel){
			options.defaultExpandLevel = expandLevel;
		}
		if (!isRoot(parentCode)){
			setParam({postData: {id: parentCode}});
		}else if (parentCode != undefined){
			options.defaultExpandLevel = options.initExpandLevel;
		}
		dataGrid.trigger('reloadGrid');
		return self;
	};
	
	// public method
	var self = {
		
		/**
		 * 直接调用jqGrid对象方法
		 */
		jqGrid: function(m, v){
			return dataGrid.jqGrid(m, v);
		},
		
		/**
		 * 设置参数
		 */
		setParam: function(params){
			return setParam(params);
		},
		
		/**
		 * 获取参数
		 */
		getParam: function(paramName){
			return getParam(paramName);
		},
		
		/**
		 * 获取行数据
		 */
		getRowData: function(rowId){
			return getRowData(rowId);
		},
		
		/**
		 * 获取选择行ID
		 */
		getSelectRow: function(){
			return getSelectRow();  
		},
		
		/**
		 * 获取选择行数组ID
		 */
		getSelectRows: function(){
			return getSelectRows();  
		},
		
		/**
		 * 设置选择行
		 */
		setSelectRow: function(id){
			return setSelectRow(id);
		},
		
		/**
		 * 刷新表格
		 */
		refresh: function(pageNo, pageSize){
			return refresh(pageNo, pageSize);
		},
		
		/**
		 * 删除树节点
		 */
		delTreeNode: function(id){
			return delTreeNode(id);
		},
		
		/**
		 * 展开树节点
		 * @param level 一次展开的层次
		 */
		expandTreeNode: function(level){
			return expandTreeNode(level);
		},

		/**
		 * 折叠树节点
		 */
		collapseTreeNode: function(){
			collapseTreeNode();
		},
		
		/**
		 * 刷新树表格
		 */
		refreshTree: function(expandLevel, parentCode){
			return refreshTree(expandLevel, parentCode);
		},
		
		/**
		 * 按钮事件绑定
		 */
		btnEventBind: function(elements){
			return btnEventBind(elements);
		}
	};
	
	return self;
};

/**
 * 翻页方法
 */
function page(pageNo, pageSize, param){
	try{
		if (param && param != ''){
			dataGrids[param].refresh(pageNo, param);
		}else{
			dataGrid.refresh(pageNo, param);
		}
	}catch(e){
	}
	return false;
}

/**
 * 扩展方法
 */
$.extend($.jgrid,{
//	getIdsByLevel : function(lvl) {
//		var ids = [];
//		this.each(function(){
//			var $t = this;
//			if( !$t.grid || !$t.p.treeGrid ) {return;}
//			var expCol = $t.p.expColInd,
//			expanded = $t.p.treeReader.expanded_field,
//			isLeaf = $t.p.treeReader.leaf_field,
//			level = $t.p.treeReader.level_field,
//			icon = $t.p.treeReader.icon_field,
//			loaded = $t.p.treeReader.loaded;
//			var data = $t.p.data;
//			for (var i=0; i<data.length; i++){
//				if (!data[i][isLeaf] && data[i][level] == lvl){
//					ids.push(data[i]._id_);
//				}
//			};
//		});
//		return ids;
//	},
	/**
	 * 去掉HTML标记
	 */
	stripHtml : function(v) {
		v = String(v);
    	return v.replace(/<[^>]*>/g, "");
	}
});

/**
 * jqGrid complexGroupHeaders
 * name cj
 * email 85309651@qq.com
 * blog http://cjblog.iteye.com/
 * version 0.2
 * date 2012-06-03
**/
(function($){
	$.jgrid.extend({
		/**
		 * 三级表头 by ljq
		 * @参数 o : 三级表头参数
		 */
		setComplexHeaders : function ( o ) {
			o = $.extend({
				complexHeaders: {
					defaultStyle : true,
					threeLevel:[],
					twoLevel:[]
					}
			}, o || {});
			return this.each(function(){
				//三级表头和二级表头
				var complexHeaders = o.complexHeaders,
					threeHeaders = complexHeaders["threeLevel"],
					twoHeaders = complexHeaders["twoLevel"];
				if (threeHeaders.length === 0 || twoHeaders.length === 0) {
					return;	
				}
				this.p.complexHeader = o;
				var ts = this,
				defaultStyle = complexHeaders.defaultStyle === undefined ? true : complexHeaders.defaultStyle,
				i, cmi, $thirdHeaderRow, $fourHeaderRow, $colHeader, th, $th, thStyle,
				iCol,
				tempInCol,
				inThreeCol,
				inTwoCol,
				threeLevelSkip = 0, 
				twoLevelSkip = 0, 
				hasTwoLevel = false,
				cghi,
				numberOfColumns,
				titleText,
				cVisibleColumns,
				colModel = ts.p.colModel,
				cml = colModel.length,
				ths = ts.grid.headers,
				$htable = $("table.ui-jqgrid-htable", ts.grid.hDiv),
				$secondHeaderRow = $htable.children("thead").children("tr.ui-jqgrid-labels:last").addClass("jqg-second-row-header"),
				$thead = $htable.children("thead"),
				$theadInTable,
				originalResizeStop,
				$firstHeaderRow = $htable.find(".jqg-first-row-header");
				if ($firstHeaderRow[0] === undefined) {
					$firstHeaderRow = $('<tr>', {role: "row", "aria-hidden": "true"}).addClass("jqg-first-row-header").css("height", "auto");
				} else {
					$firstHeaderRow.empty();
				}
				var $firstRow,
				inColumnHeader = function (text, columnHeaders) {
					var i = 0, length = columnHeaders.length;
					for (; i < length; i++) {
						if (columnHeaders[i] && columnHeaders[i].startColumnName === text) {
							return i;
						}
					}
					return -1;
				};

				$(ts).prepend($thead);
				$thirdHeaderRow = $('<tr>', {role: "rowheader"}).addClass("ui-jqgrid-labels jqg-third-row-header");
				$fourHeaderRow = $('<tr>', {role: "rowheader"}).addClass("ui-jqgrid-labels jqg-four-row-header");
				
					  //循环模型每列
				for (i = 0; i < cml; i++) {
					th = ths[i].el;
					$th = $(th);
					cmi = colModel[i];
					// 给第一行添加单元格
					thStyle = { height: '0px', width: ths[i].width  +'px', display: (cmi.hidden ? 'none' : '')};
					$("<th>", {role: 'gridcell'}).css(thStyle).addClass("ui-first-th-"+ts.p.direction).appendTo($firstHeaderRow);
					//移除不需要的样式
					th.style.width = ""; 
					
					//是否在threeHeaders配置里面
					inThreeCol = inColumnHeader(cmi.name, threeHeaders);
					if (inThreeCol >= 0) {
						cghi = threeHeaders[inThreeCol];
						numberOfColumns = parseInt(cghi.numberOfColumns,10);
						titleText = cghi.titleText;

						// 计算显示的列长度
						for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
							if (!colModel[i + iCol].hidden) {
								cVisibleColumns++;
							}
						}
				
						$colHeader = $('<th>').attr({role: "columnheader",noWrap:true})
							.addClass("ui-state-default ui-th-column-header ui-th-"+ts.p.direction)
							.css({'height':'22px', 'border-top':'0px none','overflow':'hidden'})
							.html(titleText);
						if(cVisibleColumns > 0) {
							$colHeader.attr("colspan", String(cVisibleColumns));
						}
						if (ts.p.headertitles) {
							$colHeader.attr("title", $colHeader.text());
						}
						//如果没有可见列则隐藏
						if( cVisibleColumns === 0) {
							$colHeader.hide();
						}
						$th.before($colHeader); 
						threeLevelSkip = numberOfColumns;
						if (defaultStyle === false) {
							hasTwoLevel = false;
							//判断是否存在二层
							for (var a=0;a<threeLevelSkip && (a+i<cml);a++) {
								tempInCol = inColumnHeader(colModel[a+i].name, twoHeaders);
								if (tempInCol>=0) {
																   cghi = twoHeaders[tempInCol];
											   numberOfColumns = parseInt(cghi.numberOfColumns,10);
																   // 计算显示的列长度
											   for (iCol = 0; iCol < numberOfColumns && (a+i+iCol < cml); iCol++) {
											   if (!colModel[a+i+iCol].hidden) {
																		   hasTwoLevel = true;
										   break;
											   }
											   }
																   if (hasTwoLevel === true) {
																	   break;
																   }
								}
							}
						}
					}
					
					//是否在twoHeaders配置里面
					inTwoCol = inColumnHeader(cmi.name, twoHeaders);
					if (threeLevelSkip >0 && inTwoCol >= 0) {
						cghi = twoHeaders[inTwoCol];
						numberOfColumns = parseInt(cghi.numberOfColumns,10);
						titleText = cghi.titleText;

						// 计算显示的列长度
						for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
							if (!colModel[i + iCol].hidden) {
								cVisibleColumns++;
							}
						}
						
						$colHeader = $('<th>').attr({role: "columnheader",noWrap:true})
							.addClass("ui-state-default ui-th-column-header ui-th-"+ts.p.direction)
							.css({'height':'22px', 'border-top': '0px none','overflow':'hidden'})
							.html(titleText);
						if(cVisibleColumns > 0) {
							$colHeader.attr("colspan", String(cVisibleColumns));
						}
						
						if (ts.p.headertitles) {
							$colHeader.attr("title", $colHeader.text());
						}
						//如果没有可见列则隐藏
						if( cVisibleColumns === 0) {
							$colHeader.hide();
						}
						$thirdHeaderRow.append($colHeader);
						twoLevelSkip = numberOfColumns;
					}
					
					//不在第三层
					if (threeLevelSkip === 0) {
						$th.attr("rowspan", "3");
						twoLevelSkip = 0;
						continue;
					}
					
					//在第三层不在第二层
					if (threeLevelSkip > 0 && twoLevelSkip === 0) {
						if (defaultStyle) {
							$th.attr("rowspan", "2");
							$thirdHeaderRow.append(th);
						} else {
							if (hasTwoLevel) {
								$th.attr("rowspan", "2");
								$thirdHeaderRow.append(th);	
							} else {
								$colHeader.attr("rowspan", "2");
								$fourHeaderRow.append(th);	
							}
						}
						threeLevelSkip--;
						continue;
					}
					
					//在第三层也在第二层
					if (threeLevelSkip > 0 && twoLevelSkip > 0) {
						$fourHeaderRow.append(th);
						threeLevelSkip--;
						twoLevelSkip--;
					}
				}
				$theadInTable = $(ts).children("thead");
				$theadInTable.prepend($firstHeaderRow);
				$thirdHeaderRow.insertAfter($secondHeaderRow);
				$fourHeaderRow.insertAfter($thirdHeaderRow);
				$htable.append($theadInTable);

				$htable.find("span.ui-jqgrid-resize").each(function () {
					var $parent = $(this).parent();
					if ($parent.is(":visible")) {
						this.style.cssText = 'height: ' + ($parent.height()/*+4*/) + 'px !important; cursor: col-resize;';
					}
				});
				$htable.find("div.ui-jqgrid-sortable").each(function () {
					var $ts = $(this), $parent = $ts.parent();
					if ($parent.is(":visible") && $parent.is(":has(span.ui-jqgrid-resize)")) {
						$ts.css('top', ($parent.height() - $ts.outerHeight()) / 2 + 'px');
					}
				});
					
				$firstRow = $theadInTable.find("tr.jqg-first-row-header");
				$(ts).bind('jqGridResizeStop.setGroupHeaders', function (e, nw, idx) {
					$firstRow.find('th').eq(idx).width(nw);
				});
			});				
		},
		
		/**
		 * 构造多表头冻结表格 by ljq
		 * @参数 maxfrozen : 冻结的列index
		 * @参数 htbl : 表格
		 */
		createFrozenHtable : function(maxfrozen, htbl) {
			var ts = this[0],
			complexHeader = ts.p.complexHeader,
			complexHeaders = complexHeader["complexHeaders"],
			defaultStyle = complexHeaders.defaultStyle,
			threeHeaders = complexHeaders["threeLevel"],
			twoHeaders = complexHeaders["twoLevel"],
			tableid = ts.p.id,
			colModel = ts.p.colModel,
			cml = maxfrozen+1,
			ths = ts.grid.headers,
			threeLevelSkip = 0, 
			twoLevelSkip = 0, 
			hasTwoLevel = false,
			i, cmi, thStyle, th, $th, cn, $tempTh, iCol, $colHeader, cghi,
			tempInCol,
			inThreeCol,
			inTwoCol,
			numberOfColumns,
			titleText,
			cVisibleColumns,
			height = ts.grid.fhDiv.height(),
			$fhTable = $("<table class='ui-jqgrid-htable' style='width:1px;height:"+ height +"px' role='grid' aria-labelledby='gbox_"+tableid+"' cellspacing='0' cellpadding='0' border='0'></table>"),
			$thead = $("<thead></thead>"),
			$firstHeaderRow = $('<tr>', {role: "row", "aria-hidden": "true"}).addClass("jqg-first-row-header").css("height", "auto"),
			$secondHeaderRow = $('<tr>', {role: "rowheader"}).addClass("ui-jqgrid-labels jqg-second-row-header"),
			$thirdHeaderRow = $('<tr>', {role: "rowheader"}).addClass("ui-jqgrid-labels jqg-third-row-header"),
			$fourHeaderRow = $('<tr>', {role: "rowheader"}).addClass("ui-jqgrid-labels jqg-four-row-header"),
			inColumnHeader = function (text, columnHeaders) {
				var i = 0, length = columnHeaders.length;
				for (; i < length; i++) {
					if (columnHeaders[i] && columnHeaders[i].startColumnName === text) {
						return i;
					}
				}
				return -1;
			};
			
			//循环模型每列
			for (i = 0; i < cml; i++) {
				cmi = colModel[i];
				cn = cmi.name;
				$th = $("#"+tableid+"_"+cn,htbl);
				// 给第一行添加单元格
				thStyle = { height: '0px', width: ths[i].width  +'px', display: (cmi.hidden ? 'none' : '')};
				$("<th>", {role: 'gridcell'}).css(thStyle).addClass("ui-first-th-"+ts.p.direction).appendTo($firstHeaderRow);
				//是否在threeHeaders配置里面
				inThreeCol = inColumnHeader(cmi.name, threeHeaders);
				if (inThreeCol >= 0) {
					cghi = threeHeaders[inThreeCol];
					numberOfColumns = parseInt(cghi.numberOfColumns,10);
					if (numberOfColumns+i-1>maxfrozen) {
						numberOfColumns = maxfrozen-i+1;
					}
					titleText = cghi.titleText;
					// 计算显示的列长度
					for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
						if (!colModel[i + iCol].hidden) {
							cVisibleColumns++;
						}
					}
					$colHeader = $('<th>').attr({role: "columnheader",noWrap:true})
						.addClass("ui-state-default ui-th-column-header ui-th-"+ts.p.direction)
						.css({'height':'22px', 'border-top':'0px none','overflow':'hidden'})
						.html(titleText);
					if(cVisibleColumns > 0) {
						$colHeader.attr("colspan", String(cVisibleColumns));
					}
					if (ts.p.headertitles) {
						$colHeader.attr("title", $colHeader.text());
					}
					//如果没有可见列则隐藏
					if( cVisibleColumns === 0) {
						$colHeader.hide();
					}
					$colHeader.appendTo($secondHeaderRow);
					threeLevelSkip = numberOfColumns;
					if (defaultStyle === false) {
						hasTwoLevel = false;
						//判断是否存在二层
						for (var a=0;a<threeLevelSkip && (a+i<cml);a++) {
												   tempInCol = inColumnHeader(colModel[a+i].name, twoHeaders);
							   if (tempInCol>=0) {
													   cghi = twoHeaders[tempInCol];
								   numberOfColumns = parseInt(cghi.numberOfColumns,10);
													   if (numberOfColumns+a+i-1>maxfrozen) {
									   numberOfColumns = maxfrozen-(a+i)+1;
									   }
													   // 计算显示的列长度
								   for (iCol = 0; iCol < numberOfColumns && (a+i+iCol < cml); iCol++) {
								   if (!colModel[a+i+iCol].hidden) {
																hasTwoLevel = true;
											break;
									}
									}
												   }
												   if (hasTwoLevel === true) {
													   break;
												   }	
							}
					}
				}
				//是否在twoHeaders配置里面
				inTwoCol = inColumnHeader(cmi.name, twoHeaders);
				if (threeLevelSkip >0 && inTwoCol >= 0) {
					cghi = twoHeaders[inTwoCol];
					numberOfColumns = parseInt(cghi.numberOfColumns,10);
					if (numberOfColumns+i-1>maxfrozen) {
						numberOfColumns = maxfrozen-i+1;
					}
					titleText = cghi.titleText;
					// 计算显示的列长度
					for (cVisibleColumns = 0, iCol = 0; iCol < numberOfColumns && (i + iCol < cml); iCol++) {
						if (!colModel[i + iCol].hidden) {
							cVisibleColumns++;
						}
					}
					$colHeader = $('<th>').attr({role: "columnheader",noWrap:true})
						.addClass("ui-state-default ui-th-column-header ui-th-"+ts.p.direction)
						.css({'height':'22px', 'border-top': '0px none','overflow':'hidden'})
						.html(titleText);
					if(cVisibleColumns > 0) {
						$colHeader.attr("colspan", String(cVisibleColumns));
					}
					if (ts.p.headertitles) {
						$colHeader.attr("title", $colHeader.text());
					}
					//如果没有可见列则隐藏
					if( cVisibleColumns === 0) {
						$colHeader.hide();
					}
					$colHeader.appendTo($thirdHeaderRow);
					twoLevelSkip = numberOfColumns;
				}
				//不在第三层
				if (threeLevelSkip === 0) {
					$th.attr("rowspan", "3");
					$th.appendTo($secondHeaderRow);
					twoLevelSkip = 0;
					continue;
				}
				//在第三层不在第二层
				if (threeLevelSkip > 0 && twoLevelSkip === 0) {
					if (defaultStyle) {
						$th.attr("rowspan", "2");
						$th.appendTo($thirdHeaderRow);
					} else {
						if (hasTwoLevel) {
							$th.attr("rowspan", "2");
							$th.appendTo($thirdHeaderRow);
						} else {
							$colHeader.attr("rowspan", "2");
							$th.appendTo($fourHeaderRow);
						}
					}
					threeLevelSkip--;
					continue;
				}
				//在第三层也在第二层
				if (threeLevelSkip > 0 && twoLevelSkip > 0) {
					$th.appendTo($fourHeaderRow);
					threeLevelSkip--;
					twoLevelSkip--;
				}
			}
			var thirdLen = $thirdHeaderRow.children().length,
				fourLen = $fourHeaderRow.children().length;
			if (thirdLen=== 0 && fourLen === 0) {
				$secondHeaderRow.height(height);
			}
			if (defaultStyle) {
				//默认渲染样式处理
				if (thirdLen > 0 && fourLen === 0) {
					$thirdHeaderRow.height(height-23);
				}
			} else {
				if (thirdLen === 0 && fourLen > 0) {
					$secondHeaderRow.height(height-23);
					$secondHeaderRow.find("th").each(function(){
						var rowspan = $(this).attr("rowspan");
						if (rowspan) {
							$(this).attr("rowspan",String(parseInt(rowspan,10)-1));
						}
					});
				}
			}
			
			$firstHeaderRow.appendTo($thead);
			$secondHeaderRow.appendTo($thead);
			thirdLen && $thirdHeaderRow.appendTo($thead);
			fourLen && $fourHeaderRow.appendTo($thead);
			$thead.appendTo($fhTable);
			return $fhTable;
		}
	});
})(jQuery);

;(function($){
	/**
	 * jqGrid Chinese Translation
	 * 咖啡兔 yanhonglei@gmail.com
	 * http://www.kafeitu.me 
	 * Dual licensed under the MIT and GPL licenses:
	 * http://www.opensource.org/licenses/mit-license.php
	 * http://www.gnu.org/licenses/gpl.html
	**/
	$.jgrid = $.jgrid || {};
	$.extend($.jgrid,{
	    defaults : {
	        recordtext: "{0} - {1}\u3000共 {2} 条", // 共字前是全角空格
	        emptyrecords: "无数据显示",
	        loadtext: "正在加载...",
	        pgtext : " {0} 共 {1} 页"
	    },
	    search : {
	        caption: "搜索...",
	        Find: "查找",
	        Reset: "重置",
	        odata: [{ oper:'eq', text:'等于\u3000\u3000'},{ oper:'ne', text:'不等\u3000\u3000'},{ oper:'lt', text:'小于\u3000\u3000'},{ oper:'le', text:'小于等于'},{ oper:'gt', text:'大于\u3000\u3000'},{ oper:'ge', text:'大于等于'},{ oper:'bw', text:'开始于'},{ oper:'bn', text:'不开始于'},{ oper:'in', text:'属于\u3000\u3000'},{ oper:'ni', text:'不属于'},{ oper:'ew', text:'结束于'},{ oper:'en', text:'不结束于'},{ oper:'cn', text:'包含\u3000\u3000'},{ oper:'nc', text:'不包含'},{ oper:'nu', text:'不存在'},{ oper:'nn', text:'存在'}],
	        groupOps: [ { op: "AND", text: "所有" },    { op: "OR",  text: "任一" } ],
			operandTitle : "Click to select search operation.",
			resetTitle : "Reset Search Value"
	    },
	    edit : {
	        addCaption: "添加记录",
	        editCaption: "编辑记录",
	        bSubmit: "提交",
	        bCancel: "取消",
	        bClose: "关闭",
	        saveData: "数据已改变，是否保存？",
	        bYes : "是",
	        bNo : "否",
	        bExit : "取消",
	        msg: {
	            required:"此字段必需",
	            number:"请输入有效数字",
	            minValue:"输值必须大于等于 ",
	            maxValue:"输值必须小于等于 ",
	            email: "这不是有效的e-mail地址",
	            integer: "请输入有效整数",
	            date: "请输入有效时间",
	            url: "无效网址。前缀必须为 ('http://' 或 'https://')",
	            nodefined : " 未定义！",
	            novalue : " 需要返回值！",
	            customarray : "自定义函数需要返回数组！",
	            customfcheck : "必须有自定义函数!"
	        }
	    },
	    view : {
	        caption: "查看记录",
	        bClose: "关闭"
	    },
	    del : {
	        caption: "删除",
	        msg: "删除所选记录？",
	        bSubmit: "删除",
	        bCancel: "取消"
	    },
	    nav : {
	        edittext: "",
	        edittitle: "编辑所选记录",
	        addtext:"",
	        addtitle: "添加新记录",
	        deltext: "",
	        deltitle: "删除所选记录",
	        searchtext: "",
	        searchtitle: "查找",
	        refreshtext: "",
	        refreshtitle: "刷新表格",
	        alertcap: "注意",
	        alerttext: "请选择记录",
	        viewtext: "",
	        viewtitle: "查看所选记录"
	    },
	    col : {
	        caption: "选择列",
	        bSubmit: "确定",
	        bCancel: "取消"
	    },
	    errors : {
	        errcap : "错误",
	        nourl : "没有设置url",
	        norecords: "没有要处理的记录",
	        model : "colNames 和 colModel 长度不等！"
	    },
	    formatter : {
	        integer : {thousandsSeparator: ",", defaultValue: '0'},
	        number : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, defaultValue: '0.00'},
	        currency : {decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "", suffix:"", defaultValue: '0.00'},
	        date : {
	            dayNames:   [
	                "日", "一", "二", "三", "四", "五", "六",
	                "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
	            ],
	            monthNames: [
	                "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二",
	                "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"
	            ],
	            AmPm : ["am","pm","上午","下午"],
	            S: function (j) {return j < 11 || j > 13 ? ['st', 'nd', 'rd', 'th'][Math.min((j - 1) % 10, 3)] : 'th';},
	            srcformat: 'Y-m-d',
	            newformat: 'Y-m-d',
	            parseRe : /[#%\\\/:_;.,\t\s-]/,
	            masks : {
	                // see http://php.net/manual/en/function.date.php for PHP format used in jqGrid
	                // and see http://docs.jquery.com/UI/Datepicker/formatDate
	                // and https://github.com/jquery/globalize#dates for alternative formats used frequently
	                // one can find on https://github.com/jquery/globalize/tree/master/lib/cultures many
	                // information about date, time, numbers and currency formats used in different countries
	                // one should just convert the information in PHP format
	                ISO8601Long:"Y-m-d H:i:s",
	                ISO8601Short:"Y-m-d",
	                // short date:
	                //    n - Numeric representation of a month, without leading zeros
	                //    j - Day of the month without leading zeros
	                //    Y - A full numeric representation of a year, 4 digits
	                // example: 3/1/2012 which means 1 March 2012
	                ShortDate: "n/j/Y", // in jQuery UI Datepicker: "M/d/yyyy"
	                // long date:
	                //    l - A full textual representation of the day of the week
	                //    F - A full textual representation of a month
	                //    d - Day of the month, 2 digits with leading zeros
	                //    Y - A full numeric representation of a year, 4 digits
	                LongDate: "l, F d, Y", // in jQuery UI Datepicker: "dddd, MMMM dd, yyyy"
	                // long date with long time:
	                //    l - A full textual representation of the day of the week
	                //    F - A full textual representation of a month
	                //    d - Day of the month, 2 digits with leading zeros
	                //    Y - A full numeric representation of a year, 4 digits
	                //    g - 12-hour format of an hour without leading zeros
	                //    i - Minutes with leading zeros
	                //    s - Seconds, with leading zeros
	                //    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
	                FullDateTime: "l, F d, Y g:i:s A", // in jQuery UI Datepicker: "dddd, MMMM dd, yyyy h:mm:ss tt"
	                // month day:
	                //    F - A full textual representation of a month
	                //    d - Day of the month, 2 digits with leading zeros
	                MonthDay: "F d", // in jQuery UI Datepicker: "MMMM dd"
	                // short time (without seconds)
	                //    g - 12-hour format of an hour without leading zeros
	                //    i - Minutes with leading zeros
	                //    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
	                ShortTime: "g:i A", // in jQuery UI Datepicker: "h:mm tt"
	                // long time (with seconds)
	                //    g - 12-hour format of an hour without leading zeros
	                //    i - Minutes with leading zeros
	                //    s - Seconds, with leading zeros
	                //    A - Uppercase Ante meridiem and Post meridiem (AM or PM)
	                LongTime: "g:i:s A", // in jQuery UI Datepicker: "h:mm:ss tt"
	                SortableDateTime: "Y-m-d\\TH:i:s",
	                UniversalSortableDateTime: "Y-m-d H:i:sO",
	                // month with year
	                //    Y - A full numeric representation of a year, 4 digits
	                //    F - A full textual representation of a month
	                YearMonth: "F, Y" // in jQuery UI Datepicker: "MMMM, yyyy"
	            },
	            reformatAfterEdit : false
	        },
	        baseLinkUrl: '',
	        showAction: '',
	        target: '',
	        checkbox : {disabled:true},
	        idName : 'id'
	    }
	});
})(jQuery);