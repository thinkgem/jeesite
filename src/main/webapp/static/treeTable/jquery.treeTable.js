/**
* 树表组件
* @author benzhan (詹潮江)
* @version 1.4.2
* @lastUpdateDate 2011-09-03
* @mail zhanchaojiang@qq.com
*/
(function ($) {
//	window.SITE_URL = window.SITE_URL || '';
//	if (document.location.href.indexOf('http://') != 0)	{
//		var path = '../themes/';
//	} else {
//		var path = SITE_URL + '/themes/';
//	}

    $.fn.treeTable = function (opts) {
        opts = $.extend({
        	path: '',
            theme: 'default',
            expandLevel: 1,
            column: 0,
            onSelect: function($treeTable, id){},
            beforeExpand: function($treeTable, id){}
        }, opts);

        var $treeTable = this;
        $treeTable.addClass('tree_table');

//        //添加需要的样式
//        if ($('head').find('#tree_table_' + opts.theme).length == 0) {
//            $('head').append('<link id="tree_table_' + opts.theme + '" href="' + opts.path + 'themes/' + opts.theme + '/treeTable.css" rel="stylesheet" type="text/css" />');
//        }

        var css = {
            'N' : opts.theme + '_node',
            'AN' : opts.theme + '_active_node',
            'O' : opts.theme + '_open',
            'LO' : opts.theme + '_last_open',
            'S' : opts.theme + '_shut',
            'LS' : opts.theme + '_last_shut',
            'HO' : opts.theme + '_hover_open',
            'HS' : opts.theme + '_hover_shut',
            'HLO' : opts.theme + '_hover_last_open',
            'HLS' : opts.theme + '_hover_last_shut',
            'L' : opts.theme + '_leaf',
            'LL' : opts.theme + '_last_leaf',
            'B' : opts.theme + '_blank',
            'V' : opts.theme + '_vertline'
        };

        var pMap = {}, cMap = {};
        var $trs = $treeTable.find('tr');
        initRelation($trs, true);    

        $treeTable.click(function (event) {
            var $target = $(event.target);

            if ($target.attr('controller')) {
                $target = $target.parents('tr[haschild]').find('[arrow]');
                //判断是否是叶子节点
				if ($target.attr('class').indexOf(css['AN']) == -1 && $target.attr('class').indexOf(css['N']) == -1) { return; }
                var id = $target.parents('tr[haschild]')[0].id;
                if (opts.onSelect && opts.onSelect($treeTable, id) === false) { return; }
            }

            if ($target.attr('arrow')) {
                var className = $target.attr('class');
                if (className == css['AN'] + ' ' + css['HLO'] || className == css['AN'] + ' ' + css['HO']) {
                    var id = $target.parents('tr[haschild]')[0].id;
                    $target.attr('class', css['AN'] + " " + (className.indexOf(css['HO']) != -1 ?  css['HS'] : css['HLS']));

                    //关闭所有孩子的tr
                    shut(id);
					return;
                } else if (className == css['AN'] + ' ' + css['HLS'] || className == css['AN'] + ' ' + css['HS']) {
                    var id = $target.parents('tr')[0].id;
                    $target.attr('class', css['AN'] + " " + (className.indexOf(css['HS']) != -1 ? css['HO'] : css['HLO']));
                    
                    opts.beforeExpand($treeTable, id);
                    //展开所有直属节点，根据图标展开子孙节点
                    open(id);
					return;
                }
            }
        });
		
		$treeTable.mouseover(hoverActiveNode).mouseout(hoverActiveNode);

        function hoverActiveNode(event) {
            var $target = $(event.target);

            if ($target.attr('controller')) {
                $target = $target.parents('tr[haschild]').find('[arrow]');
            }

            if ($target.attr('arrow')) { 
                var className = $target.attr('class');
                if (className && !className.indexOf(css['AN'])) {
                    var len = opts.theme.length + 1;
                    className = className.split(' ')[1].substr(len);
                    if (className.indexOf('hover_') === 0) {
                        className = opts.theme + '_' + className.substr(6);
                    } else {
                        className = opts.theme + '_hover_' + className;
                    }
                    
                    $target.attr('class', css['AN'] + ' ' + className);
                    return;
                }
            } 
        }
        
        /** 初始化节点关系　*/
        function initRelation($trs, hideLevel) {
            //构造父子关系
            $trs.each(function (i) {
                var pId = $(this).attr('pId') || 0;
                pMap[pId] || (pMap[pId] = []);
                pMap[pId].push(this.id);
                cMap[this.id] = pId;
                
                //给这个tr增加类为了提高选择器的效率
                $(this).addClass(pId);
            }).find('[controller]').css('cursor', 'pointer');

            //标识父节点是否有孩子、是否最后一个节点
            $trs.each(function (i) {
                if (!this.id) { return; }
                var $tr = $(this);
                
                pMap[this.id] && $tr.attr('hasChild', true);
                var pArr = pMap[cMap[this.id]];
                if (pArr[0] == this.id) {
                    $tr.attr('isFirstOne', true);
                } else {
                    var prevId = 0;
                    for (var i = 0; i < pArr.length; i++) {
                        if (pArr[i] == this.id) { break; }
                        prevId = pArr[i];
                    }
                    $tr.attr('prevId', prevId);
                }

                pArr[pArr.length - 1] == this.id && $tr.attr('isLastOne', true);

                var depth = getDepth(this.id);
                $tr.attr('depth', depth);

                //格式化节点
				formatNode(this);

                //判断是否要隐藏限制的层次
                if (hideLevel) {
                    depth > opts.expandLevel && $tr.hide();
                    //判断是否小于深度，如果小于深度则要换成展开的图标
                    if ($tr.attr('hasChild') && $tr.attr('depth') < opts.expandLevel) {
                        var className = $tr.attr('isLastOne') ? css['LO'] : css['O'];
                        $tr.find('.' + css['AN']).attr('class', css['AN'] + ' ' + className);
                    }
                }               
            });
            
            //递归获取深度
            function getDepth(id) {
                if (cMap[id] == 0) { return 1; } 
                var $parentDepth = getDepth(cMap[id]);
                return $parentDepth + 1; 
            }
        }

        //递归关闭所有的孩子
        function shut(id) {
            if (!pMap[id]) { return false; }
            for (var i = 0; i < pMap[id].length; i++) {
                shut(pMap[id][i]);
            }
            $('tr.' + id, $treeTable).hide();
        }

        //根据历史记录来展开节点
        function open(id) {
            $('tr.' + id, $treeTable).show();
            if (!pMap[id]) { return false; }
            for (var i = 0; i < pMap[id].length; i++) {
                var cId = pMap[id][i];
                if (pMap[cId]) {
                    var className = $('#' + cId, $treeTable).find('.' + css['AN']).attr('class');
                    //如果子节点是展开图表的，则需要展开此节点
                    (className == css['AN'] + ' ' + css['O'] || className == css['AN'] + ' ' + css['LO']) && open(cId);
                }
            }
        }

        function formatNode(tr) {
            var $cur = $(tr);
            var id = tr.id;

            //-------------下面一大段都是获取$preSpan---------
            if (cMap[id] == 0) {
                //如果是顶级节点，则没有prev_sp
                var $preSpan = $('<span class="prev_sp"></span>');
            } else {
                //先判断是否有上一个兄弟节点
                if (!$cur.attr('isFirstOne')) {
                    var $preSpan = $('#' + $cur.attr('prevId'), $treeTable).children("td").eq(opts.column).find('.prev_sp').clone();
                } else {
                    var $parent = $('#' + cMap[id], $treeTable);
                    //没有上一个兄弟节点，则使用父节点的prev_sp
                    var $preSpan = $parent.children("td").eq(opts.column).find('.prev_sp').clone();

                    //如果父亲后面没有兄弟，则直接加空白，若有则加竖线
                    if ($parent.attr('isLastOne')) {
                        $preSpan.append('<span class="' + css['N'] + ' ' + css['B'] + '"></span>');
                    } else {
                        $preSpan.append('<span class="' + css['N'] + ' ' + css['V'] + '"></span>');
                    }
                }
            }
            //------------------------------------------------

            if ($cur.attr('hasChild')) {
                //如果有下一个节点，并且下一个节点的父亲与当前节点的父亲相同，则说明该节点不是最后一个节点
                var className = $cur.attr('isLastOne') ? css['LS'] : css['S'];
                className = css['AN'] + ' ' + className;
            } else {
                var className = css['N'] + ' ' + ($cur.attr('isLastOne') ? css['LL'] : css['L']);
            }
            
            var $td = $cur.children("td").eq(opts.column);
            $td.prepend('<span arrow="true" class="' + className + '"></span>').prepend($preSpan);
        };
        
        $treeTable.addChilds = function(trsHtml) {
            var $trs = $(trsHtml);
            if (!$trs.length) { return false; }
            
            var pId = $($trs[0]).attr('pId');
            if (!pId) { return false; }
            
            //插入到最后一个孩子后面，或者直接插在父节点后面
            var insertId = pMap[pId] && pMap[pId][pMap[pId].length - 1] || pId;
            $('#' + insertId, $treeTable).after($trs);
            initRelation($trs);
        };

        return $treeTable;
    };
})(jQuery);

