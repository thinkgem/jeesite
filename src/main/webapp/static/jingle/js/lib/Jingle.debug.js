/*
 * Jingle v0.4 Copyright (c) 2013 shixy, http://shixy.github.io/Jingle/
 * Released under MIT license
 * walker.shixy@gmail.com
 */
var Jingle = J = {
    version : '0.41',
    $ : window.Zepto,
    //参数设置
    settings : {
        //single  单页面工程  muti  多页面工程
        appType : 'single',
        //page默认动画效果
        transitionType : 'slide',
        //自定义动画时的默认动画时间(非page转场动画时间)
        transitionTime : 250,
        //自定义动画时的默认动画函数(非page转场动画函数)
        transitionTimingFunc : 'ease-in',
        //toast 持续时间,默认为3s
        toastDuration : 3000,
        //是否显示欢迎界面
        showWelcome : false,
        //欢迎界面卡片切换时的执行函数，可以制作酷帅吊炸天的欢迎界面
        welcomeSlideChange : null,
        //加载page模板时，是否显示遮罩
        showPageLoading : false,
        //page模板默认的相对位置，主要用于开发hybrid应用，实现page的自动装载
        basePagePath : 'html/',
        basePageSuffix : '.html',
        //page模板的远程路径{#id: href,#id: href}
        remotePage:{}
    },
    //手机或者平板
    mode : window.innerWidth < 800 ? "phone" : "tablet",
    hasTouch : 'ontouchstart' in window,
    //是否启动完成
    launchCompleted : false,
    //是否有打开的侧边菜单
    hasMenuOpen : false,
    //是否有打开的弹出框
    hasPopupOpen : false,
    isWebApp : location.protocol == 'http:',
    /**
     * 启动Jingle
     * @param opts {object}
     */
    launch : function(opts){
        $.extend(this.settings,opts);
        var hasShowWelcome = window.localStorage.getItem('hasShowWelcome');
        if(!hasShowWelcome && this.settings.showWelcome){
            this.Welcome.show();
        }
        this.Element.init();
        this.Element.initControlGroup();
        this.Router.init();
        this.Menu.init();
    }
};

/**
 * 初始化页面组件元素
 */
J.Element = (function($){
    var SELECTOR  = {
        'icon' : '[data-icon]',
        'scroll' : '[data-scroll="true"]',
        'toggle' : '.toggle',
        'range' : '[data-rangeinput]',
        'progress' : '[data-progress]',
        'count' : '[data-count]',
        'checkbox' : '[data-checkbox]'
    }
    /**
     * 初始化容器内组件
     * @param {String} 父元素的css选择器
     * @param {Object} 父元素或者父元素的zepto实例
     */
    var init = function(selector){
        if(!selector){
            //iscroll 必须在元素可见的情况下才能初始化
            $(document).on('articleshow','article',function(){
                J.Element.scroll(this);
            });
        };
        var $el = $(selector || 'body');
        if($el.length == 0)return;

        $.map(_getMatchElements($el,SELECTOR.icon),_init_icon);
        $.map(_getMatchElements($el,SELECTOR.toggle),_init_toggle);
        $.map(_getMatchElements($el,SELECTOR.range),_init_range);
        $.map(_getMatchElements($el,SELECTOR.progress),_init_progress);
        $.map(_getMatchElements($el,SELECTOR.count),_init_badge);
        $.map(_getMatchElements($el,SELECTOR.checkbox),_init_checkbox);
        $el = null;
    }

    /**
     * 初始化按钮组(绑定事件)
     */
    var initControlGroup = function(){
        $(document).on('tap','ul.control-group li',function(){
            var $this = $(this);
            if($this.hasClass('active'))return;
            $this.addClass('active').siblings('.active').removeClass('active').parent().trigger('change',[$this]);
        });
    }
    /**
     * 自身与子集相结合
     */
    var _getMatchElements = function($el,selector){
        return $el.find(selector).add($el.filter(selector));
    }
    /**
     * 初始化iscroll组件或容器内iscroll组件
     */
    var initScroll = function(selector){
        $.map(_getMatchElements($(selector),SELECTOR.scroll),function(el){J.Scroll(el);});
    }
    /**
     * 构造icon组件
     */
    var _init_icon = function(el){
        var $el = $(el),$icon=$el.children('i.icon'),icon = $el.data('icon');
        if($icon.length > 0){//已经初始化，就更新icon
            $icon.attr('class','icon '+icon);
        }else{
            $el.prepend('<i class="icon '+icon+'"></i>');
        }
    }
    /**
     * 构造toggle切换组件
     */
    var _init_toggle = function(el){
        var $el = $(el);
        if($el.find('div.toggle-handle').length>0){//已经初始化
            return;
        }
        var name = $el.attr('name');
        //添加隐藏域，方便获取值
        if(name){
            $el.append('<input style="display: none;" name="'+name+'" value="'+$el.hasClass('active')+'"/>');
        }
        $el.append('<div class="toggle-handle"></div>');
        $el.tap(function(){
            var $t = $(this),v = !$t.hasClass('active');
            $t.toggleClass('active').trigger('toggle',[v]);//定义toggle事件
            $t.find('input').val(v);
        })
    }
    /**
     * 构造range滑块组件
     */
    var _init_range = function(el){
        var $el = $(el),$input;
        var $range = $('input[type="range"]',el);
        var align = $el.data('rangeinput');
        var input = $('<input type="number" name="test" value="'+$range.val()+'"/>');
        if(align == 'left'){
            $input = input.prependTo($el);
        }else{
            $input = input.appendTo($el);
        }
        var max = parseInt($range.attr('max'),10);
        var min = parseInt($range.attr('min'),10);
        $range.change(function(){
            $input.val($range.val());
        });
        $input.on('input',function(){
            var value = parseInt($input.val(),10);
            value = value>max?max:(value<min?min:value);
            $range.val(value);
            $input.val(value);
        })
    }
    /**
     * 构造progress组件
     */
    var _init_progress = function(el){
        var $el = $(el),$bar;
        var progress = parseFloat($el.data('progress'))+'%';
        var title = $el.data('title') || '';
        $bar = $el.find('div.bar');
        if($bar.length == 0){
            $bar = $('<div class="bar"></div>').appendTo($el);
        }
        $bar.width(progress).text(title+progress);
        if(progress == '100%'){
            $bar.css('border-radius','10px');
        }
    }
    /**
     * 构造badge组件
     */
    var _init_badge = function(el){
        var $el = $(el),$count = $el.find('span.count'),count = parseInt($el.data('count')),
            orient = $el.data('orient'), className = (orient == 'left')?'left':'';
        if($count.length>0){
            $count.text(count).show();//更新数字
        }else{
            $count = $('<span class="count '+className+'">'+count+'</span>').appendTo($el);
        }
        if(count == 0){
            $count.hide();
        }
    }

    var _init_checkbox = function(el){
        var $el = $(el);
        var value = $el.data('checkbox');
        if($el.find('i.icon').length>0){
            return;
        }
        $el.prepend('<i class="icon checkbox-'+value+'"></i>');
        $el.on('tap',function(){
            var status = ($el.data('checkbox') == 'checked') ? 'unchecked':'checked';
            $el.data('checkbox',status).find('i.icon').attr('class','icon checkbox-'+status);
            //自定义change事件
            $el.trigger('change');
        });

    }

    return {
        init : init,
        initControlGroup : initControlGroup,
        icon : _init_icon,
        toggle : _init_toggle,
        progress : _init_progress,
        range : _init_range,
        badge : _init_badge,
        scroll : initScroll
    }
})(J.$);
/**
 * 侧边菜单
 */
