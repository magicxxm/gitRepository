/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "pickStation";

    $rootScope.pickStationTypeSource = masterService.getDataSource({
      key: "getPickStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.pickStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "pickStationType.name",
        template: "<a ui-sref='main.pick_station_type'>#: pickStationType.name # </a>",
        headerTemplate: "<span translate='PICK_STATION_TYPE'></span>"
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
    $scope.pickStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("pickStation")
    });

  }).controller("pickStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("pickStation", {
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.pickStationType ? $scope.pickStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.pick_station");
        });
      }
    };
  }).controller("pickStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("pickStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("pickStation", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.pickStationType ? $scope.pickStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.pick_station");
        });
      }
    };
  }).controller("pickStationReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("pickStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();