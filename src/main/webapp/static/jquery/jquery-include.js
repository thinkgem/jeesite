(function(c){
	c.extend({
	    include: function(id, path, file){
	    	if (document.getElementById(id)==null){
		        var files = typeof file == "string" ? [file] : file;
		        for (var i = 0; i < files.length; i++){
		            var name = files[i].replace(/^\s|\s$/g, "");
		            var att = name.split('.');
		            var ext = att[att.length - 1].toLowerCase();
		            var isCSS = ext == "css";
		            var tag = isCSS ? "link" : "script";
		            var attr = isCSS ? " type='text/css' rel='stylesheet' " : " type='text/javascript' ";
		            var link = (isCSS ? "href" : "src") + "='" + path + name + "'";
		            document.write("<" + tag + (i==0?" id="+id:"") + attr + link + "></" + tag + ">");
		        }
	    	}
	    }
	});
})(jQuery);