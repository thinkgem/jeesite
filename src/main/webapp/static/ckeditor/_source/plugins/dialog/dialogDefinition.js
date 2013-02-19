/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the "virtual" dialog, dialog content and dialog button
 * definition classes.
 */

/**
 * The definition of a dialog window.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create dialogs.
 * </div>
 * @name CKEDITOR.dialog.definition
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * CKEDITOR.dialog.add( 'testOnly', function( editor )
 *       {
 *           return {
 *               title : 'Test Dialog',
 *               resizable : CKEDITOR.DIALOG_RESIZE_BOTH,
 *               minWidth : 500,
 *               minHeight : 400,
 *               contents : [
 *                   {
 *                       id : 'tab1',
 *                       label : 'First Tab',
 *                       title : 'First Tab Title',
 *                       accessKey : 'Q',
 *                       elements : [
 *                           {
 *                               type : 'text',
 *                               label : 'Test Text 1',
 *                               id : 'testText1',
 *                               'default' : 'hello world!'
 *                           }
 *                       ]
 *                    }
 *               ]
 *           };
 *       });
 */

/**
 * The dialog title, displayed in the dialog's header. Required.
 * @name CKEDITOR.dialog.definition.prototype.title
 * @field
 * @type String
 * @example
 */

/**
 * How the dialog can be resized, must be one of the four contents defined below.
 * <br /><br />
 * <strong>CKEDITOR.DIALOG_RESIZE_NONE</strong><br />
 * <strong>CKEDITOR.DIALOG_RESIZE_WIDTH</strong><br />
 * <strong>CKEDITOR.DIALOG_RESIZE_HEIGHT</strong><br />
 * <strong>CKEDITOR.DIALOG_RESIZE_BOTH</strong><br />
 * @name CKEDITOR.dialog.definition.prototype.resizable
 * @field
 * @type Number
 * @default CKEDITOR.DIALOG_RESIZE_NONE
 * @example
 */

/**
 * The minimum width of the dialog, in pixels.
 * @name CKEDITOR.dialog.definition.prototype.minWidth
 * @field
 * @type Number
 * @default 600
 * @example
 */

/**
 * The minimum height of the dialog, in pixels.
 * @name CKEDITOR.dialog.definition.prototype.minHeight
 * @field
 * @type Number
 * @default 400
 * @example
 */


/**
 * The initial width of the dialog, in pixels.
 * @name CKEDITOR.dialog.definition.prototype.width
 * @field
 * @type Number
 * @default @CKEDITOR.dialog.definition.prototype.minWidth
 * @since 3.5.3
 * @example
 */

/**
 * The initial height of the dialog, in pixels.
 * @name CKEDITOR.dialog.definition.prototype.height
 * @field
 * @type Number
 * @default @CKEDITOR.dialog.definition.prototype.minHeight
 * @since 3.5.3
 * @example
 */

/**
 * The buttons in the dialog, defined as an array of
 * {@link CKEDITOR.dialog.definition.button} objects.
 * @name CKEDITOR.dialog.definition.prototype.buttons
 * @field
 * @type Array
 * @default [ CKEDITOR.dialog.okButton, CKEDITOR.dialog.cancelButton ]
 * @example
 */

/**
 * The contents in the dialog, defined as an array of
 * {@link CKEDITOR.dialog.definition.content} objects. Required.
 * @name CKEDITOR.dialog.definition.prototype.contents
 * @field
 * @type Array
 * @example
 */

/**
 * The function to execute when OK is pressed.
 * @name CKEDITOR.dialog.definition.prototype.onOk
 * @field
 * @type Function
 * @example
 */

/**
 * The function to execute when Cancel is pressed.
 * @name CKEDITOR.dialog.definition.prototype.onCancel
 * @field
 * @type Function
 * @example
 */

/**
 * The function to execute when the dialog is displayed for the first time.
 * @name CKEDITOR.dialog.definition.prototype.onLoad
 * @field
 * @type Function
 * @example
 */

