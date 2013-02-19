/*
 * Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://CKFinder.com/license
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */

CKFinder.addPlugin( 'imageresize', {
	readOnly : false,
	connectorInitialized : function( api, xml )
	{
		var node = xml.selectSingleNode( 'Connector/PluginsInfo/imageresize/@smallThumb' );
		if ( node )
			CKFinder.config.imageresize_thumbSmall = node.value;

		node = xml.selectSingleNode( 'Connector/PluginsInfo/imageresize/@mediumThumb' );
		if ( node )
			CKFinder.config.imageresize_thumbMedium = node.value;

		node = xml.selectSingleNode( 'Connector/PluginsInfo/imageresize/@largeThumb' );
		if ( node )
			CKFinder.config.imageresize_thumbLarge = node.value;
	},
	uiReady : function( api )
	{
		var regexExt = /^(.*)\.([^\.]+)$/,
			regexFileName = /^(.*?)(?:_\d+x\d+)?\.([^\.]+)$/,
			regexGetSize = /^\s*(\d+)(px)?\s*$/i,
			regexGetSizeOrEmpty = /(^\s*(\d+)(px)?\s*$)|^$/i,
			imageDimension = { width : 0, height : 0 },
			file,
			doc;

		var updateFileName = function( dialog )
		{
			var width = dialog.getValueOf( 'tab1', 'width' ) || 0,
				height = dialog.getValueOf( 'tab1', 'height' ) || 0,
				e = dialog.getContentElement( 'tab1', 'createNewBox' );

			if ( width && height )
			{
				var matches = file.name.match( regexFileName );
				dialog.setValueOf( 'tab1', 'fileName', matches[1] + '_' + width + 'x' + height );
				e.getElement().show();
			}
			else
				e.getElement().hide();
		};

		var onSizeChange = function()
		{
			var value = this.getValue(),	// This = input element.
				dialog = this.getDialog(),
				maxWidth = api.config.imagesMaxWidth,
				maxHeight = api.config.imagesMaxHeight,
				aMatch  =  value.match( regexGetSize ),
				width = imageDimension.width,
				height = imageDimension.height,
				newHeight,
				newWidth;

			if ( aMatch )
				value = aMatch[1];

			if ( !api.config.imageresize_allowEnlarging )
			{
				if ( width && width < maxWidth )
					maxWidth = width;

				if ( height && height < maxHeight )
					maxHeight = height;
			}

			if ( maxHeight > 0 && this.id == 'height' && value > maxHeight )
			{
				value = maxHeight;
				dialog.setValueOf( 'tab1', 'height', value );
			}

			if ( maxWidth > 0 && this.id == 'width' && value > maxWidth )
			{
				value = maxWidth;
				dialog.setValueOf( 'tab1', 'width', value );
			}

			// Only if ratio is locked
			if ( dialog.lockRatio && width && height )
			{
				if ( this.id == 'height' )
				{
					if ( value && value != '0' )
						value = Math.round( width * ( value  / height ) );

					if ( !isNaN( value ) )
					{
						// newWidth > maxWidth
						if ( maxWidth > 0 && value > maxWidth )
						{
							value = maxWidth;
							newHeight = Math.round( height * ( value  / width ) );
							dialog.setValueOf( 'tab1', 'height', newHeight );
						}

						dialog.setValueOf( 'tab1', 'width', value );
					}
				}
				else	//this.id = txtWidth.
				{
					if ( value && value != '0' )
						value = Math.round( height * ( value  / width ) );

					if ( !isNaN( value ) )
					{
						// newHeight > maxHeight
						if ( maxHeight > 0 && value > maxHeight )
						{
							value = maxHeight;
							newWidth = Math.round( width * ( value  / height ) );
							dialog.setValueOf( 'tab1', 'width', newWidth );
						}

						dialog.setValueOf( 'tab1', 'height', value );
					}
				}
			}

			updateFileName( dialog );
		};

		var resetSize = function( dialog )
		{
			if ( imageDimension.width && imageDimension.height )
			{
				dialog.setValueOf( 'tab1', 'width', imageDimension.width );
				dialog.setValueOf( 'tab1', 'height', imageDimension.height );
				updateFileName( dialog );
			}
		};

		var switchLockRatio = function( dialog, value )
		{
			var doc = dialog.getElement().getDocument(),
				ratioButton = doc.getById( 'btnLockSizes' );

			if ( imageDimension.width && imageDimension.height )
			{
				if ( value == 'check' )	// Check image ratio and original image ratio.
				{
					var width = dialog.getValueOf( 'tab1', 'width' ),
						height = dialog.getValueOf( 'tab1', 'height' ),
						originalRatio = imageDimension.width * 1000 / imageDimension.height,
						thisRatio = width * 1000 / height;
					dialog.lockRatio  = false;	// Default: unlock ratio

					if ( !width && !height )
						dialog.lockRatio = true; // If someone didn't start typing, lock ratio.
					else if ( !isNaN( originalRatio ) && !isNaN( thisRatio ) )
					{
						if ( Math.round( originalRatio ) == Math.round( thisRatio ) )
							dialog.lockRatio = true;
					}
				}
				else if ( value != undefined )
					dialog.lockRatio = value;
				else
					dialog.lockRatio = !dialog.lockRatio;
			}
			else if ( value != 'check' )	// I can't lock ratio if ratio is unknown.
				dialog.lockRatio = false;

			if ( dialog.lockRatio )
				ratioButton.removeClass( 'ckf_btn_unlocked' );
			else
				ratioButton.addClass( 'ckf_btn_unlocked' );

			return dialog.lockRatio;
		};

		CKFinder.dialog.add( 'resizeDialog', function( api )
		{
			return {
				title : api.lang.Imageresize.dialogTitle.replace( '%s', api.getSelectedFile().name ),
				// TODO resizable : CKFinder.DIALOG_RESIZE_BOTH
				minWidth : CKFinder.env.webkit ? 290 : 390,
				minHeight : 230,
				onShow : function()
				{
					var dialog = this,
						thumbSmall = CKFinder.config.imageresize_thumbSmall,
						thumbMedium = CKFinder.config.imageresize_thumbMedium,
						thumbLarge = CKFinder.config.imageresize_thumbLarge;

					doc = dialog.getElement().getDocument();
					file = api.getSelectedFile();

					this.setTitle( api.lang.Imageresize.dialogTitle.replace( '%s', file.name ) );

					var previewImg = doc.getById( 'previewImage' );
					var sizeSpan = doc.getById( 'imageSize' );

					// Thumbnails should be limited to a reasonable value (#1020).
					previewImg.setAttribute( 'src', file.getThumbnailUrl( true ) );
					previewImg.on( 'load', function()
					{
						previewImg.removeStyle( 'width' );
						previewImg.removeStyle( 'height' );
						var width = previewImg.$.width,
							height = previewImg.$.height;
						previewImg.hide();
						if ( CKFinder.env.ie6Compat )
						{
							if ( width > height )
								previewImg.setStyles( { width : 100 + 'px', height : Math.round( height / ( width / 100 ) ) + 'px' } );
							else
								previewImg.setStyles( { height : 100 + 'px', width : Math.round( width / ( height / 100 ) ) + 'px' } );
						}
						else
						{
							previewImg.removeStyle( 'max-width' );
							previewImg.removeStyle( 'max-height' );
							if ( width > height )
								previewImg.setStyle( 'max-width', '100px' );
							else
								previewImg.setStyle( 'max-height', '100px' );
						}
						previewImg.show();
					});

					var updateImgDimension = function( width, height )
					{
						if ( !width || !height )
						{
							sizeSpan.setText( '' );
							return;
						}

						imageDimension.width = width;
						imageDimension.height = height;
						sizeSpan.setText( width + ' x ' + height + ' px' );
						CKFinder.tools.setTimeout( function(){ switchLockRatio( dialog, 'check' ); }, 0, dialog );
					};

					api.connector.sendCommand( 'ImageResizeInfo', {
							fileName : file.name
						},
						function( xml )
						{
							if ( xml.checkError() )
								return;
							var width = xml.selectSingleNode( 'Connector/ImageInfo/@width' ),
								height = xml.selectSingleNode( 'Connector/ImageInfo/@height' ),
								result;

							if ( width && height )
							{
								width = parseInt( width.value, 10 );
								height = parseInt( height.value, 10 );
								updateImgDimension( width, height );

								var checkThumbs = function( id, size )
								{
									if ( !size )
										return;

									var reThumb = /^(\d+)x(\d+)$/;
										result = reThumb.exec( size );

									var el = dialog.getContentElement( 'tab1', id );
									if ( 0 + result[ 1 ] > width && 0 + result[ 2 ] > height )
									{
										el.disable();
										el.getElement().setAttribute( 'title', api.lang.Imageresize.imageSmall ).addClass( 'cke_disabled' );
									}
									else
									{
										el.enable();
										el.getElement().setAttribute( 'title', '' ).removeClass( 'cke_disabled' );
									}
								};

								checkThumbs( 'smallThumb', thumbSmall );
								checkThumbs( 'mediumThumb', thumbMedium );
								checkThumbs( 'largeThumb', thumbLarge );
							}
						},
						file.folder.type,
						file.folder
					);

					if ( !thumbSmall )
						dialog.getContentElement( 'tab1', 'smallThumb' ).getElement().hide();

					if ( !thumbMedium )
						dialog.getContentElement( 'tab1', 'mediumThumb' ).getElement().hide();

					if ( !thumbLarge )
						dialog.getContentElement( 'tab1', 'largeThumb' ).getElement().hide();

					if ( !thumbSmall && !thumbMedium && !thumbLarge )
						dialog.getContentElement( 'tab1', 'thumbsLabel' ).getElement().hide();

					dialog.setValueOf( 'tab1', 'fileName', file.name );
					dialog.getContentElement( 'tab1', 'fileNameExt' ).getElement().setHtml( '.' + file.ext );
					dialog.getContentElement( 'tab1', 'width' ).focus();
					dialog.getContentElement( 'tab1', 'fileName').setValue( '' );
					dialog.getContentElement( 'tab1', 'createNewBox' ).getElement().hide();
					updateImgDimension( 0,0 );
				},
				onOk : function()
				{
					var dialog = this,
						width = dialog.getValueOf( 'tab1', 'width' ),
						height = dialog.getValueOf( 'tab1', 'height' ),
						small = dialog.getValueOf( 'tab1', 'smallThumb' ),
						medium = dialog.getValueOf( 'tab1', 'mediumThumb' ),
						large = dialog.getValueOf( 'tab1', 'largeThumb' ),
						fileName = dialog.getValueOf( 'tab1', 'fileName' ),
						createNew = dialog.getValueOf( 'tab1', 'createNew' );

					if ( width && !height )
					{
						api.openMsgDialog( '', api.lang.Imageresize.invalidHeight );
						return false;
					}
					else if ( !width && height )
					{
						api.openMsgDialog( '', api.lang.Imageresize.invalidWidth );
						return false;
					}
					if ( !api.config.imageresize_allowEnlarging && ( parseInt( width, 10 ) > imageDimension.width || parseInt( height, 10 ) > imageDimension.height ) )
					{
						var str = api.lang.Imageresize.sizeTooBig;
						api.openMsgDialog( '', str.replace( '%size', imageDimension.width + 'x' + imageDimension.height ) );
						return false;
					}

					if ( ( width && height ) || small || medium || large )
					{
						if ( !createNew )
							fileName = file.name;

						api.connector.sendCommandPost( 'ImageResize', null, {
								width : width,
								height : height,
								fileName : file.name,
								newFileName : fileName + '.' + file.ext,
								overwrite : createNew ? 0 : 1,
								small : small ? 1 : 0,
								medium : medium ? 1 : 0,
								large : large ? 1 : 0
							},
							function( xml )
							{
								if ( xml.checkError() )
									return;
								api.openMsgDialog( '', api.lang.Imageresize.resizeSuccess );
								api.refreshOpenedFolder();
							},
							file.folder.type,
							file.folder
						);
					}
					return undefined;
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
								type : 'hbox',
								// The dialog window looks weird on Webkit (#1021)
								widths : [ ( CKFinder.env.webkit ? 130 : 180 ) + 'px', ( CKFinder.env.webkit ? 250 : 280 ) + 'px' ],
								children:
								[
									{
										type : 'vbox',
										children:
										[
											{
												type : 'html',
												html : '' +
												'<style type="text/css">' +
												'a.ckf_btn_reset' +
												'{' +
													'float: right;' +
													'background-position: 0 -32px;' +
													'background-image: url("' + CKFinder.getPluginPath( 'imageresize' ) + 'images/mini.gif");' +
													'width: 16px;' +
													'height: 16px;' +
													'background-repeat: no-repeat;' +
													'border: 1px none;' +
													'font-size: 1px;' +
												'}' +

												'a.ckf_btn_locked,' +
												'a.ckf_btn_unlocked' +
												'{' +
													'float: left;' +
													'background-position: 0 0;' +
													'background-image: url("' + CKFinder.getPluginPath( 'imageresize' ) + 'images/mini.gif");' +
													'width: 16px;' +
													'height: 16px;' +
													'background-repeat: no-repeat;' +
													'border: none 1px;' +
													'font-size: 1px;' +
												'}' +

												'a.ckf_btn_unlocked' +
												'{' +
													'background-position: 0 -16px;' +
													'background-image: url("' + CKFinder.getPluginPath( 'imageresize' ) + '/images/mini.gif");' +
												'}' +

												'.ckf_btn_over' +
												'{' +
													'border: outset 1px;' +
													'cursor: pointer;' +
													'cursor: hand;' +
												'}' +
												'</style>' +

												'<div style="height:110px;padding:7px">' +
												'<img id="previewImage" src="" style="margin-bottom:4px; max-width: 100px; max-height: 100px;" /><br />' +
												'<span style="font-size:9px;" id="imageSize"></span>' +
												'</div>'
											},
											{
												type : 'html',
												id : 'thumbsLabel',
												html : '<strong>' + api.lang.Imageresize.thumbnailNew + '</strong>'
											},
											{
												type : 'checkbox',
												id : 'smallThumb',
												checked : false,
												label : api.lang.Imageresize.thumbnailSmall.replace( '%s', CKFinder.config.imageresize_thumbSmall )
											},
											{
												type : 'checkbox',
												id : 'mediumThumb',
												checked : false,
												label : api.lang.Imageresize.thumbnailMedium.replace( '%s', CKFinder.config.imageresize_thumbMedium )
											},
											{
												type : 'checkbox',
												id : 'largeThumb',
												checked : false,
												label : api.lang.Imageresize.thumbnailLarge.replace( '%s', CKFinder.config.imageresize_thumbLarge )
											}
										]
									},
									{
										type : 'vbox',
										children :
										[
											{
												type : 'html',
												html : '<strong>' + api.lang.Imageresize.newSize + '</strong>'
											},
											{
												type : 'hbox',
												widths : [ '80%', '20%' ],
												children:
												[
													{
														type : 'vbox',
														children:
														[
															{
																type : 'text',
																labelLayout : 'horizontal',
																label : api.lang.Imageresize.width,
																onKeyUp : onSizeChange,
																validate: function()
																{
																	var value = this.getValue();
																	if ( value )
																	{
																		var aMatch  =  value.match( regexGetSize );
																		if ( !aMatch || parseInt( aMatch[1], 10 ) < 1 )
																		{
																			api.openMsgDialog( '', api.lang.Imageresize.invalidWidth );
																			return false;
																		}
																	}
																	return true;
																},
																id : 'width'
															},
															{
																type : 'text',
																labelLayout : 'horizontal',
																label : api.lang.Imageresize.height,
																onKeyUp : onSizeChange,
																validate: function()
																{
																	var value = this.getValue();
																	if ( value )
																	{
																		var aMatch  =  value.match( regexGetSize );
																		if ( !aMatch || parseInt( aMatch[1], 10 ) < 1 )
																		{
																			api.openMsgDialog( '', api.lang.Imageresize.invalidHeight );
																			return false;
																		}
																	}
																	return true;
																},
																id : 'height'
															}
														]
													},
													{
														type : 'html',
														onLoad : function()
														{
															var doc = this.getElement().getDocument(),
																dialog = this.getDialog();
															// Activate Reset button
															var	resetButton = doc.getById( 'btnResetSize' ),
																ratioButton = doc.getById( 'btnLockSizes' );
															if ( resetButton )
															{
																resetButton.on( 'click', function( evt )
																	{
																		resetSize( this );
																		evt.data.preventDefault();
																	}, dialog );
																resetButton.on( 'mouseover', function()
																	{
																		this.addClass( 'ckf_btn_over' );
																	}, resetButton );
																resetButton.on( 'mouseout', function()
																	{
																		this.removeClass( 'ckf_btn_over' );
																	}, resetButton );
															}
															// Activate (Un)LockRatio button
															if ( ratioButton )
															{
																ratioButton.on( 'click', function( evt )
																	{
																		var locked = switchLockRatio( this ),
																			width = this.getValueOf( 'tab1', 'width' );

																		if ( imageDimension.width && width )
																		{
																			var height = imageDimension.height / imageDimension.width * width;
																			if ( !isNaN( height ) )
																			{
																				this.setValueOf( 'tab1', 'height', Math.round( height ) );
																				updateFileName( dialog );
																			}
																		}
																		evt.data.preventDefault();
																	}, dialog );
																ratioButton.on( 'mouseover', function()
																	{
																		this.addClass( 'ckf_btn_over' );
																	}, ratioButton );
																ratioButton.on( 'mouseout', function()
																	{
																		this.removeClass( 'ckf_btn_over' );
																	}, ratioButton );
															}
														},
														html : '<div style="margin-top:4px">'+
															'<a href="javascript:void(0)" tabindex="-1" title="' + api.lang.Imageresize.lockRatio + '" class="ckf_btn_locked ckf_btn_unlocked" id="btnLockSizes"></a>' +
															'<a href="javascript:void(0)" tabindex="-1" title="' + api.lang.Imageresize.resetSize + '" class="ckf_btn_reset" id="btnResetSize"></a>'+
															'</div>'
													}
												]
											},
											{
												type : 'vbox',
												id : 'createNewBox',
												hidden : true,
												children:
												[
													{
														type : 'checkbox',
														checked : true,
														id : 'createNew',
														label : api.lang.Imageresize.newImage,
														'default' : true,
														onChange : function()
														{
															var dialog = this.getDialog();
															var filenameInput = dialog.getContentElement( 'tab1', 'fileName' );
															if ( filenameInput )
															{
																if ( !this.getValue() )
																	filenameInput.getElement().hide();
																else
																	filenameInput.getElement().show();
															}
														}
													},
													{
														type : 'hbox',
														widths : [ '90%', '10%' ],
														padding : 0,
														children :
														[
															{
																type : 'text',
																label : '',
																validate : function()
																{
																	var dialog = this.getDialog(),
																		createNew = dialog.getContentElement( 'tab1', 'createNew' ),
																		value = this.getValue();

																	if ( createNew && dialog.getValueOf( 'tab1', 'width' ) && dialog.getValueOf( 'tab1', 'height' ) )
																	{
																		if ( !value )
																		{
																			api.openMsgDialog( '', api.lang.Imageresize.invalidName );
																			return false;
																		}
																	}
																	return true;
																},
																id : 'fileName'
															},
															{
																type : 'html',
																html : '',
																id : 'fileNameExt',
																onLoad : function()
																{
																	this.getElement().getParent().setStyles( { 'vertical-align' : 'bottom', 'padding-bottom' : '2px' } );
																}
															}
														]
													}
												]
											}
										]
									}
								]
							}
						]
					}
				],
				// TODO http://dev.fckeditor.net/ticket/4750
				buttons : [ CKFinder.dialog.okButton, CKFinder.dialog.cancelButton ]
			};
		} );

		api.addFileContextMenuOption( { label : api.lang.Imageresize.contextMenuName, command : 'resizeImage' } , function( api, file )
			{
				api.openDialog( 'resizeDialog' );
			},
			function ( file )
			{
				// Disable for files other than images.
				if ( !file.isImage() || !api.getSelectedFolder().type )
					return false;
				if ( file.folder.acl.fileDelete && file.folder.acl.fileUpload )
					return true;
				else
					return -1;
			}
		);
	}
} );
