/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("jobrelationCtl", function ($scope, $rootScope, $window, $state, commonService, totService) {
    $window.localStorage["currentItem"] = "jobrelation";
   $rootScope.obpCellTypeSource = totService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
   $rootScope.obpWallSource = totService.getDataSource({key: "getObpWall", text: "name", value: "id"});
   
    var columns = [
      {field: "operation",headerTemplate: "<span translate='J_OPERATION'></span>"},
      {field: "tool",headerTemplate: "<span translate='J_TOOL'></span>"},
      {field: "jobcategoryName",headerTemplate: "<span translate='J_JOBCATEGORYNAME'></span>"}
    ];
    $scope.jobrelationGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("jobrelation")});

  }).controller("jobrelationCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("jobrelation", {
          "operation": $scope.operation,
          "tool": $scope.tool,
          "jobcategoryName": $scope.jobcategoryName,
        }, function () {
          $state.go("main.tot_jobrelation");
        });
      }
    };
  }).controller("jobrelationUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("jobrelation", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("jobrelation", {
          "id": $scope.id,
          "operation": $scope.operation,
          "tool": $scope.tool,
          "jobcategoryName": $scope.jobcategoryName
        }, function () {
          $state.go("main.tot_jobrelation");
        });
      }
    };
  })
})();