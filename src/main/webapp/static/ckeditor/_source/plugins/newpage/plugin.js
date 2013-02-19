/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Horizontal Page Break
 */

// Register a plugin named "newpage".
CKEDITOR.plugins.add( 'newpage',
{
	init : function( editor )
	{
		editor.addCommand( 'newpage',
			{
				modes : { wysiwyg:1, source:1 },

				exec : function( editor )
				{
					var command = this;
					editor.setData( editor.config.newpage_html || '', function()
					{
						// Save the undo snapshot after all document changes are affected. (#4889)
						setTimeout( function ()
						{
							editor.fire( 'afterCommandExec',
							{
								name: 'newpage',
								command: command
							} );
							editor.selectionChange();

						}, 200 );
					} );
					editor.focus();
				},
				async : true
			});

		editor.ui.addButton( 'NewPage',
			{
				label : editor.lang.newPage,
				command : 'newpage'
			});
	}
});
/**
 * The HTML to load in the editor when the "new page" command is executed.
 * @name CKEDITOR.config.newpage_html
 * @type String
 * @default ''
 * @example
 * config.newpage_html = '&lt;p&gt;Type your text here.&lt;/p&gt;';
 */
