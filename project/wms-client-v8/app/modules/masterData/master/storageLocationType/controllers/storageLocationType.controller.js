/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("storageLocationTypeCtl", function ($scope, $rootScope, $translate, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "storageLocationType";

    $rootScope.inventoryStateSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "INVENTORY_STATE"}
    });

    $rootScope.storageTypeSource = masterService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "STORAGE_TYPE"}
    });

    var columns = [
      {field: "name", template: "<a ui-sref='main.storageLocationTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "width", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='DEPTH'></span><span>(mm)</span>"},
      {field: "volume", headerTemplate: "<span translate='VOLUME'></span><span>(mm³)</span>"},
      {field: "liftingCapacity", headerTemplate: "<span translate='LIFTING_CAPACITY'></span><span>(g)</span>"},
      {field: "maxItemDataAmount", headerTemplate: "<span translate='MAX_ITEM_DATA_AMOUNT'></span>"},
      {field: "inventoryState", headerTemplate: "<span translate='INVENTORY_STATE'></span>", template: function(item){
        return $translate.instant(item.inventoryState);
      }},
      {field: "storageType", headerTemplate: "<span translate='STORAGE_TYPE'></span>", template: function(item){
        return $translate.instant(item.storageType);
      }},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.storageLocationTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("storageLocationType")});

  }).controller("storageLocationTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("storageLocationType", {
          "name": $scope.name,
          "description": $scope.description,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "maxItemDataAmount": $scope.maxItemDataAmount,
          "liftingCapacity": $scope.liftingCapacity,
          "inventoryState": $scope.inventoryState? $scope.inventoryState.selectionValue: null,
          "storageType": $scope.storageType? $scope.storageType.selectionValue: null
         }, function () {
           $state.go("main.storage_location_type");
         });
      }
    };
  }).controller("storageLocationTypeUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("storageLocationType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.inventoryState = {selectionValue: data.inventoryState};
      $scope.storageType = {selectionValue: data.storageType};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("storageLocationType", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "maxItemDataAmount": $scope.maxItemDataAmount,
          "liftingCapacity": $scope.liftingCapacity,
          "inventoryState": $scope.inventoryState? $scope.inventoryState.selectionValue: null,
          "storageType": $scope.storageType? $scope.storageType.selectionValue: null
        }, function () {
          $state.go("main.storage_location_type");
        });
      }
    };
  }).controller("storageLocationTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("storageLocationType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.inventoryState = {selectionValue: data.inventoryState};
      $scope.storageType = {selectionValue: data.storageType};
    });
  });
})();