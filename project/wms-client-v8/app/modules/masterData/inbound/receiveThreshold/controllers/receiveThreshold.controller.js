/**
 * Created by frank.zhou on 2017/04/26.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveThresholdCtl", function ($scope, $rootScope, $window, commonService, masterService) {

    $window.localStorage["currentItem"] = "receiveThreshold";

    var columns = [
      {field: "name", template: "<a ui-sref='main.receiveThresholdRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "threshold", headerTemplate: "<span translate='THRESHOLD'></span>"}
    ];
    $scope.receiveThresholdGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("receiveThreshold")});

  }).controller("receiveThresholdCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("receiveThreshold", {
          "name": $scope.name,
          "threshold": $scope.threshold,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.receive_threshold");
        });
      }
    };
  }).controller("receiveThresholdUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveThreshold", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.client = {id: data.clientId};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("receiveThreshold", {
          "id": $scope.id,
          "name": $scope.name,
          "threshold": $scope.threshold,
          "clientId": $scope.client? $scope.client.id: null
        }, function () {
          $state.go("main.receive_threshold");
        });
      }
    };
  }).controller("receiveThresholdReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveThreshold", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();