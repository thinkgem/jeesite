/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Paste as plain text plugin
 */

(function()
{
	// The pastetext command definition.
	var pasteTextCmd =
	{
		exec : function( editor )
		{
			var clipboardText = CKEDITOR.tools.tryThese(
				function()
				{
					var clipboardText = window.clipboardData.getData( 'Text' );
					if ( !clipboardText )
						throw 0;
					return clipboardText;
				}
				// Any other approach that's working...
				);

			if ( !clipboardText )   // Clipboard access privilege is not granted.
			{
				editor.openDialog( 'pastetext' );
				return false;
			}
			else
				editor.fire( 'paste', { 'text' : clipboardText } );

			return true;
		}
	};

	// Register the plugin.
	CKEDITOR.plugins.add( 'pastetext',
	{
		init : function( editor )
		{
			var commandName = 'pastetext',
				command = editor.addCommand( commandName, pasteTextCmd );

			editor.ui.addButton( 'PasteText',
				{
					label : editor.lang.pasteText.button,
					command : commandName
				});

			CKEDITOR.dialog.add( commandName, CKEDITOR.getUrl( this.path + 'dialogs/pastetext.js' ) );

			if ( editor.config.forcePasteAsPlainText )
			{
				// Intercept the default pasting process.
				editor.on( 'beforeCommandExec', function ( evt )
				{
					var mode = evt.data.commandData;
					// Do NOT overwrite if HTML format is explicitly requested.
					if ( evt.data.name == 'paste' && mode != 'html' )
					{
						editor.execCommand( 'pastetext' );
						evt.cancel();
					}
				}, null, null, 0 );

				editor.on( 'beforePaste', function( evt )
				{
					evt.data.mode = 'text';
				});
			}

			editor.on( 'pasteState', function( evt )
				{
					editor.getCommand( 'pastetext' ).setState( evt.data );
				});
		},

		requires : [ 'clipboard' ]
	});

})();


/**
 * Whether to force all pasting operations to insert on plain text into the
 * editor, loosing any formatting information possibly available in the source
 * text.
 * <strong>Note:</strong> paste from word is not affected by this configuration.
 * @name CKEDITOR.config.forcePasteAsPlainText
 * @type Boolean
 * @default false
 * @example
 * config.forcePasteAsPlainText = true;
 */
