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