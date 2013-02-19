/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{

function setupAdvParams( element )
{
	var attrName = this.att;

	var value = element && element.hasAttribute( attrName ) && element.getAttribute( attrName ) || '';

	if ( value !== undefined )
		this.setValue( value );
}

function commitAdvParams()
{
	// Dialogs may use different parameters in the commit list, so, by
	// definition, we take the first CKEDITOR.dom.element available.
	var element;

	for ( var i = 0 ; i < arguments.length ; i++ )
	{
		if ( arguments[ i ] instanceof CKEDITOR.dom.element )
		{
			element = arguments[ i ];
			break;
		}
	}

	if ( element )
	{
		var attrName = this.att,
			value = this.getValue();

		if ( value )
			element.setAttribute( attrName, value );
		else
			element.removeAttribute( attrName, value );
	}
}

CKEDITOR.plugins.add( 'dialogadvtab',
{
	/**
	 *
	 * @param tabConfig
	 * id, dir, classes, styles
	 */
	createAdvancedTab : function( editor, tabConfig )
	{
		if ( !tabConfig )
			tabConfig = { id:1, dir:1, classes:1, styles:1 };

		var lang = editor.lang.common;

		var result =
		{
			id : 'advanced',
			label : lang.advancedTab,
			title : lang.advancedTab,
			elements :
				[
					{
						type : 'vbox',
						padding : 1,
						children : []
					}
				]
		};

		var contents = [];

		if ( tabConfig.id || tabConfig.dir )
		{
			if ( tabConfig.id )
			{
				contents.push(
					{
						id : 'advId',
						att : 'id',
						type : 'text',
						label : lang.id,
						setup : setupAdvParams,
						commit : commitAdvParams
					});
			}

			if ( tabConfig.dir )
			{
				contents.push(
					{
						id : 'advLangDir',
						att : 'dir',
						type : 'select',
						label : lang.langDir,
						'default' : '',
						style : 'width:100%',
						items :
						[
							[ lang.notSet, '' ],
							[ lang.langDirLTR, 'ltr' ],
							[ lang.langDirRTL, 'rtl' ]
						],
						setup : setupAdvParams,
						commit : commitAdvParams
					});
			}

			result.elements[ 0 ].children.push(
				{
					type : 'hbox',
					widths : [ '50%', '50%' ],
					children : [].concat( contents )
				});
		}

		if ( tabConfig.styles || tabConfig.classes )
		{
			contents = [];

			if ( tabConfig.styles )
			{
				contents.push(
					{
						id : 'advStyles',
						att : 'style',
						type : 'text',
						label : lang.styles,
						'default' : '',

						validate : CKEDITOR.dialog.validate.inlineStyle( lang.invalidInlineStyle ),
						onChange : function(){},

						getStyle : function( name, defaultValue )
						{
							var match = this.getValue().match( new RegExp( name + '\\s*:\\s*([^;]*)', 'i') );
							return match ? match[ 1 ] : defaultValue;
						},

						updateStyle : function( name, value )
						{
							var styles = this.getValue();

							// Remove the current value.
							if ( styles )
							{
								styles = styles
									.replace( new RegExp( '\\s*' + name + '\s*:[^;]*(?:$|;\s*)', 'i' ), '' )
									.replace( /^[;\s]+/, '' )
									.replace( /\s+$/, '' );
							}

							if ( value )
							{
								styles && !(/;\s*$/).test( styles ) && ( styles += '; ' );
								styles += name + ': ' + value;
							}

							this.setValue( styles, 1 );

						},

						setup : setupAdvParams,

						commit : commitAdvParams

					});
			}

			if ( tabConfig.classes )
			{
				contents.push(
					{
						type : 'hbox',
						widths : [ '45%', '55%' ],
						children :
						[
							{
								id : 'advCSSClasses',
								att : 'class',
								type : 'text',
								label : lang.cssClasses,
								'default' : '',
								setup : setupAdvParams,
								commit : commitAdvParams

							}
						]
					});
			}

			result.elements[ 0 ].children.push(
				{
					type : 'hbox',
					widths : [ '50%', '50%' ],
					children : [].concat( contents )
				});
		}

		return result;
	}
});

})();
