/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinCellCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinCell";

    $rootScope.rebinCellTypeSource = masterService.getDataSource({key: "getRebinCellType", text: "name", value: "id"});
    $rootScope.rebinWallSource = masterService.getDataSource({key: "getRebinWall", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.rebinCellRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "rebinCellType.name",template: "<a ui-sref='main.rebin_cell_type'>#: rebinCellType.name # </a>", headerTemplate: "<span translate='REBIN_CELL_TYPE'></span>"},
      {field: "rebinWall.name",template: "<a ui-sref='main.rebin_wall'>#: rebinWall.name # </a>", headerTemplate: "<span translate='REBIN_WALL'></span>"},
      {field: "xPos", headerTemplate: "<span translate='X_POS'></span>"},
      {field: "yPos", headerTemplate: "<span translate='Y_POS'></span>"},
      {field: "zPos", headerTemplate: "<span translate='Z_POS'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "labelColor", headerTemplate: "<span translate='LABEL_COLOR'></span>"}
    ];
    $scope.rebinCellGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("rebinCell")});

  }).controller("rebinCellCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinCell", {
          "name": $scope.name,
          "typeId": $scope.rebinCellType? $scope.rebinCellType.id: null,
          "rebinWallId": $scope.rebinWall? $scope.rebinWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor
        }, function () {
          $state.go("main.rebin_cell");
        });
      }
    };
  }).controller("rebinCellUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebinCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinCell", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.rebinCellType? $scope.rebinCellType.id: null,
          "rebinWallId": $scope.rebinWall? $scope.rebinWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor
        }, function () {
          $state.go("main.rebin_cell");
        });
      }
    };
  }).controller("rebinCellReadCtl", function ($scope, $stateParams, masterService){
    masterService.read("rebinCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();