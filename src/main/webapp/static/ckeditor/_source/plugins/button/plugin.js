/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'button',
{
	beforeInit : function( editor )
	{
		editor.ui.addHandler( CKEDITOR.UI_BUTTON, CKEDITOR.ui.button.handler );
	}
});

/**
 * Button UI element.
 * @constant
 * @example
 */
CKEDITOR.UI_BUTTON = 'button';

/**
 * Represents a button UI element. This class should not be called directly. To
 * create new buttons use {@link CKEDITOR.ui.prototype.addButton} instead.
 * @constructor
 * @param {Object} definition The button definition.
 * @example
 */
CKEDITOR.ui.button = function( definition )
{
	// Copy all definition properties to this object.
	CKEDITOR.tools.extend( this, definition,
		// Set defaults.
		{
			title		: definition.label,
			className	: definition.className || ( definition.command && 'cke_button_' + definition.command ) || '',
			click		: definition.click || function( editor )
				{
					editor.execCommand( definition.command );
				}
		});

	this._ = {};
};

/**
 * Transforms a button definition in a {@link CKEDITOR.ui.button} instance.
 * @type Object
 * @example
 */
CKEDITOR.ui.button.handler =
{
	create : function( definition )
	{
		return new CKEDITOR.ui.button( definition );
	}
};

