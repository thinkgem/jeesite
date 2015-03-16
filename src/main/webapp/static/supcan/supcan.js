/*!
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 * 
 * 硕正组件封装
 * @author ThinkGem
 * @version 2013-11-09
 */

/**
 * 硕正实例对象数组
 */
var supcans = [];

/**
 * 实例化一个硕正控件
 * @param AF 硕正对象
 * @param url 硕正描述文件URL，如果不是一个URL，则填写实体类名
 * @param options 参数选项
 * @returns {Supcan} 硕正封装对象实例
 */
function supcan(AF, url, options){
	var su = new Supcan(AF, url, options);
	supcans.push(su);
	return su;
}

/**
 * 硕正控件加载，id参数说明：如果页面中有多个插件/加载项，那么OnReady事件会被触发多次，id就是插件/加载项的id
 */
function OnReady(id){
	for (i=0; i<supcans.length; i++){
		if (supcans[i].AF.id == id){
			supcans[i].init(supcans[i].url);
			supcans[i].options.ready();
		}
	}
}

/**
 * 硕正控件事件，参数说明: “Event”为事件名，p1,p2,p3,p4参数有特定的含义，由具体的功能组件规定.
 */
function OnEvent(id, Event, p1, p2, p3, p4){
	for (i=0; i<supcans.length; i++){
		if (supcans[i].AF.id == id){
			supcans[i].options.event(Event, p1, p2, p3, p4);
		}
	}
}

/**
 * 硕正组件封装类
 * @param AF 硕正对象
 * @param url 硕正描述文件URL
 * @param options 选项
 */
function Supcan(AF, url, options){
	
	var $this = this;
	
	/**
	 * 硕正原始对象
	 */
	$this.AF = AF;
	
	/**
	 * 硕正描述文件URL，如果不是一个URL，则填写实体类名
	 */
	if (url.indexOf('/') == -1){
		$this.url = ctx + '/supcan/treeList/' + url + '.xml';
	}else{
		$this.url = url;
	}
	
	/**
	 * 硕正组件配置选项
	 */
	$this.options = $.extend({
		su: $this,			// 当前硕正对象
		checked: true,		// 是否插入复选框
		curSelMode: "rows",	// 行选择模式（row：单选；rows：多选；excel：Excel选择模式）
		dataType: "json",	// 数据类型，如果数据类型不是json，则This.data不自动转换为对象，分页代码也不自动赋值
		height: "auto",			// 控件高度，默认自适应。
		body: "body",			// 控件高度自适应，相关对象
		ready: function(){},	// 默认的控件加载调用方法
		event: function(){}		// 默认的控件事件调用方法
    }, options);
	
	/**
	 * 硕正数据对象，如果dataType为json，则为JSON对象，否则为数据字符串
	 */
	$this.data = "";

	/**
	 * 调用硕正内置函数
	 */
	$this.func = function(name, param){
		return $this.AF.func(name, param);
	}
	
	/**
	 * 硕正控件自动高度
	 */
	$this.autoHeight = function(){
		var supcanDiv = $($this.AF).parent(), height = 0;
		$($this.options.body).each(function(){
			height += $(this).height();
		});
		$(supcanDiv).height($(window).height() + $(supcanDiv).height() - height - 10);
	}
	
	/**
	 * 初始化方法
	 * @param url 描述文件URL
	 */
	$this.init = function(url){
		// 编译控件
		$this.func("Build", url);
		// 在第一列位置插入复选框列
		if ($this.options.checked){
			$this.func("InsertCol", "0\r\nname=checked;isCheckboxOnly=true");
		}
		// 多行选择(curselmode="rows")状态下的表现形式
		if ($this.options.curSelMode){
			$this.func("SetProp", "curSelMode \r\n " + $this.options.curSelMode);
		}
		// 自适应行高
		if ($this.options.height == "auto"){
			$(window).resize(function(){
				$this.autoHeight();
			});
			$this.autoHeight();
		}else{
			$($this.AF).parent().height($this.options.height);
		}
	}
	
	/**
	 * 加载数据
	 * @param dataUrl 数据调用URL
	 * @param paramString 参数字符串
	 * @param pareDiv 分页DIV的ID，如果数据格式为JSON，则自动设置分页器代码。
	 */
	$this.load = function(dataUrl, paramString, callback){
		$.post(dataUrl, paramString, function(data){
			$this.func("Load", data);
			if ($this.options.dataType == "json"){
				// 转换为JSON对象，保存到data变量
				$this.data = eval("("+data+")");
			}else{
				$this.data = data;
			}
			if (typeof callback == 'function'){
				callback($this.data);
			}
		}, "text");
    }
	
	/**
	 * 加载数据
	 * @param form 参数表单，自动序列化表单字段，加入数据URL中
	 * @param page 分页DIV，如果数据格式为JSON，则自动设置分页器代码。
	 */
	$this.loadByForm = function(form, page){
		$this.load($(form).attr('action'), $(form).serialize(), function(data){
			// 设置分页代码
			if ($this.options.dataType == "json" && data != ''){
				$(form+"[name=pageNo]").val(data.pageNo);
				$(form+"[name=pageSize]").val(data.pageSize);
				$(page).html(data.html);
				// 链接去掉虚框
				$(page).find("a").bind("focus",function() {
					if(this.blur) {this.blur()};
				});
			}
		});
		
    }

	/**
	 * 获取单元格数据
	 * @param columnName 列表列名
	 * @param isMany 是否支持多选，获取多个值
	 */
	$this.getCellText = function(columnName, isMany){
		var rowIdxs = $this.func("GetCurrentRow", "");
		if(rowIdxs != ''){
			rowIdxs = rowIdxs.split(',');
			if (isMany){
				var vals = [];
				for (i=0; i<rowIdxs.length; i++){
					vals.push($this.func("GetCellText", rowIdxs[i] + " \r\n " + columnName));
				}
				return vals.join(',');
			}else{
				if (rowIdxs.length == 1){
					return $this.func("GetCellText", rowIdxs[0] + " \r\n " + columnName);
				}
			}
		}
		return "";
	}
	
	/**
	 * 切换到以树显示，并让勾选列和第一列(树的排序列)合并显示
	 */
	$this.setTree = function(flag){
		$this.func("SetProp", "curSelMode \r\n row"+(flag?"":"s"));
		$this.func("SetColProp", "checked \r\n TreeCombine \r\n "+(flag?"auto,node":"")); // 合并单元格
		$this.func("SetProp", "isTree \r\n "+(flag?"true":"false"));
	}
	
	/**
	 * 使硕正控件变为灰色
	 */
	$this.setGrayWindow = function(trueOrFalse){
		$this.func("GrayWindow", trueOrFalse ? "1" : "0");
	}

	return $this;
}