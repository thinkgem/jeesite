
/*! 
 * layDate 5.3.1 日期与时间组件（单独版） 
 * MIT Licensed 
 */ 


/*!
 * Layui
 * Classic modular Front-End UI library
 * MIT Licensed
 */
 
;!function(win){
  "use strict";

  var doc = win.document, config = {
    modules: {} //记录模块物理路径
    ,status: {} //记录模块加载状态
    ,timeout: 10 //符合规范的模块请求最长等待秒数
    ,event: {} //记录模块自定义事件
  }

  ,Layui = function(){
    this.v = '2.6.7'; // layui 版本号
  }
  
  //识别预先可能定义的指定全局对象
  ,GLOBAL = win.LAYUI_GLOBAL || {}

  //获取 layui 所在目录
  ,getPath = function(){
    var jsPath = doc.currentScript ? doc.currentScript.src : function(){
      var js = doc.scripts
      ,last = js.length - 1
      ,src;
      for(var i = last; i > 0; i--){
        if(js[i].readyState === 'interactive'){
          src = js[i].src;
          break;
        }
      }
      return src || js[last].src;
    }();
    
    return config.dir = GLOBAL.dir || jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
  }()

  //异常提示
  ,error = function(msg, type){
    type = type || 'log';
    win.console && console[type] && console[type]('layui error hint: ' + msg);
  }

  ,isOpera = typeof opera !== 'undefined' && opera.toString() === '[object Opera]'

  //内置模块
  ,modules = config.builtin = {
    lay: 'lay' //基础 DOM 操作
    ,layer: 'layer' //弹层
    ,laydate: 'laydate' //日期
    ,laypage: 'laypage' //分页
    ,laytpl: 'laytpl' //模板引擎
    ,layedit: 'layedit' //富文本编辑器
    ,form: 'form' //表单集
    ,upload: 'upload' //上传
    ,dropdown: 'dropdown' //下拉菜单
    ,transfer: 'transfer' //穿梭框
    ,tree: 'tree' //树结构
    ,table: 'table' //表格
    ,element: 'element' //常用元素操作
    ,rate: 'rate'  //评分组件
    ,colorpicker: 'colorpicker' //颜色选择器
    ,slider: 'slider' //滑块
    ,carousel: 'carousel' //轮播
    ,flow: 'flow' //流加载
    ,util: 'util' //工具块
    ,code: 'code' //代码修饰器
    ,jquery: 'jquery' //DOM 库（第三方）
    
    ,all: 'all'
    ,'layui.all': 'layui.all' //聚合标识（功能性的，非真实模块）
  };

  //记录基础数据
  Layui.prototype.cache = config;

  //定义模块
  Layui.prototype.define = function(deps, factory){
    var that = this
    ,type = typeof deps === 'function'
    ,callback = function(){
      var setApp = function(app, exports){
        layui[app] = exports;
        config.status[app] = true;
      };
      typeof factory === 'function' && factory(function(app, exports){
        setApp(app, exports);
        config.callback[app] = function(){
          factory(setApp);
        }
      });
      return this;
    };
    
    type && (
      factory = deps,
      deps = []
    );
    
    that.use(deps, callback, null, 'define');
    return that;
  };

  //使用特定模块
  Layui.prototype.use = function(apps, callback, exports, from){
    var that = this
    ,dir = config.dir = config.dir ? config.dir : getPath
    ,head = doc.getElementsByTagName('head')[0];

    apps = function(){
      if(typeof apps === 'string'){
        return [apps];
      } 
      //当第一个参数为 function 时，则自动加载所有内置模块，且执行的回调即为该 function 参数；
      else if(typeof apps === 'function'){
        callback = apps;
        return ['all'];
      }      
      return apps;
    }();
    
    //如果页面已经存在 jQuery 1.7+ 库且所定义的模块依赖 jQuery，则不加载内部 jquery 模块
    if(win.jQuery && jQuery.fn.on){
      that.each(apps, function(index, item){
        if(item === 'jquery'){
          apps.splice(index, 1);
        }
      });
      layui.jquery = layui.$ = jQuery;
    }
    
    var item = apps[0]
    ,timeout = 0;
    exports = exports || [];

    //静态资源host
    config.host = config.host || (dir.match(/\/\/([\s\S]+?)\//)||['//'+ location.host +'/'])[0];
    
    //加载完毕
    function onScriptLoad(e, url){
      var readyRegExp = navigator.platform === 'PLaySTATION 3' ? /^complete$/ : /^(complete|loaded)$/
      if (e.type === 'load' || (readyRegExp.test((e.currentTarget || e.srcElement).readyState))) {
        config.modules[item] = url;
        head.removeChild(node);
        (function poll() {
          if(++timeout > config.timeout * 1000 / 4){
            return error(item + ' is not a valid module', 'error');
          };
          config.status[item] ? onCallback() : setTimeout(poll, 4);
        }());
      }
    }
  
    //回调
    function onCallback(){
      exports.push(layui[item]);
      apps.length > 1 ?
        that.use(apps.slice(1), callback, exports, from)
      : ( typeof callback === 'function' && function(){
        //保证文档加载完毕再执行回调
        if(layui.jquery && typeof layui.jquery === 'function' && from !== 'define'){
          return layui.jquery(function(){
            callback.apply(layui, exports);
          });
        }
        callback.apply(layui, exports);
      }() );
    }
    
    //如果引入了聚合板，内置的模块则不必重复加载
    if( apps.length === 0 || (layui['layui.all'] && modules[item]) ){
      return onCallback(), that;
    }
    
    //获取加载的模块 URL
    //如果是内置模块，则按照 dir 参数拼接模块路径
    //如果是扩展模块，则判断模块路径值是否为 {/} 开头，
    //如果路径值是 {/} 开头，则模块路径即为后面紧跟的字符。
    //否则，则按照 base 参数拼接模块路径
    
    var url = ( modules[item] ? (dir + 'modules/') 
      : (/^\{\/\}/.test(that.modules[item]) ? '' : (config.base || ''))
    ) + (that.modules[item] || item) + '.js';
    url = url.replace(/^\{\/\}/, '');
    
    //如果扩展模块（即：非内置模块）对象已经存在，则不必再加载
    if(!config.modules[item] && layui[item]){
      config.modules[item] = url; //并记录起该扩展模块的 url
    }

    //首次加载模块
    if(!config.modules[item]){
      var node = doc.createElement('script');
      
      node.async = true;
      node.charset = 'utf-8';
      node.src = url + function(){
        var version = config.version === true 
        ? (config.v || (new Date()).getTime())
        : (config.version||'');
        return version ? ('?v=' + version) : '';
      }();
      
      head.appendChild(node);
      
      if(node.attachEvent && !(node.attachEvent.toString && node.attachEvent.toString().indexOf('[native code') < 0) && !isOpera){
        node.attachEvent('onreadystatechange', function(e){
          onScriptLoad(e, url);
        });
      } else {
        node.addEventListener('load', function(e){
          onScriptLoad(e, url);
        }, false);
      }
      
      config.modules[item] = url;
    } else { //缓存
      (function poll() {
        if(++timeout > config.timeout * 1000 / 4){
          return error(item + ' is not a valid module', 'error');
        };
        (typeof config.modules[item] === 'string' && config.status[item]) 
        ? onCallback() 
        : setTimeout(poll, 4);
      }());
    }
    
    return that;
  };

  //获取节点的 style 属性值
  Layui.prototype.getStyle = function(node, name){
    var style = node.currentStyle ? node.currentStyle : win.getComputedStyle(node, null);
    return style[style.getPropertyValue ? 'getPropertyValue' : 'getAttribute'](name);
  };

  //css外部加载器
  Layui.prototype.link = function(href, fn, cssname){
    var that = this
    ,head = doc.getElementsByTagName('head')[0]
    ,link = doc.createElement('link');
    
    if(typeof fn === 'string') cssname = fn;
    
    var app = (cssname || href).replace(/\.|\//g, '')
    ,id = link.id = 'layuicss-'+ app
    ,STAUTS_NAME = 'creating'
    ,timeout = 0;
    
    link.rel = 'stylesheet';
    link.href = href + (config.debug ? '?v='+new Date().getTime() : '');
    link.media = 'all';
    
    if(!doc.getElementById(id)){
      head.appendChild(link);
    }
    
    if(typeof fn !== 'function') return that;
    
    //轮询 css 是否加载完毕
    (function poll(status) {
      var delay = 100
      ,getLinkElem = doc.getElementById(id); //获取动态插入的 link 元素
      
      //如果轮询超过指定秒数，则视为请求文件失败或 css 文件不符合规范
      if(++timeout > config.timeout * 1000 / delay){
        return error(href + ' timeout');
      };
      
      //css 加载就绪
      if(parseInt(that.getStyle(getLinkElem, 'width')) === 1989){
        //如果参数来自于初始轮询（即未加载就绪时的），则移除 link 标签状态
        if(status === STAUTS_NAME) getLinkElem.removeAttribute('lay-status');
        //如果 link 标签的状态仍为「创建中」，则继续进入轮询，直到状态改变，则执行回调
        getLinkElem.getAttribute('lay-status') === STAUTS_NAME ? setTimeout(poll, delay) : fn();
      } else {
        getLinkElem.setAttribute('lay-status', STAUTS_NAME);
        setTimeout(function(){
          poll(STAUTS_NAME);
        }, delay);
      }
    }());
    
    //轮询css是否加载完毕
    /*
    (function poll() {
      if(++timeout > config.timeout * 1000 / 100){
        return error(href + ' timeout');
      };
      parseInt(that.getStyle(doc.getElementById(id), 'width')) === 1989 ? function(){
        fn();
      }() : setTimeout(poll, 100);
    }());
    */
    
    return that;
  };
  
  //css 内部加载器
  Layui.prototype.addcss = function(firename, fn, cssname){
    return layui.link(config.dir + 'css/' + firename, fn, cssname);
  };
  
  //存储模块的回调
  config.callback = {};
  
  //重新执行模块的工厂函数
  Layui.prototype.factory = function(modName){
    if(layui[modName]){
      return typeof config.callback[modName] === 'function' 
        ? config.callback[modName]
      : null;
    }
  };

  //图片预加载
  Layui.prototype.img = function(url, callback, error) {   
    var img = new Image();
    img.src = url; 
    if(img.complete){
      return callback(img);
    }
    img.onload = function(){
      img.onload = null;
      typeof callback === 'function' && callback(img);
    };
    img.onerror = function(e){
      img.onerror = null;
      typeof error === 'function' && error(e);
    };  
  };

  //全局配置
  Layui.prototype.config = function(options){
    options = options || {};
    for(var key in options){
      config[key] = options[key];
    }
    return this;
  };

  //记录全部模块
  Layui.prototype.modules = function(){
    var clone = {};
    for(var o in modules){
      clone[o] = modules[o];
    }
    return clone;
  }();

  //拓展模块
  Layui.prototype.extend = function(options){
    var that = this;

    //验证模块是否被占用
    options = options || {};
    for(var o in options){
      if(that[o] || that.modules[o]){
        error(o+ ' Module already exists', 'error');
      } else {
        that.modules[o] = options[o];
      }
    }

    return that;
  };

  // location.hash 路由解析
  Layui.prototype.router = function(hash){
    var that = this
    ,hash = hash || location.hash
    ,data = {
      path: []
      ,search: {}
      ,hash: (hash.match(/[^#](#.*$)/) || [])[1] || ''
    };
    
    if(!/^#\//.test(hash)) return data; //禁止非路由规范
    hash = hash.replace(/^#\//, '');
    data.href = '/' + hash;
    hash = hash.replace(/([^#])(#.*$)/, '$1').split('/') || [];
    
    //提取 Hash 结构
    that.each(hash, function(index, item){
      /^\w+=/.test(item) ? function(){
        item = item.split('=');
        data.search[item[0]] = item[1];
      }() : data.path.push(item);
    });
    
    return data;
  };
  
  //URL 解析
  Layui.prototype.url = function(href){
    var that = this
    ,data = {
      //提取 url 路径
      pathname: function(){
        var pathname = href
          ? function(){
            var str = (href.match(/\.[^.]+?\/.+/) || [])[0] || '';
            return str.replace(/^[^\/]+/, '').replace(/\?.+/, '');
          }()
        : location.pathname;
        return pathname.replace(/^\//, '').split('/');
      }()
      
      //提取 url 参数
      ,search: function(){
        var obj = {}
        ,search = (href 
          ? function(){
            var str = (href.match(/\?.+/) || [])[0] || '';
            return str.replace(/\#.+/, '');
          }()
          : location.search
        ).replace(/^\?+/, '').split('&'); //去除 ?，按 & 分割参数
        
        //遍历分割后的参数
        that.each(search, function(index, item){
          var _index = item.indexOf('=')
          ,key = function(){ //提取 key
            if(_index < 0){
              return item.substr(0, item.length);
            } else if(_index === 0){
              return false;
            } else {
              return item.substr(0, _index);
            }
          }(); 
          //提取 value
          if(key){
            obj[key] = _index > 0 ? item.substr(_index + 1) : null;
          }
        });
        
        return obj;
      }()
      
      //提取 Hash
      ,hash: that.router(function(){
        return href 
          ? ((href.match(/#.+/) || [])[0] || '/')
        : location.hash;
      }())
    };
    
    return data;
  };

  //本地持久性存储
  Layui.prototype.data = function(table, settings, storage){
    table = table || 'layui';
    storage = storage || localStorage;
    
    if(!win.JSON || !win.JSON.parse) return;
    
    //如果settings为null，则删除表
    if(settings === null){
      return delete storage[table];
    }
    
    settings = typeof settings === 'object' 
      ? settings 
    : {key: settings};
    
    try{
      var data = JSON.parse(storage[table]);
    } catch(e){
      var data = {};
    }
    
    if('value' in settings) data[settings.key] = settings.value;
    if(settings.remove) delete data[settings.key];
    storage[table] = JSON.stringify(data);
    
    return settings.key ? data[settings.key] : data;
  };
  
  //本地会话性存储
  Layui.prototype.sessionData = function(table, settings){
    return this.data(table, settings, sessionStorage);
  }

  //设备信息
  Layui.prototype.device = function(key){
    var agent = navigator.userAgent.toLowerCase()

    //获取版本号
    ,getVersion = function(label){
      var exp = new RegExp(label + '/([^\\s\\_\\-]+)');
      label = (agent.match(exp)||[])[1];
      return label || false;
    }
    
    //返回结果集
    ,result = {
      os: function(){ //底层操作系统
        if(/windows/.test(agent)){
          return 'windows';
        } else if(/linux/.test(agent)){
          return 'linux';
        } else if(/iphone|ipod|ipad|ios/.test(agent)){
          return 'ios';
        } else if(/mac/.test(agent)){
          return 'mac';
        } 
      }()
      ,ie: function(){ //ie版本
        return (!!win.ActiveXObject || "ActiveXObject" in win) ? (
          (agent.match(/msie\s(\d+)/) || [])[1] || '11' //由于ie11并没有msie的标识
        ) : false;
      }()
      ,weixin: getVersion('micromessenger')  //是否微信
    };
    
    //任意的key
    if(key && !result[key]){
      result[key] = getVersion(key);
    }
    
    //移动设备
    result.android = /android/.test(agent);
    result.ios = result.os === 'ios';
    result.mobile = (result.android || result.ios) ? true : false;
    
    return result;
  };

  //提示
  Layui.prototype.hint = function(){
    return {
      error: error
    };
  };

  
  //typeof 类型细分 -> string/number/boolean/undefined/null、object/array/function/…
  Layui.prototype._typeof = function(operand){
    if(operand === null) return String(operand);
    
    //细分引用类型
    return (typeof operand === 'object' || typeof operand === 'function') ? function(){
      var type = Object.prototype.toString.call(operand).match(/\s(.+)\]$/) || [] //匹配类型字符
      ,classType = 'Function|Array|Date|RegExp|Object|Error|Symbol'; //常见类型字符
      
      type = type[1] || 'Object';
      
      //除匹配到的类型外，其他对象均返回 object
      return new RegExp('\\b('+ classType + ')\\b').test(type) 
        ? type.toLowerCase() 
      : 'object';
    }() : typeof operand;
  };
  
  //对象是否具备数组结构（此处为兼容 jQuery 对象）
  Layui.prototype._isArray = function(obj){
    var that = this
    ,len
    ,type = that._typeof(obj);
    
    if(!obj || (typeof obj !== 'object') || obj === win) return false;
    
    len = 'length' in obj && obj.length; //兼容 ie
    return type === 'array' || len === 0 || (
      typeof len === 'number' && len > 0 && (len - 1) in obj //兼容 jQuery 对象
    );
  };

  //遍历
  Layui.prototype.each = function(obj, fn){
    var key
    ,that = this
    ,callFn = function(key, obj){ //回调
      return fn.call(obj[key], key, obj[key])
    };
    
    if(typeof fn !== 'function') return that;
    obj = obj || [];
    
    //优先处理数组结构
    if(that._isArray(obj)){
      for(key = 0; key < obj.length; key++){
        if(callFn(key, obj)) break;
      }
    } else {
      for(key in obj){
        if(callFn(key, obj)) break;
      }
    }
    
    return that;
  };

  //将数组中的对象按其某个成员排序
  Layui.prototype.sort = function(obj, key, desc){
    var clone = JSON.parse(
      JSON.stringify(obj || [])
    );
    
    if(!key) return clone;
    
    //如果是数字，按大小排序；如果是非数字，则按字典序排序
    clone.sort(function(o1, o2){
      var isNum = /^-?\d+$/
      ,v1 = o1[key]
      ,v2 = o2[key];
      
      if(isNum.test(v1)) v1 = parseFloat(v1);
      if(isNum.test(v2)) v2 = parseFloat(v2);

      return v1 - v2;
      
      /*
      if(v1 && !v2){
        return 1;
      } else if(!v1 && v2){
        return -1;
      }
        
      if(v1 > v2){
        return 1;
      } else if (v1 < v2) {
        return -1;
      } else {
        return 0;
      }
      */
      
    });

    desc && clone.reverse(); //倒序
    return clone;
  };

  //阻止事件冒泡
  Layui.prototype.stope = function(thisEvent){
    thisEvent = thisEvent || win.event;
    try { thisEvent.stopPropagation() } catch(e){
      thisEvent.cancelBubble = true;
    }
  };
  
  //字符常理
  var EV_REMOVE = 'LAYUI-EVENT-REMOVE';

  //自定义模块事件
  Layui.prototype.onevent = function(modName, events, callback){
    if(typeof modName !== 'string' 
    || typeof callback !== 'function') return this;

    return Layui.event(modName, events, null, callback);
  };

  //执行自定义模块事件
  Layui.prototype.event = Layui.event = function(modName, events, params, fn){
    var that = this
    ,result = null
    ,filter = (events || '').match(/\((.*)\)$/)||[] //提取事件过滤器字符结构，如：select(xxx)
    ,eventName = (modName + '.'+ events).replace(filter[0], '') //获取事件名称，如：form.select
    ,filterName = filter[1] || '' //获取过滤器名称,，如：xxx
    ,callback = function(_, item){
      var res = item && item.call(that, params);
      res === false && result === null && (result = false);
    };
    
    //如果参数传入特定字符，则执行移除事件
    if(params === EV_REMOVE){
      delete (that.cache.event[eventName] || {})[filterName];
      return that;
    }
    
    //添加事件
    if(fn){
      config.event[eventName] = config.event[eventName] || {};

      //这里不再对重复事件做支持
      //config.event[eventName][filterName] ? config.event[eventName][filterName].push(fn) : 
      config.event[eventName][filterName] = [fn];
      return this;
    }
    
    //执行事件回调
    layui.each(config.event[eventName], function(key, item){
      //执行当前模块的全部事件
      if(filterName === '{*}'){
        layui.each(item, callback);
        return;
      }
      
      //执行指定事件
      key === '' && layui.each(item, callback);
      (filterName && key === filterName) && layui.each(item, callback);
    });
    
    return result;
  };
  
  //新增模块事件
  Layui.prototype.on = function(events, modName, callback){
    var that = this;
    return that.onevent.call(that, modName, events, callback);
  }
  
  //移除模块事件
  Layui.prototype.off = function(events, modName){
    var that = this;
    return that.event.call(that, modName, events, EV_REMOVE);
  };
  
  //exports layui
  var layui = new Layui();
  



/*! lay 基础 DOM 操作 | MIT Licensed */


  "use strict";
  
  var MOD_NAME = 'lay' //模块名
  ,document = window.document
  
  //DOM查找
  ,lay = function(selector){   
    return new LAY(selector);
  }
  
  //DOM构造器
  ,LAY = function(selector){
    var index = 0
    ,nativeDOM = typeof selector === 'object' ? [selector] : (
      this.selector = selector
      ,document.querySelectorAll(selector || null)
    );
    for(; index < nativeDOM.length; index++){
      this.push(nativeDOM[index]);
    }
  };
  
  /*
    lay 对象操作
  */
  
  LAY.prototype = [];
  LAY.prototype.constructor = LAY;
  
  //普通对象深度扩展
  lay.extend = function(){
    var ai = 1, args = arguments
    ,clone = function(target, obj){
      target = target || (obj.constructor === Array ? [] : {}); 
      for(var i in obj){
        //如果值为对象，则进入递归，继续深度合并
        target[i] = (obj[i] && (obj[i].constructor === Object))
          ? clone(target[i], obj[i])
        : obj[i];
      }
      return target;
    }

    args[0] = typeof args[0] === 'object' ? args[0] : {};

    for(; ai < args.length; ai++){
      if(typeof args[ai] === 'object'){
        clone(args[0], args[ai])
      }
    }
    return args[0];
  };
  
  //lay 模块版本
  lay.v = '1.0.7';
  
  //ie版本
  lay.ie = function(){
    var agent = navigator.userAgent.toLowerCase();
    return (!!window.ActiveXObject || "ActiveXObject" in window) ? (
      (agent.match(/msie\s(\d+)/) || [])[1] || '11' //由于 ie11 并没有 msie 的标识
    ) : false;
  }();
  
  
  
  
  
  
  /** 
   * 获取 layui 常见方法，以便用于组件单独版
   */
  
  lay.layui = layui;
  lay.getPath = layui.cache.dir; //获取当前 JS 所在目录
  lay.stope = layui.stope; //中止冒泡
  lay.each = function(){ //遍历
    layui.each.apply(layui, arguments);
    return this;
  };
  
  
  
  
  
  //数字前置补零
  lay.digit = function(num, length, end){
    var str = '';
    num = String(num);
    length = length || 2;
    for(var i = num.length; i < length; i++){
      str += '0';
    }
    return num < Math.pow(10, length) ? str + (num|0) : num;
  };
  
  //创建元素
  lay.elem = function(elemName, attr){
    var elem = document.createElement(elemName);
    lay.each(attr || {}, function(key, value){
      elem.setAttribute(key, value);
    });
    return elem;
  };

  //当前页面是否存在滚动条
  lay.hasScrollbar = function(){
    return document.body.scrollHeight > (window.innerHeight || document.documentElement.clientHeight);
  };
  
  //元素定位
  lay.position = function(elem, elemView, obj){
    if(!elemView) return;
    obj = obj || {};
    
    //如果绑定的是 document 或 body 元素，则直接获取鼠标坐标
    if(elem === document || elem === lay('body')[0]){
      obj.clickType = 'right';
    }

    //绑定绑定元素的坐标
    var rect = obj.clickType === 'right' ? function(){
      var e = obj.e || window.event || {};
      return {
        left: e.clientX
        ,top: e.clientY
        ,right: e.clientX
        ,bottom: e.clientY
      }
    }() : elem.getBoundingClientRect()
    ,elemWidth = elemView.offsetWidth //控件的宽度
    ,elemHeight = elemView.offsetHeight //控件的高度
    
    //滚动条高度
    ,scrollArea = function(type){
      type = type ? 'scrollLeft' : 'scrollTop';
      return document.body[type] | document.documentElement[type];
    }
    
    //窗口宽高
    ,winArea = function(type){
      return document.documentElement[type ? 'clientWidth' : 'clientHeight']
    }, margin = 5, left = rect.left, top = rect.bottom;

    //判断右侧是否超出边界
    if(left + elemWidth + margin > winArea('width')){
      left = winArea('width') - elemWidth - margin; //如果超出右侧，则将面板向右靠齐
    }
    
    //判断底部和顶部是否超出边界
    if(top + elemHeight + margin > winArea()){
      //优先顶部是否有足够区域显示完全
      if(rect.top > elemHeight + margin){
        top = rect.top - elemHeight - margin*2; //顶部有足够的区域显示
      } else {
        //如果面板是鼠标右键弹出，且顶部没有足够区域显示，则将面板向底部靠齐
        if(obj.clickType === 'right'){
          top = winArea() - elemHeight - margin*2;
          if(top < 0) top = 0; //不能溢出窗口顶部
        }
      }
    }
    
    //定位类型
    var position = obj.position;
    if(position) elemView.style.position = position;
    
    //设置坐标
    elemView.style.left = left + (position === 'fixed' ? 0 : scrollArea(1)) + 'px';
    elemView.style.top = top + (position === 'fixed' ? 0 : scrollArea()) + 'px';

    //防止页面无滚动条时，又因为弹出面板而出现滚动条导致的坐标计算偏差
    if(!lay.hasScrollbar()){
      var rect1 = elemView.getBoundingClientRect();
      //如果弹出面板的溢出窗口底部，则表示将出现滚动条，此时需要重新计算坐标
      if(!obj.SYSTEM_RELOAD && (rect1.bottom + margin) > winArea()){
        obj.SYSTEM_RELOAD = true;
        setTimeout(function(){
          lay.position(elem, elemView, obj);
        }, 50);
      }
    }
  };
  
  //获取元素上的参数配置上
  lay.options = function(elem, attr){
    var othis = lay(elem)
    ,attrName = attr || 'lay-options';
    try {
      return new Function('return '+ (othis.attr(attrName) || '{}'))();
    } catch(ev) {
      hint.error('parseerror：'+ ev, 'error');
      return {};
    }
  };
  
  //元素是否属于顶级元素（document 或 body）
  lay.isTopElem = function(elem){
    var topElems = [document, lay('body')[0]]
    ,matched = false;
    lay.each(topElems, function(index, item){
      if(item === elem){
        return matched = true
      }
    });
    return matched;
  };
  
  //追加字符
  LAY.addStr = function(str, new_str){
    str = str.replace(/\s+/, ' ');
    new_str = new_str.replace(/\s+/, ' ').split(' ');
    lay.each(new_str, function(ii, item){
      if(!new RegExp('\\b'+ item + '\\b').test(str)){
        str = str + ' ' + item;
      }
    });
    return str.replace(/^\s|\s$/, '');
  };
  
  //移除值
  LAY.removeStr = function(str, new_str){
    str = str.replace(/\s+/, ' ');
    new_str = new_str.replace(/\s+/, ' ').split(' ');
    lay.each(new_str, function(ii, item){
      var exp = new RegExp('\\b'+ item + '\\b')
      if(exp.test(str)){
        str = str.replace(exp, '');
      }
    });
    return str.replace(/\s+/, ' ').replace(/^\s|\s$/, '');
  };
  
  //查找子元素
  LAY.prototype.find = function(selector){
    var that = this;
    var index = 0, arr = []
    ,isObject = typeof selector === 'object';
    
    this.each(function(i, item){
      var nativeDOM = isObject ? item.contains(selector) : item.querySelectorAll(selector || null);
      for(; index < nativeDOM.length; index++){
        arr.push(nativeDOM[index]);
      }
      that.shift();
    });
    
    if(!isObject){
      that.selector =  (that.selector ? that.selector + ' ' : '') + selector
    }
    
    lay.each(arr, function(i, item){
      that.push(item);
    });
    
    return that;
  };
  
  //DOM遍历
  LAY.prototype.each = function(fn){
    return lay.each.call(this, this, fn);
  };
  
  //添加css类
  LAY.prototype.addClass = function(className, type){
    return this.each(function(index, item){
      item.className = LAY[type ? 'removeStr' : 'addStr'](item.className, className)
    });
  };
  
  //移除 css 类
  LAY.prototype.removeClass = function(className){
    return this.addClass(className, true);
  };
  
  //是否包含 css 类
  LAY.prototype.hasClass = function(className){
    var has = false;
    this.each(function(index, item){
      if(new RegExp('\\b'+ className +'\\b').test(item.className)){
        has = true;
      }
    });
    return has;
  };
  
  //添加或获取 css style
  LAY.prototype.css = function(key, value){
    var that = this
    ,parseValue = function(v){
      return isNaN(v) ? v : (v +'px');
    };
    return (typeof key === 'string' && value === undefined) ? function(){
      if(that.length > 0) return that[0].style[key];
    }() : that.each(function(index, item){
      typeof key === 'object' ? lay.each(key, function(thisKey, thisValue){
        item.style[thisKey] = parseValue(thisValue);
      }) : item.style[key] = parseValue(value);
    });   
  };
  
  //添加或获取宽度
  LAY.prototype.width = function(value){
    var that = this;
    return value === undefined ? function(){
      if(that.length > 0) return that[0].offsetWidth; //此处还需做兼容
    }() : that.each(function(index, item){
      that.css('width', value);
    });   
  };
  
  //添加或获取高度
  LAY.prototype.height = function(value){
    var that = this;
    return value === undefined ? function(){
      if(that.length > 0) return that[0].offsetHeight; //此处还需做兼容
    }() : that.each(function(index, item){
      that.css('height', value);
    });   
  };
  
  //添加或获取属性
  LAY.prototype.attr = function(key, value){
    var that = this;
    return value === undefined ? function(){
      if(that.length > 0) return that[0].getAttribute(key);
    }() : that.each(function(index, item){
      item.setAttribute(key, value);
    });   
  };
  
  //移除属性
  LAY.prototype.removeAttr = function(key){
    return this.each(function(index, item){
      item.removeAttribute(key);
    });
  };
  
  //设置或获取 HTML 内容
  LAY.prototype.html = function(html){
    var that = this;
    return html === undefined ? function(){
      if(that.length > 0) return that[0].innerHTML;
    }() : this.each(function(index, item){
      item.innerHTML = html;
    });
  };
  
  //设置或获取值
  LAY.prototype.val = function(value){
    var that = this;
    return value === undefined ? function(){
      if(that.length > 0) return that[0].value;
    }() : this.each(function(index, item){
        item.value = value;
    });
  };
  
  //追加内容
  LAY.prototype.append = function(elem){
    return this.each(function(index, item){
      typeof elem === 'object' 
        ? item.appendChild(elem)
      :  item.innerHTML = item.innerHTML + elem;
    });
  };
  
  //移除内容
  LAY.prototype.remove = function(elem){
    return this.each(function(index, item){
      elem ? item.removeChild(elem) : item.parentNode.removeChild(item);
    });
  };
  
  //事件绑定
  LAY.prototype.on = function(eventName, fn){
    return this.each(function(index, item){
      item.attachEvent ? item.attachEvent('on' + eventName, function(e){
        e.target = e.srcElement;
        fn.call(item, e);
      }) : item.addEventListener(eventName, fn, false);
    });
  };
  
  //解除事件
  LAY.prototype.off = function(eventName, fn){
    return this.each(function(index, item){
      item.detachEvent 
        ? item.detachEvent('on'+ eventName, fn)  
      : item.removeEventListener(eventName, fn, false);
    });
  };
  
  //暴露 lay 到全局作用域
  window.lay = lay;
  
  //如果在 layui 体系中
  if(window.layui && layui.define){
    layui.define(function(exports){ //layui 加载
      exports(MOD_NAME, lay);
    });
  }
  
}(window, window.document);


/*! layDate 日期与时间控件 | MIT Licensed */

;!function(window, document){
  "use strict";

  var isLayui = window.layui && layui.define, ready = {
    getPath: (window.lay && lay.getPath) ? lay.getPath : ''
    
    //载入 CSS 依赖
    ,link: function(href, fn, cssname){
      
      //未设置路径，则不主动加载 css
      if(!laydate.path) return;
      
      //加载 css
      if(window.lay && lay.layui){
        lay.layui.link(laydate.path + href, fn, cssname);
      }
    }
  }
  
  //识别预先可能定义的指定全局对象
  ,GLOBAL = window.LAYUI_GLOBAL || {}
  
  //外部调用
  ,laydate = {
    v: '5.3.1' //layDate 版本号
    ,config: {} //全局配置项
    ,index: (window.laydate && window.laydate.v) ? 100000 : 0
    ,path: GLOBAL.laydate_dir || ready.getPath
    
    //设置全局项
    ,set: function(options){
      var that = this;
      that.config = lay.extend({}, that.config, options);
      return that;
    }
    
    //主体CSS等待事件
    ,ready: function(fn){
      var cssname = 'laydate', ver = ''
      ,path = (isLayui ? 'modules/laydate/' : 'theme/') + 'default/laydate.css?v='+ laydate.v + ver;
      isLayui ? layui.addcss(path, fn, cssname) : ready.link(path, fn, cssname);
      return this;
    }
  }
  
  //操作当前实例
  ,thisModule = function(){
    var that = this
    ,options = that.config
    ,id = options.id;
    
    thisModule.that[id] = that; //记录当前实例对象
    
    return {
      //提示框
      hint: function(content){
        that.hint.call(that, content);
      }
      ,config: that.config
    };
  }

  //字符常量
  ,MOD_NAME = 'laydate', ELEM = '.layui-laydate', THIS = 'layui-this', SHOW = 'layui-show', HIDE = 'layui-hide', DISABLED = 'laydate-disabled', LIMIT_YEAR = [100, 200000]
  
  ,ELEM_STATIC = 'layui-laydate-static', ELEM_LIST = 'layui-laydate-list', ELEM_SELECTED = 'laydate-selected', ELEM_HINT = 'layui-laydate-hint', ELEM_PREV = 'laydate-day-prev', ELEM_NEXT = 'laydate-day-next', ELEM_FOOTER = 'layui-laydate-footer', ELEM_CONFIRM = '.laydate-btns-confirm', ELEM_TIME_TEXT = 'laydate-time-text', ELEM_TIME_BTN = 'laydate-btns-time', ELEM_PREVIEW = 'layui-laydate-preview'
  
  //组件构造器
  ,Class = function(options){
    var that = this;
    that.index = ++laydate.index;
    that.config = lay.extend({}, that.config, laydate.config, options);
    
    //初始化 id 参数
    options = that.config;
    options.id = ('id' in options) ? options.id : that.index;
    
    //初始化
    laydate.ready(function(){
      that.init();
    });
  }
  
  //日期格式字符
  ,dateType = 'yyyy|y|MM|M|dd|d|HH|H|mm|m|ss|s';
  
  //将日期格式字符转换为数组
  thisModule.formatArr = function(format){
    return (format || '').match(new RegExp(dateType + '|.', 'g')) || []
  };
  
  /*
    组件操作
  */
  
  //是否闰年
  Class.isLeapYear = function(year){
    return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
  };
  
  //默认配置
  Class.prototype.config = {
    type: 'date' //控件类型，支持：year/month/date/time/datetime
    ,range: false //是否开启范围选择，即双控件
    ,format: 'yyyy-MM-dd' //默认日期格式
    ,value: null //默认日期，支持传入new Date()，或者符合format参数设定的日期格式字符
    ,isInitValue: true //用于控制是否自动向元素填充初始值（需配合 value 参数使用）
    ,min: '1900-1-1' //有效最小日期，年月日必须用“-”分割，时分秒必须用“:”分割。注意：它并不是遵循 format 设定的格式。
    ,max: '2099-12-31' //有效最大日期，同上
    ,trigger: 'click' //呼出控件的事件
    ,show: false //是否直接显示，如果设置 true，则默认直接显示控件
    ,showBottom: true //是否显示底部栏
    ,isPreview: true //是否显示值预览
    ,btns: ['clear', 'now', 'confirm'] //右下角显示的按钮，会按照数组顺序排列
    ,lang: 'cn' //语言，只支持cn/en，即中文和英文
    ,theme: 'default' //主题
    ,position: null //控件定位方式定位, 默认absolute，支持：fixed/absolute/static
    ,calendar: false //是否开启公历重要节日，仅支持中文版
    ,mark: {} //日期备注，如重要事件或活动标记
    ,zIndex: null //控件层叠顺序
    ,done: null //控件选择完毕后的回调，点击清空/现在/确定也均会触发
    ,change: null //日期时间改变后的回调
  };
  
  //多语言
  Class.prototype.lang = function(){
    var that = this
    ,options = that.config
    ,text = {
      cn: {
        weeks: ['日', '一', '二', '三', '四', '五', '六']
        ,time: ['时', '分', '秒']
        ,timeTips: '选择时间'
        ,startTime: '开始时间'
        ,endTime: '结束时间'
        ,dateTips: '返回日期'
        ,month: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二']
        ,tools: {
          confirm: '确定'
          ,clear: '清空'
          ,now: '现在'
        }
        ,timeout: '结束时间不能早于开始时间<br>请重新选择'
        ,invalidDate: '不在有效日期或时间范围内'
        ,formatError: ['日期格式不合法<br>必须遵循下述格式：<br>', '<br>已为你重置']
        ,preview: '当前选中的结果'
      }
      ,en: {
        weeks: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa']
        ,time: ['Hours', 'Minutes', 'Seconds']
        ,timeTips: 'Select Time'
        ,startTime: 'Start Time'
        ,endTime: 'End Time'
        ,dateTips: 'Select Date'
        ,month: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        ,tools: {
          confirm: 'Confirm'
          ,clear: 'Clear'
          ,now: 'Now'
        }
        ,timeout: 'End time cannot be less than start Time<br>Please re-select'
        ,invalidDate: 'Invalid date'
        ,formatError: ['The date format error<br>Must be followed：<br>', '<br>It has been reset']
        ,preview: 'The selected result'
      }
    };
    return text[options.lang] || text['cn'];
  };
  
  //初始准备
  Class.prototype.init = function(){
    var that = this
    ,options = that.config
    ,isStatic = options.position === 'static'
    ,format = {
      year: 'yyyy'
      ,month: 'yyyy-MM'
      ,date: 'yyyy-MM-dd'
      ,time: 'HH:mm:ss'
      ,datetime: 'yyyy-MM-dd HH:mm:ss'
    };
    
    options.elem = lay(options.elem);
    options.eventElem = lay(options.eventElem);
    
    if(!options.elem[0]) return;
    
    //日期范围分隔符
    that.rangeStr =  options.range ? (typeof options.range === 'string' ? options.range : '-') : '';
    
    //若 range 参数为数组，则表示为开始日期和结束日期的 input 对象
    if(options.range && options.range.constructor === Array){
      that.rangeElem = [
        lay(options.range[0]),
        lay(options.range[1])
      ];
    }
    
    //若 type 设置非法，则初始化为 date 类型
    if(!format[options.type]){
      window.console && console.error && console.error('laydate type error:\''+ options.type + '\' is not supported')
      options.type = 'date';
    }
    
    //根据不同 type，初始化默认 format
    if(options.format === format.date){
      options.format = format[options.type] || format.date;
    }
    
    //将日期格式转化成数组
    that.format = thisModule.formatArr(options.format);
    
    //生成正则表达式
    that.EXP_IF = ''; 
    that.EXP_SPLIT = ''; 
    lay.each(that.format, function(i, item){
      var EXP =  new RegExp(dateType).test(item) 
        ? '\\d{'+ function(){
          if(new RegExp(dateType).test(that.format[i === 0 ? i + 1 : i - 1]||'')){
            if(/^yyyy|y$/.test(item)) return 4;
            return item.length;
          }
          if(/^yyyy$/.test(item)) return '1,4';
          if(/^y$/.test(item)) return '1,308';
          return '1,2';
        }() +'}' 
      : '\\' + item;
      that.EXP_IF = that.EXP_IF + EXP;
      that.EXP_SPLIT = that.EXP_SPLIT + '(' + EXP + ')';
    });
    //验证日期格式正则
    that.EXP_IF_ONE = new RegExp('^'+ that.EXP_IF +'$'); //验证单个日期格式
    that.EXP_IF = new RegExp('^'+ (
      options.range ? 
        that.EXP_IF + '\\s\\'+ that.rangeStr + '\\s' + that.EXP_IF
      : that.EXP_IF
    ) +'$');
    that.EXP_SPLIT = new RegExp('^'+ that.EXP_SPLIT +'$', '');
    
    //如果不是input|textarea元素，则默认采用click事件
    if(!that.isInput(options.elem[0])){
      if(options.trigger === 'focus'){
        options.trigger = 'click';
      }
    }
    
    //设置唯一KEY
    if(!options.elem.attr('lay-key')){
      options.elem.attr('lay-key', that.index);
      options.eventElem.attr('lay-key', that.index);
    }
    
    //记录重要日期
    options.mark = lay.extend({}, (options.calendar && options.lang === 'cn') ? {
      '0-1-1': '元旦'
      ,'0-2-14': '情人'
      ,'0-3-8': '妇女'
      ,'0-3-12': '植树'
      ,'0-4-1': '愚人'
      ,'0-5-1': '劳动'
      ,'0-5-4': '青年'
      ,'0-6-1': '儿童'
      ,'0-9-10': '教师'
      ,'0-9-18': '国耻'
      ,'0-10-1': '国庆'
      ,'0-12-25': '圣诞'
    } : {}, options.mark);
    
    //获取限制内日期
    lay.each(['min', 'max'], function(i, item){
      var ymd = [], hms = [];
      if(typeof options[item] === 'number'){ //如果为数字
        var day = options[item]
        ,time = new Date().getTime()
        ,STAMP = 86400000 //代表一天的毫秒数
        ,thisDate = new Date(
          day ? (
            day < STAMP ? time + day*STAMP : day //如果数字小于一天的毫秒数，则数字为天数，否则为毫秒数
          ) : time
        );
        ymd = [thisDate.getFullYear(), thisDate.getMonth() + 1, thisDate.getDate()];
        day < STAMP || (hms = [thisDate.getHours(), thisDate.getMinutes(), thisDate.getSeconds()]);
      } else {
        ymd = (options[item].match(/\d+-\d+-\d+/) || [''])[0].split('-');
        hms = (options[item].match(/\d+:\d+:\d+/) || [''])[0].split(':');
      }
      options[item] = {
        year: ymd[0] | 0 || new Date().getFullYear()
        ,month: ymd[1] ? (ymd[1] | 0) - 1 : new Date().getMonth()
        ,date: ymd[2] | 0 || new Date().getDate()
        ,hours: hms[0] | 0
        ,minutes: hms[1] | 0
        ,seconds: hms[2] | 0
      };
    });
    
    that.elemID = 'layui-laydate'+ options.elem.attr('lay-key');
    
    if(options.show || isStatic) that.render();
    isStatic || that.events();
    
    //默认赋值
    if(options.value && options.isInitValue){
      if(options.value.constructor === Date){
        that.setValue(that.parse(0, that.systemDate(options.value))); 
      } else {
        that.setValue(options.value); 
      }
    }
  };
  
  //控件主体渲染
  Class.prototype.render = function(){
    var that = this
    ,options = that.config
    ,lang = that.lang()
    ,isStatic = options.position === 'static'
    
    //主面板
    ,elem = that.elem = lay.elem('div', {
      id: that.elemID
      ,'class': [
        'layui-laydate'
        ,options.range ? ' layui-laydate-range' : ''
        ,isStatic ? (' '+ ELEM_STATIC) : ''
        ,options.theme && options.theme !== 'default' && !/^#/.test(options.theme) ? (' laydate-theme-' + options.theme) : ''
      ].join('')
    })
    
    //主区域
    ,elemMain = that.elemMain = []
    ,elemHeader = that.elemHeader = []
    ,elemCont = that.elemCont = []
    ,elemTable = that.table = []

    //底部区域
    ,divFooter = that.footer = lay.elem('div', {
      'class': ELEM_FOOTER
    });
    
    if(options.zIndex) elem.style.zIndex = options.zIndex;
    
    //单双日历区域
    lay.each(new Array(2), function(i){
      if(!options.range && i > 0){
        return true;
      }

      //头部区域
      var divHeader = lay.elem('div', {
        'class': 'layui-laydate-header'
      })
      
      //左右切换
      ,headerChild = [function(){ //上一年
        var elem = lay.elem('i', {
          'class': 'layui-icon laydate-icon laydate-prev-y'
        });
        elem.innerHTML = '&#xe65a;';
        return elem;
      }(), function(){ //上一月
        var elem = lay.elem('i', {
          'class': 'layui-icon laydate-icon laydate-prev-m'
        });
        elem.innerHTML = '&#xe603;';
        return elem;
      }(), function(){ //年月选择
        var elem = lay.elem('div', {
          'class': 'laydate-set-ym'
        }), spanY = lay.elem('span'), spanM = lay.elem('span');
        elem.appendChild(spanY);
        elem.appendChild(spanM);
        return elem;
      }(), function(){ //下一月
        var elem = lay.elem('i', {
          'class': 'layui-icon laydate-icon laydate-next-m'
        });
        elem.innerHTML = '&#xe602;';
        return elem;
      }(), function(){ //下一年
        var elem = lay.elem('i', {
          'class': 'layui-icon laydate-icon laydate-next-y'
        });
        elem.innerHTML = '&#xe65b;';
        return elem;
      }()]
      
      //日历内容区域
      ,divContent = lay.elem('div', {
        'class': 'layui-laydate-content'
      })
      ,table = lay.elem('table')
      ,thead = lay.elem('thead'), theadTr = lay.elem('tr');
      
      //生成年月选择
      lay.each(headerChild, function(i, item){
        divHeader.appendChild(item);
      });
      
       //生成表格
      thead.appendChild(theadTr);
      lay.each(new Array(6), function(i){ //表体
        var tr = table.insertRow(0);
        lay.each(new Array(7), function(j){
          if(i === 0){
            var th = lay.elem('th');
            th.innerHTML = lang.weeks[j];
            theadTr.appendChild(th);
          }
          tr.insertCell(j);
        });
      });
      table.insertBefore(thead, table.children[0]); //表头
      divContent.appendChild(table);
      
      elemMain[i] = lay.elem('div', {
        'class': 'layui-laydate-main laydate-main-list-'+ i
      });
      
      elemMain[i].appendChild(divHeader);
      elemMain[i].appendChild(divContent);
      
      elemHeader.push(headerChild);
      elemCont.push(divContent);
      elemTable.push(table);
    });
    
    //生成底部栏
    lay(divFooter).html(function(){
      var html = [], btns = [];
      if(options.type === 'datetime'){
        html.push('<span lay-type="datetime" class="'+ ELEM_TIME_BTN +'">'+ lang.timeTips +'</span>');
      }
      if(!(!options.range && options.type === 'datetime')){
        html.push('<span class="'+ ELEM_PREVIEW +'" title="'+ lang.preview +'"></span>')
      }
      
      lay.each(options.btns, function(i, item){
        var title = lang.tools[item] || 'btn';
        if(options.range && item === 'now') return;
        if(isStatic && item === 'clear') title = options.lang === 'cn' ? '重置' : 'Reset';
        btns.push('<span lay-type="'+ item +'" class="laydate-btns-'+ item +'">'+ title +'</span>');
      });
      html.push('<div class="laydate-footer-btns">'+ btns.join('') +'</div>');
      return html.join('');
    }());
    
    //插入到主区域
    lay.each(elemMain, function(i, main){
      elem.appendChild(main);
    });
    options.showBottom && elem.appendChild(divFooter);
    
    //生成自定义主题
    if(/^#/.test(options.theme)){
      var style = lay.elem('style')
      ,styleText = [
        '#{{id}} .layui-laydate-header{background-color:{{theme}};}'
        ,'#{{id}} .layui-this{background-color:{{theme}} !important;}'
      ].join('').replace(/{{id}}/g, that.elemID).replace(/{{theme}}/g, options.theme);
      
      if('styleSheet' in style){
        style.setAttribute('type', 'text/css');
        style.styleSheet.cssText = styleText;
      } else {
        style.innerHTML = styleText;
      }
      
      lay(elem).addClass('laydate-theme-molv');
      elem.appendChild(style);
    }
    
    //记录当前执行的实例索引
    laydate.thisId = options.id;
    
    //移除上一个控件
    that.remove(Class.thisElemDate); 
    
    //如果是静态定位，则插入到指定的容器中，否则，插入到body
    isStatic ? options.elem.append(elem) : (
      document.body.appendChild(elem)
      ,that.position() //定位
    );
    
    that.checkDate().calendar(null, 0, 'init'); //初始校验
    that.changeEvent(); //日期切换
    
    Class.thisElemDate = that.elemID;

    typeof options.ready === 'function' && options.ready(lay.extend({}, options.dateTime, {
      month: options.dateTime.month + 1
    }));
    
    that.preview();
  };
  
  //控件移除
  Class.prototype.remove = function(prev){
    var that = this
    ,options = that.config
    ,elem = lay('#'+ (prev || that.elemID));
    if(!elem[0]) return that;
    
    if(!elem.hasClass(ELEM_STATIC)){
      that.checkDate(function(){
        elem.remove();
        //delete options.dateTime;
        //delete that.endDate;
      });
    }
    return that;
  };
  
  //定位算法
  Class.prototype.position = function(){
    var that = this
    ,options = that.config;
    lay.position(that.bindElem || options.elem[0], that.elem, {
      position: options.position
    });
    return that;
  };
  
  //提示
  Class.prototype.hint = function(content){
    var that = this
    ,options = that.config
    ,div = lay.elem('div', {
      'class': ELEM_HINT
    });
    
    if(!that.elem) return;
    
    div.innerHTML = content || '';
    lay(that.elem).find('.'+ ELEM_HINT).remove();
    that.elem.appendChild(div);

    clearTimeout(that.hinTimer);
    that.hinTimer = setTimeout(function(){
      lay(that.elem).find('.'+ ELEM_HINT).remove();
    }, 3000);
  };
  
  //获取递增/减后的年月
  Class.prototype.getAsYM = function(Y, M, type){
    type ? M-- : M++;
    if(M < 0){
      M = 11;
      Y--;
    }
    if(M > 11){
      M = 0;
      Y++;
    }
    return [Y, M];
  };
  
  //系统日期
  Class.prototype.systemDate = function(newDate){
    var thisDate = newDate || new Date();
    return {
      year: thisDate.getFullYear() //年
      ,month: thisDate.getMonth() //月
      ,date: thisDate.getDate() //日
      ,hours: newDate ? newDate.getHours() : 0 //时
      ,minutes: newDate ? newDate.getMinutes() : 0 //分
      ,seconds: newDate ? newDate.getSeconds() : 0 //秒
    }
  };
  
  //日期校验
  Class.prototype.checkDate = function(fn){
    var that = this
    ,thisDate = new Date()
    ,options = that.config
    ,lang = that.lang()
    ,dateTime = options.dateTime = options.dateTime || that.systemDate()
    ,thisMaxDate, error
    
    ,elem = that.bindElem || options.elem[0]
    ,valType = that.isInput(elem) ? 'val' : 'html'
    ,value = function(){
      //如果传入了开始和结束日期的 input 对象，则将其拼接为日期范围字符
      if(that.rangeElem){
        var vals = [that.rangeElem[0].val(), that.rangeElem[1].val()];
        if(vals[0] && vals[1]){
          return vals.join(' ' + that.rangeStr + ' ');
        }
      }
      return that.isInput(elem) ? elem.value : (options.position === 'static' ? '' : lay(elem).attr('lay-date'));
    }()
    
    //校验日期有效数字
    ,checkValid = function(dateTime){
      if(dateTime.year > LIMIT_YEAR[1]) dateTime.year = LIMIT_YEAR[1], error = true; //不能超过20万年
      if(dateTime.month > 11) dateTime.month = 11, error = true;
      if(dateTime.hours > 23) dateTime.hours = 0, error = true;
      if(dateTime.minutes > 59) dateTime.minutes = 0, dateTime.hours++, error = true;
      if(dateTime.seconds > 59) dateTime.seconds = 0, dateTime.minutes++, error = true;
      
      //计算当前月的最后一天
      thisMaxDate = laydate.getEndDate(dateTime.month + 1, dateTime.year);
      if(dateTime.date > thisMaxDate) dateTime.date = thisMaxDate, error = true;
    }
    
    //获得初始化日期值
    ,initDate = function(dateTime, value, index){
      var startEnd = ['startTime', 'endTime'];
      value = (value.match(that.EXP_SPLIT) || []).slice(1);
      index = index || 0;
      
      if(options.range){
        that[startEnd[index]] = that[startEnd[index]] || {};
      }
      lay.each(that.format, function(i, item){
        var thisv = parseFloat(value[i]);
        if(value[i].length < item.length) error = true;
        if(/yyyy|y/.test(item)){ //年
          if(thisv < LIMIT_YEAR[0]) thisv = LIMIT_YEAR[0], error = true; //年不能低于100年
          dateTime.year = thisv;
        } else if(/MM|M/.test(item)){ //月
          if(thisv < 1) thisv = 1, error = true;
          dateTime.month = thisv - 1;
        } else if(/dd|d/.test(item)){ //日
          if(thisv < 1) thisv = 1, error = true;
          dateTime.date = thisv;
        } else if(/HH|H/.test(item)){ //时
          if(thisv < 1) thisv = 0, error = true;
          dateTime.hours = thisv;
          options.range && (that[startEnd[index]].hours = thisv);
        } else if(/mm|m/.test(item)){ //分
          if(thisv < 1) thisv = 0, error = true;
          dateTime.minutes = thisv;
          options.range && (that[startEnd[index]].minutes = thisv);
        } else if(/ss|s/.test(item)){ //秒
          if(thisv < 1) thisv = 0, error = true;
          dateTime.seconds = thisv;
          options.range && (that[startEnd[index]].seconds = thisv);
        }
      });
      checkValid(dateTime);
    };
    
    if(fn === 'limit') return checkValid(dateTime), that;
    
    value = value || options.value;
    if(typeof value === 'string'){
      value = value.replace(/\s+/g, ' ').replace(/^\s|\s$/g, '');
    }

    //如果开启范围，则计算结束日期
    var getEndDate = function(){
      if(options.range){
        that.endDate = that.endDate || lay.extend({}, options.dateTime, function(){
          var obj = {}
          ,dateTime = options.dateTime
          ,EYM = that.getAsYM(dateTime.year, dateTime.month);
          
          //初始右侧面板的年月
          if(options.type === 'year'){
            obj.year = dateTime.year + 1;
          } else if(options.type !== 'time'){
            obj.year = EYM[0];
            obj.month = EYM[1];
          }
          
          //初始右侧面板的时间
          if(options.type === 'datetime' || options.type === 'time'){
            obj.hours = 23;
            obj.minutes = obj.seconds = 59;
          }
          
          return obj;
        }());
      }
    };
    getEndDate();
    
    if(typeof value === 'string' && value){
      if(that.EXP_IF.test(value)){ //校验日期格式
        if(options.range){
          value = value.split(' '+ that.rangeStr +' ');
          lay.each([options.dateTime, that.endDate], function(i, item){
            initDate(item, value[i], i);
          });
        } else {
          initDate(dateTime, value);
        }
      } else {
        //格式不合法
        that.hint(lang.formatError[0] + (
          options.range ? (options.format + ' '+ that.rangeStr +' ' + options.format) : options.format
        ) + lang.formatError[1]);
        error = true;
      }
    } else if(value && value.constructor === Date){ //如果值为日期对象时
      options.dateTime = that.systemDate(value);
    } else {
      //重置开始日期
      options.dateTime = that.systemDate();
      delete that.startTime;
      
      //重置结束日期
      delete that.endDate; //删除原有的结束日期
      getEndDate(); //并重新获得新的结束日期
      delete that.endTime;
    }
    
    //从日期范围表单中获取初始值
    (function(){
      if(that.rangeElem){
        var vals = [that.rangeElem[0].val(), that.rangeElem[1].val()]
        ,arrDate = [options.dateTime, that.endDate];
        lay.each(vals, function(_i, _v){
          if(that.EXP_IF_ONE.test(_v)){ //校验日期格式
            initDate(arrDate[_i], _v, _i);
          }
        });
      }
    })();
    
    //校验日期有效数字
    checkValid(dateTime);
    if(options.range) checkValid(that.endDate);
    
    //如果初始值格式错误，则纠正初始值
    if(error && value){
      that.setValue(
        options.range ? (that.endDate ? that.parse() : '') : that.parse()
      );
    }
    
    //如果当前日期不在设定的最大小日期区间，则自动纠正在可选区域
    var getDateTime = function(obj){
      return that.newDate(obj).getTime();
    };
    
    //校验主面板是否在可选日期区间
    if(getDateTime(dateTime) > getDateTime(options.max) || getDateTime(dateTime) < getDateTime(options.min)){
      dateTime = options.dateTime = lay.extend({}, options.min);
    }
    
    //校验右侧面板是否在可选日期区间
    if(options.range){
      if(getDateTime(that.endDate) < getDateTime(options.min) || getDateTime(that.endDate) > getDateTime(options.max)){
        that.endDate = lay.extend({}, options.max);
      }
    }
    
    fn && fn();
    return that;
  };
  
  //公历重要日期与自定义备注
  Class.prototype.mark = function(td, YMD){
    var that = this
    ,mark, options = that.config;
    lay.each(options.mark, function(key, title){
      var keys = key.split('-');
      if((keys[0] == YMD[0] || keys[0] == 0) //每年的每月
      && (keys[1] == YMD[1] || keys[1] == 0) //每月的每日
      && keys[2] == YMD[2]){ //特定日
        mark = title || YMD[2];
      }
    });
    mark && td.html('<span class="laydate-day-mark">'+ mark +'</span>');
    
    return that;
  };
  
  //无效日期范围的标记
  Class.prototype.limit = function(elem, date, index, time){
    var that = this
    ,options = that.config, timestrap = {}
    ,dateTime = options[index > 41 ? 'endDate' : 'dateTime']
    ,isOut, thisDateTime = lay.extend({}, dateTime, date || {});
    
    lay.each({
      now: thisDateTime
      ,min: options.min
      ,max: options.max
    }, function(key, item){
      timestrap[key] = that.newDate(lay.extend({
        year: item.year
        ,month: item.month
        ,date: item.date
      }, function(){
        var hms = {};
        lay.each(time, function(i, keys){
          hms[keys] = item[keys];
        });
        return hms;
      }())).getTime();  //time：是否比较时分秒
    });
    
    isOut = timestrap.now < timestrap.min || timestrap.now > timestrap.max;
    elem && elem[isOut ? 'addClass' : 'removeClass'](DISABLED);
    return isOut;
  };
  
  //当前日期对象
  Class.prototype.thisDateTime = function(index){
    var that = this
    ,options = that.config;
    return index ? that.endDate: options.dateTime;
  };
  
  //日历表
  Class.prototype.calendar = function(value, index, type){
    var that = this
    ,options = that.config
    ,index = index ? 1 : 0
    ,dateTime = value || that.thisDateTime(index)
    ,thisDate = new Date(), startWeek, prevMaxDate, thisMaxDate
    ,lang = that.lang()
    
    ,isAlone = options.type !== 'date' && options.type !== 'datetime'
    ,tds = lay(that.table[index]).find('td')
    ,elemYM = lay(that.elemHeader[index][2]).find('span');
    
    if(dateTime.year < LIMIT_YEAR[0]) dateTime.year = LIMIT_YEAR[0], that.hint(lang.invalidDate);
    if(dateTime.year > LIMIT_YEAR[1]) dateTime.year = LIMIT_YEAR[1], that.hint(lang.invalidDate);
    
    //记录初始值
    if(!that.firstDate){
      that.firstDate = lay.extend({}, dateTime);
    }
    
    //计算当前月第一天的星期
    thisDate.setFullYear(dateTime.year, dateTime.month, 1);
    startWeek = thisDate.getDay();
    
    prevMaxDate = laydate.getEndDate(dateTime.month || 12, dateTime.year); //计算上个月的最后一天
    thisMaxDate = laydate.getEndDate(dateTime.month + 1, dateTime.year); //计算当前月的最后一天
    
    //赋值日
    lay.each(tds, function(index_, item){
      var YMD = [dateTime.year, dateTime.month], st = 0;
      item = lay(item);
      item.removeAttr('class');
      if(index_ < startWeek){
        st = prevMaxDate - startWeek + index_;
        item.addClass('laydate-day-prev');
        YMD = that.getAsYM(dateTime.year, dateTime.month, 'sub');
      } else if(index_ >= startWeek && index_ < thisMaxDate + startWeek){
        st = index_ - startWeek;
        st + 1 === dateTime.date && item.addClass(THIS);
      } else {
        st = index_ - thisMaxDate - startWeek;
        item.addClass('laydate-day-next');
        YMD = that.getAsYM(dateTime.year, dateTime.month);
      }
      YMD[1]++;
      YMD[2] = st + 1;
      item.attr('lay-ymd', YMD.join('-')).html(YMD[2]);
      that.mark(item, YMD).limit(item, {
        year: YMD[0]
        ,month: YMD[1] - 1
        ,date: YMD[2]
      }, index_);
    });  
    
    //同步头部年月
    lay(elemYM[0]).attr('lay-ym', dateTime.year + '-' + (dateTime.month + 1));
    lay(elemYM[1]).attr('lay-ym', dateTime.year + '-' + (dateTime.month + 1));
    
    if(options.lang === 'cn'){
      lay(elemYM[0]).attr('lay-type', 'year').html(dateTime.year + ' 年')
      lay(elemYM[1]).attr('lay-type', 'month').html((dateTime.month + 1) + ' 月');
    } else {
      lay(elemYM[0]).attr('lay-type', 'month').html(lang.month[dateTime.month]);
      lay(elemYM[1]).attr('lay-type', 'year').html(dateTime.year);
    }

    //初始默认选择器
    if(isAlone){ //年、月等独立选择器
      if(options.range){
        if(value){
          that.listYM = [
            [options.dateTime.year, options.dateTime.month + 1]
            ,[that.endDate.year, that.endDate.month + 1]
          ];  
          that.list(options.type, 0).list(options.type, 1);
          
          //同步按钮可点状态
          options.type === 'time' ? that.setBtnStatus('时间'
            ,lay.extend({}, that.systemDate(), that.startTime)
            ,lay.extend({}, that.systemDate(), that.endTime)
          ) : that.setBtnStatus(true);
        }        
      } else {
        that.listYM = [[dateTime.year, dateTime.month + 1]];
        that.list(options.type, 0);
      }
    }
    
    //初始赋值双日历
    if(options.range && type === 'init' && !value){
      //执行渲染第二个日历
      that.calendar(that.endDate, 1);
    }
    
    //通过检测当前有效日期，来设定确定按钮是否可点
    if(!options.range) that.limit(lay(that.footer).find(ELEM_CONFIRM), null, 0, ['hours', 'minutes', 'seconds']);
    
    //同步按钮可点状态
    that.setBtnStatus();
    
    return that;
  };

  //生成年月时分秒列表
  Class.prototype.list = function(type, index){
    var that = this
    ,options = that.config
    ,dateTime = options.dateTime
    ,lang = that.lang()
    ,isAlone = options.range && options.type !== 'date' && options.type !== 'datetime' //独立范围选择器
    
    ,ul = lay.elem('ul', {
      'class': ELEM_LIST + ' ' + ({
        year: 'laydate-year-list'
        ,month: 'laydate-month-list'
        ,time: 'laydate-time-list'
      })[type]
    })
    ,elemHeader = that.elemHeader[index]
    ,elemYM = lay(elemHeader[2]).find('span')
    ,elemCont = that.elemCont[index || 0]
    ,haveList = lay(elemCont).find('.'+ ELEM_LIST)[0]
    ,isCN = options.lang === 'cn'
    ,text = isCN ? '年' : ''
   
    ,listYM = that.listYM[index] || {}
    ,hms = ['hours', 'minutes', 'seconds']
    ,startEnd = ['startTime', 'endTime'][index];

    if(listYM[0] < 1) listYM[0] = 1;
    
    //生成年列表
    if(type === 'year'){
      var yearNum, startY = yearNum = listYM[0] - 7;
      if(startY < 1) startY = yearNum = 1;
      lay.each(new Array(15), function(i){
        var li = lay.elem('li', {
          'lay-ym': yearNum
        }), ymd = {year: yearNum};
        yearNum == listYM[0] && lay(li).addClass(THIS);
        li.innerHTML = yearNum + text;
        ul.appendChild(li);
        if(yearNum < that.firstDate.year){
          ymd.month = options.min.month;
          ymd.date = options.min.date;
        } else if(yearNum >= that.firstDate.year){
          ymd.month = options.max.month;
          ymd.date = options.max.date;
        }
        that.limit(lay(li), ymd, index);
        yearNum++;
      });
      lay(elemYM[isCN ? 0 : 1]).attr('lay-ym', (yearNum - 8) + '-' + listYM[1])
      .html((startY + text) + ' - ' + (yearNum - 1 + text));
    }
    
    //生成月列表
    else if(type === 'month'){
      lay.each(new Array(12), function(i){
        var li = lay.elem('li', {
          'lay-ym': i
        }), ymd = {year: listYM[0], month: i};
        i + 1 == listYM[1] && lay(li).addClass(THIS);
        li.innerHTML = lang.month[i] + (isCN ? '月' : '');
        ul.appendChild(li);
        if(listYM[0] < that.firstDate.year){
          ymd.date = options.min.date;
        } else if(listYM[0] >= that.firstDate.year){
          ymd.date = options.max.date;
        }
        that.limit(lay(li), ymd, index);
      });
      lay(elemYM[isCN ? 0 : 1]).attr('lay-ym', listYM[0] + '-' + listYM[1])
      .html(listYM[0] + text);
    } 
    
    //生成时间列表
    else if(type === 'time'){
      //检测时分秒状态是否在有效日期时间范围内
      var setTimeStatus = function(){
        lay(ul).find('ol').each(function(i, ol){
          lay(ol).find('li').each(function(ii, li){
            that.limit(lay(li), [{
              hours: ii
            }, {
              hours: that[startEnd].hours
              ,minutes: ii
            }, {
              hours: that[startEnd].hours
              ,minutes: that[startEnd].minutes
              ,seconds: ii
            }][i], index, [['hours'], ['hours', 'minutes'], ['hours', 'minutes', 'seconds']][i]);
          });
        });
        if(!options.range) that.limit(lay(that.footer).find(ELEM_CONFIRM), that[startEnd], 0, ['hours', 'minutes', 'seconds']);
      };
      
      //初始化时间对象
      if(options.range){
        if(!that[startEnd]){
          that[startEnd] = startEnd === 'startTime' ? dateTime : that.endDate;
        }
      } else {
        that[startEnd] = dateTime;
      }
      
      //生成时分秒
      lay.each([24, 60, 60], function(i, item){
        var li = lay.elem('li'), childUL = ['<p>'+ lang.time[i] +'</p><ol>'];
        lay.each(new Array(item), function(ii){
          childUL.push('<li'+ (that[startEnd][hms[i]] === ii ? ' class="'+ THIS +'"' : '') +'>'+ lay.digit(ii, 2) +'</li>');
        });
        li.innerHTML = childUL.join('') + '</ol>';
        ul.appendChild(li);
      });
      setTimeStatus();
    }
    
    //插入容器
    if(haveList) elemCont.removeChild(haveList);
    elemCont.appendChild(ul);
    
    //年月面板 - 选择事件
    if(type === 'year' || type === 'month'){      
      //显示切换箭头
      lay(that.elemMain[index]).addClass('laydate-ym-show');
      
      //选中
      lay(ul).find('li').on('click', function(){
        var ym = lay(this).attr('lay-ym') | 0;
        if(lay(this).hasClass(DISABLED)) return;
        
        if(index === 0){
          dateTime[type] = ym;
          that.limit(lay(that.footer).find(ELEM_CONFIRM), null, 0);
        } else { //范围选择
          that.endDate[type] = ym;
        }
        
        //当为年选择器或者年月选择器
        var isYearOrMonth = options.type === 'year' || options.type === 'month';
        if(isYearOrMonth){
          lay(ul).find('.'+ THIS).removeClass(THIS);
          lay(this).addClass(THIS);
          
          //如果为年月选择器，点击了年列表，则切换到月选择器
          if(options.type === 'month' && type === 'year'){
            that.listYM[index][0] = ym;
            isAlone && ((index ? that.endDate : dateTime).year = ym);
            that.list('month', index);
          }
        } else {
          that.checkDate('limit').calendar(null, index);
          that.closeList();
        }

        that.setBtnStatus(); //同步按钮可点状态
        
        //若为月选择器，只有当选择月份时才自动关闭；
        //若为年选择器，选择年份即自动关闭
        //且在范围未开启时
        if(!options.range){
          if((options.type === 'month' && type === 'month') || (options.type === 'year' && type === 'year')){
            that.setValue(that.parse()).remove().done();
          }
        }
        
        that.done(null, 'change');
        lay(that.footer).find('.'+ ELEM_TIME_BTN).removeClass(DISABLED);
      });
    } else { //时间选择面板 - 选择事件
      var span = lay.elem('span', {
        'class': ELEM_TIME_TEXT
      })
      
      //滚动条定位
      ,scroll = function(){
        lay(ul).find('ol').each(function(i){
          var ol = this
          ,li = lay(ol).find('li')
          ol.scrollTop = 30*(that[startEnd][hms[i]] - 2);
          if(ol.scrollTop <= 0){
            li.each(function(ii, item){
              if(!lay(this).hasClass(DISABLED)){
                ol.scrollTop = 30*(ii - 2);
                return true;
              }
            });
          }
        });
      }
      ,haveSpan = lay(elemHeader[2]).find('.'+ ELEM_TIME_TEXT);
      
      scroll();
      span.innerHTML = options.range ? [lang.startTime,lang.endTime][index] : lang.timeTips;
      lay(that.elemMain[index]).addClass('laydate-time-show');
      
      if(haveSpan[0]) haveSpan.remove();
      elemHeader[2].appendChild(span);

      lay(ul).find('ol').each(function(i){
        var ol = this;
        //选择时分秒
        lay(ol).find('li').on('click', function(){
          var value = this.innerHTML | 0;
          if(lay(this).hasClass(DISABLED)) return;
          
          if(options.range){
            that[startEnd][hms[i]]  = value;
          } else {
            dateTime[hms[i]] = value;
          }
          lay(ol).find('.'+ THIS).removeClass(THIS);
          lay(this).addClass(THIS);

          setTimeStatus();
          scroll();
          (that.endDate || options.type === 'time') && that.done(null, 'change');
          
          //同步按钮可点状态
          that.setBtnStatus();
        });
      });
    }
    
    return that;
  };
  
  //记录列表切换后的年月
  Class.prototype.listYM = [];
  
  //关闭列表
  Class.prototype.closeList = function(){
    var that = this
    ,options = that.config;
    
    lay.each(that.elemCont, function(index, item){
      lay(this).find('.'+ ELEM_LIST).remove();
      lay(that.elemMain[index]).removeClass('laydate-ym-show laydate-time-show');
    });
    lay(that.elem).find('.'+ ELEM_TIME_TEXT).remove();
  };
  
  //检测结束日期是否超出开始日期
  Class.prototype.setBtnStatus = function(tips, start, end){
    var that = this
    ,options = that.config
    ,lang = that.lang()
    ,isOut, elemBtn = lay(that.footer).find(ELEM_CONFIRM);
    if(options.range && options.type !== 'time'){
      start = start || options.dateTime;
      end = end || that.endDate;
      isOut = that.newDate(start).getTime() > that.newDate(end).getTime();
      
      //如果不在有效日期内，直接禁用按钮，否则比较开始和结束日期
      (that.limit(null, start) || that.limit(null, end)) 
        ? elemBtn.addClass(DISABLED)
      : elemBtn[isOut ? 'addClass' : 'removeClass'](DISABLED);
      
      //是否异常提示
      if(tips && isOut) that.hint(
        typeof tips === 'string' ? lang.timeout.replace(/日期/g, tips) : lang.timeout
      );
    }
  };
  
  //转义为规定格式的日期字符
  Class.prototype.parse = function(state, date){
    var that = this
    ,options = that.config
    ,dateTime = date || (state == 'end'
      ? lay.extend({}, that.endDate, that.endTime)
    : (options.range ? lay.extend({}, options.dateTime, that.startTime) : options.dateTime))
    ,format = laydate.parse(dateTime, that.format, 1);
    
    //返回日期范围字符
    if(options.range && state === undefined){
      return format + ' '+ that.rangeStr +' ' + that.parse('end');
    }
    
    return format;
  };
  
  //创建指定日期时间对象
  Class.prototype.newDate = function(dateTime){
    dateTime = dateTime || {};
    return new Date(
      dateTime.year || 1
      ,dateTime.month || 0
      ,dateTime.date || 1
      ,dateTime.hours || 0
      ,dateTime.minutes || 0
      ,dateTime.seconds || 0
    );
  };
  
  //赋值
  Class.prototype.setValue = function(value){
    var that = this
    ,options = that.config
    ,elem = that.bindElem || options.elem[0];
    
    //静态展现则不作默认赋值
    if(options.position === 'static') return that;
    
    value = value || '';
    
    //绑定的元素是否为 input
    if(that.isInput(elem)){
      lay(elem).val(value);
    } else {
      //如果 range 传入了开始和结束的 input 对象，则分别对其赋值
      if(that.rangeElem){
        that.rangeElem[0].val(value ? that.parse('start') : '');
        that.rangeElem[1].val(value ? that.parse('end') : '');
      } else {
        if(lay(elem).find('*').length === 0){
          lay(elem).html(value);
        }
        lay(elem).attr('lay-date', value);
      }
    }
    
    return that;
  };
  
  //预览
  Class.prototype.preview = function(){
    var that = this
    ,options = that.config;
    
    if(!options.isPreview) return;
    
    var elemPreview =  lay(that.elem).find('.'+ ELEM_PREVIEW)
    ,value = options.range ? (that.endDate ? that.parse() : '') : that.parse();
    
    //显示预览
    elemPreview.html(value).css({
      'color': '#08c'
      ,'font-size': '14px;'
    });
    setTimeout(function(){
      elemPreview.css({
        'color': '#666'
        ,'font-size': '12px;'
      });
    }, 300);
  };
  
  //执行 done/change 回调
  Class.prototype.done = function(param, type){
    var that = this
    ,options = that.config
    ,start = lay.extend({}, lay.extend(options.dateTime, that.startTime))
    ,end = lay.extend({}, lay.extend(that.endDate, that.endTime))
    
    lay.each([start, end], function(i, item){
      if(!('month' in item)) return;
      lay.extend(item, {
        month: item.month + 1
      });
    });
    
    that.preview();
    
    param = param || [that.parse(), start, end];
    typeof options[type || 'done'] === 'function' && options[type || 'done'].apply(options, param);
    
    return that;
  };
  
  //选择日期
  Class.prototype.choose = function(td, index){
    var that = this
    ,options = that.config
    ,dateTime = that.thisDateTime(index)

    ,tds = lay(that.elem).find('td')
    ,YMD = td.attr('lay-ymd').split('-');
    
    YMD = {
      year: YMD[0] | 0
      ,month: (YMD[1] | 0) - 1
      ,date: YMD[2] | 0
    };
    
    if(td.hasClass(DISABLED)) return;
    
    lay.extend(dateTime, YMD); //同步 dateTime

    //范围选择
    if(options.range){
      //补充时分秒
      lay.each(['startTime', 'endTime'], function(i, item){
        that[item] = that[item] || {
          hours: 0
          ,minutes: 0
          ,seconds: 0
        };
      });
      that.calendar(null, index).done(null, 'change');
    } else if(options.position === 'static'){ //直接嵌套的选中
      that.calendar().done().done(null, 'change'); //同时执行 done 和 change 回调
    } else if(options.type === 'date'){
      that.setValue(that.parse()).remove().done();
    } else if(options.type === 'datetime'){
      that.calendar().done(null, 'change');
    }
  };
  
  //底部按钮
  Class.prototype.tool = function(btn, type){
    var that = this
    ,options = that.config
    ,lang = that.lang()
    ,dateTime = options.dateTime
    ,isStatic = options.position === 'static'
    ,active = {
      //选择时间
      datetime: function(){
        if(lay(btn).hasClass(DISABLED)) return;
        that.list('time', 0);
        options.range && that.list('time', 1);
        lay(btn).attr('lay-type', 'date').html(that.lang().dateTips);
      }
      
      //选择日期
      ,date: function(){
        that.closeList();
        lay(btn).attr('lay-type', 'datetime').html(that.lang().timeTips);
      }
      
      //清空、重置
      ,clear: function(){
        isStatic && (
          lay.extend(dateTime, that.firstDate)
          ,that.calendar()
        )
        options.range && (
          delete options.dateTime
          ,delete that.endDate
          ,delete that.startTime
          ,delete that.endTime
        );
        that.setValue('').remove();
        that.done(['', {}, {}]);
      }
      
      //现在
      ,now: function(){
        var thisDate = new Date();
        lay.extend(dateTime, that.systemDate(), {
          hours: thisDate.getHours()
          ,minutes: thisDate.getMinutes()
          ,seconds: thisDate.getSeconds()
        });
        that.setValue(that.parse()).remove();
        isStatic && that.calendar();
        that.done();
      }
      
      //确定
      ,confirm: function(){
        if(options.range){
          if(lay(btn).hasClass(DISABLED)) return that.hint(
            options.type === 'time' ? lang.timeout.replace(/日期/g, '时间') : lang.timeout
          );
        } else {
          if(lay(btn).hasClass(DISABLED)) return that.hint(lang.invalidDate);
        }
		// ThinkGem 当下一个日期弹出前，可能上一个还未设置，导致选择不上的问题（下面2行调换顺序）
        that.setValue(that.parse()).remove()
        that.done();
      }
    };
    active[type] && active[type]();
  };
  
  //统一切换处理
  Class.prototype.change = function(index){
    var that = this
    ,options = that.config
    ,dateTime = that.thisDateTime(index)
    ,isAlone = options.range && (options.type === 'year' || options.type === 'month')
    
    ,elemCont = that.elemCont[index || 0]
    ,listYM = that.listYM[index]
    ,addSubYeay = function(type){
      var isYear = lay(elemCont).find('.laydate-year-list')[0]
      ,isMonth = lay(elemCont).find('.laydate-month-list')[0];
      
      //切换年列表
      if(isYear){
        listYM[0] = type ? listYM[0] - 15 : listYM[0] + 15;
        that.list('year', index);
      }
      
      if(isMonth){ //切换月面板中的年
        type ? listYM[0]-- : listYM[0]++;
        that.list('month', index);
      }
      
      if(isYear || isMonth){
        lay.extend(dateTime, {
          year: listYM[0]
        });
        if(isAlone) dateTime.year = listYM[0];
        options.range || that.done(null, 'change');     
        options.range || that.limit(lay(that.footer).find(ELEM_CONFIRM), {
          year: listYM[0]
        });
      }
      
      that.setBtnStatus();
      return isYear || isMonth;
    };
    
    return {
      prevYear: function(){
        if(addSubYeay('sub')) return;
        dateTime.year--;
        that.checkDate('limit').calendar(null, index);
        that.done(null, 'change');
      }
      ,prevMonth: function(){
        var YM = that.getAsYM(dateTime.year, dateTime.month, 'sub');
        lay.extend(dateTime, {
          year: YM[0]
          ,month: YM[1]
        });
        that.checkDate('limit').calendar(null, index);
        that.done(null, 'change');
      }
      ,nextMonth: function(){
        var YM = that.getAsYM(dateTime.year, dateTime.month);
        lay.extend(dateTime, {
          year: YM[0]
          ,month: YM[1]
        });
        that.checkDate('limit').calendar(null, index);
        that.done(null, 'change');
      }
      ,nextYear: function(){
        if(addSubYeay()) return;
        dateTime.year++
        that.checkDate('limit').calendar(null, index);
        that.done(null, 'change');
      }
    };
  };
  
  //日期切换事件
  Class.prototype.changeEvent = function(){
    var that = this
    ,options = that.config;

    //日期选择事件
    lay(that.elem).on('click', function(e){
      lay.stope(e);
    }).on('mousedown', function(e){
      lay.stope(e);
    });
    
    //年月切换
    lay.each(that.elemHeader, function(i, header){
      //上一年
      lay(header[0]).on('click', function(e){
        that.change(i).prevYear();
      });
      
      //上一月
      lay(header[1]).on('click', function(e){
        that.change(i).prevMonth();
      });
      
      //选择年月
      lay(header[2]).find('span').on('click', function(e){
        var othis = lay(this)
        ,layYM = othis.attr('lay-ym')
        ,layType = othis.attr('lay-type');
        
        if(!layYM) return;
        
        layYM = layYM.split('-');

        that.listYM[i] = [layYM[0] | 0, layYM[1] | 0];
        that.list(layType, i);
        lay(that.footer).find('.'+ ELEM_TIME_BTN).addClass(DISABLED);
      });

      //下一月
      lay(header[3]).on('click', function(e){
        that.change(i).nextMonth();
      });
      
      //下一年
      lay(header[4]).on('click', function(e){
        that.change(i).nextYear();
      });
    });
    
    //点击日期
    lay.each(that.table, function(i, table){
      var tds = lay(table).find('td');
      tds.on('click', function(){
        that.choose(lay(this), i);
      });
    });
    
    //点击底部按钮
    lay(that.footer).find('span').on('click', function(){
      var type = lay(this).attr('lay-type');
      that.tool(this, type);
    });
  };
  
  //是否输入框
  Class.prototype.isInput = function(elem){
    return /input|textarea/.test(elem.tagName.toLocaleLowerCase());
  };

  //绑定的元素事件处理
  Class.prototype.events = function(){
    var that = this
    ,options = that.config

    //绑定呼出控件事件
    ,showEvent = function(elem, bind){
      elem.on(options.trigger, function(){
        bind && (that.bindElem = this);
        if(options.trigger === 'focus'){ // ThinkGem 冒泡事件优先处理时，可能会呼不出，增加延迟。
        	setTimeout(function(){
        		that.render();
        	},100);
        }else{
        	that.render();
        }
      });
    };
    
    if(!options.elem[0] || options.elem[0].eventHandler) return;
    
    showEvent(options.elem, 'bind');
    showEvent(options.eventElem);
    
    options.elem[0].eventHandler = true;
  };
  
  //记录所有实例
  thisModule.that = {}; //记录所有实例对象
  
  //获取当前实例对象
  thisModule.getThis = function(id){
    var that = thisModule.that[id];
    if(!that) hint.error(id ? (MOD_NAME +' instance with ID \''+ id +'\' not found') : 'ID argument required');
    return that;
  };
  
  //初始执行
  ready.run = function(lay){
    //绑定关闭控件事件
    lay(document).on('mousedown', function(e){
      if(!laydate.thisId) return;
      var that = thisModule.getThis(laydate.thisId);
      if(!that) return;
      
      var options = that.config;
      
      if(
        e.target === options.elem[0] || 
        e.target === options.eventElem[0] ||
        e.target === lay(options.closeStop)[0]
      ) return;
      
      that.remove();
      
    }).on('keydown', function(e){
      if(!laydate.thisId) return;
      var that = thisModule.getThis(laydate.thisId);
      if(!that) return;
      
      if(e.keyCode === 13){
        if(lay('#'+ that.elemID)[0] && that.elemID === Class.thisElemDate){
          e.preventDefault();
          lay(that.footer).find(ELEM_CONFIRM)[0].click();
        }
      }
    });
    
    //自适应定位
    lay(window).on('resize', function(){
      if(!laydate.thisId) return;
      var that = thisModule.getThis(laydate.thisId);
      if(!that) return;
      
      if(!that.elem || !lay(ELEM)[0]){
        return false;
      }
      
      that.position();
    });
  };
  
  //核心接口
  laydate.render = function(options){
    var inst = new Class(options);
    return thisModule.call(inst);
  };
  
  //将指定对象转化为日期值
  laydate.parse = function(dateTime, format, one){
    dateTime = dateTime || {};
    
    //如果 format 是字符型，则转换为数组格式
    if(typeof format === 'string'){
      format = thisModule.formatArr(format);
    }
    
    format = (format || []).concat();
    
    //转义为规定格式
    lay.each(format, function(i, item){
      if(/yyyy|y/.test(item)){ //年
        format[i] = lay.digit(dateTime.year, item.length);
      } else if(/MM|M/.test(item)){ //月
        format[i] = lay.digit(dateTime.month + (one || 0), item.length);
      } else if(/dd|d/.test(item)){ //日
        format[i] = lay.digit(dateTime.date, item.length);
      } else if(/HH|H/.test(item)){ //时
        format[i] = lay.digit(dateTime.hours, item.length);
      } else if(/mm|m/.test(item)){ //分
        format[i] = lay.digit(dateTime.minutes, item.length);
      } else if(/ss|s/.test(item)){ //秒
        format[i] = lay.digit(dateTime.seconds, item.length);
      }
    });
    
    return format.join('');
  };
  
  //得到某月的最后一天
  laydate.getEndDate = function(month, year){
    var thisDate = new Date();
    //设置日期为下个月的第一天
    thisDate.setFullYear(
      year || thisDate.getFullYear()
      ,month || (thisDate.getMonth() + 1)
    ,1);
    //减去一天，得到当前月最后一天
    return new Date(thisDate.getTime() - 1000*60*60*24).getDate();
  };
  
  //加载方式
  isLayui ? (
    laydate.ready()
    ,layui.define('lay', function(exports){ //layui 加载
      laydate.path = layui.cache.dir;
      ready.run(lay);
      exports(MOD_NAME, laydate);
    })
  ) : (
    (typeof define === 'function' && define.amd) ? define(function(){ //requirejs 加载
      ready.run(lay);
      return laydate;
    }) : function(){ //普通 script 标签加载
      laydate.ready();
      ready.run(window.lay);
      window.laydate = laydate;
    }()
  );

}(window, window.document);

