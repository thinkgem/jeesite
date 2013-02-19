/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.dom.node} class which is the base
 *		class for classes that represent DOM nodes.
 */

/**
 * Base class for classes representing DOM nodes. This constructor may return
 * an instance of a class that inherits from this class, like
 * {@link CKEDITOR.dom.element} or {@link CKEDITOR.dom.text}.
 * @augments CKEDITOR.dom.domObject
 * @param {Object} domNode A native DOM node.
 * @constructor
 * @see CKEDITOR.dom.element
 * @see CKEDITOR.dom.text
 * @example
 */
CKEDITOR.dom.node = function( domNode )
{
	if ( domNode )
	{
		var type = domNode.nodeType == CKEDITOR.NODE_DOCUMENT ? 'document'
			: domNode.nodeType == CKEDITOR.NODE_ELEMENT ? 'element'
			: domNode.nodeType == CKEDITOR.NODE_TEXT ? 'text'
			: domNode.nodeType == CKEDITOR.NODE_COMMENT ? 'comment'
			: 'domObject';  // Call the base constructor otherwise.

		return new CKEDITOR.dom[ type ]( domNode );
	}

	return this;
};

CKEDITOR.dom.node.prototype = new CKEDITOR.dom.domObject();

/**
 * Element node type.
 * @constant
 * @example
 */
CKEDITOR.NODE_ELEMENT = 1;

/**
 * Document node type.
 * @constant
 * @example
 */
CKEDITOR.NODE_DOCUMENT = 9;

/**
 * Text node type.
 * @constant
 * @example
 */
CKEDITOR.NODE_TEXT = 3;

/**
 * Comment node type.
 * @constant
 * @example
 */
CKEDITOR.NODE_COMMENT = 8;

CKEDITOR.NODE_DOCUMENT_FRAGMENT = 11;

CKEDITOR.POSITION_IDENTICAL = 0;
CKEDITOR.POSITION_DISCONNECTED = 1;
CKEDITOR.POSITION_FOLLOWING = 2;
CKEDITOR.POSITION_PRECEDING = 4;
CKEDITOR.POSITION_IS_CONTAINED = 8;
CKEDITOR.POSITION_CONTAINS = 16;

