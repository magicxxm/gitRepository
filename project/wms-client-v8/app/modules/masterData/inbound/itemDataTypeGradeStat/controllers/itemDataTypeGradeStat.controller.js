/**
 * Created by frank.zhou on 2016/12/20.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("itemDataTypeGradeStatCtl", function ($scope, $rootScope, $window, commonService, masterService, itemDataTypeGradeStatService) {

        $window.localStorage["currentItem"] = "itemDataTypeGradeStat";
        $scope.dayNumber = "";
        $scope.refreshTime = "";
        $rootScope.sabcRuleSource = masterService.getDataSource({
            key: "getSabcRule",
            text: "skuTypeName",
            value: "id"
        });

        //获取刷新的时间和天数
        itemDataTypeGradeStatService.getRefreshTime(
            function (date) {
                debugger;
                $scope.dayNumber = date.refreshDay;
                $scope.refreshTime = date.refreshTime;
            }
        );
        //保存刷新天数
        $rootScope.sureDay = function () {
            if ($scope.dayNumber) {
                itemDataTypeGradeStatService.sureDay($scope.dayNumber, function (data) {
                    commonService.dialogMushiny($scope.window, {
                        url: "modules/common/templates/successWindow.html",
                        open: function (win) {
                            win.close();
                        }
                    });
                });
            }
        }
        //保存每天刷新时间
        $rootScope.sureTime = function () {
            debugger;
            if ($scope.refreshTime) {
                itemDataTypeGradeStatService.sureRefreshTime($scope.refreshTime, function (data) {
                    commonService.dialogMushiny($scope.window, {
                        url: "modules/common/templates/successWindow.html",
                        open: function (win) {
                            win.close();
                        }
                    });
                });
            }
        }
        // 列
        var columns = [
            {field: "itemData.name", width: 150, headerTemplate: "<span>商品名称</span>"},
            {field: "skuGrade", width: 100, headerTemplate: "<span>商品实际等级</span>"},
            {field: "skuAdjustGrade", width: 100, headerTemplate: "<span>商品调整等级</span>"},
            {field: "shipmentDay", width: 70, headerTemplate: "<span translate='SHIPMENT_DAY'></span>"},
            {field: "unitsShipment", width: 70, headerTemplate: "<span translate='UNITS_SHIPMENT'></span>"},
            {field: "unitsDay", width: 70, headerTemplate: "<span translate='u/d'></span>"},
            {
                field: "alterState", width: 120,
                template: function (item) {
                    return item.alterState === 0 ? "否" : "是";
                },
                headerTemplate: "<span>是否修改过调整等级</span>"
            },
            {field: "adjustExpireDate", width: 120, headerTemplate: "<span>商品调整等级更新日期</span>"},
            {field: "createdDate", width: 120, headerTemplate: "<span>创建日期</span>"},
            {field: "modifiedDate", width: 120, headerTemplate: "<span>商品等级更新日期</span>"}
        ];
        $scope.itemDataTypeGradeStatGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("itemDataTypeGradeStat")
        });
    }).controller("itemDataTypeGradeStatCreateCtl", function ($scope, $rootScope, $state, masterService) {
        $scope.client = null;
        $scope.changeClient = function () {
            $scope.itemData = {};
        };
        // 选择sku
        $scope.selectItemData = function (client) {
            if (client == null) return;
            $rootScope.selectInWindow({
                title: "ITEM_DATA",
                srcKey: "itemData",
                srcColumns: [
                    {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
                    {field: "itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"}
                ],
                init: function (options) {
                    options.showClient = true;
                    options.disableClient = true;
                    $rootScope.client = client;
                },
                back: function (data) {
                    $scope.itemData = {id: data.id, name: data.name};
                }
            });
        };
        // 保存
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("itemDataTypeGradeStat", {
                    // "positionNo": $scope.positionNo,
                    "itemDataId": $scope.itemData.id,
                    "shipmentDay": $scope.shipmentDay,
                    "unitsShipment": $scope.unitsShipment,
                    "unitsDay": $scope.unitsDay,
                    "adjustExpireDate": $scope.adjustExpireDate,
                    "clientId": $scope.client ? $scope.client.id : null
                }, function () {
                    $state.go("main.item_data_type_grade_stat");
                });
            }
        };
    }).controller("itemDataTypeGradeStatUpdateCtl", function ($scope, $rootScope, $state, $stateParams, masterService) {
        masterService.read("itemDataTypeGradeStat", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
        $scope.changeClient = function () {
            $scope.itemData = {};
        };
        // 选择sku
        $scope.selectItemData = function (client) {
            if (client == null) return;
            $rootScope.selectInWindow({
                title: "ITEM_DATA",
                srcKey: "itemData",
                srcColumns: [
                    {"field": "name", headerTemplate: "<span translate='NAME'></span>"},
                    {field: "itemNo", headerTemplate: "<span translate='ITEM_NO'></span>"}
                ],
                init: function (options) {
                    options.showClient = true;
                    options.disableClient = true;
                    $rootScope.client = client;
                },
                back: function (data) {
                    $scope.itemData = {id: data.id, name: data.name};
                }
            });
        };
        // 修改
        $scope.validate = function (event) {

            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("itemDataTypeGradeStat", {
                    "id": $scope.id,
                    // "positionNo": $scope.positionNo,
                    "itemDataId": $scope.itemData.id,
                    "skuAdjustGrade": $scope.skuAdjustGrade.skuTypeName,
                    "shipmentDay": $scope.shipmentDay,
                    "unitsShipment": $scope.unitsShipment,
                    // "unitsDay": $scope.unitsDay,
                    "adjustExpireDate": $scope.adjustExpireDate,
                    "clientId": $scope.client ? $scope.client.id : null
                }, function () {
                    $state.go("main.item_data_type_grade_stat");
                });
            }
        };
    }).controller("itemDataTypeGradeStatReadCtl", function ($scope, $stateParams, masterService) {
        masterService.read("itemDataTypeGradeStat", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    });
})();