(function () {
    'use strict';

    angular.module('myApp').controller("campareBoxCtl", function ($scope,campareBoxService, commonService) {
        setTimeout(function () {$("#compareBoxId").focus();}, 600);
        $scope.option = {"startDate":$scope.startTime,"endDate":$scope.endTime,"compare":$scope.compare,"seek":$scope.seek};
        var compareBoxColumns = [
            {field: "packDate", width: 50, headerTemplate: "<span>日期</span>"},
            {field: "shipmentNo", width: 50,template: "<a ui-sref='main.shipment_detail({shipment:dataItem.shipmentNo})'>#: shipmentNo # </a>", headerTemplate: "<span >Shipment Id</span>"},
            {field: "processPath", width: 50, headerTemplate: "<span>Process Path</span>"},
            {field: "usedBoxType",template:"<a ui-sref='main.box_type'>#: usedBoxType #</a>", width: 50, headerTemplate: "<span >使用箱型</span>"},
            {field: "recommendBoxType",template:"<a ui-sref='main.box_type'>#: recommendBoxType #</a>" ,width: 50, headerTemplate: "<span>推荐箱型</span>"},
            {field: "useBoxVolume", width: 50, headerTemplate: "<span>使用箱型体积</span><br><span>(mm³)</span>"},
            {field: "recommendBoxVolume", width: 50, headerTemplate: "<span>推荐箱型体积</span><br><span>(mm³)</span>"},
            {field: "difference", width: 50, headerTemplate: "<span>差异百分比</span>"},
            {field: "operator", width: 50, headerTemplate: "<span>包装员工</span>"}
        ];
        $scope.compareBoxGridOptions = commonService.gridMushiny({
            columns: compareBoxColumns,
            dataSource: null,
            height:$(document.body).height() - 200
        });
        $scope.searchKeyUp=function(e){
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode != 13) return;
            $scope.option["startDate"]=$scope.startTime;
            $scope.option["endDate"]=$scope.endTime;
            $scope.option["compare"]=$scope.compare;
            $scope.option["seek"]=$scope.seek;
            compareBox($scope.option);
        };
        $scope.search=function(){
            $scope.option ["startDate"]=$scope.startTime;
            $scope.option ["endDate"]=$scope.endTime;
            $scope.option ["compare"]=$scope.compare;
            $scope.option ["seek"]=$scope.seek;
            compareBox($scope.option);
        };
        function compareBox(data) {
            if (data.startDate == undefined)
                data.startDate = "";
            if (data.endDate == undefined)
                data.endDate = "";
            if (data.compare == undefined)
                data.compare = "";
            if (data.seek == undefined)
                data.seek = "";
            campareBoxService.compareBoxType(data,function(datas){
                $scope.compareBoxGridOptions = commonService.gridMushiny({
                    columns: compareBoxColumns,
                    dataSource: datas,
                    height:$(document.body).height() - 200
                });
                setTimeout(function () {
                    var grid = $("#inputValidityQueryGrid").data("kendoGrid");
                    grid.setDataSource(new kendo.data.DataSource({data: datas}));
                }, 100);
            })
        }
    });
})();


