/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Slovak language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['sk'] =
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
	source			: 'Zdroj',
	newPage			: 'Nová stránka',
	save			: 'Uložiť',
	preview			: 'Náhľad',
	cut				: 'Vystrihnúť',
	copy			: 'Kopírovať',
	paste			: 'Vložiť',
	print			: 'Tlač',
	underline		: 'Podčiarknuté',
	bold			: 'Tučné',
	italic			: 'Kurzíva',
	selectAll		: 'Vybrať všetko',
	removeFormat	: 'Odstrániť formátovanie',
	strike			: 'Prečiarknuté',
	subscript		: 'Dolný index',
	superscript		: 'Horný index',
	horizontalrule	: 'Vložiť vodorovnú čiaru',
	pagebreak		: 'Vložiť oddeľovač stránky',
	pagebreakAlt		: 'Zalomenie strany',
	unlink			: 'Odstrániť odkaz',
	undo			: 'Späť',
	redo			: 'Znovu',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Prechádzať server',
		url				: 'URL',
		protocol		: 'Protokol',
		upload			: 'Odoslať',
		uploadSubmit	: 'Odoslať na server',
		image			: 'Obrázok',
		flash			: 'Flash',
		form			: 'Formulár',
		checkbox		: 'Zaškrtávacie políčko',
		radio			: 'Prepínač',
		textField		: 'Textové pole',
		textarea		: 'Textová oblasť',
		hiddenField		: 'Skryté pole',
		button			: 'Tlačidlo',
		select			: 'Rozbaľovací zoznam',
		imageButton		: 'Obrázkové tlačidlo',
		notSet			: '<nenastavené>',
		id				: 'Id',
		name			: 'Meno',
		langDir			: 'Orientácia jazyka',
		langDirLtr		: 'Zľava doprava (LTR)',
		langDirRtl		: 'Sprava doľava (RTL)',
		langCode		: 'Kód jazyka',
		longDescr		: 'Dlhý popis URL',
		cssClass		: 'Trieda štýlu',
		advisoryTitle	: 'Pomocný titulok',
		cssStyle		: 'Štýl',
		ok				: 'OK',
		cancel			: 'Zrušiť',
		close			: 'Zatvorit',
		preview			: 'Náhľad',
		generalTab		: 'Hlavné',
		advancedTab		: 'Rozšírené',
		validateNumberFailed : 'Hodnota nieje číslo.',
		confirmNewPage	: 'Prajete si načítat novú stránku? Všetky neuložené zmeny budú stratené. ',
		confirmCancel	: 'Niektore možnosti boli zmenené. Naozaj chcete zavrieť okno?',
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
		width			: 'Šírka',
		height			: 'Výška',
		align			: 'Zarovnanie',
		alignLeft		: 'Vľavo',
		alignRight		: 'Vpravo',
		alignCenter		: 'Na stred',
		alignTop		: 'Nahor',
		alignMiddle		: 'Na stred',
		alignBottom		: 'Dole',
		invalidHeight	: 'Výška musí byť číslo.',
		invalidWidth	: 'Šírka musí byť číslo.',
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
		toolbar		: 'Vložiť špeciálne znaky',
		title		: 'Výber špeciálneho znaku',
		options : 'Možnosti špecíalneho znaku'
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Vložiť/zmeniť odkaz',
		other 		: '<iný>',
		menu		: 'Zmeniť odkaz',
		title		: 'Odkaz',
		info		: 'Informácie o odkaze',
		target		: 'Cieľ',
		upload		: 'Odoslať',
		advanced	: 'Rozšírené',
		type		: 'Typ odkazu',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Kotva v tejto stránke',
		toEmail		: 'E-Mail',
		targetFrame		: '<rámec>',
		targetPopup		: '<vyskakovacie okno>',
		targetFrameName	: 'Meno rámu cieľa',
		targetPopupName	: 'Názov vyskakovacieho okna',
		popupFeatures	: 'Vlastnosti vyskakovacieho okna',
		popupResizable	: 'Meniteľná veľkosť',
		popupStatusBar	: 'Stavový riadok',
		popupLocationBar: 'Panel umiestnenia',
		popupToolbar	: 'Panel nástrojov',
		popupMenuBar	: 'Panel ponuky',
		popupFullScreen	: 'Celá obrazovka (IE)',
		popupScrollBars	: 'Posuvníky',
		popupDependent	: 'Závislosť (Netscape)',
		popupLeft		: 'Ľavý okraj',
		popupTop		: 'Horný okraj',
		id				: 'Id', // MISSING
		langDir			: 'Orientácia jazyka',
		langDirLTR		: 'Zľava doprava (LTR)',
		langDirRTL		: 'Sprava doľava (RTL)',
		acccessKey		: 'Prístupový kľúč',
		name			: 'Meno',
		langCode			: 'Orientácia jazyka',
		tabIndex			: 'Poradie prvku',
		advisoryTitle		: 'Pomocný titulok',
		advisoryContentType	: 'Pomocný typ obsahu',
		cssClasses		: 'Trieda štýlu',
		charset			: 'Priradená znaková sada',
		styles			: 'Štýl',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Vybrať kotvu',
		anchorName		: 'Podľa mena kotvy',
		anchorId			: 'Podľa Id objektu',
		emailAddress		: 'E-Mailová adresa',
		emailSubject		: 'Predmet správy',
		emailBody		: 'Telo správy',
		noAnchors		: '(V stránke nie je definovaná žiadna kotva)',
		noUrl			: 'Zadajte prosím URL odkazu',
		noEmail			: 'Zadajte prosím e-mailovú adresu'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Vložiť/zmeniť kotvu',
		menu		: 'Vlastnosti kotvy',
		title		: 'Vlastnosti kotvy',
		name		: 'Meno kotvy',
		errorName	: 'Zadajte prosím meno kotvy',
		remove		: 'Remove Anchor' // MISSING
	},

	// List style dialog
	list:
	{
		numberedTitle		: 'Vlastnosti číselného zoznamu',
		bulletedTitle		: 'Bulleted List Properties', // MISSING
		type				: 'Druh',
		start				: 'Začiatok',
		validateStartNumber				:'Začiatočné číslo číselného zoznamu musí byť celé číslo.',
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
		title				: 'Nájsť a nahradiť',
		find				: 'Hľadať',
		replace				: 'Nahradiť',
		findWhat			: 'Čo hľadať:',
		replaceWith			: 'Čím nahradiť:',
		notFoundMsg			: 'Hľadaný text nebol nájdený.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Rozlišovať malé/veľké písmená',
		matchWord			: 'Len celé slová',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Nahradiť všetko',
		replaceSuccessMsg	: '%1 výskyt(ov) nahradených.'
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabuľka',
		title		: 'Vlastnosti tabuľky',
		menu		: 'Vlastnosti tabuľky',
		deleteTable	: 'Vymazať tabuľku',
		rows		: 'Riadky',
		columns		: 'Stĺpce',
		border		: 'Ohraničenie',
		widthPx		: 'pixelov',
		widthPc		: 'percent',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Vzdialenosť buniek',
		cellPad		: 'Odsadenie obsahu',
		caption		: 'Popis',
		summary		: 'Prehľad',
		headers		: 'Hlavička',
		headersNone		: 'Žiadne',
		headersColumn	: 'Prvý stĺpec',
		headersRow		: 'Prvý riadok',
		headersBoth		: 'Obe',
		invalidRows		: 'Počet riadkov musí byť číslo väčšie ako 0.',
		invalidCols		: 'Počet stĺpcov musí byť číslo väčšie ako 0.',
		invalidBorder	: 'Širka rámu musí byť celé číslo.',
		invalidWidth	: 'Širka tabuľky musí byť číslo.',
		invalidHeight	: 'Výška tabuľky musí byť číslo.',
		invalidCellSpacing	: 'Medzera mädzi bunkami (spacing) musí byť číslo.',
		invalidCellPadding	: 'Odsadenie v bunkách (padding) musí byť číslo.',

		cell :
		{
			menu			: 'Bunka',
			insertBefore	: 'Vložiť bunku pred',
			insertAfter		: 'Vložiť bunku za',
			deleteCell		: 'Vymazať bunky',
			merge			: 'Zlúčiť bunky',
			mergeRight		: 'Zlúčiť doprava',
			mergeDown		: 'Zlúčiť dole',
			splitHorizontal	: 'Rozdeliť bunky horizontálne',
			splitVertical	: 'Rozdeliť bunky vertikálne',
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
			menu			: 'Riadok',
			insertBefore	: 'Vložiť riadok za',
			insertAfter		: 'Vložiť riadok pred',
			deleteRow		: 'Vymazať riadok'
		},

		column :
		{
			menu			: 'Stĺpec',
			insertBefore	: 'Vložiť stĺpec za',
			insertAfter		: 'Vložiť stĺpec pred',
			deleteColumn	: 'Zmazať stĺpec'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Vlastnosti tlačidla',
		text		: 'Text',
		type		: 'Typ',
		typeBtn		: 'Tlačidlo',
		typeSbm		: 'Odoslať',
		typeRst		: 'Vymazať'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Vlastnosti zaškrtávacieho políčka',
		radioTitle	: 'Vlastnosti prepínača',
		value		: 'Hodnota',
		selected	: 'Vybrané'
	},

	// Form Dialog.
	form :
	{
		title		: 'Vlastnosti formulára',
		menu		: 'Vlastnosti formulára',
		action		: 'Akcie',
		method		: 'Metóda',
		encoding	: 'Kódovanie'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Vlastnosti rozbaľovacieho zoznamu',
		selectInfo	: 'Info',
		opAvail		: 'Dostupné možnosti',
		value		: 'Hodnota',
		size		: 'Veľkosť',
		lines		: 'riadkov',
		chkMulti	: 'Povoliť viacnásobný výber',
		opText		: 'Text',
		opValue		: 'Hodnota',
		btnAdd		: 'Pridať',
		btnModify	: 'Zmeniť',
		btnUp		: 'Hore',
		btnDown		: 'Dole',
		btnSetValue : 'Nastaviť ako vybranú hodnotu',
		btnDelete	: 'Zmazať'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Vlastnosti textovej oblasti',
		cols		: 'Stĺpce',
		rows		: 'Riadky'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Vlastnosti textového poľa',
		name		: 'Názov',
		value		: 'Hodnota',
		charWidth	: 'Šírka pola (znakov)',
		maxChars	: 'Maximálny počet znakov',
		type		: 'Typ',
		typeText	: 'Text',
		typePass	: 'Heslo'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Vlastnosti skrytého poľa',
		name	: 'Názov',
		value	: 'Hodnota'
	},

	// Image Dialog.
	image :
	{
		title		: 'Vlastnosti obrázku',
		titleButton	: 'Vlastnosti obrázkového tlačidla',
		menu		: 'Vlastnosti obrázku',
		infoTab		: 'Informácie o obrázku',
		btnUpload	: 'Odoslať na server',
		upload		: 'Odoslať',
		alt			: 'Alternatívny text',
		lockRatio	: 'Zámok',
		resetSize	: 'Pôvodná veľkosť',
		border		: 'Okraje',
		hSpace		: 'H-medzera',
		vSpace		: 'V-medzera',
		alertUrl	: 'Zadajte prosím URL obrázku',
		linkTab		: 'Odkaz',
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
		properties		: 'Vlastnosti Flashu',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Vlastnosti Flashu',
		chkPlay			: 'Automatické prehrávanie',
		chkLoop			: 'Opakovanie',
		chkMenu			: 'Povoliť Flash Menu',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Mierka',
		scaleAll		: 'Zobraziť mierku',
		scaleNoBorder	: 'Bez okrajov',
		scaleFit		: 'Roztiahnuť na celé',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Úplne dole',
		alignAbsMiddle	: 'Do stredu',
		alignBaseline	: 'Na základňu',
		alignTextTop	: 'Na horný okraj textu',
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
		bgcolor			: 'Farba pozadia',
		hSpace			: 'H-medzera',
		vSpace			: 'V-medzera',
		validateSrc		: 'Zadajte prosím URL odkazu',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Kontrola pravopisu',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Služba práve nieje dostupná.',
		errorLoading	: 'Chyba pri načítaní slovníka z adresy: %s.',
		notInDic		: 'Nie je v slovníku',
		changeTo		: 'Zmeniť na',
		btnIgnore		: 'Ignorovať',
		btnIgnoreAll	: 'Ignorovať všetko',
		btnReplace		: 'Prepísat',
		btnReplaceAll	: 'Prepísat všetko',
		btnUndo			: 'Späť',
		noSuggestions	: '- Žiadny návrh -',
		progress		: 'Prebieha kontrola pravopisu...',
		noMispell		: 'Kontrola pravopisu dokončená: bez chýb',
		noChanges		: 'Kontrola pravopisu dokončená: žiadne slová nezmenené',
		oneChange		: 'Kontrola pravopisu dokončená: zmenené jedno slovo',
		manyChanges		: 'Kontrola pravopisu dokončená: zmenených %1 slov',
		ieSpellDownload	: 'Kontrola pravopisu nie je naištalovaná. Chcete ju hneď stiahnuť?'
	},

	smiley :
	{
		toolbar	: 'Smajlíky',
		title	: 'Vkladanie smajlíkov',
		options : 'Možnosti smajlíkov'
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Číslovanie',
	bulletedlist	: 'Odrážky',
	indent			: 'Zväčšiť odsadenie',
	outdent			: 'Zmenšiť odsadenie',

	justify :
	{
		left	: 'Zarovnať vľavo',
		center	: 'Zarovnať na stred',
		right	: 'Zarovnať vpravo',
		block	: 'Zarovnať do bloku'
	},

	blockquote : 'Citácia',

	clipboard :
	{
		title		: 'Vložiť',
		cutError	: 'Bezpečnostné nastavenia Vášho prehliadača nedovoľujú editoru spustiť funkciu pre vystrihnutie zvoleného textu do schránky. Prosím vystrihnite zvolený text do schránky pomocou klávesnice (Ctrl/Cmd+X).',
		copyError	: 'Bezpečnostné nastavenia Vášho prehliadača nedovoľujú editoru spustiť funkciu pre kopírovanie zvoleného textu do schránky. Prosím skopírujte zvolený text do schránky pomocou klávesnice (Ctrl/Cmd+C).',
		pasteMsg	: 'Prosím vložte nasledovný rámček použitím klávesnice (<STRONG>Ctrl/Cmd+V</STRONG>) a stlačte <STRONG>OK</STRONG>.',
		securityMsg	: 'Bezpečnostné nastavenia Vášho prehliadača nedovoľujú editoru pristupovať priamo k datám v schránke. Musíte ich vložiť znovu do tohto okna.',
		pasteArea	: 'Vložiť pole'
	},

	pastefromword :
	{
		confirmCleanup	: 'Vkladaný text vyzerá byť skopírovaný z Wordu. Chcete ho automaticky vyčistiť pred vkladaním?',
		toolbar			: 'Vložiť z Wordu',
		title			: 'Vložiť z Wordu',
		error			: 'Nastala chyba pri čistení údajov. Nie je možné vyčistiť vložené údaje.'
	},

	pasteText :
	{
		button	: 'Vložiť ako čistý text',
		title	: 'Vložiť ako čistý text'
	},

	templates :
	{
		button			: 'Šablóny',
		title			: 'Šablóny obsahu',
		options : 'Vlastnosti šablóny',
		insertOption	: 'Nahradiť aktuálny obsah',
		selectPromptMsg	: 'Prosím vyberte šablóny na otvorenie v editore<br>(súšasný obsah bude stratený):',
		emptyListMsg	: '(žiadne šablóny nenájdené)'
	},

	showBlocks : 'Ukázať bloky',

	stylesCombo :
	{
		label		: 'Štýl',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Formát',
		panelTitle	: 'Formát',

		tag_p		: 'Normálny',
		tag_pre		: 'Formátovaný',
		tag_address	: 'Adresa',
		tag_h1		: 'Nadpis 1',
		tag_h2		: 'Nadpis 2',
		tag_h3		: 'Nadpis 3',
		tag_h4		: 'Nadpis 4',
		tag_h5		: 'Nadpis 5',
		tag_h6		: 'Nadpis 6',
		tag_div		: 'Odsek (DIV)'
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
		title		: 'IFrame - vlastnosti',
		toolbar		: 'IFrame', // MISSING
		noUrl		: 'Vložte URL pre iframe',
		scrolling	: 'Povoliť skrolovanie',
		border		: 'Zobraziť orámovanie'
	},

	font :
	{
		label		: 'Písmo',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Písmo'
	},

	fontSize :
	{
		label		: 'Veľkosť',
		voiceLabel	: 'Veľkosť písma',
		panelTitle	: 'Veľkosť'
	},

	colorButton :
	{
		textColorTitle	: 'Farba textu',
		bgColorTitle	: 'Farba pozadia',
		panelTitle		: 'Farby',
		auto			: 'Automaticky',
		more			: 'Viac farieb...'
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

	maximize : 'Maximalizovať',
	minimize : 'Minimalizovať',

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
		label : 'Vlastnosti dokumentu',
		title : 'Vlastnosti dokumentu',
		design : 'Design', // MISSING
		meta : 'Meta Data',
		chooseColor : 'Choose', // MISSING
		other : '<iný>',
		docTitle :	'Titulok',
		charset : 	'Kódová stránka',
		charsetOther : 'Iná kódová stránka',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Stredoeurópske',
		charsetCT : 'Čínština tradičná (Big5)',
		charsetCR : 'Cyrillika',
		charsetGR : 'Gréčtina',
		charsetJP : 'Japončina',
		charsetKR : 'Korejčina',
		charsetTR : 'Turečtina',
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Západná európa',
		docType : 'Typ záhlavia dokumentu',
		docTypeOther : 'Iný typ záhlavia dokumentu',
		xhtmlDec : 'Obsahuje deklarácie XHTML',
		bgColor : 'Farba pozadia',
		bgImage : 'URL adresa obrázku na pozadí',
		bgFixed : 'Fixné pozadie',
		txtColor : 'Farba textu',
		margin : 'Okraje stránky',
		marginTop : 'Horný',
		marginLeft : 'Ľavý',
		marginRight : 'Pravý',
		marginBottom : 'Dolný',
		metaKeywords : 'Kľúčové slová pre indexovanie (oddelené čiarkou)',
		metaDescription : 'Popis stránky',
		metaAuthor : 'Autor',
		metaCopyright : 'Autorské práva',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
