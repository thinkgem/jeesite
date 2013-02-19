/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the "virtual" {@link CKEDITOR.commandDefinition} class,
 *		which contains the defintion of a command. This file is for
 *		documentation purposes only.
 */

/**
 * (Virtual Class) Do not call this constructor. This class is not really part
 * of the API.
 * @name CKEDITOR.commandDefinition
 * @class Virtual class that illustrates the features of command objects to be
 *		passed to the {@link CKEDITOR.editor.prototype.addCommand} function.
 * @example
 */

 /**
 * The function to be fired when the commend is executed.
 * @name CKEDITOR.commandDefinition.prototype.exec
 * @function
 * @param {CKEDITOR.editor} editor The editor within which run the command.
 * @param {Object} [data] Additional data to be used to execute the command.
 * @returns {Boolean} Whether the command has been successfully executed.
 *		Defaults to "true", if nothing is returned.
 * @example
 * editorInstance.addCommand( 'sample',
 * {
 *     exec : function( editor )
 *     {
 *         alert( 'Executing a command for the editor name "' + editor.name + '"!' );
 *     }
 * });
 */

/**
 * Whether the command need to be hooked into the redo/undo system.
 * @name  CKEDITOR.commandDefinition.prototype.canUndo
 * @type {Boolean}
 * @default true
 * @field
 * @example
 * editorInstance.addCommand( 'alertName',
 * {
 *     exec : function( editor )
 *     {
 *         alert( editor.name );
 *     },
 *     canUndo : false    // No support for undo/redo
 * });
 */

/**
 * Whether the command is asynchronous, which means that the
 * {@link CKEDITOR.editor#event:afterCommandExec} event will be fired by the
 * command itself manually, and that the return value of this command is not to
 * be returned by the {@link CKEDITOR.command#exec} function.
 * @name  CKEDITOR.commandDefinition.prototype.async
 * @default false
 * @type {Boolean}
 * @example
 * editorInstance.addCommand( 'loadOptions',
 * {
 *     exec : function( editor )
 *     {
 *         // Asynchronous operation below.
 *         CKEDITOR.ajax.loadXml( 'data.xml', function()
 *             {
 *                 editor.fire( 'afterCommandExec' );
 *             ));
 *     },
 *     async : true    // The command need some time to complete after exec function returns.
 * });
 */

/**
 * Whether the command should give focus to the editor before execution.
 * @name  CKEDITOR.commandDefinition.prototype.editorFocus
 * @type {Boolean}
 * @default true
 * @see CKEDITOR.command#editorFocus
 * @example
 * editorInstance.addCommand( 'maximize',
 * {
 *     exec : function( editor )
 *     {
 *         // ...
 *     },
 *     editorFocus : false    // The command doesn't require focusing the editing document.
 * });
 */


/**
 * Whether the command state should be set to {@link CKEDITOR.TRISTATE_DISABLED} on startup.
 * @name  CKEDITOR.commandDefinition.prototype.startDisabled
 * @type {Boolean}
 * @default false
 * @example
 * editorInstance.addCommand( 'unlink',
 * {
 *     exec : function( editor )
 *     {
 *         // ...
 *     },
 *     startDisabled : true    // Command is unavailable until selection is inside a link.
 * });
 */

/**
 * The editor modes within which the command can be executed. The execution
 * will have no action if the current mode is not listed in this property.
 * @name  CKEDITOR.commandDefinition.prototype.modes
 * @type Object
 * @default { wysiwyg : 1 }
 * @see CKEDITOR.command#modes
 * @example
 * editorInstance.addCommand( 'link',
 * {
 *     exec : function( editor )
 *     {
 *         // ...
 *     },
 *     modes : { wysiwyg : 1 }    // Command is available in wysiwyg mode only.
 * });
 */
