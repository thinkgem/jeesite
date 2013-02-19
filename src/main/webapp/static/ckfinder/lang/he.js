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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Hebrew
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['he'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, לא זמין</span>',
		confirmCancel	: 'חלק מהאפשרויות שונו. האם לסגור את החלון?',
		ok				: 'אישור',
		cancel			: 'ביטול',
		confirmationTitle	: 'אישור',
		messageTitle	: 'הודעה',
		inputTitle		: 'שאלה',
		undo			: 'לבטל',
		redo			: 'לעשות שוב',
		skip			: 'דלג',
		skipAll			: 'דלג הכל',
		makeDecision	: 'איזו פעולה לבצע?',
		rememberDecision: 'זכור החלטתי'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'rtl',
	HelpLang : 'en',
	LangCode : 'he',

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
	DateTime : 'd/m/yyyy HH:MM',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'תיקיות',
	FolderLoading	: 'טוען...',
	FolderNew		: 'יש להקליד שם חדש לתיקיה: ',
	FolderRename	: 'יש להקליד שם חדש לתיקיה: ',
	FolderDelete	: 'האם למחוק את התיקיה "%1" ?',
	FolderRenaming	: ' (משנה שם...)',
	FolderDeleting	: ' (מוחק...)',
	DestinationFolder	: 'תיקיית יעד',

	// Files
	FileRename		: 'יש להקליד שם חדש לקובץ: ',
	FileRenameExt	: 'האם לשנות את הסיומת של הקובץ?',
	FileRenaming	: 'משנה שם...',
	FileDelete		: 'האם למחוק את הקובץ "%1"?',
	FilesDelete	: 'האם למחוק %1 קבצים?',
	FilesLoading	: 'טוען...',
	FilesEmpty		: 'תיקיה ריקה',
	DestinationFile	: 'קובץ יעד',
	SkippedFiles	: 'רשימת קבצים שדולגו:',

	// Basket
	BasketFolder		: 'סל קבצים',
	BasketClear			: 'ניקוי סל הקבצים',
	BasketRemove		: 'מחיקה מסל הקבצים',
	BasketOpenFolder	: 'פתיחת תיקיית אב',
	BasketTruncateConfirm : 'האם למחוק את כל הקבצים מסל הקבצים?',
	BasketRemoveConfirm	: 'האם למחוק את הקובץ "%1" מסל הקבצים?',
	BasketRemoveConfirmMultiple	: 'האם למחוק %1 קבצים מסל הקבצים?',
	BasketEmpty			: 'אין קבצים בסל הקבצים, יש לגרור לכאן קובץ.',
	BasketCopyFilesHere	: 'העתקת קבצים מסל הקבצים',
	BasketMoveFilesHere	: 'הזזת קבצים מסל הקבצים',

	// Global messages
	OperationCompletedSuccess	: 'הפעולה הושלמה בהצלחה.',
	OperationCompletedErrors		: 'הפעולה הושלמה עם שגיאות.',
	FileError				: '%s: %e',

	// Move and Copy files
	MovedFilesNumber		: 'מספר קבצים שהוזזו: %s.',
	CopiedFilesNumber	: 'מספר קבצים שהועתקו: %s.',
	MoveFailedList		: 'המערכת לא הצליחה להזיז את הקבצים הבאים:<br />%s',
	CopyFailedList		: 'המערכת לא הצליחה להעתיק את הקבצים הבאים:<br />%s',

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'העלאה',
	UploadTip	: 'העלאת קובץ חדש',
	Refresh		: 'ריענון',
	Settings	: 'הגדרות',
	Help		: 'עזרה',
	HelpTip		: 'עזרה',

	// Context Menus
	Select			: 'בחירה',
	SelectThumbnail : 'בחירת תמונה מוקטנת',
	View			: 'צפיה',
	Download		: 'הורדה',

	NewSubFolder	: 'תת-תיקיה חדשה',
	Rename			: 'שינוי שם',
	Delete			: 'מחיקה',
	DeleteFiles		: 'מחיקת קבצים',

	CopyDragDrop	: 'העתקת קבצים לכאן',
	MoveDragDrop	: 'הזזת קבצים לכאן',

	// Dialogs
	RenameDlgTitle		: 'שינוי שם',
	NewNameDlgTitle		: 'שם חדש',
	FileExistsDlgTitle	: 'קובץ זה כבר קיים',
	SysErrorDlgTitle : 'שגיאת מערכת',

	FileOverwrite	: 'החלפה',
	FileAutorename	: 'שינוי שם אוטומטי',
	ManuallyRename	: 'שינוי שם ידני',

	// Generic
	OkBtn		: 'אישור',
	CancelBtn	: 'ביטול',
	CloseBtn	: 'סגור',

	// Upload Panel
	UploadTitle			: 'העלאת קובץ חדש',
	UploadSelectLbl		: 'בחירת קובץ להעלאה',
	UploadProgressLbl	: '(העלאה מתבצעת, נא להמתין...)',
	UploadBtn			: 'העלאת קובץ',
	UploadBtnCancel		: 'ביטול',

	UploadNoFileMsg		: 'יש לבחור קובץ מהמחשב',
	UploadNoFolder		: 'יש לבחור תיקיה לפני ההעלאה.',
	UploadNoPerms		: 'העלאת קובץ אסורה.',
	UploadUnknError		: 'שגיאה בשליחת הקובץ.',
	UploadExtIncorrect	: 'סוג קובץ זה לא מאושר בתיקיה זאת.',

	// Flash Uploads
	UploadLabel			: 'קבצים להעלאה',
	UploadTotalFiles	: 'כמות קבצים:',
	UploadTotalSize		: 'גודל סופי:',
	UploadSend			: 'התחלת העלאה',
	UploadAddFiles		: 'הוספת קבצים',
	UploadClearFiles	: 'ניקוי קבצים',
	UploadCancel		: 'ביטול העלאה',
	UploadRemove		: 'מחיקה מהרשימה',
	UploadRemoveTip		: 'מחיקת הקובץ !f',
	UploadUploaded		: '!n% הועלו',
	UploadProcessing	: 'מעבד...',

	// Settings Panel
	SetTitle		: 'הגדרות',
	SetView			: 'צפיה:',
	SetViewThumb	: 'תמונות מוקטנות',
	SetViewList		: 'רשימה',
	SetDisplay		: 'תצוגה:',
	SetDisplayName	: 'שם קובץ',
	SetDisplayDate	: 'תאריך',
	SetDisplaySize	: 'גודל קובץ',
	SetSort			: 'מיון:',
	SetSortName		: 'לפי שם',
	SetSortDate		: 'לפי תאריך',
	SetSortSize		: 'לפי גודל',
	SetSortExtension		: 'לפי סיומת (Extension)',

	// Status Bar
	FilesCountEmpty : '<תיקיה ריקה>',
	FilesCountOne	: 'קובץ 1',
	FilesCountMany	: '%1 קבצים',

	// Size and Speed
	Kb				: '%1KB',
	Mb				: '%1MB',
	Gb				: '%1GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'לא היה ניתן להשלים את הבקשה. (שגיאה %1)',
	Errors :
	{
	 10 : 'הוראה לא תקינה.',
	 11 : 'סוג המשאב לא צויין בבקשה לשרת.',
	 12 : 'סוג המשאב המצויין לא תקין.',
	102 : 'שם הקובץ או התיקיה לא תקין.',
	103 : 'לא היה ניתן להשלים את הבקשה בשל הרשאות מוגבלות.',
	104 : 'לא היה ניתן להשלים את הבקשה בשל הרשאות מערכת קבצים מוגבלות.',
	105 : 'סיומת הקובץ לא תקינה.',
	109 : 'בקשה לא תקינה.',
	110 : 'שגיאה לא ידועה.',
	111 : 'לא ניתן היה להשלים את הבקשה בשל הגודל החריג של הקובץ הנוצר.',
	115 : 'כבר קיים/ת קובץ או תיקיה באותו השם.',
	116 : 'התיקיה לא נמצאה. נא לרענן ולנסות שוב.',
	117 : 'הקובץ לא נמצא. נא לרענן ולנסות שוב.',
	118 : 'כתובות המקור והיעד זהות.',
	201 : 'קובץ עם אותו השם כבר קיים. שם הקובץ שהועלה שונה ל "%1"',
	202 : 'הקובץ לא תקין.',
	203 : 'הקובץ לא תקין. גודל הקובץ גדול מדי.',
	204 : 'הקובץ המועלה לא תקין',
	205 : 'לא קיימת בשרת תיקיה זמנית להעלאת קבצים.',
	206 : 'ההעלאה בוטלה מסיבות אבטחה. הקובץ מכיל תוכן שדומה ל-HTML.',
	207 : 'שם הקובץ שהועלה שונה ל "%1"',
	300 : 'העברת הקבצים נכשלה.',
	301 : 'העתקת הקבצים נכשלה.',
	500 : 'דפדפן הקבצים מנוטרל מסיבות אבטחה. יש לפנות למנהל המערכת ולבדוק את קובץ התצורה של CKFinder.',
	501 : 'התמיכה בתמונות מוקטנות מבוטלת.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'שם הקובץ לא יכול להיות ריק',
		FileExists		: 'הקובץ %s כבר קיים',
		FolderEmpty		: 'שם התיקיה לא יכול להיות ריק',
		FolderExists	: 'התיקיה %s כבר קיימת.',
		FolderNameExists	: 'התיקיה כבר קיימת.',

		FileInvChar		: 'שם הקובץ לא יכול לכלול תווים הבאים: \n\\ / : * ? " < > |',
		FolderInvChar	: 'שם התיקיה לא יכול לכלול תווים הבאים: \n\\ / : * ? " < > |',

		PopupBlockView	: 'לא היה ניתן לפתוח קובץ בחלון חדש. נא לבדוק את הגדרות הדפדפן ולבטל את חוסמי החלונות הקובצים.',
		XmlError		: 'לא היה ניתן לטעון מהשרת כהלכה את קובץ ה-XML.',
		XmlEmpty		: 'לא היה ניתן לטעון מהשרת את קובץ ה-XML. השרת החזיר תגובה ריקה.',
		XmlRawResponse	: 'תגובה גולמית מהשרת: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'שינוי גודל התמונה %s',
		sizeTooBig		: 'גובה ורוחב התמונה לא יכולים להיות גדולים מהגודל המקורי שלה (%size).',
		resizeSuccess	: 'גודל התמונה שונה שהצלחה.',
		thumbnailNew	: 'יצירת תמונה מוקטנת (Thumbnail)',
		thumbnailSmall	: 'קטנה (%s)',
		thumbnailMedium	: 'בינונית (%s)',
		thumbnailLarge	: 'גדולה (%s)',
		newSize			: 'קביעת גודל חדש',
		width			: 'רוחב',
		height			: 'גובה',
		invalidHeight	: 'גובה לא חוקי.',
		invalidWidth	: 'רוחב לא חוקי.',
		invalidName		: 'שם הקובץ לא חוקי.',
		newImage		: 'יצירת תמונה חדשה',
		noExtensionChange : 'לא ניתן לשנות את סוג הקובץ.',
		imageSmall		: 'התמונה המקורית קטנה מדי',
		contextMenuName	: 'שינוי גודל',
		lockRatio		: 'נעילת היחס',
		resetSize		: 'איפוס הגודל'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'שמירה',
		fileOpenError	: 'לא היה ניתן לפתוח את הקובץ.',
		fileSaveSuccess	: 'הקובץ נשמר בהצלחה.',
		contextMenuName	: 'עריכה',
		loadingFile		: 'טוען קובץ, נא להמתין...'
	},

	Maximize :
	{
		maximize : 'הגדלה למקסימום',
		minimize : 'הקטנה למינימום'
	},

	Gallery :
	{
		current : 'תמונה {current} מתוך {total}'
	},

	Zip :
	{
		extractHereLabel	: 'חילוץ לפה',
		extractToLabel		: 'חילוץ ל...',
		downloadZipLabel	: 'הורדה כקובץ ZIP',
		compressZipLabel	: 'דחיסה לקובץ ZIP',
		removeAndExtract	: 'מחיקת הקובץ וחילוצו',
		extractAndOverwrite	: 'חילוץ והחלפת קבצים קיימים',
		extractSuccess		: 'הקבצים חולצו בהצלחה.'
	}
};
