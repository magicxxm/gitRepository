(function () {
    'use strict';

    angular.module('myApp').controller("tripManagerCtl",function($scope,$rootScope,tripManagerService,commonService,problemOutboundBaseService,internalToolService,$state){
        $scope.startTime="";
        $scope.endTime="";
        $("#multiselect").val("");
        $scope.checked=false;
        $("#multiselect").kendoMultiSelect();
        $scope.seek="";
        var multiselect = $("#multiselect").data("kendoMultiSelect");
        var tripManagerColumns = [
            {field: "createDate", width: 50, headerTemplate: "<span translate='创建时间'></span>"},
            {field: "modifiedDate", width: 50, headerTemplate: "<span translate='修改时间'></span>"},
            {field: "tripType", width: 50, headerTemplate: "<span translate='调度单类型'></span>"},
            {field: "tripState", width: 50, headerTemplate: "<span translate='状态'></span>"},
            {field: "driveId",width: 50, headerTemplate: "<span translate='车辆号码'></span>"},
            {field: "podName" ,width: 50, headerTemplate: "<span translate='货架号码'></span>"},
            {field: "stationName", width: 50, headerTemplate: "<span translate='物理工作站'></span>"},
            {field: "logicName", width: 50, headerTemplate: "<span translate='逻辑工作站'></span>"},
            {field: "itemDataAmount", width: 50, headerTemplate: "<span translate='操作商品数量'></span>"},
            {field: "storageLocationAmount", width: 50, headerTemplate: "<span translate='操作货位数量'></span>"},
            {field: "faceAmount", width: 50, headerTemplate: "<span translate='操作面数量'></span>"},
            {field: "charger", width: 50, headerTemplate: "<span translate='充电桩号码'></span>"},
            {field: "userName", width: 50, headerTemplate: "<span translate='创建人员'></span>"},
            {field: "time", width: 50, headerTemplate: "<span translate='调度单时长'></span><br><span>(分钟)</span>"}
        ];

        initPage();

        //初始化页面
        function initPage() {
            var url = "masterdata/robot/robots/page-trip-manager"+"?startTime="+$scope.startTime+"&endTime="+$scope.endTime+"&type="+$scope.type+"&isFinish="+$scope.checked+"&seek="+$scope.seek;
            $rootScope.exportTripUrl=url;
            $scope.tripManagerOptions = commonService.gridMushiny({
                columns: tripManagerColumns,
                dataSource: internalToolService.getGridDataSourceSpecial(url+"&isExport=false"),
                height:$(document.body).height() - 238
            });
        }

        $scope.search=function(){
            if ($("#yetFinish").is(':checked')) {
                $scope.checked=true;
            }
            multiselect.value($scope.type);
            console.log("调度单类型：",$scope.type);
            var url = "masterdata/robot/robots/page-trip-manager"+"?startTime="+$scope.startTime+"&endTime="+$scope.endTime+"&type="+$scope.type+"&isFinish="+$scope.checked+"&seek=";
            $rootScope.exportTripUrl=url;
            var grid = $("#tripGrid").data("kendoGrid");
            grid.setOptions(commonService.gridMushiny({
                    columns: tripManagerColumns,
                    dataSource: internalToolService.getGridDataSourceSpecial(url+"&isExport=false"),
                    height: $(document.body).height() - 238
                })
            )
        };

        $scope.wcsTripManager=function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode != 13) return;
            $scope.tripSearch();
        };

        //根据时间，调度单类型，是否显示Finish,搜索条件搜索调度单
        $scope.tripSearch=function(){
            if ($("#yetFinish").is(':checked')) {
                $scope.checked=true;
            }
            multiselect.value($scope.type);
            console.log("调度单类型：",$scope.type);
            var url = "masterdata/robot/robots/page-trip-manager"+"?startTime="+$scope.startTime+"&endTime="+$scope.endTime+"&type="+$scope.type+"&isFinish="+$scope.checked+"&seek="+$scope.seek;
            $rootScope.exportTripUrl=url;
            var grid = $("#tripGrid").data("kendoGrid");
            grid.setOptions(commonService.gridMushiny({
                columns: tripManagerColumns,
                dataSource: internalToolService.getGridDataSourceSpecial(url+"&isExport=false"),
                height: $(document.body).height() - 238
             })
             )
        };

        $scope.export = function () {
            $state.go("main.tripmanager_export");
        }
    }).controller("tripManagerExportCtl", function ($scope, $rootScope, $state, Excel, masterService, commonService,tripManagerService) {

        var exportColumns = [
            {field: "createDate", width: 45, headerTemplate: "<span translate='创建时间'></span>"},
            {field: "modifiedDate", width: 45, headerTemplate: "<span translate='修改时间'></span>"},
            {field: "tripType", width: 45, headerTemplate: "<span translate='调度单类型'></span>"},
            {field: "tripState", width: 40, headerTemplate: "<span translate='状态'></span>"},
            {field: "driveId",width: 40, headerTemplate: "<span translate='车辆号码'></span>"},
            {field: "podName" ,width: 50, headerTemplate: "<span translate='货架号码'></span>"},
            {field: "stationName", width: 50, headerTemplate: "<span translate='物理工作站'></span>"},
            {field: "logicName", width: 50, headerTemplate: "<span translate='逻辑工作站'></span>"},
            {field: "itemDataAmount", width: 50, headerTemplate: "<span translate='操作商品数量'></span>"},
            {field: "storageLocationAmount", width: 50, headerTemplate: "<span translate='操作货位数量'></span>"},
            {field: "faceAmount", width: 50, headerTemplate: "<span translate='操作面数量'></span>"},
            {field: "charger", width: 40, headerTemplate: "<span translate='充电桩号码'></span>"},
            {field: "userName", width: 40, headerTemplate: "<span translate='创建人员'></span>"},
            {field: "time", width: 50, headerTemplate: "<span translate='调度单时长(分钟)'></span>"}
        ];
        var url=$rootScope.exportTripUrl+"&isExport=true";
        tripManagerService.exportTrip(url,function (datas) {
            $scope.tripExportGridOptions = commonService.gridMushiny1({
                height: $(document.body).height() - 150,
                columns: exportColumns,
                dataSource: datas.content
            });
        });
        var columnsName = ["创建时间","修改时间","调度单类型","状态","车辆号码","货架号码","物理工作站","逻辑工作站","操作商品数量","操作货位数量","操作面数量","充电桩号码","创建人员","调度单时长(分钟)"];
        //列宽
        var cellWidths = [150,150,100,100,100,100,100,100,100,100,100,100,100,130];
        $scope.OKExport = function () {
            Excel.exportToExcel("#tripExportGrid", "调度单", cellWidths, columnsName);
        };
    });
})();

