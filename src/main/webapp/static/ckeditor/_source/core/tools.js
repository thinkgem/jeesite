/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview Defines the {@link CKEDITOR.tools} object, which contains
 *		utility functions.
 */

(function()
{
	var functions = [];

	CKEDITOR.on( 'reset', function()
		{
			functions = [];
		});

	/**
	 * Utility functions.
	 * @namespace
	 * @example
	 */
	CKEDITOR.tools =
	{
		/**
		 * Compare the elements of two arrays.
		 * @param {Array} arrayA An array to be compared.
		 * @param {Array} arrayB The other array to be compared.
		 * @returns {Boolean} "true" is the arrays have the same lenght and
		 *		their elements match.
		 * @example
		 * var a = [ 1, 'a', 3 ];
		 * var b = [ 1, 3, 'a' ];
		 * var c = [ 1, 'a', 3 ];
		 * var d = [ 1, 'a', 3, 4 ];
		 *
		 * alert( CKEDITOR.tools.arrayCompare( a, b ) );  // false
		 * alert( CKEDITOR.tools.arrayCompare( a, c ) );  // true
		 * alert( CKEDITOR.tools.arrayCompare( a, d ) );  // false
		 */
		arrayCompare : function( arrayA, arrayB )
		{
			if ( !arrayA && !arrayB )
				return true;

			if ( !arrayA || !arrayB || arrayA.length != arrayB.length )
				return false;

			for ( var i = 0 ; i < arrayA.length ; i++ )
			{
				if ( arrayA[ i ] != arrayB[ i ] )
					return false;
			}

			return true;
		},

		/**
		 * Creates a deep copy of an object.
		 * Attention: there is no support for recursive references.
		 * @param {Object} object The object to be cloned.
		 * @returns {Object} The object clone.
		 * @example
		 * var obj =
		 *     {
		 *         name : 'John',
		 *         cars :
		 *             {
		 *                 Mercedes : { color : 'blue' },
		 *                 Porsche : { color : 'red' }
		 *             }
		 *     };
		 * var clone = CKEDITOR.tools.clone( obj );
		 * clone.name = 'Paul';
		 * clone.cars.Porsche.color = 'silver';
		 * alert( obj.name );	// John
		 * alert( clone.name );	// Paul
		 * alert( obj.cars.Porsche.color );	// red
		 * alert( clone.cars.Porsche.color );	// silver
		 */
		clone : function( obj )
		{
			var clone;

			// Array.
			if ( obj && ( obj instanceof Array ) )
			{
				clone = [];

				for ( var i = 0 ; i < obj.length ; i++ )
					clone[ i ] = this.clone( obj[ i ] );

				return clone;
			}

			// "Static" types.
			if ( obj === null
				|| ( typeof( obj ) != 'object' )
				|| ( obj instanceof String )
				|| ( obj instanceof Number )
				|| ( obj instanceof Boolean )
				|| ( obj instanceof Date )
				|| ( obj instanceof RegExp) )
			{
				return obj;
			}

			// Objects.
			clone = new obj.constructor();

			for ( var propertyName in obj )
			{
				var property = obj[ propertyName ];
				clone[ propertyName ] = this.clone( property );
			}

			return clone;
		},

		/**
		 * Turn the first letter of string to upper-case.
		 * @param {String} str
		 */
		capitalize: function( str )
		{
			return str.charAt( 0 ).toUpperCase() + str.substring( 1 ).toLowerCase();
		},

		/**
		 * Copy the properties from one object to another. By default, properties
		 * already present in the target object <strong>are not</strong> overwritten.
		 * @param {Object} target The object to be extended.
		 * @param {Object} source[,souce(n)] The objects from which copy
		 *		properties. Any number of objects can be passed to this function.
		 * @param {Boolean} [overwrite] If 'true' is specified it indicates that
		 *            properties already present in the target object could be
		 *            overwritten by subsequent objects.
		 * @param {Object} [properties] Only properties within the specified names
		 *            list will be received from the source object.
		 * @returns {Object} the extended object (target).
		 * @example
		 * // Create the sample object.
		 * var myObject =
		 * {
		 *     prop1 : true
		 * };
		 *
		 * // Extend the above object with two properties.
		 * CKEDITOR.tools.extend( myObject,
		 *     {
		 *         prop2 : true,
		 *         prop3 : true
		 *     } );
		 *
		 * // Alert "prop1", "prop2" and "prop3".
		 * for ( var p in myObject )
		 *     alert( p );
		 */
		extend : function( target )
		{
			var argsLength = arguments.length,
				overwrite, propertiesList;

			if ( typeof ( overwrite = arguments[ argsLength - 1 ] ) == 'boolean')
				argsLength--;
			else if ( typeof ( overwrite = arguments[ argsLength - 2 ] ) == 'boolean' )
			{
				propertiesList = arguments [ argsLength -1 ];
				argsLength-=2;
			}
			for ( var i = 1 ; i < argsLength ; i++ )
			{
				var source = arguments[ i ];
				for ( var propertyName in source )
				{
					// Only copy existed fields if in overwrite mode.
					if ( overwrite === true || target[ propertyName ] == undefined )
					{
						// Only copy  specified fields if list is provided.
						if ( !propertiesList || ( propertyName in propertiesList ) )
							target[ propertyName ] = source[ propertyName ];

					}
				}
			}

			return target;
		},

		/**
		 * Creates an object which is an instance of a class which prototype is a
		 * predefined object. All properties defined in the source object are
		 * automatically inherited by the resulting object, including future
		 * changes to it.
		 * @param {Object} source The source object to be used as the prototype for
		 *		the final object.
		 * @returns {Object} The resulting copy.
		 */
		prototypedCopy : function( source )
		{
			var copy = function()
			{};
			copy.prototype = source;
			return new copy();
		},

		/**
		 * Checks if an object is an Array.
		 * @param {Object} object The object to be checked.
		 * @type Boolean
		 * @returns <i>true</i> if the object is an Array, otherwise <i>false</i>.
		 * @example
		 * alert( CKEDITOR.tools.isArray( [] ) );      // "true"
		 * alert( CKEDITOR.tools.isArray( 'Test' ) );  // "false"
		 */
		isArray : function( object )
		{
			return ( !!object && object instanceof Array );
		},

		/**
		 * Whether the object contains no properties of it's own.
 		 * @param object
		 */
		isEmpty : function ( object )
		{
			for ( var i in object )
			{
				if ( object.hasOwnProperty( i ) )
					return false;
			}
			return true;
		},

		/**
		 * Transforms a CSS property name to its relative DOM style name.
		 * @param {String} cssName The CSS property name.
		 * @returns {String} The transformed name.
		 * @example
		 * alert( CKEDITOR.tools.cssStyleToDomStyle( 'background-color' ) );  // "backgroundColor"
		 * alert( CKEDITOR.tools.cssStyleToDomStyle( 'float' ) );             // "cssFloat"
		 */
		cssStyleToDomStyle : ( function()
		{
			var test = document.createElement( 'div' ).style;

			var cssFloat = ( typeof test.cssFloat != 'undefined' ) ? 'cssFloat'
				: ( typeof test.styleFloat != 'undefined' ) ? 'styleFloat'
				: 'float';

			return function( cssName )
			{
				if ( cssName == 'float' )
					return cssFloat;
				else
				{
					return cssName.replace( /-./g, function( match )
						{
							return match.substr( 1 ).toUpperCase();
						});
				}
			};
		} )(),

		/**
		 * Build the HTML snippet of a set of &lt;style>/&lt;link>.
		 * @param css {String|Array} Each of which are url (absolute) of a CSS file or
		 * a trunk of style text.
		 */
		buildStyleHtml : function ( css )
		{
			css = [].concat( css );
			var item, retval = [];
			for ( var i = 0; i < css.length; i++ )
			{
				item = css[ i ];
				// Is CSS style text ?
				if ( /@import|[{}]/.test(item) )
					retval.push('<style>' + item + '</style>');
				else
					retval.push('<link type="text/css" rel=stylesheet href="' + item + '">');
			}
			return retval.join( '' );
		},

		/**
		 * Replace special HTML characters in a string with their relative HTML
		 * entity values.
		 * @param {String} text The string to be encoded.
		 * @returns {String} The encode string.
		 * @example
		 * alert( CKEDITOR.tools.htmlEncode( 'A > B & C < D' ) );  // "A &amp;gt; B &amp;amp; C &amp;lt; D"
		 */
		htmlEncode : function( text )
		{
			var standard = function( text )
			{
				var span = new CKEDITOR.dom.element( 'span' );
				span.setText( text );
				return span.getHtml();
			};

			var fix1 = ( standard( '\n' ).toLowerCase() == '<br>' ) ?
				function( text )
				{
					// #3874 IE and Safari encode line-break into <br>
					return standard( text ).replace( /<br>/gi, '\n' );
				} :
				standard;

			var fix2 = ( standard( '>' ) == '>' ) ?
				function( text )
				{
					// WebKit does't encode the ">" character, which makes sense, but
					// it's different than other browsers.
					return fix1( text ).replace( />/g, '&gt;' );
				} :
				fix1;

			var fix3 = ( standard( '  ' ) == '&nbsp; ' ) ?
				function( text )
				{
					// #3785 IE8 changes spaces (>= 2) to &nbsp;
					return fix2( text ).replace( /&nbsp;/g, ' ' );
				} :
				fix2;

			this.htmlEncode = fix3;

			return this.htmlEncode( text );
		},

		/**
		 * Replace special HTML characters in HTMLElement's attribute with their relative HTML entity values.
		 * @param {String} The attribute's value to be encoded.
		 * @returns {String} The encode value.
		 * @example
		 * element.setAttribute( 'title', '<a " b >' );
		 * alert( CKEDITOR.tools.htmlEncodeAttr( element.getAttribute( 'title' ) );  // "&gt;a &quot; b &lt;"
		 */
		htmlEncodeAttr : function( text )
		{
			return text.replace( /"/g, '&quot;' ).replace( /</g, '&lt;' ).replace( />/g, '&gt;' );
		},

		/**
		 * Gets a unique number for this CKEDITOR execution session. It returns
		 * progressive numbers starting at 1.
		 * @function
		 * @returns {Number} A unique number.
		 * @example
		 * alert( CKEDITOR.tools.<b>getNextNumber()</b> );  // "1" (e.g.)
		 * alert( CKEDITOR.tools.<b>getNextNumber()</b> );  // "2"
		 */
		getNextNumber : (function()
		{
			var last = 0;
			return function()
			{
				return ++last;
			};
		})(),

		/**
		 * Gets a unique ID for CKEditor's interface elements. It returns a
		 * string with the "cke_" prefix and a progressive number.
		 * @function
		 * @returns {String} A unique ID.
		 * @example
		 * alert( CKEDITOR.tools.<b>getNextId()</b> );  // "cke_1" (e.g.)
		 * alert( CKEDITOR.tools.<b>getNextId()</b> );  // "cke_2"
		 */
		getNextId : function()
		{
			return 'cke_' + this.getNextNumber();
		},

		/**
		 * Creates a function override.
		 * @param {Function} originalFunction The function to be overridden.
		 * @param {Function} functionBuilder A function that returns the new
		 *		function. The original function reference will be passed to this
		 *		function.
		 * @returns {Function} The new function.
		 * @example
		 * var example =
		 * {
		 *     myFunction : function( name )
		 *     {
		 *         alert( 'Name: ' + name );
		 *     }
		 * };
		 *
		 * example.myFunction = CKEDITOR.tools.override( example.myFunction, function( myFunctionOriginal )
		 *     {
		 *         return function( name )
		 *             {
		 *                 alert( 'Override Name: ' + name );
		 *                 myFunctionOriginal.call( this, name );
		 *             };
		 *     });
		 */
		override : function( originalFunction, functionBuilder )
		{
			return functionBuilder( originalFunction );
		},

		/**
		 * Executes a function after specified delay.
		 * @param {Function} func The function to be executed.
		 * @param {Number} [milliseconds] The amount of time (millisecods) to wait
		 *		to fire the function execution. Defaults to zero.
		 * @param {Object} [scope] The object to hold the function execution scope
		 *		(the "this" object). By default the "window" object.
		 * @param {Object|Array} [args] A single object, or an array of objects, to
		 *		pass as arguments to the function.
		 * @param {Object} [ownerWindow] The window that will be used to set the
		 *		timeout. By default the current "window".
		 * @returns {Object} A value that can be used to cancel the function execution.
		 * @example
		 * CKEDITOR.tools.<b>setTimeout(
		 *     function()
		 *     {
		 *         alert( 'Executed after 2 seconds' );
		 *     },
		 *     2000 )</b>;
		 */
		setTimeout : function( func, milliseconds, scope, args, ownerWindow )
		{
			if ( !ownerWindow )
				ownerWindow = window;

			if ( !scope )
				scope = ownerWindow;

			return ownerWindow.setTimeout(
				function()
				{
					if ( args )
						func.apply( scope, [].concat( args ) ) ;
					else
						func.apply( scope ) ;
				},
				milliseconds || 0 );
		},

		/**
		 * Remove spaces from the start and the end of a string. The following
		 * characters are removed: space, tab, line break, line feed.
		 * @function
		 * @param {String} str The text from which remove the spaces.
		 * @returns {String} The modified string without the boundary spaces.
		 * @example
		 * alert( CKEDITOR.tools.trim( '  example ' );  // "example"
		 */
		trim : (function()
		{
			// We are not using \s because we don't want "non-breaking spaces" to be caught.
			var trimRegex = /(?:^[ \t\n\r]+)|(?:[ \t\n\r]+$)/g;
			return function( str )
			{
				return str.replace( trimRegex, '' ) ;
			};
		})(),

		/**
		 * Remove spaces from the start (left) of a string. The following
		 * characters are removed: space, tab, line break, line feed.
		 * @function
		 * @param {String} str The text from which remove the spaces.
		 * @returns {String} The modified string excluding the removed spaces.
		 * @example
		 * alert( CKEDITOR.tools.ltrim( '  example ' );  // "example "
		 */
		ltrim : (function()
		{
			// We are not using \s because we don't want "non-breaking spaces" to be caught.
			var trimRegex = /^[ \t\n\r]+/g;
			return function( str )
			{
				return str.replace( trimRegex, '' ) ;
			};
		})(),

		/**
		 * Remove spaces from the end (right) of a string. The following
		 * characters are removed: space, tab, line break, line feed.
		 * @function
		 * @param {String} str The text from which remove the spaces.
		 * @returns {String} The modified string excluding the removed spaces.
		 * @example
		 * alert( CKEDITOR.tools.ltrim( '  example ' );  // "  example"
		 */
		rtrim : (function()
		{
			// We are not using \s because we don't want "non-breaking spaces" to be caught.
			var trimRegex = /[ \t\n\r]+$/g;
			return function( str )
			{
				return str.replace( trimRegex, '' ) ;
			};
		})(),

		/**
		 * Returns the index of an element in an array.
		 * @param {Array} array The array to be searched.
		 * @param {Object} entry The element to be found.
		 * @returns {Number} The (zero based) index of the first entry that matches
		 *		the entry, or -1 if not found.
		 * @example
		 * var letters = [ 'a', 'b', 0, 'c', false ];
		 * alert( CKEDITOR.tools.indexOf( letters, '0' ) );  "-1" because 0 !== '0'
		 * alert( CKEDITOR.tools.indexOf( letters, false ) );  "4" because 0 !== false
		 */
		indexOf :
			// #2514: We should try to use Array.indexOf if it does exist.
			( Array.prototype.indexOf ) ?
				function( array, entry )
					{
						return array.indexOf( entry );
					}
			:
				function( array, entry )
				{
					for ( var i = 0, len = array.length ; i < len ; i++ )
					{
						if ( array[ i ] === entry )
							return i;
					}
					return -1;
				},

		/**
		 * Creates a function that will always execute in the context of a
		 * specified object.
		 * @param {Function} func The function to be executed.
		 * @param {Object} obj The object to which bind the execution context.
		 * @returns {Function} The function that can be used to execute the
		 *		"func" function in the context of "obj".
		 * @example
		 * var obj = { text : 'My Object' };
		 *
		 * function alertText()
		 * {
		 *     alert( this.text );
		 * }
		 *
		 * var newFunc = <b>CKEDITOR.tools.bind( alertText, obj )</b>;
		 * newFunc();  // Alerts "My Object".
		 */
		bind : function( func, obj )
		{
			return function() { return func.apply( obj, arguments ); };
		},

		/**
		 * Class creation based on prototype inheritance, with supports of the
		 * following features:
		 * <ul>
		 * <li> Static fields </li>
		 * <li> Private fields </li>
		 * <li> Public (prototype) fields </li>
		 * <li> Chainable base class constructor </li>
		 * </ul>
		 * @param {Object} definition The class definition object.
		 * @returns {Function} A class-like JavaScript function.
		 */
		createClass : function( definition )
		{
			var $ = definition.$,
				baseClass = definition.base,
				privates = definition.privates || definition._,
				proto = definition.proto,
				statics = definition.statics;

			if ( privates )
			{
				var originalConstructor = $;
				$ = function()
				{
					// Create (and get) the private namespace.
					var _ = this._ || ( this._ = {} );

					// Make some magic so "this" will refer to the main
					// instance when coding private functions.
					for ( var privateName in privates )
					{
						var priv = privates[ privateName ];

						_[ privateName ] =
							( typeof priv == 'function' ) ? CKEDITOR.tools.bind( priv, this ) : priv;
					}

					originalConstructor.apply( this, arguments );
				};
			}

			if ( baseClass )
			{
				$.prototype = this.prototypedCopy( baseClass.prototype );
				$.prototype.constructor = $;
				$.prototype.base = function()
				{
					this.base = baseClass.prototype.base;
					baseClass.apply( this, arguments );
					this.base = arguments.callee;
				};
			}

			if ( proto )
				this.extend( $.prototype, proto, true );

			if ( statics )
				this.extend( $, statics, true );

			return $;
		},

		/**
		 * Creates a function reference that can be called later using
		 * CKEDITOR.tools.callFunction. This approach is specially useful to
		 * make DOM attribute function calls to JavaScript defined functions.
		 * @param {Function} fn The function to be executed on call.
		 * @param {Object} [scope] The object to have the context on "fn" execution.
		 * @returns {Number} A unique reference to be used in conjuction with
		 *		CKEDITOR.tools.callFunction.
		 * @example
		 * var ref = <b>CKEDITOR.tools.addFunction</b>(
		 *     function()
		 *     {
		 *         alert( 'Hello!');
		 *     });
		 * CKEDITOR.tools.callFunction( ref );  // Hello!
		 */
		addFunction : function( fn, scope )
		{
			return functions.push( function()
				{
					return fn.apply( scope || this, arguments );
				}) - 1;
		},

		/**
		 * Removes the function reference created with {@see CKEDITOR.tools.addFunction}.
		 * @param {Number} ref The function reference created with
		 *		CKEDITOR.tools.addFunction.
		 */
		removeFunction : function( ref )
		{
			functions[ ref ] = null;
		},

		/**
		 * Executes a function based on the reference created with
		 * CKEDITOR.tools.addFunction.
		 * @param {Number} ref The function reference created with
		 *		CKEDITOR.tools.addFunction.
		 * @param {[Any,[Any,...]} params Any number of parameters to be passed
		 *		to the executed function.
		 * @returns {Any} The return value of the function.
		 * @example
		 * var ref = CKEDITOR.tools.addFunction(
		 *     function()
		 *     {
		 *         alert( 'Hello!');
		 *     });
		 * <b>CKEDITOR.tools.callFunction( ref )</b>;  // Hello!
		 */
		callFunction : function( ref )
		{
			var fn = functions[ ref ];
			return fn && fn.apply( window, Array.prototype.slice.call( arguments, 1 ) );
		},

		/**
		 * Append the 'px' length unit to the size if it's missing.
		 * @param length
		 */
		cssLength : (function()
		{
			return function( length )
			{
				return length + ( !length || isNaN( Number( length ) ) ? '' : 'px' );
			};
		})(),

		/**
		 * Convert the specified CSS length value to the calculated pixel length inside this page.
		 * <strong>Note:</strong> Percentage based value is left intact.
		 * @param {String} cssLength CSS length value.
		 */
		convertToPx : ( function ()
			{
				var calculator;

				return function( cssLength )
				{
					if ( !calculator )
					{
						calculator = CKEDITOR.dom.element.createFromHtml(
								'<div style="position:absolute;left:-9999px;' +
								'top:-9999px;margin:0px;padding:0px;border:0px;"' +
								'></div>', CKEDITOR.document );
						CKEDITOR.document.getBody().append( calculator );
					}

					if ( !(/%$/).test( cssLength ) )
					{
						calculator.setStyle( 'width', cssLength );
						return calculator.$.clientWidth;
					}

					return cssLength;
				};
			} )(),

		/**
		 * String specified by {@param str} repeats {@param times} times.
		 * @param str
		 * @param times
		 */
		repeat : function( str, times )
		{
			return new Array( times + 1 ).join( str );
		},

		/**
		 * Return the first successfully executed function's return value that
		 * doesn't throw any exception.
		 */
		tryThese : function()
		{
			var returnValue;
			for ( var i = 0, length = arguments.length; i < length; i++ )
			{
				var lambda = arguments[i];
				try
				{
					returnValue = lambda();
					break;
				}
				catch (e) {}
			}
			return returnValue;
		},

		/**
		 * Generate a combined key from a series of params.
		 * @param {String} subKey One or more string used as sub keys.
		 * @example
		 * var key = CKEDITOR.tools.genKey( 'key1', 'key2', 'key3' );
		 * alert( key );		// "key1-key2-key3".
		 */
		genKey : function()
		{
			return Array.prototype.slice.call( arguments ).join( '-' );
		}
	};
})();

// PACKAGER_RENAME( CKEDITOR.tools )
