/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("jobcategoryCtl", function ($scope, $rootScope, $window, $state, commonService, totService) {
    $window.localStorage["currentItem"] = "jobcategory";
   $rootScope.obpCellTypeSource = totService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
   $rootScope.obpWallSource = totService.getDataSource({key: "getObpWall", text: "name", value: "id"});
    var columns = [
      {field: "code",headerTemplate: "<span translate='J_CODE'></span>"},
      {field: "name",headerTemplate: "<span translate='J_NAME'></span>"},
      {field: "description",headerTemplate: "<span translate='J_DESCRIPTION'></span>"}
    ];
    $scope.jobcategoryGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("jobcategory")});

  }).controller("jobcategoryCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("jobcategory", {
          "code": $scope.code,
          "name": $scope.name,
          "description": $scope.description,
          "jobType":"INDIRECT"
        }, function () {
          $state.go("main.tot_jobcategory");
        });
      }
    };
  }).controller("jobcategoryUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("jobcategory", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("jobcategory", {
          "id": $scope.id,
          "code": $scope.code,
          "name": $scope.name,
          "description": $scope.description,
          "jobType":"INDIRECT"
        }, function () {
          $state.go("main.tot_jobcategory");
        });
      }
    };
  }).controller("jobcategoryReadCtl", function ($scope, $stateParams, totService){
    totService.read("jobcategory", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();