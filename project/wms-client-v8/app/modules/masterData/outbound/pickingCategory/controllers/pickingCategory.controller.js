/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("pickingCategoryCtl", function ($scope, $rootScope, $window, $state, $translate, commonService, masterService, pickingCategoryService) {

        $window.localStorage["currentItem"] = "pickingCategory";

        $rootScope.processPathSource = masterService.getDataSource({key: "getProcessPath", text: "name", value: "id"});
        $rootScope.pickingCategoryRuleSource = masterService.getDataSource({
            key: "getPickingCategoryRule",
            text: "name",
            value: "id"
        });

        $rootScope.addPickingCategoryPosition = function (object) {
            var grid = $("#pickingCategoryPositionGrid").data("kendoGrid");
            grid.dataSource.insert(0, {rule: object});
        };

        $rootScope.getList = function (key, clientId) {
            var list = [];
            if (key === "BOX_TYPE") list = pickingCategoryService.getBoxType(clientId);
            else if (key === "ITEM_GROUP") list = pickingCategoryService.getItemGroup();
            else if (key === "ITEM_ZONE") list = pickingCategoryService.getZone(clientId);
            else if (key === "CARRIER") list = pickingCategoryService.getCarrier();
            else if (key === "ORDER_TYPE") list = [
                {id: "Transfer", name: "Transfer"},
                {id: "Vendor Return", name: "Vendor Return"},
                {id: "Customer", name: "Customer"},
                {id: "Replenish", name: "Replenish"}
            ];
            return list;
        };

        $rootScope.getListName = function (ids, lists) {
            for (var i = 0, names = [], items = []; i < lists.length; i++) {
                var list = lists[i];
                ids.indexOf(list.id) >= 0 && names.push(list.name) && items.push({id: list.id, name: list.name});
            }
            return [names, items];
        };

        // main
        var columns = [
            {field: "name", headerTemplate: "<span translate='NAME'></span>"},
            {field: "processPath.name", headerTemplate: "<span translate='PROCESS_PATH'></span>"},
            {field: "orderIndex", headerTemplate: "<span translate='ORDER_INDEX'></span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];

        $scope.pickingCategoryGridOptions = commonService.gridMushiny({
            dataSource: masterService.getGridDataSource("pickingCategory"),
            columns: columns,
            detailInit: function (e) {
                masterService.read("pickingCategory", e.data.id, function (data) {
                    $("<div></div>").appendTo(e.detailCell).kendoGrid(commonService.gridMushiny({
                        dataSource: data.positions,
                        height: 220,
                        columns: [
                            {field: "rule.name", width: 200, headerTemplate: $translate.instant("RULE_NAME")},
                            {field: "operator", width: 200, headerTemplate: $translate.instant("OPERATOR")},
                            {
                                field: "value", headerTemplate: $translate.instant("VALUE"), template: function (item) {
                                var rule = item.rule, values = [item.value];
                                if (rule.comparisonType === "VALUE_FROM_CONTEXT") {
                                    var ids = item.value.split(",");
                                    values = $rootScope.getListName(ids, $rootScope.getList(rule.key, data.clientId))[0];
                                }
                                return values.join(",");
                            }
                            }
                        ]
                    }));
                });
            }
        });

    }).controller("pickingCategoryCreateCtl", function ($scope, $rootScope, $state, $window, commonService, masterService) {
        // position
        $scope.pickingCategoryPositionGridOptions = masterService.editGrid({
            height: 220,
            columns: [
                {field: "rule.name", width: 200, headerTemplate: "<span translate='RULE_NAME'></span>"},
                {
                    field: "operator",
                    width: 200,
                    headerTemplate: "<span translate='OPERATOR'></span>",
                    editor: function (container, options) {
                        masterService.selectEditor(container, options, options.model.rule.operators);
                    }
                },
                {
                    field: "value",
                    headerTemplate: "<span translate='VALUE'></span>",
                    editor: function (container, options) {
                        var rule = options.model.rule;
                        if (rule.comparisonType === "VALUE_FROM_CONTEXT") {
                            masterService.selectEditor(container, options, $rootScope.getList(rule.key, $scope.client.id));
                        } else
                            $('<input name="' + options.field + '" class="k-textbox" />').appendTo(container);
                    },
                    template: function (item) {
                        var datas = [];
                        if (item.value) {
                            // start: 初始id需转换
                            var rule = item.rule;
                            if (rule.comparisonType === "VALUE_FROM_CONTEXT" && typeof item.value === "string") {
                                item.value = $rootScope.getListName(item.value.split(","), $rootScope.getList(rule.key, $scope.client.id))[1];
                            }
                            // end
                            if (typeof item.value === "string") datas.push(item.value);
                            else for (var i = 0; i < item.value.length; i++) datas.push(item.value[i].name);
                        }
                        return datas.join(",");
                    }
                }
            ]
        });
        //
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var grid = $("#pickingCategoryPositionGrid").data("kendoGrid"), datas = grid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i], items = [];
                    if (data.value) {
                        if (typeof data.value === "string") items.push(data.value);
                        else for (var j = 0; j < data.value.length; j++) items.push(data.value[j].id);
                    }
                    details.push({
                        "name": data.rule.name,
                        "ruleId": data.rule.id,
                        "operator": data.operator,
                        "value": items.join(","),
                        "clientId": $scope.client.id,
                        "warehouseId": $window.localStorage["warehouseId"]
                    });
                }
                masterService.create("pickingCategory", {
                    "name": $scope.name,
                    "orderIndex": $scope.orderIndex,
                    "description": $scope.description,
                    "processPathId": $scope.processPath.id,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "warehouseId": $window.localStorage["warehouseId"],
                    "positions": details
                }, function () {
                    $state.go("main.picking_category");
                });
            }
        };
    }).controller("pickingCategoryUpdateCtl", function ($scope, $rootScope, $window, $state, $stateParams, commonService, masterService) {
        // position
        $scope.pickingCategoryPositionGridOptions = masterService.editGrid({
            height: 220,
            columns: [
                {field: "rule.name", width: 200, headerTemplate: "<span translate='RULE_NAME'></span>"},
                {
                    field: "operator",
                    width: 200,
                    headerTemplate: "<span translate='OPERATOR'></span>",
                    editor: function (container, options) {
                        masterService.selectEditor(container, options, options.model.rule.operators);
                    }
                },
                {
                    field: "value",
                    headerTemplate: "<span translate='VALUE'></span>",
                    editor: function (container, options) {
                        var rule = options.model.rule;
                        if (rule.comparisonType === "VALUE_FROM_CONTEXT") {
                            masterService.selectEditor(container, options, $rootScope.getList(rule.key, $scope.client.id));
                        } else
                            $('<input name="' + options.field + '" class="k-textbox" />').appendTo(container);
                    },
                    template: function (item) {
                        var datas = [];
                        if (item.value) {
                            // start: 初始id需转换
                            var rule = item.rule;
                            if (rule.comparisonType === "VALUE_FROM_CONTEXT" && typeof item.value === "string") {
                                item.value = $rootScope.getListName(item.value.split(","), $rootScope.getList(rule.key, $scope.client.id))[1];
                            }
                            // end
                            if (typeof item.value === "string") datas.push(item.value);
                            else for (var i = 0; i < item.value.length; i++) datas.push(item.value[i].name);
                        }
                        return datas.join(",");
                    }
                }
            ]
        });
        //
        masterService.read("pickingCategory", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            $scope.client = {id: $scope.clientId};
            var grid = $("#pickingCategoryPositionGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
        //
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var grid = $("#pickingCategoryPositionGrid").data("kendoGrid"), datas = grid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i], items = [];
                    if (data.value) {
                        if (typeof data.value === "string") items.push(data.value);
                        else for (var j = 0; j < data.value.length; j++) items.push(data.value[j].id);
                    }
                    details.push({
                        "id": data.id || null,
                        "name": data.rule.name,
                        "ruleId": data.rule.id,
                        "operator": data.operator,
                        "value": items.join(","),
                        "clientId": $scope.client.id,
                        "warehouseId": $window.localStorage["warehouseId"]
                    });
                }
                masterService.update("pickingCategory", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "orderIndex": $scope.orderIndex,
                    "description": $scope.description,
                    "processPathId": $scope.processPath.id,
                    "clientId": $scope.client ? $scope.client.id : null,
                    "warehouseId": $window.localStorage["warehouseId"],
                    "positions": details
                }, function () {
                    $state.go("main.picking_category");
                });
            }
        };
    });
})();