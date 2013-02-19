/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview The floating dialog plugin.
 */

/**
 * No resize for this dialog.
 * @constant
 */
CKEDITOR.DIALOG_RESIZE_NONE = 0;

/**
 * Only allow horizontal resizing for this dialog, disable vertical resizing.
 * @constant
 */
CKEDITOR.DIALOG_RESIZE_WIDTH = 1;

/**
 * Only allow vertical resizing for this dialog, disable horizontal resizing.
 * @constant
 */
CKEDITOR.DIALOG_RESIZE_HEIGHT = 2;

/*
 * Allow the dialog to be resized in both directions.
 * @constant
 */
CKEDITOR.DIALOG_RESIZE_BOTH = 3;

(function()
{
	var cssLength = CKEDITOR.tools.cssLength;
	function isTabVisible( tabId )
	{
		return !!this._.tabs[ tabId ][ 0 ].$.offsetHeight;
	}

	function getPreviousVisibleTab()
	{
		var tabId = this._.currentTabId,
			length = this._.tabIdList.length,
			tabIndex = CKEDITOR.tools.indexOf( this._.tabIdList, tabId ) + length;

		for ( var i = tabIndex - 1 ; i > tabIndex - length ; i-- )
		{
			if ( isTabVisible.call( this, this._.tabIdList[ i % length ] ) )
				return this._.tabIdList[ i % length ];
		}

		return null;
	}

	function getNextVisibleTab()
	{
		var tabId = this._.currentTabId,
			length = this._.tabIdList.length,
			tabIndex = CKEDITOR.tools.indexOf( this._.tabIdList, tabId );

		for ( var i = tabIndex + 1 ; i < tabIndex + length ; i++ )
		{
			if ( isTabVisible.call( this, this._.tabIdList[ i % length ] ) )
				return this._.tabIdList[ i % length ];
		}

		return null;
	}


	function clearOrRecoverTextInputValue( container, isRecover )
	{
		var inputs = container.$.getElementsByTagName( 'input' );
		for ( var i = 0, length = inputs.length; i < length ; i++ )
		{
			var item = new CKEDITOR.dom.element( inputs[ i ] );

			if ( item.getAttribute( 'type' ).toLowerCase() == 'text' )
			{
				if ( isRecover )
				{
					item.setAttribute( 'value', item.getCustomData( 'fake_value' ) || '' );
					item.removeCustomData( 'fake_value' );
				}
				else
				{
					item.setCustomData( 'fake_value', item.getAttribute( 'value' ) );
					item.setAttribute( 'value', '' );
				}
			}
		}
	}

	// Handle dialog element validation state UI changes.
	function handleFieldValidated( isValid, msg )
	{
		var input = this.getInputElement();
		if ( input )
		{
			isValid ? input.removeAttribute( 'aria-invalid' )
				: input.setAttribute( 'aria-invalid', true );
		}

		if ( !isValid )
		{
			if ( this.select )
				this.select();
			else
				this.focus();
		}

		msg && alert( msg );

		this.fire( 'validated', { valid : isValid, msg : msg } );
	}

	function resetField()
	{
		var input = this.getInputElement();
		input && input.removeAttribute( 'aria-invalid' );
	}


	/**
	 * This is the base class for runtime dialog objects. An instance of this
	 * class represents a single named dialog for a single editor instance.
	 * @param {Object} editor The editor which created the dialog.
	 * @param {String} dialogName The dialog's registered name.
	 * @constructor
	 * @example
	 * var dialogObj = new CKEDITOR.dialog( editor, 'smiley' );
	 */
	CKEDITOR.dialog = function( editor, dialogName )
	{
		// Load the dialog definition.
		var definition = CKEDITOR.dialog._.dialogDefinitions[ dialogName ],
			defaultDefinition = CKEDITOR.tools.clone( defaultDialogDefinition ),
			buttonsOrder = editor.config.dialog_buttonsOrder || 'OS',
			dir = editor.lang.dir,
			tabsToRemove = {},
			i,
			processed, stopPropagation;

			if ( ( buttonsOrder == 'OS' && CKEDITOR.env.mac ) ||    // The buttons in MacOS Apps are in reverse order (#4750)
				( buttonsOrder == 'rtl' && dir == 'ltr' ) ||
				( buttonsOrder == 'ltr' && dir == 'rtl' ) )
					defaultDefinition.buttons.reverse();


		// Completes the definition with the default values.
		definition = CKEDITOR.tools.extend( definition( editor ), defaultDefinition );

		// Clone a functionally independent copy for this dialog.
		definition = CKEDITOR.tools.clone( definition );

		// Create a complex definition object, extending it with the API
		// functions.
		definition = new definitionObject( this, definition );

		var doc = CKEDITOR.document;

		var themeBuilt = editor.theme.buildDialog( editor );

		// Initialize some basic parameters.
		this._ =
		{
			editor : editor,
			element : themeBuilt.element,
			name : dialogName,
			contentSize : { width : 0, height : 0 },
			size : { width : 0, height : 0 },
			contents : {},
			buttons : {},
			accessKeyMap : {},

			// Initialize the tab and page map.
			tabs : {},
			tabIdList : [],
			currentTabId : null,
			currentTabIndex : null,
			pageCount : 0,
			lastTab : null,
			tabBarMode : false,

			// Initialize the tab order array for input widgets.
			focusList : [],
			currentFocusIndex : 0,
			hasFocus : false
		};

		this.parts = themeBuilt.parts;

		CKEDITOR.tools.setTimeout( function()
			{
				editor.fire( 'ariaWidget', this.parts.contents );
			},
			0, this );

		// Set the startup styles for the dialog, avoiding it enlarging the
		// page size on the dialog creation.
		var startStyles = {
				position : CKEDITOR.env.ie6Compat ? 'absolute' : 'fixed',
				top : 0,
				visibility : 'hidden'
		};

		startStyles[ dir == 'rtl' ? 'right' : 'left' ] = 0;
		this.parts.dialog.setStyles( startStyles );


		// Call the CKEDITOR.event constructor to initialize this instance.
		CKEDITOR.event.call( this );

		// Fire the "dialogDefinition" event, making it possible to customize
		// the dialog definition.
		this.definition = definition = CKEDITOR.fire( 'dialogDefinition',
			{
				name : dialogName,
				definition : definition
			}
			, editor ).definition;

		// Cache tabs that should be removed.
		if ( !( 'removeDialogTabs' in editor._ ) && editor.config.removeDialogTabs )
		{
			var removeContents = editor.config.removeDialogTabs.split( ';' );

			for ( i = 0; i < removeContents.length; i++ )
			{
				var parts = removeContents[ i ].split( ':' );
				if ( parts.length == 2 )
				{
					var removeDialogName = parts[ 0 ];
					if ( !tabsToRemove[ removeDialogName ] )
						tabsToRemove[ removeDialogName ] = [];
					tabsToRemove[ removeDialogName ].push( parts[ 1 ] );
				}
			}
			editor._.removeDialogTabs = tabsToRemove;
		}

		// Remove tabs of this dialog.
		if ( editor._.removeDialogTabs && ( tabsToRemove = editor._.removeDialogTabs[ dialogName ] ) )
		{
			for ( i = 0; i < tabsToRemove.length; i++ )
				definition.removeContents( tabsToRemove[ i ] );
		}

		// Initialize load, show, hide, ok and cancel events.
		if ( definition.onLoad )
			this.on( 'load', definition.onLoad );

		if ( definition.onShow )
			this.on( 'show', definition.onShow );

		if ( definition.onHide )
			this.on( 'hide', definition.onHide );

		if ( definition.onOk )
		{
			this.on( 'ok', function( evt )
				{
					// Dialog confirm might probably introduce content changes (#5415).
					editor.fire( 'saveSnapshot' );
					setTimeout( function () { editor.fire( 'saveSnapshot' ); }, 0 );
					if ( definition.onOk.call( this, evt ) === false )
						evt.data.hide = false;
				});
		}

		if ( definition.onCancel )
		{
			this.on( 'cancel', function( evt )
				{
					if ( definition.onCancel.call( this, evt ) === false )
						evt.data.hide = false;
				});
		}

		var me = this;

		// Iterates over all items inside all content in the dialog, calling a
		// function for each of them.
		var iterContents = function( func )
		{
			var contents = me._.contents,
				stop = false;

			for ( var i in contents )
			{
				for ( var j in contents[i] )
				{
					stop = func.call( this, contents[i][j] );
					if ( stop )
						return;
				}
			}
		};

		this.on( 'ok', function( evt )
			{
				iterContents( function( item )
					{
						if ( item.validate )
						{
							var retval = item.validate( this ),
								invalid = typeof ( retval ) == 'string' || retval === false;

							if ( invalid )
							{
								evt.data.hide = false;
								evt.stop();
							}

							handleFieldValidated.call( item, !invalid, typeof retval == 'string' ? retval : undefined );
							return invalid;
						}
					});
			}, this, null, 0 );

		this.on( 'cancel', function( evt )
			{
				iterContents( function( item )
					{
						if ( item.isChanged() )
						{
							if ( !confirm( editor.lang.common.confirmCancel ) )
								evt.data.hide = false;
							return true;
						}
					});
			}, this, null, 0 );

		this.parts.close.on( 'click', function( evt )
				{
					if ( this.fire( 'cancel', { hide : true } ).hide !== false )
						this.hide();
					evt.data.preventDefault();
				}, this );

		// Sort focus list according to tab order definitions.
		function setupFocus()
		{
			var focusList = me._.focusList;
			focusList.sort( function( a, b )
				{
					// Mimics browser tab order logics;
					if ( a.tabIndex != b.tabIndex )
						return b.tabIndex - a.tabIndex;
					//  Sort is not stable in some browsers,
					// fall-back the comparator to 'focusIndex';
					else
						return a.focusIndex - b.focusIndex;
				});

			var size = focusList.length;
			for ( var i = 0; i < size; i++ )
				focusList[ i ].focusIndex = i;
		}

		function changeFocus( offset )
		{
			var focusList = me._.focusList;
			offset = offset || 0;

			if ( focusList.length < 1 )
				return;

			var current = me._.currentFocusIndex;

			// Trigger the 'blur' event of  any input element before anything,
			// since certain UI updates may depend on it.
			try
			{
				focusList[ current ].getInputElement().$.blur();
			}
			catch( e ){}

			var startIndex = ( current + offset + focusList.length ) % focusList.length,
				currentIndex = startIndex;
			while ( offset && !focusList[ currentIndex ].isFocusable() )
			{
				currentIndex = ( currentIndex + offset + focusList.length ) % focusList.length;
				if ( currentIndex == startIndex )
					break;
			}

			focusList[ currentIndex ].focus();

			// Select whole field content.
			if ( focusList[ currentIndex ].type == 'text' )
				focusList[ currentIndex ].select();
		}

		this.changeFocus = changeFocus;


		function keydownHandler( evt )
		{
			// If I'm not the top dialog, ignore.
			if ( me != CKEDITOR.dialog._.currentTop )
				return;

			var keystroke = evt.data.getKeystroke(),
				rtl = editor.lang.dir == 'rtl',
				button;

			processed = stopPropagation = 0;

			if ( keystroke == 9 || keystroke == CKEDITOR.SHIFT + 9 )
			{
				var shiftPressed = ( keystroke == CKEDITOR.SHIFT + 9 );

				// Handling Tab and Shift-Tab.
				if ( me._.tabBarMode )
				{
					// Change tabs.
					var nextId = shiftPressed ? getPreviousVisibleTab.call( me ) : getNextVisibleTab.call( me );
					me.selectPage( nextId );
					me._.tabs[ nextId ][ 0 ].focus();
				}
				else
				{
					// Change the focus of inputs.
					changeFocus( shiftPressed ? -1 : 1 );
				}

				processed = 1;
			}
			else if ( keystroke == CKEDITOR.ALT + 121 && !me._.tabBarMode && me.getPageCount() > 1 )
			{
				// Alt-F10 puts focus into the current tab item in the tab bar.
				me._.tabBarMode = true;
				me._.tabs[ me._.currentTabId ][ 0 ].focus();
				processed = 1;
			}
			else if ( ( keystroke == 37 || keystroke == 39 ) && me._.tabBarMode )
			{
				// Arrow keys - used for changing tabs.
				nextId = ( keystroke == ( rtl ? 39 : 37 ) ? getPreviousVisibleTab.call( me ) : getNextVisibleTab.call( me ) );
				me.selectPage( nextId );
				me._.tabs[ nextId ][ 0 ].focus();
				processed = 1;
			}
			else if ( ( keystroke == 13 || keystroke == 32 ) && me._.tabBarMode )
			{
				this.selectPage( this._.currentTabId );
				this._.tabBarMode = false;
				this._.currentFocusIndex = -1;
				changeFocus( 1 );
				processed = 1;
			}
			// If user presses enter key in a text box, it implies clicking OK for the dialog.
			else if ( keystroke == 13 /*ENTER*/ )
			{
				// Don't do that for a target that handles ENTER.
				var target = evt.data.getTarget();
				if ( !target.is( 'a', 'button', 'select' ) && ( !target.is( 'input' ) || target.$.type != 'button' ) )
				{
					button = this.getButton( 'ok' );
					button && CKEDITOR.tools.setTimeout( button.click, 0, button );
					processed = 1;
				}
				stopPropagation = 1; // Always block the propagation (#4269)
			}
			else if ( keystroke == 27 /*ESC*/ )
			{
				button = this.getButton( 'cancel' );

				// If there's a Cancel button, click it, else just fire the cancel event and hide the dialog.
				if ( button )
					CKEDITOR.tools.setTimeout( button.click, 0, button );
				else
				{
					if ( this.fire( 'cancel', { hide : true } ).hide !== false )
						this.hide();
				}
				stopPropagation = 1; // Always block the propagation (#4269)
			}
			else
				return;

			keypressHandler( evt );
		}

		function keypressHandler( evt )
		{
			if ( processed )
				evt.data.preventDefault(1);
			else if ( stopPropagation )
				evt.data.stopPropagation();
		}

		var dialogElement = this._.element;
		// Add the dialog keyboard handlers.
		this.on( 'show', function()
			{
				dialogElement.on( 'keydown', keydownHandler, this );

				// Some browsers instead, don't cancel key events in the keydown, but in the
				// keypress. So we must do a longer trip in those cases. (#4531,#8985)
				if ( CKEDITOR.env.opera || CKEDITOR.env.gecko )
					dialogElement.on( 'keypress', keypressHandler, this );

			} );
		this.on( 'hide', function()
			{
				dialogElement.removeListener( 'keydown', keydownHandler );
				if ( CKEDITOR.env.opera || CKEDITOR.env.gecko )
					dialogElement.removeListener( 'keypress', keypressHandler );

				// Reset fields state when closing dialog.
				iterContents( function( item ) { resetField.apply( item ); } );
			} );
		this.on( 'iframeAdded', function( evt )
			{
				var doc = new CKEDITOR.dom.document( evt.data.iframe.$.contentWindow.document );
				doc.on( 'keydown', keydownHandler, this, null, 0 );
			} );

		// Auto-focus logic in dialog.
		this.on( 'show', function()
			{
				// Setup tabIndex on showing the dialog instead of on loading
				// to allow dynamic tab order happen in dialog definition.
				setupFocus();

				if ( editor.config.dialog_startupFocusTab
					&& me._.pageCount > 1 )
				{
					me._.tabBarMode = true;
					me._.tabs[ me._.currentTabId ][ 0 ].focus();
				}
				else if ( !this._.hasFocus )
				{
					this._.currentFocusIndex = -1;

					// Decide where to put the initial focus.
					if ( definition.onFocus )
					{
						var initialFocus = definition.onFocus.call( this );
						// Focus the field that the user specified.
						initialFocus && initialFocus.focus();
					}
					// Focus the first field in layout order.
					else
						changeFocus( 1 );

					/*
					 * IE BUG: If the initial focus went into a non-text element (e.g. button),
					 * then IE would still leave the caret inside the editing area.
					 */
					if ( this._.editor.mode == 'wysiwyg' && CKEDITOR.env.ie )
					{
						var $selection = editor.document.$.selection,
							$range = $selection.createRange();

						if ( $range )
						{
							if ( $range.parentElement && $range.parentElement().ownerDocument == editor.document.$
							  || $range.item && $range.item( 0 ).ownerDocument == editor.document.$ )
							{
								var $myRange = document.body.createTextRange();
								$myRange.moveToElementText( this.getElement().getFirst().$ );
								$myRange.collapse( true );
								$myRange.select();
							}
						}
					}
				}
			}, this, null, 0xffffffff );

		// IE6 BUG: Text fields and text areas are only half-rendered the first time the dialog appears in IE6 (#2661).
		// This is still needed after [2708] and [2709] because text fields in hidden TR tags are still broken.
		if ( CKEDITOR.env.ie6Compat )
		{
			this.on( 'load', function( evt )
					{
						var outer = this.getElement(),
							inner = outer.getFirst();
						inner.remove();
						inner.appendTo( outer );
					}, this );
		}

		initDragAndDrop( this );
		initResizeHandles( this );

		// Insert the title.
		( new CKEDITOR.dom.text( definition.title, CKEDITOR.document ) ).appendTo( this.parts.title );

		// Insert the tabs and contents.
		for ( i = 0 ; i < definition.contents.length ; i++ )
		{
			var page = definition.contents[i];
			page && this.addPage( page );
		}

		this.parts[ 'tabs' ].on( 'click', function( evt )
				{
					var target = evt.data.getTarget();
					// If we aren't inside a tab, bail out.
					if ( target.hasClass( 'cke_dialog_tab' ) )
					{
						// Get the ID of the tab, without the 'cke_' prefix and the unique number suffix.
						var id = target.$.id;
						this.selectPage( id.substring( 4, id.lastIndexOf( '_' ) ) );

						if ( this._.tabBarMode )
						{
							this._.tabBarMode = false;
							this._.currentFocusIndex = -1;
							changeFocus( 1 );
						}
						evt.data.preventDefault();
					}
				}, this );

		// Insert buttons.
		var buttonsHtml = [],
			buttons = CKEDITOR.dialog._.uiElementBuilders.hbox.build( this,
				{
					type : 'hbox',
					className : 'cke_dialog_footer_buttons',
					widths : [],
					children : definition.buttons
				}, buttonsHtml ).getChild();
		this.parts.footer.setHtml( buttonsHtml.join( '' ) );

		for ( i = 0 ; i < buttons.length ; i++ )
			this._.buttons[ buttons[i].id ] = buttons[i];
	};

	// Focusable interface. Use it via dialog.addFocusable.
	function Focusable( dialog, element, index )
	{
		this.element = element;
		this.focusIndex = index;
		// TODO: support tabIndex for focusables.
		this.tabIndex = 0;
		this.isFocusable = function()
		{
			return !element.getAttribute( 'disabled' ) && element.isVisible();
		};
		this.focus = function()
		{
			dialog._.currentFocusIndex = this.focusIndex;
			this.element.focus();
		};
		// Bind events
		element.on( 'keydown', function( e )
			{
				if ( e.data.getKeystroke() in { 32:1, 13:1 }  )
					this.fire( 'click' );
			} );
		element.on( 'focus', function()
			{
				this.fire( 'mouseover' );
			} );
		element.on( 'blur', function()
			{
				this.fire( 'mouseout' );
			} );
	}

	CKEDITOR.dialog.prototype =
	{
		destroy : function()
		{
			this.hide();
			this._.element.remove();
		},

		/**
		 * Resizes the dialog.
		 * @param {Number} width The width of the dialog in pixels.
		 * @param {Number} height The height of the dialog in pixels.
		 * @function
		 * @example
		 * dialogObj.resize( 800, 640 );
		 */
		resize : (function()
		{
			return function( width, height )
			{
				if ( this._.contentSize && this._.contentSize.width == width && this._.contentSize.height == height )
					return;

				CKEDITOR.dialog.fire( 'resize',
					{
						dialog : this,
						skin : this._.editor.skinName,
						width : width,
						height : height
					}, this._.editor );

				this.fire( 'resize',
					{
						skin : this._.editor.skinName,
						width : width,
						height : height
					}, this._.editor );

				// Update dialog position when dimension get changed in RTL.
				if ( this._.editor.lang.dir == 'rtl' && this._.position )
					this._.position.x = CKEDITOR.document.getWindow().getViewPaneSize().width -
						this._.contentSize.width - parseInt( this._.element.getFirst().getStyle( 'right' ), 10 );

				this._.contentSize = { width : width, height : height };
			};
		})(),

		/**
		 * Gets the current size of the dialog in pixels.
		 * @returns {Object} An object with "width" and "height" properties.
		 * @example
		 * var width = dialogObj.getSize().width;
		 */
		getSize : function()
		{
			var element = this._.element.getFirst();
			return { width : element.$.offsetWidth || 0, height : element.$.offsetHeight || 0};
		},

		/**
		 * Moves the dialog to an (x, y) coordinate relative to the window.
		 * @function
		 * @param {Number} x The target x-coordinate.
		 * @param {Number} y The target y-coordinate.
		 * @param {Boolean} save Flag indicate whether the dialog position should be remembered on next open up.
		 * @example
		 * dialogObj.move( 10, 40 );
		 */
		move : (function()
		{
			var isFixed;
			return function( x, y, save )
			{
				// The dialog may be fixed positioned or absolute positioned. Ask the
				// browser what is the current situation first.
				var element = this._.element.getFirst(),
					rtl = this._.editor.lang.dir == 'rtl';

				if ( isFixed === undefined )
					isFixed = element.getComputedStyle( 'position' ) == 'fixed';

				if ( isFixed && this._.position && this._.position.x == x && this._.position.y == y )
					return;

				// Save the current position.
				this._.position = { x : x, y : y };

				// If not fixed positioned, add scroll position to the coordinates.
				if ( !isFixed )
				{
					var scrollPosition = CKEDITOR.document.getWindow().getScrollPosition();
					x += scrollPosition.x;
					y += scrollPosition.y;
				}

				// Translate coordinate for RTL.
				if ( rtl )
				{
					var dialogSize = this.getSize(),
						viewPaneSize = CKEDITOR.document.getWindow().getViewPaneSize();
					x = viewPaneSize.width - dialogSize.width - x;
				}

				var styles = { 'top'	: ( y > 0 ? y : 0 ) + 'px' };
				styles[ rtl ? 'right' : 'left' ] = ( x > 0 ? x : 0 ) + 'px';

				element.setStyles( styles );

				save && ( this._.moved = 1 );
			};
		})(),

		/**
		 * Gets the dialog's position in the window.
		 * @returns {Object} An object with "x" and "y" properties.
		 * @example
		 * var dialogX = dialogObj.getPosition().x;
		 */
		getPosition : function(){ return CKEDITOR.tools.extend( {}, this._.position ); },

		/**
		 * Shows the dialog box.
		 * @example
		 * dialogObj.show();
		 */
		show : function()
		{
			// Insert the dialog's element to the root document.
			var element = this._.element;
			var definition = this.definition;
			if ( !( element.getParent() && element.getParent().equals( CKEDITOR.document.getBody() ) ) )
				element.appendTo( CKEDITOR.document.getBody() );
			else
				element.setStyle( 'display', 'block' );

			// FIREFOX BUG: Fix vanishing caret for Firefox 2 or Gecko 1.8.
			if ( CKEDITOR.env.gecko && CKEDITOR.env.version < 10900 )
			{
				var dialogElement = this.parts.dialog;
				dialogElement.setStyle( 'position', 'absolute' );
				setTimeout( function()
					{
						dialogElement.setStyle( 'position', 'fixed' );
					}, 0 );
			}


			// First, set the dialog to an appropriate size.
			this.resize( this._.contentSize && this._.contentSize.width || definition.width || definition.minWidth,
					this._.contentSize && this._.contentSize.height || definition.height || definition.minHeight );

			// Reset all inputs back to their default value.
			this.reset();

			// Select the first tab by default.
			this.selectPage( this.definition.contents[0].id );

			// Set z-index.
			if ( CKEDITOR.dialog._.currentZIndex === null )
				CKEDITOR.dialog._.currentZIndex = this._.editor.config.baseFloatZIndex;
			this._.element.getFirst().setStyle( 'z-index', CKEDITOR.dialog._.currentZIndex += 10 );

			// Maintain the dialog ordering and dialog cover.
			if ( CKEDITOR.dialog._.currentTop === null )
			{
				CKEDITOR.dialog._.currentTop = this;
				this._.parentDialog = null;
				showCover( this._.editor );

			}
			else
			{
				this._.parentDialog = CKEDITOR.dialog._.currentTop;
				var parentElement = this._.parentDialog.getElement().getFirst();
				parentElement.$.style.zIndex  -= Math.floor( this._.editor.config.baseFloatZIndex / 2 );
				CKEDITOR.dialog._.currentTop = this;
			}

			element.on( 'keydown', accessKeyDownHandler );
			element.on( CKEDITOR.env.opera ? 'keypress' : 'keyup', accessKeyUpHandler );

			// Reset the hasFocus state.
			this._.hasFocus = false;

			CKEDITOR.tools.setTimeout( function()
				{
					this.layout();
					this.parts.dialog.setStyle( 'visibility', '' );

					// Execute onLoad for the first show.
					this.fireOnce( 'load', {} );
					CKEDITOR.ui.fire( 'ready', this );

					this.fire( 'show', {} );
					this._.editor.fire( 'dialogShow', this );

					// Save the initial values of the dialog.
					this.foreach( function( contentObj ) { contentObj.setInitValue && contentObj.setInitValue(); } );

				},
				100, this );
		},

		/**
		 * Rearrange the dialog to its previous position or the middle of the window.
		 * @since 3.5
		 */
		layout : function()
		{
			var viewSize = CKEDITOR.document.getWindow().getViewPaneSize(),
					dialogSize = this.getSize();

			this.move( this._.moved ? this._.position.x : ( viewSize.width - dialogSize.width ) / 2,
					this._.moved ? this._.position.y : ( viewSize.height - dialogSize.height ) / 2 );
		},

		/**
		 * Executes a function for each UI element.
		 * @param {Function} fn Function to execute for each UI element.
		 * @returns {CKEDITOR.dialog} The current dialog object.
		 */
		foreach : function( fn )
		{
			for ( var i in this._.contents )
			{
				for ( var j in this._.contents[i] )
					fn.call( this, this._.contents[i][j] );
			}
			return this;
		},

		/**
		 * Resets all input values in the dialog.
		 * @example
		 * dialogObj.reset();
		 * @returns {CKEDITOR.dialog} The current dialog object.
		 */
		reset : (function()
		{
			var fn = function( widget ){ if ( widget.reset ) widget.reset( 1 ); };
			return function(){ this.foreach( fn ); return this; };
		})(),


		/**
		 * Calls the {@link CKEDITOR.dialog.definition.uiElement#setup} method of each of the UI elements, with the arguments passed through it.
		 * It is usually being called when the dialog is opened, to put the initial value inside the field.
		 * @example
		 * dialogObj.setupContent();
		 * @example
		 * var timestamp = ( new Date() ).valueOf();
		 * dialogObj.setupContent( timestamp );
		 */
		setupContent : function()
		{
			var args = arguments;
			this.foreach( function( widget )
				{
					if ( widget.setup )
						widget.setup.apply( widget, args );
				});
		},

		/**
		 * Calls the {@link CKEDITOR.dialog.definition.uiElement#commit} method of each of the UI elements, with the arguments passed through it.
		 * It is usually being called when the user confirms the dialog, to process the values.
		 * @example
		 * dialogObj.commitContent();
		 * @example
		 * var timestamp = ( new Date() ).valueOf();
		 * dialogObj.commitContent( timestamp );
		 */
		commitContent : function()
		{
			var args = arguments;
			this.foreach( function( widget )
				{
					// Make sure IE triggers "change" event on last focused input before closing the dialog. (#7915)
					if ( CKEDITOR.env.ie && this._.currentFocusIndex == widget.focusIndex )
						widget.getInputElement().$.blur();

					if ( widget.commit )
						widget.commit.apply( widget, args );
				});
		},

		/**
		 * Hides the dialog box.
		 * @example
		 * dialogObj.hide();
		 */
		hide : function()
		{
			if ( !this.parts.dialog.isVisible() )
				return;

			this.fire( 'hide', {} );
			this._.editor.fire( 'dialogHide', this );
			// Reset the tab page.
			this.selectPage( this._.tabIdList[ 0 ] );
			var element = this._.element;
			element.setStyle( 'display', 'none' );
			this.parts.dialog.setStyle( 'visibility', 'hidden' );
			// Unregister all access keys associated with this dialog.
			unregisterAccessKey( this );

			// Close any child(top) dialogs first.
			while( CKEDITOR.dialog._.currentTop != this )
				CKEDITOR.dialog._.currentTop.hide();

			// Maintain dialog ordering and remove cover if needed.
			if ( !this._.parentDialog )
				hideCover();
			else
			{
				var parentElement = this._.parentDialog.getElement().getFirst();
				parentElement.setStyle( 'z-index', parseInt( parentElement.$.style.zIndex, 10 ) + Math.floor( this._.editor.config.baseFloatZIndex / 2 ) );
			}
			CKEDITOR.dialog._.currentTop = this._.parentDialog;

			// Deduct or clear the z-index.
			if ( !this._.parentDialog )
			{
				CKEDITOR.dialog._.currentZIndex = null;

				// Remove access key handlers.
				element.removeListener( 'keydown', accessKeyDownHandler );
				element.removeListener( CKEDITOR.env.opera ? 'keypress' : 'keyup', accessKeyUpHandler );

				var editor = this._.editor;
				editor.focus();

				if ( editor.mode == 'wysiwyg' && CKEDITOR.env.ie )
				{
					var selection = editor.getSelection();
					selection && selection.unlock( true );
				}
			}
			else
				CKEDITOR.dialog._.currentZIndex -= 10;

			delete this._.parentDialog;
			// Reset the initial values of the dialog.
			this.foreach( function( contentObj ) { contentObj.resetInitValue && contentObj.resetInitValue(); } );
		},

		/**
		 * Adds a tabbed page into the dialog.
		 * @param {Object} contents Content definition.
		 * @example
		 */
		addPage : function( contents )
		{
			var pageHtml = [],
				titleHtml = contents.label ? ' title="' + CKEDITOR.tools.htmlEncode( contents.label ) + '"' : '',
				elements = contents.elements,
				vbox = CKEDITOR.dialog._.uiElementBuilders.vbox.build( this,
						{
							type : 'vbox',
							className : 'cke_dialog_page_contents',
							children : contents.elements,
							expand : !!contents.expand,
							padding : contents.padding,
							style : contents.style || 'width: 100%;height:100%'
						}, pageHtml );

			// Create the HTML for the tab and the content block.
			var page = CKEDITOR.dom.element.createFromHtml( pageHtml.join( '' ) );
			page.setAttribute( 'role', 'tabpanel' );

			var env = CKEDITOR.env;
			var tabId = 'cke_' + contents.id + '_' + CKEDITOR.tools.getNextNumber(),
				 tab = CKEDITOR.dom.element.createFromHtml( [
					'<a class="cke_dialog_tab"',
						( this._.pageCount > 0 ? ' cke_last' : 'cke_first' ),
						titleHtml,
						( !!contents.hidden ? ' style="display:none"' : '' ),
						' id="', tabId, '"',
						env.gecko && env.version >= 10900 && !env.hc ? '' : ' href="javascript:void(0)"',
						' tabIndex="-1"',
						' hidefocus="true"',
						' role="tab">',
							contents.label,
					'</a>'
				].join( '' ) );

			page.setAttribute( 'aria-labelledby', tabId );

			// Take records for the tabs and elements created.
			this._.tabs[ contents.id ] = [ tab, page ];
			this._.tabIdList.push( contents.id );
			!contents.hidden && this._.pageCount++;
			this._.lastTab = tab;
			this.updateStyle();

			var contentMap = this._.contents[ contents.id ] = {},
				cursor,
				children = vbox.getChild();

			while ( ( cursor = children.shift() ) )
			{
				contentMap[ cursor.id ] = cursor;
				if ( typeof( cursor.getChild ) == 'function' )
					children.push.apply( children, cursor.getChild() );
			}

			// Attach the DOM nodes.

			page.setAttribute( 'name', contents.id );
			page.appendTo( this.parts.contents );

			tab.unselectable();
			this.parts.tabs.append( tab );

			// Add access key handlers if access key is defined.
			if ( contents.accessKey )
			{
				registerAccessKey( this, this, 'CTRL+' + contents.accessKey,
					tabAccessKeyDown, tabAccessKeyUp );
				this._.accessKeyMap[ 'CTRL+' + contents.accessKey ] = contents.id;
			}
		},

		/**
		 * Activates a tab page in the dialog by its id.
		 * @param {String} id The id of the dialog tab to be activated.
		 * @example
		 * dialogObj.selectPage( 'tab_1' );
		 */
		selectPage : function( id )
		{
			if ( this._.currentTabId == id )
				return;

			// Returning true means that the event has been canceled
			if ( this.fire( 'selectPage', { page : id, currentPage : this._.currentTabId } ) === true )
				return;

			// Hide the non-selected tabs and pages.
			for ( var i in this._.tabs )
			{
				var tab = this._.tabs[i][0],
					page = this._.tabs[i][1];
				if ( i != id )
				{
					tab.removeClass( 'cke_dialog_tab_selected' );
					page.hide();
				}
				page.setAttribute( 'aria-hidden', i != id );
			}

			var selected = this._.tabs[ id ];
			selected[ 0 ].addClass( 'cke_dialog_tab_selected' );

			// [IE] an invisible input[type='text'] will enlarge it's width
			// if it's value is long when it shows, so we clear it's value
			// before it shows and then recover it (#5649)
			if ( CKEDITOR.env.ie6Compat || CKEDITOR.env.ie7Compat )
			{
				clearOrRecoverTextInputValue( selected[ 1 ] );
				selected[ 1 ].show();
				setTimeout( function()
				{
					clearOrRecoverTextInputValue( selected[ 1 ], 1 );
				}, 0 );
			}
			else
				selected[ 1 ].show();

			this._.currentTabId = id;
			this._.currentTabIndex = CKEDITOR.tools.indexOf( this._.tabIdList, id );
		},

		// Dialog state-specific style updates.
		updateStyle : function()
		{
			// If only a single page shown, a different style is used in the central pane.
			this.parts.dialog[ ( this._.pageCount === 1 ? 'add' : 'remove' ) + 'Class' ]( 'cke_single_page' );
		},

		/**
		 * Hides a page's tab away from the dialog.
		 * @param {String} id The page's Id.
		 * @example
		 * dialog.hidePage( 'tab_3' );
		 */
		hidePage : function( id )
		{
			var tab = this._.tabs[id] && this._.tabs[id][0];
			if ( !tab || this._.pageCount == 1 || !tab.isVisible() )
				return;
			// Switch to other tab first when we're hiding the active tab.
			else if ( id == this._.currentTabId )
				this.selectPage( getPreviousVisibleTab.call( this ) );

			tab.hide();
			this._.pageCount--;
			this.updateStyle();
		},

		/**
		 * Unhides a page's tab.
		 * @param {String} id The page's Id.
		 * @example
		 * dialog.showPage( 'tab_2' );
		 */
		showPage : function( id )
		{
			var tab = this._.tabs[id] && this._.tabs[id][0];
			if ( !tab )
				return;
			tab.show();
			this._.pageCount++;
			this.updateStyle();
		},

		/**
		 * Gets the root DOM element of the dialog.
		 * @returns {CKEDITOR.dom.element} The &lt;span&gt; element containing this dialog.
		 * @example
		 * var dialogElement = dialogObj.getElement().getFirst();
		 * dialogElement.setStyle( 'padding', '5px' );
		 */
		getElement : function()
		{
			return this._.element;
		},

		/**
		 * Gets the name of the dialog.
		 * @returns {String} The name of this dialog.
		 * @example
		 * var dialogName = dialogObj.getName();
		 */
		getName : function()
		{
			return this._.name;
		},

		/**
		 * Gets a dialog UI element object from a dialog page.
		 * @param {String} pageId id of dialog page.
		 * @param {String} elementId id of UI element.
		 * @example
		 * dialogObj.getContentElement( 'tabId', 'elementId' ).setValue( 'Example' );
		 * @returns {CKEDITOR.ui.dialog.uiElement} The dialog UI element.
		 */
		getContentElement : function( pageId, elementId )
		{
			var page = this._.contents[ pageId ];
			return page && page[ elementId ];
		},

		/**
		 * Gets the value of a dialog UI element.
		 * @param {String} pageId id of dialog page.
		 * @param {String} elementId id of UI element.
		 * @example
		 * alert( dialogObj.getValueOf( 'tabId', 'elementId' ) );
		 * @returns {Object} The value of the UI element.
		 */
		getValueOf : function( pageId, elementId )
		{
			return this.getContentElement( pageId, elementId ).getValue();
		},

		/**
		 * Sets the value of a dialog UI element.
		 * @param {String} pageId id of the dialog page.
		 * @param {String} elementId id of the UI element.
		 * @param {Object} value The new value of the UI element.
		 * @example
		 * dialogObj.setValueOf( 'tabId', 'elementId', 'Example' );
		 */
		setValueOf : function( pageId, elementId, value )
		{
			return this.getContentElement( pageId, elementId ).setValue( value );
		},

		/**
		 * Gets the UI element of a button in the dialog's button row.
		 * @param {String} id The id of the button.
		 * @example
		 * @returns {CKEDITOR.ui.dialog.button} The button object.
		 */
		getButton : function( id )
		{
			return this._.buttons[ id ];
		},

		/**
		 * Simulates a click to a dialog button in the dialog's button row.
		 * @param {String} id The id of the button.
		 * @example
		 * @returns The return value of the dialog's "click" event.
		 */
		click : function( id )
		{
			return this._.buttons[ id ].click();
		},

		/**
		 * Disables a dialog button.
		 * @param {String} id The id of the button.
		 * @example
		 */
		disableButton : function( id )
		{
			return this._.buttons[ id ].disable();
		},

		/**
		 * Enables a dialog button.
		 * @param {String} id The id of the button.
		 * @example
		 */
		enableButton : function( id )
		{
			return this._.buttons[ id ].enable();
		},

		/**
		 * Gets the number of pages in the dialog.
		 * @returns {Number} Page count.
		 */
		getPageCount : function()
		{
			return this._.pageCount;
		},

		/**
		 * Gets the editor instance which opened this dialog.
		 * @returns {CKEDITOR.editor} Parent editor instances.
		 */
		getParentEditor : function()
		{
			return this._.editor;
		},

		/**
		 * Gets the element that was selected when opening the dialog, if any.
		 * @returns {CKEDITOR.dom.element} The element that was selected, or null.
		 */
		getSelectedElement : function()
		{
			return this.getParentEditor().getSelection().getSelectedElement();
		},

		/**
		 * Adds element to dialog's focusable list.
		 *
		 * @param {CKEDITOR.dom.element} element
		 * @param {Number} [index]
		 */
		addFocusable: function( element, index ) {
			if ( typeof index == 'undefined' )
			{
				index = this._.focusList.length;
				this._.focusList.push( new Focusable( this, element, index ) );
			}
			else
			{
				this._.focusList.splice( index, 0, new Focusable( this, element, index ) );
				for ( var i = index + 1 ; i < this._.focusList.length ; i++ )
					this._.focusList[ i ].focusIndex++;
			}
		}
	};

	CKEDITOR.tools.extend( CKEDITOR.dialog,
		/**
		 * @lends CKEDITOR.dialog
		 */
		{
			/**
			 * Registers a dialog.
			 * @param {String} name The dialog's name.
			 * @param {Function|String} dialogDefinition
			 * A function returning the dialog's definition, or the URL to the .js file holding the function.
			 * The function should accept an argument "editor" which is the current editor instance, and
			 * return an object conforming to {@link CKEDITOR.dialog.definition}.
			 * @see CKEDITOR.dialog.definition
			 * @example
			 * // Full sample plugin, which does not only register a dialog window but also adds an item to the context menu.
			 * // To open the dialog window, choose "Open dialog" in the context menu.
			 * CKEDITOR.plugins.add( 'myplugin',
			 * {
			 * 	init: function( editor )
			 * 	{
			 * 		editor.addCommand( 'mydialog',new CKEDITOR.dialogCommand( 'mydialog' ) );
			 *
			 * 		if ( editor.contextMenu )
			 * 		{
			 * 			editor.addMenuGroup( 'mygroup', 10 );
			 * 			editor.addMenuItem( 'My Dialog',
			 * 			{
			 * 				label : 'Open dialog',
			 * 				command : 'mydialog',
			 * 				group : 'mygroup'
			 * 			});
			 * 			editor.contextMenu.addListener( function( element )
			 * 			{
			 *  				return { 'My Dialog' : CKEDITOR.TRISTATE_OFF };
			 * 			});
			 * 		}
			 *
			 * 		<strong>CKEDITOR.dialog.add</strong>( 'mydialog', function( api )
			 * 		{
			 * 			// CKEDITOR.dialog.definition
			 * 			var <strong>dialogDefinition</strong> =
			 * 			{
			 * 				title : 'Sample dialog',
			 * 				minWidth : 390,
			 * 				minHeight : 130,
			 * 				contents : [
			 * 					{
			 * 						id : 'tab1',
			 * 						label : 'Label',
			 * 						title : 'Title',
			 * 						expand : true,
			 * 						padding : 0,
			 * 						elements :
			 * 						[
			 * 							{
			 * 								type : 'html',
			 * 								html : '&lt;p&gt;This is some sample HTML content.&lt;/p&gt;'
			 * 							},
			 * 							{
			 * 								type : 'textarea',
			 * 								id : 'textareaId',
			 * 								rows : 4,
			 * 								cols : 40
			 * 							}
			 * 						]
			 * 					}
			 * 				],
			 * 				buttons : [ CKEDITOR.dialog.okButton, CKEDITOR.dialog.cancelButton ],
			 * 				onOk : function() {
			 * 					// "this" is now a CKEDITOR.dialog object.
			 * 					// Accessing dialog elements:
			 * 					var textareaObj = this.<strong>getContentElement</strong>( 'tab1', 'textareaId' );
			 * 					alert( "You have entered: " + textareaObj.getValue() );
			 * 				}
			 * 			};
			 *
			 * 			return dialogDefinition;
			 * 		} );
			 * 	}
			 * } );
			 *
			 * CKEDITOR.replace( 'editor1', { extraPlugins : 'myplugin' } );
			 */
			add : function( name, dialogDefinition )
			{
				// Avoid path registration from multiple instances override definition.
				if ( !this._.dialogDefinitions[name]
					|| typeof  dialogDefinition == 'function' )
					this._.dialogDefinitions[name] = dialogDefinition;
			},

			exists : function( name )
			{
				return !!this._.dialogDefinitions[ name ];
			},

			getCurrent : function()
			{
				return CKEDITOR.dialog._.currentTop;
			},

			/**
			 * The default OK button for dialogs. Fires the "ok" event and closes the dialog if the event succeeds.
			 * @static
			 * @field
			 * @example
			 * @type Function
			 */
			okButton : (function()
			{
				var retval = function( editor, override )
				{
					override = override || {};
					return CKEDITOR.tools.extend( {
						id : 'ok',
						type : 'button',
						label : editor.lang.common.ok,
						'class' : 'cke_dialog_ui_button_ok',
						onClick : function( evt )
						{
							var dialog = evt.data.dialog;
							if ( dialog.fire( 'ok', { hide : true } ).hide !== false )
								dialog.hide();
						}
					}, override, true );
				};
				retval.type = 'button';
				retval.override = function( override )
				{
					return CKEDITOR.tools.extend( function( editor ){ return retval( editor, override ); },
							{ type : 'button' }, true );
				};
				return retval;
			})(),

			/**
			 * The default cancel button for dialogs. Fires the "cancel" event and closes the dialog if no UI element value changed.
			 * @static
			 * @field
			 * @example
			 * @type Function
			 */
			cancelButton : (function()
			{
				var retval = function( editor, override )
				{
					override = override || {};
					return CKEDITOR.tools.extend( {
						id : 'cancel',
						type : 'button',
						label : editor.lang.common.cancel,
						'class' : 'cke_dialog_ui_button_cancel',
						onClick : function( evt )
						{
							var dialog = evt.data.dialog;
							if ( dialog.fire( 'cancel', { hide : true } ).hide !== false )
								dialog.hide();
						}
					}, override, true );
				};
				retval.type = 'button';
				retval.override = function( override )
				{
					return CKEDITOR.tools.extend( function( editor ){ return retval( editor, override ); },
							{ type : 'button' }, true );
				};
				return retval;
			})(),

			/**
			 * Registers a dialog UI element.
			 * @param {String} typeName The name of the UI element.
			 * @param {Function} builder The function to build the UI element.
			 * @example
			 */
			addUIElement : function( typeName, builder )
			{
				this._.uiElementBuilders[ typeName ] = builder;
			}
		});

	CKEDITOR.dialog._ =
	{
		uiElementBuilders : {},

		dialogDefinitions : {},

		currentTop : null,

		currentZIndex : null
	};

	// "Inherit" (copy actually) from CKEDITOR.event.
	CKEDITOR.event.implementOn( CKEDITOR.dialog );
	CKEDITOR.event.implementOn( CKEDITOR.dialog.prototype, true );

	var defaultDialogDefinition =
	{
		resizable : CKEDITOR.DIALOG_RESIZE_BOTH,
		minWidth : 600,
		minHeight : 400,
		buttons : [ CKEDITOR.dialog.okButton, CKEDITOR.dialog.cancelButton ]
	};

	// Tool function used to return an item from an array based on its id
	// property.
	var getById = function( array, id, recurse )
	{
		for ( var i = 0, item ; ( item = array[ i ] ) ; i++ )
		{
			if ( item.id == id )
				return item;
			if ( recurse && item[ recurse ] )
			{
				var retval = getById( item[ recurse ], id, recurse ) ;
				if ( retval )
					return retval;
			}
		}
		return null;
	};

	// Tool function used to add an item into an array.
	var addById = function( array, newItem, nextSiblingId, recurse, nullIfNotFound )
	{
		if ( nextSiblingId )
		{
			for ( var i = 0, item ; ( item = array[ i ] ) ; i++ )
			{
				if ( item.id == nextSiblingId )
				{
					array.splice( i, 0, newItem );
					return newItem;
				}

				if ( recurse && item[ recurse ] )
				{
					var retval = addById( item[ recurse ], newItem, nextSiblingId, recurse, true );
					if ( retval )
						return retval;
				}
			}

			if ( nullIfNotFound )
				return null;
		}

		array.push( newItem );
		return newItem;
	};

	// Tool function used to remove an item from an array based on its id.
	var removeById = function( array, id, recurse )
	{
		for ( var i = 0, item ; ( item = array[ i ] ) ; i++ )
		{
			if ( item.id == id )
				return array.splice( i, 1 );
			if ( recurse && item[ recurse ] )
			{
				var retval = removeById( item[ recurse ], id, recurse );
				if ( retval )
					return retval;
			}
		}
		return null;
	};

	/**
	 * This class is not really part of the API. It is the "definition" property value
	 * passed to "dialogDefinition" event handlers.
	 * @constructor
	 * @name CKEDITOR.dialog.definitionObject
	 * @extends CKEDITOR.dialog.definition
	 * @example
	 * CKEDITOR.on( 'dialogDefinition', function( evt )
	 * 	{
	 * 		var definition = evt.data.definition;
	 * 		var content = definition.getContents( 'page1' );
	 * 		...
	 * 	} );
	 */
	var definitionObject = function( dialog, dialogDefinition )
	{
		// TODO : Check if needed.
		this.dialog = dialog;

		// Transform the contents entries in contentObjects.
		var contents = dialogDefinition.contents;
		for ( var i = 0, content ; ( content = contents[i] ) ; i++ )
			contents[ i ] = content && new contentObject( dialog, content );

		CKEDITOR.tools.extend( this, dialogDefinition );
	};

	definitionObject.prototype =
	/** @lends CKEDITOR.dialog.definitionObject.prototype */
	{
		/**
		 * Gets a content definition.
		 * @param {String} id The id of the content definition.
		 * @returns {CKEDITOR.dialog.definition.content} The content definition
		 *		matching id.
		 */
		getContents : function( id )
		{
			return getById( this.contents, id );
		},

		/**
		 * Gets a button definition.
		 * @param {String} id The id of the button definition.
		 * @returns {CKEDITOR.dialog.definition.button} The button definition
		 *		matching id.
		 */
		getButton : function( id )
		{
			return getById( this.buttons, id );
		},

		/**
		 * Adds a content definition object under this dialog definition.
		 * @param {CKEDITOR.dialog.definition.content} contentDefinition The
		 *		content definition.
		 * @param {String} [nextSiblingId] The id of an existing content
		 *		definition which the new content definition will be inserted
		 *		before. Omit if the new content definition is to be inserted as
		 *		the last item.
		 * @returns {CKEDITOR.dialog.definition.content} The inserted content
		 *		definition.
		 */
		addContents : function( contentDefinition, nextSiblingId )
		{
			return addById( this.contents, contentDefinition, nextSiblingId );
		},

		/**
		 * Adds a button definition object under this dialog definition.
		 * @param {CKEDITOR.dialog.definition.button} buttonDefinition The
		 *		button definition.
		 * @param {String} [nextSiblingId] The id of an existing button
		 *		definition which the new button definition will be inserted
		 *		before. Omit if the new button definition is to be inserted as
		 *		the last item.
		 * @returns {CKEDITOR.dialog.definition.button} The inserted button
		 *		definition.
		 */
		addButton : function( buttonDefinition, nextSiblingId )
		{
			return addById( this.buttons, buttonDefinition, nextSiblingId );
		},

		/**
		 * Removes a content definition from this dialog definition.
		 * @param {String} id The id of the content definition to be removed.
		 * @returns {CKEDITOR.dialog.definition.content} The removed content
		 *		definition.
		 */
		removeContents : function( id )
		{
			removeById( this.contents, id );
		},

		/**
		 * Removes a button definition from the dialog definition.
		 * @param {String} id The id of the button definition to be removed.
		 * @returns {CKEDITOR.dialog.definition.button} The removed button
		 *		definition.
		 */
		removeButton : function( id )
		{
			removeById( this.buttons, id );
		}
	};

	/**
	 * This class is not really part of the API. It is the template of the
	 * objects representing content pages inside the
	 * CKEDITOR.dialog.definitionObject.
	 * @constructor
	 * @name CKEDITOR.dialog.definition.contentObject
	 * @example
	 * CKEDITOR.on( 'dialogDefinition', function( evt )
	 * 	{
	 * 		var definition = evt.data.definition;
	 * 		var content = definition.getContents( 'page1' );
	 *		content.remove( 'textInput1' );
	 * 		...
	 * 	} );
	 */
	function contentObject( dialog, contentDefinition )
	{
		this._ =
		{
			dialog : dialog
		};

		CKEDITOR.tools.extend( this, contentDefinition );
	}

	contentObject.prototype =
	/** @lends CKEDITOR.dialog.definition.contentObject.prototype */
	{
		/**
		 * Gets a UI element definition under the content definition.
		 * @param {String} id The id of the UI element definition.
		 * @returns {CKEDITOR.dialog.definition.uiElement}
		 */
		get : function( id )
		{
			return getById( this.elements, id, 'children' );
		},

		/**
		 * Adds a UI element definition to the content definition.
		 * @param {CKEDITOR.dialog.definition.uiElement} elementDefinition The
		 *		UI elemnet definition to be added.
		 * @param {String} nextSiblingId The id of an existing UI element
		 *		definition which the new UI element definition will be inserted
		 *		before. Omit if the new button definition is to be inserted as
		 *		the last item.
		 * @returns {CKEDITOR.dialog.definition.uiElement} The element
		 *		definition inserted.
		 */
		add : function( elementDefinition, nextSiblingId )
		{
			return addById( this.elements, elementDefinition, nextSiblingId, 'children' );
		},

		/**
		 * Removes a UI element definition from the content definition.
		 * @param {String} id The id of the UI element definition to be
		 *		removed.
		 * @returns {CKEDITOR.dialog.definition.uiElement} The element
		 *		definition removed.
		 * @example
		 */
		remove : function( id )
		{
			removeById( this.elements, id, 'children' );
		}
	};

	function initDragAndDrop( dialog )
	{
		var lastCoords = null,
			abstractDialogCoords = null,
			element = dialog.getElement().getFirst(),
			editor = dialog.getParentEditor(),
			magnetDistance = editor.config.dialog_magnetDistance,
			margins = editor.skin.margins || [ 0, 0, 0, 0 ];

		if ( typeof magnetDistance == 'undefined' )
			magnetDistance = 20;

		function mouseMoveHandler( evt )
		{
			var dialogSize = dialog.getSize(),
				viewPaneSize = CKEDITOR.document.getWindow().getViewPaneSize(),
				x = evt.data.$.screenX,
				y = evt.data.$.screenY,
				dx = x - lastCoords.x,
				dy = y - lastCoords.y,
				realX, realY;

			lastCoords = { x : x, y : y };
			abstractDialogCoords.x += dx;
			abstractDialogCoords.y += dy;

			if ( abstractDialogCoords.x + margins[3] < magnetDistance )
				realX = - margins[3];
			else if ( abstractDialogCoords.x - margins[1] > viewPaneSize.width - dialogSize.width - magnetDistance )
				realX = viewPaneSize.width - dialogSize.width + ( editor.lang.dir == 'rtl' ? 0 : margins[1] );
			else
				realX = abstractDialogCoords.x;

			if ( abstractDialogCoords.y + margins[0] < magnetDistance )
				realY = - margins[0];
			else if ( abstractDialogCoords.y - margins[2] > viewPaneSize.height - dialogSize.height - magnetDistance )
				realY = viewPaneSize.height - dialogSize.height + margins[2];
			else
				realY = abstractDialogCoords.y;

			dialog.move( realX, realY, 1 );

			evt.data.preventDefault();
		}

		function mouseUpHandler( evt )
		{
			CKEDITOR.document.removeListener( 'mousemove', mouseMoveHandler );
			CKEDITOR.document.removeListener( 'mouseup', mouseUpHandler );

			if ( CKEDITOR.env.ie6Compat )
			{
				var coverDoc = currentCover.getChild( 0 ).getFrameDocument();
				coverDoc.removeListener( 'mousemove', mouseMoveHandler );
				coverDoc.removeListener( 'mouseup', mouseUpHandler );
			}
		}

		dialog.parts.title.on( 'mousedown', function( evt )
			{
				lastCoords = { x : evt.data.$.screenX, y : evt.data.$.screenY };

				CKEDITOR.document.on( 'mousemove', mouseMoveHandler );
				CKEDITOR.document.on( 'mouseup', mouseUpHandler );
				abstractDialogCoords = dialog.getPosition();

				if ( CKEDITOR.env.ie6Compat )
				{
					var coverDoc = currentCover.getChild( 0 ).getFrameDocument();
					coverDoc.on( 'mousemove', mouseMoveHandler );
					coverDoc.on( 'mouseup', mouseUpHandler );
				}

				evt.data.preventDefault();
			}, dialog );
	}

	function initResizeHandles( dialog )
	{
		var def = dialog.definition,
			resizable = def.resizable;

		if ( resizable == CKEDITOR.DIALOG_RESIZE_NONE )
			return;

		var editor = dialog.getParentEditor();
		var wrapperWidth, wrapperHeight,
				viewSize, origin, startSize,
				dialogCover;

		var mouseDownFn = CKEDITOR.tools.addFunction( function( $event )
		{
			startSize = dialog.getSize();

			var content = dialog.parts.contents,
				iframeDialog = content.$.getElementsByTagName( 'iframe' ).length;

			// Shim to help capturing "mousemove" over iframe.
			if ( iframeDialog )
			{
				dialogCover = CKEDITOR.dom.element.createFromHtml( '<div class="cke_dialog_resize_cover" style="height: 100%; position: absolute; width: 100%;"></div>' );
				content.append( dialogCover );
			}

			// Calculate the offset between content and chrome size.
			wrapperHeight = startSize.height - dialog.parts.contents.getSize( 'height',  ! ( CKEDITOR.env.gecko || CKEDITOR.env.opera || CKEDITOR.env.ie && CKEDITOR.env.quirks ) );
			wrapperWidth = startSize.width - dialog.parts.contents.getSize( 'width', 1 );

			origin = { x : $event.screenX, y : $event.screenY };

			viewSize = CKEDITOR.document.getWindow().getViewPaneSize();

			CKEDITOR.document.on( 'mousemove', mouseMoveHandler );
			CKEDITOR.document.on( 'mouseup', mouseUpHandler );

			if ( CKEDITOR.env.ie6Compat )
			{
				var coverDoc = currentCover.getChild( 0 ).getFrameDocument();
				coverDoc.on( 'mousemove', mouseMoveHandler );
				coverDoc.on( 'mouseup', mouseUpHandler );
			}

			$event.preventDefault && $event.preventDefault();
		});

		// Prepend the grip to the dialog.
		dialog.on( 'load', function()
		{
			var direction = '';
			if ( resizable == CKEDITOR.DIALOG_RESIZE_WIDTH )
				direction = ' cke_resizer_horizontal';
			else if ( resizable == CKEDITOR.DIALOG_RESIZE_HEIGHT )
				direction = ' cke_resizer_vertical';
			var resizer = CKEDITOR.dom.element.createFromHtml( '<div' +
					' class="cke_resizer' + direction + ' cke_resizer_' + editor.lang.dir + '"' +
					' title="' + CKEDITOR.tools.htmlEncode( editor.lang.resize ) + '"' +
					' onmousedown="CKEDITOR.tools.callFunction(' + mouseDownFn + ', event )"></div>' );
			dialog.parts.footer.append( resizer, 1 );
		});
		editor.on( 'destroy', function() { CKEDITOR.tools.removeFunction( mouseDownFn ); } );

		function mouseMoveHandler( evt )
		{
			var rtl = editor.lang.dir == 'rtl',
				dx = ( evt.data.$.screenX - origin.x ) * ( rtl ? -1 : 1 ),
				dy = evt.data.$.screenY - origin.y,
				width = startSize.width,
				height = startSize.height,
				internalWidth = width + dx * ( dialog._.moved ? 1 : 2 ),
				internalHeight = height + dy * ( dialog._.moved ? 1 : 2 ),
				element = dialog._.element.getFirst(),
				right = rtl && element.getComputedStyle( 'right' ),
				position = dialog.getPosition();

			if ( position.y + internalHeight > viewSize.height )
				internalHeight = viewSize.height - position.y;

			if ( ( rtl ? right : position.x ) + internalWidth > viewSize.width )
				internalWidth = viewSize.width - ( rtl ? right : position.x );

			// Make sure the dialog will not be resized to the wrong side when it's in the leftmost position for RTL.
			if ( ( resizable == CKEDITOR.DIALOG_RESIZE_WIDTH || resizable == CKEDITOR.DIALOG_RESIZE_BOTH ) )
				width = Math.max( def.minWidth || 0, internalWidth - wrapperWidth );

			if ( resizable == CKEDITOR.DIALOG_RESIZE_HEIGHT || resizable == CKEDITOR.DIALOG_RESIZE_BOTH )
				height = Math.max( def.minHeight || 0, internalHeight - wrapperHeight );

			dialog.resize( width, height );

			if ( !dialog._.moved )
				dialog.layout();

			evt.data.preventDefault();
		}

		function mouseUpHandler()
		{
			CKEDITOR.document.removeListener( 'mouseup', mouseUpHandler );
			CKEDITOR.document.removeListener( 'mousemove', mouseMoveHandler );

			if ( dialogCover )
			{
				dialogCover.remove();
				dialogCover = null;
			}

			if ( CKEDITOR.env.ie6Compat )
			{
				var coverDoc = currentCover.getChild( 0 ).getFrameDocument();
				coverDoc.removeListener( 'mouseup', mouseUpHandler );
				coverDoc.removeListener( 'mousemove', mouseMoveHandler );
			}
		}
	}

	var resizeCover;
	// Caching resuable covers and allowing only one cover
	// on screen.
	var covers = {},
		currentCover;

	function cancelEvent( ev )
	{
		ev.data.preventDefault(1);
	}

	function showCover( editor )
	{
		var win = CKEDITOR.document.getWindow();
		var config = editor.config,
			backgroundColorStyle = config.dialog_backgroundCoverColor || 'white',
			backgroundCoverOpacity = config.dialog_backgroundCoverOpacity,
			baseFloatZIndex = config.baseFloatZIndex,
			coverKey = CKEDITOR.tools.genKey(
					backgroundColorStyle,
					backgroundCoverOpacity,
					baseFloatZIndex ),
			coverElement = covers[ coverKey ];

		if ( !coverElement )
		{
			var html = [
					'<div tabIndex="-1" style="position: ', ( CKEDITOR.env.ie6Compat ? 'absolute' : 'fixed' ),
					'; z-index: ', baseFloatZIndex,
					'; top: 0px; left: 0px; ',
					( !CKEDITOR.env.ie6Compat ? 'background-color: ' + backgroundColorStyle : '' ),
					'" class="cke_dialog_background_cover">'
				];

			if ( CKEDITOR.env.ie6Compat )
			{
				// Support for custom document.domain in IE.
				var isCustomDomain = CKEDITOR.env.isCustomDomain(),
					iframeHtml = '<html><body style=\\\'background-color:' + backgroundColorStyle + ';\\\'></body></html>';

				html.push(
					'<iframe' +
						' hidefocus="true"' +
						' frameborder="0"' +
						' id="cke_dialog_background_iframe"' +
						' src="javascript:' );

				html.push( 'void((function(){' +
								'document.open();' +
								( isCustomDomain ? 'document.domain=\'' + document.domain + '\';' : '' ) +
								'document.write( \'' + iframeHtml + '\' );' +
								'document.close();' +
							'})())' );

				html.push(
						'"' +
						' style="' +
							'position:absolute;' +
							'left:0;' +
							'top:0;' +
							'width:100%;' +
							'height: 100%;' +
							'progid:DXImageTransform.Microsoft.Alpha(opacity=0)">' +
					'</iframe>' );
			}

			html.push( '</div>' );

			coverElement = CKEDITOR.dom.element.createFromHtml( html.join( '' ) );
			coverElement.setOpacity( backgroundCoverOpacity != undefined ? backgroundCoverOpacity : 0.5 );

			coverElement.on( 'keydown', cancelEvent );
			coverElement.on( 'keypress', cancelEvent );
			coverElement.on( 'keyup', cancelEvent );

			coverElement.appendTo( CKEDITOR.document.getBody() );
			covers[ coverKey ] = coverElement;
		}
		else
			coverElement.	show();

		currentCover = coverElement;
		var resizeFunc = function()
		{
			var size = win.getViewPaneSize();
			coverElement.setStyles(
				{
					width : size.width + 'px',
					height : size.height + 'px'
				} );
		};

		var scrollFunc = function()
		{
			var pos = win.getScrollPosition(),
				cursor = CKEDITOR.dialog._.currentTop;
			coverElement.setStyles(
					{
						left : pos.x + 'px',
						top : pos.y + 'px'
					});

			if ( cursor )
			{
				do
				{
					var dialogPos = cursor.getPosition();
					cursor.move( dialogPos.x, dialogPos.y );
				} while ( ( cursor = cursor._.parentDialog ) );
			}
		};

		resizeCover = resizeFunc;
		win.on( 'resize', resizeFunc );
		resizeFunc();
		// Using Safari/Mac, focus must be kept where it is (#7027)
		if ( !( CKEDITOR.env.mac && CKEDITOR.env.webkit ) )
			coverElement.focus();

		if ( CKEDITOR.env.ie6Compat )
		{
			// IE BUG: win.$.onscroll assignment doesn't work.. it must be window.onscroll.
			// So we need to invent a really funny way to make it work.
			var myScrollHandler = function()
				{
					scrollFunc();
					arguments.callee.prevScrollHandler.apply( this, arguments );
				};
			win.$.setTimeout( function()
				{
					myScrollHandler.prevScrollHandler = window.onscroll || function(){};
					window.onscroll = myScrollHandler;
				}, 0 );
			scrollFunc();
		}
	}

	function hideCover()
	{
		if ( !currentCover )
			return;

		var win = CKEDITOR.document.getWindow();
		currentCover.hide();
		win.removeListener( 'resize', resizeCover );

		if ( CKEDITOR.env.ie6Compat )
		{
			win.$.setTimeout( function()
				{
					var prevScrollHandler = window.onscroll && window.onscroll.prevScrollHandler;
					window.onscroll = prevScrollHandler || null;
				}, 0 );
		}
		resizeCover = null;
	}

	function removeCovers()
	{
		for ( var coverId in covers )
			covers[ coverId ].remove();
		covers = {};
	}

	var accessKeyProcessors = {};

	var accessKeyDownHandler = function( evt )
	{
		var ctrl = evt.data.$.ctrlKey || evt.data.$.metaKey,
			alt = evt.data.$.altKey,
			shift = evt.data.$.shiftKey,
			key = String.fromCharCode( evt.data.$.keyCode ),
			keyProcessor = accessKeyProcessors[( ctrl ? 'CTRL+' : '' ) + ( alt ? 'ALT+' : '') + ( shift ? 'SHIFT+' : '' ) + key];

		if ( !keyProcessor || !keyProcessor.length )
			return;

		keyProcessor = keyProcessor[keyProcessor.length - 1];
		keyProcessor.keydown && keyProcessor.keydown.call( keyProcessor.uiElement, keyProcessor.dialog, keyProcessor.key );
		evt.data.preventDefault();
	};

	var accessKeyUpHandler = function( evt )
	{
		var ctrl = evt.data.$.ctrlKey || evt.data.$.metaKey,
			alt = evt.data.$.altKey,
			shift = evt.data.$.shiftKey,
			key = String.fromCharCode( evt.data.$.keyCode ),
			keyProcessor = accessKeyProcessors[( ctrl ? 'CTRL+' : '' ) + ( alt ? 'ALT+' : '') + ( shift ? 'SHIFT+' : '' ) + key];

		if ( !keyProcessor || !keyProcessor.length )
			return;

		keyProcessor = keyProcessor[keyProcessor.length - 1];
		if ( keyProcessor.keyup )
		{
			keyProcessor.keyup.call( keyProcessor.uiElement, keyProcessor.dialog, keyProcessor.key );
			evt.data.preventDefault();
		}
	};

	var registerAccessKey = function( uiElement, dialog, key, downFunc, upFunc )
	{
		var procList = accessKeyProcessors[key] || ( accessKeyProcessors[key] = [] );
		procList.push( {
				uiElement : uiElement,
				dialog : dialog,
				key : key,
				keyup : upFunc || uiElement.accessKeyUp,
				keydown : downFunc || uiElement.accessKeyDown
			} );
	};

	var unregisterAccessKey = function( obj )
	{
		for ( var i in accessKeyProcessors )
		{
			var list = accessKeyProcessors[i];
			for ( var j = list.length - 1 ; j >= 0 ; j-- )
			{
				if ( list[j].dialog == obj || list[j].uiElement == obj )
					list.splice( j, 1 );
			}
			if ( list.length === 0 )
				delete accessKeyProcessors[i];
		}
	};

	var tabAccessKeyUp = function( dialog, key )
	{
		if ( dialog._.accessKeyMap[key] )
			dialog.selectPage( dialog._.accessKeyMap[key] );
	};

	var tabAccessKeyDown = function( dialog, key )
	{
	};

	(function()
	{
		CKEDITOR.ui.dialog =
		{
			/**
			 * The base class of all dialog UI elements.
			 * @constructor
			 * @param {CKEDITOR.dialog} dialog Parent dialog object.
			 * @param {CKEDITOR.dialog.definition.uiElement} elementDefinition Element
			 * definition. Accepted fields:
			 * <ul>
			 * 	<li><strong>id</strong> (Required) The id of the UI element. See {@link
			 * 	CKEDITOR.dialog#getContentElement}</li>
			 * 	<li><strong>type</strong> (Required) The type of the UI element. The
			 * 	value to this field specifies which UI element class will be used to
			 * 	generate the final widget.</li>
			 * 	<li><strong>title</strong> (Optional) The popup tooltip for the UI
			 * 	element.</li>
			 * 	<li><strong>hidden</strong> (Optional) A flag that tells if the element
			 * 	should be initially visible.</li>
			 * 	<li><strong>className</strong> (Optional) Additional CSS class names
			 * 	to add to the UI element. Separated by space.</li>
			 * 	<li><strong>style</strong> (Optional) Additional CSS inline styles
			 * 	to add to the UI element. A semicolon (;) is required after the last
			 * 	style declaration.</li>
			 * 	<li><strong>accessKey</strong> (Optional) The alphanumeric access key
			 * 	for this element. Access keys are automatically prefixed by CTRL.</li>
			 * 	<li><strong>on*</strong> (Optional) Any UI element definition field that
			 * 	starts with <em>on</em> followed immediately by a capital letter and
			 * 	probably more letters is an event handler. Event handlers may be further
			 * 	divided into registered event handlers and DOM event handlers. Please
			 * 	refer to {@link CKEDITOR.ui.dialog.uiElement#registerEvents} and
			 * 	{@link CKEDITOR.ui.dialog.uiElement#eventProcessors} for more
			 * 	information.</li>
			 * </ul>
			 * @param {Array} htmlList
			 * List of HTML code to be added to the dialog's content area.
			 * @param {Function|String} nodeNameArg
			 * A function returning a string, or a simple string for the node name for
			 * the root DOM node. Default is 'div'.
			 * @param {Function|Object} stylesArg
			 * A function returning an object, or a simple object for CSS styles applied
			 * to the DOM node. Default is empty object.
			 * @param {Function|Object} attributesArg
			 * A fucntion returning an object, or a simple object for attributes applied
			 * to the DOM node. Default is empty object.
			 * @param {Function|String} contentsArg
			 * A function returning a string, or a simple string for the HTML code inside
			 * the root DOM node. Default is empty string.
			 * @example
			 */
			uiElement : function( dialog, elementDefinition, htmlList, nodeNameArg, stylesArg, attributesArg, contentsArg )
			{
				if ( arguments.length < 4 )
					return;

				var nodeName = ( nodeNameArg.call ? nodeNameArg( elementDefinition ) : nodeNameArg ) || 'div',
					html = [ '<', nodeName, ' ' ],
					styles = ( stylesArg && stylesArg.call ? stylesArg( elementDefinition ) : stylesArg ) || {},
					attributes = ( attributesArg && attributesArg.call ? attributesArg( elementDefinition ) : attributesArg ) || {},
					innerHTML = ( contentsArg && contentsArg.call ? contentsArg.call( this, dialog, elementDefinition ) : contentsArg ) || '',
					domId = this.domId = attributes.id || CKEDITOR.tools.getNextId() + '_uiElement',
					id = this.id = elementDefinition.id,
					i;

				// Set the id, a unique id is required for getElement() to work.
				attributes.id = domId;

				// Set the type and definition CSS class names.
				var classes = {};
				if ( elementDefinition.type )
					classes[ 'cke_dialog_ui_' + elementDefinition.type ] = 1;
				if ( elementDefinition.className )
					classes[ elementDefinition.className ] = 1;
				if ( elementDefinition.disabled )
					classes[ 'cke_disabled' ] = 1;

				var attributeClasses = ( attributes['class'] && attributes['class'].split ) ? attributes['class'].split( ' ' ) : [];
				for ( i = 0 ; i < attributeClasses.length ; i++ )
				{
					if ( attributeClasses[i] )
						classes[ attributeClasses[i] ] = 1;
				}
				var finalClasses = [];
				for ( i in classes )
					finalClasses.push( i );
				attributes['class'] = finalClasses.join( ' ' );

				// Set the popup tooltop.
				if ( elementDefinition.title )
					attributes.title = elementDefinition.title;

				// Write the inline CSS styles.
				var styleStr = ( elementDefinition.style || '' ).split( ';' );

				// Element alignment support.
				if ( elementDefinition.align )
				{
					var align = elementDefinition.align;
					styles[ 'margin-left' ] = align == 'left' ? 0 : 'auto';
					styles[ 'margin-right' ] = align == 'right' ? 0 : 'auto';
				}

				for ( i in styles )
					styleStr.push( i + ':' + styles[i] );
				if ( elementDefinition.hidden )
					styleStr.push( 'display:none' );
				for ( i = styleStr.length - 1 ; i >= 0 ; i-- )
				{
					if ( styleStr[i] === '' )
						styleStr.splice( i, 1 );
				}
				if ( styleStr.length > 0 )
					attributes.style = ( attributes.style ? ( attributes.style + '; ' ) : '' ) + styleStr.join( '; ' );

				// Write the attributes.
				for ( i in attributes )
					html.push( i + '="' + CKEDITOR.tools.htmlEncode( attributes[i] ) + '" ');

				// Write the content HTML.
				html.push( '>', innerHTML, '</', nodeName, '>' );

				// Add contents to the parent HTML array.
				htmlList.push( html.join( '' ) );

				( this._ || ( this._ = {} ) ).dialog = dialog;

				// Override isChanged if it is defined in element definition.
				if ( typeof( elementDefinition.isChanged ) == 'boolean' )
					this.isChanged = function(){ return elementDefinition.isChanged; };
				if ( typeof( elementDefinition.isChanged ) == 'function' )
					this.isChanged = elementDefinition.isChanged;

				// Overload 'get(set)Value' on definition.
				if ( typeof( elementDefinition.setValue ) == 'function' )
				{
						this.setValue = CKEDITOR.tools.override( this.setValue, function( org )
						{
								return function( val ){ org.call( this, elementDefinition.setValue.call( this, val ) ); };
						} );
				}

				if ( typeof( elementDefinition.getValue ) == 'function' )
				{
						this.getValue = CKEDITOR.tools.override( this.getValue, function( org )
						{
								return function(){ return  elementDefinition.getValue.call( this, org.call( this ) ); };
						} );
				}

				// Add events.
				CKEDITOR.event.implementOn( this );

				this.registerEvents( elementDefinition );
				if ( this.accessKeyUp && this.accessKeyDown && elementDefinition.accessKey )
					registerAccessKey( this, dialog, 'CTRL+' + elementDefinition.accessKey );

				var me = this;
				dialog.on( 'load', function()
					{
						var input = me.getInputElement();
						if ( input )
						{
							var focusClass = me.type in { 'checkbox' : 1, 'ratio' : 1 } && CKEDITOR.env.ie && CKEDITOR.env.version < 8 ? 'cke_dialog_ui_focused' : '';
							input.on( 'focus', function()
								{
									dialog._.tabBarMode = false;
									dialog._.hasFocus = true;
									me.fire( 'focus' );
									focusClass && this.addClass( focusClass );

								});

							input.on( 'blur', function()
								{
									me.fire( 'blur' );
									focusClass && this.removeClass( focusClass );
								});
						}
					} );

				// Register the object as a tab focus if it can be included.
				if ( this.keyboardFocusable )
				{
					this.tabIndex = elementDefinition.tabIndex || 0;

					this.focusIndex = dialog._.focusList.push( this ) - 1;
					this.on( 'focus', function()
						{
							dialog._.currentFocusIndex = me.focusIndex;
						} );
				}

				// Completes this object with everything we have in the
				// definition.
				CKEDITOR.tools.extend( this, elementDefinition );
			},

			/**
			 * Horizontal layout box for dialog UI elements, auto-expends to available width of container.
			 * @constructor
			 * @extends CKEDITOR.ui.dialog.uiElement
			 * @param {CKEDITOR.dialog} dialog
			 * Parent dialog object.
			 * @param {Array} childObjList
			 * Array of {@link CKEDITOR.ui.dialog.uiElement} objects inside this
			 * container.
			 * @param {Array} childHtmlList
			 * Array of HTML code that correspond to the HTML output of all the
			 * objects in childObjList.
			 * @param {Array} htmlList
			 * Array of HTML code that this element will output to.
			 * @param {CKEDITOR.dialog.definition.uiElement} elementDefinition
			 * The element definition. Accepted fields:
			 * <ul>
			 * 	<li><strong>widths</strong> (Optional) The widths of child cells.</li>
			 * 	<li><strong>height</strong> (Optional) The height of the layout.</li>
			 * 	<li><strong>padding</strong> (Optional) The padding width inside child
			 * 	 cells.</li>
			 * 	<li><strong>align</strong> (Optional) The alignment of the whole layout
			 * 	</li>
			 * </ul>
			 * @example
			 */
			hbox : function( dialog, childObjList, childHtmlList, htmlList, elementDefinition )
			{
				if ( arguments.length < 4 )
					return;

				this._ || ( this._ = {} );

				var children = this._.children = childObjList,
					widths = elementDefinition && elementDefinition.widths || null,
					height = elementDefinition && elementDefinition.height || null,
					styles = {},
					i;
				/** @ignore */
				var innerHTML = function()
				{
					var html = [ '<tbody><tr class="cke_dialog_ui_hbox">' ];
					for ( i = 0 ; i < childHtmlList.length ; i++ )
					{
						var className = 'cke_dialog_ui_hbox_child',
							styles = [];
						if ( i === 0 )
							className = 'cke_dialog_ui_hbox_first';
						if ( i == childHtmlList.length - 1 )
							className = 'cke_dialog_ui_hbox_last';
						html.push( '<td class="', className, '" role="presentation" ' );
						if ( widths )
						{
							if ( widths[i] )
								styles.push( 'width:' + cssLength( widths[i] ) );
						}
						else
							styles.push( 'width:' + Math.floor( 100 / childHtmlList.length ) + '%' );
						if ( height )
							styles.push( 'height:' + cssLength( height ) );
						if ( elementDefinition && elementDefinition.padding != undefined )
							styles.push( 'padding:' + cssLength( elementDefinition.padding ) );
						// In IE Quirks alignment has to be done on table cells. (#7324)
						if ( CKEDITOR.env.ie && CKEDITOR.env.quirks && children[ i ].align )
							styles.push( 'text-align:' + children[ i ].align );
						if ( styles.length > 0 )
							html.push( 'style="' + styles.join('; ') + '" ' );
						html.push( '>', childHtmlList[i], '</td>' );
					}
					html.push( '</tr></tbody>' );
					return html.join( '' );
				};

				var attribs = { role : 'presentation' };
				elementDefinition && elementDefinition.align && ( attribs.align = elementDefinition.align );

				CKEDITOR.ui.dialog.uiElement.call(
					this,
					dialog,
					elementDefinition || { type : 'hbox' },
					htmlList,
					'table',
					styles,
					attribs,
					innerHTML );
			},

			/**
			 * Vertical layout box for dialog UI elements.
			 * @constructor
			 * @extends CKEDITOR.ui.dialog.hbox
			 * @param {CKEDITOR.dialog} dialog
			 * Parent dialog object.
			 * @param {Array} childObjList
			 * Array of {@link CKEDITOR.ui.dialog.uiElement} objects inside this
			 * container.
			 * @param {Array} childHtmlList
			 * Array of HTML code that correspond to the HTML output of all the
			 * objects in childObjList.
			 * @param {Array} htmlList
			 * Array of HTML code that this element will output to.
			 * @param {CKEDITOR.dialog.definition.uiElement} elementDefinition
			 * The element definition. Accepted fields:
			 * <ul>
			 * 	<li><strong>width</strong> (Optional) The width of the layout.</li>
			 * 	<li><strong>heights</strong> (Optional) The heights of individual cells.
			 * 	</li>
			 * 	<li><strong>align</strong> (Optional) The alignment of the layout.</li>
			 * 	<li><strong>padding</strong> (Optional) The padding width inside child
			 * 	cells.</li>
			 * 	<li><strong>expand</strong> (Optional) Whether the layout should expand
			 * 	vertically to fill its container.</li>
			 * </ul>
			 * @example
			 */
			vbox : function( dialog, childObjList, childHtmlList, htmlList, elementDefinition )
			{
				if ( arguments.length < 3 )
					return;

				this._ || ( this._ = {} );

				var children = this._.children = childObjList,
					width = elementDefinition && elementDefinition.width || null,
					heights = elementDefinition && elementDefinition.heights || null;
				/** @ignore */
				var innerHTML = function()
				{
					var html = [ '<table role="presentation" cellspacing="0" border="0" ' ];
					html.push( 'style="' );
					if ( elementDefinition && elementDefinition.expand )
						html.push( 'height:100%;' );
					html.push( 'width:' + cssLength( width || '100%' ), ';' );
					html.push( '"' );
					html.push( 'align="', CKEDITOR.tools.htmlEncode(
						( elementDefinition && elementDefinition.align ) || ( dialog.getParentEditor().lang.dir == 'ltr' ? 'left' : 'right' ) ), '" ' );

					html.push( '><tbody>' );
					for ( var i = 0 ; i < childHtmlList.length ; i++ )
					{
						var styles = [];
						html.push( '<tr><td role="presentation" ' );
						if ( width )
							styles.push( 'width:' + cssLength( width || '100%' ) );
						if ( heights )
							styles.push( 'height:' + cssLength( heights[i] ) );
						else if ( elementDefinition && elementDefinition.expand )
							styles.push( 'height:' + Math.floor( 100 / childHtmlList.length ) + '%' );
						if ( elementDefinition && elementDefinition.padding != undefined )
							styles.push( 'padding:' + cssLength( elementDefinition.padding ) );
						// In IE Quirks alignment has to be done on table cells. (#7324)
						if ( CKEDITOR.env.ie && CKEDITOR.env.quirks && children[ i ].align )
							styles.push( 'text-align:' + children[ i ].align );
						if ( styles.length > 0 )
							html.push( 'style="', styles.join( '; ' ), '" ' );
						html.push( ' class="cke_dialog_ui_vbox_child">', childHtmlList[i], '</td></tr>' );
					}
					html.push( '</tbody></table>' );
					return html.join( '' );
				};
				CKEDITOR.ui.dialog.uiElement.call( this, dialog, elementDefinition || { type : 'vbox' }, htmlList, 'div', null, { role : 'presentation' }, innerHTML );
			}
		};
	})();

	CKEDITOR.ui.dialog.uiElement.prototype =
	{
		/**
		 * Gets the root DOM element of this dialog UI object.
		 * @returns {CKEDITOR.dom.element} Root DOM element of UI object.
		 * @example
		 * uiElement.getElement().hide();
		 */
		getElement : function()
		{
			return CKEDITOR.document.getById( this.domId );
		},

		/**
		 * Gets the DOM element that the user inputs values.
		 * This function is used by setValue(), getValue() and focus(). It should
		 * be overrided in child classes where the input element isn't the root
		 * element.
		 * @returns {CKEDITOR.dom.element} The element where the user input values.
		 * @example
		 * var rawValue = textInput.getInputElement().$.value;
		 */
		getInputElement : function()
		{
			return this.getElement();
		},

		/**
		 * Gets the parent dialog object containing this UI element.
		 * @returns {CKEDITOR.dialog} Parent dialog object.
		 * @example
		 * var dialog = uiElement.getDialog();
		 */
		getDialog : function()
		{
			return this._.dialog;
		},

		/**
		 * Sets the value of this dialog UI object.
		 * @param {Object} value The new value.
		 * @param {Boolean} noChangeEvent Internal commit, to supress 'change' event on this element.
		 * @returns {CKEDITOR.dialog.uiElement} The current UI element.
		 * @example
		 * uiElement.setValue( 'Dingo' );
		 */
		setValue : function( value, noChangeEvent )
		{
			this.getInputElement().setValue( value );
			!noChangeEvent && this.fire( 'change', { value : value } );
			return this;
		},

		/**
		 * Gets the current value of this dialog UI object.
		 * @returns {Object} The current value.
		 * @example
		 * var myValue = uiElement.getValue();
		 */
		getValue : function()
		{
			return this.getInputElement().getValue();
		},

		/**
		 * Tells whether the UI object's value has changed.
		 * @returns {Boolean} true if changed, false if not changed.
		 * @example
		 * if ( uiElement.isChanged() )
		 * &nbsp;&nbsp;confirm( 'Value changed! Continue?' );
		 */
		isChanged : function()
		{
			// Override in input classes.
			return false;
		},

		/**
		 * Selects the parent tab of this element. Usually called by focus() or overridden focus() methods.
		 * @returns {CKEDITOR.dialog.uiElement} The current UI element.
		 * @example
		 * focus : function()
		 * {
		 * 		this.selectParentTab();
		 * 		// do something else.
		 * }
		 */
		selectParentTab : function()
		{
			var element = this.getInputElement(),
				cursor = element,
				tabId;
			while ( ( cursor = cursor.getParent() ) && cursor.$.className.search( 'cke_dialog_page_contents' ) == -1 )
			{ /*jsl:pass*/ }

			// Some widgets don't have parent tabs (e.g. OK and Cancel buttons).
			if ( !cursor )
				return this;

			tabId = cursor.getAttribute( 'name' );
			// Avoid duplicate select.
			if ( this._.dialog._.currentTabId != tabId )
				this._.dialog.selectPage( tabId );
			return this;
		},

		/**
		 * Puts the focus to the UI object. Switches tabs if the UI object isn't in the active tab page.
		 * @returns {CKEDITOR.dialog.uiElement} The current UI element.
		 * @example
		 * uiElement.focus();
		 */
		focus : function()
		{
			this.selectParentTab().getInputElement().focus();
			return this;
		},

		/**
		 * Registers the on* event handlers defined in the element definition.
		 * The default behavior of this function is:
		 * <ol>
		 *  <li>
		 *  	If the on* event is defined in the class's eventProcesors list,
		 *  	then the registration is delegated to the corresponding function
		 *  	in the eventProcessors list.
		 *  </li>
		 *  <li>
		 *  	If the on* event is not defined in the eventProcessors list, then
		 *  	register the event handler under the corresponding DOM event of
		 *  	the UI element's input DOM element (as defined by the return value
		 *  	of {@link CKEDITOR.ui.dialog.uiElement#getInputElement}).
		 *  </li>
		 * </ol>
		 * This function is only called at UI element instantiation, but can
		 * be overridded in child classes if they require more flexibility.
		 * @param {CKEDITOR.dialog.definition.uiElement} definition The UI element
		 * definition.
		 * @returns {CKEDITOR.dialog.uiElement} The current UI element.
		 * @example
		 */
		registerEvents : function( definition )
		{
			var regex = /^on([A-Z]\w+)/,
				match;

			var registerDomEvent = function( uiElement, dialog, eventName, func )
			{
				dialog.on( 'load', function()
				{
					uiElement.getInputElement().on( eventName, func, uiElement );
				});
			};

			for ( var i in definition )
			{
				if ( !( match = i.match( regex ) ) )
					continue;
				if ( this.eventProcessors[i] )
					this.eventProcessors[i].call( this, this._.dialog, definition[i] );
				else
					registerDomEvent( this, this._.dialog, match[1].toLowerCase(), definition[i] );
			}

			return this;
		},

		/**
		 * The event processor list used by
		 * {@link CKEDITOR.ui.dialog.uiElement#getInputElement} at UI element
		 * instantiation. The default list defines three on* events:
		 * <ol>
		 *  <li>onLoad - Called when the element's parent dialog opens for the
		 *  first time</li>
		 *  <li>onShow - Called whenever the element's parent dialog opens.</li>
		 *  <li>onHide - Called whenever the element's parent dialog closes.</li>
		 * </ol>
		 * @field
		 * @type Object
		 * @example
		 * // This connects the 'click' event in CKEDITOR.ui.dialog.button to onClick
		 * // handlers in the UI element's definitions.
		 * CKEDITOR.ui.dialog.button.eventProcessors = CKEDITOR.tools.extend( {},
		 * &nbsp;&nbsp;CKEDITOR.ui.dialog.uiElement.prototype.eventProcessors,
		 * &nbsp;&nbsp;{ onClick : function( dialog, func ) { this.on( 'click', func ); } },
		 * &nbsp;&nbsp;true );
		 */
		eventProcessors :
		{
			onLoad : function( dialog, func )
			{
				dialog.on( 'load', func, this );
			},

			onShow : function( dialog, func )
			{
				dialog.on( 'show', func, this );
			},

			onHide : function( dialog, func )
			{
				dialog.on( 'hide', func, this );
			}
		},

		/**
		 * The default handler for a UI element's access key down event, which
		 * tries to put focus to the UI element.<br />
		 * Can be overridded in child classes for more sophisticaed behavior.
		 * @param {CKEDITOR.dialog} dialog The parent dialog object.
		 * @param {String} key The key combination pressed. Since access keys
		 * are defined to always include the CTRL key, its value should always
		 * include a 'CTRL+' prefix.
		 * @example
		 */
		accessKeyDown : function( dialog, key )
		{
			this.focus();
		},

		/**
		 * The default handler for a UI element's access key up event, which
		 * does nothing.<br />
		 * Can be overridded in child classes for more sophisticated behavior.
		 * @param {CKEDITOR.dialog} dialog The parent dialog object.
		 * @param {String} key The key combination pressed. Since access keys
		 * are defined to always include the CTRL key, its value should always
		 * include a 'CTRL+' prefix.
		 * @example
		 */
		accessKeyUp : function( dialog, key )
		{
		},

		/**
		 * Disables a UI element.
		 * @example
		 */
		disable : function()
		{
			var element = this.getElement(),
				input = this.getInputElement();
			input.setAttribute( 'disabled', 'true' );
			element.addClass( 'cke_disabled' );
		},

		/**
		 * Enables a UI element.
		 * @example
		 */
		enable : function()
		{
			var element = this.getElement(),
				input = this.getInputElement();
			input.removeAttribute( 'disabled' );
			element.removeClass( 'cke_disabled' );
		},

		/**
		 * Determines whether an UI element is enabled or not.
		 * @returns {Boolean} Whether the UI element is enabled.
		 * @example
		 */
		isEnabled : function()
		{
			return !this.getElement().hasClass( 'cke_disabled' );
		},

		/**
		 * Determines whether an UI element is visible or not.
		 * @returns {Boolean} Whether the UI element is visible.
		 * @example
		 */
		isVisible : function()
		{
			return this.getInputElement().isVisible();
		},

		/**
		 * Determines whether an UI element is focus-able or not.
		 * Focus-able is defined as being both visible and enabled.
		 * @returns {Boolean} Whether the UI element can be focused.
		 * @example
		 */
		isFocusable : function()
		{
			if ( !this.isEnabled() || !this.isVisible() )
				return false;
			return true;
		}
	};

	CKEDITOR.ui.dialog.hbox.prototype = CKEDITOR.tools.extend( new CKEDITOR.ui.dialog.uiElement,
		/**
		 * @lends CKEDITOR.ui.dialog.hbox.prototype
		 */
		{
			/**
			 * Gets a child UI element inside this container.
			 * @param {Array|Number} indices An array or a single number to indicate the child's
			 * position in the container's descendant tree. Omit to get all the children in an array.
			 * @returns {Array|CKEDITOR.ui.dialog.uiElement} Array of all UI elements in the container
			 * if no argument given, or the specified UI element if indices is given.
			 * @example
			 * var checkbox = hbox.getChild( [0,1] );
			 * checkbox.setValue( true );
			 */
			getChild : function( indices )
			{
				// If no arguments, return a clone of the children array.
				if ( arguments.length < 1 )
					return this._.children.concat();

				// If indices isn't array, make it one.
				if ( !indices.splice )
					indices = [ indices ];

				// Retrieve the child element according to tree position.
				if ( indices.length < 2 )
					return this._.children[ indices[0] ];
				else
					return ( this._.children[ indices[0] ] && this._.children[ indices[0] ].getChild ) ?
						this._.children[ indices[0] ].getChild( indices.slice( 1, indices.length ) ) :
						null;
			}
		}, true );

	CKEDITOR.ui.dialog.vbox.prototype = new CKEDITOR.ui.dialog.hbox();



	(function()
	{
		var commonBuilder = {
			build : function( dialog, elementDefinition, output )
			{
				var children = elementDefinition.children,
					child,
					childHtmlList = [],
					childObjList = [];
				for ( var i = 0 ; ( i < children.length && ( child = children[i] ) ) ; i++ )
				{
					var childHtml = [];
					childHtmlList.push( childHtml );
					childObjList.push( CKEDITOR.dialog._.uiElementBuilders[ child.type ].build( dialog, child, childHtml ) );
				}
				return new CKEDITOR.ui.dialog[elementDefinition.type]( dialog, childObjList, childHtmlList, output, elementDefinition );
			}
		};

		CKEDITOR.dialog.addUIElement( 'hbox', commonBuilder );
		CKEDITOR.dialog.addUIElement( 'vbox', commonBuilder );
	})();

	/**
	 * Generic dialog command. It opens a specific dialog when executed.
	 * @constructor
	 * @augments CKEDITOR.commandDefinition
	 * @param {string} dialogName The name of the dialog to open when executing
	 *		this command.
	 * @example
	 * // Register the "link" command, which opens the "link" dialog.
	 * editor.addCommand( 'link', <b>new CKEDITOR.dialogCommand( 'link' )</b> );
	 */
	CKEDITOR.dialogCommand = function( dialogName )
	{
		this.dialogName = dialogName;
	};

	CKEDITOR.dialogCommand.prototype =
	{
		/** @ignore */
		exec : function( editor )
		{
			// Special treatment for Opera. (#8031)
			CKEDITOR.env.opera ?
				CKEDITOR.tools.setTimeout( function() { editor.openDialog( this.dialogName ); }, 0, this )
				: editor.openDialog( this.dialogName );
		},

		// Dialog commands just open a dialog ui, thus require no undo logic,
		// undo support should dedicate to specific dialog implementation.
		canUndo: false,

		editorFocus : CKEDITOR.env.ie || CKEDITOR.env.webkit
	};

	(function()
	{
		var notEmptyRegex = /^([a]|[^a])+$/,
			integerRegex = /^\d*$/,
			numberRegex = /^\d*(?:\.\d+)?$/,
			htmlLengthRegex = /^(((\d*(\.\d+))|(\d*))(px|\%)?)?$/,
			cssLengthRegex = /^(((\d*(\.\d+))|(\d*))(px|em|ex|in|cm|mm|pt|pc|\%)?)?$/i,
			inlineStyleRegex = /^(\s*[\w-]+\s*:\s*[^:;]+(?:;|$))*$/;

		CKEDITOR.VALIDATE_OR = 1;
		CKEDITOR.VALIDATE_AND = 2;

		CKEDITOR.dialog.validate =
		{
			functions : function()
			{
				var args = arguments;
				return function()
				{
					/**
					 * It's important for validate functions to be able to accept the value
					 * as argument in addition to this.getValue(), so that it is possible to
					 * combine validate functions together to make more sophisticated
					 * validators.
					 */
					var value = this && this.getValue ? this.getValue() : args[ 0 ];

					var msg = undefined,
						relation = CKEDITOR.VALIDATE_AND,
						functions = [], i;

					for ( i = 0 ; i < args.length ; i++ )
					{
						if ( typeof( args[i] ) == 'function' )
							functions.push( args[i] );
						else
							break;
					}

					if ( i < args.length && typeof( args[i] ) == 'string' )
					{
						msg = args[i];
						i++;
					}

					if ( i < args.length && typeof( args[i]) == 'number' )
						relation = args[i];

					var passed = ( relation == CKEDITOR.VALIDATE_AND ? true : false );
					for ( i = 0 ; i < functions.length ; i++ )
					{
						if ( relation == CKEDITOR.VALIDATE_AND )
							passed = passed && functions[i]( value );
						else
							passed = passed || functions[i]( value );
					}

					return !passed ? msg : true;
				};
			},

			regex : function( regex, msg )
			{
				/*
				 * Can be greatly shortened by deriving from functions validator if code size
				 * turns out to be more important than performance.
				 */
				return function()
				{
					var value = this && this.getValue ? this.getValue() : arguments[0];
					return !regex.test( value ) ? msg : true;
				};
			},

			notEmpty : function( msg )
			{
				return this.regex( notEmptyRegex, msg );
			},

			integer : function( msg )
			{
				return this.regex( integerRegex, msg );
			},

			'number' : function( msg )
			{
				return this.regex( numberRegex, msg );
			},

			'cssLength' : function( msg )
			{
				return this.functions( function( val ){ return cssLengthRegex.test( CKEDITOR.tools.trim( val ) ); }, msg );
			},

			'htmlLength' : function( msg )
			{
				return this.functions( function( val ){ return htmlLengthRegex.test( CKEDITOR.tools.trim( val ) ); }, msg );
			},

			'inlineStyle' : function( msg )
			{
				return this.functions( function( val ){ return inlineStyleRegex.test( CKEDITOR.tools.trim( val ) ); }, msg );
			},

			equals : function( value, msg )
			{
				return this.functions( function( val ){ return val == value; }, msg );
			},

			notEqual : function( value, msg )
			{
				return this.functions( function( val ){ return val != value; }, msg );
			}
		};

	CKEDITOR.on( 'instanceDestroyed', function( evt )
	{
		// Remove dialog cover on last instance destroy.
		if ( CKEDITOR.tools.isEmpty( CKEDITOR.instances ) )
		{
			var currentTopDialog;
			while ( ( currentTopDialog = CKEDITOR.dialog._.currentTop ) )
				currentTopDialog.hide();
			removeCovers();
		}

		var dialogs = evt.editor._.storedDialogs;
		for ( var name in dialogs )
			dialogs[ name ].destroy();

	});

	})();

	// Extend the CKEDITOR.editor class with dialog specific functions.
	CKEDITOR.tools.extend( CKEDITOR.editor.prototype,
		/** @lends CKEDITOR.editor.prototype */
		{
			/**
			 * Loads and opens a registered dialog.
			 * @param {String} dialogName The registered name of the dialog.
			 * @param {Function} callback The function to be invoked after dialog instance created.
			 * @see CKEDITOR.dialog.add
			 * @example
			 * CKEDITOR.instances.editor1.openDialog( 'smiley' );
			 * @returns {CKEDITOR.dialog} The dialog object corresponding to the dialog displayed. null if the dialog name is not registered.
			 */
			openDialog : function( dialogName, callback )
			{
				if ( this.mode == 'wysiwyg' && CKEDITOR.env.ie )
				{
					var selection = this.getSelection();
					selection && selection.lock();
				}

				var dialogDefinitions = CKEDITOR.dialog._.dialogDefinitions[ dialogName ],
						dialogSkin = this.skin.dialog;

				if ( CKEDITOR.dialog._.currentTop === null )
					showCover( this );

				// If the dialogDefinition is already loaded, open it immediately.
				if ( typeof dialogDefinitions == 'function' && dialogSkin._isLoaded )
				{
					var storedDialogs = this._.storedDialogs ||
						( this._.storedDialogs = {} );

					var dialog = storedDialogs[ dialogName ] ||
						( storedDialogs[ dialogName ] = new CKEDITOR.dialog( this, dialogName ) );

					callback && callback.call( dialog, dialog );
					dialog.show();

					return dialog;
				}
				else if ( dialogDefinitions == 'failed' )
				{
					hideCover();
					throw new Error( '[CKEDITOR.dialog.openDialog] Dialog "' + dialogName + '" failed when loading definition.' );
				}

				var me = this;

				function onDialogFileLoaded( success )
				{
					var dialogDefinition = CKEDITOR.dialog._.dialogDefinitions[ dialogName ],
							skin = me.skin.dialog;

					// Check if both skin part and definition is loaded.
					if ( !skin._isLoaded || loadDefinition && typeof success == 'undefined' )
						return;

					// In case of plugin error, mark it as loading failed.
					if ( typeof dialogDefinition != 'function' )
						CKEDITOR.dialog._.dialogDefinitions[ dialogName ] = 'failed';

					me.openDialog( dialogName, callback );
				}

				if ( typeof dialogDefinitions == 'string' )
				{
					var loadDefinition = 1;
					CKEDITOR.scriptLoader.load( CKEDITOR.getUrl( dialogDefinitions ), onDialogFileLoaded, null, 0, 1 );
				}

				CKEDITOR.skins.load( this, 'dialog', onDialogFileLoaded );

				return null;
			}
		});
})();