/**
 * The function to execute when the dialog is loaded (executed every time the dialog is opened).
 * @name CKEDITOR.dialog.definition.prototype.onShow
 * @field
 * @type Function
 * @example
 */

/**
 * <div class="notapi">This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create dialog content pages.</div>
 * @name CKEDITOR.dialog.definition.content
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 */

/**
 * The id of the content page.
 * @name CKEDITOR.dialog.definition.content.prototype.id
 * @field
 * @type String
 * @example
 */

/**
 * The tab label of the content page.
 * @name CKEDITOR.dialog.definition.content.prototype.label
 * @field
 * @type String
 * @example
 */

/**
 * The popup message of the tab label.
 * @name CKEDITOR.dialog.definition.content.prototype.title
 * @field
 * @type String
 * @example
 */

/**
 * The CTRL hotkey for switching to the tab.
 * @name CKEDITOR.dialog.definition.content.prototype.accessKey
 * @field
 * @type String
 * @example
 * contentDefinition.accessKey = 'Q';	// Switch to this page when CTRL-Q is pressed.
 */

/**
 * The UI elements contained in this content page, defined as an array of
 * {@link CKEDITOR.dialog.definition.uiElement} objects.
 * @name CKEDITOR.dialog.definition.content.prototype.elements
 * @field
 * @type Array
 * @example
 */

/**
 * The definition of user interface element (textarea, radio etc).
 * <div class="notapi">This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create dialog UI elements.</div>
 * @name CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @see CKEDITOR.ui.dialog.uiElement
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 */

/**
 * The id of the UI element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.id
 * @field
 * @type String
 * @example
 */

/**
 * The type of the UI element. Required.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.type
 * @field
 * @type String
 * @example
 */

/**
 * The popup label of the UI element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.title
 * @field
 * @type String
 * @example
 */

/**
 * CSS class names to append to the UI element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.className
 * @field
 * @type String
 * @example
 */

/**
 * Inline CSS classes to append to the UI element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.style
 * @field
 * @type String
 * @example
 */

/**
 * Horizontal alignment (in container) of the UI element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.align
 * @field
 * @type String
 * @example
 */

/**
 * Function to execute the first time the UI element is displayed.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.onLoad
 * @field
 * @type Function
 * @example
 */

/**
 * Function to execute whenever the UI element's parent dialog is displayed.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.onShow
 * @field
 * @type Function
 * @example
 */

/**
 * Function to execute whenever the UI element's parent dialog is closed.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.onHide
 * @field
 * @type Function
 * @example
 */

/**
 * Function to execute whenever the UI element's parent dialog's {@link CKEDITOR.dialog.definition.setupContent} method is executed.
 * It usually takes care of the respective UI element as a standalone element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.setup
 * @field
 * @type Function
 * @example
 */

/**
 * Function to execute whenever the UI element's parent dialog's {@link CKEDITOR.dialog.definition.commitContent} method is executed.
 * It usually takes care of the respective UI element as a standalone element.
 * @name CKEDITOR.dialog.definition.uiElement.prototype.commit
 * @field
 * @type Function
 * @example
 */

// ----- hbox -----

/**
 * Horizontal layout box for dialog UI elements, auto-expends to available width of container.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create horizontal layouts.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.hbox} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * @name CKEDITOR.dialog.definition.hbox
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'hbox',</b>
 * 	widths : [ '25%', '25%', '50%' ],
 * 	children :
 * 	[
 * 		{
 * 			type : 'text',
 * 			id : 'id1',
 * 			width : '40px',
 * 		},
 * 		{
 * 			type : 'text',
 * 			id : 'id2',
 * 			width : '40px',
 * 		},
 * 		{
 * 			type : 'text',
 * 			id : 'id3'
 * 		}
 * 	]
 * }
 */

/**
 * Array of {@link CKEDITOR.ui.dialog.uiElement} objects inside this container.
 * @name CKEDITOR.dialog.definition.hbox.prototype.children
 * @field
 * @type Array
 * @example
 */

/**
 * (Optional) The widths of child cells.
 * @name CKEDITOR.dialog.definition.hbox.prototype.widths
 * @field
 * @type Array
 * @example
 */

