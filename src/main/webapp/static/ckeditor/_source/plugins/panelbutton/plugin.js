/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'panelbutton',
{
	requires : [ 'button' ],
	onLoad : function()
	{
		function clickFn( editor )
		{
			var _ = this._;

			if ( _.state == CKEDITOR.TRISTATE_DISABLED )
				return;

			this.createPanel( editor );

			if ( _.on )
			{
				_.panel.hide();
				return;
			}

			_.panel.showBlock( this._.id, this.document.getById( this._.id ), 4 );
		}

		CKEDITOR.ui.panelButton = CKEDITOR.tools.createClass(
		{
			base : CKEDITOR.ui.button,

			$ : function( definition )
			{
				// We don't want the panel definition in this object.
				var panelDefinition = definition.panel;
				delete definition.panel;

				this.base( definition );

				this.document = ( panelDefinition
									&& panelDefinition.parent
									&& panelDefinition.parent.getDocument() )
								|| CKEDITOR.document;

				panelDefinition.block =
				{
					attributes : panelDefinition.attributes
				};

				this.hasArrow = true;

				this.click = clickFn;

				this._ =
				{
					panelDefinition : panelDefinition
				};
			},

			statics :
			{
				handler :
				{
					create : function( definition )
					{
						return new CKEDITOR.ui.panelButton( definition );
					}
				}
			},

			proto :
			{
				createPanel : function( editor )
				{
					var _ = this._;

					if ( _.panel )
						return;

					var panelDefinition = this._.panelDefinition || {},
						panelBlockDefinition = this._.panelDefinition.block,
						panelParentElement = panelDefinition.parent || CKEDITOR.document.getBody(),
						panel = this._.panel = new CKEDITOR.ui.floatPanel( editor, panelParentElement, panelDefinition ),
						block = panel.addBlock( _.id, panelBlockDefinition ),
						me = this;

					panel.onShow = function()
						{
							if ( me.className )
								this.element.getFirst().addClass( me.className + '_panel' );

							me.setState( CKEDITOR.TRISTATE_ON );

							_.on = 1;

							if ( me.onOpen )
								me.onOpen();
						};

					panel.onHide = function( preventOnClose )
						{
							if ( me.className )
								this.element.getFirst().removeClass( me.className + '_panel' );

							me.setState( me.modes && me.modes[ editor.mode ] ? CKEDITOR.TRISTATE_OFF : CKEDITOR.TRISTATE_DISABLED );

							_.on = 0;

							if ( !preventOnClose && me.onClose )
								me.onClose();
						};

					panel.onEscape = function()
						{
							panel.hide();
							me.document.getById( _.id ).focus();
						};

					if ( this.onBlock )
						this.onBlock( panel, block );

					block.onHide = function()
						{
							_.on = 0;
							me.setState( CKEDITOR.TRISTATE_OFF );
						};
				}
			}
		});

	},
	beforeInit : function( editor )
	{
		editor.ui.addHandler( CKEDITOR.UI_PANELBUTTON, CKEDITOR.ui.panelButton.handler );
	}
});

/**
 * Button UI element.
 * @constant
 * @example
 */
CKEDITOR.UI_PANELBUTTON = 'panelbutton';
