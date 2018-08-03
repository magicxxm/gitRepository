/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("warehouseCtl", function ($scope, $window, commonService, systemService) {

    $window.localStorage["currentItem"] = "warehouse";

    var columns = [
      {
        field: "warehouseNo",
        template: "<a ui-sref='main.warehouseRead({id:dataItem.id})'>#: warehouseNo # </a>",
        headerTemplate: "<span translate='WAREHOUSE_NO'></span>"
      },
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "email", headerTemplate: "<span translate='EMAIL'></span>"},
      {field: "phone", headerTemplate: "<span translate='PHONE'></span>"},
      {field: "fax", headerTemplate: "<span translate='FAX'></span>"}
    ];
    $scope.warehouseGridOptions = commonService.gridMushiny({columns: columns, dataSource: systemService.getGridDataSource('warehouse')});

  }).controller("warehouseCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("warehouse", {
          "warehouseNo": $scope.warehouseNo,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "fax": $scope.fax
        }, function () {
          $state.go("main.warehouse");
        });
      }
    }
  }).controller("warehouseUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("warehouse", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.update("warehouse", {
          "id": $scope.id,
          "warehouseNo": $scope.warehouseNo,
          "name": $scope.name,
          "email": $scope.email,
          "phone": $scope.phone,
          "fax": $scope.fax
        }, function () {
          $state.go("main.warehouse");
        });
      }
    }
  }).controller("warehouseReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("warehouse", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();