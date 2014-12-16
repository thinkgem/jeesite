<%@page import="java.io.File"%>
<%@page import="com.thinkgem.jeesite.common.utils.ImageGeo"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<link href="mapfiles/css/default.css" rel="stylesheet" type="text/css" />
	<title>地图显示 - 读取图片GPS信息</title><!-- 
	<script type="text/javascript" src="mapapi3.8.6.js"></script> -->
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script type="text/javascript">
		<%
			ImageGeo geo = new ImageGeo(request.getRealPath("static/map/IMAG0068.jpg"));
		%>

		// 地图服务地址
		var strURL = "http://192.168.11.233:8780/maptile/googlemaps/roadmap/"
	
		function LocalMapType() {}
		LocalMapType.prototype.tileSize = new google.maps.Size(256, 256);
		LocalMapType.prototype.maxZoom = 16;   //地图显示最大级别
		LocalMapType.prototype.minZoom = 4;    //地图显示最小级别
		LocalMapType.prototype.name = "本地";
		LocalMapType.prototype.alt = "显示本地地图数据";
		LocalMapType.prototype.getTile = function(coord, zoom, ownerDocument) {
			var img = ownerDocument.createElement("img");
			img.style.width = this.tileSize.width + "px";
			img.style.height = this.tileSize.height + "px";
			img.src = strURL + zoom + "/" + coord.x + "/" + coord.y + ".png";
			return img;
		};
	  
		var localMapType = new LocalMapType();
		 
		function initialize() {
		  var myLatlng = new google.maps.LatLng(<%=geo.lat%>, <%=geo.lon%>);
		  var myOptions = {
		    center: myLatlng,
		    zoom: 12,
		    streetViewControl: false,
		 	mapTypeControlOptions: {
		        mapTypeIds: [
 		          //google.maps.MapTypeId.ROADMAP,
 		          //google.maps.MapTypeId.HYBRID,
 		          //google.maps.MapTypeId.SATELLITE,
 		          //google.maps.MapTypeId.TERRAIN,
 		          //'locaMap' //定义地图类型
		     	] 
			}
		  };
		  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
		  
		  map.mapTypes.set('locaMap', localMapType);   //绑定本地地图类型
		  //map.setMapTypeId('locaMap');    //指定显示本地地图
		  
		  var marker = new google.maps.Marker({
		      position: myLatlng, 
		      map: map,
		      //draggable: true,
			  //icon: 'mapfiles/rotate2.png',  
              //animation :google.maps.Animation.DROP,
		      title: "点击我!"
		  });
		  
		  var infowindow = new google.maps.InfoWindow();
		
		  google.maps.event.addListener(marker, 'click', function() {
		    infowindow.setContent("当前位置: " + marker.getPosition().toUrlValue(14));
		    infowindow.open(map, this);
		  });
		  
		}
	</script>
</head>
<body onload="initialize()">
  <div id="map_canvas"></div>
</body>
</html>
