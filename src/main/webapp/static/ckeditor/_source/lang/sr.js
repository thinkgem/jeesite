/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Serbian (Cyrillic) language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['sr'] =
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
	source			: 'Kôд',
	newPage			: 'Нова страница',
	save			: 'Сачувај',
	preview			: 'Изглед странице',
	cut				: 'Исеци',
	copy			: 'Копирај',
	paste			: 'Залепи',
	print			: 'Штампа',
	underline		: 'Подвучено',
	bold			: 'Подебљано',
	italic			: 'Курзив',
	selectAll		: 'Означи све',
	removeFormat	: 'Уклони форматирање',
	strike			: 'Прецртано',
	subscript		: 'Индекс',
	superscript		: 'Степен',
	horizontalrule	: 'Унеси хоризонталну линију',
	pagebreak		: 'Insert Page Break for Printing', // MISSING
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Уклони линк',
	undo			: 'Поништи акцију',
	redo			: 'Понови акцију',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Претражи сервер',
		url				: 'УРЛ',
		protocol		: 'Протокол',
		upload			: 'Пошаљи',
		uploadSubmit	: 'Пошаљи на сервер',
		image			: 'Слика',
		flash			: 'Флеш елемент',
		form			: 'Форма',
		checkbox		: 'Поље за потврду',
		radio			: 'Радио-дугме',
		textField		: 'Текстуално поље',
		textarea		: 'Зона текста',
		hiddenField		: 'Скривено поље',
		button			: 'Дугме',
		select			: 'Изборно поље',
		imageButton		: 'Дугме са сликом',
		notSet			: '<није постављено>',
		id				: 'Ид',
		name			: 'Назив',
		langDir			: 'Смер језика',
		langDirLtr		: 'С лева на десно (LTR)',
		langDirRtl		: 'С десна на лево (RTL)',
		langCode		: 'Kôд језика',
		longDescr		: 'Пун опис УРЛ',
		cssClass		: 'Stylesheet класе',
		advisoryTitle	: 'Advisory наслов',
		cssStyle		: 'Стил',
		ok				: 'OK',
		cancel			: 'Oткажи',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Напредни тагови',
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
		width			: 'Ширина',
		height			: 'Висина',
		align			: 'Равнање',
		alignLeft		: 'Лево',
		alignRight		: 'Десно',
		alignCenter		: 'Средина',
		alignTop		: 'Врх',
		alignMiddle		: 'Средина',
		alignBottom		: 'Доле',
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
		toolbar		: 'Унеси специјални карактер',
		title		: 'Одаберите специјални карактер',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Унеси/измени линк',
		other 		: '<other>', // MISSING
		menu		: 'Промени линк',
		title		: 'Линк',
		info		: 'Линк инфо',
		target		: 'Meтa',
		upload		: 'Пошаљи',
		advanced	: 'Напредни тагови',
		type		: 'Врста линка',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Сидро на овој страници',
		toEmail		: 'Eлектронска пошта',
		targetFrame		: '<оквир>',
		targetPopup		: '<искачући прозор>',
		targetFrameName	: 'Назив одредишног фрејма',
		targetPopupName	: 'Назив искачућег прозора',
		popupFeatures	: 'Могућности искачућег прозора',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Статусна линија',
		popupLocationBar: 'Локација',
		popupToolbar	: 'Toolbar',
		popupMenuBar	: 'Контекстни мени',
		popupFullScreen	: 'Приказ преко целог екрана (ИE)',
		popupScrollBars	: 'Скрол бар',
		popupDependent	: 'Зависно (Netscape)',
		popupLeft		: 'Од леве ивице екрана (пиксела)',
		popupTop		: 'Од врха екрана (пиксела)',
		id				: 'Id', // MISSING
		langDir			: 'Смер језика',
		langDirLTR		: 'С лева на десно (LTR)',
		langDirRTL		: 'С десна на лево (RTL)',
		acccessKey		: 'Приступни тастер',
		name			: 'Назив',
		langCode			: 'Смер језика',
		tabIndex			: 'Таб индекс',
		advisoryTitle		: 'Advisory наслов',
		advisoryContentType	: 'Advisory врста садржаја',
		cssClasses		: 'Stylesheet класе',
		charset			: 'Linked Resource Charset',
		styles			: 'Стил',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Одабери сидро',
		anchorName		: 'По називу сидра',
		anchorId			: 'Пo Ид-jу елемента',
		emailAddress		: 'Адреса електронске поште',
		emailSubject		: 'Наслов',
		emailBody		: 'Садржај поруке',
		noAnchors		: '(Нема доступних сидра)',
		noUrl			: 'Унесите УРЛ линка',
		noEmail			: 'Откуцајте адресу електронске поште'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Унеси/измени сидро',
		menu		: 'Особине сидра',
		title		: 'Особине сидра',
		name		: 'Име сидра',
		errorName	: 'Молимо Вас да унесете име сидра',
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
		title				: 'Find and Replace', // MISSING
		find				: 'Претрага',
		replace				: 'Замена',
		findWhat			: 'Пронађи:',
		replaceWith			: 'Замени са:',
		notFoundMsg			: 'Тражени текст није пронађен.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Разликуј велика и мала слова',
		matchWord			: 'Упореди целе речи',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Замени све',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Табела',
		title		: 'Особине табеле',
		menu		: 'Особине табеле',
		deleteTable	: 'Delete Table', // MISSING
		rows		: 'Редова',
		columns		: 'Kолона',
		border		: 'Величина оквира',
		widthPx		: 'пиксела',
		widthPc		: 'процената',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Ћелијски простор',
		cellPad		: 'Размак ћелија',
		caption		: 'Наслов табеле',
		summary		: 'Summary', // MISSING
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
			menu			: 'Cell', // MISSING
			insertBefore	: 'Insert Cell Before', // MISSING
			insertAfter		: 'Insert Cell After', // MISSING
			deleteCell		: 'Обриши ћелије',
			merge			: 'Спој ћелије',
			mergeRight		: 'Merge Right', // MISSING
			mergeDown		: 'Merge Down', // MISSING
			splitHorizontal	: 'Split Cell Horizontally', // MISSING
			splitVertical	: 'Split Cell Vertically', // MISSING
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
			menu			: 'Row', // MISSING
			insertBefore	: 'Insert Row Before', // MISSING
			insertAfter		: 'Insert Row After', // MISSING
			deleteRow		: 'Обриши редове'
		},

		column :
		{
			menu			: 'Column', // MISSING
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Обриши колоне'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Особине дугмета',
		text		: 'Текст (вредност)',
		type		: 'Tип',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Особине поља за потврду',
		radioTitle	: 'Особине радио-дугмета',
		value		: 'Вредност',
		selected	: 'Означено'
	},

	// Form Dialog.
	form :
	{
		title		: 'Особине форме',
		menu		: 'Особине форме',
		action		: 'Aкција',
		method		: 'Mетода',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Особине изборног поља',
		selectInfo	: 'Инфо',
		opAvail		: 'Доступне опције',
		value		: 'Вредност',
		size		: 'Величина',
		lines		: 'линија',
		chkMulti	: 'Дозволи вишеструку селекцију',
		opText		: 'Текст',
		opValue		: 'Вредност',
		btnAdd		: 'Додај',
		btnModify	: 'Измени',
		btnUp		: 'Горе',
		btnDown		: 'Доле',
		btnSetValue : 'Подеси као означену вредност',
		btnDelete	: 'Обриши'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Особине зоне текста',
		cols		: 'Број колона',
		rows		: 'Број редова'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Особине текстуалног поља',
		name		: 'Назив',
		value		: 'Вредност',
		charWidth	: 'Ширина (карактера)',
		maxChars	: 'Максимално карактера',
		type		: 'Тип',
		typeText	: 'Текст',
		typePass	: 'Лозинка'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Особине скривеног поља',
		name	: 'Назив',
		value	: 'Вредност'
	},

	// Image Dialog.
	image :
	{
		title		: 'Особине слика',
		titleButton	: 'Особине дугмета са сликом',
		menu		: 'Особине слика',
		infoTab		: 'Инфо слике',
		btnUpload	: 'Пошаљи на сервер',
		upload		: 'Пошаљи',
		alt			: 'Алтернативни текст',
		lockRatio	: 'Закључај однос',
		resetSize	: 'Ресетуј величину',
		border		: 'Оквир',
		hSpace		: 'HSpace',
		vSpace		: 'VSpace',
		alertUrl	: 'Унесите УРЛ слике',
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
		properties		: 'Особине Флеша',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Особине флеша',
		chkPlay			: 'Аутоматски старт',
		chkLoop			: 'Понављај',
		chkMenu			: 'Укључи флеш мени',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Скалирај',
		scaleAll		: 'Прикажи све',
		scaleNoBorder	: 'Без ивице',
		scaleFit		: 'Попуни површину',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs доле',
		alignAbsMiddle	: 'Abs средина',
		alignBaseline	: 'Базно',
		alignTextTop	: 'Врх текста',
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
		bgcolor			: 'Боја позадине',
		hSpace			: 'HSpace',
		vSpace			: 'VSpace',
		validateSrc		: 'Унесите УРЛ линка',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Провери спеловање',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Није у речнику',
		changeTo		: 'Измени',
		btnIgnore		: 'Игнориши',
		btnIgnoreAll	: 'Игнориши све',
		btnReplace		: 'Замени',
		btnReplaceAll	: 'Замени све',
		btnUndo			: 'Врати акцију',
		noSuggestions	: '- Без сугестија -',
		progress		: 'Провера спеловања у току...',
		noMispell		: 'Провера спеловања завршена: грешке нису пронађене',
		noChanges		: 'Провера спеловања завршена: Није измењена ниједна реч',
		oneChange		: 'Провера спеловања завршена: Измењена је једна реч',
		manyChanges		: 'Провера спеловања завршена:  %1 реч(и) је измењено',
		ieSpellDownload	: 'Провера спеловања није инсталирана. Да ли желите да је скинете са Интернета?'
	},

	smiley :
	{
		toolbar	: 'Смајли',
		title	: 'Унеси смајлија',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Набројиву листу',
	bulletedlist	: 'Ненабројива листа',
	indent			: 'Увећај леву маргину',
	outdent			: 'Смањи леву маргину',

	justify :
	{
		left	: 'Лево равнање',
		center	: 'Центриран текст',
		right	: 'Десно равнање',
		block	: 'Обострано равнање'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'Залепи',
		cutError	: 'Сигурносна подешавања Вашег претраживача не дозвољавају операције аутоматског исецања текста. Молимо Вас да користите пречицу са тастатуре (Ctrl/Cmd+X).',
		copyError	: 'Сигурносна подешавања Вашег претраживача не дозвољавају операције аутоматског копирања текста. Молимо Вас да користите пречицу са тастатуре (Ctrl/Cmd+C).',
		pasteMsg	: 'Молимо Вас да залепите унутар доње површине користећи тастатурну пречицу (<STRONG>Ctrl/Cmd+V</STRONG>) и да притиснете <STRONG>OK</STRONG>.',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Залепи из Worda',
		title			: 'Залепи из Worda',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Залепи као чист текст',
		title	: 'Залепи као чист текст'
	},

	templates :
	{
		button			: 'Обрасци',
		title			: 'Обрасци за садржај',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Молимо Вас да одаберете образац који ће бити примењен на страницу (тренутни садржај ће бити обрисан):',
		emptyListMsg	: '(Нема дефинисаних образаца)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'Стил',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Формат',
		panelTitle	: 'Формат',

		tag_p		: 'Normal',
		tag_pre		: 'Formatirano',
		tag_address	: 'Adresa',
		tag_h1		: 'Heading 1',
		tag_h2		: 'Heading 2',
		tag_h3		: 'Heading 3',
		tag_h4		: 'Heading 4',
		tag_h5		: 'Heading 5',
		tag_h6		: 'Heading 6',
		tag_div		: 'Normal (DIV)' // MISSING
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
		label		: 'Величина фонта',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Величина фонта'
	},

	colorButton :
	{
		textColorTitle	: 'Боја текста',
		bgColorTitle	: 'Боја позадине',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Аутоматски',
		more			: 'Више боја...'
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
		label : 'Особине документа',
		title : 'Особине документа',
		design : 'Design', // MISSING
		meta : 'Метаподаци',
		chooseColor : 'Choose', // MISSING
		other : '<other>',
		docTitle :	'Наслов странице',
		charset : 	'Кодирање скупа карактера',
		charsetOther : 'Остала кодирања скупа карактера',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Central European', // MISSING
		charsetCT : 'Chinese Traditional (Big5)', // MISSING
		charsetCR : 'Cyrillic', // MISSING
		charsetGR : 'Greek', // MISSING
		charsetJP : 'Japanese', // MISSING
		charsetKR : 'Korean', // MISSING
		charsetTR : 'Turkish', // MISSING
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Western European', // MISSING
		docType : 'Заглавље типа документа',
		docTypeOther : 'Остала заглавља типа документа',
		xhtmlDec : 'Улључи XHTML декларације',
		bgColor : 'Боја позадине',
		bgImage : 'УРЛ позадинске слике',
		bgFixed : 'Фиксирана позадина',
		txtColor : 'Боја текста',
		margin : 'Маргине странице',
		marginTop : 'Горња',
		marginLeft : 'Лева',
		marginRight : 'Десна',
		marginBottom : 'Доња',
		metaKeywords : 'Кључне речи за индексирање документа (раздвојене зарезом)',
		metaDescription : 'Опис документа',
		metaAuthor : 'Аутор',
		metaCopyright : 'Ауторска права',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
