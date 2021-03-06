var editType = "new";
var reportFunTree = null;
var thisInst = "";
jQuery(function ($) {
    $(".wsedate").datetimepicker({
        lang: 'ch',
        format: 'Y-m-d',
    });
    getLoginUser("div_user", "TRUCK_WORK", "WORK_LOGS", function () {
        //ReportIni();
        searchTruckReport();
    });
});

function ReportIni() {
    doRefresh(
        "",
        "STATS",
        "ReportIni",
        "", function (_data) {
            if (_data.r == 0) {
            }
        });
}

//搜索车辆
function searchTruckReport(_page) {
    if (_page == null) {
        _page = $("#report_page_thisp").val();
    }
    if (_page == null || _page == "" || _page == 0) {
        _page = 1;
    }
    doRefresh(
        "frmBusiness",
        "STATS",
        "searchTruckReportList",
        "&pg_size=50&pg_num=" + (_page - 1),
        function (_data) {
            clearTable("report_tab");
            if (_data.r == 0) {
                for (var i = 0; i < _data.data.length; i++) {
                    var oneD = _data.data[i];
                    var _oneTr = addTableRow("report_tab");
                    var _truck = "<div><strong>" + oneD.truckname + "[" + oneD.trucktype + "]</strong></div>";
                    _truck += "<div>资产编号：" + oneD.truckno + "&nbsp;[项目组：" + oneD.truckorg + "]</div>";
                    _truck += "<div>负责人：" + oneD.username + "[" + oneD.userid + "]；电话：" + oneD.userphone + "</div>";
                    addTableCell("report_tab", _oneTr, "name", _truck);
                    addTableCell("report_tab", _oneTr, "oil", oneD.oil);
                    addTableCell("report_tab", _oneTr, "dis", oneD.distance);
                    addTableCell("report_tab", _oneTr, "worktimes", oneD.worktimes);
                    addTableCell("report_tab", _oneTr, "useeff", oneD.useff);
                }
                setPage("report_page", _data.max, 50, _page, "searchTruckReport");
                $(document).scrollTop(0);
            }
        });
}

function exportTruckReport() {
    doRefreshDownLoad("frmBusiness", "KULUINTERFACE", "exportTruckReportList", "");
}