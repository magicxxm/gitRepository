/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("pickPackFieldTypeCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "pickPackFieldType";

    $rootScope.pickPackCellTypeSource = masterService.getDataSource({key: "getPickPackCellType", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.pickPackFieldTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "pickPackCellType.name",template: "<a ui-sref='main.pick_pack_cell_type'>#: pickPackCellType.name # </a>", headerTemplate: "<span translate='PICK_PACK_CELL_TYPE'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"}
    ];
    $scope.pickPackFieldTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("pickPackFieldType")});

  }).controller("pickPackFieldTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("pickPackFieldType", {
          "name": $scope.name,
          "pickPackCellTypeId": $scope.pickPackCellType? $scope.pickPackCellType.id: null,
          "numberOfColumns": $scope.numberOfColumns,
          "numberOfRows": $scope.numberOfRows
        }, function () {
          $state.go("main.pick_pack_field_type");
        });
      }
    };
  }).controller("pickPackFieldTypeUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("pickPackFieldType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("pickPackFieldType", {
          "id": $scope.id,
          "name": $scope.name,
          "pickPackCellTypeId": $scope.pickPackCellType? $scope.pickPackCellType.id: null,
          "numberOfColumns": $scope.numberOfColumns,
          "numberOfRows": $scope.numberOfRows
        }, function () {
          $state.go("main.pick_pack_field_type");
        });
      }
    };
  }).controller("pickPackFieldTypeReadCtl", function ($scope, $stateParams, masterService){
    masterService.read("pickPackFieldType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();