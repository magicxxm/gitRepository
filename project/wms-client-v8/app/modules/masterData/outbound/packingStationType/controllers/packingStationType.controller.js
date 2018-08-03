/**
 * Created by frank.zhou on 2017/05/03.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("packingStationTypeCtl", function ($scope, $rootScope, $translate, $window, $state, commonService, masterService) {
        // ===================================================stationType====================================================
        $window.localStorage["currentItem"] = "packingStationType";
        $rootScope.isScanBoxTypeSource = ["是", "否"];
        $rootScope.packStationTypeSource = ["Pick To Pack  Verify", "Pick To Pack Pack", "RebinPack"];
        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.packingStationTypeRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                width: 350, headerTemplate: "<span translate='PACKING_STATION'></span>", template: function (item) {
                var packingStations = item.packingStations;
                for (var i = 0, datas = []; i < packingStations.length; i = i + 2) {
                    var station = packingStations[i], next = packingStations[i + 1];
                    var htmlStr = "<div style='margin:1px;'>";
                    station && (htmlStr += "<div class='gridCellList'>" + station.name + "</div>");
                    next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
                    htmlStr += "</div>";
                    datas.push(htmlStr);
                }
                return datas.join("");
            }
            },
            {field: "ifScan", headerTemplate: "<span translate='IF_SCAN'></span>"},
            {field: "ifPrint", headerTemplate: "<span translate='IF_PRINT'></span>"},
            {field: "ifScanInvoice", headerTemplate: "<span translate='IF_SCAN_INVOICE'></span>"},
            {field: "ifWeight", headerTemplate: "<span translate='IF_WEIGHT'></span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.packingStationTypeGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("packingStationType")
        });

        // =================================================stationTypePosition===============================================
        // 函数
        function stateEditor(container, options) {
            var source = masterService.getDataSource({
                key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey",
                data: {selectionKey: "INVENTORY_STATE"}
            });
            masterService.selectEditor(container, options, source);
        }

        // stationTypePosition-column
        var stationTypePositionColumns = [
            {
                field: "positionIndex",
                editor: masterService.numberEditor,
                headerTemplate: "<span translate='POSITION_INDEX'></span>"
            },
            {
                field: "positionState",
                editor: stateEditor,
                headerTemplate: "<span translate='POSITION_STATE'></span>",
                template: function (item) {
                    return item.positionState ? (typeof item.positionState === "string" ? $translate.instant(item.positionState) : item.positionState.resourceKey) : "";
                }
            }
        ];
        $rootScope.stationTypePositionGridOptions = masterService.editGrid({
            height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 6 - 10 - 20 - 20 - 40),
            columns: stationTypePositionColumns
        });
    }).controller("packingStationTypeCreateCtl", function ($scope, $state, masterService) {
        $scope.ifScan = "false";
        $scope.ifPrint = "false";
        $scope.ifScanInvoice = "false";
        $scope.ifWeight = "false";
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"),
                    datas = stationTypePositionGrid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i];
                    details.push({
                        "positionIndex": data.positionIndex,
                        "positionState": data.positionState ? data.positionState.selectionValue : ""
                    });
                }
                masterService.create("packingStationType", {
                    "name": $scope.name,
                    "packStationType": $scope.packStationType,
                    "ifScan": $scope.ifScan,
                    "ifPrint": $scope.ifPrint,
                    "ifScanInvoice": $scope.ifScanInvoice,
                    "ifWeight": $scope.ifWeight,
                    "description": $scope.description,
                    "isScanBoxType": $scope.isScanBoxType,
                    "positions": details
                }, function () {
                    $state.go("main.packing_station_type");
                });
            }
        };
    }).controller("packingStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("packingStationType", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                var stationTypePositionGrid = $("#stationTypePositionGrid").data("kendoGrid"),
                    datas = stationTypePositionGrid.dataSource.data();
                for (var i = 0, details = []; i < datas.length; i++) {
                    var data = datas[i];
                    details.push({
                        "id": data.id || null,
                        "positionIndex": data.positionIndex,
                        "positionState": data.positionState.selectionValue ? data.positionState.selectionValue : data.positionState
                    });
                }
                masterService.update("packingStationType", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "packStationType": $scope.packStationType,
                    "ifScan": $scope.ifScan,
                    "ifPrint": $scope.ifPrint,
                    "ifScanInvoice": $scope.ifScanInvoice,
                    "isScanBoxType": $scope.isScanBoxType,
                    "ifWeight": $scope.ifWeight,
                    "description": $scope.description,
                    "positions": details
                }, function () {
                    $state.go("main.packing_station_type");
                });
            }
        };
    }).controller("packingStationTypeReadCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("packingStationType", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            grid.setOptions({"editable": false});
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
    });
})();