(function () {
    'use strict';

    angular.module('myApp').controller("totStatisticsCtimedetailCtl", function ($scope,$stateParams, $rootScope,$timeout, $window, $state, commonService, totService, totStatisticsService,totStatisticsDetailService,TOT_CONSTANT) {
        $window.localStorage["currentItem"] = "totStatisticsCtimedetail";
        $scope.statisticsType = 'cTime';
        $scope.wareHouseData = [];
        $scope.dayDatetime = [];
        $scope.clientData = [];
        $scope.employeeCode=totStatisticsDetailService.employeeCode
        $scope.dayDate=totStatisticsDetailService.dayDate
        $scope.totEmployeeCode=totStatisticsDetailService.employeeCode
        $scope.totEmployeeName=totStatisticsDetailService.employeeName
        $scope.warehouseName=totStatisticsDetailService.warehouseId
        $scope.timeType=totStatisticsDetailService.timeType
        var clockTypeData;
        var tableData;
        var totalTime = 1;
        var htmlStr ="";
        var category;
        var job;

        $("#selectWarehouse").kendoComboBox({
            dataSource: [{name:totStatisticsDetailService.warehouseId,id:totStatisticsDetailService.warehouseId}],
            dataTextField: "name",
            dataValueField: "id",
            filter: "contains",
            suggest: true,
            index: 0
        });

        $("#selectClientInfos").kendoComboBox({
            dataSource: [{name:totStatisticsDetailService.clientId,id:totStatisticsDetailService.warehouseId}],
            dataTextField: "name",
            dataValueField: "id",
            filter: "contains",
            suggest: true,
            index: 0
        });
        function isContains(str, substr) {
            return new RegExp(substr).test(str);
        }
        //甘特图No work点击事件
        $scope.openPage = function(dataItem) {
            $scope.startDateTime = dataItem.activityStartTime;
            $scope.endDateTime = dataItem.activityEndTime;
            $scope.message = dataItem.message;
            if($.isEmptyObject($scope.ClockWindow) || $scope.ClockWindow.selector != "#reassignment") {
                $scope.ClockWindow = $("#reassignment");
                //二级联动下拉框
                $(document).ready(function() {
                    category = $("#selectCategory").kendoDropDownList({
                        optionLabel: "--请选择--",
                        dataTextField: "name",
                        dataValueField: "code",
                        dataSource: totService.getDataSource({key: "getJobcategoryNames"}),
                        change:onChange
                    }).data("kendoDropDownList");
                    job = $("#selectJob").kendoDropDownList({
                        // autoBind: false,
                        // cascadeFrom: "selectCategory",
                        optionLabel: "--请选择--",
                        dataTextField: "name",
                        dataValueField: "code",
                        dataSource: totStatisticsService.getDataSourceMy({key: TOT_CONSTANT.getJobByName+category.text()})
                    }).data("kendoDropDownList");

                    function onChange() {
                        var key = TOT_CONSTANT.getJobByName+category.text();
                        job = $("#selectJob").kendoDropDownList({
                            optionLabel: "--请选择--",
                            dataTextField: "name",
                            dataValueField: "code",
                            dataSource: totStatisticsService.getDataSourceMy({key: key})
                        }).data("kendoDropDownList");
                    };

                });
                $scope.ClockWindow.show();
                $scope.ClockWindow.kendoWindow({
                    width: "500px",
                    height: "600px",
                    title: "Time On Task Reassignment",
                    actions: [
                        "Pin",
                        "Minimize",
                        "Maximize",
                        "Close"
                    ],
                    close:function(){
                        $scope.ClockWindow.hide();
                    }
                }).data("kendoWindow").center().open();
            }else {
                $scope.ClockWindow.show();
                $scope.ClockWindow.data("kendoWindow").center().open();
            }
        }
        $timeout(function () {
            //选择时间事件
            $("#startDatetimepicker").kendoDateTimePicker({
                format: "yyyy-MM-dd HH:mm:ss",
                close:function()
                {
                    // $scope.initDateParam();
                }
            });
            $("#endDatetimepicker").kendoDateTimePicker({
                format: "yyyy-MM-dd HH:mm:ss",
                close:function()
                {
                    // $scope.initDateParam();
                }
            });
            $("#clockDatetimepicker").kendoDateTimePicker({
                format: "yyyy-MM-dd HH:mm:ss"

            });
            $('#dayDatetimepicker').data('kendoDatePicker').bind("close", function(e) {
                // $scope.initDateParam();
            })

            var columns = [
                {
                    field: "message",
                    width: 320,
                    headerTemplate: "<span translate='MESSAGE'></span>"
                },
                {
                    field: "activityStartTime",
                    width: 160,
                    headerTemplate: "<span translate='ACTIVITYSTARTTIME'></span>"
                },
                {
                    field: "activityEndTime",
                    width: 160,
                    headerTemplate: "<span translate='ACTIVITYENDTIME'></span>"
                },
                {
                    field: "total",
                    width: 90,
                    headerTemplate: "<span translate='TIMEFORHOURS'></span>"
                },
                {
                    headerTemplate: "<span translate='GANTTCHART'></span>",
                    template: function (dataItem) {
                        htmlStr = htmlStr.replace("opacity:1", "opacity:0");//不透明度
                        htmlStr = htmlStr.replace("#00B050;height:22px;", "#00B050;");//没有height不占地方以便去掉之前的Onclock
                        htmlStr = htmlStr.replace("cursor:pointer;", "");//去掉鼠标箭头变手的特效
                        htmlStr = htmlStr.replace("openPage(dataItem)", "");//去掉透明的部分的超链
                        var time = dataItem.total;
                        var color = "FFFFFF";
                        if (dataItem.actionType == "直接工作")
                            color = "0000FF";
                        if (dataItem.actionType == "普通间接")
                            color = "7030A0";
                        if (dataItem.actionType == "超级间接")
                            color = "00FFFF";
                        if (isContains(dataItem.message,"preparatory work"))
                            color = "FFC000";
                        if (isContains(dataItem.message,"No work"))
                            color = "000000";
                        if (dataItem.message == "Offclock")
                            color = "A6A6A6";
                        if (dataItem.message == "Onclock")
                            color = "00B050";
                        if (color=="000000" || color == "FFC000" || color == "7030A0" || color == "00FFFF")
                            htmlStr += "<a style='align:left;float:left;opacity:1;background:#" + color + ";height:22px;width:" + time / totalTime * 100 + "%;cursor:pointer;'ng-click='openPage(dataItem)'></a>";
                            else
                            htmlStr += "<div style='align:left;float:left;opacity:1;background:#" + color + ";height:22px;width:" + time / totalTime * 100 + "%;'></div>";
                        return htmlStr;
                    }
                }
            ];

            $scope.totStatisticsCtimedetailGridOptions = {
                dataSource: [],
                columns: columns,
                editable: false,
                selectable: "row",
                sortable: true,
                scrollable: true,
                pageable: false,
            };
            $scope.setDateType();
            $scope.changeColck();
            $scope.searchCtimeDetails();
        });
        $scope.setDateType=function() {
            if($scope.timeType==1){
                $("#dayRadio").attr("checked","checked");
                $("#periodsDayRadio").removeAttr("checked");
                $scope.dayDate=totStatisticsDetailService.dayDate;
            }else{
                $("#periodsDayRadio").attr("checked","checked");
                $("#dayRadio").removeAttr("checked");
                $scope.startDate=totStatisticsDetailService.startDate
                $scope.endDate=totStatisticsDetailService.endDate
            }
        }

        $scope.goJobDetailPage = function(){
            $state.go("main.totStatistics_ctimedetail.totJobdetail")
            $scope.initDateParam();
            $scope.setDateType();
            $scope.changeColck()

        };
        $scope.goClockDetailPage = function(){
            $state.go("main.totStatistics_ctimedetail.totClockdetail");//甘特图页非添加打卡页
            $scope.initDateParam();
            $scope.setDateType();
            $(document).ready(function(){
             $scope.changeColck();
             $scope.searchCtimeDetails();
            });
        };

        $scope.changeColck=function(){
            $("#changeClock").unbind("click").bind("click", function() {
                if($.isEmptyObject($scope.ClockWindow) || $scope.ClockWindow.selector != "#clockDetail") {
                    $scope.ClockWindow = $("#clockDetail");
                    $scope.ClockWindow.show();
                    $("#clockType").kendoComboBox({
                        dataTextField: "text",
                        dataValueField: "value",
                        dataSource: [
                            { text: "上班", value: "CLOCK_IN" },
                            { text: "下班", value: "CLOCK_OFF" },

                        ],
                        filter: "contains",
                        suggest: true,
                        index: 0
                    });
                    clockTypeData = $("#clockType").data("kendoComboBox");
                    $scope.ClockWindow.kendoWindow({
                        width: "800px",
                        height: "50%",
                        title: "编辑打卡",
                        actions: [
                            "Pin",
                            "Minimize",
                            "Maximize",
                            "Close"
                        ],
                        close:function(){
                            $scope.ClockWindow.hide();
                        }
                    }).data("kendoWindow").center().open();
                }else {
                    $scope.ClockWindow.show();
                    $scope.ClockWindow.data("kendoWindow").center().open();
                }

                $scope.CtimeChange();
                return;

            });
        }

        $scope.addJobRecord=function() {
            var param={employeeCode:$scope.employeeCode,
                dateTime:$scope.startDateTime,
                jobCode:job.value()}
            console.info("JobRecordParam:")
            console.info(param);
            totStatisticsService.addIndirectJobRecord(param,function (data) {
                // $scope.ClockWindow.hide();
                $scope.ClockWindow.data("kendoWindow").close();
                $scope.searchCtimeDetails();
            }, errMessageFun);
        }

        function errMessageFun(data) {
            alert(data.message);
        }


        $scope.colckDetailColumns=[{name:"打卡类型",width:"25%"},{name:"打卡时间",width:"38%"},{name:"打卡方式",width:"25%"},{name:"是否删除",width:"25%"}]
        $scope.colckDetailData=[];
        $scope.DeleteCtime=function(eve) {
            totStatisticsService.deleteClockTimedetail(function (data) {
                $scope.CtimeChange();
            },{keyId:eve.id});

        }

        $scope.addClockTimedetail=function() {
            $scope.clockType=clockTypeData.value();
            var  param={employeeCode:totStatisticsDetailService.employeeCode,
                employeeName:totStatisticsDetailService.employeeName,
                clockType:$scope.clockType,
                clockTime:$scope.clockDateTime,
                clockMethod:"HARD",
                clientId:totStatisticsDetailService.clientId,
                warehouseId:totStatisticsDetailService.warehouseId}
                console.info(param);
            totStatisticsService.addClockTimedetail(function (data) {
                $scope.CtimeChange();
            },param);
        }
        $scope.CtimeChange=function() {
            $scope.initDateParam();
            $scope.searchClockTime={emCode:totStatisticsDetailService.employeeCode,
                dayDate:totStatisticsDetailService.dayDate,
                beginTime:totStatisticsDetailService.startDate,
                endTime:totStatisticsDetailService.endDate}
            totStatisticsService.searchClockTimedetail(function (data) {
                $scope.colckDetailData=data;
            },$scope.searchClockTime);
        }
        //submit
        $scope.searchCtimeDetails = function () {
                $scope.initDateParam();
                 $scope.initSearchParam();
                 console.log("参数-->");
                 console.log($scope.searchParam)
                 if($scope.statisticsType==='cTime')
                 {
                     totStatisticsService.searchCtimedetail(function (data) {
                         totalTime = 1;
                         htmlStr ="";
                         tableData = data;
                         for (var i in tableData)
                         {
                             if (tableData[i].message=="Onclock" || tableData[i].message=="Offclock")
                                 totalTime += tableData[i].total;
                         }
                         var totStatisticsCtimedetailGridId = $("#totStatisticsCtimedetailGRID").data("kendoGrid");
                         totStatisticsCtimedetailGridId.setOptions({
                             dataSource: data
                         });
                     },$scope.searchParam );
                 }else{
                     totStatisticsService.getJobTotal(function (data) {
                         var totStatisticsJobTotalGridId = $("#totStatisticsJobTotalGRID").data("kendoGrid");
                         totStatisticsJobTotalGridId.setOptions({
                             dataSource: data
                         });

                     },$scope.searchParam);

                     //表格数据初始化
                     var totStatisticsJobDetailGridId = $("#totStatisticsJobDetailGRID").data("kendoGrid");
                     totStatisticsJobDetailGridId.setOptions({
                         dataSource: totService.getGridDataSourceByJobDetail($scope.searchParam)
                     });

                     // $scope.totStatisticsJobDetailGridOptions = commonService.gridMushiny({
                     //     dataSource: totService.getGridDataSourceByJobDetail($scope.searchParam)});
                 }


        };
        $scope.dayDatecheck = function () {
            if($scope.timeType==1)
            {
                $scope.startDate='';
                $scope.endDate='';
                totStatisticsDetailService.startDate='';
                totStatisticsDetailService.endDate='';
                totStatisticsDetailService.timeType=1
            }else{
                $scope.dayDate='';
                totStatisticsDetailService.dayDate='';
                totStatisticsDetailService.timeType=2

            }

        }
        $scope.initDateParam=function()
        {
            if($scope.timeType==1)
            {
                totStatisticsDetailService.dayDate= $scope.dayDate;
                totStatisticsDetailService.startDate='';
                totStatisticsDetailService.endDate='';
            }else{
                totStatisticsDetailService.dayDate='';
                totStatisticsDetailService.startDate=$scope.startDate;
                totStatisticsDetailService.endDate=$scope.endDate;
            }
        }

        $scope.initSearchParam=function()
        {
            $scope.searchParam={employeeCode:totStatisticsDetailService.employeeCode,
                startDate:totStatisticsDetailService.startDate,
                endDate:totStatisticsDetailService.endDate,
                dayDate:totStatisticsDetailService.dayDate,
                clientId:totStatisticsDetailService.clientId,
                warehouseId:totStatisticsDetailService.warehouseId}

        }

    });
})();