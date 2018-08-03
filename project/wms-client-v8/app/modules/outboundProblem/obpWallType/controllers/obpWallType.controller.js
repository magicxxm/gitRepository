/**
 * Created by frank.zhou on 2017/05/010.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpWallTypeCtl", function ($scope, $window, $state, commonService, problemOutboundBaseService) {

    $window.localStorage["currentItem"] = "obpWallType";

    var columns = [
      {field: "name", template: "<a ui-sref='main.obpWallTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.obpWallTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: problemOutboundBaseService.getGridDataSource("obpWallType")});

  }).controller("obpWallTypeCreateCtl", function ($scope, $state, commonService, problemOutboundBaseService){
    // 保存
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.create("obpWallType", {
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_wall_type");
        });
      }
    };
  }).controller("obpWallTypeUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpWallType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    // 修改
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.update("obpWallType", {
          "id": $scope.id,
          "name": $scope.name,
          "numberOfRows": $scope.numberOfRows,
          "numberOfColumns": $scope.numberOfColumns,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_wall_type");
        });
      }
    };
  }).controller("obpWallTypeReadCtl", function ($scope, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpWallType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();