CKEDITOR.plugins.add( 'dialog',
	{
		requires : [ 'dialogui' ]
	});

// Dialog related configurations.

/**
 * The color of the dialog background cover. It should be a valid CSS color
 * string.
 * @name CKEDITOR.config.dialog_backgroundCoverColor
 * @type String
 * @default 'white'
 * @example
 * config.dialog_backgroundCoverColor = 'rgb(255, 254, 253)';
 */

/**
 * The opacity of the dialog background cover. It should be a number within the
 * range [0.0, 1.0].
 * @name CKEDITOR.config.dialog_backgroundCoverOpacity
 * @type Number
 * @default 0.5
 * @example
 * config.dialog_backgroundCoverOpacity = 0.7;
 */

/**
 * If the dialog has more than one tab, put focus into the first tab as soon as dialog is opened.
 * @name CKEDITOR.config.dialog_startupFocusTab
 * @type Boolean
 * @default false
 * @example
 * config.dialog_startupFocusTab = true;
 */

/**
 * The distance of magnetic borders used in moving and resizing dialogs,
 * measured in pixels.
 * @name CKEDITOR.config.dialog_magnetDistance
 * @type Number
 * @default 20
 * @example
 * config.dialog_magnetDistance = 30;
 */

/**
 * The guideline to follow when generating the dialog buttons. There are 3 possible options:
 * <ul>
 *     <li>'OS' - the buttons will be displayed in the default order of the user's OS;</li>
 *     <li>'ltr' - for Left-To-Right order;</li>
 *     <li>'rtl' - for Right-To-Left order.</li>
 * </ul>
 * @name CKEDITOR.config.dialog_buttonsOrder
 * @type String
 * @default 'OS'
 * @since 3.5
 * @example
 * config.dialog_buttonsOrder = 'rtl';
 */

