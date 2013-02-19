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
 * @fileOverview Defines the {@link CKFinder.lang} object for the French
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['fr'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, Inaccessible</span>',
		confirmCancel	: 'Certaines options ont été modifiées. Êtes vous sûr de vouloir fermer cette fenêtre?',
		ok				: 'OK',
		cancel			: 'Annuler',
		confirmationTitle	: 'Confirmation',
		messageTitle	: 'Information',
		inputTitle		: 'Question',
		undo			: 'Annuler',
		redo			: 'Rétablir',
		skip			: 'Passer',
		skipAll			: 'Passer tout',
		makeDecision	: 'Quelle action choisir?',
		rememberDecision: 'Se rappeler de la décision'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'fr',

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
	FoldersTitle	: 'Dossiers',
	FolderLoading	: 'Chargement...',
	FolderNew		: 'Entrez le nouveau nom du dossier: ',
	FolderRename	: 'Entrez le nouveau nom du dossier: ',
	FolderDelete	: 'Êtes-vous sûr de vouloir effacer le dossier "%1"?',
	FolderRenaming	: ' (Renommage en cours...)',
	FolderDeleting	: ' (Suppression en cours...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Entrez le nouveau nom du fichier: ',
	FileRenameExt	: 'Êtes-vous sûr de vouloir changer l\'extension de ce fichier? Le fichier pourrait devenir inutilisable.',
	FileRenaming	: 'Renommage en cours...',
	FileDelete		: 'Êtes-vous sûr de vouloir effacer le fichier "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Chargement...',
	FilesEmpty		: 'Répertoire vide',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Corbeille',
	BasketClear			: 'Vider la corbeille',
	BasketRemove		: 'Retirer de la corbeille',
	BasketOpenFolder	: 'Ouvrir le répertiore parent',
	BasketTruncateConfirm : 'Êtes vous sûr de vouloir supprimer tous les fichiers de la corbeille?',
	BasketRemoveConfirm	: 'Êtes vous sûr de vouloir supprimer le fichier "%1" de la corbeille?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Aucun fichier dans la corbeille, déposez en queques uns.',
	BasketCopyFilesHere	: 'Copier des fichiers depuis la corbeille',
	BasketMoveFilesHere	: 'Déplacer des fichiers depuis la corbeille',

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
	Upload		: 'Envoyer',
	UploadTip	: 'Envoyer un nouveau fichier',
	Refresh		: 'Rafraîchir',
	Settings	: 'Configuration',
	Help		: 'Aide',
	HelpTip		: 'Aide',

	// Context Menus
	Select			: 'Choisir',
	SelectThumbnail : 'Choisir une miniature',
	View			: 'Voir',
	Download		: 'Télécharger',

	NewSubFolder	: 'Nouveau sous-dossier',
	Rename			: 'Renommer',
	Delete			: 'Effacer',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copier ici',
	MoveDragDrop	: 'Déplacer ici',

	// Dialogs
	RenameDlgTitle		: 'Renommer',
	NewNameDlgTitle		: 'Nouveau fichier',
	FileExistsDlgTitle	: 'Fichier déjà existant',
	SysErrorDlgTitle : 'Erreur système',

	FileOverwrite	: 'Ré-écrire',
	FileAutorename	: 'Re-nommage automatique',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Annuler',
	CloseBtn	: 'Fermer',

	// Upload Panel
	UploadTitle			: 'Envoyer un nouveau fichier',
	UploadSelectLbl		: 'Sélectionner le fichier à télécharger',
	UploadProgressLbl	: '(Envoi en cours, veuillez patienter...)',
	UploadBtn			: 'Envoyer le fichier sélectionné',
	UploadBtnCancel		: 'Annuler',

	UploadNoFileMsg		: 'Sélectionner un fichier sur votre ordinateur.',
	UploadNoFolder		: 'Merci de sélectionner un répertoire avant l\'envoi.',
	UploadNoPerms		: 'L\'envoi de fichier n\'est pas autorisé.',
	UploadUnknError		: 'Erreur pendant l\'envoi du fichier.',
	UploadExtIncorrect	: 'L\'extension du fichier n\'est pas autorisée dans ce dossier.',

	// Flash Uploads
	UploadLabel			: 'Fichier à envoyer',
	UploadTotalFiles	: 'Nombre de fichiers:',
	UploadTotalSize		: 'Poids total:',
	UploadSend			: 'Envoyer',
	UploadAddFiles		: 'Ajouter des fichiers',
	UploadClearFiles	: 'Supprimer les fichiers',
	UploadCancel		: 'Annuler l\'envoi',
	UploadRemove		: 'Retirer',
	UploadRemoveTip		: 'Retirer !f',
	UploadUploaded		: 'Téléchargement !n%',
	UploadProcessing	: 'Progression...',

	// Settings Panel
	SetTitle		: 'Configuration',
	SetView			: 'Voir:',
	SetViewThumb	: 'Miniatures',
	SetViewList		: 'Liste',
	SetDisplay		: 'Affichage:',
	SetDisplayName	: 'Nom du fichier',
	SetDisplayDate	: 'Date',
	SetDisplaySize	: 'Taille du fichier',
	SetSort			: 'Classement:',
	SetSortName		: 'par nom de fichier',
	SetSortDate		: 'par date',
	SetSortSize		: 'par taille',
	SetSortExtension		: 'par extension de fichier',

	// Status Bar
	FilesCountEmpty : '<Dossier Vide>',
	FilesCountOne	: '1 fichier',
	FilesCountMany	: '%1 fichiers',

	// Size and Speed
	Kb				: '%1 Ko',
	Mb				: '%1 Mo',
	Gb				: '%1 Go',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'La demande n\'a pas abouti. (Erreur %1)',
	Errors :
	{
	 10 : 'Commande invalide.',
	 11 : 'Le type de ressource n\'a pas été spécifié dans la commande.',
	 12 : 'Le type de ressource n\'est pas valide.',
	102 : 'Nom de fichier ou de dossier invalide.',
	103 : 'La demande n\'a pas abouti : problème d\'autorisations.',
	104 : 'La demande n\'a pas abouti : problème de restrictions de permissions.',
	105 : 'Extension de fichier invalide.',
	109 : 'Demande invalide.',
	110 : 'Erreur inconnue.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Un fichier ou un dossier avec ce nom existe déjà.',
	116 : 'Ce dossier n\'existe pas. Veuillez rafraîchir la page et réessayer.',
	117 : 'Ce fichier n\'existe pas. Veuillez rafraîchir la page et réessayer.',
	118 : 'Les chemins vers la source et la cible sont les mêmes.',
	201 : 'Un fichier avec ce nom existe déjà. Le fichier téléversé a été renommé en "%1".',
	202 : 'Fichier invalide.',
	203 : 'Fichier invalide. La taille est trop grande.',
	204 : 'Le fichier téléversé est corrompu.',
	205 : 'Aucun dossier temporaire n\'est disponible sur le serveur.',
	206 : 'Envoi interrompu pour raisons de sécurité. Le fichier contient des données de type HTML.',
	207 : 'Le fichier téléchargé a été renommé "%1".',
	300 : 'Le déplacement des fichiers a échoué.',
	301 : 'La copie des fichiers a échoué.',
	500 : 'L\'interface de gestion des fichiers est désactivé. Contactez votre administrateur et vérifier le fichier de configuration de CKFinder.',
	501 : 'La fonction "miniatures" est désactivée.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Le nom du fichier ne peut être vide.',
		FileExists		: 'Le fichier %s existes déjà.',
		FolderEmpty		: 'Le nom du dossier ne peut être vide.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Le nom du fichier ne peut pas contenir les charactères suivants : \n\\ / : * ? " < > |',
		FolderInvChar	: 'Le nom du dossier ne peut pas contenir les charactères suivants : \n\\ / : * ? " < > |',

		PopupBlockView	: 'Il n\'a pas été possible d\'ouvrir la nouvelle fenêtre. Désactiver votre bloqueur de fenêtres pour ce site.',
		XmlError		: 'Impossible de charger correctement la réponse XML du serveur web.',
		XmlEmpty		: 'Impossible de charger la réponse XML du serveur web. Le serveur a renvoyé une réponse vide.',
		XmlRawResponse	: 'Réponse du serveur: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensionner %s',
		sizeTooBig		: 'Impossible de modifier la hauteur ou la largeur de cette image pour une valeur plus grande que l\'original (%size).',
		resizeSuccess	: 'L\'image a été redimensionné avec succès.',
		thumbnailNew	: 'Créer une nouvelle vignette',
		thumbnailSmall	: 'Petit (%s)',
		thumbnailMedium	: 'Moyen (%s)',
		thumbnailLarge	: 'Gros (%s)',
		newSize			: 'Déterminer les nouvelles dimensions',
		width			: 'Largeur',
		height			: 'Hauteur',
		invalidHeight	: 'Hauteur invalide.',
		invalidWidth	: 'Largeur invalide.',
		invalidName		: 'Nom de fichier incorrect.',
		newImage		: 'Créer une nouvelle image',
		noExtensionChange : 'L\'extension du fichier ne peut pas être changé.',
		imageSmall		: 'L\'image est trop petit',
		contextMenuName	: 'Redimensionner',
		lockRatio		: 'Conserver les proportions',
		resetSize		: 'Taille d\'origine'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Sauvegarder',
		fileOpenError	: 'Impossible d\'ouvrir le fichier',
		fileSaveSuccess	: 'Fichier sauvegardé avec succès.',
		contextMenuName	: 'Edition',
		loadingFile		: 'Chargement du fichier, veuillez patientez...'
	},

	Maximize :
	{
		maximize : 'Agrandir',
		minimize : 'Minimiser'
	},

	Gallery :
	{
		current : 'Image {current} sur {total}'
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
