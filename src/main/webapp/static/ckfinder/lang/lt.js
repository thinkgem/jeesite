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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Lithuanian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['lt'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, nėra</span>',
		confirmCancel	: 'Kai kurie nustatymai buvo pakeisti. Ar tikrai norite uždaryti šį langą?',
		ok				: 'Gerai',
		cancel			: 'Atšaukti',
		confirmationTitle	: 'Patvirtinimas',
		messageTitle	: 'Informacija',
		inputTitle		: 'Klausimas',
		undo			: 'Veiksmas atgal',
		redo			: 'Veiksmas pirmyn',
		skip			: 'Praleisti',
		skipAll			: 'Praleisti viską',
		makeDecision	: 'Ką pasirinksite?',
		rememberDecision: 'Atsiminti mano pasirinkimą'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'lt',
	LangCode : 'lt',

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
	DateTime : 'yyyy.mm.dd H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Segtuvai',
	FolderLoading	: 'Prašau palaukite...',
	FolderNew		: 'Prašau įrašykite naujo segtuvo pavadinimą: ',
	FolderRename	: 'Prašau įrašykite naujo segtuvo pavadinimą: ',
	FolderDelete	: 'Ar tikrai norite ištrinti "%1" segtuvą?',
	FolderRenaming	: ' (Pervadinama...)',
	FolderDeleting	: ' (Trinama...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Prašau įrašykite naujo failo pavadinimą: ',
	FileRenameExt	: 'Ar tikrai norite pakeisti šio failo plėtinį? Failas gali būti nebepanaudojamas',
	FileRenaming	: 'Pervadinama...',
	FileDelete		: 'Ar tikrai norite ištrinti failą "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Prašau palaukite...',
	FilesEmpty		: 'Tuščias segtuvas',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Krepšelis',
	BasketClear			: 'Ištuštinti krepšelį',
	BasketRemove		: 'Ištrinti krepšelį',
	BasketOpenFolder	: 'Atidaryti failo segtuvą',
	BasketTruncateConfirm : 'Ar tikrai norite ištrinti visus failus iš krepšelio?',
	BasketRemoveConfirm	: 'Ar tikrai norite ištrinti failą "%1" iš krepšelio?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Krepšelyje failų nėra, nuvilkite ir įmeskite juos į krepšelį.',
	BasketCopyFilesHere	: 'Kopijuoti failus iš krepšelio',
	BasketMoveFilesHere	: 'Perkelti failus iš krepšelio',

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
	Upload		: 'Įkelti',
	UploadTip	: 'Įkelti naują failą',
	Refresh		: 'Atnaujinti',
	Settings	: 'Nustatymai',
	Help		: 'Pagalba',
	HelpTip		: 'Patarimai',

	// Context Menus
	Select			: 'Pasirinkti',
	SelectThumbnail : 'Pasirinkti miniatiūrą',
	View			: 'Peržiūrėti',
	Download		: 'Atsisiųsti',

	NewSubFolder	: 'Naujas segtuvas',
	Rename			: 'Pervadinti',
	Delete			: 'Ištrinti',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Nukopijuoti čia',
	MoveDragDrop	: 'Perkelti čia',

	// Dialogs
	RenameDlgTitle		: 'Pervadinti',
	NewNameDlgTitle		: 'Naujas pavadinimas',
	FileExistsDlgTitle	: 'Toks failas jau egzistuoja',
	SysErrorDlgTitle : 'Sistemos klaida',

	FileOverwrite	: 'Užrašyti ant viršaus',
	FileAutorename	: 'Automatiškai pervadinti',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Gerai',
	CancelBtn	: 'Atšaukti',
	CloseBtn	: 'Uždaryti',

	// Upload Panel
	UploadTitle			: 'Įkelti naują failą',
	UploadSelectLbl		: 'Pasirinkite failą įkėlimui',
	UploadProgressLbl	: '(Vykdomas įkėlimas, prašau palaukite...)',
	UploadBtn			: 'Įkelti pasirinktą failą',
	UploadBtnCancel		: 'Atšaukti',

	UploadNoFileMsg		: 'Pasirinkite failą iš savo kompiuterio',
	UploadNoFolder		: 'Pasirinkite segtuvą prieš įkeliant.',
	UploadNoPerms		: 'Failų įkėlimas uždraustas.',
	UploadUnknError		: 'Įvyko klaida siunčiant failą.',
	UploadExtIncorrect	: 'Šiame segtuve toks failų plėtinys yra uždraustas.',

	// Flash Uploads
	UploadLabel			: 'Įkeliami failai',
	UploadTotalFiles	: 'Iš viso failų:',
	UploadTotalSize		: 'Visa apimtis:',
	UploadSend			: 'Įkelti',
	UploadAddFiles		: 'Pridėti failus',
	UploadClearFiles	: 'Išvalyti failus',
	UploadCancel		: 'Atšaukti nusiuntimą',
	UploadRemove		: 'Pašalinti',
	UploadRemoveTip		: 'Pašalinti !f',
	UploadUploaded		: 'Įkeltas !n%',
	UploadProcessing	: 'Apdorojama...',

	// Settings Panel
	SetTitle		: 'Nustatymai',
	SetView			: 'Peržiūrėti:',
	SetViewThumb	: 'Miniatiūros',
	SetViewList		: 'Sąrašas',
	SetDisplay		: 'Rodymas:',
	SetDisplayName	: 'Failo pavadinimas',
	SetDisplayDate	: 'Data',
	SetDisplaySize	: 'Failo dydis',
	SetSort			: 'Rūšiavimas:',
	SetSortName		: 'pagal failo pavadinimą',
	SetSortDate		: 'pagal datą',
	SetSortSize		: 'pagal apimtį',
	SetSortExtension		: 'pagal plėtinį',

	// Status Bar
	FilesCountEmpty : '<Tuščias segtuvas>',
	FilesCountOne	: '1 failas',
	FilesCountMany	: '%1 failai',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Užklausos įvykdyti nepavyko. (Klaida %1)',
	Errors :
	{
	 10 : 'Neteisinga komanda.',
	 11 : 'Resurso rūšis nenurodyta užklausoje.',
	 12 : 'Neteisinga resurso rūšis.',
	102 : 'Netinkamas failas arba segtuvo pavadinimas.',
	103 : 'Nepavyko įvykdyti užklausos dėl autorizavimo apribojimų.',
	104 : 'Nepavyko įvykdyti užklausos dėl failų sistemos leidimų apribojimų.',
	105 : 'Netinkamas failo plėtinys.',
	109 : 'Netinkama užklausa.',
	110 : 'Nežinoma klaida.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Failas arba segtuvas su tuo pačiu pavadinimu jau yra.',
	116 : 'Segtuvas nerastas. Pabandykite atnaujinti.',
	117 : 'Failas nerastas. Pabandykite atnaujinti failų sąrašą.',
	118 : 'Šaltinio ir nurodomos vietos nuorodos yra vienodos.',
	201 : 'Failas su tuo pačiu pavadinimu jau tra. Įkeltas failas buvo pervadintas į "%1"',
	202 : 'Netinkamas failas',
	203 : 'Netinkamas failas. Failo apimtis yra per didelė.',
	204 : 'Įkeltas failas yra pažeistas.',
	205 : 'Nėra laikinojo segtuvo skirto failams įkelti.',
	206 : 'Įkėlimas bus nutrauktas dėl saugumo sumetimų. Šiame faile yra HTML duomenys.',
	207 : 'Įkeltas failas buvo pervadintas į "%1"',
	300 : 'Failų perkėlimas nepavyko.',
	301 : 'Failų kopijavimas nepavyko.',
	500 : 'Failų naršyklė yra išjungta dėl saugumo nustaymų. Prašau susisiekti su sistemų administratoriumi ir patikrinkite CKFinder konfigūracinį failą.',
	501 : 'Miniatiūrų palaikymas išjungtas.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Failo pavadinimas negali būti tuščias',
		FileExists		: 'Failas %s jau egzistuoja',
		FolderEmpty		: 'Segtuvo pavadinimas negali būti tuščias',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Failo pavadinimas negali turėti bent vieno iš šių simbolių: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Segtuvo pavadinimas negali turėti bent vieno iš šių simbolių: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Nepavyko atidaryti failo naujame lange. Prašau pakeiskite savo naršyklės nustatymus, kad būtų leidžiami iškylantys langai šiame tinklapyje.',
		XmlError		: 'Nepavyko įkrauti XML atsako iš web serverio.',
		XmlEmpty		: 'Nepavyko įkrauti XML atsako iš web serverio. Serveris gražino tuščią užklausą.',
		XmlRawResponse	: 'Vientisas atsakas iš serverio: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Keisti matmenis %s',
		sizeTooBig		: 'Negalima nustatyti aukščio ir pločio į didesnius nei originalaus paveiksliuko (%size).',
		resizeSuccess	: 'Paveiksliuko matmenys pakeisti.',
		thumbnailNew	: 'Sukurti naują miniatiūrą',
		thumbnailSmall	: 'Mažas (%s)',
		thumbnailMedium	: 'Vidutinis (%s)',
		thumbnailLarge	: 'Didelis (%s)',
		newSize			: 'Nustatyti naujus matmenis',
		width			: 'Plotis',
		height			: 'Aukštis',
		invalidHeight	: 'Neteisingas aukštis.',
		invalidWidth	: 'Neteisingas plotis.',
		invalidName		: 'Neteisingas pavadinimas.',
		newImage		: 'Sukurti naują paveiksliuką',
		noExtensionChange : 'Failo plėtinys negali būti pakeistas.',
		imageSmall		: 'Šaltinio paveiksliukas yra per mažas',
		contextMenuName	: 'Pakeisti matmenis',
		lockRatio		: 'Išlaikyti matmenų santykį',
		resetSize		: 'Nustatyti dydį iš naujo'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Išsaugoti',
		fileOpenError	: 'Nepavyko atidaryti failo.',
		fileSaveSuccess	: 'Failas sėkmingai išsaugotas.',
		contextMenuName	: 'Redaguoti',
		loadingFile		: 'Įkraunamas failas, prašau palaukite...'
	},

	Maximize :
	{
		maximize : 'Padidinti',
		minimize : 'Sumažinti'
	},

	Gallery :
	{
		current : 'Nuotrauka {current} iš {total}'
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
