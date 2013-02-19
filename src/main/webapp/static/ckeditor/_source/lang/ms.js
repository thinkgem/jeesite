/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Malay language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['ms'] =
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
	source			: 'Sumber',
	newPage			: 'Helaian Baru',
	save			: 'Simpan',
	preview			: 'Prebiu',
	cut				: 'Potong',
	copy			: 'Salin',
	paste			: 'Tampal',
	print			: 'Cetak',
	underline		: 'Underline',
	bold			: 'Bold',
	italic			: 'Italic',
	selectAll		: 'Pilih Semua',
	removeFormat	: 'Buang Format',
	strike			: 'Strike Through',
	subscript		: 'Subscript',
	superscript		: 'Superscript',
	horizontalrule	: 'Masukkan Garisan Membujur',
	pagebreak		: 'Insert Page Break for Printing', // MISSING
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'Buang Sambungan',
	undo			: 'Batalkan',
	redo			: 'Ulangkan',

	// Common messages and labels.
	common :
	{
		browseServer	: 'Browse Server',
		url				: 'URL',
		protocol		: 'Protokol',
		upload			: 'Muat Naik',
		uploadSubmit	: 'Hantar ke Server',
		image			: 'Gambar',
		flash			: 'Flash', // MISSING
		form			: 'Borang',
		checkbox		: 'Checkbox',
		radio			: 'Butang Radio',
		textField		: 'Text Field',
		textarea		: 'Textarea',
		hiddenField		: 'Field Tersembunyi',
		button			: 'Butang',
		select			: 'Field Pilihan',
		imageButton		: 'Butang Bergambar',
		notSet			: '<tidak di set>',
		id				: 'Id',
		name			: 'Nama',
		langDir			: 'Arah Tulisan',
		langDirLtr		: 'Kiri ke Kanan (LTR)',
		langDirRtl		: 'Kanan ke Kiri (RTL)',
		langCode		: 'Kod Bahasa',
		longDescr		: 'Butiran Panjang URL',
		cssClass		: 'Kelas-kelas Stylesheet',
		advisoryTitle	: 'Tajuk Makluman',
		cssStyle		: 'Stail',
		ok				: 'OK',
		cancel			: 'Batal',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'Advanced',
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
		width			: 'Lebar',
		height			: 'Tinggi',
		align			: 'Jajaran',
		alignLeft		: 'Kiri',
		alignRight		: 'Kanan',
		alignCenter		: 'Tengah',
		alignTop		: 'Atas',
		alignMiddle		: 'Pertengahan',
		alignBottom		: 'Bawah',
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
		toolbar		: 'Masukkan Huruf Istimewa',
		title		: 'Sila pilih huruf istimewa',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'Masukkan/Sunting Sambungan',
		other 		: '<lain>',
		menu		: 'Sunting Sambungan',
		title		: 'Sambungan',
		info		: 'Butiran Sambungan',
		target		: 'Sasaran',
		upload		: 'Muat Naik',
		advanced	: 'Advanced',
		type		: 'Jenis Sambungan',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'Pautan dalam muka surat ini',
		toEmail		: 'E-Mail',
		targetFrame		: '<bingkai>',
		targetPopup		: '<tetingkap popup>',
		targetFrameName	: 'Nama Bingkai Sasaran',
		targetPopupName	: 'Nama Tetingkap Popup',
		popupFeatures	: 'Ciri Tetingkap Popup',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'Bar Status',
		popupLocationBar: 'Bar Lokasi',
		popupToolbar	: 'Toolbar',
		popupMenuBar	: 'Bar Menu',
		popupFullScreen	: 'Skrin Penuh (IE)',
		popupScrollBars	: 'Bar-bar skrol',
		popupDependent	: 'Bergantungan (Netscape)',
		popupLeft		: 'Posisi Kiri',
		popupTop		: 'Posisi Atas',
		id				: 'Id', // MISSING
		langDir			: 'Arah Tulisan',
		langDirLTR		: 'Kiri ke Kanan (LTR)',
		langDirRTL		: 'Kanan ke Kiri (RTL)',
		acccessKey		: 'Kunci Akses',
		name			: 'Nama',
		langCode			: 'Arah Tulisan',
		tabIndex			: 'Indeks Tab ',
		advisoryTitle		: 'Tajuk Makluman',
		advisoryContentType	: 'Jenis Kandungan Makluman',
		cssClasses		: 'Kelas-kelas Stylesheet',
		charset			: 'Linked Resource Charset',
		styles			: 'Stail',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'Sila pilih pautan',
		anchorName		: 'dengan menggunakan nama pautan',
		anchorId			: 'dengan menggunakan ID elemen',
		emailAddress		: 'Alamat E-Mail',
		emailSubject		: 'Subjek Mesej',
		emailBody		: 'Isi Kandungan Mesej',
		noAnchors		: '(Tiada pautan terdapat dalam dokumen ini)',
		noUrl			: 'Sila taip sambungan URL',
		noEmail			: 'Sila taip alamat e-mail'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'Masukkan/Sunting Pautan',
		menu		: 'Ciri-ciri Pautan',
		title		: 'Ciri-ciri Pautan',
		name		: 'Nama Pautan',
		errorName	: 'Sila taip nama pautan',
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
		find				: 'Cari',
		replace				: 'Ganti',
		findWhat			: 'Perkataan yang dicari:',
		replaceWith			: 'Diganti dengan:',
		notFoundMsg			: 'Text yang dicari tidak dijumpai.',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'Padanan case huruf',
		matchWord			: 'Padana Keseluruhan perkataan',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'Ganti semua',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'Jadual',
		title		: 'Ciri-ciri Jadual',
		menu		: 'Ciri-ciri Jadual',
		deleteTable	: 'Delete Table', // MISSING
		rows		: 'Barisan',
		columns		: 'Jaluran',
		border		: 'Saiz Border',
		widthPx		: 'piksel-piksel',
		widthPc		: 'peratus',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'Ruangan Antara Sel',
		cellPad		: 'Tambahan Ruang Sel',
		caption		: 'Keterangan',
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
			deleteCell		: 'Buangkan Sel-sel',
			merge			: 'Cantumkan Sel-sel',
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
			deleteRow		: 'Buangkan Baris'
		},

		column :
		{
			menu			: 'Column', // MISSING
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'Buangkan Lajur'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'Ciri-ciri Butang',
		text		: 'Teks (Nilai)',
		type		: 'Jenis',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'Ciri-ciri Checkbox',
		radioTitle	: 'Ciri-ciri Butang Radio',
		value		: 'Nilai',
		selected	: 'Dipilih'
	},

	// Form Dialog.
	form :
	{
		title		: 'Ciri-ciri Borang',
		menu		: 'Ciri-ciri Borang',
		action		: 'Tindakan borang',
		method		: 'Cara borang dihantar',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'Ciri-ciri Selection Field',
		selectInfo	: 'Select Info', // MISSING
		opAvail		: 'Pilihan sediada',
		value		: 'Nilai',
		size		: 'Saiz',
		lines		: 'garisan',
		chkMulti	: 'Benarkan pilihan pelbagai',
		opText		: 'Teks',
		opValue		: 'Nilai',
		btnAdd		: 'Tambah Pilihan',
		btnModify	: 'Ubah Pilihan',
		btnUp		: 'Naik ke atas',
		btnDown		: 'Turun ke bawah',
		btnSetValue : 'Set sebagai nilai terpilih',
		btnDelete	: 'Padam'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'Ciri-ciri Textarea',
		cols		: 'Lajur',
		rows		: 'Baris'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'Ciri-ciri Text Field',
		name		: 'Nama',
		value		: 'Nilai',
		charWidth	: 'Lebar isian',
		maxChars	: 'Isian Maksimum',
		type		: 'Jenis',
		typeText	: 'Teks',
		typePass	: 'Kata Laluan'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'Ciri-ciri Field Tersembunyi',
		name	: 'Nama',
		value	: 'Nilai'
	},

	// Image Dialog.
	image :
	{
		title		: 'Ciri-ciri Imej',
		titleButton	: 'Ciri-ciri Butang Bergambar',
		menu		: 'Ciri-ciri Imej',
		infoTab		: 'Info Imej',
		btnUpload	: 'Hantar ke Server',
		upload		: 'Muat Naik',
		alt			: 'Text Alternatif',
		lockRatio	: 'Tetapkan Nisbah',
		resetSize	: 'Saiz Set Semula',
		border		: 'Border',
		hSpace		: 'Ruang Melintang',
		vSpace		: 'Ruang Menegak',
		alertUrl	: 'Sila taip URL untuk fail gambar',
		linkTab		: 'Sambungan',
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
		properties		: 'Flash Properties', // MISSING
		propertiesTab	: 'Properties', // MISSING
		title			: 'Flash Properties', // MISSING
		chkPlay			: 'Auto Play', // MISSING
		chkLoop			: 'Loop', // MISSING
		chkMenu			: 'Enable Flash Menu', // MISSING
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'Scale', // MISSING
		scaleAll		: 'Show all', // MISSING
		scaleNoBorder	: 'No Border', // MISSING
		scaleFit		: 'Exact Fit', // MISSING
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Bawah Mutlak',
		alignAbsMiddle	: 'Pertengahan Mutlak',
		alignBaseline	: 'Garis Dasar',
		alignTextTop	: 'Atas Text',
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
		bgcolor			: 'Warna Latarbelakang',
		hSpace			: 'Ruang Melintang',
		vSpace			: 'Ruang Menegak',
		validateSrc		: 'Sila taip sambungan URL',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'Semak Ejaan',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'Tidak terdapat didalam kamus',
		changeTo		: 'Tukarkan kepada',
		btnIgnore		: 'Biar',
		btnIgnoreAll	: 'Biarkan semua',
		btnReplace		: 'Ganti',
		btnReplaceAll	: 'Gantikan Semua',
		btnUndo			: 'Batalkan',
		noSuggestions	: '- Tiada cadangan -',
		progress		: 'Pemeriksaan ejaan sedang diproses...',
		noMispell		: 'Pemeriksaan ejaan siap: Tiada salah ejaan',
		noChanges		: 'Pemeriksaan ejaan siap: Tiada perkataan diubah',
		oneChange		: 'Pemeriksaan ejaan siap: Satu perkataan telah diubah',
		manyChanges		: 'Pemeriksaan ejaan siap: %1 perkataan diubah',
		ieSpellDownload	: 'Pemeriksa ejaan tidak dipasang. Adakah anda mahu muat turun sekarang?'
	},

	smiley :
	{
		toolbar	: 'Smiley',
		title	: 'Masukkan Smiley',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'Senarai bernombor',
	bulletedlist	: 'Senarai tidak bernombor',
	indent			: 'Tambahkan Inden',
	outdent			: 'Kurangkan Inden',

	justify :
	{
		left	: 'Jajaran Kiri',
		center	: 'Jajaran Tengah',
		right	: 'Jajaran Kanan',
		block	: 'Jajaran Blok'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'Tampal',
		cutError	: 'Keselamatan perisian browser anda tidak membenarkan operasi suntingan text/imej. Sila gunakan papan kekunci (Ctrl/Cmd+X).',
		copyError	: 'Keselamatan perisian browser anda tidak membenarkan operasi salinan text/imej. Sila gunakan papan kekunci (Ctrl/Cmd+C).',
		pasteMsg	: 'Please paste inside the following box using the keyboard (<strong>Ctrl/Cmd+V</strong>) and hit OK', // MISSING
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'Tampal dari Word',
		title			: 'Tampal dari Word',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'Tampal sebagai text biasa',
		title	: 'Tampal sebagai text biasa'
	},

	templates :
	{
		button			: 'Templat',
		title			: 'Templat Kandungan',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'Sila pilih templat untuk dibuka oleh editor<br>(kandungan sebenar akan hilang):',
		emptyListMsg	: '(Tiada Templat Disimpan)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'Stail',
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
		tag_pre		: 'Telah Diformat',
		tag_address	: 'Alamat',
		tag_h1		: 'Heading 1',
		tag_h2		: 'Heading 2',
		tag_h3		: 'Heading 3',
		tag_h4		: 'Heading 4',
		tag_h5		: 'Heading 5',
		tag_h6		: 'Heading 6',
		tag_div		: 'Perenggan (DIV)'
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
		label		: 'Saiz',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'Saiz'
	},

	colorButton :
	{
		textColorTitle	: 'Warna Text',
		bgColorTitle	: 'Warna Latarbelakang',
		panelTitle		: 'Colors', // MISSING
		auto			: 'Otomatik',
		more			: 'Warna lain-lain...'
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
		label : 'Ciri-ciri dokumen',
		title : 'Ciri-ciri dokumen',
		design : 'Design', // MISSING
		meta : 'Data Meta',
		chooseColor : 'Choose', // MISSING
		other : '<lain>',
		docTitle :	'Tajuk Muka Surat',
		charset : 	'Enkod Set Huruf',
		charsetOther : 'Enkod Set Huruf yang Lain',
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
		docType : 'Jenis Kepala Dokumen',
		docTypeOther : 'Jenis Kepala Dokumen yang Lain',
		xhtmlDec : 'Masukkan pemula kod XHTML',
		bgColor : 'Warna Latarbelakang',
		bgImage : 'URL Gambar Latarbelakang',
		bgFixed : 'Imej Latarbelakang tanpa Skrol',
		txtColor : 'Warna Text',
		margin : 'Margin Muka Surat',
		marginTop : 'Atas',
		marginLeft : 'Kiri',
		marginRight : 'Kanan',
		marginBottom : 'Bawah',
		metaKeywords : 'Kata Kunci Indeks Dokumen (dipisahkan oleh koma)',
		metaDescription : 'Keterangan Dokumen',
		metaAuthor : 'Penulis',
		metaCopyright : 'Hakcipta',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
