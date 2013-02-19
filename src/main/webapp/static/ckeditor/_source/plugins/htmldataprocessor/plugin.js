/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	// Regex to scan for &nbsp; at the end of blocks, which are actually placeholders.
	// Safari transforms the &nbsp; to \xa0. (#4172)
	var tailNbspRegex = /^[\t\r\n ]*(?:&nbsp;|\xa0)$/;

	var protectedSourceMarker = '{cke_protected}';

	// Return the last non-space child node of the block (#4344).
	function lastNoneSpaceChild( block )
	{
		var lastIndex = block.children.length,
			last = block.children[ lastIndex - 1 ];
		while (  last && last.type == CKEDITOR.NODE_TEXT && !CKEDITOR.tools.trim( last.value ) )
			last = block.children[ --lastIndex ];
		return last;
	}

	function trimFillers( block, fromSource )
	{
		// If the current node is a block, and if we're converting from source or
		// we're not in IE then search for and remove any tailing BR node.
		//
		// Also, any &nbsp; at the end of blocks are fillers, remove them as well.
		// (#2886)
		var children = block.children, lastChild = lastNoneSpaceChild( block );
		if ( lastChild )
		{
			if ( ( fromSource || !CKEDITOR.env.ie ) && lastChild.type == CKEDITOR.NODE_ELEMENT && lastChild.name == 'br' )
				children.pop();
			if ( lastChild.type == CKEDITOR.NODE_TEXT && tailNbspRegex.test( lastChild.value ) )
				children.pop();
		}
	}

	function blockNeedsExtension( block, fromSource, extendEmptyBlock )
	{
		if( !fromSource && ( !extendEmptyBlock ||
			typeof extendEmptyBlock == 'function' && ( extendEmptyBlock( block ) === false ) ) )
			return false;

	// 1. For IE version >=8,  empty blocks are displayed correctly themself in wysiwiyg;
	// 2. For the rest, at least table cell and list item need no filler space.
	// (#6248)
	if ( fromSource && CKEDITOR.env.ie &&
		( document.documentMode > 7
			|| block.name in CKEDITOR.dtd.tr
			|| block.name in CKEDITOR.dtd.$listItem ) )
		return false;

		var lastChild = lastNoneSpaceChild( block );

		return !lastChild || lastChild &&
				( lastChild.type == CKEDITOR.NODE_ELEMENT && lastChild.name == 'br'
				// Some of the controls in form needs extension too,
				// to move cursor at the end of the form. (#4791)
				|| block.name == 'form' && lastChild.name == 'input' );
	}

	function getBlockExtension( isOutput, emptyBlockFiller )
	{
		return function( node )
		{
			trimFillers( node, !isOutput );

			if ( blockNeedsExtension( node, !isOutput, emptyBlockFiller ) )
			{
				if ( isOutput || CKEDITOR.env.ie )
					node.add( new CKEDITOR.htmlParser.text( '\xa0' ) );
				else
					node.add( new CKEDITOR.htmlParser.element( 'br', {} ) );
			}
		};
	}

	var dtd = CKEDITOR.dtd;

	// Define orders of table elements.
	var tableOrder = [ 'caption', 'colgroup', 'col', 'thead', 'tfoot', 'tbody' ];

	// Find out the list of block-like tags that can contain <br>.
	var blockLikeTags = CKEDITOR.tools.extend( {}, dtd.$block, dtd.$listItem, dtd.$tableContent );
	for ( var i in blockLikeTags )
	{
		if ( ! ( 'br' in dtd[i] ) )
			delete blockLikeTags[i];
	}
	// We just avoid filler in <pre> right now.
	// TODO: Support filler for <pre>, line break is also occupy line height.
	delete blockLikeTags.pre;
	var defaultDataFilterRules =
	{
		elements : {},
		attributeNames :
		[
			// Event attributes (onXYZ) must not be directly set. They can become
			// active in the editing area (IE|WebKit).
			[ ( /^on/ ), 'data-cke-pa-on' ]
		]
	};

	var defaultDataBlockFilterRules = { elements : {} };

	for ( i in blockLikeTags )
		defaultDataBlockFilterRules.elements[ i ] = getBlockExtension();

	var defaultHtmlFilterRules =
		{
			elementNames :
			[
				// Remove the "cke:" namespace prefix.
				[ ( /^cke:/ ), '' ],

				// Ignore <?xml:namespace> tags.
				[ ( /^\?xml:namespace$/ ), '' ]
			],

			attributeNames :
			[
				// Attributes saved for changes and protected attributes.
				[ ( /^data-cke-(saved|pa)-/ ), '' ],

				// All "data-cke-" attributes are to be ignored.
				[ ( /^data-cke-.*/ ), '' ],

				[ 'hidefocus', '' ]
			],

			elements :
			{
				$ : function( element )
				{
					var attribs = element.attributes;

					if ( attribs )
					{
						// Elements marked as temporary are to be ignored.
						if ( attribs[ 'data-cke-temp' ] )
							return false;

						// Remove duplicated attributes - #3789.
						var attributeNames = [ 'name', 'href', 'src' ],
							savedAttributeName;
						for ( var i = 0 ; i < attributeNames.length ; i++ )
						{
							savedAttributeName = 'data-cke-saved-' + attributeNames[ i ];
							savedAttributeName in attribs && ( delete attribs[ attributeNames[ i ] ] );
						}
					}

					return element;
				},

				// The contents of table should be in correct order (#4809).
				table : function( element )
				{
					var children = element.children;
					children.sort( function ( node1, node2 )
								   {
									   return node1.type == CKEDITOR.NODE_ELEMENT && node2.type == node1.type ?
											CKEDITOR.tools.indexOf( tableOrder, node1.name )  > CKEDITOR.tools.indexOf( tableOrder, node2.name ) ? 1 : -1 : 0;
								   } );
				},

				embed : function( element )
				{
					var parent = element.parent;

					// If the <embed> is child of a <object>, copy the width
					// and height attributes from it.
					if ( parent && parent.name == 'object' )
					{
						var parentWidth = parent.attributes.width,
							parentHeight = parent.attributes.height;
						parentWidth && ( element.attributes.width = parentWidth );
						parentHeight && ( element.attributes.height = parentHeight );
					}
				},
				// Restore param elements into self-closing.
				param : function( param )
				{
					param.children = [];
					param.isEmpty = true;
					return param;
				},

				// Remove empty link but not empty anchor.(#3829)
				a : function( element )
				{
					if ( !( element.children.length ||
							element.attributes.name ||
							element.attributes[ 'data-cke-saved-name' ] ) )
					{
						return false;
					}
				},

				// Remove dummy span in webkit.
				span: function( element )
				{
					if ( element.attributes[ 'class' ] == 'Apple-style-span' )
						delete element.name;
				},

				// Empty <pre> in IE is reported with filler node (&nbsp;).
				pre : function( element ) { CKEDITOR.env.ie && trimFillers( element ); },

				html : function( element )
				{
					delete element.attributes.contenteditable;
					delete element.attributes[ 'class' ];
				},

				body : function( element )
				{
					delete element.attributes.spellcheck;
					delete element.attributes.contenteditable;
				},

				style : function( element )
				{
					var child = element.children[ 0 ];
					child && child.value && ( child.value = CKEDITOR.tools.trim( child.value ));

					if ( !element.attributes.type )
						element.attributes.type = 'text/css';
				},

				title : function( element )
				{
					var titleText = element.children[ 0 ];
					titleText && ( titleText.value = element.attributes[ 'data-cke-title' ] || '' );
				}
			},

			attributes :
			{
				'class' : function( value, element )
				{
					// Remove all class names starting with "cke_".
					return CKEDITOR.tools.ltrim( value.replace( /(?:^|\s+)cke_[^\s]*/g, '' ) ) || false;
				}
			}
		};

	if ( CKEDITOR.env.ie )
	{
		// IE outputs style attribute in capital letters. We should convert
		// them back to lower case, while not hurting the values (#5930)
		defaultHtmlFilterRules.attributes.style = function( value, element )
		{
			return value.replace( /(^|;)([^\:]+)/g, function( match )
				{
					return match.toLowerCase();
				});
		};
	}

	function protectReadOnly( element )
	{
		var attrs = element.attributes;

		// We should flag that the element was locked by our code so
		// it'll be editable by the editor functions (#6046).
		if ( attrs.contenteditable != "false" )
			attrs[ 'data-cke-editable' ] = attrs.contenteditable ? 'true' : 1;

		attrs.contenteditable = "false";
	}
	function unprotectReadyOnly( element )
	{
		var attrs = element.attributes;
		switch( attrs[ 'data-cke-editable' ] )
		{
			case 'true':	attrs.contenteditable = 'true';	break;
			case '1':		delete attrs.contenteditable;	break;
		}
	}
	// Disable form elements editing mode provided by some browers. (#5746)
	for ( i in { input : 1, textarea : 1 } )
	{
		defaultDataFilterRules.elements[ i ] = protectReadOnly;
		defaultHtmlFilterRules.elements[ i ] = unprotectReadyOnly;
	}

	var protectElementRegex = /<(a|area|img|input)\b([^>]*)>/gi,
		protectAttributeRegex = /\b(on\w+|href|src|name)\s*=\s*(?:(?:"[^"]*")|(?:'[^']*')|(?:[^ "'>]+))/gi;

	var protectElementsRegex = /(?:<style(?=[ >])[^>]*>[\s\S]*<\/style>)|(?:<(:?link|meta|base)[^>]*>)/gi,
		encodedElementsRegex = /<cke:encoded>([^<]*)<\/cke:encoded>/gi;

	var protectElementNamesRegex = /(<\/?)((?:object|embed|param|html|body|head|title)[^>]*>)/gi,
		unprotectElementNamesRegex = /(<\/?)cke:((?:html|body|head|title)[^>]*>)/gi;

	var protectSelfClosingRegex = /<cke:(param|embed)([^>]*?)\/?>(?!\s*<\/cke:\1)/gi;

	function protectAttributes( html )
	{
		return html.replace( protectElementRegex, function( element, tag, attributes )
		{
			return '<' +  tag + attributes.replace( protectAttributeRegex, function( fullAttr, attrName )
			{
				// Avoid corrupting the inline event attributes (#7243).
				// We should not rewrite the existed protected attributes, e.g. clipboard content from editor. (#5218)
				if ( !( /^on/ ).test( attrName ) && attributes.indexOf( 'data-cke-saved-' + attrName ) == -1 )
					return ' data-cke-saved-' + fullAttr + ' data-cke-' + CKEDITOR.rnd + '-' + fullAttr;

				return fullAttr;
			}) + '>';
		});
	}

	function protectElements( html )
	{
		return html.replace( protectElementsRegex, function( match )
			{
				return '<cke:encoded>' + encodeURIComponent( match ) + '</cke:encoded>';
			});
	}

	function unprotectElements( html )
	{
		return html.replace( encodedElementsRegex, function( match, encoded )
			{
				return decodeURIComponent( encoded );
			});
	}

	function protectElementsNames( html )
	{
		return html.replace( protectElementNamesRegex, '$1cke:$2');
	}

	function unprotectElementNames( html )
	{
		return html.replace( unprotectElementNamesRegex, '$1$2' );
	}

	function protectSelfClosingElements( html )
	{
		return html.replace( protectSelfClosingRegex, '<cke:$1$2></cke:$1>' );
	}

	function protectPreFormatted( html )
	{
		return html.replace( /(<pre\b[^>]*>)(\r\n|\n)/g, '$1$2$2' );
	}

	function protectRealComments( html )
	{
		return html.replace( /<!--(?!{cke_protected})[\s\S]+?-->/g, function( match )
			{
				return '<!--' + protectedSourceMarker +
						'{C}' +
						encodeURIComponent( match ).replace( /--/g, '%2D%2D' ) +
						'-->';
			});
	}

	function unprotectRealComments( html )
	{
		return html.replace( /<!--\{cke_protected\}\{C\}([\s\S]+?)-->/g, function( match, data )
			{
				return decodeURIComponent( data );
			});
	}

	function unprotectSource( html, editor )
	{
		var store = editor._.dataStore;

		return html.replace( /<!--\{cke_protected\}([\s\S]+?)-->/g, function( match, data )
			{
				return decodeURIComponent( data );
			}).replace( /\{cke_protected_(\d+)\}/g, function( match, id )
			{
				return store && store[ id ] || '';
			});
	}

	function protectSource( data, editor )
	{
		var protectedHtml = [],
			protectRegexes = editor.config.protectedSource,
			store = editor._.dataStore || ( editor._.dataStore = { id : 1 } ),
			tempRegex = /<\!--\{cke_temp(comment)?\}(\d*?)-->/g;

		var regexes =
			[
				// Script tags will also be forced to be protected, otherwise
				// IE will execute them.
				( /<script[\s\S]*?<\/script>/gi ),

				// <noscript> tags (get lost in IE and messed up in FF).
				/<noscript[\s\S]*?<\/noscript>/gi
			]
			.concat( protectRegexes );

		// First of any other protection, we must protect all comments
		// to avoid loosing them (of course, IE related).
		// Note that we use a different tag for comments, as we need to
		// transform them when applying filters.
		data = data.replace( (/<!--[\s\S]*?-->/g), function( match )
			{
				return  '<!--{cke_tempcomment}' + ( protectedHtml.push( match ) - 1 ) + '-->';
			});

		for ( var i = 0 ; i < regexes.length ; i++ )
		{
			data = data.replace( regexes[i], function( match )
				{
					match = match.replace( tempRegex, 		// There could be protected source inside another one. (#3869).
						function( $, isComment, id )
						{
							return protectedHtml[ id ];
						}
					);

					// Avoid protecting over protected, e.g. /\{.*?\}/
					return ( /cke_temp(comment)?/ ).test( match ) ? match
						: '<!--{cke_temp}' + ( protectedHtml.push( match ) - 1 ) + '-->';
				});
		}
		data = data.replace( tempRegex,	function( $, isComment, id )
			{
				return '<!--' + protectedSourceMarker +
						( isComment ? '{C}' : '' ) +
						encodeURIComponent( protectedHtml[ id ] ).replace( /--/g, '%2D%2D' ) +
						'-->';
			}
		);

		// Different protection pattern is used for those that
		// live in attributes to avoid from being HTML encoded.
		return data.replace( /(['"]).*?\1/g, function ( match )
		{
			return match.replace( /<!--\{cke_protected\}([\s\S]+?)-->/g, function( match, data )
			{
				store[ store.id ] = decodeURIComponent( data );
				return '{cke_protected_'+ ( store.id++ )  + '}';
			});
		});
	}

	CKEDITOR.plugins.add( 'htmldataprocessor',
	{
		requires : [ 'htmlwriter' ],

		init : function( editor )
		{
			var dataProcessor = editor.dataProcessor = new CKEDITOR.htmlDataProcessor( editor );

			dataProcessor.writer.forceSimpleAmpersand = editor.config.forceSimpleAmpersand;

			dataProcessor.dataFilter.addRules( defaultDataFilterRules );
			dataProcessor.dataFilter.addRules( defaultDataBlockFilterRules );
			dataProcessor.htmlFilter.addRules( defaultHtmlFilterRules );

			var defaultHtmlBlockFilterRules = { elements : {} };
			for ( i in blockLikeTags )
				defaultHtmlBlockFilterRules.elements[ i ] = getBlockExtension( true, editor.config.fillEmptyBlocks );

			dataProcessor.htmlFilter.addRules( defaultHtmlBlockFilterRules );
		},

		onLoad : function()
		{
			! ( 'fillEmptyBlocks' in CKEDITOR.config ) && ( CKEDITOR.config.fillEmptyBlocks = 1 );
		}
	});

	CKEDITOR.htmlDataProcessor = function( editor )
	{
		this.editor = editor;

		this.writer = new CKEDITOR.htmlWriter();
		this.dataFilter = new CKEDITOR.htmlParser.filter();
		this.htmlFilter = new CKEDITOR.htmlParser.filter();
	};

	CKEDITOR.htmlDataProcessor.prototype =
	{
		toHtml : function( data, fixForBody )
		{
			// The source data is already HTML, but we need to clean
			// it up and apply the filter.

			data = protectSource( data, this.editor );

			// Before anything, we must protect the URL attributes as the
			// browser may changing them when setting the innerHTML later in
			// the code.
			data = protectAttributes( data );

			// Protect elements than can't be set inside a DIV. E.g. IE removes
			// style tags from innerHTML. (#3710)
			data = protectElements( data );

			// Certain elements has problem to go through DOM operation, protect
			// them by prefixing 'cke' namespace. (#3591)
			data = protectElementsNames( data );

			// All none-IE browsers ignore self-closed custom elements,
			// protecting them into open-close. (#3591)
			data = protectSelfClosingElements( data );

			// Compensate one leading line break after <pre> open as browsers
			// eat it up. (#5789)
			data = protectPreFormatted( data );

			// Call the browser to help us fixing a possibly invalid HTML
			// structure.
			var div = new CKEDITOR.dom.element( 'div' );

			// Add fake character to workaround IE comments bug. (#3801)
			div.setHtml( 'a' + data );
			data = div.getHtml().substr( 1 );

			// Restore shortly protected attribute names.
			data = data.replace( new RegExp( ' data-cke-' + CKEDITOR.rnd + '-', 'ig' ), ' ' );

			// Unprotect "some" of the protected elements at this point.
			data = unprotectElementNames( data );

			data = unprotectElements( data );

			// Restore the comments that have been protected, in this way they
			// can be properly filtered.
			data = unprotectRealComments( data );

			// Now use our parser to make further fixes to the structure, as
			// well as apply the filter.
			var fragment = CKEDITOR.htmlParser.fragment.fromHtml( data, fixForBody ),
				writer = new CKEDITOR.htmlParser.basicWriter();

			fragment.writeHtml( writer, this.dataFilter );
			data = writer.getHtml( true );

			// Protect the real comments again.
			data = protectRealComments( data );

			return data;
		},

		toDataFormat : function( html, fixForBody )
		{
			var writer = this.writer,
				fragment = CKEDITOR.htmlParser.fragment.fromHtml( html, fixForBody );

			writer.reset();

			fragment.writeHtml( writer, this.htmlFilter );

			var data = writer.getHtml( true );

			// Restore those non-HTML protected source. (#4475,#4880)
			data = unprotectRealComments( data );
			data = unprotectSource( data, this.editor );

			return data;
		}
	};
})();

/**
 * Whether to force using "&" instead of "&amp;amp;" in elements attributes
 * values, it's not recommended to change this setting for compliance with the
 * W3C XHTML 1.0 standards (<a href="http://www.w3.org/TR/xhtml1/#C_12">C.12, XHTML 1.0</a>).
 * @name CKEDITOR.config.forceSimpleAmpersand
 * @name CKEDITOR.config.forceSimpleAmpersand
 * @type Boolean
 * @default false
 * @example
 * config.forceSimpleAmpersand = false;
 */

/**
 * Whether a filler text (non-breaking space entity - &nbsp;) will be inserted into empty block elements in HTML output,
 * this is used to render block elements properly with line-height; When a function is instead specified,
 * it'll be passed a {@link CKEDITOR.htmlParser.element} to decide whether adding the filler text
 * by expecting a boolean return value.
 * @name CKEDITOR.config.fillEmptyBlocks
 * @since 3.5
 * @type Boolean
 * @default true
 * @example
 * config.fillEmptyBlocks = false;	// Prevent filler nodes in all empty blocks.
 *
 * // Prevent filler node only in float cleaners.
 * config.fillEmptyBlocks = function( element )
 * {
 * 	if ( element.attributes[ 'class' ].indexOf ( 'clear-both' ) != -1 )
 * 		return false;
 * }
 */
