/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

(function()
{
	CKEDITOR.dialog.add( 'pastetext', function( editor )
		{
			return {
				title : editor.lang.pasteText.title,

				minWidth : CKEDITOR.env.ie && CKEDITOR.env.quirks ? 368 : 350,
				minHeight : 240,

				onShow : function(){ this.setupContent(); },
				onOk : function(){ this.commitContent(); },

				contents :
				[
					{
						label : editor.lang.common.generalTab,
						id : 'general',
						elements :
						[
							{
								type : 'html',
								id : 'pasteMsg',
								html : '<div style="white-space:normal;width:340px;">' + editor.lang.clipboard.pasteMsg + '</div>'
							},
							{
								type : 'textarea',
								id : 'content',
								className : 'cke_pastetext',

								onLoad : function()
								{
									var label = this.getDialog().getContentElement( 'general', 'pasteMsg' ).getElement(),
										input = this.getElement().getElementsByTag( 'textarea' ).getItem( 0 );

									input.setAttribute( 'aria-labelledby', label.$.id );
									input.setStyle( 'direction', editor.config.contentsLangDirection );
								},

								focus : function()
								{
									this.getElement().focus();
								},
								setup : function()
								{
									this.setValue( '' );
								},
								commit : function()
								{
									var value = this.getValue();
									setTimeout( function()
									{
										editor.fire( 'paste', { 'text' : value } );
									}, 0 );
								}
							}
						]
					}
				]
			};
		});
})();
