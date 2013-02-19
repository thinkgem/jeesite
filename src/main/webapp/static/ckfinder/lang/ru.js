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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Russian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['ru'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, недоступно</span>',
		confirmCancel	: 'Внесенные вами изменения будут утеряны. Вы уверены?',
		ok				: 'OK',
		cancel			: 'Отмена',
		confirmationTitle	: 'Подтверждение',
		messageTitle	: 'Информация',
		inputTitle		: 'Вопрос',
		undo			: 'Отменить',
		redo			: 'Повторить',
		skip			: 'Пропустить',
		skipAll			: 'Пропустить все',
		makeDecision	: 'Что следует сделать?',
		rememberDecision: 'Запомнить мой выбор'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'ru',

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
	DateTime : 'dd.mm.yyyy H:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Папки',
	FolderLoading	: 'Загрузка...',
	FolderNew		: 'Пожалуйста, введите новое имя папки: ',
	FolderRename	: 'Пожалуйста, введите новое имя папки: ',
	FolderDelete	: 'Вы уверены, что хотите удалить папку "%1"?',
	FolderRenaming	: ' (Переименовываю...)',
	FolderDeleting	: ' (Удаляю...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Пожалуйста, введите новое имя файла: ',
	FileRenameExt	: 'Вы уверены, что хотите изменить расширение файла? Файл может стать недоступным.',
	FileRenaming	: 'Переименовываю...',
	FileDelete		: 'Вы уверены, что хотите удалить файл "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Загрузка...',
	FilesEmpty		: 'Пустая папка',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Корзина',
	BasketClear			: 'Очистить корзину',
	BasketRemove		: 'Убрать из корзины',
	BasketOpenFolder	: 'Перейти в папку этого файла',
	BasketTruncateConfirm : 'Вы точно хотите очистить корзину?',
	BasketRemoveConfirm	: 'Вы точно хотите убрать файл "%1" из корзины?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'В корзине пока нет файлов, добавьте новые с помощью драг-н-дропа (перетащите файл в корзину).',
	BasketCopyFilesHere	: 'Скопировать файл из корзины',
	BasketMoveFilesHere	: 'Переместить файл из корзины',

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
	Upload		: 'Загрузить файл',
	UploadTip	: 'Загрузить новый файл',
	Refresh		: 'Обновить список',
	Settings	: 'Настройка',
	Help		: 'Помощь',
	HelpTip		: 'Помощь',

	// Context Menus
	Select			: 'Выбрать',
	SelectThumbnail : 'Выбрать миниатюру',
	View			: 'Посмотреть',
	Download		: 'Сохранить',

	NewSubFolder	: 'Новая папка',
	Rename			: 'Переименовать',
	Delete			: 'Удалить',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Копировать',
	MoveDragDrop	: 'Переместить',

	// Dialogs
	RenameDlgTitle		: 'Переименовать',
	NewNameDlgTitle		: 'Новое имя',
	FileExistsDlgTitle	: 'Файл уже существует',
	SysErrorDlgTitle : 'Системная ошибка',

	FileOverwrite	: 'Заменить файл',
	FileAutorename	: 'Автоматически переименовывать',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'ОК',
	CancelBtn	: 'Отмена',
	CloseBtn	: 'Закрыть',

	// Upload Panel
	UploadTitle			: 'Загрузить новый файл',
	UploadSelectLbl		: 'Выбрать файл для загрузки',
	UploadProgressLbl	: '(Загрузка в процессе, пожалуйста подождите...)',
	UploadBtn			: 'Загрузить выбранный файл',
	UploadBtnCancel		: 'Отмена',

	UploadNoFileMsg		: 'Пожалуйста, выберите файл на вашем компьютере.',
	UploadNoFolder		: 'Пожалуйста, выберите папку, в которую вы хотите загрузить файл.',
	UploadNoPerms		: 'Загрузка файлов запрещена.',
	UploadUnknError		: 'Ошибка при передаче файла.',
	UploadExtIncorrect	: 'В эту папку нельзя загружать файлы с таким расширением.',

	// Flash Uploads
	UploadLabel			: 'Файлы для загрузки',
	UploadTotalFiles	: 'Всего файлов:',
	UploadTotalSize		: 'Общий размер:',
	UploadSend			: 'Загрузить файл',
	UploadAddFiles		: 'Добавить файлы',
	UploadClearFiles	: 'Очистить',
	UploadCancel		: 'Отменить загрузку',
	UploadRemove		: 'Убрать',
	UploadRemoveTip		: 'Убрать !f',
	UploadUploaded		: 'Загружено !n%',
	UploadProcessing	: 'Загружаю...',

	// Settings Panel
	SetTitle		: 'Настройка',
	SetView			: 'Внешний вид:',
	SetViewThumb	: 'Миниатюры',
	SetViewList		: 'Список',
	SetDisplay		: 'Показывать:',
	SetDisplayName	: 'Имя файла',
	SetDisplayDate	: 'Дата',
	SetDisplaySize	: 'Размер файла',
	SetSort			: 'Сортировка:',
	SetSortName		: 'по имени файла',
	SetSortDate		: 'по дате',
	SetSortSize		: 'по размеру',
	SetSortExtension		: 'по расширению',

	// Status Bar
	FilesCountEmpty : '<Пустая папка>',
	FilesCountOne	: '1 файл',
	FilesCountMany	: '%1 файлов',

	// Size and Speed
	Kb				: '%1 KБ',
	Mb				: '%1 MB', // MISSING
	Gb				: '%1 GB', // MISSING
	SizePerSecond	: '%1/s', // MISSING

	// Connector Error Messages.
	ErrorUnknown	: 'Невозможно завершить запрос. (Ошибка %1)',
	Errors :
	{
	 10 : 'Неверная команда.',
	 11 : 'Тип ресурса не указан в запросе.',
	 12 : 'Неверный запрошенный тип ресурса.',
	102 : 'Неверное имя файла или папки.',
	103 : 'Невозможно завершить запрос из-за ограничений авторизации.',
	104 : 'Невозможно завершить запрос из-за ограничения разрешений файловой системы.',
	105 : 'Неверное расширение файла.',
	109 : 'Неверный запрос.',
	110 : 'Неизвестная ошибка.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Файл или папка с таким именем уже существует.',
	116 : 'Папка не найдена. Пожалуйста, обновите вид папок и попробуйте еще раз.',
	117 : 'Файл не найден. Пожалуйста, обновите список файлов и попробуйте еще раз.',
	118 : 'Исходное расположение файла совпадает с указанным.',
	201 : 'Файл с таким именем уже существует. Загруженный файл был переименован в "%1".',
	202 : 'Неверный файл.',
	203 : 'Неверный файл. Размер файла слишком большой.',
	204 : 'Загруженный файл поврежден.',
	205 : 'Недоступна временная папка для загрузки файлов на сервер.',
	206 : 'Загрузка отменена из-за соображений безопасности. Файл содержит похожие на HTML данные.',
	207 : 'Загруженный файл был переименован в "%1".',
	300 : 'Произошла ошибка при перемещении файла(ов).',
	301 : 'Произошла ошибка при копировании файла(ов).',
	500 : 'Браузер файлов отключен из-за соображений безопасности. Пожалуйста, сообщите вашему системному администратру и проверьте конфигурационный файл CKFinder.',
	501 : 'Поддержка миниатюр отключена.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Имя файла не может быть пустым.',
		FileExists		: 'Файл %s уже существует.',
		FolderEmpty		: 'Имя папки не может быть пустым.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Имя файла не может содержать любой из перечисленных символов: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Имя папки не может содержать любой из перечисленных символов: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Невозможно открыть файл в новом окне. Пожалуйста, проверьте настройки браузера и отключите блокировку всплывающих окон для этого сайта.',
		XmlError		: 'Ошибка при разборе XML-ответа сервера.',
		XmlEmpty		: 'Невозможно прочитать XML-ответ сервера, получена пустая строка.',
		XmlRawResponse	: 'Необработанный ответ сервера: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Изменить размеры %s',
		sizeTooBig		: 'Нельзя указывать размеры больше, чем у оригинального файла (%size).',
		resizeSuccess	: 'Размеры успешно изменены.',
		thumbnailNew	: 'Создать миниатюру(ы)',
		thumbnailSmall	: 'Маленькая (%s)',
		thumbnailMedium	: 'Средняя (%s)',
		thumbnailLarge	: 'Большая (%s)',
		newSize			: 'Установить новые размеры',
		width			: 'Ширина',
		height			: 'Высота',
		invalidHeight	: 'Высота должна быть числом больше нуля.',
		invalidWidth	: 'Ширина должна быть числом больше нуля.',
		invalidName		: 'Неверное имя файла.',
		newImage		: 'Сохранить как новый файл',
		noExtensionChange : 'Не удалось поменять расширение файла.',
		imageSmall		: 'Исходная картинка слишком маленькая.',
		contextMenuName	: 'Изменить размер',
		lockRatio		: 'Сохранять пропорции',
		resetSize		: 'Вернуть обычные размеры'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Сохранить',
		fileOpenError	: 'Не удалось открыть файл.',
		fileSaveSuccess	: 'Файл успешно сохранен.',
		contextMenuName	: 'Редактировать',
		loadingFile		: 'Файл загружается, пожалуйста подождите...'
	},

	Maximize :
	{
		maximize : 'Развернуть',
		minimize : 'Свернуть'
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
