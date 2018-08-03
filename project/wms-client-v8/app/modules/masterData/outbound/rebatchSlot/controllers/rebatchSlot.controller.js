/**
 * Created by frank.zhou on 2017/05/27.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebatchSlotCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebatchSlot";

    var columns = [
      {field: "name", template: "<a ui-sref='main.rebatchSlotRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "labelColor", headerTemplate: "<span translate='LABEL_COLOR'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebatchSlotGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("rebatchSlot")});

  }).controller("rebatchSlotCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebatchSlot", {
          "name": $scope.name,
          "labelColor": $scope.labelColor,
          "orderIndex": $scope.orderIndex,
          "description": $scope.description
        }, function () {
          $state.go("main.rebatch_slot");
        });
      }
    };
  }).controller("rebatchSlotUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebatchSlot", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebatchSlot", {
          "id": $scope.id,
          "name": $scope.name,
          "labelColor": $scope.labelColor,
          "orderIndex": $scope.orderIndex,
          "description": $scope.description
        }, function () {
          $state.go("main.rebatch_slot");
        });
      }
    };
  }).controller("rebatchSlotReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebatchSlot", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();