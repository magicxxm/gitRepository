/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpStationCtl", function ($scope, $rootScope, $window, $state, commonService, problemOutboundBaseService) {

    $window.localStorage["currentItem"] = "obpStation";

    $rootScope.obpStationTypeSource = problemOutboundBaseService.getDataSource({
      key: "getObpStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = problemOutboundBaseService.getDataSource({
      key: "getWorkstation",
      text: "name",
      value: "id"
    });

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.obpStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "obpStationType.name",
        template: "<a ui-sref='main.obp_station_type'>#: obpStationType.name # </a>",
        headerTemplate: "<span translate='OBP_STATION_TYPE'></span>"
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
    $scope.obpStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: problemOutboundBaseService.getGridDataSource("obpStation")
    });

  }).controller("obpStationCreateCtl", function ($scope, $state, problemOutboundBaseService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.create("obpStation", {
          "name": $scope.name,
          "typeId": $scope.obpStationType ? $scope.obpStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_station");
        });
      }
    };
  }).controller("obpStationUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService) {
    problemOutboundBaseService.read("obpStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.update("obpStation", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.obpStationType ? $scope.obpStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_station");
        });
      }
    };
  }).controller("obpStationReadCtl", function ($scope, $state, $stateParams, problemOutboundBaseService) {
    problemOutboundBaseService.read("obpStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();