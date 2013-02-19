/*
 * Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://CKFINDER.com/license
 *
 * The software, this file and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying or distribute this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 */

CKFinder.addPlugin( 'fileeditor', function( api )
{
	var regexExt = /^(.*)\.([^\.]+)$/,
		regexTextExt = /^(txt|css|html|htm|js|asp|cfm|cfc|ascx|php|inc|xml|xslt|xsl|cs)$/i,
		regexCodeMirrorExt = /^(css|html|htm|js|xml|xsl|php|cs)$/i,
		codemirror,
		file,
		fileLoaded = false,
		doc;

	var codemirrorPath = CKFinder.getPluginPath( 'fileeditor' ) + 'codemirror/';
	var codeMirrorParsers = {
		css :	codemirrorPath + 'mode/css/css.js',
		js :	codemirrorPath + 'mode/javascript/javascript.js',
		html :	[ codemirrorPath + 'mode/xml/xml.js', codemirrorPath + 'mode/javascript/javascript.js', codemirrorPath + 'mode/css/css.js', codemirrorPath + 'mode/htmlmixed/htmlmixed.js' ],
		xml :	codemirrorPath + 'mode/xml/xml.js',
		php :	[ codemirrorPath + 'mode/xml/xml.js', codemirrorPath + 'mode/javascript/javascript.js', codemirrorPath + 'mode/css/css.js', codemirrorPath + 'mode/clike/clike.js', codemirrorPath + 'mode/php/php.js' ],
		c :	codemirrorPath + 'mode/clike/clike.js'
	};
	codeMirrorParsers.htm = codeMirrorParsers.html;
	codeMirrorParsers.xsl = codeMirrorParsers.xml;
	codeMirrorParsers.cs = codeMirrorParsers.c;
	codeMirrorParsers.cpp = codeMirrorParsers.c;
	var codeMirrorModes = {
		js : 'javascript',
		htm : 'htmlmixed',
		html : 'htmlmixed',
		xsl : 'xml',
		c : 'clike',
		cs : 'clike',
		cpp : 'clike'
	};

	CKFinder.dialog.add( 'fileEditor', function( api )
	{
		var height, width;
		var saveButton = (function()
				{
					return {
						id : 'save',
						label : api.lang.Fileeditor.save,
						type : 'button',
						onClick : function ( evt )
						{
							if ( !fileLoaded )
								return true;

							var dialog = evt.data.dialog;
							var content = codemirror ? codemirror.getValue() : doc.getById( 'fileContent' ).getValue();
							api.connector.sendCommandPost( 'SaveFile', null, {
									content : content,
									fileName : file.name
								},
								function( xml )
								{
									if ( xml.checkError() )
										return false;

									api.openMsgDialog( '', api.lang.Fileeditor.fileSaveSuccess );
									dialog.hide();
									return undefined;
								},
								file.folder.type,
								file.folder
							);
							return false;
						}
					};
				})();

		if ( api.inPopup )
		{
			width = api.document.documentElement.offsetWidth;
			height = api.document.documentElement.offsetHeight;
		}
		else
		{
			var parentWindow = ( api.document.parentWindow || api.document.defaultView ).parent;
			width = parentWindow.innerWidth ? parentWindow.innerWidth : parentWindow.document.documentElement.clientWidth;
			height = parentWindow.innerHeight ? parentWindow.innerHeight : parentWindow.document.documentElement.clientHeight;
		}

		var cssWidth = parseInt( width, 10 ) * 0.6 - 10,
			cssHeight = parseInt( height, 10 ) * 0.7 - 20;

		return {
			title : api.getSelectedFile().name,
			minWidth : parseInt( width, 10 ) * 0.6,
			minHeight : parseInt( height, 10 ) * 0.7,
			onHide : function()
			{
				if ( fileLoaded )
				{
					var fileContent = doc.getById( 'fileContent' );
					if ( fileContent )
						fileContent.remove();
				}
			},
			onShow : function()
			{
				var dialog = this;

				doc = dialog.getElement().getDocument();
				var win = doc.getWindow();
				doc.getById( 'fileArea' ).setHtml( '<div class="ckfinder_loader_32" style="margin: 100px auto 0 auto;text-align:center;"><p style="height:' + cssHeight + 'px;width:' + cssWidth + 'px;">' + api.lang.Fileeditor.loadingFile + '</p></div>' );

				file = api.getSelectedFile();
				var enableCodeMirror = regexCodeMirrorExt.test( file.ext );
				this.setTitle( file.name );

				if ( enableCodeMirror && win.$.CodeMirror === undefined )
					doc.appendStyleSheet( codemirrorPath + 'lib/codemirror.css' );

				// If CKFinder is runninng under a different domain than baseUrl, then the following call will fail:
				// CKFinder.ajax.load( file.getUrl() + '?t=' + (new Date().getTime()), function( data )...

				var url = api.connector.composeUrl( 'DownloadFile', { FileName : file.name, format : 'text', t : new Date().getTime() },
						file.folder.type, file.folder );

				CKFinder.ajax.load( url, function( data )
				{
					if ( data === null || ( file.size > 0 && data === '' ) )
					{
						api.openMsgDialog( '', api.lang.Fileeditor.fileOpenError );
						dialog.hide();
						return;
					}
					else
						fileLoaded = true;

					var fileArea = doc.getById( 'fileArea' );

					fileArea.setStyle( 'height', '100%' );
					fileArea.setHtml( '<textarea id="fileContent" style="height:' + cssHeight + 'px; width:' + cssWidth + 'px"></textarea>' );
					if ( CKFinder.env.opera )
						doc.getById( 'fileContent' ).setHtml( CKFinder.tools.htmlEncode( data ) );
					else
						doc.getById( 'fileContent' ).setText( data );

					codemirror = null;
					if ( enableCodeMirror )
					{
						CKFinder.scriptLoader.load( codemirrorPath + 'lib/codemirror.js', function()
						{
							CKFinder.scriptLoader.load( codeMirrorParsers[ file.ext ], function()
							{
								codemirror = win.$.CodeMirror.fromTextArea( doc.getById( 'fileContent' ).$, { mode : codeMirrorModes[ file.ext ] || file.ext } );

								// TODO get rid of ugly buttons and provide something better
								var undoB = doc.createElement( 'button', { attributes: { 'label' : api.lang.common.undo } } );
								undoB.on( 'click', function()
								{
									codemirror.undo();
								});
								undoB.setHtml( api.lang.common.undo );
								undoB.appendTo( doc.getById( 'fileArea' ) );

								var redoB = doc.createElement( 'button', { attributes: { 'label' : api.lang.common.redo } } );
								redoB.on( 'click', function()
								{
									codemirror.redo();
								});
								redoB.setHtml( api.lang.common.redo );
								redoB.appendTo( doc.getById( 'fileArea' ) );
							}, this, false, doc.getHead() );
						}, this, false, doc.getHead() );
					}
				});
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
							id : 'htmlLoader',
							html : '' +
							'<style type="text/css">' +
							'#fileArea .CodeMirror {background:white}' +
							'#fileArea .CodeMirror-scroll {height:'+cssHeight+'px; width:'+cssWidth+'px}' +
							'#fileArea .CodeMirror .cm-tab {white-space:pre;}' +
							// FF >= 12 has some scrolling issue
							( CKFinder.env.gecko && CKFinder.env.version >= 120000 ? '#fileArea .CodeMirror-scroll > div > div {position:absolute !important}' : '' ) +
							'</style>' +
							'<div id="fileArea"></div>'
						}
					]
				}
			],
			// TODO http://dev.fckeditor.net/ticket/4750
			buttons : [ saveButton, CKFinder.dialog.cancelButton ]
		};
	} );

	api.addFileContextMenuOption( { label : api.lang.Fileeditor.contextMenuName, command : 'fileEditor' } , function( api, file )
			{
				api.openDialog( 'fileEditor' );
			},
			function ( file )
			{
				var maxSize = 1024;

				if ( typeof ( CKFinder.config.fileeditorMaxSize ) != 'undefined' )
					maxSize = CKFinder.config.fileeditorMaxSize;

				// Disable for images, binary files, large files etc.
				if ( regexTextExt.test( file.ext ) && file.size <= maxSize )
					return file.folder.acl.fileDelete ? true : -1;

				return false;
			});
} );
