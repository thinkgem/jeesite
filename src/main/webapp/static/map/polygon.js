/*
 * Polygon creator class
 * Developed by The Di Lab
 * www.the-di-lab.com
 * 22.06.2010
 */
function PolygonCreator(map) {
	//create pen to draw on map
	this.map = map;
	this.pen = new Pen(this.map);
	this.latLng = '';
	var thisOjb = this;
	this.event = google.maps.event.addListener(thisOjb.map, 'click', function (event) {
		thisOjb.latLng = event.latLng;
		thisOjb.pen.draw(thisOjb.latLng);
	});
	
	this.getDot = function () {
		return (this.latLng+"").replace(/[()]/g,"");
	}

	this.getData = function () {
		return this.pen.getData() != null ? (this.pen.getData()+"").replace(/\)\(/g,";").replace(/[()]/g,"") : '';
	}

	this.showData = function () {
		return this.pen.getData();
	}
	
	this.showColor = function () {
		return this.pen.getColor();
	}

	//destroy the pen
	this.destroy = function () {
		this.pen.deleteMis();
		if (null != this.pen.polygon) {
			this.pen.polygon.remove();
		}
		google.maps.event.removeListener(this.event);
	}

	this.drawPolygon = function (latLngList) {
		$.each(latLngList, function (index, value) {
			thisOjb.pen.draw(value);
		});
		this.pen.drawPloygon(this.pen.listOfDots);
	}

	this.getPolygon = function () {
		return this.pen.polygon.getPolygonObj();
	}

}
/*
 * pen class
 */
function Pen(map) {
	this.map = map;
	this.listOfDots = new Array();
	this.polyline = null;
	this.polygon = null;
	this.currentDot = null;
	//draw function
	this.draw = function (latLng) {
		if (null != this.polygon) {
			//alert('已有一个图形区域。');
		} else {
			if (this.currentDot != null && this.listOfDots.length > 1 && this.currentDot == this.listOfDots[0]) {
				this.drawPloygon(this.listOfDots);
			} else {
				//remove previous line
				if (null != this.polyline) {
					this.polyline.remove();
				}
				//draw Dot
				var dot = new Dot(latLng, this.map, this);
				this.listOfDots.push(dot);
				//draw line
				if (this.listOfDots.length > 1) {
					this.polyline = new Line(this.listOfDots, this.map);
				}
			}
		}
	}
	//draw ploygon
	this.drawPloygon = function (listOfDots, color, des, id) {
		this.polygon = new Polygon(listOfDots, this.map, this, color, des, id);
		this.deleteMis();
	}
	//delete all dots and polylines
	this.deleteMis = function () {
		//delete dots
		$.each(this.listOfDots, function (index, value) {
			value.remove();
		});
		this.listOfDots.length = 0;
		//delete lines
		if (null != this.polyline) {
			this.polyline.remove();
			this.polyline = null;
		}
	}
	//cancel
	this.cancel = function () {
		if (null != this.polygon) {
			(this.polygon.remove());
		}
		this.polygon = null;
		this.deleteMis();
	}
	//setter
	this.setCurrentDot = function (dot) {
		this.currentDot = dot;
	}
	//getter
	this.getListOfDots = function () {
		return this.listOfDots;
	}
	//get plots data
	this.getData = function () {
		if (this.polygon != null) {
			var data = "";
			var paths = this.polygon.getPlots();
			//get paths
			paths.getAt(0).forEach(function (value, index) {
				data += (value.toString());
			});
			return data;
		} else {
			return null;
		}
	}
	//get color
	this.getColor = function () {
		if (this.polygon != null) {
			var color = this.polygon.getColor();
			return color;
		} else {
			return null;
		}

	}
}

/* 
 * Child of Pen class
 * dot class
 */
