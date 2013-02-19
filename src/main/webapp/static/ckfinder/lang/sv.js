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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Swedish
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['sv'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, Ej tillgänglig</span>',
		confirmCancel	: 'Några av alternativen har ändrats. Är du säker på att du vill stänga dialogrutan?',
		ok				: 'OK',
		cancel			: 'Avbryt',
		confirmationTitle	: 'Bekräftelse',
		messageTitle	: 'Information',
		inputTitle		: 'Fråga',
		undo			: 'Ångra',
		redo			: 'Gör om',
		skip			: 'Hoppa över',
		skipAll			: 'Hoppa över alla',
		makeDecision	: 'Vilken åtgärd ska utföras?',
		rememberDecision: 'Kom ihåg mitt val'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'sv',

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
	DateTime : 'yyyy-mm-dd HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Mappar',
	FolderLoading	: 'Laddar...',
	FolderNew		: 'Skriv namnet på den nya mappen: ',
	FolderRename	: 'Skriv det nya namnet på mappen: ',
	FolderDelete	: 'Är du säker på att du vill radera mappen "%1"?',
	FolderRenaming	: ' (Byter mappens namn...)',
	FolderDeleting	: ' (Raderar...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Skriv det nya filnamnet: ',
	FileRenameExt	: 'Är du säker på att du vill ändra filändelsen? Filen kan bli oanvändbar.',
	FileRenaming	: 'Byter filnamn...',
	FileDelete		: 'Är du säker på att du vill radera filen "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laddar...',
	FilesEmpty		: 'Mappen är tom.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Filkorg',
	BasketClear			: 'Rensa filkorgen',
	BasketRemove		: 'Ta bort från korgen',
	BasketOpenFolder	: 'Öppna överliggande mapp',
	BasketTruncateConfirm : 'Vill du verkligen ta bort alla filer från korgen?',
	BasketRemoveConfirm	: 'Vill du verkligen ta bort filen "%1" från korgen?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Inga filer i korgen, dra och släpp några.',
	BasketCopyFilesHere	: 'Kopiera filer från korgen',
	BasketMoveFilesHere	: 'Flytta filer från korgen',

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
	Upload		: 'Ladda upp',
	UploadTip	: 'Ladda upp en ny fil',
	Refresh		: 'Uppdatera',
	Settings	: 'Inställningar',
	Help		: 'Hjälp',
	HelpTip		: 'Hjälp',

	// Context Menus
	Select			: 'Infoga bild',
	SelectThumbnail : 'Infoga som tumnagel',
	View			: 'Visa',
	Download		: 'Ladda ner',

	NewSubFolder	: 'Ny Undermapp',
	Rename			: 'Byt namn',
	Delete			: 'Radera',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopiera hit',
	MoveDragDrop	: 'Flytta hit',

	// Dialogs
	RenameDlgTitle		: 'Byt namn',
	NewNameDlgTitle		: 'Nytt namn',
	FileExistsDlgTitle	: 'Filen finns redan',
	SysErrorDlgTitle : 'Systemfel',

	FileOverwrite	: 'Skriv över',
	FileAutorename	: 'Auto-namnändring',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Avbryt',
	CloseBtn	: 'Stäng',

	// Upload Panel
	UploadTitle			: 'Ladda upp en ny fil',
	UploadSelectLbl		: 'Välj fil att ladda upp',
	UploadProgressLbl	: '(Laddar upp filen, var god vänta...)',
	UploadBtn			: 'Ladda upp den valda filen',
	UploadBtnCancel		: 'Avbryt',

	UploadNoFileMsg		: 'Välj en fil från din dator.',
	UploadNoFolder		: 'Välj en mapp före uppladdning.',
	UploadNoPerms		: 'Filuppladdning ej tillåten.',
	UploadUnknError		: 'Fel vid filuppladdning.',
	UploadExtIncorrect	: 'Filändelsen är inte tillåten i denna mapp.',

	// Flash Uploads
	UploadLabel			: 'Filer att ladda upp',
	UploadTotalFiles	: 'Totalt antal filer:',
	UploadTotalSize		: 'Total storlek:',
	UploadSend			: 'Ladda upp',
	UploadAddFiles		: 'Lägg till filer',
	UploadClearFiles	: 'Rensa filer',
	UploadCancel		: 'Avbryt uppladdning',
	UploadRemove		: 'Ta bort',
	UploadRemoveTip		: 'Ta bort !f',
	UploadUploaded		: 'Uppladdat !n%',
	UploadProcessing	: 'Bearbetar...',

	// Settings Panel
	SetTitle		: 'Inställningar',
	SetView			: 'Visa:',
	SetViewThumb	: 'Tumnaglar',
	SetViewList		: 'Lista',
	SetDisplay		: 'Visa:',
	SetDisplayName	: 'Filnamn',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Storlek',
	SetSort			: 'Sortering:',
	SetSortName		: 'Filnamn',
	SetSortDate		: 'Datum',
	SetSortSize		: 'Storlek',
	SetSortExtension		: 'Filändelse',

	// Status Bar
	FilesCountEmpty : '<Tom Mapp>',
	FilesCountOne	: '1 fil',
	FilesCountMany	: '%1 filer',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Begäran kunde inte utföras eftersom ett fel uppstod. (Fel %1)',
	Errors :
	{
	 10 : 'Ogiltig begäran.',
	 11 : 'Resursens typ var inte specificerad i förfrågan.',
	 12 : 'Den efterfrågade resurstypen är inte giltig.',
	102 : 'Ogiltigt fil- eller mappnamn.',
	103 : 'Begäran kunde inte utföras p.g.a. restriktioner av rättigheterna.',
	104 : 'Begäran kunde inte utföras p.g.a. restriktioner av rättigheter i filsystemet.',
	105 : 'Ogiltig filändelse.',
	109 : 'Ogiltig begäran.',
	110 : 'Okänt fel.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'En fil eller mapp med aktuellt namn finns redan.',
	116 : 'Mappen kunde inte hittas. Var god uppdatera sidan och försök igen.',
	117 : 'Filen kunde inte hittas. Var god uppdatera sidan och försök igen.',
	118 : 'Sökväg till källa och mål är identisk.',
	201 : 'En fil med aktuellt namn fanns redan. Den uppladdade filen har döpts om till "%1".',
	202 : 'Ogiltig fil.',
	203 : 'Ogiltig fil. Filen var för stor.',
	204 : 'Den uppladdade filen var korrupt.',
	205 : 'En tillfällig mapp för uppladdning är inte tillgänglig på servern.',
	206 : 'Uppladdningen stoppades av säkerhetsskäl. Filen innehåller HTML-liknande data.',
	207 : 'Den uppladdade filen har döpts om till "%1".',
	300 : 'Flytt av fil(er) misslyckades.',
	301 : 'Kopiering av fil(er) misslyckades.',
	500 : 'Filhanteraren har stoppats av säkerhetsskäl. Var god kontakta administratören för att kontrollera konfigurationsfilen för CKFinder.',
	501 : 'Stöd för tumnaglar har stängts av.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Filnamnet får inte vara tomt.',
		FileExists		: 'Filen %s finns redan.',
		FolderEmpty		: 'Mappens namn får inte vara tomt.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Filnamnet får inte innehålla något av följande tecken: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Mappens namn får inte innehålla något av följande tecken: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Det gick inte att öppna filen i ett nytt fönster. Ändra inställningarna i din webbläsare så att den tillåter popup-fönster på den här webbplatsen.',
		XmlError		: 'Det gick inte att ladda XML-svaret från webbservern ordentligt.',
		XmlEmpty		: 'Det gick inte att ladda XML-svaret från webbservern. Servern returnerade ett tomt svar.',
		XmlRawResponse	: 'Svar från servern: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Storleksändra %s',
		sizeTooBig		: 'Bildens höjd eller bredd kan inte vara större än originalfilens storlek (%size).',
		resizeSuccess	: 'Storleksändring lyckades.',
		thumbnailNew	: 'Skapa en ny tumnagel',
		thumbnailSmall	: 'Liten (%s)',
		thumbnailMedium	: 'Mellan (%s)',
		thumbnailLarge	: 'Stor (%s)',
		newSize			: 'Välj en ny storlek',
		width			: 'Bredd',
		height			: 'Höjd',
		invalidHeight	: 'Ogiltig höjd.',
		invalidWidth	: 'Ogiltig bredd.',
		invalidName		: 'Ogiltigt filnamn.',
		newImage		: 'Skapa en ny bild',
		noExtensionChange : 'Filändelsen kan inte ändras.',
		imageSmall		: 'Originalbilden är för liten.',
		contextMenuName	: 'Ändra storlek',
		lockRatio		: 'Lås höjd/bredd förhållanden',
		resetSize		: 'Återställ storlek'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Spara',
		fileOpenError	: 'Kan inte öppna filen.',
		fileSaveSuccess	: 'Filen sparades.',
		contextMenuName	: 'Redigera',
		loadingFile		: 'Laddar fil, var god vänta...'
	},

	Maximize :
	{
		maximize : 'Maximera',
		minimize : 'Minimera'
	},

	Gallery :
	{
		current : 'Bild {current} av {total}'
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
