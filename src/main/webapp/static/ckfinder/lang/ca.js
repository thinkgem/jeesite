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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Catalan
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['ca'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, no disponible</span>',
		confirmCancel	: 'Algunes opcions s\'han canviat\r\nEstàs segur de tancar el quadre de diàleg?',
		ok				: 'Acceptar',
		cancel			: 'Cancel·lar',
		confirmationTitle	: 'Confirmació',
		messageTitle	: 'Informació',
		inputTitle		: 'Pregunta',
		undo			: 'Desfer',
		redo			: 'Refer',
		skip			: 'Ometre',
		skipAll			: 'Ometre tots',
		makeDecision	: 'Quina acció s\'ha de realitzar?',
		rememberDecision: 'Recordar la meva decisió'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'ca',
	LangCode : 'ca',

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
	FoldersTitle	: 'Carpetes',
	FolderLoading	: 'Carregant...',
	FolderNew		: 'Si us plau, escriu el nom per la nova carpeta: ',
	FolderRename	: 'Si us plau, escriu el nom per la carpeta: ',
	FolderDelete	: 'Estàs segur que vols esborrar la carpeta "%1"?',
	FolderRenaming	: ' (Canviant el nom...)',
	FolderDeleting	: ' (Esborrant...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Si us plau, escriu el nom del fitxer: ',
	FileRenameExt	: 'Estàs segur de canviar la extensió del fitxer? El fitxer pot quedar inservible.',
	FileRenaming	: 'Canviant el nom...',
	FileDelete		: 'Estàs segur d\'esborrar el fitxer "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Carregant...',
	FilesEmpty		: 'Carpeta buida',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Cistella',
	BasketClear			: 'Buidar la cistella',
	BasketRemove		: 'Treure de la cistella',
	BasketOpenFolder	: 'Obrir carpeta pare',
	BasketTruncateConfirm : 'Estàs segur de treure tots els fitxers de la cistella?',
	BasketRemoveConfirm	: 'Estàs segur de treure el fitxer "%1" de la cistella?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'No hi ha fitxers a la cistella, arrossega i deixa anar alguns.',
	BasketCopyFilesHere	: 'Copiar fitxers de la cistella',
	BasketMoveFilesHere	: 'Moure fitxers de la cistella',

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
	Upload		: 'Afegir',
	UploadTip	: 'Afegir nou fitxer',
	Refresh		: 'Actualitzar',
	Settings	: 'Configuració',
	Help		: 'Ajuda',
	HelpTip		: 'Ajuda',

	// Context Menus
	Select			: 'Seleccionar',
	SelectThumbnail : 'Seleccionar la icona',
	View			: 'Veure',
	Download		: 'Descarregar',

	NewSubFolder	: 'Nova Subcarpeta',
	Rename			: 'Canviar el nom',
	Delete			: 'Esborrar',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copiar aquí',
	MoveDragDrop	: 'Moure aquí',

	// Dialogs
	RenameDlgTitle		: 'Canviar el nom',
	NewNameDlgTitle		: 'Nou nom',
	FileExistsDlgTitle	: 'Fitxer existent',
	SysErrorDlgTitle : 'Error de sistema',

	FileOverwrite	: 'Sobreescriure',
	FileAutorename	: 'Auto-renombrar',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Acceptar',
	CancelBtn	: 'Cancel·lar',
	CloseBtn	: 'Tancar',

	// Upload Panel
	UploadTitle			: 'Afegir nou fitxer',
	UploadSelectLbl		: 'Triar el fitxer a pujar',
	UploadProgressLbl	: '(Pujada en progrés, si us plau esperi...)',
	UploadBtn			: 'Pujar el fitxer escollit',
	UploadBtnCancel		: 'Cancel·lar',

	UploadNoFileMsg		: 'Si us plau, escull un fitxer del teu ordinador.',
	UploadNoFolder		: 'Si us plau, escull la carpeta abans d\'iniciar la pujada.',
	UploadNoPerms		: 'No pot pujar fitxers.',
	UploadUnknError		: 'Error enviant el fitxer.',
	UploadExtIncorrect	: 'La extensió del fitxer no està permesa en aquesta carpeta.',

	// Flash Uploads
	UploadLabel			: 'Fitxers a pujar',
	UploadTotalFiles	: 'Total de fitxers:',
	UploadTotalSize		: 'Grandària total:',
	UploadSend			: 'Afegir',
	UploadAddFiles		: 'Afegir fitxers',
	UploadClearFiles	: 'Esborrar fitxers',
	UploadCancel		: 'Cancel·lar la pujada',
	UploadRemove		: 'Treure',
	UploadRemoveTip		: 'Treure !f',
	UploadUploaded		: 'Enviat !n%',
	UploadProcessing	: 'Processant...',

	// Settings Panel
	SetTitle		: 'Configuració',
	SetView			: 'Vista:',
	SetViewThumb	: 'Icones',
	SetViewList		: 'Llista',
	SetDisplay		: 'Mostrar:',
	SetDisplayName	: 'Nom del fitxer',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Grandària del fitxer',
	SetSort			: 'Ordenar:',
	SetSortName		: 'per Nom',
	SetSortDate		: 'per Data',
	SetSortSize		: 'per Grandària',
	SetSortExtension		: 'per Extensió',

	// Status Bar
	FilesCountEmpty : '<Carpeta buida>',
	FilesCountOne	: '1 fitxer',
	FilesCountMany	: '%1 fitxers',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'No ha estat possible completar la solicitut. (Error %1)',
	Errors :
	{
	 10 : 'Ordre incorrecte.',
	 11 : 'El tipus de recurs no ha estat especificat a la solicitut.',
	 12 : 'El tipus de recurs solicitat no és vàlid.',
	102 : 'Nom de fitxer o carpeta no vàlids.',
	103 : 'No s\'ha pogut completar la solicitut degut a les restriccions d\'autorització.',
	104 : 'No s\'ha pogut completar la solicitut degut a les restriccions en el sistema de fitxers.',
	105 : 'La extensió del fitxer no es vàlida.',
	109 : 'Petició invàlida.',
	110 : 'Error desconegut.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Ja existeix un fitxer o carpeta amb aquest nom.',
	116 : 'No s\'ha trobat la carpeta. Si us plau, actualitzi i torni-ho a provar.',
	117 : 'No s\'ha trobat el fitxer. Si us plau, actualitzi i torni-ho a provar.',
	118 : 'Les rutes origen i destí són iguals.',
	201 : 'Ja existeix un fitxer amb aquest nom. El fitxer pujat ha estat renombrat com a "%1".',
	202 : 'Fitxer invàlid.',
	203 : 'Fitxer invàlid. El pes és massa gran.',
	204 : 'El fitxer pujat està corrupte.',
	205 : 'La carpeta temporal no està disponible en el servidor per poder realitzar pujades.',
	206 : 'La pujada s\'ha cancel·lat per raons de seguretat. El fitxer conté codi HTML.',
	207 : 'El fitxer pujat ha estat renombrat com a "%1".',
	300 : 'Ha fallat el moure el(s) fitxer(s).',
	301 : 'Ha fallat el copiar el(s) fitxer(s).',
	500 : 'El navegador de fitxers està deshabilitat per raons de seguretat. Si us plau, contacti amb l\'administrador del sistema i comprovi el fitxer de configuració de CKFinder.',
	501 : 'El suport per a icones està deshabilitat.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'El nom del fitxer no pot estar buit.',
		FileExists		: 'El fitxer %s ja existeix.',
		FolderEmpty		: 'El nom de la carpeta no pot estar buit.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'El nom del fitxer no pot contenir cap dels caràcters següents: \n\\ / : * ? " < > |',
		FolderInvChar	: 'El nom de la carpeta no pot contenir cap dels caràcters següents: \n\\ / : * ? " < > |',

		PopupBlockView	: 'No ha estat possible obrir el fitxer en una nova finestra. Si us plau, configuri el seu navegador i desactivi tots els blocadors de finestres per a aquesta pàgina.',
		XmlError		: 'No ha estat possible carregar correctament la resposta XML del servidor.',
		XmlEmpty		: 'No ha estat possible carregar correctament la resposta XML del servidor. El servidor ha enviat una cadena buida.',
		XmlRawResponse	: 'Resposta del servidor: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensionar %s',
		sizeTooBig		: 'No es pot posar l\'altura o l\'amplada de la imatge més gran que les dimensions originals (%size).',
		resizeSuccess	: 'Imatge redimensionada correctament.',
		thumbnailNew	: 'Crear nova miniatura',
		thumbnailSmall	: 'Petita (%s)',
		thumbnailMedium	: 'Mitjana (%s)',
		thumbnailLarge	: 'Gran (%s)',
		newSize			: 'Establir nova grandària',
		width			: 'Amplada',
		height			: 'Altura',
		invalidHeight	: 'Altura invàlida.',
		invalidWidth	: 'Amplada invàlida.',
		invalidName		: 'Nom no vàlid.',
		newImage		: 'Crear nova imatge',
		noExtensionChange : 'L\'extensió no es pot canviar.',
		imageSmall		: 'La imatge original és massa petita.',
		contextMenuName	: 'Redimensionar',
		lockRatio		: 'Proporcional',
		resetSize		: 'Grandària Original'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Desar',
		fileOpenError	: 'No es pot obrir el fitxero.',
		fileSaveSuccess	: 'Fitxer desat correctament.',
		contextMenuName	: 'Editar',
		loadingFile		: 'Carregant fitxer, si us plau, esperi...'
	},

	Maximize :
	{
		maximize : 'Maximitzar',
		minimize : 'Minimitzar'
	},

	Gallery :
	{
		current : 'Imatge {current} de {total}'
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
