/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.lang} object, for the
 * Hindi language.
 */

/**#@+
   @type String
   @example
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKEDITOR.lang['hi'] =
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
	toolbars	: 'एडिटर टूलबार',
	editor		: 'रिच टेक्स्ट एडिटर',

	// Toolbar buttons without dialogs.
	source			: 'सोर्स',
	newPage			: 'नया पेज',
	save			: 'सेव',
	preview			: 'प्रीव्यू',
	cut				: 'कट',
	copy			: 'कॉपी',
	paste			: 'पेस्ट',
	print			: 'प्रिन्ट',
	underline		: 'रेखांकण',
	bold			: 'बोल्ड',
	italic			: 'इटैलिक',
	selectAll		: 'सब सॅलॅक्ट करें',
	removeFormat	: 'फ़ॉर्मैट हटायें',
	strike			: 'स्ट्राइक थ्रू',
	subscript		: 'अधोलेख',
	superscript		: 'अभिलेख',
	horizontalrule	: 'हॉरिज़ॉन्टल रेखा इन्सर्ट करें',
	pagebreak		: 'पेज ब्रेक इन्सर्ट् करें',
	pagebreakAlt		: 'पेज ब्रेक',
	unlink			: 'लिंक हटायें',
	undo			: 'अन्डू',
	redo			: 'रीडू',

	// Common messages and labels.
	common :
	{
		browseServer	: 'सर्वर ब्राउज़ करें',
		url				: 'URL',
		protocol		: 'प्रोटोकॉल',
		upload			: 'अपलोड',
		uploadSubmit	: 'इसे सर्वर को भेजें',
		image			: 'तस्वीर',
		flash			: 'फ़्लैश',
		form			: 'फ़ॉर्म',
		checkbox		: 'चॅक बॉक्स',
		radio			: 'रेडिओ बटन',
		textField		: 'टेक्स्ट फ़ील्ड',
		textarea		: 'टेक्स्ट एरिया',
		hiddenField		: 'गुप्त फ़ील्ड',
		button			: 'बटन',
		select			: 'चुनाव फ़ील्ड',
		imageButton		: 'तस्वीर बटन',
		notSet			: '<सॅट नहीं>',
		id				: 'Id',
		name			: 'नाम',
		langDir			: 'भाषा लिखने की दिशा',
		langDirLtr		: 'बायें से दायें (LTR)',
		langDirRtl		: 'दायें से बायें (RTL)',
		langCode		: 'भाषा कोड',
		longDescr		: 'अधिक विवरण के लिए URL',
		cssClass		: 'स्टाइल-शीट क्लास',
		advisoryTitle	: 'परामर्श शीर्शक',
		cssStyle		: 'स्टाइल',
		ok				: 'ठीक है',
		cancel			: 'रद्द करें',
		close			: 'Close', // MISSING
		preview			: 'Preview', // MISSING
		generalTab		: 'सामान्य',
		advancedTab		: 'ऍड्वान्स्ड',
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
		width			: 'चौड़ाई',
		height			: 'ऊँचाई',
		align			: 'ऍलाइन',
		alignLeft		: 'दायें',
		alignRight		: 'दायें',
		alignCenter		: 'बीच में',
		alignTop		: 'ऊपर',
		alignMiddle		: 'मध्य',
		alignBottom		: 'नीचे',
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
		toolbar		: 'विशेष करॅक्टर इन्सर्ट करें',
		title		: 'विशेष करॅक्टर चुनें',
		options : 'Special Character Options' // MISSING
	},

	// Link dialog.
	link :
	{
		toolbar		: 'लिंक इन्सर्ट/संपादन',
		other 		: '<अन्य>',
		menu		: 'लिंक संपादन',
		title		: 'लिंक',
		info		: 'लिंक  ',
		target		: 'टार्गेट',
		upload		: 'अपलोड',
		advanced	: 'ऍड्वान्स्ड',
		type		: 'लिंक प्रकार',
		toUrl		: 'URL', // MISSING
		toAnchor	: 'इस पेज का ऐंकर',
		toEmail		: 'ई-मेल',
		targetFrame		: '<फ़्रेम>',
		targetPopup		: '<पॉप-अप विन्डो>',
		targetFrameName	: 'टार्गेट फ़्रेम का नाम',
		targetPopupName	: 'पॉप-अप विन्डो का नाम',
		popupFeatures	: 'पॉप-अप विन्डो फ़ीचर्स',
		popupResizable	: 'Resizable', // MISSING
		popupStatusBar	: 'स्टेटस बार',
		popupLocationBar: 'लोकेशन बार',
		popupToolbar	: 'टूल बार',
		popupMenuBar	: 'मॅन्यू बार',
		popupFullScreen	: 'फ़ुल स्क्रीन (IE)',
		popupScrollBars	: 'स्क्रॉल बार',
		popupDependent	: 'डिपेन्डॅन्ट (Netscape)',
		popupLeft		: 'बायीं तरफ',
		popupTop		: 'दायीं तरफ',
		id				: 'Id', // MISSING
		langDir			: 'भाषा लिखने की दिशा',
		langDirLTR		: 'बायें से दायें (LTR)',
		langDirRTL		: 'दायें से बायें (RTL)',
		acccessKey		: 'ऍक्सॅस की',
		name			: 'नाम',
		langCode			: 'भाषा लिखने की दिशा',
		tabIndex			: 'टैब इन्डॅक्स',
		advisoryTitle		: 'परामर्श शीर्शक',
		advisoryContentType	: 'परामर्श कन्टॅन्ट प्रकार',
		cssClasses		: 'स्टाइल-शीट क्लास',
		charset			: 'लिंक रिसोर्स करॅक्टर सॅट',
		styles			: 'स्टाइल',
		rel			: 'Relationship', // MISSING
		selectAnchor		: 'ऐंकर चुनें',
		anchorName		: 'ऐंकर नाम से',
		anchorId			: 'ऍलीमॅन्ट Id से',
		emailAddress		: 'ई-मेल पता',
		emailSubject		: 'संदेश विषय',
		emailBody		: 'संदेश',
		noAnchors		: '(डॉक्यूमॅन्ट में ऐंकर्स की संख्या)',
		noUrl			: 'लिंक URL टाइप करें',
		noEmail			: 'ई-मेल पता टाइप करें'
	},

	// Anchor dialog
	anchor :
	{
		toolbar		: 'ऐंकर इन्सर्ट/संपादन',
		menu		: 'ऐंकर प्रॉपर्टीज़',
		title		: 'ऐंकर प्रॉपर्टीज़',
		name		: 'ऐंकर का नाम',
		errorName	: 'ऐंकर का नाम टाइप करें',
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
		title				: 'खोजें और बदलें',
		find				: 'खोजें',
		replace				: 'रीप्लेस',
		findWhat			: 'यह खोजें:',
		replaceWith			: 'इससे रिप्लेस करें:',
		notFoundMsg			: 'आपके द्वारा दिया गया टेक्स्ट नहीं मिला',
		findOptions			: 'Find Options', // MISSING
		matchCase			: 'केस मिलायें',
		matchWord			: 'पूरा शब्द मिलायें',
		matchCyclic			: 'Match cyclic', // MISSING
		replaceAll			: 'सभी रिप्लेस करें',
		replaceSuccessMsg	: '%1 occurrence(s) replaced.' // MISSING
	},

	// Table Dialog
	table :
	{
		toolbar		: 'टेबल',
		title		: 'टेबल प्रॉपर्टीज़',
		menu		: 'टेबल प्रॉपर्टीज़',
		deleteTable	: 'टेबल डिलीट करें',
		rows		: 'पंक्तियाँ',
		columns		: 'कालम',
		border		: 'बॉर्डर साइज़',
		widthPx		: 'पिक्सैल',
		widthPc		: 'प्रतिशत',
		widthUnit	: 'width unit', // MISSING
		cellSpace	: 'सैल अंतर',
		cellPad		: 'सैल पैडिंग',
		caption		: 'शीर्षक',
		summary		: 'सारांश',
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
			menu			: 'खाना',
			insertBefore	: 'पहले सैल डालें',
			insertAfter		: 'बाद में सैल डालें',
			deleteCell		: 'सैल डिलीट करें',
			merge			: 'सैल मिलायें',
			mergeRight		: 'बाँया विलय',
			mergeDown		: 'नीचे विलय करें',
			splitHorizontal	: 'सैल को क्षैतिज स्थिति में विभाजित करें',
			splitVertical	: 'सैल को लम्बाकार में विभाजित करें',
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
			menu			: 'पंक्ति',
			insertBefore	: 'पहले पंक्ति डालें',
			insertAfter		: 'बाद में पंक्ति डालें',
			deleteRow		: 'पंक्तियाँ डिलीट करें'
		},

		column :
		{
			menu			: 'कालम',
			insertBefore	: 'पहले कालम डालें',
			insertAfter		: 'बाद में कालम डालें',
			deleteColumn	: 'कालम डिलीट करें'
		}
	},

	// Button Dialog.
	button :
	{
		title		: 'बटन प्रॉपर्टीज़',
		text		: 'टेक्स्ट (वैल्यू)',
		type		: 'प्रकार',
		typeBtn		: 'बटन',
		typeSbm		: 'सब्मिट',
		typeRst		: 'रिसेट'
	},

	// Checkbox and Radio Button Dialogs.
	checkboxAndRadio :
	{
		checkboxTitle : 'चॅक बॉक्स प्रॉपर्टीज़',
		radioTitle	: 'रेडिओ बटन प्रॉपर्टीज़',
		value		: 'वैल्यू',
		selected	: 'सॅलॅक्टॅड'
	},

	// Form Dialog.
	form :
	{
		title		: 'फ़ॉर्म प्रॉपर्टीज़',
		menu		: 'फ़ॉर्म प्रॉपर्टीज़',
		action		: 'क्रिया',
		method		: 'तरीका',
		encoding	: 'Encoding' // MISSING
	},

	// Select Field Dialog.
	select :
	{
		title		: 'चुनाव फ़ील्ड प्रॉपर्टीज़',
		selectInfo	: 'सूचना',
		opAvail		: 'उपलब्ध विकल्प',
		value		: 'वैल्यू',
		size		: 'साइज़',
		lines		: 'पंक्तियाँ',
		chkMulti	: 'एक से ज्यादा विकल्प चुनने दें',
		opText		: 'टेक्स्ट',
		opValue		: 'वैल्यू',
		btnAdd		: 'जोड़ें',
		btnModify	: 'बदलें',
		btnUp		: 'ऊपर',
		btnDown		: 'नीचे',
		btnSetValue : 'चुनी गई वैल्यू सॅट करें',
		btnDelete	: 'डिलीट'
	},

	// Textarea Dialog.
	textarea :
	{
		title		: 'टेक्स्त एरिया प्रॉपर्टीज़',
		cols		: 'कालम',
		rows		: 'पंक्तियां'
	},

	// Text Field Dialog.
	textfield :
	{
		title		: 'टेक्स्ट फ़ील्ड प्रॉपर्टीज़',
		name		: 'नाम',
		value		: 'वैल्यू',
		charWidth	: 'करॅक्टर की चौढ़ाई',
		maxChars	: 'अधिकतम करॅक्टर',
		type		: 'टाइप',
		typeText	: 'टेक्स्ट',
		typePass	: 'पास्वर्ड'
	},

	// Hidden Field Dialog.
	hidden :
	{
		title	: 'गुप्त फ़ील्ड प्रॉपर्टीज़',
		name	: 'नाम',
		value	: 'वैल्यू'
	},

	// Image Dialog.
	image :
	{
		title		: 'तस्वीर प्रॉपर्टीज़',
		titleButton	: 'तस्वीर बटन प्रॉपर्टीज़',
		menu		: 'तस्वीर प्रॉपर्टीज़',
		infoTab		: 'तस्वीर की जानकारी',
		btnUpload	: 'इसे सर्वर को भेजें',
		upload		: 'अपलोड',
		alt			: 'वैकल्पिक टेक्स्ट',
		lockRatio	: 'लॉक अनुपात',
		resetSize	: 'रीसॅट साइज़',
		border		: 'बॉर्डर',
		hSpace		: 'हॉरिज़ॉन्टल स्पेस',
		vSpace		: 'वर्टिकल स्पेस',
		alertUrl	: 'तस्वीर का URL टाइप करें ',
		linkTab		: 'लिंक',
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
		properties		: 'फ़्लैश प्रॉपर्टीज़',
		propertiesTab	: 'Properties', // MISSING
		title			: 'फ़्लैश प्रॉपर्टीज़',
		chkPlay			: 'ऑटो प्ले',
		chkLoop			: 'लूप',
		chkMenu			: 'फ़्लैश मॅन्यू का प्रयोग करें',
		chkFull			: 'Allow Fullscreen', // MISSING
 		scale			: 'स्केल',
		scaleAll		: 'सभी दिखायें',
		scaleNoBorder	: 'कोई बॉर्डर नहीं',
		scaleFit		: 'बिल्कुल फ़िट',
		access			: 'Script Access', // MISSING
		accessAlways	: 'Always', // MISSING
		accessSameDomain: 'Same domain', // MISSING
		accessNever		: 'Never', // MISSING
		alignAbsBottom	: 'Abs नीचे',
		alignAbsMiddle	: 'Abs ऊपर',
		alignBaseline	: 'मूल रेखा',
		alignTextTop	: 'टेक्स्ट ऊपर',
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
		bgcolor			: 'बैक्ग्राउन्ड रंग',
		hSpace			: 'हॉरिज़ॉन्टल स्पेस',
		vSpace			: 'वर्टिकल स्पेस',
		validateSrc		: 'लिंक URL टाइप करें',
		validateHSpace	: 'HSpace must be a number.', // MISSING
		validateVSpace	: 'VSpace must be a number.' // MISSING
	},

	// Speller Pages Dialog
	spellCheck :
	{
		toolbar			: 'वर्तनी (स्पेलिंग) जाँच',
		title			: 'Spell Check', // MISSING
		notAvailable	: 'Sorry, but service is unavailable now.', // MISSING
		errorLoading	: 'Error loading application service host: %s.', // MISSING
		notInDic		: 'शब्दकोश में नहीं',
		changeTo		: 'इसमें बदलें',
		btnIgnore		: 'इग्नोर',
		btnIgnoreAll	: 'सभी इग्नोर करें',
		btnReplace		: 'रिप्लेस',
		btnReplaceAll	: 'सभी रिप्लेस करें',
		btnUndo			: 'अन्डू',
		noSuggestions	: '- कोई सुझाव नहीं -',
		progress		: 'वर्तनी की जाँच (स्पॅल-चॅक) जारी है...',
		noMispell		: 'वर्तनी की जाँच : कोई गलत वर्तनी (स्पॅलिंग) नहीं पाई गई',
		noChanges		: 'वर्तनी की जाँच :कोई शब्द नहीं बदला गया',
		oneChange		: 'वर्तनी की जाँच : एक शब्द बदला गया',
		manyChanges		: 'वर्तनी की जाँच : %1 शब्द बदले गये',
		ieSpellDownload	: 'स्पॅल-चॅकर इन्स्टाल नहीं किया गया है। क्या आप इसे डाउनलोड करना चाहेंगे?'
	},

	smiley :
	{
		toolbar	: 'स्माइली',
		title	: 'स्माइली इन्सर्ट करें',
		options : 'Smiley Options' // MISSING
	},

	elementsPath :
	{
		eleLabel : 'Elements path', // MISSING
		eleTitle : '%1 element' // MISSING
	},

	numberedlist	: 'अंकीय सूची',
	bulletedlist	: 'बुलॅट सूची',
	indent			: 'इन्डॅन्ट बढ़ायें',
	outdent			: 'इन्डॅन्ट कम करें',

	justify :
	{
		left	: 'बायीं तरफ',
		center	: 'बीच में',
		right	: 'दायीं तरफ',
		block	: 'ब्लॉक जस्टीफ़ाई'
	},

	blockquote : 'ब्लॉक-कोट',

	clipboard :
	{
		title		: 'पेस्ट',
		cutError	: 'आपके ब्राउज़र की सुरक्षा सॅटिन्ग्स ने कट करने की अनुमति नहीं प्रदान की है। (Ctrl/Cmd+X) का प्रयोग करें।',
		copyError	: 'आपके ब्राआउज़र की सुरक्षा सॅटिन्ग्स ने कॉपी करने की अनुमति नहीं प्रदान की है। (Ctrl/Cmd+C) का प्रयोग करें।',
		pasteMsg	: 'Ctrl/Cmd+V का प्रयोग करके पेस्ट करें और ठीक है करें.',
		securityMsg	: 'आपके ब्राउज़र की सुरक्षा आपके ब्राउज़र की सुरKश सैटिंग के कारण, एडिटर आपके क्लिपबोर्ड डेटा को नहीं पा सकता है. आपको उसे इस विन्डो में दोबारा पेस्ट करना होगा.',
		pasteArea	: 'Paste Area' // MISSING
	},

	pastefromword :
	{
		confirmCleanup	: 'The text you want to paste seems to be copied from Word. Do you want to clean it before pasting?', // MISSING
		toolbar			: 'पेस्ट (वर्ड से)',
		title			: 'पेस्ट (वर्ड से)',
		error			: 'It was not possible to clean up the pasted data due to an internal error' // MISSING
	},

	pasteText :
	{
		button	: 'पेस्ट (सादा टॅक्स्ट)',
		title	: 'पेस्ट (सादा टॅक्स्ट)'
	},

	templates :
	{
		button			: 'टॅम्प्लेट',
		title			: 'कन्टेन्ट टॅम्प्लेट',
		options : 'Template Options', // MISSING
		insertOption	: 'मूल शब्दों को बदलें',
		selectPromptMsg	: 'ऍडिटर में ओपन करने हेतु टॅम्प्लेट चुनें(वर्तमान कन्टॅन्ट सेव नहीं होंगे):',
		emptyListMsg	: '(कोई टॅम्प्लेट डिफ़ाइन नहीं किया गया है)'
	},

	showBlocks : 'ब्लॉक दिखायें',

	stylesCombo :
	{
		label		: 'स्टाइल',
		panelTitle	: 'Formatting Styles', // MISSING
		panelTitle1	: 'Block Styles', // MISSING
		panelTitle2	: 'Inline Styles', // MISSING
		panelTitle3	: 'Object Styles' // MISSING
	},

	format :
	{
		label		: 'फ़ॉर्मैट',
		panelTitle	: 'फ़ॉर्मैट',

		tag_p		: 'साधारण',
		tag_pre		: 'फ़ॉर्मैटॅड',
		tag_address	: 'पता',
		tag_h1		: 'शीर्षक 1',
		tag_h2		: 'शीर्षक 2',
		tag_h3		: 'शीर्षक 3',
		tag_h4		: 'शीर्षक 4',
		tag_h5		: 'शीर्षक 5',
		tag_h6		: 'शीर्षक 6',
		tag_div		: 'शीर्षक (DIV)'
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
		label		: 'फ़ॉन्ट',
		voiceLabel	: 'Font', // MISSING
		panelTitle	: 'फ़ॉन्ट'
	},

	fontSize :
	{
		label		: 'साइज़',
		voiceLabel	: 'Font Size', // MISSING
		panelTitle	: 'साइज़'
	},

	colorButton :
	{
		textColorTitle	: 'टेक्स्ट रंग',
		bgColorTitle	: 'बैक्ग्राउन्ड रंग',
		panelTitle		: 'Colors', // MISSING
		auto			: 'स्वचालित',
		more			: 'और रंग...'
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

	maximize : 'मेक्सिमाईज़',
	minimize : 'मिनिमाईज़',

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
		label : 'डॉक्यूमॅन्ट प्रॉपर्टीज़',
		title : 'डॉक्यूमॅन्ट प्रॉपर्टीज़',
		design : 'Design', // MISSING
		meta : 'मॅटाडेटा',
		chooseColor : 'Choose', // MISSING
		other : '<अन्य>',
		docTitle :	'पेज शीर्षक',
		charset : 	'करेक्टर सॅट ऍन्कोडिंग',
		charsetOther : 'अन्य करेक्टर सॅट ऍन्कोडिंग',
		charsetASCII : 'ASCII', // MISSING
		charsetCE : 'मध्य यूरोपीय (Central European)',
		charsetCT : 'चीनी (Chinese Traditional Big5)',
		charsetCR : 'सिरीलिक (Cyrillic)',
		charsetGR : 'यवन (Greek)',
		charsetJP : 'जापानी (Japanese)',
		charsetKR : 'कोरीयन (Korean)',
		charsetTR : 'तुर्की (Turkish)',
		charsetUN : 'यूनीकोड (UTF-8)',
		charsetWE : 'पश्चिम यूरोपीय (Western European)',
		docType : 'डॉक्यूमॅन्ट प्रकार शीर्षक',
		docTypeOther : 'अन्य डॉक्यूमॅन्ट प्रकार शीर्षक',
		xhtmlDec : 'XHTML सूचना सम्मिलित करें',
		bgColor : 'बैक्ग्राउन्ड रंग',
		bgImage : 'बैक्ग्राउन्ड तस्वीर URL',
		bgFixed : 'स्क्रॉल न करने वाला बैक्ग्राउन्ड',
		txtColor : 'टेक्स्ट रंग',
		margin : 'पेज मार्जिन',
		marginTop : 'ऊपर',
		marginLeft : 'बायें',
		marginRight : 'दायें',
		marginBottom : 'नीचे',
		metaKeywords : 'डॉक्युमॅन्ट इन्डेक्स संकेतशब्द (अल्पविराम से अलग करें)',
		metaDescription : 'डॉक्यूमॅन्ट करॅक्टरन',
		metaAuthor : 'लेखक',
		metaCopyright : 'कॉपीराइट',
		previewHtml : '<p>This is some <strong>sample text</strong>. You are using <a href="javascript:void(0)">CKEditor</a>.</p>' // MISSING
	}
};
