/**
 * Created by frank.zhou on 2017/05/22.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("stocktakingStationTypeCtl", function ($scope, $rootScope, $window, $translate, $state, commonService, ICQABaseService) {
        // ===================================================stationType====================================================
        $window.localStorage["currentItem"] = "stocktakingStationType";
        $rootScope.stationTypeSource=["货位检查","货位暗灯","货位盘点"];

        var columns = [
            {field: "name", width: 300,template: "<a ui-sref='main.stocktakingStationTypeRead({id:dataItem.id})'>#: name # </a>", headerTemplate: "<span translate='NAME'></span>"},
            {width: 500, headerTemplate: "<span translate='盘点工作站'></span>", template: function (item) {
                var stockTakingStations = item.stockTakingStations;
                for (var i = 0, datas = []; i < stockTakingStations.length; i = i + 2) {
                    var station = stockTakingStations[i], next = stockTakingStations[i + 1];
                    var htmlStr = "<div style='margin:1px;'>";
                    station && (htmlStr += "<div class='gridCellList'>" + station.name + "</div>");
                    next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
                    htmlStr += "</div>";
                    datas.push(htmlStr);
                }
                return datas.join("");
            }
            },
            {field: "stationType", headerTemplate: "<span>工作站类型</span>"},
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.stocktakingStationTypeGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: ICQABaseService.getGridDataSource("stocktakingStationType")
        });

        // =================================================stationTypePosition===============================================
        // 函数
        function stateEditor(container, options) {
            var source = ICQABaseService.getDataSource({
                key: "getSelectionBySelectionKey", value: "selectionValue", text: "resourceKey",
                data: {selectionKey: "INVENTORY_STATE"}
            });
            ICQABaseService.selectEditor(container, options, source);
        }

        // stationTypePosition-column
        var stationTypePositionColumns = [
            {
                field: "positionIndex",
                editor: ICQABaseService.numberEditor,
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
        $rootScope.stationTypePositionGridOptions = ICQABaseService.editGrid({
            height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 2 - 10 - 20 - 20 - 40),
            columns: stationTypePositionColumns
        });

    }).controller("stocktakingStationTypeCreateCtl", function ($scope, $state, ICQABaseService) {
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
                ICQABaseService.create("stocktakingStationType", {
                    "name": $scope.name,
                    "description": $scope.description,
                    "positions": details
                }, function () {
                    $state.go("main.stocktaking_station_type");
                });
            }
        };
    }).controller("stocktakingStationTypeUpdateCtl", function ($scope, $state, $stateParams, ICQABaseService) {
        ICQABaseService.read("stocktakingStationType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
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
                        "positionState": data.positionState ? data.positionState.selectionValue : ""
                    });
                }
                ICQABaseService.update("stocktakingStationType", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "description": $scope.description,
                    "positions": details
                }, function () {
                    $state.go("main.stocktaking_station_type");
                });
            }
        };
    }).controller("stocktakingStationTypeReadCtl", function ($scope, $state, $stateParams, ICQABaseService) {
        ICQABaseService.read("stocktakingStationType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            grid.setOptions({"editable": false});
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
    });
})();