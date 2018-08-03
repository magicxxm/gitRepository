/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinWallCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinWall";

    $rootScope.rebinWallTypeSource =  masterService.getDataSource({key: "getRebinWallType", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.rebinWallRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "rebinWallType.name",template: "<a ui-sref='main.rebin_wall_type'>#: rebinWallType.name # </a>", headerTemplate: "<span translate='REBIN_WALL_TYPE'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebinWallGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("rebinWall")});

  }).controller("rebinWallCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinWall", {
          "name": $scope.name,
          "numberOfRows": $scope.rebinWallType? $scope.rebinWallType.numberOfRows: 0,
          "numberOfColumns": $scope.rebinWallType? $scope.rebinWallType.numberOfColumns: 0,
          "typeId": $scope.rebinWallType? $scope.rebinWallType.id: null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_wall");
        });
      }
    };
  }).controller("rebinWallUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebinWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinWall", {
          "id": $scope.id,
          "name": $scope.name,
          "numberOfRows": $scope.rebinWallType? $scope.rebinWallType.numberOfRows: 0,
          "numberOfColumns": $scope.rebinWallType? $scope.rebinWallType.numberOfColumns: 0,
          "typeId": $scope.rebinWallType? $scope.rebinWallType.id: null,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_wall");

        });
      }
    };
  }).controller("rebinWallReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebinWall", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();