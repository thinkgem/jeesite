/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Bulgarian language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['bg'] =
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
	toolbars	: 'Ленти с инструменти',
	editor		: 'Текстов редактор за форматиран текст',

	// Toolbar buttons without dialogs.
	source			: 'Източник',
	newPage			: 'Нова страница',
	save			: 'Запис',
	preview			: 'Преглед',
	cut				: 'Отрежи',
	copy			: 'Копирай',
	paste			: 'Вмъкни',
	print			: 'Печат',
	underline		: 'Подчертан',
	bold			: 'Удебелен',
	italic			: 'Наклонен',
	selectAll		: 'Избери всичко',
	removeFormat	: 'Премахване на форматирането',
	strike			: 'Зачертан текст',
	subscript		: 'Индексиран текст',
	superscript		: 'Суперскрипт',
	horizontalrule	: 'Вмъкване на хоризонтална линия',
	pagebreak		: 'Вмъкване на нова страница при печат',
	pagebreakAlt		: 'Разделяне на страници',
	unlink			: 'Премахни връзката',
	undo			: 'Възтанови',
	redo			: 'Връщане на предишен статус',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Избор от сървъра',
		url				: 'URL',
		protocol		: 'Протокол',
		upload			: 'Качване',
		uploadSubmit	: 'Изпращане към сървъра',
		image			: 'Снимка',
		flash			: 'Флаш',
		form			: 'Форма',
		checkbox		: 'Поле за избор',
		radio			: 'Радио бутон',
		textField		: 'Текстово поле',
		textarea		: 'Текстова зона',
		hiddenField		: 'Скрито поле',
		button			: 'Бутон',
		select			: 'Поле за избор',
		imageButton		: 'Бутон за снимка',
		notSet			: '<не е избрано>',
		id				: 'ID',
		name			: 'Име',
		langDir			: 'Посока на езика',
		langDirLtr		: 'Ляво на дясно (ЛнД)',
		langDirRtl		: 'Дясно на ляво (ДнЛ)',
		langCode		: 'Код на езика',
		longDescr		: 'Уеб адрес за дълго описание',
		cssClass		: 'Класове за CSS',
		advisoryTitle	: 'Advisory Title', // MISSING
		cssStyle		: 'Стил',
		ok				: 'ОК',
		cancel			: 'Отказ',
		close			: 'Затвори',
		preview			: 'Преглед',
		generalTab		: 'Общо',
		advancedTab		: 'Разширено',
		validateNumberFailed : 'Тази стойност не е число',
		confirmNewPage	: 'Any unsaved changes to this content will be lost. Are you sure you want to load new page?', // MISSING
		confirmCancel	: 'Some of the options have been changed. Are you sure to close the dialog?', // MISSING
		options			: 'Опции',
		target			: 'Цел',
		targetNew		: 'Нов прозорец (_blank)',
		targetTop		: 'Горна позиция (_top)',
		targetSelf		: 'Текущия прозорец (_self)',
		targetParent	: 'Основен прозорец (_parent)',
		langDirLTR		: 'Ляво на дясно (ЛнД)',
		langDirRTL		: 'Дясно на ляво (ДнЛ)',
		styles			: 'Стил',
		cssClasses		: 'Класове за CSS',
		width			: 'Ширина',
		height			: 'Височина',
		align			: 'Подравняване',
		alignLeft		: 'Ляво',
		alignRight		: 'Дясно',
		alignCenter		: 'Център',
		alignTop		: 'Горе',
		alignMiddle		: 'По средата',
		alignBottom		: 'Долу',
		invalidHeight	: 'Височината трябва да е число.',
		invalidWidth	: 'Ширина требе да е число.',
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>' // MISSING
	},

	contextmenu :
	{
		options : 'Опции на контекстното меню'
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Вмъкване на специален знак',
		title		: 'Избор на специален знак',
		options : 'Опции за специален знак'
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Връзка',
		other 		: '<друго>',
		menu		: 'Промяна на връзка',
		title		: 'Връзка',
		info		: 'Инфо за връзката',
		target		: 'Цел',
		upload		: 'Качване',
		advanced	: 'Разширено',
		type		: 'Тип на връзката',
		toUrl		: 'Уеб адрес',
		toAnchor	: 'Връзка към котва в текста',
		toEmail		: 'E-mail',
		targetFrame		: '<frame>',
		targetPopup		: '<изкачащ прозорец>',
		targetFrameName	: 'Име на целевият прозорец',
		targetPopupName	: 'Име на изкачащ прозорец',
		popupFeatures	: 'Функции на изкачащ прозорец',
		popupResizable	: 'Оразмеряем',
		popupStatusBar	: 'Статусна лента',
		popupLocationBar: 'Лента с локацията',
		popupToolbar	: 'Лента с инструменти',
		popupMenuBar	: 'Лента за меню',
		popupFullScreen	: 'Цял екран (IE)',
		popupScrollBars	: 'Скролери',
		popupDependent	: 'Зависимост (Netscape)',
		popupLeft		: 'Лява позиция',
		popupTop		: 'Горна позиция',
		id				: 'ID',
		langDir			: 'Посока на езика',
		langDirLTR		: 'Ляво на Дясно (ЛнД)',
		langDirRTL		: 'Дясно на Ляво (ДнЛ)',
		acccessKey		: 'Ключ за достъп',
		name			: 'Име',
		langCode			: 'Код за езика',
		tabIndex			: 'Tab Index', // MISSING
		advisoryTitle		: 'Advisory Title', // MISSING
		advisoryContentType	: 'Advisory Content Type', // MISSING
		cssClasses		: 'Класове за CSS',
		charset			: 'Linked Resource Charset', // MISSING
		styles			: 'Стил',
		rel			: 'Връзка',
		selectAnchor		: 'Изберете котва',
		anchorName		: 'По име на котва',
		anchorId			: 'По ID на елемент',
		emailAddress		: 'E-mail aдрес',
		emailSubject		: 'Тема',
		emailBody		: 'Съдържание',
		noAnchors		: '(No anchors available in the document)', // MISSING
		noUrl			: 'Моля въведете URL адреса',
		noEmail			: 'Моля въведете e-mail aдрес'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Котва',
		menu		: 'Промяна на котва',
		title		: 'Настройки на котва',
		name		: 'Име на котва',
		errorName	: 'Моля въведете име на котвата',
		remove		: 'Премахване на котва'
	},

	// List style dialog
	list:
	{
		numberedTitle		: 'Numbered List Properties', // MISSING
		bulletedTitle		: 'Bulleted List Properties', // MISSING
		type				: 'Тип',
		start				: 'Старт',
		validateStartNumber				:'List start number must be a whole number.', // MISSING
		circle				: 'Кръг',
		disc				: 'Диск',
		square				: 'Квадрат',
		none				: 'Няма',
		notset				: '<не е указано>',
		armenian			: 'Арменско номериране',
		georgian			: 'Грузинско номериране (an, ban, gan, и т.н.)',
		lowerRoman			: 'Малки римски числа (i, ii, iii, iv, v и т.н.)',
		upperRoman			: 'Големи римски числа (I, II, III, IV, V и т.н.)',
		lowerAlpha			: 'Малки букви (а, б, в, г, д и т.н.)',
		upperAlpha			: 'Големи букви (А, Б, В, Г, Д и т.н.)',
		lowerGreek			: 'Малки гръцки букви (алфа, бета, гама и т.н.)',
		decimal				: 'Числа (1, 2, 3 и др.)',
		decimalLeadingZero	: 'Числа с водеща нула (01, 02, 03 и т.н.)'
	},

	// Find And Replace Dialog
	findAndReplace :
	{
		title				: 'Търсене и препокриване',
		find				: 'Търсене',
		replace				: 'Препокриване',
		findWhat			: 'Търси за:',
		replaceWith			: 'Препокрива с:',
		notFoundMsg			: 'Указаният текст не е намерен.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Съвпадение',
		matchWord			: 'Съвпадение с дума',
		matchCyclic			: 'Циклично съвпадение',
		replaceAll			: 'Препокрий всички',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Таблица',
		title		: 'Настройки на таблицата',
		menu		: 'Настройки на таблицата',
		deleteTable	: 'Изтриване на таблица',
		rows		: 'Редове',
		columns		: 'Колони',
		border		: 'Размер на рамката',
		widthPx		: 'пиксела',
		widthPc		: 'процент',
		widthUnit	: 'единица за ширина',
		cellSpace	: 'Разтояние между клетките',
		cellPad		: 'Отделяне на клетките',
		caption		: 'Заглавие',
		summary		: 'Обща информация',
		headers		: 'Хедъри',
		headersNone		: 'Няма',
		headersColumn	: 'Първа колона',
		headersRow		: 'Първи ред',
		headersBoth		: 'Заедно',
		invalidRows		: 'Броят редове трябва да е по-голям от 0.',
		invalidCols		: 'Броят колони трябва да е по-голям от 0.',
		invalidBorder	: 'Border size must be a number.', // MISSING
		invalidWidth	: 'Table width must be a number.', // MISSING
		invalidHeight	: 'Table height must be a number.', // MISSING
		invalidCellSpacing	: 'Cell spacing must be a positive number.', // MISSING
		invalidCellPadding	: 'Cell padding must be a positive number.', // MISSING

		cell :
		{
			menu			: 'Клетка',
			insertBefore	: 'Вмъкване на клетка преди',
			insertAfter		: 'Вмъкване на клетка след',
			deleteCell		: 'Изтриване на клетки',
			merge			: 'Сливане на клетки',
			mergeRight		: 'Сливане в дясно',
			mergeDown		: 'Merge Down', // MISSING
			splitHorizontal	: 'Split Cell Horizontally', // MISSING
			splitVertical	: 'Split Cell Vertically', // MISSING
			title			: 'Настройки на клетката',
			cellType		: 'Тип на клетката',
			rowSpan			: 'Rows Span', // MISSING
			colSpan			: 'Columns Span', // MISSING
			wordWrap		: 'Авто. пренос',
			hAlign			: 'Хоризонтално подравняване',
			vAlign			: 'Вертикално подравняване',
			alignBaseline	: 'Базова линия',
			bgColor			: 'Фон',
			borderColor		: 'Цвят на рамката',
			data			: 'Данни',
			header			: 'Хедър',
			yes				: 'Да',
			no				: 'Не',
			invalidWidth	: 'Cell width must be a number.', // MISSING
			invalidHeight	: 'Cell height must be a number.', // MISSING
			invalidRowSpan	: 'Rows span must be a whole number.', // MISSING
			invalidColSpan	: 'Columns span must be a whole number.', // MISSING
			chooseColor		: 'Изберете'
		},

		row :
		{
			menu			: 'Ред',
			insertBefore	: 'Insert Row Before', // MISSING
			insertAfter		: 'Вмъкване на ред след',
			deleteRow		: 'Изтриване на редове'
		},

		column :
		{
			menu			: 'Колона',
			insertBefore	: 'Вмъкване на колона преди',
			insertAfter		: 'Вмъкване на колона след',
			deleteColumn	: 'Изтриване на колони'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Настройки на бутона',
		text		: 'Текст (стойност)',
		type		: 'Тип',
		typeBtn		: 'Бутон',
		typeSbm		: 'Добави',
		typeRst		: 'Нулиране'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Checkbox Properties', // MISSING
		radioTitle	: 'Настройки на радиобутон',
		value		: 'Стойност',
		selected	: 'Избрано'
	},

	// Form Dialog.
	form :
	{
		title		: 'Настройки на формата',
		menu		: 'Настройки на формата',
		action		: 'Действие',
		method		: 'Метод',
		encoding	: 'Кодиране'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Selection Field Properties', // MISSING
		selectInfo	: 'Select Info', // MISSING
		opAvail		: 'Налични опции',
		value		: 'Стойност',
		size		: 'Размер',
		lines		: 'линии',
		chkMulti	: 'Allow multiple selections', // MISSING
		opText		: 'Текст',
		opValue		: 'Стойност',
		btnAdd		: 'Добави',
		btnModify	: 'Промени',
		btnUp		: 'На горе',
		btnDown		: 'На долу',
		btnSetValue : 'Set as selected value', // MISSING
		btnDelete	: 'Изтриване'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Опции за текстовата зона',
		cols		: 'Колони',
		rows		: 'Редове'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Настройки за текстово поле',
		name		: 'Име',
		value		: 'Стойност',
		charWidth	: 'Ширина на знаците',
		maxChars	: 'Макс. знаци',
		type		: 'Тип',
		typeText	: 'Текст',
		typePass	: 'Парола'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Настройки за скрито поле',
		name	: 'Име',
		value	: 'Стойност'
	},

	// Image Dialog.
	image :
	{
		title		: 'Настройки за снимка',
		titleButton	: 'Настойки за бутон за снимка',
		menu		: 'Настройки за снимка',
		infoTab		: 'Инфо за снимка',
		btnUpload	: 'Изпрати я на сървъра',
		upload		: 'Качване',
		alt			: 'Алтернативен текст',
		lockRatio	: 'Заключване на съотношението',
		resetSize	: 'Нулиране на размер',
		border		: 'Рамка',
		hSpace		: 'HSpace', // MISSING
		vSpace		: 'VSpace', // MISSING
		alertUrl	: 'Please type the image URL', // MISSING
		linkTab		: 'Връзка',
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
		properties		: 'Настройки за флаш',
		propertiesTab	: 'Настройки',
		title			: 'Настройки за флаш',
		chkPlay			: 'Авто. пускане',
		chkLoop			: 'Цикъл',
		chkMenu			: 'Enable Flash Menu', // MISSING
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Scale', // MISSING
		scaleAll		: 'Показва всичко',
		scaleNoBorder	: 'Без рамка',
		scaleFit		: 'Exact Fit', // MISSING
		access			: 'Script Access', // MISSING
		accessAlways	: 'Винаги',
		accessSameDomain: 'Същият домейн',
		accessNever		: 'Никога',
		alignAbsBottom	: 'Abs Bottom', // MISSING
		alignAbsMiddle	: 'Abs Middle', // MISSING
		alignBaseline	: 'Baseline', // MISSING
		alignTextTop	: 'Text Top', // MISSING
		quality			: 'Качество',
		qualityBest		: 'Отлично',
		qualityHigh		: 'Високо',
		qualityAutoHigh	: 'Авто. високо',
		qualityMedium	: 'Средно',
		qualityAutoLow	: 'Авто. ниско',
		qualityLow		: 'Ниско',
		windowModeWindow: 'Прозорец',
		windowModeOpaque: 'Плътност',
		windowModeTransparent : 'Прозрачност',
		windowMode		: 'Режим на прозореца',
		flashvars		: 'Променливи за Флаш',
		bgcolor			: 'Background color', // MISSING
		hSpace			: 'HSpace', // MISSING
		vSpace			: 'VSpace', // MISSING
		validateSrc		: 'Уеб адреса не трябва да е празен.',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Проверка на правопис',
		title			: 'Проверка на правопис',
		notAvailable	: 'Съжаляваме, но услугата не е достъпна за момента',
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Не е в речника',
		changeTo		: 'Промени на',
		btnIgnore		: 'Игнорирай',
		btnIgnoreAll	: 'Игнорирай всичко',
		btnReplace		: 'Препокриване',
		btnReplaceAll	: 'Препокрий всичко',
		btnUndo			: 'Възтанови',
		noSuggestions	: '- Няма препоръчани -',
		progress		: 'Проверява се правописа...',
		noMispell		: 'Spell check complete: No misspellings found', // MISSING
		noChanges		: 'Spell check complete: No words changed', // MISSING
		oneChange		: 'Spell check complete: One word changed', // MISSING
		manyChanges		: 'Spell check complete: %1 words changed', // MISSING
		ieSpellDownload	: 'Spell checker not installed. Do you want to download it now?' // MISSING
	},

	smiley :
	{
		toolbar	: 'Усмивка',
		title	: 'Вмъкване на усмивка',
		options : 'Опции за усмивката'
	},

	elementsPath :
	{
		eleLabel : 'Път за елементите',
		eleTitle : '%1 елемент'
	},

	numberedlist	: 'Вмъкване/Премахване на номериран списък',
	bulletedlist	: 'Вмъкване/Премахване на точков списък',
	indent			: 'Увеличаване на отстъпа',
	outdent			: 'Намаляване на отстъпа',

	justify :
	{
		left	: 'Подравни в ляво',
		center	: 'Център',
		right	: 'Подравни в дясно',
		block	: 'Justify' // MISSING
	},

	blockquote : 'Блок за цитат',

	clipboard :
	{
		title		: 'Paste', // MISSING
		cutError	: 'Настройките за сигурност на Вашия браузър не позволяват на редактора автоматично да изъплни действията за отрязване. Моля ползвайте клавиатурните команди за целта (ctrl+x).',
		copyError	: 'Your browser security settings don\'t permit the editor to automatically execute copying operations. Please use the keyboard for that (Ctrl/Cmd+C).', // MISSING
		pasteMsg	: 'Please paste inside the following box using the keyboard (<strong>Ctrl/Cmd+V</strong>) and hit OK', // MISSING
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Paste from Word', // MISSING
		title			: 'Paste from Word', // MISSING
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Paste as plain text', // MISSING
		title	: 'Paste as Plain Text' // MISSING
	},

	templates :
	{
		button			: 'Templates', // MISSING
		title			: 'Content Templates', // MISSING
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Please select the template to open in the editor', // MISSING
		emptyListMsg	: '(No templates defined)' // MISSING
	},

	showBlocks : 'Показва блокове',

	stylesCombo :
	{
		label		: 'Styles', // MISSING
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Format', // MISSING
		panelTitle	: 'Paragraph Format', // MISSING

		tag_p		: 'Normal', // MISSING
		tag_pre		: 'Formatted', // MISSING
		tag_address	: 'Address', // MISSING
		tag_h1		: 'Heading 1', // MISSING
		tag_h2		: 'Heading 2', // MISSING
		tag_h3		: 'Heading 3', // MISSING
		tag_h4		: 'Heading 4', // MISSING
		tag_h5		: 'Heading 5', // MISSING
		tag_h6		: 'Heading 6', // MISSING
		tag_div		: 'Normal (DIV)' // MISSING
	},

	div :
	{
		title				: 'Create Div Container', // MISSING
		toolbar				: 'Create Div Container', // MISSING
		cssClassInputLabel	: 'Stylesheet Classes', // MISSING
		styleSelectLabel	: 'Стил',
		IdInputLabel		: 'ID',
		languageCodeInputLabel	: ' Код на езика',
		inlineStyleInputLabel	: 'Inline Style', // MISSING
		advisoryTitleInputLabel	: 'Advisory Title', // MISSING
		langDirLabel		: 'Language Direction', // MISSING
		langDirLTRLabel		: 'Left to Right (LTR)', // MISSING
		langDirRTLLabel		: 'Right to Left (RTL)', // MISSING
		edit				: 'Промяна на Div',
		remove				: 'Премахване на Div'
  	},

	iframe :
	{
		title		: 'IFrame настройки',
		toolbar		: 'IFrame',
		noUrl		: 'Please type the iframe URL', // MISSING
		scrolling	: 'Enable scrollbars', // MISSING
		border		: 'Show frame border' // MISSING
	},

	font :
	{
		label		: 'Шрифт',
		voiceLabel	: 'Шрифт',
		panelTitle	: 'Име на шрифт'
	},

	fontSize :
	{
		label		: 'Размер',
		voiceLabel	: 'Размер на шрифт',
		panelTitle	: 'Размер на шрифт'
	},

	colorButton :
	{
		textColorTitle	: 'Цвят на шрифт',
		bgColorTitle	: 'Фонов цвят',
		panelTitle		: 'Цветове',
		auto			: 'Автоматично',
		more			: 'Още цветове'
	},

	colors :
	{
		'000' : 'Черно',
		'800000' : 'Кестеняво',
		'8B4513' : 'Светлокафяво',
		'2F4F4F' : 'Dark Slate Gray', // MISSING
		'008080' : 'Teal', // MISSING
		'000080' : 'Navy', // MISSING
		'4B0082' : 'Индиго',
		'696969' : 'Тъмно сиво',
		'B22222' : 'Огнено червено',
		'A52A2A' : 'Кафяво',
		'DAA520' : 'Златисто',
		'006400' : 'Тъмно зелено',
		'40E0D0' : 'Тюркуазено',
		'0000CD' : 'Средно синьо',
		'800080' : 'Пурпурно',
		'808080' : 'Сиво',
		'F00' : 'Червено',
		'FF8C00' : 'Тъмно оранжево',
		'FFD700' : 'Златно',
		'008000' : 'Зелено',
		'0FF' : 'Светло синьо',
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
		mixedWithDigits	: 'Игнорирани думи и цифри',

		languagesTab	: 'Езици',

		dictionariesTab	: 'Речници',
		dic_field_name	: 'Име на речнк',
		dic_create		: 'Нов',
		dic_restore		: 'Възтановяване',
		dic_delete		: 'Изтриване',
		dic_rename		: 'Преименуване',
		dic_info		: 'Initially the User Dictionary is stored in a Cookie. However, Cookies are limited in size. When the User Dictionary grows to a point where it cannot be stored in a Cookie, then the dictionary may be stored on our server. To store your personal dictionary on our server you should specify a name for your dictionary. If you already have a stored dictionary, please type its name and click the Restore button.', // MISSING

		aboutTab		: 'Относно'
	},

	about :
	{
		title		: 'Относно CKEditor',
		dlgTitle	: 'Относно CKEditor',
		help	: 'Проверете $1 за помощ.',
		userGuide : 'CKEditor User\'s Guide', // MISSING
		moreInfo	: 'За лицензионна информация моля посетете сайта ни:',
		copy		: 'Copyright &copy; $1. All rights reserved.'
	},

	maximize : 'Максимизиране',
	minimize : 'Минимизиране',

	fakeobjects :
	{
		anchor		: 'Кука',
		flash		: 'Флаш анимация',
		iframe		: 'IFrame',
		hiddenfield	: 'Скрито поле',
		unknown		: 'Неизвестен обект'
	},

	resize : 'Влачете за да оразмерите',

	colordialog :
	{
		title		: 'Изберете цвят',
		options	:	'Цветови опции',
		highlight	: 'Осветяване',
		selected	: 'Изберете цвят',
		clear		: 'Изчистване'
	},

	toolbarCollapse	: 'Свиване на лентата с инструменти',
	toolbarExpand	: 'Разширяване на лентата с инструменти',

	toolbarGroups :
	{
		document : 'Документ',
		clipboard : 'Clipboard/Undo', // MISSING
		editing : 'Промяна',
		forms : 'Форми',
		basicstyles : 'Базови стилове',
		paragraph : 'Параграф',
		links : 'Връзки',
		insert : 'Вмъкване',
		styles : 'Стилове',
		colors : 'Цветове',
		tools : 'Инструменти'
	},

	bidi :
	{
		ltr : 'Text direction from left to right', // MISSING
		rtl : 'Text direction from right to left' // MISSING
	},

	docprops :
	{
		label : 'Настройки на документа',
		title : 'Настройки на документа',
		design : 'Дизайн',
		meta : 'Мета етикети',
		chooseColor : 'Изберете',
		other : 'Други...',
		docTitle :	'Заглавие на страницата',
		charset : 	'Кодова таблица',
		charsetOther : 'Друга кодова таблица',
		charsetASCII : 'ASCII',
		charsetCE : 'Централна европейска',
		charsetCT : 'Китайски традиционен',
		charsetCR : 'Cyrillic', // MISSING
		charsetGR : 'Greek', // MISSING
		charsetJP : 'Japanese', // MISSING
		charsetKR : 'Korean', // MISSING
		charsetTR : 'Turkish', // MISSING
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Western European', // MISSING
		docType : 'Document Type Heading', // MISSING
		docTypeOther : 'Other Document Type Heading', // MISSING
		xhtmlDec : 'Include XHTML Declarations', // MISSING
		bgColor : 'Background Color', // MISSING
		bgImage : 'Background Image URL', // MISSING
		bgFixed : 'Non-scrolling (Fixed) Background', // MISSING
		txtColor : 'Text Color', // MISSING
		margin : 'Page Margins', // MISSING
		marginTop : 'Top', // MISSING
		marginLeft : 'Left', // MISSING
		marginRight : 'Right', // MISSING
		marginBottom : 'Bottom', // MISSING
		metaKeywords : 'Document Indexing Keywords (comma separated)', // MISSING
		metaDescription : 'Document Description', // MISSING
		metaAuthor : 'Author', // MISSING
		metaCopyright : 'Copyright', // MISSING
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
