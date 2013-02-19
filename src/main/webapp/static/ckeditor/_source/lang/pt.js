/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Portuguese language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['pt'] =
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
	toolbars	: 'Editor de barras de ferramentas',
	editor		: 'Editor de texto enriquecido',

	// Toolbar buttons without dialogs.
	source			: 'Fonte',
	newPage			: 'Nova Página',
	save			: 'Guardar',
	preview			: 'Pré-visualizar',
	cut				: 'Cortar',
	copy			: 'Copiar',
	paste			: 'Colar',
	print			: 'Imprimir',
	underline		: 'Sublinhado',
	bold			: 'Negrito',
	italic			: 'Itálico',
	selectAll		: 'Seleccionar Tudo',
	removeFormat	: 'Eliminar Formato',
	strike			: 'Rasurado',
	subscript		: 'Superior à Linha',
	superscript		: 'Inferior à Linha',
	horizontalrule	: 'Inserir Linha Horizontal',
	pagebreak		: 'Inserir Quebra de Página',
	pagebreakAlt		: 'Quebra de página',
	unlink			: 'Eliminar Hiperligação',
	undo			: 'Anular',
	redo			: 'Repetir',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Navegar no Servidor',
		url				: 'URL',
		protocol		: 'Protocolo',
		upload			: 'Carregar',
		uploadSubmit	: 'Enviar para o Servidor',
		image			: 'Imagem',
		flash			: 'Flash',
		form			: 'Formulário',
		checkbox		: 'Caixa de Verificação',
		radio			: 'Botão de Opção',
		textField		: 'Campo de Texto',
		textarea		: 'Área de Texto',
		hiddenField		: 'Campo Escondido',
		button			: 'Botão',
		select			: 'Caixa de Combinação',
		imageButton		: 'Botão de Imagem',
		notSet			: '<Não definido>',
		id				: 'Id',
		name			: 'Nome',
		langDir			: 'Orientação de idioma',
		langDirLtr		: 'Esquerda à Direita (LTR)',
		langDirRtl		: 'Direita a Esquerda (RTL)',
		langCode		: 'Código de Idioma',
		longDescr		: 'Descrição Completa do URL',
		cssClass		: 'Classes de Estilo de Folhas Classes',
		advisoryTitle	: 'Título',
		cssStyle		: 'Estilo',
		ok				: 'OK',
		cancel			: 'Cancelar',
		close			: 'Fechar',
		preview			: 'Pré-visualização',
		generalTab		: 'Geral',
		advancedTab		: 'Avançado',
		validateNumberFailed : 'This value is not a number.', // MISSING
		confirmNewPage	: 'Any unsaved changes to this content will be lost. Are you sure you want to load new page?', // MISSING
		confirmCancel	: 'Some of the options have been changed. Are you sure to close the dialog?', // MISSING
		options			: 'Opções',
		target			: 'Alvo',
		targetNew		: 'Nova Janela (_blank)',
		targetTop		: 'Janela superior (_top)',
		targetSelf		: 'Mesma janela (_self)',
		targetParent	: 'Parent Window (_parent)', // MISSING
		langDirLTR		: 'Left to Right (LTR)', // MISSING
		langDirRTL		: 'Direita para Esquerda (RTL)',
		styles			: 'Estilo',
		cssClasses		: 'Stylesheet Classes', // MISSING
		width			: 'Largura',
		height			: 'Altura',
		align			: 'Alinhamento',
		alignLeft		: 'Esquerda',
		alignRight		: 'Direita',
		alignCenter		: 'Centrado',
		alignTop		: 'Topo',
		alignMiddle		: 'Centro',
		alignBottom		: 'Fundo',
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
		options : 'Menu de opções de contexto'
	},

	// Special char dialog.
	specialChar		:
	{
		toolbar		: 'Inserir Caracter Especial',
		title		: 'Seleccione um caracter especial',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Inserir/Editar Hiperligação',
		other 		: '<outro>',
		menu		: 'Editar Hiperligação',
		title		: 'Hiperligação',
		info		: 'Informação de Hiperligação',
		target		: 'Destino',
		upload		: 'Carregar',
		advanced	: 'Avançado',
		type		: 'Tipo de Hiperligação',
		toUrl		: 'URL',
		toAnchor	: 'Referência a esta página',
		toEmail		: 'E-Mail',
		targetFrame		: '<Frame>',
		targetPopup		: '<Janela de popup>',
		targetFrameName	: 'Nome do Frame Destino',
		targetPopupName	: 'Nome da Janela de Popup',
		popupFeatures	: 'Características de Janela de Popup',
		popupResizable	: 'Redimensionável',
		popupStatusBar	: 'Barra de Estado',
		popupLocationBar: 'Barra de localização',
		popupToolbar	: 'Barra de Ferramentas',
		popupMenuBar	: 'Barra de Menu',
		popupFullScreen	: 'Janela Completa (IE)',
		popupScrollBars	: 'Barras de deslocamento',
		popupDependent	: 'Dependente (Netscape)',
		popupLeft		: 'Posição Esquerda',
		popupTop		: 'Posição Direita',
		id				: 'ID',
		langDir			: 'Orientação de idioma',
		langDirLTR		: 'Esquerda à Direita (LTR)',
		langDirRTL		: 'Direita a Esquerda (RTL)',
		acccessKey		: 'Chave de Acesso',
		name			: 'Nome',
		langCode			: 'Orientação de idioma',
		tabIndex			: 'Índice de Tubulação',
		advisoryTitle		: 'Título',
		advisoryContentType	: 'Tipo de Conteúdo',
		cssClasses		: 'Classes de Estilo de Folhas Classes',
		charset			: 'Fonte de caracteres vinculado',
		styles			: 'Estilo',
		rel			: 'Relação',
		selectAnchor		: 'Seleccionar una referência',
		anchorName		: 'Por Nome de Referência',
		anchorId			: 'Por ID de elemento',
		emailAddress		: 'Endereço de E-Mail',
		emailSubject		: 'Título de Mensagem',
		emailBody		: 'Corpo da Mensagem',
		noAnchors		: '(Não há referências disponíveis no documento)',
		noUrl			: 'Por favor introduza a hiperligação URL',
		noEmail			: 'Por favor introduza o endereço de e-mail'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: ' Inserir/Editar Âncora',
		menu		: 'Propriedades da Âncora',
		title		: 'Propriedades da Âncora',
		name		: 'Nome da Âncora',
		errorName	: 'Por favor, introduza o nome da âncora',
		remove		: 'Remove Anchor' // MISSING
	},

	// List style dialog
	list:
	{
		numberedTitle		: 'Numbered List Properties', // MISSING
		bulletedTitle		: 'Bulleted List Properties', // MISSING
		type				: 'Tipo',
		start				: 'Iniciar',
		validateStartNumber				:'List start number must be a whole number.', // MISSING
		circle				: 'Círculo',
		disc				: 'Disco',
		square				: 'Quadrado',
		none				: 'Nenhum',
		notset				: '<not set>',
		armenian			: 'Numeração armênia',
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
		findWhat			: 'Texto a Procurar:',
		replaceWith			: 'Substituir por:',
		notFoundMsg			: 'O texto especificado não foi encontrado.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Maiúsculas/Minúsculas',
		matchWord			: 'Coincidir com toda a palavra',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Substituir Tudo',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Tabela',
		title		: 'Propriedades da Tabela',
		menu		: 'Propriedades da Tabela',
		deleteTable	: 'Eliminar Tabela',
		rows		: 'Linhas',
		columns		: 'Colunas',
		border		: 'Tamanho do Limite',
		widthPx		: 'pixeis',
		widthPc		: 'percentagem',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Esp. e/células',
		cellPad		: 'Esp. interior',
		caption		: 'Título',
		summary		: 'Sumário',
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
			menu			: 'Célula',
			insertBefore	: 'Insert Cell Before', // MISSING
			insertAfter		: 'Insert Cell After', // MISSING
			deleteCell		: 'Eliminar Célula',
			merge			: 'Unir Células',
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
			menu			: 'Linha',
			insertBefore	: 'Insert Row Before', // MISSING
			insertAfter		: 'Insert Row After', // MISSING
			deleteRow		: 'Eliminar Linhas'
		},

		column :
		{
			menu			: 'Coluna',
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Eliminar Coluna'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Propriedades do Botão',
		text		: 'Texto (Valor)',
		type		: 'Tipo',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Propriedades da Caixa de Verificação',
		radioTitle	: 'Propriedades do Botão de Opção',
		value		: 'Valor',
		selected	: 'Seleccionado'
	},

	// Form Dialog.
	form :
	{
		title		: 'Propriedades do Formulário',
		menu		: 'Propriedades do Formulário',
		action		: 'Acção',
		method		: 'Método',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Propriedades da Caixa de Combinação',
		selectInfo	: 'Informação',
		opAvail		: 'Opções Possíveis',
		value		: 'Valor',
		size		: 'Tamanho',
		lines		: 'linhas',
		chkMulti	: 'Permitir selecções múltiplas',
		opText		: 'Texto',
		opValue		: 'Valor',
		btnAdd		: 'Adicionar',
		btnModify	: 'Modificar',
		btnUp		: 'Para cima',
		btnDown		: 'Para baixo',
		btnSetValue : 'Definir um valor por defeito',
		btnDelete	: 'Apagar'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Propriedades da Área de Texto',
		cols		: 'Colunas',
		rows		: 'Linhas'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Propriedades do Campo de Texto',
		name		: 'Nome',
		value		: 'Valor',
		charWidth	: 'Tamanho do caracter',
		maxChars	: 'Nr. Máximo de Caracteres',
		type		: 'Tipo',
		typeText	: 'Texto',
		typePass	: 'Palavra-chave'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Propriedades do Campo Escondido',
		name	: 'Nome',
		value	: 'Valor'
	},

	// Image Dialog.
	image :
	{
		title		: 'Propriedades da Imagem',
		titleButton	: 'Propriedades do Botão de imagens',
		menu		: 'Propriedades da Imagem',
		infoTab		: 'Informação da Imagem',
		btnUpload	: 'Enviar para o Servidor',
		upload		: 'Carregar',
		alt			: 'Texto Alternativo',
		lockRatio	: 'Proporcional',
		resetSize	: 'Tamanho Original',
		border		: 'Limite',
		hSpace		: 'Esp.Horiz',
		vSpace		: 'Esp.Vert',
		alertUrl	: 'Por favor introduza o URL da imagem',
		linkTab		: 'Hiperligação',
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
		properties		: 'Propriedades do Flash',
		propertiesTab	: 'Properties', // MISSING
		title			: 'Propriedades do Flash',
		chkPlay			: 'Reproduzir automaticamente',
		chkLoop			: 'Loop',
		chkMenu			: 'Permitir Menu do Flash',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Escala',
		scaleAll		: 'Mostrar tudo',
		scaleNoBorder	: 'Sem Limites',
		scaleFit		: 'Tamanho Exacto',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs inferior',
		alignAbsMiddle	: 'Abs centro',
		alignBaseline	: 'Linha de base',
		alignTextTop	: 'Topo do texto',
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
		bgcolor			: 'Cor de Fundo',
		hSpace			: 'Esp.Horiz',
		vSpace			: 'Esp.Vert',
		validateSrc		: 'Por favor introduza a hiperligação URL',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Verificação Ortográfica',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Não está num directório',
		changeTo		: 'Mudar para',
		btnIgnore		: 'Ignorar',
		btnIgnoreAll	: 'Ignorar Tudo',
		btnReplace		: 'Substituir',
		btnReplaceAll	: 'Substituir Tudo',
		btnUndo			: 'Anular',
		noSuggestions	: '- Sem sugestões -',
		progress		: 'Verificação ortográfica em progresso…',
		noMispell		: 'Verificação ortográfica completa: não foram encontrados erros',
		noChanges		: 'Verificação ortográfica completa: não houve alteração de palavras',
		oneChange		: 'Verificação ortográfica completa: uma palavra alterada',
		manyChanges		: 'Verificação ortográfica completa: %1 palavras alteradas',
		ieSpellDownload	: ' Verificação ortográfica não instalada. Quer descarregar agora?'
	},

	smiley :
	{
		toolbar	: 'Emoticons',
		title	: 'Inserir um Emoticon',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Numeração',
	bulletedlist	: 'Marcas',
	indent			: 'Aumentar Avanço',
	outdent			: 'Diminuir Avanço',

	justify :
	{
		left	: 'Alinhar à Esquerda',
		center	: 'Alinhar ao Centro',
		right	: 'Alinhar à Direita',
		block	: 'Justificado'
	},

	blockquote : 'Bloco de citação',

	clipboard :
	{
		title		: 'Colar',
		cutError	: 'A configuração de segurança do navegador não permite a execução automática de operações de cortar. Por favor use o teclado (Ctrl/Cmd+X).',
		copyError	: 'A configuração de segurança do navegador não permite a execução automática de operações de copiar. Por favor use o teclado (Ctrl/Cmd+C).',
		pasteMsg	: 'Por favor, cole dentro da seguinte caixa usando o teclado (<STRONG>Ctrl/Cmd+V</STRONG>) e prima <STRONG>OK</STRONG>.',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Colar do Word',
		title			: 'Colar do Word',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Colar como Texto Simples',
		title	: 'Colar como Texto Simples'
	},

	templates :
	{
		button			: 'Modelos',
		title			: 'Modelo de Conteúdo',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Por favor, seleccione o modelo a abrir no editor<br>(o conteúdo actual será perdido):',
		emptyListMsg	: '(Sem modelos definidos)'
	},

	showBlocks : 'Exibir blocos',

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
		tag_pre		: 'Formatado',
		tag_address	: 'Endereço',
		tag_h1		: 'Título 1',
		tag_h2		: 'Título 2',
		tag_h3		: 'Título 3',
		tag_h4		: 'Título 4',
		tag_h5		: 'Título 5',
		tag_h6		: 'Título 6',
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
		label		: 'Tipo de Letra',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'Tipo de Letra'
	},

	fontSize :
	{
		label		: 'Tamanho',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Tamanho'
	},

	colorButton :
	{
		textColorTitle	: 'Cor do Texto',
		bgColorTitle	: 'Cor de Fundo',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Automático',
		more			: 'Mais Cores...'
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

	maximize : 'Maximizar',
	minimize : 'Minimizar',

	fakeobjects :
	{
		anchor		: 'Anchor', // MISSING
		flash		: 'Flash Animation', // MISSING
		iframe		: 'IFrame', // MISSING
		hiddenfield	: 'Hidden Field', // MISSING
		unknown		: 'Unknown Object' // MISSING
	},

	resize : 'Arraste para redimensionar',

	colordialog :
	{
		title		: 'Select color', // MISSING
		options	:	'Color Options', // MISSING
		highlight	: 'Highlight', // MISSING
		selected	: 'Selected Color', // MISSING
		clear		: 'Clear' // MISSING
	},

	toolbarCollapse	: 'Colapsar Barra',
	toolbarExpand	: 'Expandir Barra',

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
		meta : 'Meta Data',
		chooseColor : 'Choose', // MISSING
		other : '<outro>',
		docTitle :	'Título da Página',
		charset : 	'Codificação de Caracteres',
		charsetOther : 'Outra Codificação de Caracteres',
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
		docType : 'Tipo de Cabeçalho do Documento',
		docTypeOther : 'Outro Tipo de Cabeçalho do Documento',
		xhtmlDec : 'Incluir Declarações XHTML',
		bgColor : 'Cor de Fundo',
		bgImage : 'Caminho para a Imagem de Fundo',
		bgFixed : 'Fundo Fixo',
		txtColor : 'Cor do Texto',
		margin : 'Margem das Páginas',
		marginTop : 'Topo',
		marginLeft : 'Esquerda',
		marginRight : 'Direita',
		marginBottom : 'Fundo',
		metaKeywords : 'Palavras de Indexação do Documento (separadas por virgula)',
		metaDescription : 'Descrição do Documento',
		metaAuthor : 'Autor',
		metaCopyright : 'Direitos de Autor',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