/**
 * (Optional) The height of the layout.
 * @name CKEDITOR.dialog.definition.hbox.prototype.height
 * @field
 * @type Number
 * @example
 */

/**
 * The CSS styles to apply to this element.
 * @name CKEDITOR.dialog.definition.hbox.prototype.styles
 * @field
 * @type String
 * @example
 */

/**
 * (Optional) The padding width inside child cells. Example: 0, 1.
 * @name CKEDITOR.dialog.definition.hbox.prototype.padding
 * @field
 * @type Number
 * @example
 */

/**
 * (Optional) The alignment of the whole layout. Example: center, top.
 * @name CKEDITOR.dialog.definition.hbox.prototype.align
 * @field
 * @type String
 * @example
 */

// ----- vbox -----

/**
 * Vertical layout box for dialog UI elements.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create vertical layouts.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.vbox} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * <style type="text/css">.details .detailList {display:none;} </style>
 * @name CKEDITOR.dialog.definition.vbox
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'vbox',</b>
 * 	align : 'right',
 * 	width : '200px',
 * 	children :
 * 	[
 * 		{
 * 			type : 'text',
 * 			id : 'age',
 * 			label : 'Age'
 * 		},
 * 		{
 * 			type : 'text',
 * 			id : 'sex',
 * 			label : 'Sex'
 * 		},
 * 		{
 * 			type : 'text',
 * 			id : 'nationality',
 * 			label : 'Nationality'
 * 		}
 * 	]
 * }
 */

/**
 * Array of {@link CKEDITOR.ui.dialog.uiElement} objects inside this container.
 * @name CKEDITOR.dialog.definition.vbox.prototype.children
 * @field
 * @type Array
 * @example
 */

/**
 * (Optional) The width of the layout.
 * @name CKEDITOR.dialog.definition.vbox.prototype.width
 * @field
 * @type Array
 * @example
 */

/**
 * (Optional) The heights of individual cells.
 * @name CKEDITOR.dialog.definition.vbox.prototype.heights
 * @field
 * @type Number
 * @example
 */

/**
 * The CSS styles to apply to this element.
 * @name CKEDITOR.dialog.definition.vbox.prototype.styles
 * @field
 * @type String
 * @example
 */

/**
 * (Optional) The padding width inside child cells. Example: 0, 1.
 * @name CKEDITOR.dialog.definition.vbox.prototype.padding
 * @field
 * @type Number
 * @example
 */

/**
 * (Optional) The alignment of the whole layout. Example: center, top.
 * @name CKEDITOR.dialog.definition.vbox.prototype.align
 * @field
 * @type String
 * @example
 */

/**
 * (Optional) Whether the layout should expand vertically to fill its container.
 * @name CKEDITOR.dialog.definition.vbox.prototype.expand
 * @field
 * @type Boolean
 * @example
 */

// ----- labeled element ------

/**
 * The definition of labeled user interface element (textarea, textInput etc).
 * <div class="notapi">This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create dialog UI elements.</div>
 * @name CKEDITOR.dialog.definition.labeledElement
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @see CKEDITOR.ui.dialog.labeledElement
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 */

/**
 * The label of the UI element.
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.label
 * @type String
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label '
 * }
 */

/**
 * (Optional) Specify the layout of the label. Set to 'horizontal' for horizontal layout.
 * The default layout is vertical.
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.labelLayout
 * @type String
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label ',
 * 	<strong>	labelLayout : 'horizontal',</strong>
 * }
 */

/**
 * (Optional) Applies only to horizontal layouts: a two elements array of lengths to specify the widths of the
* 	label and the content element. See also {@link CKEDITOR.dialog.definition.labeledElement#labelLayout}.
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.widths
 * @type Array
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label ',
 * 		labelLayout : 'horizontal',
 * 	<strong>	widths : [100, 200],</strong>
 * }
 */

/**
 *  Specify the inline style of the uiElement label.
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.labelStyle
 * @type String
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label ',
 * 	<strong>	labelStyle : 'color: red',</strong>
 * }
 */


