/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * A lightweight representation of an HTML element.
 * @param {String} name The element name.
 * @param {Object} attributes And object holding all attributes defined for
 *		this element.
 * @constructor
 * @example
 */
CKEDITOR.htmlParser.element = function( name, attributes )
{
	/**
	 * The element name.
	 * @type String
	 * @example
	 */
	this.name = name;

	/**
	 * Holds the attributes defined for this element.
	 * @type Object
	 * @example
	 */
	this.attributes = attributes || {};

	/**
	 * The nodes that are direct children of this element.
	 * @type Array
	 * @example
	 */
	this.children = [];

	// Reveal the real semantic of our internal custom tag name (#6639),
	// when resolving whether it's block like.
	var realName = name || '',
		prefixed = realName.match( /^cke:(.*)/ );
  	prefixed && ( realName = prefixed[ 1 ] );

	var isBlockLike	= !!( CKEDITOR.dtd.$nonBodyContent[ realName ]
				|| CKEDITOR.dtd.$block[ realName ]
				|| CKEDITOR.dtd.$listItem[ realName ]
				|| CKEDITOR.dtd.$tableContent[ realName ]
				|| CKEDITOR.dtd.$nonEditable[ realName ]
				|| realName == 'br' );

	this.isEmpty	= !!CKEDITOR.dtd.$empty[ name ];
	this.isUnknown	= !CKEDITOR.dtd[ name ];

	/** @private */
	this._ =
	{
		isBlockLike : isBlockLike,
		hasInlineStarted : this.isEmpty || !isBlockLike
	};
};

/**
 *  Object presentation of  CSS style declaration text.
 *  @param {CKEDITOR.htmlParser.element|String} elementOrStyleText A html parser element or the inline style text.
 */
