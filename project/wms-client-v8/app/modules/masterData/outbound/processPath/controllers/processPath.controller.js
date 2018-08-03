/**
 * Created by frank.zhou on 2017/05/08.
 */
(function () {
    'use strict';

    angular.module('myApp').controller("processPathCtl", function ($scope, $rootScope, $window, commonService, masterService) {

        $window.localStorage["currentItem"] = "processPath";

        $rootScope.collateTypeSource = ["AUTO", "MANUAL"];
        $rootScope.pickTypeSource = ["PICK_TOTOTE", "PICK_TOPACK"];
        $rootScope.pickWaySource = ["人工拣货", "机器拣货"];
        $rootScope.regenerateShortedPicksSource = ["Hot Pick", "Back To Process"];
        $rootScope.collateDocumentsSource = ["Batch Cover", "Face Label", "Shipment Detail"];
        $rootScope.allowedContainerTypesSource = ["Tote", "Cart", "Pallet"];
        $rootScope.processPathTypeSource = masterService.getDataSource({
            key: "getProcessPathType",
            text: "name",
            value: "id"
        });
        $rootScope.pickStationTypeSource = masterService.getDataSource({
            key: "getPickStationType",
            text: "name",
            value: "id"
        });
        $rootScope.rebinStationTypeSource = masterService.getDataSource({
            key: "getRebinStationType",
            text: "name",
            value: "id"
        });
        $rootScope.rebinWallTypeSource = masterService.getDataSource({
            key: "getRebinWallType",
            text: "name",
            value: "id"
        });
        $rootScope.packingStationTypeSource = masterService.getDataSource({
            key: "getPackingStationType",
            text: "name",
            value: "id"
        });

        var columns = [
            {
                field: "name",
                template: "<a ui-sref='main.processPathRead({id:dataItem.id})'>#: name # </a>",
                headerTemplate: "<span translate='NAME'></span>"
            },
            {
                field: "processPathType.name",
                template: "<a ui-sref='main.process_path_type'>#: processPathType.name # </a>",
                headerTemplate: "<span translate='PROCESS_PATH_TYPE'></span>"
            },
            {
                field: "pickDestination.name",
                template: "<a ui-sref='main.pick_station_type'>#: pickDestination.name # </a>",
                headerTemplate: "<span translate='PICK_DESTINATION'></span>"
            },
            {
                field: "rebinDestination.name",
                template: "<a ui-sref='main.rebin_station_type'>#: rebinDestination.name # </a>",
                headerTemplate: "<span translate='REBIN_DESTINATION'></span>"
            },
            {
                field: "rebinWallType.name",
                template: "<a ui-sref='main.rebin_wall_type'>#: rebinWallType.name # </a>",
                headerTemplate: "<span translate='REBIN_WALL_TYPE'></span>"
            },
            {
                field: "packDestination.name",
                template: "<a ui-sref='main.packing_station_type'>#: packDestination.name # </a>",
                headerTemplate: "<span translate='PACKING_STATION_TYPE'></span>"
            },
            {
                field: "pickWay",
                headerTemplate: "<span>拣货方式</span>"
            },
            {
                field: "targetPickRate",
                headerTemplate: "<span>目标捡货效率</span>"
            }

        ];
        $scope.processPathGridOptions = commonService.gridMushiny({
            columns: columns,
            dataSource: masterService.getGridDataSource("processPath")
        });

    }).controller("processPathCreateCtl", function ($scope, $state, masterService) {
        $scope.hotpick = "true";
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.create("processPath", {
                    "name": $scope.name,
                    "processPathTypeId": $scope.processPathType ? $scope.processPathType.id : null,
                    "regenerateShortedPicks": $scope.regenerateShortedPicks,
                    "minShipmentsPerBatch": $scope.minShipmentsPerBatch,
                    "maxShipmentsPerBatch": $scope.maxShipmentsPerBatch,
                    "minItemsPerBatch": $scope.minItemsPerBatch,
                    "maxItemsPerBatch": $scope.maxItemsPerBatch,
                    "collateDocuments": $scope.collateDocuments.join(","),
                    "pickDestinationId": $scope.pickDestination ? $scope.pickDestination.id : null,
                    "rebinDestinationId": $scope.rebinDestination ? $scope.rebinDestination.id : null,
                    "rebinWallTypeId": $scope.rebinWallType ? $scope.rebinWallType.id : null,
                    "packDestinationId": $scope.packDestination ? $scope.packDestination.id : null,
                    "targetPickRate": $scope.targetPickRate,
                    "processPad": $scope.processPad,
                    "batchLimit": $scope.batchLimit,
                    "pickWay": $scope.pickWay,
                    "toteLimit": $scope.toteLimit,
                    "description": $scope.description,
                    "hotpick": $scope.hotpick
                }, function () {
                    $state.go("main.process_path");
                });
            }
        };
    }).controller("processPathUpdateCtl", function ($scope, $state, $stateParams, masterService) {
        masterService.read("processPath", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = (k === "collateDocuments" ? data[k].split(",") : data[k]);
            }
        });
        $scope.validate = function (event) {
            event.preventDefault();
            if ($scope.validator.validate()) {
                masterService.update("processPath", {
                    "id": $scope.id,
                    "name": $scope.name,
                    "processPathTypeId": $scope.processPathType ? $scope.processPathType.id : null,
                    "regenerateShortedPicks": $scope.regenerateShortedPicks,
                    "minShipmentsPerBatch": $scope.minShipmentsPerBatch,
                    "maxShipmentsPerBatch": $scope.maxShipmentsPerBatch,
                    "minItemsPerBatch": $scope.minItemsPerBatch,
                    "maxItemsPerBatch": $scope.maxItemsPerBatch,
                    "collateDocuments": $scope.collateDocuments.join(","),
                    "pickDestinationId": $scope.pickDestination ? $scope.pickDestination.id : null,
                    "rebinDestinationId": $scope.rebinDestination ? $scope.rebinDestination.id : null,
                    "rebinWallTypeId": $scope.rebinWallType ? $scope.rebinWallType.id : null,
                    "packDestinationId": $scope.packDestination ? $scope.packDestination.id : null,
                    "targetPickRate": $scope.targetPickRate,
                    "processPad": $scope.processPad,
                    "batchLimit": $scope.batchLimit,
                    "toteLimit": $scope.toteLimit,
                    "pickWay": $scope.pickWay,
                    "description": $scope.description,
                    "hotpick": $scope.hotpick
                }, function () {
                    $state.go("main.process_path");
                });
            }
        };
    }).controller("processPathReadCtl", function ($scope, $stateParams, masterService) {
        masterService.read("processPath", $stateParams.id, function (data) {
            for (var k in data) {
                if (data[k] === true) data[k] = "true";
                else if (data[k] === false) data[k] = "false";
                $scope[k] = data[k];
            }
        });
    });
})();