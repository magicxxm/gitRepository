/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("areaCtl", function ($scope, $window, $rootScope, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "area";

    var columns = [{
        field: "name",
        template: "<a ui-sref='main.areaRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "useForGoodsIn", headerTemplate: "<span translate='USE_FOR_GOODS_IN'></span>"},
      {field: "useForGoodsOut", headerTemplate: "<span translate='USE_FOR_GOODS_OUT'></span>"},
      {field: "useForPicking", headerTemplate: "<span translate='USE_FOR_PICKING'> </span>"},
      {field: "useForReplenish", headerTemplate: "<span translate='USE_FOR_REPLENISH'> </span>"},
      {field: "useForStorage", headerTemplate: "<span translate='USE_FOR_STORAGE'> </span>"},
      {field: "useForTransfer", headerTemplate: "<span translate='USE_FOR_TRANSFER'> </span>"},
      {field: "useForReturn", headerTemplate: "<span translate='USE_FOR_RETURN'> </span>"},
      {field: "useForTransport", headerTemplate: "<span translate='USE_FOR_TRANSPORT'> </span>"}
    ];
    $scope.areaGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("area")});

 }).controller("areaCreateCtl", function ($scope, $state, masterService) {
    $scope.useForGoodsIn = "true";
    $scope.useForGoodsOut = "true";
    $scope.useForPicking = "true";
    $scope.useForReplenish = "true";
    $scope.useForStorage = "true";
    $scope.useForTransfer = "true";
    $scope.useForReturn = "true";
    $scope.useForTransport = "true";

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("area", {
          "name": $scope.name,
          "description": $scope.description,
          "useForGoodsIn": $scope.useForGoodsIn,
          "useForGoodsOut": $scope.useForGoodsOut,
          "useForPicking": $scope.useForPicking,
          "useForReplenish": $scope.useForReplenish,
          "useForStorage": $scope.useForStorage,
          "useForTransfer": $scope.useForTransfer,
          "useForReturn": $scope.useForReturn,
          "useForTransport": $scope.useForTransport,
          "clientId":$scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.area");
        });
      }
    };
  }).controller("areaUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("area", $stateParams.id, function(data){
      for (var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
      $scope.client = {id: data.clientId};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("area", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "useForGoodsIn": $scope.useForGoodsIn,
          "useForGoodsOut": $scope.useForGoodsOut,
          "useForPicking": $scope.useForPicking,
          "useForReplenish": $scope.useForReplenish,
          "useForStorage": $scope.useForStorage,
          "useForTransfer": $scope.useForTransfer,
          "useForReturn": $scope.useForReturn,
          "useForTransport": $scope.useForTransport,
          "clientId":$scope.client? $scope.client.id: null
         }, function () {
          $state.go("main.area");
        });
      }
    };
  }).controller("areaReadCtl", function ($scope, $state,$stateParams, masterService) {
    masterService.read("area", $stateParams.id, function(data){
      for (var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
  });
})();