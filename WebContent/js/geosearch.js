var cluster1;
var cluster2;
var map;

$(document)
		.ready(
				function() {

					cluster1 = new L.MarkerClusterGroup({
						animateAddingMarkers : true
					});
					cluster2 = new L.MarkerClusterGroup({
						animateAddingMarkers : true
					});

					var southWest = new L.latLng(-60.11605, -228.51562), northEast = L
							.latLng(80.01954, 228.86719), bounds = L
							.latLngBounds(southWest, northEast);

					var layer = L
							.tileLayer(
									'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
									{
										attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a>'
									});

					map = L.map('mapid', {
						minZoom : 2,
						maxZoom : 10,
						layers : [ layer ]
					});

					map.fitBounds(bounds);
					// map.fitWorld();
					map.setMaxBounds(map.getBounds());

					getAllData();

				});

function getAllData() {
	$('#lat').val("");
	$('#long').val("");
	map.removeLayer(cluster2);
	cluster1 = new L.MarkerClusterGroup({
		animateAddingMarkers : true
	});
	$.ajax({
		type : 'GET',
		// map.fitWorld();
		url : '/geospatialSearch/rest/geoSearch/getData',
		dataType : 'JSON',
		success : function(response) {

			if (response.length > 0) {

				for (var i = 0; i < response.length; i++) {

					cluster1.addLayer(new L.Marker(
							response[i].location.coordinates)
							.bindPopup(response[i].name + "\t"
									+ response[i].location.coordinates));
				}
			}
			map.addLayer(cluster1);
		},

		error : function(xhr, status, txt) {
			alert("unsuccess");
		}
	});
}

function geoSpatial() {
	map.removeLayer(cluster1);
	cluster2 = new L.MarkerClusterGroup({
		animateAddingMarkers : true
	});

	var url = '/geospatialSearch/rest/geoSearch/getLocationData';
	url = url + "/" + document.getElementById("lat").value + "/"
			+ document.getElementById("long").value;
	lati = document.getElementById("lat").value;
	longi = document.getElementById("long").value;

	$.ajax({
		type : 'GET',
		url : url,
		dataType : 'JSON',
		success : function(response) {

			if (response.length > 0) {

				for (var i = 0; i < response.length; i++) {

					cluster2.addLayer(new L.Marker(
							response[i].location.coordinates)
							.bindPopup(response[i].name + "\t"
									+ response[i].location.coordinates));
				}
			}
			map.addLayer(cluster2);
		},

		error : function(xhr, status, txt) {
			alert("unsuccess");
		}
	});
}