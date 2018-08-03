/**
 * Created by frank.zhou on 2017/05/09.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebatchStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebatchStation";

    $rootScope.rebatchStationTypeSource = masterService.getDataSource({
      key: "getRebatchStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.rebatchStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "rebatchStationType.name",
        template: "<a ui-sref='main.rebatch_station_type'>#: rebatchStationType.name # </a>",
        headerTemplate: "<span translate='REBATCH_STATION_TYPE'></span>"
      },
      {
        headerTemplate: "<span translate='STATE'></span>", template: function (item) {
        return item.user ? "BUSY" : "IDLE";
      }
      },
      {
        field: "user", headerTemplate: "<span translate='OPERATOR'></span>", template: function (item) {
        return item.user ? item.user.username : "";
      }
      },
      {field: "workstation.name", headerTemplate: "<span translate='WORKSTATION'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebatchStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("rebatchStation")
    });

  }).controller("rebatchStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebatchStation", {
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.rebatchStationType ? $scope.rebatchStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.rebatch_station");
        });
      }
    };
  }).controller("rebatchStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebatchStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebatchStation", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.rebatchStationType ? $scope.rebatchStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.rebatch_station");
        });
      }
    };
  }).controller("rebatchStationReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("rebatchStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();