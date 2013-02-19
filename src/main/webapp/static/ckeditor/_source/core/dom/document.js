/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.dom.document} class, which
 *		represents a DOM document.
 */

/**
 * Represents a DOM document.
 * @constructor
 * @augments CKEDITOR.dom.domObject
 * @param {Object} domDocument A native DOM document.
 * @example
 * var document = new CKEDITOR.dom.document( document );
 */
CKEDITOR.dom.document = function( domDocument )
{
	CKEDITOR.dom.domObject.call( this, domDocument );
};

// PACKAGER_RENAME( CKEDITOR.dom.document )

CKEDITOR.dom.document.prototype = new CKEDITOR.dom.domObject();

CKEDITOR.tools.extend( CKEDITOR.dom.document.prototype,
	/** @lends CKEDITOR.dom.document.prototype */
	{
		/**
		 * Appends a CSS file to the document.
		 * @param {String} cssFileUrl The CSS file URL.
		 * @example
		 * <b>CKEDITOR.document.appendStyleSheet( '/mystyles.css' )</b>;
		 */
		appendStyleSheet : function( cssFileUrl )
		{
			if ( this.$.createStyleSheet )
				this.$.createStyleSheet( cssFileUrl );
			else
			{
				var link = new CKEDITOR.dom.element( 'link' );
				link.setAttributes(
					{
						rel		:'stylesheet',
						type	: 'text/css',
						href	: cssFileUrl
					});

				this.getHead().append( link );
			}
		},

		appendStyleText : function( cssStyleText )
		{
			if ( this.$.createStyleSheet )
			{
				var styleSheet = this.$.createStyleSheet( "" );
				styleSheet.cssText = cssStyleText ;
			}
			else
			{
				var style = new CKEDITOR.dom.element( 'style', this );
				style.append( new CKEDITOR.dom.text( cssStyleText, this ) );
				this.getHead().append( style );
			}
		},

		createElement : function( name, attribsAndStyles )
		{
			var element = new CKEDITOR.dom.element( name, this );

			if ( attribsAndStyles )
			{
				if ( attribsAndStyles.attributes )
					element.setAttributes( attribsAndStyles.attributes );

				if ( attribsAndStyles.styles )
					element.setStyles( attribsAndStyles.styles );
			}

			return element;
		},

		createText : function( text )
		{
			return new CKEDITOR.dom.text( text, this );
		},

		focus : function()
		{
			this.getWindow().focus();
		},

		/**
		 * Gets and element based on its id.
		 * @param {String} elementId The element id.
		 * @returns {CKEDITOR.dom.element} The element instance, or null if not found.
		 * @example
		 * var element = <b>CKEDITOR.document.getById( 'myElement' )</b>;
		 * alert( element.getId() );  // "myElement"
		 */
		getById : function( elementId )
		{
			var $ = this.$.getElementById( elementId );
			return $ ? new CKEDITOR.dom.element( $ ) : null;
		},

		getByAddress : function( address, normalized )
		{
			var $ = this.$.documentElement;

			for ( var i = 0 ; $ && i < address.length ; i++ )
			{
				var target = address[ i ];

				if ( !normalized )
				{
					$ = $.childNodes[ target ];
					continue;
				}

				var currentIndex = -1;

				for (var j = 0 ; j < $.childNodes.length ; j++ )
				{
					var candidate = $.childNodes[ j ];

					if ( normalized === true &&
							candidate.nodeType == 3 &&
							candidate.previousSibling &&
							candidate.previousSibling.nodeType == 3 )
					{
						continue;
					}

					currentIndex++;

					if ( currentIndex == target )
					{
						$ = candidate;
						break;
					}
				}
			}

			return $ ? new CKEDITOR.dom.node( $ ) : null;
		},

		getElementsByTag : function( tagName, namespace )
		{
			if ( !( CKEDITOR.env.ie && ! ( document.documentMode > 8 ) ) && namespace )
				tagName = namespace + ':' + tagName;
			return new CKEDITOR.dom.nodeList( this.$.getElementsByTagName( tagName ) );
		},

		/**
		 * Gets the &lt;head&gt; element for this document.
		 * @returns {CKEDITOR.dom.element} The &lt;head&gt; element.
		 * @example
		 * var element = <b>CKEDITOR.document.getHead()</b>;
		 * alert( element.getName() );  // "head"
		 */
		getHead : function()
		{
			var head = this.$.getElementsByTagName( 'head' )[0];
			if ( !head )
				head = this.getDocumentElement().append( new CKEDITOR.dom.element( 'head' ), true );
			else
			head = new CKEDITOR.dom.element( head );

			return (
			this.getHead = function()
				{
					return head;
				})();
		},

		/**
		 * Gets the &lt;body&gt; element for this document.
		 * @returns {CKEDITOR.dom.element} The &lt;body&gt; element.
		 * @example
		 * var element = <b>CKEDITOR.document.getBody()</b>;
		 * alert( element.getName() );  // "body"
		 */
		getBody : function()
		{
			var body = new CKEDITOR.dom.element( this.$.body );

			return (
			this.getBody = function()
				{
					return body;
				})();
		},

		/**
		 * Gets the DOM document element for this document.
		 * @returns {CKEDITOR.dom.element} The DOM document element.
		 */
		getDocumentElement : function()
		{
			var documentElement = new CKEDITOR.dom.element( this.$.documentElement );

			return (
			this.getDocumentElement = function()
				{
					return documentElement;
				})();
		},

		/**
		 * Gets the window object that holds this document.
		 * @returns {CKEDITOR.dom.window} The window object.
		 */
		getWindow : function()
		{
			var win = new CKEDITOR.dom.window( this.$.parentWindow || this.$.defaultView );

			return (
			this.getWindow = function()
				{
					return win;
				})();
		},

		/**
		 * Defines the document contents through document.write. Note that the
		 * previous document contents will be lost (cleaned).
		 * @since 3.5
		 * @param {String} html The HTML defining the document contents.
		 * @example
		 * document.write(
		 *     '&lt;html&gt;' +
		 *         '&lt;head&gt;&lt;title&gt;Sample Doc&lt;/title&gt;&lt;/head&gt;' +
		 *         '&lt;body&gt;Document contents created by code&lt;/body&gt;' +
		 *      '&lt;/html&gt;' );
		 */
		write : function( html )
		{
			// Don't leave any history log in IE. (#5657)
			this.$.open( 'text/html', 'replace' );

			// Support for custom document.domain in IE.
			CKEDITOR.env.isCustomDomain() &&  ( this.$.domain = document.domain );

			this.$.write( html );
			this.$.close();
		}
	});
