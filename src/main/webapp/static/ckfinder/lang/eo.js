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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Esperanto
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['eo'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nedisponebla</span>',
		confirmCancel	: 'Iuj opcioj estas modifitaj. Ĉu vi certas, ke vi volas fermi tiun fenestron?',
		ok				: 'Bone',
		cancel			: 'Rezigni',
		confirmationTitle	: 'Konfirmo',
		messageTitle	: 'Informo',
		inputTitle		: 'Demando',
		undo			: 'Malfari',
		redo			: 'Refari',
		skip			: 'Transsalti',
		skipAll			: 'Transsalti ĉion',
		makeDecision	: 'Kiun agon elekti?',
		rememberDecision: 'Memori la decidon'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'eo',

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
	DateTime : 'dd/mm/yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Dosierujoj',
	FolderLoading	: 'Estas ŝargata...',
	FolderNew		: 'Bonvolu entajpi la nomon de la nova dosierujo: ',
	FolderRename	: 'Bonvolu entajpi la novan nomon de la dosierujo: ',
	FolderDelete	: 'Ĉu vi certas, ke vi volas forigi la "%1"dosierujon?',
	FolderRenaming	: ' (Estas renomata...)',
	FolderDeleting	: ' (Estas forigata...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Entajpu la novan nomon de la dosiero: ',
	FileRenameExt	: 'Ĉu vi certas, ke vi volas ŝanĝi la dosiernoman finaĵon? La dosiero povus fariĝi neuzebla.',
	FileRenaming	: 'Estas renomata...',
	FileDelete		: 'Ĉu vi certas, ke vi volas forigi la dosieron "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Estas ŝargata...',
	FilesEmpty		: 'La dosierujo estas malplena',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Rubujo',
	BasketClear			: 'Malplenigi la rubujon',
	BasketRemove		: 'Repreni el la rubujo',
	BasketOpenFolder	: 'Malfermi la patran dosierujon',
	BasketTruncateConfirm : 'Ĉu vi certas, ke vi volas forigi ĉiujn dosierojn el la rubujo?',
	BasketRemoveConfirm	: 'Ĉu vi certas, ke vi volas forigi la dosieron  "%1" el la rubujo?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Neniu dosiero en la rubujo, demetu kelkajn.',
	BasketCopyFilesHere	: 'Kopii dosierojn el la rubujo',
	BasketMoveFilesHere	: 'Movi dosierojn el la rubujo',

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
	Upload		: 'Alŝuti',
	UploadTip	: 'Alŝuti novan dosieron',
	Refresh		: 'Aktualigo',
	Settings	: 'Agordo',
	Help		: 'Helpilo',
	HelpTip		: 'Helpilo',

	// Context Menus
	Select			: 'Selekti',
	SelectThumbnail : 'Selekti miniaturon',
	View			: 'Vidi',
	Download		: 'Elŝuti',

	NewSubFolder	: 'Nova subdosierujo',
	Rename			: 'Renomi',
	Delete			: 'Forigi',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopii tien ĉi',
	MoveDragDrop	: 'Movi tien ĉi',

	// Dialogs
	RenameDlgTitle		: 'Renomi',
	NewNameDlgTitle		: 'Nova dosiero',
	FileExistsDlgTitle	: 'Dosiero jam ekzistas',
	SysErrorDlgTitle : 'Sistemeraro',

	FileOverwrite	: 'Anstataŭigi',
	FileAutorename	: 'Aŭtomata renomo',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Bone',
	CancelBtn	: 'Rezigni',
	CloseBtn	: 'Fermi',

	// Upload Panel
	UploadTitle			: 'Alŝuti novan dosieron',
	UploadSelectLbl		: 'Selekti la alŝutotan dosieron',
	UploadProgressLbl	: '(Estas alŝutata, bonvolu pacienci...)',
	UploadBtn			: 'Alŝuti la selektitan dosieron',
	UploadBtnCancel		: 'Rezigni',

	UploadNoFileMsg		: 'Selekti dosieron el via komputilo.',
	UploadNoFolder		: 'Bonvolu selekti dosierujon antaŭ la alŝuto.',
	UploadNoPerms		: 'La dosieralŝuto ne estas permesita.',
	UploadUnknError		: 'Eraro dum la dosieralŝuto.',
	UploadExtIncorrect	: 'La dosiernoma finaĵo ne estas permesita en tiu  dosierujo.',

	// Flash Uploads
	UploadLabel			: 'Alŝutotaj dosieroj',
	UploadTotalFiles	: 'Dosieroj:',
	UploadTotalSize		: 'Grando de la dosieroj:',
	UploadSend			: 'Alŝuti',
	UploadAddFiles		: 'Almeti dosierojn',
	UploadClearFiles	: 'Forigi dosierojn',
	UploadCancel		: 'Rezigni la alŝuton',
	UploadRemove		: 'Forigi',
	UploadRemoveTip		: 'Forigi !f',
	UploadUploaded		: 'Alŝutita !n%',
	UploadProcessing	: 'Estas alŝutata...',

	// Settings Panel
	SetTitle		: 'Agordo',
	SetView			: 'Vidi:',
	SetViewThumb	: 'Miniaturoj',
	SetViewList		: 'Listo',
	SetDisplay		: 'Vidigi:',
	SetDisplayName	: 'Dosiernomo',
	SetDisplayDate	: 'Dato',
	SetDisplaySize	: 'Dosiergrando',
	SetSort			: 'Ordigo:',
	SetSortName		: 'laŭ dosiernomo',
	SetSortDate		: 'laŭ dato',
	SetSortSize		: 'laŭ grando',
	SetSortExtension		: 'laŭ dosiernoma finaĵo',

	// Status Bar
	FilesCountEmpty : '<Malplena dosiero>',
	FilesCountOne	: '1 dosiero',
	FilesCountMany	: '%1 dosieroj',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Ne eblis plenumi la peton. (Eraro %1)',
	Errors :
	{
	 10 : 'Nevalida komando.',
	 11 : 'La risurctipo ne estas indikita en la komando.',
	 12 : 'La risurctipo ne estas valida.',
	102 : 'La dosier- aŭ dosierujnomo ne estas valida.',
	103 : 'Ne eblis plenumi la peton pro rajtaj limigoj.',
	104 : 'Ne eblis plenumi la peton pro atingopermesaj limigoj.',
	105 : 'Nevalida dosiernoma finaĵo.',
	109 : 'Nevalida peto.',
	110 : 'Nekonata eraro.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Dosiero aŭ dosierujo kun tiu nomo jam ekzistas.',
	116 : 'Tiu dosierujo ne ekzistas. Bonvolu aktualigi kaj reprovi.',
	117 : 'Tiu dosiero ne ekzistas. Bonvolu aktualigi kaj reprovi.',
	118 : 'La vojoj al la fonto kaj al la celo estas samaj.',
	201 : 'Dosiero kun la sama nomo jam ekzistas. La alŝutita dosiero estas renomita al "%1".',
	202 : 'Nevalida dosiero.',
	203 : 'Nevalida dosiero. La grando estas tro alta.',
	204 : 'La alŝutita dosiero estas difektita.',
	205 : 'Neniu provizora dosierujo estas disponebla por alŝuto al la servilo.',
	206 : 'Alŝuto nuligita pro kialoj pri sekureco. La dosiero entenas datenojn de HTMLtipo.',
	207 : 'La alŝutita dosiero estas renomita al "%1".',
	300 : 'La movo de la dosieroj malsukcesis.',
	301 : 'La kopio de la dosieroj malsukcesis.',
	500 : 'La dosieradministra sistemo estas malvalidigita. Kontaktu vian administranton kaj kontrolu la agordodosieron de CKFinder.',
	501 : 'La eblo de miniaturoj estas malvalidigita.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'La dosiernomo ne povas esti malplena.',
		FileExists		: 'La dosiero %s jam ekzistas.',
		FolderEmpty		: 'La dosierujnomo ne povas esti malplena.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'La dosiernomo ne povas enhavi la sekvajn signojn : \n\\ / : * ? " < > |',
		FolderInvChar	: 'La dosierujnomo ne povas enhavi la sekvajn signojn : \n\\ / : * ? " < > |',

		PopupBlockView	: 'Ne eblis malfermi la dosieron en nova fenestro. Agordu vian retumilon kaj malŝaltu vian ŝprucfenestran blokilon por tiu retpaĝaro.',
		XmlError		: 'Ne eblis kontentige elŝuti la XML respondon el la  servilo.',
		XmlEmpty		: 'Ne eblis elŝuti la XML respondon el la servilo. La servilo resendis malplenan respondon.',
		XmlRawResponse	: 'Kruda respondo el la servilo: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Plimalpligrandigi %s',
		sizeTooBig		: 'Ne eblas ŝanĝi la alton aŭ larĝon de tiu bildo ĝis valoro pli granda ol la origina grando (%size).',
		resizeSuccess	: 'La bildgrando estas sukcese ŝanĝita.',
		thumbnailNew	: 'Krei novan miniaturon',
		thumbnailSmall	: 'Malgranda (%s)',
		thumbnailMedium	: 'Meza (%s)',
		thumbnailLarge	: 'Granda (%s)',
		newSize			: 'Fiksi la novajn grando-erojn',
		width			: 'Larĝo',
		height			: 'Alto',
		invalidHeight	: 'Nevalida alto.',
		invalidWidth	: 'Nevalida larĝo.',
		invalidName		: 'Nevalida dosiernomo.',
		newImage		: 'Krei novan bildon',
		noExtensionChange : 'Ne eblas ŝanĝi la dosiernoman finaĵon.',
		imageSmall		: 'La bildo estas tro malgranda',
		contextMenuName	: 'Ŝanĝi la grandon',
		lockRatio		: 'Konservi proporcion',
		resetSize		: 'Origina grando'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Konservi',
		fileOpenError	: 'Ne eblas malfermi la dosieron',
		fileSaveSuccess	: 'La dosiero estas sukcese konservita.',
		contextMenuName	: 'Redakti',
		loadingFile		: 'La dosiero estas elŝutata, bonvolu pacienci...'
	},

	Maximize :
	{
		maximize : 'Pligrandigi',
		minimize : 'Malpligrandigi'
	},

	Gallery :
	{
		current : 'Bildo {current} el {total}'
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
