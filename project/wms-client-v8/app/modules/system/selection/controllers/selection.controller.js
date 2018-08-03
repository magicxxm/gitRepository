/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("selectionCtl", function ($scope, $window, commonService, systemService){

    $window.localStorage["currentItem"] = "selection";

    var columns = [
      {field: "selectionKey", headerTemplate: "<span translate='SELECTION_KEY'></span>"},
      {field: "selectionValue", headerTemplate: "<span translate='SELECTION_VALUE'></span>"},
      {field: "resourceKey", headerTemplate: "<span translate='RESOURCE_KEY'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}];
    $scope.selectionGridOptions = commonService.gridMushiny({columns: columns, dataSource: systemService.getGridDataSource('selection')});

  }).controller("selectionCreateCtl", function ($scope, $state, systemService){
    $scope.validate = function (event) {
      event.preventDefault();
      if($scope.validator.validate()){
        systemService.create("selection", {
          "selectionKey": $scope.selectionKey,
          "selectionValue": $scope.selectionValue,
          "description": $scope.description,
          "orderIndex": $scope.orderIndex,
          "resourceKey": $scope.resourceKey,
          "additionalContent": $scope.additionalContent
        }, function(){
          $state.go("main.selection");
        });
      }
    };
  }).controller("selectionUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("selection", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event){
      event.preventDefault();
      systemService.update("selection", {
        "id": $scope.id,
        "selectionKey": $scope.selectionKey,
        "selectionValue": $scope.selectionValue,
        "description": $scope.description,
        "orderIndex": $scope.orderIndex,
        "resourceKey": $scope.resourceKey,
        "additionalContent": $scope.additionalContent
      }, function(){
        $state.go("main.selection");
      });
    }
  }).controller("selectionReadCtl", function($scope, $stateParams, systemService){
    systemService.read("selection", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();