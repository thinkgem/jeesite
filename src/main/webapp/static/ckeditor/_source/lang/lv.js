/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Latvian language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['lv'] =
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
	source			: 'HTML kods',
	newPage			: 'Jauna lapa',
	save			: 'Saglabāt',
	preview			: 'Pārskatīt',
	cut				: 'Izgriezt',
	copy			: 'Kopēt',
	paste			: 'Ievietot',
	print			: 'Drukāt',
	underline		: 'Apakšsvītra',
	bold			: 'Treknu šriftu',
	italic			: 'Slīprakstā',
	selectAll		: 'Iezīmēt visu',
	removeFormat	: 'Noņemt stilus',
	strike			: 'Pārsvītrots',
	subscript		: 'Zemrakstā',
	superscript		: 'Augšrakstā',
	horizontalrule	: 'Ievietot horizontālu Atdalītājsvītru',
	pagebreak		: 'Ievietot lapas pārtraukumu',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Noņemt hipersaiti',
	undo			: 'Atcelt',
	redo			: 'Atkārtot',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Skatīt servera saturu',
		url				: 'URL',
		protocol		: 'Protokols',
		upload			: 'Augšupielādēt',
		uploadSubmit	: 'Nosūtīt serverim',
		image			: 'Attēls',
		flash			: 'Flash',
		form			: 'Forma',
		checkbox		: 'Atzīmēšanas kastīte',
		radio			: 'Izvēles poga',
		textField		: 'Teksta rinda',
		textarea		: 'Teksta laukums',
		hiddenField		: 'Paslēpta teksta rinda',
		button			: 'Poga',
		select			: 'Iezīmēšanas lauks',
		imageButton		: 'Attēlpoga',
		notSet			: '<nav iestatīts>',
		id				: 'Id',
		name			: 'Nosaukums',
		langDir			: 'Valodas lasīšanas virziens',
		langDirLtr		: 'No kreisās uz labo (LTR)',
		langDirRtl		: 'No labās uz kreiso (RTL)',
		langCode		: 'Valodas kods',
		longDescr		: 'Gara apraksta Hipersaite',
		cssClass		: 'Stilu saraksta klases',
		advisoryTitle	: 'Konsultatīvs virsraksts',
		cssStyle		: 'Stils',
		ok				: 'Darīts!',
		cancel			: 'Atcelt',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Izvērstais',
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
		width			: 'Platums',
		height			: 'Augstums',
		align			: 'Nolīdzināt',
		alignLeft		: 'Pa kreisi',
		alignRight		: 'Pa labi',
		alignCenter		: 'Centrēti',
		alignTop		: 'Augšā',
		alignMiddle		: 'Vertikāli centrēts',
		alignBottom		: 'Apakšā',
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
		toolbar		: 'Ievietot speciālo simbolu',
		title		: 'Ievietot īpašu simbolu',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Ievietot/Labot hipersaiti',
		other 		: '<cits>',
		menu		: 'Labot hipersaiti',
		title		: 'Hipersaite',
		info		: 'Hipersaites informācija',
		target		: 'Mērķis',
		upload		: 'Augšupielādēt',
		advanced	: 'Izvērstais',
		type		: 'Hipersaites tips',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Iezīme šajā lapā',
		toEmail		: 'E-pasts',
		targetFrame		: '<ietvars>',
		targetPopup		: '<uznirstošā logā>',
		targetFrameName	: 'Mērķa ietvara nosaukums',
		targetPopupName	: 'Uznirstošā loga nosaukums',
		popupFeatures	: 'Uznirstošā loga nosaukums īpašības',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Statusa josla',
		popupLocationBar: 'Atrašanās vietas josla',
		popupToolbar	: 'Rīku josla',
		popupMenuBar	: 'Izvēlnes josla',
		popupFullScreen	: 'Pilnā ekrānā (IE)',
		popupScrollBars	: 'Ritjoslas',
		popupDependent	: 'Atkarīgs (Netscape)',
		popupLeft		: 'Kreisā koordināte',
		popupTop		: 'Augšējā koordināte',
		id				: 'Id', // MISSING
		langDir			: 'Valodas lasīšanas virziens',
		langDirLTR		: 'No kreisās uz labo (LTR)',
		langDirRTL		: 'No labās uz kreiso (RTL)',
		acccessKey		: 'Pieejas kods',
		name			: 'Nosaukums',
		langCode			: 'Valodas lasīšanas virziens',
		tabIndex			: 'Ciļņu indekss',
		advisoryTitle		: 'Konsultatīvs virsraksts',
		advisoryContentType	: 'Konsultatīvs satura tips',
		cssClasses		: 'Stilu saraksta klases',
		charset			: 'Pievienotā resursa kodu tabula',
		styles			: 'Stils',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Izvēlēties iezīmi',
		anchorName		: 'Pēc iezīmes nosaukuma',
		anchorId			: 'Pēc elementa ID',
		emailAddress		: 'E-pasta adrese',
		emailSubject		: 'Ziņas tēma',
		emailBody		: 'Ziņas saturs',
		noAnchors		: '(Šajā dokumentā nav iezīmju)',
		noUrl			: 'Lūdzu norādi hipersaiti',
		noEmail			: 'Lūdzu norādi e-pasta adresi'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Ievietot/Labot iezīmi',
		menu		: 'Iezīmes īpašības',
		title		: 'Iezīmes īpašības',
		name		: 'Iezīmes nosaukums',
		errorName	: 'Lūdzu norādiet iezīmes nosaukumu',
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
		find				: 'Meklēt',
		replace				: 'Nomainīt',
		findWhat			: 'Meklēt:',
		replaceWith			: 'Nomainīt uz:',
		notFoundMsg			: 'Norādītā frāze netika atrasta.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Reģistrjūtīgs',
		matchWord			: 'Jāsakrīt pilnībā',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Aizvietot visu',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabula',
		title		: 'Tabulas īpašības',
		menu		: 'Tabulas īpašības',
		deleteTable	: 'Dzēst tabulu',
		rows		: 'Rindas',
		columns		: 'Kolonnas',
		border		: 'Rāmja izmērs',
		widthPx		: 'pikseļos',
		widthPc		: 'procentuāli',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Rūtiņu atstatums',
		cellPad		: 'Rūtiņu nobīde',
		caption		: 'Leģenda',
		summary		: 'Anotācija',
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
			menu			: 'Šūna',
			insertBefore	: 'Insert Cell Before', // MISSING
			insertAfter		: 'Insert Cell After', // MISSING
			deleteCell		: 'Dzēst rūtiņas',
			merge			: 'Apvienot rūtiņas',
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
			menu			: 'Rinda',
			insertBefore	: 'Insert Row Before', // MISSING
			insertAfter		: 'Insert Row After', // MISSING
			deleteRow		: 'Dzēst rindas'
		},

		column :
		{
			menu			: 'Kolonna',
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Dzēst kolonnas'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Pogas īpašības',
		text		: 'Teksts (vērtība)',
		type		: 'Tips',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Atzīmēšanas kastītes īpašības',
		radioTitle	: 'Izvēles poga īpašības',
		value		: 'Vērtība',
		selected	: 'Iezīmēts'
	},

	// Form Dialog.
	form :
	{
		title		: 'Formas īpašības',
		menu		: 'Formas īpašības',
		action		: 'Darbība',
		method		: 'Metode',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Iezīmēšanas lauka īpašības',
		selectInfo	: 'Informācija',
		opAvail		: 'Pieejamās iespējas',
		value		: 'Vērtība',
		size		: 'Izmērs',
		lines		: 'rindas',
		chkMulti	: 'Atļaut vairākus iezīmējumus',
		opText		: 'Teksts',
		opValue		: 'Vērtība',
		btnAdd		: 'Pievienot',
		btnModify	: 'Veikt izmaiņas',
		btnUp		: 'Augšup',
		btnDown		: 'Lejup',
		btnSetValue : 'Noteikt kā iezīmēto vērtību',
		btnDelete	: 'Dzēst'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Teksta laukuma īpašības',
		cols		: 'Kolonnas',
		rows		: 'Rindas'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Teksta rindas  īpašības',
		name		: 'Nosaukums',
		value		: 'Vērtība',
		charWidth	: 'Simbolu platums',
		maxChars	: 'Simbolu maksimālais daudzums',
		type		: 'Tips',
		typeText	: 'Teksts',
		typePass	: 'Parole'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Paslēptās teksta rindas īpašības',
		name	: 'Nosaukums',
		value	: 'Vērtība'
	},

	// Image Dialog.
	image :
	{
		title		: 'Attēla īpašības',
		titleButton	: 'Attēlpogas īpašības',
		menu		: 'Attēla īpašības',
		infoTab		: 'Informācija par attēlu',
		btnUpload	: 'Nosūtīt serverim',
		upload		: 'Augšupielādēt',
		alt			: 'Alternatīvais teksts',
		lockRatio	: 'Nemainīga Augstuma/Platuma attiecība',
		resetSize	: 'Atjaunot sākotnējo izmēru',
		border		: 'Rāmis',
		hSpace		: 'Horizontālā telpa',
		vSpace		: 'Vertikālā telpa',
		alertUrl	: 'Lūdzu norādīt attēla hipersaiti',
		linkTab		: 'Hipersaite',
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
		properties		: 'Flash īpašības',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Flash īpašības',
		chkPlay			: 'Automātiska atskaņošana',
		chkLoop			: 'Nepārtraukti',
		chkMenu			: 'Atļaut Flash izvēlni',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Mainīt izmēru',
		scaleAll		: 'Rādīt visu',
		scaleNoBorder	: 'Bez rāmja',
		scaleFit		: 'Precīzs izmērs',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Absolūti apakšā',
		alignAbsMiddle	: 'Absolūti vertikāli centrēts',
		alignBaseline	: 'Pamatrindā',
		alignTextTop	: 'Teksta augšā',
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
		bgcolor			: 'Fona krāsa',
		hSpace			: 'Horizontālā telpa',
		vSpace			: 'Vertikālā telpa',
		validateSrc		: 'Lūdzu norādi hipersaiti',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Pareizrakstības pārbaude',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Netika atrasts vārdnīcā',
		changeTo		: 'Nomainīt uz',
		btnIgnore		: 'Ignorēt',
		btnIgnoreAll	: 'Ignorēt visu',
		btnReplace		: 'Aizvietot',
		btnReplaceAll	: 'Aizvietot visu',
		btnUndo			: 'Atcelt',
		noSuggestions	: '- Nav ieteikumu -',
		progress		: 'Notiek pareizrakstības pārbaude...',
		noMispell		: 'Pareizrakstības pārbaude pabeigta: kļūdas netika atrastas',
		noChanges		: 'Pareizrakstības pārbaude pabeigta: nekas netika labots',
		oneChange		: 'Pareizrakstības pārbaude pabeigta: 1 vārds izmainīts',
		manyChanges		: 'Pareizrakstības pārbaude pabeigta: %1 vārdi tika mainīti',
		ieSpellDownload	: 'Pareizrakstības pārbaudītājs nav pievienots. Vai vēlaties to lejupielādēt tagad?'
	},

	smiley :
	{
		toolbar	: 'Smaidiņi',
		title	: 'Ievietot smaidiņu',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Numurēts saraksts',
	bulletedlist	: 'Izcelts saraksts',
	indent			: 'Palielināt atkāpi',
	outdent			: 'Samazināt atkāpi',

	justify :
	{
		left	: 'Izlīdzināt pa kreisi',
		center	: 'Izlīdzināt pret centru',
		right	: 'Izlīdzināt pa labi',
		block	: 'Izlīdzināt malas'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'Ievietot',
		cutError	: 'Jūsu pārlūkprogrammas drošības iestatījumi nepieļauj editoram automātiski veikt izgriešanas darbību.  Lūdzu, izmantojiet (Ctrl/Cmd+X, lai veiktu šo darbību.',
		copyError	: 'Jūsu pārlūkprogrammas drošības iestatījumi nepieļauj editoram automātiski veikt kopēšanas darbību.  Lūdzu, izmantojiet (Ctrl/Cmd+C), lai veiktu šo darbību.',
		pasteMsg	: 'Lūdzu, ievietojiet tekstu šajā laukumā, izmantojot klaviatūru (<STRONG>Ctrl/Cmd+V</STRONG>) un apstipriniet ar <STRONG>Darīts!</STRONG>.',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Ievietot no Worda',
		title			: 'Ievietot no Worda',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Ievietot kā vienkāršu tekstu',
		title	: 'Ievietot kā vienkāršu tekstu'
	},

	templates :
	{
		button			: 'Sagataves',
		title			: 'Satura sagataves',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Lūdzu, norādiet sagatavi, ko atvērt editorā<br>(patreizējie dati tiks zaudēti):',
		emptyListMsg	: '(Nav norādītas sagataves)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'Stils',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Formāts',
		panelTitle	: 'Formāts',

		tag_p		: 'Normāls teksts',
		tag_pre		: 'Formatēts teksts',
		tag_address	: 'Adrese',
		tag_h1		: 'Virsraksts 1',
		tag_h2		: 'Virsraksts 2',
		tag_h3		: 'Virsraksts 3',
		tag_h4		: 'Virsraksts 4',
		tag_h5		: 'Virsraksts 5',
		tag_h6		: 'Virsraksts 6',
		tag_div		: 'Rindkopa (DIV)'
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
		label		: 'Šrifts',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Šrifts'
	},

	fontSize :
	{
		label		: 'Izmērs',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Izmērs'
	},

	colorButton :
	{
		textColorTitle	: 'Teksta krāsa',
		bgColorTitle	: 'Fona krāsa',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automātiska',
		more			: 'Plašāka palete...'
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
		label : 'Dokumenta īpašības',
		title : 'Dokumenta īpašības',
		design : 'Design', // MISSING
		meta : 'META dati',
		chooseColor : 'Choose', // MISSING
		other : '<cits>',
		docTitle :	'Dokumenta virsraksts <Title>',
		charset : 	'Simbolu kodējums',
		charsetOther : 'Cits simbolu kodējums',
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
		docType : 'Dokumenta tips',
		docTypeOther : 'Cits dokumenta tips',
		xhtmlDec : 'Ietvert XHTML deklarācijas',
		bgColor : 'Fona krāsa',
		bgImage : 'Fona attēla hipersaite',
		bgFixed : 'Fona attēls ir fiksēts',
		txtColor : 'Teksta krāsa',
		margin : 'Lapas robežas',
		marginTop : 'Augšā',
		marginLeft : 'Pa kreisi',
		marginRight : 'Pa labi',
		marginBottom : 'Apakšā',
		metaKeywords : 'Dokumentu aprakstoši atslēgvārdi (atdalīti ar komatu)',
		metaDescription : 'Dokumenta apraksts',
		metaAuthor : 'Autors',
		metaCopyright : 'Autortiesības',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
