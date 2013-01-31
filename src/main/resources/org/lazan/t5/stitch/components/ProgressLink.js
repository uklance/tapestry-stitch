window.updateProgress = function(args) {
	var progressId = args.progressId;
	var progressPercentage = Math.floor(args.progress * 100);
	document.getElementById(progressId).innerHTML = progressPercentage + "%";
	
	if (args.progress < 1) {
		var closure = function() { 
			var zoneManager = Tapestry.findZoneManagerForZone(args.zoneId);
			zoneManager.updateFromURL(args.link); 
		};
		setTimeout(closure, 200);
	}
};