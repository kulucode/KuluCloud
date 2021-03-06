var mapLayers, lineLayers = null;
var map = null;
var drawVector = null;
var defaultLayer = [];
var newLayers = {};
var popup = null;
var websocket = null;
var UserFeature = {};
var UserList = {};
var isGJ = false;

jQuery(function ($) {
    $("#div_mapcond").width($(window).width() - 120);
    $("#container").height($(window).height() - 108);
    $('.wscdate').datetimepicker({
        lang: 'ch',
        format: 'Y-m-d H:i:s'
    });
    getPlateMsg();
});

function getPlateMsg() {
    doRefresh(
        null,
        "STATS",
        "searchSysBaseStats",
        "",
        function (_data) {
            if (_data.r == 0) {
                var _center = null;
                if (_data.company.lat != "" && _data.company.lon != "") {
                    _center = [parseFloat(_data.company.lon), parseFloat(_data.company.lat)];
                }
                mapIni(_center);
                websocketIni();
            }
        });
}

function mapIni(_center) {
    if (_center == null) {
        _center = [112.8903, 28.1904];
    }
    var drawSource = new ol.source.Vector();
    drawVector = new ol.layer.Vector({
        source: drawSource
    });
    defaultLayer = [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        }), drawVector];
    $("#mouse-position").html("");
    map = new ol.Map({
        controls: ol.control.defaults({
            attributionOptions: {
                collapsible: false
            }
        }).extend([
            new ol.control.ScaleLine({
                units: 'degrees'
            })
            , new ol.control.MousePosition({
                coordinateFormat: ol.coordinate.createStringXY(4),
                projection: 'EPSG:4326',
                // comment the following two lines to have the mouse position
                // be placed within the map.
                className: 'custom-mouse-position',
                target: document.getElementById("mouse-position"),
                undefinedHTML: '鼠标移动显示经纬度'
            })]),
        layers: defaultLayer,
        target: 'container',
        view: new ol.View({
            projection: 'EPSG:4326',
            center: _center,
            zoom: 13
        })
    });
    var pDiv = $("#popup");
    if (pDiv.length == 0) {
        $("body").append("<div id=\"popup\" style=\"width:250px;\"></div>");
        pDiv = $("#popup");
    }
    pDiv.popover('destroy');
    popup = new ol.Overlay({
        element: pDiv[0],
        positioning: 'bottom-center',
        stopEvent: false,
        offset: [0, -50]
    });
    map.addOverlay(popup);
    //pointermove
    map.on('pointermove', function (evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature) {
                return feature;
            });
        if (feature) {
            var coordinates = feature.getGeometry().getCoordinates();
            popup.setPosition(coordinates);
            pDiv.popover({
                'placement': 'top',
                'html': true,
                'top': 80,
                'content': feature.get('desc')
            });
            pDiv.popover('show');
        } else {
            pDiv.popover('destroy');
        }
    });
}

function mapRest() {
    //清除layer
    if (map != null) {
        map.getOverlays().clear();
        map.addOverlay(popup);
        $.each(newLayers, function (n, value) {
            map.removeLayer(value);
        });
    }
    newLayers = {};
}

var moveFeature = function (event) {
    var vectorContext = event.vectorContext;
    var frameState = event.frameState;

    if (animating) {
        var elapsedTime = frameState.time - now;
        // here the trick to increase speed is to jump some indexes
        // on lineString coordinates
        var index = Math.round(speed * elapsedTime / 1000);

        if (index >= routeLength) {
            stopAnimation(true);
            return;
        }

        var currentPoint = new ol.geom.Point(routeCoords[index]);
        var feature = new ol.Feature(currentPoint);
        vectorContext.drawFeature(feature, styles.geoMarker);
    }
    // tell OpenLayers to continue the postcompose animation
    map.render();
};

function getALlWatchData() {
    mapRest();
}

function websocketIni() {
    var websocket = null;
    if ('WebSocket' in window) {
        //ws://182.61.56.21/kulucloud/TTWebsocket/
        //ws://localhost:8080/KuluCloud/TTWebsocket/
        websocket = new WebSocket(thisWebSocket + getTTSession() + "?dataid=02");
    }
    else {
        alert('Not support websocket')
    }
    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        //获得用户列表
        //alert(getRefreshURL("", "EQUIPMENT", "getEqpGeometryList"));
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var _d = JSON.parse(event.data);
        if (!isGJ) {
            if (_d.r == 0) {
                if (_d.opfun == "login") {
                    getUserMaps();
                }
                if (_d.opfun == "gps" || _d.opfun == "hr" || _d.opfun == "step" || _d.opfun == "bpo") {
                    addMapPoint(_d);
                }
            }
            else {
                alert(JSON.stringify(_d));
            }
        }
    }
    websocket.onclose = function () {

    }
}

