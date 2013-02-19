/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Romanian language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['ro'] =
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
	toolbars	: 'Editează bara de unelte',
	editor		: 'Rich Text Editor',

	// Toolbar buttons without dialogs.
	source			: 'Sursa',
	newPage			: 'Pagină nouă',
	save			: 'Salvează',
	preview			: 'Previzualizare',
	cut				: 'Taie',
	copy			: 'Copiază',
	paste			: 'Adaugă',
	print			: 'Printează',
	underline		: 'Subliniat (underline)',
	bold			: 'Îngroşat (bold)',
	italic			: 'Înclinat (italic)',
	selectAll		: 'Selectează tot',
	removeFormat	: 'Înlătură formatarea',
	strike			: 'Tăiat (strike through)',
	subscript		: 'Indice (subscript)',
	superscript		: 'Putere (superscript)',
	horizontalrule	: 'Inserează linie orizontală',
	pagebreak		: 'Inserează separator de pagină (Page Break)',
	pagebreakAlt		: 'Page Break',
	unlink			: 'Înlătură link (legătură web)',
	undo			: 'Starea anterioară (undo)',
	redo			: 'Starea ulterioară (redo)',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Răsfoieşte server',
		url				: 'URL',
		protocol		: 'Protocol',
		upload			: 'Încarcă',
		uploadSubmit	: 'Trimite la server',
		image			: 'Imagine',
		flash			: 'Flash',
		form			: 'Formular (Form)',
		checkbox		: 'Bifă (Checkbox)',
		radio			: 'Buton radio (RadioButton)',
		textField		: 'Câmp text (TextField)',
		textarea		: 'Suprafaţă text (Textarea)',
		hiddenField		: 'Câmp ascuns (HiddenField)',
		button			: 'Buton',
		select			: 'Câmp selecţie (SelectionField)',
		imageButton		: 'Buton imagine (ImageButton)',
		notSet			: '<nesetat>',
		id				: 'Id',
		name			: 'Nume',
		langDir			: 'Direcţia cuvintelor',
		langDirLtr		: 'stânga-dreapta (LTR)',
		langDirRtl		: 'dreapta-stânga (RTL)',
		langCode		: 'Codul limbii',
		longDescr		: 'Descrierea lungă URL',
		cssClass		: 'Clasele cu stilul paginii (CSS)',
		advisoryTitle	: 'Titlul consultativ',
		cssStyle		: 'Stil',
		ok				: 'OK',
		cancel			: 'Anulare',
		close			: 'Închide',
		preview			: 'Previzualizare',
		generalTab		: 'General',
		advancedTab		: 'Avansat',
		validateNumberFailed : 'Această valoare nu este un număr.',
		confirmNewPage	: 'Orice modificări nesalvate ale acestui conținut, vor fi pierdute. Sigur doriți încărcarea unei noi pagini?',
		confirmCancel	: 'Câteva opțiuni au fost schimbate. Sigur doriți să închideți dialogul?',
		options			: 'Opțiuni',
		target			: 'Țintă',
		targetNew		: 'Fereastră nouă (_blank)',
		targetTop		: 'Topmost Window (_top)',
		targetSelf		: 'În aceeași fereastră (_self)',
		targetParent	: 'Parent Window (_parent)',
		langDirLTR		: 'Stânga spre Dreapta (LTR)',
		langDirRTL		: 'Dreapta spre Stânga (RTL)',
		styles			: 'Stil',
		cssClasses		: 'Stylesheet Classes',
		width			: 'Lăţime',
		height			: 'Înălţime',
		align			: 'Aliniere',
		alignLeft		: 'Mărește Bara',
		alignRight		: 'Dreapta',
		alignCenter		: 'Centru',
		alignTop		: 'Sus',
		alignMiddle		: 'Mijloc',
		alignBottom		: 'Jos',
		invalidHeight	: 'Înălțimea trebuie să fie un număr.',
		invalidWidth	: 'Lățimea trebuie să fie un număr.',
		invalidCssLength	: 'Value specified for the "%1" field must be a positive number with or without a valid CSS measurement unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING
		invalidHtmlLength	: 'Value specified for the "%1" field must be a positive number with or without a valid HTML measurement unit (px or %).', // MISSING
		invalidInlineStyle	: 'Value specified for the inline style must consist of one or more tuples with the format of "name : value", separated by semi-colons.', // MISSING
		cssLengthTooltip	: 'Enter a number for a value in pixels or a number with a valid CSS unit (px, %, in, cm, mm, em, ex, pt, or pc).', // MISSING

		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>' // MISSING
	},

	contextmenu :
	{
		options : 'Opțiuni Meniu Contextual'
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Inserează caracter special',
		title		: 'Selectează caracter special',
		options : 'Opțiuni caractere speciale'
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Inserează/Editează link (legătură web)',
		other 		: '<alt>',
		menu		: 'Editează Link',
		title		: 'Link (Legătură web)',
		info		: 'Informaţii despre link (Legătură web)',
		target		: 'Ţintă (Target)',
		upload		: 'Încarcă',
		advanced	: 'Avansat',
		type		: 'Tipul link-ului (al legăturii web)',
		toUrl		: 'URL',
		toAnchor	: 'Ancoră în această pagină',
		toEmail		: 'E-Mail',
		targetFrame		: '<frame>',
		targetPopup		: '<fereastra popup>',
		targetFrameName	: 'Numele frameului ţintă',
		targetPopupName	: 'Numele ferestrei popup',
		popupFeatures	: 'Proprietăţile ferestrei popup',
		popupResizable	: 'Redimensionabil',
		popupStatusBar	: 'Bara de status',
		popupLocationBar: 'Bara de locaţie',
		popupToolbar	: 'Bara de opţiuni',
		popupMenuBar	: 'Bara de meniu',
		popupFullScreen	: 'Tot ecranul (Full Screen)(IE)',
		popupScrollBars	: 'Bare de derulare',
		popupDependent	: 'Dependent (Netscape)',
		popupLeft		: 'Poziţia la stânga',
		popupTop		: 'Poziţia la dreapta',
		id				: 'Id',
		langDir			: 'Direcţia cuvintelor',
		langDirLTR		: 'stânga-dreapta (LTR)',
		langDirRTL		: 'dreapta-stânga (RTL)',
		acccessKey		: 'Tasta de acces',
		name			: 'Nume',
		langCode			: 'Direcţia cuvintelor',
		tabIndex			: 'Indexul tabului',
		advisoryTitle		: 'Titlul consultativ',
		advisoryContentType	: 'Tipul consultativ al titlului',
		cssClasses		: 'Clasele cu stilul paginii (CSS)',
		charset			: 'Setul de caractere al resursei legate',
		styles			: 'Stil',
		rel			: 'Relație',
		selectAnchor		: 'Selectaţi o ancoră',
		anchorName		: 'după numele ancorei',
		anchorId			: 'după Id-ul elementului',
		emailAddress		: 'Adresă de e-mail',
		emailSubject		: 'Subiectul mesajului',
		emailBody		: 'Opțiuni Meniu Contextual',
		noAnchors		: '(Nicio ancoră disponibilă în document)',
		noUrl			: 'Vă rugăm să scrieţi URL-ul',
		noEmail			: 'Vă rugăm să scrieţi adresa de e-mail'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Inserează/Editează ancoră',
		menu		: 'Proprietăţi ancoră',
		title		: 'Proprietăţi ancoră',
		name		: 'Numele ancorei',
		errorName	: 'Vă rugăm scrieţi numele ancorei',
		remove		: 'Elimină ancora'
	},

	// List style dialog
	list:
	{
		numberedTitle		: 'Proprietățile listei numerotate',
		bulletedTitle		: 'Proprietățile listei cu simboluri',
		type				: 'Tip',
		start				: 'Start',
		validateStartNumber				:'Începutul listei trebuie să fie un număr întreg.',
		circle				: 'Cerc',
		disc				: 'Disc',
		square				: 'Pătrat',
		none				: 'Nimic',
		notset				: '<nesetat>',
		armenian			: 'Numerotare armeniană',
		georgian			: 'Numerotare georgiană (an, ban, gan, etc.)',
		lowerRoman			: 'Cifre romane mici (i, ii, iii, iv, v, etc.)',
		upperRoman			: 'Cifre romane mari (I, II, III, IV, V, etc.)',
		lowerAlpha			: 'Litere mici (a, b, c, d, e, etc.)',
		upperAlpha			: 'Litere mari (A, B, C, D, E, etc.)',
		lowerGreek			: 'Litere grecești mici (alpha, beta, gamma, etc.)',
		decimal				: 'Decimale (1, 2, 3, etc.)',
		decimalLeadingZero	: 'Decimale cu zero în față (01, 02, 03, etc.)'
	},

	// Find And Replace Dialog
	findAndReplace :
	{
		title				: 'Găseşte şi înlocuieşte',
		find				: 'Găseşte',
		replace				: 'Înlocuieşte',
		findWhat			: 'Găseşte:',
		replaceWith			: 'Înlocuieşte cu:',
		notFoundMsg			: 'Textul specificat nu a fost găsit.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Deosebeşte majuscule de minuscule (Match case)',
		matchWord			: 'Doar cuvintele întregi',
		matchCyclic			: 'Potrivește ciclic',
		replaceAll			: 'Înlocuieşte tot',
		replaceSuccessMsg	: '%1 căutări înlocuite.'
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabel',
		title		: 'Proprietăţile tabelului',
		menu		: 'Proprietăţile tabelului',
		deleteTable	: 'Şterge tabel',
		rows		: 'Rânduri',
		columns		: 'Coloane',
		border		: 'Mărimea marginii',
		widthPx		: 'pixeli',
		widthPc		: 'procente',
		widthUnit	: 'unitate lățime',
		cellSpace	: 'Spaţiu între celule',
		cellPad		: 'Spaţiu în cadrul celulei',
		caption		: 'Titlu (Caption)',
		summary		: 'Rezumat',
		headers		: 'Antente',
		headersNone		: 'Nimic',
		headersColumn	: 'Prima coloană',
		headersRow		: 'Primul rând',
		headersBoth		: 'Ambele',
		invalidRows		: 'Numărul rândurilor trebuie să fie mai mare decât 0.',
		invalidCols		: 'Numărul coloanelor trebuie să fie mai mare decât 0.',
		invalidBorder	: 'Dimensiunea bordurii trebuie să aibe un număr.',
		invalidWidth	: 'Lățimea tabelului trebuie să fie un număr.',
		invalidHeight	: 'Table height must be a number.', // MISSING
		invalidCellSpacing	: 'Spațierea celului trebuie să fie un număr pozitiv.',
		invalidCellPadding	: 'Cell padding must be a positive number.', // MISSING

		cell :
		{
			menu			: 'Celulă',
			insertBefore	: 'Inserează celulă înainte',
			insertAfter		: 'Inserează celulă după',
			deleteCell		: 'Şterge celule',
			merge			: 'Uneşte celule',
			mergeRight		: 'Uneşte la dreapta',
			mergeDown		: 'Uneşte jos',
			splitHorizontal	: 'Împarte celula pe orizontală',
			splitVertical	: 'Împarte celula pe verticală',
			title			: 'Proprietăți celulă',
			cellType		: 'Tipul celulei',
			rowSpan			: 'Rows Span', // MISSING
			colSpan			: 'Columns Span', // MISSING
			wordWrap		: 'Word Wrap', // MISSING
			hAlign			: 'Aliniament orizontal',
			vAlign			: 'Aliniament vertical',
			alignBaseline	: 'Baseline', // MISSING
			bgColor			: 'Culoare fundal',
			borderColor		: 'Culoare bordură',
			data			: 'Data',
			header			: 'Antet',
			yes				: 'Da',
			no				: 'Nu',
			invalidWidth	: 'Lățimea celulei trebuie să fie un număr.',
			invalidHeight	: 'Înălțimea celulei trebuie să fie un număr.',
			invalidRowSpan	: 'Rows span must be a whole number.', // MISSING
			invalidColSpan	: 'Columns span must be a whole number.', // MISSING
			chooseColor		: 'Alege'
		},

		row :
		{
			menu			: 'Rând',
			insertBefore	: 'Inserează rând înainte',
			insertAfter		: 'Inserează rând după',
			deleteRow		: 'Şterge rânduri'
		},

		column :
		{
			menu			: 'Coloană',
			insertBefore	: 'Inserează coloană înainte',
			insertAfter		: 'Inserează coloană după',
			deleteColumn	: 'Şterge celule'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Proprietăţi buton',
		text		: 'Text (Valoare)',
		type		: 'Tip',
		typeBtn		: 'Buton',
		typeSbm		: 'Trimite',
		typeRst		: 'Reset'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Proprietăţi bifă (Checkbox)',
		radioTitle	: 'Proprietăţi buton radio (Radio Button)',
		value		: 'Valoare',
		selected	: 'Selectat'
	},

	// Form Dialog.
	form :
	{
		title		: 'Proprietăţi formular (Form)',
		menu		: 'Proprietăţi formular (Form)',
		action		: 'Acţiune',
		method		: 'Metodă',
		encoding	: 'Encodare'
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Proprietăţi câmp selecţie (Selection Field)',
		selectInfo	: 'Informaţii',
		opAvail		: 'Opţiuni disponibile',
		value		: 'Valoare',
		size		: 'Mărime',
		lines		: 'linii',
		chkMulti	: 'Permite selecţii multiple',
		opText		: 'Text',
		opValue		: 'Valoare',
		btnAdd		: 'Adaugă',
		btnModify	: 'Modifică',
		btnUp		: 'Sus',
		btnDown		: 'Jos',
		btnSetValue : 'Setează ca valoare selectată',
		btnDelete	: 'Şterge'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Proprietăţi suprafaţă text (Textarea)',
		cols		: 'Coloane',
		rows		: 'Linii'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Proprietăţi câmp text (Text Field)',
		name		: 'Nume',
		value		: 'Valoare',
		charWidth	: 'Lărgimea caracterului',
		maxChars	: 'Caractere maxime',
		type		: 'Tip',
		typeText	: 'Text',
		typePass	: 'Parolă'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Proprietăţi câmp ascuns (Hidden Field)',
		name	: 'Nume',
		value	: 'Valoare'
	},

	// Image Dialog.
	image :
	{
		title		: 'Proprietăţile imaginii',
		titleButton	: 'Proprietăţi buton imagine (Image Button)',
		menu		: 'Proprietăţile imaginii',
		infoTab		: 'Informaţii despre imagine',
		btnUpload	: 'Trimite la server',
		upload		: 'Încarcă',
		alt			: 'Text alternativ',
		lockRatio	: 'Păstrează proporţiile',
		resetSize	: 'Resetează mărimea',
		border		: 'Margine',
		hSpace		: 'HSpace',
		vSpace		: 'VSpace',
		alertUrl	: 'Vă rugăm să scrieţi URL-ul imaginii',
		linkTab		: 'Link (Legătură web)',
		button2Img	: 'Do you want to transform the selected image button on a simple image?', // MISSING
		img2Button	: 'Do you want to transform the selected image on a image button?', // MISSING
		urlMissing	: 'Sursa URL a imaginii lipsește.',
		validateBorder	: 'Bordura trebuie să fie un număr întreg.',
		validateHSpace	: 'Hspace trebuie să fie un număr întreg.',
		validateVSpace	: 'Vspace trebuie să fie un număr întreg.'
	},

	// Flash Dialog
	flash :
	{
		properties		: 'Proprietăţile flashului',
		propertiesTab	: 'Proprietăți',
		title			: 'Proprietăţile flashului',
		chkPlay			: 'Rulează automat',
		chkLoop			: 'Repetă (Loop)',
		chkMenu			: 'Activează meniul flash',
		chkFull			: 'Permite pe tot ecranul',
 		scale			: 'Scală',
		scaleAll		: 'Arată tot',
		scaleNoBorder	: 'Fără bordură (No border)',
		scaleFit		: 'Potriveşte',
		access			: 'Acces script',
		accessAlways	: 'Întotdeauna',
		accessSameDomain: 'Același domeniu',
		accessNever		: 'Niciodată',
		alignAbsBottom	: 'Jos absolut (Abs Bottom)',
		alignAbsMiddle	: 'Mijloc absolut (Abs Middle)',
		alignBaseline	: 'Linia de jos (Baseline)',
		alignTextTop	: 'Text sus',
		quality			: 'Calitate',
		qualityBest		: 'Cea mai bună',
		qualityHigh		: 'Înaltă',
		qualityAutoHigh	: 'Auto înaltă',
		qualityMedium	: 'Medie',
		qualityAutoLow	: 'Auto Joasă',
		qualityLow		: 'Joasă',
		windowModeWindow: 'Fereastră',
		windowModeOpaque: 'Opacă',
		windowModeTransparent : 'Transparentă',
		windowMode		: 'Mod fereastră',
		flashvars		: 'Variabile pentru flash',
		bgcolor			: 'Coloarea fundalului',
		hSpace			: 'HSpace',
		vSpace			: 'VSpace',
		validateSrc		: 'Vă rugăm să scrieţi URL-ul',
		validateHSpace	: 'Hspace trebuie să fie un număr.',
		validateVSpace	: 'VSpace trebuie să fie un număr'
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Verifică scrierea textului',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Scuzați, dar serviciul nu este disponibil momentan.',
		errorLoading	: 'Eroare în lansarea aplicației service host %s.',
		notInDic		: 'Nu e în dicţionar',
		changeTo		: 'Schimbă în',
		btnIgnore		: 'Ignoră',
		btnIgnoreAll	: 'Ignoră toate',
		btnReplace		: 'Înlocuieşte',
		btnReplaceAll	: 'Înlocuieşte tot',
		btnUndo			: 'Starea anterioară (undo)',
		noSuggestions	: '- Fără sugestii -',
		progress		: 'Verificarea textului în desfăşurare...',
		noMispell		: 'Verificarea textului terminată: Nicio greşeală găsită',
		noChanges		: 'Verificarea textului terminată: Niciun cuvânt modificat',
		oneChange		: 'Verificarea textului terminată: Un cuvânt modificat',
		manyChanges		: 'Verificarea textului terminată: 1% cuvinte modificate',
		ieSpellDownload	: 'Unealta pentru verificat textul (Spell checker) neinstalată. Doriţi să o descărcaţi acum?'
	},

	smiley :
	{
		toolbar	: 'Figură expresivă (Emoticon)',
		title	: 'Inserează o figură expresivă (Emoticon)',
		options : 'Opțiuni figuri expresive'
	},

	elementsPath :
	{
		eleLabel : 'Calea elementelor',
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Inserează / Elimină Listă numerotată',
	bulletedlist	: 'Inserează / Elimină Listă cu puncte',
	indent			: 'Creşte indentarea',
	outdent			: 'Scade indentarea',

	justify :
	{
		left	: 'Aliniere la stânga',
		center	: 'Aliniere centrală',
		right	: 'Aliniere la dreapta',
		block	: 'Aliniere în bloc (Block Justify)'
	},

	blockquote : 'Citat',

	clipboard :
	{
		title		: 'Adaugă',
		cutError	: 'Setările de securitate ale navigatorului (browser) pe care îl folosiţi nu permit editorului să execute automat operaţiunea de tăiere. Vă rugăm folosiţi tastatura (Ctrl/Cmd+X).',
		copyError	: 'Setările de securitate ale navigatorului (browser) pe care îl folosiţi nu permit editorului să execute automat operaţiunea de copiere. Vă rugăm folosiţi tastatura (Ctrl/Cmd+C).',
		pasteMsg	: 'Vă rugăm adăugaţi în căsuţa următoare folosind tastatura (<strong>Ctrl/Cmd+V</strong>) şi apăsaţi OK',
		securityMsg	: 'Din cauza setărilor de securitate ale programului dvs. cu care navigaţi pe internet (browser), editorul nu poate accesa direct datele din clipboard. Va trebui să adăugaţi din nou datele în această fereastră.',
		pasteArea	: 'Suprafața de adăugare'
	},

	pastefromword :
	{
		confirmCleanup	: 'Textul pe care doriți să-l lipiți este din Word. Doriți curățarea textului înante de a-l adăuga?',
		toolbar			: 'Adaugă din Word',
		title			: 'Adaugă din Word',
		error			: 'Nu a fost posibilă curățarea datelor adăugate datorită unei erori interne'
	},

	pasteText :
	{
		button	: 'Adaugă ca text simplu (Plain Text)',
		title	: 'Adaugă ca text simplu (Plain Text)'
	},

	templates :
	{
		button			: 'Template-uri (şabloane)',
		title			: 'Template-uri (şabloane) de conţinut',
		options : 'Opțiuni șabloane',
		insertOption	: 'Înlocuieşte cuprinsul actual',
		selectPromptMsg	: 'Vă rugăm selectaţi template-ul (şablonul) ce se va deschide în editor<br>(conţinutul actual va fi pierdut):',
		emptyListMsg	: '(Niciun template (şablon) definit)'
	},

	showBlocks : 'Arată blocurile',

	stylesCombo :
	{
		label		: 'Stil',
		panelTitle	: 'Formatarea stilurilor',
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Formatare',
		panelTitle	: 'Formatare',

		tag_p		: 'Normal',
		tag_pre		: 'Formatat',
		tag_address	: 'Adresă',
		tag_h1		: 'Heading 1',
		tag_h2		: 'Heading 2',
		tag_h3		: 'Heading 3',
		tag_h4		: 'Heading 4',
		tag_h5		: 'Heading 5',
		tag_h6		: 'Heading 6',
		tag_div		: 'Normal (DIV)'
	},

	div :
	{
		title				: 'Create Div Container', // MISSING
		toolbar				: 'Create Div Container', // MISSING
		cssClassInputLabel	: 'Stylesheet Classes', // MISSING
		styleSelectLabel	: 'Stil',
		IdInputLabel		: 'Id',
		languageCodeInputLabel	: 'Codul limbii',
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
		label		: 'Mărime',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Mărime'
	},

	colorButton :
	{
		textColorTitle	: 'Culoarea textului',
		bgColorTitle	: 'Coloarea fundalului',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automatic',
		more			: 'Mai multe culori...'
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

	maximize : 'Mărește',
	minimize : 'Micșorează',

	fakeobjects :
	{
		anchor		: 'Anchor', // MISSING
		flash		: 'Flash Animation', // MISSING
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'Unknown Object' // MISSING
	},

	resize : 'Trage pentru a redimensiona',

	colordialog :
	{
		title		: 'Select color', // MISSING
		options	:	'Color Options', // MISSING
		highlight	: 'Highlight', // MISSING
		selected	: 'Selected Color', // MISSING
		clear		: 'Clear' // MISSING
	},

	toolbarCollapse	: 'Micșorează Bara',
	toolbarExpand	: 'Mărește Bara',

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
		label : 'Proprietăţile documentului',
		title : 'Proprietăţile documentului',
		design : 'Design', // MISSING
		meta : 'Meta Tags', // MISSING
		chooseColor : 'Choose', // MISSING
		other : '<alt>',
		docTitle :	'Titlul paginii',
		charset : 	'Encoding setului de caractere',
		charsetOther : 'Alt encoding al setului de caractere',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'Central European', // MISSING
		charsetCT : 'Chinezesc tradiţional (Big5)',
		charsetCR : 'Chirilic',
		charsetGR : 'Grecesc',
		charsetJP : 'Japonez',
		charsetKR : 'Corean',
		charsetTR : 'Turcesc',
		charsetUN : 'Unicode (UTF-8)', // MISSING
		charsetWE : 'Vest european',
		docType : 'Document Type Heading', // MISSING
		docTypeOther : 'Alt Document Type Heading',
		xhtmlDec : 'Include declaraţii XHTML',
		bgColor : 'Culoarea fundalului (Background Color)',
		bgImage : 'URL-ul imaginii din fundal (Background Image URL)',
		bgFixed : 'Fundal neflotant, fix (Non-scrolling Background)',
		txtColor : 'Culoarea textului',
		margin : 'Marginile paginii',
		marginTop : 'Sus',
		marginLeft : 'Stânga',
		marginRight : 'Dreapta',
		marginBottom : 'Jos',
		metaKeywords : 'Cuvinte cheie după care se va indexa documentul (separate prin virgulă)',
		metaDescription : 'Descrierea documentului',
		metaAuthor : 'Autor',
		metaCopyright : 'Drepturi de autor',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
