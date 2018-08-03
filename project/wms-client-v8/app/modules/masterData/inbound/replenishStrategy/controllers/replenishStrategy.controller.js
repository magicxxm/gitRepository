/**
 * Created by frank.zhou on 2017/04/25.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("replenishStrategyCtl", function ($scope, $rootScope, $window, commonService, masterService) {

        $window.localStorage["currentItem"] = "replenishStrategy";

        $rootScope.rtSource = ["SafetyDOC", "Zero"];
        $rootScope.fsSource = ["计算全部订单", "只计算尚未过发货时间点的订单"];
        $rootScope.rpSource = ["是", "否"];
        $rootScope.rpsSource = ["最大库存", "安全库存"];
        $rootScope.smtSource = ["是", "否"];
        $rootScope.rplSource = ["Available", "On Pod"];

        // 列
        var columns = [

            {field: "shipmentDay", headerTemplate: "<span translate='SHIPMENT_DAY'></span>"},
            {field: "unitsShipment", headerTemplate: "<span translate='UNITS_SHIPMENT'></span>"},
            {field: "replenishTrigger", headerTemplate: "<span translate='REPLENISH_TRIGGER'></span>"},
            {field: "fudStrategy", headerTemplate: "<span translate='FUD_STRATEGY'></span>"},
            {field: "replenishPadTime", headerTemplate: "<span>补货周期时间</span>"},
            {field: "receiveIsReplenish", headerTemplate: "<span>是否收货直接补货</span>"},
            {field: "receiveIsReplenishCondition", headerTemplate: "<span>收货补货判断条件</span>"},
            {field: "replenishCount", headerTemplate: "<span>补货库存计算方式</span>"},
            {field: "skuMaxType", headerTemplate: "<span>货位最大商品种类数判断是否启用</span>"},
        ];
        $scope.replenishStrategyGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("replenishStrategy")
        });

    }).controller("replenishStrategyCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("replenishStrategy", {
                    "shipmentDay": $scope.shipmentDay,
                    "unitsShipment": $scope.unitsShipment,
                    "replenishTrigger": $scope.replenishTrigger,
                    "fudStrategy": $scope.fudStrategy,
                    "replenishPadTime": $scope.replenishPadTime,
                    "receiveIsReplenish": $scope.receiveIsReplenish,//
                    "receiveIsReplenishCondition": $scope.receiveIsReplenishCondition,//
                    "replenishCount": $scope.replenishCount,//
                    "skuMaxType": $scope.skuMaxType,
                    "clientId": $scope.client ? $scope.client.id : null
                }, function () {
                    $state.go("main.replenish_strategy");
                });
            }
        };
    }).controller("replenishStrategyUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("replenishStrategy", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            $scope.client = {id: data.clientId};
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("replenishStrategy", {
                    "id": $scope.id,
                    "shipmentDay": $scope.shipmentDay,
                    "unitsShipment": $scope.unitsShipment,
                    "replenishTrigger": $scope.replenishTrigger,
                    "fudStrategy": $scope.fudStrategy,
                    "replenishPadTime": $scope.replenishPadTime,
                    "receiveIsReplenish": $scope.receiveIsReplenish,//
                    "receiveIsReplenishCondition": $scope.receiveIsReplenishCondition,//
                    "replenishCount": $scope.replenishCount,//
                    "skuMaxType": $scope.skuMaxType,
                    "clientId": $scope.client ? $scope.client.id : null
                }, function () {
                    $state.go("main.replenish_strategy");
                });
            }
        };
    }).controller("replenishStrategyReadCtl", function ($scope, $stateParams, masterService) {
        masterService.read("replenishStrategy", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();