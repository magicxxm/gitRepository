/**
 * Created by frank.zhou on 2017/04/21.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("storageLocationCtl", function ($scope, $rootScope, $window, $state, commonService, masterService,
                                                                       Excel, storageLocationService) {

        $window.localStorage["currentItem"] = "storageLocation";

        $rootScope.storageLocationTypeDataSource = masterService.getDataSource({
            key: "getStorageLocationType",
            text: "name",
            value: "id"
        });
        $rootScope.dropZoneDataSource = masterService.getDataSource({key: "getDropZone", text: "name", value: "id"});
        $rootScope.faceDataSource = masterService.getDataSource({
            key: "getSelectionBySelectionKey",
            value: "selectionValue",
            text: "resourceKey",
            data: {selectionKey: "FACE"}
        });
        $rootScope.storageTypeSource = masterService.getDataSource({
            key: "getSelectionBySelectionKey",
            value: "selectionValue",
            text: "resourceKey",
            data: {selectionKey: "STORAGE_TYPE"}
        });
        $rootScope.areaDataSource = masterService.getDataSource({key: "getArea", text: "name", value: "id"});

        $rootScope.changeClient = function (clientId) {
            storageLocationService.getZone(clientId, function (itemDatas) {
                var zoneDataComboBox = $("#zone").data("kendoComboBox");
                zoneDataComboBox.value("");
                zoneDataComboBox.setDataSource(new kendo.data.DataSource({data: itemDatas}));
            });
            storageLocationService.getArea(clientId, function (itemDatas) {
                var areaDataComboBox = $("#area").data("kendoComboBox");
                areaDataComboBox.value("");
                areaDataComboBox.setDataSource(new kendo.data.DataSource({data: itemDatas}));
            });
        };
        /**********************************首页表格****************************************************/
        $rootScope.columns = [
            {
                field: "name",
                width: 120,
                template: "<a ui-sref='main.storageLocationRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "zone",
                width: 110,
                headerTemplate: "<span translate='ZONE'></span>",
                template: function (dataItem) {
                    return (dataItem.zone ? "<a ui-sref='main.zone'>" + dataItem.zone.name + "</a>" : "");
                }
            },
            {field: "number", width: 70, headerTemplate: "<span>总数</span>"},
            {
                width: 120, headerTemplate: "<span translate='POD'></span>", template: function (dataItem) {
                return (dataItem.pod ? "<a ui-sref='main.pod'>" + dataItem.pod.name + "</a>" : "");
            }
            },
            {field: "stationName", width: 100, headerTemplate: "<span translate='WORKSTATION'></span>"},
            {
                field: "area",
                width: 120,
                headerTemplate: "<span translate='AREA'></span>",
                template: function (dataItem) {
                    return (dataItem.area ? "<a ui-sref='main.area'>" + dataItem.area.name + "</a>" : "");
                }
            },
            {
                field: "storageLocationType",
                width: 120,

                headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>",
                template: function (dataItem) {
                    return (dataItem.storageLocationType ? "<a ui-sref='main.storage_location_type'>" + dataItem.storageLocationType.name + "</a>" : "");
                }
            },
            {
                field: "dropZone", width: 120,
                headerTemplate: "<span translate='DROP_ZONE'></span>",
                template: function (dataItem) {
                    return (dataItem.dropZone ? "<a ui-sref='main.drop_zone'>" + dataItem.dropZone.name + "</a>" : "");
                }
            }
            // {field: "face", width: 70, headerTemplate: "<span translate='FACE'></span>"},
            // {field: "color", width: 70, headerTemplate: "<span translate='COLOR'></span>"},
            // {field: "xPos", width: 70, headerTemplate: "<span translate='X_POS'></span>"},
            // {field: "yPos", width: 70, headerTemplate: "<span translate='Y_POS'></span>"},
            // {field: "zPos", width: 70, headerTemplate: "<span translate='Z_POS'></span>"},
            // {field: "orderIndex", width: 70, headerTemplate: "<span translate='ORDER_INDEX'></span>"},
            // {field: "allocation", width: 100, headerTemplate: "<span translate='ALLOCATION'></span>"},
            // {field: "allocationState", width: 100, headerTemplate: "<span translate='ALLOCATION_STATE'></span>"},
            // {
            //     field: "stocktakingDate",
            //     width: 160,
            //     headerTemplate: "<span translate='STOCKTAKING_DATE'></span>",
            //     template: function (item) {
            //         return item.stocktakingDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.stocktakingDate)) : "";
            //     }
            // }
        ];
        $scope.storageLocationGridOptions = commonService.gridMushiny({
            columns: $rootScope.columns,
            dataSource: masterService.getGridDataSource("storageLocation")
        });
        /******************************导出excel********************************************************************/
        // $scope.export=function () {
        //     storageLocationService.getName(function (nameData) {
        //         Excel.exportStorageLocationToExcel(nameData);
        //     })
        // };
        $rootScope.exportColumns = [
            {
                field: "name",
                width: 120,
                // template: "<a>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            // {
            //   field: "zone", width: 110, headerTemplate: "<span translate='ZONE'></span>", template: function (dataItem) {
            //   return (dataItem.zone ? "<a ui-sref='main.zone'>" + dataItem.zone.name + "</a>" : "");
            // }
            // },
            // {
            //   width: 120, headerTemplate: "<span translate='POD'></span>", template: function (dataItem) {
            //   return (dataItem.pod ? "<a ui-sref='main.pod'>" + dataItem.pod.name + "</a>" : "");
            // }
            // },
            // {field: "stationName", width: 100, headerTemplate: "<span translate='WORKSTATION'></span>"},
            // {
            //   field: "area", width: 120, headerTemplate: "<span translate='AREA'></span>", template: function (dataItem) {
            //   return (dataItem.area ? "<a ui-sref='main.area'>" + dataItem.area.name + "</a>" : "");
            // }
            // },
            // {
            //   field: "storageLocationType",
            //   width: 120,
            //   headerTemplate: "<span translate='STORAGE_LOCATION_TYPE'></span>",
            //   template: function (dataItem) {
            //     return (dataItem.storageLocationType ? "<a ui-sref='main.storage_location_type'>" + dataItem.storageLocationType.name + "</a>" : "");
            //   }
            // },
            // {
            //   field: "dropZone", width: 120,
            //   headerTemplate: "<span translate='DROP_ZONE'></span>",
            //   template: function (dataItem) {
            //     return (dataItem.dropZone ? "<a ui-sref='main.drop_zone'>" + dataItem.dropZone.name + "</a>" : "");
            //   }
            // },
            // {field: "number", width: 70, headerTemplate: "<span>总数</span>"},
            // {field: "face", width: 70, headerTemplate: "<span translate='FACE'></span>"},
            // {field: "color", width: 70, headerTemplate: "<span translate='COLOR'></span>"},
            // {field: "xPos", width: 70, headerTemplate: "<span translate='X_POS'></span>"},
            // {field: "yPos", width: 70, headerTemplate: "<span translate='Y_POS'></span>"},
            // {field: "zPos", width: 70, headerTemplate: "<span translate='Z_POS'></span>"},
            // {field: "orderIndex", width: 70, headerTemplate: "<span translate='ORDER_INDEX'></span>"},
            // {field: "allocation", width: 100, headerTemplate: "<span translate='ALLOCATION'></span>"},
            // {field: "allocationState", width: 100, headerTemplate: "<span translate='ALLOCATION_STATE'></span>"},
            // {
            //   field: "stocktakingDate",
            //   width: 160,
            //   headerTemplate: "<span translate='STOCKTAKING_DATE'></span>",
            //   template: function (item) {
            //     return item.stocktakingDate ? kendo.format("{0:yyyy-MM-dd HH:mm:ss}", kendo.parseDate(item.stocktakingDate)) : "";
            //   }
            // },
            // {field: "section.name", width: 100, headerTemplate: "<span>section</span>",
            //   template: function (item) {
            //     return item.section ? item.section.name: "";
            //   }},
            // {field: "warehouseId", width: 100, headerTemplate: "<span>仓库</span>"}
        ];
        $scope.downloadTemplate=function(){
            getSelect({key: "getStorageLocationType", text: "name", value: "id"});
            var columnName = ["名称","货位类型"];
            var cellWidths = [200,200];
            exportToExcelLine("#storageLocationGrid","容器模板",cellWidths,columnName);
        };
        function exportToExcelLine(tableId, tableName, columnsWidths, columnsName) {
            var rows = [];
            var headCells = [];
            var cellWidths = [];
            for (var k = 0; k < columnsName.length; k++) {
                cellWidths.push({width: columnsWidths[k]});
                var cellValue = columnsName[k];
                if (cellValue != null) {
                    headCells.push({value: cellValue});
                }
            }
            var headRow = {cells: headCells}; //标题行
            rows.push(headRow); //将标题行添加到rows集合中
            var rowInfo=[];
            for(var i = 0; i < columnsName.length; i++){
                var type="";
                if(i==1) {
                    for (var j = 0; j < $scope.sysStorageLocationType.length; j++) {
                        type = type + $scope.sysStorageLocationType[j].name + "/";
                    }
                    rowInfo.push({value: type.substring(0,type.length-1)});
                }else{
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
        function getSelect(opts){
            masterService.getSelectData(opts,function(datas){
               $scope.sysStorageLocationType=datas;
            });
        }

    }).controller("storageLocationCreateCtl", function ($scope, $state, masterService) {
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("storageLocation", {
                    "name": $scope.name,
                    "typeId": $scope.storageLocationType ? $scope.storageLocationType.id : null,
                    "dropZoneId": $scope.dropZone.id,
                    "areaId": $scope.area.id
                }, function () {
                    $state.go("main.storage_location");
                });
            }
        };
    }).controller("storageLocationReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("storageLocation", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
        });
    })
        .controller("storageLocationExportCtl", function ($scope, $rootScope, $state, Excel, masterService, commonService, storageLocationService) {
            storageLocationService.getName(function (nameData) {
                // $rootScope.tableData = nameData;
                $scope.storageLocationExportGridOptions = commonService.gridMushiny1({
                    height: $(document.body).height() - 150,
                    columns: $rootScope.exportColumns,
                    dataSource: nameData
                });
            });

            // setTimeout(function () {
            //     $("#storageLocationExportGrid").data("kendoGrid").setDataSource(new kendo.data.DataSource({data: $rootScope.tableData}));
            // }, 200);
            //列名
            // var columnsName = ["名称", "区域", "Pod", "物理工作站","功能区", "货位类型", "DropZone","总数", "面", "颜色", "X坐标", "Y坐标",
            //   "Z坐标", "排序", "分配", "分配状态", "最后一次盘点","section","仓库"];
            var columnsName = "名称";
            var cellWidths1 = "200";
            //列宽
            // var cellWidths1 = [150, 150, 150, 120,150, 120, 120, 80,80, 80, 50, 50, 50, 80, 100, 80, 150,90,90];
            $scope.OKExport = function () {
                Excel.exportToExcel("#storageLocationExportGrid", "storageLocation", cellWidths1, columnsName);
            };
        });
})
();