/**
 * Created by frank.zhou on 2017/03/06.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("collateTemplateCtl", function ($scope, $window,  $state, commonService, outboundService) {

    $window.localStorage["currentItem"] = "collateTemplate";

    var columns = [
      {field: "name", template: "<a ui-sref='main.collateTemplateRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "minItems", headerTemplate: "<span translate='MIN_ITEMS'></span>"},
      {field: "maxItems", headerTemplate: "<span translate='MAX_ITEMS'></span>"},
      {field: "minShipments", headerTemplate: "<span translate='MIN_SHIPMENTS'></span>"},
      {field: "maxShipments", headerTemplate: "<span translate='MAX_SHIPMENTS'></span>"},
      {field: "pureExsd", headerTemplate: "<span translate='PURE_EXSD'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.collateTemplateGridOptions = commonService.gridMushiny({columns:columns,dataSource:outboundService.getGridDataSource("collateTemplate")});

  }).controller("collateTemplateCreateCtl", function ($scope, $window, $state, outboundService){
    $scope.pureExsd = "false";

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        outboundService.create("collateTemplate", {
          "name": $scope.name,
          "minItems": $scope.minItems,
          "maxItems": $scope.maxItems,
          "minShipments": $scope.minShipments,
          "maxShipments": $scope.maxShipments,
          "pureExsd": $scope.pureExsd==="true",
          "warehouseId": $window.localStorage["warehouseId"],
          "description": $scope.description
        }, function () {
          $state.go("main.collate_template");
        });
      }
    };
  }).controller("collateTemplateUpdateCtl", function ($scope, $window, $state, $stateParams, outboundService){
    outboundService.read("collateTemplate", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        outboundService.update("collateTemplate", {
          "id": $scope.id,
          "name": $scope.name,
          "minItems": $scope.minItems,
          "maxItems": $scope.maxItems,
          "minShipments": $scope.minShipments,
          "maxShipments": $scope.maxShipments,
          "pureExsd": $scope.pureExsd==="true",
          "warehouseId": $window.localStorage["warehouseId"],
          "description": $scope.description
        }, function () {
          $state.go("main.collate_template");
        });
      }
    };
  }).controller("collateTemplateReadCtl", function ($scope, $state, $stateParams, outboundService){
    outboundService.read("collateTemplate", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
  });
})();