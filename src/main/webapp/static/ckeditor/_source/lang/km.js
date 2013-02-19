/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Khmer language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['km'] =
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
	source			: 'កូត',
	newPage			: 'ទំព័រថ្មី',
	save			: 'រក្សាទុក',
	preview			: 'មើលសាកល្បង',
	cut				: 'កាត់យក',
	copy			: 'ចំលងយក',
	paste			: 'ចំលងដាក់',
	print			: 'បោះពុម្ភ',
	underline		: 'ដិតបន្ទាត់ពីក្រោមអក្សរ',
	bold			: 'អក្សរដិតធំ',
	italic			: 'អក្សរផ្តេក',
	selectAll		: 'ជ្រើសរើសទាំងអស់',
	removeFormat	: 'លប់ចោល ការរចនា',
	strike			: 'ដិតបន្ទាត់ពាក់កណ្តាលអក្សរ',
	subscript		: 'អក្សរតូចក្រោម',
	superscript		: 'អក្សរតូចលើ',
	horizontalrule	: 'បន្ថែមបន្ទាត់ផ្តេក',
	pagebreak		: 'បន្ថែម ការផ្តាច់ទំព័រ',
	pagebreakAlt		: 'Page Break', // MISSING
	unlink			: 'លប់ឈ្នាប់',
	undo			: 'សារឡើងវិញ',
	redo			: 'ធ្វើឡើងវិញ',

	// Common messages and labels.
	common :
	{
		browseServer	: 'មើល',
		url				: 'URL',
		protocol		: 'ប្រូតូកូល',
		upload			: 'ទាញយក',
		uploadSubmit	: 'បញ្ជូនទៅកាន់ម៉ាស៊ីនផ្តល់សេវា',
		image			: 'រូបភាព',
		flash			: 'Flash',
		form			: 'បែបបទ',
		checkbox		: 'ប្រអប់ជ្រើសរើស',
		radio			: 'ប៉ូតុនរង្វង់មូល',
		textField		: 'ជួរសរសេរអត្ថបទ',
		textarea		: 'តំបន់សរសេរអត្ថបទ',
		hiddenField		: 'ជួរលាក់',
		button			: 'ប៉ូតុន',
		select			: 'ជួរជ្រើសរើស',
		imageButton		: 'ប៉ូតុនរូបភាព',
		notSet			: '<មិនមែន>',
		id				: 'Id',
		name			: 'ឈ្មោះ',
		langDir			: 'ទិសដៅភាសា',
		langDirLtr		: 'ពីឆ្វេងទៅស្តាំ(LTR)',
		langDirRtl		: 'ពីស្តាំទៅឆ្វេង(RTL)',
		langCode		: 'លេខកូតភាសា',
		longDescr		: 'អធិប្បាយ URL វែង',
		cssClass		: 'Stylesheet Classes',
		advisoryTitle	: 'ចំណងជើង ប្រឹក្សា',
		cssStyle		: 'ម៉ូត',
		ok				: 'យល់ព្រម',
		cancel			: 'មិនយល់ព្រម',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'General', // MISSING
		advancedTab		: 'កំរិតខ្ពស់',
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
		width			: 'ទទឹង',
		height			: 'កំពស់',
		align			: 'កំណត់ទីតាំង',
		alignLeft		: 'ខាងឆ្វង',
		alignRight		: 'ខាងស្តាំ',
		alignCenter		: 'កណ្តាល',
		alignTop		: 'ខាងលើ',
		alignMiddle		: 'កណ្តាល',
		alignBottom		: 'ខាងក្រោម',
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
		toolbar		: 'បន្ថែមអក្សរពិសេស',
		title		: 'តូអក្សរពិសេស',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'បន្ថែម/កែប្រែ ឈ្នាប់',
		other 		: '<other>', // MISSING
		menu		: 'កែប្រែឈ្នាប់',
		title		: 'ឈ្នាប់',
		info		: 'ពត៌មានអំពីឈ្នាប់',
		target		: 'គោលដៅ',
		upload		: 'ទាញយក',
		advanced	: 'កំរិតខ្ពស់',
		type		: 'ប្រភេទឈ្នាប់',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'យុថ្កានៅក្នុងទំព័រនេះ',
		toEmail		: 'អ៊ីមែល',
		targetFrame		: '<ហ្វ្រេម>',
		targetPopup		: '<វីនដូវ លោត>',
		targetFrameName	: 'ឈ្មោះហ្រ្វេមដែលជាគោលដៅ',
		targetPopupName	: 'ឈ្មោះវីនដូវលោត',
		popupFeatures	: 'លក្ខណះរបស់វីនដូលលោត',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'របា ពត៌មាន',
		popupLocationBar: 'របា ទីតាំង',
		popupToolbar	: 'របា ឩបករណ៍',
		popupMenuBar	: 'របា មឺនុយ',
		popupFullScreen	: 'អេក្រុងពេញ(IE)',
		popupScrollBars	: 'របា ទាញ',
		popupDependent	: 'អាស្រ័យលើ (Netscape)',
		popupLeft		: 'ទីតាំងខាងឆ្វេង',
		popupTop		: 'ទីតាំងខាងលើ',
		id				: 'Id', // MISSING
		langDir			: 'ទិសដៅភាសា',
		langDirLTR		: 'ពីឆ្វេងទៅស្តាំ(LTR)',
		langDirRTL		: 'ពីស្តាំទៅឆ្វេង(RTL)',
		acccessKey		: 'ឃី សំរាប់ចូល',
		name			: 'ឈ្មោះ',
		langCode			: 'ទិសដៅភាសា',
		tabIndex			: 'លេខ Tab',
		advisoryTitle		: 'ចំណងជើង ប្រឹក្សា',
		advisoryContentType	: 'ប្រភេទអត្ថបទ ប្រឹក្សា',
		cssClasses		: 'Stylesheet Classes',
		charset			: 'លេខកូតអក្សររបស់ឈ្នាប់',
		styles			: 'ម៉ូត',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'ជ្រើសរើសយុថ្កា',
		anchorName		: 'តាមឈ្មោះរបស់យុថ្កា',
		anchorId			: 'តាម Id',
		emailAddress		: 'អ៊ីមែល',
		emailSubject		: 'ចំណងជើងអត្ថបទ',
		emailBody		: 'អត្ថបទ',
		noAnchors		: '(No anchors available in the document)', // MISSING
		noUrl			: 'សូមសរសេរ អាស័យដ្ឋាន URL',
		noEmail			: 'សូមសរសេរ អាស័យដ្ឋាន អ៊ីមែល'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'បន្ថែម/កែប្រែ យុថ្កា',
		menu		: 'ការកំណត់យុថ្កា',
		title		: 'ការកំណត់យុថ្កា',
		name		: 'ឈ្មោះយុទ្ធថ្កា',
		errorName	: 'សូមសរសេរ ឈ្មោះយុទ្ធថ្កា',
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
		find				: 'ស្វែងរក',
		replace				: 'ជំនួស',
		findWhat			: 'ស្វែងរកអ្វី:',
		replaceWith			: 'ជំនួសជាមួយ:',
		notFoundMsg			: 'ពាក្យនេះ រកមិនឃើញទេ ។',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'ករណ៉ត្រូវរក',
		matchWord			: 'ត្រូវពាក្យទាំងអស់',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'ជំនួសទាំងអស់',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'តារាង',
		title		: 'ការកំណត់ តារាង',
		menu		: 'ការកំណត់ តារាង',
		deleteTable	: 'លប់តារាង',
		rows		: 'ជួរផ្តេក',
		columns		: 'ជួរឈរ',
		border		: 'ទំហំស៊ុម',
		widthPx		: 'ភីកសែល',
		widthPc		: 'ភាគរយ',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'គំលាតសែល',
		cellPad		: 'គែមសែល',
		caption		: 'ចំណងជើង',
		summary		: 'សេចក្តីសង្ខេប',
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
			deleteCell		: 'លប់សែល',
			merge			: 'បញ្ជូលសែល',
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
			deleteRow		: 'លប់ជួរផ្តេក'
		},

		column :
		{
			menu			: 'Column', // MISSING
			insertBefore	: 'Insert Column Before', // MISSING
			insertAfter		: 'Insert Column After', // MISSING
			deleteColumn	: 'លប់ជួរឈរ'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'ការកំណត់ ប៉ូតុន',
		text		: 'អត្ថបទ(តំលៃ)',
		type		: 'ប្រភេទ',
		typeBtn		: 'Button', // MISSING
		typeSbm		: 'Submit', // MISSING
		typeRst		: 'Reset' // MISSING
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'ការកំណត់ប្រអប់ជ្រើសរើស',
		radioTitle	: 'ការកំណត់ប៉ូតុនរង្វង់',
		value		: 'តំលៃ',
		selected	: 'បានជ្រើសរើស'
	},

	// Form Dialog.
	form :
	{
		title		: 'ការកំណត់បែបបទ',
		menu		: 'ការកំណត់បែបបទ',
		action		: 'សកម្មភាព',
		method		: 'វិធី',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'ការកំណត់ជួរជ្រើសរើស',
		selectInfo	: 'ពត៌មាន',
		opAvail		: 'ការកំណត់ជ្រើសរើស ដែលអាចកំណត់បាន',
		value		: 'តំលៃ',
		size		: 'ទំហំ',
		lines		: 'បន្ទាត់',
		chkMulti	: 'អនុញ្ញាតអោយជ្រើសរើសច្រើន',
		opText		: 'ពាក្យ',
		opValue		: 'តំលៃ',
		btnAdd		: 'បន្ថែម',
		btnModify	: 'ផ្លាស់ប្តូរ',
		btnUp		: 'លើ',
		btnDown		: 'ក្រោម',
		btnSetValue : 'Set as selected value', // MISSING
		btnDelete	: 'លប់'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'ការកំណត់កន្លែងសរសេរអត្ថបទ',
		cols		: 'ជូរឈរ',
		rows		: 'ជូរផ្តេក'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'ការកំណត់ជួរអត្ថបទ',
		name		: 'ឈ្មោះ',
		value		: 'តំលៃ',
		charWidth	: 'ទទឹង អក្សរ',
		maxChars	: 'អក្សរអតិបរិមា',
		type		: 'ប្រភេទ',
		typeText	: 'ពាក្យ',
		typePass	: 'ពាក្យសំងាត់'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'ការកំណត់ជួរលាក់',
		name	: 'ឈ្មោះ',
		value	: 'តំលៃ'
	},

	// Image Dialog.
	image :
	{
		title		: 'ការកំណត់រូបភាព',
		titleButton	: 'ការកំណត់ប៉ូតុនរូបភាព',
		menu		: 'ការកំណត់រូបភាព',
		infoTab		: 'ពត៌មានអំពីរូបភាព',
		btnUpload	: 'បញ្ជូនទៅកាន់ម៉ាស៊ីនផ្តល់សេវា',
		upload		: 'ទាញយក',
		alt			: 'អត្ថបទជំនួស',
		lockRatio	: 'អត្រាឡុក',
		resetSize	: 'កំណត់ទំហំឡើងវិញ',
		border		: 'ស៊ុម',
		hSpace		: 'គំលាតទទឹង',
		vSpace		: 'គំលាតបណ្តោយ',
		alertUrl	: 'សូមសរសេរងាស័យដ្ឋានរបស់រូបភាព',
		linkTab		: 'ឈ្នាប់',
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
		properties		: 'ការកំណត់ Flash',
		propertiesTab	: 'Properties', // MISSING
		title			: 'ការកំណត់ Flash',
		chkPlay			: 'លេងដោយស្វ័យប្រវត្ត',
		chkLoop			: 'ចំនួនដង',
		chkMenu			: 'បង្ហាញ មឺនុយរបស់ Flash',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'ទំហំ',
		scaleAll		: 'បង្ហាញទាំងអស់',
		scaleNoBorder	: 'មិនបង្ហាញស៊ុម',
		scaleFit		: 'ត្រូវល្មម',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs Bottom', // MISSING
		alignAbsMiddle	: 'Abs Middle', // MISSING
		alignBaseline	: 'បន្ទាត់ជាមូលដ្ឋាន',
		alignTextTop	: 'លើអត្ថបទ',
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
		bgcolor			: 'ពណ៌ផ្ទៃខាងក្រោយ',
		hSpace			: 'គំលាតទទឹង',
		vSpace			: 'គំលាតបណ្តោយ',
		validateSrc		: 'សូមសរសេរ អាស័យដ្ឋាន URL',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'ពិនិត្យអក្ខរាវិរុទ្ធ',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'គ្មានក្នុងវចនានុក្រម',
		changeTo		: 'ផ្លាស់ប្តូរទៅ',
		btnIgnore		: 'មិនផ្លាស់ប្តូរ',
		btnIgnoreAll	: 'មិនផ្លាស់ប្តូរ ទាំងអស់',
		btnReplace		: 'ជំនួស',
		btnReplaceAll	: 'ជំនួសទាំងអស់',
		btnUndo			: 'សារឡើងវិញ',
		noSuggestions	: '- គ្មានសំណើរ -',
		progress		: 'កំពុងពិនិត្យអក្ខរាវិរុទ្ធ...',
		noMispell		: 'ការពិនិត្យអក្ខរាវិរុទ្ធបានចប់: គ្មានកំហុស',
		noChanges		: 'ការពិនិត្យអក្ខរាវិរុទ្ធបានចប់: ពុំមានផ្លាស់ប្តូរ',
		oneChange		: 'ការពិនិត្យអក្ខរាវិរុទ្ធបានចប់: ពាក្យមួយត្រូចបានផ្លាស់ប្តូរ',
		manyChanges		: 'ការពិនិត្យអក្ខរាវិរុទ្ធបានចប់: %1 ពាក្យបានផ្លាស់ប្តូរ',
		ieSpellDownload	: 'ពុំមានកម្មវិធីពិនិត្យអក្ខរាវិរុទ្ធ ។ តើចង់ទាញយកពីណា?'
	},

	smiley :
	{
		toolbar	: 'រូបភាព',
		title	: 'បញ្ជូលរូបភាព',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'បញ្ជីជាអក្សរ',
	bulletedlist	: 'បញ្ជីជារង្វង់មូល',
	indent			: 'បន្ថែមការចូលបន្ទាត់',
	outdent			: 'បន្ថយការចូលបន្ទាត់',

	justify :
	{
		left	: 'តំរឹមឆ្វេង',
		center	: 'តំរឹមកណ្តាល',
		right	: 'តំរឹមស្តាំ',
		block	: 'តំរឹមសងខាង'
	},

	blockquote : 'Block Quote', // MISSING

	clipboard :
	{
		title		: 'ចំលងដាក់',
		cutError	: 'ការកំណត់សុវត្ថភាពរបស់កម្មវិធីរុករករបស់លោកអ្នក នេះ\u200bមិនអាចធ្វើកម្មវិធីតាក់តែងអត្ថបទ កាត់អត្ថបទយកដោយស្វ័យប្រវត្តបានឡើយ ។ សូមប្រើប្រាស់បន្សំ ឃីដូចនេះ  (Ctrl/Cmd+X) ។',
		copyError	: 'ការកំណត់សុវត្ថភាពរបស់កម្មវិធីរុករករបស់លោកអ្នក នេះ\u200bមិនអាចធ្វើកម្មវិធីតាក់តែងអត្ថបទ ចំលងអត្ថបទយកដោយស្វ័យប្រវត្តបានឡើយ ។ សូមប្រើប្រាស់បន្សំ ឃីដូចនេះ (Ctrl/Cmd+C)។',
		pasteMsg	: 'សូមចំលងអត្ថបទទៅដាក់ក្នុងប្រអប់ដូចខាងក្រោមដោយប្រើប្រាស់ ឃី \u200b(<STRONG>Ctrl/Cmd+V</STRONG>) ហើយចុច <STRONG>OK</STRONG> ។',
		securityMsg	: 'Because of your browser security settings, the editor is not able to access your clipboard data directly. You are required to paste it again in this window.', // MISSING
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'ចំលងដាក់ពី Word',
		title			: 'ចំលងដាក់ពី Word',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'ចំលងដាក់អត្ថបទធម្មតា',
		title	: 'ចំលងដាក់អត្ថបទធម្មតា'
	},

	templates :
	{
		button			: 'ឯកសារគំរូ',
		title			: 'ឯកសារគំរូ របស់អត្ថន័យ',
		options : 'Template Options', // MISSING
		insertOption	: 'Replace actual contents', // MISSING
		selectPromptMsg	: 'សូមជ្រើសរើសឯកសារគំរូ ដើម្បីបើកនៅក្នុងកម្មវិធីតាក់តែងអត្ថបទ<br>(អត្ថបទនឹងបាត់បង់):',
		emptyListMsg	: '(ពុំមានឯកសារគំរូត្រូវបានកំណត់)'
	},

	showBlocks : 'Show Blocks', // MISSING

	stylesCombo :
	{
		label		: 'ម៉ូត',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'រចនា',
		panelTitle	: 'រចនា',

		tag_p		: 'Normal',
		tag_pre		: 'Formatted',
		tag_address	: 'Address',
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
		label		: 'ហ្វុង',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'ហ្វុង'
	},

	fontSize :
	{
		label		: 'ទំហំ',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'ទំហំ'
	},

	colorButton :
	{
		textColorTitle	: 'ពណ៌អក្សរ',
		bgColorTitle	: 'ពណ៌ផ្ទៃខាងក្រោយ',
		panelTitle		: 'Colors', // MISSING
		auto			: 'ស្វ័យប្រវត្ត',
		more			: 'ពណ៌ផ្សេងទៀត..'
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
		label : 'ការកំណត់ ឯកសារ',
		title : 'ការកំណត់ ឯកសារ',
		design : 'Design', // MISSING
		meta : 'ទិន្នន័យមេ',
		chooseColor : 'Choose', // MISSING
		other : '<other>',
		docTitle :	'ចំណងជើងទំព័រ',
		charset : 	'កំណត់លេខកូតភាសា',
		charsetOther : 'កំណត់លេខកូតភាសាផ្សេងទៀត',
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
		docType : 'ប្រភេទក្បាលទំព័រ',
		docTypeOther : 'ប្រភេទក្បាលទំព័រផ្សេងទៀត',
		xhtmlDec : 'បញ្ជូល XHTML',
		bgColor : 'ពណ៌ខាងក្រោម',
		bgImage : 'URL របស់រូបភាពខាងក្រោម',
		bgFixed : 'ទំព័រក្រោមមិនប្តូរ',
		txtColor : 'ពណ៌អក្សរ',
		margin : 'ស៊ុមទំព័រ',
		marginTop : 'លើ',
		marginLeft : 'ឆ្វេង',
		marginRight : 'ស្ដាំ',
		marginBottom : 'ក្រោម',
		metaKeywords : 'ពាក្យនៅក្នុងឯកសារ (ផ្តាច់ពីគ្នាដោយក្បៀស)',
		metaDescription : 'សេចក្តីអត្ថាធិប្បាយអំពីឯកសារ',
		metaAuthor : 'អ្នកនិពន្ធ',
		metaCopyright : 'រក្សាសិទ្ធិ៏',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
