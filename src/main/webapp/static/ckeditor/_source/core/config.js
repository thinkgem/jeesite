/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the <code>{@link CKEDITOR.config}</code> object that stores the
 * default configuration settings.
 */

/**
 * Used in conjunction with <code>{@link CKEDITOR.config.enterMode}</code>
 * and <code>{@link CKEDITOR.config.shiftEnterMode}</code> configuration
 * settings to make the editor produce <code>&lt;p&gt;</code> tags when
 * using the <em>Enter</em> key.
 * @constant
 */
CKEDITOR.ENTER_P	= 1;

/**
 * Used in conjunction with <code>{@link CKEDITOR.config.enterMode}</code>
 * and <code>{@link CKEDITOR.config.shiftEnterMode}</code> configuration
 * settings to make the editor produce <code>&lt;br&gt;</code> tags when
 * using the <em>Enter</em> key.
 * @constant
 */
CKEDITOR.ENTER_BR	= 2;

/**
 * Used in conjunction with <code>{@link CKEDITOR.config.enterMode}</code>
 * and <code>{@link CKEDITOR.config.shiftEnterMode}</code> configuration
 * settings to make the editor produce <code>&lt;div&gt;</code> tags when
 * using the <em>Enter</em> key.
 * @constant
 */
CKEDITOR.ENTER_DIV	= 3;

/**
 * @namespace Stores default configuration settings. Changes to this object are
 * reflected in all editor instances, if not specified otherwise for a particular
 * instance.
 */
