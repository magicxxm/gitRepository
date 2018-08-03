/**
 * Created by frank.zhou on 2017/05/26.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("hardwareCtl", function ($scope, $rootScope, $window, $state, $translate, commonService, masterService) {

    $window.localStorage["currentItem"] = "hardware";

    $rootScope.hardwareTypeSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "HARDWARE_TYPE"}
    });

    var columns = [
      {field: "name", template: "<a ui-sref='main.hardwareRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "ipAddress", headerTemplate: "<span translate='IP_ADDRESS'></span>"},
      {field: "portNumber", headerTemplate: "<span translate='PORT_NUMBER'></span>"},
      {field: "hardwareType", headerTemplate: "<span translate='HARDWARE_TYPE'></span>", template: function(item){
        return item.hardwareType? $translate.instant(item.hardwareType): "";
      }}
    ];
    $scope.hardwareGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("hardware")});

  }).controller("hardwareCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("hardware", {
          "name": $scope.name,
          "ipAddress": $scope.ipAddress,
          "portNumber" : $scope.portNumber,
          "hardwareType": $scope.hardwareType? $scope.hardwareType.selectionValue: ""
        }, function () {
          $state.go("main.hardware");
        });
      }
    };
  }).controller("hardwareUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("hardware", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.hardwareType = {"selectionValue": data.hardwareType};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("hardware", {
          "id": $scope.id,
          "name": $scope.name,
          "ipAddress": $scope.ipAddress,
          "portNumber" : $scope.portNumber,
          "hardwareType": $scope.hardwareType? $scope.hardwareType.selectionValue: ""
        }, function () {
          $state.go("main.hardware");
        });
      }
    };
  }).controller("hardwareReadCtl", function ($scope, $stateParams, masterService){
    masterService.read("hardware", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();