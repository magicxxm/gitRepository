/**
 * Created by bian on 2016/9/27.
 */
(function () {
  'use strict';
  angular.module('myApp').controller("chargingPileCtl", function ($scope, $rootScope, $window, $state,workstationService, commonService, masterService) {

    $window.localStorage["currentItem"] = "chargingPile";
    $rootScope.toWardSource = ["0", "90", "180", "270"];
    $rootScope.stateSource = ["Available", "Error"];
    $rootScope.sectionSource = masterService.getDataSource({
      key: "getSection",
      text: "name",
      value: "id"
    });
    $rootScope.changeSection = function (sectionId) {
      //数据来自工作站
      workstationService.getNodeBySectionId(sectionId, function (datas) {
        var comboBox = $("#placeMark").data("kendoComboBox");
        comboBox.value("");
        comboBox.setDataSource(new kendo.data.DataSource({data: datas}));
      });
    };
    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.chargingPileRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "placeMark",
        headerTemplate: "<span>地标</span>"
      }, {
        field: "toWard",
        headerTemplate: "<span>朝向</span>"
      }, {
        field: "state",
        headerTemplate: "<span>状态</span>"
      },
      {
        field: "chargerId",
        headerTemplate: "<span>Id</span>"
      },
      {
        field: "chargerType",
        headerTemplate: "<span>类型</span>"
      }
    ];
    $scope.chargingPileGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("chargingPile")
    });

  }).controller("chargingPileCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("chargingPile", {
          "name": $scope.name,
          "placeMark": $scope.placeMark.placeMark,
          "toWard": parseInt($scope.toWard),
          "state":$scope.state ,
          "chargerId":$scope.chargerId ,
          "chargerType":$scope.chargerType ,
          "sectionId": $scope.section?$scope.section.id:""
        }, function () {
          $state.go("main.charging_pile");
        });
      }
    };
  }).controller("chargingPileUpdateCtl", function ($scope, $stateParams, $state, masterService) {
    masterService.read("chargingPile", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("chargingPile", {
          "id": $scope.id,
          "name": $scope.name,
          "placeMark": $scope.placeMark.placeMark?$scope.placeMark.placeMark:$scope.placeMark,
          "toWard": parseInt($scope.toWard),
          "state": $scope.state,
          "chargerId":$scope.chargerId ,
          "chargerType":$scope.chargerType ,
          "sectionId": $scope.section?$scope.section.id:""
        }, function () {
          $state.go("main.charging_pile");
        });
      }
    };
  }).controller("chargingPileReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("chargingPile", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();