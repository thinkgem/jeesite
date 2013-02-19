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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Hungarian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['hu'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nem elérhető</span>',
		confirmCancel	: 'Az űrlap tartalma megváltozott, ám a változásokat nem rögzítette. Biztosan be szeretné zárni az űrlapot?',
		ok				: 'Rendben',
		cancel			: 'Mégsem',
		confirmationTitle	: 'Confirmation', // MISSING
		messageTitle	: 'Information', // MISSING
		inputTitle		: 'Question', // MISSING
		undo			: 'Visszavonás',
		redo			: 'Ismétlés',
		skip			: 'Skip', // MISSING
		skipAll			: 'Skip all', // MISSING
		makeDecision	: 'What action should be taken?', // MISSING
		rememberDecision: 'Remember my decision' // MISSING
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'hu',

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
	DateTime : 'yyyy. m. d. HH:MM',
	DateAmPm : ['de.', 'du.'],

	// Folders
	FoldersTitle	: 'Mappák',
	FolderLoading	: 'Betöltés...',
	FolderNew		: 'Kérjük adja meg a mappa nevét: ',
	FolderRename	: 'Kérjük adja meg a mappa új nevét: ',
	FolderDelete	: 'Biztosan törölni szeretné a következő mappát: "%1"?',
	FolderRenaming	: ' (átnevezés...)',
	FolderDeleting	: ' (törlés...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Kérjük adja meg a fájl új nevét: ',
	FileRenameExt	: 'Biztosan szeretné módosítani a fájl kiterjesztését? A fájl esetleg használhatatlan lesz.',
	FileRenaming	: 'Átnevezés...',
	FileDelete		: 'Biztosan törölni szeretné a következő fájlt: "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Betöltés...',
	FilesEmpty		: 'The folder is empty.', // MISSING
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Basket', // MISSING
	BasketClear			: 'Clear Basket', // MISSING
	BasketRemove		: 'Remove from Basket', // MISSING
	BasketOpenFolder	: 'Open Parent Folder', // MISSING
	BasketTruncateConfirm : 'Do you really want to remove all files from the basket?', // MISSING
	BasketRemoveConfirm	: 'Do you really want to remove the file "%1" from the basket?', // MISSING
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'No files in the basket, drag and drop some.', // MISSING
	BasketCopyFilesHere	: 'Copy Files from Basket', // MISSING
	BasketMoveFilesHere	: 'Move Files from Basket', // MISSING

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
	Upload		: 'Feltöltés',
	UploadTip	: 'Új fájl feltöltése',
	Refresh		: 'Frissítés',
	Settings	: 'Beállítások',
	Help		: 'Súgó',
	HelpTip		: 'Súgó (angolul)',

	// Context Menus
	Select			: 'Kiválaszt',
	SelectThumbnail : 'Bélyegkép kiválasztása',
	View			: 'Megtekintés',
	Download		: 'Letöltés',

	NewSubFolder	: 'Új almappa',
	Rename			: 'Átnevezés',
	Delete			: 'Törlés',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copy Here', // MISSING
	MoveDragDrop	: 'Move Here', // MISSING

	// Dialogs
	RenameDlgTitle		: 'Rename', // MISSING
	NewNameDlgTitle		: 'New Name', // MISSING
	FileExistsDlgTitle	: 'File Already Exists', // MISSING
	SysErrorDlgTitle : 'System Error', // MISSING

	FileOverwrite	: 'Overwrite', // MISSING
	FileAutorename	: 'Auto-rename', // MISSING
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Mégsem',
	CloseBtn	: 'Bezárás',

	// Upload Panel
	UploadTitle			: 'Új fájl feltöltése',
	UploadSelectLbl		: 'Válassza ki a feltölteni kívánt fájlt',
	UploadProgressLbl	: '(A feltöltés folyamatban, kérjük várjon...)',
	UploadBtn			: 'A kiválasztott fájl feltöltése',
	UploadBtnCancel		: 'Mégsem',

	UploadNoFileMsg		: 'Kérjük válassza ki a fájlt a számítógépéről.',
	UploadNoFolder		: 'Please select a folder before uploading.', // MISSING
	UploadNoPerms		: 'File upload not allowed.', // MISSING
	UploadUnknError		: 'Error sending the file.', // MISSING
	UploadExtIncorrect	: 'File extension not allowed in this folder.', // MISSING

	// Flash Uploads
	UploadLabel			: 'Files to Upload', // MISSING
	UploadTotalFiles	: 'Total Files:', // MISSING
	UploadTotalSize		: 'Total Size:', // MISSING
	UploadSend			: 'Feltöltés',
	UploadAddFiles		: 'Add Files', // MISSING
	UploadClearFiles	: 'Clear Files', // MISSING
	UploadCancel		: 'Cancel Upload', // MISSING
	UploadRemove		: 'Remove', // MISSING
	UploadRemoveTip		: 'Remove !f', // MISSING
	UploadUploaded		: 'Uploaded !n%', // MISSING
	UploadProcessing	: 'Processing...', // MISSING

	// Settings Panel
	SetTitle		: 'Beállítások',
	SetView			: 'Nézet:',
	SetViewThumb	: 'bélyegképes',
	SetViewList		: 'listás',
	SetDisplay		: 'Megjelenik:',
	SetDisplayName	: 'fájl neve',
	SetDisplayDate	: 'dátum',
	SetDisplaySize	: 'fájlméret',
	SetSort			: 'Rendezés:',
	SetSortName		: 'fájlnév',
	SetSortDate		: 'dátum',
	SetSortSize		: 'méret',
	SetSortExtension		: 'by Extension', // MISSING

	// Status Bar
	FilesCountEmpty : '<üres mappa>',
	FilesCountOne	: '1 fájl',
	FilesCountMany	: '%1 fájl',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'A parancsot nem sikerült végrehajtani. (Hiba: %1)',
	Errors :
	{
	 10 : 'Érvénytelen parancs.',
	 11 : 'A fájl típusa nem lett a kérés során beállítva.',
	 12 : 'A kívánt fájl típus érvénytelen.',
	102 : 'Érvénytelen fájl vagy könyvtárnév.',
	103 : 'Hitelesítési problémák miatt nem sikerült a kérést teljesíteni.',
	104 : 'Jogosultsági problémák miatt nem sikerült a kérést teljesíteni.',
	105 : 'Érvénytelen fájl kiterjesztés.',
	109 : 'Érvénytelen kérés.',
	110 : 'Ismeretlen hiba.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'A fálj vagy mappa már létezik ezen a néven.',
	116 : 'Mappa nem található. Kérjük frissítsen és próbálja újra.',
	117 : 'Fájl nem található. Kérjük frissítsen és próbálja újra.',
	118 : 'Source and target paths are equal.', // MISSING
	201 : 'Ilyen nevű fájl már létezett. A feltöltött fájl a következőre lett átnevezve: "%1".',
	202 : 'Érvénytelen fájl.',
	203 : 'Érvénytelen fájl. A fájl mérete túl nagy.',
	204 : 'A feltöltött fájl hibás.',
	205 : 'A szerveren nem található a feltöltéshez ideiglenes mappa.',
	206 : 'Upload cancelled due to security reasons. The file contains HTML-like data.', // MISSING
	207 : 'El fichero subido ha sido renombrado como "%1".',
	300 : 'Moving file(s) failed.', // MISSING
	301 : 'Copying file(s) failed.', // MISSING
	500 : 'A fájl-tallózó biztonsági okok miatt nincs engedélyezve. Kérjük vegye fel a kapcsolatot a rendszer üzemeltetőjével és ellenőrizze a CKFinder konfigurációs fájlt.',
	501 : 'A bélyegkép támogatás nincs engedélyezve.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'A fájl neve nem lehet üres.',
		FileExists		: 'File %s already exists.', // MISSING
		FolderEmpty		: 'A mappa neve nem lehet üres.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'A fájl neve nem tartalmazhatja a következő karaktereket: \n\\ / : * ? " < > |',
		FolderInvChar	: 'A mappa neve nem tartalmazhatja a következő karaktereket: \n\\ / : * ? " < > |',

		PopupBlockView	: 'A felugró ablak megnyitása nem sikerült. Kérjük ellenőrizze a böngészője beállításait és tiltsa le a felugró ablakokat blokkoló alkalmazásait erre a honlapra.',
		XmlError		: 'It was not possible to properly load the XML response from the web server.', // MISSING
		XmlEmpty		: 'It was not possible to load the XML response from the web server. The server returned an empty response.', // MISSING
		XmlRawResponse	: 'Raw response from the server: %s' // MISSING
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Resize %s', // MISSING
		sizeTooBig		: 'Cannot set image height or width to a value bigger than the original size (%size).', // MISSING
		resizeSuccess	: 'Image resized successfully.', // MISSING
		thumbnailNew	: 'Create a new thumbnail', // MISSING
		thumbnailSmall	: 'Small (%s)', // MISSING
		thumbnailMedium	: 'Medium (%s)', // MISSING
		thumbnailLarge	: 'Large (%s)', // MISSING
		newSize			: 'Set a new size', // MISSING
		width			: 'Szélesség',
		height			: 'Magasság',
		invalidHeight	: 'Invalid height.', // MISSING
		invalidWidth	: 'Invalid width.', // MISSING
		invalidName		: 'Invalid file name.', // MISSING
		newImage		: 'Create a new image', // MISSING
		noExtensionChange : 'File extension cannot be changed.', // MISSING
		imageSmall		: 'Source image is too small.', // MISSING
		contextMenuName	: 'Resize', // MISSING
		lockRatio		: 'Arány megtartása',
		resetSize		: 'Eredeti méret'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Mentés',
		fileOpenError	: 'Unable to open file.', // MISSING
		fileSaveSuccess	: 'File saved successfully.', // MISSING
		contextMenuName	: 'Edit', // MISSING
		loadingFile		: 'Loading file, please wait...' // MISSING
	},

	Maximize :
	{
		maximize : 'Teljes méret',
		minimize : 'Kis méret'
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
