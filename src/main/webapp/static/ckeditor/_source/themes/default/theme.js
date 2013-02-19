/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @name CKEDITOR.theme
 * @class
 */

CKEDITOR.themes.add( 'default', (function()
{
	var hiddenSkins = {};

	function checkSharedSpace( editor, spaceName )
	{
		var container,
			element;

		// Try to retrieve the target element from the sharedSpaces settings.
		element = editor.config.sharedSpaces;
		element = element && element[ spaceName ];
		element = element && CKEDITOR.document.getById( element );

		// If the element is available, we'll then create the container for
		// the space.
		if ( element )
		{
			// Creates an HTML structure that reproduces the editor class hierarchy.
			var html =
				'<span class="cke_shared "' +
				' dir="'+ editor.lang.dir + '"' +
				'>' +
				'<span class="' + editor.skinClass + ' ' + editor.id + ' cke_editor_' + editor.name + '">' +
				'<span class="' + CKEDITOR.env.cssClass + '">' +
				'<span class="cke_wrapper cke_' + editor.lang.dir + '">' +
				'<span class="cke_editor">' +
				'<div class="cke_' + spaceName + '">' +
				'</div></span></span></span></span></span>';

			var mainContainer = element.append( CKEDITOR.dom.element.createFromHtml( html, element.getDocument() ) );

			// Only the first container starts visible. Others get hidden.
			if ( element.getCustomData( 'cke_hasshared' ) )
				mainContainer.hide();
			else
				element.setCustomData( 'cke_hasshared', 1 );

			// Get the deeper inner <div>.
			container = mainContainer.getChild( [0,0,0,0] );

			// Save a reference to the shared space container.
			!editor.sharedSpaces && ( editor.sharedSpaces = {} );
			editor.sharedSpaces[ spaceName ] = container;

			// When the editor gets focus, we show the space container, hiding others.
			editor.on( 'focus', function()
				{
					for ( var i = 0, sibling, children = element.getChildren() ; ( sibling = children.getItem( i ) ) ; i++ )
					{
						if ( sibling.type == CKEDITOR.NODE_ELEMENT
							&& !sibling.equals( mainContainer )
							&& sibling.hasClass( 'cke_shared' ) )
						{
							sibling.hide();
						}
					}

					mainContainer.show();
				});

			editor.on( 'destroy', function()
				{
					mainContainer.remove();
				});
		}

		return container;
	}

	return /** @lends CKEDITOR.theme */ {
		build : function( editor, themePath )
		{
			var name = editor.name,
				element = editor.element,
				elementMode = editor.elementMode;

			if ( !element || elementMode == CKEDITOR.ELEMENT_MODE_NONE )
				return;

			if ( elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
				element.hide();

			// Get the HTML for the predefined spaces.
			var topHtml			= editor.fire( 'themeSpace', { space : 'top', html : '' } ).html;
			var contentsHtml	= editor.fire( 'themeSpace', { space : 'contents', html : '' } ).html;
			var bottomHtml		= editor.fireOnce( 'themeSpace', { space : 'bottom', html : '' } ).html;

			var height	= contentsHtml && editor.config.height;

			var tabIndex = editor.config.tabIndex || editor.element.getAttribute( 'tabindex' ) || 0;

			// The editor height is considered only if the contents space got filled.
			if ( !contentsHtml )
				height = 'auto';
			else if ( !isNaN( height ) )
				height += 'px';

			var style = '';
			var width	= editor.config.width;

			if ( width )
			{
				if ( !isNaN( width ) )
					width += 'px';

				style += "width: " + width + ";";
			}

			var sharedTop		= topHtml && checkSharedSpace( editor, 'top' ),
				sharedBottoms	= checkSharedSpace( editor, 'bottom' );

			sharedTop		&& ( sharedTop.setHtml( topHtml )		, topHtml = '' );
			sharedBottoms	&& ( sharedBottoms.setHtml( bottomHtml ), bottomHtml = '' );

			var hideSkin = '<style>.' + editor.skinClass + '{visibility:hidden;}</style>';
			if ( hiddenSkins[ editor.skinClass ] )
				hideSkin = '';
			else
				hiddenSkins[ editor.skinClass ] = 1;

			var container = CKEDITOR.dom.element.createFromHtml( [
				'<span' +
					' id="cke_', name, '"' +
					' class="', editor.skinClass, ' ', editor.id, ' cke_editor_', name, '"' +
					' dir="', editor.lang.dir, '"' +
					' title="', ( CKEDITOR.env.gecko ? ' ' : '' ), '"' +
					' lang="', editor.langCode, '"' +
						( CKEDITOR.env.webkit? ' tabindex="' + tabIndex + '"' : '' ) +
					' role="application"' +
					' aria-labelledby="cke_', name, '_arialbl"' +
					( style ? ' style="' + style + '"' : '' ) +
					'>' +
					'<span id="cke_', name, '_arialbl" class="cke_voice_label">' + editor.lang.editor + '</span>' +
					'<span class="' , CKEDITOR.env.cssClass, '" role="presentation">' +
						'<span class="cke_wrapper cke_', editor.lang.dir, '" role="presentation">' +
							'<table class="cke_editor" border="0" cellspacing="0" cellpadding="0" role="presentation"><tbody>' +
								'<tr', topHtml		? '' : ' style="display:none"', ' role="presentation"><td id="cke_top_'		, name, '" class="cke_top" role="presentation">'	, topHtml		, '</td></tr>' +
								'<tr', contentsHtml	? '' : ' style="display:none"', ' role="presentation"><td id="cke_contents_', name, '" class="cke_contents" style="height:', height, '" role="presentation">', contentsHtml, '</td></tr>' +
								'<tr', bottomHtml	? '' : ' style="display:none"', ' role="presentation"><td id="cke_bottom_'	, name, '" class="cke_bottom" role="presentation">'	, bottomHtml	, '</td></tr>' +
							'</tbody></table>' +
							//Hide the container when loading skins, later restored by skin css.
							hideSkin +
						'</span>' +
					'</span>' +
				'</span>' ].join( '' ) );

			container.getChild( [1, 0, 0, 0, 0] ).unselectable();
			container.getChild( [1, 0, 0, 0, 2] ).unselectable();

			if ( elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
				container.insertAfter( element );
			else
				element.append( container );

			/**
			 * The DOM element that holds the main editor interface.
			 * @name CKEDITOR.editor.prototype.container
			 * @type CKEDITOR.dom.element
			 * @example
			 * var editor = CKEDITOR.instances.editor1;
			 * alert( <b>editor.container</b>.getName() );  "span"
			 */
			editor.container = container;

			// Disable browser context menu for editor's chrome.
			container.disableContextMenu();

			// Use a class to indicate that the current selection is in different direction than the UI.
			editor.on( 'contentDirChanged', function( evt )
			{
				var func = ( editor.lang.dir != evt.data ? 'add' : 'remove' ) + 'Class';

				container.getChild( 1 )[ func ]( 'cke_mixed_dir_content' );

				// Put the mixed direction class on the respective element also for shared spaces.
				var toolbarSpace = this.sharedSpaces && this.sharedSpaces[ this.config.toolbarLocation ];
				toolbarSpace && toolbarSpace.getParent().getParent()[ func ]( 'cke_mixed_dir_content' );
			});

			editor.fireOnce( 'themeLoaded' );
			editor.fireOnce( 'uiReady' );
		},

		buildDialog : function( editor )
		{
			var baseIdNumber = CKEDITOR.tools.getNextNumber();

			var element = CKEDITOR.dom.element.createFromHtml( [
					'<div class="', editor.id, '_dialog cke_editor_', editor.name.replace('.', '\\.'), '_dialog cke_skin_', editor.skinName,
						'" dir="', editor.lang.dir, '"' +
						' lang="', editor.langCode, '"' +
						' role="dialog"' +
						' aria-labelledby="%title#"' +
						'>' +
						'<table class="cke_dialog', ' ' + CKEDITOR.env.cssClass,
							' cke_', editor.lang.dir, '" style="position:absolute" role="presentation">' +
							'<tr><td role="presentation">' +
							'<div class="%body" role="presentation">' +
								'<div id="%title#" class="%title" role="presentation"></div>' +
								'<a id="%close_button#" class="%close_button" href="javascript:void(0)" title="' +  editor.lang.common.close+'" role="button"><span class="cke_label">X</span></a>' +
								'<div id="%tabs#" class="%tabs" role="tablist"></div>' +
								'<table class="%contents" role="presentation">' +
								'<tr>' +
								  '<td id="%contents#" class="%contents" role="presentation"></td>' +
								'</tr>' +
								'<tr>' +
								  '<td id="%footer#" class="%footer" role="presentation"></td>' +
								'</tr>' +
								'</table>' +
							'</div>' +
							'<div id="%tl#" class="%tl"></div>' +
							'<div id="%tc#" class="%tc"></div>' +
							'<div id="%tr#" class="%tr"></div>' +
							'<div id="%ml#" class="%ml"></div>' +
							'<div id="%mr#" class="%mr"></div>' +
							'<div id="%bl#" class="%bl"></div>' +
							'<div id="%bc#" class="%bc"></div>' +
							'<div id="%br#" class="%br"></div>' +
							'</td></tr>' +
						'</table>',

						//Hide the container when loading skins, later restored by skin css.
						( CKEDITOR.env.ie ? '' : '<style>.cke_dialog{visibility:hidden;}</style>' ),

					'</div>'
				].join( '' )
					.replace( /#/g, '_' + baseIdNumber )
					.replace( /%/g, 'cke_dialog_' ) );

			var body = element.getChild( [ 0, 0, 0, 0, 0 ] ),
				title = body.getChild( 0 ),
				close = body.getChild( 1 );

			// IFrame shim for dialog that masks activeX in IE. (#7619)
			if ( CKEDITOR.env.ie && !CKEDITOR.env.ie6Compat )
			{
				var isCustomDomain = CKEDITOR.env.isCustomDomain(),
					src = 'javascript:void(function(){' + encodeURIComponent( 'document.open();' + ( isCustomDomain ? ( 'document.domain="' + document.domain + '";' ) : '' ) + 'document.close();' ) + '}())',
					iframe = CKEDITOR.dom.element.createFromHtml( '<iframe' +
  							' frameBorder="0"' +
							' class="cke_iframe_shim"' +
  							' src="' + src + '"' +
							' tabIndex="-1"' +
  							'></iframe>' );
				iframe.appendTo( body.getParent() );
			}

			// Make the Title and Close Button unselectable.
			title.unselectable();
			close.unselectable();


			return {
				element : element,
				parts :
				{
					dialog		: element.getChild( 0 ),
					title		: title,
					close		: close,
					tabs		: body.getChild( 2 ),
					contents	: body.getChild( [ 3, 0, 0, 0 ] ),
					footer		: body.getChild( [ 3, 0, 1, 0 ] )
				}
			};
		},

		destroy : function( editor )
		{
			var container = editor.container,
				element = editor.element;

			if ( container )
			{
				container.clearCustomData();
				container.remove();
			}

			if ( element )
			{
				element.clearCustomData();
				editor.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE && element.show();
				delete editor.element;
			}
		}
	};
})() );

/**
 * Returns the DOM element that represents a theme space. The default theme defines
 * three spaces, namely "top", "contents" and "bottom", representing the main
 * blocks that compose the editor interface.
 * @param {String} spaceName The space name.
 * @returns {CKEDITOR.dom.element} The element that represents the space.
 * @example
 * // Hide the bottom space in the UI.
 * var bottom = editor.getThemeSpace( 'bottom' );
 * bottom.setStyle( 'display', 'none' );
 */
CKEDITOR.editor.prototype.getThemeSpace = function( spaceName )
{
	var spacePrefix = 'cke_' + spaceName;
	var space = this._[ spacePrefix ] ||
		( this._[ spacePrefix ] = CKEDITOR.document.getById( spacePrefix + '_' + this.name ) );
	return space;
};

/**
 * Resizes the editor interface.
 * @param {Number|String} width The new width. It can be an pixels integer or a
 *		CSS size value.
 * @param {Number|String} height The new height. It can be an pixels integer or
 *		a CSS size value.
 * @param {Boolean} [isContentHeight] Indicates that the provided height is to
 *		be applied to the editor contents space, not to the entire editor
 *		interface. Defaults to false.
 * @param {Boolean} [resizeInner] Indicates that the first inner interface
 *		element must receive the size, not the outer element. The default theme
 *		defines the interface inside a pair of span elements
 *		(&lt;span&gt;&lt;span&gt;...&lt;/span&gt;&lt;/span&gt;). By default the
 *		first span element receives the sizes. If this parameter is set to
 *		true, the second span is sized instead.
 * @example
 * editor.resize( 900, 300 );
 * @example
 * editor.resize( '100%', 450, true );
 */
CKEDITOR.editor.prototype.resize = function( width, height, isContentHeight, resizeInner )
{
	var container = this.container,
		contents = CKEDITOR.document.getById( 'cke_contents_' + this.name ),
		contentsFrame = CKEDITOR.env.webkit && this.document && this.document.getWindow().$.frameElement,
		outer = resizeInner ? container.getChild( 1 ) : container;

	// Set as border box width. (#5353)
	outer.setSize( 'width',  width, true );

	// WebKit needs to refresh the iframe size to avoid rendering issues. (1/2) (#8348)
	contentsFrame && ( contentsFrame.style.width = '1%' );

	// Get the height delta between the outer table and the content area.
	// If we're setting the content area's height, then we don't need the delta.
	var delta = isContentHeight ? 0 : ( outer.$.offsetHeight || 0 ) - ( contents.$.clientHeight || 0 );
	contents.setStyle( 'height', Math.max( height - delta, 0 ) + 'px' );

	// WebKit needs to refresh the iframe size to avoid rendering issues. (2/2) (#8348)
	contentsFrame && ( contentsFrame.style.width = '100%' );

	// Emit a resize event.
	this.fire( 'resize' );
};

/**
 * Gets the element that can be freely used to check the editor size. This method
 * is mainly used by the resize plugin, which adds a UI handle that can be used
 * to resize the editor.
 * @param {Boolean} forContents Whether to return the "contents" part of the theme instead of the container.
 * @returns {CKEDITOR.dom.element} The resizable element.
 * @example
 */
CKEDITOR.editor.prototype.getResizable = function( forContents )
{
	return forContents ? CKEDITOR.document.getById( 'cke_contents_' + this.name ) : this.container;
};

/**
 * Makes it possible to place some of the editor UI blocks, like the toolbar
 * and the elements path, into any element in the page.
 * The elements used to hold the UI blocks can be shared among several editor
 * instances. In that case, only the blocks of the active editor instance will
 * display.
 * @name CKEDITOR.config.sharedSpaces
 * @type Object
 * @default undefined
 * @example
 * // Place the toolbar inside the element with ID "someElementId" and the
 * // elements path into the element with ID "anotherId".
 * config.sharedSpaces =
 * {
 *     top : 'someElementId',
 *     bottom : 'anotherId'
 * };
 * @example
 * // Place the toolbar inside the element with ID "someElementId". The
 * // elements path will remain attached to the editor UI.
 * config.sharedSpaces =
 * {
 *     top : 'someElementId'
 * };
 */

/**
 * Fired after the editor instance is resized through
 * the {@link CKEDITOR.editor.prototype.resize} method.
 * @name CKEDITOR.editor#resize
 * @event
 */
