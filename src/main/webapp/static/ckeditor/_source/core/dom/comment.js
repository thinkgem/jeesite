/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.dom.comment} class, which represents
 *		a DOM comment node.
 */

/**
 * Represents a DOM comment node.
 * @constructor
 * @augments CKEDITOR.dom.node
 * @param {Object|String} comment A native DOM comment node or a string containing
 *		the text to use to create a new comment node.
 * @param {CKEDITOR.dom.document} [ownerDocument] The document that will contain
 *		the node in case of new node creation. Defaults to the current document.
 * @example
 * var nativeNode = document.createComment( 'Example' );
 * var comment = CKEDITOR.dom.comment( nativeNode );
 * @example
 * var comment = CKEDITOR.dom.comment( 'Example' );
 */
CKEDITOR.dom.comment = function( comment, ownerDocument )
{
	if ( typeof comment == 'string' )
		comment = ( ownerDocument ? ownerDocument.$ : document ).createComment( comment );

	CKEDITOR.dom.domObject.call( this, comment );
};

CKEDITOR.dom.comment.prototype = new CKEDITOR.dom.node();

CKEDITOR.tools.extend( CKEDITOR.dom.comment.prototype,
	/** @lends CKEDITOR.dom.comment.prototype */
	{
		type : CKEDITOR.NODE_COMMENT,

		getOuterHtml : function()
		{
			return '<!--' + this.$.nodeValue + '-->';
		}
	});
