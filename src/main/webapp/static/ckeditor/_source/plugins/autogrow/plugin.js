/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file AutoGrow plugin
 */
(function(){

	// Actual content height, figured out by appending check the last element's document position.
	function contentHeight( scrollable )
	{
		var overflowY = scrollable.getStyle( 'overflow-y' );

		var doc = scrollable.getDocument();
		// Create a temporary marker element.
		var marker = CKEDITOR.dom.element.createFromHtml( '<span style="margin:0;padding:0;border:0;clear:both;width:1px;height:1px;display:block;">' + ( CKEDITOR.env.webkit ? '&nbsp;' : '' ) + '</span>', doc );
		doc[ CKEDITOR.env.ie? 'getBody' : 'getDocumentElement']().append( marker );

		var height = marker.getDocumentPosition( doc ).y + marker.$.offsetHeight;
		marker.remove();
		scrollable.setStyle( 'overflow-y', overflowY );
		return height;
	}

	var resizeEditor = function( editor )
	{
		if ( !editor.window )
			return;

		var doc = editor.document,
			iframe = new CKEDITOR.dom.element( doc.getWindow().$.frameElement ),
			body = doc.getBody(),
			htmlElement = doc.getDocumentElement(),
			currentHeight = editor.window.getViewPaneSize().height,
			// Quirks mode overflows body, standards overflows document element
			scrollable = doc.$.compatMode == 'BackCompat' ? body : htmlElement,
			newHeight = contentHeight( scrollable );

		// Additional space specified by user.
		newHeight += ( editor.config.autoGrow_bottomSpace || 0 );

		var min = editor.config.autoGrow_minHeight != undefined ? editor.config.autoGrow_minHeight : 200,
			max = editor.config.autoGrow_maxHeight || Infinity;

		newHeight = Math.max( newHeight, min );
		newHeight = Math.min( newHeight, max );

		if ( newHeight != currentHeight )
		{
			newHeight = editor.fire( 'autoGrow', { currentHeight : currentHeight, newHeight : newHeight } ).newHeight;
			editor.resize( editor.container.getStyle( 'width' ), newHeight, true );
		}

		if ( scrollable.$.scrollHeight > scrollable.$.clientHeight && newHeight < max )
			scrollable.setStyle( 'overflow-y', 'hidden' );
		else
			scrollable.removeStyle( 'overflow-y' );


	};

	CKEDITOR.plugins.add( 'autogrow',
	{
		init : function( editor )
		{
			editor.addCommand( 'autogrow', { exec : resizeEditor, modes : { wysiwyg:1 }, readOnly: 1, canUndo: false, editorFocus: false } );

			var eventsList = { contentDom:1, key:1, selectionChange:1, insertElement:1, mode:1 };
			editor.config.autoGrow_onStartup && ( eventsList[ 'instanceReady' ] = 1 );
			for ( var eventName in eventsList )
			{
				editor.on( eventName, function( evt )
				{
					var maximize = editor.getCommand( 'maximize' );
					// Some time is required for insertHtml, and it gives other events better performance as well.
					if ( evt.editor.mode == 'wysiwyg' &&
						// Disable autogrow when the editor is maximized .(#6339)
						( !maximize || maximize.state != CKEDITOR.TRISTATE_ON ) )
					{
						setTimeout( function()
						{
							resizeEditor( evt.editor );
							// Second pass to make correction upon
							// the first resize, e.g. scrollbar.
							resizeEditor( evt.editor );
						}, 100 );
					}
				});
			}
		}
	});
})();
/**
 * The minimum height that the editor can reach using the AutoGrow feature.
 * @name CKEDITOR.config.autoGrow_minHeight
 * @type Number
 * @default <code>200</code>
 * @since 3.4
 * @example
 * config.autoGrow_minHeight = 300;
 */

/**
 * The maximum height that the editor can reach using the AutoGrow feature. Zero means unlimited.
 * @name CKEDITOR.config.autoGrow_maxHeight
 * @type Number
 * @default <code>0</code>
 * @since 3.4
 * @example
 * config.autoGrow_maxHeight = 400;
 */

 /**
 * Whether to have the auto grow happen on editor creation.
 * @name CKEDITOR.config.autoGrow_onStartup
 * @type Boolean
 * @default false
 * @since 3.6.2
 * @example
 * config.autoGrow_onStartup = true;
 */

/**
 * Fired when the AutoGrow plugin is about to change the size of the editor.
 * @name CKEDITOR.editor#autogrow
 * @event
 * @param {Number} data.currentHeight The current height of the editor (before resizing).
 * @param {Number} data.newHeight The new height of the editor (after resizing). It can be changed
 *				to determine a different height value to be used instead.
 */


/**
 *  Extra height in pixel to leave between the bottom boundary of content with document size when auto resizing.
 * @name CKEDITOR.config.autoGrow_bottomSpace
 * @type Number
 * @default 0
 * @since 3.6.2
 */
