/**
 * Created by frank.zhou on 2017/05/22.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("icqaCtl", function ($rootScope, $scope, $state,Excel, $translate, $window, commonService, ICQA_FILTER,ICQA_CONSTANT, ICQABaseService) {
        // ======================================================================================================================
        function search(options, isSearchInWin) {
            isSearchInWin == null && (isSearchInWin = false); // 默认非弹窗
            var currentItem = options.currentItem, gridId = currentItem + "Grid";
            var grid = $("#" + gridId).data("kendoGrid"), filterItems = ICQA_FILTER[currentItem] || [];
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
                            ICQABaseService.deleteOne(currentItem, rowData.id, function () {
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
        $rootScope.warehouseSource = ICQABaseService.getDataSource({key: "getWarehouse", text: "name", value: "id"});

        // client
        $rootScope.clientSource = ICQABaseService.getDataSource({key: "getClient", text: "name", value: "id"});

        // 批量导入
        $rootScope.import = function (key) {
            commonService.dialogMushiny($scope.window, {
                title: "<span>导入</span>",
                width: 410,
                height: 280,
                url: "modules/icqa/base/templates/ImportWindow.html",
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
                if (key === "itemData") {
                    commonService.importAjaxAsync(key, {
                        url: ICQA_CONSTANT.saveImportItemData,
                        data: fd
                    });
                } else {
                    commonService.importAjaxAsync(key, {
                        url: ICQA_CONSTANT.saveImportStorageLocation,
                        data: fd
                    });
                }
            }
        }

        // 获得仓库和客户
        function getSelect(opts) {
            ICQABaseService.getSelect(opts,function(datas){
                if(opts.key=="getClient"){
                    $scope.client = datas;
                }
                if(opts.key=="getWarehouse"){
                    $scope.warehouse = datas;
                }
            });
        }


        // 按 SKU 导出模板
        $rootScope.exportSkuData = function () {
            // var columnsName = ["商品编码"];
            var columnsName = ["商品编码", "客户", "仓库"];
            //列宽
            var cellWidths1 = [130, 100, 100];
            // var cellWidths1 = [130];
            exportToExcelLineSku("#stocktakingGridId", "盘点导入SKU模板", cellWidths1, columnsName);
        }

        // 按 货位 导出模板
        $rootScope.exportLocationData = function () {
            var columnName = ["货位","仓库","盘点方式"];
            var cellWidths = [130,100,180];
            exportToExcelLineLocation("#stocktakingGridId", "盘点导入货位模板", cellWidths, columnName);
        }

        //导出列名以及第一列到excel 按货位
        function exportToExcelLineLocation(tableId, tableName, columnsWidths, columnsName) {
            debugger;
            getSelect({key: "getWarehouse", text: "name", value: "id"});
            var grid = $(tableId).data("kendoGrid");
            var rows = [];
            var headCells = [];
            var rowInfo = [];
            var cellWidths = [];
            for (var k = 0; k < grid.columns.length; k++) {
                cellWidths.push({width: columnsWidths[k]});
                var cellValue = columnsName[k];
                if (cellValue != null) {
                    headCells.push({value: cellValue});
                }
            }
            var headRow = {cells: headCells}; //标题行
            rows.push(headRow); //将标题行添加到rows集合中
            // 第二行 列备注
            for(var i = 0; i < columnsName.length; i++){
                var warehouse="";
                if(i==1){
                    for(var j=0;j<$scope.warehouse.length;j++){
                        warehouse=warehouse+$scope.warehouse[j].name+"/";
                    }
                    rowInfo.push({value:warehouse.substring(0,warehouse.length-1)});
                }else if (i==2){
                    rowInfo.push({value:"日常盘点/系统盘点"});
                }else {
                    rowInfo.push({value:""});
                }
            }
            rows.push({cells:rowInfo});

            //创建工作册
            var workbook = new kendo.ooxml.Workbook({
                sheets: [{
                    columns: cellWidths,
                    title: tableName,
                    rows: rows
                }]
            });
            //保存为excel文件
            kendo.saveAs({dataURI: workbook.toDataURL(), fileName: tableName + '.xlsx'});
        }

        // 导出列名以及第一列到excel  按Sku
        function exportToExcelLineSku(tableId, tableName, columnsWidths, columnsName) {
            // warehouse
            getSelect({key: "getWarehouse", text: "name", value: "id"});
            // client
            getSelect({key: "getClient", text: "name", value: "id"});
            debugger;
            var grid = $(tableId).data("kendoGrid");
            var rows = [];
            var headCells = [];
            var cellWidths = [];
            var rowInfo = [];
            for (var k = 0; k < grid.columns.length; k++) {
                cellWidths.push({width: columnsWidths[k]});
                var cellValue = columnsName[k];
                if (cellValue != null) {
                    headCells.push({value: cellValue});
                }
            }
            var headRow = {cells: headCells}; //标题行
            rows.push(headRow); //将标题行添加到rows集合中

            for(var i = 0; i < columnsName.length; i++){
                if(i==0){
                    rowInfo.push({value:"文本格式"});
                }else if(i==1){
                    var client="";
                    for(var j=0;j<$scope.client.length;j++){
                        client=client+$scope.client[j].name+"/";
                    }
                    rowInfo.push({value:client.substring(0,client.length-1)});
                }else if(i==2){
                    var warehouse="";
                    for(var j=0;j<$scope.warehouse.length;j++){
                        warehouse=warehouse+$scope.warehouse[j].name+"/";
                    }
                    rowInfo.push({value:warehouse.substring(0,warehouse.length-1)});
                }
            }
            rows.push({cells:rowInfo});
            //创建工作册
            var workbook = new kendo.ooxml.Workbook({
                sheets: [{
                    columns: cellWidths,
                    title: tableName,
                    rows: rows
                }]
            });
            //保存为excel文件
            kendo.saveAs({dataURI: workbook.toDataURL(), fileName: tableName + '.xlsx'});

        }
    });
})();