/**
 *  Specify the inline style of the input element.
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.inputStyle
 * @type String
 * @since 3.6.1
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label ',
 * 	<strong>	inputStyle : 'text-align:center',</strong>
 * }
 */

/**
 *  Specify the inline style of the input element container .
 * @name CKEDITOR.dialog.definition.labeledElement.prototype.controlStyle
 * @type String
 * @since 3.6.1
 * @field
 * @example
 * {
 * 		type : 'text',
 * 		label : 'My Label ',
 * 	<strong>	controlStyle : 'width:3em',</strong>
 * }
 */


// ----- button ------

/**
 * The definition of a button.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create buttons.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.button} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.button
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'button',</b>
 * 	id : 'buttonId',
 * 	label : 'Click me',
 * 	title : 'My title',
 * 	onClick : function() {
 * 		// this = CKEDITOR.ui.dialog.button
 * 		alert( 'Clicked: ' + this.id );
 * 	}
 * }
 */

/**
 * Whether the button is disabled.
 * @name CKEDITOR.dialog.definition.button.prototype.disabled
 * @type Boolean
 * @field
 * @example
 */

/**
 * The label of the UI element.
 * @name CKEDITOR.dialog.definition.button.prototype.label
 * @type String
 * @field
 * @example
 */

// ----- checkbox ------

/**
 * The definition of a checkbox element.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create groups of checkbox buttons.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.checkbox} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.checkbox
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'checkbox',</b>
 * 	id : 'agree',
 * 	label : 'I agree',
 * 	'default' : 'checked',
 * 	onClick : function() {
 * 		// this = CKEDITOR.ui.dialog.checkbox
 * 		alert( 'Checked: ' + this.getValue() );
 * 	}
 * }
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.checkbox.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 * The label of the UI element.
 * @name CKEDITOR.dialog.definition.checkbox.prototype.label
 * @type String
 * @field
 * @example
 */

/**
 * The default state.
 * @name CKEDITOR.dialog.definition.checkbox.prototype.default
 * @type String
 * @field
 * @default
 * '' (unchecked)
 * @example
 */

// ----- file -----

/**
 * The definition of a file upload input.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create file upload elements.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.file} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.file
 * @extends CKEDITOR.dialog.definition.labeledElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'file'</b>,
 * 	id : 'upload',
 * 	label : 'Select file from your computer',
 * 	size : 38
 * },
 * {
 * 	type : 'fileButton',
 * 	id : 'fileId',
 * 	label : 'Upload file',
 * 	'for' : [ 'tab1', 'upload' ]
 * 	filebrowser : {
 * 		onSelect : function( fileUrl, data ) {
 * 			alert( 'Successfully uploaded: ' + fileUrl );
 * 		}
 * 	}
 * }
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.file.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 * (Optional) The action attribute of the form element associated with this file upload input.
 * If empty, CKEditor will use path to server connector for currently opened folder.
 * @name CKEDITOR.dialog.definition.file.prototype.action
 * @type String
 * @field
 * @example
 */

/**
 * The size of the UI element.
 * @name CKEDITOR.dialog.definition.file.prototype.size
 * @type Number
 * @field
 * @example
 */

// ----- fileButton -----

/**
 * The definition of a button for submitting the file in a file upload input.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create a button for submitting the file in a file upload input.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.fileButton} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.fileButton
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	type : 'file',
 * 	id : 'upload',
 * 	label : 'Select file from your computer',
 * 	size : 38
 * },
 * {
 * 	<b>type : 'fileButton'</b>,
 * 	id : 'fileId',
 * 	label : 'Upload file',
 * 	'for' : [ 'tab1', 'upload' ]
 * 	filebrowser : {
 * 		onSelect : function( fileUrl, data ) {
 * 			alert( 'Successfully uploaded: ' + fileUrl );
 * 		}
 * 	}
 * }
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.fileButton.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 * The label of the UI element.
 * @name CKEDITOR.dialog.definition.fileButton.prototype.label
 * @type String
 * @field
 * @example
 */

