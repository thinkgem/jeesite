/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Horizontal Rule plugin.
 */

(function()
{
	var horizontalruleCmd =
	{
		canUndo : false,    // The undo snapshot will be handled by 'insertElement'.
		exec : function( editor )
		{
			var hr = editor.document.createElement( 'hr' );
			editor.insertElement( hr );
		}
	};

	var pluginName = 'horizontalrule';

	// Register a plugin named "horizontalrule".
	CKEDITOR.plugins.add( pluginName,
	{
		init : function( editor )
		{
			editor.addCommand( pluginName, horizontalruleCmd );
			editor.ui.addButton( 'HorizontalRule',
				{
					label : editor.lang.horizontalrule,
					command : pluginName
				});
		}
	});
})();
