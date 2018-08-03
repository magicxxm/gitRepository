/**
 * Created by frank.zhou on 2017/05/22.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("stocktakingStationCtl", function ($scope, $rootScope, $window, $state, commonService, ICQABaseService) {

    $window.localStorage["currentItem"] = "stocktakingStation";

    $rootScope.stocktakingStationTypeSource = ICQABaseService.getDataSource({
      key: "getStocktakingStationType",
      text: "name",
      value: "id"
    });
    $rootScope.workstationSource = ICQABaseService.getDataSource({key: "getWorkstation", text: "name", value: "id"});

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.stocktakingStationRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "stockTakingStationType.name",
        template: "<a ui-sref='main.stocktaking_station_type'>#: stockTakingStationType.name # </a>",
        headerTemplate: "<span translate='STOCKTAKING_STATION_TYPE'></span>"
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
    $scope.stocktakingStationGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: ICQABaseService.getGridDataSource("stocktakingStation")
    });

  }).controller("stocktakingStationCreateCtl", function ($scope, $state, ICQABaseService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        ICQABaseService.create("stocktakingStation", {
          "name": $scope.name,
          "typeId": $scope.stocktakingStationType ? $scope.stocktakingStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.stocktaking_station");
        });
      }
    };
  }).controller("stocktakingStationUpdateCtl", function ($scope, $state, $stateParams, ICQABaseService) {
    ICQABaseService.read("stocktakingStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        ICQABaseService.update("stocktakingStation", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.stockTakingStationType ? $scope.stockTakingStationType.id : null,
          "workstationId": $scope.workstation ? $scope.workstation.id : null,
          "description": $scope.description
        }, function () {
          $state.go("main.stocktaking_station");
        });
      }
    };
  }).controller("stocktakingStationReadCtl", function ($scope, $state, $stateParams, ICQABaseService) {
    ICQABaseService.read("stocktakingStation", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();