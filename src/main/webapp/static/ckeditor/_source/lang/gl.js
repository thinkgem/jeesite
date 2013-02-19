/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Galician language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['gl'] =
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
	source			: 'Código Fonte',
	newPage			: 'Nova Páxina',
	save			: 'Gardar',
	preview			: 'Vista Previa',
	cut				: 'Cortar',
	copy			: 'Copiar',
	paste			: 'Pegar',
	print			: 'Imprimir',
	underline		: 'Sub-raiado',
	bold			: 'Negrita',
	italic			: 'Cursiva',
	selectAll		: 'Seleccionar todo',
	removeFormat	: 'Eliminar Formato',
	strike			: 'Tachado',
	subscript		: 'Subíndice',
	superscript		: 'Superíndice',
	horizontalrule	: 'Inserir Liña Horizontal',
	pagebreak		: 'Inserir Salto de Páxina',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Eliminar Ligazón',
	undo			: 'Desfacer',
	redo			: 'Refacer',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Navegar no Servidor',
		url				: 'URL',
		protocol		: 'Protocolo',
		upload			: 'Carregar',
		uploadSubmit	: 'Enviar ó Servidor',
		image			: 'Imaxe',
		flash			: 'Flash',
		form			: 'Formulario',
		checkbox		: 'Cadro de Verificación',
		radio			: 'Botón de Radio',
		textField		: 'Campo de Texto',
		textarea		: 'Área de Texto',
		hiddenField		: 'Campo Oculto',
		button			: 'Botón',
		select			: 'Campo de Selección',
		imageButton		: 'Botón de Imaxe',
		notSet			: '<non definido>',
		id				: 'Id',
		name			: 'Nome',
		langDir			: 'Orientación do Idioma',
		langDirLtr		: 'Esquerda a Dereita (LTR)',
		langDirRtl		: 'Dereita a Esquerda (RTL)',
		langCode		: 'Código do Idioma',
		longDescr		: 'Descrición Completa da URL',
		cssClass		: 'Clases da Folla de Estilos',
		advisoryTitle	: 'Título',
		cssStyle		: 'Estilo',
		ok				: 'OK',
		cancel			: 'Cancelar',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Advanzado',
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
		width			: 'Largura',
		height			: 'Altura',
		align			: 'Aliñamento',
		alignLeft		: 'Esquerda',
		alignRight		: 'Dereita',
		alignCenter		: 'Centro',
		alignTop		: 'Tope',
		alignMiddle		: 'Centro',
		alignBottom		: 'Pé',
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
		toolbar		: 'Inserir Carácter Especial',
		title		: 'Seleccione Caracter Especial',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Inserir/Editar Ligazón',
		other 		: '<outro>',
		menu		: 'Editar Ligazón',
		title		: 'Ligazón',
		info		: 'Información da Ligazón',
		target		: 'Destino',
		upload		: 'Carregar',
		advanced	: 'Advanzado',
		type		: 'Tipo de Ligazón',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Referencia nesta páxina',
		toEmail		: 'E-Mail',
		targetFrame		: '<frame>',
		targetPopup		: '<Xanela Emerxente>',
		targetFrameName	: 'Nome do Marco Destino',
		targetPopupName	: 'Nome da Xanela Emerxente',
		popupFeatures	: 'Características da Xanela Emerxente',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Barra de Estado',
		popupLocationBar: 'Barra de Localización',
		popupToolbar	: 'Barra de Ferramentas',
		popupMenuBar	: 'Barra de Menú',
		popupFullScreen	: 'A Toda Pantalla (IE)',
		popupScrollBars	: 'Barras de Desplazamento',
		popupDependent	: 'Dependente (Netscape)',
		popupLeft		: 'Posición Esquerda',
		popupTop		: 'Posición dende Arriba',
		id				: 'Id', // MISSING
		langDir			: 'Orientación do Idioma',
		langDirLTR		: 'Esquerda a Dereita (LTR)',
		langDirRTL		: 'Dereita a Esquerda (RTL)',
		acccessKey		: 'Chave de Acceso',
		name			: 'Nome',
		langCode			: 'Orientación do Idioma',
		tabIndex			: 'Índice de Tabulación',
		advisoryTitle		: 'Título',
		advisoryContentType	: 'Tipo de Contido',
		cssClasses		: 'Clases da Folla de Estilos',
		charset			: 'Fonte de Caracteres Vinculado',
		styles			: 'Estilo',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Seleccionar unha Referencia',
		anchorName		: 'Por Nome de Referencia',
		anchorId			: 'Por Element Id',
		emailAddress		: 'Enderezo de E-Mail',
		emailSubject		: 'Asunto do Mensaxe',
		emailBody		: 'Corpo do Mensaxe',
		noAnchors		: '(Non hai referencias disponibles no documento)',
		noUrl			: 'Por favor, escriba a ligazón URL',
		noEmail			: 'Por favor, escriba o enderezo de e-mail'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Inserir/Editar Referencia',
		menu		: 'Propriedades da Referencia',
		title		: 'Propriedades da Referencia',
		name		: 'Nome da Referencia',
		errorName	: 'Por favor, escriba o nome da referencia',
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
		find				: 'Procurar',
		replace				: 'Substituir',
		findWhat			: 'Texto a procurar:',
		replaceWith			: 'Substituir con:',
		notFoundMsg			: 'Non te atopou o texto indicado.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Coincidir Mai./min.',
		matchWord			: 'Coincidir con toda a palabra',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Substitiur Todo',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabla',
		title		: 'Propiedades da Táboa',
		menu		: 'Propiedades da Táboa',
		deleteTable	: 'Borrar Táboa',
		rows		: 'Filas',
		columns		: 'Columnas',
		border		: 'Tamaño do Borde',
		widthPx		: 'pixels',
		widthPc		: 'percent',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Marxe entre Celas',
		cellPad		: 'Marxe interior',
		caption		: 'Título',
		summary		: 'Sumario',
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
			menu			: 'Cela',
			insertBefore	: 'Insert Cell Before', // MISSING
			insertAfter		: 'Insert Cell After', // MISSING
			deleteCell		: 'Borrar Cela',
			merge			: 'Unir Celas',
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
			menu			: 'Fila',
			insertBefore	: 'Insert Row Before', // MISSING
			insertAfter		: 'Insert Row After', // MISSING
			deleteRow		: 'Borrar Filas'
		},

		column :
		{
			menu			: 'Columna',
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Borrar Columnas'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Propriedades do Botón',
		text		: 'Texto (Valor)',
		type		: 'Tipo',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Propriedades do Cadro de Verificación',
		radioTitle	: 'Propriedades do Botón de Radio',
		value		: 'Valor',
		selected	: 'Seleccionado'
	},

	// Form Dialog.
	form :
	{
		title		: 'Propriedades do Formulario',
		menu		: 'Propriedades do Formulario',
		action		: 'Acción',
		method		: 'Método',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Propriedades do Campo de Selección',
		selectInfo	: 'Info',
		opAvail		: 'Opcións Disponibles',
		value		: 'Valor',
		size		: 'Tamaño',
		lines		: 'liñas',
		chkMulti	: 'Permitir múltiples seleccións',
		opText		: 'Texto',
		opValue		: 'Valor',
		btnAdd		: 'Engadir',
		btnModify	: 'Modificar',
		btnUp		: 'Subir',
		btnDown		: 'Baixar',
		btnSetValue : 'Definir como valor por defecto',
		btnDelete	: 'Borrar'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Propriedades da Área de Texto',
		cols		: 'Columnas',
		rows		: 'Filas'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Propriedades do Campo de Texto',
		name		: 'Nome',
		value		: 'Valor',
		charWidth	: 'Tamaño do Caracter',
		maxChars	: 'Máximo de Caracteres',
		type		: 'Tipo',
		typeText	: 'Texto',
		typePass	: 'Chave'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Propriedades do Campo Oculto',
		name	: 'Nome',
		value	: 'Valor'
	},

	// Image Dialog.
	image :
	{
		title		: 'Propriedades da Imaxe',
		titleButton	: 'Propriedades do Botón de Imaxe',
		menu		: 'Propriedades da Imaxe',
		infoTab		: 'Información da Imaxe',
		btnUpload	: 'Enviar ó Servidor',
		upload		: 'Carregar',
		alt			: 'Texto Alternativo',
		lockRatio	: 'Proporcional',
		resetSize	: 'Tamaño Orixinal',
		border		: 'Límite',
		hSpace		: 'Esp. Horiz.',
		vSpace		: 'Esp. Vert.',
		alertUrl	: 'Por favor, escriba a URL da imaxe',
		linkTab		: 'Ligazón',
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
		properties		: 'Propriedades Flash',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Propriedades Flash',
		chkPlay			: 'Auto Execución',
		chkLoop			: 'Bucle',
		chkMenu			: 'Activar Menú Flash',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Escalar',
		scaleAll		: 'Amosar Todo',
		scaleNoBorder	: 'Sen Borde',
		scaleFit		: 'Encaixar axustando',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs Inferior',
		alignAbsMiddle	: 'Abs Centro',
		alignBaseline	: 'Liña Base',
		alignTextTop	: 'Tope do Texto',
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
		bgcolor			: 'Cor do Fondo',
		hSpace			: 'Esp. Horiz.',
		vSpace			: 'Esp. Vert.',
		validateSrc		: 'Por favor, escriba a ligazón URL',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Corrección Ortográfica',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Non está no diccionario',
		changeTo		: 'Cambiar a',
		btnIgnore		: 'Ignorar',
		btnIgnoreAll	: 'Ignorar Todas',
		btnReplace		: 'Substituir',
		btnReplaceAll	: 'Substituir Todas',
		btnUndo			: 'Desfacer',
		noSuggestions	: '- Sen candidatos -',
		progress		: 'Corrección ortográfica en progreso...',
		noMispell		: 'Corrección ortográfica rematada: Non se atoparon erros',
		noChanges		: 'Corrección ortográfica rematada: Non se substituiu nengunha verba',
		oneChange		: 'Corrección ortográfica rematada: Unha verba substituida',
		manyChanges		: 'Corrección ortográfica rematada: %1 verbas substituidas',
		ieSpellDownload	: 'O corrector ortográfico non está instalado. ¿Quere descargalo agora?'
	},

	smiley :
	{
		toolbar	: 'Smiley',
		title	: 'Inserte un Smiley',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Lista Numerada',
	bulletedlist	: 'Marcas',
	indent			: 'Aumentar Sangría',
	outdent			: 'Disminuir Sangría',

	justify :
	{
		left	: 'Aliñar á Esquerda',
		center	: 'Centrado',
		right	: 'Aliñar á Dereita',
		block	: 'Xustificado'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'Pegar',
		cutError	: 'Os axustes de seguridade do seu navegador non permiten que o editor realice automáticamente as tarefas de corte. Por favor, use o teclado para iso (Ctrl/Cmd+X).',
		copyError	: 'Os axustes de seguridade do seu navegador non permiten que o editor realice automáticamente as tarefas de copia. Por favor, use o teclado para iso (Ctrl/Cmd+C).',
		pasteMsg	: 'Por favor, pegue dentro do seguinte cadro usando o teclado (<STRONG>Ctrl/Cmd+V</STRONG>) e pulse <STRONG>OK</STRONG>.',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Pegar dende Word',
		title			: 'Pegar dende Word',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Pegar como texto plano',
		title	: 'Pegar como texto plano'
	},

	templates :
	{
		button			: 'Plantillas',
		title			: 'Plantillas de Contido',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Por favor, seleccione a plantilla a abrir no editor<br>(o contido actual perderase):',
		emptyListMsg	: '(Non hai plantillas definidas)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'Estilo',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'Formato',
		panelTitle	: 'Formato',

		tag_p		: 'Normal',
		tag_pre		: 'Formateado',
		tag_address	: 'Enderezo',
		tag_h1		: 'Enacabezado 1',
		tag_h2		: 'Encabezado 2',
		tag_h3		: 'Encabezado 3',
		tag_h4		: 'Encabezado 4',
		tag_h5		: 'Encabezado 5',
		tag_h6		: 'Encabezado 6',
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
		label		: 'Tipo',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Tipo'
	},

	fontSize :
	{
		label		: 'Tamaño',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Tamaño'
	},

	colorButton :
	{
		textColorTitle	: 'Cor do Texto',
		bgColorTitle	: 'Cor do Fondo',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automático',
		more			: 'Máis Cores...'
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
		label : 'Propriedades do Documento',
		title : 'Propriedades do Documento',
		design : 'Design', // MISSING
		meta : 'Meta Tags', // MISSING
		chooseColor : 'Choose', // MISSING
		other : '<outro>',
		docTitle :	'Título da Páxina',
		charset : 	'Codificación do Xogo de Caracteres',
		charsetOther : 'Outra Codificación do Xogo de Caracteres',
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
		docType : 'Encabezado do Tipo de Documento',
		docTypeOther : 'Outro Encabezado do Tipo de Documento',
		xhtmlDec : 'Incluir Declaracións XHTML',
		bgColor : 'Cor de Fondo',
		bgImage : 'URL da Imaxe de Fondo',
		bgFixed : 'Fondo Fixo',
		txtColor : 'Cor do Texto',
		margin : 'Marxes da Páxina',
		marginTop : 'Arriba',
		marginLeft : 'Esquerda',
		marginRight : 'Dereita',
		marginBottom : 'Abaixo',
		metaKeywords : 'Palabras Chave de Indexación do Documento (separadas por comas)',
		metaDescription : 'Descripción do Documento',
		metaAuthor : 'Autor',
		metaCopyright : 'Copyright', // MISSING
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
