/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("orderStrategyCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "orderStrategy";

    // 列
    var columns = [
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "createShippingOrder", headerTemplate: "<span translate='CREATE_SHIPPING_ORDER'></span>"},
      {field: "createFollowupPicks", headerTemplate: "<span translate='CREATE_FOLLOWUP_PICKS'></span>"},
      {field: "manualCreationIndex", headerTemplate: "<span translate='MANUAL_CREATION_INDEX'></span>"},
      {field: "preferMatchingStock", headerTemplate: "<span translate='PREFER_MATCHING_STOCK'></span>"},
      {field: "preferUnopened", headerTemplate: "<span translate='PREFER_UNOPENED'></span>"},
      {field: "useLockedLot", headerTemplate: "<span translate='USE_LOCKED_LOT'></span>"},
      {field: "useLockedStock", headerTemplate: "<span translate='USE_LOCKED_STOCK'></span>"},
      {field: "storageLocation", headerTemplate: "<span translate='DEFAULT_DESTINATION'></span>", template: function(item){
        return item.storageLocation? item.storageLocation.name: "";
      }}
    ];
    $scope.orderStrategyGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("orderStrategy")});

  }).controller("orderStrategyCreateCtl", function ($scope, $rootScope, $state, masterService) {
    $scope.createShippingOrder = "false"; $scope.createFollowupPicks = "false";
    $scope.manualCreationIndex = "false"; $scope.preferMatchingStock = "false";
    $scope.preferUnopened = "false";
    $scope.useLockedLot = "false"; $scope.useLockedStock = "false";
    $scope.client = {};

    $scope.changeClient = function(){
      $scope.storageLocation = {};
    };

    $scope.selectStorageLocation = function(client){
      if(client == null) return;
      $rootScope.selectInWindow({
        title: "STORAGE_LOCATION",
        width: 662,
        srcKey: "storageLocation",
        srcColumns: [
          {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
          {field: "storageLocationType.name", headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>"}
        ],
        init: function(options){
          options.showClient = true;
          options.disableClient = true;
          $rootScope.client = client;
        },
        back: function(data){
          $scope.storageLocation = {id: data.id, name: data.name};
        }
      });
    };

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("orderStrategy", {
          "name": $scope.name,
          "createShippingOrder": $scope.createShippingOrder,
          "createFollowupPicks": $scope.createFollowupPicks,
          "manualCreationIndex": $scope.manualCreationIndex,
          "preferMatchingStock": $scope.preferMatchingStock,
          "preferUnopened": $scope.preferUnopened,
          "useLockedLot": $scope.useLockedLot,
          "useLockedStock": $scope.useLockedStock,
          "defaultDestinationId": $scope.storageLocation? $scope.storageLocation.id: null,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.order_strategy");
        });
      }
    };
  }).controller("orderStrategyUpdateCtl", function ($scope, $rootScope, $state, $stateParams, masterService) {
    masterService.read("orderStrategy", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });

    $scope.changeClient = function(){
      $scope.storageLocation = {};
    };

    $scope.selectStorageLocation = function(client){
      if(client == null) return;
      $rootScope.selectInWindow({
        title: "STORAGE_LOCATION",
        width: 662,
        srcKey: "storageLocation",
        srcColumns: [
          {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
          {field: "storageLocationType.name", headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>"}
        ],
        init: function(options){
          options.showClient = true;
          options.disableClient = true;
          $rootScope.client = client;
        },
        back: function(data){
          $scope.storageLocation = {id: data.id, name: data.name};
        }
      });
    };

    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("orderStrategy", {
          "id": $scope.id,
          "name": $scope.name,
          "createShippingOrder": $scope.createShippingOrder,
          "createFollowupPicks": $scope.createFollowupPicks,
          "manualCreationIndex": $scope.manualCreationIndex,
          "preferMatchingStock": $scope.preferMatchingStock,
          "preferUnopened": $scope.preferUnopened,
          "useLockedLot": $scope.useLockedLot,
          "useLockedStock": $scope.useLockedStock,
          "defaultDestinationId": $scope.storageLocation? $scope.storageLocation.id: null,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.order_strategy");
        });
      }
    };
  }).controller("orderStrategyReadCtl", function ($scope, $stateParams, masterService) {
    masterService.read("orderStrategy", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
  });
})();