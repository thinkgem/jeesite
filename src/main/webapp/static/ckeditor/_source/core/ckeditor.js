/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Contains the third and last part of the {@link CKEDITOR} object
 *		definition.
 */

// Remove the CKEDITOR.loadFullCore reference defined on ckeditor_basic.
delete CKEDITOR.loadFullCore;

/**
 * Holds references to all editor instances created. The name of the properties
 * in this object correspond to instance names, and their values contains the
 * {@link CKEDITOR.editor} object representing them.
 * @type {Object}
 * @example
 * alert( <b>CKEDITOR.instances</b>.editor1.name );  // "editor1"
 */
CKEDITOR.instances = {};

/**
 * The document of the window holding the CKEDITOR object.
 * @type {CKEDITOR.dom.document}
 * @example
 * alert( <b>CKEDITOR.document</b>.getBody().getName() );  // "body"
 */
CKEDITOR.document = new CKEDITOR.dom.document( document );

/**
 * Adds an editor instance to the global {@link CKEDITOR} object. This function
 * is available for internal use mainly.
 * @param {CKEDITOR.editor} editor The editor instance to be added.
 * @example
 */
CKEDITOR.add = function( editor )
{
	CKEDITOR.instances[ editor.name ] = editor;

	editor.on( 'focus', function()
		{
			if ( CKEDITOR.currentInstance != editor )
			{
				CKEDITOR.currentInstance = editor;
				CKEDITOR.fire( 'currentInstance' );
			}
		});

	editor.on( 'blur', function()
		{
			if ( CKEDITOR.currentInstance == editor )
			{
				CKEDITOR.currentInstance = null;
				CKEDITOR.fire( 'currentInstance' );
			}
		});
};

/**
 * Removes an editor instance from the global {@link CKEDITOR} object. This function
 * is available for internal use only. External code must use {@link CKEDITOR.editor.prototype.destroy}
 * to avoid memory leaks.
 * @param {CKEDITOR.editor} editor The editor instance to be removed.
 * @example
 */
CKEDITOR.remove = function( editor )
{
	delete CKEDITOR.instances[ editor.name ];
};

/**
 * Perform global clean up to free as much memory as possible
 * when there are no instances left
 */
CKEDITOR.on( 'instanceDestroyed', function ()
	{
		if ( CKEDITOR.tools.isEmpty( this.instances ) )
			CKEDITOR.fire( 'reset' );
	});

// Load the bootstrap script.
CKEDITOR.loader.load( 'core/_bootstrap' );		// @Packager.RemoveLine

// Tri-state constants.

/**
 * Used to indicate the ON or ACTIVE state.
 * @constant
 * @example
 */
CKEDITOR.TRISTATE_ON = 1;

/**
 * Used to indicate the OFF or NON ACTIVE state.
 * @constant
 * @example
 */
CKEDITOR.TRISTATE_OFF = 2;

/**
 * Used to indicate DISABLED state.
 * @constant
 * @example
 */
CKEDITOR.TRISTATE_DISABLED = 0;

/**
 * The editor which is currently active (have user focus).
 * @name CKEDITOR.currentInstance
 * @type CKEDITOR.editor
 * @see CKEDITOR#currentInstance
 * @example
 * function showCurrentEditorName()
 * {
 *     if ( CKEDITOR.currentInstance )
 *         alert( CKEDITOR.currentInstance.name );
 *     else
 *         alert( 'Please focus an editor first.' );
 * }
 */

/**
 * Fired when the CKEDITOR.currentInstance object reference changes. This may
 * happen when setting the focus on different editor instances in the page.
 * @name CKEDITOR#currentInstance
 * @event
 * var editor;  // Variable to hold a reference to the current editor.
 * CKEDITOR.on( 'currentInstance' , function( e )
 *     {
 *         editor = CKEDITOR.currentInstance;
 *     });
 */

/**
 * Fired when the last instance has been destroyed. This event is used to perform
 * global memory clean up.
 * @name CKEDITOR#reset
 * @event
 */
