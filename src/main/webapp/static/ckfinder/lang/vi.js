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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Vietnamese
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['vi'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, không khả dụng</span>',
		confirmCancel	: 'Vài tùy chọn đã thay đổi. Bạn có muốn đóng hộp thoại?',
		ok				: 'OK',
		cancel			: 'Hủy',
		confirmationTitle	: 'Xác nhận',
		messageTitle	: 'Thông tin',
		inputTitle		: 'Câu hỏi',
		undo			: 'Hoàn tác',
		redo			: 'Làm lại',
		skip			: 'Bỏ qua',
		skipAll			: 'Bỏ qua tất cả',
		makeDecision	: 'Chọn hành động nào?',
		rememberDecision: 'Ghi nhớ quyết định này'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'vi',

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
	DateAmPm : ['SA', 'CH'],

	// Folders
	FoldersTitle	: 'Thư mục',
	FolderLoading	: 'Đang tải...',
	FolderNew		: 'Xin chọn tên cho thư mục mới: ',
	FolderRename	: 'Xin chọn tên mới cho thư mục: ',
	FolderDelete	: 'Bạn có chắc muốn xóa thư mục "%1"?',
	FolderRenaming	: ' (Đang đổi tên...)',
	FolderDeleting	: ' (Đang xóa...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Xin nhập tên tập tin mới: ',
	FileRenameExt	: 'Bạn có chắc muốn đổi phần mở rộng? Tập tin có thể sẽ không dùng được.',
	FileRenaming	: 'Đang đổi tên...',
	FileDelete		: 'Bạn có chắc muốn xóa tập tin "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: 'Đang tải...',
	FilesEmpty		: 'Thư mục trống.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'Rổ',
	BasketClear			: 'Dọn rổ',
	BasketRemove		: 'Xóa khỏi rổ',
	BasketOpenFolder	: 'Mở thư mục cha',
	BasketTruncateConfirm : 'Bạn có chắc muốn bỏ tất cả tập tin trong rổ?',
	BasketRemoveConfirm	: 'Bạn có chắc muốn bỏ tập tin "%1" khỏi rổ?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Không có tập tin trong rổ, hãy kéo và thả tập tin vào rổ.',
	BasketCopyFilesHere	: 'Chép tập tin từ rổ',
	BasketMoveFilesHere	: 'Chuyển tập tin từ rổ',

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
	Upload		: 'Tải lên',
	UploadTip	: 'Tải tập tin mới',
	Refresh		: 'Làm tươi',
	Settings	: 'Thiết lập',
	Help		: 'Hướng dẫn',
	HelpTip		: 'Hướng dẫn',

	// Context Menus
	Select			: 'Chọn',
	SelectThumbnail : 'Chọn ảnh mẫu',
	View			: 'Xem',
	Download		: 'Tải về',

	NewSubFolder	: 'Tạo thư mục con',
	Rename			: 'Đổi tên',
	Delete			: 'Xóa',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Sao chép ở đây',
	MoveDragDrop	: 'Di chuyển ở đây',

	// Dialogs
	RenameDlgTitle		: 'Đổi tên',
	NewNameDlgTitle		: 'Tên mới',
	FileExistsDlgTitle	: 'Tập tin đã tồn tại',
	SysErrorDlgTitle : 'Lỗi hệ thống',

	FileOverwrite	: 'Ghi đè',
	FileAutorename	: 'Tự đổi tên',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'Hủy bỏ',
	CloseBtn	: 'Đóng',

	// Upload Panel
	UploadTitle			: 'Tải tập tin mới',
	UploadSelectLbl		: 'Chọn tập tin tải lên',
	UploadProgressLbl	: '(Đang tải lên, vui lòng chờ...)',
	UploadBtn			: 'Tải tập tin đã chọn',
	UploadBtnCancel		: 'Hủy bỏ',

	UploadNoFileMsg		: 'Xin chọn một tập tin trong máy tính.',
	UploadNoFolder		: 'Xin chọn thư mục trước khi tải lên.',
	UploadNoPerms		: 'Không được phép tải lên.',
	UploadUnknError		: 'Lỗi khi tải tập tin.',
	UploadExtIncorrect	: 'Kiểu tập tin không được chấp nhận trong thư mục này.',

	// Flash Uploads
	UploadLabel			: 'Tập tin sẽ tải:',
	UploadTotalFiles	: 'Tổng số tập tin:',
	UploadTotalSize		: 'Dung lượng tổng cộng:',
	UploadSend			: 'Tải lên',
	UploadAddFiles		: 'Thêm tập tin',
	UploadClearFiles	: 'Xóa tập tin',
	UploadCancel		: 'Hủy tải',
	UploadRemove		: 'Xóa',
	UploadRemoveTip		: 'Xóa !f',
	UploadUploaded		: 'Đã tải !n%',
	UploadProcessing	: 'Đang xử lí...',

	// Settings Panel
	SetTitle		: 'Thiết lập',
	SetView			: 'Xem:',
	SetViewThumb	: 'Ảnh mẫu',
	SetViewList		: 'Danh sách',
	SetDisplay		: 'Hiển thị:',
	SetDisplayName	: 'Tên tập tin',
	SetDisplayDate	: 'Ngày',
	SetDisplaySize	: 'Dung lượng',
	SetSort			: 'Sắp xếp:',
	SetSortName		: 'theo tên',
	SetSortDate		: 'theo ngày',
	SetSortSize		: 'theo dung lượng',
	SetSortExtension		: 'theo phần mở rộng',

	// Status Bar
	FilesCountEmpty : '<Thư mục rỗng>',
	FilesCountOne	: '1 tập tin',
	FilesCountMany	: '%1 tập tin',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Không thể hoàn tất yêu cầu. (Lỗi %1)',
	Errors :
	{
	 10 : 'Lệnh không hợp lệ.',
	 11 : 'Kiểu tài nguyên không được chỉ định trong yêu cầu.',
	 12 : 'Kiểu tài nguyên yêu cầu không hợp lệ.',
	102 : 'Tên tập tin hay thư mục không hợp lệ.',
	103 : 'Không thể hoàn tất yêu cầu vì giới hạn quyền.',
	104 : 'Không thể hoàn tất yêu cầu vì giới hạn quyền của hệ thống tập tin.',
	105 : 'Phần mở rộng tập tin không hợp lệ.',
	109 : 'Yêu cầu không hợp lệ.',
	110 : 'Lỗi không xác định.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'Tập tin hoặc thư mục cùng tên đã tồn tại.',
	116 : 'Không thấy thư mục. Hãy làm tươi và thử lại.',
	117 : 'Không thấy tập tin. Hãy làm tươi và thử lại.',
	118 : 'Đường dẫn nguồn và đích giống nhau.',
	201 : 'Tập tin cùng tên đã tồn tại. Tập tin vừa tải lên được đổi tên thành "%1".',
	202 : 'Tập tin không hợp lệ.',
	203 : 'Tập tin không hợp lệ. Dung lượng quá lớn.',
	204 : 'Tập tin tải lên bị hỏng.',
	205 : 'Không có thư mục tạm để tải tập tin.',
	206 : 'Huỷ tải lên vì lí do bảo mật. Tập tin chứa dữ liệu giống HTML.',
	207 : 'Tập tin được đổi tên thành "%1".',
	300 : 'Di chuyển tập tin thất bại.',
	301 : 'Chép tập tin thất bại.',
	500 : 'Trình duyệt tập tin bị vô hiệu vì lí do bảo mật. Xin liên hệ quản trị hệ thống và kiểm tra tập tin cấu hình CKFinder.',
	501 : 'Chức năng hỗ trợ ảnh mẫu bị vô hiệu.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Không thể để trống tên tập tin.',
		FileExists		: 'Tập tin %s đã tồn tại.',
		FolderEmpty		: 'Không thể để trống tên thư mục.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'Tên tập tin không thể chưa các kí tự: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Tên thư mục không thể chứa các kí tự: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Không thể mở tập tin trong cửa sổ mới. Hãy kiểm tra trình duyệt và tắt chức năng chặn popup trên trang web này.',
		XmlError		: 'Không thể nạp hồi đáp XML từ máy chủ web.',
		XmlEmpty		: 'Không thể nạp hồi đáp XML từ máy chủ web. Dữ liệu rỗng.',
		XmlRawResponse	: 'Hồi đáp thô từ máy chủ: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Đổi kích thước %s',
		sizeTooBig		: 'Không thể đặt chiều cao hoặc rộng to hơn kích thước gốc (%size).',
		resizeSuccess	: 'Đổi kích thước ảnh thành công.',
		thumbnailNew	: 'Tạo ảnh mẫu mới',
		thumbnailSmall	: 'Nhỏ (%s)',
		thumbnailMedium	: 'Vừa (%s)',
		thumbnailLarge	: 'Lớn (%s)',
		newSize			: 'Chọn kích thước mới',
		width			: 'Rộng',
		height			: 'Cao',
		invalidHeight	: 'Chiều cao không hợp lệ.',
		invalidWidth	: 'Chiều rộng không hợp lệ.',
		invalidName		: 'Tên tập tin không hợp lệ.',
		newImage		: 'Tạo ảnh mới',
		noExtensionChange : 'Không thể thay đổi phần mở rộng.',
		imageSmall		: 'Ảnh nguồn quá nhỏ.',
		contextMenuName	: 'Đổi kích thước',
		lockRatio		: 'Khoá tỉ lệ',
		resetSize		: 'Đặt lại kích thước'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Lưu',
		fileOpenError	: 'Không thể mở tập tin.',
		fileSaveSuccess	: 'Lưu tập tin thành công.',
		contextMenuName	: 'Sửa',
		loadingFile		: 'Đang tải tập tin, xin chờ...'
	},

	Maximize :
	{
		maximize : 'Cực đại hóa',
		minimize : 'Cực tiểu hóa'
	},

	Gallery :
	{
		current : 'Hình thứ {current} trên {total}'
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
