/**
 * Created by frank.zhou on 2017/07/27.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("mapCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "map";

    $rootScope.sectionSource = masterService.getDataSource({key: "getSection", text: "name", value: "id"});

    var columns = [
      {field: "name", template: "<a ui-sref='main.mapRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "nodeSize", headerTemplate: "<span translate='NODE_SIZE'></span>"},
      {field: "numberOfColumns", headerTemplate: "<span translate='NUMBER_OF_COLUMNS'></span>"},
      {field: "numberOfRows", headerTemplate: "<span translate='NUMBER_OF_ROWS'></span>"},
      {field: "section.name", headerTemplate: "<span translate='SECTION'></span>"},
      {field: "active", headerTemplate: "<span translate='ACTIVE'></span>"}
     ];
    $scope.mapGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("map")});

  }).controller("mapCreateCtl", function ($scope, $state, masterService){
    $scope.active = "false";
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("map", {
          "name": $scope.name,
          "nodeSize": $scope.nodeSize,
          "numberOfColumns": $scope.numberOfColumns,
          "numberOfRows": $scope.numberOfRows,
          "sectionId": $scope.section? $scope.section.id: "",
          "active": $scope.active
        }, function () {
          $state.go("main.map");
        });
      }
    };
  }).controller("mapUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("map", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("map", {
          "id": $scope.id,
          "name": $scope.name,
          "nodeSize": $scope.nodeSize,
          "numberOfColumns": $scope.numberOfColumns,
          "numberOfRows": $scope.numberOfRows,
          "sectionId": $scope.section? $scope.section.id: "",
          "active": $scope.active
        }, function () {
          $state.go("main.map");
        });
      }
    };
  }).controller("mapReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("map", $stateParams.id, function(data){
      for(var k in data){
        if(data[k] === true) data[k] = "true";
        else if(data[k] === false) data[k] = "false";
        $scope[k] = data[k];
      }
    });
  });
})();