/**
 * The dialog contents to removed. It's a string composed by dialog name and tab name with a colon between them.
 * Separate each pair with semicolon (see example).
 * <b>Note: All names are case-sensitive.</b>
 * <b>Note: Be cautious when specifying dialog tabs that are mandatory, like "info", dialog functionality might be broken because of this!</b>
 * @name CKEDITOR.config.removeDialogTabs
 * @type String
 * @since 3.5
 * @default ''
 * @example
 * config.removeDialogTabs = 'flash:advanced;image:Link';
 */

/**
 * Fired when a dialog definition is about to be used to create a dialog into
 * an editor instance. This event makes it possible to customize the definition
 * before creating it.
 * <p>Note that this event is called only the first time a specific dialog is
 * opened. Successive openings will use the cached dialog, and this event will
 * not get fired.</p>
 * @name CKEDITOR#dialogDefinition
 * @event
 * @param {CKEDITOR.dialog.definition} data The dialog defination that
 *		is being loaded.
 * @param {CKEDITOR.editor} editor The editor instance that will use the
 *		dialog.
 */

/**
 * Fired when a tab is going to be selected in a dialog
 * @name CKEDITOR.dialog#selectPage
 * @event
 * @param {String} page The id of the page that it's gonna be selected.
 * @param {String} currentPage The id of the current page.
 */

