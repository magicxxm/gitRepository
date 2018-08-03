/**
 * Created by frank.zhou on 2017/06/06.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("processPathTypesCtl", function ($scope, $window, $state, commonService, totService) {

    $window.localStorage["currentItem"] = "processPathTypes";

    var columns = [
      {field: "name", template: "<a ui-sref='main.processPathTypesRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.processPathTypesGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("processPathTypes")});

  }).controller("processPathTypesCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("processPathTypes", {
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.process_path_types");
        });
      }
    };
  }).controller("processPathTypesUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("processPathTypes", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("processPathTypes", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description
        }, function () {
          $state.go("main.process_path_types");
        });
      }
    };
  }).controller("processPathTypesReadCtl", function ($scope, $state, $stateParams, totService){
    totService.read("processPathTypes", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();