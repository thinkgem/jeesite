/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Image plugin
 */

(function()
{

CKEDITOR.plugins.add( 'image',
{
	requires: [ 'dialog' ],

	init : function( editor )
	{
		var pluginName = 'image';

		// Register the dialog.
		CKEDITOR.dialog.add( pluginName, this.path + 'dialogs/image.js' );

		// Register the command.
		editor.addCommand( pluginName, new CKEDITOR.dialogCommand( pluginName ) );

		// Register the toolbar button.
		editor.ui.addButton( 'Image',
			{
				label : editor.lang.common.image,
				command : pluginName
			});

		editor.on( 'doubleclick', function( evt )
			{
				var element = evt.data.element;

				if ( element.is( 'img' ) && !element.data( 'cke-realelement' ) && !element.isReadOnly() )
					evt.data.dialog = 'image';
			});

		// If the "menu" plugin is loaded, register the menu items.
		if ( editor.addMenuItems )
		{
			editor.addMenuItems(
				{
					image :
					{
						label : editor.lang.image.menu,
						command : 'image',
						group : 'image'
					}
				});
		}

		// If the "contextmenu" plugin is loaded, register the listeners.
		if ( editor.contextMenu )
		{
			editor.contextMenu.addListener( function( element, selection )
				{
					if ( getSelectedImage( editor, element ) )
						return { image : CKEDITOR.TRISTATE_OFF };
				});
		}
	},
	afterInit : function( editor )
	{
		// Customize the behavior of the alignment commands. (#7430)
		setupAlignCommand( 'left' );
		setupAlignCommand( 'right' );
		setupAlignCommand( 'center' );
		setupAlignCommand( 'block' );

		function setupAlignCommand( value )
		{
			var command = editor.getCommand( 'justify' + value );
			if ( command )
			{
				if ( value == 'left' || value == 'right' )
				{
					command.on( 'exec', function( evt )
						{
							var img = getSelectedImage( editor ), align;
							if ( img )
							{
								align = getImageAlignment( img );
								if ( align == value )
								{
									img.removeStyle( 'float' );

									// Remove "align" attribute when necessary.
									if ( value == getImageAlignment( img ) )
										img.removeAttribute( 'align' );
								}
								else
									img.setStyle( 'float', value );

								evt.cancel();
							}
						});
				}

				command.on( 'refresh', function( evt )
					{
						var img = getSelectedImage( editor ), align;
						if ( img )
						{
							align = getImageAlignment( img );

							this.setState(
								( align == value ) ? CKEDITOR.TRISTATE_ON :
								( value == 'right' || value == 'left' ) ? CKEDITOR.TRISTATE_OFF :
								CKEDITOR.TRISTATE_DISABLED );

							evt.cancel();
						}
					});
			}
		}
	}
});

function getSelectedImage( editor, element )
{
	if ( !element )
	{
		var sel = editor.getSelection();
		element = ( sel.getType() == CKEDITOR.SELECTION_ELEMENT ) && sel.getSelectedElement();
	}

	if ( element && element.is( 'img' ) && !element.data( 'cke-realelement' ) && !element.isReadOnly() )
		return element;
}

function getImageAlignment( element )
{
	var align = element.getStyle( 'float' );

	if ( align == 'inherit' || align == 'none' )
		align = 0;

	if ( !align )
		align = element.getAttribute( 'align' );

	return align;
}

})();

/**
 * Whether to remove links when emptying the link URL field in the image dialog.
 * @type Boolean
 * @default true
 * @example
 * config.image_removeLinkByEmptyURL = false;
 */
CKEDITOR.config.image_removeLinkByEmptyURL = true;

/**
 *  Padding text to set off the image in preview area.
 * @name CKEDITOR.config.image_previewText
 * @type String
 * @default "Lorem ipsum dolor..." placehoder text.
 * @example
 * config.image_previewText = CKEDITOR.tools.repeat( '___ ', 100 );
 */
