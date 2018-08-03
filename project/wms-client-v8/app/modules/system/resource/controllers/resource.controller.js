/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("resourceCtl", function ($scope, $rootScope, $window, commonService, systemService) {

    $window.localStorage["currentItem"] = "resource";

    var columns = [
      {field: "resourceKey", headerTemplate: "<span translate='RESOURCE_KEY'></span>"},
      {field: "locale", headerTemplate: "<span translate='LOCALE'></span>"},
      {field: "resourceValue", headerTemplate: "<span translate='RESOURCE_VALUE'></span>"}
    ];
    $scope.resourceGridOptions = commonService.gridMushiny({columns: columns, dataSource: systemService.getGridDataSource('resource')});

    $rootScope.languageSource = systemService.getDataSource({
      key: "getSelectionBySelectionKey",
      value: "selectionValue",
      text: "resourceKey",
      data: {selectionKey: "LANGUAGE"}
    });
  }).controller("resourceCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        systemService.create("resource", {
          "resourceKey": $scope.resourceKey,
          "locale": $scope.locale.selectionValue,
          "resourceValue": $scope.resourceValue
        }, function(){
          $state.go("main.resource");
        });
      }
    }
  }).controller("resourceUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("resource", $stateParams.id, function(data){
      for(var k in data) $scope[k] = (k=="locale"? systemService.toMap(data[k]): data[k]);
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if($scope.validator.validate()){
        systemService.update("resource", {
          "id": $scope.id,
          "resourceKey": $scope.resourceKey,
          "locale": $scope.locale.selectionValue,
          "resourceValue": $scope.resourceValue
        }, function(){
          $state.go("main.resource");
        });
      }
    }
  }).controller("resourceReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("resource", $stateParams.id, function(data){
      for(var k in data) $scope[k] = (k=="locale"? systemService.toMap(data[k]): data[k]);
    });
  });
})();