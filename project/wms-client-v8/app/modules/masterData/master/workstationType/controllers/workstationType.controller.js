/**
 * Created by frank.zhou on 2017/05/02.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("workstationTypeCtl", function ($scope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "workstationType";
    
    var columns = [
      {field: "name", width: 150, template: "<a ui-sref='main.workstationTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {headerTemplate: ""}
     ];
    $scope.workstationTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("workstationType")});

  }).controller("workstationTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("workstationType", {
          "name": $scope.name
        }, function () {
          $state.go("main.workstation_type");
        });
      }
    };
  }).controller("workstationTypeUpdateCtl", function ($scope, $stateParams, $state, masterService){
    masterService.read("workstationType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("workstationType", {
          "id": $scope.id,
          "name": $scope.name
        }, function () {
          $state.go("main.workstation_type");
        });
      }
    };
  }).controller("workstationTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("workstationType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();