/**
 * Fired when the user tries to dismiss a dialog
 * @name CKEDITOR.dialog#cancel
 * @event
 * @param {Boolean} hide Whether the event should proceed or not.
 */

/**
 * Fired when the user tries to confirm a dialog
 * @name CKEDITOR.dialog#ok
 * @event
 * @param {Boolean} hide Whether the event should proceed or not.
 */

/**
 * Fired when a dialog is shown
 * @name CKEDITOR.dialog#show
 * @event
 */

/**
 * Fired when a dialog is shown
 * @name CKEDITOR.editor#dialogShow
 * @event
 */

/**
 * Fired when a dialog is hidden
 * @name CKEDITOR.dialog#hide
 * @event
 */

/**
 * Fired when a dialog is hidden
 * @name CKEDITOR.editor#dialogHide
 * @event
 */

/**
 * Fired when a dialog is being resized. The event is fired on
 * both the 'CKEDITOR.dialog' object and the dialog instance
 * since 3.5.3, previously it's available only in the global object.
 * @name CKEDITOR.dialog#resize
 * @since 3.5
 * @event
 * @param {CKEDITOR.dialog} dialog The dialog being resized (if
 * it's fired on the dialog itself, this parameter isn't sent).
 * @param {String} skin The skin name.
 * @param {Number} width The new width.
 * @param {Number} height The new height.
 */
