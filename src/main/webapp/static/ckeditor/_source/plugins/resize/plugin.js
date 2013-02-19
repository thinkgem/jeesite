/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'resize',
{
	init : function( editor )
	{
		var config = editor.config;

		// Resize in the same direction of chrome,
		// which is identical to dir of editor element. (#6614)
		var resizeDir = editor.element.getDirection( 1 );

		!config.resize_dir && ( config.resize_dir = 'both' );
		( config.resize_maxWidth == undefined ) && ( config.resize_maxWidth = 3000 );
		( config.resize_maxHeight == undefined ) && ( config.resize_maxHeight = 3000 );
		( config.resize_minWidth == undefined ) && ( config.resize_minWidth = 750 );
		( config.resize_minHeight == undefined ) && ( config.resize_minHeight = 250 );

		if ( config.resize_enabled !== false )
		{
			var container = null,
				origin,
				startSize,
				resizeHorizontal = ( config.resize_dir == 'both' || config.resize_dir == 'horizontal' ) &&
					( config.resize_minWidth != config.resize_maxWidth ),
				resizeVertical = ( config.resize_dir == 'both' || config.resize_dir == 'vertical' ) &&
					( config.resize_minHeight != config.resize_maxHeight );

			function dragHandler( evt )
			{
				var dx = evt.data.$.screenX - origin.x,
					dy = evt.data.$.screenY - origin.y,
					width = startSize.width,
					height = startSize.height,
					internalWidth = width + dx * ( resizeDir == 'rtl' ? -1 : 1 ),
					internalHeight = height + dy;

				if ( resizeHorizontal )
					width =  Math.max( config.resize_minWidth, Math.min( internalWidth, config.resize_maxWidth ) );

				if ( resizeVertical )
					height =  Math.max( config.resize_minHeight, Math.min( internalHeight, config.resize_maxHeight ) );

				// DO NOT impose fixed size with single direction resize. (#6308)
				editor.resize( resizeHorizontal ? width : null, height );
			}

			function dragEndHandler ( evt )
			{
				CKEDITOR.document.removeListener( 'mousemove', dragHandler );
				CKEDITOR.document.removeListener( 'mouseup', dragEndHandler );

				if ( editor.document )
				{
					editor.document.removeListener( 'mousemove', dragHandler );
					editor.document.removeListener( 'mouseup', dragEndHandler );
				}
			}

			var mouseDownFn = CKEDITOR.tools.addFunction( function( $event )
				{
					if ( !container )
						container = editor.getResizable();

					startSize = { width : container.$.offsetWidth || 0, height : container.$.offsetHeight || 0 };
					origin = { x : $event.screenX, y : $event.screenY };

					config.resize_minWidth > startSize.width && ( config.resize_minWidth = startSize.width );
					config.resize_minHeight > startSize.height && ( config.resize_minHeight = startSize.height );

					CKEDITOR.document.on( 'mousemove', dragHandler );
					CKEDITOR.document.on( 'mouseup', dragEndHandler );

					if ( editor.document )
					{
						editor.document.on( 'mousemove', dragHandler );
						editor.document.on( 'mouseup', dragEndHandler );
					}
				});

			editor.on( 'destroy', function() { CKEDITOR.tools.removeFunction( mouseDownFn ); } );

			editor.on( 'themeSpace', function( event )
				{
					if ( event.data.space == 'bottom' )
					{
						var direction = '';
						if ( resizeHorizontal && !resizeVertical )
							direction = ' cke_resizer_horizontal';
						if ( !resizeHorizontal && resizeVertical )
							direction = ' cke_resizer_vertical';

						var resizerHtml =
							'<div' +
							' class="cke_resizer' + direction + ' cke_resizer_' + resizeDir + '"' +
							' title="' + CKEDITOR.tools.htmlEncode( editor.lang.resize ) + '"' +
							' onmousedown="CKEDITOR.tools.callFunction(' + mouseDownFn + ', event)"' +
							'></div>';

						// Always sticks the corner of botttom space.
						resizeDir == 'ltr' && direction == 'ltr' ?
							event.data.html += resizerHtml :
							event.data.html = resizerHtml + event.data.html;
					}
				}, editor, null, 100 );
		}
	}
} );

/**
 * The minimum editor width, in pixels, when resizing the editor interface by using the resize handle.
 * Note: It falls back to editor's actual width if it is smaller than the default value.
 * @name CKEDITOR.config.resize_minWidth
 * @type Number
 * @default 750
 * @example
 * config.resize_minWidth = 500;
 */

/**
 * The minimum editor height, in pixels, when resizing the editor interface by using the resize handle.
 * Note: It falls back to editor's actual height if it is smaller than the default value.
 * @name CKEDITOR.config.resize_minHeight
 * @type Number
 * @default 250
 * @example
 * config.resize_minHeight = 600;
 */

/**
 * The maximum editor width, in pixels, when resizing the editor interface by using the resize handle.
 * @name CKEDITOR.config.resize_maxWidth
 * @type Number
 * @default 3000
 * @example
 * config.resize_maxWidth = 750;
 */

/**
 * The maximum editor height, in pixels, when resizing the editor interface by using the resize handle.
 * @name CKEDITOR.config.resize_maxHeight
 * @type Number
 * @default 3000
 * @example
 * config.resize_maxHeight = 600;
 */

/**
 * Whether to enable the resizing feature. If this feature is disabled, the resize handle will not be visible.
 * @name CKEDITOR.config.resize_enabled
 * @type Boolean
 * @default true
 * @example
 * config.resize_enabled = false;
 */

/**
 * The dimensions for which the editor resizing is enabled. Possible values
 * are <code>both</code>, <code>vertical</code>, and <code>horizontal</code>.
 * @name CKEDITOR.config.resize_dir
 * @type String
 * @default 'both'
 * @since 3.3
 * @example
 * config.resize_dir = 'vertical';
 */
