/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview The "sourcearea" plugin. It registers the "source" editing
 *		mode, which displays the raw data being edited in the editor.
 */

CKEDITOR.plugins.add( 'sourcearea',
{
	requires : [ 'editingblock' ],

	init : function( editor )
	{
		var sourcearea = CKEDITOR.plugins.sourcearea,
			win = CKEDITOR.document.getWindow();

		editor.on( 'editingBlockReady', function()
			{
				var textarea,
					onResize;

				editor.addMode( 'source',
					{
						load : function( holderElement, data )
						{
							if ( CKEDITOR.env.ie && CKEDITOR.env.version < 8 )
								holderElement.setStyle( 'position', 'relative' );

							// Create the source area <textarea>.
							editor.textarea = textarea = new CKEDITOR.dom.element( 'textarea' );
							textarea.setAttributes(
								{
									dir : 'ltr',
									tabIndex : CKEDITOR.env.webkit ? -1 : editor.tabIndex,
									'role' : 'textbox',
									'aria-label' : editor.lang.editorTitle.replace( '%1', editor.name )
								});
							textarea.addClass( 'cke_source' );
							textarea.addClass( 'cke_enable_context_menu' );

							editor.readOnly && textarea.setAttribute( 'readOnly', 'readonly' );

							var styles =
							{
								// IE7 has overflow the <textarea> from wrapping table cell.
								width	: CKEDITOR.env.ie7Compat ?  '99%' : '100%',
								height	: '100%',
								resize	: 'none',
								outline	: 'none',
								'text-align' : 'left'
							};

							// Having to make <textarea> fixed sized to conque the following bugs:
							// 1. The textarea height/width='100%' doesn't constraint to the 'td' in IE6/7.
							// 2. Unexpected vertical-scrolling behavior happens whenever focus is moving out of editor
							// if text content within it has overflowed. (#4762)
							if ( CKEDITOR.env.ie )
							{
								onResize = function()
								{
									// Holder rectange size is stretched by textarea,
									// so hide it just for a moment.
									textarea.hide();
									textarea.setStyle( 'height', holderElement.$.clientHeight + 'px' );
									textarea.setStyle( 'width', holderElement.$.clientWidth + 'px' );
									// When we have proper holder size, show textarea again.
									textarea.show();
								};

								editor.on( 'resize', onResize );
								win.on( 'resize', onResize );
								setTimeout( onResize, 0 );
							}

							// Reset the holder element and append the
							// <textarea> to it.
							holderElement.setHtml( '' );
							holderElement.append( textarea );
							textarea.setStyles( styles );

							editor.fire( 'ariaWidget', textarea );

							textarea.on( 'blur', function()
								{
									editor.focusManager.blur();
								});

							textarea.on( 'focus', function()
								{
									editor.focusManager.focus();
								});

							// The editor data "may be dirty" after this point.
							editor.mayBeDirty = true;

							// Set the <textarea> value.
							this.loadData( data );

							var keystrokeHandler = editor.keystrokeHandler;
							if ( keystrokeHandler )
								keystrokeHandler.attach( textarea );

							setTimeout( function()
							{
								editor.mode = 'source';
								editor.fire( 'mode', { previousMode : editor._.previousMode } );
							},
							( CKEDITOR.env.gecko || CKEDITOR.env.webkit ) ? 100 : 0 );
						},

						loadData : function( data )
						{
							textarea.setValue( data );
							editor.fire( 'dataReady' );
						},

						getData : function()
						{
							return textarea.getValue();
						},

						getSnapshotData : function()
						{
							return textarea.getValue();
						},

						unload : function( holderElement )
						{
							textarea.clearCustomData();
							editor.textarea = textarea = null;

							if ( onResize )
							{
								editor.removeListener( 'resize', onResize );
								win.removeListener( 'resize', onResize );
							}

							if ( CKEDITOR.env.ie && CKEDITOR.env.version < 8 )
								holderElement.removeStyle( 'position' );
						},

						focus : function()
						{
							textarea.focus();
						}
					});
			});

		editor.on( 'readOnly', function()
			{
				if ( editor.mode == 'source' )
				{
					if ( editor.readOnly )
						editor.textarea.setAttribute( 'readOnly', 'readonly' );
					else
						editor.textarea.removeAttribute( 'readOnly' );
				}
			});

		editor.addCommand( 'source', sourcearea.commands.source );

		if ( editor.ui.addButton )
		{
			editor.ui.addButton( 'Source',
				{
					label : editor.lang.source,
					command : 'source'
				});
		}

		editor.on( 'mode', function()
			{
				editor.getCommand( 'source' ).setState(
					editor.mode == 'source' ?
						CKEDITOR.TRISTATE_ON :
						CKEDITOR.TRISTATE_OFF );
			});
	}
});

/**
 * Holds the definition of commands an UI elements included with the sourcearea
 * plugin.
 * @example
 */
CKEDITOR.plugins.sourcearea =
{
	commands :
	{
		source :
		{
			modes : { wysiwyg:1, source:1 },
			editorFocus : false,
			readOnly : 1,
			exec : function( editor )
			{
				if ( editor.mode == 'wysiwyg' )
					editor.fire( 'saveSnapshot' );
				editor.getCommand( 'source' ).setState( CKEDITOR.TRISTATE_DISABLED );
				editor.setMode( editor.mode == 'source' ? 'wysiwyg' : 'source' );
			},

			canUndo : false
		}
	}
};
