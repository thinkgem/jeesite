/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Undo/Redo system for saving shapshot for document modification
 *		and other recordable changes.
 */

(function()
{
	CKEDITOR.plugins.add( 'undo',
	{
		requires : [ 'selection', 'wysiwygarea' ],

		init : function( editor )
		{
			var undoManager = new UndoManager( editor );

			var undoCommand = editor.addCommand( 'undo',
				{
					exec : function()
					{
						if ( undoManager.undo() )
						{
							editor.selectionChange();
							this.fire( 'afterUndo' );
						}
					},
					state : CKEDITOR.TRISTATE_DISABLED,
					canUndo : false
				});

			var redoCommand = editor.addCommand( 'redo',
				{
					exec : function()
					{
						if ( undoManager.redo() )
						{
							editor.selectionChange();
							this.fire( 'afterRedo' );
						}
					},
					state : CKEDITOR.TRISTATE_DISABLED,
					canUndo : false
				});

			undoManager.onChange = function()
			{
				undoCommand.setState( undoManager.undoable() ? CKEDITOR.TRISTATE_OFF : CKEDITOR.TRISTATE_DISABLED );
				redoCommand.setState( undoManager.redoable() ? CKEDITOR.TRISTATE_OFF : CKEDITOR.TRISTATE_DISABLED );
			};

			function recordCommand( event )
			{
				// If the command hasn't been marked to not support undo.
				if ( undoManager.enabled && event.data.command.canUndo !== false )
					undoManager.save();
			}

			// We'll save snapshots before and after executing a command.
			editor.on( 'beforeCommandExec', recordCommand );
			editor.on( 'afterCommandExec', recordCommand );

			// Save snapshots before doing custom changes.
			editor.on( 'saveSnapshot', function( evt )
				{
					undoManager.save( evt.data && evt.data.contentOnly );
				});

			// Registering keydown on every document recreation.(#3844)
			editor.on( 'contentDom', function()
				{
					editor.document.on( 'keydown', function( event )
						{
							// Do not capture CTRL hotkeys.
							if ( !event.data.$.ctrlKey && !event.data.$.metaKey )
								undoManager.type( event );
						});
				});

			// Always save an undo snapshot - the previous mode might have
			// changed editor contents.
			editor.on( 'beforeModeUnload', function()
				{
					editor.mode == 'wysiwyg' && undoManager.save( true );
				});

			// Make the undo manager available only in wysiwyg mode.
			editor.on( 'mode', function()
				{
					undoManager.enabled = editor.readOnly ? false : editor.mode == 'wysiwyg';
					undoManager.onChange();
				});

			editor.ui.addButton( 'Undo',
				{
					label : editor.lang.undo,
					command : 'undo'
				});

			editor.ui.addButton( 'Redo',
				{
					label : editor.lang.redo,
					command : 'redo'
				});

			editor.resetUndo = function()
			{
				// Reset the undo stack.
				undoManager.reset();

				// Create the first image.
				editor.fire( 'saveSnapshot' );
			};

			/**
			 * Amend the top of undo stack (last undo image) with the current DOM changes.
			 * @name CKEDITOR.editor#updateUndo
			 * @example
			 * function()
			 * {
			 *  editor.fire( 'saveSnapshot' );
			 * 	editor.document.body.append(...);
			 *  // Make new changes following the last undo snapshot part of it.
			 * 	editor.fire( 'updateSnapshot' );
			 * ...
			 * }
			 */
			editor.on( 'updateSnapshot', function()
			{
				if ( undoManager.currentImage )
					undoManager.update();
			});
		}
	});

	CKEDITOR.plugins.undo = {};

	/**
	 * Undo snapshot which represents the current document status.
	 * @name CKEDITOR.plugins.undo.Image
	 * @param editor The editor instance on which the image is created.
	 */
	var Image = CKEDITOR.plugins.undo.Image = function( editor )
	{
		this.editor = editor;

		editor.fire( 'beforeUndoImage' );

		var contents = editor.getSnapshot(),
			selection	= contents && editor.getSelection();

		// In IE, we need to remove the expando attributes.
		CKEDITOR.env.ie && contents && ( contents = contents.replace( /\s+data-cke-expando=".*?"/g, '' ) );

		this.contents	= contents;
		this.bookmarks	= selection && selection.createBookmarks2( true );

		editor.fire( 'afterUndoImage' );
	};

	// Attributes that browser may changing them when setting via innerHTML.
	var protectedAttrs = /\b(?:href|src|name)="[^"]*?"/gi;

	Image.prototype =
	{
		equals : function( otherImage, contentOnly )
		{

			var thisContents = this.contents,
				otherContents = otherImage.contents;

			// For IE6/7 : Comparing only the protected attribute values but not the original ones.(#4522)
			if ( CKEDITOR.env.ie && ( CKEDITOR.env.ie7Compat || CKEDITOR.env.ie6Compat ) )
			{
				thisContents = thisContents.replace( protectedAttrs, '' );
				otherContents = otherContents.replace( protectedAttrs, '' );
			}

			if ( thisContents != otherContents )
				return false;

			if ( contentOnly )
				return true;

			var bookmarksA = this.bookmarks,
				bookmarksB = otherImage.bookmarks;

			if ( bookmarksA || bookmarksB )
			{
				if ( !bookmarksA || !bookmarksB || bookmarksA.length != bookmarksB.length )
					return false;

				for ( var i = 0 ; i < bookmarksA.length ; i++ )
				{
					var bookmarkA = bookmarksA[ i ],
						bookmarkB = bookmarksB[ i ];

					if (
						bookmarkA.startOffset != bookmarkB.startOffset ||
						bookmarkA.endOffset != bookmarkB.endOffset ||
						!CKEDITOR.tools.arrayCompare( bookmarkA.start, bookmarkB.start ) ||
						!CKEDITOR.tools.arrayCompare( bookmarkA.end, bookmarkB.end ) )
					{
						return false;
					}
				}
			}

			return true;
		}
	};

	/**
	 * @constructor Main logic for Redo/Undo feature.
	 */
	function UndoManager( editor )
	{
		this.editor = editor;

		// Reset the undo stack.
		this.reset();
	}


	var editingKeyCodes = { /*Backspace*/ 8:1, /*Delete*/ 46:1 },
		modifierKeyCodes = { /*Shift*/ 16:1, /*Ctrl*/ 17:1, /*Alt*/ 18:1 },
		navigationKeyCodes = { 37:1, 38:1, 39:1, 40:1 };  // Arrows: L, T, R, B

	UndoManager.prototype =
	{
		/**
		 * Process undo system regard keystrikes.
		 * @param {CKEDITOR.dom.event} event
		 */
		type : function( event )
		{
			var keystroke = event && event.data.getKey(),
				isModifierKey = keystroke in modifierKeyCodes,
				isEditingKey = keystroke in editingKeyCodes,
				wasEditingKey = this.lastKeystroke in editingKeyCodes,
				sameAsLastEditingKey = isEditingKey && keystroke == this.lastKeystroke,
				// Keystrokes which navigation through contents.
				isReset = keystroke in navigationKeyCodes,
				wasReset = this.lastKeystroke in navigationKeyCodes,

				// Keystrokes which just introduce new contents.
				isContent = ( !isEditingKey && !isReset ),

				// Create undo snap for every different modifier key.
				modifierSnapshot = ( isEditingKey && !sameAsLastEditingKey ),
				// Create undo snap on the following cases:
				// 1. Just start to type .
				// 2. Typing some content after a modifier.
				// 3. Typing some content after make a visible selection.
				startedTyping = !( isModifierKey || this.typing )
					|| ( isContent && ( wasEditingKey || wasReset ) );

			if ( startedTyping || modifierSnapshot )
			{
				var beforeTypeImage = new Image( this.editor ),
					beforeTypeCount = this.snapshots.length;

				// Use setTimeout, so we give the necessary time to the
				// browser to insert the character into the DOM.
				CKEDITOR.tools.setTimeout( function()
					{
						var currentSnapshot = this.editor.getSnapshot();

						// In IE, we need to remove the expando attributes.
						if ( CKEDITOR.env.ie )
							currentSnapshot = currentSnapshot.replace( /\s+data-cke-expando=".*?"/g, '' );

						// If changes have taken place, while not been captured yet (#8459),
						// compensate the snapshot.
						if ( beforeTypeImage.contents != currentSnapshot &&
							 beforeTypeCount == this.snapshots.length )
						{
							// It's safe to now indicate typing state.
							this.typing = true;

							// This's a special save, with specified snapshot
							// and without auto 'fireChange'.
							if ( !this.save( false, beforeTypeImage, false ) )
								// Drop future snapshots.
								this.snapshots.splice( this.index + 1, this.snapshots.length - this.index - 1 );

							this.hasUndo = true;
							this.hasRedo = false;

							this.typesCount = 1;
							this.modifiersCount = 1;

							this.onChange();
						}
					},
					0, this
				);
			}

			this.lastKeystroke = keystroke;

			// Create undo snap after typed too much (over 25 times).
			if ( isEditingKey )
			{
				this.typesCount = 0;
				this.modifiersCount++;

				if ( this.modifiersCount > 25 )
				{
					this.save( false, null, false );
					this.modifiersCount = 1;
				}
			}
			else if ( !isReset )
			{
				this.modifiersCount = 0;
				this.typesCount++;

				if ( this.typesCount > 25 )
				{
					this.save( false, null, false );
					this.typesCount = 1;
				}
			}

		},

		reset : function()	// Reset the undo stack.
		{
			/**
			 * Remember last pressed key.
			 */
			this.lastKeystroke = 0;

			/**
			 * Stack for all the undo and redo snapshots, they're always created/removed
			 * in consistency.
			 */
			this.snapshots = [];

			/**
			 * Current snapshot history index.
			 */
			this.index = -1;

			this.limit = this.editor.config.undoStackSize || 20;

			this.currentImage = null;

			this.hasUndo = false;
			this.hasRedo = false;

			this.resetType();
		},

		/**
		 * Reset all states about typing.
		 * @see  UndoManager.type
		 */
		resetType : function()
		{
			this.typing = false;
			delete this.lastKeystroke;
			this.typesCount = 0;
			this.modifiersCount = 0;
		},
		fireChange : function()
		{
			this.hasUndo = !!this.getNextImage( true );
			this.hasRedo = !!this.getNextImage( false );
			// Reset typing
			this.resetType();
			this.onChange();
		},

		/**
		 * Save a snapshot of document image for later retrieve.
		 */
		save : function( onContentOnly, image, autoFireChange )
		{
			var snapshots = this.snapshots;

			// Get a content image.
			if ( !image )
				image = new Image( this.editor );

			// Do nothing if it was not possible to retrieve an image.
			if ( image.contents === false )
				return false;

			// Check if this is a duplicate. In such case, do nothing.
			if ( this.currentImage && image.equals( this.currentImage, onContentOnly ) )
				return false;

			// Drop future snapshots.
			snapshots.splice( this.index + 1, snapshots.length - this.index - 1 );

			// If we have reached the limit, remove the oldest one.
			if ( snapshots.length == this.limit )
				snapshots.shift();

			// Add the new image, updating the current index.
			this.index = snapshots.push( image ) - 1;

			this.currentImage = image;

			if ( autoFireChange !== false )
				this.fireChange();
			return true;
		},

		restoreImage : function( image )
		{
			// Bring editor focused to restore selection.
			var editor = this.editor,
				sel;

			if ( image.bookmarks )
			{
				editor.focus();
				// Retrieve the selection beforehand. (#8324)
				sel = editor.getSelection();
			}

			this.editor.loadSnapshot( image.contents );

			if ( image.bookmarks )
				sel.selectBookmarks( image.bookmarks );
			else if ( CKEDITOR.env.ie )
			{
				// IE BUG: If I don't set the selection to *somewhere* after setting
				// document contents, then IE would create an empty paragraph at the bottom
				// the next time the document is modified.
				var $range = this.editor.document.getBody().$.createTextRange();
				$range.collapse( true );
				$range.select();
			}

			this.index = image.index;

			// Update current image with the actual editor
			// content, since actualy content may differ from
			// the original snapshot due to dom change. (#4622)
			this.update();
			this.fireChange();
		},

		// Get the closest available image.
		getNextImage : function( isUndo )
		{
			var snapshots = this.snapshots,
				currentImage = this.currentImage,
				image, i;

			if ( currentImage )
			{
				if ( isUndo )
				{
					for ( i = this.index - 1 ; i >= 0 ; i-- )
					{
						image = snapshots[ i ];
						if ( !currentImage.equals( image, true ) )
						{
							image.index = i;
							return image;
						}
					}
				}
				else
				{
					for ( i = this.index + 1 ; i < snapshots.length ; i++ )
					{
						image = snapshots[ i ];
						if ( !currentImage.equals( image, true ) )
						{
							image.index = i;
							return image;
						}
					}
				}
			}

			return null;
		},

		/**
		 * Check the current redo state.
		 * @return {Boolean} Whether the document has previous state to
		 *		retrieve.
		 */
		redoable : function()
		{
			return this.enabled && this.hasRedo;
		},

		/**
		 * Check the current undo state.
		 * @return {Boolean} Whether the document has future state to restore.
		 */
		undoable : function()
		{
			return this.enabled && this.hasUndo;
		},

		/**
		 * Perform undo on current index.
		 */
		undo : function()
		{
			if ( this.undoable() )
			{
				this.save( true );

				var image = this.getNextImage( true );
				if ( image )
					return this.restoreImage( image ), true;
			}

			return false;
		},

		/**
		 * Perform redo on current index.
		 */
		redo : function()
		{
			if ( this.redoable() )
			{
				// Try to save. If no changes have been made, the redo stack
				// will not change, so it will still be redoable.
				this.save( true );

				// If instead we had changes, we can't redo anymore.
				if ( this.redoable() )
				{
					var image = this.getNextImage( false );
					if ( image )
						return this.restoreImage( image ), true;
				}
			}

			return false;
		},

		/**
		 * Update the last snapshot of the undo stack with the current editor content.
		 */
		update : function()
		{
			this.snapshots.splice( this.index, 1, ( this.currentImage = new Image( this.editor ) ) );
		}
	};
})();

/**
 * The number of undo steps to be saved. The higher this setting value the more
 * memory is used for it.
 * @name CKEDITOR.config.undoStackSize
 * @type Number
 * @default 20
 * @example
 * config.undoStackSize = 50;
 */

/**
 * Fired when the editor is about to save an undo snapshot. This event can be
 * fired by plugins and customizations to make the editor saving undo snapshots.
 * @name CKEDITOR.editor#saveSnapshot
 * @event
 */

/**
 * Fired before an undo image is to be taken. An undo image represents the
 * editor state at some point. It's saved into an undo store, so the editor is
 * able to recover the editor state on undo and redo operations.
 * @name CKEDITOR.editor#beforeUndoImage
 * @since 3.5.3
 * @see CKEDITOR.editor#afterUndoImage
 * @event
 */

/**
 * Fired after an undo image is taken. An undo image represents the
 * editor state at some point. It's saved into an undo store, so the editor is
 * able to recover the editor state on undo and redo operations.
 * @name CKEDITOR.editor#afterUndoImage
 * @since 3.5.3
 * @see CKEDITOR.editor#beforeUndoImage
 * @event
 */
