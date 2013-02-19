/*
Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	config.language = 'zh-cn';
	config.uiColor = '#f7f5f4';
	config.width = '99.7%';
	config.height = '400px';
	config.removePlugins = 'elementspath,scayt';
	config.disableNativeSpellChecker = false;
	config.resize_dir = 'vertical';
	config.keystrokes =[[ CKEDITOR.CTRL + 13 /*Enter*/, 'maximize' ]];	
	config.extraPlugins = '';
	config.enterMode = CKEDITOR.ENTER_BR; 
	config.shiftEnterMode = CKEDITOR.ENTER_P;
	config.font_names='宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;'+ config.font_names;
	config.image_previewText='&nbsp;';
	config.toolbar_default = [
		['Source','-','Templates','Preview'],
	    ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
	    ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],['ShowBlocks'],['Image','Capture','Flash'],['Maximize'],
	    '/',
	    ['Bold','Italic','Underline','Strike','-'],
	    ['Subscript','Superscript','-'],
	    ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
	    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
	    ['Link','Unlink','Anchor'],
	    ['Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
	    '/',
	    ['Styles','Format','Font','FontSize'],
	    ['TextColor','BGColor']
	];
	config.toolbar = 'default';
	if(config.ckfinderPath){
		config.filebrowserBrowseUrl = config.ckfinderPath+'/ckfinder.html?type=files&start=files:'+config.ckfinderUploadPath;
		config.filebrowserImageBrowseUrl = config.ckfinderPath+'/ckfinder.html?type=images&start=images:'+config.ckfinderUploadPath;
		config.filebrowserFlashBrowseUrl = config.ckfinderPath+'/ckfinder.html?type=flash&start=flash:'+config.ckfinderUploadPath;
		config.filebrowserUploadUrl = config.ckfinderPath+'/core/connector/java/connector.java?command=QuickUpload&type=files&currentFolder='+config.ckfinderUploadPath;
		config.filebrowserImageUploadUrl = config.ckfinderPath+'/core/connector/java/connector.java?command=QuickUpload&type=images&currentFolder='+config.ckfinderUploadPath;
		config.filebrowserFlashUploadUrl = config.ckfinderPath+'/core/connector/java/connector.java?command=QuickUpload&type=flash&currentFolder='+config.ckfinderUploadPath;
		config.filebrowserWindowWidth = '1000';
		config.filebrowserWindowHeight = '700';
	}
};

