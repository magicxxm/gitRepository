(function () {
    'use strict';
    angular.module('myApp').controller("robotCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {
        $window.localStorage["currentItem"] = "robot";
        var columns = [
            {field: "robot", template: "<a ui-sref='main.robotRead({id:dataItem.id})'>#: robot # </a>", headerTemplate: "<span translate='车辆ID'></span>"},
            {field: "hardware",  headerTemplate: "<span translate='硬件版本'></span>"},
            {field: "software",  headerTemplate: "<span translate='软件版本'></span>"},
            {field: "priduction",  headerTemplate: "<span translate='出厂日期'></span>", template: function (dataItem) {
              var priduction = dataItem.priduction;
              if (typeof priduction != "undefined"){
                return priduction.substring(0, 10);
              }else{
                return "";
              }
            }},
            {field: "acc", template: function (dataItem) {
                var accDur = dataItem.acc;
                var days = 0;
                var hours = 0;
                var mins = 0;
                var secs = 0;
                if (typeof accDur != "undefined"){
                    days = Math.floor(accDur/24/60/60);
                    hours = Math.floor(accDur/60/60 - days*24);
                    mins = Math.floor(accDur/60 - days*24*60 - hours*60);
                    secs = accDur - days*24*60*60 - hours*60*60 - mins*60;
                    return days + "天" + hours + "小时" + mins + "分钟" + secs + "秒";
                }else {
                    return "";
                }
            }, width: 150, headerTemplate: "<span translate='累计时长'></span>"},
            {field: "recently",  headerTemplate: "<span translate='最近维修时间'></span>", template: function (dataItem) {
              var recently = dataItem.recently;
              if (typeof recently != "undefined"){
                return recently.substring(0, 16).replace("T", " ");
              }else{
                return "";
              }
            }},
            {field: "inbreak",  headerTemplate: "<span translate='入侵检测次数'></span>"},
            {field: "cold",  headerTemplate: "<span translate='冷复位次数'></span>"},
            {field: "hot",  headerTemplate: "<span translate='热复位次数'></span>"},
            {field: "robotType", headerTemplate: "<span translate='车辆类型'></span>", template: function(item){
                return item.robotType? item.robotType.name: "";}},
            {field: "batteryNumber", headerTemplate: "<span translate='电池厂商编号'></span>"}];
        $scope.robotGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("robot")});

        $rootScope.robotTypeSource = masterService.getDataSource({key: "getRobotType", text: "name", value: "id"});

    }).controller("robotCreateCtl", function ($scope, $state, masterService){
      $scope.validate = function (event) {
        event.preventDefault();
        if ($scope.validator.validate()) {
          masterService.create("robot", {
            "robot": $scope.robot,
            "password": $scope.password,
            "robotTypeId": $scope.robotType ? $scope.robotType.id : null
          }, function () {
            $state.go("main.robot");
          });
        }
      };
    }).controller("robotUpdateCtl", function ($scope,$stateParams, $state, masterService){
      masterService.read("robot", $stateParams.id, function(data){
            for(var k in data) $scope[k] = data[k];
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
              masterService.update("robot", {
                    "id": $scope.id,
                    "robot": $scope.robot,
                    "robotTypeId": $scope.robotType ? $scope.robotType.id : null
                }, function () {
                    $state.go("main.robot");
                });
            }
        };
    }).controller("robotReadCtl", function ($scope, $state, $stateParams, masterService){
      masterService.read("robot", $stateParams.id, function(data){
            for(var k in data) $scope[k] = data[k];
            $scope.priductionText = $scope.priduction ? $scope.priduction.substring(0, 10) : "";
            $scope.recentlyText = $scope.recently ? $scope.recently.substring(0, 16).replace("T", " ") : "";
        });
    });
})();