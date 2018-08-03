/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("obpCellTypeCtl", function ($scope, $rootScope, $window, $state, commonService, problemOutboundBaseService) {

    $window.localStorage["currentItem"] = "obpCellType";

    var columns = [
      {field: "name", template: "<a ui-sref='main.obpCellTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "width", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='DEPTH'></span><span>(mm)</span>"},
      {field: "volume", headerTemplate: "<span translate='VOLUME'></span><span>(mm³)</span>"},
      {field: "liftingCapacity", headerTemplate: "<span translate='LIFTING_CAPACITY'></span><span>(g)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.obpCellTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: problemOutboundBaseService.getGridDataSource("obpCellType")});

  }).controller("obpCellTypeCreateCtl", function ($scope, $state, problemOutboundBaseService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.create("obpCellType", {
          "name": $scope.name,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_cell_type");
        });
      }
    };
  }).controller("obpCellTypeUpdateCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        problemOutboundBaseService.update("obpCellType", {
          "id": $scope.id,
          "name": $scope.name,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity,
          "description": $scope.description
        }, function () {
          $state.go("main.obp_cell_type");
        });
      }
    };
  }).controller("obpCellTypeReadCtl", function ($scope, $state, $stateParams, problemOutboundBaseService){
    problemOutboundBaseService.read("obpCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();