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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Dutch
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['nl'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, niet beschikbaar</span>',
		confirmCancel	: 'Enkele opties zijn gewijzigd. Weet u zeker dat u dit dialoogvenster wilt sluiten?',
		ok				: 'OK',
		cancel			: 'Annuleren',
		confirmationTitle	: 'Bevestigen',
		messageTitle	: 'Informatie',
		inputTitle		: 'Vraag',
		undo			: 'Ongedaan maken',
		redo			: 'Opnieuw uitvoeren',
		skip			: 'Overslaan',
		skipAll			: 'Alles overslaan',
		makeDecision	: 'Welke actie moet uitgevoerd worden?',
		rememberDecision: 'Onthoud mijn keuze'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'nl',

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
	DateTime : 'd-m-yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Mappen',
	FolderLoading	: 'Laden...',
	FolderNew		: 'Vul de mapnaam in: ',
	FolderRename	: 'Vul de nieuwe mapnaam in: ',
	FolderDelete	: 'Weet je het zeker dat je de map "%1" wilt verwijderen?',
	FolderRenaming	: ' (Aanpassen...)',
	FolderDeleting	: ' (Verwijderen...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Vul de nieuwe bestandsnaam in: ',
	FileRenameExt	: 'Weet je zeker dat je de extensie wilt wijzigen? Het bestand kan onbruikbaar worden.',
	FileRenaming	: 'Aanpassen...',
	FileDelete		: 'Weet je zeker dat je het bestand "%1" wilt verwijderen?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Laden...',
	FilesEmpty		: 'De map is leeg.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Mandje',
	BasketClear			: 'Mandje legen',
	BasketRemove		: 'Verwijder uit het mandje',
	BasketOpenFolder	: 'Bovenliggende map openen',
	BasketTruncateConfirm : 'Weet je zeker dat je alle bestand uit het mandje wilt verwijderen?',
	BasketRemoveConfirm	: 'Weet je zeker dat je het bestand "%1" uit het mandje wilt verwijderen?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Geen bestanden in het mandje, sleep bestanden hierheen.',
	BasketCopyFilesHere	: 'Bestanden kopiëren uit het mandje',
	BasketMoveFilesHere	: 'Bestanden verplaatsen uit het mandje',

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
	Upload		: 'Uploaden',
	UploadTip	: 'Nieuw bestand uploaden',
	Refresh		: 'Vernieuwen',
	Settings	: 'Instellingen',
	Help		: 'Help',
	HelpTip		: 'Help',

	// Context Menus
	Select			: 'Selecteer',
	SelectThumbnail : 'Selecteer miniatuurafbeelding',
	View			: 'Bekijken',
	Download		: 'Downloaden',

	NewSubFolder	: 'Nieuwe onderliggende map',
	Rename			: 'Naam wijzigen',
	Delete			: 'Verwijderen',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Hierheen kopiëren',
	MoveDragDrop	: 'Hierheen verplaatsen',

	// Dialogs
	RenameDlgTitle		: 'Naam wijzigen',
	NewNameDlgTitle		: 'Nieuwe naam',
	FileExistsDlgTitle	: 'Bestand bestaat al',
	SysErrorDlgTitle : 'Systeemfout',

	FileOverwrite	: 'Overschrijven',
	FileAutorename	: 'Automatisch hernoemen',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Annuleren',
	CloseBtn	: 'Sluiten',

	// Upload Panel
	UploadTitle			: 'Nieuw bestand uploaden',
	UploadSelectLbl		: 'Selecteer het bestand om te uploaden',
	UploadProgressLbl	: '(Bezig met uploaden, even geduld a.u.b...)',
	UploadBtn			: 'Upload geselecteerde bestand',
	UploadBtnCancel		: 'Annuleren',

	UploadNoFileMsg		: 'Kies een bestand van je computer.',
	UploadNoFolder		: 'Selecteer a.u.b. een map voordat je gaat uploaden.',
	UploadNoPerms		: 'Uploaden bestand niet toegestaan.',
	UploadUnknError		: 'Fout bij het versturen van het bestand.',
	UploadExtIncorrect	: 'Bestandsextensie is niet toegestaan in deze map.',

	// Flash Uploads
	UploadLabel			: 'Te uploaden bestanden',
	UploadTotalFiles	: 'Totaal aantal bestanden:',
	UploadTotalSize		: 'Totale grootte:',
	UploadSend			: 'Uploaden',
	UploadAddFiles		: 'Bestanden toevoegen',
	UploadClearFiles	: 'Bestanden wissen',
	UploadCancel		: 'Upload annuleren',
	UploadRemove		: 'Verwijderen',
	UploadRemoveTip		: 'Verwijder !f',
	UploadUploaded		: '!n% geüpload',
	UploadProcessing	: 'Verwerken...',

	// Settings Panel
	SetTitle		: 'Instellingen',
	SetView			: 'Bekijken:',
	SetViewThumb	: 'Miniatuurafbeelding',
	SetViewList		: 'Lijst',
	SetDisplay		: 'Weergave:',
	SetDisplayName	: 'Bestandsnaam',
	SetDisplayDate	: 'Datum',
	SetDisplaySize	: 'Bestandsgrootte',
	SetSort			: 'Sorteren op:',
	SetSortName		: 'Op bestandsnaam',
	SetSortDate		: 'Op datum',
	SetSortSize		: 'Op grootte',
	SetSortExtension		: 'Op bestandsextensie',

	// Status Bar
	FilesCountEmpty : '<Lege map>',
	FilesCountOne	: '1 bestand',
	FilesCountMany	: '%1 bestanden',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Het was niet mogelijk om deze actie uit te voeren. (Fout %1)',
	Errors :
	{
	 10 : 'Ongeldig commando.',
	 11 : 'Het bestandstype komt niet voor in de aanvraag.',
	 12 : 'Het gevraagde brontype is niet geldig.',
	102 : 'Ongeldige bestands- of mapnaam.',
	103 : 'Het verzoek kon niet worden voltooid vanwege autorisatie beperkingen.',
	104 : 'Het verzoek kon niet worden voltooid door beperkingen in de rechten op het bestandssysteem.',
	105 : 'Ongeldige bestandsextensie.',
	109 : 'Ongeldige aanvraag.',
	110 : 'Onbekende fout.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Er bestaat al een bestand of map met deze naam.',
	116 : 'Map niet gevonden, vernieuw de mappenlijst of kies een andere map.',
	117 : 'Bestand niet gevonden, vernieuw de mappenlijst of kies een andere map.',
	118 : 'Bron- en doelmap zijn gelijk.',
	201 : 'Er bestaat al een bestand met dezelfde naam. Het geüploade bestand is hernoemd naar: "%1".',
	202 : 'Ongeldige bestand.',
	203 : 'Ongeldige bestand. Het bestand is te groot.',
	204 : 'De geüploade file is kapot.',
	205 : 'Er is geen hoofdmap gevonden.',
	206 : 'Het uploaden van het bestand is om veiligheidsredenen afgebroken. Er is HTML code in het bestand aangetroffen.',
	207 : 'Het geüploade bestand is hernoemd naar: "%1".',
	300 : 'Bestand(en) verplaatsen is mislukt.',
	301 : 'Bestand(en) kopiëren is mislukt.',
	500 : 'Het uploaden van een bestand is momenteel niet mogelijk. Contacteer de beheerder en controleer het CKFinder configuratiebestand.',
	501 : 'De ondersteuning voor miniatuurafbeeldingen is uitgeschakeld.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'De bestandsnaam mag niet leeg zijn.',
		FileExists		: 'Bestand %s bestaat al.',
		FolderEmpty		: 'De mapnaam mag niet leeg zijn.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'De bestandsnaam mag de volgende tekens niet bevatten: \n\\ / : * ? " < > |',
		FolderInvChar	: 'De mapnaam mag de volgende tekens niet bevatten: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Het was niet mogelijk om dit bestand in een nieuw venster te openen. Configureer de browser zodat het de popups van deze jeesite niet blokkeert.',
		XmlError		: 'Het is niet gelukt om de XML van de webserver te laden.',
		XmlEmpty		: 'Het is niet gelukt om de XML van de webserver te laden. De server gaf een leeg resultaat terug.',
		XmlRawResponse	: 'Origineel resultaat van de server: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: '%s herschalen',
		sizeTooBig		: 'Het is niet mogelijk om een breedte of hoogte in te stellen die groter is dan de originele afmetingen (%size).',
		resizeSuccess	: 'De afbeelding is met succes herschaald.',
		thumbnailNew	: 'Miniatuurafbeelding maken',
		thumbnailSmall	: 'Klein (%s)',
		thumbnailMedium	: 'Medium (%s)',
		thumbnailLarge	: 'Groot (%s)',
		newSize			: 'Nieuwe afmetingen instellen',
		width			: 'Breedte',
		height			: 'Hoogte',
		invalidHeight	: 'Ongeldige hoogte.',
		invalidWidth	: 'Ongeldige breedte.',
		invalidName		: 'Ongeldige bestandsnaam.',
		newImage		: 'Nieuwe afbeelding maken',
		noExtensionChange : 'De bestandsextensie kan niet worden gewijzigd.',
		imageSmall		: 'Bronafbeelding is te klein.',
		contextMenuName	: 'Herschalen',
		lockRatio		: 'Afmetingen vergrendelen',
		resetSize		: 'Afmetingen resetten'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Opslaan',
		fileOpenError	: 'Kan het bestand niet openen.',
		fileSaveSuccess	: 'Bestand is succesvol opgeslagen.',
		contextMenuName	: 'Wijzigen',
		loadingFile		: 'Bestand laden, even geduld a.u.b...'
	},

	Maximize :
	{
		maximize : 'Maximaliseren',
		minimize : 'Minimaliseren'
	},

	Gallery :
	{
		current : 'Afbeelding {current} van {total}'
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
