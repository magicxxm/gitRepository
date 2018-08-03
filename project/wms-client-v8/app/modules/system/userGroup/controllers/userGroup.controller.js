/**
 * Created by frank.zhou on 2017/04/18.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("userGroupCtl", function ($scope, $window, commonService, systemService) {

    $window.localStorage["currentItem"] = "userGroup";

    var columns = [
      {field: "name", template: "<a ui-sref='main.userGroupRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.userGroupGridOptions = commonService.gridMushiny({columns: columns, dataSource: systemService.getGridDataSource('userGroup')});

  }).controller("userGroupCreateCtl", function ($scope, $state, systemService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if($scope.validator.validate()){
        systemService.create("userGroup", {
          "name": $scope.name,
          "description": $scope.description
        }, function(){
          $state.go("main.usergroup");
        });
      }
    }
  }).controller("userGroupUpdateCtl", function ($scope, $state, $stateParams, systemService) {
    systemService.read("userGroup", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event){
      event.preventDefault();
      if($scope.validator.validate()){
        systemService.update("userGroup", {
          "id": $scope.id,
          "name": $scope.name,
          "description": $scope.description
        }, function(){
          $state.go("main.usergroup");
        });
      }
    }
  }).controller("userGroupReadCtl", function ($scope, $stateParams, systemService) {
    systemService.read("userGroup", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  })
})();