function Dot(latLng, map, pen) {
	//property
	this.latLng = latLng;
	this.parent = pen;

	this.markerObj = new google.maps.Marker({
			position : this.latLng,
			map : map
		});

	//closure
	this.addListener = function () {
		var parent = this.parent;
		var thisMarker = this.markerObj;
		var thisDot = this;
		google.maps.event.addListener(thisMarker, 'click', function () {
			parent.setCurrentDot(thisDot);
			parent.draw(thisMarker.getPosition());
		});
	}
	this.addListener();

	//getter
	this.getLatLng = function () {
		return this.latLng;
	}

	this.getMarkerObj = function () {
		return this.markerObj;
	}

	this.remove = function () {
		this.markerObj.setMap(null);
	}
}

/* 
 * Child of Pen class
 * Line class
 */
function Line(listOfDots, map) {
	this.listOfDots = listOfDots;
	this.map = map;
	this.coords = new Array();
	this.polylineObj = null;

	if (this.listOfDots.length > 1) {
		var thisObj = this;
		$.each(this.listOfDots, function (index, value) {
			thisObj.coords.push(value.getLatLng());
		});

		this.polylineObj = new google.maps.Polyline({
				path : this.coords,
				strokeColor : "#FF0000",
				strokeOpacity : 1.0,
				strokeWeight : 2,
				map : this.map
			});
	}

	this.remove = function () {
		this.polylineObj.setMap(null);
	}
}

/*
 * Child of Pen class
 * polygon class
 */
function Polygon(listOfDots, map, pen, color) {
	this.listOfDots = listOfDots;
	this.map = map;
	this.coords = new Array();
	this.parent = pen;
	this.des = 'Hello';

	var thisObj = this;
	$.each(this.listOfDots, function (index, value) {
		thisObj.coords.push(value.getLatLng());
	});

	this.polygonObj = new google.maps.Polygon({
			paths : this.coords,
			strokeColor : "#FF0000",
			strokeOpacity : 0.8,
			strokeWeight : 2,
			fillColor : "#FF0000",
			fillOpacity : 0.05,
			map : this.map
		});

	this.remove = function () {
		this.info.remove();
		this.polygonObj.setMap(null);
	}

	//getter
	this.getContent = function () {
		return this.des;
	}

	this.getPolygonObj = function () {
		return this.polygonObj;
	}

	this.getListOfDots = function () {
		return this.listOfDots;
	}

	this.getPlots = function () {
		return this.polygonObj.getPaths();
	}

	this.getColor = function () {
		return this.getPolygonObj().fillColor;
	}

	//setter
	this.setColor = function (color) {
		return this.getPolygonObj().setOptions({
			fillColor : color,
			strokeColor : color,
			strokeWeight : 2
		});
	}

	this.info = new Info(this, this.map);

	//closure
	this.addListener = function () {
		var info = this.info;
		var thisPolygon = this.polygonObj;
		google.maps.event.addListener(thisPolygon, 'rightclick', function (event) {
			info.show(event.latLng);
		});
	}
	//this.addListener();

}

/*
 * Child of Polygon class
 * Info Class
 */
function Info(polygon, map) {
	this.parent = polygon;
	this.map = map;

	this.color = document.createElement('input');

	this.button = document.createElement('input');
	$(this.button).attr('type', 'button');
	$(this.button).val("Change Color");
	var thisOjb = this;

	//change color action
	this.changeColor = function () {
		thisOjb.parent.setColor($(thisOjb.color).val());
	}

	//get content
	this.getContent = function () {
		var content = document.createElement('div');

		$(this.color).val(this.parent.getColor());
		$(this.button).click(function () {
			thisObj.changeColor();
		});

		$(content).append(this.color);
		$(content).append(this.button);
		return content;
	}

	thisObj = this;
	this.infoWidObj = new google.maps.InfoWindow({
			content : thisObj.getContent()
		});

	this.show = function (latLng) {
		this.infoWidObj.setPosition(latLng);
		this.infoWidObj.open(this.map);
	}

	this.remove = function () {
		this.infoWidObj.close();
	}

}
