T5.extendInitializers({
	onEvent: function (spec) {
		var element = $(spec.id).observe(spec.event, function () {
			var params = {};
			if (spec.fieldIds) {
				for (var i = 0; i < spec.fieldIds.length; ++i) {
					var fieldId = spec.fieldIds[i];
					var paramName = "onEvent." + fieldId;
					var paramValue = $(fieldId).getValue();
					params[paramName] = paramValue;
				}
			}
			var zoneManager = Tapestry.findZoneManagerForZone(spec.zone);
			zoneManager.updateFromURL(spec.url, params);
		});
	}
});