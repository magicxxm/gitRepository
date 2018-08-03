/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("deliverySortCodeCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "deliverySortCode";

    var columns = [
      {field: "code", template: "<a ui-sref='main.deliverySortCodeRead({id:dataItem.id})'>#: code # </a>", headerTemplate: "<span translate='SORT_CODE'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.deliverySortCodeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("deliverySortCode")});

  }).controller("deliverySortCodeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("deliverySortCode", {
          "code": $scope.code,
          "description": $scope.description
        }, function () {
          $state.go("main.delivery_sort_code");
        });
      }
    };

  }).controller("deliverySortCodeUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("deliverySortCode", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("deliverySortCode", {
          "id": $scope.id,
          "code": $scope.code,
          "description": $scope.description
        }, function () {
          $state.go("main.delivery_sort_code");
        });
      }
    };
  }).controller("deliverySortCodeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("deliverySortCode", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();