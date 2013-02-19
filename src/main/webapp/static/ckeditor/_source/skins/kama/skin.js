/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.skins.add( 'kama', (function()
{
	var uiColorStylesheetId = 'cke_ui_color';

	return {
		editor		: { css : [ 'editor.css' ] },
		dialog		: { css : [ 'dialog.css' ] },
		richcombo	: { canGroup: false },
		templates	: { css : [ 'templates.css' ] },
		margins		: [ 0, 0, 0, 0 ],
		init : function( editor )
		{
			if ( editor.config.width && !isNaN( editor.config.width ) )
				editor.config.width -= 12;

			var uiColorMenus = [];
			var uiColorRegex = /\$color/g;
			var uiColorMenuCss = "/* UI Color Support */\
.cke_skin_kama .cke_menuitem .cke_icon_wrapper\
{\
	background-color: $color !important;\
	border-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuitem a:hover .cke_icon_wrapper,\
.cke_skin_kama .cke_menuitem a:focus .cke_icon_wrapper,\
.cke_skin_kama .cke_menuitem a:active .cke_icon_wrapper\
{\
	background-color: $color !important;\
	border-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuitem a:hover .cke_label,\
.cke_skin_kama .cke_menuitem a:focus .cke_label,\
.cke_skin_kama .cke_menuitem a:active .cke_label\
{\
	background-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuitem a.cke_disabled:hover .cke_label,\
.cke_skin_kama .cke_menuitem a.cke_disabled:focus .cke_label,\
.cke_skin_kama .cke_menuitem a.cke_disabled:active .cke_label\
{\
	background-color: transparent !important;\
}\
\
.cke_skin_kama .cke_menuitem a.cke_disabled:hover .cke_icon_wrapper,\
.cke_skin_kama .cke_menuitem a.cke_disabled:focus .cke_icon_wrapper,\
.cke_skin_kama .cke_menuitem a.cke_disabled:active .cke_icon_wrapper\
{\
	background-color: $color !important;\
	border-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuitem a.cke_disabled .cke_icon_wrapper\
{\
	background-color: $color !important;\
	border-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuseparator\
{\
	background-color: $color !important;\
}\
\
.cke_skin_kama .cke_menuitem a:hover,\
.cke_skin_kama .cke_menuitem a:focus,\
.cke_skin_kama .cke_menuitem a:active\
{\
	background-color: $color !important;\
}";
			// We have to split CSS declarations for webkit.
			if ( CKEDITOR.env.webkit )
			{
				uiColorMenuCss = uiColorMenuCss.split( '}' ).slice( 0, -1 );
				for ( var i = 0 ; i < uiColorMenuCss.length ; i++ )
					uiColorMenuCss[ i ] = uiColorMenuCss[ i ].split( '{' );
			}

			function getStylesheet( document )
			{
				var node = document.getById( uiColorStylesheetId );
				if ( !node )
				{
					node = document.getHead().append( 'style' );
					node.setAttribute( "id", uiColorStylesheetId );
					node.setAttribute( "type", "text/css" );
				}
				return node;
			}

			function updateStylesheets( styleNodes, styleContent, replace )
			{
				var r, i, content;
				for ( var id  = 0 ; id < styleNodes.length ; id++ )
				{
					if ( CKEDITOR.env.webkit )
					{
						for ( i = 0 ; i < styleContent.length ; i++ )
						{
							content = styleContent[ i ][ 1 ];
							for ( r  = 0 ; r < replace.length ; r++ )
								content = content.replace( replace[ r ][ 0 ], replace[ r ][ 1 ] );

							styleNodes[ id ].$.sheet.addRule( styleContent[ i ][ 0 ], content );
						}
					}
					else
					{
						content = styleContent;
						for ( r  = 0 ; r < replace.length ; r++ )
							content = content.replace( replace[ r ][ 0 ], replace[ r ][ 1 ] );

						if ( CKEDITOR.env.ie )
							styleNodes[ id ].$.styleSheet.cssText += content;
						else
							styleNodes[ id ].$.innerHTML += content;
					}
				}
			}

			var uiColorRegexp = /\$color/g;

			CKEDITOR.tools.extend( editor,
			{
				uiColor: null,

				getUiColor : function()
				{
					return this.uiColor;
				},

				setUiColor : function( color )
				{
					var cssContent,
						uiStyle = getStylesheet( CKEDITOR.document ),
						cssId = '.' + editor.id;

					var cssSelectors =
						[
							cssId + " .cke_wrapper",
							cssId + "_dialog .cke_dialog_contents",
							cssId + "_dialog a.cke_dialog_tab",
							cssId + "_dialog .cke_dialog_footer"
						].join( ',' );
					var cssProperties = "background-color: $color !important;";

					if ( CKEDITOR.env.webkit )
						cssContent = [ [ cssSelectors, cssProperties ] ];
					else
						cssContent = cssSelectors + '{' + cssProperties + '}';

					return ( this.setUiColor =
						function( color )
						{
							var replace = [ [ uiColorRegexp, color ] ];
							editor.uiColor = color;

							// Update general style.
							updateStylesheets( [ uiStyle ], cssContent, replace );

							// Update menu styles.
							updateStylesheets( uiColorMenus, uiColorMenuCss, replace );
						})( color );
				}
			});

			editor.on( 'menuShow', function( event )
			{
				var panel = event.data[ 0 ];
				var iframe = panel.element.getElementsByTag( 'iframe' ).getItem( 0 ).getFrameDocument();

				// Add stylesheet if missing.
				if ( !iframe.getById( 'cke_ui_color' ) )
				{
					var node = getStylesheet( iframe );
					uiColorMenus.push( node );

					var color = editor.getUiColor();
					// Set uiColor for new menu.
					if ( color )
						updateStylesheets( [ node ], uiColorMenuCss, [ [ uiColorRegexp, color ] ] );
				}
			});

			// Apply UI color if specified in config.
			if ( editor.config.uiColor )
				editor.setUiColor( editor.config.uiColor );
		}
	};
})() );

(function()
{
	CKEDITOR.dialog ? dialogSetup() : CKEDITOR.on( 'dialogPluginReady', dialogSetup );

	function dialogSetup()
	{
		CKEDITOR.dialog.on( 'resize', function( evt )
			{
				var data = evt.data,
					width = data.width,
					height = data.height,
					dialog = data.dialog,
					contents = dialog.parts.contents;

				if ( data.skin != 'kama' )
					return;

				contents.setStyles(
					{
						width : width + 'px',
						height : height + 'px'
					});
			});
	}
})();

/**
 * The base user interface color to be used by the editor. Not all skins are
 * compatible with this setting.
 * @name CKEDITOR.config.uiColor
 * @type String
 * @default '' (empty)
 * @example
 * // Using a color code.
 * config.uiColor = '#AADC6E';
 * @example
 * // Using an HTML color name.
 * config.uiColor = 'Gold';
 */
