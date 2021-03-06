var editType = "new";
var orgTree = null;
var thisInst = "";
var thisComp = null;
var thisGroupId = "";
var thisGroupName = "全部云盒";
var COMPCenter = null;
var FancePoints = null;
jQuery(function ($) {
    $("#t_eqpmuser").lookup({bsid: "DCUSER", opname: "searchUserLookUp", lname: "pg_text", exfun: "setUserOrg"});
    $("#t_eqpmorg").lookup({bsid: "DCUSER", opname: "searchOrgLookUp", lname: "pg_text"});
    $("#t_releqp").lookup({bsid: "EQUIPMENT", opname: "searchEqpInstLookUp", lname: "pg_text"});
    $("#t_truck").lookup({bsid: "TRUCK", opname: "searchTruckLookUp", lname: "pg_text"});
    $("#t_eqppdate").datetimepicker({
        lang: 'ch',
        format: 'Y-m-d H:i:s',
    });
    getLoginUser("div_user", "TRUCK_EQP", "EQP_MANG", function () {
        EqpInstIni();
        UserIni();
    });
});

function UserIni() {
    doRefresh(
        "",
        "DCUSER",
        "UserIni",
        "",
        function (_data) {
            if (_data.r == 0) {
                thisComp = _data.comp;
                $("#t_orgcomp").val(thisComp.id);
                iniOrgTree("orgTree", "div_orgtree");
            }
        });
}

function EqpInstIni() {
    doRefresh(
        "",
        "EQUIPMENT",
        "searchEqpDefineList",
        "&pg_type=EQUIPMENT_DEFTYPE_0", function (_data) {
            if (_data.r == 0) {
                var _html = "<option value=''>---请选择---</option>";
                var _defId = "";
                for (var i = 0; i < _data.list.length; i++) {
                    var oneD = _data.list[i];
                    if (_defId != "") {
                        _defId = oneD.id;
                    }
                    _html += "<option value='" + oneD.id + "'>" + oneD.name + "</option>";
                }
                $("#s_eqpdef").html(_html);
                $("#s_eqpdef").val(_defId);
                if (_data.company.lat != "" && _data.company.lon != "") {
                    COMPCenter = [parseFloat(_data.company.lon), parseFloat(_data.company.lat)];
                }
            }
        });
    searchVehicle();
}

function iniOrgTree(_name, _div) {
    orgTree = new BSTreeView(_name, "", false, "", _div);
    orgTree.imagePath = webHome + "/common/images/tree/";
    orgTree
        .addRootNode(
            "root",
            thisComp.name,
            "searchVehicleFromG('','全部手表')", "", "", true);
    orgTree.DrawTree(true);
    if (thisComp.id != "") {
        getOrgTree();
    }
}

function getOrgTree() {
    doRefresh(
        "",
        "DCUSER",
        "getOrgTree",
        "&in_compid=" + thisComp.id,
        function (_data) {
            if (_data.r == 0) {
                var root = orgTree.getNodeById(0);
                for (var i = 0; i < _data.list.length; i++) {
                    var oneD = _data.list[i];
                    var sub = root
                        .addNode(
                            oneD.id,
                            oneD.name,
                            "searchVehicleFromG('" + oneD.id + "','" + oneD.name + "')", "", "",
                            true);
                    if (oneD.cnum > 0) {
                        _setChildOrg(sub, oneD.children);
                    }
                }
            }
            else {
                showErrDlg(_data);
            }
            $(document).scrollTop(0);
        });
}

function _setChildOrg(_pnode, _nodes) {
    for (var j = 0; j < _nodes.length; j++) {
        var oneSubD = _nodes[j];
        var subNode = _pnode.addNode(
            oneSubD.id,
            oneSubD.name,
            "searchVehicleFromG('" + oneSubD.id + "','" + oneSubD.name + "')",
            "", "", true);
        if (oneSubD.cnum > 0) {
            _setChildOrg(subNode, oneSubD.children);
        }
    }
}

/**手表***/
function searchVehicleFromG(_gid, _gname) {
    thisGroupId = _gid;
    thisGroupName = _gname;
    searchVehicle();
}

