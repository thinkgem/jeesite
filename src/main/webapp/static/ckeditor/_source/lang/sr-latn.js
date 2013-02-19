/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Serbian (Latin) language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['sr-latn'] =
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
	source			: 'Kôd',
	newPage			: 'Nova stranica',
	save			: 'Sačuvaj',
	preview			: 'Izgled stranice',
	cut				: 'Iseci',
	copy			: 'Kopiraj',
	paste			: 'Zalepi',
	print			: 'Štampa',
	underline		: 'Podvučeno',
	bold			: 'Podebljano',
	italic			: 'Kurziv',
	selectAll		: 'Označi sve',
	removeFormat	: 'Ukloni formatiranje',
	strike			: 'Precrtano',
	subscript		: 'Indeks',
	superscript		: 'Stepen',
	horizontalrule	: 'Unesi horizontalnu liniju',
	pagebreak		: 'Insert Page Break for Printing', // MISSING
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Ukloni link',
	undo			: 'Poni�ti akciju',
	redo			: 'Ponovi akciju',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Pretraži server',
		url				: 'URL',
		protocol		: 'Protokol',
		upload			: 'Pošalji',
		uploadSubmit	: 'Pošalji na server',
		image			: 'Slika',
		flash			: 'Fleš',
		form			: 'Forma',
		checkbox		: 'Polje za potvrdu',
		radio			: 'Radio-dugme',
		textField		: 'Tekstualno polje',
		textarea		: 'Zona teksta',
		hiddenField		: 'Skriveno polje',
		button			: 'Dugme',
		select			: 'Izborno polje',
		imageButton		: 'Dugme sa slikom',
		notSet			: '<nije postavljeno>',
		id				: 'Id',
		name			: 'Naziv',
		langDir			: 'Smer jezika',
		langDirLtr		: 'S leva na desno (LTR)',
		langDirRtl		: 'S desna na levo (RTL)',
		langCode		: 'Kôd jezika',
		longDescr		: 'Pun opis URL',
		cssClass		: 'Stylesheet klase',
		advisoryTitle	: 'Advisory naslov',
		cssStyle		: 'Stil',
		ok				: 'OK',
		cancel			: 'Otkaži',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Napredni tagovi',
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
		width			: 'Širina',
		height			: 'Visina',
		align			: 'Ravnanje',
		alignLeft		: 'Levo',
		alignRight		: 'Desno',
		alignCenter		: 'Sredina',
		alignTop		: 'Vrh',
		alignMiddle		: 'Sredina',
		alignBottom		: 'Dole',
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
		toolbar		: 'Unesi specijalni karakter',
		title		: 'Odaberite specijalni karakter',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Unesi/izmeni link',
		other 		: '<остало>',
		menu		: 'Izmeni link',
		title		: 'Link',
		info		: 'Link Info',
		target		: 'Meta',
		upload		: 'Pošalji',
		advanced	: 'Napredni tagovi',
		type		: 'Vrsta linka',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Sidro na ovoj stranici',
		toEmail		: 'E-Mail',
		targetFrame		: '<okvir>',
		targetPopup		: '<popup prozor>',
		targetFrameName	: 'Naziv odredišnog frejma',
		targetPopupName	: 'Naziv popup prozora',
		popupFeatures	: 'Mogućnosti popup prozora',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Statusna linija',
		popupLocationBar: 'Lokacija',
		popupToolbar	: 'Toolbar',
		popupMenuBar	: 'Kontekstni meni',
		popupFullScreen	: 'Prikaz preko celog ekrana (IE)',
		popupScrollBars	: 'Scroll bar',
		popupDependent	: 'Zavisno (Netscape)',
		popupLeft		: 'Od leve ivice ekrana (px)',
		popupTop		: 'Od vrha ekrana (px)',
		id				: 'Id', // MISSING
		langDir			: 'Smer jezika',
		langDirLTR		: 'S leva na desno (LTR)',
		langDirRTL		: 'S desna na levo (RTL)',
		acccessKey		: 'Pristupni taster',
		name			: 'Naziv',
		langCode			: 'Smer jezika',
		tabIndex			: 'Tab indeks',
		advisoryTitle		: 'Advisory naslov',
		advisoryContentType	: 'Advisory vrsta sadržaja',
		cssClasses		: 'Stylesheet klase',
		charset			: 'Linked Resource Charset',
		styles			: 'Stil',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Odaberi sidro',
		anchorName		: 'Po nazivu sidra',
		anchorId			: 'Po Id-ju elementa',
		emailAddress		: 'E-Mail adresa',
		emailSubject		: 'Naslov',
		emailBody		: 'Sadržaj poruke',
		noAnchors		: '(Nema dostupnih sidra)',
		noUrl			: 'Unesite URL linka',
		noEmail			: 'Otkucajte adresu elektronske pote'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Unesi/izmeni sidro',
		menu		: 'Osobine sidra',
		title		: 'Osobine sidra',
		name		: 'Ime sidra',
		errorName	: 'Unesite ime sidra',
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
		find				: 'Pretraga',
		replace				: 'Zamena',
		findWhat			: 'Pronadi:',
		replaceWith			: 'Zameni sa:',
		notFoundMsg			: 'Traženi tekst nije pronađen.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Razlikuj mala i velika slova',
		matchWord			: 'Uporedi cele reci',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Zameni sve',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabela',
		title		: 'Osobine tabele',
		menu		: 'Osobine tabele',
		deleteTable	: 'Delete Table', // MISSING
		rows		: 'Redova',
		columns		: 'Kolona',
		border		: 'Veličina okvira',
		widthPx		: 'piksela',
		widthPc		: 'procenata',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Ćelijski prostor',
		cellPad		: 'Razmak ćelija',
		caption		: 'Naslov tabele',
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
			deleteCell		: 'Obriši ćelije',
			merge			: 'Spoj celije',
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
			deleteRow		: 'Obriši redove'
		},

		column :
		{
			menu			: 'Column', // MISSING
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Obriši kolone'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Osobine dugmeta',
		text		: 'Tekst (vrednost)',
		type		: 'Tip',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Osobine polja za potvrdu',
		radioTitle	: 'Osobine radio-dugmeta',
		value		: 'Vrednost',
		selected	: 'Označeno'
	},

	// Form Dialog.
	form :
	{
		title		: 'Osobine forme',
		menu		: 'Osobine forme',
		action		: 'Akcija',
		method		: 'Metoda',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Osobine izbornog polja',
		selectInfo	: 'Info',
		opAvail		: 'Dostupne opcije',
		value		: 'Vrednost',
		size		: 'Veličina',
		lines		: 'linija',
		chkMulti	: 'Dozvoli višestruku selekciju',
		opText		: 'Tekst',
		opValue		: 'Vrednost',
		btnAdd		: 'Dodaj',
		btnModify	: 'Izmeni',
		btnUp		: 'Gore',
		btnDown		: 'Dole',
		btnSetValue : 'Podesi kao označenu vrednost',
		btnDelete	: 'Obriši'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Osobine zone teksta',
		cols		: 'Broj kolona',
		rows		: 'Broj redova'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Osobine tekstualnog polja',
		name		: 'Naziv',
		value		: 'Vrednost',
		charWidth	: 'Širina (karaktera)',
		maxChars	: 'Maksimalno karaktera',
		type		: 'Tip',
		typeText	: 'Tekst',
		typePass	: 'Lozinka'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Osobine skrivenog polja',
		name	: 'Naziv',
		value	: 'Vrednost'
	},

	// Image Dialog.
	image :
	{
		title		: 'Osobine slika',
		titleButton	: 'Osobine dugmeta sa slikom',
		menu		: 'Osobine slika',
		infoTab		: 'Info slike',
		btnUpload	: 'Pošalji na server',
		upload		: 'Pošalji',
		alt			: 'Alternativni tekst',
		lockRatio	: 'Zaključaj odnos',
		resetSize	: 'Resetuj veličinu',
		border		: 'Okvir',
		hSpace		: 'HSpace',
		vSpace		: 'VSpace',
		alertUrl	: 'Unesite URL slike',
		linkTab		: 'Link',
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
		properties		: 'Osobine fleša',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Osobine fleša',
		chkPlay			: 'Automatski start',
		chkLoop			: 'Ponavljaj',
		chkMenu			: 'Uključi fleš meni',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Skaliraj',
		scaleAll		: 'Prikaži sve',
		scaleNoBorder	: 'Bez ivice',
		scaleFit		: 'Popuni površinu',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs dole',
		alignAbsMiddle	: 'Abs sredina',
		alignBaseline	: 'Bazno',
		alignTextTop	: 'Vrh teksta',
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
		bgcolor			: 'Boja pozadine',
		hSpace			: 'HSpace',
		vSpace			: 'VSpace',
		validateSrc		: 'Unesite URL linka',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Proveri spelovanje',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Nije u rečniku',
		changeTo		: 'Izmeni',
		btnIgnore		: 'Ignoriši',
		btnIgnoreAll	: 'Ignoriši sve',
		btnReplace		: 'Zameni',
		btnReplaceAll	: 'Zameni sve',
		btnUndo			: 'Vrati akciju',
		noSuggestions	: '- Bez sugestija -',
		progress		: 'Provera spelovanja u toku...',
		noMispell		: 'Provera spelovanja završena: greške nisu pronadene',
		noChanges		: 'Provera spelovanja završena: Nije izmenjena nijedna rec',
		oneChange		: 'Provera spelovanja završena: Izmenjena je jedna reč',
		manyChanges		: 'Provera spelovanja završena: %1 reč(i) je izmenjeno',
		ieSpellDownload	: 'Provera spelovanja nije instalirana. Da li želite da je skinete sa Interneta?'
	},

	smiley :
	{
		toolbar	: 'Smajli',
		title	: 'Unesi smajlija',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Nabrojiva lista',
	bulletedlist	: 'Nenabrojiva lista',
	indent			: 'Uvećaj levu marginu',
	outdent			: 'Smanji levu marginu',

	justify :
	{
		left	: 'Levo ravnanje',
		center	: 'Centriran tekst',
		right	: 'Desno ravnanje',
		block	: 'Obostrano ravnanje'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'Zalepi',
		cutError	: 'Sigurnosna podešavanja Vašeg pretraživača ne dozvoljavaju operacije automatskog isecanja teksta. Molimo Vas da koristite prečicu sa tastature (Ctrl/Cmd+X).',
		copyError	: 'Sigurnosna podešavanja Vašeg pretraživača ne dozvoljavaju operacije automatskog kopiranja teksta. Molimo Vas da koristite prečicu sa tastature (Ctrl/Cmd+C).',
		pasteMsg	: 'Molimo Vas da zalepite unutar donje povrine koristeći tastaturnu prečicu (<STRONG>Ctrl/Cmd+V</STRONG>) i da pritisnete <STRONG>OK</STRONG>.',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Zalepi iz Worda',
		title			: 'Zalepi iz Worda',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Zalepi kao čist tekst',
		title	: 'Zalepi kao čist tekst'
	},

	templates :
	{
		button			: 'Obrasci',
		title			: 'Obrasci za sadržaj',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Molimo Vas da odaberete obrazac koji ce biti primenjen na stranicu (trenutni sadržaj ce biti obrisan):',
		emptyListMsg	: '(Nema definisanih obrazaca)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'Stil',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Format',
		panelTitle	: 'Format',

		tag_p		: 'Normal',
		tag_pre		: 'Formatirano',
		tag_address	: 'Adresa',
		tag_h1		: 'Naslov 1',
		tag_h2		: 'Naslov 2',
		tag_h3		: 'Naslov 3',
		tag_h4		: 'Naslov 4',
		tag_h5		: 'Naslov 5',
		tag_h6		: 'Naslov 6',
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
		label		: 'Font',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Font'
	},

	fontSize :
	{
		label		: 'Veličina fonta',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Veličina fonta'
	},

	colorButton :
	{
		textColorTitle	: 'Boja teksta',
		bgColorTitle	: 'Boja pozadine',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automatski',
		more			: 'Više boja...'
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
		label : 'Osobine dokumenta',
		title : 'Osobine dokumenta',
		design : 'Design', // MISSING
		meta : 'Metapodaci',
		chooseColor : 'Choose', // MISSING
		other : '<остало>',
		docTitle :	'Naslov stranice',
		charset : 	'Kodiranje skupa karaktera',
		charsetOther : 'Ostala kodiranja skupa karaktera',
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
		docType : 'Zaglavlje tipa dokumenta',
		docTypeOther : 'Ostala zaglavlja tipa dokumenta',
		xhtmlDec : 'Ukljuci XHTML deklaracije',
		bgColor : 'Boja pozadine',
		bgImage : 'URL pozadinske slike',
		bgFixed : 'Fiksirana pozadina',
		txtColor : 'Boja teksta',
		margin : 'Margine stranice',
		marginTop : 'Gornja',
		marginLeft : 'Leva',
		marginRight : 'Desna',
		marginBottom : 'Donja',
		metaKeywords : 'Ključne reci za indeksiranje dokumenta (razdvojene zarezima)',
		metaDescription : 'Opis dokumenta',
		metaAuthor : 'Autor',
		metaCopyright : 'Autorska prava',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
