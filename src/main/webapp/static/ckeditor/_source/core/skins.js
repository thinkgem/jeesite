/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.skins} object, which is used to
 *		manage skins loading.
 */

/**
 * Manages skins loading.
 * @namespace
 * @example
 */
CKEDITOR.skins = (function()
{
	// Holds the list of loaded skins.
	var loaded = {},
		paths = {};

	var loadPart = function( editor, skinName, part, callback )
	{
		// Get the skin definition.
		var skinDefinition = loaded[ skinName ];

		if ( !editor.skin )
		{
			editor.skin = skinDefinition;

			// Trigger init function if any.
			if ( skinDefinition.init )
				skinDefinition.init( editor );
		}

		var appendSkinPath = function( fileNames )
		{
			for ( var n = 0 ; n < fileNames.length ; n++ )
			{
				fileNames[ n ] = CKEDITOR.getUrl( paths[ skinName ] + fileNames[ n ] );
			}
		};

		function fixCSSTextRelativePath( cssStyleText, baseUrl )
		{
			return cssStyleText.replace( /url\s*\(([\s'"]*)(.*?)([\s"']*)\)/g,
					function( match, opener, path, closer )
					{
						if ( /^\/|^\w?:/.test( path ) )
							return match;
						else
							return 'url(' + baseUrl + opener +  path + closer + ')';
					} );
		}

		// Get the part definition.
		part = skinDefinition[ part ];
		var partIsLoaded = !part || !!part._isLoaded;

		// Call the callback immediately if already loaded.
		if ( partIsLoaded )
			callback && callback();
		else
		{
			// Put the callback in a queue.
			var pending = part._pending || ( part._pending = [] );
			pending.push( callback );

			// We may have more than one skin part load request. Just the first
			// one must do the loading job.
			if ( pending.length > 1 )
				return;

			// Check whether the "css" and "js" properties have been defined
			// for that part.
			var cssIsLoaded = !part.css || !part.css.length,
				jsIsLoaded = !part.js || !part.js.length;

			// This is the function that will trigger the callback calls on
			// load.
			var checkIsLoaded = function()
			{
				if ( cssIsLoaded && jsIsLoaded )
				{
					// Mark the part as loaded.
					part._isLoaded = 1;

					// Call all pending callbacks.
					for ( var i = 0 ; i < pending.length ; i++ )
					{
						if ( pending[ i ] )
							pending[ i ]();
					}
				}
			};

			// Load the "css" pieces.
			if ( !cssIsLoaded )
			{
				var cssPart = part.css;

				if ( CKEDITOR.tools.isArray( cssPart ) )
				{
					appendSkinPath( cssPart );
					for ( var c = 0 ; c < cssPart.length ; c++ )
						CKEDITOR.document.appendStyleSheet( cssPart[ c ] );
				}
				else
				{
					cssPart = fixCSSTextRelativePath(
								cssPart, CKEDITOR.getUrl( paths[ skinName ] ) );
					// Processing Inline CSS part.
					CKEDITOR.document.appendStyleText( cssPart );
				}

				part.css = cssPart;

				cssIsLoaded = 1;
			}

			// Load the "js" pieces.
			if ( !jsIsLoaded )
			{
				appendSkinPath( part.js );
				CKEDITOR.scriptLoader.load( part.js, function()
					{
						jsIsLoaded = 1;
						checkIsLoaded();
					});
			}

			// We may have nothing to load, so check it immediately.
			checkIsLoaded();
		}
	};

	return /** @lends CKEDITOR.skins */ {

		/**
		 * Registers a skin definition.
		 * @param {String} skinName The skin name.
		 * @param {Object} skinDefinition The skin definition.
		 * @example
		 */
		add : function( skinName, skinDefinition )
		{
			loaded[ skinName ] = skinDefinition;

			skinDefinition.skinPath = paths[ skinName ]
				|| ( paths[ skinName ] =
						CKEDITOR.getUrl(
							'_source/' +	// @Packager.RemoveLine
							'skins/' + skinName + '/' ) );
		},

		/**
		 * Loads a skin part. Skins are defined in parts, which are basically
		 * separated CSS files. This function is mainly used by the core code and
		 * should not have much use out of it.
		 * @param {String} skinName The name of the skin to be loaded.
		 * @param {String} skinPart The skin part to be loaded. Common skin parts
		 *		are "editor" and "dialog".
		 * @param {Function} [callback] A function to be called once the skin
		 *		part files are loaded.
		 * @example
		 */
		load : function( editor, skinPart, callback )
		{
			var skinName = editor.skinName,
				skinPath = editor.skinPath;

			if ( loaded[ skinName ] )
				loadPart( editor, skinName, skinPart, callback );
			else
			{
				paths[ skinName ] = skinPath;
				CKEDITOR.scriptLoader.load( CKEDITOR.getUrl( skinPath + 'skin.js' ), function()
						{
							 loadPart( editor, skinName, skinPart, callback );
						});
			}
		}
	};
})();
