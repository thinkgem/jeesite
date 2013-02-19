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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Japanese
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['ja'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, は利用できません。</span>',
		confirmCancel	: '変更された項目があります。ウィンドウを閉じてもいいですか？',
		ok				: '適用',
		cancel			: 'キャンセル',
		confirmationTitle	: '確認',
		messageTitle	: 'インフォメーション',
		inputTitle		: '質問',
		undo			: '元に戻す',
		redo			: 'やり直す',
		skip			: 'スキップ',
		skipAll			: 'すべてスキップ',
		makeDecision	: 'どうしますか？',
		rememberDecision: '注意：'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'ja',

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
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: 'Folders',
	FolderLoading	: '読み込み中...',
	FolderNew		: '新しいフォルダ名を入力してください: ',
	FolderRename	: '新しいフォルダ名を入力してください: ',
	FolderDelete	: '本当にフォルダ「"%1"」を削除してもよろしいですか？',
	FolderRenaming	: ' (リネーム中...)',
	FolderDeleting	: ' (削除中...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: '新しいファイル名を入力してください: ',
	FileRenameExt	: 'ファイルが使えなくなる可能性がありますが、本当に拡張子を変更してもよろしいですか？',
	FileRenaming	: 'リネーム中...',
	FileDelete		: '本当に「"%1"」を削除してもよろしいですか？',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: '読み込み中...',
	FilesEmpty		: 'ファイルがありません',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Basket',
	BasketClear			: 'バスケットを空にする',
	BasketRemove		: 'バスケットから削除',
	BasketOpenFolder	: '親フォルダを開く',
	BasketTruncateConfirm : '本当にバスケットの中身を空にしますか？',
	BasketRemoveConfirm	: '本当に「"%1"」をバスケットから削除しますか？',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'バスケットの中にファイルがありません。このエリアにドラッグ＆ドロップして追加することができます。',
	BasketCopyFilesHere	: 'バスケットからファイルをコピー',
	BasketMoveFilesHere	: 'バスケットからファイルを移動',

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
	Upload		: 'アップロード',
	UploadTip	: '新しいファイルのアップロード',
	Refresh		: '表示の更新',
	Settings	: 'カスタマイズ',
	Help		: 'ヘルプ',
	HelpTip		: 'ヘルプ',

	// Context Menus
	Select			: 'この画像を選択',
	SelectThumbnail : 'この画像のサムネイルを選択',
	View			: '画像だけを表示',
	Download		: 'ダウンロード',

	NewSubFolder	: '新しいフォルダに入れる',
	Rename			: 'ファイル名の変更',
	Delete			: '削除',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'コピーするファイルをここにドロップしてください',
	MoveDragDrop	: '移動するファイルをここにドロップしてください',

	// Dialogs
	RenameDlgTitle		: 'リネーム',
	NewNameDlgTitle		: '新しい名前',
	FileExistsDlgTitle	: 'ファイルはすでに存在します。',
	SysErrorDlgTitle : 'システムエラー',

	FileOverwrite	: '上書き',
	FileAutorename	: 'A自動でリネーム',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'キャンセル',
	CloseBtn	: '閉じる',

	// Upload Panel
	UploadTitle			: 'ファイルのアップロード',
	UploadSelectLbl		: 'アップロードするファイルを選択してください',
	UploadProgressLbl	: '(ファイルのアップロード中...)',
	UploadBtn			: 'アップロード',
	UploadBtnCancel		: 'キャンセル',

	UploadNoFileMsg		: 'ファイルを選んでください。',
	UploadNoFolder		: 'アップロードの前にフォルダを選択してください。',
	UploadNoPerms		: 'ファイルのアップロード権限がありません。',
	UploadUnknError		: 'ファイルの送信に失敗しました。',
	UploadExtIncorrect	: '選択されたファイルの拡張子は許可されていません。',

	// Flash Uploads
	UploadLabel			: 'Files to Upload', // MISSING
	UploadTotalFiles	: 'Total Files:', // MISSING
	UploadTotalSize		: 'Total Size:', // MISSING
	UploadSend			: 'アップロード',
	UploadAddFiles		: 'Add Files', // MISSING
	UploadClearFiles	: 'Clear Files', // MISSING
	UploadCancel		: 'Cancel Upload', // MISSING
	UploadRemove		: 'Remove', // MISSING
	UploadRemoveTip		: 'Remove !f', // MISSING
	UploadUploaded		: 'Uploaded !n%', // MISSING
	UploadProcessing	: 'Processing...', // MISSING

	// Settings Panel
	SetTitle		: '表示のカスタマイズ',
	SetView			: '表示方法:',
	SetViewThumb	: 'サムネイル',
	SetViewList		: '表示形式',
	SetDisplay		: '表示する項目:',
	SetDisplayName	: 'ファイル名',
	SetDisplayDate	: '日時',
	SetDisplaySize	: 'ファイルサイズ',
	SetSort			: '表示の順番:',
	SetSortName		: 'ファイル名',
	SetSortDate		: '日付',
	SetSortSize		: 'サイズ',
	SetSortExtension		: 'by Extension', // MISSING

	// Status Bar
	FilesCountEmpty : '<フォルダ内にファイルがありません>',
	FilesCountOne	: '１つのファイル',
	FilesCountMany	: '%1個のファイル',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB', // MISSING
	Gb				: '%1 GB', // MISSING
	SizePerSecond	: '%1/s', // MISSING

	// Connector Error Messages.
	ErrorUnknown	: 'リクエストの処理に失敗しました。 (Error %1)',
	Errors :
	{
	 10 : '不正なコマンドです。',
	 11 : 'リソースタイプが特定できませんでした。',
	 12 : '要求されたリソースのタイプが正しくありません。',
	102 : 'ファイル名/フォルダ名が正しくありません。',
	103 : 'リクエストを完了できませんでした。認証エラーです。',
	104 : 'リクエストを完了できませんでした。ファイルのパーミッションが許可されていません。',
	105 : '拡張子が正しくありません。',
	109 : '不正なリクエストです。',
	110 : '不明なエラーが発生しました。',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : '同じ名前のファイル/フォルダがすでに存在しています。',
	116 : 'フォルダが見つかりませんでした。ページを更新して再度お試し下さい。',
	117 : 'ファイルが見つかりませんでした。ページを更新して再度お試し下さい。',
	118 : '対象が移動元と同じ場所を指定されています。',
	201 : '同じ名前のファイルがすでに存在しています。"%1" にリネームして保存されました。',
	202 : '不正なファイルです。',
	203 : 'ファイルのサイズが大きすぎます。',
	204 : 'アップロードされたファイルは壊れています。',
	205 : 'サーバ内の一時作業フォルダが利用できません。',
	206 : 'セキュリティ上の理由からアップロードが取り消されました。このファイルにはHTMLに似たデータが含まれています。',
	207 : 'ファイルは "%1" にリネームして保存されました。',
	300 : 'ファイルの移動に失敗しました。',
	301 : 'ファイルのコピーに失敗しました。',
	500 : 'ファイルブラウザはセキュリティ上の制限から無効になっています。システム担当者に連絡をして、CKFinderの設定をご確認下さい。',
	501 : 'サムネイル機能は無効になっています。'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'ファイル名を入力してください',
		FileExists		: ' %s はすでに存在しています。別の名前を入力してください。',
		FolderEmpty		: 'フォルダ名を入力してください',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'ファイルに以下の文字は使えません: \n\\ / : * ? " < > |',
		FolderInvChar	: 'フォルダに以下の文字は使えません: \n\\ / : * ? " < > |',

		PopupBlockView	: 'ファイルを新しいウィンドウで開くことに失敗しました。 お使いのブラウザの設定でポップアップをブロックする設定を解除してください。',
		XmlError		: 'It was not possible to properly load the XML response from the web server.', // MISSING
		XmlEmpty		: 'It was not possible to load the XML response from the web server. The server returned an empty response.', // MISSING
		XmlRawResponse	: 'Raw response from the server: %s' // MISSING
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'リサイズ： %s',
		sizeTooBig		: 'オリジナルの画像よりも大きいサイズは指定できません。 (%size).',
		resizeSuccess	: '画像のリサイズに成功しました',
		thumbnailNew	: 'サムネイルをつくる',
		thumbnailSmall	: '小 (%s)',
		thumbnailMedium	: '中 (%s)',
		thumbnailLarge	: '大 (%s)',
		newSize			: 'Set new size',
		width			: '幅',
		height			: '高さ',
		invalidHeight	: '高さの値が不正です。',
		invalidWidth	: '幅の値が不正です。',
		invalidName		: 'ファイル名が不正です。',
		newImage		: '新しい画像を作成',
		noExtensionChange : '拡張子は変更できません。',
		imageSmall		: '元画像が小さすぎます。',
		contextMenuName	: 'リサイズ',
		lockRatio		: 'ロック比率',
		resetSize		: 'サイズリセット'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: '保存',
		fileOpenError	: 'ファイルを開けませんでした。',
		fileSaveSuccess	: 'ファイルの保存が完了しました。',
		contextMenuName	: '編集',
		loadingFile		: 'ファイルの読み込み中...'
	},

	Maximize :
	{
		maximize : '最大化',
		minimize : '最小化'
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
