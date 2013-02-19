/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @fileOverview API initialization code.
 */

(function()
{
	// Disable HC detaction in WebKit. (#5429)
	if ( CKEDITOR.env.webkit )
	{
		CKEDITOR.env.hc = false;
		return;
	}

	// Check whether high contrast is active by creating a colored border.
	var hcDetect = CKEDITOR.dom.element.createFromHtml(
		'<div style="width:0px;height:0px;position:absolute;left:-10000px;' +
			'border: 1px solid;border-color: red blue;"></div>', CKEDITOR.document );

	hcDetect.appendTo( CKEDITOR.document.getHead() );

	// Update CKEDITOR.env.
	// Catch exception needed sometimes for FF. (#4230)
	try
	{
		CKEDITOR.env.hc = hcDetect.getComputedStyle( 'border-top-color' ) == hcDetect.getComputedStyle( 'border-right-color' );
	}
	catch (e)
	{
		CKEDITOR.env.hc = false;
	}

	if ( CKEDITOR.env.hc )
		CKEDITOR.env.cssClass += ' cke_hc';

	hcDetect.remove();
})();

// Load core plugins.
CKEDITOR.plugins.load( CKEDITOR.config.corePlugins.split( ',' ), function()
	{
		CKEDITOR.status = 'loaded';
		CKEDITOR.fire( 'loaded' );

		// Process all instances created by the "basic" implementation.
		var pending = CKEDITOR._.pending;
		if ( pending )
		{
			delete CKEDITOR._.pending;

			for ( var i = 0 ; i < pending.length ; i++ )
				CKEDITOR.add( pending[ i ] );
		}
	});

// Needed for IE6 to not request image (HTTP 200 or 304) for every CSS background. (#6187)
if ( CKEDITOR.env.ie )
{
	// Remove IE mouse flickering on IE6 because of background images.
	try
	{
		document.execCommand( 'BackgroundImageCache', false, true );
	}
	catch (e)
	{
		// We have been reported about loading problems caused by the above
		// line. For safety, let's just ignore errors.
	}
}

/**
 * Indicates that CKEditor is running on a High Contrast environment.
 * @name CKEDITOR.env.hc
 * @example
 * if ( CKEDITOR.env.hc )
 *     alert( 'You're running on High Contrast mode. The editor interface will get adapted to provide you a better experience.' );
 */

/**
 * Fired when a CKEDITOR core object is fully loaded and ready for interaction.
 * @name CKEDITOR#loaded
 * @event
 */
