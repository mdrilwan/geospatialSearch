var map;
var cluster1;
var cluster2;

$(document)
		.ready(
				function() {

					cluster1 = new L.MarkerClusterGroup({
						animateAddingMarkers : true
					});
					cluster2 = new L.MarkerClusterGroup({
						animateAddingMarkers : true
					});

					var bounds = L.latLngBounds([ 14.43468021529728,
							-128.75976562500003 ], [ 56.897003921272606,
							-32.87109375000001 ]);

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

	map.removeLayer(cluster2);
	cluster1 = new L.MarkerClusterGroup({
		animateAddingMarkers : true
	});

	$.ajax({
		type : 'GET',
		url : '/geospatialSearch/rest/geoSearch/getData',
		dataType : 'JSON',
		success : function(response) {

			if (response.length > 0) {

				for (var i = 0; i < response.length; i++) {

					var latLon = [ response[i].location.coordinates[1],
							response[i].location.coordinates[0] ];
					cluster1.addLayer(new L.Marker(latLon)
							.bindPopup(response[i].name
									+ "<br/><b>Address</b> : "
									+ response[i].address
									+ "<br/><b>City</b> : " + response[i].city
									+ "<br/><b>State</b> : "
									+ response[i].state + "<br/><b>Zip</b> : "
									+ response[i].zip));
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

	var url = '/geospatialSearch/rest/geoSearch/2d/near';
	url = url + "/" + document.getElementById("name").value + "/"
			+ document.getElementById("dist").value;

	$.ajax({
		type : 'GET',
		url : url,
		dataType : 'JSON',
		success : function(response) {

			if (response.length > 0) {

				for (var i = 0; i < response.length; i++) {

					var latLon = [ response[i].location.coordinates[1],
							response[i].location.coordinates[0] ];
					cluster2.addLayer(new L.Marker(latLon)
							.bindPopup(response[i].name
									+ "<br/><b>Address</b> : "
									+ response[i].address
									+ "<br/><b>City</b> : " + response[i].city
									+ "<br/><b>State</b> : "
									+ response[i].state + "<br/><b>Zip</b> : "
									+ response[i].zip));
				}
			}
			map.addLayer(cluster2);
		},

		error : function(xhr, status, txt) {
			alert("unsuccess");
		}
	});
}