J.Menu = (function($){
    var $asideContainer,$sectionContainer,$sectionMask;
    var init = function(){
        $asideContainer = $('#aside_container');
        $sectionContainer = $('#section_container');
        $sectionMask = $('<div id="section_container_mask"></div>').appendTo('#section_container');
        //添加各种关闭事件
        $sectionMask.on('tap',hideMenu);
        $asideContainer.on('swipeRight','aside',function(){
            if($(this).data('position') == 'right'){
                hideMenu();
            }
        });
        $asideContainer.on('swipeLeft','aside',function(){
            if($(this).data('position') != 'right'){
                hideMenu();
            }
        });
        $asideContainer.on('tap','.aside-close',hideMenu);
    }
    /**
     * 打开侧边菜单
     * @param selector css选择器或element实例
     */
    var showMenu = function(selector){
        var $aside = $(selector).addClass('active'),
            transition = $aside.data('transition'),// push overlay  reveal
            position = $aside.data('position') || 'left',
            showClose = $aside.data('show-close'),
            width = $aside.width(),
            translateX = position == 'left'?width+'px':'-'+width+'px';
        if(showClose && $aside.find('div.aside-close').length == 0){
            $aside.append('<div class="aside-close icon close"></div>');
        }

        //aside中可能需要scroll组件
        J.Element.scroll($aside);

        if(transition == 'overlay'){
            J.anim($aside,{translateX : '0%'});
        }else if(transition == 'reveal'){
            J.anim($sectionContainer,{translateX : translateX});
        }else{//默认为push
            J.anim($aside,{translateX : '0%'});
            J.anim($sectionContainer,{translateX : translateX});
        }
        $('#section_container_mask').show();
        J.hasMenuOpen = true;
    }
    /**
     * 关闭侧边菜单
     * @param duration {int} 动画持续时间
     * @param callback 动画完毕回调函数
     */
    var hideMenu = function(duration,callback){
        var $aside = $('#aside_container aside.active'),
            transition = $aside.data('transition'),// push overlay  reveal
            position = $aside.data('position') || 'left',
            translateX = position == 'left'?'-100%':'100%';

        var _finishTransition = function(){
            $aside.removeClass('active');
            J.hasMenuOpen = false;
            callback && callback.call(this);
        };

        if(transition == 'overlay'){
            J.anim($aside,{translateX : translateX},duration,_finishTransition);
        }else if(transition == 'reveal'){
            J.anim($sectionContainer,{translateX : '0'},duration,_finishTransition);
        }else{//默认为push
            J.anim($aside,{translateX : translateX},duration);
            J.anim($sectionContainer,{translateX : '0'},duration,_finishTransition);
        }

        $('#section_container_mask').hide();
    }
    return {
        init : init,
        show : showMenu,
        hide : hideMenu
    }
})(J.$);
/**
 * section 页面远程加载
 */
J.Page = (function($){
    var _formatHash = function(hash){
        return hash.indexOf('#') == 0 ? hash.substr(1) : hash;
    }
    /**
     * 加载section模板
     * @param {string} hash信息
     * @param {string} url参数
     */
    var loadSectionTpl = function(hash,callback){
        var param = {},query,replaceSection = false;
        if($.type(hash) == 'object'){
            param = hash.param;
            query = hash.query;
            hash = hash.tag;
        }
        var q = $(hash).data('query');
        //已经存在则直接跳转到对应的页面
        if($(hash).length == 1){
            if(q == query){
                callback();
                return;
            }else{
                replaceSection = true;
            }
        }
        var id = _formatHash(hash);
        //当前dom中不存在，需要从服务端加载
        var url = J.settings.remotePage[hash];
        //检查remotePage中是否有配置,没有则自动从basePagePath中装载模板
        url || (url = J.settings.basePagePath+id+J.settings.basePageSuffix);
        J.settings.showPageLoading && J.showMask();
        loadContent(url,param,function(html){
            J.settings.showPageLoading && J.hideMask();
            //添加到dom树中
            $(hash).remove();
            var $h = $(html);
            $('#section_container').append($h);
            if(replaceSection){
                $h.addClass('active');
            }
            //触发pageload事件
            $h.trigger('pageload').data('query',query);
            //构造组件
            J.Element.init(hash);
            callback();
            $h = null;
        });
    }
    var loadSectionRemote = function(url,section){
        var param = J.Util.parseHash(window.location.hash).param;
        loadContent(url,param,function(html){
            $(section).html(html);
            J.Element.init(section);
        });
    }
    /**
     * 加载文档片段
     * @param url
     */
    var loadContent = function(url,param,callback){
        return $.ajax({
                url : url,
                timeout : 20000,
                data : param,
                success : function(html){
                    callback && callback(html);
                }
            });
    }
    return {
        load : loadSectionTpl,
        loadSection : loadSectionRemote,
        loadContent : loadContent
    }
})(J.$);
/**
 * 路由控制器
 */
