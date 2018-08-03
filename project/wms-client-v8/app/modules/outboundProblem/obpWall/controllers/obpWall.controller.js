/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpWallCtl", function ($scope, $rootScope, $window,  $state, commonService, problemOutboundBaseService) {

    $window.localStorage["currentItem"] = "obpWall";

    $rootScope.obpWallTypeSource =  problemOutboundBaseService.getDataSource({key: "getObpWallType", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.obpWallRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "obpWallType.name",template: "<a ui-sref='main.obp_wall_type'>#: obpWallType.name # </a>", headerTemplate: "<span translate='OBP_WALL_TYPE'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.obpWallGridOptions = commonService.gridMushiny({columns: columns, dataSource: problemOutboundBaseService.getGridDataSource("obpWall")});

  }).controller("obpWallCreateCtl", function ($scope, $state, problemOutboundBaseService){
    $scope.obpCellTypeSource = problemOutboundBaseService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.create("obpWall", {
          "name": $scope.name,
          "numberOfRows": $scope.obpWallType? $scope.obpWallType.numberOfRows: 0,
          "numberOfColumns": $scope.obpWallType? $scope.obpWallType.numberOfColumns: 0,
          "typeId": $scope.obpWallType? $scope.obpWallType.id: null,
          "obpCellTypeId": $scope.obpCellType? $scope.obpCellType.id: null,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_wall");
        });
      }
    };
  }).controller("obpWallUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.update("obpWall", {
          "id": $scope.id,
          "name": $scope.name,
          "numberOfRows": $scope.obpWallType? $scope.obpWallType.numberOfRows: 0,
          "numberOfColumns": $scope.obpWallType? $scope.obpWallType.numberOfColumns: 0,
          "typeId": $scope.obpWallType? $scope.obpWallType.id: null,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_wall");
        });
      }
    };
  }).controller("obpWallReadCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();