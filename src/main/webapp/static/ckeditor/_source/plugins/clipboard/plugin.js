/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Clipboard support
 */

(function()
{
	// Tries to execute any of the paste, cut or copy commands in IE. Returns a
	// boolean indicating that the operation succeeded.
	var execIECommand = function( editor, command )
	{
		var doc = editor.document,
			body = doc.getBody();

		var enabled = false;
		var onExec = function()
		{
			enabled = true;
		};

		// The following seems to be the only reliable way to detect that
		// clipboard commands are enabled in IE. It will fire the
		// onpaste/oncut/oncopy events only if the security settings allowed
		// the command to execute.
		body.on( command, onExec );

		// IE6/7: document.execCommand has problem to paste into positioned element.
		( CKEDITOR.env.version > 7 ? doc.$ : doc.$.selection.createRange() ) [ 'execCommand' ]( command );

		body.removeListener( command, onExec );

		return enabled;
	};

	// Attempts to execute the Cut and Copy operations.
	var tryToCutCopy =
		CKEDITOR.env.ie ?
			function( editor, type )
			{
				return execIECommand( editor, type );
			}
		:		// !IE.
			function( editor, type )
			{
				try
				{
					// Other browsers throw an error if the command is disabled.
					return editor.document.$.execCommand( type, false, null );
				}
				catch( e )
				{
					return false;
				}
			};

	// A class that represents one of the cut or copy commands.
	var cutCopyCmd = function( type )
	{
		this.type = type;
		this.canUndo = this.type == 'cut';		// We can't undo copy to clipboard.
		this.startDisabled = true;
	};

	cutCopyCmd.prototype =
	{
		exec : function( editor, data )
		{
			this.type == 'cut' && fixCut( editor );

			var success = tryToCutCopy( editor, this.type );

			if ( !success )
				alert( editor.lang.clipboard[ this.type + 'Error' ] );		// Show cutError or copyError.

			return success;
		}
	};

	// Paste command.
	var pasteCmd =
	{
		canUndo : false,

		exec :
			CKEDITOR.env.ie ?
				function( editor )
				{
					// Prevent IE from pasting at the begining of the document.
					editor.focus();

					if ( !editor.document.getBody().fire( 'beforepaste' )
						 && !execIECommand( editor, 'paste' ) )
					{
						editor.fire( 'pasteDialog' );
						return false;
					}
				}
			:
				function( editor )
				{
					try
					{
						if ( !editor.document.getBody().fire( 'beforepaste' )
							 && !editor.document.$.execCommand( 'Paste', false, null ) )
						{
							throw 0;
						}
					}
					catch ( e )
					{
						setTimeout( function()
							{
								editor.fire( 'pasteDialog' );
							}, 0 );
						return false;
					}
				}
	};

	// Listens for some clipboard related keystrokes, so they get customized.
	var onKey = function( event )
	{
		if ( this.mode != 'wysiwyg' )
			return;

		switch ( event.data.keyCode )
		{
			// Paste
			case CKEDITOR.CTRL + 86 :		// CTRL+V
			case CKEDITOR.SHIFT + 45 :		// SHIFT+INS

				var body = this.document.getBody();

				// 1. Opera just misses the "paste" event.
				// 2. Firefox's "paste" event comes too late to have the plain
				// text paste bin to work.
				if ( CKEDITOR.env.opera || CKEDITOR.env.gecko )
					body.fire( 'paste' );
				return;

			// Cut
			case CKEDITOR.CTRL + 88 :		// CTRL+X
			case CKEDITOR.SHIFT + 46 :		// SHIFT+DEL

				// Save Undo snapshot.
				var editor = this;
				this.fire( 'saveSnapshot' );		// Save before paste
				setTimeout( function()
					{
						editor.fire( 'saveSnapshot' );		// Save after paste
					}, 0 );
		}
	};

	function cancel( evt ) { evt.cancel(); }

	// Allow to peek clipboard content by redirecting the
	// pasting content into a temporary bin and grab the content of it.
	function getClipboardData( evt, mode, callback )
	{
		var doc = this.document;

		// Avoid recursions on 'paste' event or consequent paste too fast. (#5730)
		if ( doc.getById( 'cke_pastebin' ) )
			return;

		// If the browser supports it, get the data directly
		if ( mode == 'text' && evt.data && evt.data.$.clipboardData )
		{
			// evt.data.$.clipboardData.types contains all the flavours in Mac's Safari, but not on windows.
			var plain = evt.data.$.clipboardData.getData( 'text/plain' );
			if ( plain )
			{
				evt.data.preventDefault();
				callback( plain );
				return;
			}
		}

		var sel = this.getSelection(),
			range = new CKEDITOR.dom.range( doc );

		// Create container to paste into
		var pastebin = new CKEDITOR.dom.element( mode == 'text' ? 'textarea' : CKEDITOR.env.webkit ? 'body' : 'div', doc );
		pastebin.setAttribute( 'id', 'cke_pastebin' );
		// Safari requires a filler node inside the div to have the content pasted into it. (#4882)
		CKEDITOR.env.webkit && pastebin.append( doc.createText( '\xa0' ) );
		doc.getBody().append( pastebin );

		pastebin.setStyles(
			{
				position : 'absolute',
				// Position the bin exactly at the position of the selected element
				// to avoid any subsequent document scroll.
				top : sel.getStartElement().getDocumentPosition().y + 'px',
				width : '1px',
				height : '1px',
				overflow : 'hidden'
			});

		// It's definitely a better user experience if we make the paste-bin pretty unnoticed
		// by pulling it off the screen.
		pastebin.setStyle( this.config.contentsLangDirection == 'ltr' ? 'left' : 'right', '-1000px' );

		var bms = sel.createBookmarks();

		this.on( 'selectionChange', cancel, null, null, 0 );

		// Turn off design mode temporarily before give focus to the paste bin.
		if ( mode == 'text' )
			pastebin.$.focus();
		else
		{
			range.setStartAt( pastebin, CKEDITOR.POSITION_AFTER_START );
			range.setEndAt( pastebin, CKEDITOR.POSITION_BEFORE_END );
			range.select( true );
		}

		var editor  = this;
		// Wait a while and grab the pasted contents
		window.setTimeout( function()
		{
			// Restore properly the document focus. (#5684, #8849)
			editor.document.getBody().focus();

			editor.removeListener( 'selectionChange', cancel );

			// IE7: selection must go before removing paste bin. (#8691)
			if ( CKEDITOR.env.ie7Compat )
			{
				sel.selectBookmarks( bms );
				pastebin.remove();
			}
			// Webkit: selection must go after removing paste bin. (#8921)
			else
			{
				pastebin.remove();
				sel.selectBookmarks( bms );
			}

			// Grab the HTML contents.
			// We need to look for a apple style wrapper on webkit it also adds
			// a div wrapper if you copy/paste the body of the editor.
			// Remove hidden div and restore selection.
			var bogusSpan;
			pastebin = ( CKEDITOR.env.webkit
						 && ( bogusSpan = pastebin.getFirst() )
						 && ( bogusSpan.is && bogusSpan.hasClass( 'Apple-style-span' ) ) ?
							bogusSpan : pastebin );

			callback( pastebin[ 'get' + ( mode == 'text' ? 'Value' : 'Html' ) ]() );
		}, 0 );
	}

	// Cutting off control type element in IE standards breaks the selection entirely. (#4881)
	function fixCut( editor )
	{
		if ( !CKEDITOR.env.ie || CKEDITOR.env.quirks )
			return;

		var sel = editor.getSelection();
		var control;
		if( ( sel.getType() == CKEDITOR.SELECTION_ELEMENT ) && ( control = sel.getSelectedElement() ) )
		{
			var range = sel.getRanges()[ 0 ];
			var dummy = editor.document.createText( '' );
			dummy.insertBefore( control );
			range.setStartBefore( dummy );
			range.setEndAfter( control );
			sel.selectRanges( [ range ] );

			// Clear up the fix if the paste wasn't succeeded.
			setTimeout( function()
			{
				// Element still online?
				if ( control.getParent() )
				{
					dummy.remove();
					sel.selectElement( control );
				}
			}, 0 );
		}
	}

	var depressBeforeEvent,
		inReadOnly;
	function stateFromNamedCommand( command, editor )
	{
		var retval;

		if ( inReadOnly && command in { Paste : 1, Cut : 1 } )
			return CKEDITOR.TRISTATE_DISABLED;

		if ( command == 'Paste' )
		{
			// IE Bug: queryCommandEnabled('paste') fires also 'beforepaste(copy/cut)',
			// guard to distinguish from the ordinary sources (either
			// keyboard paste or execCommand) (#4874).
			CKEDITOR.env.ie && ( depressBeforeEvent = 1 );
			try
			{
				// Always return true for Webkit (which always returns false).
				retval = editor.document.$.queryCommandEnabled( command ) || CKEDITOR.env.webkit;
			}
			catch( er ) {}
			depressBeforeEvent = 0;
		}
		// Cut, Copy - check if the selection is not empty
		else
		{
			var sel = editor.getSelection(),
				ranges = sel && sel.getRanges();
			retval = sel && !( ranges.length == 1 && ranges[ 0 ].collapsed );
		}

		return retval ? CKEDITOR.TRISTATE_OFF : CKEDITOR.TRISTATE_DISABLED;
	}

	function setToolbarStates()
	{
		if ( this.mode != 'wysiwyg' )
			return;

		var pasteState = stateFromNamedCommand( 'Paste', this );

		this.getCommand( 'cut' ).setState( stateFromNamedCommand( 'Cut', this ) );
		this.getCommand( 'copy' ).setState( stateFromNamedCommand( 'Copy', this ) );
		this.getCommand( 'paste' ).setState( pasteState );
		this.fire( 'pasteState', pasteState );
	}

	// Register the plugin.
	CKEDITOR.plugins.add( 'clipboard',
		{
			requires : [ 'dialog', 'htmldataprocessor' ],
			init : function( editor )
			{
				// Inserts processed data into the editor at the end of the
				// events chain.
				editor.on( 'paste', function( evt )
					{
						var data = evt.data;
						if ( data[ 'html' ] )
							editor.insertHtml( data[ 'html' ] );
						else if ( data[ 'text' ] )
							editor.insertText( data[ 'text' ] );

						setTimeout( function () { editor.fire( 'afterPaste' ); }, 0 );

					}, null, null, 1000 );

				editor.on( 'pasteDialog', function( evt )
					{
						setTimeout( function()
						{
							// Open default paste dialog.
							editor.openDialog( 'paste' );
						}, 0 );
					});

				editor.on( 'pasteState', function( evt )
					{
						editor.getCommand( 'paste' ).setState( evt.data );
					});

				function addButtonCommand( buttonName, commandName, command, ctxMenuOrder )
				{
					var lang = editor.lang[ commandName ];

					editor.addCommand( commandName, command );
					editor.ui.addButton( buttonName,
						{
							label : lang,
							command : commandName
						});

					// If the "menu" plugin is loaded, register the menu item.
					if ( editor.addMenuItems )
					{
						editor.addMenuItem( commandName,
							{
								label : lang,
								command : commandName,
								group : 'clipboard',
								order : ctxMenuOrder
							});
					}
				}

				addButtonCommand( 'Cut', 'cut', new cutCopyCmd( 'cut' ), 1 );
				addButtonCommand( 'Copy', 'copy', new cutCopyCmd( 'copy' ), 4 );
				addButtonCommand( 'Paste', 'paste', pasteCmd, 8 );

				CKEDITOR.dialog.add( 'paste', CKEDITOR.getUrl( this.path + 'dialogs/paste.js' ) );

				editor.on( 'key', onKey, editor );

				// We'll be catching all pasted content in one line, regardless of whether the
				// it's introduced by a document command execution (e.g. toolbar buttons) or
				// user paste behaviors. (e.g. Ctrl-V)
				editor.on( 'contentDom', function()
				{
					var body = editor.document.getBody();

					// Intercept the paste before it actually takes place.
					body.on( !CKEDITOR.env.ie ? 'paste' : 'beforepaste', function( evt )
						{
							if ( depressBeforeEvent )
								return;

							// Dismiss the (wrong) 'beforepaste' event fired on toolbar menu open.
							var domEvent = evt.data && evt.data.$;
							if ( CKEDITOR.env.ie && domEvent && !domEvent.ctrlKey )
								return;

							// Fire 'beforePaste' event so clipboard flavor get customized
							// by other plugins.
							var eventData =  { mode : 'html' };
							editor.fire( 'beforePaste', eventData );

							getClipboardData.call( editor, evt, eventData.mode, function ( data )
							{
								// The very last guard to make sure the
								// paste has successfully happened.
								if ( !( data = CKEDITOR.tools.trim( data.replace( /<span[^>]+data-cke-bookmark[^<]*?<\/span>/ig,'' ) ) ) )
									return;

								var dataTransfer = {};
								dataTransfer[ eventData.mode ] = data;
								editor.fire( 'paste', dataTransfer );
							} );
						});

					if ( CKEDITOR.env.ie )
					{
						// Dismiss the (wrong) 'beforepaste' event fired on context menu open. (#7953)
						body.on( 'contextmenu', function()
						{
							depressBeforeEvent = 1;
							// Important: The following timeout will be called only after menu closed.
							setTimeout( function() { depressBeforeEvent = 0; }, 0 );
						} );

						// Handle IE's late coming "paste" event when pasting from
						// browser toolbar/context menu.
						body.on( 'paste', function( evt )
						{
							if ( !editor.document.getById( 'cke_pastebin' ) )
							{
								// Prevent native paste.
								evt.data.preventDefault();

								depressBeforeEvent = 0;
								// Resort to the paste command.
								pasteCmd.exec( editor );
							}
						} );
					}

					body.on( 'beforecut', function() { !depressBeforeEvent && fixCut( editor ); } );

					body.on( 'mouseup', function(){ setTimeout( function(){ setToolbarStates.call( editor ); }, 0 ); }, editor );
					body.on( 'keyup', setToolbarStates, editor );
				});

				// For improved performance, we're checking the readOnly state on selectionChange instead of hooking a key event for that.
				editor.on( 'selectionChange', function( evt )
				{
					inReadOnly = evt.data.selection.getRanges()[ 0 ].checkReadOnly();
					setToolbarStates.call( editor );
				});

				// If the "contextmenu" plugin is loaded, register the listeners.
				if ( editor.contextMenu )
				{
					editor.contextMenu.addListener( function( element, selection )
						{
							var readOnly = selection.getRanges()[ 0 ].checkReadOnly();
							return {
								cut : stateFromNamedCommand( 'Cut', editor ),
								copy : stateFromNamedCommand( 'Copy', editor ),
								paste : stateFromNamedCommand( 'Paste', editor )
							};
						});
				}
			}
		});
})();

/**
 * Fired when a clipboard operation is about to be taken into the editor.
 * Listeners can manipulate the data to be pasted before having it effectively
 * inserted into the document.
 * @name CKEDITOR.editor#paste
 * @since 3.1
 * @event
 * @param {String} [data.html] The HTML data to be pasted. If not available, e.data.text will be defined.
 * @param {String} [data.text] The plain text data to be pasted, available when plain text operations are to used. If not available, e.data.html will be defined.
 */

/**
 * Internal event to open the Paste dialog
 * @name CKEDITOR.editor#pasteDialog
 * @event
 */
