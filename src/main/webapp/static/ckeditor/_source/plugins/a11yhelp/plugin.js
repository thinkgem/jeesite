/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Plugin definition for the a11yhelp, which provides a dialog
 * with accessibility related help.
 */

(function()
{
	var pluginName = 'a11yhelp',
		commandName = 'a11yHelp';

	CKEDITOR.plugins.add( pluginName,
	{
		requires: [ 'dialog' ],

		// List of available localizations.
		availableLangs : { cs:1, cy:1, da:1, de:1, el:1, en:1, eo:1, fa:1, fi:1, fr:1, gu:1, he:1, it:1, mk:1, nb:1, nl:1, no:1, 'pt-br':1, ro:1, tr:1, ug:1, vi:1, 'zh-cn':1 },

		init : function( editor )
		{
			var plugin = this;
			editor.addCommand( commandName,
				{
					exec : function()
					{
						var langCode = editor.langCode;
						langCode = plugin.availableLangs[ langCode ] ? langCode : 'en';

						CKEDITOR.scriptLoader.load(
								CKEDITOR.getUrl( plugin.path + 'lang/' + langCode + '.js' ),
								function()
								{
									CKEDITOR.tools.extend( editor.lang, plugin.langEntries[ langCode ] );
									editor.openDialog( commandName );
								});
					},
					modes : { wysiwyg:1, source:1 },
					readOnly : 1,
					canUndo : false
				});

			CKEDITOR.dialog.add( commandName, this.path + 'dialogs/a11yhelp.js' );
		}
	});
})();
