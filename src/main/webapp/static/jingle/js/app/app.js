document.addEventListener('deviceready', onDeviceReady, false);
function onDeviceReady(){
    navigator.splashscreen.hide();
    //注册后退按钮
    document.addEventListener("backbutton", function (e) {
        if(J.hasMenuOpen){
            J.Menu.hide();
        }else if(J.hasPopupOpen){
            J.closePopup();
        }else{
            var sectionId = $('section.active').attr('id');
            if(sectionId == 'index_section'){
                J.confirm('提示','是否退出程序？',function(){
                    navigator.app.exitApp();
                });
            }else{
                window.history.go(-1);
            }
        }
    }, false);
}
var App = (function(){
    var pages = {};
    var run = function(){
        $.each(pages,function(k,v){
            var sectionId = '#'+k+'_section';
            $('body').delegate(sectionId,'pageinit',function(){
                v.init && v.init.call(v);
            });
            $('body').delegate(sectionId,'pageshow',function(e,isBack){
                //页面加载的时候都会执行
                v.show && v.show.call(v);
                //后退时不执行
                if(!isBack && v.load){
                    v.load.call(v);
                }
            });
        });
		J.Transition.add('flip','slideLeftOut','flipOut','slideRightOut','flipIn');
        Jingle.launch({
            showWelcome : false,
            showPageLoading : true,
            remotePage : {
            	'#index_section': ctx,
            	'#user_section': ctx + '/sys/user'
            }
        });
       
    };
    var page = function(id,factory){
        return ((id && factory)?_addPage:_getPage).call(this,id,factory);
    }
    var _addPage = function(id,factory){
        pages[id] = new factory();
    };
    var _getPage = function(id){
        return pages[id];
    }
    //动态计算chart canvas的高度，宽度，以适配终端界面
    var calcChartOffset = function(){
        return {
            height : $(document).height() - 44 - 30 -60,
            width : $(document).width()
        }

    }
    return {
        run : run,
        page : page,
        calcChartOffset : calcChartOffset
    }
}());/*
App.page('index',function(){
    this.init = function(){
        $('#btn_show_welcome').on('tap', J.Welcome.show);
    }
})
App.page('calendar',function(){
    this.init = function(){
        new J.Calendar('#calendar_demo',{
            onRenderDay : function(day,date){
                if(day == 5){
                    return '<div>'+day+'</div><div style="font-size: .8em;color: red">威武</div>'
                }
                return day;
            },
            onSelect:function(date){
                alert(date);
            }
        });
        $('#btn_popup_calendar').tap(function(){
            J.popup({
                html : '<div id="popup_calendar"></div>',
                pos : 'center',
                backgroundOpacity : 0.4,
                showCloseBtn : false,
                onShow : function(){
                    new J.Calendar('#popup_calendar',{
                        date : new Date(2013,7,1),
                        onSelect:function(date){
                            $('#btn_popup_calendar').text(date);
                            J.closePopup();
                        }
                    });
                }
            });
        });
    }
});
App.page('refresh',function(){
    this.init = function(){
        J.Refresh({
            selector : '#down_refresh_article',
            type : 'pullDown',
            pullText : '你敢往下拉么...',
            releaseText : '什么时候你才愿意放手？',
            refreshTip : '最后一次拉的人：<span style="color:#e222a5">骚年</span>',
            callback : function(){
                var scroll = this;
                setTimeout(function () {
                    $('#down_refresh_article ul.list li').text('嗯哈，长大后我就成了你~');
                    scroll.refresh();
                    J.showToast('更新成功','success');
                }, 2000);
            }
        });
//    最简约的调用方式
        J.Refresh( '#up_refresh_article','pullUp', function(){
            var scroll = this;
            setTimeout(function () {
                var html = '';
                for(var i=0;i<10;i++){
                    html += '<li style="color:#E74C3C">我是被拉出来的...</li>'
                }
                $('#up_refresh_article ul.list').append(html);
                scroll.refresh();
                J.showToast('加载成功','success');
            }, 2000);
        })
    }
});
App.page('scroll',function(){
    this.init = function(){
        $('#h_scroll_article').on('articleshow',function(){
            J.Scroll('#h_scroll_demo',{hScroll:true,hScrollbar : false});
        })
    }
});
App.page('menu',function(){
    this.init = function(){
        $.get('html/custom_aside.html',function(aside){
            $('#aside_container').append(aside);
        })
    }
});
App.page('layout',function(){
    this.init = function(){
        $('#layout_header_ctrl').on('change',function(event,el){
            J.alert('提示','你点了'+$(el).text());
        })
        $('#layout-btn-getmore').tap(function(){
            J.popup({
                html: '<div style="height: 100px;line-height: 100px;font-size: 20px;font-weight: 600;text-align: center;">这里展示更多功能</div>',
                pos : 'bottom-second',
                showCloseBtn : false
            });
        })
    }
});
App.page('popup',function(){
    this.init = function(){
        $('#btn_alert').tap(function(){
            J.alert('提示','这是一个Alert');
        })
        $('#btn_confirm').tap(function(){
            J.confirm('提示','这是一个Confirm！',function(){J.showToast('你选择了“确定”')},function(){J.showToast('你选择了“取消”')});
        })
        $('#btn_loading').tap(function(){
            J.showMask();
        })
        $('#btn_center').tap(function(){
            J.popup({
                html: '<div style="height: 100px;text-align: center;font-size: 20px;font-weight: 600;margin-top: 10px;color:#E74C3C ">随意设计你的弹出框吧</div>',
                pos : 'center'
            })
        })
        $('#btn_from_tpl').tap(function(){
            J.popup({
                tplId : 'tpl_popup_login',
                pos : 'center'
            })
        })
        $('#btn_t_top').tap(function(){
            J.popup({
                html: '这是一个来自顶部的弹出框',
                pos : 'top',
                showCloseBtn : false
            })
        })
        $('#btn_t_ts').tap(function(){
            J.popup({
                html: '这是一个在header之下的弹出框',
                pos : 'top-second',
                showCloseBtn : false
            })
        })
        $('#btn_t_bottom').tap(function(){
            J.popup({
                html: '这是一个来自底部弹出框',
                pos : 'bottom',
                showCloseBtn : false
            })
        })
        $('#btn_t_bs').tap(function(){
            J.popup({
                html: '这是一个在footer之上的弹出框',
                pos : 'bottom-second',
                showCloseBtn : false
            })
        })
        $('#btn_popover').tap(function(){
            J.popover('<ul class="list"><li style="color:#000;">Hello Jingle</li><li style="color:#000;">你好，Jingle</li></ul>',{top:'50px',left:'10%',right:'10%'},'top');
        });
        $('#btn_actionsheet').tap(function(){
            J.Popup.actionsheet([{
                text : '告诉QQ好友',
                handler : function(){
                    J.showToast('告诉QQ好友！');
                }
            },{
                text : '告诉MSN好友',
                handler : function(){
                    J.showToast('告诉MSN好友！');
                }
            }
            ]);
        });
    }
});
App.page('slider',function(){
    this.init = function(){
        var slider;
        $('#slider_section article').on('articleshow',function(){
            slider = new J.Slider({
                selector : '#slider_test',
                onBeforeSlide : function(){
                    return true;
                },
                onAfterSlide : function(i){
                    //alert(i);
                }
            });
        });
        $('#slider_prev').tap(function(){slider.prev()});
        $('#slider_next').tap(function(){slider.next()});
    }
});
App.page('toast',function(){
    this.init = function(){
        $('#btn_t_default').tap(function(){
            J.showToast('这是默认的Toast,默认3s后小时');
        })
        $('#btn_t_success').tap(function(){
            J.showToast('恭喜，success,5s后消失','success',5000);
        })
        $('#btn_t_error').tap(function(){
            J.showToast('抱歉，error','error');
        })
        $('#btn_t_info').tap(function(){
            J.showToast('提示，info','info');
        })
        $('#btn_t_top').tap(function(){
            J.showToast('更新了50条数据','toast top');
        })
    }
});
App.page('chart_line',function(){
    var line,$chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#line_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-30);

        renderLine();
        $('#changeLineType').on('change',function(e,el){
            updateChart(el.data('type'));
        })
    }

    function renderLine(){
        var data = {
            labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89]
                }
            ]
        }
        line = new JChart.Line(data,{
            id : 'line_canvas',
            smooth : false,
            fill : false
        });
        line.on('tap.point',function(d,i,j){
            J.alert(data.labels[i],d);
        });
        line.draw();
    }
    function updateChart(type){
        if(type == 'smooth'){
            line.refresh({
                smooth : true,
                fill : false
            });
        }else if(type == 'area'){
            line.refresh({
                smooth : true,
                fill : true
            });
        }else{
            line.refresh({
                smooth : false,
                fill : false
            });
        }

    }
});
App.page('chart_bar',function(){
    var $chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#bar_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height);

        var data = {
            labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89]
                }
            ]
        }
        var bar = new JChart.Bar(data,{
            id : 'bar_canvas'
        });
        bar.on('tap.bar',function(d,i,j){
            J.alert(data.labels[i],d);
        });
        bar.on('longTap.bar',function(d,i,j){
            J.alert('提示',d+' = 按住750ms会出现此提示');
        });
        bar.draw();
    }
});
App.page('chart_pie',function(){
    var pie,$chart;
    this.init = function(){
        //重新设置canvas大小
        $chart = $('#pie_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-100);
        renderPie();
        $('#changePieType').on('change',function(e,el){
            updateChart(el.data('type'));
        })
    }

    function renderPie(){
        var pie_data = [
            {
                name : '直接访问',
                value: 335,
                color:"#F38630"
            },{
                name : '联盟广告',
                value : 234,
                color : "#E0E4CC"
            },{
                name : '视频广告',
                value : 135,
                color : "#72caed"
            },{
                name : '搜索引擎',
                value : 1400,
                color : "#a6d854"
            }
        ];
        pie = new JChart.Pie(pie_data,{
            id : 'pie_canvas',
            clickType : 'rotate'
        });
        pie.on('rotate',function(d,i,j){
            $('#pie_segment_info').html(d.name + '    '+ d.value).show();
        });
        pie.draw();
    }
    function updateChart(type){
        $('#pie_segment_info').hide();
        if(type == 'pie'){
            pie.refresh({
                isDount : false
            });
        }else if(type == 'dount'){
            pie.refresh({
                isDount : true,
                dountText : '访问来源'
            });
        }

    }
});
App.page('chart_drag',function(){
    var $lineChart,$barChart;
    this.init = function(){
        //重新设置canvas大小
        $lineChart = $('#chart_drag_line_canvas');
        $barChart = $('#chart_drag_bar_canvas');
        var wh = App.calcChartOffset();
        $lineChart.attr('width',wh.width).attr('height',wh.height-30);
        $barChart.attr('width',wh.width).attr('height',wh.height-30);
        var data = {
            labels : ["2012","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月','2013',"二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月','2014','一月','二月'],
            datasets : [
                {
                    name : 'A产品',
                    color : "#72caed",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [65,59,90,81,56,55,40,20,13,20,11,60,65,59,90,81,56,55,40,20,11,20,10,60,11,60,65]
                },
                {
                    name : 'B产品',
                    color : "#a6d854",
                    pointColor : "#95A5A6",
                    pointBorderColor : "#fff",
                    data : [28,48,40,19,96,27,100,40,40,70,11,89,28,48,40,19,96,27,100,40,40,70,10,89,28,48,40]
                }
            ]
        }
        $('#changeDragChartType').on('change',function(e,el){
            renderChart(el.data('type'),data);
        })
        renderChart('line',data);
    }
    var renderChart = function(type,data){
        if(type == 'line'){
            $lineChart.show();
            $barChart.hide();
            new JChart.Line(data,{
                id : 'chart_drag_line_canvas',
                datasetGesture : true,
                datasetOffsetNumber : 10
            }).draw(true);
        }else{
            $lineChart.hide();
            $barChart.show();
            new JChart.Bar(data,{
                id : 'chart_drag_bar_canvas',
                datasetGesture : true,
                datasetOffsetNumber : 10
            }).draw(true);
        }
    }
});
App.page('chart_dynamic',function(){
    var pause = false,$chart;
    var datasets = [[65,59,90,81,56,55,40,20,3,20,10,60],[28,48,40,19,96,27,100,40,40,70,10,89]];
    var data = {
        labels : ["一月","二月","三月","四月","五月","六月","七月",'八月','九月','十月','十一月','十二月'],
        datasets : [
            {
                name : 'A产品',
                color : "#72caed",
                pointColor : "#95A5A6",
                pointBorderColor : "#fff",
                data : datasets[0]
            },
            {
                name : 'B产品',
                color : "#a6d854",
                pointColor : "#95A5A6",
                pointBorderColor : "#fff",
                data : datasets[1]
            }
        ]
    }

    this.init = function(){
        //重新设置canvas大小
        $chart = $('#dynamic_line_canvas');
        var wh = App.calcChartOffset();
        $chart.attr('width',wh.width).attr('height',wh.height-30);
        var line = new JChart.Line(data,{
            id : 'dynamic_line_canvas'
        });
        line.draw();
        refreshChart(line);
        $('#pause_dynamic_chart').on('tap',function(){
            pause = !pause;
            $(this).text(pause?'继续':'暂停');
        })
    }

    function refreshChart(chart){
        setTimeout(function(){
            if(!pause){
                datasets[0].shift();
                datasets[0].push(Math.floor(Math.random()*100));
                datasets[1].shift();
                datasets[1].push(Math.floor(Math.random()*100));
                chart.load(data);
            }
            refreshChart(chart);
        },1000);
    }
});
App.page('form',function(){
    this.init = function(){
        alert('init');
        $('#checkbox_1').on('change',function(){
            alert($(this).data('checkbox'));
        })
    }
})*/
$(function(){
    App.run();
})