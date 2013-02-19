/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview The "wysiwygarea" plugin. It registers the "wysiwyg" editing
 *		mode, which handles the main editing area space.
 */

(function()
{
	// Matching an empty paragraph at the end of document.
	var emptyParagraphRegexp = /(^|<body\b[^>]*>)\s*<(p|div|address|h\d|center|pre)[^>]*>\s*(?:<br[^>]*>|&nbsp;|\u00A0|&#160;)?\s*(:?<\/\2>)?\s*(?=$|<\/body>)/gi;

	var notWhitespaceEval = CKEDITOR.dom.walker.whitespaces( true ),
	  notBogus = CKEDITOR.dom.walker.bogus( true ),
	  notEmpty = function( node ) { return notWhitespaceEval( node ) && notBogus( node ); };

	// Elements that could blink the cursor anchoring beside it, like hr, page-break. (#6554)
	function nonEditable( element )
	{
		return element.isBlockBoundary() && CKEDITOR.dtd.$empty[ element.getName() ];
	}


	function onInsert( insertFunc )
	{
		return function( evt )
		{
			if ( this.mode == 'wysiwyg' )
			{
				this.focus();

				// Since the insertion might happen from within dialog or menu
				// where the editor selection might be locked at the moment,
				// update the locked selection.
				var selection = this.getSelection(),
				selIsLocked = selection.isLocked;

				selIsLocked && selection.unlock();

				this.fire( 'saveSnapshot' );

				insertFunc.call( this, evt.data );

				selIsLocked && this.getSelection().lock();

				// Save snaps after the whole execution completed.
				// This's a workaround for make DOM modification's happened after
				// 'insertElement' to be included either, e.g. Form-based dialogs' 'commitContents'
				// call.
				CKEDITOR.tools.setTimeout( function()
				   {
					   this.fire( 'saveSnapshot' );
				   }, 0, this );
			}
		};
	}

	function doInsertHtml( data )
	{
		if ( this.dataProcessor )
			data = this.dataProcessor.toHtml( data );

		if ( !data )
			return;

		// HTML insertion only considers the first range.
		var selection = this.getSelection(),
			range = selection.getRanges()[ 0 ];

		if ( range.checkReadOnly() )
			return;

		// Opera: force block splitting when pasted content contains block. (#7801)
		if ( CKEDITOR.env.opera )
		{
			var path = new CKEDITOR.dom.elementPath( range.startContainer );
			if ( path.block )
			{
				var nodes = CKEDITOR.htmlParser.fragment.fromHtml( data, false ).children;
				for ( var i = 0, count = nodes.length; i < count; i++ )
				{
					if ( nodes[ i ]._.isBlockLike )
					{
						range.splitBlock( this.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p' );
						range.insertNode( range.document.createText( '' ) );
						range.select();
						break;
					}
				}
			}
		}

		if ( CKEDITOR.env.ie )
		{
			var $sel = selection.getNative();

			// Delete control selections to avoid IE bugs on pasteHTML.
			if ( $sel.type == 'Control' )
				$sel.clear();
			else if ( selection.getType() == CKEDITOR.SELECTION_TEXT )
			{
				// Due to IE bugs on handling contenteditable=false blocks
				// (#6005), we need to make some checks and eventually
				// delete the selection first.

				range = selection.getRanges()[ 0 ];
				var endContainer = range && range.endContainer;

				if ( endContainer &&
						endContainer.type == CKEDITOR.NODE_ELEMENT &&
						endContainer.getAttribute( 'contenteditable' ) == 'false' &&
						range.checkBoundaryOfElement( endContainer, CKEDITOR.END ) )
				{
					range.setEndAfter( range.endContainer );
					range.deleteContents();
				}
			}

			$sel.createRange().pasteHTML( data );
		}
		else
			this.document.$.execCommand( 'inserthtml', false, data );

		// Webkit does not scroll to the cursor position after pasting (#5558)
		if ( CKEDITOR.env.webkit )
		{
			selection = this.getSelection();
			selection.scrollIntoView();
		}
	}

	function doInsertText( text )
	{
		var selection = this.getSelection(),
			mode = selection.getStartElement().hasAscendant( 'pre', true ) ?
				   CKEDITOR.ENTER_BR : this.config.enterMode,
			isEnterBrMode = mode == CKEDITOR.ENTER_BR;

		var html = CKEDITOR.tools.htmlEncode( text.replace( /\r\n|\r/g, '\n' ) );

		// Convert leading and trailing whitespaces into &nbsp;
		html = html.replace( /^[ \t]+|[ \t]+$/g, function( match, offset, s )
			{
				if ( match.length == 1 )	// one space, preserve it
					return '&nbsp;';
				else if ( !offset )		// beginning of block
					return CKEDITOR.tools.repeat( '&nbsp;', match.length - 1 ) + ' ';
				else				// end of block
					return ' ' + CKEDITOR.tools.repeat( '&nbsp;', match.length - 1 );
			} );

		// Convert subsequent whitespaces into &nbsp;
		html = html.replace( /[ \t]{2,}/g, function ( match )
		   {
			   return CKEDITOR.tools.repeat( '&nbsp;', match.length - 1 ) + ' ';
		   } );

		var paragraphTag = mode == CKEDITOR.ENTER_P ? 'p' : 'div';

		// Two line-breaks create one paragraph.
		if ( !isEnterBrMode )
		{
			html = html.replace( /(\n{2})([\s\S]*?)(?:$|\1)/g,
				function( match, group1, text )
				{
					return '<'+paragraphTag + '>' + text + '</' + paragraphTag + '>';
				});
		}

		// One <br> per line-break.
		html = html.replace( /\n/g, '<br>' );

		// Compensate padding <br> for non-IE.
		if ( !( isEnterBrMode || CKEDITOR.env.ie ) )
		{
			html = html.replace( new RegExp( '<br>(?=</' + paragraphTag + '>)' ), function( match )
			{
				return CKEDITOR.tools.repeat( match, 2 );
			} );
		}

		// Inline styles have to be inherited in Firefox.
		if ( CKEDITOR.env.gecko || CKEDITOR.env.webkit )
		{
			var path = new CKEDITOR.dom.elementPath( selection.getStartElement() ),
				context = [];

			for ( var i = 0; i < path.elements.length; i++ )
			{
				var tag = path.elements[ i ].getName();
				if ( tag in CKEDITOR.dtd.$inline )
					context.unshift( path.elements[ i ].getOuterHtml().match( /^<.*?>/) );
				else if ( tag in CKEDITOR.dtd.$block )
					break;
			}

			// Reproduce the context  by preceding the pasted HTML with opening inline tags.
			html = context.join( '' ) + html;
		}

		doInsertHtml.call( this, html );
	}

	function doInsertElement( element )
	{
		var selection = this.getSelection(),
				ranges = selection.getRanges(),
				elementName = element.getName(),
				isBlock = CKEDITOR.dtd.$block[ elementName ];

		var selIsLocked = selection.isLocked;

		if ( selIsLocked )
			selection.unlock();

		var range, clone, lastElement, bookmark;

		for ( var i = ranges.length - 1 ; i >= 0 ; i-- )
		{
			range = ranges[ i ];

				if ( !range.checkReadOnly() )
				{
					// Remove the original contents, merge splitted nodes.
					range.deleteContents( 1 );

					clone = !i && element || element.clone( 1 );

					// If we're inserting a block at dtd-violated position, split
					// the parent blocks until we reach blockLimit.
					var current, dtd;
					if ( isBlock )
					{
						while ( ( current = range.getCommonAncestor( 0, 1 ) )
								&& ( dtd = CKEDITOR.dtd[ current.getName() ] )
								&& !( dtd && dtd [ elementName ] ) )
						{
							// Split up inline elements.
							if ( current.getName() in CKEDITOR.dtd.span )
								range.splitElement( current );
							// If we're in an empty block which indicate a new paragraph,
							// simply replace it with the inserting block.(#3664)
							else if ( range.checkStartOfBlock()
									&& range.checkEndOfBlock() )
							{
								range.setStartBefore( current );
								range.collapse( true );
								current.remove();
							}
							else
								range.splitBlock();
						}
					}

					// Insert the new node.
					range.insertNode( clone );

					// Save the last element reference so we can make the
					// selection later.
					if ( !lastElement )
						lastElement = clone;
				}
			}

			if ( lastElement )
			{
				range.moveToPosition( lastElement, CKEDITOR.POSITION_AFTER_END );

				// If we're inserting a block element immediately followed by
				// another block element, the selection must be optimized. (#3100,#5436,#8950)
				if ( isBlock )
				{
					var next = lastElement.getNext( notEmpty ),
						nextName = next && next.type == CKEDITOR.NODE_ELEMENT && next.getName();

					// If the next one is a text block, move cursor to the start of it's content.
					if ( nextName && CKEDITOR.dtd.$block[ nextName ] )
					{
						if ( CKEDITOR.dtd[ nextName ][ '#' ] )
							range.moveToElementEditStart( next );
						// Otherwise move cursor to the before end of the last element.
						else
							range.moveToElementEditEnd( lastElement );
					}
					// Open a new line if the block is inserted at the end of parent.
					else if ( !next )
					{
						next = range.fixBlock( true, this.config.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p' );
						range.moveToElementEditStart( next );
					}
				}
			}

			selection.selectRanges( [ range ] );

		if ( selIsLocked )
			this.getSelection().lock();
	}

	// DOM modification here should not bother dirty flag.(#4385)
	function restoreDirty( editor )
	{
		if ( !editor.checkDirty() )
			setTimeout( function(){ editor.resetDirty(); }, 0 );
	}

	var isNotWhitespace = CKEDITOR.dom.walker.whitespaces( true ),
		isNotBookmark = CKEDITOR.dom.walker.bookmark( false, true );

	function isNotEmpty( node )
	{
		return isNotWhitespace( node ) && isNotBookmark( node );
	}

	function isNbsp( node )
	{
		return node.type == CKEDITOR.NODE_TEXT
			   && CKEDITOR.tools.trim( node.getText() ).match( /^(?:&nbsp;|\xa0)$/ );
	}

	function restoreSelection( selection )
	{
		if ( selection.isLocked )
		{
			selection.unlock();
			setTimeout( function() { selection.lock(); }, 0 );
		}
	}

	function isBlankParagraph( block )
	{
		return block.getOuterHtml().match( emptyParagraphRegexp );
	}

	isNotWhitespace = CKEDITOR.dom.walker.whitespaces( true );

	// Gecko need a key event to 'wake up' the editing
	// ability when document is empty.(#3864, #5781)
	function activateEditing( editor )
	{
		var win = editor.window,
			doc = editor.document,
			body = editor.document.getBody(),
			bodyFirstChild = body.getFirst(),
			bodyChildsNum = body.getChildren().count();

		if ( !bodyChildsNum
			|| bodyChildsNum == 1
				&& bodyFirstChild.type == CKEDITOR.NODE_ELEMENT
				&& bodyFirstChild.hasAttribute( '_moz_editor_bogus_node' ) )
		{
			restoreDirty( editor );

			// Memorize scroll position to restore it later (#4472).
			var hostDocument = editor.element.getDocument();
			var hostDocumentElement = hostDocument.getDocumentElement();
			var scrollTop = hostDocumentElement.$.scrollTop;
			var scrollLeft = hostDocumentElement.$.scrollLeft;

			// Simulating keyboard character input by dispatching a keydown of white-space text.
			var keyEventSimulate = doc.$.createEvent( "KeyEvents" );
			keyEventSimulate.initKeyEvent( 'keypress', true, true, win.$, false,
				false, false, false, 0, 32 );
			doc.$.dispatchEvent( keyEventSimulate );

			if ( scrollTop != hostDocumentElement.$.scrollTop || scrollLeft != hostDocumentElement.$.scrollLeft )
				hostDocument.getWindow().$.scrollTo( scrollLeft, scrollTop );

			// Restore the original document status by placing the cursor before a bogus br created (#5021).
			bodyChildsNum && body.getFirst().remove();
			doc.getBody().appendBogus();
			var nativeRange = new CKEDITOR.dom.range( doc );
			nativeRange.setStartAt( body , CKEDITOR.POSITION_AFTER_START );
			nativeRange.select();
		}
	}

	/**
	 *  Auto-fixing block-less content by wrapping paragraph (#3190), prevent
	 *  non-exitable-block by padding extra br.(#3189)
	 */
	function onSelectionChangeFixBody( evt )
	{
		var editor = evt.editor,
			path = evt.data.path,
			blockLimit = path.blockLimit,
			selection = evt.data.selection,
			range = selection.getRanges()[0],
			body = editor.document.getBody(),
			enterMode = editor.config.enterMode;

		if ( CKEDITOR.env.gecko )
		{
			activateEditing( editor );

			// Ensure bogus br could help to move cursor (out of styles) to the end of block. (#7041)
			var pathBlock = path.block || path.blockLimit,
				lastNode = pathBlock && pathBlock.getLast( isNotEmpty );

			// Check some specialities of the current path block:
			// 1. It is really displayed as block; (#7221)
			// 2. It doesn't end with one inner block; (#7467)
			// 3. It doesn't have bogus br yet.
			if ( pathBlock
					&& pathBlock.isBlockBoundary()
					&& !( lastNode && lastNode.type == CKEDITOR.NODE_ELEMENT && lastNode.isBlockBoundary() )
					&& !pathBlock.is( 'pre' )
					&& !pathBlock.getBogus() )
			{
				pathBlock.appendBogus();
			}
		}

		// When we're in block enter mode, a new paragraph will be established
		// to encapsulate inline contents right under body. (#3657)
		if ( editor.config.autoParagraph !== false
				&& enterMode != CKEDITOR.ENTER_BR
				&& range.collapsed
				&& blockLimit.getName() == 'body'
				&& !path.block )
		{
			var fixedBlock = range.fixBlock( true,
					editor.config.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p'  );

			// For IE, we should remove any filler node which was introduced before.
			if ( CKEDITOR.env.ie )
			{
				var first = fixedBlock.getFirst( isNotEmpty );
				first && isNbsp( first ) && first.remove();
			}

			// If the fixed block is actually blank and is already followed by an exitable blank
			// block, we should revert the fix and move into the existed one. (#3684)
			if ( isBlankParagraph( fixedBlock ) )
			{
				var element = fixedBlock.getNext( isNotWhitespace );
				if ( element &&
					 element.type == CKEDITOR.NODE_ELEMENT &&
					 !nonEditable( element ) )
				{
					range.moveToElementEditStart( element );
					fixedBlock.remove();
				}
				else
				{
					element = fixedBlock.getPrevious( isNotWhitespace );
					if ( element &&
						 element.type == CKEDITOR.NODE_ELEMENT &&
						 !nonEditable( element ) )
					{
						range.moveToElementEditEnd( element );
						fixedBlock.remove();
					}
				}
			}

			range.select();
			// Cancel this selection change in favor of the next (correct).  (#6811)
			evt.cancel();
		}

		// Browsers are incapable of moving cursor out of certain block elements (e.g. table, div, pre)
		// at the end of document, makes it unable to continue adding content, we have to make this
		// easier by opening an new empty paragraph.
		var testRange = new CKEDITOR.dom.range( editor.document );
		testRange.moveToElementEditEnd( editor.document.getBody() );
		var testPath = new CKEDITOR.dom.elementPath( testRange.startContainer );
		if ( !testPath.blockLimit.is( 'body') )
		{
			var paddingBlock;
			if ( enterMode != CKEDITOR.ENTER_BR )
				paddingBlock = body.append( editor.document.createElement( enterMode == CKEDITOR.ENTER_P ? 'p' : 'div' ) );
			else
				paddingBlock = body;

			if ( !CKEDITOR.env.ie )
				paddingBlock.appendBogus();
		}
	}

	CKEDITOR.plugins.add( 'wysiwygarea',
	{
		requires : [ 'editingblock' ],

		init : function( editor )
		{
			var fixForBody = ( editor.config.enterMode != CKEDITOR.ENTER_BR && editor.config.autoParagraph !== false )
				? editor.config.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p' : false;

			var frameLabel = editor.lang.editorTitle.replace( '%1', editor.name ),
				frameDesc = editor.lang.editorHelp;

			if ( CKEDITOR.env.ie )
				frameLabel += ', ' + frameDesc;

			var win = CKEDITOR.document.getWindow();
			var contentDomReadyHandler;
			editor.on( 'editingBlockReady', function()
				{
					var mainElement,
						iframe,
						isLoadingData,
						isPendingFocus,
						frameLoaded,
						fireMode,
						onResize;


					// Support for custom document.domain in IE.
					var isCustomDomain = CKEDITOR.env.isCustomDomain();

					// Creates the iframe that holds the editable document.
					var createIFrame = function( data )
					{
						if ( iframe )
							iframe.remove();

						var src =
							'document.open();' +

							// The document domain must be set any time we
							// call document.open().
							( isCustomDomain ? ( 'document.domain="' + document.domain + '";' ) : '' ) +

							'document.close();';

						// With IE, the custom domain has to be taken care at first,
						// for other browers, the 'src' attribute should be left empty to
						// trigger iframe's 'load' event.
  						src =
							CKEDITOR.env.air ?
								'javascript:void(0)' :
							CKEDITOR.env.ie ?
								'javascript:void(function(){' + encodeURIComponent( src ) + '}())'
							:
								'';

						var labelId = CKEDITOR.tools.getNextId();
						iframe = CKEDITOR.dom.element.createFromHtml( '<iframe' +
  							' style="width:100%;height:100%"' +
  							' frameBorder="0"' +
  							' aria-describedby="' + labelId + '"' +
							' title="' + frameLabel + '"' +
  							' src="' + src + '"' +
							' tabIndex="' + ( CKEDITOR.env.webkit? -1 : editor.tabIndex ) + '"' +
  							' allowTransparency="true"' +
  							'></iframe>' );

						// Running inside of Firefox chrome the load event doesn't bubble like in a normal page (#5689)
						if ( document.location.protocol == 'chrome:' )
							CKEDITOR.event.useCapture = true;

						// With FF, it's better to load the data on iframe.load. (#3894,#4058)
						iframe.on( 'load', function( ev )
							{
								frameLoaded = 1;
								ev.removeListener();

								var doc = iframe.getFrameDocument();
								doc.write( data );

								CKEDITOR.env.air && contentDomReady( doc.getWindow().$ );
							});

						// Reset adjustment back to default (#5689)
						if ( document.location.protocol == 'chrome:' )
							CKEDITOR.event.useCapture = false;

						mainElement.append( CKEDITOR.dom.element.createFromHtml(
							'<span id="' + labelId + '" class="cke_voice_label">' + frameDesc + '</span>'));

						mainElement.append( iframe );

						// Webkit: iframe size doesn't auto fit well. (#7360)
						if ( CKEDITOR.env.webkit )
						{
							onResize = function()
							{
								// Hide the iframe to get real size of the holder. (#8941)
								mainElement.setStyle( 'width', '100%' );
								iframe.hide();

								iframe.setSize( 'width', mainElement.getSize( 'width' ) );
								mainElement.removeStyle( 'width' );
								iframe.show();
							};

							win.on( 'resize', onResize );
						}
					};

					// The script that launches the bootstrap logic on 'domReady', so the document
					// is fully editable even before the editing iframe is fully loaded (#4455).
					contentDomReadyHandler = CKEDITOR.tools.addFunction( contentDomReady );
					var activationScript =
						'<script id="cke_actscrpt" type="text/javascript" data-cke-temp="1">' +
							( isCustomDomain ? ( 'document.domain="' + document.domain + '";' ) : '' ) +
							'window.parent.CKEDITOR.tools.callFunction( ' + contentDomReadyHandler + ', window );' +
						'</script>';

					// Editing area bootstrap code.
					function contentDomReady( domWindow )
					{
						if ( !frameLoaded )
							return;
						frameLoaded = 0;

						editor.fire( 'ariaWidget', iframe );

						var domDocument = domWindow.document,
							body = domDocument.body;

						// Remove this script from the DOM.
						var script = domDocument.getElementById( "cke_actscrpt" );
						script && script.parentNode.removeChild( script );

						body.spellcheck = !editor.config.disableNativeSpellChecker;

						var editable = !editor.readOnly;

						if ( CKEDITOR.env.ie )
						{
							// Don't display the focus border.
							body.hideFocus = true;

							// Disable and re-enable the body to avoid IE from
							// taking the editing focus at startup. (#141 / #523)
							body.disabled = true;
							body.contentEditable = editable;
							body.removeAttribute( 'disabled' );
						}
						else
						{
							// Avoid opening design mode in a frame window thread,
							// which will cause host page scrolling.(#4397)
							setTimeout( function()
							{
								// Prefer 'contentEditable' instead of 'designMode'. (#3593)
								if ( CKEDITOR.env.gecko && CKEDITOR.env.version >= 10900
										|| CKEDITOR.env.opera )
									domDocument.$.body.contentEditable = editable;
								else if ( CKEDITOR.env.webkit )
									domDocument.$.body.parentNode.contentEditable = editable;
								else
									domDocument.$.designMode = editable? 'off' : 'on';
							}, 0 );
						}

						editable && CKEDITOR.env.gecko && CKEDITOR.tools.setTimeout( activateEditing, 0, null, editor );

						domWindow	= editor.window	= new CKEDITOR.dom.window( domWindow );
						domDocument	= editor.document	= new CKEDITOR.dom.document( domDocument );

						editable && domDocument.on( 'dblclick', function( evt )
						{
							var element = evt.data.getTarget(),
								data = { element : element, dialog : '' };
							editor.fire( 'doubleclick', data );
							data.dialog && editor.openDialog( data.dialog );
						});

						// Prevent automatic submission in IE #6336
						CKEDITOR.env.ie && domDocument.on( 'click', function( evt )
						{
							var element = evt.data.getTarget();
							if ( element.is( 'input' ) )
							{
								var type = element.getAttribute( 'type' );
								if ( type == 'submit' || type == 'reset' )
									evt.data.preventDefault();
							}
						});

						// Gecko/Webkit need some help when selecting control type elements. (#3448)
						if ( !( CKEDITOR.env.ie || CKEDITOR.env.opera ) )
						{
							domDocument.on( 'mousedown', function( ev )
							{
								var control = ev.data.getTarget();
								if ( control.is( 'img', 'hr', 'input', 'textarea', 'select' ) )
									editor.getSelection().selectElement( control );
							} );
						}

						if ( CKEDITOR.env.gecko )
						{
							domDocument.on( 'mouseup', function( ev )
							{
								if ( ev.data.$.button == 2 )
								{
									var target = ev.data.getTarget();

									// Prevent right click from selecting an empty block even
									// when selection is anchored inside it. (#5845)
									if ( !target.getOuterHtml().replace( emptyParagraphRegexp, '' ) )
									{
										var range = new CKEDITOR.dom.range( domDocument );
										range.moveToElementEditStart( target );
										range.select( true );
									}
								}
							} );
						}

						// Prevent the browser opening links in read-only blocks. (#6032)
						domDocument.on( 'click', function( ev )
							{
								ev = ev.data;
								if ( ev.getTarget().is( 'a' ) && ev.$.button != 2 )
									ev.preventDefault();
							});

						// Webkit: avoid from editing form control elements content.
						if ( CKEDITOR.env.webkit )
						{
							// Mark that cursor will right blinking (#7113).
							domDocument.on( 'mousedown', function() { wasFocused = 1; } );
							// Prevent from tick checkbox/radiobox/select
							domDocument.on( 'click', function( ev )
							{
								if ( ev.data.getTarget().is( 'input', 'select' ) )
									ev.data.preventDefault();
							} );

							// Prevent from editig textfield/textarea value.
							domDocument.on( 'mouseup', function( ev )
							{
								if ( ev.data.getTarget().is( 'input', 'textarea' ) )
									ev.data.preventDefault();
							} );
						}

						var focusTarget = CKEDITOR.env.ie ? iframe : domWindow;
						focusTarget.on( 'blur', function()
							{
								editor.focusManager.blur();
							});

						var wasFocused;

						focusTarget.on( 'focus', function()
							{
								var doc = editor.document;

								if ( CKEDITOR.env.gecko || CKEDITOR.env.opera )
									doc.getBody().focus();
								// Webkit needs focus for the first time on the HTML element. (#6153)
								else if ( CKEDITOR.env.webkit )
								{
									if ( !wasFocused )
									{
										editor.document.getDocumentElement().focus();
										wasFocused = 1;
									}
								}

								editor.focusManager.focus();
							});

						var keystrokeHandler = editor.keystrokeHandler;
						// Prevent backspace from navigating off the page.
						keystrokeHandler.blockedKeystrokes[ 8 ] = !editable;
						keystrokeHandler.attach( domDocument );

						domDocument.getDocumentElement().addClass( domDocument.$.compatMode );
						// Override keystroke behaviors.
						editable && domDocument.on( 'keydown', function( evt )
						{
							var keyCode = evt.data.getKeystroke();

							// Backspace OR Delete.
							if ( keyCode in { 8 : 1, 46 : 1 } )
							{
								var sel = editor.getSelection(),
									selected = sel.getSelectedElement(),
									range = sel.getRanges()[ 0 ],
									path = new CKEDITOR.dom.elementPath( range.startContainer ),
									block,
									parent,
									next,
									rtl = keyCode == 8;

								// Override keystrokes which should have deletion behavior
								//  on fully selected element . (#4047) (#7645)
								if ( selected )
								{
									// Make undo snapshot.
									editor.fire( 'saveSnapshot' );

									// Delete any element that 'hasLayout' (e.g. hr,table) in IE8 will
									// break up the selection, safely manage it here. (#4795)
									range.moveToPosition( selected, CKEDITOR.POSITION_BEFORE_START );
									// Remove the control manually.
									selected.remove();
									range.select();

									editor.fire( 'saveSnapshot' );

									evt.data.preventDefault();
								}
								else
								{
									// Handle the following special cases: (#6217)
									// 1. Del/Backspace key before/after table;
									// 2. Backspace Key after start of table.
									if ( ( block = path.block ) &&
										 range[ rtl ? 'checkStartOfBlock' : 'checkEndOfBlock' ]() &&
										 ( next = block[ rtl ? 'getPrevious' : 'getNext' ]( notWhitespaceEval ) ) &&
										 next.is( 'table' ) )
									{
										editor.fire( 'saveSnapshot' );

										// Remove the current empty block.
										if ( range[ rtl ? 'checkEndOfBlock' : 'checkStartOfBlock' ]() )
											block.remove();

										// Move cursor to the beginning/end of table cell.
										range[ 'moveToElementEdit' + ( rtl ? 'End' : 'Start' ) ]( next );
										range.select();

										editor.fire( 'saveSnapshot' );

										evt.data.preventDefault();
									}
									else if ( path.blockLimit.is( 'td' ) &&
											  ( parent = path.blockLimit.getAscendant( 'table' ) ) &&
											  range.checkBoundaryOfElement( parent, rtl ? CKEDITOR.START : CKEDITOR.END ) &&
											  ( next = parent[ rtl ? 'getPrevious' : 'getNext' ]( notWhitespaceEval ) ) )
									{
										editor.fire( 'saveSnapshot' );

										// Move cursor to the end of previous block.
										range[ 'moveToElementEdit' + ( rtl ? 'End' : 'Start' ) ]( next );

										// Remove any previous empty block.
										if ( range.checkStartOfBlock() && range.checkEndOfBlock() )
											next.remove();
										else
											range.select();

										editor.fire( 'saveSnapshot' );

										evt.data.preventDefault();
									}

								}
							}

							// PageUp OR PageDown
							if ( keyCode == 33 || keyCode == 34 )
							{
								if ( CKEDITOR.env.gecko )
								{
									var body = domDocument.getBody();

									// Page up/down cause editor selection to leak
									// outside of editable thus we try to intercept
									// the behavior, while it affects only happen
									// when editor contents are not overflowed. (#7955)
									if ( domWindow.$.innerHeight > body.$.offsetHeight )
									{
										range = new CKEDITOR.dom.range( domDocument );
										range[ keyCode == 33 ? 'moveToElementEditStart' : 'moveToElementEditEnd']( body );
										range.select();
										evt.data.preventDefault();
									}
								}

							}
						} );

						// PageUp/PageDown scrolling is broken in document
						// with standard doctype, manually fix it. (#4736)
						if ( CKEDITOR.env.ie && domDocument.$.compatMode == 'CSS1Compat' )
						{
							var pageUpDownKeys = { 33 : 1, 34 : 1 };
							domDocument.on( 'keydown', function( evt )
							{
								if ( evt.data.getKeystroke() in pageUpDownKeys )
								{
									setTimeout( function ()
									{
										editor.getSelection().scrollIntoView();
									}, 0 );
								}
							} );
						}

						// Prevent IE from leaving new paragraph after deleting all contents in body. (#6966)
						if ( CKEDITOR.env.ie && editor.config.enterMode != CKEDITOR.ENTER_P )
						{
							domDocument.on( 'selectionchange', function()
							{
								var body = domDocument.getBody(),
									sel = editor.getSelection(),
									range = sel && sel.getRanges()[ 0 ];

								if ( range && body.getHtml().match( /^<p>&nbsp;<\/p>$/i )
									&& range.startContainer.equals( body ) )
								{
									// Avoid the ambiguity from a real user cursor position.
									setTimeout( function ()
									{
										range = editor.getSelection().getRanges()[ 0 ];
										if ( !range.startContainer.equals ( 'body' ) )
										{
											body.getFirst().remove( 1 );
											range.moveToElementEditEnd( body );
											range.select( 1 );
										}
									}, 0 );
								}
							});
						}

						// Adds the document body as a context menu target.
						if ( editor.contextMenu )
							editor.contextMenu.addTarget( domDocument, editor.config.browserContextMenuOnCtrl !== false );

						setTimeout( function()
							{
								editor.fire( 'contentDom' );

								if ( fireMode )
								{
									editor.mode = 'wysiwyg';
									editor.fire( 'mode', { previousMode : editor._.previousMode } );
									fireMode = false;
								}

								isLoadingData = false;

								if ( isPendingFocus )
								{
									editor.focus();
									isPendingFocus = false;
								}
								setTimeout( function()
								{
									editor.fire( 'dataReady' );
								}, 0 );

								// Enable dragging of position:absolute elements in IE.
								try { editor.document.$.execCommand ( '2D-position', false, true); } catch(e) {}

								// IE, Opera and Safari may not support it and throw errors.
								try { editor.document.$.execCommand( 'enableInlineTableEditing', false, !editor.config.disableNativeTableHandles ); } catch(e) {}
								if ( editor.config.disableObjectResizing )
								{
									try
									{
										editor.document.$.execCommand( 'enableObjectResizing', false, false );
									}
									catch(e)
									{
										// For browsers in which the above method failed, we can cancel the resizing on the fly (#4208)
										editor.document.getBody().on( CKEDITOR.env.ie ? 'resizestart' : 'resize', function( evt )
										{
											evt.data.preventDefault();
										});
									}
								}

								/*
								 * IE BUG: IE might have rendered the iframe with invisible contents.
								 * (#3623). Push some inconsequential CSS style changes to force IE to
								 * refresh it.
								 *
								 * Also, for some unknown reasons, short timeouts (e.g. 100ms) do not
								 * fix the problem. :(
								 */
								if ( CKEDITOR.env.ie )
								{
									setTimeout( function()
										{
											if ( editor.document )
											{
												var $body = editor.document.$.body;
												$body.runtimeStyle.marginBottom = '0px';
												$body.runtimeStyle.marginBottom = '';
											}
										}, 1000 );
								}
							},
							0 );
					}

					editor.addMode( 'wysiwyg',
						{
							load : function( holderElement, data, isSnapshot )
							{
								mainElement = holderElement;

								if ( CKEDITOR.env.ie && CKEDITOR.env.quirks )
									holderElement.setStyle( 'position', 'relative' );

								// The editor data "may be dirty" after this
								// point.
								editor.mayBeDirty = true;

								fireMode = true;

								if ( isSnapshot )
									this.loadSnapshotData( data );
								else
									this.loadData( data );
							},

							loadData : function( data )
							{
								isLoadingData = true;
								editor._.dataStore = { id : 1 };

								var config = editor.config,
									fullPage = config.fullPage,
									docType = config.docType;

								// Build the additional stuff to be included into <head>.
								var headExtra =
									'<style type="text/css" data-cke-temp="1">' +
										editor._.styles.join( '\n' ) +
									'</style>';

								!fullPage && ( headExtra =
									CKEDITOR.tools.buildStyleHtml( editor.config.contentsCss ) +
									headExtra );

								var baseTag = config.baseHref ? '<base href="' + config.baseHref + '" data-cke-temp="1" />' : '';

								if ( fullPage )
								{
									// Search and sweep out the doctype declaration.
									data = data.replace( /<!DOCTYPE[^>]*>/i, function( match )
										{
											editor.docType = docType = match;
											return '';
										}).replace( /<\?xml\s[^\?]*\?>/i, function( match )
										{
											editor.xmlDeclaration = match;
											return '';
										});
								}

								// Get the HTML version of the data.
								if ( editor.dataProcessor )
									data = editor.dataProcessor.toHtml( data, fixForBody );

								if ( fullPage )
								{
									// Check if the <body> tag is available.
									if ( !(/<body[\s|>]/).test( data ) )
										data = '<body>' + data;

									// Check if the <html> tag is available.
									if ( !(/<html[\s|>]/).test( data ) )
										data = '<html>' + data + '</html>';

									// Check if the <head> tag is available.
									if ( !(/<head[\s|>]/).test( data ) )
										data = data.replace( /<html[^>]*>/, '$&<head><title></title></head>' ) ;
									else if ( !(/<title[\s|>]/).test( data ) )
										data = data.replace( /<head[^>]*>/, '$&<title></title>' ) ;

									// The base must be the first tag in the HEAD, e.g. to get relative
									// links on styles.
									baseTag && ( data = data.replace( /<head>/, '$&' + baseTag ) );

									// Inject the extra stuff into <head>.
									// Attention: do not change it before testing it well. (V2)
									// This is tricky... if the head ends with <meta ... content type>,
									// Firefox will break. But, it works if we place our extra stuff as
									// the last elements in the HEAD.
									data = data.replace( /<\/head\s*>/, headExtra + '$&' );

									// Add the DOCTYPE back to it.
									data = docType + data;
								}
								else
								{
									data =
										config.docType +
										'<html dir="' + config.contentsLangDirection + '"' +
											' lang="' + ( config.contentsLanguage || editor.langCode ) + '">' +
										'<head>' +
											'<title>' + frameLabel + '</title>' +
											baseTag +
											headExtra +
										'</head>' +
										'<body' + ( config.bodyId ? ' id="' + config.bodyId + '"' : '' ) +
												  ( config.bodyClass ? ' class="' + config.bodyClass + '"' : '' ) +
												  '>' +
											data +
										'</html>';
								}

								// Distinguish bogus to normal BR at the end of document for Mozilla. (#5293).
								if ( CKEDITOR.env.gecko )
									data = data.replace( /<br \/>(?=\s*<\/(:?html|body)>)/, '$&<br type="_moz" />' );

								data += activationScript;


								// The iframe is recreated on each call of setData, so we need to clear DOM objects
								this.onDispose();
								createIFrame( data );
							},

							getData : function()
							{
								var config = editor.config,
									fullPage = config.fullPage,
									docType = fullPage && editor.docType,
									xmlDeclaration = fullPage && editor.xmlDeclaration,
									doc = iframe.getFrameDocument();

								var data = fullPage
									? doc.getDocumentElement().getOuterHtml()
									: doc.getBody().getHtml();

								// BR at the end of document is bogus node for Mozilla. (#5293).
								if ( CKEDITOR.env.gecko )
									data = data.replace( /<br>(?=\s*(:?$|<\/body>))/, '' );

								if ( editor.dataProcessor )
									data = editor.dataProcessor.toDataFormat( data, fixForBody );

								// Reset empty if the document contains only one empty paragraph.
								if ( config.ignoreEmptyParagraph )
									data = data.replace( emptyParagraphRegexp, function( match, lookback ) { return lookback; } );

								if ( xmlDeclaration )
									data = xmlDeclaration + '\n' + data;
								if ( docType )
									data = docType + '\n' + data;

								return data;
							},

							getSnapshotData : function()
							{
								return iframe.getFrameDocument().getBody().getHtml();
							},

							loadSnapshotData : function( data )
							{
								iframe.getFrameDocument().getBody().setHtml( data );
							},

							onDispose : function()
							{
								if ( !editor.document )
									return;

								editor.document.getDocumentElement().clearCustomData();
								editor.document.getBody().clearCustomData();

								editor.window.clearCustomData();
								editor.document.clearCustomData();

								iframe.clearCustomData();

								/*
								* IE BUG: When destroying editor DOM with the selection remains inside
								* editing area would break IE7/8's selection system, we have to put the editing
								* iframe offline first. (#3812 and #5441)
								*/
								iframe.remove();
							},

							unload : function( holderElement )
							{
								this.onDispose();

								if ( onResize )
									win.removeListener( 'resize', onResize );

								editor.window = editor.document = iframe = mainElement = isPendingFocus = null;

								editor.fire( 'contentDomUnload' );
							},

							focus : function()
							{
								var win = editor.window;

								if ( isLoadingData )
									isPendingFocus = true;
								else if ( win )
								{
									var sel = editor.getSelection(),
										ieSel = sel && sel.getNative();

									// IE considers control-type element as separate
									// focus host when selected, avoid destroying the
									// selection in such case. (#5812) (#8949)
									if ( ieSel && ieSel.type == 'Control' )
										return;

									// AIR needs a while to focus when moving from a link.
									CKEDITOR.env.air ? setTimeout( function () { win.focus(); }, 0 ) : win.focus();
									editor.selectionChange();
								}
							}
						});

					editor.on( 'insertHtml', onInsert( doInsertHtml ) , null, null, 20 );
					editor.on( 'insertElement', onInsert( doInsertElement ), null, null, 20 );
					editor.on( 'insertText', onInsert( doInsertText ), null, null, 20 );
					// Auto fixing on some document structure weakness to enhance usabilities. (#3190 and #3189)
					editor.on( 'selectionChange', function( evt )
					{
						if ( editor.readOnly )
							return;

						var sel = editor.getSelection();
						// Do it only when selection is not locked. (#8222)
						if ( sel && !sel.isLocked )
						{
							var isDirty = editor.checkDirty();
							editor.fire( 'saveSnapshot', { contentOnly : 1 } );
							onSelectionChangeFixBody.call( this, evt );
							editor.fire( 'updateSnapshot' );
							!isDirty && editor.resetDirty();
						}

					}, null, null, 1 );
				});

			editor.on( 'contentDom', function()
				{
					var title = editor.document.getElementsByTag( 'title' ).getItem( 0 );
					title.data( 'cke-title', editor.document.$.title );

					// [IE] JAWS will not recognize the aria label we used on the iframe
					// unless the frame window title string is used as the voice label,
					// backup the original one and restore it on output.
					CKEDITOR.env.ie && ( editor.document.$.title = frameLabel );
				});

			editor.on( 'readOnly', function()
				{
					if ( editor.mode == 'wysiwyg' )
					{
						// Symply reload the wysiwyg area. It'll take care of read-only.
						var wysiwyg = editor.getMode();
						wysiwyg.loadData( wysiwyg.getData() );
					}
				});

			// IE>=8 stricts mode doesn't have 'contentEditable' in effect
			// on element unless it has layout. (#5562)
			if ( CKEDITOR.document.$.documentMode >= 8 )
			{
				editor.addCss( 'html.CSS1Compat [contenteditable=false]{ min-height:0 !important;}' );

				var selectors = [];
				for ( var tag in CKEDITOR.dtd.$removeEmpty )
					selectors.push( 'html.CSS1Compat ' + tag + '[contenteditable=false]' );
				editor.addCss( selectors.join( ',' ) + '{ display:inline-block;}' );
			}
			// Set the HTML style to 100% to have the text cursor in affect (#6341)
			else if ( CKEDITOR.env.gecko )
			{
				editor.addCss( 'html { height: 100% !important; }' );
				editor.addCss( 'img:-moz-broken { -moz-force-broken-image-icon : 1;	min-width : 24px; min-height : 24px; }' );
			}
			// Remove the margin to avoid mouse confusion. (#8835)
			else if ( CKEDITOR.env.ie && CKEDITOR.env.version < 8 && editor.config.contentsLangDirection == 'ltr' )
				editor.addCss( 'body{margin-right:0;}' );

			/* #3658: [IE6] Editor document has horizontal scrollbar on long lines
			To prevent this misbehavior, we show the scrollbar always */
			/* #6341: The text cursor must be set on the editor area. */
			/* #6632: Avoid having "text" shape of cursor in IE7 scrollbars.*/
			editor.addCss( 'html {	_overflow-y: scroll; cursor: text;	*cursor:auto;}' );
			// Use correct cursor for these elements
			editor.addCss( 'img, input, textarea { cursor: default;}' );

			// Disable form elements editing mode provided by some browers. (#5746)
			editor.on( 'insertElement', function ( evt )
			{
				var element = evt.data;
				if ( element.type == CKEDITOR.NODE_ELEMENT
						&& ( element.is( 'input' ) || element.is( 'textarea' ) ) )
				{
					// We should flag that the element was locked by our code so
					// it'll be editable by the editor functions (#6046).
					var readonly = element.getAttribute( 'contenteditable' ) == 'false';
					if ( !readonly )
					{
						element.data( 'cke-editable', element.hasAttribute( 'contenteditable' ) ? 'true' : '1' );
						element.setAttribute( 'contenteditable', false );
					}
				}
			});

		}
	});

	// Fixing Firefox 'Back-Forward Cache' break design mode. (#4514)
	if ( CKEDITOR.env.gecko )
	{
		(function()
		{
			var body = document.body;

			if ( !body )
				window.addEventListener( 'load', arguments.callee, false );
			else
			{
				var currentHandler = body.getAttribute( 'onpageshow' );
				body.setAttribute( 'onpageshow', ( currentHandler ? currentHandler + ';' : '') +
							'event.persisted && (function(){' +
								'var allInstances = CKEDITOR.instances, editor, doc;' +
								'for ( var i in allInstances )' +
								'{' +
								'	editor = allInstances[ i ];' +
								'	doc = editor.document;' +
								'	if ( doc )' +
								'	{' +
								'		doc.$.designMode = "off";' +
								'		doc.$.designMode = "on";' +
								'	}' +
								'}' +
						'})();' );
			}
		} )();

	}
})();

/**
 * Disables the ability of resize objects (image and tables) in the editing
 * area.
 * @type Boolean
 * @default false
 * @example
 * config.disableObjectResizing = true;
 */
CKEDITOR.config.disableObjectResizing = false;

/**
 * Disables the "table tools" offered natively by the browser (currently
 * Firefox only) to make quick table editing operations, like adding or
 * deleting rows and columns.
 * @type Boolean
 * @default true
 * @example
 * config.disableNativeTableHandles = false;
 */
CKEDITOR.config.disableNativeTableHandles = true;

/**
 * Disables the built-in words spell checker if browser provides one.<br /><br />
 *
 * <strong>Note:</strong> Although word suggestions provided by browsers (natively) will not appear in CKEditor's default context menu,
 * users can always reach the native context menu by holding the <em>Ctrl</em> key when right-clicking if {@link CKEDITOR.config.browserContextMenuOnCtrl}
 * is enabled or you're simply not using the context menu plugin.
 *
 * @type Boolean
 * @default true
 * @example
 * config.disableNativeSpellChecker = false;
 */
CKEDITOR.config.disableNativeSpellChecker = true;

/**
 * Whether the editor must output an empty value ("") if it's contents is made
 * by an empty paragraph only.
 * @type Boolean
 * @default true
 * @example
 * config.ignoreEmptyParagraph = false;
 */
CKEDITOR.config.ignoreEmptyParagraph = true;

/**
 * Fired when data is loaded and ready for retrieval in an editor instance.
 * @name CKEDITOR.editor#dataReady
 * @event
 */

/**
 * Whether automatically create wrapping blocks around inline contents inside document body,
 * this helps to ensure the integrality of the block enter mode.
 * <strong>Note:</strong> Changing the default value might introduce unpredictable usability issues.
 * @name CKEDITOR.config.autoParagraph
 * @since 3.6
 * @type Boolean
 * @default true
 * @example
 * config.autoParagraph = false;
 */

/**
 * Fired when some elements are added to the document
 * @name CKEDITOR.editor#ariaWidget
 * @event
 * @param {Object} element The element being added
 */
