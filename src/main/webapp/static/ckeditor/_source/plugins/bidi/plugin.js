/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	var guardElements = { table:1, ul:1, ol:1, blockquote:1, div:1 },
		directSelectionGuardElements = {},
		// All guard elements which can have a direction applied on them.
		allGuardElements = {};
	CKEDITOR.tools.extend( directSelectionGuardElements, guardElements, { tr:1, p:1, div:1, li:1 } );
	CKEDITOR.tools.extend( allGuardElements, directSelectionGuardElements, { td:1 } );

	function onSelectionChange( e )
	{
		setToolbarStates( e );
		handleMixedDirContent( e );
	}

	function setToolbarStates( evt )
	{
		var editor = evt.editor,
			path = evt.data.path;

		if ( editor.readOnly )
			return;

		var useComputedState = editor.config.useComputedState,
			selectedElement;

		useComputedState = useComputedState === undefined || useComputedState;

		// We can use computedState provided by the browser or traverse parents manually.
		if ( !useComputedState )
			selectedElement = getElementForDirection( path.lastElement );

		selectedElement = selectedElement || path.block || path.blockLimit;

		// If we're having BODY here, user probably done CTRL+A, let's try to get the enclosed node, if any.
		if ( selectedElement.is( 'body' ) )
		{
			var enclosedNode = editor.getSelection().getRanges()[ 0 ].getEnclosedNode();
			enclosedNode && enclosedNode.type == CKEDITOR.NODE_ELEMENT && ( selectedElement = enclosedNode );
		}

		if ( !selectedElement  )
			return;

		var selectionDir = useComputedState ?
			selectedElement.getComputedStyle( 'direction' ) :
			selectedElement.getStyle( 'direction' ) || selectedElement.getAttribute( 'dir' );

		editor.getCommand( 'bidirtl' ).setState( selectionDir == 'rtl' ? CKEDITOR.TRISTATE_ON : CKEDITOR.TRISTATE_OFF );
		editor.getCommand( 'bidiltr' ).setState( selectionDir == 'ltr' ? CKEDITOR.TRISTATE_ON : CKEDITOR.TRISTATE_OFF );
	}

	function handleMixedDirContent( evt )
	{
		var editor = evt.editor,
			directionNode = evt.data.path.block || evt.data.path.blockLimit;

		editor.fire( 'contentDirChanged', directionNode ? directionNode.getComputedStyle( 'direction' ) : editor.lang.dir );
	}

	/**
	 * Returns element with possibility of applying the direction.
	 * @param node
	 */
	function getElementForDirection( node )
	{
		while ( node && !( node.getName() in allGuardElements || node.is( 'body' ) ) )
		{
			var parent = node.getParent();
			if ( !parent )
				break;

			node = parent;
		}

		return node;
	}

	function switchDir( element, dir, editor, database )
	{
		if ( element.isReadOnly() )
			return;

		// Mark this element as processed by switchDir.
		CKEDITOR.dom.element.setMarker( database, element, 'bidi_processed', 1 );

		// Check whether one of the ancestors has already been styled.
		var parent = element;
		while ( ( parent = parent.getParent() ) && !parent.is( 'body' ) )
		{
			if ( parent.getCustomData( 'bidi_processed' ) )
			{
				// Ancestor style must dominate.
				element.removeStyle( 'direction' );
				element.removeAttribute( 'dir' );
				return;
			}
		}

		var useComputedState = ( 'useComputedState' in editor.config ) ? editor.config.useComputedState : 1;

		var elementDir = useComputedState ? element.getComputedStyle( 'direction' )
			: element.getStyle( 'direction' ) || element.hasAttribute( 'dir' );

		// Stop if direction is same as present.
		if ( elementDir == dir )
			return;

		// Clear direction on this element.
		element.removeStyle( 'direction' );

		// Do the second check when computed state is ON, to check
		// if we need to apply explicit direction on this element.
		if ( useComputedState )
		{
			element.removeAttribute( 'dir' );
			if ( dir != element.getComputedStyle( 'direction' ) )
				element.setAttribute( 'dir', dir );
		}
		else
			// Set new direction for this element.
			element.setAttribute( 'dir', dir );

		editor.forceNextSelectionCheck();

		return;
	}

	function getFullySelected( range, elements, enterMode )
	{
		var ancestor = range.getCommonAncestor( false, true );

		range = range.clone();
		range.enlarge( enterMode == CKEDITOR.ENTER_BR ?
				CKEDITOR.ENLARGE_LIST_ITEM_CONTENTS
				: CKEDITOR.ENLARGE_BLOCK_CONTENTS );

		if ( range.checkBoundaryOfElement( ancestor, CKEDITOR.START )
				&& range.checkBoundaryOfElement( ancestor, CKEDITOR.END ) )
		{
			var parent;
			while ( ancestor && ancestor.type == CKEDITOR.NODE_ELEMENT
					&& ( parent = ancestor.getParent() )
					&& parent.getChildCount() == 1
					&& !( ancestor.getName() in elements ) )
				ancestor = parent;

			return ancestor.type == CKEDITOR.NODE_ELEMENT
					&& ( ancestor.getName() in elements )
					&& ancestor;
		}
	}

	function bidiCommand( dir )
	{
		return function( editor )
		{
			var selection = editor.getSelection(),
				enterMode = editor.config.enterMode,
				ranges = selection.getRanges();

			if ( ranges && ranges.length )
			{
				var database = {};

				// Creates bookmarks for selection, as we may split some blocks.
				var bookmarks = selection.createBookmarks();

				var rangeIterator = ranges.createIterator(),
					range,
					i = 0;

				while ( ( range = rangeIterator.getNextRange( 1 ) ) )
				{
					// Apply do directly selected elements from guardElements.
					var selectedElement = range.getEnclosedNode();

					// If this is not our element of interest, apply to fully selected elements from guardElements.
					if ( !selectedElement || selectedElement
							&& !( selectedElement.type == CKEDITOR.NODE_ELEMENT && selectedElement.getName() in directSelectionGuardElements )
						)
						selectedElement = getFullySelected( range, guardElements, enterMode );

					selectedElement && switchDir( selectedElement, dir, editor, database );

					var iterator,
						block;

					// Walker searching for guardElements.
					var walker = new CKEDITOR.dom.walker( range );

					var start = bookmarks[ i ].startNode,
						end = bookmarks[ i++ ].endNode;

					walker.evaluator = function( node )
					{
						return !! ( node.type == CKEDITOR.NODE_ELEMENT
								&& node.getName() in guardElements
								&& !( node.getName() == ( enterMode == CKEDITOR.ENTER_P ? 'p' : 'div' )
									&& node.getParent().type == CKEDITOR.NODE_ELEMENT
									&& node.getParent().getName() == 'blockquote' )
								// Element must be fully included in the range as well. (#6485).
								&& node.getPosition( start ) & CKEDITOR.POSITION_FOLLOWING
								&& ( ( node.getPosition( end ) & CKEDITOR.POSITION_PRECEDING + CKEDITOR.POSITION_CONTAINS ) == CKEDITOR.POSITION_PRECEDING ) );
					};

					while ( ( block = walker.next() ) )
						switchDir( block, dir, editor, database );

					iterator = range.createIterator();
					iterator.enlargeBr = enterMode != CKEDITOR.ENTER_BR;

					while ( ( block = iterator.getNextParagraph( enterMode == CKEDITOR.ENTER_P ? 'p' : 'div' ) ) )
						switchDir( block, dir, editor, database );
					}

				CKEDITOR.dom.element.clearAllMarkers( database );

				editor.forceNextSelectionCheck();
				// Restore selection position.
				selection.selectBookmarks( bookmarks );

				editor.focus();
			}
		};
	}

	CKEDITOR.plugins.add( 'bidi',
	{
		requires : [ 'styles', 'button' ],

		init : function( editor )
		{
			// All buttons use the same code to register. So, to avoid
			// duplications, let's use this tool function.
			var addButtonCommand = function( buttonName, buttonLabel, commandName, commandExec )
			{
				editor.addCommand( commandName, new CKEDITOR.command( editor, { exec : commandExec }) );

				editor.ui.addButton( buttonName,
					{
						label : buttonLabel,
						command : commandName
					});
			};

			var lang = editor.lang.bidi;

			addButtonCommand( 'BidiLtr', lang.ltr, 'bidiltr', bidiCommand( 'ltr' ) );
			addButtonCommand( 'BidiRtl', lang.rtl, 'bidirtl', bidiCommand( 'rtl' ) );

			editor.on( 'selectionChange', onSelectionChange );
			editor.on( 'contentDom', function()
			{
				editor.document.on( 'dirChanged', function( evt )
				{
					editor.fire( 'dirChanged',
						{
							node : evt.data,
							dir : evt.data.getDirection( 1 )
						} );
				});
			});
		}
	});

	// If the element direction changed, we need to switch the margins of
	// the element and all its children, so it will get really reflected
	// like a mirror. (#5910)
	function isOffline( el )
	{
		var html = el.getDocument().getBody().getParent();
		while ( el )
		{
			if ( el.equals( html ) )
				return false;
			el = el.getParent();
		}
		return true;
	}
	function dirChangeNotifier( org )
	{
		var isAttribute = org == elementProto.setAttribute,
			isRemoveAttribute = org == elementProto.removeAttribute,
			dirStyleRegexp = /\bdirection\s*:\s*(.*?)\s*(:?$|;)/;

		return function( name, val )
		{
			if ( !this.getDocument().equals( CKEDITOR.document ) )
			{
				var orgDir;
				if ( ( name == ( isAttribute || isRemoveAttribute ? 'dir' : 'direction' ) ||
					 name == 'style' && ( isRemoveAttribute || dirStyleRegexp.test( val ) ) ) && !isOffline( this ) )
				{
					orgDir = this.getDirection( 1 );
					var retval = org.apply( this, arguments );
					if ( orgDir != this.getDirection( 1 ) )
					{
						this.getDocument().fire( 'dirChanged', this );
						return retval;
					}
				}
			}

			return org.apply( this, arguments );
		};
	}

	var elementProto = CKEDITOR.dom.element.prototype,
		methods = [ 'setStyle', 'removeStyle', 'setAttribute', 'removeAttribute' ];
	for ( var i = 0; i < methods.length; i++ )
		elementProto[ methods[ i ] ] = CKEDITOR.tools.override( elementProto[ methods [ i ] ], dirChangeNotifier );
})();

/**
 * Fired when the language direction of an element is changed
 * @name CKEDITOR.editor#dirChanged
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {Object} eventData.node The element that is being changed.
 * @param {String} eventData.dir The new direction.
 */

/**
 * Fired when the language direction in the specific cursor position is changed
 * @name CKEDITOR.editor#contentDirChanged
 * @event
 * @param {String} eventData The direction in the current position.
 */