//搜索云盒
function searchVehicle(_page) {
    if (_page == null) {
        _page = $("#vehicle_page_thisp").val();
    }
    if (_page == null || _page == "" || _page == 0) {
        _page = 1;
    }
    doRefresh(
        "",
        "EQUIPMENT",
        "searchEqpInstList",
        "&pg_group=" + thisGroupId + "&pg_eqptye=EQUIPMENT_DEFTYPE_0&pg_size=50&pg_num=" + (_page - 1),
        function (_data) {
            clearTable("vehicle_tab");
            if (_data.r == 0) {
                for (var i = 0; i < _data.list.length; i++) {
                    var oneD = _data.list[i];
                    var _oneTr = addTableRow("vehicle_tab");
                    addTableCell("vehicle_tab", _oneTr, "ma", "<div>用户:" + oneD.musername + "[" + oneD.muserid + "] " + oneD.muserphone + "</div>"
                        + "<div>机构:" + oneD.morgname + "</div>"
                        + "<div>车辆:" + oneD.mtruckname + "</div>");
                    addTableCell("vehicle_tab", _oneTr, "state", oneD.statename + "<div>" + oneD.online + "</div>");
                    addTableCell("vehicle_tab", _oneTr, "name", oneD.name
                        + "<div class='text-gray'>设备ID:" + oneD.wycode + "</div>"
                        + "<div class='text-gray'>Token:" + oneD.token + "</div>"
                        + "<div>物联网号码:" + oneD.phone + "</div>"
                        + "<div>条码:" + oneD.qr + "</div>");
                    addTableCell("vehicle_tab", _oneTr, "edit",
                        "<div><button type=\"button\" class=\"button bg-main button-little\" onclick=\"editEqpInst('"
                        + oneD.id + "')\">编辑</button>"
                        + "&nbsp;<button type=\"button\" class=\"button bg-main button-little\" onclick=\"showVehicleMsg('"
                        + oneD.id + "')\">实时状态</button></div><div class='margin-little-top'>"
                        + (oneD.state == 0 ? "<button type=\"button\" class=\"icon-trash-o button bg-yellow button-little\" onclick=\"deleteOneVehicle('"
                        + oneD.id + "','state')\"> 无效</button>" : "<button type=\"button\" class=\"icon-undo button bg-main button-little\" onclick=\"deleteOneVehicle('"
                        + oneD.id + "','reset')\"> 还原</button><button type=\"button\" class=\"margin-little-left icon-times button bg-dot button-little\" onclick=\"deleteOneVehicle('"
                        + oneD.id + "','delete')\"> 删除</button>")
                        + "</div><div class='margin-little-top'><button type=\"button\" class=\"button bg-sub button-little\" onclick=\"showEqpR('"
                        + oneD.id + "','" + oneD.name + "')\">关联设备</button></div>");
                }
                setPage("vehicle_page", _data.max, 50, _page, "searchVehicle");
                $(document).scrollTop(0);
            }
        });
}

// 编辑云盒
function editEqpInst(_instid) {
    editType = "new";
    var title = "新增云盒";
    if (_instid != null && _instid != "") {
        thisInst = _instid;
        editType = "edit";
        title = "编辑云盒";
    }
    doRefresh(null, "EQUIPMENT", "getOneEqpInst", "&in_type=" + editType + "&eqpid="
        + _instid, function (_data) {
        showEqpInstBase(_data, editType);
        openDialogs({
            id: "vehicle-base",
            title: title,
            width: "50%",
            father: "",
        }, function () {
            return true;
        });
    });
}

// 设置云盒
function showEqpInstBase(_data, _type) {
    $("#t_eqpid").val(_data.id);
    $("#t_eqpwycode").val(_data.wycode);
    $("#t_eqptoken").val(_data.token);
    $("#t_eqpname").val(_data.name);
    $("#s_eqpdef").val(_data.def);
    $("#t_eqpqr").val(_data.qr);
    $("#t_eqpphone").val(_data.phone);
    $("#t_eqppdate").val(_data.pdate);
    $("#t_eqppara1").val(_data.para1);
    $("#t_eqpmuser_v").val(_data.muser);
    $("#t_eqpmuser").val(_data.muserid == "" ? "" : _data.musername + "[" + _data.muserid + "]");
    $("#s_eqpmorg").val(_data.morg);
    $("#t_truck_v").val(_data.mtruck);
    $("#t_truck").val(_data.mtruck == "" ? "" : _data.mtruckname + "[" + _data.mtruckpnum + "]");
    $("#t_eqpmorg_v").val(_data.morg);
    $("#t_eqpmorg").val(_data.morgallname);

    $("#t_eqppara2").val(_data.para2);
    $("#t_eqptonnage").val(_data.tonnage);
    $("#t_eqpinstall").val(_data.install);
    $("#t_eqplon").val(_data.deflon);
    $("#t_eqplat").val(_data.deflat);
    $("#l_compgeo").html("经度:" + _data.deflon + ";纬度:" + _data.deflat);
}

