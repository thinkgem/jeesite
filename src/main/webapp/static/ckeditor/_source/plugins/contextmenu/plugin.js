/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'contextmenu',
{
	requires : [ 'menu' ],

	// Make sure the base class (CKEDITOR.menu) is loaded before it (#3318).
	onLoad : function()
	{
		CKEDITOR.plugins.contextMenu = CKEDITOR.tools.createClass(
		{
			base : CKEDITOR.menu,

			$ : function( editor )
			{
				this.base.call( this, editor,
				{
					panel:
					{
						className : editor.skinClass + ' cke_contextmenu',
						attributes :
						{
							'aria-label' : editor.lang.contextmenu.options
						}
					}
				});
			},

			proto :
			{
				addTarget : function( element, nativeContextMenuOnCtrl )
				{
					// Opera doesn't support 'contextmenu' event, we have duo approaches employed here:
					// 1. Inherit the 'button override' hack we introduced in v2 (#4530), while this require the Opera browser
					//  option 'Allow script to detect context menu/right click events' to be always turned on.
					// 2. Considering the fact that ctrl/meta key is not been occupied
					//  for multiple range selecting (like Gecko), we use this key
					//  combination as a fallback for triggering context-menu. (#4530)
					if ( CKEDITOR.env.opera && !( 'oncontextmenu' in document.body ))
					{
						var contextMenuOverrideButton;
						element.on( 'mousedown', function( evt )
						{
							evt = evt.data;
							if ( evt.$.button != 2 )
							{
								if ( evt.getKeystroke() == CKEDITOR.CTRL + 1 )
									element.fire( 'contextmenu', evt );
								return;
							}

							if ( nativeContextMenuOnCtrl
								 && ( CKEDITOR.env.mac ? evt.$.metaKey : evt.$.ctrlKey ) )
								return;

							var target = evt.getTarget();

							if ( !contextMenuOverrideButton )
							{
								var ownerDoc =  target.getDocument();
								contextMenuOverrideButton = ownerDoc.createElement( 'input' ) ;
								contextMenuOverrideButton.$.type = 'button' ;
								ownerDoc.getBody().append( contextMenuOverrideButton ) ;
							}

							contextMenuOverrideButton.setAttribute( 'style', 'position:absolute;top:' + ( evt.$.clientY - 2 ) +
								'px;left:' + ( evt.$.clientX - 2 ) +
								'px;width:5px;height:5px;opacity:0.01' );

						} );

						element.on( 'mouseup', function ( evt )
						{
							if ( contextMenuOverrideButton )
							{
								contextMenuOverrideButton.remove();
								contextMenuOverrideButton = undefined;
								// Simulate 'contextmenu' event.
								element.fire( 'contextmenu', evt.data );
							}
						} );
					}

					element.on( 'contextmenu', function( event )
						{
							var domEvent = event.data;

							if ( nativeContextMenuOnCtrl &&
								 // Safari on Windows always show 'ctrlKey' as true in 'contextmenu' event,
								// which make this property unreliable. (#4826)
								 ( CKEDITOR.env.webkit ? holdCtrlKey : ( CKEDITOR.env.mac ? domEvent.$.metaKey : domEvent.$.ctrlKey ) ) )
								return;


							// Cancel the browser context menu.
							domEvent.preventDefault();

							var offsetParent = domEvent.getTarget().getDocument().getDocumentElement(),
								offsetX = domEvent.$.clientX,
								offsetY = domEvent.$.clientY;

							CKEDITOR.tools.setTimeout( function()
								{
									this.open( offsetParent, null, offsetX, offsetY );

								// IE needs a short while to allow selection change before opening menu. (#7908)
								}, CKEDITOR.env.ie? 200 : 0, this );
						},
						this );

					if ( CKEDITOR.env.opera )
					{
						// 'contextmenu' event triggered by Windows menu key is unpreventable,
						// cancel the key event itself. (#6534)
						element.on( 'keypress' , function ( evt )
						{
							var domEvent = evt.data;

							if ( domEvent.$.keyCode === 0 )
								domEvent.preventDefault();
						});
					}

					if ( CKEDITOR.env.webkit )
					{
						var holdCtrlKey,
							onKeyDown = function( event )
							{
								holdCtrlKey = CKEDITOR.env.mac ? event.data.$.metaKey : event.data.$.ctrlKey ;
							},
							resetOnKeyUp = function()
							{
								holdCtrlKey = 0;
							};

						element.on( 'keydown', onKeyDown );
						element.on( 'keyup', resetOnKeyUp );
						element.on( 'contextmenu', resetOnKeyUp );
					}
				},

				open : function( offsetParent, corner, offsetX, offsetY )
				{
					this.editor.focus();
					offsetParent = offsetParent || CKEDITOR.document.getDocumentElement();
					this.show( offsetParent, corner, offsetX, offsetY );
				}
			}
		});
	},

	beforeInit : function( editor )
	{
		editor.contextMenu = new CKEDITOR.plugins.contextMenu( editor );

		editor.addCommand( 'contextMenu',
			{
				exec : function()
					{
						editor.contextMenu.open( editor.document.getBody() );
					}
			});
	}
});

/**
 * Whether to show the browser native context menu when the <em>Ctrl</em> or
 * <em>Meta</em> (Mac) key is pressed on opening the context menu with the
 * right mouse button click or the <em>Menu</em> key.
 * @name CKEDITOR.config.browserContextMenuOnCtrl
 * @since 3.0.2
 * @type Boolean
 * @default <code>true</code>
 * @example
 * config.browserContextMenuOnCtrl = false;
 */
