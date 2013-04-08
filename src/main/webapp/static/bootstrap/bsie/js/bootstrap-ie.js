(function($) {
  $.eb = $.eb || {};

  // $.eb.ie = function (min,max) {
  //   // return true;
  //   if ($.browser.msie) {
  //     var v = Math.floor($.browser.version);
  //     if (v >= min && v <= max) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }
  $.eb.ie6 = function () {
    return navigator.userAgent.toLowerCase().indexOf('msie 6.0') > -1;
    // alert(navigator.userAgent.toLowerCase().indexOf('msie 6.0'));
  }
 

  $.eb.color = function () {
    var pad = function(num, totalChars) {
        var pad = '0';
        num = num + '';
        while (num.length < totalChars) {
            num = pad + num;
        }
        return num;
    };

    // Ratio is between 0 and 1
    this.changeColor = function(color, ratio, darker) {
        // Trim trailing/leading whitespace
        color = color.replace(/^\s*|\s*$/, '');

        // Expand three-digit hex
        color = color.replace(
            /^#?([a-f0-9])([a-f0-9])([a-f0-9])$/i,
            '#$1$1$2$2$3$3'
        );

        // Calculate ratio
        var difference = Math.round(ratio * 256) * (darker ? -1 : 1),
            // Determine if input is RGB(A)
            rgb = color.match(new RegExp('^rgba?\\(\\s*' +
                '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
                '\\s*,\\s*' +
                '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
                '\\s*,\\s*' +
                '(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])' +
                '(?:\\s*,\\s*' +
                '(0|1|0?\\.\\d+))?' +
                '\\s*\\)$'
            , 'i')),
            alpha = !!rgb && rgb[4] != null ? rgb[4] : null,

            // Convert hex to decimal
            decimal = !!rgb? [rgb[1], rgb[2], rgb[3]] : color.replace(
                /^#?([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])([a-f0-9][a-f0-9])/i,
                function() {
                    return parseInt(arguments[1], 16) + ',' +
                        parseInt(arguments[2], 16) + ',' +
                        parseInt(arguments[3], 16);
                }
            ).split(/,/),
            returnValue;

        // Return RGB(A)
        return !!rgb ?
            'rgb' + (alpha !== null ? 'a' : '') + '(' +
                Math[darker ? 'max' : 'min'](
                    parseInt(decimal[0], 10) + difference, darker ? 0 : 255
                ) + ', ' +
                Math[darker ? 'max' : 'min'](
                    parseInt(decimal[1], 10) + difference, darker ? 0 : 255
                ) + ', ' +
                Math[darker ? 'max' : 'min'](
                    parseInt(decimal[2], 10) + difference, darker ? 0 : 255
                ) +
                (alpha !== null ? ', ' + alpha : '') +
                ')' :
            // Return hex
            [
                '#',
                pad(Math[darker ? 'max' : 'min'](
                    parseInt(decimal[0], 10) + difference, darker ? 0 : 255
                ).toString(16), 2),
                pad(Math[darker ? 'max' : 'min'](
                    parseInt(decimal[1], 10) + difference, darker ? 0 : 255
                ).toString(16), 2),
                pad(Math[darker ? 'max' : 'min'](
                    parseInt(decimal[2], 10) + difference, darker ? 0 : 255
                ).toString(16), 2)
            ].join('');
    };
    this.lighten = function(color, ratio) {
        return changeColor(color, ratio, false);
    };
    this.darken = function(color, ratio) {
        return changeColor(color, ratio, true);
    };
    return this;
  }();


  function bootstrapIE6(el) {
    var dropdownWidthFix = function (el) {
      el.each(function () {
        var w = 0;
        $(this).children('li').each(function() {
          var aw = $(this).outerWidth();
          if (aw > w) w = aw;
        });

        $(this).width(w);
      });
    }

    if ($.eb.ie6()) {
      el = el || $('html');

	  // 去除超链接虚框
	  $("a").bind("focus",function() { 
	  	if(this.blur) {this.blur();}; 
	  });
   
      //-------------
      // GRID
      //-------------
      $('.row-fluid [class*="span"]:first-child, .row [class*="span"]:first-child').addClass('span-first-child');

      //-------------
      // dropdown 
      //-------------
      // fix for IE6 not support li:hover
//      var lis = ['dropdown-submenu'];
//      for (var i in lis) {
//        var child = 'li.' + lis[i];
//        var hover = lis[i] + '-hover';
//        $('ul', el).on('mouseenter', child, function () {
//          $(this).addClass(hover);
//        }).on('mouseleave', child, function () {
//          $(this).removeClass(hover);
//        });
//      }

      /// fix :after selector -- dropdown-submenu > a:after
      $('.dropdown-submenu > a', el).after('<span class="dropdown-tri"></span>');

      /// fix multi class selector -- .dropdown-submenu.pull-left
      $('.dropdown-submenu.pull-left', el).removeClass('pull-left').addClass('dropdown-submenu-pull-left');
      // $('.navbar .nav.pull-right').removeClass('pull-right').addClass('nav-pull-right');

      /// fix ul li 100% width bug, set ul width to max width of it's sub li
      dropdownWidthFix($('.dropdown-menu:visible', el));


      //-------------
      // buttons
      //-------------
      var btnColorCls = ['btn-primary','btn-warning','btn-danger','btn-success','btn-info','btn-inverse'];
      var btnSizeCls = ['btn-mini','btn-small','btn-large'];
      $('.btn-group', el).parent().find('.btn-group:eq(0)').addClass('btn-group-first');
      $('.btn', el).parent().find('.btn:eq(0)').addClass('btn-first');

      // fix button:hover
      $('body', el).on('mouseenter', '.btn', function () {
        var btn = $(this);
        var hover = 'btn-hover';
        btn.data('ie6hover',hover);
        $.each(btnColorCls, function (k,v) {
          if (btn.hasClass(v)) {
            hover = v + '-hover';
            btn.data('ie6hover',hover);
            return false;
          }
        });
        btn.addClass(hover);
      }).on('mouseleave', '.btn', function () {
        var btn = $(this);
        var hover = btn.data('ie6hover');
        btn.removeData('ie6hover');
        if (hover) btn.removeClass(hover);
      });

      // fix .btn.dropdown-toggle, .btn-primary.dropdown-toggle ...
      // fix .btn.dropdown-toggle, .btn-small.dropdown-toggle ...
      $('.btn.dropdown-toggle', el).each(function () {
        var btn = $(this);
        var ddt = 'btn-dropdown-toggle';
        btn.addClass(ddt);

        ddt = null;
        $.each(btnColorCls, function (k,v) {
          if (btn.hasClass(v)) {
            ddt = v + '-dropdown-toggle';
            return false;
          }
        });
        if (ddt) btn.addClass(ddt);

        ddt = null;
        $.each(btnSizeCls, function (k,v) {
          if (btn.hasClass(v)) {
            ddt = v + '-dropdown-toggle';
            return false;
          }
        });
        if (ddt) btn.addClass(ddt);
      });

      // fix split button dropdown toggle background color
      $('.btn + .btn.dropdown-toggle', el).each(function () {
        var btn = $(this);
        var c = btn.css('background-color');
        // alert($.eb.color.darken(c, .2));
        btn.css('background-color', $.eb.color.darken(c, .1));
      });

      // fix .btn-group.open
      var dropdownPropertyChange = function(e) {
        var self = $(this);
        var cls = e.data.cls;

        /// fix ul li 100% width bug, set ul width to max width of it's sub li
        var el = $('.dropdown-menu:visible', this);
        if (el.length) dropdownWidthFix(el);

        if (self.hasClass('open') && !self.hasClass(cls+'-open')) {
          self.addClass(cls+'-open');
        }
        else if (!self.hasClass('open') && self.hasClass(cls+'-open')) {
          self.removeClass(cls+'-open');
        }

        self.one('propertychange', {cls:cls}, dropdownPropertyChange);
      };
      $.each(['btn-group', 'dropdown'], function (k,cls) {
        $('.'+cls, el).one('propertychange', {cls:cls}, dropdownPropertyChange);
      });

      // fix .btn.disabled selector
      $('.btn.disabled', el).addClass('btn-disabled');

      var btnPropertyChange = function (e) {
        var self = $(this);
        var cls = e.data.cls;

        if (self.hasClass('disabled') && !self.hasClass(cls+'-disabled')) {
          self.addClass(cls+'-disabled');
        }
        else if (!self.hasClass('disabled') && self.hasClass(cls+'-disabled')) {
          self.removeClass(cls+'-disabled');
        }

        self.one('propertychange', {cls:cls}, btnPropertyChange);
      }
      $.each(['btn'], function (k,cls) {
        $('.'+cls, el).one('propertychange', {cls:cls}, btnPropertyChange);
      });


      //-------------
      // table
      //-------------

      // fix table-hover effect
      $('table.table-hover', el).on('mouseenter', 'tr', function () {
        $(this).addClass('tr-hover');
      }).on('mouseleave', 'tr', function () {
        $(this).removeClass('tr-hover');
      });

      //-------------
      // form
      //-------------

      // fix input[type=xxx] selector
      $('input[type="file"], input[type="image"], input[type="submit"], input[type="reset"], input[type="button"], input[type="radio"], input[type="checkbox"], input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"]', el).each(function () {
        var input = $(this);
        input.addClass('input-'+input.attr('type'));
      });

      // fix form-horizontal controls margin-left
      $('.form-horizontal .controls:first-child', el).addClass('controls-first-child');

      // fix .checkbox.inline
      $('.checkbox.inline', el).addClass('checkbox-inline');
      $('.radio.inline', el).addClass('radio-inline');

      // fix select[multiple], select[size]
      $('select[multiple]', el).addClass('select-multiple');
      $('select[size]', el).addClass('select-size');

      // fix tag[disabled]
      $('input[disabled], select[disabled], textarea[disabled]', el).each(function () {
        var self = $(this);
        self.addClass(self[0].tagName.toLowerCase()+'-disabled');
      });

//       $('input,select,textarea', el).on('propertychange', function() {
//         var self = $(this);
//         if (self.data('chgDisabled')) return;

//         var cls = self[0].tagName.toLowerCase();
// // alert(self.attr('disabled'));
//         if (self.attr('disabled') && !self.hasClass(cls+'-disabled')) {
//           // alert('abc');
//           self.addClass(cls+'-disabled');
//           self.data('chgDisabled', true);
//         }
//         else if (!self.attr('disabled') && self.hasClass(cls+'-disabled')) {
//           self.removeClass(cls+'-disabled');
//           self.data('chgDisabled', true);
//         }
//       });

//       $('input,select,textarea', el).on('propertychange', function() {
//         var self = $(this);
//         if (self.data('chgReadonly')) return;

//         var cls = self[0].tagName.toLowerCase();

//         if (self.attr('readonly') && !self.hasClass(cls+'-readonly')) {
//           self.addClass(cls+'-readonly');
//           self.data('chgReadonly', true);
//         }
//         else if (typeof self.attr('readonly') == 'undefined' && self.hasClass(cls+'-readonly')) {
//           self.removeClass(cls+'-readonly');
//           self.data('chgReadonly', true);
//         }
//       });

      // fix tag[readonly]
      $('input[readonly], select[readonly], textarea[readonly]', el).each(function () {
        var self = $(this);
        self.addClass(self[0].tagName.toLowerCase()+'-readonly');
      });

      // fix input[type=xxx][disabled]
      $('input[type="radio"][disabled], input[type="checkbox"][disabled]', el).each(function () {
        var self = $(this);
        self.addClass(self.attr('type').toLowerCase()+'-disabled');
      });

      // fix input[type=xxx][readonly]
      $('input[type="radio"][readonly], input[type="checkbox"][readonly]', el).each(function () {
        var self = $(this);
        self.addClass(self.attr('type').toLowerCase()+'-readonly');
      });

      // fix.control-group.warning ...
      var ctlGrpTypeCls = ['warning','success','error','info'];
      $.each(ctlGrpTypeCls, function (k,v) {
        $('.control-group.'+v, el).addClass('control-group-'+v);
      });

      var controlGroupPropertyChange = function(e) {
        if(e.originalEvent.propertyName.toLowerCase() == 'classname') {
          var self = $(this);
          $.each(ctlGrpTypeCls, function (k,v) {
            var ieCls = 'control-group-'+v;
            if (self.hasClass(v)) {
              if (!self.hasClass(ieCls)) {
                self.addClass(ieCls);
              }
            }
            else {
              if (self.hasClass(ieCls)) {
                self.removeClass(ieCls);
              }
            }
          });
        }
        $(this).one('propertychange', controlGroupPropertyChange);
      };
      $('.control-group', el).one('propertychange', controlGroupPropertyChange);

      //-------------
      // popover
      //-------------
      // $('.popover .arrow', el).after('<span class="arrow-after"></span>');

      //-------------
      // pagination
      //-------------
      $('.pagination ul li:first-child', el).addClass('first-child');


      //-------------
      // icons
      //-------------
//      $('[class^="icon-"],[class*=" icon-"]').each(function () {
//        var self = $(this); 
//        if (!self.hasClass('icon-xxx')) {
//          self.addClass('icon-xxx');
//          self.css('background-position-y', 
//            (parseInt(self.css('background-position-y')) + 1)+'px');
//        }
//      });
      $('[class^="icon-"], [class*=" icon-"]').addClass('icon-xxx');

      //-------------
      // carousel
      //-------------
      $('.carousel-control.left', el).removeClass('left').addClass('carousel-control-left');
      $('.carousel-control.right', el).removeClass('right').addClass('carousel-control-right');
      $('.carousel-caption').each(function() {
        var self = $(this);
        var padding = self.outerWidth() - self.width();
        self.width(self.parents('.carousel-inner .item').width() - padding);
      });


    }
  }
  $.bootstrapIE6 = bootstrapIE6;


  $(document).ready(function () {
    bootstrapIE6();
  });

})(jQuery);