CKEDITOR.config =
{
	/**
	 * The URL path for the custom configuration file to be loaded. If not
	 * overloaded with inline configuration, it defaults to the <code>config.js</code>
	 * file present in the root of the CKEditor installation directory.<br /><br />
	 *
	 * CKEditor will recursively load custom configuration files defined inside
	 * other custom configuration files.
	 * @type String
	 * @default <code>'<em>&lt;CKEditor folder&gt;</em>/config.js'</code>
	 * @example
	 * // Load a specific configuration file.
	 * CKEDITOR.replace( 'myfield', { customConfig : '/myconfig.js' } );
	 * @example
	 * // Do not load any custom configuration file.
	 * CKEDITOR.replace( 'myfield', { customConfig : '' } );
	 */
	customConfig : 'config.js',

	/**
	 * Whether the replaced element (usually a <code>&lt;textarea&gt;</code>)
	 * is to be updated automatically when posting the form containing the editor.
	 * @type Boolean
	 * @default <code>true</code>
	 * @example
	 * config.autoUpdateElement = true;
	 */
	autoUpdateElement : true,

	/**
	 * The base href URL used to resolve relative and absolute URLs in the
	 * editor content.
	 * @type String
	 * @default <code>''</code> (empty)
	 * @example
	 * config.baseHref = 'http://www.example.com/path/';
	 */
	baseHref : '',

	/**
	 * The CSS file(s) to be used to apply style to editor contents. It should
	 * reflect the CSS used in the final pages where the contents are to be
	 * used.
	 * @type String|Array
	 * @default <code>'<em>&lt;CKEditor folder&gt;</em>/contents.css'</code>
	 * @example
	 * config.contentsCss = '/css/mysitestyles.css';
	 * config.contentsCss = ['/css/mysitestyles.css', '/css/anotherfile.css'];
	 */
	contentsCss : CKEDITOR.basePath + 'contents.css',

	/**
	 * The writing direction of the language used to create the editor
	 * contents. Allowed values are:
	 * <ul>
	 *     <li><code>'ui'</code> &ndash; indicates that content direction will be the same as the user interface language direction;</li>
	 *     <li><code>'ltr'</code> &ndash; for Left-To-Right language (like English);</li>
	 *     <li><code>'rtl'</code> &ndash; for Right-To-Left languages (like Arabic).</li>
	 * </ul>
	 * @default <code>'ui'</code>
	 * @type String
	 * @example
	 * config.contentsLangDirection = 'rtl';
	 */
	contentsLangDirection : 'ui',

	/**
	 * Language code of  the writing language which is used to create the editor
	 * contents.
	 * @default Same value as editor UI language.
	 * @type String
	 * @example
	 * config.contentsLanguage = 'fr';
	 */
	contentsLanguage : '',

	/**
	 * The user interface language localization to use. If left empty, the editor
	 * will automatically be localized to the user language. If the user language is not supported,
	 * the language specified in the <code>{@link CKEDITOR.config.defaultLanguage}</code>
	 * configuration setting is used.
	 * @default <code>''</code> (empty)
	 * @type String
	 * @example
	 * // Load the German interface.
	 * config.language = 'de';
	 */
	language : '',

	/**
	 * The language to be used if the <code>{@link CKEDITOR.config.language}</code>
	 * setting is left empty and it is not possible to localize the editor to the user language.
	 * @default <code>'en'</code>
	 * @type String
	 * @example
	 * config.defaultLanguage = 'it';
	 */
	defaultLanguage : 'en',

	/**
	 * Sets the behavior of the <em>Enter</em> key. It also determines other behavior
	 * rules of the editor, like whether the <code>&lt;br&gt;</code> element is to be used
	 * as a paragraph separator when indenting text.
	 * The allowed values are the following constants that cause the behavior outlined below:
	 * <ul>
	 *     <li><code>{@link CKEDITOR.ENTER_P}</code> (1) &ndash; new <code>&lt;p&gt;</code> paragraphs are created;</li>
	 *     <li><code>{@link CKEDITOR.ENTER_BR}</code> (2) &ndash; lines are broken with <code>&lt;br&gt;</code> elements;</li>
	 *     <li><code>{@link CKEDITOR.ENTER_DIV}</code> (3) &ndash; new <code>&lt;div&gt;</code> blocks are created.</li>
	 * </ul>
	 * <strong>Note</strong>: It is recommended to use the
	 * <code>{@link CKEDITOR.ENTER_P}</code> setting because of its semantic value and
	 * correctness. The editor is optimized for this setting.
	 * @type Number
	 * @default <code>{@link CKEDITOR.ENTER_P}</code>
	 * @example
	 * // Not recommended.
	 * config.enterMode = CKEDITOR.ENTER_BR;
	 */
	enterMode : CKEDITOR.ENTER_P,

	/**
	 * Force the use of <code>{@link CKEDITOR.config.enterMode}</code> as line break regardless
	 * of the context. If, for example, <code>{@link CKEDITOR.config.enterMode}</code> is set
	 * to <code>{@link CKEDITOR.ENTER_P}</code>, pressing the <em>Enter</em> key inside a
	 * <code>&lt;div&gt;</code> element will create a new paragraph with <code>&lt;p&gt;</code>
	 * instead of a <code>&lt;div&gt;</code>.
	 * @since 3.2.1
	 * @type Boolean
	 * @default <code>false</code>
	 * @example
	 * // Not recommended.
	 * config.forceEnterMode = true;
	 */
	forceEnterMode : false,

	/**
	 * Similarly to the <code>{@link CKEDITOR.config.enterMode}</code> setting, it defines the behavior
	 * of the <em>Shift+Enter</em> key combination.
	 * The allowed values are the following constants the behavior outlined below:
	 * <ul>
	 *     <li><code>{@link CKEDITOR.ENTER_P}</code> (1) &ndash; new <code>&lt;p&gt;</code> paragraphs are created;</li>
	 *     <li><code>{@link CKEDITOR.ENTER_BR}</code> (2) &ndash; lines are broken with <code>&lt;br&gt;</code> elements;</li>
	 *     <li><code>{@link CKEDITOR.ENTER_DIV}</code> (3) &ndash; new <code>&lt;div&gt;</code> blocks are created.</li>
	 * </ul>
	 * @type Number
	 * @default <code>{@link CKEDITOR.ENTER_BR}</code>
	 * @example
	 * config.shiftEnterMode = CKEDITOR.ENTER_P;
	 */
	shiftEnterMode : CKEDITOR.ENTER_BR,

	/**
	 * A comma separated list of plugins that are not related to editor
	 * instances. Reserved for plugins that extend the core code only.<br /><br />
	 *
	 * There are no ways to override this setting except by editing the source
	 * code of CKEditor (<code>_source/core/config.js</code>).
	 * @type String
	 * @example
	 */
	corePlugins : '',

	/**
	 * Sets the <code>DOCTYPE</code> to be used when loading the editor content as HTML.
	 * @type String
	 * @default <code>'&lt;!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"&gt;'</code>
	 * @example
	 * // Set the DOCTYPE to the HTML 4 (Quirks) mode.
	 * config.docType = '&lt;!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"&gt;';
	 */
	docType : '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">',

	/**
	 * Sets the <code>id</code> attribute to be used on the <code>body</code> element
	 * of the editing area. This can be useful when you intend to reuse the original CSS
	 * file you are using on your live jeesite and want to assign the editor the same ID
	 * as the section that will include the contents. In this way ID-specific CSS rules will
	 * be enabled.
	 * @since 3.1
	 * @type String
	 * @default <code>''</code> (empty)
	 * @example
	 * config.bodyId = 'contents_id';
	 */
	bodyId : '',

	/**
	 * Sets the <code>class</code> attribute to be used on the <code>body</code> element
	 * of the editing area. This can be useful when you intend to reuse the original CSS
	 * file you are using on your live jeesite and want to assign the editor the same class
	 * as the section that will include the contents. In this way class-specific CSS rules will
	 * be enabled.
	 * @since 3.1
	 * @type String
	 * @default <code>''</code> (empty)
	 * @example
	 * config.bodyClass = 'contents';
	 */
	bodyClass : '',

	/**
	 * Indicates whether the contents to be edited are being input as a full
	 * HTML page. A full page includes the <code>&lt;html&gt;</code>,
	 * <code>&lt;head&gt;</code>, and <code>&lt;body&gt;</code> elements.
	 * The final output will also reflect this setting, including the
	 * <code>&lt;body&gt;</code> contents only if this setting is disabled.
	 * @since 3.1
	 * @type Boolean
	 * @default <code>false</code>
	 * @example
	 * config.fullPage = true;
	 */
	fullPage : false,

	/**
	 * The height of the editing area (that includes the editor content). This
	 * can be an integer, for pixel sizes, or any CSS-defined length unit.<br>
	 * <br>
	 * <strong>Note:</strong> Percent units (%) are not supported.
	 * @type Number|String
	 * @default <code>200</code>
	 * @example
	 * config.height = 500; // 500 pixels.
	 * @example
	 * config.height = '25em'; // CSS length.
	 * @example
	 * config.height = '300px'; // CSS length.
	 */
	height : 200,

	/**
	 * Comma separated list of plugins to be loaded and initialized for an editor
	 * instance. This setting should rarely be changed. It is recommended to use the
	 * <code>{@link CKEDITOR.config.extraPlugins}</code> and
	 * <code>{@link CKEDITOR.config.removePlugins}</code> for customization purposes instead.
	 * @type String
	 * @example
	 */
	plugins :
		'about,' +
		'a11yhelp,' +
		'basicstyles,' +
		'bidi,' +
		'blockquote,' +
		'button,' +
		'clipboard,' +
		'colorbutton,' +
		'colordialog,' +
		'contextmenu,' +
		'dialogadvtab,' +
		'div,' +
		'elementspath,' +
		'enterkey,' +
		'entities,' +
		'filebrowser,' +
		'find,' +
		'flash,' +
		'font,' +
		'format,' +
		'forms,' +
		'horizontalrule,' +
		'htmldataprocessor,' +
		'iframe,' +
		'image,' +
		'indent,' +
		'justify,' +
		'keystrokes,' +
		'link,' +
		'list,' +
		'liststyle,' +
		'maximize,' +
		'newpage,' +
		'pagebreak,' +
		'pastefromword,' +
		'pastetext,' +
		'popup,' +
		'preview,' +
		'print,' +
		'removeformat,' +
		'resize,' +
		'save,' +
		'scayt,' +
		'showblocks,' +
		'showborders,' +
		'smiley,' +
		'sourcearea,' +
		'specialchar,' +
		'stylescombo,' +
		'tab,' +
		'table,' +
		'tabletools,' +
		'templates,' +
		'toolbar,' +
		'undo,' +
		'wsc,' +
		'wysiwygarea',

	/**
	 * A list of additional plugins to be loaded. This setting makes it easier
	 * to add new plugins without having to touch and potentially break the
	 * <code>{@link CKEDITOR.config.plugins}</code> setting.
	 * @type String
	 * @example
	 * config.extraPlugins = 'myplugin,anotherplugin';
	 */
	extraPlugins : '',

	/**
	 * A list of plugins that must not be loaded. This setting makes it possible
	 * to avoid loading some plugins defined in the <code>{@link CKEDITOR.config.plugins}</code>
	 * setting, without having to touch it and potentially break it.
	 * @type String
	 * @example
	 * config.removePlugins = 'elementspath,save,font';
	 */
	removePlugins : '',

	/**
	 * List of regular expressions to be executed on input HTML,
	 * indicating HTML source code that when matched, must <strong>not</strong> be available in the WYSIWYG
	 * mode for editing.
	 * @type Array
	 * @default <code>[]</code> (empty array)
	 * @example
	 * config.protectedSource.push( /<\?[\s\S]*?\?>/g );   // PHP code
	 * config.protectedSource.push( /<%[\s\S]*?%>/g );   // ASP code
	 * config.protectedSource.push( /(<asp:[^\>]+>[\s|\S]*?<\/asp:[^\>]+>)|(<asp:[^\>]+\/>)/gi );   // ASP.Net code
	 */
	protectedSource : [],

	/**
	 * The editor <code>tabindex</code> value.
	 * @type Number
	 * @default <code>0</code> (zero)
	 * @example
	 * config.tabIndex = 1;
	 */
	tabIndex : 0,

	/**
	 * The theme to be used to build the user interface.
	 * @type String
	 * @default <code>'default'</code>
	 * @see CKEDITOR.config.skin
	 * @example
	 * config.theme = 'default';
	 */
	theme : 'default',

	/**
	 * The skin to load. It may be the name of the skin folder inside the
	 * editor installation path, or the name and the path separated by a comma.
	 * @type String
	 * @default <code>'default'</code>
	 * @example
	 * config.skin = 'v2';
	 * @example
	 * config.skin = 'myskin,/customstuff/myskin/';
	 */
	skin : 'kama',

	/**
	 * The editor UI outer width. This can be an integer, for pixel sizes, or
	 * any CSS-defined unit.<br>
	 * <br>
	 * Unlike the <code>{@link CKEDITOR.config.height}</code> setting, this
	 * one will set the outer width of the entire editor UI, not for the
	 * editing area only.
	 * @type String|Number
	 * @default <code>''</code> (empty)
	 * @example
	 * config.width = 850; // 850 pixels wide.
	 * @example
	 * config.width = '75%'; // CSS unit.
	 */
	width : '',

	/**
	 * The base Z-index for floating dialog windows and popups.
	 * @type Number
	 * @default <code>10000</code>
	 * @example
	 * config.baseFloatZIndex = 2000
	 */
	baseFloatZIndex : 10000
};

/**
 * Indicates that some of the editor features, like alignment and text
 * direction, should use the "computed value" of the feature to indicate its
 * on/off state instead of using the "real value".<br />
 * <br />
 * If enabled in a Left-To-Right written document, the "Left Justify"
 * alignment button will be shown as active, even if the alignment style is not
 * explicitly applied to the current paragraph in the editor.
 * @name CKEDITOR.config.useComputedState
 * @type Boolean
 * @default <code>true</code>
 * @since 3.4
 * @example
 * config.useComputedState = false;
 */

// PACKAGER_RENAME( CKEDITOR.config )
