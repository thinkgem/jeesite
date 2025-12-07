/*!
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * 级联选择插件
 * @author ThinkGem
 * @version 2021-4-25
 */
(function ($) {
    $.fn.cascadeSelect = function (options) {
        var self = this, opts = $.extend({
            data: [],
            id: "id",
            name: "name",
            children: "children",
            blankOptionLabel: "请选择",
            cssClass: 'form-control',
            cssStyle: "width:150px",
            change: function(){}
        }, options);
		var data = opts.data,
			i, l, key = "id", parentKey = "pId", childKey = "children";
		if (Object.prototype.toString.apply(data) === "[object Array]") {
			var treeData = [], map = [];
			for (i=0, l=data.length; i<l; i++) {
				map[data[i][key]] = data[i];
			}
			for (i=0, l=data.length; i<l; i++) {
				if (map[data[i][parentKey]] && data[i][key] != data[i][parentKey]) {
					if (!map[data[i][parentKey]][childKey]){
						map[data[i][parentKey]][childKey] = [];
					}
					map[data[i][parentKey]][childKey].push(data[i]);
				} else {
					treeData.push(data[i]);
				}
			}
			opts.data = treeData;
		}else {
			opts.data = [data];
		}
		
		$("<style></style>").html("\n\
			.input-group.cascade-select .form-control {display:table-cell;}\n\
			.input-group.cascade-select .form-control:not(:last-child) {border-right:0}\n\
			.input-group.cascade-select .form-control:first-child:last-child {border-radius:4px}\n\
		").appendTo($(self).addClass('cascade-select').parent());
        var html = '', data = opts.data, params = [data];
        html += '<select class="'+opts.cssClass+'" style="'+opts.cssStyle+'">'
        		+'<option value="">'+opts.blankOptionLabel+'</option>';
        for (var i = 0; i < data.length; i++) {
            html += '<option value="' + data[i][opts.id]
	            	+ '|' + (data[i][opts.children] == null ? null : i)
            		+ '|' + (escape(data[i][opts.name]))
                	+ '">' + data[i][opts.name] + '</option>';
        }
        html += '</select>';
        $(self).html(html);
        var createSelect = function() {
            $(self).children('select').change(function () {
                var self2 = this, val = $(self2).val().split("|")[1];
                $(self2).nextAll('select').remove();
                if (val % 1 == 0) {
                    var idx = $(self2).index(), html2 = "";
                    params[idx + 1] = params[idx][val][opts.children];//一条线放一个数组 不同线重置
                    html2 += '<select class="'+opts.cssClass+'" style="'+opts.cssStyle+'">'
            			+'<option value="">'+opts.blankOptionLabel+'</option>';
                    for (var i = 0; i < params[idx + 1].length; i++) {
                        html2 += '<option value="' + params[idx + 1][i][opts.id]
                        		+ '|' + (params[idx + 1][i][opts.children] == null ? null : i)
                        		+ '|' + (escape(params[idx + 1][i][opts.name]))
                        		+ '">' + params[idx + 1][i][opts.name] + '</option>';
                    }
                    html2 += '</select>';
                    $(self2).after(html2);
                    $(self).children("select").unbind("change");
                    createSelect();
                }
                var vals = [], names = [];
                $(self).find('select').each(function(){
                	var val = $(this).val();
                	if (val != ''){
                    	vals.push(val.split("|")[0]);
                    	names.push(unescape(val.split("|")[2]));
                	}
                });
                opts.change(vals, names);
            })
        }
        createSelect();
    }
})(jQuery);