/**
 * The instruction for CKEditor how to deal with file upload.
 * By default, the file and fileButton elements will not work "as expected" if this attribute is not set.
 * @name CKEDITOR.dialog.definition.fileButton.prototype.filebrowser
 * @type String|Object
 * @field
 * @example
 * // Update field with id 'txtUrl' in the 'tab1' tab when file is uploaded.
 * filebrowser : 'tab1:txtUrl'
 *
 * // Call custom onSelect function when file is successfully uploaded.
 * filebrowser : {
 * 	onSelect : function( fileUrl, data ) {
 * 		alert( 'Successfully uploaded: ' + fileUrl );
 * 	}
 * }
 */

/**
 * An array that contains pageId and elementId of the file upload input element for which this button is created.
 * @name CKEDITOR.dialog.definition.fileButton.prototype.for
 * @type String
 * @field
 * @example
 * [ pageId, elementId ]
 */

// ----- html -----

/**
 * The definition of a raw HTML element.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create elements made from raw HTML code.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.html} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.<br />
 * To access HTML elements use {@link CKEDITOR.dom.document#getById}
 * @name CKEDITOR.dialog.definition.html
 * @extends CKEDITOR.dialog.definition.uiElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example 1:
 * {
 * 	<b>type : 'html',</b>
 * 	html : '&lt;h3>This is some sample HTML content.&lt;/h3>'
 * }
 * @example
 * // Example 2:
 * // Complete sample with document.getById() call when the "Ok" button is clicked.
 * var dialogDefinition =
 * {
 * 	title : 'Sample dialog',
 * 	minWidth : 300,
 * 	minHeight : 200,
 * 	onOk : function() {
 * 		// "this" is now a CKEDITOR.dialog object.
 * 		var document = this.getElement().getDocument();
 * 		// document = CKEDITOR.dom.document
 * 		var element = <b>document.getById( 'myDiv' );</b>
 * 		if ( element )
 * 			alert( element.getHtml() );
 * 	},
 * 	contents : [
 * 		{
 * 			id : 'tab1',
 * 			label : '',
 * 			title : '',
 * 			elements :
 * 			[
 * 				{
 * 					<b>type : 'html',</b>
 * 					html : '<b>&lt;div id="myDiv">Sample &lt;b>text&lt;/b>.&lt;/div></b>&lt;div id="otherId">Another div.&lt;/div>'
 * 				},
 * 			]
 * 		}
 * 	],
 * 	buttons : [ CKEDITOR.dialog.cancelButton, CKEDITOR.dialog.okButton ]
 * };
 */

/**
 * (Required) HTML code of this element.
 * @name CKEDITOR.dialog.definition.html.prototype.html
 * @type String
 * @field
 * @example
 */

// ----- radio ------

/**
 * The definition of a radio group.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create groups of radio buttons.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.radio} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.radio
 * @extends CKEDITOR.dialog.definition.labeledElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'radio',</b>
 * 	id : 'country',
 * 	label : 'Which country is bigger',
 * 	items : [ [ 'France', 'FR' ], [ 'Germany', 'DE' ] ] ,
 * 	style : 'color:green',
 * 	'default' : 'DE',
 * 	onClick : function() {
 * 		// this = CKEDITOR.ui.dialog.radio
 * 		alert( 'Current value: ' + this.getValue() );
 * 	}
 * }
 */

/**
 * The default value.
 * @name CKEDITOR.dialog.definition.radio.prototype.default
 * @type String
 * @field
 * @example
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.radio.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 *  An array of options. Each option is a 1- or 2-item array of format [ 'Description', 'Value' ]. If 'Value' is missing, then the value would be assumed to be the same as the description.
 * @name CKEDITOR.dialog.definition.radio.prototype.items
 * @field
 * @type Array
 * @example
 */

// ----- selectElement ------

