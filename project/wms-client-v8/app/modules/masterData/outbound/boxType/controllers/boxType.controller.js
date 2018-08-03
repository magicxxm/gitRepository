/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("boxTypeCtl", function ($scope, $rootScope, $window,  $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "boxType";

    $rootScope.groupSource = ["BAG", "BOX"];

    var columns = [
      {field: "name", template: "<a ui-sref='main.boxTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "group", headerTemplate: "<span translate='GROUP'></span>"},
      {field: "width", headerTemplate: "<span translate='LENGTH'></span><span>(mm)</span>"},
      {field: "depth", headerTemplate: "<span translate='WIDTH'></span><span>(mm)</span>"},
      {field: "height", headerTemplate: "<span translate='HEIGHT'></span><span>(mm)</span>"},
      {field: "thickness", headerTemplate: "<span translate='THICKNESS'></span><span>(mm)</span>"},
      {field: "volume", headerTemplate: "<span translate='VOLUME'></span><span>(mm³)</span>"},
      {field: "weight", headerTemplate: "<span translate='WEIGHT'></span><span>(g)</span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.boxTypeGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("boxType")});

  }).controller("boxTypeCreateCtl", function ($scope, $state, masterService){
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("boxType", {
          "name": $scope.name,
          "group": $scope.group,
          "description": $scope.description,
          "width": $scope.width,
          "height": $scope.height,
          "depth": $scope.depth,
          "thickness": $scope.thickness,
          "volume": $scope.width*$scope.height*$scope.depth,
          "weight": $scope.weight,
          "clientId": $scope.client.id
        }, function () {
          $state.go("main.box_type");
        });
      }
    };
  }).controller("boxTypeUpdateCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("boxType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      $scope.client = {id: data.clientId};
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("boxType", {
          "id": $scope.id,
          "name": $scope.name,
          "group": $scope.group,
          "description": $scope.description,
          "width": $scope.width,
          "height": $scope.height,
          "depth": $scope.depth,
          "thickness": $scope.thickness,
          "volume": $scope.width*$scope.height*$scope.depth,
          "weight": $scope.weight,
          "clientId": $scope.client.id
        }, function () {
          $state.go("main.box_type");
        });
      }
    };
  }).controller("boxTypeReadCtl", function ($scope, $state, $stateParams, masterService){
    masterService.read("boxType", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  });
})();