/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview The "colorbutton" plugin that makes it possible to assign
 *               text and background colors to editor contents.
 *
 */
CKEDITOR.plugins.add( 'colorbutton',
{
	requires : [ 'panelbutton', 'floatpanel', 'styles' ],

	init : function( editor )
	{
		var config = editor.config,
			lang = editor.lang.colorButton;

		var clickFn;

		if ( !CKEDITOR.env.hc )
		{
			addButton( 'TextColor', 'fore', lang.textColorTitle );
			addButton( 'BGColor', 'back', lang.bgColorTitle );
		}

		function addButton( name, type, title )
		{
			var colorBoxId = CKEDITOR.tools.getNextId() + '_colorBox';
			editor.ui.add( name, CKEDITOR.UI_PANELBUTTON,
				{
					label : title,
					title : title,
					className : 'cke_button_' + name.toLowerCase(),
					modes : { wysiwyg : 1 },

					panel :
					{
						css : editor.skin.editor.css,
						attributes : { role : 'listbox', 'aria-label' : lang.panelTitle }
					},

					onBlock : function( panel, block )
					{
						block.autoSize = true;
						block.element.addClass( 'cke_colorblock' );
						block.element.setHtml( renderColors( panel, type, colorBoxId ) );
						// The block should not have scrollbars (#5933, #6056)
						block.element.getDocument().getBody().setStyle( 'overflow', 'hidden' );

						CKEDITOR.ui.fire( 'ready', this );

						var keys = block.keys;
						var rtl = editor.lang.dir == 'rtl';
						keys[ rtl ? 37 : 39 ]	= 'next';					// ARROW-RIGHT
						keys[ 40 ]	= 'next';					// ARROW-DOWN
						keys[ 9 ]	= 'next';					// TAB
						keys[ rtl ? 39 : 37 ]	= 'prev';					// ARROW-LEFT
						keys[ 38 ]	= 'prev';					// ARROW-UP
						keys[ CKEDITOR.SHIFT + 9 ]	= 'prev';	// SHIFT + TAB
						keys[ 32 ]	= 'click';					// SPACE
					},

					// The automatic colorbox should represent the real color (#6010)
					onOpen : function()
					{
						var selection = editor.getSelection(),
							block = selection && selection.getStartElement(),
							path = new CKEDITOR.dom.elementPath( block ),
							color;

						// Find the closest block element.
						block = path.block || path.blockLimit || editor.document.getBody();

						// The background color might be transparent. In that case, look up the color in the DOM tree.
						do
						{
							color = block && block.getComputedStyle( type == 'back' ? 'background-color' : 'color' ) || 'transparent';
						}
						while ( type == 'back' && color == 'transparent' && block && ( block = block.getParent() ) );

						// The box should never be transparent.
						if ( !color || color == 'transparent' )
							color = '#ffffff';

						this._.panel._.iframe.getFrameDocument().getById( colorBoxId ).setStyle( 'background-color', color );
					}
				});
		}


		function renderColors( panel, type, colorBoxId )
		{
			var output = [],
				colors = config.colorButton_colors.split( ',' );

			var clickFn = CKEDITOR.tools.addFunction( function( color, type )
				{
					if ( color == '?' )
					{
						var applyColorStyle = arguments.callee;
						function onColorDialogClose( evt )
						{
							this.removeListener( 'ok', onColorDialogClose );
							this.removeListener( 'cancel', onColorDialogClose );

							evt.name == 'ok' && applyColorStyle( this.getContentElement( 'picker', 'selectedColor' ).getValue(), type );
						}

						editor.openDialog( 'colordialog', function()
						{
							this.on( 'ok', onColorDialogClose );
							this.on( 'cancel', onColorDialogClose );
						} );

						return;
					}

					editor.focus();

					panel.hide( false );

					editor.fire( 'saveSnapshot' );

					// Clean up any conflicting style within the range.
					new CKEDITOR.style( config['colorButton_' + type + 'Style'], { color : 'inherit' } ).remove( editor.document );

					if ( color )
					{
						var colorStyle = config['colorButton_' + type + 'Style'];

						colorStyle.childRule = type == 'back' ?
							function( element )
							{
								// It's better to apply background color as the innermost style. (#3599)
								// Except for "unstylable elements". (#6103)
								return isUnstylable( element );
							}
							:
							function( element )
							{
								// Fore color style must be applied inside links instead of around it. (#4772,#6908)
								return !( element.is( 'a' ) || element.getElementsByTag( 'a' ).count() ) || isUnstylable( element );
							};

						new CKEDITOR.style( colorStyle, { color : color } ).apply( editor.document );
					}

					editor.fire( 'saveSnapshot' );
				});

			// Render the "Automatic" button.
			output.push(
				'<a class="cke_colorauto" _cke_focus=1 hidefocus=true' +
					' title="', lang.auto, '"' +
					' onclick="CKEDITOR.tools.callFunction(', clickFn, ',null,\'', type, '\');return false;"' +
					' href="javascript:void(\'', lang.auto, '\')"' +
					' role="option">' +
					'<table role="presentation" cellspacing=0 cellpadding=0 width="100%">' +
						'<tr>' +
							'<td>' +
								'<span class="cke_colorbox" id="', colorBoxId, '"></span>' +
							'</td>' +
							'<td colspan=7 align=center>',
								lang.auto,
							'</td>' +
						'</tr>' +
					'</table>' +
				'</a>' +
				'<table role="presentation" cellspacing=0 cellpadding=0 width="100%">' );

			// Render the color boxes.
			for ( var i = 0 ; i < colors.length ; i++ )
			{
				if ( ( i % 8 ) === 0 )
					output.push( '</tr><tr>' );

				var parts = colors[ i ].split( '/' ),
					colorName = parts[ 0 ],
					colorCode = parts[ 1 ] || colorName;

				// The data can be only a color code (without #) or colorName + color code
				// If only a color code is provided, then the colorName is the color with the hash
				// Convert the color from RGB to RRGGBB for better compatibility with IE and <font>. See #5676
				if (!parts[1])
					colorName = '#' + colorName.replace( /^(.)(.)(.)$/, '$1$1$2$2$3$3' );

				var colorLabel = editor.lang.colors[ colorCode ] || colorCode;
				output.push(
					'<td>' +
						'<a class="cke_colorbox" _cke_focus=1 hidefocus=true' +
							' title="', colorLabel, '"' +
							' onclick="CKEDITOR.tools.callFunction(', clickFn, ',\'', colorName, '\',\'', type, '\'); return false;"' +
							' href="javascript:void(\'', colorLabel, '\')"' +
							' role="option">' +
							'<span class="cke_colorbox" style="background-color:#', colorCode, '"></span>' +
						'</a>' +
					'</td>' );
			}

			// Render the "More Colors" button.
			if ( config.colorButton_enableMore === undefined || config.colorButton_enableMore )
			{
				output.push(
					'</tr>' +
					'<tr>' +
						'<td colspan=8 align=center>' +
							'<a class="cke_colormore" _cke_focus=1 hidefocus=true' +
								' title="', lang.more, '"' +
								' onclick="CKEDITOR.tools.callFunction(', clickFn, ',\'?\',\'', type, '\');return false;"' +
								' href="javascript:void(\'', lang.more, '\')"',
								' role="option">',
								lang.more,
							'</a>' +
						'</td>' );	// tr is later in the code.
			}

			output.push( '</tr></table>' );

			return output.join( '' );
		}

		function isUnstylable( ele )
		{
			return ( ele.getAttribute( 'contentEditable' ) == 'false' ) || ele.getAttribute( 'data-nostyle' );
		}
	}
});

