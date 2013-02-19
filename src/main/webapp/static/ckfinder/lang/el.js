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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Greek
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['el'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, μη διαθέσιμο</span>',
		confirmCancel	: 'Κάποιες από τις επιλογές έχουν αλλάξει. Θέλετε σίγουρα να κλείσετε το παράθυρο διαλόγου;',
		ok				: 'OK',
		cancel			: 'Ακύρωση',
		confirmationTitle	: 'Επιβεβαίωση',
		messageTitle	: 'Πληροφορίες',
		inputTitle		: 'Ερώτηση',
		undo			: 'Αναίρεση',
		redo			: 'Επαναφορά',
		skip			: 'Παράβλεψη',
		skipAll			: 'Παράβλεψη όλων',
		makeDecision	: 'Ποια ενέργεια πρέπει να ληφθεί;',
		rememberDecision: 'Να θυμάσαι την απόφασή μου'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'el',

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
	DateAmPm : ['ΜΜ', 'ΠΜ'],

	// Folders
	FoldersTitle	: 'Φάκελοι',
	FolderLoading	: 'Φόρτωση...',
	FolderNew		: 'Παρακαλούμε πληκτρολογήστε την ονομασία του νέου φακέλου: ',
	FolderRename	: 'Παρακαλούμε πληκτρολογήστε την νέα ονομασία του φακέλου: ',
	FolderDelete	: 'Είστε σίγουροι ότι θέλετε να διαγράψετε το φάκελο "%1";',
	FolderRenaming	: ' (Μετονομασία...)',
	FolderDeleting	: ' (Διαγραφή...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Παρακαλούμε πληκτρολογήστε την νέα ονομασία του αρχείου: ',
	FileRenameExt	: 'Είστε σίγουροι ότι θέλετε να αλλάξετε την επέκταση του αρχείου; Μετά από αυτή την ενέργεια το αρχείο είναι δυνατόν να μην μπορεί να χρησιμοποιηθεί',
	FileRenaming	: 'Μετονομασία...',
	FileDelete		: 'Είστε σίγουροι ότι θέλετε να διαγράψετε το αρχείο "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Φόρτωση...',
	FilesEmpty		: 'Ο φάκελος είναι κενός.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Καλάθι',
	BasketClear			: 'Καθαρισμός καλαθιού',
	BasketRemove		: 'Αφαίρεση από το καλάθι',
	BasketOpenFolder	: 'Άνοιγμα γονικού φακέλου',
	BasketTruncateConfirm : 'Θέλετε σίγουρα να αφαιρέσετε όλα τα αρχεία από το καλάθι;',
	BasketRemoveConfirm	: 'Θέλετε σίγουρα να αφαιρέσετε το αρχείο "%1" από το καλάθι;',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Δεν υπάρχουν αρχεία στο καλάθι, μεταφέρετε κάποια με drag and drop.',
	BasketCopyFilesHere	: 'Αντιγραφή αρχείων από το καλάθι',
	BasketMoveFilesHere	: 'Μετακίνηση αρχείων από το καλάθι',

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
	Upload		: 'Μεταφόρτωση',
	UploadTip	: 'Μεταφόρτωση νέου αρχείου',
	Refresh		: 'Ανανέωση',
	Settings	: 'Ρυθμίσεις',
	Help		: 'Βοήθεια',
	HelpTip		: 'Βοήθεια',

	// Context Menus
	Select			: 'Επιλογή',
	SelectThumbnail : 'Επιλογή μικρογραφίας',
	View			: 'Προβολή',
	Download		: 'Λήψη αρχείου',

	NewSubFolder	: 'Νέος υποφάκελος',
	Rename			: 'Μετονομασία',
	Delete			: 'Διαγραφή',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Αντέγραψε εδώ',
	MoveDragDrop	: 'Μετακίνησε εδώ',

	// Dialogs
	RenameDlgTitle		: 'Μετονομασία',
	NewNameDlgTitle		: 'Νέα ονομασία',
	FileExistsDlgTitle	: 'Το αρχείο υπάρχει ήδη',
	SysErrorDlgTitle : 'Σφάλμα συστήματος',

	FileOverwrite	: 'Αντικατάσταση αρχείου',
	FileAutorename	: 'Αυτόματη-μετονομασία',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Ακύρωση',
	CloseBtn	: 'Κλείσιμο',

	// Upload Panel
	UploadTitle			: 'Μεταφόρτωση νέου αρχείου',
	UploadSelectLbl		: 'επιλέξτε το αρχείο που θέλετε να μεταφερθεί κάνοντας κλίκ στο κουμπί',
	UploadProgressLbl	: '(Η μεταφόρτωση εκτελείται, παρακαλούμε περιμένετε...)',
	UploadBtn			: 'Μεταφόρτωση επιλεγμένου αρχείου',
	UploadBtnCancel		: 'Ακύρωση',

	UploadNoFileMsg		: 'Παρακαλούμε επιλέξτε ένα αρχείο από τον υπολογιστή σας.',
	UploadNoFolder		: 'Παρακαλούμε επιλέξτε ένα φάκελο πριν εκκινήσετε την διαδικασία της μεταφόρτωσης.',
	UploadNoPerms		: 'Η μεταφόρτωση των αρχείων δεν επιτρέπεται.',
	UploadUnknError		: 'Παρουσιάστηκε σφάλμα κατά την αποστολή του αρχείου.',
	UploadExtIncorrect	: 'Η επέκταση του αρχείου δεν επιτρέπεται σε αυτόν τον φάκελο.',

	// Flash Uploads
	UploadLabel			: 'Αρχεία προς μεταφόρτωση',
	UploadTotalFiles	: 'Συνολικά αρχεία:',
	UploadTotalSize		: 'Συνολικό μέγεθος:',
	UploadSend			: 'Μεταφόρτωση',
	UploadAddFiles		: 'Προσθήκη αρχείων',
	UploadClearFiles	: 'Αφαίρεση αρχείων',
	UploadCancel		: 'Ακύρωση μεταφόρτωσης',
	UploadRemove		: 'Αφαίρεση',
	UploadRemoveTip		: 'Αφαίρεση !f',
	UploadUploaded		: 'Μεταφορτώθηκε !n%',
	UploadProcessing	: 'Επεξεργασία...',

	// Settings Panel
	SetTitle		: 'Ρυθμίσεις',
	SetView			: 'Προβολή:',
	SetViewThumb	: 'Μικρογραφίες',
	SetViewList		: 'Λίστα',
	SetDisplay		: 'Εμφάνιση:',
	SetDisplayName	: 'Όνομα αρχείου',
	SetDisplayDate	: 'Ημερομηνία',
	SetDisplaySize	: 'Μέγεθος αρχείου',
	SetSort			: 'Ταξινόμηση:',
	SetSortName		: 'βάσει Όνοματος αρχείου',
	SetSortDate		: 'βάσει Ημερομήνιας',
	SetSortSize		: 'βάσει Μεγέθους',
	SetSortExtension		: 'βάσει Επέκτασης',

	// Status Bar
	FilesCountEmpty : '<Κενός Φάκελος>',
	FilesCountOne	: '1 αρχείο',
	FilesCountMany	: '%1 αρχεία',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Η ενέργεια δεν ήταν δυνατόν να εκτελεστεί. (Σφάλμα %1)',
	Errors :
	{
	 10 : 'Λανθασμένη Εντολή.',
	 11 : 'Το resource type δεν ήταν δυνατόν να προσδιοριστεί.',
	 12 : 'Το resource type δεν είναι έγκυρο.',
	102 : 'Το όνομα αρχείου ή φακέλου δεν είναι έγκυρο.',
	103 : 'Δεν ήταν δυνατή η εκτέλεση της ενέργειας λόγω έλλειψης δικαιωμάτων ασφαλείας.',
	104 : 'Δεν ήταν δυνατή η εκτέλεση της ενέργειας λόγω περιορισμών του συστήματος αρχείων.',
	105 : 'Λανθασμένη επέκταση αρχείου.',
	109 : 'Λανθασμένη ενέργεια.',
	110 : 'Άγνωστο λάθος.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Το αρχείο ή φάκελος υπάρχει ήδη.',
	116 : 'Ο φάκελος δεν βρέθηκε. Παρακαλούμε ανανεώστε τη σελίδα και προσπαθήστε ξανά.',
	117 : 'Το αρχείο δεν βρέθηκε. Παρακαλούμε ανανεώστε τη σελίδα και προσπαθήστε ξανά.',
	118 : 'Η αρχική και τελική διαδρομή είναι ίδιες.',
	201 : 'Ένα αρχείο με την ίδια ονομασία υπάρχει ήδη. Το μεταφορτωμένο αρχείο μετονομάστηκε σε "%1".',
	202 : 'Λανθασμένο αρχείο.',
	203 : 'Λανθασμένο αρχείο. Το μέγεθος του αρχείου είναι πολύ μεγάλο.',
	204 : 'Το μεταφορτωμένο αρχείο είναι χαλασμένο.',
	205 : 'Δεν υπάρχει προσωρινός φάκελος για να χρησιμοποιηθεί για τις μεταφορτώσεις των αρχείων.',
	206 : 'Η μεταφόρτωση ακυρώθηκε για λόγους ασφαλείας. Το αρχείο περιέχει δεδομένα μορφής HTML.',
	207 : 'Το μεταφορτωμένο αρχείο μετονομάστηκε σε "%1".',
	300 : 'Η μετακίνηση των αρχείων απέτυχε.',
	301 : 'Η αντιγραφή των αρχείων απέτυχε.',
	500 : 'Ο πλοηγός αρχείων έχει απενεργοποιηθεί για λόγους ασφαλείας. Παρακαλούμε επικοινωνήστε με τον διαχειριστή της ιστοσελίδας και ελέγξτε το αρχείο ρυθμίσεων του πλοηγού (CKFinder).',
	501 : 'Η υποστήριξη των μικρογραφιών έχει απενεργοποιηθεί.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Η ονομασία του αρχείου δεν μπορεί να είναι κενή.',
		FileExists		: 'Το αρχείο %s υπάρχει ήδη.',
		FolderEmpty		: 'Η ονομασία του φακέλου δεν μπορεί να είναι κενή.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Η ονομασία του αρχείου δεν μπορεί να περιέχει τους ακόλουθους χαρακτήρες: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Η ονομασία του φακέλου δεν μπορεί να περιέχει τους ακόλουθους χαρακτήρες: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Δεν ήταν εφικτό να ανοίξει το αρχείο σε νέο παράθυρο. Παρακαλώ, ελέγξτε τις ρυθμίσεις τους πλοηγού σας και απενεργοποιήστε όλους τους popup blockers για αυτή την ιστοσελίδα.',
		XmlError		: 'Δεν ήταν εφικτή η σωστή ανάγνωση του XML response από τον διακομιστή.',
		XmlEmpty		: 'Δεν ήταν εφικτή η φόρτωση του XML response από τον διακομιστή. Ο διακομιστής επέστρεψε ένα κενό response.',
		XmlRawResponse	: 'Raw response από τον διακομιστή: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Αλλαγή διαστάσεων της εικόνας %s',
		sizeTooBig		: 'Το πλάτος ή το ύψος της εικόνας δεν μπορεί να είναι μεγαλύτερα των αρχικών διαστάσεων (%size).',
		resizeSuccess	: 'Οι διαστάσεις της εικόνας άλλαξαν επιτυχώς.',
		thumbnailNew	: 'Δημιουργία νέας μικρογραφίας',
		thumbnailSmall	: 'Μικρή (%s)',
		thumbnailMedium	: 'Μεσαία (%s)',
		thumbnailLarge	: 'Μεγάλη (%s)',
		newSize			: 'Ορισμός νέου μεγέθους',
		width			: 'Πλάτος',
		height			: 'Ύψος',
		invalidHeight	: 'Μη έγκυρο ύψος.',
		invalidWidth	: 'Μη έγκυρο πλάτος.',
		invalidName		: 'Μη έγκυρο όνομα αρχείου.',
		newImage		: 'Δημιουργία νέας εικόνας',
		noExtensionChange : 'Η επέκταση του αρχείου δεν μπορεί να αλλάξει.',
		imageSmall		: 'Η αρχική εικόνα είναι πολύ μικρή.',
		contextMenuName	: 'Αλλαγή διαστάσεων',
		lockRatio		: 'Κλείδωμα αναλογίας',
		resetSize		: 'Επαναφορά αρχικού μεγέθους'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Αποθήκευση',
		fileOpenError	: 'Δεν ήταν εφικτό το άνοιγμα του αρχείου.',
		fileSaveSuccess	: 'Το αρχείο αποθηκεύτηκε επιτυχώς.',
		contextMenuName	: 'Επεξεργασία',
		loadingFile		: 'Φόρτωση αρχείου, παρακαλώ περιμένετε...'
	},

	Maximize :
	{
		maximize : 'Μεγιστοποίηση',
		minimize : 'Ελαχιστοποίηση'
	},

	Gallery :
	{
		current : 'Εικόνα {current} από {total}'
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
