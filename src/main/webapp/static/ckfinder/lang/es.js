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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Spanish
 *		language.
*/

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['es'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, no disponible</span>',
		confirmCancel	: 'Algunas opciones se han cambiado\r\n¿Está seguro de querer cerrar el diálogo?',
		ok				: 'Aceptar',
		cancel			: 'Cancelar',
		confirmationTitle	: 'Confirmación',
		messageTitle	: 'Información',
		inputTitle		: 'Pregunta',
		undo			: 'Deshacer',
		redo			: 'Rehacer',
		skip			: 'Omitir',
		skipAll			: 'Omitir todos',
		makeDecision	: '¿Qué acción debe realizarse?',
		rememberDecision: 'Recordar mi decisión'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'es',
	LangCode : 'es',

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
	FoldersTitle	: 'Carpetas',
	FolderLoading	: 'Cargando...',
	FolderNew		: 'Por favor, escriba el nombre para la nueva carpeta: ',
	FolderRename	: 'Por favor, escriba el nuevo nombre para la carpeta: ',
	FolderDelete	: '¿Está seguro de que quiere borrar la carpeta "%1"?',
	FolderRenaming	: ' (Renombrando...)',
	FolderDeleting	: ' (Borrando...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Por favor, escriba el nuevo nombre del fichero: ',
	FileRenameExt	: '¿Está seguro de querer cambiar la extensión del fichero? El fichero puede dejar de ser usable.',
	FileRenaming	: 'Renombrando...',
	FileDelete		: '¿Está seguro de que quiere borrar el fichero "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Cargando...',
	FilesEmpty		: 'Carpeta vacía',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Cesta',
	BasketClear			: 'Vaciar cesta',
	BasketRemove		: 'Quitar de la cesta',
	BasketOpenFolder	: 'Abrir carpeta padre',
	BasketTruncateConfirm : '¿Está seguro de querer quitar todos los ficheros de la cesta?',
	BasketRemoveConfirm	: '¿Está seguro de querer quitar el fichero "%1" de la cesta?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'No hay ficheros en la cesta, arrastra y suelta algunos.',
	BasketCopyFilesHere	: 'Copiar ficheros de la cesta',
	BasketMoveFilesHere	: 'Mover ficheros de la cesta',

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
	Upload		: 'Añadir',
	UploadTip	: 'Añadir nuevo fichero',
	Refresh		: 'Actualizar',
	Settings	: 'Configuración',
	Help		: 'Ayuda',
	HelpTip		: 'Ayuda',

	// Context Menus
	Select			: 'Seleccionar',
	SelectThumbnail : 'Seleccionar el icono',
	View			: 'Ver',
	Download		: 'Descargar',

	NewSubFolder	: 'Nueva Subcarpeta',
	Rename			: 'Renombrar',
	Delete			: 'Borrar',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Copiar aquí',
	MoveDragDrop	: 'Mover aquí',

	// Dialogs
	RenameDlgTitle		: 'Renombrar',
	NewNameDlgTitle		: 'Nuevo nombre',
	FileExistsDlgTitle	: 'Fichero existente',
	SysErrorDlgTitle : 'Error de sistema',

	FileOverwrite	: 'Sobreescribir',
	FileAutorename	: 'Auto-renombrar',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'Aceptar',
	CancelBtn	: 'Cancelar',
	CloseBtn	: 'Cerrar',

	// Upload Panel
	UploadTitle			: 'Añadir nuevo fichero',
	UploadSelectLbl		: 'Elija el fichero a subir',
	UploadProgressLbl	: '(Subida en progreso, por favor espere...)',
	UploadBtn			: 'Subir el fichero elegido',
	UploadBtnCancel		: 'Cancelar',

	UploadNoFileMsg		: 'Por favor, elija un fichero de su ordenador.',
	UploadNoFolder		: 'Por favor, escoja la carpeta antes de iniciar la subida.',
	UploadNoPerms		: 'No puede subir ficheros.',
	UploadUnknError		: 'Error enviando el fichero.',
	UploadExtIncorrect	: 'La extensión del fichero no está permitida en esta carpeta.',

	// Flash Uploads
	UploadLabel			: 'Ficheros a subir',
	UploadTotalFiles	: 'Total de ficheros:',
	UploadTotalSize		: 'Tamaño total:',
	UploadSend			: 'Añadir',
	UploadAddFiles		: 'Añadir ficheros',
	UploadClearFiles	: 'Borrar ficheros',
	UploadCancel		: 'Cancelar subida',
	UploadRemove		: 'Quitar',
	UploadRemoveTip		: 'Quitar !f',
	UploadUploaded		: 'Enviado !n%',
	UploadProcessing	: 'Procesando...',

	// Settings Panel
	SetTitle		: 'Configuración',
	SetView			: 'Vista:',
	SetViewThumb	: 'Iconos',
	SetViewList		: 'Lista',
	SetDisplay		: 'Mostrar:',
	SetDisplayName	: 'Nombre de fichero',
	SetDisplayDate	: 'Fecha',
	SetDisplaySize	: 'Tamaño del fichero',
	SetSort			: 'Ordenar:',
	SetSortName		: 'por Nombre',
	SetSortDate		: 'por Fecha',
	SetSortSize		: 'por Tamaño',
	SetSortExtension		: 'por Extensión',

	// Status Bar
	FilesCountEmpty : '<Carpeta vacía>',
	FilesCountOne	: '1 fichero',
	FilesCountMany	: '%1 ficheros',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'No ha sido posible completar la solicitud. (Error %1)',
	Errors :
	{
	 10 : 'Comando incorrecto.',
	 11 : 'El tipo de recurso no ha sido especificado en la solicitud.',
	 12 : 'El tipo de recurso solicitado no es válido.',
	102 : 'Nombre de fichero o carpeta no válido.',
	103 : 'No se ha podido completar la solicitud debido a las restricciones de autorización.',
	104 : 'No ha sido posible completar la solicitud debido a restricciones en el sistema de ficheros.',
	105 : 'La extensión del archivo no es válida.',
	109 : 'Petición inválida.',
	110 : 'Error desconocido.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Ya existe un fichero o carpeta con ese nombre.',
	116 : 'No se ha encontrado la carpeta. Por favor, actualice y pruebe de nuevo.',
	117 : 'No se ha encontrado el fichero. Por favor, actualice la lista de ficheros y pruebe de nuevo.',
	118 : 'Las rutas origen y destino son iguales.',
	201 : 'Ya existía un fichero con ese nombre. El fichero subido ha sido renombrado como "%1".',
	202 : 'Fichero inválido.',
	203 : 'Fichero inválido. El peso es demasiado grande.',
	204 : 'El fichero subido está corrupto.',
	205 : 'La carpeta temporal no está disponible en el servidor para las subidas.',
	206 : 'La subida se ha cancelado por razones de seguridad. El fichero contenía código HTML.',
	207 : 'El fichero subido ha sido renombrado como "%1".',
	300 : 'Ha fallado el mover el(los) fichero(s).',
	301 : 'Ha fallado el copiar el(los) fichero(s).',
	500 : 'El navegador de archivos está deshabilitado por razones de seguridad. Por favor, contacte con el administrador de su sistema y compruebe el fichero de configuración de CKFinder.',
	501 : 'El soporte para iconos está deshabilitado.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'El nombre del fichero no puede estar vacío.',
		FileExists		: 'El fichero %s ya existe.',
		FolderEmpty		: 'El nombre de la carpeta no puede estar vacío.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'El nombre del fichero no puede contener ninguno de los caracteres siguientes: \n\\ / : * ? " < > |',
		FolderInvChar	: 'El nombre de la carpeta no puede contener ninguno de los caracteres siguientes: \n\\ / : * ? " < > |',

		PopupBlockView	: 'No ha sido posible abrir el fichero en una nueva ventana. Por favor, configure su navegador y desactive todos los bloqueadores de ventanas para esta página.',
		XmlError		: 'No ha sido posible cargar correctamente la respuesta XML del servidor.',
		XmlEmpty		: 'No ha sido posible cargar correctamente la respuesta XML del servidor. El servidor envió una cadena vacía.',
		XmlRawResponse	: 'Respuesta del servidor: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Redimensionar %s',
		sizeTooBig		: 'No se puede poner la altura o anchura de la imagen mayor que las dimensiones originales (%size).',
		resizeSuccess	: 'Imagen redimensionada correctamente.',
		thumbnailNew	: 'Crear nueva minuatura',
		thumbnailSmall	: 'Pequeña (%s)',
		thumbnailMedium	: 'Mediana (%s)',
		thumbnailLarge	: 'Grande (%s)',
		newSize			: 'Establecer nuevo tamaño',
		width			: 'Ancho',
		height			: 'Alto',
		invalidHeight	: 'Altura inválida.',
		invalidWidth	: 'Anchura inválida.',
		invalidName		: 'Nombre no válido.',
		newImage		: 'Crear nueva imagen',
		noExtensionChange : 'La extensión no se puede cambiar.',
		imageSmall		: 'La imagen original es demasiado pequeña.',
		contextMenuName	: 'Redimensionar',
		lockRatio		: 'Proporcional',
		resetSize		: 'Tamaño Original'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Guardar',
		fileOpenError	: 'No se puede abrir el fichero.',
		fileSaveSuccess	: 'Fichero guardado correctamente.',
		contextMenuName	: 'Editar',
		loadingFile		: 'Cargando fichero, por favor espere...'
	},

	Maximize :
	{
		maximize : 'Maximizar',
		minimize : 'Minimizar'
	},

	Gallery :
	{
		current : 'Imagen {current} de {total}'
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
