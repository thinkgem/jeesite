/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'about',
{
	requires : [ 'dialog' ],
	init : function( editor )
	{
		var command = editor.addCommand( 'about', new CKEDITOR.dialogCommand( 'about' ) );
		command.modes = { wysiwyg:1, source:1 };
		command.canUndo = false;
		command.readOnly = 1;

		editor.ui.addButton( 'About',
			{
				label : editor.lang.about.title,
				command : 'about'
			});

		CKEDITOR.dialog.add( 'about', this.path + 'dialogs/about.js' );
	}
});