//保存云盒
function commitEqpInst() {
    if (checkForm("vehiclebase-form") && confirm("是否保存云盒信息？")) {
        doRefresh("vehiclebase-form", "EQUIPMENT", "updateEqpInst", "&in_type="
            + editType, function (_data) {
            if (_data.r == 0) {
                closeDialog("vehicle-base");
                searchVehicle();
            } else {
                alert(_data.error);
            }
        });
    }

}

function setUserOrg(_org) {
    if ($("#t_eqpmuser_v").val() != "") {
        doRefresh(null, "DCUSER", "getOneOrgByUser", "&pg_user="
            + $("#t_eqpmuser_v").val(), function (_data) {
            if (_data.r == 0) {
                $("#t_eqpmorg_v").val(_data.id);
                $("#t_eqpmorg").val(_data.allname);
            } else {
                alert(_data.error);
            }
        });
    }
}

function getVehicleParas(_vehickeId, _name) {
    doRefresh(null, "VEHICLE", "getVehicleParas", "&vehickeid=" + _vehickeId, function (_data) {
        if (_data.r == 0) {
            //$("#t_eqpr_0").val(_data.gps_date);
            //$("#t_eqpr_1").val(_data.oil_date);
            $("#t_dicitem_2").val(_data.stop_date);
            $("#t_dicitem_3").val(_data.oil_diff);
        } else {
            alert(_data.error);
        }
    });
    openDialogs({
        id: "vehicleparas-base",
        title: "【" + _name + "】参数设置",
        width: "50%",
        father: "",
    }, function () {
        searchVehicle();
        return true;
    });
}

function commitVehicleParas() {
    if (checkForm("vehicleparas-form") && confirm("是否保存云盒参数信息？")) {
        doRefresh("vehicleparas-form", "VEHICLE", "updateVehicleParas", "", function (_data) {
            if (_data.r != 0) {
                alert(_data.error);
            }
            else {
                closeDialog("vehicleparas-base");
            }
        });
    }
}

function showVehicleMsg() {
    window.open(webHome + "/storage/equipment/vehiclegeo.html", "dlg_vehicle");
}

function showEqpR(_id, _name) {
    clearTable("eqpr_tab");
    $("#t_peqpid").val(_id);
    openDialogs({
        id: "eqpr-dlg",
        title: "为【" + _name + "】关联其他设备",
        width: "50%",
        father: "",
        zindex: 1010
    }, function () {
        return true;
    });
    searchRelEqpList();
}

function searchRelEqpList() {
    doRefresh(null, "EQUIPMENT", "searchEqpInstList", "&pg_size=50&pg_num=0&pg_peqp=" + $("#t_peqpid").val(), function (_data) {
        if (_data.r == 0) {
            clearTable("eqpr_tab");
            for (var i = 0; i < _data.list.length; i++) {
                var oneD = _data.list[i];
                var _oneTr = addTableRow("eqpr_tab");
                addTableCell("eqpr_tab", _oneTr, "name", "<div class=\"text-strong\"><strong>" + oneD.name + "</strong></div>"
                    + "<div class='text-gray'>设备ID:" + oneD.wycode + "</div>"
                    + "<div>条码:" + oneD.qr + "</div>");
                addTableCell("eqpr_tab", _oneTr, "del",
                    "<button type=\"button\" class=\"button bg-dot button-small icon-trash-o\" onclick=\"deleteRelEqp('"
                    + oneD.id + "','" + oneD.name + "')\"> 删除</button>");
            }
            setPage("eqpr_page", _data.list.length, _data.list.length, 1, "");
        }
        else {
            alert(_data.error);
        }
    });
}

function createRelEqp() {
    openDialogs({
        id: "eqpradd-dlg",
        title: "新增关联设备",
        width: "40%",
        father: "",
        zindex: 2050
    }, function () {
        return true;
    });
}

