/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * A lightweight representation of an HTML DOM structure.
 * @constructor
 * @example
 */
CKEDITOR.htmlParser.fragment = function()
{
	/**
	 * The nodes contained in the root of this fragment.
	 * @type Array
	 * @example
	 * var fragment = CKEDITOR.htmlParser.fragment.fromHtml( '<b>Sample</b> Text' );
	 * alert( fragment.children.length );  "2"
	 */
	this.children = [];

	/**
	 * Get the fragment parent. Should always be null.
	 * @type Object
	 * @default null
	 * @example
	 */
	this.parent = null;

	/** @private */
	this._ =
	{
		isBlockLike : true,
		hasInlineStarted : false
	};
};

(function()
{
	// Block-level elements whose internal structure should be respected during
	// parser fixing.
	var nonBreakingBlocks = CKEDITOR.tools.extend( { table:1,ul:1,ol:1,dl:1 }, CKEDITOR.dtd.table, CKEDITOR.dtd.ul, CKEDITOR.dtd.ol, CKEDITOR.dtd.dl );

	// IE < 8 don't output the close tag on definition list items. (#6975)
	var optionalCloseTags = CKEDITOR.env.ie && CKEDITOR.env.version < 8 ? { dd : 1, dt :1 } : {};

	var listBlocks = { ol:1, ul:1 };

	// Dtd of the fragment element, basically it accept anything except for intermediate structure, e.g. orphan <li>.
	var rootDtd = CKEDITOR.tools.extend( {}, { html: 1 }, CKEDITOR.dtd.html, CKEDITOR.dtd.body, CKEDITOR.dtd.head, { style:1,script:1 } );

	function isRemoveEmpty( node )
	{
		// Empty link is to be removed when empty but not anchor. (#7894)
		return node.name == 'a' && node.attributes.href
			|| CKEDITOR.dtd.$removeEmpty[ node.name ];
	}

	/**
	 * Creates a {@link CKEDITOR.htmlParser.fragment} from an HTML string.
	 * @param {String} fragmentHtml The HTML to be parsed, filling the fragment.
	 * @param {Number} [fixForBody=false] Wrap body with specified element if needed.
	 * @param {CKEDITOR.htmlParser.element} contextNode Parse the html as the content of this element.
	 * @returns CKEDITOR.htmlParser.fragment The fragment created.
	 * @example
	 * var fragment = CKEDITOR.htmlParser.fragment.fromHtml( '<b>Sample</b> Text' );
	 * alert( fragment.children[0].name );  "b"
	 * alert( fragment.children[1].value );  " Text"
	 */
	CKEDITOR.htmlParser.fragment.fromHtml = function( fragmentHtml, fixForBody, contextNode )
	{
		var parser = new CKEDITOR.htmlParser(),
			fragment = contextNode || new CKEDITOR.htmlParser.fragment(),
			pendingInline = [],
			pendingBRs = [],
			currentNode = fragment,
		    // Indicate we're inside a <textarea> element, spaces should be touched differently.
			inTextarea = false,
		    // Indicate we're inside a <pre> element, spaces should be touched differently.
			inPre = false;

		function checkPending( newTagName )
		{
			var pendingBRsSent;

			if ( pendingInline.length > 0 )
			{
				for ( var i = 0 ; i < pendingInline.length ; i++ )
				{
					var pendingElement = pendingInline[ i ],
						pendingName = pendingElement.name,
						pendingDtd = CKEDITOR.dtd[ pendingName ],
						currentDtd = currentNode.name && CKEDITOR.dtd[ currentNode.name ];

					if ( ( !currentDtd || currentDtd[ pendingName ] ) && ( !newTagName || !pendingDtd || pendingDtd[ newTagName ] || !CKEDITOR.dtd[ newTagName ] ) )
					{
						if ( !pendingBRsSent )
						{
							sendPendingBRs();
							pendingBRsSent = 1;
						}

						// Get a clone for the pending element.
						pendingElement = pendingElement.clone();

						// Add it to the current node and make it the current,
						// so the new element will be added inside of it.
						pendingElement.parent = currentNode;
						currentNode = pendingElement;

						// Remove the pending element (back the index by one
						// to properly process the next entry).
						pendingInline.splice( i, 1 );
						i--;
					}
					else
					{
						// Some element of the same type cannot be nested, flat them,
						// e.g. <a href="#">foo<a href="#">bar</a></a>. (#7894)
						if ( pendingName == currentNode.name )
							addElement( currentNode, currentNode.parent, 1 ), i--;
					}
				}
			}
		}

		function sendPendingBRs()
		{
			while ( pendingBRs.length )
				addElement( pendingBRs.shift(), currentNode );
		}

		/*
		* Beside of simply append specified element to target, this function also takes
		* care of other dirty lifts like forcing block in body, trimming spaces at
		* the block boundaries etc.
		*
		* @param {Element} element  The element to be added as the last child of {@link target}.
		* @param {Element} target The parent element to relieve the new node.
		* @param {Boolean} [moveCurrent=false] Don't change the "currentNode" global unless
		* there's a return point node specified on the element, otherwise move current onto {@link target} node.
		 */
		function addElement( element, target, moveCurrent )
		{
			// Ignore any element that has already been added.
			if ( element.previous !== undefined )
				return;

			target = target || currentNode || fragment;

			// Current element might be mangled by fix body below,
			// save it for restore later.
			var savedCurrent = currentNode;

			// If the target is the fragment and this inline element can't go inside
			// body (if fixForBody).
			if ( fixForBody && ( !target.type || target.name == 'body' ) )
			{
				var elementName, realElementName;
				if ( element.attributes
					 && ( realElementName =
						  element.attributes[ 'data-cke-real-element-type' ] ) )
					elementName = realElementName;
				else
					elementName =  element.name;

				if ( elementName && !( elementName in CKEDITOR.dtd.$body || elementName == 'body' || element.isOrphan ) )
				{
					// Create a <p> in the fragment.
					currentNode = target;
					parser.onTagOpen( fixForBody, {} );

					// The new target now is the <p>.
					element.returnPoint = target = currentNode;
				}
			}

			// Rtrim empty spaces on block end boundary. (#3585)
			if ( element._.isBlockLike
				 && element.name != 'pre' && element.name != 'textarea' )
			{

				var length = element.children.length,
					lastChild = element.children[ length - 1 ],
					text;
				if ( lastChild && lastChild.type == CKEDITOR.NODE_TEXT )
				{
					if ( !( text = CKEDITOR.tools.rtrim( lastChild.value ) ) )
						element.children.length = length -1;
					else
						lastChild.value = text;
				}
			}

			target.add( element );

			if ( element.name == 'pre' )
				inPre = false;

			if ( element.name == 'textarea' )
				inTextarea = false;


			if ( element.returnPoint )
			{
				currentNode = element.returnPoint;
				delete element.returnPoint;
			}
			else
				currentNode = moveCurrent ? target : savedCurrent;
		}

		parser.onTagOpen = function( tagName, attributes, selfClosing, optionalClose )
		{
			var element = new CKEDITOR.htmlParser.element( tagName, attributes );

			// "isEmpty" will be always "false" for unknown elements, so we
			// must force it if the parser has identified it as a selfClosing tag.
			if ( element.isUnknown && selfClosing )
				element.isEmpty = true;

			// Check for optional closed elements, including browser quirks and manually opened blocks.
			element.isOptionalClose = tagName in optionalCloseTags || optionalClose;

			// This is a tag to be removed if empty, so do not add it immediately.
			if ( isRemoveEmpty( element ) )
			{
				pendingInline.push( element );
				return;
			}
			else if ( tagName == 'pre' )
				inPre = true;
			else if ( tagName == 'br' && inPre )
			{
				currentNode.add( new CKEDITOR.htmlParser.text( '\n' ) );
				return;
			}
			else if ( tagName == 'textarea' )
				inTextarea = true;

			if ( tagName == 'br' )
			{
				pendingBRs.push( element );
				return;
			}

			while( 1 )
			{
				var currentName = currentNode.name;

				var currentDtd = currentName ? ( CKEDITOR.dtd[ currentName ]
						|| ( currentNode._.isBlockLike ? CKEDITOR.dtd.div : CKEDITOR.dtd.span ) )
						: rootDtd;

				// If the element cannot be child of the current element.
				if ( !element.isUnknown && !currentNode.isUnknown && !currentDtd[ tagName ] )
				{
					// Current node doesn't have a close tag, time for a close
					// as this element isn't fit in. (#7497)
					if ( currentNode.isOptionalClose )
						parser.onTagClose( currentName );
					// Fixing malformed nested lists by moving it into a previous list item. (#3828)
					else if ( tagName in listBlocks
						&& currentName in listBlocks )
					{
						var children = currentNode.children,
							lastChild = children[ children.length - 1 ];

						// Establish the list item if it's not existed.
						if ( !( lastChild && lastChild.name == 'li' ) )
							addElement( ( lastChild = new CKEDITOR.htmlParser.element( 'li' ) ), currentNode );

						!element.returnPoint && ( element.returnPoint = currentNode );
						currentNode = lastChild;
					}
					// Establish new list root for orphan list items.
					else if ( tagName in CKEDITOR.dtd.$listItem && currentName != tagName )
						parser.onTagOpen( tagName == 'li' ? 'ul' : 'dl', {}, 0, 1 );
					// We're inside a structural block like table and list, AND the incoming element
					// is not of the same type (e.g. <td>td1<td>td2</td>), we simply add this new one before it,
					// and most importantly, return back to here once this element is added,
					// e.g. <table><tr><td>td1</td><p>p1</p><td>td2</td></tr></table>
					else if ( currentName in nonBreakingBlocks && currentName != tagName )
					{
						!element.returnPoint && ( element.returnPoint = currentNode );
						currentNode = currentNode.parent;
					}
					else
					{
						// The current element is an inline element, which
						// need to be continued even after the close, so put
						// it in the pending list.
						if ( currentName in CKEDITOR.dtd.$inline )
							pendingInline.unshift( currentNode );

						// The most common case where we just need to close the
						// current one and append the new one to the parent.
						if ( currentNode.parent )
							addElement( currentNode, currentNode.parent, 1 );
						// We've tried our best to fix the embarrassment here, while
						// this element still doesn't find it's parent, mark it as
						// orphan and show our tolerance to it.
						else
						{
							element.isOrphan = 1;
							break;
						}
					}
				}
				else
					break;
			}

			checkPending( tagName );
			sendPendingBRs();

			element.parent = currentNode;

			if ( element.isEmpty )
				addElement( element );
			else
				currentNode = element;
		};

		parser.onTagClose = function( tagName )
		{
			// Check if there is any pending tag to be closed.
			for ( var i = pendingInline.length - 1 ; i >= 0 ; i-- )
			{
				// If found, just remove it from the list.
				if ( tagName == pendingInline[ i ].name )
				{
					pendingInline.splice( i, 1 );
					return;
				}
			}

			var pendingAdd = [],
				newPendingInline = [],
				candidate = currentNode;

			while ( candidate != fragment && candidate.name != tagName )
			{
				// If this is an inline element, add it to the pending list, if we're
				// really closing one of the parents element later, they will continue
				// after it.
				if ( !candidate._.isBlockLike )
					newPendingInline.unshift( candidate );

				// This node should be added to it's parent at this point. But,
				// it should happen only if the closing tag is really closing
				// one of the nodes. So, for now, we just cache it.
				pendingAdd.push( candidate );

				// Make sure return point is properly restored.
				candidate = candidate.returnPoint || candidate.parent;
			}

			if ( candidate != fragment )
			{
				// Add all elements that have been found in the above loop.
				for ( i = 0 ; i < pendingAdd.length ; i++ )
				{
					var node = pendingAdd[ i ];
					addElement( node, node.parent );
				}

				currentNode = candidate;

				if ( candidate._.isBlockLike )
					sendPendingBRs();

				addElement( candidate, candidate.parent );

				// The parent should start receiving new nodes now, except if
				// addElement changed the currentNode.
				if ( candidate == currentNode )
					currentNode = currentNode.parent;

				pendingInline = pendingInline.concat( newPendingInline );
			}

			if ( tagName == 'body' )
				fixForBody = false;
		};

		parser.onText = function( text )
		{
			// Trim empty spaces at beginning of text contents except <pre> and <textarea>.
			if ( ( !currentNode._.hasInlineStarted || pendingBRs.length ) && !inPre && !inTextarea )
			{
				text = CKEDITOR.tools.ltrim( text );

				if ( text.length === 0 )
					return;
			}

			var currentName = currentNode.name,
			currentDtd = currentName ? ( CKEDITOR.dtd[ currentName ]
							|| ( currentNode._.isBlockLike ?
								 CKEDITOR.dtd.div : CKEDITOR.dtd.span ) ) : rootDtd;

			// Fix orphan text in list/table. (#8540) (#8870)
			if ( !inTextarea &&
				 !currentDtd [ '#' ] &&
				 currentName in nonBreakingBlocks )
			{
				parser.onTagOpen( currentName in listBlocks ? 'li' :
								  currentName == 'dl' ? 'dd' :
								  currentName == 'table' ? 'tr' :
								  currentName == 'tr' ? 'td' : '' );
				parser.onText( text );
				return;
			}

			sendPendingBRs();
			checkPending();

			if ( fixForBody
				 && ( !currentNode.type || currentNode.name == 'body' )
				 && CKEDITOR.tools.trim( text ) )
			{
				this.onTagOpen( fixForBody, {}, 0, 1 );
			}

			// Shrinking consequential spaces into one single for all elements
			// text contents.
			if ( !inPre && !inTextarea )
				text = text.replace( /[\t\r\n ]{2,}|[\t\r\n]/g, ' ' );

			currentNode.add( new CKEDITOR.htmlParser.text( text ) );
		};

		parser.onCDATA = function( cdata )
		{
			currentNode.add( new CKEDITOR.htmlParser.cdata( cdata ) );
		};

		parser.onComment = function( comment )
		{
			sendPendingBRs();
			checkPending();
			currentNode.add( new CKEDITOR.htmlParser.comment( comment ) );
		};

		// Parse it.
		parser.parse( fragmentHtml );

		// Send all pending BRs except one, which we consider a unwanted bogus. (#5293)
		sendPendingBRs( !CKEDITOR.env.ie && 1 );

		// Close all pending nodes, make sure return point is properly restored.
		while ( currentNode != fragment )
			addElement( currentNode, currentNode.parent, 1 );

		return fragment;
	};

	CKEDITOR.htmlParser.fragment.prototype =
	{
		/**
		 * Adds a node to this fragment.
		 * @param {Object} node The node to be added. It can be any of of the
		 *		following types: {@link CKEDITOR.htmlParser.element},
		 *		{@link CKEDITOR.htmlParser.text} and
		 *		{@link CKEDITOR.htmlParser.comment}.
		 *	@param {Number} [index] From where the insertion happens.
		 * @example
		 */
		add : function( node, index )
		{
			isNaN( index ) && ( index = this.children.length );

			var previous = index > 0 ? this.children[ index - 1 ] : null;
			if ( previous )
			{
				// If the block to be appended is following text, trim spaces at
				// the right of it.
				if ( node._.isBlockLike && previous.type == CKEDITOR.NODE_TEXT )
				{
					previous.value = CKEDITOR.tools.rtrim( previous.value );

					// If we have completely cleared the previous node.
					if ( previous.value.length === 0 )
					{
						// Remove it from the list and add the node again.
						this.children.pop();
						this.add( node );
						return;
					}
				}

				previous.next = node;
			}

			node.previous = previous;
			node.parent = this;

			this.children.splice( index, 0, node );

			this._.hasInlineStarted = node.type == CKEDITOR.NODE_TEXT || ( node.type == CKEDITOR.NODE_ELEMENT && !node._.isBlockLike );
		},

		/**
		 * Writes the fragment HTML to a CKEDITOR.htmlWriter.
		 * @param {CKEDITOR.htmlWriter} writer The writer to which write the HTML.
		 * @example
		 * var writer = new CKEDITOR.htmlWriter();
		 * var fragment = CKEDITOR.htmlParser.fragment.fromHtml( '&lt;P&gt;&lt;B&gt;Example' );
		 * fragment.writeHtml( writer )
		 * alert( writer.getHtml() );  "&lt;p&gt;&lt;b&gt;Example&lt;/b&gt;&lt;/p&gt;"
		 */
		writeHtml : function( writer, filter )
		{
			var isChildrenFiltered;
			this.filterChildren = function()
			{
				var writer = new CKEDITOR.htmlParser.basicWriter();
				this.writeChildrenHtml.call( this, writer, filter, true );
				var html = writer.getHtml();
				this.children = new CKEDITOR.htmlParser.fragment.fromHtml( html ).children;
				isChildrenFiltered = 1;
			};

			// Filtering the root fragment before anything else.
			!this.name && filter && filter.onFragment( this );

			this.writeChildrenHtml( writer, isChildrenFiltered ? null : filter );
		},

		writeChildrenHtml : function( writer, filter )
		{
			for ( var i = 0 ; i < this.children.length ; i++ )
				this.children[i].writeHtml( writer, filter );
		}
	};
})();
