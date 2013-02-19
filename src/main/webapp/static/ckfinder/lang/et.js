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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Estonian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['et'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, pole saadaval</span>',
		confirmCancel	: 'Mõned valikud on muudetud. Kas oled kindel, et tahad dialoogiakna sulgeda?',
		ok				: 'Olgu',
		cancel			: 'Loobu',
		confirmationTitle	: 'Kinnitus',
		messageTitle	: 'Andmed',
		inputTitle		: 'Küsimus',
		undo			: 'Võta tagasi',
		redo			: 'Tee uuesti',
		skip			: 'Jäta vahele',
		skipAll			: 'Jäta kõik vahele',
		makeDecision	: 'Mida tuleks teha?',
		rememberDecision: 'Jäta valik meelde'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'et',

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
	DateTime : 'yyyy-mm-dd H:MM',
	DateAmPm : ['EL', 'PL'],

	// Folders
	FoldersTitle	: 'Kaustad',
	FolderLoading	: 'Laadimine...',
	FolderNew		: 'Palun sisesta uue kataloogi nimi: ',
	FolderRename	: 'Palun sisesta uue kataloogi nimi: ',
	FolderDelete	: 'Kas tahad kindlasti kausta "%1" kustutada?',
	FolderRenaming	: ' (ümbernimetamine...)',
	FolderDeleting	: ' (kustutamine...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Palun sisesta faili uus nimi: ',
	FileRenameExt	: 'Kas oled kindel, et tahad faili laiendit muuta? Fail võib muutuda kasutamatuks.',
	FileRenaming	: 'Ümbernimetamine...',
	FileDelete		: 'Kas oled kindel, et tahad kustutada faili "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laadimine...',
	FilesEmpty		: 'See kaust on tühi.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Korv',
	BasketClear			: 'Tühjenda korv',
	BasketRemove		: 'Eemalda korvist',
	BasketOpenFolder	: 'Ava ülemine kaust',
	BasketTruncateConfirm : 'Kas tahad tõesti eemaldada korvist kõik failid?',
	BasketRemoveConfirm	: 'Kas tahad tõesti eemaldada korvist faili "%1"?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Korvis ei ole ühtegi faili, lohista mõni siia.',
	BasketCopyFilesHere	: 'Failide kopeerimine korvist',
	BasketMoveFilesHere	: 'Failide liigutamine korvist',

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
	Upload		: 'Laadi üles',
	UploadTip	: 'Laadi üles uus fail',
	Refresh		: 'Värskenda',
	Settings	: 'Sätted',
	Help		: 'Abi',
	HelpTip		: 'Abi',

	// Context Menus
	Select			: 'Vali',
	SelectThumbnail : 'Vali pisipilt',
	View			: 'Kuva',
	Download		: 'Laadi alla',

	NewSubFolder	: 'Uus alamkaust',
	Rename			: 'Nimeta ümber',
	Delete			: 'Kustuta',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopeeri siia',
	MoveDragDrop	: 'Liiguta siia',

	// Dialogs
	RenameDlgTitle		: 'Ümbernimetamine',
	NewNameDlgTitle		: 'Uue nime andmine',
	FileExistsDlgTitle	: 'Fail on juba olemas',
	SysErrorDlgTitle : 'Süsteemi viga',

	FileOverwrite	: 'Kirjuta üle',
	FileAutorename	: 'Nimeta automaatselt ümber',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Olgu',
	CancelBtn	: 'Loobu',
	CloseBtn	: 'Sulge',

	// Upload Panel
	UploadTitle			: 'Uue faili üleslaadimine',
	UploadSelectLbl		: 'Vali üleslaadimiseks fail',
	UploadProgressLbl	: '(Üleslaadimine, palun oota...)',
	UploadBtn			: 'Laadi valitud fail üles',
	UploadBtnCancel		: 'Loobu',

	UploadNoFileMsg		: 'Palun vali fail oma arvutist.',
	UploadNoFolder		: 'Palun vali enne üleslaadimist kataloog.',
	UploadNoPerms		: 'Failide üleslaadimine pole lubatud.',
	UploadUnknError		: 'Viga faili saatmisel.',
	UploadExtIncorrect	: 'Selline faili laiend pole selles kaustas lubatud.',

	// Flash Uploads
	UploadLabel			: 'Üleslaaditavad failid',
	UploadTotalFiles	: 'Faile kokku:',
	UploadTotalSize		: 'Kogusuurus:',
	UploadSend			: 'Laadi üles',
	UploadAddFiles		: 'Lisa faile',
	UploadClearFiles	: 'Eemalda failid',
	UploadCancel		: 'Katkesta üleslaadimine',
	UploadRemove		: 'Eemalda',
	UploadRemoveTip		: 'Eemalda !f',
	UploadUploaded		: '!n% üles laaditud',
	UploadProcessing	: 'Töötlemine...',

	// Settings Panel
	SetTitle		: 'Sätted',
	SetView			: 'Vaade:',
	SetViewThumb	: 'Pisipildid',
	SetViewList		: 'Loend',
	SetDisplay		: 'Kuva:',
	SetDisplayName	: 'Faili nimi',
	SetDisplayDate	: 'Kuupäev',
	SetDisplaySize	: 'Faili suurus',
	SetSort			: 'Sortimine:',
	SetSortName		: 'faili nime järgi',
	SetSortDate		: 'kuupäeva järgi',
	SetSortSize		: 'suuruse järgi',
	SetSortExtension		: 'laiendi järgi',

	// Status Bar
	FilesCountEmpty : '<tühi kaust>',
	FilesCountOne	: '1 fail',
	FilesCountMany	: '%1 faili',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Päringu täitmine ei olnud võimalik. (Viga %1)',
	Errors :
	{
	 10 : 'Vigane käsk.',
	 11 : 'Allika liik ei olnud päringus määratud.',
	 12 : 'Päritud liik ei ole sobiv.',
	102 : 'Sobimatu faili või kausta nimi.',
	103 : 'Piiratud õiguste tõttu ei olnud võimalik päringut lõpetada.',
	104 : 'Failisüsteemi piiratud õiguste tõttu ei olnud võimalik päringut lõpetada.',
	105 : 'Sobimatu faililaiend.',
	109 : 'Vigane päring.',
	110 : 'Tundmatu viga.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Sellenimeline fail või kaust on juba olemas.',
	116 : 'Kausta ei leitud. Palun värskenda lehte ja proovi uuesti.',
	117 : 'Faili ei leitud. Palun värskenda lehte ja proovi uuesti.',
	118 : 'Lähte- ja sihtasukoht on sama.',
	201 : 'Samanimeline fail on juba olemas. Üles laaditud faili nimeks pandi "%1".',
	202 : 'Vigane fail.',
	203 : 'Vigane fail. Fail on liiga suur.',
	204 : 'Üleslaaditud fail on rikutud.',
	205 : 'Serverisse üleslaadimiseks pole ühtegi ajutiste failide kataloogi.',
	206 : 'Üleslaadimine katkestati turvakaalutlustel. Fail sisaldab HTMLi sarnaseid andmeid.',
	207 : 'Üleslaaditud faili nimeks pandi "%1".',
	300 : 'Faili(de) liigutamine nurjus.',
	301 : 'Faili(de) kopeerimine nurjus.',
	500 : 'Failide sirvija on turvakaalutlustel keelatud. Palun võta ühendust oma süsteemi administraatoriga ja kontrolli CKFinderi seadistusfaili.',
	501 : 'Pisipiltide tugi on keelatud.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Faili nimi ei tohi olla tühi.',
		FileExists		: 'Fail nimega %s on juba olemas.',
		FolderEmpty		: 'Kausta nimi ei tohi olla tühi.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Faili nimi ei tohi sisaldada ühtegi järgnevatest märkidest: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Faili nimi ei tohi sisaldada ühtegi järgnevatest märkidest: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Faili avamine uues aknas polnud võimalik. Palun seadista oma brauserit ning keela kõik hüpikakende blokeerijad selle saidi jaoks.',
		XmlError		: 'XML vastust veebiserverist polnud võimalik korrektselt laadida.',
		XmlEmpty		: 'XML vastust veebiserverist polnud võimalik korrektselt laadida. Serveri vastus oli tühi.',
		XmlRawResponse	: 'Serveri vastus toorkujul: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: '%s suuruse muutmine',
		sizeTooBig		: 'Pildi kõrgust ega laiust ei saa määrata suuremaks pildi esialgsest vastavast mõõtmest (%size).',
		resizeSuccess	: 'Pildi suuruse muutmine õnnestus.',
		thumbnailNew	: 'Tee uus pisipilt',
		thumbnailSmall	: 'Väike (%s)',
		thumbnailMedium	: 'Keskmine (%s)',
		thumbnailLarge	: 'Suur (%s)',
		newSize			: 'Määra uus suurus',
		width			: 'Laius',
		height			: 'Kõrgus',
		invalidHeight	: 'Sobimatu kõrgus.',
		invalidWidth	: 'Sobimatu laius.',
		invalidName		: 'Sobimatu faili nimi.',
		newImage		: 'Loo uus pilt',
		noExtensionChange : 'Faili laiendit pole võimalik muuta.',
		imageSmall		: 'Lähtepilt on liiga väike.',
		contextMenuName	: 'Muuda suurust',
		lockRatio		: 'Lukusta külgede suhe',
		resetSize		: 'Lähtesta suurus'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Salvesta',
		fileOpenError	: 'Faili avamine pole võimalik.',
		fileSaveSuccess	: 'Faili salvestamine õnnestus.',
		contextMenuName	: 'Muuda',
		loadingFile		: 'Faili laadimine, palun oota...'
	},

	Maximize :
	{
		maximize : 'Maksimeeri',
		minimize : 'Minimeeri'
	},

	Gallery :
	{
		current : 'Pilt {current}, kokku {total}'
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
