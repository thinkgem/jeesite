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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Persian
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['fa'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, عدم دسترسی</span>',
		confirmCancel	: 'برخی از گزینه ها تغییر کرده است، آیا مایل به بستن این پنجره هستید؟',
		ok				: 'تائید',
		cancel			: 'لغو',
		confirmationTitle	: 'تاییدیه',
		messageTitle	: 'اطلاعات',
		inputTitle		: 'سوال',
		undo			: 'حالت قبلی',
		redo			: 'حالت بعدی',
		skip			: 'نادیده گرفتن',
		skipAll			: 'نادیده گرفتن همه',
		makeDecision	: 'چه عملی انجام شود؟',
		rememberDecision: 'انتخاب من را بیاد داشته باش'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'rtl',
	HelpLang : 'en',
	LangCode : 'fa',

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
	DateTime : 'yyyy/mm/dd h:MM aa',
	DateAmPm : ['ق.ظ', 'ب.ظ'],

	// Folders
	FoldersTitle	: 'پوشه ها',
	FolderLoading	: 'بارگذاری...',
	FolderNew		: 'لطفا نام پوشه جدید را وارد کنید: ',
	FolderRename	: 'لطفا نام پوشه جدید را وارد کنید: ',
	FolderDelete	: 'آیا اطمینان دارید که قصد حذف کردن پوشه "%1" را دارید؟',
	FolderRenaming	: ' (در حال تغییر نام...)',
	FolderDeleting	: ' (در حال حذف...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'لطفا نام جدید فایل را درج کنید: ',
	FileRenameExt	: 'آیا اطمینان دارید که قصد تغییر نام پسوند این فایل را دارید؟ ممکن است فایل غیر قابل استفاده شود',
	FileRenaming	: 'در حال تغییر نام...',
	FileDelete		: 'آیا اطمینان دارید که قصد حذف نمودن فایل "%1" را دارید؟',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'بارگذاری...',
	FilesEmpty		: 'این پوشه خالی است',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'سبد',
	BasketClear			: 'پاک کردن سبد',
	BasketRemove		: 'حذف از سبد',
	BasketOpenFolder	: 'باز نمودن پوشه والد',
	BasketTruncateConfirm : 'تمام فایل های موجود در سبد حذف شود؟',
	BasketRemoveConfirm	: 'فایل "%1" از سبد حذف شود؟',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'هیچ فایلی در سبد نیست, برای افزودن فایل را به اینجا بکشید و رها کنید',
	BasketCopyFilesHere	: 'کپی فایلها از سبد',
	BasketMoveFilesHere	: 'انتقال فایلها از سبد',

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
	Upload		: 'آپلود',
	UploadTip	: 'آپلود فایل جدید',
	Refresh		: 'بروزرسانی',
	Settings	: 'تنظیمات',
	Help		: 'راهنما',
	HelpTip		: 'راهنما',

	// Context Menus
	Select			: 'انتخاب',
	SelectThumbnail : 'انتخاب تصویر کوچک',
	View			: 'نمایش',
	Download		: 'دانلود',

	NewSubFolder	: 'زیرپوشه جدید',
	Rename			: 'تغییر نام',
	Delete			: 'حذف',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'کپی فایل به اینجا',
	MoveDragDrop	: 'انتقال فایل به اینجا',

	// Dialogs
	RenameDlgTitle		: 'تغییر نام',
	NewNameDlgTitle		: 'نام جدید',
	FileExistsDlgTitle	: 'فایلی با این نام وجود دارد',
	SysErrorDlgTitle : 'خطای سیستم',

	FileOverwrite	: 'رونویسی',
	FileAutorename	: 'تغییر نام خودکار',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'تایید',
	CancelBtn	: 'لغو',
	CloseBtn	: 'بستن',

	// Upload Panel
	UploadTitle			: 'آپلود فایل جدید',
	UploadSelectLbl		: 'انتخاب فابل برای آپلود',
	UploadProgressLbl	: '(درحال ارسال، لطفا صبر کنید...)',
	UploadBtn			: 'آپلود فایل',
	UploadBtnCancel		: 'لغو',

	UploadNoFileMsg		: 'لطفا یک فایل جهت ارسال انتخاب کنید',
	UploadNoFolder		: 'لطفا پیش از آپلود، یک پوشه انتخاب کنید.',
	UploadNoPerms		: 'اجازه ارسال فایل نداده شنده است',
	UploadUnknError		: 'خطا در ارسال',
	UploadExtIncorrect	: 'پسوند فایل برای این پوشه مجاز نیست.',

	// Flash Uploads
	UploadLabel			: 'آپلود فایل',
	UploadTotalFiles	: 'مجموع فایلها:',
	UploadTotalSize		: 'مجموع حجم:',
	UploadSend			: 'آپلود فایل',
	UploadAddFiles		: 'افزودن فایلها',
	UploadClearFiles	: 'پاک کردن فایلها',
	UploadCancel		: 'لغو آپلود',
	UploadRemove		: 'حذف',
	UploadRemoveTip		: '!f حذف فایل',
	UploadUploaded		: '!n% آپلود شد',
	UploadProcessing	: 'در حال پردازش...',

	// Settings Panel
	SetTitle		: 'تنظیمات',
	SetView			: 'نمایش:',
	SetViewThumb	: 'تصویر کوچک',
	SetViewList		: 'فهرست',
	SetDisplay		: 'نمایش:',
	SetDisplayName	: 'نام فایل',
	SetDisplayDate	: 'تاریخ',
	SetDisplaySize	: 'اندازه فایل',
	SetSort			: 'مرتبسازی:',
	SetSortName		: 'با نام فایل',
	SetSortDate		: 'با تاریخ',
	SetSortSize		: 'با اندازه',
	SetSortExtension		: 'با پسوند',

	// Status Bar
	FilesCountEmpty : '<پوشه خالی>',
	FilesCountOne	: 'یک فایل',
	FilesCountMany	: '%1 فایل',

	// Size and Speed
	Kb				: '%1KB',
	Mb				: '%1MB',
	Gb				: '%1GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'امکان تکمیل درخواست فوق وجود ندارد (خطا: %1)',
	Errors :
	{
	 10 : 'دستور نامعتبر.',
	 11 : 'نوع منبع در درخواست تعریف نشده است.',
	 12 : 'نوع منبع درخواست شده معتبر نیست.',
	102 : 'نام فایل یا پوشه نامعتبر است.',
	103 : 'امکان کامل کردن این درخواست بخاطر محدودیت اختیارات وجود ندارد.',
	104 : 'امکان کامل کردن این درخواست بخاطر محدودیت دسترسی وجود ندارد.',
	105 : 'پسوند فایل نامعتبر  است.',
	109 : 'درخواست نامعتبر است.',
	110 : 'خطای ناشناخته.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'فایل یا پوشه ای با این نام وجود دارد',
	116 : 'پوشه یافت نشد. لطفا بروزرسانی کرده و مجددا تلاش کنید.',
	117 : 'فایل یافت نشد. لطفا فهرست فایلها را بروزرسانی کرده و مجددا تلاش کنید.',
	118 : 'منبع و مقصد مسیر یکی است.',
	201 : 'یک فایل با همان نام از قبل موجود است. فایل آپلود شده به "%1" تغییر نام یافت.',
	202 : 'فایل نامعتبر',
	203 : 'فایل نامعتبر. اندازه فایل بیش از حد بزرگ است.',
	204 : 'فایل آپلود شده خراب است.',
	205 : 'هیچ پوشه موقتی برای آپلود فایل در سرور موجود نیست.',
	206 : 'آپلود به دلایل امنیتی متوقف شد. فایل محتوی اطلاعات HTML است.',
	207 : 'فایل آپلود شده به "%1" تغییر نام یافت.',
	300 : 'انتقال فایل (ها) شکست خورد.',
	301 : 'کپی فایل (ها) شکست خورد.',
	500 : 'مرورگر فایل به دلایل امنیتی غیر فعال است. لطفا با مدیر سامانه تماس بگیرید تا تنظیمات این بخش را بررسی نماید.',
	501 : 'پشتیبانی از تصاویر کوچک غیرفعال شده است'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'نام فایل نمیتواند خالی باشد',
		FileExists		: 'فایل %s از قبل وجود دارد',
		FolderEmpty		: 'نام پوشه نمیتواند خالی باشد',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'نام فایل نباید شامل این کاراکترها باشد: \n\\ / : * ? " < > |',
		FolderInvChar	: 'نام پوشه نباید شامل این کاراکترها باشد: \n\\ / : * ? " < > |',

		PopupBlockView	: 'امکان بازگشایی فایل در پنجره جدید نیست. لطفا به بخش تنظیمات مرورگر خود مراجعه کنید و امکان بازگشایی پنجرههای بازشور را برای این سایت فعال کنید.',
		XmlError		: 'امکان بارگیری صحیح پاسخ XML از سرور مقدور نیست.',
		XmlEmpty		: 'امکان بارگیری صحیح پاسخ XML از سرور مقدور نیست. سرور پاسخ خالی بر میگرداند.',
		XmlRawResponse	: 'پاسخ اولیه از سرور: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'تغییر اندازه %s',
		sizeTooBig		: 'امکان تغییر مقادیر ابعاد طول و عرض تصویر به مقداری بیش از ابعاد اصلی ممکن نیست (%size).',
		resizeSuccess	: 'تصویر با موفقیت تغییر اندازه یافت.',
		thumbnailNew	: 'ایجاد انگشتی جدید',
		thumbnailSmall	: 'کوچک (%s)',
		thumbnailMedium	: 'متوسط (%s)',
		thumbnailLarge	: 'بزرگ (%s)',
		newSize			: 'اندازه جدید',
		width			: 'پهنا',
		height			: 'ارتفاع',
		invalidHeight	: 'ارتفاع نامعتبر.',
		invalidWidth	: 'پهنا نامعتبر.',
		invalidName		: 'نام فایل نامعتبر.',
		newImage		: 'ایجاد تصویر جدید',
		noExtensionChange : 'تغییر پسوند فایل امکان پذیر نیست.',
		imageSmall		: 'تصویر اصلی خیلی کوچک است',
		contextMenuName	: 'تغییر اندازه',
		lockRatio		: 'قفل کردن تناسب.',
		resetSize		: 'بازنشانی اندازه.'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'ذخیره',
		fileOpenError	: 'امکان باز کردن فایل نیست',
		fileSaveSuccess	: 'فایل با موفقیت ذخیره شد.',
		contextMenuName	: 'ویرایش',
		loadingFile		: 'بارگذاری فایل، منتظر باشید...'
	},

	Maximize :
	{
		maximize : 'بیشینه',
		minimize : 'کمینه'
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
