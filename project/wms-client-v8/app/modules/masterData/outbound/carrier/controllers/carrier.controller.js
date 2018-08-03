/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("carrierCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "carrier";

    var columns = [
      {field: "name", template: "<a ui-sref='main.carrierRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "carrierNo", headerTemplate: "<span translate='CARRIER_NO'></span>"}
    ];
    $scope.carrierGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("carrier")});

  }).controller("carrierCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("carrier", {
          "name": $scope.name,
          "carrierNo": $scope.carrierNo
        }, function () {
          $state.go("main.carrier");
        });
      }
    };
  }).controller("carrierUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("carrier", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("carrier", {
          "id": $scope.id,
          "name": $scope.name,
          "carrierNo": $scope.carrierNo
        }, function () {
          $state.go("main.carrier");
        });
      }
    };
  }).controller("carrierReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("carrier", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();