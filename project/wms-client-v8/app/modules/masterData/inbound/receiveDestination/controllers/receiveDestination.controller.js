/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("receiveDestinationCtl", function ($scope, $rootScope, $window, $state, commonService, masterService, receiveDestinationService) {

    $window.localStorage["currentItem"] = "receiveDestination";

    $rootScope.changeClient = function(clientId){
      receiveDestinationService.getArea(clientId, function(datas){
        var comboBox = $("#area").data("kendoComboBox");
        comboBox.value("");
        comboBox.setDataSource(new kendo.data.DataSource({data: datas}));
      });
    };

    var columns = [
      {field: "name", template: "<a ui-sref='main.receiveDestinationRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
      {field: "area.name", headerTemplate: "<span translate='AREA'></span>"},
      {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
    ];
    $scope.receiveDestinationGridOptions = commonService.gridMushiny({columns: columns, dataSource: masterService.getGridDataSource("receiveDestination")});

  }).controller("receiveDestinationCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("receiveDestination", {
          "name": $scope.name,
          "areaId": $scope.area.id,
          "description": $scope.description
        }, function () {
          $state.go("main.receive_destination");
        });
      }
    };
  }).controller("receiveDestinationUpdateCtl", function ($scope, $state, $stateParams, masterService, receiveDestinationService) {
    masterService.read("receiveDestination", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
      var clientId = data.area.clientId;
      $scope.client = {id: clientId};
      receiveDestinationService.getArea(clientId, function(datas){
        var areaDataComboBox = $("#area").data("kendoComboBox");
        areaDataComboBox.setDataSource(new kendo.data.DataSource({data: datas}));
        $scope.area = {id: data.area.id};
      });
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("receiveDestination", {
          "id": $scope.id,
          "name": $scope.name,
          "areaId": $scope.area.id,
          "description": $scope.description
        }, function () {
          $state.go("main.receive_destination");
        });
      }
    };
  }).controller("receiveDestinationReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("receiveDestination", $stateParams.id, function(data){
      for(var k in data) $scope[k] = data[k];
    });
  })
})();