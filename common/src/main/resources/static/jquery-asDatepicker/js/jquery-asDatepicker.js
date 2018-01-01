/*
 * asDatepicker
 * https://www.sucaijiayuan.com
 * Copyright (c) 2014 amazingSurge
 * Licensed under the MIT license.
 * 
	// 初始化日期范围选择控件
	var datepicker = function($this){
		var d = $this.val().split(' -- ');
		if ($.trim(d[0]) == '' || strToDate($.trim(d[0])) == 'Invalid Date'){
			d[0] = new Date().format("yyyy-MM-dd");
		}
		if ($.trim(d[1]) == '' || strToDate($.trim(d[1])) == 'Invalid Date'){
			d[1] = d[0];
		}
		$("#beginDate").val(d[0]);
		$("#endDate").val(d[1]);
		$this.inputmask('remove').inputmask({mask: "y-m-d -- y-m-d",
			alias:"yyyy/mm/dd", placeholder: "yyyy-MM-dd -- yyyy-MM-dd"})
		.asDatepicker('destroy').asDatepicker({mode:'range', date:d[0] + ' -- ' + d[1]})
		.unbind('blur').bind('blur', function(){
			datepicker($(this));
		});
	}
    datepicker($('#calendar2').val($("#beginDate").val() + ' -- ' + $("#endDate").val()));
    
 */
