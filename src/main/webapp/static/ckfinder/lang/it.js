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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Italian
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['it'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, non disponibile</span>',
		confirmCancel	: 'Alcune delle opzioni sono state cambiate. Sei sicuro di voler chiudere la finestra di dialogo?',
		ok				: 'OK',
		cancel			: 'Annulla',
		confirmationTitle	: 'Confermare',
		messageTitle	: 'Informazione',
		inputTitle		: 'Domanda',
		undo			: 'Annulla',
		redo			: 'Ripristina',
		skip			: 'Ignora',
		skipAll			: 'Ignora tutti',
		makeDecision	: 'Che azione prendere?',
		rememberDecision: 'Ricorda mia decisione'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'it',

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
	DateTime : 'dd/mm/yyyy HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Cartelle',
	FolderLoading	: 'Caricando...',
	FolderNew		: 'Nome della cartella: ',
	FolderRename	: 'Nuovo nome della cartella: ',
	FolderDelete	: 'Se sicuro di voler eliminare la cartella "%1"?',
	FolderRenaming	: ' (Rinominando...)',
	FolderDeleting	: ' (Eliminando...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Nuovo nome del file: ',
	FileRenameExt	: 'Sei sicure di voler cambiare la estensione del file? Il file può risultare inusabile.',
	FileRenaming	: 'Rinominando...',
	FileDelete		: 'Sei sicuro di voler eliminare il file "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Caricamento in corso...',
	FilesEmpty		: 'Cartella vuota',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Cestino',
	BasketClear			: 'Svuota Cestino',
	BasketRemove		: 'Rimuove dal Cestino',
	BasketOpenFolder	: 'Apre Cartella Superiore',
	BasketTruncateConfirm : 'Sei sicuro di voler svuotare il cestino?',
	BasketRemoveConfirm	: 'Sei sicuro di voler rimuovere il file "%1" dal cestino?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Nessun file nel cestino, si deve prima trascinare qualcuno.',
	BasketCopyFilesHere	: 'Copia i File dal Cestino',
	BasketMoveFilesHere	: 'Muove i File dal Cestino',

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
	Upload		: 'Upload',
	UploadTip	: 'Carica Nuovo File',
	Refresh		: 'Aggiorna',
	Settings	: 'Configurazioni',
	Help		: 'Aiuto',
	HelpTip		: 'Aiuto (Inglese)',

	// Context Menus
	Select			: 'Seleziona',
	SelectThumbnail : 'Seleziona la miniatura',
	View			: 'Vedi',
	Download		: 'Scarica',

	NewSubFolder	: 'Nuova Sottocartella',
	Rename			: 'Rinomina',
	Delete			: 'Elimina',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copia qui',
	MoveDragDrop	: 'Muove qui',

	// Dialogs
	RenameDlgTitle		: 'Rinomina',
	NewNameDlgTitle		: 'Nuovo nome',
	FileExistsDlgTitle	: 'Il file già esiste',
	SysErrorDlgTitle : 'Errore di Sistema',

	FileOverwrite	: 'Sovrascrivere',
	FileAutorename	: 'Rinomina automaticamente',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Anulla',
	CloseBtn	: 'Chiudi',

	// Upload Panel
	UploadTitle			: 'Carica Nuovo File',
	UploadSelectLbl		: 'Seleziona il file',
	UploadProgressLbl	: '(Caricamento in corso, attendere prego...)',
	UploadBtn			: 'Carica File',
	UploadBtnCancel		: 'Annulla',

	UploadNoFileMsg		: 'Seleziona il file da caricare',
	UploadNoFolder		: 'Seleziona il file prima di caricare.',
	UploadNoPerms		: 'Non è permesso il caricamento di file.',
	UploadUnknError		: 'Errore nel caricamento del file.',
	UploadExtIncorrect	: 'In questa cartella non sono permessi file con questa estensione.',

	// Flash Uploads
	UploadLabel			: 'File da Caricare',
	UploadTotalFiles	: 'File:',
	UploadTotalSize		: 'Dimensione:',
	UploadSend			: 'Upload',
	UploadAddFiles		: 'Aggiungi File',
	UploadClearFiles	: 'Elimina File',
	UploadCancel		: 'Annulla il Caricamento',
	UploadRemove		: 'Rimuovi',
	UploadRemoveTip		: 'Rimuove !f',
	UploadUploaded		: '!n% caricato',
	UploadProcessing	: 'Attendere...',

	// Settings Panel
	SetTitle		: 'Configurazioni',
	SetView			: 'Vedi:',
	SetViewThumb	: 'Anteprima',
	SetViewList		: 'Lista',
	SetDisplay		: 'Informazioni:',
	SetDisplayName	: 'Nome del File',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Dimensione',
	SetSort			: 'Ordina:',
	SetSortName		: 'per Nome',
	SetSortDate		: 'per Data',
	SetSortSize		: 'per Dimensione',
	SetSortExtension		: 'per Estensione',

	// Status Bar
	FilesCountEmpty : '<Nessun file>',
	FilesCountOne	: '1 file',
	FilesCountMany	: '%1 file',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Impossibile completare la richiesta. (Errore %1)',
	Errors :
	{
	 10 : 'Commando non valido.',
	 11 : 'Il tipo di risorsa non è stato specificato nella richiesta.',
	 12 : 'Il tipo di risorsa richiesto non è valido.',
	102 : 'Nome di file o cartella non valido.',
	103 : 'Non è stato possibile completare la richiesta a causa di restrizioni di autorizazione.',
	104 : 'Non è stato possibile completare la richiesta a causa di restrizioni nei permessi del file system.',
	105 : 'L\'estensione del file non è valida.',
	109 : 'Richiesta invalida.',
	110 : 'Errore sconosciuto.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Un file o cartella con lo stesso nome è già esistente.',
	116 : 'Cartella non trovata. Prego aggiornare e riprovare.',
	117 : 'File non trovato. Prego aggirnare la lista dei file e riprovare.',
	118 : 'Il percorso di origine e di destino sono uguali.',
	201 : 'Un file con lo stesso nome è già disponibile. Il file caricato è stato rinominato in "%1".',
	202 : 'File invalido.',
	203 : 'File invalido. La dimensione del file eccede i limiti del sistema.',
	204 : 'Il file caricato è corrotto.',
	205 : 'Il folder temporario non è disponibile new server.',
	206 : 'Upload annullato per motivi di sicurezza. Il file contiene dati in formatto HTML.',
	207 : 'Il file caricato è stato rinominato a "%1".',
	300 : 'Non è stato possibile muovere i file.',
	301 : 'Non è stato possibile copiare i file.',
	500 : 'Questo programma è disabilitato per motivi di sicurezza. Prego contattare l\'amministratore del sistema e verificare le configurazioni di CKFinder.',
	501 : 'Il supporto alle anteprime non è attivo.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Il nome del file non può essere vuoto.',
		FileExists		: 'File %s già esiste.',
		FolderEmpty		: 'Il nome della cartella non può essere vuoto.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'I seguenti caratteri non possono essere usati per comporre il nome del file: \n\\ / : * ? " < > |',
		FolderInvChar	: 'I seguenti caratteri non possono essere usati per comporre il nome della cartella: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Non è stato possile aprire il file in una nuova finestra. Prego configurare il browser e disabilitare i blocchi delle popup.',
		XmlError		: 'Non è stato possibile caricare la risposta XML dal server.',
		XmlEmpty		: 'Non è stato possibile caricare la risposta XML dal server. La risposta è vuota.',
		XmlRawResponse	: 'Risposta originale inviata dal server: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Ridimensiona %s',
		sizeTooBig		: 'Non si può usare valori di altezza e larghezza che siano maggiore che le dimensioni originali (%size).',
		resizeSuccess	: 'Immagine ridimensionata.',
		thumbnailNew	: 'Crea una nuova thumbnail',
		thumbnailSmall	: 'Piccolo (%s)',
		thumbnailMedium	: 'Medio (%s)',
		thumbnailLarge	: 'Grande (%s)',
		newSize			: 'Nuove dimensioni',
		width			: 'Larghezza',
		height			: 'Altezza',
		invalidHeight	: 'Altezza non valida.',
		invalidWidth	: 'Larghezza non valida.',
		invalidName		: 'Nome del file non valido.',
		newImage		: 'Crea nuova immagine',
		noExtensionChange : 'L\'estensione del file non può essere cambiata.',
		imageSmall		: 'L\'immagine originale è molto piccola.',
		contextMenuName	: 'Ridimensiona',
		lockRatio		: 'Blocca rapporto',
		resetSize		: 'Reimposta dimensione'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Salva',
		fileOpenError	: 'Non è stato possibile aprire il file.',
		fileSaveSuccess	: 'File salvato.',
		contextMenuName	: 'Modifica',
		loadingFile		: 'Attendere prego. Caricamento del file in corso...'
	},

	Maximize :
	{
		maximize : 'Massimizza',
		minimize : 'Minimizza'
	},

	Gallery :
	{
		current : 'Immagine {current} di {total}'
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
