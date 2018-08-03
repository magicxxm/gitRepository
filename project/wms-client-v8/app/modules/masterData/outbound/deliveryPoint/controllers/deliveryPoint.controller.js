/**
 * Created by frank.zhou on 2017/05/04.
 */
(function () {
  'use strict';

  angular.module('myApp').controller("deliveryPointCtl", function ($scope, $rootScope, $window, $state, commonService, masterService) {

    $window.localStorage["currentItem"] = "deliveryPoint";
    //下拉框数据的获取   text对应数据库中字段
    $rootScope.carrierSource = masterService.getDataSource({
      key: "getCarrier",
      text: "name",
      value: "id"
    });
    $rootScope.deliverySortCodeSource = masterService.getDataSource({
      key: "getDeliverySortCode",
      text: "code",
      value: "id"
    });
    $rootScope.deliveryTimeSource = masterService.getDataSource({
      key: "getDeliveryTime",
      text: "deliveryTime",
      value: "id"
    });

    // $("#deliveryTime").kendoDatePicker({culture: "zh-CN", format: "yyyy-MM-dd"});
    //定义主页面下的列就相当于与是dto的数据加载到页面上
    var columns = [
      {
        field: "carrier.name",
        template: "<a ui-sref='main.deliveryPointRead({id:dataItem.id})'>#:carrier.name # </a>",
        headerTemplate: "<span translate='CARRIER'></span>"
      },
      {
        field: "deliverySortCode.code",
        template: function (dataItem) {
          return dataItem.deliverySortCode ? dataItem.deliverySortCode.code : ''
        },
        headerTemplate: "<span translate='DELIVERY_SORT_CODE'></span>"
      },
      {
        field: "deliveryTime.deliveryTime",
        template: function (dataItem) {
          return dataItem.deliveryTime ? dataItem.deliveryTime.deliveryTime.substring(0, 19) : ''
        },
        headerTemplate: "<span translate='DELIVERY_TIME'></span>"
      },

    ];
    $scope.deliveryPointGridOptions = commonService.gridMushiny({
      columns: columns,
      dataSource: masterService.getGridDataSource("deliveryPoint")
    });
    console.log($scope.deliveryPointGridOptions);
  }).controller("deliveryPointCreateCtl", function ($scope, $state, masterService) {
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.create("deliveryPoint", {
          "carrierId": $scope.carrier ? $scope.carrier.id : null,
          "sortCodeId": $scope.deliverySortCode ? $scope.deliverySortCode.id : null,
          "time": $scope.deliveryTime ? $scope.deliveryTime.id : null
        }, function () {
          $state.go("main.delivery_point");
        });
      }
    };
  }).controller("deliveryPointUpdateCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("deliveryPoint", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
    $scope.validate = function (event) {
      event.preventDefault();
      if ($scope.validator.validate()) {
        masterService.update("deliveryPoint", {
          "id": $scope.id,
          "carrierId": $scope.carrier ? $scope.carrier.id : null,
          "sortCodeId": $scope.deliverySortCode ? $scope.deliverySortCode.id : null,
          "time": $scope.deliveryTime ? $scope.deliveryTime.id : null
        }, function () {
          $state.go("main.delivery_point");
        });
      }
    };
  }).controller("deliveryPointReadCtl", function ($scope, $state, $stateParams, masterService) {
    masterService.read("deliveryPoint", $stateParams.id, function (data) {
      for (var k in data) $scope[k] = data[k];
    });
  });
})();