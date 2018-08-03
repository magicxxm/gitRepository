/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickPackCellTypeCtl", function ($scope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "pickPackCellType";

    var columns = [
      {field: "name", template: "<a ui-sref='main.pickPackCellTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "width", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='DEPTH'></span><span>(mm)</span>"},
      {field: "volume", headerTemplate: "<span translate='VOLUME'></span><span>(mm³)</span>"},
      {field: "liftingCapacity", headerTemplate: "<span translate='LIFTING_CAPACITY'></span><span>(g)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.pickPackCellTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("pickPackCellType")});

  }).controller("pickPackCellTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("pickPackCellType", {
          "name": $scope.name,
          "description": $scope.description,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity
         }, function () {
           $state.go("main.pick_pack_cell_type");
         });
      }
    };
  }).controller("pickPackCellTypeUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("pickPackCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("pickPackCellType", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description,
          "height": $scope.height,
          "width": $scope.width,
          "depth": $scope.depth,
          "volume": $scope.height*$scope.width*$scope.depth,
          "liftingCapacity": $scope.liftingCapacity
        }, function () {
          $state.go("main.pick_pack_cell_type");
        });
      }
    };
  }).controller("pickPackCellTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("pickPackCellType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();