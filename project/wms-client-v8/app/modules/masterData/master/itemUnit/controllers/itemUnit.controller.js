
/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("itemUnitCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "itemUnit";

    $rootScope.baseUnitSource =  masterService.getDataSource({key: "getItemUnit", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.itemUnitRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "baseUnit", headerTemplate: "<span translate='BASE_UNIT'></span>", template: function(item){
        return item.baseUnit? item.baseUnit.name: "";
      }},
      {field: "unitType", headerTemplate: "<span translate='UNIT_TYPE'></span>"},
      {field: "baseFactor", headerTemplate: "<span translate='BASE_FACTOR'></span>"}
    ];
    $scope.itemUnitGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("itemUnit")});
 }).controller("itemUnitCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("itemUnit", {
          "name": $scope.name,
          "unitType": $scope.unitType,
          "baseFactor": $scope.baseFactor,
          "baseUnitId": $scope.baseUnit? $scope.baseUnit.id: null
        }, function () {
          $state.go("main.item_unit");
        });
      }
    };
  }).controller("itemUnitUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("itemUnit", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("itemUnit", {
          "id": $scope.id,
          "name": $scope.name,
          "unitType": $scope.unitType,
          "baseFactor": $scope.baseFactor,
          "baseUnitId": $scope.baseUnit? $scope.baseUnit.id: null
          }, function () {
          $state.go("main.item_unit");
         });
      }
    };
  }).controller("itemUnitReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("itemUnit", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();