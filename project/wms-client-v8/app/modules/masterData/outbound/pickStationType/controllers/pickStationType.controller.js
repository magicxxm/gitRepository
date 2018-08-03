/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("pickStationTypeCtl", function ($scope, $rootScope, $translate, $window, commonService, masterService) {
        // ===================================================stationType====================================================
        $window.localStorage["currentItem"] = "pickStationType";
        $rootScope.pickStationTypeSource = ["PICK_TO_PACK", "PICK_TO_TOTE", "PICK_SINGLE","PICK_MULTIPLE"];
        $rootScope.isPrintFHDSource = ["是", "否"];
        $rootScope.isPrintZXDSource = ["是", "否"];
        $rootScope.isPrintQDSource = ["是","否"];

        $rootScope.pickPackWallTypeSource = masterService.getDataSource({
            key: "getPickPackWallType",
            text: "name",
            value: "id"
        });

        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.pickStationTypeRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "pickPackWallType",
                headerTemplate: "<span translate='PICK_PACK_WALL_TYPE'></span>",
                template: function (item) {
                    return item.pickPackWallType ? item.pickPackWallType.name : "";
                }
            },
            {
                width: 400, headerTemplate: "<span translate='PICK_STATION'></span>", template: function (item) {
                var stations = item.pickStations;
                for (var i = 0, datas = []; i < stations.length; i = i + 2) {
                    var station = stations[i], next = stations[i + 1];
                    var htmlStr = "<div style='margin:1px;'>";
                    station && (htmlStr += "<div class='gridCellList'>" + station.name + "</div>");
                    next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
                    htmlStr += "</div>";
                    datas.push(htmlStr);
                }
                return datas.join("");
            }
            },
            {
                width: 400, headerTemplate: "<span translate='Pick Pack Wall'></span>", template: function (item) {
                var walls = item.pickpackWalls;
                for (var i = 0, datas = []; i < walls.length; i = i + 2) {
                    var wall = walls[i], next = walls[i + 1];
                    var htmlStr = "<div style='margin:1px;'>";
                    wall && (htmlStr += "<div class='gridCellList'>" + wall.name + "</div>");
                    next && (htmlStr += "<div class='gridCellList' style='margin-left:5px;'>" + next.name + "</div>");
                    htmlStr += "</div>";
                    datas.push(htmlStr);
                }
                return datas.join("");
            }
            },
            {
                field: "isPrintFHD",
                headerTemplate: "<span>是否打印发货单</span>"
            },
            {
                field: "isPrintZXD",
                headerTemplate: "<span>是否打印装箱单</span>"
            },
            {
                field: "isPrintQD",
                headerTemplate: "<span>是否打印清单</span>"
            },
            {field: "description", headerTemplate: "<span translate='DESCRIPTION'></span>"}
        ];
        $scope.pickStationTypeGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("pickStationType")
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
            height: Math.max(300, $rootScope.mainHeight - 20 - 34 * 3 - 10 - 20 - 20 - 40),
            columns: stationTypePositionColumns
        });

    }).controller("pickStationTypeCreateCtl", function ($scope, $state, masterService) {
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
                masterService.create("pickStationType", {
                    "name": $scope.name,
                    "pickPackWallTypeId": $scope.pickPackWallType ? $scope.pickPackWallType.id : null,
                    "pickStationType": $scope.pickStationType,
                    "description": $scope.description,
                    "isPrintFHD": $scope.isPrintFHD,
                    "isPrintZXD": $scope.isPrintZXD,
                    "isPrintQD": $scope.isPrintQD,
                    "positions": details
                }, function () {
                    $state.go("main.pick_station_type");
                });
            }
        };
    }).controller("pickStationTypeUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("pickStationType", $stateParams.id, function (data) {
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
                        "positionState": data.positionState.selectionValue ? data.positionState.selectionValue : data.positionState
                    });
                }
                masterService.update("pickStationType", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "pickPackWallTypeId": $scope.pickPackWallType ? $scope.pickPackWallType.id : null,
                    "pickStationType": $scope.pickStationType,
                    "description": $scope.description,
                    "isPrintFHD": $scope.isPrintFHD,
                    "isPrintZXD": $scope.isPrintZXD,
                    "isPrintQD": $scope.isPrintQD,
                    "positions": details
                }, function () {
                    $state.go("main.pick_station_type");
                });
            }
        };
    }).controller("pickStationTypeReadCtl", function ($scope, $stateParams, masterService) {
        masterService.read("pickStationType", $stateParams.id, function (data) {
            for (var k in data) $scope[k] = data[k];
            var grid = $("#stationTypePositionGrid").data("kendoGrid");
            grid.setOptions({"editable": false});
            grid.setDataSource(new kendo.data.DataSource({data: data.positions}));
        });
    });
})();