( function()
{
CKEDITOR.ui.button.prototype =
{
	/**
	 * Renders the button.
	 * @param {CKEDITOR.editor} editor The editor instance which this button is
	 *		to be used by.
	 * @param {Array} output The output array to which append the HTML relative
	 *		to this button.
	 * @example
	 */
	render : function( editor, output )
	{
		var env = CKEDITOR.env,
			id = this._.id = CKEDITOR.tools.getNextId(),
			classes = '',
			command = this.command, // Get the command name.
			clickFn;

		this._.editor = editor;

		var instance =
		{
			id : id,
			button : this,
			editor : editor,
			focus : function()
			{
				var element = CKEDITOR.document.getById( id );
				element.focus();
			},
			execute : function()
			{
				// IE 6 needs some time before execution (#7922)
				if ( CKEDITOR.env.ie && CKEDITOR.env.version < 7 )
					CKEDITOR.tools.setTimeout( function(){ this.button.click( editor ); }, 0, this );
				else
					this.button.click( editor );
			}
		};

		var keydownFn = CKEDITOR.tools.addFunction( function( ev )
			{
				if ( instance.onkey )
				{
					ev = new CKEDITOR.dom.event( ev );
					return ( instance.onkey( instance, ev.getKeystroke() ) !== false );
				}
			});

		var focusFn = CKEDITOR.tools.addFunction( function( ev )
			{
				var retVal;

				if ( instance.onfocus )
					  retVal = ( instance.onfocus( instance, new CKEDITOR.dom.event( ev ) ) !== false );

				// FF2: prevent focus event been bubbled up to editor container, which caused unexpected editor focus.
				if ( CKEDITOR.env.gecko && CKEDITOR.env.version < 10900 )
					  ev.preventBubble();
				return retVal;
			});

		instance.clickFn = clickFn = CKEDITOR.tools.addFunction( instance.execute, instance );


		// Indicate a mode sensitive button.
		if ( this.modes )
		{
			var modeStates = {};

			function updateState()
			{
				// "this" is a CKEDITOR.ui.button instance.

				var mode = editor.mode;

				if ( mode )
				{
					// Restore saved button state.
					var state = this.modes[ mode ] ? modeStates[ mode ] != undefined ? modeStates[ mode ] :
							CKEDITOR.TRISTATE_OFF : CKEDITOR.TRISTATE_DISABLED;

					this.setState( editor.readOnly && !this.readOnly ? CKEDITOR.TRISTATE_DISABLED : state );
				}
			}

			editor.on( 'beforeModeUnload', function()
				{
					if ( editor.mode && this._.state != CKEDITOR.TRISTATE_DISABLED )
						modeStates[ editor.mode ] = this._.state;
				}, this );

			editor.on( 'mode', updateState, this);

			// If this button is sensitive to readOnly state, update it accordingly.
			!this.readOnly && editor.on( 'readOnly', updateState, this);
		}
		else if ( command )
		{
			// Get the command instance.
			command = editor.getCommand( command );

			if ( command )
			{
				command.on( 'state', function()
					{
						this.setState( command.state );
					}, this);

				classes += 'cke_' + (
					command.state == CKEDITOR.TRISTATE_ON ? 'on' :
					command.state == CKEDITOR.TRISTATE_DISABLED ? 'disabled' :
					'off' );
			}
		}

		if ( !command )
			classes	+= 'cke_off';

		if ( this.className )
			classes += ' ' + this.className;

		output.push(
			'<span class="cke_button' + ( this.icon && this.icon.indexOf( '.png' ) == -1 ? ' cke_noalphafix' : '' ) + '">',
			'<a id="', id, '"' +
				' class="', classes, '"',
				env.gecko && env.version >= 10900 && !env.hc  ? '' : '" href="javascript:void(\''+ ( this.title || '' ).replace( "'", '' )+ '\')"',
				' title="', this.title, '"' +
				' tabindex="-1"' +
				' hidefocus="true"' +
			    ' role="button"' +
				' aria-labelledby="' + id + '_label"' +
				( this.hasArrow ?  ' aria-haspopup="true"' : '' ) );

		// Some browsers don't cancel key events in the keydown but in the
		// keypress.
		// TODO: Check if really needed for Gecko+Mac.
		if ( env.opera || ( env.gecko && env.mac ) )
		{
			output.push(
				' onkeypress="return false;"' );
		}

		// With Firefox, we need to force the button to redraw, otherwise it
		// will remain in the focus state.
		if ( env.gecko )
		{
			output.push(
				' onblur="this.style.cssText = this.style.cssText;"' );
		}

		output.push(
					' onkeydown="return CKEDITOR.tools.callFunction(', keydownFn, ', event);"' +
					' onfocus="return CKEDITOR.tools.callFunction(', focusFn,', event);" ' +
					( CKEDITOR.env.ie ? 'onclick="return false;" onmouseup' : 'onclick' ) +		// #188
						'="CKEDITOR.tools.callFunction(', clickFn, ', this); return false;">' +
					'<span class="cke_icon"' );

		if ( this.icon )
		{
			var offset = ( this.iconOffset || 0 ) * -16;
			output.push( ' style="background-image:url(', CKEDITOR.getUrl( this.icon ), ');background-position:0 ' + offset + 'px;"' );
		}

		output.push(
					'>&nbsp;</span>' +
					'<span id="', id, '_label" class="cke_label">', this.label, '</span>' );

		if ( this.hasArrow )
		{
			output.push(
					'<span class="cke_buttonarrow">'
					// BLACK DOWN-POINTING TRIANGLE
					+ ( CKEDITOR.env.hc ? '&#9660;' : '&nbsp;' )
					+ '</span>' );
		}

		output.push(
			'</a>',
			'</span>' );

		if ( this.onRender )
			this.onRender();

		return instance;
	},

	setState : function( state )
	{
		if ( this._.state == state )
			return false;

		this._.state = state;

		var element = CKEDITOR.document.getById( this._.id );

		if ( element )
		{
			element.setState( state );
			state == CKEDITOR.TRISTATE_DISABLED ?
				element.setAttribute( 'aria-disabled', true ) :
				element.removeAttribute( 'aria-disabled' );

			state == CKEDITOR.TRISTATE_ON ?
				element.setAttribute( 'aria-pressed', true ) :
				element.removeAttribute( 'aria-pressed' );

			return true;
		}
		else
			return false;
	}
};

})();

/**
 * Adds a button definition to the UI elements list.
 * @param {String} name The button name.
 * @param {Object} definition The button definition.
 * @example
 * editorInstance.ui.addButton( 'MyBold',
 *     {
 *         label : 'My Bold',
 *         command : 'bold'
 *     });
 */
CKEDITOR.ui.prototype.addButton = function( name, definition )
{
	this.add( name, CKEDITOR.UI_BUTTON, definition );
};