/**
 * Whether to enable the <strong>More Colors</strong> button in the color selectors.
 * @name CKEDITOR.config.colorButton_enableMore
 * @default <code>true</code>
 * @type Boolean
 * @example
 * config.colorButton_enableMore = false;
 */

/**
 * Defines the colors to be displayed in the color selectors. This is a string
 * containing hexadecimal notation for HTML colors, without the "#" prefix.
 * <br /><br />
 * Since 3.3: A color name may optionally be defined by prefixing the entries with
 * a name and the slash character. For example, "FontColor1/FF9900" will be
 * displayed as the color #FF9900 in the selector, but will be output as "FontColor1".
 * @name CKEDITOR.config.colorButton_colors
 * @type String
 * @default <code>'000,800000,8B4513,2F4F4F,008080,000080,4B0082,696969,B22222,A52A2A,DAA520,006400,40E0D0,0000CD,800080,808080,F00,FF8C00,FFD700,008000,0FF,00F,EE82EE,A9A9A9,FFA07A,FFA500,FFFF00,00FF00,AFEEEE,ADD8E6,DDA0DD,D3D3D3,FFF0F5,FAEBD7,FFFFE0,F0FFF0,F0FFFF,F0F8FF,E6E6FA,FFF'</code>
 * @example
 * // Brazil colors only.
 * config.colorButton_colors = '00923E,F8C100,28166F';
 * @example
 * config.colorButton_colors = 'FontColor1/FF9900,FontColor2/0066CC,FontColor3/F00'
 */
CKEDITOR.config.colorButton_colors =
	'000,800000,8B4513,2F4F4F,008080,000080,4B0082,696969,' +
	'B22222,A52A2A,DAA520,006400,40E0D0,0000CD,800080,808080,' +
	'F00,FF8C00,FFD700,008000,0FF,00F,EE82EE,A9A9A9,' +
	'FFA07A,FFA500,FFFF00,00FF00,AFEEEE,ADD8E6,DDA0DD,D3D3D3,' +
	'FFF0F5,FAEBD7,FFFFE0,F0FFF0,F0FFFF,F0F8FF,E6E6FA,FFF';

/**
 * Stores the style definition that applies the text foreground color.
 * @name CKEDITOR.config.colorButton_foreStyle
 * @type Object
 * @default (see example)
 * @example
 * // This is actually the default value.
 * config.colorButton_foreStyle =
 *     {
 *         element : 'span',
 *         styles : { 'color' : '#(color)' }
 *     };
 */
CKEDITOR.config.colorButton_foreStyle =
	{
		element		: 'span',
		styles		: { 'color' : '#(color)' },
		overrides	: [ { element : 'font', attributes : { 'color' : null } } ]
	};

/**
 * Stores the style definition that applies the text background color.
 * @name CKEDITOR.config.colorButton_backStyle
 * @type Object
 * @default (see example)
 * @example
 * // This is actually the default value.
 * config.colorButton_backStyle =
 *     {
 *         element : 'span',
 *         styles : { 'background-color' : '#(color)' }
 *     };
 */
CKEDITOR.config.colorButton_backStyle =
	{
		element		: 'span',
		styles		: { 'background-color' : '#(color)' }
	};
