(function(){
	function BarcodeScanner() {};

	BarcodeScanner.prototype.scan = function(mode,win,err){
		cordova.exec(win, err, "BarcodeScanner", "scan", [mode]);
	};


	if(!window.plugins) {
	    window.plugins = {};
	}
	if (!window.plugins.barcodeScanner) {
	    window.plugins.barcodeScanner = new BarcodeScanner();
	}	
})();
