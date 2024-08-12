(function($){
$.jgrid.extend({
	setFrozenColumns : function (type) {
//		$(this).jqGrid('setFrozenColumnsPlus', 'left');
		$(this).jqGrid('setFrozenColumnsPlus', 'right');
	},
	setFrozenColumnsPlus : function (type) {
		return this.each(function() {
			if ( !this.grid ) {return;}
			if ( !type ) { type = 'left'; }
			var $t = this, cm = $t.p.colModel,i=0, len = cm.length, maxfrozen = -1, frozen = false, isLeft = type == 'left';
			if ( !$t.grid.fcs ) {$t.grid.fcs = {left:{}, right:{}};}
			// TODO treeGrid and grouping  Support
			if($t.p.subGrid === true || $t.p.treeGrid === true || $t.p.cellEdit === true || $t.p.sortable || $t.p.scroll ) {
				return;
			}
			if (isLeft){
				if($t.p.rownumbers) { i++; }
				if($t.p.multiselect) { i++; }
				// get the max index of frozen col
				while(i<len) {
					// from left, no breaking frozen
					if(cm[i].frozen === true) {
						frozen = true;
						maxfrozen = i;
					} else {
						break;
					}
					i++;
				}
			}else{
				i = len-1;
				while(i>0) {
					// from left, no breaking frozen
					if(cm[i].frozen === true) {
						frozen = true;
						maxfrozen = i;
					} else {
						break;
					}
					i--;
				}
			}
			if(maxfrozen>=0 && frozen) {
				var top = $t.p.caption ? $($t.grid.cDiv).outerHeight() : 0,
				hth = $(".ui-jqgrid-htable","#gview_"+$.jgrid.jqID($t.p.id)).height();
				//headers
				if($t.p.toppager) {
					top = top + $($t.grid.topDiv).outerHeight();
				}
				if($t.p.toolbar[0] === true) {
					if($t.p.toolbar[1] !== "bottom") {
						top = top + $($t.grid.uDiv).outerHeight();
					}
				}
				//$t.grid.fhDiv = $('<div style="position:absolute;left:0px;top:'+top+'px;height:'+hth+'px;" class="frozen-div ui-state-default ui-jqgrid-hdiv"></div>');
				$t.grid.fcs[type].fhDiv = $('<div style="position:absolute;'+(isLeft?'left:0':'right:11px')+';top:'+top+'px;height2:'+hth+'px;z-index:1" class="frozen-div ui-state-default ui-jqgrid-hdiv"></div>'); // ThinkGem 修正标题栏高度计算错误
				//$t.grid.fcs[type].fbDiv = $('<div style="position:absolute;left:0px;top:'+(parseInt(top,10)+parseInt(hth,10) + 1)+'px;overflow-y:hidden" class="frozen-bdiv ui-jqgrid-bdiv"></div>');
				$t.grid.fcs[type].fbDiv = $('<div style="position:absolute;'+(isLeft?'left:0':'right:11px')+';top:'+(parseInt(top,10)+parseInt(hth,10))+'px;overflow:hidden" class="frozen-bdiv ui-jqgrid-bdiv"></div>'); // ThinkGem 修正标题栏高度计算错误
				$("#gview_"+$.jgrid.jqID($t.p.id)).append($t.grid.fcs[type].fhDiv);
				var htbl = $(".ui-jqgrid-htable","#gview_"+$.jgrid.jqID($t.p.id)).clone(true);
				// groupheader support - only if useColSpanstyle is false
//					if($t.p.groupHeader) { // 
				if($t.p.groupHeader || $t.p.groupHeaders) { // ThinkGem 增加三级表头锁定
					$("tr.jqg-first-row-header,tr.jqg-third-row-header,tr.jqg-four-row-header", htbl).each(function(){
						if(isLeft){
							$("th:gt("+maxfrozen+")",this).remove();
						}else{
							$("th:lt("+maxfrozen+")",this).remove();
						}
					});
					var swapfroz = -1, fdel = -1, cs, rs;
					$("tr.jqg-second-row-header th", htbl).each(function(){
						cs= parseInt($(this).attr("colspan"),10);
						rs= parseInt($(this).attr("rowspan"),10);
						if(rs) {
							swapfroz++;
							fdel++;
						}
						if(cs) {
							swapfroz = swapfroz+cs;
							fdel++;
						}
						if(swapfroz === maxfrozen) {
							return false;
						}
					});
					if(swapfroz !== maxfrozen) {
						fdel = maxfrozen;
					}
					$("tr.jqg-second-row-header", htbl).each(function(){
						if (fdel > 0){
							if(isLeft){
								$("th:gt("+fdel+")",this).remove();
							}else{
								$("th:lt("+fdel+")",this).remove();
							}
						}else{
							$("th",this).remove();
						}
					});
				} else {
					$("tr",htbl).each(function(){
						if(isLeft){
							$("th:gt("+maxfrozen+")",this).remove();
						}else{
							$("th:lt("+maxfrozen+")",this).remove();
						}
					});
				}
				$(htbl).width(1);
				// resizing stuff
				$($t.grid.fcs[type].fhDiv).append(htbl).mousemove(function (e) {
					if($t.grid.resizing){ $t.grid.dragMove(e);return false; }
				});
				if($t.p.footerrow) {
					var hbd = $(".ui-jqgrid-bdiv","#gview_"+$.jgrid.jqID($t.p.id)).height();

					//$t.grid.fcs[type].fsDiv = $('<div style="position:absolute;left:0px;top:'+(parseInt(top,10)+parseInt(hth,10) + parseInt(hbd,10)+1)+'px;" class="frozen-sdiv ui-jqgrid-sdiv"></div>');
					$t.grid.fcs[type].fsDiv = $('<div style="position:absolute;left:0px;bottom:0px;" class="frozen-sdiv ui-jqgrid-sdiv"></div>'); // ThinkGem 修正锁定列，统计行位置计算错误
					$("#gview_"+$.jgrid.jqID($t.p.id)).append($t.grid.fcs[type].fsDiv);
					var ftbl = $(".ui-jqgrid-ftable","#gview_"+$.jgrid.jqID($t.p.id)).clone(true);
					$("tr",ftbl).each(function(){
						if(isLeft){
							$("td:gt("+maxfrozen+")",this).remove();
						}else{
							$("td:lt("+maxfrozen+")",this).remove();
						}
					});
					$(ftbl).width(1);
					$($t.grid.fcs[type].fsDiv).append(ftbl);
				}
				$($t).bind('jqGridResizeStop.setFrozenColumns', function (e, w, index) {
					var rhth = $(".ui-jqgrid-htable",$t.grid.fcs[type].fhDiv);
					$("th:eq("+index+")",rhth).width( w ); 
					var btd = $(".ui-jqgrid-btable",$t.grid.fcs[type].fbDiv);
					$("tr:first td:eq("+index+")",btd).width( w );
					if($t.p.footerrow) {
						var ftd = $(".ui-jqgrid-ftable",$t.grid.fcs[type].fsDiv);
						$("tr:first td:eq("+index+")",ftd).width( w );
					}
				});
				// sorting stuff
				$($t).bind('jqGridSortCol.setFrozenColumns', function (e, index, idxcol) {

					var previousSelectedTh = $("tr.ui-jqgrid-labels:last th:eq("+$t.p.lastsort+")",$t.grid.fcs[type].fhDiv), newSelectedTh = $("tr.ui-jqgrid-labels:last th:eq("+idxcol+")",$t.grid.fcs[type].fhDiv);

					$("span.ui-grid-ico-sort",previousSelectedTh).addClass('ui-state-disabled');
					$(previousSelectedTh).attr("aria-selected","false");
					$("span.ui-icon-"+$t.p.sortorder,newSelectedTh).removeClass('ui-state-disabled');
					$(newSelectedTh).attr("aria-selected","true");
					if(!$t.p.viewsortcols[0]) {
						if($t.p.lastsort !== idxcol) {
							$("span.s-ico",previousSelectedTh).hide();
							$("span.s-ico",newSelectedTh).show();
						}
					}
				});
				
				// data stuff
				//TODO support for setRowData
				$("#gview_"+$.jgrid.jqID($t.p.id)).append($t.grid.fcs[type].fbDiv);
				$($t.grid.bDiv).scroll(function () {
					$($t.grid.fcs[type].fbDiv).scrollTop($(this).scrollTop());
				});
				if($t.p.hoverrows === true) {
					$("#"+$.jgrid.jqID($t.p.id)).unbind('mouseover').unbind('mouseout');
				}
				$($t).bind('jqGridAfterGridComplete.setFrozenColumns', function () {
					log($("#"+$.jgrid.jqID($t.p.id)+"_frozen").length)
					$("#"+$.jgrid.jqID($t.p.id)+"_frozen").remove();
					$($t.grid.fcs[type].fhDiv).height($($t.grid.hDiv).height()); // ThinkGem add 修正锁定列，标题栏高度计算错误
					//$($t.grid.fcs[type].fbDiv).height($($t.grid.bDiv).height()-16);
					$($t.grid.fcs[type].fbDiv).height($($t.grid.bDiv).height()-16+($.jgrid.bigscroll?0:4)); // ThinkGem 修正IE下高度计算错误
					//$($t.grid.fcs[type].fsDiv).css('top', $($t.grid.hDiv).height() + $($t.grid.fcs[type].fbDiv).height() - $($t.grid.fcs[type].fsDiv).height() - 1 + 5); // ThinkGem 修正锁定列，统计行位置计算错误
					// ThinkGem 凑合用IE8下冻结列显示问题
					//if (!! navigator.userAgent.match(/MSIE/) ||  !!(navigator.userAgent.match(/Trident/))){
					/*if (!!navigator.userAgent.match(/MSIE 8.0/)){
						setTimeout(function(){
							$($t.grid.fcs[type].fhDiv).height($($t.grid.hDiv).height() + 100);
							$($t.grid.fcs[type].fbDiv).height($($t.grid.bDiv).height() + 2);
							$($t.grid.fcs[type].fbDiv).css('top', $($t.grid.hDiv).height()+1);
							$($t.grid.fcs[type].fsDiv).css('top', $($t.grid.hDiv).height() + $($t.grid.fcs[type].fbDiv).height() - $($t.grid.fcs[type].fsDiv).height() - 1);
						}, 200);
					}*/
					var btbl = $("#"+$.jgrid.jqID($t.p.id)).clone(true);
					log(btbl)
					$("tr[role=row]",btbl).each(function(){
						if(isLeft){
							$("td[role=gridcell]:gt("+maxfrozen+")",this).remove();
						}else{
							$("td[role=gridcell]:lt("+maxfrozen+")",this).remove();
						}
					});

					$(btbl).width(1).attr("id",$t.p.id+"_frozen");
					$($t.grid.fcs[type].fbDiv).append(btbl);
					$($t.grid.bDiv).scroll(); // ThinkGem 排序后，重置冻结列滚动位置
					if($t.p.hoverrows === true) {
						$("tr.jqgrow", btbl).hover(
							function(){ $(this).addClass("ui-state-hover"); $("#"+$.jgrid.jqID(this.id), "#"+$.jgrid.jqID($t.p.id)).addClass("ui-state-hover"); },
							function(){ $(this).removeClass("ui-state-hover"); $("#"+$.jgrid.jqID(this.id), "#"+$.jgrid.jqID($t.p.id)).removeClass("ui-state-hover"); }
						);
						$("tr.jqgrow", "#"+$.jgrid.jqID($t.p.id)).hover(
							function(){ $(this).addClass("ui-state-hover"); $("#"+$.jgrid.jqID(this.id), "#"+$.jgrid.jqID($t.p.id)+"_frozen").addClass("ui-state-hover");},
							function(){ $(this).removeClass("ui-state-hover"); $("#"+$.jgrid.jqID(this.id), "#"+$.jgrid.jqID($t.p.id)+"_frozen").removeClass("ui-state-hover"); }
						);
					}
					btbl=null;
				});
				if(!$t.grid.hDiv.loading) {
					$($t).triggerHandler("jqGridAfterGridComplete");
				}
				$t.p.frozenColumns = true;
			}
		});
	},
	destroyFrozenColumns :  function(type) {
		return this.each(function() {
			if ( !this.grid ) {return;}
			if ( !type ) { type = 'left'; }
			if(this.p.frozenColumns === true) {
				var $t = this;
				$($t.grid.fcs[type].fhDiv).remove();
				$($t.grid.fcs[type].fbDiv).remove();
				$t.grid.fcs[type].fhDiv = null; $t.grid.fcs[type].fbDiv=null;
				if($t.p.footerrow) {
					$($t.grid.fcs[type].fsDiv).remove();
					$t.grid.fcs[type].fsDiv = null;
				}
				$(this).unbind('.setFrozenColumns');
				if($t.p.hoverrows === true) {
					var ptr;
					$("#"+$.jgrid.jqID($t.p.id)).bind('mouseover',function(e) {
						ptr = $(e.target).closest("tr.jqgrow");
						if($(ptr).attr("class") !== "ui-subgrid") {
						$(ptr).addClass("ui-state-hover");
					}
					}).bind('mouseout',function(e) {
						ptr = $(e.target).closest("tr.jqgrow");
						$(ptr).removeClass("ui-state-hover");
					});
				}
				this.p.frozenColumns = false;
			}
		});
	}
});
})(jQuery);