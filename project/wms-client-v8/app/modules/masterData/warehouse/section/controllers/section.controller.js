/**
 * Created by frank.zhou on 2017/07/27.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("sectionCtl", function ($scope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "section";
    
    var columns = [
      {field: "name", template: "<a ui-sref='main.sectionRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span>描述</span>"}
     ];
    $scope.sectionGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("section")});

  }).controller("sectionCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("section", {
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.section");
        });
      }
    };
  }).controller("sectionUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("section", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("section", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.section");
        });
      }
    };
  }).controller("sectionReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("section", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();