CKEDITOR.htmlParser.cssStyle = function()
{
	 var styleText,
		arg = arguments[ 0 ],
		rules = {};

	styleText = arg instanceof CKEDITOR.htmlParser.element ? arg.attributes.style : arg;

	// html-encoded quote might be introduced by 'font-family'
	// from MS-Word which confused the following regexp. e.g.
	//'font-family: &quot;Lucida, Console&quot;'
	( styleText || '' )
		.replace( /&quot;/g, '"' )
		.replace( /\s*([^ :;]+)\s*:\s*([^;]+)\s*(?=;|$)/g,
			function( match, name, value )
			{
				name == 'font-family' && ( value = value.replace( /["']/g, '' ) );
				rules[ name.toLowerCase() ] = value;
			});

	return {

		rules : rules,

		/**
		 *  Apply the styles onto the specified element or object.
		 * @param {CKEDITOR.htmlParser.element|CKEDITOR.dom.element|Object} obj
		 */
		populate : function( obj )
		{
			var style = this.toString();
			if ( style )
			{
				obj instanceof CKEDITOR.dom.element ?
					obj.setAttribute( 'style', style ) :
					obj instanceof CKEDITOR.htmlParser.element ?
						obj.attributes.style = style :
						obj.style = style;
			}
		},

		toString : function()
		{
			var output = [];
			for ( var i in rules )
				rules[ i ] && output.push( i, ':', rules[ i ], ';' );
			return output.join( '' );
		}
	};
};

(function()
{
	// Used to sort attribute entries in an array, where the first element of
	// each object is the attribute name.
	var sortAttribs = function( a, b )
	{
		a = a[0];
		b = b[0];
		return a < b ? -1 : a > b ? 1 : 0;
	};

	CKEDITOR.htmlParser.element.prototype =
	{
		/**
		 * The node type. This is a constant value set to {@link CKEDITOR.NODE_ELEMENT}.
		 * @type Number
		 * @example
		 */
		type : CKEDITOR.NODE_ELEMENT,

		/**
		 * Adds a node to the element children list.
		 * @param {Object} node The node to be added. It can be any of of the
		 *		following types: {@link CKEDITOR.htmlParser.element},
		 *		{@link CKEDITOR.htmlParser.text} and
		 *		{@link CKEDITOR.htmlParser.comment}.
		 * @function
		 * @example
		 */
		add : CKEDITOR.htmlParser.fragment.prototype.add,

		/**
		 * Clone this element.
		 * @returns {CKEDITOR.htmlParser.element} The element clone.
		 * @example
		 */
		clone : function()
		{
			return new CKEDITOR.htmlParser.element( this.name, this.attributes );
		},

		/**
		 * Writes the element HTML to a CKEDITOR.htmlWriter.
		 * @param {CKEDITOR.htmlWriter} writer The writer to which write the HTML.
		 * @example
		 */
		writeHtml : function( writer, filter )
		{
			var attributes = this.attributes;

			// Ignore cke: prefixes when writing HTML.
			var element = this,
				writeName = element.name,
				a, newAttrName, value;

			var isChildrenFiltered;

			/**
			 * Providing an option for bottom-up filtering order ( element
			 * children to be pre-filtered before the element itself ).
			 */
			element.filterChildren = function()
			{
				if ( !isChildrenFiltered )
				{
					var writer = new CKEDITOR.htmlParser.basicWriter();
					CKEDITOR.htmlParser.fragment.prototype.writeChildrenHtml.call( element, writer, filter );
					element.children = new CKEDITOR.htmlParser.fragment.fromHtml( writer.getHtml(), 0, element.clone() ).children;
					isChildrenFiltered = 1;
				}
			};

			if ( filter )
			{
				while ( true )
				{
					if ( !( writeName = filter.onElementName( writeName ) ) )
						return;

					element.name = writeName;

					if ( !( element = filter.onElement( element ) ) )
						return;

					element.parent = this.parent;

					if ( element.name == writeName )
						break;

					// If the element has been replaced with something of a
					// different type, then make the replacement write itself.
					if ( element.type != CKEDITOR.NODE_ELEMENT )
					{
						element.writeHtml( writer, filter );
						return;
					}

					writeName = element.name;

					// This indicate that the element has been dropped by
					// filter but not the children.
					if ( !writeName )
					{
						// Fix broken parent refs.
						for ( var c = 0, length = this.children.length ; c < length ; c++ )
							this.children[ c ].parent = element.parent;

						this.writeChildrenHtml.call( element, writer, isChildrenFiltered ? null : filter );
						return;
					}
				}

				// The element may have been changed, so update the local
				// references.
				attributes = element.attributes;
			}

			// Open element tag.
			writer.openTag( writeName, attributes );

			// Copy all attributes to an array.
			var attribsArray = [];
			// Iterate over the attributes twice since filters may alter
			// other attributes.
			for ( var i = 0 ; i < 2; i++ )
			{
				for ( a in attributes )
				{
					newAttrName = a;
					value = attributes[ a ];
					if ( i == 1 )
						attribsArray.push( [ a, value ] );
					else if ( filter )
					{
						while ( true )
						{
							if ( !( newAttrName = filter.onAttributeName( a ) ) )
							{
								delete attributes[ a ];
								break;
							}
							else if ( newAttrName != a )
							{
								delete attributes[ a ];
								a = newAttrName;
								continue;
							}
							else
								break;
						}
						if ( newAttrName )
						{
							if ( ( value = filter.onAttribute( element, newAttrName, value ) ) === false )
								delete attributes[ newAttrName ];
							else
								attributes [ newAttrName ] = value;
						}
					}
				}
			}
			// Sort the attributes by name.
			if ( writer.sortAttributes )
				attribsArray.sort( sortAttribs );

			// Send the attributes.
			var len = attribsArray.length;
			for ( i = 0 ; i < len ; i++ )
			{
				var attrib = attribsArray[ i ];
				writer.attribute( attrib[0], attrib[1] );
			}

			// Close the tag.
			writer.openTagClose( writeName, element.isEmpty );

			if ( !element.isEmpty )
			{
				this.writeChildrenHtml.call( element, writer, isChildrenFiltered ? null : filter );
				// Close the element.
				writer.closeTag( writeName );
			}
		},

		writeChildrenHtml : function( writer, filter )
		{
			// Send children.
			CKEDITOR.htmlParser.fragment.prototype.writeChildrenHtml.apply( this, arguments );

		}
	};
})();
