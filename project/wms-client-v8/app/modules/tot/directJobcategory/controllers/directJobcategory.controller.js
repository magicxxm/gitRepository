/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("directJobcategoryCtl", function ($scope, $rootScope, $window, $state, commonService, totService) {
   $rootScope.subproDataSourceValue = ["Receive Station Type","Drop Zone","Process Path","Rebin Station Type","Pack Station Type"];
   $window.localStorage["currentItem"] = "directJobcategory";
    var columns = [
      {field: "code",headerTemplate: "<span translate='D_CODE'></span>"},
      {field: "name",headerTemplate: "<span translate='D_NAME'></span>"},
      {field: "subproDataSource", headerTemplate: "<span translate='SUBPRO_DATA_SOURCE'></span>"},
      {field: "description", headerTemplate: "<span translate='D_DESCRIPTION'></span>"}
    ];
    $scope.directJobcategoryGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("directJobcategory")});

  }).controller("directJobcategoryCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("directJobcategory", {
          "code": $scope.code,
          "name": $scope.name,
          "subproDataSource": $scope.subproDataSource,
          "description": $scope.description,
          "jobType":"DIRECT"
        }, function () {
          $state.go("main.tot_directjobcategory");
        });
      }
    };
  }).controller("directJobcategoryUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("directJobcategory", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("directJobcategory", {
          "id": $scope.id,
          "code": $scope.code,
          "name": $scope.name,
          "subproDataSource": $scope.subproDataSource,
           "description": $scope.description,
           "jobType":"DIRECT"
        }, function () {
          $state.go("main.tot_directjobcategory");
        });
      }
    };
  }).controller("directJobcategoryReadCtl", function ($scope, $stateParams, totService){
    totService.read("directJobcategory", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();