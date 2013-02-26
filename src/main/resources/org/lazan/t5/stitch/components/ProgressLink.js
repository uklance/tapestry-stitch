window.updateProgress = function(args) {
	var progressId = args.progressId;
	var progressPercentage = Math.floor(args.progress * 100);
	document.getElementById(progressId).style.width = progressPercentage + "%";
	
	if (args.progress < 1) {
		var nextUpdate = function() { 
			var zoneManager = Tapestry.findZoneManagerForZone(args.zoneId);
			zoneManager.updateFromURL(args.url); 
		};
		setTimeout(nextUpdate, 200);
	}
};