(function ($) {
    $.pe = $.pe ||
    {
        version: '0.3.7'
    };
    $.fn.easyDrag = function (h) {
        return i(this, h, 'd')
    };
    $.fn.easyResize = function (h) {
        return i(this, h, 'r')
    };
    $.dragAndResize = {
        dragAndResize: {},
        e: 0,
        drag: function (v) {
            if (M.k == 'd')
                E.css({
                    left: M.X + v.pageX - M.pX,
                    top: M.Y + v.pageY - M.pY
                });
            else
                E.css({
                    width: Math.max(v.pageX - M.pX + M.W, 0),
                    height: Math.max(v.pageY - M.pY + M.H, 0)
                });
            return false
        },
        stop: function () {
            //E.css('opacity', 1);
            $(document).unbind('mousemove', J.drag).unbind('mouseup', J.stop)
        }
    };
    var J = $.dragAndResize, M = J.dragAndResize, E = J.e, i = function (e, h, k) {
        return e.each(function () {
            h = (h) ? $(h, e) : e;
            h.css('cursor', 'move');
            h.bind('mousedown', {
                e: e,
                k: k
            }, function (v) {
                var d = v.data, p = {};
                E = d.e;
                if (E.css('position') != 'relative') {
                    try {
                        E.position(p);
                    }
                    catch (e) {
                    }
                }
                M = {
                    X: p.left || f('left') || 0,
                    Y: p.top || f('top') || 0,
                    W: f('width') || E[0].scrollWidth || 0,
                    H: f('height') || E[0].scrollHeight || 0,
                    pX: v.pageX,
                    pY: v.pageY,
                    k: d.k
                };
                E.css({
                    //opacity: 0.8
                });
                $(document).mousemove($.dragAndResize.drag).mouseup($.dragAndResize.stop);
                return false
            })
        })
    }, f = function (k) {
        return parseInt(E.css(k)) || false
    };
    var l;
    l = $.pe.expose = {
        conf: {
            maskId: 'expose',
            loadSpeed: 'slow',
            closeSpeed: 'fast',
            closeOnClick: true,
            closeOnEsc: true,
            zIndex: 9998,
            opacity: 0.5,
            startOpacity: 0,
            color: '#000',
            onLoad: null,
            onClose: null
        }
    };
    function viewport() {
        if ($.browser.msie) {
            var d = $(document).height(), w = $(window).height();
            return [window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth, d - w < 20 ? w : d]
        }
        return [$(document).width(), $(document).height()]
    }
    function call(a) {
        if (a) {
            return a.call($.mask)
        }
    }
    var m, exposed, loaded, config, overlayIndex;
    $.mask = {
        load: function (b, c) {
            if (loaded) {
                return this
            }
            if (typeof b == 'string') {
                b = {
                    color: b
                }
            }
            b = b || config;
            config = b = $.extend($.extend({}, l.conf), b);
            m = $("#" + b.maskId);
            if (!m.length) {
                m = $('<div/>').attr("id", b.maskId);
                $("body").append(m)
            }
            var d = viewport();
            m.css({
                position: 'absolute',
                top: 0,
                left: 0,
                width: d[0],
                height: d[1],
                display: 'none',
                opacity: b.startOpacity,
                zIndex: b.zIndex
            });
            if (b.color) {
                m.css("backgroundColor", b.color)
            }
            if (call(b.onBeforeLoad) === false) {
                return this
            }
            if (b.closeOnEsc) {
                $(document).bind("keydown.mask", function (e) {
                    if (e.keyCode == 27) {
                        $.mask.close(e)
                    }
                })
            }
            if (b.closeOnClick) {
                m.bind("click.mask", function (e) {
                    $.mask.close(e)
                })
            }
            $(window).bind("resize.mask", function () {
                $.mask.fit()
            });
            if (c && c.length) {
                overlayIndex = c.eq(0).css("zIndex");
                $.each(c, function () {
                    var a = $(this);
                    if (!/relative|absolute|fixed/i.test(a.css("position"))) {
                        a.css("position", "relative")
                    }
                });
                exposed = c.css({
                    zIndex: Math.max(b.zIndex + 1, overlayIndex == 'auto' ? 0 : overlayIndex)
                })
            }
            m.css({
                display: 'block'
            }).fadeTo(b.loadSpeed, b.opacity, function () {
                $.mask.fit();
                call(b.onLoad)
            });
            loaded = true;
            return this
        },
        close: function () {
            if (loaded) {
                if (call(config.onBeforeClose) === false) {
                    return this
                }
                m.fadeOut(config.closeSpeed, function () {
                    call(config.onClose);
                    if (exposed) {
                        exposed.css({
                            zIndex: overlayIndex
                        })
                    }
                });
                $(document).unbind("keydown.mask");
                m.unbind("click.mask");
                $(window).unbind("resize.mask");
                loaded = false
            }
            return this
        },
        fit: function () {
            if (loaded) {
                var a = viewport();
                m.css({
                    width: a[0],
                    height: a[1]
                })
            }
        },
        getMask: function () {
            return m
        },
        isLoaded: function () {
            return loaded
        },
        getConf: function () {
            return config
        },
        getExposed: function () {
            return exposed
        }
    };
    $.fn.mask = function (a) {
        $.mask.load(a);
        return this
    };
    $.fn.expose = function (a) {
        $.mask.load(a, this);
        return this
    };
    $.pe.overlay = {
        addEffect: function (a, b, c) {
            effects[a] = [b, c]
        },
        conf: {
            close: null,
            closeOnClick: true,
            closeOnEsc: true,
            closeSpeed: 'fast',
            effect: 'default',
            render: false,
            width: 500,
            height: 360,
            title: '标题',
            loadUrl: 'http://www.powereasy.net',
            fixed: !$.browser.msie || $.browser.version > 6,
            left: 'center',
            load: false,
            mask: null,
            oneInstance: true,
            speed: 'normal',
            target: null,
            top: 'center'
        }
    };
    var n = [], effects = {};
    $.pe.overlay.addEffect('default', function (a, b) {
        var c = this.getConf(), w = $(window);
        if (!c.fixed) {
            a.top += w.scrollTop();
            a.left += w.scrollLeft()
        }
        a.position = c.fixed ? 'fixed' : 'absolute';
        this.getOverlay().css(a).fadeIn(c.speed, b)
    }, function (a) {
        this.getOverlay().fadeOut(this.getConf().closeSpeed, a)
    });
    function Overlay(c, d) {
        var f = this, fire = c.add(f), w = $(window), closers, overlay, opened, maskConf = $.pe.expose && (d.mask || d.expose), uid = Math.random().toString().slice(10);
        if (maskConf) {
            if (typeof maskConf == 'string') {
                maskConf = {
                    color: maskConf
                }
            }
            maskConf.closeOnClick = maskConf.closeOnEsc = false
        }
        var g = d.target || c.attr("rel");
        overlay = g ? $(g) : null || c;
        if (d.render) {
            overlay.html('');
            var h = '<div style="width:' + d.width + 'px;height:' + d.height + 'px" class="overlay">';
            h += '<div class="overlay_header"><span class="overlay_header_title_sign"></span><h3 class="overlay_header_title">';
            h += d.title;
            h += '</h3><span class="overlay_header_background_right"></span>';
            h += '<a class="overlay_header_close" href="javascript:void(0)"></a></div>';
            h += '<div class="overlay_body"><iframe height="' + (d.height - 30) + '" frameborder="0" width="100%" src="';
            h += d.loadUrl;
            h += ' "marginwidth="0" marginheight="0"></iframe><iframe id="iframe_IE6_Z-Index" width="' + d.width + '" frameborder=0 height="' + d.height + '" style="position:absolute; top:0px; left:0px; z-index:-1; border-style:none;"></iframe></div>';
            overlay.append(h)
        }
        if (!overlay.length) {
            throw "Could not find Overlay: " + g;
        }
        if (c && c.index(overlay) == -1) {
            c.click(function (e) {
                f.load(e);
                return e.preventDefault()
            })
        }

        $.extend(f, {
            load: function (e) {
                if (f.isOpened()) {
                    return f
                }
                var a = effects[d.effect];
                if (!a) {
                    throw "Overlay: cannot find effect : \"" + d.effect + "\"";
                }
                if (d.oneInstance) {
                    $.each(n, function () {
                        this.close(e)
                    })
                }
                e = e || $.Event();
                e.type = "onBeforeLoad";
                fire.trigger(e);
                if (e.isDefaultPrevented()) {
                    return f
                }
                opened = true;
                if (maskConf) {
                    $(overlay).expose(maskConf)
                }
                var b = d.top, left = d.left, oWidth = overlay.outerWidth({
                    margin: true
                }), oHeight = overlay.outerHeight({
                    margin: true
                });
                if (typeof b == 'string') {
                    b = b == 'center' ? Math.max((w.height() - d.height) / 2, 0) : parseInt(b, 10) / 100 * w.height()
                }
                if (left == 'center') {
                    left = Math.max((w.width() - d.width) / 2, 0)
                }
                a[0].call(f, {
                    top: b,
                    left: left
                }, function () {
                    if (opened) {
                        e.type = "onLoad";
                        fire.trigger(e)
                    }
                });

                fire.css('display', 'block');

                if (maskConf && d.closeOnClick) {
                    $.mask.getMask().one("click", f.close)
                }
                if (d.closeOnClick) {
                    $(document).bind("click." + uid, function (e) {
                        if (!$(e.target).parents(overlay).length) {
                            f.close(e)
                        }
                    })
                }
                if (d.closeOnEsc) {
                    $(document).bind("keydown." + uid, function (e) {
                        if (e.keyCode == 27) {
                            f.close(e)
                        }
                    })
                }
                return f
            },
            close: function (e) {
                if (!f.isOpened()) {
                    return f
                }
                e = e || $.Event();
                e.type = "onBeforeClose";
                fire.trigger(e);
                if (e.isDefaultPrevented()) {
                    return
                }
                opened = false;
                effects[d.effect][1].call(f, function () {
                    e.type = "onClose";
                    fire.trigger(e)
                });
                $(document).unbind("click." + uid).unbind("keydown." + uid);
                if (maskConf) {
                    $.mask.close()
                }
                return f
            },
            getOverlay: function () {
                return overlay
            },
            getTrigger: function () {
                return c
            },
            getClosers: function () {
                return closers
            },
            isOpened: function () {
                return opened
            },
            getConf: function () {
                return d
            }
        });
        $.each("onBeforeLoad,onStart,onLoad,onBeforeClose,onClose".split(","), function (i, b) {
            if ($.isFunction(d[b])) {
                $(f).bind(b, d[b])
            }
            f[b] = function (a) {
                $(f).bind(b, a);
                return f
            }
        });
        var j = overlay.find('.overlay_header');
        overlay.easyDrag(j);
        closers = overlay.find(d.close || ".overlay_header_close" || ".close");
        if (!closers.length && !d.close) {
            closers = $('<a class="close"></a>');
            overlay.prepend(closers)
        }
        closers.click(function (e) {
            f.close(e)
        });
        if (d.load) {
            f.load()
        }
    }
    $.fn.overlay = function (a) {
        var b = this.data("overlay");
        if (b) {
            this.removeData("overlay")
        }
        if ($.isFunction(a)) {
            a = {
                onBeforeLoad: a
            }
        }
        a = $.extend(true, {}, $.pe.overlay.conf, a);
        this.each(function () {
            b = new Overlay($(this), a);
            n.push(b);
            $(this).data("overlay", b)
        });
        return a.api ? b : this
    };
    $.pe.tabs = {
        conf: {
            tabs: 'a',
            current: 'current',
            onBeforeClick: null,
            onClick: null,
            effect: 'default',
            initialIndex: 0,
            event: 'click',
            rotate: false,
            history: false
        },
        addEffect: function (a, b) {
            h[a] = b
        }
    };
    var h = {
        'default': function (i, a) {
            this.getPanes().hide().eq(i).show();
            a.call()
        },
        fade: function (i, a) {
            var b = this.getConf(), speed = b.fadeOutSpeed, panes = this.getPanes();
            if (speed) {
                panes.fadeOut(speed)
            }
            else {
                panes.hide()
            }
            panes.eq(i).fadeIn(b.fadeInSpeed, a)
        },
        slide: function (i, a) {
            this.getPanes().slideUp(200);
            this.getPanes().eq(i).slideDown(400, a)
        },
        ajax: function (i, a) {
            this.getPanes().eq(0).load(this.getTabs().eq(i).attr("href"), a)
        }
    };
    var w;
    $.pe.tabs.addEffect("horizontal", function (i, a) {
        if (!w) {
            w = this.getPanes().eq(0).width()
        }
        this.getCurrentPane().animate({
            width: 0
        }, function () {
            $(this).hide()
        });
        this.getPanes().eq(i).animate({
            width: w
        }, function () {
            $(this).show();
            a.call()
        })
    });
    function Tabs(c, d, f) {
        var g = this, trigger = c.add(this), tabs = c.find(f.tabs), panes = d.jquery ? d : c.children(d), current;
        if (!tabs.length) {
            tabs = c.children()
        }
        if (!panes.length) {
            panes = c.parent().find(d)
        }
        if (!panes.length) {
            panes = $(d)
        }
        $.extend(this, {
            click: function (i, e) {
                var a = tabs.eq(i);
                if (typeof i == 'string' && i.replace("#", "")) {
                    a = tabs.filter("[href*=" + i.replace("#", "") + "]");
                    i = Math.max(tabs.index(a), 0)
                }
                if (f.rotate) {
                    var b = tabs.length - 1;
                    if (i < 0) {
                        return g.click(b, e)
                    }
                    if (i > b) {
                        return g.click(0, e)
                    }
                }
                if (!a.length) {
                    if (current >= 0) {
                        return g
                    }
                    i = f.initialIndex;
                    a = tabs.eq(i)
                }
                if (i === current) {
                    return g
                }
                e = e || $.Event();
                e.type = "onBeforeClick";
                trigger.trigger(e, [i]);
                if (e.isDefaultPrevented()) {
                    return
                }
                h[f.effect].call(g, i, function () {
                    e.type = "onClick";
                    trigger.trigger(e, [i])
                });
                current = i;
                tabs.removeClass(f.current);
                a.addClass(f.current);
                return g
            },
            getConf: function () {
                return f
            },
            getTabs: function () {
                return tabs
            },
            getPanes: function () {
                return panes
            },
            getCurrentPane: function () {
                return panes.eq(current)
            },
            getCurrentTab: function () {
                return tabs.eq(current)
            },
            getIndex: function () {
                return current
            },
            next: function () {
                return g.click(current + 1)
            },
            prev: function () {
                return g.click(current - 1)
            },
            destroy: function () {
                tabs.unbind(f.event).removeClass(f.current);
                panes.find("a[href^=#]").unbind("click.T");
                return g
            }
        });
        $.each("onBeforeClick,onClick".split(","), function (i, b) {
            if ($.isFunction(f[b])) {
                $(g).bind(b, f[b])
            }
            g[b] = function (a) {
                $(g).bind(b, a);
                return g
            }
        });
        if (f.history && $.fn.history) {
            $.pe.history.init(tabs);
            f.event = 'history'
        }
        tabs.each(function (i) {
            $(this).bind(f.event, function (e) {
                g.click(i, e);
                return e.preventDefault()
            })
        });
        panes.find("a[href^=#]").bind("click.T", function (e) {
            g.click($(this).attr("href"), e)
        });
        if (location.hash) {
            g.click(location.hash)
        }
        else {
            if (f.initialIndex === 0 || f.initialIndex > 0) {
                g.click(f.initialIndex)
            }
        }
    };
    $.fn.tabs = function (a, b) {
        var c = this.data("tabs");
        if (c) {
            c.destroy();
            this.removeData("tabs")
        }
        if ($.isFunction(b)) {
            b = {
                onBeforeClick: b
            }
        }
        b = $.extend({}, $.pe.tabs.conf, b);
        this.each(function () {
            c = new Tabs($(this), a, b);
            $(this).data("tabs", c)
        });
        return b.api ? c : this
    };

    $.fn.extend({
        jsRightMenu: function (options) {
            options = $.extend({
                menuList: []
            }, options);
            if ($("#div_RightMenu").size() == 0);
            {
                $(document.body).append("\<div class=\"div_RightMenu\" id=\"div_RightMenu\"\>\<\/div>");
                $("#div_RightMenu").hide();
            }
            return this.each(function () {
                this.oncontextmenu = function () {
                    var menuCount = options.menuList.length;
                    var divMunuItem = "";
                    if (menuCount > 0) {
                        for (var i = 0; i < menuCount; i++) {
                            divMunuItem += "<div class=\"divMenuItem\" id=\"divMenuItem" + $(this).attr("id") + "\" onclick=\"" + options.menuList[i].clickEvent + "\"  onmouseover=\"" + options.menuList[i].mouseoverEvent + "\" onmouseout=\"" + options.menuList[i].mouseoutEvent + "\">" + options.menuList[i].menuName + "</div>";
                        }
                        $("#div_RightMenu").html(divMunuItem);
                        $("#div_RightMenu").hide();
                    }
                    var objMenu = $("#div_RightMenu");
                    if (objMenu.size() > 0) {
                        objMenu.hide();
                        var event = arguments[0] || window.event;
                        var clientX = event.clientX;
                        var clientY = event.clientY;
                        var redge = document.documentElement.clientWidth - clientX;
                        var bedge = document.documentElement.clientHeight - clientY;
                        var menu = objMenu.get(0);
                        var menuLeft = 0;
                        var menuTop = 0;
                        if (redge < menu.offsetWidth)
                            menuLeft = document.documentElement.scrollLeft + clientX - menu.offsetWidth;
                        else
                            menuLeft = document.documentElement.scrollLeft + clientX;
                        if (bedge < menu.offsetHeight)
                            menuTop = document.documentElement.scrollTop + clientY - menu.offsetHeight;
                        else
                            menuTop = document.documentElement.scrollTop + clientY;
                        objMenu.css({ top: menuTop + "px", left: menuLeft + "px" });
                        objMenu.show();
                        return false;
                    }
                }
                document.onclick = function () {
                    var objMenu = $("#div_RightMenu");
                    if (objMenu.size() > 0) objMenu.hide();
                }
            });
        }
    });

    $.fn.extend({
        check: function () {
            return this.each(function () {
                this.checked = true
            })
        },
        uncheck: function () {
            return this.each(function () {
                this.checked = false
            })
        },
        inverse: function () {
            return this.each(function () {
                this.checked = (this.checked == true ? false : true);
            })
        },
        shiftSelect: function (m) {
            var g = this;
            var h;
            $(this).click(function (a) {
                if (!h) {
                    h = this;
                    return
                }
                if (a.shiftKey) {
                    var b = g.index(this);
                    var c = g.index(h);
                    var d = h.checked;
                    if (b == c) {
                        return true
                    }
                    var e = Math.max(b, c);
                    var f = Math.min(b, c);
                    for (i = f; i <= e; i++) {
                        g[i].checked = d
                    }
                    if ($.isFunction(m))
                        call(m);
                }
                h = this
            })
        },
        lostFocus: function () {
            $(this).focus(function () {
                this.blur()
            })
        },
        addFavorite: function () {
            $(this).click(function () {
                var a = $('title').text();
                if (document.all)
                    window.external.addFavorite(location.href, a);
                else
                    if (window.sidebar)
                        window.sidebar.addPanel(a, location.href, null);
                    else
                        alert('您可以尝试通过快捷键CTRL + D 加入到收藏夹')
            })
        },
        setHomePage: function () {
            $(this).click(function () {
                if (document.all) {
                    document.body.style.behavior = 'url(#default#homepage)';
                    document.body.setHomePage(location.href)
                }
                else
                    if (window.sidebar) {
                        if (window.netscape) {
                            try {
                                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect")
                            }
                            catch (e) {
                                alert("该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项 signed.applets.codebase_principal_support 值该为true");
                                return
                            }
                        }
                        var a = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);
                        a.setCharPref('browser.startup.homepage', location.href)
                    }
            });
        },
        imageResize: function (options) {
            var params = jQuery.extend({
                height: 500,
                width: 660
            }, options);

            this.each(function () {
                var height = params.height,
				width = params.width,
				img_height = $(this).height(),
				img_width = $(this).width(),
				m_ceil = Math.ceil,
				m_floor = Math.floor;
                if (img_height <= height && img_width <= width) {
                    return;
                }

                if (img_height >= img_width) {
                    width = m_floor(m_ceil(img_width / img_height * height));
                } else {
                    height = m_floor(m_ceil(img_height / img_width * width));
                }
                $(this).attr({
                    'height': height,
                    'width': width
                }).css({ 'height': height + 'px', 'width': width + 'px' });
            });
        },
        useKeypressSubmit: function (target) {
            $(this).keypress(function (e) {
                if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
                    $(target).click();
                    return false;
                }
                return true;
            })
        }
    })
    $.extend($.pe, {
        htmlEncode: function (text) {
            var standard = function (text) {
                var span = $('<span>');
                span.html(text);
                return span.html();
            };

            var fix1 = (standard('\n').toLowerCase() == '<br>') ?
				function (text) {
				    // #3874 IE and Safari encode line-break into <br>
				    return standard(text).replace(/<br>/gi, '\n');
				} :
				standard;

            var fix2 = (standard('>') == '>') ?
				function (text) {
				    // WebKit does't encode the ">" character, which makes sense, but
				    // it's different than other browsers.
				    return fix1(text).replace(/>/g, '&gt;');
				} :
				fix1;

            var fix3 = (standard('  ') == '&nbsp; ') ?
				function (text) {
				    // #3785 IE8 changes spaces (>= 2) to &nbsp;
				    return fix2(text).replace(/&nbsp;/g, ' ');
				} :
				fix2;

            this.htmlEncode = fix3;

            return this.htmlEncode(text);
        },
        random: function (a) {
            return Math.floor(a * (Math.random() % 1))
        },
        cookie: function (a, b, c) {
            if (typeof b != 'undefined') {
                c = c || {};
                if (b === null) {
                    b = '';
                    c.expires = -1
                }
                var d = '';
                if (c.expires && (typeof c.expires == 'number' || c.expires.toUTCString)) {
                    var e;
                    if (typeof c.expires == 'number') {
                        e = new Date();
                        e.setTime(e.getTime() + (c.expires * 24 * 60 * 60 * 1000))
                    }
                    else {
                        e = c.expires
                    }
                    d = '; expires=' + e.toUTCString()
                }
                var f = c.path ? '; path=' + (c.path) : '', domain = c.domain ? '; domain=' + (c.domain) : '', secure = c.secure ? '; secure' : '';
                document.cookie = [a, '=', encodeURIComponent(b), d, f, domain, secure].join('')
            }
            else {
                var g = null;
                if (document.cookie && document.cookie != '') {
                    var h = document.cookie.split(';');
                    for (var i = 0; i < h.length; i++) {
                        var j = $.trim(h[i]);
                        if (j.substring(0, a.length + 1) == (a + '=')) {
                            g = decodeURIComponent(j.substring(a.length + 1));
                            break
                        }
                    }
                }
                return g
            }
        },
        stringToJSON: function (string) {
            return eval('(' + string + ')');
        },
        jsonToString: function (obj) {
            var THIS = this;
            switch (typeof (obj)) {
                case 'string':
                    return '"' + obj.replace(/(["\\])/g, '\\$1') + '"';
                case 'array':
                    return '[' + obj.map(THIS.jsonToString).join(',') + ']';
                case 'object':
                    if (obj instanceof Array) {
                        var strArr = [];
                        var len = obj.length;
                        for (var i = 0; i < len; i++) {
                            strArr.push(THIS.jsonToString(obj[i]));
                        }
                        return '[' + strArr.join(',') + ']';
                    } else if (obj == null) {
                        return 'null';

                    } else {
                        var string = [];
                        for (var property in obj) string.push(THIS.jsonToString(property) + ':' + THIS.jsonToString(obj[property]));
                        return '{' + string.join(',') + '}';
                    }
                case 'number':
                    return obj;
                case false:
                    return obj;
            }
        },
        ajax: function (e, f) {
            if (typeof (e) != 'string' || e == 'undefind')
                return;
            var g = {
                url: siteSetup.ajaxPath,
                data: '',
                type: 'POST',
                dataType: 'xml',
                params: {}
            };
            var h = function (a, b) {
                var c = '';
                for (var d in a.params) {
                    if (b)
                        c += ('<attrib><' + d + '>' + $.pe.htmlEncode(a.params[d]) + '</' + d + '></attrib>');
                    else
                        c += ('<' + d + '>' + $.pe.htmlEncode(a.params[d]) + '</' + d + '>')
                }
                return c
            }
            var i = $.extend(g, f);
            var e = $.trim(e).toLowerCase();
            switch (e) {
                case 'accesslabel':
                    e = 'updatelabel';
                    break;
                case 'accesspage':
                    e = 'updatepage';
                    break;
                case 'checkuserlogin':
                    e = 'logincheck';
                    break;
                case 'checkloginvalidate':
                    e = 'EnableValidCode';
                    break;
                case 'logout':
                    e = 'userlogout';
                    break;
                default:
                    break;
            }
            var j = '<root><type>' + e + '</type>';
            switch (e) {
                case 'updatelabel':
                    j += ('<labelname>' + i.labelName + '</labelname>')
                    j += ('<currentpage>' + i.currentPage + '</currentpage>');
                    j += h(i, true);
                    break;
                case 'updatepage':
                    j += ('<labelname>' + i.labelName + '</labelname>');
                    j += ('<sourcename>' + i.pageName + '</sourcename>');
                    j += ('<pagesize>' + i.pageSize + '</pagesize>');
                    j += ('<currentpage>' + i.currentPage + '</currentpage>');
                    j += ('<total>' + i.recordCount + '</total>');
                    break;
                default:
                    j += h(i, false);
                    break;
            }
            j += '</root>';
            i.data = j;
            $.ajax(i)
        },
        refreshValidateCode: function (a) {
            var path = siteSetup.sitePath + 'Controls/ValidateCodeImage.aspx?code=' + $.pe.random(100);
            $(a).attr('src', path);
        },
        replaceUserInfoVariable: function (a, b) {
            a = a.replace('{username}', b.find('username').text());
            a = a.replace('{experience}', b.find('exp').text());
            a = a.replace('{message}', b.find('msg').text());
            a = a.replace('{loginCount}', b.find('logintimes').text());
            a = a.replace('{pointName}', b.find('pointname').text());
            a = a.replace('{point}', b.find('point').text());
            a = a.replace('{pointUnit}', b.find('pointunit').text());
            a = a.replace('{signinArticle}', b.find('signincontent').text());
            a = a.replace('{balances}', b.find('balance').text());
            return a
        },
        supplyDemandInfoVariable: function (a, b) {
            a = a.replace('{username}', b.find('username').text());
            a = a.replace('{experience}', b.find('exp').text());
            a = a.replace('{message}', b.find('msg').text());
            a = a.replace('{loginCount}', b.find('logintimes').text());
            a = a.replace('{pointName}', b.find('pointname').text());
            a = a.replace('{point}', b.find('point').text());
            a = a.replace('{signinArticle}', b.find('signincontent').text());
            a = a.replace('{balances}', b.find('balance').text());
            a = a.replace(new RegExp('{indexurl}', 'g'), b.find('indexurl').text());
            a = a.replace(new RegExp('{contacturl}', 'g'), b.find('contacturl').text());
            a = a.replace('{supplylisturl}', b.find('supplylisturl').text());
            a = a.replace('{companyurl}', b.find('companyurl').text());
            a = a.replace('{contact}', b.find('contact').text());
            a = a.replace(new RegExp('{companyname}', 'g'), b.find('companyname').text());
            return a
        }
    })
    $.pe.pealert = function (message, options) {
        message = message || "";
        message = message.replace(/\r\n/gi, "<br />");
        var defaultOptions = {
            title: '提示信息',
            top: 160
        };
        if (options) {
            defaultOptions = $.extend(defaultOptions, options);
        }
        var closeAlertBox = function () {
            $(".msgBox").remove();
            $("#expose").remove();
        }
        closeAlertBox();
        var $alerBox = $("<div class='msgBox' id='alertBox'><div class='hd'><em id='closeAlertBox'>x</em><h3>" + defaultOptions.title + "</h3></div><div class='bd'><div class='bdIn'>" + message + "<div class='subDiv'><a  class='btn'  id='btnConfirmAlert'><span>确定</span></a></div></div></div>");
        $("body").append($alerBox);

        $alerBox.overlay({
            top: defaultOptions.top,
            mask: {
                color: '#ccc',
                loadSpeed: 10,
                opacity: 0.5
            },
            closeOnClick: true,
            closeOnEsc: true,
            api: true
        }).load();

        $alerBox.easyDrag();
        $("#closeAlertBox", $alerBox).bind("click", closeAlertBox);
        $("#btnConfirmAlert", $alerBox).bind("click", closeAlertBox);
    }
})(jQuery);


(function (d) {
    function R(b, c) {
        return 32 - (new Date(b, c, 32)).getDate()
    }
    function S(b, c) {
        b = "" + b;
        for (c = c || 2; b.length < c; )
            b = "0" + b;
        return b
    }
    function T(b, c, j) {
        var m = b.getDate(), h = b.getDay(), t = b.getMonth();
        b = b.getFullYear();
        var f = {
            d: m,
            dd: S(m),
            ddd: B[j].shortDays[h],
            dddd: B[j].days[h],
            m: t + 1,
            mm: S(t + 1),
            mmm: B[j].shortMonths[t],
            mmmm: B[j].months[t],
            yy: String(b).slice(2),
            yyyy: b
        };
        c = c.replace(X, function (o) {
            return o in f ? f[o] : o.slice(1, o.length - 1)
        });
        return Y.html(c).html()
    }
    function y(b) {
        return parseInt(b, 10)
    }
    function U(b, c) {
        return b.getFullYear() === c.getFullYear() && b.getMonth() == c.getMonth() && b.getDate() == c.getDate()
    }
    function C(b) {
        if (b) {
            if (b.constructor == Date)
                return b;
            if (typeof b == "string") {
                var c = b.split("-");
                if (c.length == 3)
                    return new Date(y(c[0]), y(c[1]) - 1, y(c[2]));
                if (!/^-?\d+$/.test(b))
                    return;
                b = y(b)
            }
            c = new Date;
            c.setDate(c.getDate() + b);
            return c
        }
    }
    function Z(b, c) {
        function j(a, e, g) {
            l = a;
            D = a.getFullYear();
            E = a.getMonth();
            G = a.getDate();
            g = g || d.Event("api");
            g.type = "change";
            H.trigger(g, [a]);
            if (!g.isDefaultPrevented()) {
                b.val(T(a, e.format, e.lang));
                b.data("date", a);
                h.hide(g)
            }
        }
        function m(a) {
            a.type = "onShow";
            H.trigger(a);
            d(document).bind("keydown.d", function (e) {
                var g = e.keyCode;
                if (g == 8) {
                    b.val("");
                    return h.hide(e)
                }
                if (g == 27)
                    return h.hide(e);
                if (d(V).index(g) >= 0) {
                    if (!u) {
                        h.show(e);
                        return e.preventDefault()
                    }
                    var i = d("#" + f.weeks + " a"), p = d("." + f.focus), q = i.index(p);
                    p.removeClass(f.focus);
                    if (g == 74 || g == 40)
                        q += 7;
                    else
                        if (g == 75 || g == 38)
                            q -= 7;
                        else
                            if (g == 76 || g == 39)
                                q += 1;
                            else
                                if (g == 72 || g == 37)
                                    q -= 1;
                    if (q == -1) {
                        h.addMonth(-1);
                        p = d("#" + f.weeks + " a:last")
                    }
                    else
                        if (q ==
                        35) {
                            h.addMonth();
                            p = d("#" + f.weeks + " a:first")
                        }
                        else
                            p = i.eq(q);
                    p.addClass(f.focus);
                    return e.preventDefault()
                }
                if (g == 34)
                    return h.addMonth();
                if (g == 33)
                    return h.addMonth(-1);
                if (g == 36)
                    return h.today();
                if (g == 13)
                    d(e.target).is("select") || d("." + f.focus).click();
                return d([16, 17, 18, 9]).index(g) >= 0
            });
            d(document).bind("click.d", function (e) {
                var g = e.target;
                if (!d(g).parents("#" + f.root).length && g != b[0] && (!K || g != K[0]))
                    h.hide(e)
            })
        }
        var h = this, t = new Date, f = c.css, o = B[c.lang], k = d("#" + f.root), L = k.find("#" + f.title), K, I, J, D, E, G, l = b.attr("data-value") || c.value || b.val(), r = b.attr("min") || c.min, s = b.attr("max") || c.max, u;
        l = C(l) || t;
        r = C(r || c.yearRange[0] * 365);
        s = C(s || c.yearRange[1] * 365);
        if (!o)
            throw "Dateinput: invalid language: " + c.lang;
        if (b.attr("type") == "date") {
            var M = d("<input/>");
            d.each("name,readonly,disabled,value,required".split(","), function (a, e) {
                M.attr(e, b.attr(e))
            });
            b.replaceWith(M);
            b = M
        }
        b.addClass(f.input);
        var H = b.add(h);
        if (!k.length) {
            k = d("<div><div><a/><div/><a/></div><div><div/><div/></div></div>").hide().css({
                position: "absolute"
            }).attr("id", f.root);
            k.children().eq(0).attr("id", f.head).end().eq(1).attr("id", f.body).children().eq(0).attr("id", f.days).end().eq(1).attr("id", f.weeks).end().end().end().find("a").eq(0).attr("id", f.prev).end().eq(1).attr("id", f.next);
            L = k.find("#" + f.head).find("div").attr("id", f.title);
            if (c.selectors) {
                var z = d("<select/>").attr("id", f.month), A = d("<select/>").attr("id", f.year);
                L.append(z.add(A))
            }
            for (var $ = k.find("#" + f.days), N = 0; N < 7; N++)
                $.append(d("<span/>").text(o.shortDays[(N + c.firstDay) % 7]));
            b.after(k)
        }
        if (c.trigger)
            K = d("<a/>").attr("href", "#").addClass(f.trigger).click(function (a) {
                h.show();
                return a.preventDefault()
            }).insertAfter(b);
        var O = k.find("#" + f.weeks);
        A = k.find("#" + f.year);
        z = k.find("#" + f.month);
        d.extend(h, {
            show: function (a) {
                if (!(b.is("[readonly]") || u)) {
                    a = a || d.Event();
                    a.type = "onBeforeShow";
                    H.trigger(a);
                    if (!a.isDefaultPrevented()) {
                        d.each(W, function () {
                            this.hide()
                        });
                        u = true;
                        z.unbind("change").change(function () {
                            h.setValue(A.val(), d(this).val())
                        });
                        A.unbind("change").change(function () {
                            h.setValue(d(this).val(), z.val())
                        });
                        I = k.find("#" + f.prev).unbind("click").click(function () {
                            I.hasClass(f.disabled) || h.addMonth(-1);
                            return false
                        });
                        J = k.find("#" + f.next).unbind("click").click(function () {
                            J.hasClass(f.disabled) || h.addMonth();
                            return false
                        });
                        h.setValue(l);
                        var e = b.position();
                        k.css({
                            top: e.top + b.outerHeight({
                                margins: true
                            }) + c.offset[0],
                            left: e.left + c.offset[1]
                        });
                        if (c.speed)
                            k.show(c.speed, function () {
                                m(a)
                            });
                        else {
                            k.show();
                            m(a)
                        }
                        return h
                    }
                }
            },
            setValue: function (a, e, g) {
                var i;
                if (parseInt(e, 10) >= -1) {
                    a = y(a);
                    e = y(e);
                    g = y(g);
                    i = new Date(a, e, g)
                }
                else {
                    i = a || l;
                    a = i.getFullYear();
                    e = i.getMonth();
                    g = i.getDate()
                }
                if (e == -1) {
                    e = 11;
                    a--
                }
                else
                    if (e == 12) {
                        e = 0;
                        a++
                    }
                if (!u) {
                    j(i, c);
                    return h
                }
                E = e;
                D = a;
                i = new Date(a, e, 1 - c.firstDay);
                g = i.getDay();
                var p = R(a, e), q = R(a, e - 1), P;
                if (c.selectors) {
                    z.empty();
                    d.each(o.months, function (v, F) {
                        r < new Date(a, v + 1, -1) && s > new Date(a, v, 0) && z.append(d("<option/>").html(F).attr("value", v))
                    });
                    A.empty();
                    for (i = a + c.yearRange[0]; i < a + c.yearRange[1]; i++)
                        r < new Date(i + 1, -1, 0) && s > new Date(i, 0, 0) && A.append(d("<option/>").text(i));
                    z.val(e);
                    A.val(a)
                }
                else
                    L.html(o.months[e] +
                    " " +
                    a);
                O.empty();
                I.add(J).removeClass(f.disabled);
                for (var w = 0, n, x; w < 42; w++) {
                    n = d("<a/>");
                    if (w % 7 === 0) {
                        P = d("<div/>").addClass(f.week);
                        O.append(P)
                    }
                    if (w < g) {
                        n.addClass(f.off);
                        x = q - g + w + 1;
                        i = new Date(a, e - 1, x)
                    }
                    else
                        if (w >= g + p) {
                            n.addClass(f.off);
                            x = w - p - g + 1;
                            i = new Date(a, e + 1, x)
                        }
                        else {
                            x = w - g + 1;
                            i = new Date(a, e, x);
                            if (U(l, i))
                                n.attr("id", f.current).addClass(f.focus);
                            else
                                U(t, i) && n.attr("id", f.today)
                        }
                    r && i < r && n.add(I).addClass(f.disabled);
                    s && i > s && n.add(J).addClass(f.disabled);
                    n.attr("href", "#" + x).text(x).data("date", i);
                    P.append(n);
                    n.click(function (v) {
                        var F = d(this);
                        if (!F.hasClass(f.disabled)) {
                            d("#" + f.current).removeAttr("id");
                            F.attr("id", f.current);
                            j(F.data("date"), c, v)
                        }
                        return false
                    })
                }
                f.sunday && O.find(f.week).each(function () {
                    var v = c.firstDay ? 7 - c.firstDay : 0;
                    d(this).children().slice(v, v + 1).addClass(f.sunday)
                });
                return h
            },
            setMin: function (a, e) {
                r = C(a);
                e && l < r && h.setValue(r);
                return h
            },
            setMax: function (a, e) {
                s = C(a);
                e && l > s && h.setValue(s);
                return h
            },
            today: function () {
                return h.setValue(t)
            },
            addDay: function (a) {
                return this.setValue(D, E, G +
                (a ||
                1))
            },
            addMonth: function (a) {
                return this.setValue(D, E + (a || 1), G)
            },
            addYear: function (a) {
                return this.setValue(D + (a || 1), E, G)
            },
            hide: function (a) {
                if (u) {
                    a = a || d.Event();
                    a.type = "onHide";
                    H.trigger(a);
                    d(document).unbind("click.d").unbind("keydown.d");
                    if (a.isDefaultPrevented())
                        return;
                    k.hide();
                    u = false
                }
                return h
            },
            getConf: function () {
                return c
            },
            getInput: function () {
                return b
            },
            getCalendar: function () {
                return k
            },
            getValue: function (a) {
                return a ? T(l, a, c.lang) : l
            },
            isOpen: function () {
                return u
            }
        });
        d.each(["onBeforeShow", "onShow", "change", "onHide"], function (a, e) {
            d.isFunction(c[e]) && d(h).bind(e, c[e]);
            h[e] = function (g) {
                d(h).bind(e, g);
                return h
            }
        });
        b.bind("focus click", h.show).keydown(function (a) {
            var e = a.keyCode;
            if (!u && d(V).index(e) >= 0) {
                h.show(a);
                return a.preventDefault()
            }
            return a.shiftKey || a.ctrlKey || a.altKey || e == 9 ? true : a.preventDefault()
        });
        C(b.val()) && j(l, c)
    }
    d.tools = d.tools ||
    {
        version: "1.2.3"
    };
    var W = [], Q, V = [75, 76, 38, 39, 74, 72, 40, 37], B = {};
    Q = d.tools.dateinput = {
        conf: {
            format: "mm/dd/yy",
            selectors: false,
            yearRange: [-5, 5],
            lang: "en",
            offset: [0, 0],
            speed: 0,
            firstDay: 0,
            min: 0,
            max: 0,
            trigger: false,
            css: {
                prefix: "cal",
                input: "date",
                root: 0,
                head: 0,
                title: 0,
                prev: 0,
                next: 0,
                month: 0,
                year: 0,
                days: 0,
                body: 0,
                weeks: 0,
                today: 0,
                current: 0,
                week: 0,
                off: 0,
                sunday: 0,
                focus: 0,
                disabled: 0,
                trigger: 0
            }
        },
        localize: function (b, c) {
            d.each(c, function (j, m) {
                c[j] = m.split(",")
            });
            B[b] = c
        }
    };
    Q.localize("en", {
        months: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
        shortMonths: "一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月",
        days: "日,一,二,三,四,五,六",
        shortDays: "日,一,二,三,四,五,六"
    });
    var X = /d{1,4}|m{1,4}|yy(?:yy)?|"[^"]*"|'[^']*'/g, Y = d("<a/>");
    d.expr[":"].date = function (b) {
        var c = b.getAttribute("type");
        return c && c == "date" || !!d(b).data("dateinput")
    };
    d.fn.dateinput = function (b) {
        if (this.data("dateinput"))
            return this;
        b = d.extend(true, {}, Q.conf, b);
        d.each(b.css, function (j, m) {
            if (!m && j != "prefix")
                b.css[j] = (b.css.prefix || "") + (m || j)
        });
        var c;
        this.each(function () {
            var j = new Z(d(this), b);
            W.push(j);
            j = j.getInput().data("dateinput", j);
            c = c ? c.add(j) : j
        });
        return c ? c : this
    }
})(jQuery);



