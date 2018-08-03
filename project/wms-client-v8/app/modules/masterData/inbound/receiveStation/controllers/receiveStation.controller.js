/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "receiveStation";

    $rootScope.receiveStationTypeSource = masterService.getDataSource({
      key: "getReceiveStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.receiveStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "receiveStationType.name",
        template: "<a ui-sref='main.receive_station_type'>#: receiveStationType.name # </a>",
        headerTemplate: "<span translate='RECEIVE_STATION_TYPE'></span>"
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
    $scope.receiveStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("receiveStation")
    });

  }).controller("receiveStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("receiveStation", {
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.receiveStationType ? $scope.receiveStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.receive_station");
        });
      }
    };
  }).controller("receiveStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("receiveStation", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.receiveStationType ? $scope.receiveStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.receive_station");
        });
      }
    };
  }).controller("receiveStationReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();