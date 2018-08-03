/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("digitalLabelCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "digitalLabel";

    $rootScope.labelControllerSource = masterService.getDataSource({
      key: "getLabelController",
      text: "name",
      value: "id"
    });

    var columns = [
      {
        field: "name",
        template: "<a ui-sref='main.digitalLabelRead({id:dataItem.id})'>#: name # </a>",
        headerTemplate: "<span translate='NAME'></span>"
      },
      {
        field: "numOrder",
        headerTemplate: "<span translate='NUM_ORDER'></span>"
      },
      {
        field: "labelController.name",
        template: "<a ui-sref='main.label_controller'>#: labelController.name # </a>",
        headerTemplate: "<span translate='LABEL_CONTROLLER'></span>"
      }
    ];
    $scope.digitalLabelGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("digitalLabel")
    });

  }).controller("digitalLabelCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("digitalLabel", {
          "name": $scope.name,
          "numOrder": $scope.numOrder,
          "labelControllerId": $scope.labelController ? $scope.labelController.id : null
        }, function () {
          $state.go("main.digital_label");
        });
      }
    };
  }).controller("digitalLabelUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("digitalLabel", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("digitalLabel", {
          "id": $scope.id,
          "name": $scope.name,
          "numOrder": $scope.numOrder,
          "labelControllerId": $scope.labelController ? $scope.labelController.id : null
        }, function () {
          $state.go("main.digital_label");
        });
      }
    };
  }).controller("digitalLabelReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("digitalLabel", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();