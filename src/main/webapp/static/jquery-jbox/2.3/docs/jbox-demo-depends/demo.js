
/* jBox 测试代码开始 */
var skin2 = false;
$(function () {
    changeSkin();
    $(window).scroll(function () { $('.sel-skin').css({ top: $(window).scrollTop() + 300 }); });
});

function viewConfig(name) {
    var height = name == 'defaults' ? 500 : 'auto';
    jBox.open('id:' + name, '$.jBox.' + name, 'auto', height, { top: '50px', loaded: function () {
        $('.jbox-content').css('background-color', '#eeeeee').find('pre').css('border', 'none')
    }
    });
}
function changeSkin(show) {
    var skin = $('#select-skin').val().split('|');
    document.getElementById('skin').href = "../Skins" + (skin2 == true ? "2" : "") + "/" + skin[0] + "/jbox.css";
    $('.sel-skin').css('background-color', skin[1]);
    if (show) {
        $.jBox.tip('换肤成功，当前皮肤：' + skin[0], 'success');
    }
}
function show_jbox_set_defaults() {
    $.jBox.info("请自行在 jBox/i18n/ 目录下修改相应js文件。");
}
function show_jbox_set_border(val) {
    $.jBox.setDefaults({ defaults: { border: val} }); // 只修改全局 border
    $.jBox.info('边框已设置为 ' + val);
}

function demo_1_1() {
    $.jBox.info('jQuery jBox<br /><br />版本：v2.0<br />日期：2011-7-24<br />官网：<a target="_blank" href="http://kudystudio.com/jbox">http://kudystudio.com/jbox</a>');
}
function demo_1_2() {
    $.jBox('id:id-html', { bottomText: '这是底部文字' });
}
function demo_1_3() {
    $.jBox("get:ajax.html");
}
function demo_1_4() {
    $.jBox("iframe:http://www.baidu.com", {
        title: "百度一下",
        width: 800,
        height: 350,
        buttons: { '关闭': true }
    });
}
function demo_1_5() {
    var content = {
        state1: {
            content: '状态一',
            buttons: { '下一步': 1, '取消': 0 },
            buttonsFocus: 0,
            submit: function (v, h, f) {
                if (v == 0) {
                    return true; // close the window
                }
                else {
                    $.jBox.nextState(); //go forward
                    // 或 $.jBox.goToState('state2')
                }
                return false;
            }
        },
        state2: {
            content: '状态二，请关闭窗口哇：）',
            buttons: { '上一步': 1, '取消': 0 },
            buttonsFocus: 0,
            submit: function (v, h, f) {
                if (v == 0) {
                    return true; // close the window
                } else {
                    $.jBox.prevState() //go back
                    // 或 $.jBox.goToState('state1');
                }

                return false;
            }
        }
    };

    $.jBox(content);
}
function demo_1_6() {
    var html = "<div style='padding:10px;'>输入姓名：<input type='text' id='yourname' name='yourname' /></div>";
    var submit = function (v, h, f) {
        if (f.yourname == '') {
            $.jBox.tip("请输入您的姓名。", 'error', { focusId: "yourname" }); // 关闭设置 yourname 为焦点
            return false;
        }

        $.jBox.tip("你叫：" + f.yourname);
        //$.jBox.tip("你叫：" + h.find("#yourname").val());
        //$.jBox.tip("你叫：" + h.find(":input[name='yourname']").val());

        return true;
    };

    $.jBox(html, { title: "你叫什么名字？", submit: submit });
}