function addRelEqp() {
    if (checkForm("eqpradd-form") && confirm("是否确认添加该关联设备？")) {
        doRefresh("eqpradd-form", "EQUIPMENT", "updateEqpRel", "", function (_data) {
            if (_data.r == 0) {
                closeDialog("eqpradd-dlg");
                searchRelEqpList();
            }
            else {
                alert(_data.error);
            }
        });
    }
}

function deleteRelEqp(_id) {
    if (confirm("是否解除添加该关联设备？")) {
        doRefresh(null, "EQUIPMENT", "deleteEqpRel", "&t_peqpid=" + $("#t_peqpid").val() + "&t_releqp=" + _id, function (_data) {
            if (_data.r == 0) {
                searchRelEqpList();
            }
            else {
                alert(_data.error);
            }
        });
    }
}


function deleteOneVehicle(_id, _type) {
    var isOk = true;
    if (_type == "delete") {
        isOk = confirm("彻底删除后无法还原，是否继续？");
    }
    if (isOk) {
        doRefresh(
            "",
            "VEHICLE",
            "deleteOneVehicle",
            "&pg_inst=" + _id + "&pg_type=" + _type,
            function (_data) {
                if (_data.r == 0) {
                    searchVehicle();
                }
                else {
                    alert(_data.error);
                }
            }
        );
    }
}

function updateAllVehicle(_state, _text) {
    $(".dropdown-hover, .button-group, .drop").removeClass("open");
    var isOk = confirm(_text);
    if (isOk) {
        doRefresh(
            "",
            "VEHICLE",
            "updateAllVehicle",
            "&pg_allstate=" + _state,
            function (_data) {
                if (_data.r == 0) {
                    searchVehicle();
                }
                else {
                    alert(_data.error);
                }
            }
        );
    }
}

function setEqpDefGeo() {
    openDialogs({
        id: "center-dlg",
        title: "选择中心位置",
        width: "90%",
        father: ""
    }, function () {
        return true;
    });
    var _center = [112.9380, 28.1711];
    if (COMPCenter != null) {
        _center = COMPCenter;
    }
    var _eqpDef = null;
    if ($("#t_eqplat").val() != "" && $("#t_eqplon").val() != "") {
        _eqpDef = [parseFloat($("#t_eqplon").val()), parseFloat($("#t_eqplat").val())]
    }
    mapIni(_eqpDef, _center);
}

function getCenterPoint() {
    $("#t_eqplat").val(FancePoints.lat);
    $("#t_eqplon").val(FancePoints.lon);
    $("#l_compgeo").html("经度:" + FancePoints.lon + ";纬度:" + FancePoints.lat);
    closeDialog("center-dlg");
}

function mapIni(_points, _center) {
    $("#container").html("");
    $("#container").height($("#center-dlg-body").height());
    $("#div_mapcond").width($("#p_form").width() - 54);
    var onePF = null;
    if (_points != null) {
        FancePoints = _points;
        _center = _points;
        onePF = new ol.Feature({
            geometry: new ol.geom.Point(_points)
        });
    }
    else {
        FancePoints = null;
    }
    if (onePF != null) {
        drawSource = new ol.source.Vector({
                features: [onePF]
            }
        );
    }
    else {
        drawSource = new ol.source.Vector();
    }

    drawVector = new ol.layer.Vector({
        source: drawSource,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(0, 255, 0, 0.3)'
            }),
            stroke: new ol.style.Stroke({
                color: '#ff0000',
                width: 2
            }),
            image: new ol.style.Circle({
                radius: 7,
                fill: new ol.style.Fill({
                    color: '#ff0000'
                })
            })
        })
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

    function addInteractions() {
        draw = new ol.interaction.Draw({
            source: drawSource,
            type: "Point"
        });
        map.addInteraction(draw);
        snap = new ol.interaction.Snap({source: drawSource});
        map.addInteraction(snap);
    }

    addInteractions();
    draw.on('drawstart',
        function (evt) {
            drawSource.clear();
            FancePoints = {};
        }, this);
    draw.on('drawend',
        function (evt) {
            // set sketch
            var geom = evt.feature.getGeometry().getCoordinates();
            FancePoints = {lon: geom[0], lat: geom[1]};
        }, this);
}