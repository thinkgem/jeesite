/**
 * Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 *
 * CKFinder 2.x - sample "dummy" plugin.
 *
 * To enable it, add the following line to config.js:
 *     config.extraPlugins = 'dummy';
 */

/**
 * See http://docs.cksource.com/ckfinder_2.x_api/symbols/CKFinder.html#.addPlugin
 */
CKFinder.addPlugin( 'dummy', {

	lang : [ 'en', 'pl' ],

	appReady : function( api ) {
		CKFinder.dialog.add( 'dummydialog', function( api )
			{
				// CKFinder.dialog.definition
				var dialogDefinition =
				{
					title : api.lang.dummy.title,
					minWidth : 390,
					minHeight : 230,
					onOk : function() {
						// "this" is now a CKFinder.dialog object.
						var value = this.getValueOf( 'tab1', 'textareaId' );
						if ( !value ) {
							api.openMsgDialog( '', api.lang.dummy.typeText );
							return false;
						}
						else {
							alert( "You have entered: " + value );
							return true;
						}
					},
					contents : [
						{
							id : 'tab1',
							label : '',
							title : '',
							expand : true,
							padding : 0,
							elements :
							[
								{
									type : 'html',
									html : '<h3>' +  api.lang.dummy.typeText + '</h3>'
								},
								{
									type : 'textarea',
									id : 'textareaId',
									rows : 10,
									cols : 40
								}
							]
						}
					],
					buttons : [ CKFinder.dialog.cancelButton, CKFinder.dialog.okButton ]
				};

				return dialogDefinition;
			} );

		api.addFileContextMenuOption( { label : api.lang.dummy.menuItem, command : "dummycommand" } , function( api, file )
		{
			api.openDialog('dummydialog');
		});
	}
});
