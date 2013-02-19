/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	var flashFilenameRegex = /\.swf(?:$|\?)/i;

	function isFlashEmbed( element )
	{
		var attributes = element.attributes;

		return ( attributes.type == 'application/x-shockwave-flash' || flashFilenameRegex.test( attributes.src || '' ) );
	}

	function createFakeElement( editor, realElement )
	{
		return editor.createFakeParserElement( realElement, 'cke_flash', 'flash', true );
	}

	CKEDITOR.plugins.add( 'flash',
	{
		init : function( editor )
		{
			editor.addCommand( 'flash', new CKEDITOR.dialogCommand( 'flash' ) );
			editor.ui.addButton( 'Flash',
				{
					label : editor.lang.common.flash,
					command : 'flash'
				});
			CKEDITOR.dialog.add( 'flash', this.path + 'dialogs/flash.js' );

			editor.addCss(
				'img.cke_flash' +
				'{' +
					'background-image: url(' + CKEDITOR.getUrl( this.path + 'images/placeholder.png' ) + ');' +
					'background-position: center center;' +
					'background-repeat: no-repeat;' +
					'border: 1px solid #a9a9a9;' +
					'width: 80px;' +
					'height: 80px;' +
				'}'
				);

			// If the "menu" plugin is loaded, register the menu items.
			if ( editor.addMenuItems )
			{
				editor.addMenuItems(
					{
						flash :
						{
							label : editor.lang.flash.properties,
							command : 'flash',
							group : 'flash'
						}
					});
			}

			editor.on( 'doubleclick', function( evt )
				{
					var element = evt.data.element;

					if ( element.is( 'img' ) && element.data( 'cke-real-element-type' ) == 'flash' )
						evt.data.dialog = 'flash';
				});

			// If the "contextmenu" plugin is loaded, register the listeners.
			if ( editor.contextMenu )
			{
				editor.contextMenu.addListener( function( element, selection )
					{
						if ( element && element.is( 'img' ) && !element.isReadOnly()
								&& element.data( 'cke-real-element-type' ) == 'flash' )
							return { flash : CKEDITOR.TRISTATE_OFF };
					});
			}
		},

		afterInit : function( editor )
		{
			var dataProcessor = editor.dataProcessor,
				dataFilter = dataProcessor && dataProcessor.dataFilter;

			if ( dataFilter )
			{
				dataFilter.addRules(
					{
						elements :
						{
							'cke:object' : function( element )
							{
								var attributes = element.attributes,
									classId = attributes.classid && String( attributes.classid ).toLowerCase();

								if ( !classId && !isFlashEmbed( element ) )
								{
									// Look for the inner <embed>
									for ( var i = 0 ; i < element.children.length ; i++ )
									{
										if ( element.children[ i ].name == 'cke:embed' )
										{
											if ( !isFlashEmbed( element.children[ i ] ) )
												return null;

											return createFakeElement( editor, element );
										}
									}
									return null;
								}

								return createFakeElement( editor, element );
							},

							'cke:embed' : function( element )
							{
								if ( !isFlashEmbed( element ) )
									return null;

								return createFakeElement( editor, element );
							}
						}
					},
					5);
			}
		},

		requires : [ 'fakeobjects' ]
	});
})();

CKEDITOR.tools.extend( CKEDITOR.config,
{
	/**
	 * Save as EMBED tag only. This tag is unrecommended.
	 * @type Boolean
	 * @default false
	 */
	flashEmbedTagOnly : false,

	/**
	 * Add EMBED tag as alternative: &lt;object&gt&lt;embed&gt&lt;/embed&gt&lt;/object&gt
	 * @type Boolean
	 * @default false
	 */
	flashAddEmbedTag : true,

	/**
	 * Use embedTagOnly and addEmbedTag values on edit.
	 * @type Boolean
	 * @default false
	 */
	flashConvertOnEdit : false
} );
