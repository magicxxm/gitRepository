/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("stowStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "stowStation";

    $rootScope.stowStationTypeSource = masterService.getDataSource({key: "getStowStationType", text: "name", value: "id"});
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.stowStationRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "stowStationType.name", template: "<a ui-sref='main.stow_station_type'>#: stowStationType.name # </a>", headerTemplate: "<span translate='STOW_STATION_TYPE'></span>"},
      {headerTemplate: "<span translate='STATE'></span>", template: function(item){
        return item.user? "BUSY": "IDLE";
      }},
      {field: "user", headerTemplate: "<span translate='OPERATOR'></span>", template: function(item){
        return item.user? item.user.username: "";
      }},
      {field: "workstation.name", headerTemplate: "<span translate='WORKSTATION'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.stowStationGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("stowStation")});

  }).controller("stowStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("stowStation", {
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.stowStationType? $scope.stowStationType.id: null,
          "workstationId": $scope.workstation? $scope.workstation.id: null
        }, function () {
          $state.go("main.stow_station");
        });
      }
    };
  }).controller("stowStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("stowStation", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("stowStation", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.stowStationType? $scope.stowStationType.id: null,
          "workstationId": $scope.workstation? $scope.workstation.id: null
        }, function () {
          $state.go("main.stow_station");
        });
      }
    };
  }).controller("stowStationReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("stowStation", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();