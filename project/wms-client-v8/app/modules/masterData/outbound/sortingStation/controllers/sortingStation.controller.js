/**
 * Created by frank.zhou on 2017/05/09.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("sortingStationCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "sortingStation";

    $rootScope.sortingStationTypeSource = masterService.getDataSource({
      key: "getSortingStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = masterService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.sortingStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "sortingStationType.name",
        template: "<a ui-sref='main.sorting_station_type'>#: sortingStationType.name # </a>",
        headerTemplate: "<span translate='SORTING_STATION_TYPE'></span>"
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
    $scope.sortingStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("sortingStation")
    });

  }).controller("sortingStationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("sortingStation", {
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.sortingStationType ? $scope.sortingStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.sorting_station");
        });
      }
    };
  }).controller("sortingStationUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("sortingStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("sortingStation", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "typeId": $scope.sortingStationType ? $scope.sortingStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null
        }, function () {
          $state.go("main.sorting_station");
        });
      }
    };
  }).controller("sortingStationReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("sortingStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();