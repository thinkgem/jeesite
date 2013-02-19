/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.plugins.add( 'listblock',
{
	requires : [ 'panel' ],

	onLoad : function()
	{
		CKEDITOR.ui.panel.prototype.addListBlock = function( name, definition )
		{
			return this.addBlock( name, new CKEDITOR.ui.listBlock( this.getHolderElement(), definition ) );
		};

		CKEDITOR.ui.listBlock = CKEDITOR.tools.createClass(
			{
				base : CKEDITOR.ui.panel.block,

				$ : function( blockHolder, blockDefinition )
				{
					blockDefinition = blockDefinition || {};

					var attribs = blockDefinition.attributes || ( blockDefinition.attributes = {} );
					( this.multiSelect = !!blockDefinition.multiSelect ) &&
						( attribs[ 'aria-multiselectable' ] = true );
					// Provide default role of 'listbox'.
					!attribs.role && ( attribs.role = 'listbox' );

					// Call the base contructor.
					this.base.apply( this, arguments );

					var keys = this.keys;
					keys[ 40 ]	= 'next';					// ARROW-DOWN
					keys[ 9 ]	= 'next';					// TAB
					keys[ 38 ]	= 'prev';					// ARROW-UP
					keys[ CKEDITOR.SHIFT + 9 ]	= 'prev';	// SHIFT + TAB
					keys[ 32 ]	= CKEDITOR.env.ie ? 'mouseup' : 'click';					// SPACE
					CKEDITOR.env.ie && ( keys[ 13 ] = 'mouseup' );		// Manage ENTER, since onclick is blocked in IE (#8041).

					this._.pendingHtml = [];
					this._.items = {};
					this._.groups = {};
				},

				_ :
				{
					close : function()
					{
						if ( this._.started )
						{
							this._.pendingHtml.push( '</ul>' );
							delete this._.started;
						}
					},

					getClick : function()
					{
						if ( !this._.click )
						{
							this._.click = CKEDITOR.tools.addFunction( function( value )
								{
									var marked = true;

									if ( this.multiSelect )
										marked = this.toggle( value );
									else
										this.mark( value );

									if ( this.onClick )
										this.onClick( value, marked );
								},
								this );
						}
						return this._.click;
					}
				},

				proto :
				{
					add : function( value, html, title )
					{
						var pendingHtml = this._.pendingHtml,
							id = CKEDITOR.tools.getNextId();

						if ( !this._.started )
						{
							pendingHtml.push( '<ul role="presentation" class=cke_panel_list>' );
							this._.started = 1;
							this._.size = this._.size || 0;
						}

						this._.items[ value ] = id;

						pendingHtml.push(
							'<li id=', id, ' class=cke_panel_listItem role=presentation>' +
								'<a id="', id, '_option" _cke_focus=1 hidefocus=true' +
									' title="', title || value, '"' +
									' href="javascript:void(\'', value, '\')" ' +
									( CKEDITOR.env.ie ? 'onclick="return false;" onmouseup' : 'onclick' ) +		// #188
										'="CKEDITOR.tools.callFunction(', this._.getClick(), ',\'', value, '\'); return false;"',
									' role="option">',
									html || value,
								'</a>' +
							'</li>' );
					},

					startGroup : function( title )
					{
						this._.close();

						var id = CKEDITOR.tools.getNextId();

						this._.groups[ title ] = id;

						this._.pendingHtml.push( '<h1 role="presentation" id=', id, ' class=cke_panel_grouptitle>', title, '</h1>' );
					},

					commit : function()
					{
						this._.close();
						this.element.appendHtml( this._.pendingHtml.join( '' ) );
						delete this._.size;

						this._.pendingHtml = [];
					},

					toggle : function( value )
					{
						var isMarked = this.isMarked( value );

						if ( isMarked )
							this.unmark( value );
						else
							this.mark( value );

						return !isMarked;
					},

					hideGroup : function( groupTitle )
					{
						var group = this.element.getDocument().getById( this._.groups[ groupTitle ] ),
							list = group && group.getNext();

						if ( group )
						{
							group.setStyle( 'display', 'none' );

							if ( list && list.getName() == 'ul' )
								list.setStyle( 'display', 'none' );
						}
					},

					hideItem : function( value )
					{
						this.element.getDocument().getById( this._.items[ value ] ).setStyle( 'display', 'none' );
					},

					showAll : function()
					{
						var items = this._.items,
							groups = this._.groups,
							doc = this.element.getDocument();

						for ( var value in items )
						{
							doc.getById( items[ value ] ).setStyle( 'display', '' );
						}

						for ( var title in groups )
						{
							var group = doc.getById( groups[ title ] ),
								list = group.getNext();

							group.setStyle( 'display', '' );

							if ( list && list.getName() == 'ul' )
								list.setStyle( 'display', '' );
						}
					},

					mark : function( value )
					{
						if ( !this.multiSelect )
							this.unmarkAll();

						var itemId = this._.items[ value ],
							item = this.element.getDocument().getById( itemId );
						item.addClass( 'cke_selected' );

						this.element.getDocument().getById( itemId + '_option' ).setAttribute( 'aria-selected', true );
						this.onMark && this.onMark( item );
					},

					unmark : function( value )
					{
						var doc = this.element.getDocument(),
							itemId = this._.items[ value ],
							item = doc.getById( itemId );

						item.removeClass( 'cke_selected' );
						doc.getById( itemId + '_option' ).removeAttribute( 'aria-selected' );

						this.onUnmark && this.onUnmark( item );
					},

					unmarkAll : function()
					{
						var items = this._.items,
							doc = this.element.getDocument();

						for ( var value in items )
						{
							var itemId = items[ value ];

							doc.getById( itemId ).removeClass( 'cke_selected' );
							doc.getById( itemId + '_option' ).removeAttribute( 'aria-selected' );
						}

						this.onUnmark && this.onUnmark();
					},

					isMarked : function( value )
					{
						return this.element.getDocument().getById( this._.items[ value ] ).hasClass( 'cke_selected' );
					},

					focus : function( value )
					{
						this._.focusIndex = -1;

						if ( value )
						{
							var selected = this.element.getDocument().getById( this._.items[ value ] ).getFirst();

							var links = this.element.getElementsByTag( 'a' ),
								link,
								i = -1;

							while ( ( link = links.getItem( ++i ) ) )
							{
								if ( link.equals( selected ) )
								{
									this._.focusIndex = i;
									break;
								}
							}

							setTimeout( function()
								{
									selected.focus();
								},
								0 );
						}
					}
				}
			});
	}
});
