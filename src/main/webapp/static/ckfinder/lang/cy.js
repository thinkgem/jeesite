/*
 * CKFinder
 * ========
 * http://ckfinder.com
 * Copyright (C) 2007-2012, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object for the Welsh
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['cy'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, ddim ar gael</span>',
		confirmCancel	: 'Cafodd rhai o\'r opsiynau eu newid. Ydych chi wir am gau ffenestr y deialog?',
		ok				: 'Iawn',
		cancel			: 'Diddymu',
		confirmationTitle	: 'Cadarnhad',
		messageTitle	: 'Gwybodaeth',
		inputTitle		: 'Cwestiwn',
		undo			: 'Dadwneud',
		redo			: 'Ailadrodd',
		skip			: 'Neidio',
		skipAll			: 'Neidio pob',
		makeDecision	: 'Pa weithred i\'w chymryd?',
		rememberDecision: 'Cofio fy mhenderfyniad'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'cy',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'mm/dd/yyyy HH:MM',
	DateAmPm : ['YB', 'YH'],

	// Folders
	FoldersTitle	: 'Ffolderi',
	FolderLoading	: 'Yn llwytho...',
	FolderNew		: 'Rhowch enw newydd y ffolder: ',
	FolderRename	: 'Rhowch enw newydd y ffolder: ',
	FolderDelete	: 'Ydych chi wir am ddileu\'r ffolder "%1"?',
	FolderRenaming	: ' (Yn ailenwi...)',
	FolderDeleting	: ' (Yn dileu...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Rhowch enw newydd y ffeil: ',
	FileRenameExt	: 'Ydych chi wir am newid estyniad y ffeil? Gall hwn atal y ffeil rhag gweithio.',
	FileRenaming	: 'Yn ailenwi...',
	FileDelete		: 'Ydych chi wir am ddileu\'r ffeil "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Yn llwytho...',
	FilesEmpty		: 'Mae\'r ffolder yn wag.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Basged',
	BasketClear			: 'Clirio\'r Fasged',
	BasketRemove		: 'Tynnu o\'r Fasged',
	BasketOpenFolder	: 'Agor yr Uwch Ffolder',
	BasketTruncateConfirm : 'Ydych chi wir am dynnu\'r holl ffeiliau o\'r fasged?',
	BasketRemoveConfirm	: 'Ydych chi wir am dynnu\'r ffeil "%1" o\'r fasged?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Dim ffeiliau yn y fasged, llusgwch a\'m gollwng.',
	BasketCopyFilesHere	: 'Copïo Ffeiliau o\'r Fasged',
	BasketMoveFilesHere	: 'Symud Ffeiliau o\'r Fasged',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Lanlwytho',
	UploadTip	: 'Lanlwytho Ffeil Newydd',
	Refresh		: 'Adfywio',
	Settings	: 'Gosodiadau',
	Help		: 'Cymorth',
	HelpTip		: 'Cymorth',

	// Context Menus
	Select			: 'Dewis',
	SelectThumbnail : 'Dewis Bawdlun',
	View			: 'Dangos',
	Download		: 'Lawrlwytho',

	NewSubFolder	: 'Is-ffolder Newydd',
	Rename			: 'Ailenwi',
	Delete			: 'Dileu',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copïo Yma',
	MoveDragDrop	: 'Symud Yma',

	// Dialogs
	RenameDlgTitle		: 'Ailenwi',
	NewNameDlgTitle		: 'Enw Newydd',
	FileExistsDlgTitle	: 'Ffeil Eisoes yn Bodoli',
	SysErrorDlgTitle : 'Gwall System',

	FileOverwrite	: 'Trosysgrifo',
	FileAutorename	: 'Awto-ailenwi',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Iawn',
	CancelBtn	: 'Diddymu',
	CloseBtn	: 'Cau',

	// Upload Panel
	UploadTitle			: 'Lanlwytho Ffeil Newydd',
	UploadSelectLbl		: 'Dewis ffeil i lanlwytho',
	UploadProgressLbl	: '(Lanlwythiad ar y gweill, arhoswch...)',
	UploadBtn			: 'Lanlwytho\'r Ffeil a Ddewiswyd',
	UploadBtnCancel		: 'Diddymu',

	UploadNoFileMsg		: 'Dewiswch ffeil ar eich cyfrifiadur.',
	UploadNoFolder		: 'Dewiswch ffolder cyn lanlwytho.',
	UploadNoPerms		: 'Does dim hawl lanlwytho ffeiliau.',
	UploadUnknError		: 'Gwall wrth anfon y ffeil.',
	UploadExtIncorrect	: 'Does dim hawl cadw\'r ffeiliau â\'r estyniad hwn yn y ffolder hwn.',

	// Flash Uploads
	UploadLabel			: 'Ffeiliau i\'w Lanlwytho',
	UploadTotalFiles	: 'Nifer y Ffeiliau:',
	UploadTotalSize		: 'Maint Cyfan:',
	UploadSend			: 'Lanlwytho',
	UploadAddFiles		: 'Ychwanegu Ffeiliau',
	UploadClearFiles	: 'Clirio Ffeiliau',
	UploadCancel		: 'Diddymu Lanlwythiad',
	UploadRemove		: 'Tynnu',
	UploadRemoveTip		: 'Tynnu !f',
	UploadUploaded		: 'Wedi Lanlwytho !n%',
	UploadProcessing	: 'Yn prosesu...',

	// Settings Panel
	SetTitle		: 'Gosodiadau',
	SetView			: 'Golwg:',
	SetViewThumb	: 'Bawdluniau',
	SetViewList		: 'Rhestr',
	SetDisplay		: 'Arddangosiad:',
	SetDisplayName	: 'Enw\'r Ffeil',
	SetDisplayDate	: 'Dyddiad',
	SetDisplaySize	: 'Maint y Ffeil',
	SetSort			: 'Trefnu:',
	SetSortName		: 'gan Enw\'r Ffeil',
	SetSortDate		: 'gan y Dyddiad',
	SetSortSize		: 'gan y Maint',
	SetSortExtension		: 'gan Estyniad',

	// Status Bar
	FilesCountEmpty : '<Ffolder Gwag>',
	FilesCountOne	: '1 ffeil',
	FilesCountMany	: '%1 ffeil',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB', // MISSING
	Gb				: '%1 GB', // MISSING
	SizePerSecond	: '%1/s', // MISSING

	// Connector Error Messages.
	ErrorUnknown	: 'Does dim modd cwblhau\'r cais. (Gwall %1)',
	Errors :
	{
	 10 : 'Gorchymyn annilys.',
	 11 : 'Doedd math yr adnodd heb ei benodi yn y cais.',
	 12 : 'Dyw math yr adnodd ddim yn ddilys.',
	102 : 'Enw ffeil neu ffolder annilys.',
	103 : 'Doedd dim modd cwblhau\'r cais oherwydd cyfyngiadau awdurdodi.',
	104 : 'Doedd dim modd cwblhau\'r cais oherwydd cyfyngiadau i hawliau\'r system ffeilio.',
	105 : 'Estyniad ffeil annilys.',
	109 : 'Cais annilys.',
	110 : 'Gwall anhysbys.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Mae ffeil neu ffolder gyda\'r un enw yn bodoli yn barod.',
	116 : 'Methu â darganfod y ffolder. Adfywiwch a cheisio eto.',
	117 : 'Methu â darganfod y ffeil. Adfywiwch y rhestr ffeiliau a cheisio eto.',
	118 : 'Mae\'r llwybrau gwreiddiol a tharged yn unfath.',
	201 : 'Mae ffeil â\'r enw hwnnw yn bodoli yn barod. Cafodd y ffeil a lanlwythwyd ei hailenwi i "%1".',
	202 : 'Ffeil annilys.',
	203 : 'Ffeil annilys. Mae maint y ffeil yn rhy fawr.',
	204 : 'Mae\'r ffeil a lanwythwyd wedi chwalu.',
	205 : 'Does dim ffolder dros dro ar gael er mwyn lanlwytho ffeiliau iddo ar y gweinydd hwn.',
	206 : 'Cafodd y lanlwythiad ei ddiddymu oherwydd rhesymau diogelwch. Mae\'r ffeil yn cynnwys data yn debyg i HTML.',
	207 : 'Cafodd y ffeil a lanlwythwyd ei hailenwi i "%1".',
	300 : 'Methodd symud y ffeil(iau).',
	301 : 'Methodd copïo\'r ffeil(iau).',
	500 : 'Cafodd y porwr ffeiliau ei anallogi oherwydd rhesymau diogelwch. Cysylltwch â\'ch gweinyddwr system a gwirio\'ch ffeil ffurfwedd CKFinder.',
	501 : 'Mae cynhaliaeth bawdluniau wedi\'i hanalluogi.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Does dim modd i enw\'r ffeil fod yn wag.',
		FileExists		: 'Mae\'r ffeil %s yn bodoli yn barod.',
		FolderEmpty		: 'Does dim modd i\'r ffolder fod yn wag.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Does dim hawl defnyddio\'r nodau canlynol i enwi ffeil: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Does dim hawl defnyddio\'r nodau canlynol i enwi ffolder: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Doedd dim modd agor y ffeil mewn ffenestr newydd. Bydd angen ffurfweddu\'r porwr i analluogi pob ataliwr \'popup\' ar gyfer y safle hwn.',
		XmlError		: 'Doedd dim modd llwytho\'r ymateb XML yn gywir o\'r gweinydd.',
		XmlEmpty		: 'Doedd dim modd llwytho\'r ymateb XML o\'r gweinydd gwe. Gwnaeth y gweinydd ddychwelyd ymateb gwag.',
		XmlRawResponse	: 'Yr ymateb noeth o\'r gweinydd: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Ailmeintio %s',
		sizeTooBig		: 'Methu â gosod lled neu uchder y ddelwedd i werth yn uwch na\'r maint gwreiddiol (%size).',
		resizeSuccess	: 'Delwedd wedi\'i hailmeintio.',
		thumbnailNew	: 'Creu bawdlun newydd',
		thumbnailSmall	: 'Bach (%s)',
		thumbnailMedium	: 'Canolig (%s)',
		thumbnailLarge	: 'Mawr (%s)',
		newSize			: 'Gosod maint newydd',
		width			: 'Lled',
		height			: 'Uchder',
		invalidHeight	: 'Uchder annilys.',
		invalidWidth	: 'Lled annilys.',
		invalidName		: 'Enw ffeil annilys.',
		newImage		: 'Creu delwedd newydd',
		noExtensionChange : 'Methu â newid estyniad y ffeil.',
		imageSmall		: 'Mae\'r ddelwedd wreiddiol yn rhy fach.',
		contextMenuName	: 'Ailmeintio',
		lockRatio		: 'Cloi\'r cymhareb',
		resetSize		: 'Ailosod y maint'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Cadw',
		fileOpenError	: 'Methu ag agor y ffeil.',
		fileSaveSuccess	: 'Ffeil wedi\'i chadw.',
		contextMenuName	: 'Golygu',
		loadingFile		: 'Llwytho ffeil, arhoswch...'
	},

	Maximize :
	{
		maximize : 'Uchafu',
		minimize : 'Isafu'
	},

	Gallery :
	{
		current : 'Image {current} of {total}' // MISSING
	},

	Zip :
	{
		extractHereLabel	: 'Extract here', // MISSING
		extractToLabel		: 'Extract to...', // MISSING
		downloadZipLabel	: 'Download as zip', // MISSING
		compressZipLabel	: 'Compress to zip', // MISSING
		removeAndExtract	: 'Remove existing and extract', // MISSING
		extractAndOverwrite	: 'Extract overwriting existing files', // MISSING
		extractSuccess		: 'File extracted successfully.' // MISSING
	}
};
