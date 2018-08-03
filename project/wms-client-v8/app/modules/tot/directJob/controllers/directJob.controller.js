/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("directJobCtl", function ($scope, $rootScope, $window, $state, commonService, totService,directJobService) {

    $window.localStorage["currentItem"] = "directJob";

    var typeTable;

    $rootScope.getDJobcategoryNamesSource = totService.getDataSource({key: "getDJobcategoryNames"});
    $rootScope.keywordDataSourceValue = [];
    $rootScope.changeCategory = function (categoryText,categoryValue) {
        // alert(categoryText+"=="+categoryValue);
        switch (categoryValue)
        {
            case 'Receive Station Type':
                typeTable ="IB_RECEIVESTATIONTYPE";
                directJobService.getCascadingData(function (key) {
                    $rootScope.keywordDataSourceValue = directJobService.getDataSourceMy({key: key});
                },typeTable);
                break;
            case 'Drop Zone':
                typeTable ="MD_DROPZONE";
                directJobService.getCascadingData(function (key) {
                    $rootScope.keywordDataSourceValue = directJobService.getDataSourceMy({key: key});
                },typeTable);
                break;
            case 'Process Path':
                typeTable ="OB_PROCESSPATH";
                directJobService.getCascadingData(function (key) {
                    $rootScope.keywordDataSourceValue = directJobService.getDataSourceMy({key: key});
                },typeTable);
                break;
            case 'Rebin Station Type':
                typeTable ="OB_REBINSTATIONTYPE";
                directJobService.getCascadingData(function (key) {
                    $rootScope.keywordDataSourceValue = directJobService.getDataSourceMy({key: key});
                },typeTable);
                break;
            case 'Pack Station Type':
                typeTable ="OB_PACKINGSTATIONTYPE";
                directJobService.getCascadingData(function (key) {
                    $rootScope.keywordDataSourceValue = directJobService.getDataSourceMy({key: key});
                },typeTable);
                break;
            default:
                $rootScope.keywordDataSourceValue = [];
                break;
        }
    };

    var columns = [
      {field: "code", headerTemplate: "<span translate='DJOB_CODE'></span>"},
      {field: "name", headerTemplate: "<span translate='DJOB_NAME'></span>"},
      {field: "categoryDTO.name", headerTemplate: "<span translate='DJOB_CATEGOTY_NAME'></span>"},
      {field: "keyword", headerTemplate: "<span translate='DSUBPRO_DATA_SOURCE'></span>"},
      {field: "description", headerTemplate: "<span translate='DJOB_DESCRIPTION'></span>"}
    ];
    $scope.directJobGridOptions = commonService.gridMushiny({columns: columns, dataSource: totService.getGridDataSource("directJob")});


  }).controller("directJobCreateCtl", function ($scope, $state, totService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.create("directJob", {
          "code": $scope.code,
          "name": $scope.name,
          "categoryId" : $scope.categoryDTO? $scope.categoryDTO.id: null,
          "keyword":$scope.keyword,
          "jobType": "DIRECT",
          "description": $scope.description
        }, function () {
          $state.go("main.tot_directjob");
        });
      }
    };
  }).controller("directJobUpdateCtl", function ($scope, $state, $stateParams, totService){
    totService.read("directJob", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        totService.update("directJob", {
            "id": $scope.id,
            "code": $scope.code,
            "name": $scope.name,
            "categoryId" : $scope.categoryDTO? $scope.categoryDTO.id: null,
            "keyword":$scope.keyword,
            "jobType": "DIRECT",
            "description": $scope.description
        }, function () {
          $state.go("main.tot_directjob");
        });
      }
    };
  }).controller("directJobReadCtl", function ($scope, $stateParams, totService){
    totService.read("directJob", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();