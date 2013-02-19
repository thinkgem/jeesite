/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'uicolor',
{
	requires : [ 'dialog' ],
	lang : [ 'bg', 'cs', 'cy', 'da', 'de', 'el', 'en', 'eo', 'et', 'fa', 'fi', 'fr', 'he', 'hr', 'it', 'mk', 'nb', 'nl', 'no', 'pl', 'pt-br', 'tr', 'ug', 'uk', 'vi', 'zh-cn' ],

	init : function( editor )
	{
		if ( CKEDITOR.env.ie6Compat )
			return;

		editor.addCommand( 'uicolor', new CKEDITOR.dialogCommand( 'uicolor' ) );
		editor.ui.addButton( 'UIColor',
			{
				label : editor.lang.uicolor.title,
				command : 'uicolor',
				icon : this.path + 'uicolor.gif'
			});
		CKEDITOR.dialog.add( 'uicolor', this.path + 'dialogs/uicolor.js' );

		// Load YUI js files.
		CKEDITOR.scriptLoader.load( CKEDITOR.getUrl(
			'_source/' + // @Packager.RemoveLine
			'plugins/uicolor/yui/yui.js'
		));

		// Load YUI css files.
		editor.element.getDocument().appendStyleSheet( CKEDITOR.getUrl(
				'_source/' + // @Packager.RemoveLine
				'plugins/uicolor/yui/assets/yui.css'
		));
	}
} );
