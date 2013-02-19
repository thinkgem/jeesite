/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.editor} class, which represents an
 *		editor instance.
 */

(function()
{
	// The counter for automatic instance names.
	var nameCounter = 0;

	var getNewName = function()
	{
		var name = 'editor' + ( ++nameCounter );
		return ( CKEDITOR.instances && CKEDITOR.instances[ name ] ) ? getNewName() : name;
	};

	// ##### START: Config Privates

	// These function loads custom configuration files and cache the
	// CKEDITOR.editorConfig functions defined on them, so there is no need to
	// download them more than once for several instances.
	var loadConfigLoaded = {};
	var loadConfig = function( editor )
	{
		var customConfig = editor.config.customConfig;

		// Check if there is a custom config to load.
		if ( !customConfig )
			return false;

		customConfig = CKEDITOR.getUrl( customConfig );

		var loadedConfig = loadConfigLoaded[ customConfig ] || ( loadConfigLoaded[ customConfig ] = {} );

		// If the custom config has already been downloaded, reuse it.
		if ( loadedConfig.fn )
		{
			// Call the cached CKEDITOR.editorConfig defined in the custom
			// config file for the editor instance depending on it.
			loadedConfig.fn.call( editor, editor.config );

			// If there is no other customConfig in the chain, fire the
			// "configLoaded" event.
			if ( CKEDITOR.getUrl( editor.config.customConfig ) == customConfig || !loadConfig( editor ) )
				editor.fireOnce( 'customConfigLoaded' );
		}
		else
		{
			// Load the custom configuration file.
			CKEDITOR.scriptLoader.load( customConfig, function()
				{
					// If the CKEDITOR.editorConfig function has been properly
					// defined in the custom configuration file, cache it.
					if ( CKEDITOR.editorConfig )
						loadedConfig.fn = CKEDITOR.editorConfig;
					else
						loadedConfig.fn = function(){};

					// Call the load config again. This time the custom
					// config is already cached and so it will get loaded.
					loadConfig( editor );
				});
		}

		return true;
	};

	var initConfig = function( editor, instanceConfig )
	{
		// Setup the lister for the "customConfigLoaded" event.
		editor.on( 'customConfigLoaded', function()
			{
				if ( instanceConfig )
				{
					// Register the events that may have been set at the instance
					// configuration object.
					if ( instanceConfig.on )
					{
						for ( var eventName in instanceConfig.on )
						{
							editor.on( eventName, instanceConfig.on[ eventName ] );
						}
					}

					// Overwrite the settings from the in-page config.
					CKEDITOR.tools.extend( editor.config, instanceConfig, true );

					delete editor.config.on;
				}

				onConfigLoaded( editor );
			});

		// The instance config may override the customConfig setting to avoid
		// loading the default ~/config.js file.
		if ( instanceConfig && instanceConfig.customConfig != undefined )
			editor.config.customConfig = instanceConfig.customConfig;

		// Load configs from the custom configuration files.
		if ( !loadConfig( editor ) )
			editor.fireOnce( 'customConfigLoaded' );
	};

	// ##### END: Config Privates

	var onConfigLoaded = function( editor )
	{
		// Set config related properties.

		var skin = editor.config.skin.split( ',' ),
			skinName = skin[ 0 ],
			skinPath = CKEDITOR.getUrl( skin[ 1 ] || (
				'_source/' +	// @Packager.RemoveLine
				'skins/' + skinName + '/' ) );

		/**
		 * The name of the skin used by this editor instance. The skin name can
		 * be set through the <code>{@link CKEDITOR.config.skin}</code> setting.
		 * @name CKEDITOR.editor.prototype.skinName
		 * @type String
		 * @example
		 * alert( editor.skinName );  // E.g. "kama"
		 */
		editor.skinName = skinName;

		/**
		 * The full URL of the skin directory.
		 * @name CKEDITOR.editor.prototype.skinPath
		 * @type String
		 * @example
		 * alert( editor.skinPath );  // E.g. "http://example.com/ckeditor/skins/kama/"
		 */
		editor.skinPath = skinPath;

		/**
		 * The CSS class name used for skin identification purposes.
		 * @name CKEDITOR.editor.prototype.skinClass
		 * @type String
		 * @example
		 * alert( editor.skinClass );  // E.g. "cke_skin_kama"
		 */
		editor.skinClass = 'cke_skin_' + skinName;

		/**
		 * The <a href="http://en.wikipedia.org/wiki/Tabbing_navigation">tabbing
		 * navigation</a> order that has been calculated for this editor
		 * instance. This can be set by the <code>{@link CKEDITOR.config.tabIndex}</code>
		 * setting or taken from the <code>tabindex</code> attribute of the
		 * <code>{@link #element}</code> associated with the editor.
		 * @name CKEDITOR.editor.prototype.tabIndex
		 * @type Number
		 * @default 0 (zero)
		 * @example
		 * alert( editor.tabIndex );  // E.g. "0"
		 */
		editor.tabIndex = editor.config.tabIndex || editor.element.getAttribute( 'tabindex' ) || 0;

		/**
		 * Indicates the read-only state of this editor. This is a read-only property.
		 * @name CKEDITOR.editor.prototype.readOnly
		 * @type Boolean
		 * @since 3.6
		 * @see CKEDITOR.editor#setReadOnly
		 */
		editor.readOnly = !!( editor.config.readOnly || editor.element.getAttribute( 'disabled' ) );

		// Fire the "configLoaded" event.
		editor.fireOnce( 'configLoaded' );

		// Load language file.
		loadSkin( editor );
	};

	var loadLang = function( editor )
	{
		CKEDITOR.lang.load( editor.config.language, editor.config.defaultLanguage, function( languageCode, lang )
			{
				/**
				 * The code for the language resources that have been loaded
				 * for the user interface elements of this editor instance.
				 * @name CKEDITOR.editor.prototype.langCode
				 * @type String
				 * @example
				 * alert( editor.langCode );  // E.g. "en"
				 */
				editor.langCode = languageCode;

				/**
				 * An object that contains all language strings used by the editor
				 * interface.
				 * @name CKEDITOR.editor.prototype.lang
				 * @type CKEDITOR.lang
				 * @example
				 * alert( editor.lang.bold );  // E.g. "Negrito" (if the language is set to Portuguese)
				 */
				// As we'll be adding plugin specific entries that could come
				// from different language code files, we need a copy of lang,
				// not a direct reference to it.
				editor.lang = CKEDITOR.tools.prototypedCopy( lang );

				// We're not able to support RTL in Firefox 2 at this time.
				if ( CKEDITOR.env.gecko && CKEDITOR.env.version < 10900 && editor.lang.dir == 'rtl' )
					editor.lang.dir = 'ltr';

				editor.fire( 'langLoaded' );

				var config = editor.config;
				config.contentsLangDirection == 'ui' && ( config.contentsLangDirection = editor.lang.dir );

				loadPlugins( editor );
			});
	};

	var loadPlugins = function( editor )
	{
		var config			= editor.config,
			plugins			= config.plugins,
			extraPlugins	= config.extraPlugins,
			removePlugins	= config.removePlugins;

		if ( extraPlugins )
		{
			// Remove them first to avoid duplications.
			var removeRegex = new RegExp( '(?:^|,)(?:' + extraPlugins.replace( /\s*,\s*/g, '|' ) + ')(?=,|$)' , 'g' );
			plugins = plugins.replace( removeRegex, '' );

			plugins += ',' + extraPlugins;
		}

		if ( removePlugins )
		{
			removeRegex = new RegExp( '(?:^|,)(?:' + removePlugins.replace( /\s*,\s*/g, '|' ) + ')(?=,|$)' , 'g' );
			plugins = plugins.replace( removeRegex, '' );
		}

		// Load the Adobe AIR plugin conditionally.
		CKEDITOR.env.air && ( plugins += ',adobeair' );

		// Load all plugins defined in the "plugins" setting.
		CKEDITOR.plugins.load( plugins.split( ',' ), function( plugins )
			{
				// The list of plugins.
				var pluginsArray = [];

				// The language code to get loaded for each plugin. Null
				// entries will be appended for plugins with no language files.
				var languageCodes = [];

				// The list of URLs to language files.
				var languageFiles = [];

				/**
				 * An object that contains references to all plugins used by this
				 * editor instance.
				 * @name CKEDITOR.editor.prototype.plugins
				 * @type Object
				 * @example
				 * alert( editor.plugins.dialog.path );  // E.g. "http://example.com/ckeditor/plugins/dialog/"
				 */
				editor.plugins = plugins;

				// Loop through all plugins, to build the list of language
				// files to get loaded.
				for ( var pluginName in plugins )
				{
					var plugin = plugins[ pluginName ],
						pluginLangs = plugin.lang,
						pluginPath = CKEDITOR.plugins.getPath( pluginName ),
						lang = null;

					// Set the plugin path in the plugin.
					plugin.path = pluginPath;

					// If the plugin has "lang".
					if ( pluginLangs )
					{
						// Resolve the plugin language. If the current language
						// is not available, get the first one (default one).
						lang = ( CKEDITOR.tools.indexOf( pluginLangs, editor.langCode ) >= 0 ? editor.langCode : pluginLangs[ 0 ] );

						if ( !plugin.langEntries || !plugin.langEntries[ lang ] )
						{
							// Put the language file URL into the list of files to
							// get downloaded.
							languageFiles.push( CKEDITOR.getUrl( pluginPath + 'lang/' + lang + '.js' ) );
						}
						else
						{
							CKEDITOR.tools.extend( editor.lang, plugin.langEntries[ lang ] );
							lang = null;
						}
					}

					// Save the language code, so we know later which
					// language has been resolved to this plugin.
					languageCodes.push( lang );

					pluginsArray.push( plugin );
				}

				// Load all plugin specific language files in a row.
				CKEDITOR.scriptLoader.load( languageFiles, function()
					{
						// Initialize all plugins that have the "beforeInit" and "init" methods defined.
						var methods = [ 'beforeInit', 'init', 'afterInit' ];
						for ( var m = 0 ; m < methods.length ; m++ )
						{
							for ( var i = 0 ; i < pluginsArray.length ; i++ )
							{
								var plugin = pluginsArray[ i ];

								// Uses the first loop to update the language entries also.
								if ( m === 0 && languageCodes[ i ] && plugin.lang )
									CKEDITOR.tools.extend( editor.lang, plugin.langEntries[ languageCodes[ i ] ] );

								// Call the plugin method (beforeInit and init).
								if ( plugin[ methods[ m ] ] )
									plugin[ methods[ m ] ]( editor );
							}
						}

						// Load the editor skin.
						editor.fire( 'pluginsLoaded' );
						loadTheme( editor );
					});
			});
	};

	var loadSkin = function( editor )
	{
		CKEDITOR.skins.load( editor, 'editor', function()
			{
				loadLang( editor );
			});
	};

	var loadTheme = function( editor )
	{
		var theme = editor.config.theme;
		CKEDITOR.themes.load( theme, function()
			{
				/**
				 * The theme used by this editor instance.
				 * @name CKEDITOR.editor.prototype.theme
				 * @type CKEDITOR.theme
				 * @example
				 * alert( editor.theme );  // E.g. "http://example.com/ckeditor/themes/default/"
				 */
				var editorTheme = editor.theme = CKEDITOR.themes.get( theme );
				editorTheme.path = CKEDITOR.themes.getPath( theme );
				editorTheme.build( editor );

				if ( editor.config.autoUpdateElement )
					attachToForm( editor );
			});
	};

	var attachToForm = function( editor )
	{
		var element = editor.element;

		// If are replacing a textarea, we must
		if ( editor.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE && element.is( 'textarea' ) )
		{
			var form = element.$.form && new CKEDITOR.dom.element( element.$.form );
			if ( form )
			{
				function onSubmit()
				{
					editor.updateElement();
				}
				form.on( 'submit',onSubmit );

				// Setup the submit function because it doesn't fire the
				// "submit" event.
				if ( !form.$.submit.nodeName && !form.$.submit.length )
				{
					form.$.submit = CKEDITOR.tools.override( form.$.submit, function( originalSubmit )
						{
							return function()
								{
									editor.updateElement();

									// For IE, the DOM submit function is not a
									// function, so we need thid check.
									if ( originalSubmit.apply )
										originalSubmit.apply( this, arguments );
									else
										originalSubmit();
								};
						});
				}

				// Remove 'submit' events registered on form element before destroying.(#3988)
				editor.on( 'destroy', function()
				{
					form.removeListener( 'submit', onSubmit );
				} );
			}
		}
	};

	function updateCommands()
	{
		var command,
			commands = this._.commands,
			mode = this.mode;

		if ( !mode )
			return;

		for ( var name in commands )
		{
			command = commands[ name ];
			command[ command.startDisabled ? 'disable' :
					 this.readOnly && !command.readOnly ? 'disable' : command.modes[ mode ] ? 'enable' : 'disable' ]();
		}
	}

	/**
	 * Initializes the editor instance. This function is called by the editor
	 * contructor (<code>editor_basic.js</code>).
	 * @private
	 */
	CKEDITOR.editor.prototype._init = function()
		{
			// Get the properties that have been saved in the editor_base
			// implementation.
			var element			= CKEDITOR.dom.element.get( this._.element ),
				instanceConfig	= this._.instanceConfig;
			delete this._.element;
			delete this._.instanceConfig;

			this._.commands = {};
			this._.styles = [];

			/**
			 * The DOM element that was replaced by this editor instance. This
			 * element stores the editor data on load and post.
			 * @name CKEDITOR.editor.prototype.element
			 * @type CKEDITOR.dom.element
			 * @example
			 * var editor = CKEDITOR.instances.editor1;
			 * alert( <strong>editor.element</strong>.getName() );  // E.g. "textarea"
			 */
			this.element = element;

			/**
			 * The editor instance name. It may be the replaced element ID, name, or
			 * a default name using the progressive counter (<code>editor1</code>,
			 * <code>editor2</code>, ...).
			 * @name CKEDITOR.editor.prototype.name
			 * @type String
			 * @example
			 * var editor = CKEDITOR.instances.editor1;
			 * alert( <strong>editor.name</strong> );  // "editor1"
			 */
			this.name = ( element && ( this.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
							&& ( element.getId() || element.getNameAtt() ) )
						|| getNewName();

			if ( this.name in CKEDITOR.instances )
				throw '[CKEDITOR.editor] The instance "' + this.name + '" already exists.';

			/**
			 * A unique random string assigned to each editor instance on the page.
			 * @name CKEDITOR.editor.prototype.id
			 * @type String
			 */
			this.id = CKEDITOR.tools.getNextId();

			/**
			 * The configurations for this editor instance. It inherits all
			 * settings defined in <code>(@link CKEDITOR.config}</code>, combined with settings
			 * loaded from custom configuration files and those defined inline in
			 * the page when creating the editor.
			 * @name CKEDITOR.editor.prototype.config
			 * @type Object
			 * @example
			 * var editor = CKEDITOR.instances.editor1;
			 * alert( <strong>editor.config.theme</strong> );  // E.g. "default"
			 */
			this.config = CKEDITOR.tools.prototypedCopy( CKEDITOR.config );

			/**
			 * The namespace containing UI features related to this editor instance.
			 * @name CKEDITOR.editor.prototype.ui
			 * @type CKEDITOR.ui
			 * @example
			 */
			this.ui = new CKEDITOR.ui( this );

			/**
			 * Controls the focus state of this editor instance. This property
			 * is rarely used for normal API operations. It is mainly
			 * intended for developers adding UI elements to the editor interface.
			 * @name CKEDITOR.editor.prototype.focusManager
			 * @type CKEDITOR.focusManager
			 * @example
			 */
			this.focusManager = new CKEDITOR.focusManager( this );

			CKEDITOR.fire( 'instanceCreated', null, this );

			this.on( 'mode', updateCommands, null, null, 1 );
			this.on( 'readOnly', updateCommands, null, null, 1 );

			initConfig( this, instanceConfig );
		};
})();

CKEDITOR.tools.extend( CKEDITOR.editor.prototype,
	/** @lends CKEDITOR.editor.prototype */
	{
		/**
		 * Adds a command definition to the editor instance. Commands added with
		 * this function can be executed later with the <code>{@link #execCommand}</code> method.
		 * @param {String} commandName The indentifier name of the command.
		 * @param {CKEDITOR.commandDefinition} commandDefinition The command definition.
		 * @example
		 * editorInstance.addCommand( 'sample',
		 * {
		 *     exec : function( editor )
		 *     {
		 *         alert( 'Executing a command for the editor name "' + editor.name + '"!' );
		 *     }
		 * });
		 */
		addCommand : function( commandName, commandDefinition )
		{
			return this._.commands[ commandName ] = new CKEDITOR.command( this, commandDefinition );
		},

		/**
		 * Adds a piece of CSS code to the editor which will be applied to the WYSIWYG editing document.
		 * This CSS would not be added to the output, and is there mainly for editor-specific editing requirements.
		 * Note: This function should be called before the editor is loaded to take effect.
		 * @param css {String} CSS text.
		 * @example
		 * editorInstance.addCss( 'body { background-color: grey; }' );
		 */
		addCss : function( css )
		{
			this._.styles.push( css );
		},

		/**
		 * Destroys the editor instance, releasing all resources used by it.
		 * If the editor replaced an element, the element will be recovered.
		 * @param {Boolean} [noUpdate] If the instance is replacing a DOM
		 *		element, this parameter indicates whether or not to update the
		 *		element with the instance contents.
		 * @example
		 * alert( CKEDITOR.instances.editor1 );  //  E.g "object"
		 * <strong>CKEDITOR.instances.editor1.destroy()</strong>;
		 * alert( CKEDITOR.instances.editor1 );  // "undefined"
		 */
		destroy : function( noUpdate )
		{
			if ( !noUpdate )
				this.updateElement();

			this.fire( 'destroy' );
			this.theme && this.theme.destroy( this );

			CKEDITOR.remove( this );
			CKEDITOR.fire( 'instanceDestroyed', null, this );
		},

		/**
		 * Executes a command associated with the editor.
		 * @param {String} commandName The indentifier name of the command.
		 * @param {Object} [data] Data to be passed to the command.
		 * @returns {Boolean} <code>true</code> if the command was executed
		 *		successfully, otherwise <code>false</code>.
		 * @see CKEDITOR.editor.addCommand
		 * @example
		 * editorInstance.execCommand( 'bold' );
		 */
		execCommand : function( commandName, data )
		{
			var command = this.getCommand( commandName );

			var eventData =
			{
				name: commandName,
				commandData: data,
				command: command
			};

			if ( command && command.state != CKEDITOR.TRISTATE_DISABLED )
			{
				if ( this.fire( 'beforeCommandExec', eventData ) !== true )
				{
					eventData.returnValue = command.exec( eventData.commandData );

					// Fire the 'afterCommandExec' immediately if command is synchronous.
					if ( !command.async && this.fire( 'afterCommandExec', eventData ) !== true )
						return eventData.returnValue;
				}
			}

			// throw 'Unknown command name "' + commandName + '"';
			return false;
		},

		/**
		 * Gets one of the registered commands. Note that after registering a
		 * command definition with <code>{@link #addCommand}</code>, it is
		 * transformed internally into an instance of
		 * <code>{@link CKEDITOR.command}</code>, which will then be returned
		 * by this function.
		 * @param {String} commandName The name of the command to be returned.
		 * This is the same name that is used to register the command with
		 * 		<code>addCommand</code>.
		 * @returns {CKEDITOR.command} The command object identified by the
		 * provided name.
		 */
		getCommand : function( commandName )
		{
			return this._.commands[ commandName ];
		},

		/**
		 * Gets the editor data. The data will be in raw format. It is the same
		 * data that is posted by the editor.
		 * @type String
		 * @returns (String) The editor data.
		 * @example
		 * if ( CKEDITOR.instances.editor1.<strong>getData()</strong> == '' )
		 *     alert( 'There is no data available' );
		 */
		getData : function()
		{
			this.fire( 'beforeGetData' );

			var eventData = this._.data;

			if ( typeof eventData != 'string' )
			{
				var element = this.element;
				if ( element && this.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
					eventData = element.is( 'textarea' ) ? element.getValue() : element.getHtml();
				else
					eventData = '';
			}

			eventData = { dataValue : eventData };

			// Fire "getData" so data manipulation may happen.
			this.fire( 'getData', eventData );

			return eventData.dataValue;
		},

		/**
		 * Gets the "raw data" currently available in the editor. This is a
		 * fast method which returns the data as is, without processing, so it is
		 * not recommended to use it on resulting pages. Instead it can be used
		 * combined with the <code>{@link #loadSnapshot}</code> method in order
		 * to be able to automatically save the editor data from time to time
		 * while the user is using the editor, to avoid data loss, without risking
		 * performance issues.
		 * @see CKEDITOR.editor.getData
		 * @example
		 * alert( editor.getSnapshot() );
		 */
		getSnapshot : function()
		{
			var data = this.fire( 'getSnapshot' );

			if ( typeof data != 'string' )
			{
				var element = this.element;
				if ( element && this.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
					data = element.is( 'textarea' ) ? element.getValue() : element.getHtml();
			}

			return data;
		},

		/**
		 * Loads "raw data" into the editor. The data is loaded with processing
		 * straight to the editing area. It should not be used as a way to load
		 * any kind of data, but instead in combination with
		 * <code>{@link #getSnapshot}</code> produced data.
		 * @see CKEDITOR.editor.setData
		 * @example
		 * var data = editor.getSnapshot();
		 * editor.<strong>loadSnapshot( data )</strong>;
		 */
		loadSnapshot : function( snapshot )
		{
			this.fire( 'loadSnapshot', snapshot );
		},

		/**
		 * Sets the editor data. The data must be provided in the raw format (HTML).<br />
		 * <br />
		 * Note that this method is asynchronous. The <code>callback</code> parameter must
		 * be used if interaction with the editor is needed after setting the data.
		 * @param {String} data HTML code to replace the curent content in the
		 *		editor.
		 * @param {Function} callback Function to be called after the <code>setData</code>
		 *		is completed.
		 *@param {Boolean} internal Whether to suppress any event firing when copying data
		 *		internally inside the editor.
		 * @example
		 * CKEDITOR.instances.editor1.<strong>setData</strong>( '&lt;p&gt;This is the editor data.&lt;/p&gt;' );
		 * @example
		 * CKEDITOR.instances.editor1.<strong>setData</strong>( '&lt;p&gt;Some other editor data.&lt;/p&gt;', function()
		 *     {
		 *         this.checkDirty();  // true
		 *     });
		 */
		setData : function( data , callback, internal )
		{
			if( callback )
			{
				this.on( 'dataReady', function( evt )
				{
					evt.removeListener();
					callback.call( evt.editor );
				} );
			}

			// Fire "setData" so data manipulation may happen.
			var eventData = { dataValue : data };
			!internal && this.fire( 'setData', eventData );

			this._.data = eventData.dataValue;

			!internal && this.fire( 'afterSetData', eventData );
		},

		/**
		 * Puts or restores the editor into read-only state. When in read-only,
		 * the user is not able to change the editor contents, but can still use
		 * some editor features. This function sets the <code>{@link CKEDITOR.config.readOnly}</code>
		 * property of the editor, firing the <code>{@link CKEDITOR.editor#readOnly}</code> event.<br><br>
		 * <strong>Note:</strong> the current editing area will be reloaded.
		 * @param {Boolean} [isReadOnly] Indicates that the editor must go
		 *		read-only (<code>true</code>, default) or be restored and made editable
		 * 		(<code>false</code>).
		 * @since 3.6
		 */
		setReadOnly : function( isReadOnly )
		{
			isReadOnly = ( isReadOnly == undefined ) || isReadOnly;

			if ( this.readOnly != isReadOnly )
			{
				this.readOnly = isReadOnly;

				// Fire the readOnly event so the editor features can update
				// their state accordingly.
				this.fire( 'readOnly' );
			}
		},

		/**
		 * Inserts HTML code into the currently selected position in the editor in WYSIWYG mode.
		 * @param {String} data HTML code to be inserted into the editor.
		 * @example
		 * CKEDITOR.instances.editor1.<strong>insertHtml( '&lt;p&gt;This is a new paragraph.&lt;/p&gt;' )</strong>;
		 */
		insertHtml : function( data )
		{
			this.fire( 'insertHtml', data );
		},

		/**
		 * Insert text content into the currently selected position in the
		 * editor in WYSIWYG mode. The styles of the selected element will be applied to the inserted text.
		 * Spaces around the text will be leaving untouched.
		 * <strong>Note:</strong> two subsequent line-breaks will introduce one paragraph. This depends on <code>{@link CKEDITOR.config.enterMode}</code>;
		 * A single line-break will be instead translated into one &lt;br /&gt;.
		 * @since 3.5
		 * @param {String} text Text to be inserted into the editor.
		 * @example
		 * CKEDITOR.instances.editor1.<strong>insertText( ' line1 \n\n line2' )</strong>;
		 */
		insertText : function( text )
		{
			this.fire( 'insertText', text );
		},

		/**
		 * Inserts an element into the currently selected position in the
		 * editor in WYSIWYG mode.
		 * @param {CKEDITOR.dom.element} element The element to be inserted
		 *		into the editor.
		 * @example
		 * var element = CKEDITOR.dom.element.createFromHtml( '&lt;img src="hello.png" border="0" title="Hello" /&gt;' );
		 * CKEDITOR.instances.editor1.<strong>insertElement( element )</strong>;
		 */
		insertElement : function( element )
		{
			this.fire( 'insertElement', element );
		},

		/**
		 * Checks whether the current editor contents contain changes when
		 * compared to the contents loaded into the editor at startup, or to
		 * the contents available in the editor when <code>{@link #resetDirty}</code>
		 * was called.
		 * @returns {Boolean} "true" is the contents contain changes.
		 * @example
		 * function beforeUnload( e )
		 * {
		 *     if ( CKEDITOR.instances.editor1.<strong>checkDirty()</strong> )
		 * 	        return e.returnValue = "You will lose the changes made in the editor.";
		 * }
		 *
		 * if ( window.addEventListener )
		 *     window.addEventListener( 'beforeunload', beforeUnload, false );
		 * else
		 *     window.attachEvent( 'onbeforeunload', beforeUnload );
		 */
		checkDirty : function()
		{
			return ( this.mayBeDirty && this._.previousValue !== this.getSnapshot() );
		},

		/**
		 * Resets the "dirty state" of the editor so subsequent calls to
		 * <code>{@link #checkDirty}</code> will return <code>false</code> if the user will not
		 * have made further changes to the contents.
		 * @example
		 * alert( editor.checkDirty() );  // E.g. "true"
		 * editor.<strong>resetDirty()</strong>;
		 * alert( editor.checkDirty() );  // "false"
		 */
		resetDirty : function()
		{
			if ( this.mayBeDirty )
				this._.previousValue = this.getSnapshot();
		},

		/**
		 * Updates the <code>&lt;textarea&gt;</code> element that was replaced by the editor with
		 * the current data available in the editor.
		 * @see CKEDITOR.editor.element
		 * @example
		 * CKEDITOR.instances.editor1.updateElement();
		 * alert( document.getElementById( 'editor1' ).value );  // The current editor data.
		 */
		updateElement : function()
		{
			var element = this.element;
			if ( element && this.elementMode == CKEDITOR.ELEMENT_MODE_REPLACE )
			{
				var data = this.getData();

				if ( this.config.htmlEncodeOutput )
					data = CKEDITOR.tools.htmlEncode( data );

				if ( element.is( 'textarea' ) )
					element.setValue( data );
				else
					element.setHtml( data );
			}
		}
	});

CKEDITOR.on( 'loaded', function()
	{
		// Run the full initialization for pending editors.
		var pending = CKEDITOR.editor._pending;
		if ( pending )
		{
			delete CKEDITOR.editor._pending;

			for ( var i = 0 ; i < pending.length ; i++ )
				pending[ i ]._init();
		}
	});

/**
 * Whether to escape HTML when the editor updates the original input element.
 * @name CKEDITOR.config.htmlEncodeOutput
 * @since 3.1
 * @type Boolean
 * @default false
 * @example
 * config.htmlEncodeOutput = true;
 */

/**
 * If <code>true</code>, makes the editor start in read-only state. Otherwise, it will check
 * if the linked <code>&lt;textarea&gt;</code> element has the <code>disabled</code> attribute.
 * @name CKEDITOR.config.readOnly
 * @see CKEDITOR.editor#setReadOnly
 * @type Boolean
 * @default false
 * @since 3.6
 * @example
 * config.readOnly = true;
 */

/**
 * Fired when a CKEDITOR instance is created, but still before initializing it.
 * To interact with a fully initialized instance, use the
 * <code>{@link CKEDITOR#instanceReady}</code> event instead.
 * @name CKEDITOR#instanceCreated
 * @event
 * @param {CKEDITOR.editor} editor The editor instance that has been created.
 */

/**
 * Fired when a CKEDITOR instance is destroyed.
 * @name CKEDITOR#instanceDestroyed
 * @event
 * @param {CKEDITOR.editor} editor The editor instance that has been destroyed.
 */

/**
 * Fired when the language is loaded into the editor instance.
 * @name CKEDITOR.editor#langLoaded
 * @event
 * @since 3.6.1
 * @param {CKEDITOR.editor} editor This editor instance.
 */

/**
 * Fired when all plugins are loaded and initialized into the editor instance.
 * @name CKEDITOR.editor#pluginsLoaded
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 */

/**
 * Fired before the command execution when <code>{@link #execCommand}</code> is called.
 * @name CKEDITOR.editor#beforeCommandExec
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data.name The command name.
 * @param {Object} data.commandData The data to be sent to the command. This
 *		can be manipulated by the event listener.
 * @param {CKEDITOR.command} data.command The command itself.
 */

/**
 * Fired after the command execution when <code>{@link #execCommand}</code> is called.
 * @name CKEDITOR.editor#afterCommandExec
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data.name The command name.
 * @param {Object} data.commandData The data sent to the command.
 * @param {CKEDITOR.command} data.command The command itself.
 * @param {Object} data.returnValue The value returned by the command execution.
 */

/**
 * Fired when the custom configuration file is loaded, before the final
 * configurations initialization.<br />
 * <br />
 * Custom configuration files can be loaded thorugh the
 * <code>{@link CKEDITOR.config.customConfig}</code> setting. Several files can be loaded
 * by changing this setting.
 * @name CKEDITOR.editor#customConfigLoaded
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 */

/**
 * Fired once the editor configuration is ready (loaded and processed).
 * @name CKEDITOR.editor#configLoaded
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 */

/**
 * Fired when this editor instance is destroyed. The editor at this
 * point is not usable and this event should be used to perform the clean-up
 * in any plugin.
 * @name CKEDITOR.editor#destroy
 * @event
 */

/**
 * Internal event to get the current data.
 * @name CKEDITOR.editor#beforeGetData
 * @event
 */

/**
 * Internal event to perform the <code>#getSnapshot</code> call.
 * @name CKEDITOR.editor#getSnapshot
 * @event
 */

/**
 * Internal event to perform the <code>#loadSnapshot</code> call.
 * @name CKEDITOR.editor#loadSnapshot
 * @event
 */

/**
 * Event fired before the <code>#getData</code> call returns allowing additional manipulation.
 * @name CKEDITOR.editor#getData
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data.dataValue The data that will be returned.
 */

/**
 * Event fired before the <code>#setData</code> call is executed allowing additional manipulation.
 * @name CKEDITOR.editor#setData
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data.dataValue The data that will be used.
 */

/**
 * Event fired at the end of the <code>#setData</code> call execution. Usually it is better to use the
 * <code>{@link CKEDITOR.editor.prototype.dataReady}</code> event.
 * @name CKEDITOR.editor#afterSetData
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data.dataValue The data that has been set.
 */

/**
 * Internal event to perform the <code>#insertHtml</code> call
 * @name CKEDITOR.editor#insertHtml
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} data The HTML to insert.
 */

/**
 * Internal event to perform the <code>#insertText</code> call
 * @name CKEDITOR.editor#insertText
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {String} text The text to insert.
 */

/**
 * Internal event to perform the <code>#insertElement</code> call
 * @name CKEDITOR.editor#insertElement
 * @event
 * @param {CKEDITOR.editor} editor This editor instance.
 * @param {Object} element The element to insert.
 */

/**
 * Event fired after the <code>{@link CKEDITOR.editor#readOnly}</code> property changes.
 * @name CKEDITOR.editor#readOnly
 * @event
 * @since 3.6
 * @param {CKEDITOR.editor} editor This editor instance.
 */
