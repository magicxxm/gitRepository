/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("ibpStationCtl", function ($scope, $rootScope, $window, $state, commonService, problemInboundBaseService) {

    $window.localStorage["currentItem"] = "ibpStation";

    $rootScope.ibpStationTypeSource = problemInboundBaseService.getDataSource({
      key: "getIbpStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = problemInboundBaseService.getDataSource({
      key: "getWorkstation",
      text: "name",
      value: "id"
    });

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.ibpStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "ibpStationType.name",
        template: "<a ui-sref='main.ibp_station_type'>#: ibpStationType.name # </a>",
        headerTemplate: "<span translate='IBP_STATION_TYPE'></span>"
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
    $scope.ibpStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: problemInboundBaseService.getGridDataSource("ibpStation")
    });

  }).controller("ibpStationCreateCtl", function ($scope, $state, problemInboundBaseService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemInboundBaseService.create("ibpStation", {
          "name": $scope.name,
          "typeId": $scope.ibpStationType ? $scope.ibpStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.ibp_station");
        });
      }
    };
  }).controller("ibpStationUpdateCtl", function ($scope, $state, $stateParams, problemInboundBaseService) {
    problemInboundBaseService.read("ibpStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemInboundBaseService.update("ibpStation", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.ibpStationType ? $scope.ibpStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.ibp_station");
        });
      }
    };
  }).controller("ibpStationReadCtl", function ($scope, $state, $stateParams, problemInboundBaseService) {
    problemInboundBaseService.read("ibpStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();