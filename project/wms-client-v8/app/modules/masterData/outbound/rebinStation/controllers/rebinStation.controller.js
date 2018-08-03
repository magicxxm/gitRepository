/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinStationCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinStation";

    $rootScope.rebinStationTypeSource = masterService.getDataSource({
      key: "getRebinStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.rebinStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "rebinStationType.name",
        template: "<a ui-sref='main.rebin_station_type'>#: rebinStationType.name # </a>",
        headerTemplate: "<span translate='REBIN_STATION_TYPE'></span>"
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
    $scope.rebinStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("rebinStation")
    });

  }).controller("rebinStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinStation", {
          "name": $scope.name,
          "typeId": $scope.rebinStationType ? $scope.rebinStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_station");
        });
      }
    };
  }).controller("rebinStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebinStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinStation", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.rebinStationType ? $scope.rebinStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_station");
        });
      }
    };
  }).controller("rebinStationReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("rebinStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();