
/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("itemGroupCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "itemGroup";

    $rootScope.parentItemGroupSource =  masterService.getDataSource({key: "getItemGroup", text: "name", value: "id"});

    var columns = [
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"}];

    $scope.itemGroupGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getListDataSource("itemGroup")});
  }).controller("itemGroupCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("itemGroup", {
          "name": $scope.name,
          "parentId": $scope.parentItemGroup? $scope.parentItemGroup.id: null,
          "orderIndex": $scope.orderIndex,
          "description":  $scope.description
        }, function () {
          $state.go("main.item_group");
        });
      }
    };
  }).controller("itemGroupUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("itemGroup", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("itemGroup", {
          "id": $scope.id,
          "name": $scope.name,
          "parentId": $scope.parentItemGroup? $scope.parentItemGroup.id: null,
          "orderIndex": $scope.orderIndex,
          "description":  $scope.description
        }, function () {
          $state.go("main.item_group");
        });
      }
    };
  });
})();