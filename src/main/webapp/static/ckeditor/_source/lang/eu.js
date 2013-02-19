/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Basque language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['eu'] =
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
	source			: 'HTML Iturburua',
	newPage			: 'Orrialde Berria',
	save			: 'Gorde',
	preview			: 'Aurrebista',
	cut				: 'Ebaki',
	copy			: 'Kopiatu',
	paste			: 'Itsatsi',
	print			: 'Inprimatu',
	underline		: 'Azpimarratu',
	bold			: 'Lodia',
	italic			: 'Etzana',
	selectAll		: 'Hautatu dena',
	removeFormat	: 'Kendu Formatua',
	strike			: 'Marratua',
	subscript		: 'Azpi-indize',
	superscript		: 'Goi-indize',
	horizontalrule	: 'Txertatu Marra Horizontala',
	pagebreak		: 'Txertatu Orrialde-jauzia',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Kendu Esteka',
	undo			: 'Desegin',
	redo			: 'Berregin',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Zerbitzaria arakatu',
		url				: 'URL',
		protocol		: 'Protokoloa',
		upload			: 'Gora kargatu',
		uploadSubmit	: 'Zerbitzarira bidalia',
		image			: 'Irudia',
		flash			: 'Flasha',
		form			: 'Formularioa',
		checkbox		: 'Kontrol-laukia',
		radio			: 'Aukera-botoia',
		textField		: 'Testu Eremua',
		textarea		: 'Testu-area',
		hiddenField		: 'Ezkutuko Eremua',
		button			: 'Botoia',
		select			: 'Hautespen Eremua',
		imageButton		: 'Irudi Botoia',
		notSet			: '<Ezarri gabe>',
		id				: 'Id',
		name			: 'Izena',
		langDir			: 'Hizkuntzaren Norabidea',
		langDirLtr		: 'Ezkerretik Eskumara(LTR)',
		langDirRtl		: 'Eskumatik Ezkerrera (RTL)',
		langCode		: 'Hizkuntza Kodea',
		longDescr		: 'URL Deskribapen Luzea',
		cssClass		: 'Estilo-orriko Klaseak',
		advisoryTitle	: 'Izenburua',
		cssStyle		: 'Estiloa',
		ok				: 'Ados',
		cancel			: 'Utzi',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'Orokorra',
		advancedTab		: 'Aurreratua',
		validateNumberFailed : 'Balio hau ez da zenbaki bat.',
		confirmNewPage	: 'Eduki honetan gorde gabe dauden aldaketak galduko dira. Ziur zaude orri berri bat kargatu nahi duzula?',
		confirmCancel	: 'Aukera batzuk aldatu egin dira. Ziur zaude elkarrizketa-koadroa itxi nahi duzula?',
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
		width			: 'Zabalera',
		height			: 'Altuera',
		align			: 'Lerrokatu',
		alignLeft		: 'Ezkerrera',
		alignRight		: 'Eskuman',
		alignCenter		: 'Erdian',
		alignTop		: 'Goian',
		alignMiddle		: 'Erdian',
		alignBottom		: 'Behean',
		invalidHeight	: 'Altuera zenbaki bat izan behar da.',
		invalidWidth	: 'Zabalera zenbaki bat izan behar da.',
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, erabilezina</span>'
	},

	contextmenu :
	{
		options : 'Context Menu Options' // MISSING
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Txertatu Karaktere Berezia',
		title		: 'Karaktere Berezia Aukeratu',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Txertatu/Editatu Esteka',
		other 		: '<other>', // MISSING
		menu		: 'Aldatu Esteka',
		title		: 'Esteka',
		info		: 'Estekaren Informazioa',
		target		: 'Target (Helburua)',
		upload		: 'Gora kargatu',
		advanced	: 'Aurreratua',
		type		: 'Esteka Mota',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Aingura orrialde honetan',
		toEmail		: 'ePosta',
		targetFrame		: '<marko>',
		targetPopup		: '<popup leihoa>',
		targetFrameName	: 'Marko Helburuaren Izena',
		targetPopupName	: 'Popup Leihoaren Izena',
		popupFeatures	: 'Popup Leihoaren Ezaugarriak',
		popupResizable	: 'Tamaina Aldakorra',
		popupStatusBar	: 'Egoera Barra',
		popupLocationBar: 'Kokaleku Barra',
		popupToolbar	: 'Tresna Barra',
		popupMenuBar	: 'Menu Barra',
		popupFullScreen	: 'Pantaila Osoa (IE)',
		popupScrollBars	: 'Korritze Barrak',
		popupDependent	: 'Menpekoa (Netscape)',
		popupLeft		: 'Ezkerreko  Posizioa',
		popupTop		: 'Goiko Posizioa',
		id				: 'Id',
		langDir			: 'Hizkuntzaren Norabidea',
		langDirLTR		: 'Ezkerretik Eskumara(LTR)',
		langDirRTL		: 'Eskumatik Ezkerrera (RTL)',
		acccessKey		: 'Sarbide-gakoa',
		name			: 'Izena',
		langCode			: 'Hizkuntzaren Norabidea',
		tabIndex			: 'Tabulazio Indizea',
		advisoryTitle		: 'Izenburua',
		advisoryContentType	: 'Eduki Mota (Content Type)',
		cssClasses		: 'Estilo-orriko Klaseak',
		charset			: 'Estekatutako Karaktere Multzoa',
		styles			: 'Estiloa',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Aingura bat hautatu',
		anchorName		: 'Aingura izenagatik',
		anchorId			: 'Elementuaren ID-gatik',
		emailAddress		: 'ePosta Helbidea',
		emailSubject		: 'Mezuaren Gaia',
		emailBody		: 'Mezuaren Gorputza',
		noAnchors		: '(Ez daude aingurak eskuragarri dokumentuan)',
		noUrl			: 'Mesedez URL esteka idatzi',
		noEmail			: 'Mesedez ePosta helbidea idatzi'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Aingura',
		menu		: 'Ainguraren Ezaugarriak',
		title		: 'Ainguraren Ezaugarriak',
		name		: 'Ainguraren Izena',
		errorName	: 'Idatzi ainguraren izena',
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
		title				: 'Bilatu eta Ordeztu',
		find				: 'Bilatu',
		replace				: 'Ordezkatu',
		findWhat			: 'Zer bilatu:',
		replaceWith			: 'Zerekin ordeztu:',
		notFoundMsg			: 'Idatzitako testua ez da topatu.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Maiuskula/minuskula',
		matchWord			: 'Esaldi osoa bilatu',
		matchCyclic			: 'Bilaketa ziklikoa',
		replaceAll			: 'Ordeztu Guztiak',
		replaceSuccessMsg	: 'Zenbat aldiz ordeztua: %1'
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Taula',
		title		: 'Taularen Ezaugarriak',
		menu		: 'Taularen Ezaugarriak',
		deleteTable	: 'Ezabatu Taula',
		rows		: 'Lerroak',
		columns		: 'Zutabeak',
		border		: 'Ertzaren Zabalera',
		widthPx		: 'pixel',
		widthPc		: 'ehuneko',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Gelaxka arteko tartea',
		cellPad		: 'Gelaxken betegarria',
		caption		: 'Epigrafea',
		summary		: 'Laburpena',
		headers		: 'Goiburuak',
		headersNone		: 'Bat ere ez',
		headersColumn	: 'Lehen zutabea',
		headersRow		: 'Lehen lerroa',
		headersBoth		: 'Biak',
		invalidRows		: 'Lerro kopurua 0 baino handiagoa den zenbakia izan behar da.',
		invalidCols		: 'Zutabe kopurua 0 baino handiagoa den zenbakia izan behar da.',
		invalidBorder	: 'Ertzaren tamaina zenbaki bat izan behar da.',
		invalidWidth	: 'Taularen zabalera zenbaki bat izan behar da.',
		invalidHeight	: 'Taularen altuera zenbaki bat izan behar da.',
		invalidCellSpacing	: 'Gelaxka arteko tartea zenbaki bat izan behar da.',
		invalidCellPadding	: 'Gelaxken betegarria zenbaki bat izan behar da.',

		cell :
		{
			menu			: 'Gelaxka',
			insertBefore	: 'Txertatu Gelaxka Aurretik',
			insertAfter		: 'Txertatu Gelaxka Ostean',
			deleteCell		: 'Kendu Gelaxkak',
			merge			: 'Batu Gelaxkak',
			mergeRight		: 'Elkartu Eskumara',
			mergeDown		: 'Elkartu Behera',
			splitHorizontal	: 'Banatu Gelaxkak Horizontalki',
			splitVertical	: 'Banatu Gelaxkak Bertikalki',
			title			: 'Gelaxken Ezaugarriak',
			cellType		: 'Gelaxka Mota',
			rowSpan			: 'Hedatutako Lerroak',
			colSpan			: 'Hedatutako Zutabeak',
			wordWrap		: 'Itzulbira',
			hAlign			: 'Lerrokatze Horizontala',
			vAlign			: 'Lerrokatze Bertikala',
			alignBaseline	: 'Oinarri-lerroan',
			bgColor			: 'Fondoaren Kolorea',
			borderColor		: 'Ertzaren Kolorea',
			data			: 'Data',
			header			: 'Goiburua',
			yes				: 'Bai',
			no				: 'Ez',
			invalidWidth	: 'Gelaxkaren zabalera zenbaki bat izan behar da.',
			invalidHeight	: 'Gelaxkaren altuera zenbaki bat izan behar da.',
			invalidRowSpan	: 'Lerroen hedapena zenbaki osoa izan behar da.',
			invalidColSpan	: 'Zutabeen hedapena zenbaki osoa izan behar da.',
			chooseColor		: 'Choose' // MISSING
		},

		row :
		{
			menu			: 'Lerroa',
			insertBefore	: 'Txertatu Lerroa Aurretik',
			insertAfter		: 'Txertatu Lerroa Ostean',
			deleteRow		: 'Ezabatu Lerroak'
		},

		column :
		{
			menu			: 'Zutabea',
			insertBefore	: 'Txertatu Zutabea Aurretik',
			insertAfter		: 'Txertatu Zutabea Ostean',
			deleteColumn	: 'Ezabatu Zutabeak'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Botoiaren Ezaugarriak',
		text		: 'Testua (Balorea)',
		type		: 'Mota',
		typeBtn		: 'Botoia',
		typeSbm		: 'Bidali',
		typeRst		: 'Garbitu'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Kontrol-laukiko Ezaugarriak',
		radioTitle	: 'Aukera-botoiaren Ezaugarriak',
		value		: 'Balorea',
		selected	: 'Hautatuta'
	},

	// Form Dialog.
	form :
	{
		title		: 'Formularioaren Ezaugarriak',
		menu		: 'Formularioaren Ezaugarriak',
		action		: 'Ekintza',
		method		: 'Metodoa',
		encoding	: 'Kodeketa'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Hautespen Eremuaren Ezaugarriak',
		selectInfo	: 'Informazioa',
		opAvail		: 'Aukera Eskuragarriak',
		value		: 'Balorea',
		size		: 'Tamaina',
		lines		: 'lerro kopurura',
		chkMulti	: 'Hautaketa anitzak baimendu',
		opText		: 'Testua',
		opValue		: 'Balorea',
		btnAdd		: 'Gehitu',
		btnModify	: 'Aldatu',
		btnUp		: 'Gora',
		btnDown		: 'Behera',
		btnSetValue : 'Aukeratutako balorea ezarri',
		btnDelete	: 'Ezabatu'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Testu-arearen Ezaugarriak',
		cols		: 'Zutabeak',
		rows		: 'Lerroak'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Testu Eremuaren Ezaugarriak',
		name		: 'Izena',
		value		: 'Balorea',
		charWidth	: 'Zabalera',
		maxChars	: 'Zenbat karaktere gehienez',
		type		: 'Mota',
		typeText	: 'Testua',
		typePass	: 'Pasahitza'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Ezkutuko Eremuaren Ezaugarriak',
		name	: 'Izena',
		value	: 'Balorea'
	},

	// Image Dialog.
	image :
	{
		title		: 'Irudi Ezaugarriak',
		titleButton	: 'Irudi Botoiaren Ezaugarriak',
		menu		: 'Irudi Ezaugarriak',
		infoTab		: 'Irudi informazioa',
		btnUpload	: 'Zerbitzarira bidalia',
		upload		: 'Gora Kargatu',
		alt			: 'Ordezko Testua',
		lockRatio	: 'Erlazioa Blokeatu',
		resetSize	: 'Tamaina Berrezarri',
		border		: 'Ertza',
		hSpace		: 'HSpace',
		vSpace		: 'VSpace',
		alertUrl	: 'Mesedez Irudiaren URLa idatzi',
		linkTab		: 'Esteka',
		button2Img	: 'Aukeratutako irudi botoia, irudi normal batean eraldatu nahi duzu?',
		img2Button	: 'Aukeratutako irudia, irudi botoi batean eraldatu nahi duzu?',
		urlMissing	: 'Image source URL is missing.', // MISSING
		validateBorder	: 'Border must be a whole number.', // MISSING
		validateHSpace	: 'HSpace must be a whole number.', // MISSING
		validateVSpace	: 'VSpace must be a whole number.' // MISSING
	},

	// Flash Dialog
	flash :
	{
		properties		: 'Flasharen Ezaugarriak',
		propertiesTab	: 'Ezaugarriak',
		title			: 'Flasharen Ezaugarriak',
		chkPlay			: 'Automatikoki Erreproduzitu',
		chkLoop			: 'Begizta',
		chkMenu			: 'Flasharen Menua Gaitu',
		chkFull			: 'Onartu Pantaila osoa',
 		scale			: 'Eskalatu',
		scaleAll		: 'Dena erakutsi',
		scaleNoBorder	: 'Ertzik gabe',
		scaleFit		: 'Doitu',
		access			: 'Scriptak baimendu',
		accessAlways	: 'Beti',
		accessSameDomain: 'Domeinu berdinekoak',
		accessNever		: 'Inoiz ere ez',
		alignAbsBottom	: 'Abs Behean',
		alignAbsMiddle	: 'Abs Erdian',
		alignBaseline	: 'Oinan',
		alignTextTop	: 'Testua Goian',
		quality			: 'Kalitatea',
		qualityBest		: 'Hoberena',
		qualityHigh		: 'Altua',
		qualityAutoHigh	: 'Auto Altua',
		qualityMedium	: 'Ertaina',
		qualityAutoLow	: 'Auto Baxua',
		qualityLow		: 'Baxua',
		windowModeWindow: 'Leihoa',
		windowModeOpaque: 'Opakoa',
		windowModeTransparent : 'Gardena',
		windowMode		: 'Leihoaren modua',
		flashvars		: 'Flash Aldagaiak',
		bgcolor			: 'Atzeko kolorea',
		hSpace			: 'HSpace',
		vSpace			: 'VSpace',
		validateSrc		: 'Mesedez URL esteka idatzi',
		validateHSpace	: 'HSpace zenbaki bat izan behar da.',
		validateVSpace	: 'VSpace zenbaki bat izan behar da.'
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Ortografia',
		title			: 'Ortografia zuzenketa',
		notAvailable	: 'Barkatu baina momentu honetan zerbitzua ez dago erabilgarri.',
		errorLoading	: 'Errorea gertatu da aplikazioa zerbitzaritik kargatzean: %s.',
		notInDic		: 'Ez dago hiztegian',
		changeTo		: 'Honekin ordezkatu',
		btnIgnore		: 'Ezikusi',
		btnIgnoreAll	: 'Denak Ezikusi',
		btnReplace		: 'Ordezkatu',
		btnReplaceAll	: 'Denak Ordezkatu',
		btnUndo			: 'Desegin',
		noSuggestions	: '- Iradokizunik ez -',
		progress		: 'Zuzenketa ortografikoa martxan...',
		noMispell		: 'Zuzenketa ortografikoa bukatuta: Akatsik ez',
		noChanges		: 'Zuzenketa ortografikoa bukatuta: Ez da ezer aldatu',
		oneChange		: 'Zuzenketa ortografikoa bukatuta: Hitz bat aldatu da',
		manyChanges		: 'Zuzenketa ortografikoa bukatuta: %1 hitz aldatu dira',
		ieSpellDownload	: 'Zuzentzaile ortografikoa ez dago instalatuta. Deskargatu nahi duzu?'
	},

	smiley :
	{
		toolbar	: 'Aurpegierak',
		title	: 'Aurpegiera Sartu',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 elementua'
	},

	numberedlist	: 'Zenbakidun Zerrenda',
	bulletedlist	: 'Buletdun Zerrenda',
	indent			: 'Handitu Koska',
	outdent			: 'Txikitu Koska',

	justify :
	{
		left	: 'Lerrokatu Ezkerrean',
		center	: 'Lerrokatu Erdian',
		right	: 'Lerrokatu Eskuman',
		block	: 'Justifikatu'
	},

	blockquote : 'Aipamen blokea',

	clipboard :
	{
		title		: 'Itsatsi',
		cutError	: 'Zure web nabigatzailearen segurtasun ezarpenak testuak automatikoki moztea ez dute baimentzen. Mesedez teklatua erabili ezazu (Ctrl/Cmd+X).',
		copyError	: 'Zure web nabigatzailearen segurtasun ezarpenak testuak automatikoki kopiatzea ez dute baimentzen. Mesedez teklatua erabili ezazu (Ctrl/Cmd+C).',
		pasteMsg	: 'Mesedez teklatua erabilita (<STRONG>Ctrl/Cmd+V</STRONG>) ondorego eremuan testua itsatsi eta <STRONG>OK</STRONG> sakatu.',
		securityMsg	: 'Nabigatzailearen segurtasun ezarpenak direla eta, editoreak ezin du arbela zuzenean erabili. Leiho honetan berriro itsatsi behar duzu.',
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'Itsatsi nahi duzun testua Wordetik hartua dela dirudi. Itsatsi baino lehen garbitu nahi duzu?',
		toolbar			: 'Itsatsi Word-etik',
		title			: 'Itsatsi Word-etik',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Testu Arrunta bezala Itsatsi',
		title	: 'Testu Arrunta bezala Itsatsi'
	},

	templates :
	{
		button			: 'Txantiloiak',
		title			: 'Eduki Txantiloiak',
		options : 'Template Options', // MISSING
		insertOption	: 'Ordeztu oraingo edukiak',
		selectPromptMsg	: 'Mesedez txantiloia aukeratu editorean kargatzeko<br>(orain dauden edukiak galduko dira):',
		emptyListMsg	: '(Ez dago definitutako txantiloirik)'
	},

	showBlocks : 'Blokeak erakutsi',

	stylesCombo :
	{
		label		: 'Estiloa',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Bloke Estiloak',
		panelTitle2	: 'Inline Estiloak',
		panelTitle3	: 'Objektu Estiloak'
	},

	format :
	{
		label		: 'Formatua',
		panelTitle	: 'Formatua',

		tag_p		: 'Arrunta',
		tag_pre		: 'Formateatua',
		tag_address	: 'Helbidea',
		tag_h1		: 'Izenburua 1',
		tag_h2		: 'Izenburua 2',
		tag_h3		: 'Izenburua 3',
		tag_h4		: 'Izenburua 4',
		tag_h5		: 'Izenburua 5',
		tag_h6		: 'Izenburua 6',
		tag_div		: 'Paragrafoa (DIV)'
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
		label		: 'Letra-tipoa',
		voiceLabel	: 'Letra-tipoa',
		panelTitle	: 'Letra-tipoa'
	},

	fontSize :
	{
		label		: 'Tamaina',
		voiceLabel	: 'Tamaina',
		panelTitle	: 'Tamaina'
	},

	colorButton :
	{
		textColorTitle	: 'Testu Kolorea',
		bgColorTitle	: 'Atzeko kolorea',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automatikoa',
		more			: 'Kolore gehiago...'
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
		title			: 'Ortografia Zuzenketa Idatzi Ahala (SCAYT)',
		opera_title		: 'Not supported by Opera', // MISSING
		enable			: 'Gaitu SCAYT',
		disable			: 'Desgaitu SCAYT',
		about			: 'SCAYTi buruz',
		toggle			: 'SCAYT aldatu',
		options			: 'Aukerak',
		langs			: 'Hizkuntzak',
		moreSuggestions	: 'Iradokizun gehiago',
		ignore			: 'Baztertu',
		ignoreAll		: 'Denak baztertu',
		addWord			: 'Hitza Gehitu',
		emptyDic		: 'Hiztegiaren izena ezin da hutsik egon.',

		optionsTab		: 'Aukerak',
		allCaps			: 'Ignore All-Caps Words', // MISSING
		ignoreDomainNames : 'Ignore Domain Names', // MISSING
		mixedCase		: 'Ignore Words with Mixed Case', // MISSING
		mixedWithDigits	: 'Ignore Words with Numbers', // MISSING

		languagesTab	: 'Hizkuntzak',

		dictionariesTab	: 'Hiztegiak',
		dic_field_name	: 'Dictionary name', // MISSING
		dic_create		: 'Create', // MISSING
		dic_restore		: 'Restore', // MISSING
		dic_delete		: 'Delete', // MISSING
		dic_rename		: 'Rename', // MISSING
		dic_info		: 'Initially the User Dictionary is stored in a Cookie. However, Cookies are limited in size. When the User Dictionary grows to a point where it cannot be stored in a Cookie, then the dictionary may be stored on our server. To store your personal dictionary on our server you should specify a name for your dictionary. If you already have a stored dictionary, please type its name and click the Restore button.', // MISSING

		aboutTab		: 'Honi buruz'
	},

	about :
	{
		title		: 'CKEditor(r)i buruz',
		dlgTitle	: 'CKEditor(r)i buruz',
		help	: 'Check $1 for help.', // MISSING
		userGuide : 'CKEditor User\'s Guide', // MISSING
		moreInfo	: 'Lizentziari buruzko informazioa gure webgunean:',
		copy		: 'Copyright &copy; $1. Eskubide guztiak erreserbaturik.'
	},

	maximize : 'Maximizatu',
	minimize : 'Minimize', // MISSING

	fakeobjects :
	{
		anchor		: 'Aingura',
		flash		: 'Flash Animazioa',
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'Objektu ezezaguna'
	},

	resize : 'Arrastatu tamaina aldatzeko',

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
		label : 'Dokumentuaren Ezarpenak',
		title : 'Dokumentuaren Ezarpenak',
		design : 'Design', // MISSING
		meta : 'Meta Informazioa',
		chooseColor : 'Choose', // MISSING
		other : '<other>',
		docTitle :	'Orriaren Izenburua',
		charset : 	'Karaktere Multzoaren Kodeketa',
		charsetOther : 'Beste Karaktere Multzoko Kodeketa',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Erdialdeko Europakoa',
		charsetCT : 'Txinatar Tradizionala (Big5)',
		charsetCR : 'Zirilikoa',
		charsetGR : 'Grekoa',
		charsetJP : 'Japoniarra',
		charsetKR : 'Korearra',
		charsetTR : 'Turkiarra',
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Mendebaldeko Europakoa',
		docType : 'Document Type Goiburua',
		docTypeOther : 'Beste Document Type Goiburua',
		xhtmlDec : 'XHTML Ezarpenak',
		bgColor : 'Atzeko Kolorea',
		bgImage : 'Atzeko Irudiaren URL-a',
		bgFixed : 'Korritze gabeko Atzealdea',
		txtColor : 'Testu Kolorea',
		margin : 'Orrialdearen marjinak',
		marginTop : 'Goian',
		marginLeft : 'Ezkerrean',
		marginRight : 'Eskuman',
		marginBottom : 'Behean',
		metaKeywords : 'Dokumentuaren Gako-hitzak (komarekin bananduta)',
		metaDescription : 'Dokumentuaren Deskribapena',
		metaAuthor : 'Egilea',
		metaCopyright : 'Copyright', // MISSING
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
