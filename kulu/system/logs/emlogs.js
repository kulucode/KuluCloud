var editType = "new";
var thisInst = "";
var LogsTypeList = null;
jQuery(function ($) {
    $(".wsdate").datetimepicker({
        lang: 'ch',
        format: 'Y-m-d H:i:s',
    });
    getLoginUser("div_user", "LOGS_EM_MANG", "LOGS_MANG", function () {
        LogsIni();
    });
});

function LogsIni() {
    doRefresh(
        "",
        "DCLOGS",
        "LogsIni",
        "", function (_data) {
            if (_data.r == 0) {
                $("#pg_sdate").val(_data.sdate);
                $("#pg_edate").val(_data.edate);
                searchLogs();
            }
        });
}

//搜索报警
function searchLogs(_page) {
    if (_page == null) {
        _page = $("#logs_page_thisp").val();
    }
    if (_page == null || _page == "" || _page == 0) {
        _page = 1;
    }
    doRefresh(
        "frmBusiness",
        "DCLOGS",
        "searchEqpMsgLogsList",
        "&pg_size=200&pg_num=" + (_page - 1),
        function (_data) {
            clearTable("logs_tab");
            if (_data.r == 0) {
                for (var i = 0; i < _data.list.length; i++) {
                    var oneD = _data.list[i];
                    var _oneTr = addTableRow("logs_tab");
                    addTableCell("logs_tab", _oneTr, "name", "<div><strong>" + oneD.name + "</strong></div>"
                        + "<div>日志时间：<strong>" + oneD.date + "</strong>；报文时间：" + oneD.msgdate + "</div>"
                        + "<div>设备：" + oneD.eqpname + "</div>"
                        + "<div>" + oneD.desc + "</div>", oneD.desc);
                    addTableCell("logs_tab", _oneTr, "edit",
                        "<button type=\"button\" class=\"button bg-main button-small\" onclick=\"showMsg('"
                        + oneD.id + "','" + oneD.name + "')\">报文</button>");
                }
                setPage("logs_page", _data.max, 200, _page, "searchLogs");
                $(document).scrollTop(0);
            }
        });
}

// 编辑报警
function showMsg(_id, _name) {
    doRefresh(null, "DCLOGS", "getOneEqpMsgLogs", "&pg_logsid="
        + _id, function (_data) {
        $("#t_eqpname").val(_data.data.eqpname);
        $("#t_inmsg").val(JSON.stringify(_data.data.inmsg));
        $("#t_outmsg").val(JSON.stringify(_data.data.outmsg));
        openDialogs({
            id: "msg-base",
            title: _name,
            width: "50%",
            father: ""
        }, function () {
            return true;
        });
    });
}