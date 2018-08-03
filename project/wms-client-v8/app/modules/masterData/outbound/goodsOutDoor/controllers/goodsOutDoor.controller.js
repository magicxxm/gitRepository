/**
 * Created by frank.zhou on 2017/05/23.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("goodsOutDoorCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "goodsOutDoor";

    var columns = [
      {field: "name", template: "<a ui-sref='main.goodsOutDoorRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "sortCode", headerTemplate: "<span translate='SORT_CODE'></span>"},
      {field: "additionalContent", headerTemplate: "<span translate='ADDITIONAL_CONTENT'></span>"},
    ];
    $scope.goodsOutDoorGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("goodsOutDoor")});

  }).controller("goodsOutDoorCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("goodsOutDoor", {
          "name": $scope.name,
          "additionalContent": $scope.additionalContent
        }, function () {
          $state.go("main.goods_out_door");
        });
      }
    };
  }).controller("goodsOutDoorUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("goodsOutDoor", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("goodsOutDoor", {
          "id": $scope.id,
          "name": $scope.name,
          "additionalContent": $scope.additionalContent
        }, function () {
          $state.go("main.goods_out_door");
        });
      }
    };
  }).controller("goodsOutDoorReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("goodsOutDoor", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();