function addMapPoint(_data) {
    var _oneD = _data;
    if (newLayers["d_" + _oneD.eqpid] != null) {
        _oneD = UserList["u_" + _oneD.eqpid];
        if (_data.opfun == "gps") {
            _oneD.lat = _data.lat;
            _oneD.lon = _data.lon;
            _oneD.date = _data.geodate;
        }
        else if (_data.opfun == "hr") {
            _oneD.hr = _data.hr;
            _oneD.ele = _data.ele;
            _oneD.date = _data.hrdate;
        }
        else if (_data.opfun == "bpo") {
            _oneD.bpoh = _data.bpoh;
            _oneD.bpol = _data.bpol;
            _oneD.date = _data.bpodate;
        }
        else if (_data.opfun == "step") {
            _oneD.step = _data.step;
            _oneD.date = _data.stepdate;
        }

        UserList["u_" + _oneD.eqpid] = _oneD;
        //移动
        $(".p_" + _oneD.eqpid).remove();
    }
    if (_oneD.lat == null || _data.lon == null || _data.lat == "" || _data.lon == "") {
        return;
    }

    var _desc = "<div><strong>用户：" + _oneD.musername + "</strong></div>";
    _desc += "<div><strong>QR：" + _oneD.qr + "</strong></div>";
    _desc += "<div><strong>当天步数：" + _oneD.step + "</strong></div>";
    _desc += "<div><strong>心率：每分钟" + _oneD.hr + "次</strong></div>";
    _desc += "<div><strong>剩余电量：" + _oneD.ele + "%</strong></div>";
    _desc += "<div><strong>经度：" + _oneD.lon + "</strong></div>";
    _desc += "<div><strong>纬度：" + _oneD.lat + "</strong></div>";
    _desc += "<div><strong>更新时间：" + _oneD.date + "</strong></div>";
    var oneP = new ol.Feature({
        geometry: new ol.geom.Point([parseFloat(_oneD.lon), parseFloat(_oneD.lat)])
    });
    oneP.set("desc", _desc);
    oneP.setStyle(new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            color: '#8959A8',
            crossOrigin: 'anonymous',
            src: webHome + '/common/images/dot.png'
        }))
    }));
    // UserFeature["d_" + _data.eqpid] = oneP;
    var mapLayers = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: [oneP]
        })
    });
    map.removeLayer(newLayers["d_" + _oneD.eqpid]);
    newLayers["d_" + _oneD.eqpid] = mapLayers;
    //map.getView().setCenter([parseFloat(_oneD.lon), parseFloat(_oneD.lat)]);
    map.addLayer(mapLayers);
}

window.onbeforeunload = function () {
    websocket.close();
}

function webSocketSend(_message) {
    websocket.send(_message);
}

function getUserMaps() {
    isGJ = false;
    UserList = {};
    doRefresh("", "WATCH", "searchWatchWordParasList", "",
        function (data) {
            if (data.r == 0) {
                mapRest();
                //成功后发送登录成功消息
                var _html = "<option value=''>---请选择---</option>";
                for (var i = 0; i < data.list.length; i++) {
                    var _oneP = data.list[i];
                    _oneP.type = "6";
                    UserList["u_" + _oneP.eqpid] = _oneP;
                    addMapPoint(_oneP);
                    _html += "<option value='" + _oneP.eqpid + "'>" + _oneP.musername + "[" + _oneP.qr + "]</option>";
                }
                $("#s_userlist").html(_html);
            }
        });
}

function getUserTrace() {
    var _selUser = $("#s_userlist").val();
    if (_selUser == "") {
        alert("请选择一个用户！");
        return;
    }
    isGJ = true;
    doRefresh("", "EQUIPMENT", "getEqpGeometryTraceList", "&pg_eqp=" + _selUser + "&pg_sdate=" + $("#map_cdate").val() + "&pg_edate" + $("#map_edate").val(),
        function (data) {
            if (data.r == 0) {
                if (data.list.length > 0) {
                    mapRest();
                    var thisfeatures = [];
                    var linefeatures = [];
                    var centerP = null;
                    var geoLine = new ol.geom.LineString();
                    for (var i = 0; i < data.list.length; i++) {
                        var oneD = data.list[i];
                        if (i == 0) {
                            centerP = [parseFloat(oneD.lon), parseFloat(oneD.lat)];
                        }
                        var _id = "div_" + oneD.lon + "_" + oneD.lat;
                        $("#div_mark").append("<div class='maptext div_label text-black' id='" + _id + "'>" + (i + 1) + "</div>");
                        var _text = new ol.Overlay({
                            //位置
                            position: [oneD.lon, oneD.lat],
                            //覆盖层的元素
                            element: document.getElementById(_id)
                        });
                        map.addOverlay(_text);
                        //overlayList.push(_text);
                        var _desc = "<div><strong>序号：" + (i + 1) + "</strong></div>";
                        //_desc += "<div>ID:" + oneD[selectCol.id] + "</div>";
                        _desc += "<div>时间：" + oneD.cdate + "</div>";


                        var oneP = new ol.Feature({
                            geometry: new ol.geom.Point([parseFloat(oneD.lon), parseFloat(oneD.lat)])
                        });
                        oneP.set("desc", _desc);
                        oneP.setStyle(new ol.style.Style({
                            image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
                                color: (i == 0 ? '#ff0000' : '#8959A8'),
                                crossOrigin: 'anonymous',
                                src: webHome + '/common/images/dot.png'
                            }))
                        }));
                        thisfeatures.push(oneP);
                        geoLine.appendCoordinate([parseFloat(oneD.lon), parseFloat(oneD.lat)]);
                    }
                    var oneL = new ol.Feature({
                        geometry: geoLine
                    });
                    oneL.setStyle(new ol.style.Style({
                        stroke: new ol.style.Stroke({
                            width: 3,
                            color: [255, 0, 0, 1]
                        })
                    }));
                    linefeatures.push(oneL);
                    lineLayers = new ol.layer.Vector({
                        source: new ol.source.Vector({
                            features: linefeatures
                        })
                    });
                    mapLayers = new ol.layer.Vector({
                        source: new ol.source.Vector({
                            features: thisfeatures
                        })
                    });
                    mapLayers.on('click', function (evt) {
                        var coordinate = evt.coordinate;
                        overlay.setPosition(coordinate);
                    });
                    newLayers.lines = lineLayers;
                    newLayers.points = mapLayers;
                    map.addLayer(lineLayers);
                    map.addLayer(mapLayers);

                    //map.getView().setCenter(centerP);
                    //成功后发送登录成功消息
                }
                else {
                    alert("没有轨迹")
                }

            }
        });

}
