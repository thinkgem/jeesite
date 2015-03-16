/*
* jQuery jBox 2.3
* http://www.kudystudio.com
* Author: kudy chen (kudychen@gmail.com)
* 
* Copyright 2011, kudy studio
* Dual licensed under the MIT or GPL Version 3 licenses.
* 
* Last Modified: 2011-11-11
*/
(function ($) {
    $.jBox = function (content, options) {
        options = $.extend({}, $.jBox.defaults, options);
        options.showFade = options.opacity > 0;
        options.isTip = options.isTip || false;
        options.isMessager = options.isMessager || false;

        if (content == undefined) { content = ''; }
        if (options.border < 0) { options.border = 0; }
        if (options.id == undefined) { options.id = 'jBox_' + Math.floor(Math.random() * 1000000); }
        var isIE6 = !-[1,]&&!window.XMLHttpRequest;

        var prevBox = $('#' + options.id);
        if (prevBox.length > 0) {
            options.zIndex = $.jBox.defaults.zIndex++;
            prevBox.css({ zIndex: options.zIndex });
            prevBox.find('#jbox').css({ zIndex: options.zIndex + 1 });
            return prevBox;
        }

        var data = { url: '', type: '', html: '', isObject: content.constructor == Object };
        if (!data.isObject) {
            content = content + '';
            var tempContent = content.toLowerCase();
            if (tempContent.indexOf('id:') == 0) data.type = 'ID';
            else if (tempContent.indexOf('get:') == 0) data.type = 'GET';
            else if (tempContent.indexOf('post:') == 0) data.type = 'POST';
            else if (tempContent.indexOf('iframe:') == 0) data.type = 'IFRAME';
            else if (tempContent.indexOf('html:') == 0) data.type = 'HTML';
            else { content = 'html:' + content; data.type = 'HTML'; }
            content = content.substring(content.indexOf(":") + 1, content.length);
        }

        var withTitle = !options.isTip && !(options.title == undefined);
        var isRequest = data.type == 'GET' || data.type == 'POST' || data.type == 'IFRAME';
        var titleWidth = typeof options.width == 'number' ? (options.width - 50) + 'px' : "90%";

        var boxHtml = [];
        boxHtml.push('<div id="' + options.id + '" class="jbox-' + (options.isTip ? 'tip' : (options.isMessager ? 'messager' : 'body')) + '">');
        if (options.showFade) {
            if ((isIE6 && $('iframe').length > 0) || $('object, applet').length > 0) {
                boxHtml.push('<iframe id="jbox-fade" class="jbox-fade" src="about:blank" style="display:block;position:absolute;z-index:-1;"></iframe>');
            }
            else {
                if (isIE6) {
                    $('select').css('visibility', 'hidden');
                }
                boxHtml.push('<div id="jbox-fade" class="jbox-fade" style="position:absolute;"></div>');
            }
        }
        boxHtml.push('<div id="jbox-temp" class="jbox-temp" style="width:0px;height:0px;background-color:#ff3300;position:absolute;z-index:1984;fdisplay:none;"></div>');
        if (options.draggable) {
            boxHtml.push('<div id="jbox-drag" class="jbox-drag" style="position:absolute;z-index:1984;display:none;"></div>');
        }
        boxHtml.push('<div id="jbox" class="jbox" style="position:absolute;width:auto;height:auto;">');

        boxHtml.push('<div class="jbox-help-title jbox-title-panel" style="height:25px;display:none;"></div>');
        boxHtml.push('<div class="jbox-help-button jbox-button-panel" style="height:25px;padding:5px 0 5px 0;display:none;"></div>');

        boxHtml.push('<table border="0" cellpadding="0" cellspacing="0" style="margin:0px;padding:0px;border:none;">');
        if (options.border > 0) {
            boxHtml.push('<tr>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;border-radius:' + options.border + 'px 0 0 0;width:' + options.border + 'px;height:' + options.border + 'px;"></td>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;height:' + options.border + 'px;overflow: hidden;"></td>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;border-radius:0 ' + options.border + 'px 0 0;width:' + options.border + 'px;height:' + options.border + 'px;"></td>');
            boxHtml.push('</tr>');
        }
        boxHtml.push('<tr>');
        boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;"></td>');
        boxHtml.push('<td valign="top" style="margin:0px;padding:0px;border:none;">');
        boxHtml.push('<div class="jbox-container" style="width:auto; height:auto;">');

        boxHtml.push('<a class="jbox-close" title="' + $.jBox.languageDefaults.close + '" onmouseover="$(this).addClass(\'jbox-close-hover\');" onmouseout="$(this).removeClass(\'jbox-close-hover\');" style="position:absolute; display:block; cursor:pointer; top:' + (6 + options.border) + 'px; right:' + (6 + options.border) + 'px; width:15px; height:15px;' + (options.showClose ? '' : 'display:none;') + '"></a>');

        if (withTitle) {
            boxHtml.push('<div class="jbox-title-panel" style="height:25px;">');
            boxHtml.push('<div class="jbox-title' + (options.showIcon == true ? ' jbox-title-icon' : (options.showIcon == false ? '' : ' ' + options.showIcon)) + '" style="float:left; width:' + titleWidth + '; line-height:' + (/msie/.test(navigator.userAgent.toLowerCase()) ? 25 : 24) + 'px; padding-left:' + (options.showIcon ? 18 : 5) + 'px;overflow:hidden;text-overflow:ellipsis;word-break:break-all;">' + (options.title == '' ? '&nbsp;' : options.title) + '</div>');
            boxHtml.push('</div>');
        }
        boxHtml.push('<div id="jbox-states"></div></div>');

        boxHtml.push('</div>');
        boxHtml.push('</td>');
        boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;"></td>');
        boxHtml.push('</tr>');
        if (options.border > 0) {
            boxHtml.push('<tr>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;border-radius:0 0 0 ' + options.border + 'px; width:' + options.border + 'px; height:' + options.border + 'px;"></td>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;height:' + options.border + 'px;overflow: hidden;"></td>');
            boxHtml.push('<td class="jbox-border" style="margin:0px;padding:0px;border:none;border-radius:0 0 ' + options.border + 'px 0; width:' + options.border + 'px; height:' + options.border + 'px;"></td>');
            boxHtml.push('</tr>');
        }
        boxHtml.push('</table>');
        boxHtml.push('</div>');
        boxHtml.push('</div>');

        var iframeHtml = '<iframe name="jbox-iframe" id="jbox-iframe" width="100%" height="100%" marginheight="0" marginwidth="0" frameborder="0" scrolling="' + options.iframeScrolling + '"></iframe>';

        var $window = $(window);
        var $body = $(document.body);
        var $boxBody = $(boxHtml.join('')).appendTo($body);
        var $box = $boxBody.children('#jbox');
        var $boxFade = $boxBody.children('#jbox-fade');
        var $boxTemp = $boxBody.children('#jbox-temp');

        if (!data.isObject) {
            switch (data.type) {
                case "ID":
                    data.html = $('#' + content).html();
                    break;
                case "GET":
                case "POST":
                    data.html = '';
                    data.url = content;
                    break;
                case "HTML":
                    data.html = content;
                    break;
                case "IFRAME":
                    data.html = iframeHtml;
                    if (content.indexOf('#') == -1) {
                        data.url = content + (content.indexOf('?') == -1 ? '?___t=' : '&___t=') + Math.random();
                    } else {
                        var arr = content.split('#');
                        data.url = arr[0] + (arr[0].indexOf('?') == -1 ? '?___t=' : '&___t=') + Math.random() + '#' + arr[1];
                    }
                    break;
            }
            content = {
                state0: {
                    content: data.html, buttons: options.buttons, buttonsFocus: options.buttonsFocus, submit: options.submit
                }
            };
        }

        var states = [];
        var helpTitleHeight = $box.find('.jbox-help-title').outerHeight(true);
        var helpButtonHeight = $box.find('.jbox-help-button').outerHeight(true);
        var ieButtonFix = /msie/.test(navigator.userAgent.toLowerCase()) ? 'line-height:19px;padding:0px 6px 0px 6px;' : 'padding:0px 10px 0px 10px;';
        $.each(content, function (stateName, stateOptions) {
            if (data.isObject) {
                stateOptions = $.extend({}, $.jBox.stateDefaults, stateOptions);
            }
            content[stateName] = stateOptions;
            if (stateOptions.buttons == undefined) { stateOptions.buttons = {}; }
            var withButtons = false;
            $.each(stateOptions.buttons, function (k, v) { withButtons = true; });
            var contentHeight = 'auto';
            if (typeof options.height == 'number') {
                contentHeight = options.height;
                if (withTitle) { contentHeight = contentHeight - helpTitleHeight; }
                if (withButtons) { contentHeight = contentHeight - helpButtonHeight; }
                contentHeight = (contentHeight - 1) + 'px';
            }

            var loadingHtml = '';
            var loadingImageTop = '25px';
            if (!data.isObject && isRequest) {
                var loadingHeight = options.height;
                if (typeof options.height == 'number') {
                    if (withTitle) { loadingHeight = loadingHeight - helpTitleHeight; }
                    if (withButtons) { loadingHeight = loadingHeight - helpButtonHeight; }
                    loadingImageTop = ((loadingHeight / 5) * 2) + 'px';
                    loadingHeight = (loadingHeight - 1) + 'px';
                }
                loadingHtml = ['<div id="jbox-content-loading" class="jbox-content-loading" style="min-height:70px;height:' + loadingHeight + '; text-align:center;">',
                '<div class="jbox-content-loading-image" style="display:block; margin:auto; width:220px; height:19px; padding-top: ' + loadingImageTop + ';"></div>',
                '</div>'].join('');
            }

            states.push('<div id="jbox-state-' + stateName + '" class="jbox-state" style="display:none;">');
            states.push('<div style="min-width:50px;width:' + (typeof options.width == 'number' ? options.width + 'px' : 'auto') + '; height:' + contentHeight + ';">' + loadingHtml + '<div id="jbox-content" class="jbox-content" style="height:' + contentHeight + ';overflow:hidden;overflow-y:auto;">' + stateOptions.content + '</div></div>');
            states.push('<div class="jbox-button-panel" style="height:25px;padding:5px 0 5px 0;text-align: right;' + (withButtons ? '' : 'display:none;') + '">');
            if (!options.isTip) {
                states.push('<span class="jbox-bottom-text" style="float:left;display:block;line-height:25px;"></span>');
            }
            $.each(stateOptions.buttons, function (k, v) {
                states.push('<button class="jbox-button" value="' + v + '" style="' + ieButtonFix + '">' + k + '</button>');
            });
            states.push('</div></div>');
        });

        $box.find('#jbox-states').html(states.join('')).children('.jbox-state:first').css('display', 'block');
        /*$box.find('.jbox-button-panel:empty').css('display', 'none');*/
        if (isRequest) {
            var $iframe = $box.find('#jbox-content').css({ position: (isIE6) ? "absolute" : "fixed", left: -10000 });
        }

        $.each(content, function (stateName, stateOptions) {
            var $state = $box.find('#jbox-state-' + stateName);
            $state.children('.jbox-button-panel').children('button').click(function () {
                var object = $state.find('#jbox-content');
                var clicked = stateOptions.buttons[$(this).text()];
                var formInputs = {};

                $.each($box.find('#jbox-states :input').serializeArray(), function (i, obj) {
                    if (formInputs[obj.name] === undefined) {
                        formInputs[obj.name] = obj.value;
                    } else if (typeof formInputs[obj.name] == Array) {
                        formInputs[obj.name].push(obj.value);
                    } else {
                        formInputs[obj.name] = [formInputs[obj.name], obj.value];
                    }
                });
                var closed = stateOptions.submit(clicked, object, formInputs);
                if (closed === undefined || closed) {
                    removeBox();
                }
            })
            /*.bind('focus', function () { $(this).blur(); })*/
            .bind('mousedown', function () { $(this).addClass('jbox-button-active'); })
            .bind('mouseup', function () { $(this).removeClass('jbox-button-active'); })
            .bind('mouseover', function () { $(this).addClass('jbox-button-hover'); })
            .bind('mouseout', function () { $(this).removeClass('jbox-button-active').removeClass('jbox-button-hover'); });

            $state.find('.jbox-button-panel button:eq(' + stateOptions.buttonsFocus + ')').addClass('jbox-button-focus');
        });

        var ie6scroll = function () {
            $boxBody.css({ top: $window.scrollTop() });
            if (options.isMessager) {
                $box.css({
                    position: (isIE6) ? "absolute" : "fixed",
                    right: 1,
                    bottom: 1
                });
            }
        };
        var fadeClicked = function () {
            if (!options.showFade) {
                return;
            }
            if (options.persistent) {
                var i = 0;
                $boxBody.addClass('jbox-warning');
                var intervalid = setInterval(function () {
                    $boxBody.toggleClass('jbox-warning');
                    if (i++ > 1) {
                        clearInterval(intervalid);
                        $boxBody.removeClass('jbox-warning');
                    }
                }, 100);
            } else {
                removeBox();
            }
        };
        var keyPressEventHandler = function (e) {
            if (options.isTip || options.isMessager) {
                return false;
            }
            var key = (window.event) ? event.keyCode : e.keyCode;
            if (key == 27) {
                removeBox();
            }
            if (key == 9) {
                var $inputels = $(':input:enabled:visible', $boxBody);
                var fwd = !e.shiftKey && e.target == $inputels[$inputels.length - 1];
                var back = e.shiftKey && e.target == $inputels[0];
                if (fwd || back) {
                    setTimeout(function () {
                        if (!$inputels) return;
                        var el = $inputels[back === true ? $inputels.length - 1 : 0];
                        if (el) el.focus();
                    }, 10);
                    return false;
                }
            }
        };
        var setFade = function () {
            if (options.showFade) {
                $.jBox.FadeBoxCount++;
                $(/msie/.test(navigator.userAgent.toLowerCase()) ? 'html' : 'body').attr('style', 'overflow:hidden;padding-right:17px;');

                $boxFade.css({
                    position: "absolute",
                    height: options.isTip ? 5000 : $window.height(),
                    width: isIE6 ? $window.width() : "100%",
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0
                });
            }
        };
        var positionBox = function () {
            if (options.isMessager) {
                $box.css({
                    position: (isIE6) ? "absolute" : "fixed",
                    right: 1,
                    bottom: 1
                });
            } else {
                $boxTemp.css({ top: options.top });
                $box.css({
                    position: "absolute",
                    top: $boxTemp.offset().top + (options.isTip ? $window.scrollTop() : 0),
                    left: (($window.width() - $box.outerWidth()) / 2)
                });
            }
            if ((options.showFade && !options.isTip) || (!options.showFade && !options.isTip && !options.isMessager)) {
                $boxBody.css({
                    position: (isIE6) ? "absolute" : "fixed",
                    height: options.showFade ? $window.height() : 0,
                    width: "100%",
                    top: (isIE6) ? $window.scrollTop() : 0,
                    left: 0,
                    right: 0,
                    bottom: 0
                });
            }
            setFade();
        };
        var setTop = function () {
            options.zIndex = $.jBox.defaults.zIndex++;
            $boxBody.css({ zIndex: options.zIndex });
            $box.css({ zIndex: options.zIndex + 1 });
        };
        var styleBox = function () {
            options.zIndex = $.jBox.defaults.zIndex++;
            $boxBody.css({ zIndex: options.zIndex });
            $box.css({
                display: "none",
                zIndex: options.zIndex + 1
            });
            if (options.showFade) {
                $boxFade.css({
                    display: "none",
                    zIndex: options.zIndex,
                    opacity: options.opacity
                });
            }
        };
        var doDown = function (e) {
            var dragData = e.data;
            dragData.target.find('iframe').hide();
            if (options.dragClone) {
                dragData.target.prev().css({
                    left: dragData.target.css('left'),
                    top: dragData.target.css('top'),
                    marginLeft: -2,
                    marginTop: -2,
                    width: dragData.target.width() + 2,
                    height: dragData.target.height() + 2
                }).show();
            }
            return false;
        }
        var doMove = function (e) {
            var dragData = e.data;
            var left = dragData.startLeft + e.pageX - dragData.startX;
            var top = dragData.startTop + e.pageY - dragData.startY;
            if (options.dragLimit) {
                var minTop = 1;
                var maxTop = document.documentElement.clientHeight - e.data.target.height() - 1;
                var minLeft = 1;
                var maxLeft = document.documentElement.clientWidth - e.data.target.width() - 1;
                if (top < minTop) top = minTop + (options.dragClone ? 2 : 0);
                if (top > maxTop) top = maxTop - (options.dragClone ? 2 : 0);
                if (left < minLeft) left = minLeft + (options.dragClone ? 2 : 0);
                if (left > maxLeft) left = maxLeft - (options.dragClone ? 2 : 0);
            }
            if (options.dragClone) {
                dragData.target.prev().css({ left: left, top: top });
            } else {
                dragData.target.css({ left: left, top: top });
            }
            return false;
        }
        var doUp = function (e) {
            $(document).unbind('.draggable');
            if (options.dragClone) {
                var drag = e.data.target.prev().hide();
                e.data.target.css({ left: drag.css('left'), top: drag.css('top') }).find('iframe').show();
            } else {
                e.data.target.find('iframe').show();
            }
            return false;
        }
        var onMouseDown = function (e) {
            var position = e.data.target.position();
            var data = {
                target: e.data.target,
                startX: e.pageX,
                startY: e.pageY,
                startLeft: position.left,
                startTop: position.top
            };
            $(document).bind('mousedown.draggable', data, doDown).bind('mousemove.draggable', data, doMove).bind('mouseup.draggable', data, doUp);
        }
        var removeBox = function () {
            if (options.showFade) {
                if ($.jBox.FadeBoxCount == 1) {
                    $(/msie/.test(navigator.userAgent.toLowerCase()) ? 'html' : 'body').removeAttr('style');
                }
                $.jBox.FadeBoxCount--;
            }

            if (options.isTip) {
                var tip = $(document.body).data('tip');
                if (tip && tip.next == true) {
                    $boxTemp.css('top', tip.options.top);
                    var top = $boxTemp.offset().top + $window.scrollTop();
                    if (top == $box.offset().top) {
                        removeBoxImpl();
                    }
                    else {
                        $box.find('#jbox-content').html(tip.options.content.substr(5)).end().css({ left: (($window.width() - $box.outerWidth()) / 2) }).animate({ top: top, opacity: 0.1 }, 500, removeBoxImpl);
                    }
                }
                else {
                    $box.animate({ top: '-=200', opacity: 0 }, 500, removeBoxImpl);
                }
            }
            else {
                switch (options.showType) {
                    case 'slide':
                        $box.slideUp(options.showSpeed, removeBoxImpl);
                        break;
                    case 'fade':
                        $box.fadeOut(options.showSpeed, removeBoxImpl);
                        break;
                    case 'show':
                    default:
                        $box.hide(options.showSpeed, removeBoxImpl);
                        break;
                }
            }
        };
        var removeBoxImpl = function () {
            $window.unbind('resize', setFade);
            if (options.draggable && !options.isTip && !options.isMessager) {
                $box.find('.jbox-title-panel').unbind('mousedown', onMouseDown);
            }
            if (data.type != 'IFRAME') {
                $box.find('#jbox-iframe').attr({ 'src': 'about:blank' });
            }
            $box.html('').remove();
            if (isIE6 && !options.isTip) {
                $body.unbind('scroll', ie6scroll);
            }
            if (options.showFade) {
                $boxFade.fadeOut('fast', function () {
                    $boxFade.unbind('click', fadeClicked).unbind('mousedown', setTop).html('').remove();
                });
            }
            $boxBody.unbind('keydown keypress', keyPressEventHandler).html('').remove();
            if (isIE6 && options.showFade) {
                $('select').css('visibility', 'visible');
                //$(data.selects).attr('disabled', false);
            }
            if (typeof options.closed == 'function') { options.closed(); }
        };
        var autoClosing = function () {
            if (options.timeout > 0) {
                $box.data('autoClosing', window.setTimeout(removeBox, options.timeout));
                if (options.isMessager) {
                    $box.hover(function () {
                        window.clearTimeout($box.data('autoClosing'));
                    }, function () {
                        $box.data('autoClosing', window.setTimeout(removeBox, options.timeout));
                    });
                }
            }
        };
        var loaded = function () {
            if (typeof options.loaded == 'function') {
                options.loaded($box.find('.jbox-state:visible').find('.jbox-content'));
            }
        };

        if (!data.isObject) {
            switch (data.type) {
                case "GET":
                case "POST":
                    $.ajax({
                        type: data.type,
                        url: data.url,
                        data: options.ajaxData == undefined ? {} : options.ajaxData,
                        dataType: 'html',
                        cache: false,
                        success: function (data, textStatus) {
                            $box.find('#jbox-content').css({ position: "static" }).html(data).show().prev().hide();
                            loaded();
                        },
                        error: function () {
                            $box.find('#jbox-content-loading').html('<div style="padding-top:50px;padding-bottom:50px;text-align:center;">Loading Error.</div>');
                        }
                    });
                    break;
                case "IFRAME":
                    $box.find('#jbox-iframe').attr({ 'src': data.url }).bind("load", function (event) {
                        $(this).parent().css({ position: "static" }).show().prev().hide();
                        $box.find('#jbox-states .jbox-state:first .jbox-button-focus').focus();
                        loaded();
                    });
                    break;
                default:
                    $box.find('#jbox-content').show();
                    break;
            }
        }
        positionBox();
        styleBox();

        if (isIE6 && !options.isTip) {
            $window.scroll(ie6scroll);
        }
        if (options.showFade) {
            $boxFade.click(fadeClicked);
        }
        $window.resize(setFade);
        $boxBody.bind('keydown keypress', keyPressEventHandler);
        $box.find('.jbox-close').click(removeBox);

        if (options.showFade) {
            $boxFade.fadeIn('fast');
        }
        var showFunc = 'show';
        if (options.showType == 'slide') {
            showFunc = 'slideDown';
        } else if (options.showType == 'fade') {
            showFunc = 'fadeIn';
        }
        if (options.isMessager) {
            $box[showFunc](options.showSpeed, autoClosing);
        } else {
            var tip = $(document.body).data('tip');
            if (tip && tip.next == true) {
                $(document.body).data('tip', { next: false, options: {} });
                $box.css('display', '');
            } else {
                if (!data.isObject && isRequest) {
                    $box[showFunc](options.showSpeed);
                } else {
                    $box[showFunc](options.showSpeed, loaded); /* loaded */
                }
            }
        }
        if (!options.isTip) {
            $box.find('.jbox-bottom-text').html(options.bottomText);
        } else {
            $box.find('.jbox-container,.jbox-content').addClass('jbox-tip-color');
        }
        if (data.type != 'IFRAME') {
            $box.find('#jbox-states .jbox-state:first .jbox-button-focus').focus();
        }
        else {
            $box.focus();
        }
        if (!options.isMessager) {
            autoClosing();
        }
        $boxBody.bind('mousedown', setTop);
        if (options.draggable && !options.isTip && !options.isMessager) {
            $box.find('.jbox-title-panel').bind('mousedown', { target: $box }, onMouseDown).css('cursor', 'move');
        }
        return $boxBody;
    };

    $.jBox.version = 2.3;
    $.jBox.defaults = {
        id: null,
        top: "15%",
        zIndex: 1984,
        border: 5,
        opacity: 0.1,
        timeout: 0,
        showType: 'fade',
        showSpeed: 'fast',
        showIcon: true,
        showClose: true,
        draggable: true,
        dragLimit: true,
        dragClone: false,
        persistent: true,
        showScrolling: true,
        ajaxData: {},
        iframeScrolling: 'auto',

        title: 'jBox',
        width: 350,
        height: 'auto',
        bottomText: '',
        buttons: { '确定': 'ok' },
        buttonsFocus: 0,
        loaded: function (h) { },
        submit: function (v, h, f) { return true; },
        closed: function () { }
    };
    $.jBox.stateDefaults = { content: '', buttons: { '确定': 'ok' }, buttonsFocus: 0, submit: function (v, h, f) { return true; } };
    $.jBox.tipDefaults = { content: '', icon: 'info', top: '40%', width: 'auto', height: 'auto', opacity: 0, timeout: 3000, closed: function () { } };
    $.jBox.messagerDefaults = { content: '', title: 'jBox', icon: 'none', width: 350, height: 'auto', timeout: 3000, showType: 'slide', showSpeed: 600, border: 0, buttons: {}, buttonsFocus: 0, loaded: function () { }, submit: function (v, h, f) { return true; }, closed: function () { } };
    $.jBox.languageDefaults = { close: '关闭', ok: '确定', yes: '是', no: '否', cancel: '取消' };

    $.jBox.setDefaults = function (configs) {
        $.jBox.defaults = $.extend({}, $.jBox.defaults, configs.defaults);
        $.jBox.stateDefaults = $.extend({}, $.jBox.stateDefaults, configs.stateDefaults);
        $.jBox.tipDefaults = $.extend({}, $.jBox.tipDefaults, configs.tipDefaults);
        $.jBox.messagerDefaults = $.extend({}, $.jBox.messagerDefaults, configs.messagerDefaults);
        $.jBox.languageDefaults = $.extend({}, $.jBox.languageDefaults, configs.languageDefaults);
    };

    $.jBox.getBox = function () {
        return $('.jbox-body').eq($('.jbox-body').length - 1);
    };
    $.jBox.getIframe = function (jBoxId) {
        var box = (typeof jBoxId == 'string') ? $('#' + jBoxId) : $.jBox.getBox();
        return box.find('#jbox-iframe').get(0);
    };
    $.jBox.getContent = function () {
        return $.jBox.getState().find('.jbox-content').html();
    };
    $.jBox.setContent = function (content) {
        return $.jBox.getState().find('.jbox-content').html(content);
    };
    $.jBox.getState = function (stateNmae) {
        if (stateNmae == undefined) {
            return $.jBox.getBox().find('.jbox-state:visible');
        } else {
            return $.jBox.getBox().find('#jbox-state-' + stateNmae);
        }
    };
    $.jBox.getStateName = function () {
        return $.jBox.getState().attr('id').replace('jbox-state-', '');
    };
    $.jBox.goToState = function (stateName, stateContent) {
        var box = $.jBox.getBox();
        if (box != undefined && box != null) {
            var $next;
            stateName = stateName || false;
            box.find('.jbox-state').slideUp('fast');
            if (typeof stateName == 'string') {
                $next = box.find('#jbox-state-' + stateName);
            } else {
                $next = stateName ? box.find('.jbox-state:visible').next() : box.find('.jbox-state:visible').prev();
            }
            $next.slideDown(350, function () {
                window.setTimeout(function () {
                    $next.find('.jbox-button-focus').focus();
                    if (stateContent != undefined) {
                        $next.find('.jbox-content').html(stateContent);
                    }
                }, 20);
            });
        }
    };
    $.jBox.nextState = function (stateContent) {
        $.jBox.goToState(true, stateContent);
    };
    $.jBox.prevState = function (stateContent) {
        $.jBox.goToState(false, stateContent);
    };
    $.jBox.close = function (token, boxType) {
        token = token || false;
        boxType = boxType || 'body';
        if (typeof token == 'string') {
            $('#' + token).find('.jbox-close').click();
        }
        else {
            var boxs = $('.jbox-' + boxType);
            if (token) {
                for (var i = 0, l = boxs.length; i < l; ++i) {
                    boxs.eq(i).find('.jbox-close').click();
                }
            } else {
                if (boxs.length > 0) {
                    boxs.eq(boxs.length - 1).find('.jbox-close').click();
                }
            }
        }
    };
    $.jBox.open = function (content, title, width, height, options) {
        var defaults = {
            content: content,
            title: title,
            width: width,
            height: height
        };
        options = $.extend({}, defaults, options);
        options = $.extend({}, $.jBox.defaults, options);
        $.jBox(options.content, options);
    };

    /*'none'、'info'、'question'、'success'、'warning'、'error'*/
    $.jBox.prompt = function (content, title, icon, options) {
        var defaults = {
            content: content,
            title: title,
            icon: icon,
            buttons: eval('({ "' + $.jBox.languageDefaults.ok + '": "ok" })')
        };
        options = $.extend({}, defaults, options);
        options = $.extend({}, $.jBox.defaults, options);

        if (options.border < 0) {
            options.border = 0;
        }
        if (options.icon != 'info' && options.icon != 'warning' && options.icon != 'success' && options.icon != 'error' && options.icon != 'question') {
            padding = '';
            options.icon = 'none';
        }
        var top = options.title == undefined ? 10 : 35;
        var minHeight = options.icon == 'none' ? 'height:auto;' : 'min-height:30px;' + (!-[1,]&&!window.XMLHttpRequest ? 'height:auto !important;height:100%;_height:30px;' : 'height:auto;');
        var html = [];
        html.push('html:');
        html.push('<div style="margin:10px;' + minHeight + 'padding-left:' + (options.icon == 'none' ? 0 : 40) + 'px;text-align:left;">');
        html.push('<span class="jbox-icon jbox-icon-' + options.icon + '" style="position:absolute; top:' + (top + options.border) + 'px;left:' + (10 + options.border) + 'px; width:32px; height:32px;"></span>');
        html.push(options.content);
        html.push('</div>');
        options.content = html.join('');
        $.jBox(options.content, options);
    };
    $.jBox.alert = function (content, title, options) {
        $.jBox.prompt(content, title, 'none', options);
    };
    $.jBox.info = function (content, title, options) {
        $.jBox.prompt(content, title, 'info', options);
    };
    $.jBox.success = function (content, title, options) {
        $.jBox.prompt(content, title, 'success', options);
    };
    $.jBox.error = function (content, title, options) {
        $.jBox.prompt(content, title, 'error', options);
    };
    $.jBox.confirm = function (content, title, submit, options) {
        var defaults = {
            buttons: eval('({ "' + $.jBox.languageDefaults.ok + '": "ok", "' + $.jBox.languageDefaults.cancel + '": "cancel" })')
        };
        if (submit != undefined && typeof submit == 'function') {
            defaults.submit = submit;
        } else {
            defaults.submit = function (v, h, f) { return true; };
        }
        options = $.extend({}, defaults, options);
        $.jBox.prompt(content, title, 'question', options);
    };
    $.jBox.warning = function (content, title, submit, options) {
        var defaults = {
            buttons: eval('({ "' + $.jBox.languageDefaults.yes + '": "yes", "' + $.jBox.languageDefaults.no + '": "no", "' + $.jBox.languageDefaults.cancel + '": "cancel" })')
        };
        if (submit != undefined && typeof submit == 'function') {
            defaults.submit = submit;
        } else {
            defaults.submit = function (v, h, f) { return true; };
        }
        options = $.extend({}, defaults, options);
        $.jBox.prompt(content, title, 'warning', options);
    };

    /*'info'、'success'、'warning'、'error'、'loading'*/
    $.jBox.tip = function (content, icon, options) {
        var defaults = {
            content: content,
            icon: icon,
            opacity: 0,
            border: 0,
            showClose: false,
            buttons: {},
            isTip: true
        };
        if (defaults.icon == 'loading') {
            defaults.timeout = 0;
            defaults.opacity = 0.1;
        }
        options = $.extend({}, defaults, options);
        options = $.extend({}, $.jBox.tipDefaults, options);
        options = $.extend({}, $.jBox.defaults, options);

        if (options.timeout < 0) {
            options.timeout = 0;
        }
        if (options.border < 0) {
            options.border = 0;
        }
        if (options.icon != 'info' && options.icon != 'warning' && options.icon != 'success' && options.icon != 'error' && options.icon != 'loading') {
            options.icon = 'info';
        }

        var html = [];
        html.push('html:');
        html.push('<div style="min-height:18px;height:auto;margin:10px;padding-left:30px;padding-top:0px;text-align:left;">');
        html.push('<span class="jbox-icon jbox-icon-' + options.icon + '" style="position:absolute;top:' + (4 + options.border) + 'px;left:' + (4 + options.border) + 'px; width:32px; height:32px;"></span>');
        html.push(options.content);
        html.push('</div>');
        options.content = html.join('');

        if ($('.jbox-tip').length > 0) {
            $(document.body).data('tip', { next: true, options: options });
            $.jBox.closeTip();
        }
        if (options.focusId != undefined) {
            $('#' + options.focusId).focus(); top.$('#' + options.focusId).focus();
        }
        $.jBox(options.content, options);
    };
    $.jBox.closeTip = function () {
        $.jBox.close(false, 'tip');
    };

    $.jBox.messager = function (content, title, timeout, options) {
        $.jBox.closeMessager();
        var defaults = {
            content: content,
            title: title,
            timeout: (timeout == undefined ? $.jBox.messagerDefaults.timeout : timeout),
            opacity: 0,
            showClose: true,
            draggable: false,
            isMessager: true
        };
        options = $.extend({}, defaults, options);
        options = $.extend({}, $.jBox.messagerDefaults, options);
        var tempDefaults = $.extend({}, $.jBox.defaults, {});
        tempDefaults.title = null;
        options = $.extend({}, tempDefaults, options);

        if (options.border < 0) {
            options.border = 0;
        }
        if (options.icon != 'info' && options.icon != 'warning' && options.icon != 'success' && options.icon != 'error' && options.icon != 'question') {
            padding = '';
            options.icon = 'none';
        }
        var top = options.title == undefined ? 10 : 35;
        var minHeight = options.icon == 'none' ? 'height:auto;' : 'min-height:30px;' + (!-[1,]&&!window.XMLHttpRequest ? 'height:auto !important;height:100%;_height:30px;' : 'height:auto;');
        var html = [];
        html.push('html:');
        html.push('<div style="margin:10px;' + minHeight + 'padding-left:' + (options.icon == 'none' ? 0 : 40) + 'px;text-align:left;">');
        html.push('<span class="jbox-icon jbox-icon-' + options.icon + '" style="position:absolute; top:' + (top + options.border) + 'px;left:' + (10 + options.border) + 'px; width:32px; height:32px;"></span>');
        html.push(options.content);
        html.push('</div>');
        options.content = html.join('');
        $.jBox(options.content, options);
    }
    $.jBox.closeMessager = function () {
        $.jBox.close(false, 'messager');
    };

    $.jBox.FadeBoxCount = 0;

    window.jBox = $.jBox;
})(jQuery);
