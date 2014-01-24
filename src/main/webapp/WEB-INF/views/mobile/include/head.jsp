<%@ page contentType="text/html;charset=UTF-8" %>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"  href="${ctxStatic}/jquery.mobile/jquery.mobile-1.4.0.min.css">
<link rel="shortcut icon" href="${ctxStatic}/favicon.ico">
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$( document ).on( "mobileinit", function(){
		$.mobile.ajaxEnabled = false;
	});
</script>
<script src="${ctxStatic}/jquery.mobile/jquery.mobile-1.4.0.min.js"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.1/jquery.validate.method.min.js" type="text/javascript"></script>
<style>
	label.error {
	    color: red;
	    font-size: 16px;
	    font-weight: normal;
	    line-height: 1.4;
	    margin-top: 0.5em;
	    width: 100%;
	    float: none;
	}
	@media screen and (orientation: portrait){
	    label.error {
	        margin-left: 0;
	        display: block;
	    }
	}
	@media screen and (orientation: landscape){
	    label.error {
	        display: inline-block;
	        margin-left: 22%;
	    }
	}
</style>