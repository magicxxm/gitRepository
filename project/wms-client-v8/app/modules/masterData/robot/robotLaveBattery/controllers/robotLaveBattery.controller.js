(function () {
    'use strict';

    angular.module('myApp').controller("robotLaveBatteryCtl",function($scope,robotLaveBatteryService,commonService,problemOutboundBaseService){
        var robotLaveBatteryColumns = [
            {field: "robotId", width: 50, headerTemplate: "<span translate='车辆号码'></span>"},
            {field: "voltage", width: 50, headerTemplate: "<span translate='电压(mV)'></span>"},
            {field: "laveBattery",width: 50, headerTemplate: "<span translate='电量(%)'></span>"}
        ];
        setTimeout(function () {$("#robotBatteryId").focus();},600);

        function initPage(){
            robotLaveBatteryService.robotLaveBattery("",function(data){
                console.log("电量", data);
                $scope.robotBatteryOptions=problemOutboundBaseService.grid(
                    data,robotLaveBatteryColumns,$(document.body).height() - 180);
                setTimeout(function () {
                    var grid = $("#robotGrid").data("kendoGrid");
                    grid.setDataSource(new kendo.data.DataSource({data: data}));
                }, 100);
            });
        }
        initPage();

        function search(){
            robotLaveBatteryService.robotLaveBattery($scope.robotId,function(data){
                $scope.tripManagerOptions=problemOutboundBaseService.grid(
                    data,robotLaveBatteryColumns,$(document.body).height() - 180);
                setTimeout(function () {
                    var grid = $("#robotGrid").data("kendoGrid");
                    grid.setDataSource(new kendo.data.DataSource({data: data}));
                }, 100);
            });
        }

        $scope.robotBattery=function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode != 13) return;
            search();
        };

        $scope.robotBatterySearch=function(){
            search();
        }
    });
})();