(function($, document, window, undefined) {
    // Optional, but considered best practice by some
    "use strict";

    var pluginName = 'asDatepicker',
        defaults = {
            firstDayOfWeek: 0, // 0---6 === sunday---saturday
            mode: 'single', // single|range|multiple
            displayMode: 'dropdown', // dropdown|inline
            calendars: 1,
            date: 'today', // today|Date (yyyy-mm-dd)
            keyboard: true, // true | false
            rangeSeparator: '--',
            multipleSeparator: ',',
            multipleSize: 5,

            container: 'body',
            position: 'bottom', // top|right|bottom|left|rightTop|leftTop
            alwaysShow: false, // true or false
            onceClick: false, // true or false

            min: null, // min: '2012-12-1',//null|'today'|days|Date with (yyyy-mm-dd)
            max: null, // max: '2013-10-1',//null|'today'|days|Date with (yyyy-mm-dd)     

            selectableDate: [], // ['2013-8-1', {from: '2013-8-5', to: '2013-8-10'}, {from: -30, to: 30}]],

            selectableYear: [], // [{from: 1980, to: 1985}, 1988, {from: 2000, to: 2010}, 2013],
            selectableMonth: [], // months from 0 - 11 (jan to dec) example: [0, {from: 3, to: 9}, 11],
            selectableDay: [], // days from 0 - 31,

            selectableDayOfWeek: [], // days of week 0-6 (su to sa) [0, {from: 2, to: 4}] , [] is default all

            lang: 'zh', //'chinese'
            views: ['days'], // ['days'], ['days', 'months', 'years']
            outputFormat: 'yyyy-mm-dd',

            mobileMode: false,

            namespace: 'calendar',
            tplInputWrapper: function() {
                return '<div class="namespace-inputWrap"></div>';
            },
            tplWrapper: function() {
                return '<div class="namespace-wrap"></div>';
            },
            tplContent: function() {
                return '<div class="namespace-content">' +
                    '<div class="namespace-header">' +
                    '<div class="namespace-prev"><</div>' +
                    '<div class="namespace-caption"></div>' +
                    '<div class="namespace-next">></div>' +
                    '</div>' +
                    '<div class="namespace-days"></div>' +
                    '<div class="namespace-months"></div>' +
                    '<div class="namespace-years"></div>' +
                    '<div class="namespace-buttons">' +
                    '<div class="namespace-button-cancel"></div>' +
                    '<div class="namespace-button-save"></div>' +
                    '</div>' +
                    '</div>';
            },
            tplTitle: function() {
                return '<div class="namespace-title">test</div>';
            },
            onInit: null,
            onReady: null,
            onRender: null,
            onChange: null,
            onBeforeShow: null,
            onShow: null,
            onBeforeHide: null,
            onHide: null
        };

    var $doc = $(document);

    var $win = $(window);

    var LABEL = {};

    var SHOWED = 0;



    var Plugin = $[pluginName] = function(element, options) {
        var self = this,
            data = $(element).data();

        this.$el = $(element);

        this.defaultOptions = $.extend(true, {}, defaults, options, data);
        this.options = $.extend(true, {}, defaults, options, data);

        $.each(data, function(option, value) {
            self.options[option] = self._parseHtmlString(option, value);
            self.defaultOptions[option] = self._parseHtmlString(option, value);
        });

        this.namespace = this.options.namespace;

        this.$inputWrap = this.$el.addClass(this.namespace + '-input').wrap(this.options.tplInputWrapper().replace(/namespace/g, this.namespace)).parent();
        this.$inputIcon = $('<i class="' + this.namespace + '-icon"></i>');

        this.$inputIcon.appendTo(this.$inputWrap);

        this.$container = $(this.options.container);

        this._trigger('init');
        this._init();
    };

    Plugin.LABEL = LABEL;

    Plugin.localize = function(lang, label) {
        LABEL[lang] = label;
    };

    Plugin.localize('en', {
        days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
        daysShort: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
        months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        buttons: ['Cancel', 'Save']
    });
	
	Plugin.localize("zh", {
        days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
        daysShort: ["日", "一", "二", "三", "四", "五", "六"],
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
        buttons: ['取消', '确定']
    });

    Plugin.prototype = {
        constructor: Plugin,
        _init: function() {
            this.mode = this.options.mode;
            this.format = this._parseFormat('yyyy-mm-dd');
            this.outputFormat = this._parseFormat(this.options.outputFormat || 'yyyy/mm/dd');
            this.focused = 0;
            this.flag = SHOWED++;
            this.pickerHide = false;
            this.selected = false;
            this.showed = false;
            this.bound = false;
            this.hasKeyboard = this.options.keyboard;
            this.map = {};
            this.views = [];
            this.isMobile = this.options.mobileMode; //with other judgements

            var wrapper = this.options.tplWrapper().replace(/namespace/g, this.namespace),
                content = this.options.tplContent().replace(/namespace/g, this.namespace),
                title = this.options.tplTitle().replace(/namespace/g, this.namespace),
                buttons = LABEL[this.options.lang].buttons;
            this.$picker = $(wrapper);

            //set model default property
            switch (this.mode) {
                case 'single':
                    this.calendarsNum = 1;
                    break;
                case 'range':
                    this.calendarsNum = 2;
                    break;
                case 'multiple':
                    this.calendarsNum = this.isMobile ? 1 : this.options.calendars;
                    this.options.views = ['days'];
                    break;
            }

            //set base Views
            for (var i = 0; i < this.calendarsNum; i++) {
                this.$picker.append(content);
                this.views[i] = this.options.views[i] || 'days';
            }

            //check mobileMode 
            if (this.isMobile) {
                var innerWidth = window.innerWidth;
                var innerHeight = window.innerHeight;
                var min = Math.min(innerWidth, innerHeight);
                this.$el.attr('readonly', 'readonly');
                this.$cover = $('<div class="' + this.namespace + '-cover"></div>');

                this.$picker.append(title)
                    .addClass(this.namespace + '_isMobile');

                this.$picker.css({
                    'font-size': Math.round(min * 0.04) + 'px'
                });
            }

            //make $wrapper can be focused
            this.$picker.attr('tabindex', '0');

            //init status in different display mode
            this._initStatus(this.options.displayMode);
            //init pointer
            this._initSections();
            //init default Date
            this._initDate();

            for (var j = 0; j < this.calendarsNum; j++) {
                this._manageViews(j);
                if (this.isMobile) {
                    this.buttonCancels.eq(j).html(buttons[0]);
                    this.buttonSaves.eq(j).html(buttons[1]);
                }
            }

            //init alwaysShow
            this._initShowHide(this.options.alwaysShow);

            this._setValue();

            this._trigger('ready');
        },
        _initStatus: function(displayMode) {
            if (displayMode === 'inline') {
                this.options.alwaysShow = true;
                // this.$el.after(this.$picker).addClass(this.namespace + '_hide');
                this.$picker.addClass(this.namespace + '_show');

                this.$picker.on({
                    focus: $.proxy(this._focus, this),
                    blur: $.proxy(this._blur, this)
                });
            } else if (displayMode === 'dropdown') {
                this.$el.on({
                    focus: $.proxy(this._focus, this),
                    blur: $.proxy(this._blur, this)
                });
                this.$inputIcon.on('click.inputIcon', $.proxy(this._toggle, this));

                this.$picker.appendTo(this.options.container);
                // this.$picker.addClass(this.namespace + '_absolute');
            }
        },
        _initSections: function() {
            this.calendars = this.$picker.find('.' + this.namespace + '-content');
            this.calendarPrevs = this.calendars.find('.' + this.namespace + '-prev');
            this.calendarCaptions = this.calendars.find('.' + this.namespace + '-caption');
            this.calendarNexts = this.calendars.find('.' + this.namespace + '-next');
            this.daypickers = this.calendars.find('.' + this.namespace + '-days');
            this.monthpickers = this.calendars.find('.' + this.namespace + '-months');
            this.yearpickers = this.calendars.find('.' + this.namespace + '-years');
            this.buttonCancels = this.calendars.find('.' + this.namespace + '-button-cancel');
            this.buttonSaves = this.calendars.find('.' + this.namespace + '-button-save');
        },
        _initShowHide: function(alwaysShow) {
            if (alwaysShow === true) {
                this.show();
            }
        },
        _initDate: function() {
        	var date = this.options.date === 'today' ? new Date() : this._parseDate(this.options.date, this.format);
        	
            this._date = {};
            
        	if (this.options.date === 'today'){
                this._date.currentDate = [new Date(date)];
        	}
        	else if (this.options.mode == 'range'){
            	var ds = this.options.date.split(' ' + this.options.rangeSeparator + ' ');
            	ds[0] = this._parseDate(ds[0], this.format);
            	ds[1] = this._parseDate(ds[1], this.format);
                this._date.currentDate = [new Date(ds[0]), new Date(ds[1])];
        	}
        	
        	if (this.options.mode == 'range'){
                this._date.selectedDate = [this._date.currentDate[0], this._date.currentDate[1]];
                this._date.focusDate = this._date.currentDate[0];
                this._date.focusDate.setHours(0, 0, 0, 0);
        	}
        	else if (this.mode === 'multiple') {
                this._date.selectedDate = [];
                this._date.focusDate = new Date(date);
                this._date.focusDate.setHours(0, 0, 0, 0);
            } else {
                this._date.selectedDate = [new Date(date)];
                this._date.focusDate = [new Date(date)];
            }

            this._date.currentDay = [];
            this._date.currentMonth = [];
            this._date.currentYear = [];

            this._date.currentMonthDate = [];
            this._date.currentYearDate = [];

            this._date.selectedDay = [];
            this._date.selectedMonth = [];
            this._date.selectedYear = [];

            this._date.selectedMonthDate = [];
            this._date.selectedYearDate = [];

            this._date.cache = {};
            this._date.cache.currentDate = [];
            this._date.cache.selectedDate = [];

            for (var i = 0; i < this.calendarsNum; i++) {
                this._date.currentDate[i] = this._date.currentDate[i] || new Date(date);
                if (this.mode === 'multiple') {
                    this._setDate(this._date.currentDate[i], 'month', this._date.currentDate[i].getMonth() + i);
                } else {
                    this._date.selectedDate[i] = this._date.selectedDate[i] || new Date(date);
                    this._date.selectedDate[i].setHours(0, 0, 0, 0);

                    this._date.focusDate[i] = this._date.focusDate[i] || new Date(date);
                    this._date.focusDate[i].setHours(0, 0, 0, 0);
                }
                this._updateDate(i);
            }
        },
        _manageViews: function(index) {
            switch (this.views[index]) {
                case 'days':
                    this._generateDaypicker(index);
                    this.calendars.eq(index).addClass(this.namespace + '_days')
                        .removeClass(this.namespace + '_months')
                        .removeClass(this.namespace + '_years');
                    break;
                case 'months':
                    this._generateMonthpicker(index);
                    this.calendars.eq(index).removeClass(this.namespace + '_days')
                        .addClass(this.namespace + '_months')
                        .removeClass(this.namespace + '_years');
                    break;
                case 'years':
                    this._generateYearpicker(index);
                    this.calendars.eq(index).removeClass(this.namespace + '_days')
                        .removeClass(this.namespace + '_months')
                        .addClass(this.namespace + '_years');
                    break;
            }
        },
        _generateDaypicker: function(index) {
            this._generateHeader(index, LABEL[this.options.lang].months[this._date.currentMonth[index]] + ' ' + this._date.currentYear[index]);
            this.daypickers.eq(index).html(this._generateDays(index));
        },
        _generateMonthpicker: function(index) {
            this._generateHeader(index, this._date.currentYear[index]);
            this.monthpickers.eq(index).html(this._generateMonths(index));
        },
        _generateYearpicker: function(index) {
            this._generateHeader(index, this._date.currentYear[index] - 7 + ' ' + this.options.rangeSeparator + ' ' + (this._date.currentYear[index] + 4));
            this.yearpickers.eq(index).html(this._generateYears(index));
        },
        _generateHeader: function(index, caption) {
            this.calendarCaptions.eq(index).html(caption);
            this._judgeLock(index);
        },
        _generateDays: function(index) {
            var year = this._date.currentYear[index],
                month = this._date.currentMonth[index],
                day,
                daysInMonth = new Date(year, month + 1, 0).getDate(),
                firstDay = new Date(year, month, 1).getDay(),
                daysInPrevMonth = new Date(year, month, 0).getDate(),
                daysFromPrevMonth = firstDay - this.options.firstDayOfWeek,
                html = '<div class="' + this.namespace + '-head">',
                isUntouch, isActive, isInRange, rangeUntouch, content, className,
                status = [],
                dateArray = [];

            daysFromPrevMonth = daysFromPrevMonth < 0 ? 7 + daysFromPrevMonth : daysFromPrevMonth;

            for (var i = 0; i < 7; i++) {
                var pos = this.options.firstDayOfWeek + i > 6 ? this.options.firstDayOfWeek + i - 7 : this.options.firstDayOfWeek + i;
                html += '<span>' + LABEL[this.options.lang].daysShort[pos] + '</span>';
            }

            html += '</div><div class="' + this.namespace + '-body"><div class="' + this.namespace + '-row">';

            for (var j = 0; j < 42; j++) {
                day = (j - daysFromPrevMonth + 1);
                isActive = false;
                isInRange = false;
                isUntouch = false;
                rangeUntouch = false;
                status = [isUntouch, isActive, isInRange, rangeUntouch];
                content = 0;
                className = '';

                if (j > 0 && j % 7 === 0) {
                    html += '</div><div class="' + this.namespace + '-row">';
                }

                if (j < daysFromPrevMonth) {
                    //prev month days
                    className = this.namespace + '_otherMonth';
                    content = (daysInPrevMonth - daysFromPrevMonth + j + 1);
                    dateArray[j] = new Date(year, month - 1, content, 0, 0, 0, 0);
                } else if (j > (daysInMonth + daysFromPrevMonth - 1)) {
                    //next month days
                    className = this.namespace + '_otherMonth';
                    content = (day - daysInMonth);
                    dateArray[j] = new Date(year, (month + 1), content, 0, 0, 0, 0);
                } else {
                    //current month days
                    dateArray[j] = new Date(year, month, day, 0, 0, 0, 0);
                    content = day;
                    if (this.hasKeyboard) {
                        if (this.mode === 'multiple') {
                            if (Date.parse(dateArray[j]) === Date.parse(this._date.focusDate)) {
                                className += ' ' + this.namespace + '_focus';
                            }
                        } else {
                            if (Date.parse(dateArray[j]) === Date.parse(this._date.focusDate[index])) {
                                className += ' ' + this.namespace + '_focus';
                            }
                        }
                    }


                }
                status = this._judgeStatus(index, 'days', this.mode, status, dateArray[j], this._date.selectedDate);
                className += this._renderStatus(status);
                html += '<span class="' + className + '">' + content + '</span>';
            }
            html += '</div></div>';
            return html;
        },
        _generateMonths: function(index) {
            var year = this._date.currentYear[index],
                html = '',
                className,
                content = LABEL[this.options.lang].monthsShort,
                dateArray = [],
                focus = this._date.focusDate[index],
                isActive, isInRange, isUntouch, rangeUntouch,
                status = [];

            html += '<div class="' + this.namespace + '-row">';
            for (var i = 0; i < 12; i++) {
                isActive = false;
                isInRange = false;
                isUntouch = false;
                rangeUntouch = false;
                status = [isUntouch, isActive, isInRange, rangeUntouch];
                className = '';

                if (i > 0 && i % 3 === 0) {
                    html += '</div><div class="' + this.namespace + '-row">';
                }
                dateArray[i] = new Date(year, i, 1, 0, 0, 0, 0);

                if (this.hasKeyboard) {
                    if (Date.parse(dateArray[i]) === Date.parse(new Date(focus.getFullYear(), focus.getMonth(), 1, 0, 0, 0, 0))) {
                        className += ' ' + this.namespace + '_focus';
                    }
                }

                status = this._judgeStatus(index, 'months', this.mode, status, dateArray[i], this._date.selectedMonthDate);
                className += this._renderStatus(status);

                html += '<span class="month-' + i + ' ' + className + '">' + content[i] + '</span>';
            }
            html += '</div>';
            return html;
        },
        _generateYears: function(index) {
            var year = this._date.currentYear[index],
                html = '',
                className,
                content = 0,
                dateArray = [],
                focus = this._date.focusDate[index],
                isActive, isInRange, isUntouch, rangeUntouch,
                status = [];

            html += '<div class="' + this.namespace + '-row">';
            for (var m = 0; m < 12; m++) {
                isActive = false;
                isInRange = false;
                isUntouch = false;
                rangeUntouch = false;
                status = [isUntouch, isActive, isInRange, rangeUntouch];
                className = '';

                content = year - 7 + m;
                if (m > 0 && m % 3 === 0) {
                    html += '</div><div class="' + this.namespace + '-row">';
                }
                dateArray[m] = new Date(content, 0, 1, 0, 0, 0, 0);

                if (this.hasKeyboard) {
                    if (Date.parse(dateArray[m]) === Date.parse(new Date(focus.getFullYear(), 0, 1, 0, 0, 0, 0))) {
                        className += ' ' + this.namespace + '_focus';
                    }
                }

                status = this._judgeStatus(index, 'years', this.mode, status, dateArray[m], this._date.selectedYearDate);
                className += this._renderStatus(status);

                html += '<span class="' + className + '">' + content + '</span>';
            }
            html += '</div>';
            return html;
        },
        _judgeLock: function(index) {
            var prevLock = false,
                nextLock = false,
                current, selected;
            switch (this.mode) {
                case 'range':
                    if (index === 0) {
                        switch (this.views[index]) {
                            case 'days':
                                current = Date.parse(this._date.currentDate[index]);
                                selected = Date.parse(this._date.selectedMonthDate[1]);
                                break;
                            case 'months':
                                current = Date.parse(this._date.currentYearDate[index]);
                                selected = Date.parse(this._date.selectedYearDate[1]);
                                break;
                            case 'years':
                                current = new Date(this._date.currentYearDate[index]).setFullYear(this._date.currentYear[index] + 4);
                                selected = Date.parse(this._date.selectedYearDate[1]);
                                break;
                        }
                        nextLock = !this._setPoint('<', nextLock, current, selected);
                    } else {
                        switch (this.views[index]) {
                            case 'days':
                                current = Date.parse(this._date.currentDate[index]);
                                selected = Date.parse(this._date.selectedMonthDate[0]);
                                break;
                            case 'months':
                                current = Date.parse(this._date.currentYearDate[index]);
                                selected = Date.parse(this._date.selectedYearDate[0]);
                                break;
                            case 'years':
                                current = new Date(this._date.currentYearDate[index]).setFullYear(this._date.currentYear[index] - 7);
                                selected = Date.parse(this._date.selectedYearDate[0]);
                                break;
                        }
                        prevLock = !this._setPoint('>', prevLock, current, selected);
                    }
                    break;
                case 'multiple':
                    if (this.calendarsNum > 1) {
                        if (index === 0) {
                            nextLock = true;
                        } else if (index === this.calendarsNum - 1) {
                            prevLock = true;
                        } else {
                            prevLock = nextLock = true;
                        }
                    }
                    break;
            }
            if (prevLock === true) {
                this.calendarPrevs.eq(index).addClass(this.namespace + '_blocked');
            } else {
                this.calendarPrevs.eq(index).removeClass(this.namespace + '_blocked');
            }

            if (nextLock === true) {
                this.calendarNexts.eq(index).addClass(this.namespace + '_blocked');
            } else {
                this.calendarNexts.eq(index).removeClass(this.namespace + '_blocked');
            }
        },
        _judgeSection: function(currentDate, startDate, endDate) {
            var status = true;

            if (currentDate < startDate || currentDate > endDate) {
                status = false;
            }
            return status;
        },
        _judgeSections: function(type, currentDate, dateArray, isDay) {
            var self = this,
                status = false;
            switch (type) {
                case 'date':
                    if (isDay) {
                        currentDate = Date.parse(currentDate);
                        $.each(dateArray, function(i, date) {
                            if (!status) {
                                switch (date.length) {
                                    case undefined:
                                        if (currentDate === Date.parse(date)) {
                                            status = true;
                                        }
                                        break;
                                    case 2:
                                        status = self._judgeSection(currentDate, Date.parse(date[0]), Date.parse(date[1]));
                                        break;
                                }
                            }
                        });

                    } else {
                        var min = Date.parse(currentDate[0]),
                            max = Date.parse(currentDate[1]);
                        $.each(dateArray, function(i, date) {
                            if (!status) {
                                switch (date.length) {
                                    case undefined:
                                        if (Date.parse(date) >= min && Date.parse(date) <= max) {
                                            status = true;
                                        }
                                        break;
                                    case 2:
                                        status = true;
                                        if (max < Date.parse(date[0]) || min > Date.parse(date[1])) {
                                            status = false;
                                        }
                                        break;
                                }
                            }
                        });
                    }
                    break;
                case 'block':
                    $.each(dateArray, function(i, date) {
                        if (!status) {
                            switch (date.length) {
                                case undefined:
                                    if (currentDate === date) {
                                        status = true;
                                    }
                                    break;
                                case 2:
                                    status = self._judgeSection(currentDate, date[0], date[1]);
                                    break;
                            }
                        }
                    });
                    break;
                case 'dayOfWeek':
                    var curr = currentDate.getDay();
                    $.each(dateArray, function(i, date) {
                        if (!status) {
                            switch (date.length) {
                                case undefined:
                                    if (curr === date) {
                                        status = true;
                                    }
                                    break;
                                case 2:
                                    status = self._judgeSection(curr, date[0], date[1]);
                                    break;
                            }
                        }
                    });
                    break;
            }
            return status;
        },
        _judgeStatus: function(index, view, mode, status, currentDate, selectedDate) {
            var untouch = status[0],
                active = status[1],
                inRange = status[2];
            untouch = !this._isSelectable(view, currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate());

            switch (mode) {
                case 'single':
                    active = this._setPoint('=', active, Date.parse(currentDate), Date.parse(selectedDate[index]));
                    break;
                case 'range':
                    active = this._setPoint('=', active, Date.parse(currentDate), Date.parse(selectedDate[index]));
                    inRange = this._setSection(inRange, currentDate, selectedDate[0], selectedDate[1]);
                    if (index === 0) {
                        untouch = this._setPoint('>', untouch, Date.parse(currentDate), Date.parse(selectedDate[1]));
                    } else if (index === 1) {
                        untouch = this._setPoint('<', untouch, Date.parse(currentDate), Date.parse(selectedDate[0]));
                    }
                    break;
                case 'multiple':
                    for (var i = 0; i < this._date.selectedDate.length; i++) {
                        if (Date.parse(currentDate) === selectedDate[i]) {
                            active = true;
                        }
                    }
                    break;
            }
            return status = [untouch, active, inRange];
        },
        _setPoint: function(type, status, currentDate, selectedDate) {
            var _status = status;

            switch (type) {
                case '=':
                    if (currentDate === selectedDate) {
                        _status = true;
                    }
                    break;
                case '<':
                    if (currentDate < selectedDate) {
                        _status = true;
                    }
                    break;
                case '>':
                    if (currentDate > selectedDate) {
                        _status = true;
                    }
                    break;
            }
            return _status;
        },
        _setSection: function(status, currentDate, startDate, endDate) {
            var _status = status,
                _current = Date.parse(currentDate),
                _start = Date.parse(startDate),
                _end = Date.parse(endDate);
            if (_current >= _start && _current <= _end) {
                _status = true;
            }
            return _status;
        },
        _isSelectable: function(view, y, m, d) {
            var isSelectable = true,

                min = this._parseDate(this.options.min, this.format),
                max = this._parseDate(this.options.max, this.format),

                selectableDate = this._parseDateArr(this.options.selectableDate, this.format),

                selectableYear = this._parseDateSection(this.options.selectableYear),
                selectableMonth = this._parseDateSection(this.options.selectableMonth),
                selectableDay = this._parseDateSection(this.options.selectableDay),

                selectableDayOfWeek = this._parseDateSection(this.options.selectableDayOfWeek);

            var _minDate, _maxDate, _curr, _isDay;
            switch (view) {
                case 'years':
                    _minDate = new Date(y, 0, 1); //the first day in year
                    _maxDate = new Date(y + 1, 0, 0); //the last day in year
                    _curr = [_minDate, _maxDate];
                    _isDay = false;
                    break;
                case 'months':
                    _minDate = new Date(y, m, 1); //the first day in month
                    _maxDate = new Date(y, m + 1, 0); //the last day in month
                    _curr = [_minDate, _maxDate];
                    _isDay = false;
                    break;
                case 'days':
                    _minDate = _maxDate = _curr = new Date(y, m, d);
                    _isDay = true;
                    break;
            }

            if (min && min > _maxDate) {
                isSelectable = false;
            }
            if (max && max < _minDate) {
                isSelectable = false;
            }

            if (isSelectable && selectableDate.length > 0) {
                isSelectable = this._judgeSections('date', _curr, selectableDate, _isDay);
            }

            if (isSelectable && selectableYear.length > 0) {
                isSelectable = this._judgeSections('block', y, selectableYear);
            }

            if (view === 'months' || view === 'days') {
                if (isSelectable && selectableMonth.length > 0) {
                    isSelectable = this._judgeSections('block', m, selectableMonth);
                }
            }

            if (view === 'days') {
                if (isSelectable && selectableDay.length > 0) {
                    isSelectable = this._judgeSections('block', d, selectableDay);
                }

                if (isSelectable && selectableDayOfWeek.length > 0) {
                    isSelectable = this._judgeSections('dayOfWeek', new Date(y, m, d), selectableDayOfWeek);
                }
            }

            return isSelectable;
        },
        _renderStatus: function(status) {
            var untouch = status[0],
                active = status[1],
                inRange = status[2],
                rangeUntouch = status[3],
                className = '';
            if (rangeUntouch === true) {
                className = ' ' + this.namespace + '_untouchable';
            } else {
                if (untouch === true) {
                    className = ' ' + this.namespace + '_untouchable';
                }
                if (inRange === true) {
                    className += ' ' + this.namespace + '_inRange';
                }
            }
            if (active === true) {
                className += ' ' + this.namespace + '_active';
            }
            return className;
        },
        _changeView: function(type, index) {
            switch (type) {
                case 'caption':
                    if (this.options.mode !== 'multiple') {
                        if (this.views[index] === 'days') {
                            this.views[index] = 'months';
                        } else if (this.views[index] === 'months') {
                            this.views[index] = 'years';
                        }
                    }
                    break;
                case 'content':
                    if (this.views[index] === 'years') {
                        this.views[index] = 'months';
                    } else if (this.views[index] === 'months') {
                        this.views[index] = 'days';
                    }
                    break;
                case 'higher':
                    if (this.options.mode !== 'multiple') {
                        if (this.views[index] === 'days') {
                            this.views[index] = 'months';
                        } else if (this.views[index] === 'months') {
                            this.views[index] = 'years';
                        }
                    }
                    break;
                case 'lower':
                    if (this.options.mode !== 'multiple') {
                        if (this.views[index] === 'years') {
                            this.views[index] = 'months';
                        } else if (this.views[index] === 'months') {
                            this.views[index] = 'days';
                        }
                    }
                    break;

            }
        },
        _setDate: function(obj, YTD, date) {
            if (typeof YTD === 'object') {
                for (var key in YTD) {
                    switch (key) {
                        case 'day':
                            obj.setDate(YTD[key]);
                            break;
                        case 'month':
                            obj.setMonth(YTD[key]);
                            break;
                        case 'year':
                            obj.setYear(YTD[key]);
                            break;
                    }
                }
            } else {
                switch (YTD) {
                    case 'day':
                        obj.setDate(date);
                        break;
                    case 'month':
                        obj.setMonth(date);
                        break;
                    case 'year':
                        obj.setYear(date);
                        break;
                }
            }
        },
        _formatDate: function(date, format) {
            date = new Date(date);
            var val = {
                d: date.getDate(),
                m: date.getMonth() + 1,
                yy: date.getFullYear().toString().substring(2),
                yyyy: date.getFullYear()
            };
            val.dd = (val.d < 10 ? '0' : '') + val.d;
            val.mm = (val.m < 10 ? '0' : '') + val.m;
            date = [];
            for (var i = 0, length = format.parts.length; i < length; i++) {
                date.push(val[format.parts[i]]);
            }
            return date.join(format.separator);
        },
        _stringSeparate: function(str, separator) {
            var re = new RegExp("[.\\" + separator + "\\s].*?"),
                _separator = str.match(re),
                parts = str.split(_separator);
            return parts;
        },
        _parseHtmlString: function(option, value) {
            var array = [],
                options = Plugin.defaults;
            if (typeof options[option] === 'object') {
                var parts = this._stringSeparate(value, ','),
                    sub_parts;
                for (var i = 0; i < parts.length; i++) {
                    sub_parts = this._stringSeparate(parts[i], '>');
                    if (sub_parts.length > 1) {
                        sub_parts = {
                            'from': sub_parts[0],
                            'to': sub_parts[1]
                        };
                    } else {
                        sub_parts = sub_parts[0];
                    }
                    array.push(sub_parts);
                }
                return array;
            } else {
                return value;
            }
        },
        _parseFormat: function(format) {
            var separator = format.match(/[.\/\-\s].*?/),
                parts = format.split(/\W+/) || parts;
            if (!parts || parts.length === 0) {
                throw new Error('Invalid date format.');
            }
            return {
                separator: separator,
                parts: parts
            };
        },
        _parseDate: function(data, format) {
            if (data !== null) {
                var date = new Date(),
                    day = date.getDate();
                switch (typeof(data)) {
                    case 'string':
                        if (data.length < 5) {
                            date.setHours(0, 0, 0, 0);
                            date.setDate(day + Number(data));
                        } else {
                            var parts = data.split(format.separator) || parts,
                                val;
                            date.setHours(0, 0, 0, 0);
                            if (parts.length === format.parts.length) {
                                for (var i = 0, length = format.parts.length; i < length; i++) {
                                    val = parseInt(parts[i], 10) || 1;
                                    if (val === '1') {
                                        return;
                                    }
                                    switch (format.parts[i]) {
                                        case 'dd':
                                        case 'd':
                                            date.setDate(val);
                                            break;
                                        case 'mm':
                                        case 'm':
                                            date.setMonth((val - 1), 1);
                                            break;
                                        case 'yy':
                                            date.setFullYear(2000 + val);
                                            break;
                                        case 'yyyy':
                                            date.setFullYear(val);
                                            break;
                                    }
                                }
                            }
                        }
                        break;
                    case 'number':
                        date.setHours(0, 0, 0, 0);
                        date.setDate(day + data);
                        break;
                }
                return date;
            } else {
                return null;
            }
        },
        _parseDateArr: function(arr, format) {
            var array = [],
                count = 0;
            for (var i = 0; i < arr.length; i++) {
                if (typeof(arr[i]) === 'string') {
                    array[count++] = this._parseDate(arr[i], format);
                } else if (typeof(arr[i]) === 'object') {
                    var obj = arr[i],
                        from, to;
                    for (var key in obj) {
                        switch (key) {
                            case 'from':
                                from = obj[key];
                                break;
                            case 'to':
                                to = obj[key];
                                break;
                        }
                    }
                    array[count++] = [this._parseDate(from, format), this._parseDate(to, format)];
                }
            }
            return array;
        },
        _parseDateSection: function(arr) {
            var array = [],
                count = 0;
            for (var i = 0; i < arr.length; i++) {
                if (typeof(arr[i]) === 'number') {
                    array[count++] = arr[i];
                } else if (typeof(arr[i]) === 'string') {
                    array[count++] = Number(arr[i]);
                } else if (typeof(arr[i]) === 'object') {
                    var obj = arr[i],
                        from, to;
                    for (var key in obj) {
                        switch (key) {
                            case 'from':
                                from = Number(obj[key]);
                                break;
                            case 'to':
                                to = Number(obj[key]);
                                break;
                        }
                    }
                    array[count++] = [from, to];
                }
            }
            return array;
        },
        _updateDate: function(i) {
        	 
//            this._date.currentDate[i].setDate(1);
            this._date.currentDate[i].setHours(0, 0, 0, 0);

            this._date.currentDay[i] = this._date.currentDate[i].getDate();
            this._date.currentMonth[i] = this._date.currentDate[i].getMonth();
            this._date.currentYear[i] = this._date.currentDate[i].getFullYear();

            this._date.currentMonthDate[i] = new Date(this._date.currentYear[i], this._date.currentMonth[i], 1, 0, 0, 0, 0);
            this._date.currentYearDate[i] = new Date(this._date.currentYear[i], 0, 1, 0, 0, 0, 0);

            if (this.mode !== 'multiple') {
                this._date.selectedDay[i] = this._date.selectedDate[i].getDate();
                this._date.selectedMonth[i] = this._date.selectedDate[i].getMonth();
                this._date.selectedYear[i] = this._date.selectedDate[i].getFullYear();

                this._date.selectedMonthDate[i] = new Date(this._date.selectedYear[i], this._date.selectedMonth[i], 1, 0, 0, 0, 0);
                this._date.selectedYearDate[i] = new Date(this._date.selectedYear[i], 0, 1, 0, 0, 0, 0);
            }
        },
        _position: function() {
            var calendar_height = this.$picker.outerHeight(),
                calendar_width = this.$picker.outerWidth(),
                container_height = this.$container.height() || window.innerHeight,
                input_top = this.$el.offset().top,
                input_left = this.$el.offset().left,
                input_height = this.$el.outerHeight(),
                input_width = this.$el.outerWidth(),
                winWidth = window.innerWidth,
                winHeight = window.innerHeight,
                scroll_left = this.$container.scrollLeft() || 0,
                left,
                top,
                position = this.options.position;

            if (this.isMobile) {
                left = (winWidth - calendar_width) / 2;
                top = (winHeight - calendar_height) / 2;
            } else {
                switch (position) {
                    case 'bottom':
                    case 'right':
                    case 'left':
                        if ((input_top + input_height + calendar_height) > (container_height)) {
                            if (position === 'bottom') {
                                position = 'top';
                            } else if (position = 'left') {
                                position = 'leftTop';
                            } else if (position = 'right') {
                                position = 'rightTop';
                            }
                        }
                        break;
                    case 'top':
                    case 'rightTop':
                    case 'leftTop':
                        if (input_top - calendar_height < 0) {
                            if (position === 'top') {
                                position = 'bottom';
                            } else if (position = 'leftTop') {
                                position = 'left';
                            } else if (position = 'rightTop') {
                                position = 'right';
                            }
                        }
                        break;
                }

                switch (position) {
                    case 'top':
                        left = input_left + scroll_left;
                        top = input_top - calendar_height;
                        break;
                    case 'right':
                        left = input_left + input_width + scroll_left;
                        top = input_top;
                        break;
                    case 'bottom':
                        left = input_left + scroll_left;
                        top = input_top + input_height;
                        break;
                    case 'left':
                        left = input_left - calendar_width + scroll_left;
                        top = input_top;
                        break;
                    case 'rightTop':
                        left = input_left + input_width + scroll_left;
                        top = input_top - calendar_height + input_height;
                        break;
                    case 'leftTop':
                        left = input_left - calendar_width + scroll_left;
                        top = input_top - calendar_height + input_height;
                        break;
                }
            }

            this.$picker.css({
                "left": left,
                "top": top
            });
        },
        _toggle: function() {
            if (this.showed) {
                this.pickerHide = true;
                this._blur();
            } else {
                this._focus();
            }
        },
        _focus: function() {
            if (this.options.displayMode === 'dropdown' && this.showed === false) {
                this.show();
            }
            if (this.hasKeyboard) {
                this._keyboard.init(this);
            }
        },
        _blur: function() {
            if (this.options.displayMode === 'dropdown') {
                if (this.pickerHide === true) {
                    this.hide();
                    this.pickerHide = false;
                }
            }
            if (this.hasKeyboard) {
                this._keyboard.destroy(this);
            }
        },
        _trigger: function(eventType) {
            var data = arguments.length > 1 ? Array.prototype.slice.call(arguments, 1).push(this) : this;
            // event
            this.$el.trigger(pluginName + '::' + eventType, data);

            // callback
            eventType = eventType.replace(/\b\w+\b/g, function(word) {
                return word.substring(0, 1).toUpperCase() + word.substring(1);
            });
            var onFunction = 'on' + eventType;
            var method_arguments = arguments.length > 1 ? Array.prototype.slice.call(arguments, 1) : undefined;
            if (typeof this.options[onFunction] === 'function') {
                this.options[onFunction].apply(this, method_arguments);
            }
        },
        _click: function(e) {
            var $target = $(e.target);

            if ($target.closest(this.$inputIcon).length === 0 && $target.closest(this.$picker).length === 0 && $target.closest(this.$el).length === 0 && this.options.alwaysShow === false) {
                if (this.isMobile) {
                    this.mobileCancel(0);
                } else {
                    this.hide();
                }
            } else if ($target.closest(this.$el).length !== 1 && $target.closest(this.$picker).length === 1) {
                var _target = $(e.target).closest('div');
                var _targetSpan = $(e.target).closest('span');

                if (_target.parent('.' + this.namespace + '-header').length === 1) {
                    var i = _target.parents('.' + this.namespace + '-content').index();
                    switch (_target[0].className) {
                        case this.namespace + '-caption':
                            this._changeView('caption', i);
                            this._manageViews(i);
                            break;
                        case this.namespace + '-prev':
                            this.prev(i);
                            break;
                        case this.namespace + '-next':
                            this.next(i);
                            break;
                    }
                }

                if (_targetSpan.length === 1) {
                    var j = _targetSpan.parents('.' + this.namespace + '-content').index();

                    if (!_targetSpan.hasClass(this.namespace + '_otherMonth') && !_targetSpan.hasClass(this.namespace + '_untouchable') && _targetSpan.parent('.' + this.namespace + '-head').length !== 1) {

                        this._changeValue(_targetSpan, j);
                        this._changeView('content', j);
                        this._updateDate(j);

                        switch (this.mode) {
                            case 'single':
                                if (this.views[j] === 'days') {
                                    this.selected = true;
                                }
                                this._manageViews(j);
                                break;
                            case 'range':
                                this._manageViews(0);
                                this._manageViews(1);
                                break;
                            case 'multiple':
                                this._manageViews(j - 1);
                                this._manageViews(j);
                                this._manageViews(j + 1);
                                break;
                        }
                        if (!this.isMobile) {
                            this._setValue();
                        }
                    }
                }

                if (_target.parent('.' + this.namespace + '-buttons').length === 1) {
                    var k = _target.parents('.' + this.namespace + '-content').index(),
                        flag = _target[0].className === this.namespace + '-button-save' ? true : false;

                    if (flag) {
                        this.mobileEnter(k);
                    } else {
                        this.mobileCancel(k);
                    }
                }
                if (!this.isMobile) {
                    if (this.selected === true && this.options.alwaysShow === false && this.options.onceClick === true) {
                        this.hide();
                    } else {
                        if (this.options.displayMode === 'dropdown') {
                            this.$el.focus();
                        }
                    }
                }
            }
            e.preventDefault();
        },
        _changeValue: function(target, i) {
            var newVal = '',
                newDate = '',
                self = this;
            switch (this.views[i]) {
                case 'years':
                    newVal = parseInt(target.text(), 10);
                    this._date.currentDate[i].setYear(newVal);
                    break;
                case 'months':
                    newVal = Number(target.attr('class').match(/month\-([0-9]+)/)[1]);
                    this._date.currentDate[i].setMonth(newVal);
                    break;
                case 'days':
                    newVal = parseInt(target.text(), 10);
                    newDate = new Date(this._date.currentYear[i], this._date.currentMonth[i], newVal, 0, 0, 0, 0);
                    switch (this.options.mode) {
                        case 'single':
                        case 'range':
                            this._date.selectedDate[i] = newDate;
                            break;
                        case 'multiple':
                            var date = Date.parse(newDate);
                            if ($.inArray(date, this._date.selectedDate) > -1) {
                                $.each(this._date.selectedDate, function(nr, data) {
                                    if (data === date) {
                                        self._date.selectedDate.splice(nr, 1);
                                    }
                                });
                            } else {
                                if (this._date.selectedDate.length < this.options.multipleSize) {
                                    this._date.selectedDate.push(date);
                                }
                            }
                            break;
                    }
                    break;
            }
        },
        _setValue: function() {
            switch (this.mode) {
                case 'single':
                    var formated = this._formatDate(this._date.selectedDate[0], this.outputFormat);
                    this.$el.val(formated);
                    break;
                case 'range':
                    var formatedStart = this._formatDate(this._date.selectedDate[0], this.outputFormat),
                        formatedEnd = this._formatDate(this._date.selectedDate[1], this.outputFormat);
                    this.$el.val(formatedStart + ' ' + this.options.rangeSeparator + ' ' + formatedEnd);
                    break;
                case 'multiple':
                    var val = '',
                        _formated;
                    for (var j = 0; j < this._date.selectedDate.length; j++) {
                        _formated = this._formatDate(this._date.selectedDate[j], this.outputFormat);
                        if (val.length === 0) {
                            val += _formated;
                        } else {
                            val += (this.options.multipleSeparator + _formated);
                        }
                    }
                    this.$el.val(val);
                    break;
            }
            this._trigger('change', this.getDate('yyyy-mm-dd'), this.options.name, pluginName);
            this.oldValue = this.$el.val();
        },
        _prevent: function(e) {
            if (e.preventDefault) {
                e.preventDefault();
            } else {
                e.returnvalue = false;
            }
        },
        _removeEvents: function() {
            if (this.options.displayMode === 'inline') {
                this.picker.off('click.picke');
            } else {
                $doc.off('click.' + this.flag);
            }

            this.$el.off('focus');
            this.$el.off('blur');
        },
        prev: function(i, isTurning) {
            this.touchflag = false;
            var date = this._date.currentDate[i];
            switch (this.views[i]) {
                case 'days':
                    var prevMonthDays;
                    if (this.mode === 'multiple') {
                        if (isTurning) {
                            if (this.focused === 0) {
                                for (var j = 0; j < this.calendarsNum; j++) {
                                    this._date.currentDate[j].setMonth(this._date.currentMonth[j] - 1);
                                    this._updateDate(j);
                                    this._manageViews(j);
                                }
                            } else {
                                --this.focused;
                                this._manageViews(i);
                                this._manageViews(i - 1);
                            }
                        } else {
                            prevMonthDays = new Date(date.getFullYear(), date.getMonth(), 0).getDate();
                            if (this._date.focusDate.getDate() > prevMonthDays) {
                                this._date.focusDate.setDate(prevMonthDays);
                            }
                            this._date.focusDate.setMonth(this._date.focusDate.getMonth() - 1);
                            for (var k = 0; k < this.calendarsNum; k++) {
                                this._date.currentDate[k].setMonth(this._date.currentMonth[k] - 1);
                                this._updateDate(k);
                                this._manageViews(k);
                            }
                        }
                    } else {
                        date.setMonth(this._date.currentMonth[i] - 1);
                        if (this.hasKeyboard) {
                            prevMonthDays = new Date(date.getFullYear(), date.getMonth(), 0).getDate();
                            if (this._date.focusDate[i].getDate() > prevMonthDays) {
                                this._date.focusDate[i].setDate(prevMonthDays);
                            }
                            this._date.focusDate[i] = new Date(date.getFullYear(), date.getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                        }
                    }
                    break;
                case 'months':
                    date.setYear(this._date.currentYear[i] - 1);
                    if (this.hasKeyboard) {
                        this._date.focusDate[i] = new Date(date.getFullYear(), this._date.focusDate[i].getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                    }
                    break;
                case 'years':
                    date.setYear(this._date.currentYear[i] - 12);
                    if (this.hasKeyboard && isTurning) {
                        this._date.focusDate[i] = new Date(this._date.focusDate[i].getFullYear() - 12, this._date.focusDate[i].getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                    }
                    break;
            }
            this._updateDate(i);
            this._manageViews(i);
        },
        next: function(i, isTurning) {
            this.touchflag = false;
            var date = this._date.currentDate[i];
            switch (this.views[i]) {
                case 'days':
                    var nextMonthDays;
                    if (this.mode === 'multiple') {
                        if (isTurning) {
                            if (this.focused === this.calendarsNum - 1) {
                                for (var j = 0; j < this.calendarsNum; j++) {
                                    this._date.currentDate[j].setMonth(this._date.currentMonth[j] + 1);
                                    this._updateDate(j);
                                    this._manageViews(j);
                                }
                            } else {
                                ++this.focused;
                                this._manageViews(i);
                                this._manageViews(i + 1);
                            }
                        } else {
                            nextMonthDays = new Date(date.getFullYear(), date.getMonth() + 2, 0).getDate();
                            if (this._date.focusDate.getDate() > nextMonthDays) {
                                this._date.focusDate.setDate(nextMonthDays);
                            }
                            this._date.focusDate.setMonth(this._date.focusDate.getMonth() + 1);
                            for (var k = 0; k < this.calendarsNum; k++) {
                                this._date.currentDate[k].setMonth(this._date.currentMonth[k] + 1);
                                this._updateDate(k);
                                this._manageViews(k);
                            }
                        }
                    } else {
                        date.setMonth(this._date.currentMonth[i] + 1);

                        if (this.hasKeyboard) {
                            nextMonthDays = new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
                            if (this._date.focusDate[i].getDate() > nextMonthDays) {
                                this._date.focusDate[i].setDate(nextMonthDays);
                            }
                            this._date.focusDate[i] = new Date(date.getFullYear(), date.getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                        }
                    }
                    break;
                case 'months':
                    date.setYear(this._date.currentYear[i] + 1);
                    if (this.hasKeyboard) {
                        this._date.focusDate[i] = new Date(date.getFullYear(), this._date.focusDate[i].getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                    }
                    break;
                case 'years':
                    date.setYear(this._date.currentYear[i] + 12);
                    if (this.hasKeyboard && isTurning) {
                        this._date.focusDate[i] = new Date(this._date.focusDate[i].getFullYear() + 12, this._date.focusDate[i].getMonth(), this._date.focusDate[i].getDate(), 0, 0, 0, 0);
                    }
                    break;
            }
            this._updateDate(i);
            this._manageViews(i);
        },
        mobilePrev: function(index) {
            this.calendars.eq(index).removeClass(this.namespace + '_show');
            this.calendars.eq(index - 1).addClass(this.namespace + '_show');
        },
        mobileNext: function(index) {
            this.calendars.eq(index).removeClass(this.namespace + '_show');
            this.calendars.eq(index + 1).addClass(this.namespace + '_show');
        },
        mobileInteDate: function(index) {
            var self = this;
            if (this.mode === 'multiple') {
                if (this._date.selectedDate.length > 0) {
                    self._date.currentDate[0] = new Date(this._date.selectedDate[0]);
                }
            } else {
                this._date.currentDate[index] = new Date(this._date.selectedDate[index]);
            }

            this.views[index] = 'days';
            this._updateDate(index);
        },
        mobileEnter: function(index) {
            if (this.mode === 'range' && index === 0) {
                this.mobileNext(index);
                this.views[index] = 'days';
            } else {
                this.mobileInteDate(index);
                this._setValue();
                this.hide();
            }
            this._manageViews(index);
        },
        mobileCancel: function(index) {
            if (index === 1) {
                this.mobilePrev(index);
                this.views[index] = 'days';
            } else {
                this.dateTransform(this._date.cache, this._date);
                this.mobileInteDate(index);
                this.hide();
            }
            this._manageViews(index);
        },

        dateTransform: function(fromDate, toDate) {
            var self = this;
            toDate.currentDate = [];
            toDate.selectedDate = [];
            $.each(fromDate.currentDate, function(n, v) {
                toDate.currentDate[n] = new Date(v);
            });

            $.each(fromDate.selectedDate, function(n, v) {
                var date = new Date(v);
                toDate.selectedDate[n] = self.mode === 'multiple' ? Date.parse(date) : date;
            });
        },
        show: function() {
            var self = this;
            if (this.isMobile) {
                this.dateTransform(this._date, this._date.cache);
            }

            if (this.options.displayMode === 'inline') {
                this._trigger('beforeShow');
                this.$picker.on('mouseDown.' + this.flag, function(e) {
                    self._prevent(e);
                });
                this.$picker.on('click.' + this.flag, function(e) {
                    self._click.call(self, e);
                });
            } else {
                if (this.showed === false) {
                    this._trigger('beforeShow');
                    this.$inputWrap.addClass(this.namespace + '_active');
                    // this.$picker.removeClass(this.namespace + '_hide');
                    this.$picker.addClass(this.namespace + '_show');

                    if (this.isMobile) {
                        this.calendars.eq(0).addClass(this.namespace + '_show');
                        if (this.mode === 'range') {
                            this.calendars.eq(1).removeClass(this.namespace + '_show');
                        }

                        $('body').append(this.$cover).css('overflow', 'hidden');
                        //Prevent horizontal scroll for ios
                        $doc.on('scrollstart.' + this.flag, function(e) {
                            e.preventDefault();
                        });
                        $doc.on('tap.' + this.flag, function(e) {
                            self._click.call(self, e);
                        });
                        var handle = function(e) {
                            var startX = e.swipestart.coords[0],
                                stopX = e.swipestop.coords[0];

                            if (stopX > startX) {
                                self.prev.call(self, 0);
                            } else if (stopX < startX) {
                                self.next.call(self, 0);
                            }

                            $doc.one('swipe.' + self.flag, handle);
                        };

                        $doc.one('swipe.' + this.flag, handle);
                    } else {
                        $doc.on('click.' + this.flag, function(e) {
                            self._click.call(self, e);
                        });
                    }


                    this._position();
                    this.showed = true;

                    $win.on('resize.' + this.flag, function() {
                        self._position();
                    });
                    // this.$el.focus();
                    this.$picker.on('mousedown.' + this.flag, function(e) {
                        self._prevent(e);
                    });
                }
            }
            this._trigger('show');
            return this;
        },
        hide: function() {
            if (this.showed === true) {
                this._trigger('beforeHide');
                this.selected = false;
                this.$inputWrap.removeClass(this.namespace + '_active');
                this.$picker.removeClass(this.namespace + '_show');
                this.showed = false;
                this.$picker.off('mousedown.' + this.flag);
                $doc.off('click.' + this.flag);
                $win.off('resize.' + this.flag);
                if (this.isMobile) {
                    $('body').css('overflow', 'auto');
                    this.$cover.remove();
                    $doc.off('click.' + this.flag + ' tap.' + this.flag + ' scrollstart.' + this.flag + ' swipe.' + this.flag);
                }

                this.$el.blur();
                this._trigger('hide');
            }
            return this;
        },
        getWrap: function() {
            return this.picker;
        },
        getInput: function() {
            return this.$el;
        },
        getDate: function(format) {
            if (format === undefined) {
                return this._date.selectedDate;
            } else {
                var _format = this._parseFormat(format),
                    formated = [];
                for (var i = 0; i < this._date.selectedDate.length; i++) {
                    formated[i] = this._formatDate(this._date.selectedDate[i], _format);
                }
                return formated;
            }
        },
        multipleClear: function() {
            this._date.selectedDate = [];
            for (var i = 0; i < this.calendarsNum; i++) {
                this._manageViews(i);
            }
            this._setValue();
        },

        destroy: function() {
            this.$el.removeData('asDatepicker');
            this._removeEvents();
            this.$picker.remove();
        },
        update: function(_options) {
            if (typeof _options !== 'undefined') {
                for (var m in _options) {
                    this.options[m] = _options[m];
                }
            }
            this._removeEvents();
            this.$picker.remove();
            this._init();
        },
        reset: function(_options) {
            for (var m in this.defaultOptions) {
                this.options[m] = this.defaultOptions[m];
            }
            if (typeof _options !== 'undefined') {
                for (var n in _options) {
                    this.options[n] = _options[n];
                }
            }
            this._removeEvents();
            this.$picker.remove();
            this._init();
        },


        _keyboard: {
            init: function(self) {
                this.attach(self, this.gather(self));
            },
            destroy: function(self) {
                if (self.options.displayMode === 'dropdown') {
                    self.$el.off('keydown.dropdown');
                } else {
                    self.$picker.off('keydown.inline');
                }
                self.bound = false;
            },
            keys: function() {
                return {
                    'LEFT': 37,
                    'UP': 38,
                    'RIGHT': 39,
                    'DOWN': 40,
                    'ENTER': 13,
                    'ESC': 27,
                    'CTRL': 17,
                    'ALT': 18
                };
            },
            prevDate: function() {
                var i = this.focused,
                    date = this.mode === 'multiple' ? this._date.focusDate : this._date.focusDate[i],
                    hasLocked = this.mode === 'multiple' ? false : this.calendarPrevs.eq(this.focused).hasClass(this.namespace + '_blocked');

                switch (this.views[i]) {
                    case 'days':
                        if (Date.parse(date) === Date.parse(this._date.currentDate[i])) {
                            if (!hasLocked) {
                                date.setDate(date.getDate() - 1);
                                this.prev(i, true);
                            }
                        } else {
                            date.setDate(date.getDate() - 1);
                            this._manageViews(i);
                        }
                        break;
                    case 'months':
                        if (date.getMonth() === 0) {
                            if (!hasLocked) {
                                date.setMonth(date.getMonth() - 1);
                                this.prev(i);
                            }
                        } else {
                            date.setMonth(date.getMonth() - 1);
                            this._manageViews(i);
                        }
                        break;
                    case 'years':
                        if (date.getFullYear() === this._date.currentYear[i] - 7) {
                            if (!hasLocked) {
                                date.setFullYear(date.getFullYear() - 1);
                                this.prev(i);
                            }
                        } else {
                            date.setFullYear(date.getFullYear() - 1);
                            this._manageViews(i);
                        }
                        break;
                }
            },
            nextDate: function() {
                var i = this.focused,
                    date = this.mode === 'multiple' ? this._date.focusDate : this._date.focusDate[i],
                    hasLocked = this.mode === 'multiple' ? false : this.calendarNexts.eq(this.focused).hasClass(this.namespace + '_blocked');
                switch (this.views[i]) {
                    case 'days':
                        if (Date.parse(date) === Date.parse(new Date(this._date.currentYear[i], this._date.currentMonth[i] + 1, 0))) {
                            if (!hasLocked) {
                                date.setDate(date.getDate() + 1);
                                this.next(i, true);
                            }
                        } else {
                            date.setDate(date.getDate() + 1);
                            this._manageViews(i);
                        }
                        break;
                    case 'months':
                        if (date.getMonth() === 11) {
                            if (!hasLocked) {
                                date.setMonth(date.getMonth() + 1);
                                this.next(i);
                            }
                        } else {
                            date.setMonth(date.getMonth() + 1);
                            this._manageViews(i);
                        }
                        break;
                    case 'years':
                        if (date.getFullYear() === this._date.currentYear[i] + 4) {
                            if (!hasLocked) {
                                date.setFullYear(date.getFullYear() + 1);
                                this.next(i);
                            }
                        } else {
                            date.setFullYear(date.getFullYear() + 1);
                            this._manageViews(i);
                        }
                        break;
                }
            },
            upLine: function() {
                var i = this.focused,
                    date = this.mode === 'multiple' ? this._date.focusDate : this._date.focusDate[i],
                    hasLocked = this.mode === 'multiple' ? false : this.calendarPrevs.eq(this.focused).hasClass(this.namespace + '_blocked');
                switch (this.views[i]) {
                    case 'days':
                        if (new Date(date.getFullYear(), date.getMonth(), date.getDate() - 7).setDate(1) ===
                            new Date(this._date.currentDate[i]).setMonth(this._date.currentMonth[i] - 1)) {
                            if (!hasLocked) {
                                date.setDate(date.getDate() - 7);
                                this.prev(i, true);
                            }
                        } else {
                            date.setDate(date.getDate() - 7);
                            this._manageViews(i);
                        }
                        break;
                    case 'months':
                        if (date.getMonth() === 0 || date.getMonth() === 1 || date.getMonth() === 2) {
                            if (!hasLocked) {
                                date.setMonth(date.getMonth() - 3);
                                this.prev(i);
                            }
                        } else {
                            date.setMonth(date.getMonth() - 3);
                            this._manageViews(i);
                        }
                        break;
                    case 'years':
                        if (date.getFullYear() < this._date.currentYear[i] - 4) {
                            if (!hasLocked) {
                                date.setFullYear(date.getFullYear() - 3);
                                this.prev(i);
                            }
                        } else {
                            date.setFullYear(date.getFullYear() - 3);
                            this._manageViews(i);
                        }
                        break;
                }
            },
            downLine: function() {
                var i = this.focused,
                    date = this.mode === 'multiple' ? this._date.focusDate : this._date.focusDate[i],
                    hasLocked = this.mode === 'multiple' ? false : this.calendarNexts.eq(this.focused).hasClass(this.namespace + '_blocked');
                switch (this.views[i]) {
                    case 'days':
                        if (new Date(date.getFullYear(), date.getMonth(), date.getDate() + 7).setDate(1) ===
                            new Date(this._date.currentDate[i]).setMonth(this._date.currentMonth[i] + 1)) {
                            if (!hasLocked) {
                                date.setDate(date.getDate() + 7);
                                this.next(i, true);
                            }
                        } else {
                            date.setDate(date.getDate() + 7);
                            this._manageViews(i);
                        }
                        break;
                    case 'months':
                        if (date.getMonth() === 9 || date.getMonth() === 10 || date.getMonth() === 11) {
                            if (!hasLocked) {
                                date.setMonth(date.getMonth() + 3);
                                this.next(i);
                            }
                        } else {
                            date.setMonth(date.getMonth() + 3);
                            this._manageViews(i);
                        }
                        break;
                    case 'years':
                        if (date.getFullYear() > this._date.currentYear[i] + 1) {
                            if (!hasLocked) {
                                date.setFullYear(date.getFullYear() + 3);
                                this.next(i);
                            }
                        } else {
                            date.setFullYear(date.getFullYear() + 3);
                            this._manageViews(i);
                        }
                        break;
                }
            },
            prevPage: function() {
                if (this.mode === 'multiple') {
                    this.prev(this.focused);
                } else {
                    if (!this.calendarPrevs.eq(this.focused).hasClass(this.namespace + '_blocked')) {
                        this.prev(this.focused, true);
                    }
                }
            },
            nextPage: function() {
                if (this.mode === 'multiple') {
                    this.next(this.focused);
                } else {
                    if (!this.calendarNexts.eq(this.focused).hasClass(this.namespace + '_blocked')) {
                        this.next(this.focused, true);
                    }
                }
            },
            higherView: function() {
                if (this.mode !== 'multiple') {
                    var i = this.focused;
                    this._changeView('higher', i);
                    this._manageViews(i);
                }
            },
            prevCalendar: function() {
                if (this.mode !== 'multiple') {
                    var len = this.calendars.length;
                    if (--this.focused < 0) {
                        this.focused = len;
                    }
                }
            },
            nextCalendar: function() {
                if (this.mode !== 'multiple') {
                    var len = this.calendars.length;
                    if (++this.focused >= len) {
                        this.focused = 0;
                    }
                }
            },
            updateValue: function(self) {
                var i = self.focused,
                    date = self.mode === 'multiple' ? self._date.focusDate : self._date.focusDate[i];
                if (!self.calendars.eq(i).find('.' + self.namespace + '_focus').hasClass(self.namespace + '_untouchable')) {
                    switch (self.views[i]) {
                        case 'days':
                            switch (self.options.mode) {
                                case 'single':
                                case 'range':
                                    self._date.selectedDate[i] = new Date(date);
                                    break;
                                case 'multiple':
                                    var _date = Date.parse(new Date(date));
                                    if ($.inArray(_date, self._date.selectedDate) > -1) {
                                        $.each(self._date.selectedDate, function(nr, data) {
                                            if (data === _date) {
                                                self._date.selectedDate.splice(nr, 1);
                                                return false;
                                            }
                                        });
                                    } else {
                                        self._date.selectedDate.push(_date);
                                    }
                                    break;
                            }
                            break;
                        case 'months':
                            self._date.currentDate[i].setMonth(date.getMonth());
                            self.views[i] = 'days';
                            break;
                        case 'years':
                            self._date.currentDate[i].setFullYear(date.getFullYear());
                            self.views[i] = 'months';
                            break;
                    }
                    self._updateDate(i);
                    if (self.mode === 'range') {
                        self._manageViews(0);
                        self._manageViews(1);
                    } else if (self.mode === 'multiple') {
                        self._manageViews(i - 1);
                        self._manageViews(i);
                        self._manageViews(i + 1);
                    } else {
                        self._manageViews(i);
                    }
                    self._setValue();
                }
            },
            enter: function() {
                var inputValue = this.$el.val(),
                    self = this,
                    judge;
                if (inputValue === this.oldValue || this.oldValue === '') {
                    this._keyboard.updateValue(this);
                } else {
                    var parts;
                    switch (this.mode) {
                        case 'single':
                            var _date = Date.parse(inputValue);
                            if (_date) {
                                this._date.selectedDate[0] = new Date(_date);
                                this._date.currentDate[0] = new Date(this._date.selectedDate[0]);
                                this._updateDate(0);
                                this._manageViews(0);
                            }
                            break;
                        case 'range':
                            parts = this._stringSeparate(inputValue, this.options.rangeSeparator);
                            var from = Date.parse(parts[0]),
                                to = Date.parse(parts[1]);
                            if (parts.length === 2) {
                                judge = true;
                                if (from && to) {
                                    if (from > to) {
                                        judge = false;
                                    }
                                } else {
                                    judge = false;
                                }
                            } else {
                                judge = false;
                            }

                            if (judge === true) {
                                this._date.selectedDate[0] = new Date(from);
                                this._date.selectedDate[1] = new Date(to);
                                for (var i = 0; i < 2; i++) {
                                    this._date.currentDate[i] = new Date(this._date.selectedDate[i]);
                                    this._updateDate(i);
                                    this._manageViews(i);
                                }
                            } else {
                                this._keyboard.updateValue(this);
                            }
                            break;
                        case 'multiple':
                            parts = this._stringSeparate(inputValue, this.options.multipleSeparator);
                            var _parts = [];
                            judge = true;
                            for (var j = 0; j < parts.length; j++) {
                                _parts.push(Date.parse(parts[j]));
                                if (!Date.parse(parts[j])) {
                                    judge = false;
                                }
                            }
                            if (judge === true) {
                                this._date.selectedDate = [];
                                for (var k = 0; k < _parts.length; k++) {
                                    if ($.inArray(_parts[k], this._date.selectedDate) > -1) {
                                        $.each(this._date.selectedDate, function(nr, data) {
                                            if (data === _parts[k]) {
                                                self._date.selectedDate.splice(nr, 1);
                                            }
                                        });
                                    } else {
                                        this._date.selectedDate.push(_parts[k]);
                                    }
                                }
                                for (var m = 0; m < this.calendarsNum; m++) {
                                    this._updateDate(m);
                                    this._manageViews(m);
                                }
                            } else {
                                this._keyboard.updateValue(this);
                            }
                            break;
                    }
                }
                this._setValue();
            },
            esc: function() {
                this.$el.blur();
                this.hide();
            },
            tab: function() {
                this.pickerHide = true;
            },
            gather: function(self) {
                return {
                    left: $.proxy(this.prevDate, self),
                    up: $.proxy(this.upLine, self),
                    right: $.proxy(this.nextDate, self),
                    down: $.proxy(this.downLine, self),
                    ctrl_left: $.proxy(this.prevPage, self),
                    ctrl_up: $.proxy(this.higherView, self),
                    ctrl_right: $.proxy(this.nextPage, self),
                    // ctrl_down: $.proxy(this.lowerView, self),
                    alt_left: $.proxy(this.prevCalendar, self),
                    alt_right: $.proxy(this.nextCalendar, self),
                    enter: $.proxy(this.enter, self),
                    esc: $.proxy(this.esc, self)
                };
            },
            press: function(e) {
                var key = e.keyCode || e.which,
                    map;

                if (e.ctrlKey) {
                    e.preventDefault();
                    map = this.map[17];
                } else if (e.altKey) {
                    e.preventDefault();
                    map = this.map[18];
                } else {
                    map = this.map;
                }
                if (key === 9) {
                    this._keyboard.tab.call(this);
                }

                if (key in map && typeof map[key] === 'function') {
                    e.preventDefault();
                    map[key].call(this);
                }
            },
            attach: function(self, map) {
                var key, _self = this;
                for (key in map) {
                    if (map.hasOwnProperty(key)) {
                        var uppercase = [],
                            parts = self._stringSeparate(key, '_'),
                            len = parts.length;

                        if (len === 1) {
                            uppercase[0] = parts[0].toUpperCase();
                            self.map[this.keys()[uppercase[0]]] = map[key];
                        } else {
                            for (var i = 0; i < parts.length; i++) {
                                uppercase[i] = parts[i].toUpperCase();
                                if (i === 0) {
                                    if (self.map[this.keys()[uppercase[0]]] === undefined) {
                                        self.map[this.keys()[uppercase[0]]] = {};
                                    }
                                } else {
                                    self.map[this.keys()[uppercase[0]]][this.keys()[uppercase[i]]] = map[key];
                                }
                            }
                        }
                    }
                }
                if (!self.bound) {
                    self.bound = true;
                    if (self.options.displayMode === 'dropdown') {
                        self.$el.on('keydown.dropdown', function(e) {
                            _self.press.call(self, e);
                        });
                    } else {
                        self.$picker.on('keydown.inline', function(e) {
                            _self.press.call(self, e);
                        });
                    }
                }
            }
        }
    };
    Plugin.defaults = defaults;
    $.fn[pluginName] = function(options) {
        if (typeof options === 'string') {
            var method = options;
            var method_arguments = Array.prototype.slice.call(arguments, 1);
            if (/^\_/.test(method)) {
                return false;
            } else if (/^(getWrap|getInput|getDate)$/.test(method)) {
                var api = this.first().data(pluginName);
                if (api && typeof api[method] === 'function') {
                    return api[method].apply(api, method_arguments);
                }
            } else {
                return this.each(function() {
                    var api = $.data(this, pluginName);
                    if (api && typeof api[method] === 'function') {
                        api[method].apply(api, method_arguments);
                    }
                });
            }
        } else {
            return this.each(function() {
                if (!$.data(this, pluginName)) {
                    $.data(this, pluginName, new Plugin(this, options));
                }
            });
        }
    };
})(jQuery, document, window);
