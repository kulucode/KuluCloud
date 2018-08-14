var editType = "new";
var faultFunTree = null;
var thisInst = "";
var LogsTypeList = null;
jQuery(function ($) {
    $(".wsdate").datetimepicker({
        lang: 'ch',
        format: 'Y-m-d H:i:s',
    });
    getLoginUser("div_user", "LOGS_L_MANG", "LOGS_MANG", function () {
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
        "searchLogsList",
        "&pg_type=0&pg_size=100&pg_num=" + (_page - 1),
        function (_data) {
            clearTable("logs_tab");
            if (_data.r == 0) {
                for (var i = 0; i < _data.list.length; i++) {
                    var oneD = _data.list[i];
                    var _oneTr = addTableRow("logs_tab");
                    addTableCell("logs_tab", _oneTr, "name", "<div><strong>" + oneD.name + "</strong></div>"
                        + "<div>发生时间:<strong>" + oneD.date + "</strong></div>"
                        + "<div>" + oneD.desc + "</div>", oneD.desc);
                    addTableCell("logs_tab", _oneTr, "cuser", "<div>" + oneD.cuname + "[" + oneD.cuid + "]</div>"
                        + "<div>联系电话:" + oneD.cuphone + "</div>"
                        + "<div>单位:" + oneD.cugroup + "</div>");
                    addTableCell("logs_tab", _oneTr, "ip", "<div>内网：" + oneD.localip + "</div>"
                        + "<div class='text-gray'>外网：" + oneD.netip + "</div>");
                }
                setPage("logs_page", _data.max, 100, _page, "searchLogs");
                $(document).scrollTop(0);
            }
        });
}