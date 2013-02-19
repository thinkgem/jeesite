/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Mongolian language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['mn'] =
{
	/**
	 * The language reading direction. Possible values are "rtl" for
	 * Right-To-Left languages (like Arabic) and "ltr" for Left-To-Right
	 * languages (like English).
	 * @default 'ltr'
	 */
	dir : 'ltr',

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
	source			: 'Код',
	newPage			: 'Шинэ хуудас',
	save			: 'Хадгалах',
	preview			: 'Уридчлан харах',
	cut				: 'Хайчлах',
	copy			: 'Хуулах',
	paste			: 'Буулгах',
	print			: 'Хэвлэх',
	underline		: 'Доогуур нь зураастай болгох',
	bold			: 'Тод бүдүүн',
	italic			: 'Налуу',
	selectAll		: 'Бүгдийг нь сонгох',
	removeFormat	: 'Формат авч хаях',
	strike			: 'Дундуур нь зураастай болгох',
	subscript		: 'Суурь болгох',
	superscript		: 'Зэрэг болгох',
	horizontalrule	: 'Хөндлөн зураас оруулах',
	pagebreak		: 'Хуудас тусгаарлагч оруулах',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Линк авч хаях',
	undo			: 'Хүчингүй болгох',
	redo			: 'Өмнөх үйлдлээ сэргээх',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Сервер харуулах',
		url				: 'URL',
		protocol		: 'Протокол',
		upload			: 'Хуулах',
		uploadSubmit	: 'Үүнийг сервэррүү илгээ',
		image			: 'Зураг',
		flash			: 'Флаш',
		form			: 'Форм',
		checkbox		: 'Чекбокс',
		radio			: 'Радио товч',
		textField		: 'Техт талбар',
		textarea		: 'Техт орчин',
		hiddenField		: 'Нууц талбар',
		button			: 'Товч',
		select			: 'Сонгогч талбар',
		imageButton		: 'Зурагтай товч',
		notSet			: '<Оноохгүй>',
		id				: 'Id',
		name			: 'Нэр',
		langDir			: 'Хэлний чиглэл',
		langDirLtr		: 'Зүүнээс баруун (LTR)',
		langDirRtl		: 'Баруунаас зүүн (RTL)',
		langCode		: 'Хэлний код',
		longDescr		: 'URL-ын тайлбар',
		cssClass		: 'Stylesheet классууд',
		advisoryTitle	: 'Зөвлөлдөх гарчиг',
		cssStyle		: 'Загвар',
		ok				: 'OK',
		cancel			: 'Болих',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Нэмэлт',
		validateNumberFailed : 'This value is not a number.', // MISSING
		confirmNewPage	: 'Any unsaved changes to this content will be lost. Are you sure you want to load new page?', // MISSING
		confirmCancel	: 'Some of the options have been changed. Are you sure to close the dialog?', // MISSING
		options			: 'Options', // MISSING
		target			: 'Target', // MISSING
		targetNew		: 'New Window (_blank)', // MISSING
		targetTop		: 'Topmost Window (_top)', // MISSING
		targetSelf		: 'Same Window (_self)', // MISSING
		targetParent	: 'Parent Window (_parent)', // MISSING
		langDirLTR		: 'Left to Right (LTR)', // MISSING
		langDirRTL		: 'Right to Left (RTL)', // MISSING
		styles			: 'Style', // MISSING
		cssClasses		: 'Stylesheet Classes', // MISSING
		width			: 'Өргөн',
		height			: 'Өндөр',
		align			: 'Эгнээ',
		alignLeft		: 'Зүүн',
		alignRight		: 'Баруун',
		alignCenter		: 'Төвд',
		alignTop		: 'Дээд талд',
		alignMiddle		: 'Дунд талд',
		alignBottom		: 'Доод талд',
		invalidHeight	: 'Height must be a number.', // MISSING
		invalidWidth	: 'Width must be a number.', // MISSING
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>' // MISSING
	},

	contextmenu :
	{
		options : 'Context Menu Options' // MISSING
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Онцгой тэмдэгт оруулах',
		title		: 'Онцгой тэмдэгт сонгох',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Линк Оруулах/Засварлах',
		other 		: '<other>', // MISSING
		menu		: 'Холбоос засварлах',
		title		: 'Линк',
		info		: 'Линкийн мэдээлэл',
		target		: 'Байрлал',
		upload		: 'Хуулах',
		advanced	: 'Нэмэлт',
		type		: 'Линкийн төрөл',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Энэ хуудасандах холбоос',
		toEmail		: 'E-Mail',
		targetFrame		: '<Агуулах хүрээ>',
		targetPopup		: '<popup цонх>',
		targetFrameName	: 'Очих фремын нэр',
		targetPopupName	: 'Popup цонхны нэр',
		popupFeatures	: 'Popup цонхны онцлог',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Статус хэсэг',
		popupLocationBar: 'Location хэсэг',
		popupToolbar	: 'Багажны хэсэг',
		popupMenuBar	: 'Meню хэсэг',
		popupFullScreen	: 'Цонх дүүргэх (IE)',
		popupScrollBars	: 'Скрол хэсэгүүд',
		popupDependent	: 'Хамаатай (Netscape)',
		popupLeft		: 'Зүүн байрлал',
		popupTop		: 'Дээд байрлал',
		id				: 'Id', // MISSING
		langDir			: 'Хэлний чиглэл',
		langDirLTR		: 'Зүүнээс баруун (LTR)',
		langDirRTL		: 'Баруунаас зүүн (RTL)',
		acccessKey		: 'Холбох түлхүүр',
		name			: 'Нэр',
		langCode			: 'Хэлний чиглэл',
		tabIndex			: 'Tab индекс',
		advisoryTitle		: 'Зөвлөлдөх гарчиг',
		advisoryContentType	: 'Зөвлөлдөх төрлийн агуулга',
		cssClasses		: 'Stylesheet классууд',
		charset			: 'Тэмдэгт оноох нөөцөд холбогдсон',
		styles			: 'Загвар',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Холбоос сонгох',
		anchorName		: 'Холбоосын нэрээр',
		anchorId			: 'Элемэнт Id-гаар',
		emailAddress		: 'E-Mail Хаяг',
		emailSubject		: 'Message гарчиг',
		emailBody		: 'Message-ийн агуулга',
		noAnchors		: '(Баримт бичиг холбоосгүй байна)',
		noUrl			: 'Линк URL-ээ төрөлжүүлнэ үү',
		noEmail			: 'Е-mail хаягаа төрөлжүүлнэ үү'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Холбоос Оруулах/Засварлах',
		menu		: 'Холбоос шинж чанар',
		title		: 'Холбоос шинж чанар',
		name		: 'Холбоос нэр',
		errorName	: 'Холбоос төрөл оруулна уу',
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
		title				: 'Хай мөн Дарж бич',
		find				: 'Хайх',
		replace				: 'Солих',
		findWhat			: 'Хайх үг/үсэг:',
		replaceWith			: 'Солих үг:',
		notFoundMsg			: 'Хайсан текст олсонгүй.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Тэнцэх төлөв',
		matchWord			: 'Тэнцэх бүтэн үг',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Бүгдийг нь Солих',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Хүснэгт',
		title		: 'Хүснэгт',
		menu		: 'Хүснэгт',
		deleteTable	: 'Хүснэгт устгах',
		rows		: 'Мөр',
		columns		: 'Багана',
		border		: 'Хүрээний хэмжээ',
		widthPx		: 'цэг',
		widthPc		: 'хувь',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Нүх хоорондын зай (spacing)',
		cellPad		: 'Нүх доторлох(padding)',
		caption		: 'Тайлбар',
		summary		: 'Тайлбар',
		headers		: 'Headers', // MISSING
		headersNone		: 'None', // MISSING
		headersColumn	: 'First column', // MISSING
		headersRow		: 'First Row', // MISSING
		headersBoth		: 'Both', // MISSING
		invalidRows		: 'Number of rows must be a number greater than 0.', // MISSING
		invalidCols		: 'Number of columns must be a number greater than 0.', // MISSING
		invalidBorder	: 'Border size must be a number.', // MISSING
		invalidWidth	: 'Table width must be a number.', // MISSING
		invalidHeight	: 'Table height must be a number.', // MISSING
		invalidCellSpacing	: 'Cell spacing must be a positive number.', // MISSING
		invalidCellPadding	: 'Cell padding must be a positive number.', // MISSING

		cell :
		{
			menu			: 'Нүх/зай',
			insertBefore	: 'Нүх/зай өмнө нь оруулах',
			insertAfter		: 'Нүх/зай дараа нь оруулах',
			deleteCell		: 'Нүх устгах',
			merge			: 'Нүх нэгтэх',
			mergeRight		: 'Баруун тийш нэгтгэх',
			mergeDown		: 'Доош нэгтгэх',
			splitHorizontal	: 'Нүх/зайг босоогоор нь тусгаарлах',
			splitVertical	: 'Нүх/зайг хөндлөнгөөр нь тусгаарлах',
			title			: 'Cell Properties', // MISSING
			cellType		: 'Cell Type', // MISSING
			rowSpan			: 'Rows Span', // MISSING
			colSpan			: 'Columns Span', // MISSING
			wordWrap		: 'Word Wrap', // MISSING
			hAlign			: 'Horizontal Alignment', // MISSING
			vAlign			: 'Vertical Alignment', // MISSING
			alignBaseline	: 'Baseline', // MISSING
			bgColor			: 'Background Color', // MISSING
			borderColor		: 'Border Color', // MISSING
			data			: 'Data', // MISSING
			header			: 'Header', // MISSING
			yes				: 'Yes', // MISSING
			no				: 'No', // MISSING
			invalidWidth	: 'Cell width must be a number.', // MISSING
			invalidHeight	: 'Cell height must be a number.', // MISSING
			invalidRowSpan	: 'Rows span must be a whole number.', // MISSING
			invalidColSpan	: 'Columns span must be a whole number.', // MISSING
			chooseColor		: 'Choose' // MISSING
		},

		row :
		{
			menu			: 'Мөр',
			insertBefore	: 'Мөр өмнө нь оруулах',
			insertAfter		: 'Мөр дараа нь оруулах',
			deleteRow		: 'Мөр устгах'
		},

		column :
		{
			menu			: 'Багана',
			insertBefore	: 'Багана өмнө нь оруулах',
			insertAfter		: 'Багана дараа нь оруулах',
			deleteColumn	: 'Багана устгах'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Товчны шинж чанар',
		text		: 'Тэкст (Утга)',
		type		: 'Төрөл',
		typeBtn		: 'Товч',
		typeSbm		: 'Submit',
		typeRst		: 'Болих'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Чекбоксны шинж чанар',
		radioTitle	: 'Радио товчны шинж чанар',
		value		: 'Утга',
		selected	: 'Сонгогдсон'
	},

	// Form Dialog.
	form :
	{
		title		: 'Форм шинж чанар',
		menu		: 'Форм шинж чанар',
		action		: 'Үйлдэл',
		method		: 'Арга',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Согогч талбарын шинж чанар',
		selectInfo	: 'Мэдээлэл',
		opAvail		: 'Идвэхтэй сонголт',
		value		: 'Утга',
		size		: 'Хэмжээ',
		lines		: 'Мөр',
		chkMulti	: 'Олон сонголт зөвшөөрөх',
		opText		: 'Тэкст',
		opValue		: 'Утга',
		btnAdd		: 'Нэмэх',
		btnModify	: 'Өөрчлөх',
		btnUp		: 'Дээш',
		btnDown		: 'Доош',
		btnSetValue : 'Сонгогдсан утга оноох',
		btnDelete	: 'Устгах'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Текст орчны шинж чанар',
		cols		: 'Багана',
		rows		: 'Мөр'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Текст талбарын шинж чанар',
		name		: 'Нэр',
		value		: 'Утга',
		charWidth	: 'Тэмдэгтын өргөн',
		maxChars	: 'Хамгийн их тэмдэгт',
		type		: 'Төрөл',
		typeText	: 'Текст',
		typePass	: 'Нууц үг'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Нууц талбарын шинж чанар',
		name	: 'Нэр',
		value	: 'Утга'
	},

	// Image Dialog.
	image :
	{
		title		: 'Зураг',
		titleButton	: 'Зурган товчны шинж чанар',
		menu		: 'Зураг',
		infoTab		: 'Зурагны мэдээлэл',
		btnUpload	: 'Үүнийг сервэррүү илгээ',
		upload		: 'Хуулах',
		alt			: 'Тайлбар текст',
		lockRatio	: 'Радио түгжих',
		resetSize	: 'хэмжээ дахин оноох',
		border		: 'Хүрээ',
		hSpace		: 'Хөндлөн зай',
		vSpace		: 'Босоо зай',
		alertUrl	: 'Зурагны URL-ын төрлийн сонгоно уу',
		linkTab		: 'Линк',
		button2Img	: 'Do you want to transform the selected image button on a simple image?', // MISSING
		img2Button	: 'Do you want to transform the selected image on a image button?', // MISSING
		urlMissing	: 'Image source URL is missing.', // MISSING
		validateBorder	: 'Border must be a whole number.', // MISSING
		validateHSpace	: 'HSpace must be a whole number.', // MISSING
		validateVSpace	: 'VSpace must be a whole number.' // MISSING
	},

	// Flash Dialog
	flash :
	{
		properties		: 'Флаш шинж чанар',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Флаш  шинж чанар',
		chkPlay			: 'Автоматаар тоглох',
		chkLoop			: 'Давтах',
		chkMenu			: 'Флаш цэс идвэхжүүлэх',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Өргөгтгөх',
		scaleAll		: 'Бүгдийг харуулах',
		scaleNoBorder	: 'Хүрээгүй',
		scaleFit		: 'Яг тааруулах',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs доод талд',
		alignAbsMiddle	: 'Abs Дунд талд',
		alignBaseline	: 'Baseline',
		alignTextTop	: 'Текст дээр',
		quality			: 'Quality', // MISSING
		qualityBest		: 'Best', // MISSING
		qualityHigh		: 'High', // MISSING
		qualityAutoHigh	: 'Auto High', // MISSING
		qualityMedium	: 'Medium', // MISSING
		qualityAutoLow	: 'Auto Low', // MISSING
		qualityLow		: 'Low', // MISSING
		windowModeWindow: 'Window', // MISSING
		windowModeOpaque: 'Opaque', // MISSING
		windowModeTransparent : 'Transparent', // MISSING
		windowMode		: 'Window mode', // MISSING
		flashvars		: 'Variables for Flash', // MISSING
		bgcolor			: 'Фонны өнгө',
		hSpace			: 'Хөндлөн зай',
		vSpace			: 'Босоо зай',
		validateSrc		: 'Линк URL-ээ төрөлжүүлнэ үү',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Үгийн дүрэх шалгах',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Толь бичиггүй',
		changeTo		: 'Өөрчлөх',
		btnIgnore		: 'Зөвшөөрөх',
		btnIgnoreAll	: 'Бүгдийг зөвшөөрөх',
		btnReplace		: 'Дарж бичих',
		btnReplaceAll	: 'Бүгдийг Дарж бичих',
		btnUndo			: 'Буцаах',
		noSuggestions	: '- Тайлбаргүй -',
		progress		: 'Дүрэм шалгаж байгаа үйл явц...',
		noMispell		: 'Дүрэм шалгаад дууссан: Алдаа олдсонгүй',
		noChanges		: 'Дүрэм шалгаад дууссан: үг өөрчлөгдөөгүй',
		oneChange		: 'Дүрэм шалгаад дууссан: 1 үг өөрчлөгдсөн',
		manyChanges		: 'Дүрэм шалгаад дууссан: %1 үг өөрчлөгдсөн',
		ieSpellDownload	: 'Дүрэм шалгагч суугаагүй байна. Татаж авахыг хүсч байна уу?'
	},

	smiley :
	{
		toolbar	: 'Тодорхойлолт',
		title	: 'Тодорхойлолт оруулах',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Дугаарлагдсан жагсаалт',
	bulletedlist	: 'Цэгтэй жагсаалт',
	indent			: 'Догол мөр хасах',
	outdent			: 'Догол мөр нэмэх',

	justify :
	{
		left	: 'Зүүн талд байрлуулах',
		center	: 'Төвд байрлуулах',
		right	: 'Баруун талд байрлуулах',
		block	: 'Блок хэлбэрээр байрлуулах'
	},

	blockquote : 'Хайрцаглах',

	clipboard :
	{
		title		: 'Буулгах',
		cutError	: 'Таны browser-ын хамгаалалтын тохиргоо editor-д автоматаар хайчлах үйлдэлийг зөвшөөрөхгүй байна. (Ctrl/Cmd+X) товчны хослолыг ашиглана уу.',
		copyError	: 'Таны browser-ын хамгаалалтын тохиргоо editor-д автоматаар хуулах үйлдэлийг зөвшөөрөхгүй байна. (Ctrl/Cmd+C) товчны хослолыг ашиглана уу.',
		pasteMsg	: '(<strong>Ctrl/Cmd+V</strong>) товчийг ашиглан paste хийнэ үү. Мөн <strong>OK</strong> дар.',
		securityMsg	: 'Таны үзүүлэгч/browser/-н хамгаалалтын тохиргооноос болоод editor clipboard өгөгдөлрүү шууд хандах боломжгүй. Энэ цонход дахин paste хийхийг оролд.',
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Word-оос буулгах',
		title			: 'Word-оос буулгах',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Plain Text-ээс буулгах',
		title	: 'Plain Text-ээс буулгах'
	},

	templates :
	{
		button			: 'Загварууд',
		title			: 'Загварын агуулга',
		options : 'Template Options', // MISSING
		insertOption	: 'Одоогийн агууллагыг дарж бичих',
		selectPromptMsg	: 'Загварыг нээж editor-рүү сонгож оруулна уу<br />(Одоогийн агууллагыг устаж магадгүй):',
		emptyListMsg	: '(Загвар тодорхойлогдоогүй байна)'
	},

	showBlocks : 'Block-уудыг үзүүлэх',

	stylesCombo :
	{
		label		: 'Загвар',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Формат',
		panelTitle	: 'Формат',

		tag_p		: 'Хэвийн',
		tag_pre		: 'Formatted',
		tag_address	: 'Хаяг',
		tag_h1		: 'Heading 1',
		tag_h2		: 'Heading 2',
		tag_h3		: 'Heading 3',
		tag_h4		: 'Heading 4',
		tag_h5		: 'Heading 5',
		tag_h6		: 'Heading 6',
		tag_div		: 'Paragraph (DIV)'
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
		label		: 'Фонт',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Фонт'
	},

	fontSize :
	{
		label		: 'Хэмжээ',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Хэмжээ'
	},

	colorButton :
	{
		textColorTitle	: 'Фонтны өнгө',
		bgColorTitle	: 'Фонны өнгө',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Автоматаар',
		more			: 'Нэмэлт өнгөнүүд...'
	},

	colors :
	{
		'000' : 'Black', // MISSING
		'800000' : 'Maroon', // MISSING
		'8B4513' : 'Saddle Brown', // MISSING
		'2F4F4F' : 'Dark Slate Gray', // MISSING
		'008080' : 'Teal', // MISSING
		'000080' : 'Navy', // MISSING
		'4B0082' : 'Indigo', // MISSING
		'696969' : 'Dark Gray', // MISSING
		'B22222' : 'Fire Brick', // MISSING
		'A52A2A' : 'Brown', // MISSING
		'DAA520' : 'Golden Rod', // MISSING
		'006400' : 'Dark Green', // MISSING
		'40E0D0' : 'Turquoise', // MISSING
		'0000CD' : 'Medium Blue', // MISSING
		'800080' : 'Purple', // MISSING
		'808080' : 'Gray', // MISSING
		'F00' : 'Red', // MISSING
		'FF8C00' : 'Dark Orange', // MISSING
		'FFD700' : 'Gold', // MISSING
		'008000' : 'Green', // MISSING
		'0FF' : 'Cyan', // MISSING
		'00F' : 'Blue', // MISSING
		'EE82EE' : 'Violet', // MISSING
		'A9A9A9' : 'Dim Gray', // MISSING
		'FFA07A' : 'Light Salmon', // MISSING
		'FFA500' : 'Orange', // MISSING
		'FFFF00' : 'Yellow', // MISSING
		'00FF00' : 'Lime', // MISSING
		'AFEEEE' : 'Pale Turquoise', // MISSING
		'ADD8E6' : 'Light Blue', // MISSING
		'DDA0DD' : 'Plum', // MISSING
		'D3D3D3' : 'Light Grey', // MISSING
		'FFF0F5' : 'Lavender Blush', // MISSING
		'FAEBD7' : 'Antique White', // MISSING
		'FFFFE0' : 'Light Yellow', // MISSING
		'F0FFF0' : 'Honeydew', // MISSING
		'F0FFFF' : 'Azure', // MISSING
		'F0F8FF' : 'Alice Blue', // MISSING
		'E6E6FA' : 'Lavender', // MISSING
		'FFF' : 'White' // MISSING
	},

	scayt :
	{
		title			: 'Spell Check As You Type', // MISSING
		opera_title		: 'Not supported by Opera', // MISSING
		enable			: 'Enable SCAYT', // MISSING
		disable			: 'Disable SCAYT', // MISSING
		about			: 'About SCAYT', // MISSING
		toggle			: 'Toggle SCAYT', // MISSING
		options			: 'Options', // MISSING
		langs			: 'Languages', // MISSING
		moreSuggestions	: 'More suggestions', // MISSING
		ignore			: 'Ignore', // MISSING
		ignoreAll		: 'Ignore All', // MISSING
		addWord			: 'Add Word', // MISSING
		emptyDic		: 'Dictionary name should not be empty.', // MISSING

		optionsTab		: 'Options', // MISSING
		allCaps			: 'Ignore All-Caps Words', // MISSING
		ignoreDomainNames : 'Ignore Domain Names', // MISSING
		mixedCase		: 'Ignore Words with Mixed Case', // MISSING
		mixedWithDigits	: 'Ignore Words with Numbers', // MISSING

		languagesTab	: 'Languages', // MISSING

		dictionariesTab	: 'Dictionaries', // MISSING
		dic_field_name	: 'Dictionary name', // MISSING
		dic_create		: 'Create', // MISSING
		dic_restore		: 'Restore', // MISSING
		dic_delete		: 'Delete', // MISSING
		dic_rename		: 'Rename', // MISSING
		dic_info		: 'Initially the User Dictionary is stored in a Cookie. However, Cookies are limited in size. When the User Dictionary grows to a point where it cannot be stored in a Cookie, then the dictionary may be stored on our server. To store your personal dictionary on our server you should specify a name for your dictionary. If you already have a stored dictionary, please type its name and click the Restore button.', // MISSING

		aboutTab		: 'About' // MISSING
	},

	about :
	{
		title		: 'About CKEditor', // MISSING
		dlgTitle	: 'About CKEditor', // MISSING
		help	: 'Check $1 for help.', // MISSING
		userGuide : 'CKEditor User\'s Guide', // MISSING
		moreInfo	: 'For licensing information please visit our web site:', // MISSING
		copy		: 'Copyright &copy; $1. All rights reserved.' // MISSING
	},

	maximize : 'Maximize', // MISSING
	minimize : 'Minimize', // MISSING

	fakeobjects :
	{
		anchor		: 'Anchor', // MISSING
		flash		: 'Flash Animation', // MISSING
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'Unknown Object' // MISSING
	},

	resize : 'Drag to resize', // MISSING

	colordialog :
	{
		title		: 'Select color', // MISSING
		options	:	'Color Options', // MISSING
		highlight	: 'Highlight', // MISSING
		selected	: 'Selected Color', // MISSING
		clear		: 'Clear' // MISSING
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
		label : 'Баримт бичиг шинж чанар',
		title : 'Баримт бичиг шинж чанар',
		design : 'Design', // MISSING
		meta : 'Meta өгөгдөл',
		chooseColor : 'Choose', // MISSING
		other : '<other>',
		docTitle :	'Хуудасны гарчиг',
		charset : 	'Encoding тэмдэгт',
		charsetOther : 'Encoding-д өөр тэмдэгт оноох',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Төв европ',
		charsetCT : 'Хятадын уламжлалт (Big5)',
		charsetCR : 'Крил',
		charsetGR : 'Гред',
		charsetJP : 'Япон',
		charsetKR : 'Солонгос',
		charsetTR : 'Tурк',
		charsetUN : 'Юникод (UTF-8)',
		charsetWE : 'Баруун европ',
		docType : 'Баримт бичгийн төрөл Heading',
		docTypeOther : 'Бусад баримт бичгийн төрөл Heading',
		xhtmlDec : 'XHTML агуулж зарлах',
		bgColor : 'Фоно өнгө',
		bgImage : 'Фоно зурагны URL',
		bgFixed : 'Гүйдэггүй фоно',
		txtColor : 'Фонтны өнгө',
		margin : 'Хуудасны захын зай',
		marginTop : 'Дээд тал',
		marginLeft : 'Зүүн тал',
		marginRight : 'Баруун тал',
		marginBottom : 'Доод тал',
		metaKeywords : 'Баримт бичгийн индекс түлхүүр үг (таслалаар тусгаарлагдана)',
		metaDescription : 'Баримт бичгийн тайлбар',
		metaAuthor : 'Зохиогч',
		metaCopyright : 'Зохиогчийн эрх',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
