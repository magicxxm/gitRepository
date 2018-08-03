/**
 * Created by frank.zhou on 2017/05/10.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("ibpCtl", function ($rootScope, $scope, $state, $translate, $window, commonService, IBP_FILTER, problemInboundBaseService) {
        // ======================================================================================================================
        function search(options, isSearchInWin) {
            isSearchInWin == null && (isSearchInWin = false); // 默认非弹窗
            var currentItem = options.currentItem, gridId = (isSearchInWin ? "queryGrid" : currentItem + "Grid");
            var grid = $("#" + gridId).data(currentItem === "itemGroup" ? "kendoTreeList" : "kendoGrid"),
                filterItems = IBP_FILTER[currentItem] || [];
            for (var i = 0, filters = []; i < filterItems.length; i++) {
                var filter = filterItems[i], fields = filter.field.split("."), key = fields[0], value = fields[1];
                // *****************entity_lock: start*********************
                if (["entityLock", "locale"].indexOf(key) >= 0 && $scope[key]) {
                    value = "selectionValue";
                    if ($scope[key][value] === "all") continue;
                }
                // ******************entity_lock: end**********************
                var data = (isSearchInWin ? options : $scope);
                data[key] && filters.push({
                    field: (filter.field === "client.id" ? "clientId" : filter.field),
                    operator: filter.operator || "contains",
                    value: value ? data[key][value] : data[key]
                });
            }
            grid.dataSource.filter(filters);
        }

        // ======================================================================================================================
        $rootScope.searchGrid = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            search({currentItem: currentItem});
        };
        $rootScope.keyUp = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $rootScope.searchGrid();
            }
        };
        $rootScope.create = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            $state.go("main." + currentItem + "Create");
        };

        $rootScope.update = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            var grid = $("#" + currentItem + "Grid").data(currentItem === "itemGroup" ? "kendoTreeList" : "kendoGrid");
            var rows = grid.select();
            if (rows.length) {
                var rowData = grid.dataItem(rows[0]);
                $state.go("main." + currentItem + "Update", {id: rowData.id});
            }
        };

        $rootScope.delete = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            var grid = $("#" + currentItem + "Grid").data(currentItem === "itemGroup" ? "kendoTreeList" : "kendoGrid");
            var rows = grid.select();
            if (rows.length) {
                commonService.dialogMushiny($scope.window, {
                    open: function (win) {
                        $rootScope.deleteSure = function () {
                            var rowData = grid.dataItem(rows[0]);
                            problemInboundBaseService.deleteOne(currentItem, rowData.id, function () {
                                win.close();
                                grid.dataSource.read(); // 刷新表格
                            });
                        };
                    }
                });
            }
        };

        // 弹窗选择
        $rootScope.searchInWindow = function (options, name, client, skuNo) {
            options.name = name;
            options.showClient && (options.client = client);
            options.skuNo = skuNo;
            search(options, true);
        };
        $rootScope.selectInWindow = function (options) {
            var width = options.width || 570, height = options.height || 400;
            var url = options.url || "modules/inboundProblem/base/templates/selectInWindow.html";
            commonService.dialogMushiny($scope.window, {
                title: "<span>" + $translate.instant("SELECT_" + options.title) + "</span>",
                width: width,
                height: height,
                url: url,
                open: function () {
                    $rootScope.searchOptions = {"currentItem": options.srcKey};
                    options.init && options.init($rootScope.searchOptions); // 初始信息
                    $rootScope.queryGridOptions = commonService.gridMushiny({
                        columns: options.srcColumns,
                        dataSource: problemInboundBaseService.getGridDataSource(options.srcKey),
                        height: height - 147
                    });
                    setTimeout(function () {
                        $("#searchBtn").trigger('click');
                    }, 100);
                    // 确认选择
                    $rootScope.doSelectInWindow = function (win) {
                        var grid = $("#queryGrid").data("kendoGrid"), rows = grid.select();
                        if (!rows.length) return;
                        options.back && options.back(grid.dataItem(rows[0]));
                        win.close();
                    };
                }
            });
        };

        // 新增明细
        $rootScope.createDetail = function (id) {
            var grid = $("#" + id).data("kendoGrid");
            grid.addRow();
        };

        // 删除明细
        $rootScope.deleteDetail = function (id) {
            var grid = $("#" + id).data("kendoGrid");
            grid.removeRow(grid.select());
        };

        // warehouse
        $rootScope.warehouseSource = problemInboundBaseService.getDataSource({
            key: "getWarehouse",
            text: "name",
            value: "id"
        });

        // client
        $rootScope.clientSource = problemInboundBaseService.getDataSource({
            key: "getClient",
            text: "name",
            value: "id"
        });

        // entity_lock
        $rootScope.entityLockSource = problemInboundBaseService.getDataSource({
            key: "getSelectionBySelectionKey",
            value: "selectionValue",
            text: "resourceKey",
            data: {selectionKey: "ENTITY_LOCK"}
        });
    });
})();