/*
 *  Description: 漂浮广告
 *  Author: jjc
 *  Date: 2012.07.04
 */
;(function ( $, window, document, undefined ) {
    var pluginName = 'floatingAd';
    var defaults = {
		step: 1,
		delay: 50, 
		isLinkClosed: false,
		onClose: function(elem){}
    };
    var ads = {
    	linkUrl: '#',
    	'z-index': '100',
    	'closed-icon': '',
    	imgHeight: '',
    	imgWidth: '',
    	title: '',
    	img: '#',
    	linkWindow: '_blank',
    	headFilter: 0.2
    };

    function Plugin(element, options) {
        this.element = element;
        this.options = $.extend(
        	{}, 
        	defaults, 
        	options, 
        	{
        		width: $(window).width(),
				height: $(window).height(),
        		xPos: this.getRandomNum(0, $(window).width() - $(element).innerWidth()), 
				yPos: this.getRandomNum(0, 300),
				yOn: this.getRandomNum(0, 1),
				xOn: this.getRandomNum(0, 1),
				yPath: this.getRandomNum(0, 1),
				xPath: this.getRandomNum(0, 1),
				hOffset: $(element).innerHeight(),
				wOffset: $(element).innerWidth(),
				fn: function(){},
				interval: 0
			}
        );
        this._defaults = defaults;
        this._name = pluginName;
        
        this.init();
    }

    Plugin.prototype = {
    	init: function () {
    		var elem = $(this.element);
    		var defaults = this.options;
    		var p = this;
    		var xFlag = 0;
    		var yFlag = 0;
    		
    		elem.css({"left": defaults.xPos + p.scrollX(), "top": defaults.yPos + p.scrollY()});
    		defaults.fn = function(){
		    	defaults.width = $(window).width();
				defaults.height = $(window).height();
				
				if(xFlag == p.scrollX() && yFlag == p.scrollY()){
					elem.css({"left": defaults.xPos + p.scrollX(), "top": defaults.yPos + p.scrollY()});
					if (defaults.yOn)
						defaults.yPos = defaults.yPos + defaults.step;
					else
						defaults.yPos = defaults.yPos - defaults.step;
				
					if (defaults.yPos <= 0) {
						defaults.yOn = 1;
						defaults.yPos = 0;
					}
					if (defaults.yPos >= (defaults.height - defaults.hOffset)) {
						defaults.yOn = 0;
						defaults.yPos = (defaults.height - defaults.hOffset);
					}
					
					if (defaults.xOn) 
						defaults.xPos = defaults.xPos + defaults.step;
					else
						defaults.xPos = defaults.xPos - defaults.step;
		
					if (defaults.xPos <= 0) {
						defaults.xOn = 1;
						defaults.xPos = 0;
					}
					if (defaults.xPos >= (defaults.width - defaults.wOffset)) {
						defaults.xOn = 0;
						defaults.xPos = (defaults.width - defaults.wOffset);
					}
				}
				yFlag = $(window).scrollTop();
				xFlag = $(window).scrollLeft();
   			};
   			this.run(elem, defaults);
    	},
    	run: function(elem, defaults){
    		this.start(elem, defaults);
    		this.adEvent(elem,defaults);
    	},
    	start: function(elem, defaults){
    		elem.find('div.close').hide();
    		defaults.interval = window.setInterval(defaults.fn,  defaults.delay);
    		window.setTimeout(function(){elem.show();}, defaults.delay);
    	},
    	getRandomNum: function (Min, Max){  
			var Range = Max - Min;  
			var Rand = Math.random();  
			return(Min + Math.round(Rand * Range));  
		},
		getPath: function(on){
			return on ? 0 : 1;
		},
		clear: function(elem, defaults){
			elem.find('div.close').show();
			window.clearInterval(defaults.interval);
		},
		close: function(elem, defaults, isClose){
			elem.unbind('hover');
  			elem.hide();
  			if(isClose)
				defaults.onClose.call(elem);
		},
		adEvent: function(elem, defaults){
			var obj = {
				elem: this,
			  	fn_close: function() {
			   		this.elem.close(elem, defaults, true);
			  	},
			  	fn_clear: function() {
			  		if(this.elem.options.isLinkClosed)
			  			this.elem.close(elem, defaults, false);
			  	}
			};
			
    		elem.find('div.button').bind('click', jQuery.proxy(obj, "fn_close"));
    		
    		elem.find('a').bind('click', jQuery.proxy(obj, "fn_clear"));
    		
    		var stop = {
				elem: this,
			  	over: function(){
			    	this.elem.clear(elem, defaults);
			  	},
			  	out: function(){
					this.elem.start(elem, defaults);
			  	}
			};
    		
    		elem.hover(
			    jQuery.proxy(stop, "over"),
				jQuery.proxy(stop, "out")
			);
		},
		scrollX: function(){
			var de = document.documentElement;
			return self.pageXOffset || (de && de.scrollLeft) || document.body.scrollLeft;
		},
		scrollY: function(){
			var de = document.documentElement;
			return self.pageYOffset || (de && de.scrollTop) || document.body.scrollTop;
		}
	};
    $.fn.floatingAd = function(options) {
        return this.children("div").each(function (i, elem) {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName, new Plugin(this, options));
            }
        });
    };
	$.floatingAd = function(options){
		
		if(options){
	    	if(options.ad){
	    		var adDiv = $('#' + pluginName);
	    		
	    		if(adDiv.length <= 0)
		    		adDiv = $('<div>', {
		    			'id': pluginName,
		    			'class': pluginName
		    		}).appendTo('body');
		    		
	    		for(var i in options.ad){
	    			
	    			var ad = options.ad[i];
	    			ad = $.extend({}, ads, ad);
	    			//漂浮层
	    			var div = $('<div>', {
	    				'class': 'ad'
	    			});
	    			
	    			div.css("z-index", ad['z-index']);
	    			
	    			//关闭层
	    			var closeDiv = $('<div>', {
	    				'class': 'close',
	    				'style': (ad.imgWidth ? 'width:' + ad.imgWidth + 'px;' : '')
	    			});
	    			$('<div>', {
	    				'class': 'opacity',
	    				'style': 'opacity: ' + ad.headFilter + ';filter: alpha(opacity = ' + ad.headFilter*100 + ');'
	    			}).appendTo(closeDiv);
	    			
	    			$('<div>', {
	    				'class': 'text'
	    			}).append(
	    				$('<div>', {
	    					'class': 'title',
	    					'text': ad.title
	    				})
	    			).append(
	    				$('<div>', {
	    					'class': 'button',
	    					'style': ad['closed-icon'] ? 'background:url("' + ad['closed-icon'] + '") no-repeat;' : ''
	    				})
	    			).appendTo(closeDiv);
	    			
	    			closeDiv.appendTo(div);
	    			
	    			//内容层
	    			var content = $('<div>');
	    			
	    			$('<a>', {
	    				href: ad.linkUrl,
	    				target: ad.linkWindow,
	    				title: ad.title
	    			}).append(
	    				$('<img>', {
	    					'src': ad.img,
	    					'style': (ad.imgHeight ? 'height:' + ad.imgHeight + 'px;' : '') + 
	    					         (ad.imgWidth ? 'width:' + ad.imgWidth + 'px;' : '')
	    				})
	    			).appendTo(content);
	    			
	    			content.appendTo(div);
	    			
	    			div.appendTo(adDiv);
	    		}
	    		delete options.ad;
	    		$('#' + pluginName).floatingAd(options);
	    	}
	    } 
		else
	      	$.error('漂浮广告错误!');
	};
})(jQuery, window, document);