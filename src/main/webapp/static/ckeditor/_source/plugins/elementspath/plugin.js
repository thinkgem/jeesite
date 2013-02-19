/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview The "elementspath" plugin. It shows all elements in the DOM
 *		parent tree relative to the current selection in the editing area.
 */

(function()
{
	var commands =
	{
		toolbarFocus :
		{
			editorFocus : false,
			readOnly : 1,
			exec : function( editor )
			{
				var idBase = editor._.elementsPath.idBase;
				var element = CKEDITOR.document.getById( idBase + '0' );

				// Make the first button focus accessible for IE. (#3417)
				// Adobe AIR instead need while of delay.
				element && element.focus( CKEDITOR.env.ie || CKEDITOR.env.air );
			}
		}
	};

	var emptyHtml = '<span class="cke_empty">&nbsp;</span>';

	CKEDITOR.plugins.add( 'elementspath',
	{
		requires : [ 'selection' ],

		init : function( editor )
		{
			var spaceId = 'cke_path_' + editor.name;
			var spaceElement;
			var getSpaceElement = function()
			{
				if ( !spaceElement )
					spaceElement = CKEDITOR.document.getById( spaceId );
				return spaceElement;
			};

			var idBase = 'cke_elementspath_' + CKEDITOR.tools.getNextNumber() + '_';

			editor._.elementsPath = { idBase : idBase, filters : [] };

			editor.on( 'themeSpace', function( event )
				{
					if ( event.data.space == 'bottom' )
					{
						event.data.html +=
							'<span id="' + spaceId + '_label" class="cke_voice_label">' + editor.lang.elementsPath.eleLabel + '</span>' +
							'<div id="' + spaceId + '" class="cke_path" role="group" aria-labelledby="' + spaceId + '_label">' + emptyHtml + '</div>';
					}
				});

			function onClick( elementIndex )
			{
				editor.focus();
				var element = editor._.elementsPath.list[ elementIndex ];
				if ( element.is( 'body' ) )
				{
					var range = new CKEDITOR.dom.range( editor.document );
					range.selectNodeContents( element );
					range.select();
				}
				else
					editor.getSelection().selectElement( element );
			}

			var onClickHanlder = CKEDITOR.tools.addFunction( onClick );

			var onKeyDownHandler = CKEDITOR.tools.addFunction( function( elementIndex, ev )
				{
					var idBase = editor._.elementsPath.idBase,
						element;

					ev = new CKEDITOR.dom.event( ev );

					var rtl = editor.lang.dir == 'rtl';
					switch ( ev.getKeystroke() )
					{
						case rtl ? 39 : 37 :		// LEFT-ARROW
						case 9 :					// TAB
							element = CKEDITOR.document.getById( idBase + ( elementIndex + 1 ) );
							if ( !element )
								element = CKEDITOR.document.getById( idBase + '0' );
							element.focus();
							return false;

						case rtl ? 37 : 39 :		// RIGHT-ARROW
						case CKEDITOR.SHIFT + 9 :	// SHIFT + TAB
							element = CKEDITOR.document.getById( idBase + ( elementIndex - 1 ) );
							if ( !element )
								element = CKEDITOR.document.getById( idBase + ( editor._.elementsPath.list.length - 1 ) );
							element.focus();
							return false;

						case 27 :					// ESC
							editor.focus();
							return false;

						case 13 :					// ENTER	// Opera
						case 32 :					// SPACE
							onClick( elementIndex );
							return false;
					}
					return true;
				});

			editor.on( 'selectionChange', function( ev )
				{
					var env = CKEDITOR.env,
						selection = ev.data.selection,
						element = selection.getStartElement(),
						html = [],
						editor = ev.editor,
						elementsList = editor._.elementsPath.list = [],
						filters = editor._.elementsPath.filters;

					while ( element )
					{
						var ignore = 0,
							name;

						if ( element.data( 'cke-display-name' ) )
							name = element.data( 'cke-display-name' );
						else if ( element.data( 'cke-real-element-type' ) )
							name = element.data( 'cke-real-element-type' );
						else
							name = element.getName();

						for ( var i = 0; i < filters.length; i++ )
						{
							var ret = filters[ i ]( element, name );
							if ( ret === false )
							{
								ignore = 1;
								break;
							}
							name = ret || name;
						}

						if ( !ignore )
						{
							var index = elementsList.push( element ) - 1;

							// Use this variable to add conditional stuff to the
							// HTML (because we are doing it in reverse order... unshift).
							var extra = '';

							// Some browsers don't cancel key events in the keydown but in the
							// keypress.
							// TODO: Check if really needed for Gecko+Mac.
							if ( env.opera || ( env.gecko && env.mac ) )
								extra += ' onkeypress="return false;"';

							// With Firefox, we need to force the button to redraw, otherwise it
							// will remain in the focus state.
							if ( env.gecko )
								extra += ' onblur="this.style.cssText = this.style.cssText;"';

							var label = editor.lang.elementsPath.eleTitle.replace( /%1/, name );
							html.unshift(
								'<a' +
									' id="', idBase, index, '"' +
									' href="javascript:void(\'', name, '\')"' +
									' tabindex="-1"' +
									' title="', label, '"' +
									( ( CKEDITOR.env.gecko && CKEDITOR.env.version < 10900 ) ?
									' onfocus="event.preventBubble();"' : '' ) +
									' hidefocus="true" ' +
									' onkeydown="return CKEDITOR.tools.callFunction(', onKeyDownHandler, ',', index, ', event );"' +
									extra ,
									' onclick="CKEDITOR.tools.callFunction('+ onClickHanlder, ',', index, '); return false;"',
									' role="button" aria-labelledby="' + idBase + index + '_label">',
										name,
										'<span id="', idBase, index, '_label" class="cke_label">' + label + '</span>',
								'</a>' );

						}

						if ( name == 'body' )
							break;

						element = element.getParent();
					}

					var space = getSpaceElement();
					space.setHtml( html.join('') + emptyHtml );
					editor.fire( 'elementsPathUpdate', { space : space } );
				});

			function empty()
			{
				spaceElement && spaceElement.setHtml( emptyHtml );
				delete editor._.elementsPath.list;
			}

			editor.on( 'readOnly', empty );
			editor.on( 'contentDomUnload', empty );

			editor.addCommand( 'elementsPathFocus', commands.toolbarFocus );
		}
	});
})();

/**
 * Fired when the contents of the elementsPath are changed
 * @name CKEDITOR.editor#elementsPathUpdate
 * @event
 * @param {Object} eventData.space The elementsPath container
 */
