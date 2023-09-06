/** google_map js **/
function myMap() {
    var mapProp = {
        center: new google.maps.LatLng(37.4502889, 126.7029031),
        zoom: 18,
    };
    var map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    var position = {lat: 37.4502889, lng: 126.7029031};
    
    var marker = new google.maps.Marker({
    position: position,
    map: map,
    label: "ostb movie",
    icon: {
        url: "http://maps.google.com/mapfiles/ms/icons/red-dot.png"
    }
  });
  
  const contentString =
    '<div id="content">' +
    '<div id="siteNotice">' +
    "</div>" +
    '<div id="bodyContent">' +
    '<p style="margin: 0;"><a href="https://www.google.co.kr/maps/dir//%EC%9D%B8%EC%B2%9C%EA%B4%91%EC%97%AD%EC%8B%9C+%EB%82%A8%EB%8F%99%EA%B5%AC+%EC%9D%B8%EC%A3%BC%EB%8C%80%EB%A1%9C+%EC%97%94%ED%83%80%EC%8A%A4%EB%B9%8C%EB%94%A9+%EC%9D%B4%EC%A0%A0%EC%95%84%EC%B9%B4%EB%8D%B0%EB%AF%B8%EC%BB%B4%ED%93%A8%ED%84%B0%ED%95%99%EC%9B%90+%EC%9D%B8%EC%B2%9C%EC%BA%A0%ED%8D%BC%EC%8A%A4/data=!4m8!4m7!1m0!1m5!1m1!1s0x357b7bbc97e598d7:0x800ace7d918bc5ba!2m2!1d126.7029167!2d37.4503144?hl=ko&entry=ttu" target="_blank">' +
    "인천광역시 남동구 인주대로 593 엔타스빌딩 12층</a></p>" +
    "</div>" +
    "</div>";

  const infowindow = new google.maps.InfoWindow({
    content: contentString,
    ariaLabel: "Uluru",
  });
  
  marker.addListener("click", () => {
    infowindow.open({
      anchor: marker,
      map,
    });
  });
}

