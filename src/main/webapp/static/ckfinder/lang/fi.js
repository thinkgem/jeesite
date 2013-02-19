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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Finnish
 *		language. Translated into Finnish 2010-12-15 by Petteri Salmela,
 *		updated.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['fi'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, ei käytettävissä</span>',
		confirmCancel	: 'Valintoja on muutettu. Suljetaanko ikkuna kuitenkin?',
		ok				: 'OK',
		cancel			: 'Peru',
		confirmationTitle	: 'Varmistus',
		messageTitle	: 'Ilmoitus',
		inputTitle		: 'Kysymys',
		undo			: 'Peru',
		redo			: 'Tee uudelleen',
		skip			: 'Ohita',
		skipAll			: 'Ohita kaikki',
		makeDecision	: 'Mikä toiminto suoritetaan?',
		rememberDecision: 'Muista valintani'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'fi',
	LangCode : 'fi',

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
	FoldersTitle	: 'Kansiot',
	FolderLoading	: 'Lataan...',
	FolderNew		: 'Kirjoita uuden kansion nimi: ',
	FolderRename	: 'Kirjoita uusi nimi kansiolle ',
	FolderDelete	: 'Haluatko varmasti poistaa kansion "%1"?',
	FolderRenaming	: ' (Uudelleennimeää...)',
	FolderDeleting	: ' (Poistaa...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Kirjoita uusi tiedostonimi: ',
	FileRenameExt	: 'Haluatko varmasti muuttaa tiedostotarkennetta? Tiedosto voi muuttua käyttökelvottomaksi.',
	FileRenaming	: 'Uudelleennimeää...',
	FileDelete		: 'Haluatko varmasti poistaa tiedoston "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Lataa...',
	FilesEmpty		: 'Tyhjä kansio.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Kori',
	BasketClear			: 'Tyhjennä kori',
	BasketRemove		: 'Poista korista',
	BasketOpenFolder	: 'Avaa ylemmän tason kansio',
	BasketTruncateConfirm : 'Haluatko todella poistaa kaikki tiedostot korista?',
	BasketRemoveConfirm	: 'Haluatko todella poistaa tiedoston "%1" korista?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Korissa ei ole tiedostoja. Lisää raahaamalla.',
	BasketCopyFilesHere	: 'Kopioi tiedostot korista.',
	BasketMoveFilesHere	: 'Siirrä tiedostot korista.',

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
	Upload		: 'Lataa palvelimelle',
	UploadTip	: 'Lataa uusi tiedosto palvelimelle',
	Refresh		: 'Päivitä',
	Settings	: 'Asetukset',
	Help		: 'Apua',
	HelpTip		: 'Apua',

	// Context Menus
	Select			: 'Valitse',
	SelectThumbnail : 'Valitse esikatselukuva',
	View			: 'Näytä',
	Download		: 'Lataa palvelimelta',

	NewSubFolder	: 'Uusi alikansio',
	Rename			: 'Uudelleennimeä ',
	Delete			: 'Poista',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Kopioi tähän',
	MoveDragDrop	: 'Siirrä tähän',

	// Dialogs
	RenameDlgTitle		: 'Nimeä uudelleen',
	NewNameDlgTitle		: 'Uusi nimi',
	FileExistsDlgTitle	: 'Tiedostonimi on jo olemassa!',
	SysErrorDlgTitle : 'Järjestelmävirhe',

	FileOverwrite	: 'Ylikirjoita',
	FileAutorename	: 'Nimeä uudelleen automaattisesti',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Peru',
	CloseBtn	: 'Sulje',

	// Upload Panel
	UploadTitle			: 'Lataa uusi tiedosto palvelimelle',
	UploadSelectLbl		: 'Valitse ladattava tiedosto',
	UploadProgressLbl	: '(Lataaminen palvelimelle käynnissä...)',
	UploadBtn			: 'Lataa valittu tiedosto palvelimelle',
	UploadBtnCancel		: 'Peru',

	UploadNoFileMsg		: 'Valitse tiedosto tietokoneeltasi.',
	UploadNoFolder		: 'Valitse kansio ennen palvelimelle lataamista.',
	UploadNoPerms		: 'Tiedoston lataaminen palvelimelle evätty.',
	UploadUnknError		: 'Tiedoston siirrossa tapahtui virhe.',
	UploadExtIncorrect	: 'Tiedostotarkenne ei ole sallittu valitussa kansiossa.',

	// Flash Uploads
	UploadLabel			: 'Ladattavat tiedostot',
	UploadTotalFiles	: 'Tiedostoja yhteensä:',
	UploadTotalSize		: 'Yhteenlaskettu tiedostokoko:',
	UploadSend			: 'Lataa palvelimelle',
	UploadAddFiles		: 'Lisää tiedostoja',
	UploadClearFiles	: 'Poista tiedostot',
	UploadCancel		: 'Peru lataus',
	UploadRemove		: 'Poista',
	UploadRemoveTip		: 'Poista !f',
	UploadUploaded		: 'Ladattu !n%',
	UploadProcessing	: 'Käsittelee...',

	// Settings Panel
	SetTitle		: 'Asetukset',
	SetView			: 'Näkymä:',
	SetViewThumb	: 'Esikatselukuvat',
	SetViewList		: 'Luettelo',
	SetDisplay		: 'Näytä:',
	SetDisplayName	: 'Tiedostonimi',
	SetDisplayDate	: 'Päivämäärä',
	SetDisplaySize	: 'Tiedostokoko',
	SetSort			: 'Lajittele:',
	SetSortName		: 'aakkosjärjestykseen',
	SetSortDate		: 'päivämäärän mukaan',
	SetSortSize		: 'tiedostokoon mukaan',
	SetSortExtension		: 'tiedostopäätteen mukaan',

	// Status Bar
	FilesCountEmpty : '<Tyhjä kansio>',
	FilesCountOne	: '1 tiedosto',
	FilesCountMany	: '%1 tiedostoa',

	// Size and Speed
	Kb				: '%1 kt',
	Mb				: '%1 Mt',
	Gb				: '%1 Gt',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Pyyntöä ei voitu suorittaa. (Virhe %1)',
	Errors :
	{
	 10 : 'Virheellinen komento.',
	 11 : 'Pyynnön resurssityyppi on määrittelemättä.',
	 12 : 'Pyynnön resurssityyppi on virheellinen.',
	102 : 'Virheellinen tiedosto- tai kansionimi.',
	103 : 'Oikeutesi eivät riitä pyynnön suorittamiseen.',
	104 : 'Tiedosto-oikeudet eivät riitä pyynnön suorittamiseen.',
	105 : 'Virheellinen tiedostotarkenne.',
	109 : 'Virheellinen pyyntö.',
	110 : 'Tuntematon virhe.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Samanniminen tiedosto tai kansio on jo olemassa.',
	116 : 'Kansiota ei löydy. Yritä uudelleen kansiopäivityksen jälkeen.',
	117 : 'Tiedostoa ei löydy. Yritä uudelleen kansiopäivityksen jälkeen.',
	118 : 'Lähde- ja kohdekansio on sama!',
	201 : 'Samanniminen tiedosto on jo olemassa. Palvelimelle ladattu tiedosto on nimetty: "%1".',
	202 : 'Virheellinen tiedosto.',
	203 : 'Virheellinen tiedosto. Tiedostokoko on liian suuri.',
	204 : 'Palvelimelle ladattu tiedosto on vioittunut.',
	205 : 'Väliaikaishakemistoa ei ole määritetty palvelimelle lataamista varten.',
	206 : 'Palvelimelle lataaminen on peruttu turvallisuussyistä. Tiedosto sisältää HTML-tyylistä dataa.',
	207 : 'Palvelimelle ladattu tiedosto on  nimetty: "%1".',
	300 : 'Tiedostosiirto epäonnistui.',
	301 : 'Tiedostokopiointi epäonnistui.',
	500 : 'Tiedostoselain on kytketty käytöstä turvallisuussyistä. Pyydä pääkäyttäjää tarkastamaan CKFinderin asetustiedosto.',
	501 : 'Esikatselukuvien tuki on kytketty toiminnasta.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Tiedosto on nimettävä!',
		FileExists		: 'Tiedosto %s on jo olemassa.',
		FolderEmpty		: 'Kansio on nimettävä!',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Tiedostonimi ei voi sisältää seuraavia merkkejä: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Kansionimi ei voi sisältää seuraavia merkkejä: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Tiedostoa ei voitu avata uuteen ikkunaan. Salli selaimesi asetuksissa ponnahdusikkunat tälle sivulle.',
		XmlError		: 'Web-palvelimen XML-vastausta ei pystytty kunnolla lataamaan.',
		XmlEmpty		: 'Web-palvelimen XML vastausta ei pystytty lataamaan. Palvelin palautti tyhjän vastauksen.',
		XmlRawResponse	: 'Palvelimen käsittelemätön vastaus: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Muuta kokoa %s',
		sizeTooBig		: 'Kuvan mittoja ei voi asettaa alkuperäistä suuremmiksi(%size).',
		resizeSuccess	: 'Kuvan koon muuttaminen onnistui.',
		thumbnailNew	: 'Luo uusi esikatselukuva.',
		thumbnailSmall	: 'Pieni (%s)',
		thumbnailMedium	: 'Keskikokoinen (%s)',
		thumbnailLarge	: 'Suuri (%s)',
		newSize			: 'Aseta uusi koko',
		width			: 'Leveys',
		height			: 'Korkeus',
		invalidHeight	: 'Viallinen korkeus.',
		invalidWidth	: 'Viallinen leveys.',
		invalidName		: 'Viallinen tiedostonimi.',
		newImage		: 'Luo uusi kuva',
		noExtensionChange : 'Tiedostomäärettä ei voi vaihtaa.',
		imageSmall		: 'Lähdekuva on liian pieni.',
		contextMenuName	: 'Muuta kokoa',
		lockRatio		: 'Lukitse suhteet',
		resetSize		: 'Alkuperäinen koko'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Tallenna',
		fileOpenError	: 'Tiedostoa ei voi avata.',
		fileSaveSuccess	: 'Tiedoston tallennus onnistui.',
		contextMenuName	: 'Muokkaa',
		loadingFile		: 'Tiedostoa ladataan ...'
	},

	Maximize :
	{
		maximize : 'Suurenna',
		minimize : 'Pienennä'
	},

	Gallery :
	{
		current : 'Kuva {current} / {total}'
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