CKEDITOR.tools.extend( CKEDITOR.dom.node.prototype,
	/** @lends CKEDITOR.dom.node.prototype */
	{
		/**
		 * Makes this node a child of another element.
		 * @param {CKEDITOR.dom.element} element The target element to which
		 *		this node will be appended.
		 * @returns {CKEDITOR.dom.element} The target element.
		 * @example
		 * var p = new CKEDITOR.dom.element( 'p' );
		 * var strong = new CKEDITOR.dom.element( 'strong' );
		 * strong.appendTo( p );
		 *
		 * // result: "&lt;p&gt;&lt;strong&gt;&lt;/strong&gt;&lt;/p&gt;"
		 */
		appendTo : function( element, toStart )
		{
			element.append( this, toStart );
			return element;
		},

		clone : function( includeChildren, cloneId )
		{
			var $clone = this.$.cloneNode( includeChildren );

			var removeIds = function( node )
			{
				if ( node.nodeType != CKEDITOR.NODE_ELEMENT )
					return;

				if ( !cloneId )
					node.removeAttribute( 'id', false );
				node.removeAttribute( 'data-cke-expando', false );

				if ( includeChildren )
				{
					var childs = node.childNodes;
					for ( var i=0; i < childs.length; i++ )
						removeIds( childs[ i ] );
				}
			};

			// The "id" attribute should never be cloned to avoid duplication.
			removeIds( $clone );

			return new CKEDITOR.dom.node( $clone );
		},

		hasPrevious : function()
		{
			return !!this.$.previousSibling;
		},

		hasNext : function()
		{
			return !!this.$.nextSibling;
		},

		/**
		 * Inserts this element after a node.
		 * @param {CKEDITOR.dom.node} node The node that will precede this element.
		 * @returns {CKEDITOR.dom.node} The node preceding this one after
		 *		insertion.
		 * @example
		 * var em = new CKEDITOR.dom.element( 'em' );
		 * var strong = new CKEDITOR.dom.element( 'strong' );
		 * strong.insertAfter( em );
		 *
		 * // result: "&lt;em&gt;&lt;/em&gt;&lt;strong&gt;&lt;/strong&gt;"
		 */
		insertAfter : function( node )
		{
			node.$.parentNode.insertBefore( this.$, node.$.nextSibling );
			return node;
		},

		/**
		 * Inserts this element before a node.
		 * @param {CKEDITOR.dom.node} node The node that will succeed this element.
		 * @returns {CKEDITOR.dom.node} The node being inserted.
		 * @example
		 * var em = new CKEDITOR.dom.element( 'em' );
		 * var strong = new CKEDITOR.dom.element( 'strong' );
		 * strong.insertBefore( em );
		 *
		 * // result: "&lt;strong&gt;&lt;/strong&gt;&lt;em&gt;&lt;/em&gt;"
		 */
		insertBefore : function( node )
		{
			node.$.parentNode.insertBefore( this.$, node.$ );
			return node;
		},

		insertBeforeMe : function( node )
		{
			this.$.parentNode.insertBefore( node.$, this.$ );
			return node;
		},

		/**
		 * Retrieves a uniquely identifiable tree address for this node.
		 * The tree address returned is an array of integers, with each integer
		 * indicating a child index of a DOM node, starting from
		 * <code>document.documentElement</code>.
		 *
		 * For example, assuming <code>&lt;body&gt;</code> is the second child
		 * of <code>&lt;html&gt;</code> (<code>&lt;head&gt;</code> being the first),
		 * and we would like to address the third child under the
		 * fourth child of <code>&lt;body&gt;</code>, the tree address returned would be:
		 * [1, 3, 2]
		 *
		 * The tree address cannot be used for finding back the DOM tree node once
		 * the DOM tree structure has been modified.
		 */
		getAddress : function( normalized )
		{
			var address = [];
			var $documentElement = this.getDocument().$.documentElement;
			var node = this.$;

			while ( node && node != $documentElement )
			{
				var parentNode = node.parentNode;

				if ( parentNode )
				{
					// Get the node index. For performance, call getIndex
					// directly, instead of creating a new node object.
					address.unshift( this.getIndex.call( { $ : node }, normalized ) );
				}

				node = parentNode;
			}

			return address;
		},

		/**
		 * Gets the document containing this element.
		 * @returns {CKEDITOR.dom.document} The document.
		 * @example
		 * var element = CKEDITOR.document.getById( 'example' );
		 * alert( <strong>element.getDocument().equals( CKEDITOR.document )</strong> );  // "true"
		 */
		getDocument : function()
		{
			return new CKEDITOR.dom.document( this.$.ownerDocument || this.$.parentNode.ownerDocument );
		},

		getIndex : function( normalized )
		{
			// Attention: getAddress depends on this.$

			var current = this.$,
				index = 0;

			while ( ( current = current.previousSibling ) )
			{
				// When normalizing, do not count it if this is an
				// empty text node or if it's a text node following another one.
				if ( normalized && current.nodeType == 3 &&
					 ( !current.nodeValue.length ||
					   ( current.previousSibling && current.previousSibling.nodeType == 3 ) ) )
				{
					continue;
				}

				index++;
			}

			return index;
		},

		getNextSourceNode : function( startFromSibling, nodeType, guard )
		{
			// If "guard" is a node, transform it in a function.
			if ( guard && !guard.call )
			{
				var guardNode = guard;
				guard = function( node )
				{
					return !node.equals( guardNode );
				};
			}

			var node = ( !startFromSibling && this.getFirst && this.getFirst() ),
				parent;

			// Guarding when we're skipping the current element( no children or 'startFromSibling' ).
			// send the 'moving out' signal even we don't actually dive into.
			if ( !node )
			{
				if ( this.type == CKEDITOR.NODE_ELEMENT && guard && guard( this, true ) === false )
					return null;
				node = this.getNext();
			}

			while ( !node && ( parent = ( parent || this ).getParent() ) )
			{
				// The guard check sends the "true" paramenter to indicate that
				// we are moving "out" of the element.
				if ( guard && guard( parent, true ) === false )
					return null;

				node = parent.getNext();
			}

			if ( !node )
				return null;

			if ( guard && guard( node ) === false )
				return null;

			if ( nodeType && nodeType != node.type )
				return node.getNextSourceNode( false, nodeType, guard );

			return node;
		},

		getPreviousSourceNode : function( startFromSibling, nodeType, guard )
		{
			if ( guard && !guard.call )
			{
				var guardNode = guard;
				guard = function( node )
				{
					return !node.equals( guardNode );
				};
			}

			var node = ( !startFromSibling && this.getLast && this.getLast() ),
				parent;

			// Guarding when we're skipping the current element( no children or 'startFromSibling' ).
			// send the 'moving out' signal even we don't actually dive into.
			if ( !node )
			{
				if ( this.type == CKEDITOR.NODE_ELEMENT && guard && guard( this, true ) === false )
					return null;
				node = this.getPrevious();
			}

			while ( !node && ( parent = ( parent || this ).getParent() ) )
			{
				// The guard check sends the "true" paramenter to indicate that
				// we are moving "out" of the element.
				if ( guard && guard( parent, true ) === false )
					return null;

				node = parent.getPrevious();
			}

			if ( !node )
				return null;

			if ( guard && guard( node ) === false )
				return null;

			if ( nodeType && node.type != nodeType )
				return node.getPreviousSourceNode( false, nodeType, guard );

			return node;
		},

		getPrevious : function( evaluator )
		{
			var previous = this.$, retval;
			do
			{
				previous = previous.previousSibling;

				// Avoid returning the doc type node.
				// http://www.w3.org/TR/REC-DOM-Level-1/level-one-core.html#ID-412266927
				retval = previous && previous.nodeType != 10 && new CKEDITOR.dom.node( previous );
			}
			while ( retval && evaluator && !evaluator( retval ) )
			return retval;
		},

		/**
		 * Gets the node that follows this element in its parent's child list.
		 * @param {Function} evaluator Filtering the result node.
		 * @returns {CKEDITOR.dom.node} The next node or null if not available.
		 * @example
		 * var element = CKEDITOR.dom.element.createFromHtml( '&lt;div&gt;&lt;b&gt;Example&lt;/b&gt; &lt;i&gt;next&lt;/i&gt;&lt;/div&gt;' );
		 * var first = <strong>element.getFirst().getNext()</strong>;
		 * alert( first.getName() );  // "i"
		 */
		getNext : function( evaluator )
		{
			var next = this.$, retval;
			do
			{
				next = next.nextSibling;
				retval = next && new CKEDITOR.dom.node( next );
			}
			while ( retval && evaluator && !evaluator( retval ) )
			return retval;
		},

		/**
		 * Gets the parent element for this node.
		 * @returns {CKEDITOR.dom.element} The parent element.
		 * @example
		 * var node = editor.document.getBody().getFirst();
		 * var parent = node.<strong>getParent()</strong>;
		 * alert( node.getName() );  // "body"
		 */
		getParent : function()
		{
			var parent = this.$.parentNode;
			return ( parent && parent.nodeType == 1 ) ? new CKEDITOR.dom.node( parent ) : null;
		},

		getParents : function( closerFirst )
		{
			var node = this;
			var parents = [];

			do
			{
				parents[  closerFirst ? 'push' : 'unshift' ]( node );
			}
			while ( ( node = node.getParent() ) )

			return parents;
		},

		getCommonAncestor : function( node )
		{
			if ( node.equals( this ) )
				return this;

			if ( node.contains && node.contains( this ) )
				return node;

			var start = this.contains ? this : this.getParent();

			do
			{
				if ( start.contains( node ) )
					return start;
			}
			while ( ( start = start.getParent() ) );

			return null;
		},

		getPosition : function( otherNode )
		{
			var $ = this.$;
			var $other = otherNode.$;

			if ( $.compareDocumentPosition )
				return $.compareDocumentPosition( $other );

			// IE and Safari have no support for compareDocumentPosition.

			if ( $ == $other )
				return CKEDITOR.POSITION_IDENTICAL;

			// Only element nodes support contains and sourceIndex.
			if ( this.type == CKEDITOR.NODE_ELEMENT && otherNode.type == CKEDITOR.NODE_ELEMENT )
			{
				if ( $.contains )
				{
					if ( $.contains( $other ) )
						return CKEDITOR.POSITION_CONTAINS + CKEDITOR.POSITION_PRECEDING;

					if ( $other.contains( $ ) )
						return CKEDITOR.POSITION_IS_CONTAINED + CKEDITOR.POSITION_FOLLOWING;
				}

				if ( 'sourceIndex' in $ )
				{
					return ( $.sourceIndex < 0 || $other.sourceIndex < 0 ) ? CKEDITOR.POSITION_DISCONNECTED :
						( $.sourceIndex < $other.sourceIndex ) ? CKEDITOR.POSITION_PRECEDING :
						CKEDITOR.POSITION_FOLLOWING;
				}
			}

			// For nodes that don't support compareDocumentPosition, contains
			// or sourceIndex, their "address" is compared.

			var addressOfThis = this.getAddress(),
				addressOfOther = otherNode.getAddress(),
				minLevel = Math.min( addressOfThis.length, addressOfOther.length );

				// Determinate preceed/follow relationship.
				for ( var i = 0 ; i <= minLevel - 1 ; i++ )
 				{
					if ( addressOfThis[ i ] != addressOfOther[ i ] )
					{
						if ( i < minLevel )
						{
							return addressOfThis[ i ] < addressOfOther[ i ] ?
						            CKEDITOR.POSITION_PRECEDING : CKEDITOR.POSITION_FOLLOWING;
						}
						break;
					}
 				}

				// Determinate contains/contained relationship.
				return ( addressOfThis.length < addressOfOther.length ) ?
							CKEDITOR.POSITION_CONTAINS + CKEDITOR.POSITION_PRECEDING :
							CKEDITOR.POSITION_IS_CONTAINED + CKEDITOR.POSITION_FOLLOWING;
		},

		/**
		 * Gets the closest ancestor node of this node, specified by its name.
		 * @param {String} reference The name of the ancestor node to search or
		 *		an object with the node names to search for.
		 * @param {Boolean} [includeSelf] Whether to include the current
		 *		node in the search.
		 * @returns {CKEDITOR.dom.node} The located ancestor node or null if not found.
		 * @since 3.6.1
		 * @example
		 * // Suppose we have the following HTML structure:
		 * // &lt;div id="outer"&gt;&lt;div id="inner"&gt;&lt;p&gt;&lt;b&gt;Some text&lt;/b&gt;&lt;/p&gt;&lt;/div&gt;&lt;/div&gt;
		 * // If node == &lt;b&gt;
		 * ascendant = node.getAscendant( 'div' );      // ascendant == &lt;div id="inner"&gt
		 * ascendant = node.getAscendant( 'b' );        // ascendant == null
		 * ascendant = node.getAscendant( 'b', true );  // ascendant == &lt;b&gt;
		 * ascendant = node.getAscendant( { div: 1, p: 1} );      // Searches for the first 'div' or 'p': ascendant == &lt;div id="inner"&gt
		 */
		getAscendant : function( reference, includeSelf )
		{
			var $ = this.$,
				name;

			if ( !includeSelf )
				$ = $.parentNode;

			while ( $ )
			{
				if ( $.nodeName && ( name = $.nodeName.toLowerCase(), ( typeof reference == 'string' ? name == reference : name in reference ) ) )
					return new CKEDITOR.dom.node( $ );

				$ = $.parentNode;
			}
			return null;
		},

		hasAscendant : function( name, includeSelf )
		{
			var $ = this.$;

			if ( !includeSelf )
				$ = $.parentNode;

			while ( $ )
			{
				if ( $.nodeName && $.nodeName.toLowerCase() == name )
					return true;

				$ = $.parentNode;
			}
			return false;
		},

		move : function( target, toStart )
		{
			target.append( this.remove(), toStart );
		},

		/**
		 * Removes this node from the document DOM.
		 * @param {Boolean} [preserveChildren] Indicates that the children
		 *		elements must remain in the document, removing only the outer
		 *		tags.
		 * @example
		 * var element = CKEDITOR.dom.element.getById( 'MyElement' );
		 * <strong>element.remove()</strong>;
		 */
		remove : function( preserveChildren )
		{
			var $ = this.$;
			var parent = $.parentNode;

			if ( parent )
			{
				if ( preserveChildren )
				{
					// Move all children before the node.
					for ( var child ; ( child = $.firstChild ) ; )
					{
						parent.insertBefore( $.removeChild( child ), $ );
					}
				}

				parent.removeChild( $ );
			}

			return this;
		},

		replace : function( nodeToReplace )
		{
			this.insertBefore( nodeToReplace );
			nodeToReplace.remove();
		},

		trim : function()
		{
			this.ltrim();
			this.rtrim();
		},

		ltrim : function()
		{
			var child;
			while ( this.getFirst && ( child = this.getFirst() ) )
			{
				if ( child.type == CKEDITOR.NODE_TEXT )
				{
					var trimmed = CKEDITOR.tools.ltrim( child.getText() ),
						originalLength = child.getLength();

					if ( !trimmed )
					{
						child.remove();
						continue;
					}
					else if ( trimmed.length < originalLength )
					{
						child.split( originalLength - trimmed.length );

						// IE BUG: child.remove() may raise JavaScript errors here. (#81)
						this.$.removeChild( this.$.firstChild );
					}
				}
				break;
			}
		},

		rtrim : function()
		{
			var child;
			while ( this.getLast && ( child = this.getLast() ) )
			{
				if ( child.type == CKEDITOR.NODE_TEXT )
				{
					var trimmed = CKEDITOR.tools.rtrim( child.getText() ),
						originalLength = child.getLength();

					if ( !trimmed )
					{
						child.remove();
						continue;
					}
					else if ( trimmed.length < originalLength )
					{
						child.split( trimmed.length );

						// IE BUG: child.getNext().remove() may raise JavaScript errors here.
						// (#81)
						this.$.lastChild.parentNode.removeChild( this.$.lastChild );
					}
				}
				break;
			}

			if ( !CKEDITOR.env.ie && !CKEDITOR.env.opera )
			{
				child = this.$.lastChild;

				if ( child && child.type == 1 && child.nodeName.toLowerCase() == 'br' )
				{
					// Use "eChildNode.parentNode" instead of "node" to avoid IE bug (#324).
					child.parentNode.removeChild( child ) ;
				}
			}
		},

		/**
		 * Checks if this node is read-only (should not be changed).
		 * @returns {Boolean}
		 * @since 3.5
		 * @example
		 * // For the following HTML:
		 * // &lt;div contenteditable="false"&gt;Some &lt;b&gt;text&lt;/b&gt;&lt;/div&gt;
		 *
		 * // If "ele" is the above &lt;div&gt;
		 * ele.isReadOnly();  // true
		 */
		isReadOnly : function()
		{
			var element = this;
			if ( this.type != CKEDITOR.NODE_ELEMENT )
				element = this.getParent();

			if ( element && typeof element.$.isContentEditable != 'undefined' )
				return ! ( element.$.isContentEditable || element.data( 'cke-editable' ) );
			else
			{
				// Degrade for old browsers which don't support "isContentEditable", e.g. FF3
				var current = element;
				while( current )
				{
					if ( current.is( 'body' ) || !!current.data( 'cke-editable' ) )
						break;

					if ( current.getAttribute( 'contentEditable' ) == 'false' )
						return true;
					else if ( current.getAttribute( 'contentEditable' ) == 'true' )
						break;

					current = current.getParent();
				}

				return false;
			}
		}
	}
);
