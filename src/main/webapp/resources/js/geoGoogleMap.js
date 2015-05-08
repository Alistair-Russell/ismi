

function displayGeoname(geoname){
	
	//var rectangle;
    
    var myOptions = {
      zoom: 6,
      //center: coachella,
      mapTypeId: google.maps.MapTypeId.TERRAIN
    };
    //mapTypeId: google.maps.MapTypeId.SATELLITE
    //mapTypeId: google.maps.MapTypeId.TERRAIN

    var map = new google.maps.Map(document.getElementById("geoMap"), myOptions);
    var canvas = document.getElementById("geoMap");
	canvas.style.width= '700px';
	canvas.style.height= '450px';
	
	if(geoname){
		var latlng = new google.maps.LatLng(geoname.lat, geoname.lng);
		map.setCenter(latlng);
		map.setZoom(8);
		createMarker(map, latlng, geoname);	
	}
}

function createMarker(map, point, geoname) {
	
	var infowindow = new google.maps.InfoWindow();
	
    var pinImage = new google.maps.MarkerImage(
    	"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2%7CFE7569",
        new google.maps.Size(21, 34),
        new google.maps.Point(0,0),
        new google.maps.Point(10, 34));
    
	var marker = new google.maps.Marker({
        position: point,
        map: map,
        icon: pinImage
	});
	
	google.maps.event.addListener(marker, 'click', 
		function(){
			var infoHtml = "";
			if(geoname.geonameId){
				infoHtml+= "Id: <b>" + geoname.geonameId + "</b><br>";
			}
			if(geoname.name){
				infoHtml+= "Name: <b>" + geoname.name + "</b><br>";
			}
			if(geoname.toponymName){
				infoHtml+= "Toponym name: <b>" + geoname.toponymName + "</b><br>";
			}
			if(geoname.countryName){
				infoHtml+= "Country: <b>" + geoname.countryName + "</b><br>";
			}
			if(geoname.class.description){
				infoHtml+= "Description: <b>" + geoname.class.description + "</b><br>";
			}			
			infowindow.setContent(infoHtml);
			infowindow.open(map, marker);			
		}	
	);

	//return marker;
}
/*
function getGeonameById(id){
	var url = 
		"https://openmind-ismi-dev.mpiwg-berlin.mpg.de/geonames/service?" +
		"method=getGeoname&mode=json&geonameId="+ 
		id;
	
	var jsonResponse = httpGet(url);
	return jsonResponse;
}

function httpGet(theUrl){
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    
    xmlHttp.addEventListener("progress", updateProgress, false);
    xmlHttp.addEventListener("load", transferComplete, false);
    xmlHttp.addEventListener("error", transferFailed, false);
    xmlHttp.addEventListener("abort", transferCanceled, false);
    
    
    xmlHttp.send( );
    var txt = xmlHttp.responseText;
    return txt;
}

//progress on transfers from the server to the client (downloads)
function updateProgress(evt) {
  if (evt.lengthComputable) {
    var percentComplete = evt.loaded / evt.total;
    
  } else {
    // Unable to compute progress information since the total size is unknown
  }
}
 
function transferComplete(evt) {
  alert("The transfer is complete.");
}
 
function transferFailed(evt) {
  alert("An error occurred while transferring the file.");
}
 
function transferCanceled(evt) {
  alert("The transfer has been canceled by the user.");
}
*/