function demo_2_1() {
    $.jBox.open("iframe:http://www.baidu.com", "百度一下", 800, 350, { buttons: { '关闭': true} });
}
function demo_2_2() {
    var html1 = '<div class="msg-div">' +
                        '<p>购买数量：</p><div class="field"><input type="text" id="amount" name="amount" style="width:75px;" value="1" /></div>' +
                        '<p>收货地址：</p><div class="field"><textarea id="address" name="address"></textarea></div>' +
                        '<div class="errorBlock" style="display: none;"></div>' +
                        '</div>';

    var html2 = '<div class="msg-div">' +
                        '<p>给卖家留言：<span style="color:gray">（选填，可以告诉卖家您对商品的特殊要求）</span></p><div class="field"><textarea id="message" name="message"></textarea></div>' +
                        '</div>';

    var data = {};
    var states = {};
    states.state1 = {
        content: html1,
        buttons: { '下一步': 1, '取消': 0 },
        submit: function (v, h, f) {
            if (v == 0) {
                return true; // close the window
            }
            else {
                h.find('.errorBlock').hide('fast', function () { $(this).remove(); });

                data.amount = f.amount; //或 h.find('#amount').val();
                if (data.amount == '' || parseInt(data.amount) < 1) {
                    $('<div class="errorBlock" style="display: none;">请输入购买数量！</div>').prependTo(h).show('fast');
                    return false;
                }
                data.address = f.address;
                if (data.address == '') {
                    $('<div class="errorBlock" style="display: none;">请输入收货地址！</div>').prependTo(h).show('fast');
                    return false;
                }

                $.jBox.nextState(); //go forward
                // 或 $.jBox.goToState('state2')
            }

            return false;
        }
    };
    states.state2 = {
        content: html2,
        buttons: { '上一步': -1, '提交': 1, '取消': 0 },
        buttonsFocus: 1, // focus on the second button
        submit: function (v, o, f) {
            if (v == 0) {
                return true; // close the window
            } else if (v == -1) {
                $.jBox.prevState() //go back
                // 或 $.jBox.goToState('state1');
            }
            else {
                data.message = f.message;

                // do ajax request here
                $.jBox.nextState('<div class="msg-div">正在提交...</div>');
                // 或 $.jBox.goToState('state3', '<div class="msg-div">正在提交...</div>')

                // asume that the ajax is done, than show the result
                var msg = [];
                msg.push('<div class="msg-div">');
                msg.push('<p>下面是提交的数据</p>');
                for (var p in data) {
                    msg.push('<p>' + p + ':' + data[p] + '</p>');
                }
                msg.push('</div>');
                window.setTimeout(function () { $.jBox.nextState(msg.join('')); }, 2000);
            }

            return false;
        }
    };
    states.state3 = {
        content: '',
        buttons: {} // no buttons
    };
    states.state4 = {
        content: '',
        buttons: { '确定': 0 }
    };

    $.jBox.open(states, '提交订单', 450, 'auto');
}


function demo_3_1() {
    //加了个其它参数closed
    $.jBox.prompt('Hello jBox', 'jBox', 'info', { closed: function () { alert('prompt is closed.'); } });
}
function demo_3_2() {
    $.jBox.alert('Hello jBox', 'jBox');
}
function demo_3_3() {
    $.jBox.info('Hello jBox', 'jBox');
}
function demo_3_4() {
    $.jBox.success('Hello jBox', 'jBox');
}
function demo_3_5() {
    $.jBox.error('Hello jBox', 'jBox');
}
function demo_3_6() {
    var submit = function (v, h, f) {
        if (v == 'ok')
            jBox.tip(v, 'info');
        else if (v == 'cancel')
            jBox.tip(v, 'info');

        return true; //close
    };

    $.jBox.confirm("确定吗？", "提示", submit);
}
function demo_3_6_2() {
    var submit = function (v, h, f) {
        if (v == true)
            jBox.tip("恩", 'info');
        else
            jBox.tip("好吖", 'info');

        return true;
    };
    // 自定义按钮
    $.jBox.confirm("天使，做我女朋友吧？", "表白提示", submit, { buttons: { '恩': true, '好吖': false} });
}
function demo_3_7() {
    var submit = function (v, h, f) {
        if (v == 'yes') {
            $.jBox.tip('已保存。', 'success');
        }
        if (v == 'no') {
            $.jBox.tip('没保存。');
        }
        if (v == 'cancel') {
            $.jBox.tip('已取消。');
        }

        return true;
    };
    // 可根据需求仿上例子定义按钮
    $.jBox.warning("内容已修改，是否保存？", "提示", submit);
}

function demo_4_1() {
    $.jBox.tip('Hello jBox');
}
function demo_4_2() {
    //加了个其它参数focusId
    $.jBox.tip('关闭后设置输入框为焦点', 'info', { focusId: 'tip-input' });
}
function demo_4_3() {
    //加了个其它参数closed
    $.jBox.tip('关闭后设置输入框为已选', 'error', { closed: function () { $('#tip-input2').select(); } });
}
function demo_4_4() {
    $.jBox.tip("正在XX，你懂的...", 'loading');
    // 模拟2秒后完成操作
    window.setTimeout(function () { $.jBox.tip('XX已完成。', 'success'); }, 2000);
}
function demo_4_5() {
    var submit = function (v, h, f) {
        if (v == 'ok') {
            $.jBox.tip("正在删除数据...", 'loading');
            // 模拟2秒后完成操作
            window.setTimeout(function () { $.jBox.tip('删除成功。', 'success'); }, 2000);
        }
        else if (v == 'cancel') {
            // 取消
        }

        return true; //close
    };

    $.jBox.confirm("确定要删除数据吗？", "提示", submit);
}

function demo_5_1() {
    $.jBox.messager('Hello jBox', 'jBox');
}
function demo_5_2() {
    $.jBox.messager("Hello jBox 2", "my title", null, { width: 350, showType: 'fade' });
}
function demo_5_3() {
    $.jBox.messager("Hello jBox 3", "my title", 3000, {
        width: 350,
        icon: 'info',
        showType: 'show',
        buttons: { '去看看': true },
        submit: function (v, h, f) {
            $.jBox.info('看个蛋蛋？');
            return true;
        }
    });
}
