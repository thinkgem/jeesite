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
 * @fileOverview Defines the {@link CKFinder.lang} object for the English
 *		language. This is the base file for all translations.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['en'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, unavailable</span>',
		confirmCancel	: 'Some of the options were changed. Are you sure you want to close the dialog window?',
		ok				: 'OK',
		cancel			: 'Cancel',
		confirmationTitle	: 'Confirmation',
		messageTitle	: 'Information',
		inputTitle		: 'Question',
		undo			: 'Undo',
		redo			: 'Redo',
		skip			: 'Skip',
		skipAll			: 'Skip all',
		makeDecision	: 'What action should be taken?',
		rememberDecision: 'Remember my decision'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'en',

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
	DateTime : 'm/d/yyyy h:MM aa',
	DateAmPm : ['AM','PM'],

	// Folders
	FoldersTitle	: 'Folders',
	FolderLoading	: 'Loading...',
	FolderNew		: 'Please type the new folder name: ',
	FolderRename	: 'Please type the new folder name: ',
	FolderDelete	: 'Are you sure you want to delete the "%1" folder?',
	FolderRenaming	: ' (Renaming...)',
	FolderDeleting	: ' (Deleting...)',
	DestinationFolder	: 'Destination Folder',

	// Files
	FileRename		: 'Please type the new file name: ',
	FileRenameExt	: 'Are you sure you want to change the file extension? The file may become unusable.',
	FileRenaming	: 'Renaming...',
	FileDelete		: 'Are you sure you want to delete the file "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?',
	FilesLoading	: 'Loading...',
	FilesEmpty		: 'The folder is empty.',
	DestinationFile	: 'Destination File',
	SkippedFiles	: 'List of skipped files:',

	// Basket
	BasketFolder		: 'Basket',
	BasketClear			: 'Clear Basket',
	BasketRemove		: 'Remove from Basket',
	BasketOpenFolder	: 'Open Parent Folder',
	BasketTruncateConfirm : 'Do you really want to remove all files from the basket?',
	BasketRemoveConfirm	: 'Do you really want to remove the file "%1" from the basket?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?',
	BasketEmpty			: 'No files in the basket, drag and drop some.',
	BasketCopyFilesHere	: 'Copy Files from Basket',
	BasketMoveFilesHere	: 'Move Files from Basket',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.',
	OperationCompletedErrors		: 'Operation completed with errors.',
	FileError				: '%s: %e',

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.',
	CopiedFilesNumber	: 'Number of files copied: %s.',
	MoveFailedList		: 'The following files could not be moved:<br />%s',
	CopyFailedList		: 'The following files could not be copied:<br />%s',

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Upload',
	UploadTip	: 'Upload New File',
	Refresh		: 'Refresh',
	Settings	: 'Settings',
	Help		: 'Help',
	HelpTip		: 'Help',

	// Context Menus
	Select			: 'Select',
	SelectThumbnail : 'Select Thumbnail',
	View			: 'View',
	Download		: 'Download',

	NewSubFolder	: 'New Subfolder',
	Rename			: 'Rename',
	Delete			: 'Delete',
	DeleteFiles		: 'Delete Files',

	CopyDragDrop	: 'Copy Here',
	MoveDragDrop	: 'Move Here',

	// Dialogs
	RenameDlgTitle		: 'Rename',
	NewNameDlgTitle		: 'New Name',
	FileExistsDlgTitle	: 'File Already Exists',
	SysErrorDlgTitle : 'System Error',

	FileOverwrite	: 'Overwrite',
	FileAutorename	: 'Auto-rename',
	ManuallyRename	: 'Manually rename',

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Cancel',
	CloseBtn	: 'Close',

	// Upload Panel
	UploadTitle			: 'Upload New File',
	UploadSelectLbl		: 'Select a file to upload',
	UploadProgressLbl	: '(Upload in progress, please wait...)',
	UploadBtn			: 'Upload Selected File',
	UploadBtnCancel		: 'Cancel',

	UploadNoFileMsg		: 'Please select a file from your computer.',
	UploadNoFolder		: 'Please select a folder before uploading.',
	UploadNoPerms		: 'File upload not allowed.',
	UploadUnknError		: 'Error sending the file.',
	UploadExtIncorrect	: 'File extension not allowed in this folder.',

	// Flash Uploads
	UploadLabel			: 'Files to Upload',
	UploadTotalFiles	: 'Total Files:',
	UploadTotalSize		: 'Total Size:',
	UploadSend			: 'Upload',
	UploadAddFiles		: 'Add Files',
	UploadClearFiles	: 'Clear Files',
	UploadCancel		: 'Cancel Upload',
	UploadRemove		: 'Remove',
	UploadRemoveTip		: 'Remove !f',
	UploadUploaded		: 'Uploaded !n%',
	UploadProcessing	: 'Processing...',

	// Settings Panel
	SetTitle		: 'Settings',
	SetView			: 'View:',
	SetViewThumb	: 'Thumbnails',
	SetViewList		: 'List',
	SetDisplay		: 'Display:',
	SetDisplayName	: 'File Name',
	SetDisplayDate	: 'Date',
	SetDisplaySize	: 'File Size',
	SetSort			: 'Sorting:',
	SetSortName		: 'by File Name',
	SetSortDate		: 'by Date',
	SetSortSize		: 'by Size',
	SetSortExtension		: 'by Extension',

	// Status Bar
	FilesCountEmpty : '<Empty Folder>',
	FilesCountOne	: '1 file',
	FilesCountMany	: '%1 files',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'It was not possible to complete the request. (Error %1)',
	Errors :
	{
	 10 : 'Invalid command.',
	 11 : 'The resource type was not specified in the request.',
	 12 : 'The requested resource type is not valid.',
	102 : 'Invalid file or folder name.',
	103 : 'It was not possible to complete the request due to authorization restrictions.',
	104 : 'It was not possible to complete the request due to file system permission restrictions.',
	105 : 'Invalid file extension.',
	109 : 'Invalid request.',
	110 : 'Unknown error.',
	111 : 'It was not possible to complete the request due to resulting file size.',
	115 : 'A file or folder with the same name already exists.',
	116 : 'Folder not found. Please refresh and try again.',
	117 : 'File not found. Please refresh the files list and try again.',
	118 : 'Source and target paths are equal.',
	201 : 'A file with the same name is already available. The uploaded file was renamed to "%1".',
	202 : 'Invalid file.',
	203 : 'Invalid file. The file size is too big.',
	204 : 'The uploaded file is corrupt.',
	205 : 'No temporary folder is available for upload in the server.',
	206 : 'Upload cancelled due to security reasons. The file contains HTML-like data.',
	207 : 'The uploaded file was renamed to "%1".',
	300 : 'Moving file(s) failed.',
	301 : 'Copying file(s) failed.',
	500 : 'The file browser is disabled for security reasons. Please contact your system administrator and check the CKFinder configuration file.',
	501 : 'The thumbnails support is disabled.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'The file name cannot be empty.',
		FileExists		: 'File %s already exists.',
		FolderEmpty		: 'The folder name cannot be empty.',
		FolderExists	: 'Folder %s already exists.',
		FolderNameExists	: 'Folder already exists.',

		FileInvChar		: 'The file name cannot contain any of the following characters: \n\\ / : * ? " < > |',
		FolderInvChar	: 'The folder name cannot contain any of the following characters: \n\\ / : * ? " < > |',

		PopupBlockView	: 'It was not possible to open the file in a new window. Please configure your browser and disable all popup blockers for this site.',
		XmlError		: 'It was not possible to properly load the XML response from the web server.',
		XmlEmpty		: 'It was not possible to load the XML response from the web server. The server returned an empty response.',
		XmlRawResponse	: 'Raw response from the server: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Resize %s',
		sizeTooBig		: 'Cannot set image height or width to a value bigger than the original size (%size).',
		resizeSuccess	: 'Image resized successfully.',
		thumbnailNew	: 'Create a new thumbnail',
		thumbnailSmall	: 'Small (%s)',
		thumbnailMedium	: 'Medium (%s)',
		thumbnailLarge	: 'Large (%s)',
		newSize			: 'Set a new size',
		width			: 'Width',
		height			: 'Height',
		invalidHeight	: 'Invalid height.',
		invalidWidth	: 'Invalid width.',
		invalidName		: 'Invalid file name.',
		newImage		: 'Create a new image',
		noExtensionChange : 'File extension cannot be changed.',
		imageSmall		: 'Source image is too small.',
		contextMenuName	: 'Resize',
		lockRatio		: 'Lock ratio',
		resetSize		: 'Reset size'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Save',
		fileOpenError	: 'Unable to open file.',
		fileSaveSuccess	: 'File saved successfully.',
		contextMenuName	: 'Edit',
		loadingFile		: 'Loading file, please wait...'
	},

	Maximize :
	{
		maximize : 'Maximize',
		minimize : 'Minimize'
	},

	Gallery :
	{
		current : 'Image {current} of {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Extract here',
		extractToLabel		: 'Extract to...',
		downloadZipLabel	: 'Download as zip',
		compressZipLabel	: 'Compress to zip',
		removeAndExtract	: 'Remove existing and extract',
		extractAndOverwrite	: 'Extract overwriting existing files',
		extractSuccess		: 'File extracted successfully.'
	}
};
