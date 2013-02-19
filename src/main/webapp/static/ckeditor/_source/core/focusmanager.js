/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.focusManager} class, which is used
 *		to handle the focus on editor instances..
 */

/**
 * Creates a focusManager class instance.
 * @class Manages the focus activity in an editor instance. This class is to be
 * used mainly by UI elements coders when adding interface elements that need
 * to set the focus state of the editor.
 * @param {CKEDITOR.editor} editor The editor instance.
 * @example
 * var focusManager = <b>new CKEDITOR.focusManager( editor )</b>;
 * focusManager.focus();
 */
CKEDITOR.focusManager = function( editor )
{
	if ( editor.focusManager )
		return editor.focusManager;

	/**
	 * Indicates that the editor instance has focus.
	 * @type Boolean
	 * @example
	 * alert( CKEDITOR.instances.editor1.focusManager.hasFocus );  // e.g "true"
	 */
	this.hasFocus = false;

	/**
	 * Object used to hold private stuff.
	 * @private
	 */
	this._ =
	{
		editor : editor
	};

	return this;
};

CKEDITOR.focusManager.prototype =
{
	/**
	 * Used to indicate that the editor instance has the focus.<br />
	 * <br />
	 * Note that this function will not explicitelly set the focus in the
	 * editor (for example, making the caret blinking on it). Use
	 * {@link CKEDITOR.editor#focus} for it instead.
	 * @example
	 * var editor = CKEDITOR.instances.editor1;
	 * <b>editor.focusManager.focus()</b>;
	 */
	focus : function()
	{
		if ( this._.timer )
			clearTimeout( this._.timer );

		if ( !this.hasFocus )
		{
			// If another editor has the current focus, we first "blur" it. In
			// this way the events happen in a more logical sequence, like:
			//		"focus 1" > "blur 1" > "focus 2"
			// ... instead of:
			//		"focus 1" > "focus 2" > "blur 1"
			if ( CKEDITOR.currentInstance )
				CKEDITOR.currentInstance.focusManager.forceBlur();

			var editor = this._.editor;

			editor.container.getChild( 1 ).addClass( 'cke_focus' );

			this.hasFocus = true;
			editor.fire( 'focus' );
		}
	},

	/**
	 * Used to indicate that the editor instance has lost the focus.<br />
	 * <br />
	 * Note that this functions acts asynchronously with a delay of 100ms to
	 * avoid subsequent blur/focus effects. If you want the "blur" to happen
	 * immediately, use the {@link #forceBlur} function instead.
	 * @example
	 * var editor = CKEDITOR.instances.editor1;
	 * <b>editor.focusManager.blur()</b>;
	 */
	blur : function()
	{
		var focusManager = this;

		if ( focusManager._.timer )
			clearTimeout( focusManager._.timer );

		focusManager._.timer = setTimeout(
			function()
			{
				delete focusManager._.timer;
				focusManager.forceBlur();
			}
			, 100 );
	},

	/**
	 * Used to indicate that the editor instance has lost the focus. Unlike
	 * {@link #blur}, this function is synchronous, marking the instance as
	 * "blured" immediately.
	 * @example
	 * var editor = CKEDITOR.instances.editor1;
	 * <b>editor.focusManager.forceBlur()</b>;
	 */
	forceBlur : function()
	{
		if ( this.hasFocus )
		{
			var editor = this._.editor;

			editor.container.getChild( 1 ).removeClass( 'cke_focus' );

			this.hasFocus = false;
			editor.fire( 'blur' );
		}
	}
};

/**
 * Fired when the editor instance receives the input focus.
 * @name CKEDITOR.editor#focus
 * @event
 * @param {CKEDITOR.editor} editor The editor instance.
 * @example
 * editor.on( 'focus', function( e )
 *     {
 *         alert( 'The editor named ' + e.editor.name + ' is now focused' );
 *     });
 */

/**
 * Fired when the editor instance loses the input focus.
 * @name CKEDITOR.editor#blur
 * @event
 * @param {CKEDITOR.editor} editor The editor instance.
 * @example
 * editor.on( 'blur', function( e )
 *     {
 *         alert( 'The editor named ' + e.editor.name + ' lost the focus' );
 *     });
 */
