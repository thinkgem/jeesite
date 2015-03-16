(function ($) {
    /**
     * If no touch events are available map touch events to corresponding mouse events.
     **/
    if(!('ontouchstart' in window)){
        var _fakeCallbacks = {}; // store the faked callbacks so that they can be unbound
        function touch2mouse(context,args){
            var type = args[0],//事件类型
                t = type ,matched = false;
            if ((typeof type) == "object") {
                return args;
            }
            if(type.indexOf('touchstart')>-1){
                matched = true;
                t = t.replace('touchstart','mousedown');
            }else if(t.indexOf('touchend')>-1){
                matched = true;
                t = t.replace('touchend','mouseup');
            }else if(t.indexOf('touchmove')>-1){
                matched = true;
                t = t.replace('touchmove','mousemove');
            }
            if(matched){//匹配到touch事件
                var ci = -1;
                //找出callback，需要更换里面的event对象的属性
                for(var i =1;i<args.length;i++){
                    if(typeof args[i] == 'function'){
                        ci = i;
                        break;
                    }
                }
                args[0] = t;
                if(ci > -1){
                    args[ci] = fakeTouches(type, args[ci] , context);
                }
            }
            return args;
        }

        function fakeTouches(type, callback, context) {
            // wrap the callback with a function that adds a fake
            // touches property to the event.
            return _fakeCallbacks[callback] = function (event) {
                if(event.liveFired)context = this;//if it is delegate event,change context to target element
                if (event.button) {
                    return false;
                }
                event.touches = [{
                    length: 1,// 1 mouse (finger)
                    clientX: event.clientX,
                    clientY: event.clientY,
                    pageX: event.pageX,
                    pageY: event.pageY,
                    screenX: event.screenX,
                    screenY: event.screenY,
                    target: event.target
                }];
                event.touchType = type;
                return callback.apply(context, [event]);
            }
        }

        var _trigger = $.fn.trigger;
        $.fn.trigger = function(event, data){
            var args = Array.prototype.slice.call(arguments,0);
            return _trigger.apply(this,touch2mouse(this,args));
        };
        var _triggerHandler = $.fn.triggerHandler;
        $.fn.triggerHandler = function(event, data){
            var args = Array.prototype.slice.call(arguments,0);
            return _triggerHandler.apply(this, touch2mouse(this,args));
        };
        var _on = $.fn.on;
        $.fn.on = function(event, selector, data, callback, one){
            var args = Array.prototype.slice.call(arguments,0);
            return _on.apply(this,touch2mouse(this,args));
        }
        var _off = $.fn.off;
        $.fn.off = function(event, selector, callback){
            var args = [event];
            if(typeof selector == 'string'){
                args.push(selector);
            }
            var result = touch2mouse(this,args).concat([_fakeCallbacks[callback]||callback]);
            delete(_fakeCallbacks[callback]);
            return _off.apply(this,result);
        }
    }
})(Zepto);