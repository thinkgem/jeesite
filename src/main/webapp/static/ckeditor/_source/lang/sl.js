/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Slovenian language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['sl'] =
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
	source			: 'Izvorna koda',
	newPage			: 'Nova stran',
	save			: 'Shrani',
	preview			: 'Predogled',
	cut				: 'Izreži',
	copy			: 'Kopiraj',
	paste			: 'Prilepi',
	print			: 'Natisni',
	underline		: 'Podčrtano',
	bold			: 'Krepko',
	italic			: 'Ležeče',
	selectAll		: 'Izberi vse',
	removeFormat	: 'Odstrani oblikovanje',
	strike			: 'Prečrtano',
	subscript		: 'Podpisano',
	superscript		: 'Nadpisano',
	horizontalrule	: 'Vstavi vodoravno črto',
	pagebreak		: 'Vstavi prelom strani',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Odstrani povezavo',
	undo			: 'Razveljavi',
	redo			: 'Ponovi',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Prebrskaj na strežniku',
		url				: 'URL',
		protocol		: 'Protokol',
		upload			: 'Prenesi',
		uploadSubmit	: 'Pošlji na strežnik',
		image			: 'Slika',
		flash			: 'Flash',
		form			: 'Obrazec',
		checkbox		: 'Potrditveno polje',
		radio			: 'Izbirno polje',
		textField		: 'Vnosno polje',
		textarea		: 'Vnosno območje',
		hiddenField		: 'Skrito polje',
		button			: 'Gumb',
		select			: 'Spustni seznam',
		imageButton		: 'Gumb s sliko',
		notSet			: '<ni postavljen>',
		id				: 'Id',
		name			: 'Ime',
		langDir			: 'Smer jezika',
		langDirLtr		: 'Od leve proti desni (LTR)',
		langDirRtl		: 'Od desne proti levi (RTL)',
		langCode		: 'Oznaka jezika',
		longDescr		: 'Dolg opis URL-ja',
		cssClass		: 'Razred stilne predloge',
		advisoryTitle	: 'Predlagani naslov',
		cssStyle		: 'Slog',
		ok				: 'V redu',
		cancel			: 'Prekliči',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'Splošno',
		advancedTab		: 'Napredno',
		validateNumberFailed : 'Ta vrednost ni število.',
		confirmNewPage	: 'Vse neshranjene spremembe te vsebine bodo izgubljene. Ali gotovo želiš naložiti novo stran?',
		confirmCancel	: 'Nekaj možnosti je bilo spremenjenih. Ali gotovo želiš zapreti okno?',
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
		height			: 'Višina',
		align			: 'Poravnava',
		alignLeft		: 'Levo',
		alignRight		: 'Desno',
		alignCenter		: 'Sredinsko',
		alignTop		: 'Na vrh',
		alignMiddle		: 'V sredino',
		alignBottom		: 'Na dno',
		invalidHeight	: 'Višina mora biti število.',
		invalidWidth	: 'Širina mora biti število.',
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedosegljiv</span>'
	},

	contextmenu :
	{
		options : 'Context Menu Options' // MISSING
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Vstavi posebni znak',
		title		: 'Izberi posebni znak',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Vstavi/uredi povezavo',
		other 		: '<drug>',
		menu		: 'Uredi povezavo',
		title		: 'Povezava',
		info		: 'Podatki o povezavi',
		target		: 'Cilj',
		upload		: 'Prenesi',
		advanced	: 'Napredno',
		type		: 'Vrsta povezave',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Zaznamek na tej strani',
		toEmail		: 'Elektronski naslov',
		targetFrame		: '<okvir>',
		targetPopup		: '<pojavno okno>',
		targetFrameName	: 'Ime ciljnega okvirja',
		targetPopupName	: 'Ime pojavnega okna',
		popupFeatures	: 'Značilnosti pojavnega okna',
		popupResizable	: 'Spremenljive velikosti',
		popupStatusBar	: 'Vrstica stanja',
		popupLocationBar: 'Naslovna vrstica',
		popupToolbar	: 'Orodna vrstica',
		popupMenuBar	: 'Menijska vrstica',
		popupFullScreen	: 'Celozaslonska slika (IE)',
		popupScrollBars	: 'Drsniki',
		popupDependent	: 'Podokno (Netscape)',
		popupLeft		: 'Lega levo',
		popupTop		: 'Lega na vrhu',
		id				: 'Id',
		langDir			: 'Smer jezika',
		langDirLTR		: 'Od leve proti desni (LTR)',
		langDirRTL		: 'Od desne proti levi (RTL)',
		acccessKey		: 'Vstopno geslo',
		name			: 'Ime',
		langCode			: 'Smer jezika',
		tabIndex			: 'Številka tabulatorja',
		advisoryTitle		: 'Predlagani naslov',
		advisoryContentType	: 'Predlagani tip vsebine (content-type)',
		cssClasses		: 'Razred stilne predloge',
		charset			: 'Kodna tabela povezanega vira',
		styles			: 'Slog',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Izberi zaznamek',
		anchorName		: 'Po imenu zaznamka',
		anchorId			: 'Po ID-ju elementa',
		emailAddress		: 'Elektronski naslov',
		emailSubject		: 'Predmet sporočila',
		emailBody		: 'Vsebina sporočila',
		noAnchors		: '(V tem dokumentu ni zaznamkov)',
		noUrl			: 'Vnesite URL povezave',
		noEmail			: 'Vnesite elektronski naslov'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Vstavi/uredi zaznamek',
		menu		: 'Lastnosti zaznamka',
		title		: 'Lastnosti zaznamka',
		name		: 'Ime zaznamka',
		errorName	: 'Prosim vnesite ime zaznamka',
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
		title				: 'Najdi in zamenjaj',
		find				: 'Najdi',
		replace				: 'Zamenjaj',
		findWhat			: 'Najdi:',
		replaceWith			: 'Zamenjaj z:',
		notFoundMsg			: 'Navedeno besedilo ni bilo najdeno.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Razlikuj velike in male črke',
		matchWord			: 'Samo cele besede',
		matchCyclic			: 'Primerjaj znake v cirilici',
		replaceAll			: 'Zamenjaj vse',
		replaceSuccessMsg	: '%1 pojavitev je bilo zamenjano.'
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabela',
		title		: 'Lastnosti tabele',
		menu		: 'Lastnosti tabele',
		deleteTable	: 'Izbriši tabelo',
		rows		: 'Vrstice',
		columns		: 'Stolpci',
		border		: 'Velikost obrobe',
		widthPx		: 'pik',
		widthPc		: 'procentov',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Razmik med celicami',
		cellPad		: 'Polnilo med celicami',
		caption		: 'Naslov',
		summary		: 'Povzetek',
		headers		: 'Glave',
		headersNone		: 'Brez',
		headersColumn	: 'Prvi stolpec',
		headersRow		: 'Prva vrstica',
		headersBoth		: 'Oboje',
		invalidRows		: 'Število vrstic mora biti večje od 0.',
		invalidCols		: 'Število stolpcev mora biti večje od 0.',
		invalidBorder	: 'Širina obrobe mora biti število.',
		invalidWidth	: 'Širina tabele mora biti število.',
		invalidHeight	: 'Višina tabele mora biti število.',
		invalidCellSpacing	: 'Razmik med celicami mora biti število.',
		invalidCellPadding	: 'Zamik celic mora biti število',

		cell :
		{
			menu			: 'Celica',
			insertBefore	: 'Vstavi celico pred',
			insertAfter		: 'Vstavi celico za',
			deleteCell		: 'Izbriši celice',
			merge			: 'Združi celice',
			mergeRight		: 'Združi desno',
			mergeDown		: 'Druži navzdol',
			splitHorizontal	: 'Razdeli celico vodoravno',
			splitVertical	: 'Razdeli celico navpično',
			title			: 'Lastnosti celice',
			cellType		: 'Vrsta celice',
			rowSpan			: 'Razpon vrstic',
			colSpan			: 'Razpon stolpcev',
			wordWrap		: 'Prelom besedila',
			hAlign			: 'Vodoravna poravnava',
			vAlign			: 'Navpična poravnava',
			alignBaseline	: 'Osnovnica',
			bgColor			: 'Barva ozadja',
			borderColor		: 'Barva obrobe',
			data			: 'Podatki',
			header			: 'Glava',
			yes				: 'Da',
			no				: 'Ne',
			invalidWidth	: 'Širina celice mora biti število.',
			invalidHeight	: 'Višina celice mora biti število.',
			invalidRowSpan	: 'Razpon vrstic mora biti celo število.',
			invalidColSpan	: 'Razpon stolpcev mora biti celo število.',
			chooseColor		: 'Izberi'
		},

		row :
		{
			menu			: 'Vrstica',
			insertBefore	: 'Vstavi vrstico pred',
			insertAfter		: 'Vstavi vrstico za',
			deleteRow		: 'Izbriši vrstice'
		},

		column :
		{
			menu			: 'Stolpec',
			insertBefore	: 'Vstavi stolpec pred',
			insertAfter		: 'Vstavi stolpec za',
			deleteColumn	: 'Izbriši stolpce'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Lastnosti gumba',
		text		: 'Besedilo (Vrednost)',
		type		: 'Tip',
		typeBtn		: 'Gumb',
		typeSbm		: 'Potrdi',
		typeRst		: 'Ponastavi'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Lastnosti potrditvenega polja',
		radioTitle	: 'Lastnosti izbirnega polja',
		value		: 'Vrednost',
		selected	: 'Izbrano'
	},

	// Form Dialog.
	form :
	{
		title		: 'Lastnosti obrazca',
		menu		: 'Lastnosti obrazca',
		action		: 'Akcija',
		method		: 'Metoda',
		encoding	: 'Kodiranje znakov'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Lastnosti spustnega seznama',
		selectInfo	: 'Podatki',
		opAvail		: 'Razpoložljive izbire',
		value		: 'Vrednost',
		size		: 'Velikost',
		lines		: 'vrstic',
		chkMulti	: 'Dovoli izbor večih vrstic',
		opText		: 'Besedilo',
		opValue		: 'Vrednost',
		btnAdd		: 'Dodaj',
		btnModify	: 'Spremeni',
		btnUp		: 'Gor',
		btnDown		: 'Dol',
		btnSetValue : 'Postavi kot privzeto izbiro',
		btnDelete	: 'Izbriši'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Lastnosti vnosnega območja',
		cols		: 'Stolpcev',
		rows		: 'Vrstic'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Lastnosti vnosnega polja',
		name		: 'Ime',
		value		: 'Vrednost',
		charWidth	: 'Dolžina',
		maxChars	: 'Največje število znakov',
		type		: 'Tip',
		typeText	: 'Besedilo',
		typePass	: 'Geslo'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Lastnosti skritega polja',
		name	: 'Ime',
		value	: 'Vrednost'
	},

	// Image Dialog.
	image :
	{
		title		: 'Lastnosti slike',
		titleButton	: 'Lastnosti gumba s sliko',
		menu		: 'Lastnosti slike',
		infoTab		: 'Podatki o sliki',
		btnUpload	: 'Pošlji na strežnik',
		upload		: 'Pošlji',
		alt			: 'Nadomestno besedilo',
		lockRatio	: 'Zakleni razmerje',
		resetSize	: 'Ponastavi velikost',
		border		: 'Obroba',
		hSpace		: 'Vodoravni razmik',
		vSpace		: 'Navpični razmik',
		alertUrl	: 'Vnesite URL slike',
		linkTab		: 'Povezava',
		button2Img	: 'Želiš pretvoriti izbrani gumb s sliko v preprosto sliko?',
		img2Button	: 'Želiš pretvoriti izbrano sliko v gumb s sliko?',
		urlMissing	: 'Manjka vir (URL) slike.',
		validateBorder	: 'Border must be a whole number.', // MISSING
		validateHSpace	: 'HSpace must be a whole number.', // MISSING
		validateVSpace	: 'VSpace must be a whole number.' // MISSING
	},

	// Flash Dialog
	flash :
	{
		properties		: 'Lastnosti Flash',
		propertiesTab	: 'Lastnosti',
		title			: 'Lastnosti Flash',
		chkPlay			: 'Samodejno predvajaj',
		chkLoop			: 'Ponavljanje',
		chkMenu			: 'Omogoči Flash Meni',
		chkFull			: 'Dovoli celozaslonski način',
 		scale			: 'Povečava',
		scaleAll		: 'Pokaži vse',
		scaleNoBorder	: 'Brez obrobe',
		scaleFit		: 'Natančno prileganje',
		access			: 'Dostop skript',
		accessAlways	: 'Vedno',
		accessSameDomain: 'Samo ista domena',
		accessNever		: 'Nikoli',
		alignAbsBottom	: 'Popolnoma na dno',
		alignAbsMiddle	: 'Popolnoma v sredino',
		alignBaseline	: 'Na osnovno črto',
		alignTextTop	: 'Besedilo na vrh',
		quality			: 'Kakovost',
		qualityBest		: 'Najvišja',
		qualityHigh		: 'Visoka',
		qualityAutoHigh	: 'Samodejno visoka',
		qualityMedium	: 'Srednja',
		qualityAutoLow	: 'Samodejno nizka',
		qualityLow		: 'Nizka',
		windowModeWindow: 'Okno',
		windowModeOpaque: 'Motno',
		windowModeTransparent : 'Prosojno',
		windowMode		: 'Vrsta okna',
		flashvars		: 'Spremenljivke za Flash',
		bgcolor			: 'Barva ozadja',
		hSpace			: 'Vodoravni razmik',
		vSpace			: 'Navpični razmik',
		validateSrc		: 'Vnesite URL povezave',
		validateHSpace	: 'Vodoravni razmik mora biti število.',
		validateVSpace	: 'Navpični razmik mora biti število.'
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Preveri črkovanje',
		title			: 'Črkovalnik',
		notAvailable	: 'Oprostite, storitev trenutno ni dosegljiva.',
		errorLoading	: 'Napaka pri nalaganju storitve programa na naslovu %s.',
		notInDic		: 'Ni v slovarju',
		changeTo		: 'Spremeni v',
		btnIgnore		: 'Prezri',
		btnIgnoreAll	: 'Prezri vse',
		btnReplace		: 'Zamenjaj',
		btnReplaceAll	: 'Zamenjaj vse',
		btnUndo			: 'Razveljavi',
		noSuggestions	: '- Ni predlogov -',
		progress		: 'Preverjanje črkovanja se izvaja...',
		noMispell		: 'Črkovanje je končano: Brez napak',
		noChanges		: 'Črkovanje je končano: Nobena beseda ni bila spremenjena',
		oneChange		: 'Črkovanje je končano: Spremenjena je bila ena beseda',
		manyChanges		: 'Črkovanje je končano: Spremenjenih je bilo %1 besed',
		ieSpellDownload	: 'Črkovalnik ni nameščen. Ali ga želite prenesti sedaj?'
	},

	smiley :
	{
		toolbar	: 'Smeško',
		title	: 'Vstavi smeška',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element'
	},

	numberedlist	: 'Oštevilčen seznam',
	bulletedlist	: 'Označen seznam',
	indent			: 'Povečaj zamik',
	outdent			: 'Zmanjšaj zamik',

	justify :
	{
		left	: 'Leva poravnava',
		center	: 'Sredinska poravnava',
		right	: 'Desna poravnava',
		block	: 'Obojestranska poravnava'
	},

	blockquote : 'Citat',

	clipboard :
	{
		title		: 'Prilepi',
		cutError	: 'Varnostne nastavitve brskalnika ne dopuščajo samodejnega izrezovanja. Uporabite kombinacijo tipk na tipkovnici (Ctrl/Cmd+X).',
		copyError	: 'Varnostne nastavitve brskalnika ne dopuščajo samodejnega kopiranja. Uporabite kombinacijo tipk na tipkovnici (Ctrl/Cmd+C).',
		pasteMsg	: 'Prosim prilepite v sleči okvir s pomočjo tipkovnice (<STRONG>Ctrl/Cmd+V</STRONG>) in pritisnite <STRONG>V redu</STRONG>.',
		securityMsg	: 'Zaradi varnostnih nastavitev vašega brskalnika urejevalnik ne more neposredno dostopati do odložišča. Vsebino odložišča ponovno prilepite v to okno.',
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Prilepi iz Worda',
		title			: 'Prilepi iz Worda',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Prilepi kot golo besedilo',
		title	: 'Prilepi kot golo besedilo'
	},

	templates :
	{
		button			: 'Predloge',
		title			: 'Vsebinske predloge',
		options : 'Template Options', // MISSING
		insertOption	: 'Zamenjaj trenutno vsebino',
		selectPromptMsg	: 'Izberite predlogo, ki jo želite odpreti v urejevalniku<br>(trenutna vsebina bo izgubljena):',
		emptyListMsg	: '(Ni pripravljenih predlog)'
	},

	showBlocks : 'Prikaži ograde',

	stylesCombo :
	{
		label		: 'Slog',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Slogi odstavkov',
		panelTitle2	: 'Slogi besedila',
		panelTitle3	: 'Slogi objektov'
	},

	format :
	{
		label		: 'Oblika',
		panelTitle	: 'Oblika',

		tag_p		: 'Navaden',
		tag_pre		: 'Oblikovan',
		tag_address	: 'Napis',
		tag_h1		: 'Naslov 1',
		tag_h2		: 'Naslov 2',
		tag_h3		: 'Naslov 3',
		tag_h4		: 'Naslov 4',
		tag_h5		: 'Naslov 5',
		tag_h6		: 'Naslov 6',
		tag_div		: 'Navaden (DIV)'
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
		label		: 'Pisava',
		voiceLabel	: 'Pisava',
		panelTitle	: 'Pisava'
	},

	fontSize :
	{
		label		: 'Velikost',
		voiceLabel	: 'Velikost',
		panelTitle	: 'Velikost'
	},

	colorButton :
	{
		textColorTitle	: 'Barva besedila',
		bgColorTitle	: 'Barva ozadja',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Samodejno',
		more			: 'Več barv...'
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
		title			: 'Črkovanje med tipkanjem',
		opera_title		: 'Not supported by Opera', // MISSING
		enable			: 'Omogoči SCAYT',
		disable			: 'Onemogoči SCAYT',
		about			: 'O storitvi SCAYT',
		toggle			: 'Preklopi SCAYT',
		options			: 'Možnosti',
		langs			: 'Jeziki',
		moreSuggestions	: 'Več predlogov',
		ignore			: 'Prezri',
		ignoreAll		: 'Prezri vse',
		addWord			: 'Dodaj besedo',
		emptyDic		: 'Ime slovarja ne more biti prazno.',

		optionsTab		: 'Možnosti',
		allCaps			: 'Ignore All-Caps Words', // MISSING
		ignoreDomainNames : 'Ignore Domain Names', // MISSING
		mixedCase		: 'Ignore Words with Mixed Case', // MISSING
		mixedWithDigits	: 'Ignore Words with Numbers', // MISSING

		languagesTab	: 'Jeziki',

		dictionariesTab	: 'Slovarji',
		dic_field_name	: 'Dictionary name', // MISSING
		dic_create		: 'Create', // MISSING
		dic_restore		: 'Restore', // MISSING
		dic_delete		: 'Delete', // MISSING
		dic_rename		: 'Rename', // MISSING
		dic_info		: 'Initially the User Dictionary is stored in a Cookie. However, Cookies are limited in size. When the User Dictionary grows to a point where it cannot be stored in a Cookie, then the dictionary may be stored on our server. To store your personal dictionary on our server you should specify a name for your dictionary. If you already have a stored dictionary, please type its name and click the Restore button.', // MISSING

		aboutTab		: 'O storitvi'
	},

	about :
	{
		title		: 'O programu CKEditor',
		dlgTitle	: 'O programu CKEditor',
		help	: 'Check $1 for help.', // MISSING
		userGuide : 'CKEditor User\'s Guide', // MISSING
		moreInfo	: 'Za informacijo o licenci prostim obiščite našo spletno stran:',
		copy		: 'Copyright &copy; $1. Vse pravice pridržane.'
	},

	maximize : 'Maksimiraj',
	minimize : 'Minimiraj',

	fakeobjects :
	{
		anchor		: 'Sidro',
		flash		: 'Flash animacija',
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'Neznan objekt'
	},

	resize : 'Potegni za spremembo velikosti',

	colordialog :
	{
		title		: 'Izberi barvo',
		options	:	'Color Options', // MISSING
		highlight	: 'Poudarjeno',
		selected	: 'Izbrano',
		clear		: 'Počisti'
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
		label : 'Lastnosti dokumenta',
		title : 'Lastnosti dokumenta',
		design : 'Design', // MISSING
		meta : 'Meta podatki',
		chooseColor : 'Izberi',
		other : '<drug>',
		docTitle :	'Naslov strani',
		charset : 	'Kodna tabela',
		charsetOther : 'Druga kodna tabela',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Srednjeevropsko',
		charsetCT : 'Tradicionalno Kitajsko (Big5)',
		charsetCR : 'Cirilica',
		charsetGR : 'Grško',
		charsetJP : 'Japonsko',
		charsetKR : 'Korejsko',
		charsetTR : 'Turško',
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Zahodnoevropsko',
		docType : 'Glava tipa dokumenta',
		docTypeOther : 'Druga glava tipa dokumenta',
		xhtmlDec : 'Vstavi XHTML deklaracije',
		bgColor : 'Barva ozadja',
		bgImage : 'URL slike za ozadje',
		bgFixed : 'Nepremično ozadje',
		txtColor : 'Barva besedila',
		margin : 'Zamiki strani',
		marginTop : 'Na vrhu',
		marginLeft : 'Levo',
		marginRight : 'Desno',
		marginBottom : 'Spodaj',
		metaKeywords : 'Ključne besede (ločene z vejicami)',
		metaDescription : 'Opis strani',
		metaAuthor : 'Avtor',
		metaCopyright : 'Avtorske pravice',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
