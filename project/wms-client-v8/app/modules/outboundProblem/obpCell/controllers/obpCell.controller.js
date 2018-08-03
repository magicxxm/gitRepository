/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpCellCtl", function ($scope, $rootScope, $window, $state, commonService, problemOutboundBaseService) {

    $window.localStorage["currentItem"] = "obpCell";

    $rootScope.obpCellTypeSource = problemOutboundBaseService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
    $rootScope.obpWallSource = problemOutboundBaseService.getDataSource({key: "getObpWall", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.obpCellRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "obpCellType.name",template: "<a ui-sref='main.obp_cell_type'>#: obpCellType.name # </a>", headerTemplate: "<span translate='OBP_CELL_TYPE'></span>"},
      {field: "obpWall.name",template: "<a ui-sref='main.obp_wall'>#: obpWall.name # </a>", headerTemplate: "<span translate='OBP_WALL'></span>"},
      {field: "xPos", headerTemplate: "<span translate='X_POS'></span>"},
      {field: "yPos", headerTemplate: "<span translate='Y_POS'></span>"},
      {field: "zPos", headerTemplate: "<span translate='Z_POS'></span>"},
      {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
      {field: "labelColor", headerTemplate: "<span translate='LABEL_COLOR'></span>"}
    ];
    $scope.obpCellGridOptions = commonService.gridMushiny({columns: columns, dataSource: problemOutboundBaseService.getGridDataSource("obpCell")});

  }).controller("obpCellCreateCtl", function ($scope, $state, problemOutboundBaseService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.create("obpCell", {
          "name": $scope.name,
          "typeId": $scope.obpCellType? $scope.obpCellType.id: null,
          "wallId": $scope.obpWall? $scope.obpWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor
        }, function () {
          $state.go("main.obp_cell");
        });
      }
    };
  }).controller("obpCellUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.update("obpCell", {
          "id": $scope.id,
          "name": $scope.name,
          "typeId": $scope.obpCellType? $scope.obpCellType.id: null,
          "wallId": $scope.obpWall? $scope.obpWall.id: null,
          "xPos": $scope.xPos,
          "yPos": $scope.yPos,
          "zPos": $scope.zPos,
          "orderIndex": $scope.orderIndex,
          "labelColor": $scope.labelColor
        }, function () {
          $state.go("main.obp_cell");
        });
      }
    };
  }).controller("obpCellReadCtl", function ($scope, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpCell", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();