J.Router = (function($){
        var _history = [];
    /**
     * 初始化events、state
     */
    var init = function(){
        $(window).on('popstate', _popstateHandler);
        //阻止含data-target或者href以'#'开头的的a元素的默认行为
        $(document).on('click','a',function(e){
            var target = $(this).data('target'),
                href = $(this).attr('href');
            if(!href ||  href.match(/^#/) || target){
                e.preventDefault();
                return false;
            }
        });
        $(document).on('tap','a[data-target]',_targetHandler);
        _initIndex();
    }

    //处理app页面初始化
    var _initIndex = function(){
        var targetHash = location.hash;
        //取页面中第一个section作为app的起始页
        var $section = $('#section_container section').first();
        var indexHash = '#'+$section.attr('id');
        _add2History(indexHash,true);
        if(targetHash != '' && targetHash != indexHash){
            _showSection(targetHash);//跳转到指定的页面
        }else{
            $section.trigger('pageinit').trigger('pageshow').data('init',true).find('article.active').trigger('articleshow');
        }
    }

    /**
     * 处理浏览器的后退事件
     * 前进事件不做处理
     * @private
     */
    var _popstateHandler = function(e){
        if(e.state && e.state.hash){
            var hash = e.state.hash;
            if(_history[1] && hash === _history[1].hash){//存在历史记录，证明是后退事件
                J.hasMenuOpen && J.Menu.hide();//关闭当前页面的菜单
                J.hasPopupOpen && J.Popup.close();//关闭当前页面的弹出窗口
                back();
            }else{//其他认为是非法后退或者前进
                return;
            }
        }else{
            return;
        }

    }
    var _targetHandler = function(){
        var _this = $(this),
            target = _this.attr('data-target'),
            href = _this.attr('href');

        switch(target){
            case 'section' :
                if(J.settings.appType == 'single'){
                    _showSection(href);
                }
                break;
            case 'article' :
                _showArticle(href,_this);
                break;
            case 'menu' :
                _toggleMenu(href);
                break;
            case 'back' :
                window.history.go(-1);
                break;
        }
    }

    /**
     * 跳转到新页面
     * @param hash 新page的'#id'
     */
    var _showSection  = function(hash){
        if(J.hasMenuOpen){//关闭菜单后再转场
            J.Menu.hide(200,function(){
                _showSection(hash);
            });
            return;
        }
        //读取hash信息
        var hashObj = J.Util.parseHash(hash);
        var current = _history[0];
        //同一个页面,则不重新加载
        if(current.hash === hashObj.hash){
            return;
        }
        //加载模板
        J.Page.load(hashObj,function(){
            var sameSection = (current.tag == hashObj.tag);
           if(sameSection){//相同页面，触发相关事件
               $(current.tag).trigger('pageshow').find('article.active').trigger('articlehide');
           }else{//不同卡片页跳转动画
               _changePage(current.tag,hashObj.tag);
           }
            _add2History(hash,sameSection);
        });
    }
    /**
     * 后退
     */
    var back = function(){
        if(J.settings.appType == 'single'){
            _changePage(_history.shift().tag,_history[0].tag,true)
        }
    }
    var _changePage = function(current,target,isBack){
        J.Transition.run(current,target,isBack);
    }
    /**
     * 缓存访问记录
     */
    var _add2History = function(hash,noState){
       var hashObj = J.Util.parseHash(hash);
        if(noState){//不添加浏览器历史记录
            _history.shift(hashObj);
            window.history.replaceState(hashObj,'',hash);
        }else{
            window.history.pushState(hashObj,'',hash);
        }
        _history.unshift(hashObj);
    }

    /**
     * 激活href对应的article
     * @param href #id
     * @param el 当前锚点
     */
    var _showArticle = function(href,el){
        var article = $(href);
        if(article.hasClass('active'))return;
        el.addClass('active').siblings('.active').removeClass('active');
        var activeArticle = article.addClass('active').siblings('.active').removeClass('active');
        article.trigger('articleshow');
        activeArticle.trigger('articlehide');
    }

    var _toggleMenu = function(hash){
        J.hasMenuOpen?J.Menu.hide():J.Menu.show(hash);
    }

    return {
        init : init,
        goTo : _showSection,
        showArticle : _showArticle,
        back : back
    }
})(J.$);
/**
 * 对zeptojs的ajax进行封装，实现离线访问
 * 推荐纯数据的ajax请求调用本方法，其他的依旧使用zeptojs自己的ajax
 * @Deprecated 用J.Cache代替
 */
J.Service = (function($){
    var UNPOST_KEY = 'JINGLE_POST_DATA',
        GET_KEY_PREFIX = 'JINGLE_GET_';
    var ajax = function(options){
        if(options.type == 'post'){
            _doPost(options);
        }else{
            _doGet(options);
        }
    }

    var _doPost = function(options){
        if(J.offline){//离线模式，将数据存到本地，连线时进行提交
            _setUnPostData(options.url,options.data);
            options.success('数据已存至本地');
        }else{//在线模式，直接提交
            $.ajax(options);
        }
    }
    var _doGet = function(options){
        var key = options.url +JSON.stringify(options.data);
        if(J.offline){//离线模式，直接从本地读取
            var result = _getCache(key);
            if(result){
                options.success(result.data,key,result.cacheTime);
            }else{//未缓存该数据
                options.success(result);
            }
        }else{//在线模式，将数据保存到本地
            var callback = options.success;
            options.success = function(result){
                _saveData2local(key,result);
                callback(result,key);
            }
            $.ajax(options);
        }
    }

    /**
     * 获取本地已缓存的数据
     * @private
     */
    var _getCache = function(key){
         return JSON.parse(window.localStorage.getItem(GET_KEY_PREFIX+key));
    }
    /**
     * 缓存数据到本地
     * @private
     */
    var _saveData2local = function(key,result){
        var data = {
            data : result,
            cacheTime : new Date()
        }
        window.localStorage.setItem(GET_KEY_PREFIX+key,JSON.stringify(data));
    }

    /**
     * 将post的数据保存至本地
     * @param url
     * @param result
     * @private
     */
    var _setUnPostData = function(url,result){
        var data = getUnPostData();
        data = data || {};
        data[url] = {
            data : result,
            createdTime : new Date()
        }
        window.localStorage.setItem(UNPOST_KEY,JSON.stringify(data));
    }
    /**
     *  获取尚未同步的post数据
     * @param url  没有就返回所有未同步的数据
     */
    var getUnPostData = function(url){
        var data = JSON.parse(window.localStorage.getItem(UNPOST_KEY));
        return (data && url ) ? data[url] : data;
    }
    /**
     * 移除未同步的数据
     * @param url 没有就移除所有未同步的数据
     */
    var removeUnPostData = function(url){
        if(url){
            var data = getUnPostData();
            delete data[url];
            window.localStorage.setItem(UNPOST_KEY,JSON.stringify(data));
        }else{
            window.localStorage.removeItem(UNPOST_KEY);
        }
    }

    /**
     * 同步本地缓存的post数据
     * @param url
     */
    var syncPostData = function(url,success,error){
        var unPostData = getUnPostData(url).data;
        $.ajax({
            url : url,
            contentType:'application/json',
            data : unPostData,
            type : 'post',
            success : function(){
                success(url);
            },
            error : function(){
                error(url);
            }
        })
    }
    /**
     * 同步所有的数据
     * @param callback
     */
    var syncAllPostData = function(success,error){
        var unPostData = getUnPostData();
        for(var url in unPostData){
            syncPostData(url,success,error);
        }
        removeUnPostData();
    }

    //copy from zepto
    function parseArguments(url, data, success, dataType) {
        var hasData = !$.isFunction(data)
        return {
            url:      url,
            data:     hasData  ? data : undefined,
            success:  !hasData ? data : $.isFunction(success) ? success : undefined,
            dataType: hasData  ? dataType || success : success
        }
    }

    var get = function(url, data, success, dataType){
        return ajax(parseArguments.apply(null, arguments))
    }

    var post = function(url, data, success, dataType){
        var options = parseArguments.apply(null, arguments)
        options.type = 'POST'
        return ajax(options)
    }

    var getJSON = function(url, data, success){
        var options = parseArguments.apply(null, arguments);
        options.dataType = 'json'
        return ajax(options)
    }
    var clear = function(){
        var storage = window.localStorage;
        var keys = [];
        for(var i = 0; i< storage.length; i++){
            var key = storage.key(i);
            key.indexOf(GET_KEY_PREFIX) == 0 && keys.push(key);
        }
        for(var i = 0; i < keys.length; i++){
            storage.removeItem(keys[i]);
        }
        storage.removeItem(UNPOST_KEY);
    }
    return {
        ajax : ajax,
        get : get,
        post : post,
        getJSON : getJSON,
        getUnPostData : getUnPostData,
        removeUnPostData : removeUnPostData,
        syncPostData : syncPostData,
        syncAllPostData : syncAllPostData,
        getCacheData : _getCache,
        saveCacheData : _saveData2local,
        clear : clear
    }
})(J.$);
/**
 * 提供一些简单的模板，及artTemplate的渲染
 */
J.Template = (function($){
    /**
     * 背景模板
     * @param el  selector
     * @param title  显示文本
     * @param icon   图标
     */
    var background = function(el,title,icon){
        var markup = '<div class="back-mask"><div class="icon '+icon+'"></div><div>'+title+'</div></div>';
        $(el).html(markup);
    }

    /**
     * 无记录背景模板
     * @param el
     */
    var no_result = function(el){
        background(el,'没有找到相关数据','drawer');
    }
    /**
     * 加载等待背景模板
     * @param el
     */
    var loading = function(el){
        background(el,'加载中...','cloud-download');
    }

    /**
     * 借助artTemplate模板来渲染页面
     * @param containerSelector 目标容器
     * @param templateId  artTemplate模板ID
     * @param data 模板数据
     * @param type replace|add 渲染好的文档片段是替换还是添加到目标容器中
     */
    var render = function(containerSelector,templateId,data,type){
        var el =  $(containerSelector),
            type = type || 'replace';//replace  add
        if($.type(data) == 'array' && data.length == 0 ){
            no_result(el);
        }else{
            var html = template(templateId,data);
            if(type == 'replace'){
                el.html(html);
            }else{
                el.append(html);
            }
            J.Element.init(html);
        }
    }
    return {
        render : render,
        background : background,
        loading : loading,
        no_result : no_result
    }
})(J.$);

/**
 *  消息组件
 */
J.Toast = (function($){
    var toast_type = 'toast',_toast,timer,
        //定义模板
        TEMPLATE = {
            toast : '<a href="#">{value}</a>',
            success : '<a href="#"><i class="icon checkmark-circle"></i>{value}</a>',
            error : '<a href="#"><i class="icon cancel-circle"></i>{value}</a></div>',
            info : '<a href="#"><i class="icon info-2"></i>{value}</a>'
        }

    var _init = function(){
        //全局只有一个实例
        $('body').append('<div id="jingle_toast"></div>');
        _toast = $('#jingle_toast');
        _subscribeCloseTag();
    }

    /**
     * 关闭消息提示
     */
    var hide = function(){
        J.anim(_toast,'scaleOut',function(){
            _toast.hide();
           _toast.empty();
        });
    }
    /**
     * 显示消息提示
     * @param type 类型  toast|success|error|info  空格 + class name 可以实现自定义样式
     * @param text 文字内容
     * @param duration 持续时间 为0则不自动关闭,默认为3000ms
     */
    var show = function(type,text,duration){
        if(timer) clearTimeout(timer);
        var classname = type.split(/\s/);
        toast_type = classname[0];
        _toast.attr('class',type).html(TEMPLATE[toast_type].replace('{value}',text)).show();
        J.anim(_toast,'scaleIn');
        if(duration !== 0){//为0 不自动关闭
            timer = setTimeout(hide,duration || J.settings.toastDuration);
        }
    }
    var _subscribeCloseTag = function(){
        _toast.on('tap','[data-target="close"]',function(){
            hide();
        })
    }
    _init();
    return {
        show : show,
        hide : hide
    }
})(J.$);
/**
 * page转场动画
 * 可自定义css动画
 */
J.Transition = (function($){
    var isBack,$current,$target,transitionName,
        animationClass = {
        //[[currentOut,targetIn],[currentOut,targetIn]]
        slide : [['slideLeftOut','slideLeftIn'],['slideRightOut','slideRightIn']],
        cover : [['','slideLeftIn'],['slideRightOut','']],
        slideUp : [['','slideUpIn'],['slideDownOut','']],
        slideDown : [['','slideDownIn'],['slideUpOut','']],
        popup : [['','scaleIn'],['scaleOut','']]
        };

    var _doTransition = function(){
        //触发 beforepagehide 事件
        $current.trigger('beforepagehide',[isBack]);
        //触发 beforepageshow 事件
        $target.trigger('beforepageshow',[isBack]);
        var c_class = transitionName[0]||'empty' ,t_class = transitionName[1]||'empty';
        $current.bind('webkitAnimationEnd.jingle', _finishTransition).addClass('anim '+ c_class);
        $target.addClass('anim animating '+ t_class);
    }
    var _finishTransition = function() {
        $current.off('webkitAnimationEnd.jingle');
        $target.off('webkitAnimationEnd.jingle');
        //reset class
        $current.attr('class','');
        $target.attr('class','active');
        //add custom events
        !$target.data('init') && $target.trigger('pageinit').data('init',true);
        !$current.data('init') && $current.trigger('pageinit').data('init',true);
        //触发pagehide事件
        $current.trigger('pagehide',[isBack]);
        //触发pageshow事件
        $target.trigger('pageshow',[isBack]);

        $current.find('article.active').trigger('articlehide');
        $target.find('article.active').trigger('articleshow');
        $current = $target = null;//释放
    }

    /**
     * 执行转场动画，动画类型取决于目标page上动画配置(返回时取决于当前page)
     * @param current 当前page
     * @param target  目标page
     * @param back  是否为后退
     */
    var run = function(current,target,back){
        //关闭键盘
        $(':focus').trigger('blur');
        isBack = back;
        $current = $(current);
        $target = $(target);
        var type = isBack?$current.attr('data-transition'):$target.attr('data-transition');
        type = type|| J.settings.transitionType;
        //后退时取相反的动画效果组
        transitionName  = isBack ? animationClass[type][1] : animationClass[type][0];
        _doTransition();
    }

    /**
     * 添加自定义转场动画效果
     * @param name  动画名称
     * @param currentOut 正常情况下当前页面退去的动画class
     * @param targetIn   正常情况下目标页面进入的动画class
     * @param backCurrentOut 后退情况下当前页面退去的动画class
     * @param backCurrentIn 后退情况下目标页面进入的动画class
     */
    var addAnimation = function(name,currentOut,targetIn,backCurrentOut,backCurrentIn){
        if(animationClass[name]){
            console.error('该转场动画已经存在，请检查你自定义的动画名称(名称不能重复)');
            return;
        }
        animationClass[name] = [[currentOut,targetIn],[backCurrentOut,backCurrentIn]];
    }
    return {
        run : run,
        add : addAnimation
    }

})(J.$);
/**
 * 常用工具类
 */
J.Util = (function($){
    var parseHash = function(hash){
        var tag,query,param={};
        var arr = hash.split('?');
        tag = arr[0];
        if(arr.length>1){
            var seg,s;
            query = arr[1];
            seg = query.split('&');
            for(var i=0;i<seg.length;i++){
                if(!seg[i])continue;
                s = seg[i].split('=');
                param[s[0]] = s[1];
            }
        }
        return {
            hash : hash,
            tag : tag,
            query : query,
            param : param
        }
    }

    /**
     * 格式化date
     * @param date
     * @param format
     */
    var formatDate = function(date,format){
        var o =
        {
            "M+" : date.getMonth()+1, //month
            "d+" : date.getDate(),    //day
            "h+" : date.getHours(),   //hour
            "m+" : date.getMinutes(), //minute
            "s+" : date.getSeconds(), //second
            "q+" : Math.floor((date.getMonth()+3)/3),  //quarter
            "S" : date.getMilliseconds() //millisecond
        }
        if(/(y+)/.test(format))
            format=format.replace(RegExp.$1,(date.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(format))
                format = format.replace(RegExp.$1,RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
    }

    return {
        parseHash : parseHash,
        formatDate : formatDate
    }

})(J.$);
/**
 * 欢迎界面，可以制作酷炫吊炸天的欢迎界面哦
 * @module Welcome
 */
J.Welcome = (function($){
    /**
     * 显示欢迎界面
     */
    var showWelcome = function(){
        $.ajax({
            url : J.settings.basePagePath+'welcome.html',
            timeout : 5000,
            async : false,
            success : function(html){
                //添加到dom树中
                $('body').append(html);
                new J.Slider({
                    selector : '#jingle_welcome',
                    onAfterSlide  : J.settings.welcomeSlideChange
                });
            }
        })
    }
    /**
     * 关闭欢迎界面
     */
    var hideWelcome = function(){
        J.anim('#jingle_welcome','slideLeftOut',function(){
            $(this).remove();
            window.localStorage.setItem('hasShowWelcome',true);
        })
    }

    return {
        show : showWelcome,
        hide : hideWelcome
    }
})(J.$);
(function($){
    /*
     * alias func
     * 简化一些常用方法的写法
     ** /
    /**
     * 完善zepto的动画函数,让参数变为可选
     */
    J.anim  =  function(el,animName,duration,ease,callback){
        var d, e,c;
        var len = arguments.length;
        for(var i = 2;i<len;i++){
            var a = arguments[i];
            var t = $.type(a);
            t == 'number'?(d=a):(t=='string'?(e=a):(t=='function')?(c=a):null);
        }
        $(el).animate(animName,d|| J.settings.transitionTime,e||J.settings.transitionTimingFunc,c);
    }
    /**
     * 显示loading框
     * @param text
     */
    J.showMask = function(text){
        J.Popup.loading(text);
    }
    /**
     * 关闭loading框
     */
    J.hideMask = function(){
        J.Popup.close(true);
    }
    /**
     *  显示消息
     * @param text
     * @param type toast|success|error|info
     * @param duration 持续时间，为0则不自动关闭
     */
    J.showToast = function(text,type,duration){
        type = type || 'toast';
        J.Toast.show(type,text,duration);
    }
    /**
     * 关闭消息提示
     */
    J.hideToast = function(){
        J.Toast.hide();
    }
    J.alert = function(title,content){
        J.Popup.alert(title,content);
    }
    J.confirm = function(title,content,okCall,cancelCall){
        J.Popup.confirm(title,content,okCall,cancelCall);
    }
    /**
     * 弹出窗口
     * @param options
     */
    J.popup = function(options){
        J.Popup.show(options);
    }
    /**
     * 关闭窗口
     */
    J.closePopup = function(){
        J.Popup.close();
    }
    /**
     * 带箭头的弹出框
     * @param html [可选]
     * @param pos [可选]  位置
     * @param arrowDirection [可选] 箭头方向
     * @param onShow [可选] 显示之前执行
     */
    J.popover = function(html,pos,arrowDirection,onShow){
        J.Popup.popover(html,pos,arrowDirection,onShow);
    }
    /**
     * 自动渲染模板并填充到页面
     * @param containerSelector 欲填充的容器
     * @param templateId 模板ID
     * @param data 数据源
     * @param type [可选] add|replace
     */
    J.tmpl = function(containerSelector,templateId,data,type){
        J.Template.render(containerSelector,templateId,data,type);
    }
})(J.$);
/**
 * 弹出框组件
 */
J.Popup = (function($){
    var _popup,_mask,transition,clickMask2close,
        POSITION = {
            'top':{
                top:0,
                left:0,
                right:0
            },
            'top-second':{
                top:'44px',
                left:0,
                right:0
            },
            'center':{
                top:'50%',
                left:'5%',
                right:'5%',
                'border-radius' : '3px'
            },
            'bottom' : {
                bottom:0,
                left:0,
                right:0
            },
            'bottom-second':{
                bottom : '51px',
                left:0,
                right:0
            }
        },
        ANIM = {
            top : ['slideDownIn','slideUpOut'],
            bottom : ['slideUpIn','slideDownOut'],
            defaultAnim : ['bounceIn','bounceOut']
        },
        TEMPLATE = {
            alert : '<div class="popup-title">{title}</div><div class="popup-content">{content}</div><div id="popup_btn_container"><a data-target="closePopup" data-icon="checkmark">{ok}</a></div>',
            confirm : '<div class="popup-title">{title}</div><div class="popup-content">{content}</div><div id="popup_btn_container"><a class="cancel" data-icon="close">{cancel}</a><a data-icon="checkmark">{ok}</a></div>',
            loading : '<i class="icon spinner"></i><p>{title}</p>'
        };

    /**
     * 全局只有一个popup实例
     * @private
     */
    var _init = function(){
        $('body').append('<div id="jingle_popup"></div><div id="jingle_popup_mask"></div>');
        _mask = $('#jingle_popup_mask');
        _popup = $('#jingle_popup');
        _subscribeEvents();
    }

    var show = function(options){
        var settings = {
            height : undefined,//高度
            width : undefined,//宽度
            opacity : 0.3,//透明度
            url : null,//远程加载url
            tplId : null,//加载模板ID
            tplData : null,//模板数据，配合tplId使用
            html : '',//popup内容
            pos : 'center',//位置 {@String top|top-second|center|bottom|bottom-second}   {@object  css样式}
            clickMask2Close : true,// 是否点击外层遮罩关闭popup
            showCloseBtn : true,// 是否显示关闭按钮
            arrowDirection : undefined,//popover的箭头指向
            animation : true,//是否显示动画
            timingFunc : 'linear',
            duration : 200,//动画执行时间
            onShow : undefined //@event 在popup内容加载完毕，动画开始前触发
        }
        $.extend(settings,options);
        clickMask2close = settings.clickMask2Close;
        _mask.css('opacity',settings.opacity);
        //rest position and class
        _popup.attr({'style':'','class':''});
        settings.width && _popup.width(settings.width);
        settings.height && _popup.height(settings.height);
        var pos_type = $.type(settings.pos);
        if(pos_type == 'object'){// style
            _popup.css(settings.pos);
            transition = ANIM['defaultAnim'];
        }else if(pos_type == 'string'){
            if(POSITION[settings.pos]){ //已经默认的样式
                _popup.css(POSITION[settings.pos])
                var trans_key = settings.pos.indexOf('top')>-1?'top':(settings.pos.indexOf('bottom')>-1?'bottom':'defaultAnim');
                transition = ANIM[trans_key];
            }else{// pos 为 class
                _popup.addClass(settings.pos);
                transition = ANIM['defaultAnim'];
            }
        }else{
            console.error('错误的参数！');
            return;
        }
        _mask.show();
        var html;
        if(settings.html){
            html = settings.html;
        }else if(settings.url){//远程加载
            html = J.Page.loadContent(settings.url);
        }else if(settings.tplId){//加载模板
            html = template(settings.tplId,settings.tplData)
        }

        //是否显示关闭按钮
        if(settings.showCloseBtn){
            html += '<div id="tag_close_popup" data-target="closePopup" class="icon cancel-circle"></div>';
        }
        //popover 箭头方向
        if(settings.arrowDirection){
            _popup.addClass('arrow '+settings.arrowDirection);
            _popup.css('padding','8px');
            if(settings.arrowDirection=='top'||settings.arrowDirection=='bottom'){
                transition = ANIM[settings.arrowDirection];
            }
        }

        _popup.html(html).show();
        J.Element.init(_popup);
        //执行onShow事件，可以动态添加内容
        settings.onShow && settings.onShow.call(_popup);

        //显示获取容器高度，调整至垂直居中
        if(settings.pos == 'center'){
            var height = _popup.height();
            _popup.css('margin-top','-'+height/2+'px')
        }
        if(settings.animation){
            J.anim(_popup,transition[0],settings.duration,settings.timingFunc);
        }
        J.hasPopupOpen = true;
    }

    /**
     * 关闭弹出框
     * @param noTransition 立即关闭，无动画
     */
    var hide = function(noTransition){
        _mask.hide();
        if(transition && !noTransition){
            J.anim(_popup,transition[1],200,function(){
                _popup.hide().empty();
                J.hasPopupOpen = false;
            });
        }else{
            _popup.hide().empty();
            J.hasPopupOpen = false;
        }

    }
    var _subscribeEvents = function(){
        _mask.on('tap',function(){
            clickMask2close &&  hide();
        });
        _popup.on('tap','[data-target="closePopup"]',function(){hide();});
    }

    /**
     * alert组件
     * @param title 标题
     * @param content 内容
     */
    var alert = function(title,content,btnName){
        var markup = TEMPLATE.alert.replace('{title}',title).replace('{content}',content).replace('{ok}',btnName || '确定');
        show({
            html : markup,
            pos : 'center',
            clickMask2Close : false,
            showCloseBtn : false
        });
    }

    /**
     * confirm 组件
     * @param title 标题
     * @param content 内容
     * @param okCall 确定按钮handler
     * @param cancelCall 取消按钮handler
     */
    var confirm = function(title,content,okCall,cancelCall){
        var markup = TEMPLATE.confirm.replace('{title}',title).replace('{content}',content).replace('{cancel}','取消').replace('{ok}','确定');
        show({
            html : markup,
            pos : 'center',
            clickMask2Close : false,
            showCloseBtn : false
        });
        $('#popup_btn_container [data-icon="checkmark"]').tap(function(){
            hide();
            okCall.call(this);
        });
        $('#popup_btn_container [data-icon="close"]').tap(function(){
            hide();
            cancelCall.call(this);
        });
    }

    /**
     * 带箭头的弹出框
     * @param html 弹出框内容
     * @param pos 位置
     * @param arrow_direction 箭头方向
     * @param onShow onShow事件
     */
    var popover = function(html,pos,arrow_direction,onShow){
        show({
            html : html,
            pos : pos,
            showCloseBtn : false,
            arrowDirection : arrow_direction,
            onShow : onShow
        });
    }

    /**
     * loading组件
     * @param text 文本，默认为“加载中...”
     */
    var loading = function(text){
        var markup = TEMPLATE.loading.replace('{title}',text||'加载中...');
        show({
            html : markup,
            pos : 'loading',
            opacity :.1,
            animation : true,
            clickMask2Close : false
        });
    }

    /**
     * actionsheet组件
     * @param buttons 按钮集合
     * [{color:'red',text:'btn',handler:function(){}},{color:'red',text:'btn',handler:function(){}}]
     */
    var actionsheet = function(buttons){
        var markup = '<div class="actionsheet">';
        $.each(buttons,function(i,n){
            markup += '<button style="background-color: '+ n.color +' !important;">'+ n.text +'</button>';
        });
        markup += '<button class="alizarin">取消</button>';
        markup += '</div>';
        show({
            html : markup,
            pos : 'bottom',
            showCloseBtn : false,
            onShow : function(){
                $(this).find('button').each(function(i,button){
                    $(button).on('tap',function(){
                        if(buttons[i] && buttons[i].handler){
                            buttons[i].handler.call(button);
                        }
                        hide();
                    });
                });
            }
        });
    }

    _init();

    return {
        show : show,
        close : hide,
        alert : alert,
        confirm : confirm,
        popover : popover,
        loading : loading,
        actionsheet : actionsheet
    }
})(J.$);
/**
 * 高亮组件
 * 在zepto的tap事件里注入了一个延时器，来实现点击态
 */
J.Selected = (function($){
    var DELAY = 100,SELECTOR='[data-selected]';
    var _trigger = $.fn.trigger;
    $.fn.trigger = function (event) {
        var $this = $(this), args = arguments, classname;
        if (event === 'tap' || event.type === 'tap') {
            var match = $this.closest(SELECTOR).get(0);
            if(match){
                match = $(match);
                classname = match.data('selected');
                match.addClass(classname);
                setTimeout(function () {
                    match.removeClass(classname);
                    _trigger.apply($this, args);
                    $this = match = null;
                }, DELAY);
                return this;
            }
        }
        _trigger.apply($this, args);
        return this;
    }
})(J.$);
/**
 * 数据缓存
 * todo  对数据进行加密
 */
J.Cache = (function($){
    var UNPOST_KEY = '_J_P_',
        GET_KEY_PREFIX = '_J_';

    /**
     * 缓存从服务端获取的数据
     * @param key
     * @param value
     */
    var save = function(key,value){
        var data = {
            data : value,
            cacheTime : new Date()
        }
        window.localStorage.setItem(GET_KEY_PREFIX+key,JSON.stringify(data));
    }
    /**
     * 获取本地已缓存的数据
     */
    var get = function(key){
        return JSON.parse(window.localStorage.getItem(GET_KEY_PREFIX+key));
    }

    /**
     * 缓存本地待提交到服务端的数据(离线操作)
     * @param url
     * @param result
     */
    var savePost = function(url,result){
        var data = getPost();
        data = data || {};
        data[url] = {
            data : result,
            createdTime : new Date()
        }
        window.localStorage.setItem(UNPOST_KEY,JSON.stringify(data));
    }

    /**
     *  获取本地尚未提交到服务端的缓存数据
     * @param url  没有就返回所有未同步的数据
     */
    var getPost = function(url){
        var data = JSON.parse(window.localStorage.getItem(UNPOST_KEY));
        return (data && url ) ? data[url] : data;
    }
    /**
     * 移除未提交的待提交到服务端的缓存数据
     * @param url 没有就移除所有未提交的数据
     */
    var removePost = function(url){
        if(url){
            var data = getPost();
            delete data[url];
            window.localStorage.setItem(UNPOST_KEY,JSON.stringify(data));
        }else{
            window.localStorage.removeItem(UNPOST_KEY);
        }
    }
    /**
     * 同步本地未提交的数据到服务端
     * * @param url 没有就同步所有未提交的数据
     */
    var syncPost = function(url,success,error){
        var dataLen,index = 0;
        if($.type(url) == 'string'){
            dataLen = 1;
            sync(url);
        }else{
            var postData = getPost();
            if(!postData)return;
            dataLen = postData.length;
            for(var url in postData){
                sync(url);
            }
        }
        function sync(url){
            var data = getPost(url).data;
            $.ajax({
                url : url,
                contentType:'application/json',
                data : data,
                type : 'post',
                success : function(){
                    index++;
                    removePost(url);
                    if(index == dataLen)success(url);
                },
                error : function(){
                    error(url);
                }
            })
        }
    }

    /**
     * 清空本地缓存
     */
    var clear = function(){
        var storage = window.localStorage;
        for(var key in storage){
            if(key.indexOf(GET_KEY_PREFIX) == 0){
                storage.removeItem(key);
            }
        }
        storage.removeItem(UNPOST_KEY);
    }


    return {
        get : get,
        save : save,
        getPost : getPost,
        savePost : savePost,
        removePost : removePost,
        syncPost : syncPost,
        clear : clear
    }

})(J.$);
;(function($){
    /**
     * 日历组件
     * @param selector selector
     * @param options 配置参数
     */
    var calendar = function(selector,options){
        var defaults = {
                months : ["01月", "02月", "03月", "04月", "05月", "06月",
                    "07月", "08月", "09月", "10月", "11月", "12月"],
                days : ["日", "一", "二", "三", "四", "五", "六"],
                swipeable : true,//是否可通过手指滑动
                date : new Date(),//日历当前日期
                onRenderDay : undefined,//渲染单元格时的事件
                onSelect : undefined //选中日期时的事件
            },
            _this = this,
            $el = $(selector),
            $yearText,
            $monthText,
            $calendarBody,
            currentDate,currentYear,currentMonth;

        var _init = function(){
            _this.settings = $.extend({},defaults,options);
            currentYear = _this.settings.date.getFullYear();
            currentMonth = _this.settings.date.getMonth();
            currentDate = new Date(currentYear,currentMonth,_this.settings.date.getDate());
            _render();
            _subscribeEvents();
        }

        /**
         * 获取月份第一天是星期几[0-6]
         */
        var _fisrtDayOfMonth = function(date){
            return ( new Date(date.getFullYear(), date.getMonth(), 1) ).getDay();
        }
        /**
         * 获取月份总天数[1-31]
         */
        var _daysInMonth = function(date){
            return ( new Date(date.getFullYear(),date.getMonth()+1,0) ).getDate();
        }

        /**
         * 渲染日历
         * @private
         */
        var _render = function(){
            var html = '';
            html += '<div class="jingle-calendar">';
            html += _renderNav(currentYear,currentMonth);
            html += _renderHead();
            html += '<div class="jingle-calendar-body">';
            html += _renderBody(currentDate);
            html += '</div></div>'
            $el.html(html);
            var $span = $el.find('span');
            $yearText = $span.eq(0);
            $monthText = $span.eq(1);
            $calendarBody = $el.find('.jingle-calendar-body');
        }

        var _renderNav = function(year,month){
            var html = '<div class="jingle-calendar-nav">';
            html += '<div> <i class="icon previous" data-year='+year+'></i><span>'+year+'</span><i class="icon next" data-year='+year+'></i></div>';
            html += '<div ><i class="icon previous" data-month='+month+'></i> <span>'+_this.settings.months[month]+'</span><i class="icon next" data-month='+month+'></i></div>';
            html += '</div>';
            return html;
        }

        var _renderHead = function(){
            var html = '<table><thead><tr>';
            for(var i = 0; i < 7; i++){
                html += '<th>'+_this.settings.days[i]+'</th>';
            }
            html += '</tr></thead></table>'
            return html;
        }

        var _renderBody = function(date){
            var firstDay = _fisrtDayOfMonth(date),
                days = _daysInMonth(date),
                rows = Math.ceil((firstDay+days) / 7),
                beginDate,
                html = '';
            currentYear = date.getFullYear();
            currentMonth = date.getMonth();
            beginDate = new Date(currentYear,currentMonth,1-firstDay);//日历开始的日期
            html += '<table><tbody>';
            for(var i = 0; i < rows; i++){
                html += '<tr>';
                for(var j = 0; j < 7; j++){
                    html += _renderDay(beginDate,currentMonth);
                    beginDate.setDate(beginDate.getDate() + 1);
                }
                html += '</tr>';
            }
            html += '</tbody></table>';
            return html;
        }
        var _renderDay = function(date,month){
            var otherMonth = (date.getMonth() !== month);
            var dateStr = _this.format(date);
            var classname = (_this.format(_this.settings.date) == dateStr) ? 'active':'';
            var dayStr = date.getDate();
            if(_this.settings.onRenderDay){
                dayStr = _this.settings.onRenderDay.call(null,dayStr,dateStr);
            }
            return otherMonth ? '<td>&nbsp;</td>' : '<td data-selected="selected" class="'+classname+ '" data-date= '+dateStr+'>'+dayStr+'</td>';
        }

        var _subscribeEvents = function(){
            var $target,$ctarget;
            $el.on('tap',function(e){
                $target = $(e.target);
                if($target.is('[data-year].next')){
                    //后一年
                    currentDate.setFullYear(currentDate.getFullYear()+1);
                    _this.refresh(currentDate);
                }else if($target.is('[data-year].previous')){
                    //前一年
                    currentDate.setFullYear(currentDate.getFullYear()-1);
                    _this.refresh(currentDate);
                }else if($target.is('[data-month].next')){
                    //后一月
                    currentDate.setMonth(currentDate.getMonth()+1);
                    _this.refresh(currentDate);
                }else if($target.is('[data-month].previous')){
                    //前一月
                    currentDate.setMonth(currentDate.getMonth()-1);
                    _this.refresh(currentDate);
                }
                $ctarget = $target.closest('td');
                if(!$target.is('td') && $ctarget.length > 0){
                    $target = $ctarget;
                }
                if($target.is('td')){
                    var dateStr = $target.data('date');
                    if(dateStr && _this.settings.onSelect){
                        _this.settings.onSelect.call(_this,dateStr)
                    }
                }

            });
            $el.on('swipeLeft',function(){
                currentDate.setMonth(currentDate.getMonth()+1);
                _this.refresh(currentDate);
            });
            $el.on('swipeRight',function(){
                currentDate.setMonth(currentDate.getMonth()-1);
                _this.refresh(currentDate);
            })
        }

        /**
         * 刷新日历为指定日期
         * @param date 指定日期
         */
        this.refresh = function(date){
            var oldDate = new Date(currentYear,currentMonth,1),
                newDate = new Date(date.getFullYear(),date.getMonth(),1),
                transition = undefined,$table;

            if(oldDate.getTime() == newDate.getTime())return;
            transition = oldDate<newDate ? 'slideLeftRound' : 'slideRightRound';

            $yearText.text(date.getFullYear());
            $monthText.text(this.settings.months[date.getMonth()]);
            var newbody = _renderBody(date);
            J.anim($calendarBody,transition,function(){
                $calendarBody.html(newbody);
            });

        }
        _init();
    }
    /**
     * 字符串转化为日期对象，只支持yyyy-MM-dd 和 yyyy/MM/dd
     * @param date
     * @return {*}
     */
    calendar.prototype.parse = function(date){
        var dateRE = /^(\d{4})(?:\-|\/)(\d{1,2})(?:\-|\/)(\d{1,2})$/;
        return dateRE.test(date) ? new Date(parseInt(RegExp.$1, 10), parseInt(RegExp.$2, 10) - 1, parseInt(RegExp.$3, 10)) : null;
    }
    /**
     * 格式化日期  yyyy-MM-dd
     * @return {String}
     */
    calendar.prototype.format = function(date){
        var y  = date.getFullYear(),m = date.getMonth()+1,d = date.getDate();
        m = (m<10)?('0'+m):m;
        d = (d<10)?('0'+d):d;
        return y + '-' + m + '-' + d;
    }
    J.Calendar = calendar;
})(J.$);
/**
 *  哥屋恩动组件(iscroll)
 */
;(function($){
    var scrollCache = {},index = 1;
    J.Scroll = function(selector,opts){
        var scroll,scrollId,$el = $(selector),
            options = {
               hScroll : false,
               bounce : false,
               lockDirection : true,
               useTransform: true,
               useTransition: false,
               checkDOMChanges: false,
               onBeforeScrollStart: function (e) {
                    var target = e.target;
                    while (target.nodeType != 1) target = target.parentNode;
                    if (target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA')
                        e.preventDefault();
                }
            };
        scrollId = $el.data('_jscroll_');
        //滚动组件使用频繁，缓存起来节省开销
        if(scrollId && scrollCache[scrollId]){
            scroll = scrollCache[scrollId];
            $.extend(scroll.scroller.options,opts)
            scroll.scroller.refresh();
            return scroll;
        }else{
            scrollId = '_jscroll_'+index++;
            $el.data('_jscroll_',scrollId);
            $.extend(options,opts);
            scroller = new iScroll($el[0],options);
            return scrollCache[scrollId] = {
                scroller : scroller,
                destroy : function(){
                    scroller.destroy();
                    delete scrollCache[scrollId];
                }
            };
        };
    }
})(J.$);


/**
 * 幻灯片组件
 */
;(function($){
    function slider(selector,showDots){
        var afterSlide = function(){},
            beforeSlide = function(){return true},
            gestureStarted = false,
            index = 0,
            speed = 200,
            wrapper,
            dots,
            container,
            slides,
            slideNum,
            slideWidth,
            deltaX,
            autoPlay
            interval = 0;
        var _this = this;

        if($.isPlainObject(selector)){
            wrapper = $(selector.selector);
            showDots = selector.showDots;
            beforeSlide = selector.onBeforeSlide || beforeSlide;
            afterSlide = selector.onAfterSlide || afterSlide;
            autoPlay = selector.autoPlay;
            interval = selector.interval || 3000;
        }else{
            wrapper = $(selector);
        }
        /**
         * 初始化容器大小
         */
        var _init = function() {
            container = wrapper.children().first();
            slides = container.children();
            slideNum = slides.length;
            slideWidth = wrapper.offset().width;
            container.css('width',slideNum * slideWidth);
            slides.css({
                    'width':slideWidth,
                    'float':'left'
            }).show();
            if(showDots == undefined)showDots = true;
            showDots && _initDots();
            _slide(0, 0);
            afterSlide(0);
            autoPlay && _autoPlay();
        };

        var _autoPlay = function(){
            setTimeout(function(){
                if(index == slideNum - 1){
                    _slide(0);
                }else{
                    _this.next();
                }
                _autoPlay();
            },interval);
        }

        var _initDots = function(){
            dots = wrapper.find('.dots');
            if(dots.length>0){
                dots.show();
            }else{
                var dotsWidth = slideNum*30+20+2;
                var html = '<div class="dots"><ul>';
                for(var i=0;i<slideNum;i++){
                    html +='<li index="'+i+'"';
                    if(i == 0){
                        html += 'class="active"';
                    }
                    html += '><a href="#"></a></li>';
                }
                html += '</ul></div>';
                wrapper.append(html);
                dots = wrapper.find('.dots');
                dots.children().css('width',dotsWidth+'px');
                dots.find('li').on('tap',function(){
                    var index = $(this).attr('index');
                    _slide(parseInt(index), speed);
                })
            }
        }

        /**
         * 滑动到指定卡片
         * @param i
         * @param duration
         * @private
         */
        var _slide = function(i, duration) {
            duration = duration || speed;
            container.css({
                '-webkit-transition-duration':duration + 'ms',
                '-webkit-transform':'translate3D(' + -(i * slideWidth) + 'px,0,0)'
            });
            if(index != i){
                index = i;
                if(dots) $(dots.find('li').get(index)).addClass('active').siblings().removeClass('active');
                afterSlide(index);
            }
        };

        /**
         * 绑定滑动事件
         */
        var _bindEvents = function() {
            container.on('touchstart',_touchStart,false);
            container.on('touchmove',_touchMove,false);
            container.on('touchend',_touchEnd,false);
        };

        var  _touchStart = function(event) {
            var e = event.touches[0];
            start = {
                pageX: e.pageX,
                pageY: e.pageY,
                time: Number(new Date())
            };
            isScrolling = undefined;
            deltaX = 0;
            container[0].style.webkitTransitionDuration = 0;
            gestureStarted = true;
        };

        var _touchMove = function(event) {
            if(!gestureStarted)return;
            var e = event.touches[0];
            deltaX = e.pageX - start.pageX;
            if ( typeof isScrolling == 'undefined') {
                //根据X、Y轴的偏移量判断用户的意图是左右滑动还是上下滑动
                isScrolling = Math.abs(deltaX) < Math.abs(e.pageY - start.pageY)
            }
            if (!isScrolling) {
                event.preventDefault();
                //判定是否达到了边界即第一个右滑、最后一个左滑
                var isPastBounds = !index && deltaX > 0 || index == slideNum - 1 && deltaX < 0;
                if(isPastBounds)return;
                var pos = (deltaX - index * slideWidth);
                container[0].style.webkitTransform = 'translate3D('+pos+'px,0,0)';
            }
        };

        var _touchEnd = function(e) {
            //判定是否跳转到下一个卡片
            //滑动时间小于250ms或者滑动X轴的距离大于屏幕宽度的1/3，则直接跳转到下一个卡片
            var isValidSlide = (Number(new Date()) - start.time < 250 && Math.abs(deltaX) > 20) || Math.abs(deltaX) > slideWidth/3;
                //判定是否达到了边界即第一个右滑、最后一个左滑
            var isPastBounds = !index && deltaX > 0 || index == slideNum - 1 && deltaX < 0;
            if (!isScrolling) {
                if(beforeSlide(index,deltaX)){
                    _slide( index + ( isValidSlide && !isPastBounds ? (deltaX < 0 ? 1 : -1) : 0 ), speed );
                }else{
                    _slide(index);
                }
            }
            gestureStarted = false;
        };


        _init();
        _bindEvents();

        this.refresh = function(){
            container.attr('style','');
            _init();
        };

        this.prev = function() {
            if (index) _slide(index-1, speed);
        };

        this.next = function() {
            if(index < slideNum-1){
                _slide(index+1, speed);
            }
        };
        this.index = function(i) {
            _slide(i);
        };
    }
    J.Slider = slider;
})(J.$);
/**
 * 上拉/下拉组件
 */
;(function($){
    var refreshCache = {},index = 1;
    function Refresh(selector,type,callback){
        var iscroll, scroller,refreshEl,iconEl,labelEl,topOffset,isPullDown,
            options = {
                selector : undefined,
                type : 'pullDown',//pullDown|pullUp 默认为pullDown
                minPullHeight : 10,//拉动的像素相对值，超过才会翻转
                pullText: "下拉刷新...",
                releaseText: "松开立即刷新...",
                refreshText: "刷新中...",
                refreshTip : false,//下拉时显示的文本，比如：最后更新时间:2013-....
                onPullIcon : 'arrow-down-2',
                onReleaseIcon  : 'icon-reverse',
                onRefreshIcon : 'spinner',
                callback : undefined
            };
        //装载配置
        if(typeof selector === 'object'){
            $.extend(options,selector);
        }else{
            options.selector = selector;
            options.type = type;
            options.callback = callback;
            if(type === 'pullUp'){
                $.extend(options,{
                    pullText: "上拉加载更多...",
                    releaseText: "松开开立即加载...",
                    refreshText: "加载中...",
                    onPullIcon : 'arrow-up-3'
                })
            }
        }
        isPullDown = options.type === 'pullDown' ? true : false;

        /**
         * 初始化dom节点
         * @param opts
         * @private
         */
        var _init = function(opts){
            scroller = $(opts.selector).children()[0];
            var refreshTpl = '<div class="refresh-container"><span class="refresh-icon icon '+opts.onPullIcon
                +'"></span><span class="refresh-label">'
                +opts.pullText+'</span>'
                +(opts.refreshTip?'<div class="refresh-tip">'+opts.refreshTip+'</div>':'')+'</div>';
            if(isPullDown){
                refreshEl = $(refreshTpl).prependTo(scroller);
            }else{
                refreshEl = $(refreshTpl).appendTo(scroller);
            }
            topOffset = refreshEl.height();
            iconEl = refreshEl.find('.refresh-icon');
            labelEl = refreshEl.find('.refresh-label');
        }

        /**
         * 构造iscroll组件，并绑定滑动事件
         * @param opts
         * @private
         */
        var _excuteScroll = function(opts){
            return J.Scroll(opts.selector,{
                    topOffset:isPullDown?topOffset:0,
                    bounce : true,
                    onScrollMove : function(){
                        if (this.y > opts.minPullHeight && isPullDown && !iconEl.hasClass(opts.onReleaseIcon)) {
                            iconEl.addClass(opts.onReleaseIcon);
                            labelEl.html(opts.releaseText);
                            this.minScrollY = 0;
                        } else if (this.y < opts.minPullHeight && isPullDown && iconEl.hasClass(opts.onReleaseIcon)) {
                            iconEl.removeClass(opts.onReleaseIcon);
                            labelEl.html(opts.pullText);
                            this.minScrollY = -topOffset;
                        }else if (this.y < (this.maxScrollY - opts.minPullHeight) && !isPullDown && !iconEl.hasClass(opts.onReleaseIcon)) {
                            iconEl.addClass(opts.onReleaseIcon);
                            labelEl.html(opts.releaseText);
                            this.maxScrollY = this.maxScrollY;
                        } else if (this.y > (this.maxScrollY + opts.minPullHeight) && !isPullDown && iconEl.hasClass(opts.onReleaseIcon)) {
                            iconEl.removeClass(opts.onReleaseIcon);
                            labelEl.html(opts.pullText);
                            this.maxScrollY = topOffset;
                        }
                    },
                    onScrollEnd : function(){
                        if(iconEl.hasClass(opts.onReleaseIcon)){
                            iconEl.removeClass(opts.onReleaseIcon).removeClass(opts.onPullIcon).addClass(opts.onRefreshIcon);
                            labelEl.html(opts.refreshText);
                            var _this = this;
                            setTimeout(function(){//解决在chrome下onRefresh的时候文本无法更改的问题。奇怪的问题！
                                opts.callback.call(_this);
                            },1);

                        }
                    },
                    onRefresh: function () {
                        iconEl.removeClass(opts.onRefreshIcon).addClass(opts.onPullIcon);
                        labelEl.html(opts.pullText);
                    }
                });
        }

        //run
        _init(options);
        iscroll = _excuteScroll(options);
        return iscroll;

    }

    /**
     * 刷新组件
     * @param selector selector
     * @param type 类型 pullDown(下拉) pullUp(上拉)
     * @param callback 回调函数
     */
    J.Refresh = function(selector,type,callback){
        var el,jRefreshId;
        if(selector.selector){
            el = $(selector.selector)
        }else{
            el = $(selector);
        }
        jRefreshId = el.data('_jrefresh_');
        //因上拉下拉可能会使用的比较频繁，故缓存起来节省开销,亦可防止重复绑定
        if(jRefreshId && refreshCache[jRefreshId]){
            return refreshCache[jRefreshId];
        }else{
            jRefreshId = '_jrefresh_'+index++;
            el.data('_jrefresh_',jRefreshId);
            var refresh = new Refresh(selector,type,callback);
            return refreshCache[jRefreshId] = {
                scroller : refresh.scroller,
                destroy : function(){
                    delete refreshCache[jRefreshId];
                    refresh.scroller.destroy();
                    $('.refresh-container',selector).remove();
                }
            };
        }
    }
})(J.$);