/*--密码强度--*/
function isSecurity(v) {
    if (v.length < 6) {
        iss.reset(v.length);
        return
    }
    var a = -1;
    var b = (v.search(/[a-zA-Z]/) != -1) ? 1 : 0;
    var c = (v.search(/[0-9]/) != -1) ? 1 : 0;
    var d = (v.search(/[^A-Za-z0-9_]/) != -1) ? 1 : 0;
    var a = b + c + d;
    switch (a) {
        case 1:
            iss.level0();
            break;
        case 2:
            iss.level1();
            break;
        case 3:
            iss.level2();
            break;
        default:
            iss.reset(v.length)
    }
}

var iss = {
    width: ["60", "80", "100", "10"],
    reset: function (a) {
        $("#BarBorder_TxtUserPassword").html("<span style='color:red'>  密码强度：弱</span>");
    },
    level0: function () {
        $("#BarBorder_TxtUserPassword").html("<span style='color:#FF3300'>  密码强度：一般</span>");
    },
    level1: function () {
        $("#BarBorder_TxtUserPassword").html("<span style='color:green'>  密码强度：强</span>");
    },
    level2: function () {
        $("#BarBorder_TxtUserPassword").html("<span style='color:green'>  密码强度：很强</span>");
    }
}
function Bardisplaynone() {
    $("#BarBorder_TxtUserPassword").hide();
}

function Bardisplayshow() {
    $("#BarBorder_TxtUserPassword").empty();
    $("#ctl00_CphContent_ValrUserPassword").hide();
    document.getElementById("BarBorder_TxtUserPassword").style.display = "inline"
}

String.prototype.endWith = function (a) {
    var b = new RegExp(a + "$");
    return b.test(this)
}
function batchconfirm(a, b) {
    var a = (arguments.length > 0) ? arguments[0] : "确定要进行此批量操作？";
    var b = (arguments.length > 1) ? arguments[1] : "请选择所要操作的记录！";
    var c = false;
    for (var i = 0; i < document.forms[0].length; i++) {
        var o = document.forms[0][i];
        if (o.type == "checkbox" && o.name.endWith("CheckBoxButton") && o.checked == true) {
            c = true;
            break
        }
    }
    if (!c) {
        alert(b);
        return false
    }
    else {
        if (!confirm(a)) {
            return false
        }
    }
}
