/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Arabic language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['ar'] =
{
	/**
	 * The language reading direction. Possible values are "rtl" for
	 * Right-To-Left languages (like Arabic) and "ltr" for Left-To-Right
	 * languages (like English).
	 * @default 'ltr'
	 */
	dir : 'rtl',

	/*
	 * Screenreader titles. Please note that screenreaders are not always capable
	 * of reading non-English words. So be careful while translating it.
	 */
	editorTitle : 'Rich text editor, %1', // MISSING
	editorHelp : 'Press ALT 0 for help', // MISSING

	// ARIA descriptions.
	toolbars	: 'Editor toolbars', // MISSING
	editor		: 'Rich Text Editor', // MISSING

	// Toolbar buttons without dialogs.
	source			: 'المصدر',
	newPage			: 'صفحة جديدة',
	save			: 'حفظ',
	preview			: 'معاينة الصفحة',
	cut				: 'قص',
	copy			: 'نسخ',
	paste			: 'لصق',
	print			: 'طباعة',
	underline		: 'تسطير',
	bold			: 'غامق',
	italic			: 'مائل',
	selectAll		: 'تحديد الكل',
	removeFormat	: 'إزالة التنسيقات',
	strike			: 'يتوسطه خط',
	subscript		: 'منخفض',
	superscript		: 'مرتفع',
	horizontalrule	: 'خط فاصل',
	pagebreak		: 'إدخال صفحة جديدة',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'إزالة رابط',
	undo			: 'تراجع',
	redo			: 'إعادة',

	// Common messages and labels.
	common :
	{
		browseServer	: 'تصفح',
		url				: 'الرابط',
		protocol		: 'البروتوكول',
		upload			: 'رفع',
		uploadSubmit	: 'أرسل',
		image			: 'صورة',
		flash			: 'فلاش',
		form			: 'نموذج',
		checkbox		: 'خانة إختيار',
		radio			: 'زر اختيار',
		textField		: 'مربع نص',
		textarea		: 'مساحة نصية',
		hiddenField		: 'إدراج حقل خفي',
		button			: 'زر ضغط',
		select			: 'اختار',
		imageButton		: 'زر صورة',
		notSet			: '<بدون تحديد>',
		id				: 'الرقم',
		name			: 'الاسم',
		langDir			: 'إتجاه النص',
		langDirLtr		: 'اليسار لليمين (LTR)',
		langDirRtl		: 'اليمين لليسار (RTL)',
		langCode		: 'رمز اللغة',
		longDescr		: 'الوصف التفصيلى',
		cssClass		: 'فئات التنسيق',
		advisoryTitle	: 'عنوان التقرير',
		cssStyle		: 'نمط',
		ok				: 'موافق',
		cancel			: 'إلغاء الأمر',
		close			: 'أغلق',
		preview			: 'استعراض',
		generalTab		: 'عام',
		advancedTab		: 'متقدم',
		validateNumberFailed : 'لايوجد نتيجة',
		confirmNewPage	: 'ستفقد أي متغييرات اذا لم تقم بحفظها اولا. هل أنت متأكد أنك تريد صفحة جديدة؟',
		confirmCancel	: 'بعض الخيارات قد تغيرت. هل أنت متأكد من إغلاق مربع النص؟',
		options			: 'خيارات',
		target			: 'Target', // MISSING
		targetNew		: 'New Window (_blank)', // MISSING
		targetTop		: 'Topmost Window (_top)', // MISSING
		targetSelf		: 'Same Window (_self)', // MISSING
		targetParent	: 'Parent Window (_parent)', // MISSING
		langDirLTR		: 'Left to Right (LTR)', // MISSING
		langDirRTL		: 'Right to Left (RTL)', // MISSING
		styles			: 'Style', // MISSING
		cssClasses		: 'Stylesheet Classes', // MISSING
		width			: 'العرض',
		height			: 'الإرتفاع',
		align			: 'محاذاة',
		alignLeft		: 'يسار',
		alignRight		: 'يمين',
		alignCenter		: 'وسط',
		alignTop		: 'أعلى',
		alignMiddle		: 'وسط',
		alignBottom		: 'أسفل',
		invalidHeight	: 'الارتفاع يجب أن يكون عدداً.',
		invalidWidth	: 'العرض يجب أن يكون عدداً.',
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, غير متاح</span>'
	},

	contextmenu :
	{
		options : 'Context Menu Options' // MISSING
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'إدراج  خاص.ِ',
		title		: 'اختر الخواص',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'رابط',
		other 		: '<أخرى>',
		menu		: 'تحرير رابط',
		title		: 'إرتباط تشعبي',
		info		: 'معلومات الرابط',
		target		: 'هدف الرابط',
		upload		: 'رفع',
		advanced	: 'متقدم',
		type		: 'نوع الربط',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'مكان في هذا المستند',
		toEmail		: 'بريد إلكتروني',
		targetFrame		: '<إطار>',
		targetPopup		: '<نافذة منبثقة>',
		targetFrameName	: 'اسم الإطار المستهدف',
		targetPopupName	: 'اسم النافذة المنبثقة',
		popupFeatures	: 'خصائص النافذة المنبثقة',
		popupResizable	: 'قابلة التشكيل',
		popupStatusBar	: 'شريط الحالة',
		popupLocationBar: 'شريط العنوان',
		popupToolbar	: 'شريط الأدوات',
		popupMenuBar	: 'القوائم الرئيسية',
		popupFullScreen	: 'ملئ الشاشة (IE)',
		popupScrollBars	: 'أشرطة التمرير',
		popupDependent	: 'تابع (Netscape)',
		popupLeft		: 'التمركز لليسار',
		popupTop		: 'التمركز للأعلى',
		id				: 'هوية',
		langDir			: 'إتجاه النص',
		langDirLTR		: 'اليسار لليمين (LTR)',
		langDirRTL		: 'اليمين لليسار (RTL)',
		acccessKey		: 'مفاتيح الإختصار',
		name			: 'الاسم',
		langCode			: 'كود النص',
		tabIndex			: 'الترتيب',
		advisoryTitle		: 'عنوان التقرير',
		advisoryContentType	: 'نوع التقرير',
		cssClasses		: 'فئات التنسيق',
		charset			: 'ترميز المادة المطلوبة',
		styles			: 'نمط',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'اختر علامة مرجعية',
		anchorName		: 'حسب الاسم',
		anchorId			: 'حسب رقم العنصر',
		emailAddress		: 'عنوان البريد إلكتروني',
		emailSubject		: 'موضوع الرسالة',
		emailBody		: 'محتوى الرسالة',
		noAnchors		: '(لا توجد علامات مرجعية في هذا المستند)',
		noUrl			: 'من فضلك أدخل عنوان الموقع الذي يشير إليه الرابط',
		noEmail			: 'من فضلك أدخل عنوان البريد الإلكتروني'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'إشارة مرجعية',
		menu		: 'تحرير الإشارة المرجعية',
		title		: 'خصائص الإشارة المرجعية',
		name		: 'اسم الإشارة المرجعية',
		errorName	: 'الرجاء كتابة اسم الإشارة المرجعية',
		remove		: 'Remove Anchor' // MISSING
	},

	// List style dialog
	list:
	{
		numberedTitle		: 'Numbered List Properties', // MISSING
		bulletedTitle		: 'Bulleted List Properties', // MISSING
		type				: 'Type', // MISSING
		start				: 'Start', // MISSING
		validateStartNumber				:'List start number must be a whole number.', // MISSING
		circle				: 'Circle', // MISSING
		disc				: 'Disc', // MISSING
		square				: 'Square', // MISSING
		none				: 'None', // MISSING
		notset				: '<not set>', // MISSING
		armenian			: 'Armenian numbering', // MISSING
		georgian			: 'Georgian numbering (an, ban, gan, etc.)', // MISSING
		lowerRoman			: 'Lower Roman (i, ii, iii, iv, v, etc.)', // MISSING
		upperRoman			: 'Upper Roman (I, II, III, IV, V, etc.)', // MISSING
		lowerAlpha			: 'Lower Alpha (a, b, c, d, e, etc.)', // MISSING
		upperAlpha			: 'Upper Alpha (A, B, C, D, E, etc.)', // MISSING
		lowerGreek			: 'Lower Greek (alpha, beta, gamma, etc.)', // MISSING
		decimal				: 'Decimal (1, 2, 3, etc.)', // MISSING
		decimalLeadingZero	: 'Decimal leading zero (01, 02, 03, etc.)' // MISSING
	},

	// Find And Replace Dialog
	findAndReplace :
	{
		title				: 'بحث واستبدال',
		find				: 'بحث',
		replace				: 'إستبدال',
		findWhat			: 'البحث بـ:',
		replaceWith			: 'إستبدال بـ:',
		notFoundMsg			: 'لم يتم العثور على النص المحدد.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'مطابقة حالة الأحرف',
		matchWord			: 'مطابقة بالكامل',
		matchCyclic			: 'مطابقة دورية',
		replaceAll			: 'إستبدال الكل',
		replaceSuccessMsg	: 'تم استبدال 1% من الحالات '
	},

	// Table Dialog
	table :
	{
		toolbar		: 'جدول',
		title		: 'خصائص الجدول',
		menu		: 'خصائص الجدول',
		deleteTable	: 'حذف الجدول',
		rows		: 'صفوف',
		columns		: 'أعمدة',
		border		: 'الحدود',
		widthPx		: 'بكسل',
		widthPc		: 'بالمئة',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'تباعد الخلايا',
		cellPad		: 'المسافة البادئة',
		caption		: 'الوصف',
		summary		: 'الخلاصة',
		headers		: 'العناوين',
		headersNone		: 'بدون',
		headersColumn	: 'العمود الأول',
		headersRow		: 'الصف الأول',
		headersBoth		: 'كلاهما',
		invalidRows		: 'عدد الصفوف يجب أن يكون عدداً أكبر من صفر.',
		invalidCols		: 'عدد الأعمدة يجب أن يكون عدداً أكبر من صفر.',
		invalidBorder	: 'حجم الحد يجب أن يكون عدداً.',
		invalidWidth	: 'عرض الجدول يجب أن يكون عدداً.',
		invalidHeight	: 'ارتفاع الجدول يجب أن يكون عدداً.',
		invalidCellSpacing	: 'المسافة بين الخلايا يجب أن تكون عدداً.',
		invalidCellPadding	: 'المسافة البادئة يجب أن تكون عدداً',

		cell :
		{
			menu			: 'خلية',
			insertBefore	: 'إدراج خلية قبل',
			insertAfter		: 'إدراج خلية بعد',
			deleteCell		: 'حذف خلية',
			merge			: 'دمج خلايا',
			mergeRight		: 'دمج لليمين',
			mergeDown		: 'دمج للأسفل',
			splitHorizontal	: 'تقسيم الخلية أفقياً',
			splitVertical	: 'تقسيم الخلية عمودياً',
			title			: 'خصائص الخلية',
			cellType		: 'نوع الخلية',
			rowSpan			: 'امتداد الصفوف',
			colSpan			: 'امتداد الأعمدة',
			wordWrap		: 'التفاف النص',
			hAlign			: 'محاذاة أفقية',
			vAlign			: 'محاذاة رأسية',
			alignBaseline	: 'خط القاعدة',
			bgColor			: 'لون الخلفية',
			borderColor		: 'لون الحدود',
			data			: 'بيانات',
			header			: 'عنوان',
			yes				: 'نعم',
			no				: 'لا',
			invalidWidth	: 'عرض الخلية يجب أن يكون عدداً.',
			invalidHeight	: 'ارتفاع الخلية يجب أن يكون عدداً.',
			invalidRowSpan	: 'امتداد الصفوف يجب أن يكون عدداً صحيحاً.',
			invalidColSpan	: 'امتداد الأعمدة يجب أن يكون عدداً صحيحاً.',
			chooseColor		: 'اختر'
		},

		row :
		{
			menu			: 'صف',
			insertBefore	: 'إدراج صف قبل',
			insertAfter		: 'إدراج صف بعد',
			deleteRow		: 'حذف صفوف'
		},

		column :
		{
			menu			: 'عمود',
			insertBefore	: 'إدراج عمود قبل',
			insertAfter		: 'إدراج عمود بعد',
			deleteColumn	: 'حذف أعمدة'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'خصائص زر الضغط',
		text		: 'القيمة/التسمية',
		type		: 'نوع الزر',
		typeBtn		: 'زر',
		typeSbm		: 'إرسال',
		typeRst		: 'إعادة تعيين'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'خصائص خانة الإختيار',
		radioTitle	: 'خصائص زر الخيار',
		value		: 'القيمة',
		selected	: 'محدد'
	},

	// Form Dialog.
	form :
	{
		title		: 'خصائص النموذج',
		menu		: 'خصائص النموذج',
		action		: 'اسم الملف',
		method		: 'الأسلوب',
		encoding	: 'تشفير'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'خصائص اختيار الحقل',
		selectInfo	: 'اختار معلومات',
		opAvail		: 'الخيارات المتاحة',
		value		: 'القيمة',
		size		: 'الحجم',
		lines		: 'الأسطر',
		chkMulti	: 'السماح بتحديدات متعددة',
		opText		: 'النص',
		opValue		: 'القيمة',
		btnAdd		: 'إضافة',
		btnModify	: 'تعديل',
		btnUp		: 'أعلى',
		btnDown		: 'أسفل',
		btnSetValue : 'إجعلها محددة',
		btnDelete	: 'إزالة'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'خصائص مساحة النص',
		cols		: 'الأعمدة',
		rows		: 'الصفوف'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'خصائص مربع النص',
		name		: 'الاسم',
		value		: 'القيمة',
		charWidth	: 'عرض السمات',
		maxChars	: 'اقصى عدد للسمات',
		type		: 'نوع المحتوى',
		typeText	: 'نص',
		typePass	: 'كلمة مرور'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'خصائص الحقل المخفي',
		name	: 'الاسم',
		value	: 'القيمة'
	},

	// Image Dialog.
	image :
	{
		title		: 'خصائص الصورة',
		titleButton	: 'خصائص زر الصورة',
		menu		: 'خصائص الصورة',
		infoTab		: 'معلومات الصورة',
		btnUpload	: 'أرسلها للخادم',
		upload		: 'رفع',
		alt			: 'عنوان الصورة',
		lockRatio	: 'تناسق الحجم',
		resetSize	: 'إستعادة الحجم الأصلي',
		border		: 'سمك الحدود',
		hSpace		: 'تباعد أفقي',
		vSpace		: 'تباعد عمودي',
		alertUrl	: 'فضلاً أكتب الموقع الذي توجد عليه هذه الصورة.',
		linkTab		: 'الرابط',
		button2Img	: 'هل تريد تحويل زر الصورة المختار إلى صورة بسيطة؟',
		img2Button	: 'هل تريد تحويل الصورة المختارة إلى زر صورة؟',
		urlMissing	: 'عنوان مصدر الصورة مفقود',
		validateBorder	: 'Border must be a whole number.', // MISSING
		validateHSpace	: 'HSpace must be a whole number.', // MISSING
		validateVSpace	: 'VSpace must be a whole number.' // MISSING
	},

	// Flash Dialog
	flash :
	{
		properties		: 'خصائص الفلاش',
		propertiesTab	: 'الخصائص',
		title			: 'خصائص فيلم الفلاش',
		chkPlay			: 'تشغيل تلقائي',
		chkLoop			: 'تكرار',
		chkMenu			: 'تمكين قائمة فيلم الفلاش',
		chkFull			: 'ملء الشاشة',
 		scale			: 'الحجم',
		scaleAll		: 'إظهار الكل',
		scaleNoBorder	: 'بلا حدود',
		scaleFit		: 'ضبط تام',
		access			: 'دخول النص البرمجي',
		accessAlways	: 'دائماً',
		accessSameDomain: 'نفس النطاق',
		accessNever		: 'مطلقاً',
		alignAbsBottom	: 'أسفل النص',
		alignAbsMiddle	: 'وسط السطر',
		alignBaseline	: 'على السطر',
		alignTextTop	: 'أعلى النص',
		quality			: 'جودة',
		qualityBest		: 'أفضل',
		qualityHigh		: 'عالية',
		qualityAutoHigh	: 'عالية تلقائياً',
		qualityMedium	: 'متوسطة',
		qualityAutoLow	: 'منخفضة تلقائياً',
		qualityLow		: 'منخفضة',
		windowModeWindow: 'نافذة',
		windowModeOpaque: 'غير شفاف',
		windowModeTransparent : 'شفاف',
		windowMode		: 'وضع النافذة',
		flashvars		: 'متغيرات الفلاش',
		bgcolor			: 'لون الخلفية',
		hSpace			: 'تباعد أفقي',
		vSpace			: 'تباعد عمودي',
		validateSrc		: 'فضلاً أدخل عنوان الموقع الذي يشير إليه الرابط',
		validateHSpace	: 'HSpace يجب أن يكون عدداً.',
		validateVSpace	: 'VSpace يجب أن يكون عدداً.'
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'تدقيق إملائي',
		title			: 'التدقيق الإملائي',
		notAvailable	: 'عفواً، ولكن هذه الخدمة غير متاحة الان',
		errorLoading	: 'خطأ في تحميل تطبيق خدمة الاستضافة: %s.',
		notInDic		: 'ليست في القاموس',
		changeTo		: 'التغيير إلى',
		btnIgnore		: 'تجاهل',
		btnIgnoreAll	: 'تجاهل الكل',
		btnReplace		: 'تغيير',
		btnReplaceAll	: 'تغيير الكل',
		btnUndo			: 'تراجع',
		noSuggestions	: '- لا توجد إقتراحات -',
		progress		: 'جاري التدقيق الاملائى',
		noMispell		: 'تم التدقيق الإملائي: لم يتم العثور على أي أخطاء إملائية',
		noChanges		: 'تم التدقيق الإملائي: لم يتم تغيير أي كلمة',
		oneChange		: 'تم التدقيق الإملائي: تم تغيير كلمة واحدة فقط',
		manyChanges		: 'تم إكمال التدقيق الإملائي: تم تغيير %1 من كلمات',
		ieSpellDownload	: 'المدقق الإملائي (الإنجليزي) غير مثبّت. هل تود تحميله الآن؟'
	},

	smiley :
	{
		toolbar	: 'ابتسامات',
		title	: 'إدراج ابتسامات',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : 'عنصر 1%'
	},

	numberedlist	: 'ادخال/حذف تعداد رقمي',
	bulletedlist	: 'ادخال/حذف تعداد نقطي',
	indent			: 'زيادة المسافة البادئة',
	outdent			: 'إنقاص المسافة البادئة',

	justify :
	{
		left	: 'محاذاة إلى اليسار',
		center	: 'توسيط',
		right	: 'محاذاة إلى اليمين',
		block	: 'ضبط'
	},

	blockquote : 'اقتباس',

	clipboard :
	{
		title		: 'لصق',
		cutError	: 'الإعدادات الأمنية للمتصفح الذي تستخدمه تمنع القص التلقائي. فضلاً إستخدم لوحة المفاتيح لفعل ذلك (Ctrl/Cmd+X).',
		copyError	: 'الإعدادات الأمنية للمتصفح الذي تستخدمه تمنع النسخ التلقائي. فضلاً إستخدم لوحة المفاتيح لفعل ذلك (Ctrl/Cmd+C).',
		pasteMsg	: 'الصق داخل الصندوق بإستخدام زرائر (<STRONG>Ctrl/Cmd+V</STRONG>) في لوحة المفاتيح، ثم اضغط زر  <STRONG>موافق</STRONG>.',
		securityMsg	: 'نظراً لإعدادات الأمان الخاصة بمتصفحك، لن يتمكن هذا المحرر من الوصول لمحتوى حافظتك، لذلك يجب عليك لصق المحتوى مرة أخرى في هذه النافذة.',
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'يبدو أن النص المراد لصقه منسوخ من برنامج وورد. هل تود تنظيفه قبل الشروع في عملية اللصق؟',
		toolbar			: 'لصق من وورد',
		title			: 'لصق من وورد',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'لصق كنص بسيط',
		title	: 'لصق كنص بسيط'
	},

	templates :
	{
		button			: 'القوالب',
		title			: 'قوالب المحتوى',
		options : 'Template Options', // MISSING
		insertOption	: 'استبدال المحتوى',
		selectPromptMsg	: 'اختر القالب الذي تود وضعه في المحرر',
		emptyListMsg	: '(لم يتم تعريف أي قالب)'
	},

	showBlocks : 'مخطط تفصيلي',

	stylesCombo :
	{
		label		: 'أنماط',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'أنماط الفقرة',
		panelTitle2	: 'أنماط مضمنة',
		panelTitle3	: 'أنماط الكائن'
	},

	format :
	{
		label		: 'تنسيق',
		panelTitle	: 'تنسيق الفقرة',

		tag_p		: 'عادي',
		tag_pre		: 'منسّق',
		tag_address	: 'عنوان',
		tag_h1		: 'العنوان 1',
		tag_h2		: 'العنوان  2',
		tag_h3		: 'العنوان  3',
		tag_h4		: 'العنوان  4',
		tag_h5		: 'العنوان  5',
		tag_h6		: 'العنوان  6',
		tag_div		: 'عادي (DIV)'
	},

	div :
	{
		title				: 'Create Div Container', // MISSING
		toolbar				: 'Create Div Container', // MISSING
		cssClassInputLabel	: 'Stylesheet Classes', // MISSING
		styleSelectLabel	: 'Style', // MISSING
		IdInputLabel		: 'Id', // MISSING
		languageCodeInputLabel	: ' Language Code', // MISSING
		inlineStyleInputLabel	: 'Inline Style', // MISSING
		advisoryTitleInputLabel	: 'Advisory Title', // MISSING
		langDirLabel		: 'Language Direction', // MISSING
		langDirLTRLabel		: 'Left to Right (LTR)', // MISSING
		langDirRTLLabel		: 'Right to Left (RTL)', // MISSING
		edit				: 'Edit Div', // MISSING
		remove				: 'Remove Div' // MISSING
  	},

	iframe :
	{
		title		: 'IFrame Properties', // MISSING
		toolbar		: 'IFrame', // MISSING
		noUrl		: 'Please type the iframe URL', // MISSING
		scrolling	: 'Enable scrollbars', // MISSING
		border		: 'Show frame border' // MISSING
	},

	font :
	{
		label		: 'خط',
		voiceLabel	: 'حجم الخط',
		panelTitle	: 'حجم الخط'
	},

	fontSize :
	{
		label		: 'حجم الخط',
		voiceLabel	: 'حجم الخط',
		panelTitle	: 'حجم الخط'
	},

	colorButton :
	{
		textColorTitle	: 'لون النص',
		bgColorTitle	: 'لون الخلفية',
		panelTitle		: 'Colors', // MISSING
		auto			: 'تلقائي',
		more			: 'ألوان إضافية...'
	},

	colors :
	{
		'000' : 'أسود',
		'800000' : 'كستنائي',
		'8B4513' : 'بني فاتح',
		'2F4F4F' : 'رمادي أردوازي غامق',
		'008080' : 'أزرق مخضر',
		'000080' : 'أزرق داكن',
		'4B0082' : 'كحلي',
		'696969' : 'رمادي داكن',
		'B22222' : 'طوبي',
		'A52A2A' : 'بني',
		'DAA520' : 'ذهبي داكن',
		'006400' : 'أخضر داكن',
		'40E0D0' : 'فيروزي',
		'0000CD' : 'أزرق متوسط',
		'800080' : 'بنفسجي غامق',
		'808080' : 'رمادي',
		'F00' : 'أحمر',
		'FF8C00' : 'برتقالي داكن',
		'FFD700' : 'ذهبي',
		'008000' : 'أخضر',
		'0FF' : 'تركواز',
		'00F' : 'أزرق',
		'EE82EE' : 'بنفسجي',
		'A9A9A9' : 'رمادي شاحب',
		'FFA07A' : 'برتقالي وردي',
		'FFA500' : 'برتقالي',
		'FFFF00' : 'أصفر',
		'00FF00' : 'ليموني',
		'AFEEEE' : 'فيروزي شاحب',
		'ADD8E6' : 'أزرق فاتح',
		'DDA0DD' : 'بنفسجي فاتح',
		'D3D3D3' : 'رمادي فاتح',
		'FFF0F5' : 'وردي فاتح',
		'FAEBD7' : 'أبيض عتيق',
		'FFFFE0' : 'أصفر فاتح',
		'F0FFF0' : 'أبيض مائل للأخضر',
		'F0FFFF' : 'سماوي',
		'F0F8FF' : 'لبني',
		'E6E6FA' : 'أرجواني',
		'FFF' : 'أبيض'
	},

	scayt :
	{
		title			: 'تدقيق إملائي أثناء الكتابة',
		opera_title		: 'Not supported by Opera', // MISSING
		enable			: 'تفعيل SCAYT',
		disable			: 'تعطيل SCAYT',
		about			: 'عن SCAYT',
		toggle			: 'تثبيت SCAYT',
		options			: 'خيارات',
		langs			: 'لغات',
		moreSuggestions	: 'المزيد من المقترحات',
		ignore			: 'تجاهل',
		ignoreAll		: 'تجاهل الكل',
		addWord			: 'إضافة كلمة',
		emptyDic		: 'اسم القاموس يجب ألا يكون فارغاً.',

		optionsTab		: 'خيارات',
		allCaps			: 'Ignore All-Caps Words', // MISSING
		ignoreDomainNames : 'Ignore Domain Names', // MISSING
		mixedCase		: 'Ignore Words with Mixed Case', // MISSING
		mixedWithDigits	: 'Ignore Words with Numbers', // MISSING

		languagesTab	: 'لغات',

		dictionariesTab	: 'قواميس',
		dic_field_name	: 'Dictionary name', // MISSING
		dic_create		: 'Create', // MISSING
		dic_restore		: 'Restore', // MISSING
		dic_delete		: 'Delete', // MISSING
		dic_rename		: 'Rename', // MISSING
		dic_info		: 'Initially the User Dictionary is stored in a Cookie. However, Cookies are limited in size. When the User Dictionary grows to a point where it cannot be stored in a Cookie, then the dictionary may be stored on our server. To store your personal dictionary on our server you should specify a name for your dictionary. If you already have a stored dictionary, please type its name and click the Restore button.', // MISSING

		aboutTab		: 'عن'
	},

	about :
	{
		title		: 'عن CKEditor',
		dlgTitle	: 'عن CKEditor',
		help	: 'Check $1 for help.', // MISSING
		userGuide : 'CKEditor User\'s Guide', // MISSING
		moreInfo	: 'للحصول على معلومات الترخيص ، يرجى زيارة موقعنا على شبكة الانترنت:',
		copy		: 'حقوق النشر &copy; $1. جميع الحقوق محفوظة.'
	},

	maximize : 'تكبير',
	minimize : 'تصغير',

	fakeobjects :
	{
		anchor		: 'إرساء',
		flash		: 'رسم متحرك بالفلاش',
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'كائن غير معروف'
	},

	resize : 'اسحب لتغيير الحجم',

	colordialog :
	{
		title		: 'اختر لون',
		options	:	'Color Options', // MISSING
		highlight	: 'إلقاء الضوء',
		selected	: 'مُختار',
		clear		: 'مسح'
	},

	toolbarCollapse	: 'Collapse Toolbar', // MISSING
	toolbarExpand	: 'Expand Toolbar', // MISSING

	toolbarGroups :
	{
		document : 'Document', // MISSING
		clipboard : 'Clipboard/Undo', // MISSING
		editing : 'Editing', // MISSING
		forms : 'Forms', // MISSING
		basicstyles : 'Basic Styles', // MISSING
		paragraph : 'Paragraph', // MISSING
		links : 'Links', // MISSING
		insert : 'Insert', // MISSING
		styles : 'Styles', // MISSING
		colors : 'Colors', // MISSING
		tools : 'Tools' // MISSING
	},

	bidi :
	{
		ltr : 'Text direction from left to right', // MISSING
		rtl : 'Text direction from right to left' // MISSING
	},

	docprops :
	{
		label : 'خصائص الصفحة',
		title : 'خصائص الصفحة',
		design : 'Design', // MISSING
		meta : 'المعرّفات الرأسية',
		chooseColor : 'اختر',
		other : '<أخرى>',
		docTitle :	'عنوان الصفحة',
		charset : 	'ترميز الحروف',
		charsetOther : 'ترميز آخر',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'أوروبا الوسطى',
		charsetCT : 'الصينية التقليدية (Big5)',
		charsetCR : 'السيريلية',
		charsetGR : 'اليونانية',
		charsetJP : 'اليابانية',
		charsetKR : 'الكورية',
		charsetTR : 'التركية',
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'أوروبا الغربية',
		docType : 'ترويسة نوع  الصفحة',
		docTypeOther : 'ترويسة نوع  صفحة أخرى',
		xhtmlDec : 'تضمين   إعلانات لغة XHTMLَ',
		bgColor : 'لون الخلفية',
		bgImage : 'رابط الصورة الخلفية',
		bgFixed : 'جعلها علامة مائية',
		txtColor : 'لون النص',
		margin : 'هوامش الصفحة',
		marginTop : 'علوي',
		marginLeft : 'أيسر',
		marginRight : 'أيمن',
		marginBottom : 'سفلي',
		metaKeywords : 'الكلمات الأساسية (مفصولة بفواصل)َ',
		metaDescription : 'وصف الصفحة',
		metaAuthor : 'الكاتب',
		metaCopyright : 'المالك',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
