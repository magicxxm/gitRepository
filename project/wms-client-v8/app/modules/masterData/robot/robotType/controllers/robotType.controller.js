/**
 * Created by mingchun.mou on 2017/05/02.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("robotTypeCtl", function ($scope, $window, $state, commonService, masterService) {
    $window.localStorage["currentItem"] = "robotType";
    var columns = [
      {field: "type", template: "<a ui-sref='main.robotTypeRead({id:dataItem.id})'>#: type # </a>", headerTemplate: "<span translate='类型ID'></span>"},
      {field: "name", headerTemplate: "<span translate='NAME'></span>"},
      {field: "additionalContent", headerTemplate: "<span translate='ADDITIONAL_CONTENT'></span>"}
     ];
    $scope.robotTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("robotType")});
  }).controller("robotTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("robotType", {
          "name": $scope.name,
          "type": $scope.type,
          "additionalContent": $scope.additionalContent
        }, function () {
           $state.go("main.robot_type");
        });
      }
    };

  }).controller("robotTypeUpdateCtl", function ($scope,$stateParams, $state, masterService){
    masterService.read("robotType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("robotType", {
          "id": $scope.id,
          "type": $scope.type ,
          "name": $scope.name,
          "additionalContent": $scope.additionalContent
          }, function () {
          $state.go("main.robot_type");
        });
      }
    };
  }).controller("robotTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("robotType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();