/**
 * The definition of a select element.
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create select elements.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.select} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.select
 * @extends CKEDITOR.dialog.definition.labeledElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'select',</b>
 * 	id : 'sport',
 * 	label : 'Select your favourite sport',
 * 	items : [ [ 'Basketball' ], [ 'Baseball' ], [ 'Hockey' ], [ 'Football' ] ],
 * 	'default' : 'Football',
 * 	onChange : function( api ) {
 * 		// this = CKEDITOR.ui.dialog.select
 * 		alert( 'Current value: ' + this.getValue() );
 * 	}
 * }
 */

/**
 * The default value.
 * @name CKEDITOR.dialog.definition.select.prototype.default
 * @type String
 * @field
 * @example
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.select.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 *  An array of options. Each option is a 1- or 2-item array of format [ 'Description', 'Value' ]. If 'Value' is missing, then the value would be assumed to be the same as the description.
 * @name CKEDITOR.dialog.definition.select.prototype.items
 * @field
 * @type Array
 * @example
 */

/**
 * (Optional) Set this to true if you'd like to have a multiple-choice select box.
 * @name CKEDITOR.dialog.definition.select.prototype.multiple
 * @type Boolean
 * @field
 * @example
 * @default false
 */

/**
 * (Optional) The number of items to display in the select box.
 * @name CKEDITOR.dialog.definition.select.prototype.size
 * @type Number
 * @field
 * @example
 */

// ----- textInput -----

/**
 * The definition of a text field (single line).
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create text fields.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.textInput} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.textInput
 * @extends CKEDITOR.dialog.definition.labeledElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * {
 * 	<b>type : 'text',</b>
 * 	id : 'name',
 * 	label : 'Your name',
 * 	'default' : '',
 * 	validate : function() {
 * 		if ( !this.getValue() )
 * 		{
 * 			api.openMsgDialog( '', 'Name cannot be empty.' );
 * 			return false;
 * 		}
 * 	}
 * }
 */

/**
 * The default value.
 * @name CKEDITOR.dialog.definition.textInput.prototype.default
 * @type String
 * @field
 * @example
 */

/**
 * (Optional) The maximum length.
 * @name CKEDITOR.dialog.definition.textInput.prototype.maxLength
 * @type Number
 * @field
 * @example
 */

/**
 * (Optional) The size of the input field.
 * @name CKEDITOR.dialog.definition.textInput.prototype.size
 * @type Number
 * @field
 * @example
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.textInput.prototype.validate
 * @field
 * @type Function
 * @example
 */

// ----- textarea ------

/**
 * The definition of a text field (multiple lines).
 * <div class="notapi">
 * This class is not really part of the API. It just illustrates the properties
 * that developers can use to define and create textarea.
 * <br /><br />Once the dialog is opened, the created element becomes a {@link CKEDITOR.ui.dialog.textarea} object and can be accessed with {@link CKEDITOR.dialog#getContentElement}.
 * </div>
 * For a complete example of dialog definition, please check {@link CKEDITOR.dialog.add}.
 * @name CKEDITOR.dialog.definition.textarea
 * @extends CKEDITOR.dialog.definition.labeledElement
 * @constructor
 * @example
 * // There is no constructor for this class, the user just has to define an
 * // object with the appropriate properties.
 *
 * // Example:
 * {
 * 	<b>type : 'textarea',</b>
 * 	id : 'message',
 * 	label : 'Your comment',
 * 	'default' : '',
 * 	validate : function() {
 * 		if ( this.getValue().length < 5 )
 * 		{
 * 			api.openMsgDialog( 'The comment is too short.' );
 * 			return false;
 * 		}
 * 	}
 * }
 */

/**
 * The number of rows.
 * @name CKEDITOR.dialog.definition.textarea.prototype.rows
 * @type Number
 * @field
 * @example
 */

/**
 * The number of columns.
 * @name CKEDITOR.dialog.definition.textarea.prototype.cols
 * @type Number
 * @field
 * @example
 */

/**
 * (Optional) The validation function.
 * @name CKEDITOR.dialog.definition.textarea.prototype.validate
 * @field
 * @type Function
 * @example
 */

/**
 * The default value.
 * @name CKEDITOR.dialog.definition.textarea.prototype.default
 * @type String
 * @field
 * @example
 */
