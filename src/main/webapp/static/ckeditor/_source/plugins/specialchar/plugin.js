/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Special Character plugin
 */

CKEDITOR.plugins.add( 'specialchar',
{
	requires : [ 'dialog' ],

	// List of available localizations.
	availableLangs : { cs:1, cy:1, de:1, el:1, en:1, eo:1, et:1, fa:1, fi:1, fr:1, he:1, hr:1, it:1, nb:1, nl:1, no:1, 'pt-br':1, tr:1, ug:1, 'zh-cn':1 },

	init : function( editor )
	{
		var pluginName = 'specialchar',
			plugin = this;

		// Register the dialog.
		CKEDITOR.dialog.add( pluginName, this.path + 'dialogs/specialchar.js' );

		editor.addCommand( pluginName,
			{
				exec : function()
				{
					var langCode = editor.langCode;
					langCode = plugin.availableLangs[ langCode ] ? langCode : 'en';

					CKEDITOR.scriptLoader.load(
							CKEDITOR.getUrl( plugin.path + 'lang/' + langCode + '.js' ),
							function()
							{
								CKEDITOR.tools.extend( editor.lang.specialChar, plugin.langEntries[ langCode ] );
								editor.openDialog( pluginName );
							});
				},
				modes : { wysiwyg:1 },
				canUndo : false
			});

		// Register the toolbar button.
		editor.ui.addButton( 'SpecialChar',
			{
				label : editor.lang.specialChar.toolbar,
				command : pluginName
			});
	}
} );

/**
  * The list of special characters visible in the Special Character dialog window.
  * @type Array
  * @example
  * config.specialChars = [ '&quot;', '&rsquo;', [ '&custom;', 'Custom label' ] ];
  * config.specialChars = config.specialChars.concat( [ '&quot;', [ '&rsquo;', 'Custom label' ] ] );
  */
CKEDITOR.config.specialChars =
	[
		'!','&quot;','#','$','%','&amp;',"'",'(',')','*','+','-','.','/',
		'0','1','2','3','4','5','6','7','8','9',':',';',
		'&lt;','=','&gt;','?','@',
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O',
		'P','Q','R','S','T','U','V','W','X','Y','Z',
		'[',']','^','_','`',
		'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p',
		'q','r','s','t','u','v','w','x','y','z',
		'{','|','}','~',
		"&euro;", "&lsquo;", "&rsquo;", "&ldquo;", "&rdquo;", "&ndash;", "&mdash;", "&iexcl;", "&cent;", "&pound;", "&curren;", "&yen;", "&brvbar;", "&sect;", "&uml;", "&copy;", "&ordf;", "&laquo;", "&not;", "&reg;", "&macr;", "&deg;", "&sup2;", "&sup3;", "&acute;", "&micro;", "&para;", "&middot;", "&cedil;", "&sup1;", "&ordm;", "&raquo;", "&frac14;", "&frac12;", "&frac34;", "&iquest;", "&Agrave;", "&Aacute;", "&Acirc;", "&Atilde;", "&Auml;", "&Aring;", "&AElig;", "&Ccedil;", "&Egrave;", "&Eacute;", "&Ecirc;", "&Euml;", "&Igrave;", "&Iacute;", "&Icirc;", "&Iuml;", "&ETH;", "&Ntilde;", "&Ograve;", "&Oacute;", "&Ocirc;", "&Otilde;", "&Ouml;", "&times;", "&Oslash;", "&Ugrave;", "&Uacute;", "&Ucirc;", "&Uuml;", "&Yacute;", "&THORN;", "&szlig;", "&agrave;", "&aacute;", "&acirc;", "&atilde;", "&auml;", "&aring;", "&aelig;", "&ccedil;", "&egrave;", "&eacute;", "&ecirc;", "&euml;", "&igrave;", "&iacute;", "&icirc;", "&iuml;", "&eth;", "&ntilde;", "&ograve;", "&oacute;", "&ocirc;", "&otilde;", "&ouml;", "&divide;", "&oslash;", "&ugrave;", "&uacute;", "&ucirc;", "&uuml;", "&yacute;", "&thorn;", "&yuml;", "&OElig;", "&oelig;", "&#372;", "&#374", "&#373", "&#375;", "&sbquo;", "&#8219;", "&bdquo;", "&hellip;", "&trade;", "&#9658;", "&bull;", "&rarr;", "&rArr;", "&hArr;", "&diams;", "&asymp;"
	];
