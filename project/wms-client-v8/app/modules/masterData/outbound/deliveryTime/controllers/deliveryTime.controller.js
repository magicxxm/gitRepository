/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("deliveryTimeCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "deliveryTime";
    var columns = [
      {
        field: "deliveryTime",
        template: function (dataItem) {
          return "<a ui-sref='main.deliveryTimeRead({id:dataItem.id})'>" + dataItem.deliveryTime.substring(0, 19)+" </a>"},
        headerTemplate: "<span translate='NAME'></span>"
      },
    ];
    $scope.deliveryTimeGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("deliveryTime")
    });

  }).controller("deliveryTimeCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("deliveryTime", {
          "deliveryTime": $scope.deliveryTime
        }, function () {
          $state.go("main.delivery_time");
        });
      }
    };
  }).controller("deliveryTimeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("deliveryTime", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("deliveryTime", {
          "id": $scope.id,
          "deliveryTime": $scope.deliveryTime
        }, function () {
          $state.go("main.delivery_time");
        });
      }
    };
  }).controller("deliveryTimeReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("deliveryTime", $stateParams.id, function (data) {
      $("#deliveryTime").val(data.deliveryTime.substring(0,19));
      for (var k in data) $scope[k] = data[k];
    });
  });
})();