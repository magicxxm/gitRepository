/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("jobCtl", function ($scope, $rootScope, $window, $state, commonService, totService) {
    $window.localStorage["currentItem"] = "job";
    $rootScope.indirectTypeValue = ["普通间接","超级间接"];
   // $rootScope.obpCellTypeSource = totService.getDataSource({key: "getObpCellType", text: "name", value: "id"});
   // $rootScope.obpWallSource = totService.getDataSource({key: "getObpWall", text: "name", value: "id"});
    var columns = [
       {field: "code", headerTemplate: "<span translate='JOB_CODE'></span>"},
       {field: "name", headerTemplate: "<span translate='JOB_NAME'></span>"},
       {field: "indirectType", headerTemplate: "<span translate='JOB_INDIRECT_TYPE'></span>"},
       {field: "categoryName", headerTemplate: "<span translate='JOB_CATEGOTY_NAME'></span>", template: function(item){
           return item.categoryDTO? item.categoryDTO.name: "";
         }},
      {field: "description", headerTemplate: "<span translate='JOB_DESCRIPTION'></span>"}
    ];
    $scope.jobGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("job")});
    $rootScope.getJobcategoryNamesSource = totService.getDataSource({key: "getJobcategoryNames", text: "name", value: "id"});
 }).controller("jobCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("job", {
          "code": $scope.code,
          "name": $scope.name,
          "indirectType": $scope.indirectType,
          "description": $scope.description,
          "categoryId" : $scope.jobcategory? $scope.jobcategory.id: null,
          "jobType": "INDIRECT"
        }, function () {
          $state.go("main.tot_job");
        });
      }
    };

  }).controller("jobUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("job", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("job", {
            "id": $scope.id,
            "code": $scope.code,
            "name": $scope.name,
            "indirectType": $scope.indirectType,
            "description": $scope.description,
            "categoryId" : $scope.jobcategory? $scope.jobcategory.id: null,
            "jobType": "INDIRECT"
        }, function () {
          $state.go("main.tot_job");
        });
      }
    };
  }).controller("jobReadCtl", function ($scope, $stateParams, totService){
    totService.read("job", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();