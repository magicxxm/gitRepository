/**
 * Created by frank.zhou on 2017/07/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("stowThresholdCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "stowThreshold";

    var columns = [
      {field: "name", template: "<a ui-sref='main.stowThresholdRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "threshold", headerTemplate: "<span translate='THRESHOLD'></span>"}
    ];
    $scope.stowThresholdGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("stowThreshold")});

  }).controller("stowThresholdCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("stowThreshold", {
          "name": $scope.name,
          "threshold": $scope.threshold,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.stow_threshold");
        });
      }
    };
  }).controller("stowThresholdUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("stowThreshold", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.client = {id: data.clientId};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("stowThreshold", {
          "id": $scope.id,
          "name": $scope.name,
          "threshold": $scope.threshold,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.stow_threshold");
        });
      }
    };
  }).controller("stowThresholdReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("stowThreshold", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();