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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Bulgarian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['bg'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, недостъпно</span>',
		confirmCancel	: 'Някои от опциите са променени, желаете ли да затворите диалоговия прозорец?',
		ok				: 'ОК',
		cancel			: 'Отказ',
		confirmationTitle	: 'Потвърждение',
		messageTitle	: 'Информация',
		inputTitle		: 'Въпрос',
		undo			: 'Възтанови',
		redo			: 'Предишно',
		skip			: 'Прескочи',
		skipAll			: 'Прескочи всички',
		makeDecision	: 'Какво действие ще бъде предприето?',
		rememberDecision: 'Запомни ми избора'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'bg',

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
	DateTime : 'd/m/yyyy h:MM aa',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Папки',
	FolderLoading	: 'Зареждане...',
	FolderNew		: 'Моля въведете име на новата папка: ',
	FolderRename	: 'Моля въведете име на новата папка: ',
	FolderDelete	: 'Сигурни ли сте, че желаете да изтриете папката "%1"?',
	FolderRenaming	: ' (Преименуване...)',
	FolderDeleting	: ' (Изтриване...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Моля въведете име на файл: ',
	FileRenameExt	: 'Сигурни ли сте, че желаете да промените файловото разширение? Файлът може да стане неизползваем.',
	FileRenaming	: 'Преименуване...',
	FileDelete		: 'Сигурни ли сте, че желаете да изтриете "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Зареждане...',
	FilesEmpty		: 'Папката е празна.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Кошница',
	BasketClear			: 'Изчисти кошницата',
	BasketRemove		: 'Премахни от кошницата',
	BasketOpenFolder	: 'Отвори основната папка',
	BasketTruncateConfirm : 'Наиситина ли желаете да премахнете всичко файлове от кошницата?',
	BasketRemoveConfirm	: 'Наистина ли желаете да премахнете файла "%1" от кошницата?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Няма файлове в кошницата.',
	BasketCopyFilesHere	: 'Копиране на файлове от кошницата',
	BasketMoveFilesHere	: 'Местене на файлове от кошницата',

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
	Upload		: 'Качване',
	UploadTip	: 'Качване на нов файл',
	Refresh		: 'Опресняване',
	Settings	: 'Настройки',
	Help		: 'Помощ',
	HelpTip		: 'Помощ',

	// Context Menus
	Select			: 'Изберете',
	SelectThumbnail : 'Изберете миниатюра',
	View			: 'Виж',
	Download		: 'Изтегли',

	NewSubFolder	: 'Нов подпапка',
	Rename			: 'Преименуване',
	Delete			: 'Изтриване',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Копиране тук',
	MoveDragDrop	: 'Местене тук',

	// Dialogs
	RenameDlgTitle		: 'Преименуване',
	NewNameDlgTitle		: 'Ново име',
	FileExistsDlgTitle	: 'Файлът вече съществува',
	SysErrorDlgTitle : 'Системна грешка',

	FileOverwrite	: 'Препокриване',
	FileAutorename	: 'Авто-преименуване',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'ОК',
	CancelBtn	: 'Октаз',
	CloseBtn	: 'Затвори',

	// Upload Panel
	UploadTitle			: 'Качване на нов файл',
	UploadSelectLbl		: 'Изберете файл за качване',
	UploadProgressLbl	: '(Качва се в момента, моля изчакайте...)',
	UploadBtn			: 'Качване на избрания файл',
	UploadBtnCancel		: 'Отказ',

	UploadNoFileMsg		: 'Моля изберете файл от Вашия компютър.',
	UploadNoFolder		: 'Моля изберете файл за качване.',
	UploadNoPerms		: 'Качването на файлове не е позволено.',
	UploadUnknError		: 'Проблем с изпращането на файла.',
	UploadExtIncorrect	: 'Файловото разширение не е позволено за тази папка.',

	// Flash Uploads
	UploadLabel			: 'Файлове за качване',
	UploadTotalFiles	: 'Общо файлове:',
	UploadTotalSize		: 'Общ размер:',
	UploadSend			: 'Качване',
	UploadAddFiles		: 'Добави файлове',
	UploadClearFiles	: 'Изчисти',
	UploadCancel		: 'Отказ от качването',
	UploadRemove		: 'Премахни',
	UploadRemoveTip		: 'Премахни !f',
	UploadUploaded		: 'Качено !n%',
	UploadProcessing	: 'Обработва се...',

	// Settings Panel
	SetTitle		: 'Настройки',
	SetView			: 'Изглед:',
	SetViewThumb	: 'Миниатюри',
	SetViewList		: 'Списък',
	SetDisplay		: 'Екран:',
	SetDisplayName	: 'Име на файл',
	SetDisplayDate	: 'Дата',
	SetDisplaySize	: 'Размер на файл',
	SetSort			: 'Подреждане:',
	SetSortName		: 'по име на файл',
	SetSortDate		: 'по дата',
	SetSortSize		: 'по размер',
	SetSortExtension		: 'по разширение',

	// Status Bar
	FilesCountEmpty : '<празна папка>',
	FilesCountOne	: '1 файл',
	FilesCountMany	: '%1 файла',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Не е възможно да се извърши заявката. (ГРЕШКА %1)',
	Errors :
	{
	 10 : 'Невалидна команда.',
	 11 : 'Типът на ресурса не е определен в заявката.',
	 12 : 'Заявеният тип на ресурса не е намерен.',
	102 : 'Невалиден файл или име на папка.',
	103 : 'Не е възможно да се извърши действието заради проблем с идентификацията.',
	104 : 'Не е възможно да се извърши действието заради проблем с правата.',
	105 : 'Невалидно файлово разширение.',
	109 : 'Невалидна заявка.',
	110 : 'Неизвестна грешка.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Файл или папка със същото име вече съществува.',
	116 : 'Папката не е намерена, опреснете и опитайте отново.',
	117 : 'Файлът не е намерен, опреснете и опитайте отново.',
	118 : 'Пътищата за цел и източник трябва да са еднакви.',
	201 : 'Файл с такова име съществува, каченият файл е преименуван на "%1".',
	202 : 'Невалиден файл.',
	203 : 'Невалиден файл. Размерът е прекалено голям.',
	204 : 'Каченият файл е повреден.',
	205 : 'Няма временна папка за качените файлове.',
	206 : 'Качването е спряно заради проблеми със сигурността. Файлът съдържа HTML данни.',
	207 : 'Каченият файл е преименуван на "%1".',
	300 : 'Преместването на файловете пропадна.',
	301 : 'Копирането на файловете пропадна.',
	500 : 'Файловият браузър е изключен заради проблеми със сигурността. Моля свържете се с Вашия системен администратор и проверете конфигурацията.',
	501 : 'Поддръжката за миниатюри е изключена.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Името на файла не може да празно.',
		FileExists		: 'Файлът %s вече е наличен.',
		FolderEmpty		: 'Името на папката не може да празно.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Името на файла не може да съдържа следните знаци: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Името на папката не може да съдържа следните знаци: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Не е възможно отварянето на файла в нов прозорец. Моля конфигурирайте браузъра си и изключете блокирането на изкачащи прозорци за този сайт.',
		XmlError		: 'Не е възможно зареждането да данни чрез XML от уеб сървъра.',
		XmlEmpty		: 'Не е възможно зареждането на XML данни от уеб сървъра. Сървърът върна празен отговор.',
		XmlRawResponse	: 'Отговор от сървъра: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Оразмеряване %s',
		sizeTooBig		: 'Не бе възможно оразмеряването, защото зададените размери са по-големи от оригинала (%size).',
		resizeSuccess	: 'Снимката е оразмерена успешно.',
		thumbnailNew	: 'Създаване на миниатюра',
		thumbnailSmall	: 'Малка (%s)',
		thumbnailMedium	: 'Средна (%s)',
		thumbnailLarge	: 'Голяма (%s)',
		newSize			: 'Изберете нов размер',
		width			: 'Ширина',
		height			: 'Височина',
		invalidHeight	: 'Невалидна височина.',
		invalidWidth	: 'Невалидна ширина.',
		invalidName		: 'Невалидно име на файл.',
		newImage		: 'Създаване на нова снимка',
		noExtensionChange : 'Файловото разширение не може да бъде сменено.',
		imageSmall		: 'Оригиналната снимка е прекалено малка.',
		contextMenuName	: 'Оразмеряване',
		lockRatio		: 'Заключване на съотношението',
		resetSize		: 'Нулиране на размера'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Запис',
		fileOpenError	: 'Невъзможно отваряне на файла.',
		fileSaveSuccess	: 'Файлът е записан успешно.',
		contextMenuName	: 'Промяна',
		loadingFile		: 'Зареждане на файл, моля почакайте...'
	},

	Maximize :
	{
		maximize : 'Максимизиране',
		minimize : 'Минимизиране'
	},

	Gallery :
	{
		current : 'Снимка {current} от общо {total}'
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
