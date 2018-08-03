/**
 * Created by frank.zhou on 2017/05/05.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("rebinCellTypeCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "rebinCellType";

    var columns = [
      {field: "name", template: "<a ui-sref='main.rebinCellTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "width", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='DEPTH'></span><span>(mm)</span>"},
      {field: "volume", headerTemplate: "<span translate='VOLUME'></span><span>(mm³)</span>"},
      {field: "liftingCapacity", headerTemplate: "<span translate='LIFTING_CAPACITY'></span><span>(g)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.rebinCellTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("rebinCellType")});

  }).controller("rebinCellTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("rebinCellType", {
          "name": $scope.name,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_cell_type");
        });
      }
    };
  }).controller("rebinCellTypeUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebinCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("rebinCellType", {
          "id": $scope.id,
          "name": $scope.name,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity,
          "description": $scope.description
        }, function () {
          $state.go("main.rebin_cell_type");
        });
      }
    };
  }).controller("rebinCellTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("rebinCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();