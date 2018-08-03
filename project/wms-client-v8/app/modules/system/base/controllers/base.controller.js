/**
 * Created by frank.zhou on 2017/04/17.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("systemCtl", function ($rootScope, $scope, $state, $window, commonService, SYSTEM_CONSTANT, SYSTEM_FILTER, systemService) {
        $rootScope.searchGrid = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            var grid = $("#" + currentItem + "Grid").data("kendoGrid"), filterItems = SYSTEM_FILTER[currentItem] || [];
            for (var i = 0, filters = []; i < filterItems.length; i++) {
                var filter = filterItems[i], fields = filter.field.split("."), key = fields[0], value = fields[1];
                // ================entity_lock: start=====================
                if (["entityLock", "locale"].indexOf(key) >= 0 && $scope[key]) {
                    value = "selectionValue";
                    if ($scope[key][value] === "all") continue;
                }
                // ================entity_lock: end=======================
                $scope[key] && filters.push({
                    field: filter.field,
                    operator: filter.operator || "contains",
                    value: value ? $scope[key][value] : $scope[key]
                });
            }
            grid.dataSource.filter(filters);
        };
        $rootScope.keyUp = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $rootScope.searchGrid();
            }
        };
        $rootScope.changeSearch = function () {
            $rootScope.searchGrid();
        };
        $rootScope.create = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            $state.go("main." + currentItem + "Create");
        };
        $rootScope.import = function (key) {
            commonService.dialogMushiny($scope.window, {
                title: "<span>导入</span>",
                width: 400,
                height: 280,
                url: "modules/masterData/base/templates/ImportWindow.html",
                open: function () {
                    // 确认选择
                    $rootScope.importToSql = function (win) {
                        importUploading(key);
                        win.close();
                    };
                }
            });
        };
        function importUploading(key) {
            var fd = new FormData();
            var file = document.querySelector('input[type=file]').files[0];
            fd.append('file', file);
            var fileName = $("#fileId").val();
            if (fileName == '') {
                alert("请选择excel,再上传");
            } else if (fileName.lastIndexOf(".xls") < 0) {//可判断以.xls和.xlsx结尾的excel
                alert("请选择excel,再上传");
            } else {
                if (key === "user") {
                    commonService.importAjaxAsync(key, {
                        url: SYSTEM_CONSTANT.saveImportUser,
                        data: fd,
                    });
                } else if (key === "client") {
                    commonService.importAjaxAsync(key, {
                        url: SYSTEM_CONSTANT.saveImportClient,
                        data: fd,
                    });
                } else {
                    commonService.importAjaxAsync(key, {
                        url: SYSTEM_CONSTANT.saveImportRole,
                        data: fd,
                    });
                }
            }
        }

        $rootScope.update = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            var grid = $("#" + currentItem + "Grid").data("kendoGrid");
            var rows = grid.select();
            if (rows.length) {
                var rowData = grid.dataItem(rows[0]);
                $state.go("main." + currentItem + "Update", {id: rowData.id});
            }
        };

        $rootScope.delete = function () {
            var currentItem = $window.localStorage["currentItem"];
            if (currentItem == null) return;
            var grid = $("#" + currentItem + "Grid").data("kendoGrid");
            var rows = grid.select();
            if (rows.length) {
                commonService.dialogMushiny($scope.window, {
                    open: function (win) {
                        $rootScope.deleteSure = function () {
                            var rowData = grid.dataItem(rows[0]);
                            systemService.deleteOne(currentItem, rowData.id, function () {
                                win.close();
                                grid.dataSource.read(); // 刷新表格
                            });
                        };
                    }
                });
            }
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
        $rootScope.warehouseSource = systemService.getDataSource({key: "getWarehouse", text: "name", value: "id"});

        // client
        $rootScope.clientSource = systemService.getDataSource({key: "getClient", text: "name", value: "id"});

        // entity_lock
        $rootScope.entityLockSource = systemService.getDataSource({
            key: "getSelectionBySelectionKey",
            value: "selectionValue",
            text: "resourceKey",
            data: {selectionKey: "ENTITY_LOCK"}
        });
    });
})();